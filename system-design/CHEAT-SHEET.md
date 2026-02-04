# Quick Reference Cheat Sheet

## System Design in 1 Page

### The Interview Process
```
1. Requirements (5 min)      → Clarify scope
2. Estimation (5 min)         → Do the math
3. API Design (5 min)         → Define interfaces
4. High-Level (10 min)        → Draw diagram
5. Deep Dive (15 min)         → Solve bottlenecks
6. Wrap-up (5 min)            → Scale & edge cases
```

---

## Key Numbers to Memorize

### Latency
```
L1 cache:        0.5 ns
RAM:             100 ns        (200x slower)
SSD:             100 μs        (1,000x slower than RAM)
HDD:             10 ms         (100x slower than SSD)
Network (DC):    0.5 ms        
Network (CA→EU): 150 ms        (300x slower than DC)
```

### Throughput
```
Network card:   1 Gbps   = 125 MB/s
SSD read:       500 MB/s
HDD read:       100 MB/s
```

### Time Conversions
```
1 day      = 86,400 seconds   (~100K)
1 month    = 2.5M seconds
1 year     = 31.5M seconds
```

### Powers of 10
```
10³ = Thousand  (K)
10⁶ = Million   (M)
10⁹ = Billion   (B)
10¹² = Trillion (T)
```

---

## Common Capacity Estimates

### Storage
```
1 character = 1-2 bytes
UUID = 36 bytes
Timestamp = 8 bytes
Integer = 4-8 bytes
Image (compressed) = 200 KB
Video (1 min, HD) = 50 MB

Daily storage = (items/day) × (size/item)
5-year storage = Daily × 365 × 5
With replication (3x) = Storage × 3
```

### Traffic
```
QPS = Daily requests / 86,400
Peak QPS = Average QPS × 3
Write QPS = New items / 86,400
Read QPS = Views / 86,400
```

### Cache (80-20 rule)
```
Cache size = 20% of requests × size per item
Typical: 20% of data serves 80% of requests
```

---

## Database Selection

### SQL (RDBMS)
**Use when:**
- Structured data with relationships
- ACID transactions required
- Complex queries (joins, aggregations)
- Data integrity critical

**Examples**: Banking, Orders, User profiles

**Scale**: Vertical → Read replicas → Sharding

---

### NoSQL

**Key-Value** (DynamoDB, Redis)
- Simple lookups
- Session storage
- Caching
- Shopping cart

**Document** (MongoDB)
- Flexible schema
- Nested data
- Product catalogs
- User profiles (evolving schema)

**Column-Family** (Cassandra, HBase)
- Time-series data
- High write throughput
- Analytics
- IoT data

**Graph** (Neo4j)
- Social networks
- Recommendations
- Fraud detection

---

## Caching Strategies

```
Cache-Aside:     Read → Check cache → If miss, read DB → Update cache
Write-Through:   Write → DB + cache simultaneously
Write-Back:      Write → Cache → Async write to DB
Refresh-Ahead:   Refresh cache before expiration
```

**Cache Levels:**
```
Client (Browser) → CDN → Application (Redis) → Database
```

**Eviction Policies:**
- LRU: Least Recently Used
- LFU: Least Frequently Used  
- TTL: Time To Live
- FIFO: First In First Out

---

## Load Balancing Algorithms

```
Round Robin:        A → B → C → A (simple, fair)
Least Connections:  Route to server with fewest active connections
Weighted:           A(70%) → B(20%) → C(10%)
IP Hash:            hash(IP) → same server (session persistence)
```

**Types:**
- L4 (Network): Fast, IP/port only
- L7 (Application): Smart, HTTP headers/cookies

---

## Sharding Strategies

**Hash-based:**
```python
shard = hash(key) % num_shards
```
✓ Even distribution  
✗ Resharding is expensive

**Consistent Hashing:**
```
Ring → Virtual nodes → Minimal resharding
```
✓ Add/remove nodes easily  
✗ More complex

**Range-based:**
```
Shard 1: 0-999999
Shard 2: 1000000-1999999
```
✓ Range queries easy  
✗ Hotspots

---

## CAP Theorem

```
Choose 2 of 3 during network partition:
- Consistency
- Availability  
- Partition Tolerance (always needed in distributed systems)
```

**CP Systems** (Consistency + Partition Tolerance):
- Bank transactions
- Inventory systems
- Return error during partition

**AP Systems** (Availability + Partition Tolerance):
- Social media feeds
- Shopping cart
- Return potentially stale data

---

## Consistency Models

**Strong Consistency:**
- Read always returns latest write
- Higher latency
- Ex: SQL databases, Spanner

**Eventual Consistency:**
- Reads may return stale data temporarily
- High availability
- Ex: DynamoDB, Cassandra, DNS

**Quorum:** `W + R > N`
```
N = replicas
W = write quorum
R = read quorum
```

---

## Common Patterns

### API Design
```http
POST   /api/v1/resource      (Create)
GET    /api/v1/resource/:id  (Read one)
GET    /api/v1/resource      (Read all)
PUT    /api/v1/resource/:id  (Update)
DELETE /api/v1/resource/:id  (Delete)
```

### Polling vs WebSocket
```
Polling:     Client asks every N seconds
Long-poll:   Server holds connection until data
WebSocket:   Bi-directional, real-time
SSE:         Server → Client only
```

**Use WebSocket for:** Chat, live updates, gaming  
**Use Polling for:** Simple, infrequent updates

---

### Message Queue Patterns

**Work Queue:**
```
Producer → Queue → Worker1
                 → Worker2
Each message to ONE worker
```

**Pub-Sub:**
```
Publisher → Topic → Subscriber1
                  → Subscriber2
Each message to ALL subscribers
```

---

## Reliability Patterns

**Replication:**
```
Master → Replica1
      → Replica2
      → Replica3
```

**Health Checks:**
```python
if server.response_time > threshold or error_rate > 5%:
    mark_unhealthy()
```

**Circuit Breaker:**
```
Closed → Errors → Open (fail fast)
Open → Timeout → Half-Open (try again)
Half-Open → Success → Closed
```

**Retry with Backoff:**
```
Retry 1: wait 1s
Retry 2: wait 2s
Retry 3: wait 4s
...
```

---

## Code Generation Patterns

### Base62 Encoding
```python
chars = "0-9a-zA-Z"  # 62 chars
6 chars = 62^6 = 56B unique IDs
7 chars = 62^7 = 3.5T unique IDs
```

### UUID
```
128-bit, globally unique
Pros: No coordination
Cons: Not sortable, large (36 bytes)
```

### Snowflake (Twitter)
```
64 bits:
- 41 bits: timestamp (ms)
- 10 bits: machine ID
- 12 bits: sequence number

Sortable by time, distributed generation
```

---

## Monitoring Metrics

**The Golden Signals:**
```
1. Latency   (response time)
2. Traffic   (requests/sec)
3. Errors    (error rate %)
4. Saturation (resource usage %)
```

**Percentiles:**
```
p50 (median):  50% of requests
p95:           95% of requests
p99:           99% of requests
p99.9:         99.9% of requests
```

Always report p95 or p99, not average!

---

## Quick Architecture Patterns

### Read-Heavy System
```
Client → CDN → Load Balancer → App Servers
                                    ↓
                                  Cache (Redis)
                                    ↓
                        Primary DB ← Read Replicas
```

### Write-Heavy System  
```
Client → LB → App → Queue (Kafka)
                        ↓
                   Worker Pool
                        ↓
                  Sharded Database
```

### Global System
```
Users (US) → US Data Center ─┐
                              │
Users (EU) → EU Data Center ──┼─ Replication
                              │
Users (AS) → AS Data Center ─┘
```

---

## Trade-offs Checklist

Always discuss:
- [ ] SQL vs NoSQL
- [ ] Sync vs Async
- [ ] Push vs Pull
- [ ] Consistency vs Availability
- [ ] Normalized vs Denormalized
- [ ] Vertical vs Horizontal scaling
- [ ] Monolith vs Microservices

---

## Common Bottlenecks & Solutions

| Bottleneck | Solution |
|------------|----------|
| Database reads | Add caching, read replicas |
| Database writes | Sharding, queue for async |
| Single point of failure | Replication, load balancing |
| Slow queries | Indexing, denormalization |
| Network latency | CDN, edge computing |
| CPU-bound | Horizontal scaling |
| Memory-bound | Larger instances or caching |

---

## Interview Red Flags to Avoid

❌ Not asking questions  
❌ Jumping to code  
❌ Single server design for billions of users  
❌ Ignoring trade-offs  
❌ No diagrams  
❌ No capacity estimation  
❌ "This is the only way"  
❌ Overusing buzzwords without understanding  

✅ Ask questions  
✅ Start simple, then scale  
✅ Draw diagrams  
✅ Show math  
✅ Discuss trade-offs  
✅ Think out loud  
✅ Listen to interviewer  

---

*Keep this cheat sheet handy during practice sessions!*
