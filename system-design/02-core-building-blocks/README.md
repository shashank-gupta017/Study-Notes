# Phase 2: Core Building Blocks

## Overview
Deep dive into the fundamental components that form any distributed system.

**Duration**: Week 2  
**Goal**: Understand each building block deeply enough to explain trade-offs

---

## Module 2.1: Load Balancers

### What is a Load Balancer?

Distributes incoming traffic across multiple servers to ensure no single server becomes overwhelmed.

```
                Internet
                   ↓
            ┌──────────────┐
            │Load Balancer │
            └──────────────┘
              ↙    ↓    ↘
         ┌────┐ ┌────┐ ┌────┐
         │ S1 │ │ S2 │ │ S3 │
         └────┘ └────┘ └────┘
```

### Load Balancing Algorithms

**1. Round Robin**
```
Request 1 → Server 1
Request 2 → Server 2
Request 3 → Server 3
Request 4 → Server 1 (cycle repeats)
```
- **Pros**: Simple, fair distribution
- **Cons**: Ignores server load, session affinity issues
- **Use**: Stateless apps, similar server capacity

**2. Least Connections**
```
Server 1: 5 connections  ←  (Route here)
Server 2: 10 connections
Server 3: 8 connections
```
- **Pros**: Accounts for current load
- **Cons**: Slightly more complex
- **Use**: Long-lived connections, varying request times

**3. Weighted Round Robin**
```
Server 1 (weight=3): Gets 60% traffic
Server 2 (weight=1): Gets 20% traffic
Server 3 (weight=1): Gets 20% traffic
```
- **Pros**: Handles different server capacities
- **Cons**: Need to configure weights
- **Use**: Mixed hardware, gradual rollouts

**4. IP Hash**
```
hash(client_ip) % num_servers
123.45.67.89 → Server 2 (always)
```
- **Pros**: Session persistence
- **Cons**: Uneven distribution, server changes break sessions
- **Use**: Stateful apps, shopping carts

**5. Least Response Time**
- Routes to server with fastest response
- **Use**: Varying backend performance

### Layer 4 vs Layer 7 Load Balancing

**Layer 4 (Transport Layer)**
```
Operates on: IP, TCP, UDP
Decisions based on: IP addresses, ports
Routing: Fast, no content inspection
Example: AWS Network Load Balancer
```

**Layer 7 (Application Layer)**
```
Operates on: HTTP, HTTPS, WebSocket
Decisions based on: URL, headers, cookies
Routing: Content-aware, SSL termination
Example: AWS Application Load Balancer, Nginx
```

### Health Checks

```python
# Active Health Check
def check_health(server):
    try:
        response = http.get(f"{server}/health", timeout=2)
        return response.status == 200
    except:
        return False

# Passive Health Check
def monitor_responses(server):
    if server.error_rate > 50% in last_10_requests:
        mark_unhealthy(server)
```

### Real-World Example

**Netflix Load Balancing**
- Uses AWS ELB + Zuul (application gateway)
- Routes based on: geography, device type, A/B tests
- Dynamic routing during failures

---

## Module 2.2: Caching

### The Cache Hierarchy

```
         Speed    Size     Cost
L1      ████     ░         ████
L2      ███      ░░        ███
RAM     ██       ░░░       ██
SSD     █        ░░░░      █
HDD     ░        ░░░░░     ░
Network ░        ████      ░
```

### Where to Cache?

**1. Client-Side Cache**
```
Browser → [Local Storage/Memory] → Server
```
- Example: Browser cache, mobile app cache
- TTL: Hours to days

**2. CDN Cache**
```
User → [CDN Edge] → Origin Server
```
- Example: Cloudflare, Akamai, CloudFront
- TTL: Minutes to hours
- Use: Static assets (images, CSS, JS)

**3. Application Cache**
```
App Server → [Redis/Memcached] → Database
```
- Example: Session data, API responses
- TTL: Seconds to minutes

**4. Database Cache**
```
Query → [Query Result Cache] → Disk
```
- Example: MySQL query cache
- Automatic in many databases

### Caching Strategies

**1. Cache-Aside (Lazy Loading)**
```python
def get_user(user_id):
    # 1. Check cache first
    user = cache.get(f"user:{user_id}")
    
    if user is None:
        # 2. Cache miss - fetch from DB
        user = db.query(f"SELECT * FROM users WHERE id={user_id}")
        
        # 3. Populate cache
        cache.set(f"user:{user_id}", user, ttl=3600)
    
    return user
```
- **Pros**: Only cache what's needed, resilient to cache failures
- **Cons**: Cache miss penalty, stale data possible

**2. Write-Through Cache**
```python
def update_user(user_id, data):
    # 1. Write to database
    db.update(user_id, data)
    
    # 2. Write to cache
    cache.set(f"user:{user_id}", data, ttl=3600)
```
- **Pros**: Cache always fresh
- **Cons**: Slower writes, cache may store unused data

**3. Write-Back (Write-Behind)**
```python
def update_user(user_id, data):
    # 1. Write to cache immediately
    cache.set(f"user:{user_id}", data)
    
    # 2. Asynchronously write to DB
    queue.enqueue('db_write', user_id, data)
```
- **Pros**: Fast writes, reduced DB load
- **Cons**: Data loss risk, complex

**4. Refresh-Ahead**
```python
def schedule_refresh(key):
    if time_to_expiry(key) < threshold:
        # Refresh before expiry
        asynch_refresh(key)
```
- **Pros**: No cache miss latency
- **Cons**: Complexity, may refresh unused data

### Cache Eviction Policies

**LRU (Least Recently Used)**
```
Cache: [A, B, C, D]  (max size = 4)
Access: E
Result: [B, C, D, E]  (A evicted)
```

**LFU (Least Frequently Used)**
```
Counts: A=5, B=2, C=8, D=1
Access: E
Result: [A, C, E, B]  (D evicted, lowest frequency)
```

**TTL (Time To Live)**
```
A: expires in 10s
B: expires in 60s
After 10s: A automatically removed
```

**FIFO (First In First Out)**
```
Like a queue, oldest entry removed first
```

### Cache Invalidation

> "There are only two hard things in Computer Science: cache invalidation and naming things." - Phil Karlton

**1. TTL-based**
```python
cache.set("user:123", data, ttl=300)  # Auto-expire in 5 min
```

**2. Event-based**
```python
def on_user_update(user_id):
    cache.delete(f"user:{user_id}")
    # Or: cache.set with new data
```

**3. Cache Stampede Prevention**
```python
def get_with_lock(key):
    value = cache.get(key)
    if value is None:
        # Only one process fetches
        with lock(f"lock:{key}"):
            value = cache.get(key)  # Double-check
            if value is None:
                value = expensive_query()
                cache.set(key, value)
    return value
```

### Distributed Caching

**Consistent Hashing**
```
         Cache Ring
           ___
        /       \
    N1 ●         ● N2
       |         |
    N4 ●         ● N3
        \_____ /

Key "user:123" → hash → lands between N2 and N3 → Store in N3
```

**Benefits**:
- Adding/removing nodes: minimal rehashing
- Even distribution of keys

---

## Module 2.3: Databases

### SQL vs NoSQL Decision Tree

```
                Start
                  |
         Need ACID guarantees?
              /      \
            Yes       No
             |         |
        Complex       |
        queries?      |
         /  \         |
       Yes  No     Flexible
        |    |     schema?
       SQL  SQL      |
            or     /   \
           NoSQL  Yes   No
                  |     |
              NoSQL   SQL
```

### SQL Databases

**When to Use**:
- Structured data with relations
- Need ACID transactions
- Complex queries with joins
- Data integrity critical (banking, orders)

**Examples**: PostgreSQL, MySQL, Oracle

**Scaling Patterns**:

**1. Vertical Scaling**
```
Before:         After:
16GB RAM   →   64GB RAM
4 cores         16 cores
```

**2. Read Replicas**
```
       Writes
         ↓
    ┌────────┐
    │Primary │
    └────────┘
       ↓  ↓  ↓
    ┌──┐┌──┐┌──┐  ← Reads distributed
    │R1││R2││R3│
    └──┘└──┘└──┘
```

**3. Sharding (Horizontal Partitioning)**
```
Shard by user_id:

Shard 1: user_id 0-9999
Shard 2: user_id 10000-19999
Shard 3: user_id 20000-29999

hash(user_id) % num_shards → determines shard
```

### NoSQL Databases

**Key-Value Stores**
```
Examples: Redis, DynamoDB
Use: Session storage, caching
Schema: key → value

cart:user123 → {"items": [1,2,3]}
```

**Document Stores**
```
Examples: MongoDB, Couchbase
Use: User profiles, product catalogs
Schema: Flexible JSON documents

{
  "_id": "123",
  "name": "John",
  "address": { "city": "NYC" },
  "orders": [...]
}
```

**Column-Family Stores**
```
Examples: Cassandra, HBase
Use: Time-series, analytics
Schema: Row key → Column families

user123 → profile:name="John"
          profile:age=30
          activity:last_login=123456
```

**Graph Databases**
```
Examples: Neo4j, Amazon Neptune
Use: Social networks, recommendations
Schema: Nodes and edges

User --FOLLOWS--> User
User --LIKES--> Product
```

### Database Indexing

**Without Index**:
```sql
SELECT * FROM users WHERE email = 'john@example.com';
-- Scans all 1M rows → O(n)
```

**With Index**:
```sql
CREATE INDEX idx_email ON users(email);
-- B-tree lookup → O(log n)
```

**Trade-offs**:
- **Pros**: Faster reads (10-1000x)
- **Cons**: Slower writes, more storage

**Types of Indexes**:
1. **Primary Index**: On primary key (unique, clustered)
2. **Secondary Index**: On other columns
3. **Composite Index**: On multiple columns
4. **Full-text Index**: For text search

---

## Module 2.4: Message Queues

### Why Message Queues?

**Without Queue**:
```
API → [Process Order] → [Send Email] → [Update Inventory]
         ↓ slow          ↓ slow          ↓ slow
      User waits...
```

**With Queue**:
```
API → [Queue: process_order] → 200 OK (instant)
           ↓
      [Background Workers] → Process async
```

### Popular Message Queues

| Feature | RabbitMQ | Kafka | AWS SQS |
|---------|----------|-------|---------|
| Type | Traditional MQ | Event streaming | Cloud MQ |
| Ordering | Yes | Yes (per partition) | FIFO queues |
| Persistence | Optional | Yes | Yes |
| Throughput | 20K msg/s | 1M+ msg/s | Unlimited |
| Use Case | Task queues | Event logs | Decoupling |

### Queue Patterns

**1. Work Queue (Task Queue)**
```
Producer → [Queue] → Worker 1
                  → Worker 2
                  → Worker 3

Each message processed by ONE worker
```

**2. Publish-Subscribe (Fanout)**
```
Publisher → [Exchange] → Queue 1 → Consumer 1
                      → Queue 2 → Consumer 2
                      → Queue 3 → Consumer 3

Each message sent to ALL consumers
```

**3. Topic-based Routing**
```
Publisher → [Topic: orders.*]
              ↓
         orders.created  → Consumer 1
         orders.updated  → Consumer 2
         orders.*        → Consumer 3 (all)
```

### Reliability Patterns

**1. Acknowledgments**
```python
def process_message(msg):
    try:
        # Do work
        handle_order(msg)
        
        # Acknowledge success
        msg.ack()
    except Exception as e:
        # Reject and requeue
        msg.nack(requeue=True)
```

**2. Dead Letter Queue**
```
Main Queue
    ↓ (retry 3 times)
Failed?
    ↓
Dead Letter Queue → Manual inspection
```

**3. Idempotency**
```python
def process_order(order_id):
    # Check if already processed
    if db.exists(f"processed:{order_id}"):
        return  # Skip duplicate
    
    # Process
    create_order(order_id)
    
    # Mark as processed
    db.set(f"processed:{order_id}", True)
```

---

## Practical Exercises

### Exercise 1: Design a Caching Strategy
Design caching for an e-commerce site:
- Product catalog (millions of products)
- User sessions
- Shopping cart
- Popular searches

Questions:
- Where to cache each?
- What TTL?
- Eviction policy?
- Invalidation strategy?

### Exercise 2: Choose Database
Pick SQL or NoSQL for:
1. Banking transactions
2. User activity logs
3. Social media posts
4. Product inventory
5. Time-series metrics

Justify each choice.

### Exercise 3: Load Balancer Config
Design load balancing for:
- Stateless API servers
- WebSocket connections
- Database read replicas

Which algorithm for each? Why?

---

## Key Takeaways

1. **Load Balancers**: Distribute traffic, enable scaling
2. **Caching**: Speed vs freshness trade-off
3. **Databases**: SQL for structure, NoSQL for scale
4. **Queues**: Async processing, decoupling

---

## Next Module
[03-data-fundamentals](../03-data-fundamentals/README.md)
