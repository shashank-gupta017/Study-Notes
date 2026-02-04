# Classic System Design: URL Shortener

## Problem Statement

Design a URL shortening service like bit.ly or TinyURL.

**Functional Requirements**:
1. Given a long URL, generate a short URL
2. Given a short URL, redirect to original URL
3. Custom short URLs (optional)
4. Expiration time (optional)
5. Analytics (click tracking)

**Non-Functional Requirements**:
- High availability (99.9%+)
- Low latency (< 100ms for redirects)
- Scalable (billions of URLs)
- Durable (URLs shouldn't be lost)

---

## Step 1: Requirements Clarification

### Questions to Ask

**Q**: How many URLs shortened per month?  
**A**: 100 million new URLs/month

**Q**: How long should shortened URLs be?  
**A**: As short as possible (6-8 characters)

**Q**: What characters can be used?  
**A**: [a-z, A-Z, 0-9] = 62 characters

**Q**: Can users delete or update URLs?  
**A**: No (keep it simple initially)

**Q**: How long to store URLs?  
**A**: 10 years

**Q**: Read:write ratio?  
**A**: 100:1 (many more reads than writes)

---

## Step 2: Capacity Estimation

### Traffic Estimates

```
New URLs per month: 100M
New URLs per second: 100M / (30 × 24 × 3600) ≈ 40 URLs/sec

Redirects per month: 100M × 100 = 10B (100:1 read:write ratio)
Redirects per second: 10B / (30 × 24 × 3600) ≈ 4,000 redirects/sec
```

### Storage Estimates

```
Storage per URL:
- Original URL: 500 bytes (average)
- Short code: 7 bytes
- Created timestamp: 8 bytes
- Expiry timestamp: 8 bytes
- User ID: 8 bytes
Total: ~530 bytes ≈ 0.5 KB

URLs for 10 years: 100M/month × 12 × 10 = 12 billion URLs
Storage needed: 12B × 0.5 KB = 6 TB

With replication (3x): 18 TB
```

### Bandwidth Estimates

```
Write bandwidth: 40 URLs/sec × 0.5 KB = 20 KB/sec
Read bandwidth: 4,000 redirects/sec × 0.5 KB = 2 MB/sec
```

### Cache Estimates (80-20 Rule)

```
Daily requests: 4,000/sec × 86,400 = 346M redirects/day
Cache 20% of URLs: 346M × 0.2 = 69M URLs
Cache size: 69M × 0.5 KB ≈ 35 GB

With some buffer: ~50 GB cache
```

---

## Step 3: API Design

### REST APIs

**1. Shorten URL**
```http
POST /api/v1/shorten
Content-Type: application/json

Request:
{
  "longUrl": "https://www.example.com/very/long/url",
  "customAlias": "mylink",     // optional
  "expiryDate": "2027-01-01"   // optional
}

Response:
{
  "shortUrl": "https://short.ly/abc123",
  "longUrl": "https://www.example.com/very/long/url",
  "createdAt": "2026-02-04T00:00:00Z"
}
```

**2. Redirect**
```http
GET /{shortCode}

Response: 302 Redirect
Location: https://www.example.com/very/long/url
```

**3. Get Analytics (Optional)**
```http
GET /api/v1/analytics/{shortCode}

Response:
{
  "shortUrl": "https://short.ly/abc123",
  "totalClicks": 1523,
  "uniqueClicks": 982,
  "clicksByDate": [...],
  "clicksByCountry": [...]
}
```

---

## Step 4: Database Schema

### SQL Schema

```sql
CREATE TABLE urls (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_code VARCHAR(10) UNIQUE NOT NULL,
    long_url VARCHAR(2048) NOT NULL,
    user_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    INDEX idx_short_code (short_code),
    INDEX idx_user_id (user_id)
);

CREATE TABLE analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_code VARCHAR(10) NOT NULL,
    clicked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    country VARCHAR(2),
    INDEX idx_short_code (short_code)
);
```

### NoSQL Schema (DynamoDB)

```json
{
  "TableName": "UrlMappings",
  "KeySchema": [
    { "AttributeName": "short_code", "KeyType": "HASH" }
  ],
  "Attributes": {
    "short_code": "abc123",
    "long_url": "https://www.example.com/very/long/url",
    "user_id": "user_456",
    "created_at": 1704326400,
    "expires_at": 1735862400
  }
}
```

---

## Step 5: Short Code Generation

### Option 1: Base62 Encoding

**Approach**: Encode auto-incrementing ID to base62

```python
def id_to_base62(id):
    chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    base62 = []
    
    while id > 0:
        base62.append(chars[id % 62])
        id //= 62
    
    return ''.join(reversed(base62))

# Example
id_to_base62(12345678)  # Returns: "1LY7VK"
```

**Calculation**:
```
6 characters: 62^6 = 56 billion unique URLs
7 characters: 62^7 = 3.5 trillion unique URLs
8 characters: 62^8 = 218 trillion unique URLs
```

**Pros**:
- Predictable length
- No collisions
- Simple implementation

**Cons**:
- Predictable sequence (security concern)
- Requires centralized ID generator

---

### Option 2: Random Generation + Collision Check

```python
import random
import string

def generate_short_code(length=6):
    chars = string.ascii_letters + string.digits
    
    while True:
        code = ''.join(random.choices(chars, k=length))
        
        # Check if code already exists
        if not db.exists(code):
            return code
```

**Pros**:
- Unpredictable
- Distributed generation (no coordination)

**Cons**:
- Potential collisions (rare with 6+ chars)
- Variable time (retry on collision)

---

### Option 3: Hash + Truncate

```python
import hashlib

def generate_short_code(long_url):
    # Hash the URL
    hash_obj = hashlib.md5(long_url.encode())
    hash_hex = hash_obj.hexdigest()
    
    # Take first 6 characters
    code = hash_hex[:6]
    
    # Handle collision
    if db.exists(code):
        # Try next 6 characters or append counter
        code = hash_hex[6:12]
    
    return code
```

**Pros**:
- Same URL always generates same code (idempotent)
- Distributed generation

**Cons**:
- Higher collision rate
- Collision handling complexity

---

## Step 6: High-Level Architecture

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       ↓
┌─────────────────────────────────┐
│   Load Balancer (DNS/CDN)       │
└─────────────────────────────────┘
       │
       ├─────────────────┬────────────────┐
       ↓                 ↓                ↓
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ API Server 1 │  │ API Server 2 │  │ API Server N │
└──────┬───────┘  └──────┬───────┘  └──────┬───────┘
       │                 │                 │
       └─────────────────┼─────────────────┘
                         │
       ┌─────────────────┼─────────────────┐
       ↓                 ↓                 ↓
┌─────────────┐   ┌──────────────┐  ┌──────────────┐
│    Cache    │   │   Database   │  │   Analytics  │
│   (Redis)   │   │  (Primary +  │  │    Queue     │
│             │   │   Replicas)  │  │   (Kafka)    │
└─────────────┘   └──────────────┘  └──────────────┘
```

---

## Step 7: Detailed Component Design

### Write Flow (Shorten URL)

```
1. Client sends POST /api/v1/shorten with long URL

2. API Server:
   a. Validate URL format
   b. Check if URL already shortened (optional deduplication)
   c. Generate short code (using chosen strategy)
   d. Store mapping in database
   e. Return short URL to client

3. Async: Replicate to read replicas
```

```python
def shorten_url(long_url, user_id=None):
    # 1. Validate
    if not is_valid_url(long_url):
        raise InvalidURLError()
    
    # 2. Check cache/DB for existing mapping (optional)
    existing = cache.get(f"long:{long_url}")
    if existing:
        return existing
    
    # 3. Generate short code
    short_code = generate_short_code()
    
    # 4. Store in database
    db.insert({
        'short_code': short_code,
        'long_url': long_url,
        'user_id': user_id,
        'created_at': now()
    })
    
    # 5. Cache the mapping
    cache.set(f"short:{short_code}", long_url, ttl=86400)
    cache.set(f"long:{long_url}", short_code, ttl=86400)
    
    return f"https://short.ly/{short_code}"
```

---

### Read Flow (Redirect)

```
1. Client accesses https://short.ly/abc123

2. API Server:
   a. Extract short code: "abc123"
   b. Check cache for mapping
   c. If cache miss, query database
   d. Return 302 redirect to long URL
   e. Async: Log analytics event

3. Client browser redirects to long URL
```

```python
def redirect(short_code):
    # 1. Check cache first
    long_url = cache.get(f"short:{short_code}")
    
    if long_url is None:
        # 2. Cache miss - query database
        row = db.query(
            "SELECT long_url FROM urls WHERE short_code = ?",
            short_code
        )
        
        if row is None:
            raise NotFoundError()
        
        long_url = row['long_url']
        
        # 3. Populate cache
        cache.set(f"short:{short_code}", long_url, ttl=86400)
    
    # 4. Log analytics (async)
    analytics_queue.publish({
        'short_code': short_code,
        'timestamp': now(),
        'ip': request.ip,
        'user_agent': request.user_agent
    })
    
    # 5. Redirect
    return redirect_response(long_url, status=302)
```

---

## Step 8: Deep Dive Topics

### 8.1 Scalability

**Database Sharding**
```
Shard by short_code hash:

Shard 1: short_code hash % 4 == 0
Shard 2: short_code hash % 4 == 1
Shard 3: short_code hash % 4 == 2
Shard 4: short_code hash % 4 == 3

Example:
"abc123" → hash("abc123") % 4 = 2 → Shard 3
```

**Caching Strategy**
- Cache most recently accessed URLs (LRU)
- Cache hit ratio target: 80%+
- TTL: 24 hours (URLs rarely change)

**CDN for Redirects**
- Cache redirect responses at edge locations
- Reduce latency for global users
- Offload traffic from origin servers

---

### 8.2 Analytics

**Approach 1: Write to Database (Simple)**
```sql
INSERT INTO analytics (short_code, clicked_at, ip, country)
VALUES ('abc123', NOW(), '1.2.3.4', 'US');
```
- **Cons**: High write load, slows down redirects

**Approach 2: Async Queue (Better)**
```
Redirect → Publish to Kafka → Consumer writes to analytics DB
```
- **Pros**: Non-blocking, scales independently
- **Use**: Kafka or AWS Kinesis

**Approach 3: Stream Processing (Best)**
```
Redirect → Kinesis → Lambda → Aggregate → DynamoDB/Redshift
```
- Real-time aggregation
- Reduced storage (store aggregates, not raw clicks)

---

### 8.3 Custom Short URLs

```python
def create_custom_url(long_url, custom_alias, user_id):
    # 1. Validate alias (alphanumeric, length)
    if not is_valid_alias(custom_alias):
        raise InvalidAliasError()
    
    # 2. Check availability
    if db.exists(custom_alias):
        raise AliasUnavailableError()
    
    # 3. Reserve alias
    db.insert({
        'short_code': custom_alias,
        'long_url': long_url,
        'user_id': user_id
    })
    
    return f"https://short.ly/{custom_alias}"
```

---

### 8.4 Rate Limiting

Prevent abuse (spam, DDoS):

```python
# Rate limit: 10 URLs per hour per user
def check_rate_limit(user_id):
    key = f"rate_limit:{user_id}:{current_hour()}"
    count = redis.incr(key)
    
    if count == 1:
        redis.expire(key, 3600)  # 1 hour TTL
    
    if count > 10:
        raise RateLimitExceededError()
```

---

## Step 9: Trade-offs and Decisions

| Aspect | Option 1 | Option 2 | Decision |
|--------|----------|----------|----------|
| **Code Generation** | Base62 (sequential) | Random | Random (better security) |
| **Database** | SQL (PostgreSQL) | NoSQL (DynamoDB) | NoSQL (higher scale, simpler) |
| **Caching** | In-memory (local) | Distributed (Redis) | Redis (shared across servers) |
| **Analytics** | Sync DB writes | Async queue | Queue (non-blocking) |
| **URL Deduplication** | Yes (save storage) | No (simpler) | No (complexity not worth it) |

---

## Step 10: Monitoring & Operations

### Key Metrics

```
Availability:
- Uptime percentage
- Error rate (4xx, 5xx)

Performance:
- P50, P95, P99 latency for redirects
- Cache hit ratio
- Database query time

Business:
- URLs created per minute
- Redirects per minute
- Top URLs by traffic
```

### Alerting

```
Critical:
- Error rate > 1%
- Latency P99 > 500ms
- Cache hit ratio < 50%
- Database connection pool exhausted

Warning:
- Disk usage > 80%
- CPU usage > 70%
- Unusual traffic patterns
```

---

## Complete Solution Summary

**Architecture**: Stateless API servers + Redis cache + NoSQL database + Async analytics

**Code Generation**: Random 6-character base62 with collision check

**Scaling**:
- Horizontal scaling of API servers
- Read replicas for database
- Redis cluster for caching
- Database sharding if needed

**Availability**: 99.9%+ through redundancy and failover

**Performance**: < 100ms redirect latency via caching

---

## Interview Tips

1. **Start simple**: Basic version first, then optimize
2. **Ask questions**: Clarify requirements before diving in
3. **Use numbers**: Back up decisions with calculations
4. **Draw diagrams**: Visualize the system
5. **Discuss trade-offs**: Show you understand alternatives
6. **Think about failures**: What could go wrong?
7. **Scale gradually**: 1K → 1M → 1B users
8. **Communication**: Explain your thought process

---

*This is a complete end-to-end design. Practice drawing and explaining it in 45 minutes.*
