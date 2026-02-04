# Phase 2: Scaling Patterns

## Overview

Master the techniques and patterns used to scale systems from thousands to billions of users.

**Duration**: Week 3 (Days 15-21)  
**Prerequisites**: Fundamentals, Core Building Blocks, Data Fundamentals  
**Goal**: Understand how to scale every component of a system

---

## Module 4.1: Database Scaling

### Vertical Scaling (Scale Up)

**What**: Add more resources to a single machine

```
Before:          After:
16 GB RAM   →   128 GB RAM
4 cores         16 cores
1 TB SSD        4 TB SSD
```

**Pros**:
- ✓ Simple - no code changes
- ✓ Maintains consistency (single DB)
- ✓ No network latency
- ✓ ACID transactions work normally

**Cons**:
- ✗ Hardware limits (~96 cores, 1-2 TB RAM)
- ✗ Expensive (non-linear cost increase)
- ✗ Single point of failure
- ✗ Downtime for upgrades

**When to use**:
- Starting out (< 1000 QPS)
- Monolithic applications
- Strong consistency required
- Small to medium datasets (< 1 TB)

---

### Read Replicas (Horizontal Scaling for Reads)

```
            Writes
              ↓
        ┌──────────┐
        │ Primary  │ (Master)
        └─────┬────┘
              │ Replication
      ┌───────┼───────┐
      ↓       ↓       ↓
   ┌─────┐ ┌─────┐ ┌─────┐
   │Rep 1│ │Rep 2│ │Rep 3│ ← Reads distributed
   └─────┘ └─────┘ └─────┘
```

**Replication Methods**:

**1. Asynchronous Replication**
```
Primary: Write → Success (immediate)
          ↓
Replicas: Write (async, eventual)

Pros: Fast writes
Cons: Replicas may lag, stale reads possible
Use: Most applications (social media, blogs)
```

**2. Synchronous Replication**
```
Primary: Write → Wait for replica ACK → Success
          ↓
Replica 1: Write → ACK

Pros: No stale reads
Cons: Slower writes, availability issues
Use: Financial systems, critical data
```

**3. Semi-Synchronous**
```
Primary: Write → Wait for 1 replica → Success
          ↓
Replica 1: Write → ACK (sync)
Replica 2: Write (async)

Pros: Balance speed and consistency
Use: Production systems (MySQL default)
```

---

**Handling Replication Lag**

```python
# Problem: User writes, then reads from stale replica
def post_update(user_id, data):
    primary.write(user_id, data)  # Write to primary
    return success()

def get_profile(user_id):
    return replica.read(user_id)  # Might not have latest write!

# Solution 1: Read from primary after write
def get_profile_after_write(user_id):
    if recently_wrote(user_id):  # Within last 5 seconds
        return primary.read(user_id)
    else:
        return replica.read(user_id)

# Solution 2: Sticky sessions (always read from same replica)
def get_profile_sticky(user_id):
    replica_id = hash(user_id) % num_replicas
    return replicas[replica_id].read(user_id)

# Solution 3: Wait for replication
def post_update_with_wait(user_id, data):
    primary.write(user_id, data)
    wait_for_replication(user_id, max_wait=1000ms)
    return success()
```

---

### Database Sharding (Horizontal Partitioning)

**What**: Split data across multiple databases

```
All Data:
user_1, user_2, ..., user_1000000

Sharded (4 shards):
Shard 1: user_1 to user_250000
Shard 2: user_250001 to user_500000
Shard 3: user_500001 to user_750000
Shard 4: user_750001 to user_1000000
```

**Sharding Strategies** (covered in detail in distributed systems):
1. Hash-based: `shard = hash(user_id) % num_shards`
2. Range-based: `shard = user_id / 250000`
3. Geo-based: `shard = user.country`
4. Consistent hashing: Minimizes data movement

---

**Challenges and Solutions**:

**1. Cross-Shard Queries**
```sql
-- Get friends' posts across multiple shards
SELECT * FROM posts 
WHERE user_id IN (123, 456, 789)  -- Users on different shards
ORDER BY created_at DESC
LIMIT 10;

Solution:
1. Query each shard in parallel
2. Merge results
3. Sort and limit
```

```python
def get_feed_cross_shard(user_ids):
    # Parallel queries
    futures = []
    for shard in shards:
        future = shard.async_query(
            "SELECT * FROM posts WHERE user_id IN ? ORDER BY created_at DESC LIMIT 100",
            user_ids
        )
        futures.append(future)
    
    # Merge and sort
    all_results = []
    for future in futures:
        all_results.extend(future.get())
    
    all_results.sort(key=lambda x: x.created_at, reverse=True)
    return all_results[:10]
```

**2. Distributed Transactions**
```python
# Problem: Transfer money between accounts on different shards
def transfer_cross_shard(from_user, to_user, amount):
    # from_user on Shard 1
    # to_user on Shard 2
    
    # Two-Phase Commit (2PC)
    # Phase 1: Prepare
    shard1_ready = shard1.prepare_deduct(from_user, amount)
    shard2_ready = shard2.prepare_add(to_user, amount)
    
    if shard1_ready and shard2_ready:
        # Phase 2: Commit
        shard1.commit()
        shard2.commit()
    else:
        # Rollback both
        shard1.rollback()
        shard2.rollback()
```

**Better Approach**: Avoid cross-shard transactions
```python
# Design to keep related data together
# Shard by: (from_user, to_user) pair
# Or: Use eventual consistency with compensating actions
```

**3. Auto-Incrementing IDs**
```
Problem: Each shard has its own ID sequence
Shard 1: user_id 1, 2, 3, ...
Shard 2: user_id 1, 2, 3, ... (collision!)

Solution: Distributed ID generation
- Snowflake IDs (timestamp + shard_id + sequence)
- UUID
- Centralized ID service (e.g., Twitter Snowflake)
```

---

## Module 4.2: Stateless Application Servers

### Why Stateless?

**Stateful Problems**:
```
User → Load Balancer → Server A (has session)
User → Load Balancer → Server B (no session, user logged out!)
```

**Stateless Benefits**:
- Easy horizontal scaling (add/remove servers)
- No session affinity needed
- Servers are interchangeable
- Graceful degradation (if server fails, route to another)

---

### Moving State Out

**Before (Stateful)**:
```python
# Session stored in server memory
sessions = {}  # In-memory dictionary

def login(user_id):
    session_id = generate_session_id()
    sessions[session_id] = user_id  # Stored locally
    return session_id

def get_user(session_id):
    return sessions.get(session_id)  # Only works on same server
```

**After (Stateless)**:
```python
# Session stored in Redis (shared)
redis = Redis()

def login(user_id):
    session_id = generate_session_id()
    redis.set(f"session:{session_id}", user_id, ttl=3600)
    return session_id

def get_user(session_id):
    return redis.get(f"session:{session_id}")  # Works from any server
```

---

### Session Management Patterns

**1. Client-Side Sessions (JWT)**
```python
# Encode user data in token
token = jwt.encode({
    'user_id': 123,
    'exp': now() + 1_hour
}, secret_key)

# Client includes token in every request
# Server validates token (no database lookup)
user_id = jwt.decode(token, secret_key)['user_id']

Pros:
+ No server storage
+ Truly stateless
+ Scales infinitely

Cons:
- Can't revoke before expiry
- Token size limit
- Sensitive data in client
```

**2. Server-Side Sessions (Redis)**
```python
# Store session in fast shared storage
session_id = "sess_abc123"
redis.hset(f"session:{session_id}", {
    'user_id': 123,
    'login_time': now(),
    'preferences': {...}
})
redis.expire(f"session:{session_id}", 3600)

Pros:
+ Can revoke anytime
+ Store sensitive data
+ Smaller cookie size

Cons:
- Requires Redis lookup
- Redis is shared dependency
```

**3. Hybrid Approach**
```python
# Public data in JWT, sensitive data in Redis
jwt_token = {
    'user_id': 123,
    'name': 'John',
    'session_id': 'sess_abc123'  # Reference to Redis
}

redis.hset('session:sess_abc123', {
    'permissions': ['admin', 'write'],
    'temp_data': {...}
})
```

---

## Module 4.3: Caching at Scale

### Multi-Level Caching

```
User Request
    ↓
1. Client Cache (Browser)
    ↓ (miss)
2. CDN Cache (Edge)
    ↓ (miss)
3. Application Cache (Redis)
    ↓ (miss)
4. Database Query Cache
    ↓ (miss)
5. Database
```

**Cache at Each Level**:

**Browser Cache**:
```http
Cache-Control: public, max-age=3600
# Cache static assets for 1 hour
```

**CDN Cache**:
```
Cache static content (images, CSS, JS)
TTL: Hours to days
Purge: On deployment
```

**Application Cache (Redis)**:
```python
# Cache expensive computations
def get_user_feed(user_id):
    cache_key = f"feed:{user_id}"
    
    # Check cache
    feed = redis.get(cache_key)
    if feed:
        return feed
    
    # Generate feed (expensive)
    feed = generate_feed(user_id)
    
    # Cache for 5 minutes
    redis.setex(cache_key, 300, feed)
    return feed
```

---

### Cache Stampede Prevention

**Problem**: Cache expires, many requests hit database simultaneously

```
Time 0: Cache expires
Time 1: Request 1, 2, 3, ..., 100 all hit DB
Result: DB overloaded!
```

**Solution 1: Lock-based**
```python
def get_with_lock(key):
    value = cache.get(key)
    if value:
        return value
    
    # Only one request acquires lock
    lock_key = f"lock:{key}"
    if cache.set(lock_key, 1, nx=True, ex=10):
        try:
            # Compute value
            value = expensive_computation()
            cache.set(key, value, ex=300)
            return value
        finally:
            cache.delete(lock_key)
    else:
        # Other requests wait and retry
        time.sleep(0.1)
        return get_with_lock(key)  # Retry
```

**Solution 2: Probabilistic Early Expiration**
```python
import random

def get_with_early_expiration(key, ttl=300):
    value, expiry = cache.get_with_ttl(key)
    
    if value:
        # Refresh if close to expiry (with probability)
        time_left = expiry - now()
        if time_left < ttl * 0.2 and random.random() < 0.1:
            # 10% chance to refresh early
            value = expensive_computation()
            cache.set(key, value, ex=ttl)
        return value
    else:
        # Cache miss
        value = expensive_computation()
        cache.set(key, value, ex=ttl)
        return value
```

---

### Cache Warming

**Proactive cache population**:

```python
# On deployment or schedule
def warm_cache():
    popular_users = get_top_1000_users()
    
    for user in popular_users:
        # Pre-compute and cache
        feed = generate_feed(user.id)
        cache.set(f"feed:{user.id}", feed, ex=3600)
        
        profile = get_profile(user.id)
        cache.set(f"profile:{user.id}", profile, ex=3600)

# Run during low-traffic hours
schedule.every().day.at("03:00").do(warm_cache)
```

---

## Module 4.4: Asynchronous Processing

### Why Async?

**Synchronous (Slow)**:
```python
def handle_signup(user):
    create_account(user)         # 100ms
    send_welcome_email(user)     # 500ms ← Blocks user
    notify_team(user)            # 200ms ← Blocks user
    update_analytics(user)       # 300ms ← Blocks user
    
    return success()
    # Total: 1100ms user waits
```

**Asynchronous (Fast)**:
```python
def handle_signup(user):
    create_account(user)         # 100ms
    
    # Queue background tasks
    queue.enqueue('send_email', user)
    queue.enqueue('notify_team', user)
    queue.enqueue('update_analytics', user)
    
    return success()
    # Total: 100ms user waits, rest happens in background
```

---

### Task Queue Patterns

**1. Simple Queue**
```python
# Producer
queue.enqueue('send_email', {
    'to': 'user@example.com',
    'subject': 'Welcome!',
    'body': '...'
})

# Consumer (worker)
while True:
    task = queue.dequeue()
    if task:
        send_email(**task.data)
```

**2. Priority Queue**
```python
# High priority tasks first
queue.enqueue('send_password_reset', priority=10)
queue.enqueue('send_newsletter', priority=1)

# Worker processes high priority first
task = queue.dequeue_highest_priority()
```

**3. Delayed Queue**
```python
# Schedule task for future
queue.enqueue('send_reminder', delay=3600)  # Send in 1 hour

# Or at specific time
queue.enqueue_at('send_report', at='2024-01-01 09:00')
```

**4. Retry with Backoff**
```python
def process_with_retry(task):
    max_retries = 3
    backoff = [1, 5, 15]  # seconds
    
    for attempt in range(max_retries):
        try:
            process(task)
            return success()
        except TemporaryError:
            if attempt < max_retries - 1:
                time.sleep(backoff[attempt])
            else:
                # Move to dead letter queue
                dead_letter_queue.enqueue(task)
```

---

## Module 4.5: Content Delivery Network (CDN)

### How CDN Works

```
User in Tokyo → CDN Edge (Tokyo) → Origin Server (US)
                    ↓
                  Cache
                    ↓
User in Tokyo → CDN Edge (Tokyo) [cached] ✓
```

**First Request**:
1. User requests image from CDN
2. CDN checks local cache (miss)
3. CDN fetches from origin
4. CDN caches and returns to user

**Subsequent Requests**:
1. User requests same image
2. CDN returns from cache (no origin request)

---

### CDN Strategies

**Push CDN**:
```python
# You upload content to CDN
cdn.upload('image.jpg', '/cdn/images/image.jpg')
cdn.set_ttl('/cdn/images/image.jpg', ttl=86400)

Use: Rarely changing content
Examples: Product images, static assets
```

**Pull CDN**:
```python
# CDN fetches on first request
# You just configure origin
cdn.configure(origin='https://mysite.com')

# User requests: https://cdn.example.com/images/photo.jpg
# CDN fetches: https://mysite.com/images/photo.jpg (first time)
# CDN caches for subsequent requests

Use: Frequently updated content
Examples: User uploads, dynamic images
```

---

### CDN Optimization

**1. Cache-Control Headers**
```python
# Static assets (long TTL)
@app.route('/static/<path:filename>')
def static_file(filename):
    response = send_file(filename)
    response.headers['Cache-Control'] = 'public, max-age=31536000'  # 1 year
    return response

# Dynamic content (short TTL)
@app.route('/api/feed')
def feed():
    data = get_feed()
    response = jsonify(data)
    response.headers['Cache-Control'] = 'public, max-age=60'  # 1 minute
    return response
```

**2. Versioned URLs**
```html
<!-- Before: Can't update cached file -->
<script src="/app.js"></script>

<!-- After: New version = new URL -->
<script src="/app.v123.js"></script>
<script src="/app.js?v=123"></script>
```

**3. Purge/Invalidate**
```python
# On deployment, purge CDN cache
def deploy():
    build_assets()
    cdn.purge('/static/*')  # Clear all static files
    deploy_to_servers()
```

---

## Practical Exercises

### Exercise 1: Shard Key Selection

Design sharding strategy for:
1. **Social network**: Users, posts, follows
2. **E-commerce**: Products, orders, reviews
3. **Chat app**: Users, messages, channels

For each:
- Choose shard key
- Justify choice
- List potential issues
- How to handle hotspots?

---

### Exercise 2: Cache Strategy

Design caching for Netflix:
- Video catalog (millions of videos)
- User watch history
- Personalized recommendations
- Video thumbnails

For each:
- Where to cache (CDN, Redis, etc.)
- TTL values
- Invalidation strategy

---

### Exercise 3: Async Processing

Design task queue for e-commerce:
- Order confirmation email
- Payment processing
- Inventory update
- Shipping notification
- Fraud detection

For each task:
- Priority level
- Retry strategy
- Timeout
- Failure handling

---

## Key Takeaways

1. **Database**: Read replicas → Sharding → Multi-region
2. **App Servers**: Stateless design enables horizontal scaling
3. **Caching**: Multiple levels, prevent stampede, warm cache
4. **Async**: Queue non-critical tasks, user doesn't wait
5. **CDN**: Distribute static content globally, reduce origin load

---

## Self-Assessment

- [ ] Can design read replica architecture
- [ ] Understand sharding trade-offs
- [ ] Can make application servers stateless
- [ ] Can design multi-level caching
- [ ] Understand async processing patterns
- [ ] Can configure CDN effectively

---

## Next Module

[05-communication-patterns](../05-communication-patterns/README.md) - How services communicate at scale

---

**Remember**: Scale horizontally when possible. Vertical scaling is a temporary solution.
