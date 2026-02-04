# System Design Interview Framework

## The 45-Minute Structure

```
┌─────────────────────────────────────────┐
│ Phase 1: Requirements (5 min)          │  Clarify scope
├─────────────────────────────────────────┤
│ Phase 2: Estimation (5 min)            │  Back-of-envelope
├─────────────────────────────────────────┤
│ Phase 3: API & Data (5 min)            │  Interfaces
├─────────────────────────────────────────┤
│ Phase 4: High-Level Design (10 min)    │  Big picture
├─────────────────────────────────────────┤
│ Phase 5: Deep Dive (15 min)            │  Interviewer-led
├─────────────────────────────────────────┤
│ Phase 6: Wrap-up (5 min)               │  Edge cases, scale
└─────────────────────────────────────────┘
```

---

## Phase 1: Requirements Gathering (5 minutes)

### What to Do

**Clarify the problem** through questions. Never assume!

### Template Questions

#### Functional Requirements
```
Questions to ask:
1. What are the core features? (must-haves)
2. Who are the users? (B2C, B2B, internal)
3. What are the main user flows?
4. Any features out of scope? (nice-to-haves)
5. Mobile, web, or both?

Example for "Design YouTube":
✓ What: Upload, watch, search videos
✓ Who: 1 billion users worldwide
✓ Flows: Upload → Process → Watch, Search → Watch
✗ Out of scope: Comments, live streaming, ads
```

#### Non-Functional Requirements
```
Questions to ask:
1. How many users? (DAU, MAU)
2. Scale expectations? (requests/sec, data volume)
3. Performance targets? (latency, throughput)
4. Availability requirements? (99.9%, 99.99%)
5. Consistency vs. availability preference?
6. Geographic distribution? (global vs. regional)

Example numbers:
- 100M daily active users
- 10M videos uploaded/day
- 1B video views/day
- < 200ms initial load time
- 99.9% availability
- Global distribution
```

### Output of This Phase

**Write on whiteboard:**
```
FUNCTIONAL REQUIREMENTS:
- Upload videos (max 1GB, common formats)
- Watch videos (streaming, quality selection)
- Search videos (by title, description, tags)

NON-FUNCTIONAL REQUIREMENTS:
- 100M DAU, 1B video views/day
- < 200ms start time, 99.9% availability
- Global CDN distribution
- Eventual consistency OK for views count

OUT OF SCOPE:
- Comments, likes
- Live streaming
- Recommendations (initially)
```

---

## Phase 2: Capacity Estimation (5 minutes)

### What to Calculate

1. **Traffic** (QPS: queries per second)
2. **Storage** (total data volume)
3. **Bandwidth** (ingress/egress)
4. **Cache size** (if applicable)

### Estimation Template

```
# Traffic Estimation
Daily active users: X
Actions per user per day: Y
Total requests per day: X × Y
Requests per second (QPS): (X × Y) / 86,400
Peak QPS: Average QPS × 2-3

# Storage Estimation
Size per item: A bytes
Items per day: B
Daily storage: A × B
Storage for N years: A × B × 365 × N
With replication (3x): A × B × 365 × N × 3

# Bandwidth Estimation
Write bandwidth: Daily storage / 86,400
Read/write ratio: R:1
Read bandwidth: Write bandwidth × R

# Cache Estimation (80-20 rule)
Daily requests: QPS × 86,400
Cache 20% hot data: Daily requests × 0.2 × item_size
```

### Example: Design Twitter

```
GIVEN:
- 300M monthly active users
- 50% active daily = 150M DAU
- Each user: 2 tweets/day, reads 100 tweets/day

TRAFFIC:
- Write: 150M × 2 = 300M tweets/day
  = 300M / 86,400 ≈ 3,500 tweets/sec
- Read: 150M × 100 = 15B reads/day
  = 15B / 86,400 ≈ 173,000 reads/sec
- Read:Write ratio = 50:1

STORAGE:
- Tweet size: 280 chars × 2 bytes + 200 bytes metadata = 760 bytes
- Daily: 300M × 760 bytes = 228 GB/day
- 5 years: 228 GB × 365 × 5 = 416 TB
- With 3x replication: 1.2 PB

BANDWIDTH:
- Write: 228 GB / 86,400 = 2.6 MB/sec
- Read: 2.6 MB/sec × 50 = 130 MB/sec

CACHE (80-20):
- 20% of daily reads: 15B × 0.2 = 3B tweets
- Cache size: 3B × 760 bytes ≈ 2.2 TB
```

### Key Numbers to Memorize

```
Time conversions:
- 1 day = 86,400 seconds
- 1 month ≈ 2.5M seconds (30 × 86,400)
- 1 year ≈ 31.5M seconds (365 × 86,400)

Storage:
- 1 KB = 10³ bytes
- 1 MB = 10⁶ bytes
- 1 GB = 10⁹ bytes
- 1 TB = 10¹² bytes
- 1 PB = 10¹⁵ bytes

Latency:
- RAM: 100 ns
- SSD: 100 µs (1,000x slower than RAM)
- HDD: 10 ms (100x slower than SSD)
- Network (same datacenter): 0.5 ms
- Network (cross-continent): 150 ms

Throughput:
- Network card: 1 Gbps = 125 MB/sec
- SSD: 500 MB/sec sequential read
- HDD: 100 MB/sec sequential read
```

---

## Phase 3: API & Data Model (5 minutes)

### API Design

**REST API Template:**
```http
# Create
POST /api/v1/{resource}
Body: {...}
Response: {id, ...}

# Read (single)
GET /api/v1/{resource}/{id}
Response: {...}

# Read (list)
GET /api/v1/{resource}?limit=20&offset=0
Response: {items: [...], total: N}

# Update
PUT /api/v1/{resource}/{id}
Body: {...}

# Delete
DELETE /api/v1/{resource}/{id}
```

**Example: Twitter API**
```http
# Post tweet
POST /api/v1/tweets
{
  "userId": "123",
  "content": "Hello world",
  "mediaUrls": ["url1", "url2"]
}
Response: {tweetId: "456", timestamp: ...}

# Get timeline
GET /api/v1/timeline/{userId}?limit=20&cursor=xyz
Response: {tweets: [...], nextCursor: "abc"}

# Get tweet
GET /api/v1/tweets/{tweetId}
Response: {tweetId, userId, content, ...}
```

### Data Model

**SQL Schema Template:**
```sql
CREATE TABLE resource (
    id BIGINT PRIMARY KEY,
    field1 VARCHAR(255) NOT NULL,
    field2 TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    INDEX idx_field1 (field1)
);
```

**NoSQL Schema Template:**
```json
{
  "pk": "partition_key",
  "sk": "sort_key",
  "attribute1": "value",
  "attribute2": 123,
  "created_at": 1234567890
}
```

**Example: Twitter Schema**
```sql
-- Users
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    email VARCHAR(255),
    created_at TIMESTAMP
);

-- Tweets
CREATE TABLE tweets (
    tweet_id BIGINT PRIMARY KEY,
    user_id BIGINT,
    content VARCHAR(280),
    created_at TIMESTAMP,
    INDEX idx_user_created (user_id, created_at DESC)
);

-- Followers
CREATE TABLE followers (
    follower_id BIGINT,
    followee_id BIGINT,
    created_at TIMESTAMP,
    PRIMARY KEY (follower_id, followee_id),
    INDEX idx_followee (followee_id)
);
```

---

## Phase 4: High-Level Design (10 minutes)

### Component Diagram Template

```
┌──────────┐
│  Client  │
└────┬─────┘
     │
┌────┴────────────────┐
│   Load Balancer     │
└────┬────────────────┘
     │
┌────┴────┬───────────┬──────────┐
│  Web    │   Web     │   Web    │
│ Server 1│  Server 2 │  Server N│
└────┬────┴───────────┴──────┬───┘
     │                       │
┌────┴────┐           ┌──────┴────┐
│  Cache  │           │    DB     │
│ (Redis) │           │ (Primary) │
└─────────┘           └───────────┘
                           │
                      ┌────┴────┐
                      │Replicas │
                      └─────────┘
```

### Draw Step-by-Step

**1. Start with core flow:**
```
User → Server → Database
```

**2. Add load balancing:**
```
User → Load Balancer → Servers → Database
```

**3. Add caching:**
```
User → LB → Servers → Cache → Database
```

**4. Add specific components:**
```
- CDN for static content
- Message queue for async processing
- Search service
- Analytics pipeline
```

### Example: Instagram High-Level Design

```
          ┌─────────┐
          │  Users  │
          └────┬────┘
               │
        ┌──────┴───────┐
        │              │
    ┌───┴───┐      ┌───┴────┐
    │  CDN  │      │  API   │
    └───────┘      │Gateway │
                   └───┬────┘
                       │
        ┌──────────────┼──────────────┐
        │              │              │
   ┌────┴─────┐  ┌────┴────┐  ┌──────┴──────┐
   │  Image   │  │  Feed   │  │   Metadata  │
   │ Service  │  │ Service │  │   Service   │
   └────┬─────┘  └────┬────┘  └──────┬──────┘
        │             │              │
   ┌────┴────┐   ┌────┴────┐    ┌────┴────┐
   │   S3    │   │ Redis   │    │   DB    │
   │(Images) │   │(Feeds)  │    │(Metadata)│
   └─────────┘   └─────────┘    └─────────┘
```

---

## Phase 5: Deep Dive (15 minutes)

### Interviewer Will Ask About:

1. **Bottlenecks**: What limits scale?
2. **Failures**: What if component X fails?
3. **Specific component**: How does X work internally?
4. **Trade-offs**: Why choice A over B?
5. **Edge cases**: What about scenario Y?

### Be Ready to Discuss:

**Scalability**
- How to scale each component?
- Sharding strategy
- Caching strategy
- CDN usage

**Reliability**
- Replication
- Failover
- Circuit breakers
- Retry logic

**Performance**
- Database indexing
- Query optimization
- Async processing
- Batch operations

**Consistency**
- Strong vs. eventual
- Conflict resolution
- Transaction handling

### Deep Dive Template

**Pick a component and explain:**

```
Component: [e.g., News Feed Generation]

1. Problem:
   "Generating feed is slow. Each user follows 1000 people.
    Loading their latest tweets requires 1000 queries."

2. Solution Options:
   A. Pre-compute feeds (write-heavy)
   B. Compute on-demand (read-heavy)
   C. Hybrid (pre-compute for most, on-demand for celebrities)

3. Chosen Solution: Hybrid
   Why: Balance between read and write load
   
4. Implementation:
   - Fan-out on write for users with < 1M followers
   - On-demand merge for celebrity tweets
   - Cache final feeds in Redis
   
5. Trade-offs:
   + Pros: Fast reads, scalable
   - Cons: Complex, eventual consistency
```

---

## Phase 6: Wrap-up (5 minutes)

### Topics to Cover

**1. Bottlenecks**
```
"What are the bottlenecks?"

- Database writes (solution: sharding, write queues)
- Image uploads (solution: parallel processing, CDN)
- Feed generation (solution: pre-computation, caching)
```

**2. Monitoring**
```
"What metrics to track?"

- Latency (P50, P95, P99)
- Error rate
- Throughput (requests/sec)
- Resource utilization (CPU, memory, disk)
- Business metrics (DAU, uploads/day)
```

**3. Scaling to 10x**
```
"How to handle 10x traffic?"

Current: 100M users
10x: 1B users

Changes needed:
- More database shards
- Larger cache cluster
- Additional datacenters
- Better CDN coverage
- Async processing for everything non-critical
```

**4. Edge Cases**
```
Examples:
- What if user uploads 10GB video?
  → Reject, enforce limits
  
- What if celebrity with 100M followers tweets?
  → Don't fan-out, compute on-read
  
- What if database goes down?
  → Failover to replica
  
- What if network partition?
  → Degrade gracefully (show stale data)
```

---

## The Mental Checklist

Before saying "I'm done":

- [ ] Covered functional requirements
- [ ] Covered non-functional requirements
- [ ] Drew clear diagrams
- [ ] Explained data flow
- [ ] Discussed trade-offs
- [ ] Mentioned caching strategy
- [ ] Addressed scalability
- [ ] Addressed reliability
- [ ] Talked about monitoring
- [ ] Handled at least 2 edge cases
- [ ] Asked for feedback

---

## Common Mistakes to Avoid

**❌ Don't:**
1. Jump into design without asking questions
2. Over-engineer initially (start simple!)
3. Ignore capacity estimation
4. Focus on one aspect (e.g., only database)
5. Forget to draw diagrams
6. Use buzzwords without understanding
7. Ignore the interviewer's hints
8. Design in complete silence

**✅ Do:**
1. Ask clarifying questions first
2. Think out loud (communicate!)
3. Start with simple design, then evolve
4. Use back-of-envelope math
5. Draw everything
6. Discuss trade-offs
7. Listen to interviewer direction
8. Manage your time

---

## Sample Dialogue

**Interviewer**: "Design Instagram"

**You**: "Great! Let me start by clarifying some requirements. 
For functional requirements, should I focus on the core photo sharing features - uploading photos, viewing feed, and following users? Are features like stories, reels, and messages out of scope for now?"

**Interviewer**: "Yes, focus on core features."

**You**: "Perfect. For non-functional requirements, what scale are we targeting? Should I assume hundreds of millions of daily active users?"

**Interviewer**: "Yes, assume 500M DAU"

**You**: "Got it. Any specific performance or availability targets?"

**Interviewer**: "Photos should load quickly, and the system should be highly available."

**You**: "Understood. Let me define the requirements clearly on the board, then move to capacity estimation..."

*(Continue through the framework)*

---

## Time Management

```
0-5 min:   Requirements (don't skip!)
5-10 min:  Estimation (show math skills)
10-15 min: API + Data (quick but thorough)
15-25 min: High-level design (main focus)
25-40 min: Deep dive (follow interviewer lead)
40-45 min: Wrap-up (show breadth)

If running out of time:
- Prioritize high-level design
- Mention (don't detail) remaining components
- Explicitly say "I'd also consider X, but skipping for time"
```

---

## Practice Routine

1. **Week 1-2**: Master framework with simple systems
2. **Week 3-4**: Practice medium complexity systems
3. **Week 5-6**: Practice complex systems
4. **Week 7-8**: Mock interviews

**Daily practice** (45 min):
- Day 1: URL Shortener
- Day 2: Pastebin
- Day 3: Instagram
- Day 4: Twitter
- Day 5: YouTube
- Day 6: Uber
- Day 7: Netflix
(Repeat with variations)

---

*Remember: The interview is a conversation, not an exam. Think out loud, engage with the interviewer, and show your thought process!*
