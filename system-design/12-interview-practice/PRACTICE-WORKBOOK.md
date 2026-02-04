# Daily Practice Workbook

## How to Use This Workbook

**Daily Routine (45 minutes):**
1. Pick one problem
2. Set timer for 45 minutes
3. Follow the interview framework
4. Review your solution
5. Compare with reference design

**Track your progress**: Mark ✓ when completed

---

## Week 1: Fundamentals + Simple Systems

### Day 1: URL Shortener [ ]
**Difficulty**: ⭐⭐☆☆☆

**Key Concepts**: Hashing, base conversion, capacity estimation

**Requirements**:
- Shorten long URLs
- Redirect short URLs
- 100M URLs/month
- 100:1 read:write ratio

**Focus On**:
- Short code generation strategies
- Caching strategy
- Database choice (SQL vs NoSQL)

**Success Criteria**:
- Drew high-level architecture
- Explained 3 encoding strategies
- Calculated storage and bandwidth
- Discussed collision handling

---

### Day 2: Pastebin [ ]
**Difficulty**: ⭐⭐☆☆☆

**Key Concepts**: Text storage, expiration, scaling

**Requirements**:
- Store text snippets
- Generate unique URLs
- Optional expiration
- Syntax highlighting (out of scope)

**Similar To**: URL shortener + text storage

**Focus On**:
- TTL implementation
- Storage optimization (compression)
- Read-heavy vs write-heavy optimization

**Variations**:
- Add version history
- Add password protection
- Add analytics

---

### Day 3: Key-Value Store [ ]
**Difficulty**: ⭐⭐⭐☆☆

**Key Concepts**: Hashing, replication, consistency

**Requirements**:
- `put(key, value)`
- `get(key)`
- High availability
- Scalable to billions of keys

**Focus On**:
- Consistent hashing
- Replication strategy (W, R, N)
- Conflict resolution
- CAP theorem trade-offs

**Real Examples**: Redis, DynamoDB, Memcached

---

### Day 4: Rate Limiter [ ]
**Difficulty**: ⭐⭐⭐☆☆

**Key Concepts**: Throttling, distributed counters

**Requirements**:
- Limit requests per user
- Different limits per API
- Distributed system
- Low latency overhead

**Algorithms to Know**:
1. Token bucket
2. Leaky bucket
3. Fixed window
4. Sliding window log
5. Sliding window counter

**Focus On**:
- Algorithm trade-offs
- Distributed implementation (Redis)
- Edge cases (concurrent requests)

---

### Day 5: Unique ID Generator [ ]
**Difficulty**: ⭐⭐⭐☆☆

**Key Concepts**: Distributed ID generation, time-based IDs

**Requirements**:
- Generate unique 64-bit IDs
- Sortable by time
- High throughput (10K IDs/sec per server)
- No coordination between servers

**Solutions to Discuss**:
1. UUID (pros/cons)
2. Twitter Snowflake
3. Database auto-increment
4. Redis INCR
5. Ticket server

**Focus On**:
- Snowflake structure (timestamp + datacenter + sequence)
- Clock synchronization
- Collision avoidance

---

## Week 2: Data-Intensive Applications

### Day 6: Web Crawler [ ]
**Difficulty**: ⭐⭐⭐⭐☆

**Key Concepts**: BFS, distributed queue, deduplication

**Requirements**:
- Crawl 1 billion web pages
- Politeness (robots.txt, rate limiting)
- Priority crawling
- Avoid duplicate pages

**Focus On**:
- URL frontier (queue management)
- Bloom filter for deduplication
- Distributed crawling coordination
- Handling failures

**Components**:
- URL frontier
- Fetcher
- Parser
- Duplicate detector
- Storage

---

### Day 7: Search Autocomplete [ ]
**Difficulty**: ⭐⭐⭐☆☆

**Key Concepts**: Trie, caching, ranking

**Requirements**:
- Suggest top 5 queries as user types
- Real-time suggestions (< 100ms)
- Learn from user behavior
- Handle typos (optional)

**Focus On**:
- Trie data structure
- Caching strategy (CDN + Redis)
- Ranking algorithm (frequency, recency, personalization)
- Data collection pipeline

**Scale**:
- Billions of queries
- Millions of unique queries
- Read-heavy (99% reads)

---

### Day 8: Design Netflix / YouTube [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Video streaming, CDN, encoding

**Requirements**:
- Upload videos
- Stream videos (multiple qualities)
- 1 billion users
- Global distribution

**Focus On**:
- Video upload pipeline
- Encoding (multiple formats/qualities)
- CDN architecture
- Adaptive bitrate streaming
- Storage (blob storage)

**Deep Dive Topics**:
- Video encoding workflow
- CDN selection algorithm
- Cost optimization
- Offline viewing

---

### Day 9: Design Uber / Lyft [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Geospatial indexing, matching, real-time

**Requirements**:
- Match riders with drivers
- Real-time location tracking
- ETA calculation
- Surge pricing

**Focus On**:
- Geohashing / QuadTree
- WebSocket for real-time updates
- Matching algorithm
- Location database (Redis Geo)

**Components**:
- Location service
- Matching service
- Trip service
- Payment service
- Notification service

---

### Day 10: Design Chat Application (WhatsApp) [ ]
**Difficulty**: ⭐⭐⭐⭐☆

**Key Concepts**: WebSocket, message queue, presence

**Requirements**:
- 1-on-1 and group chat
- Online status
- Message delivery guarantees
- 1 billion users

**Focus On**:
- WebSocket vs long polling
- Message ordering
- Read receipts / delivery status
- Push notifications
- Offline message storage

**Deep Dive**:
- How to scale WebSocket connections?
- Message persistence strategy
- End-to-end encryption (optional)

---

## Week 3: Social & Content Platforms

### Day 11: Design Twitter [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Fan-out, timeline, social graph

**Requirements**:
- Post tweets (280 chars)
- Follow users
- Timeline (feed of tweets from people you follow)
- 300M DAU

**Focus On**:
- Fan-out on write vs fan-out on read
- Timeline generation (Redis sorted sets)
- Celebrity problem
- Tweet storage and replication

**Trade-offs**:
```
Fan-out on write:
+ Fast reads
- Slow writes for celebrities

Fan-out on read:
+ Fast writes
- Slow reads

Hybrid:
+ Balanced
- Complex
```

---

### Day 12: Design Instagram [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Image storage, feed ranking, graph DB

**Requirements**:
- Upload photos (max 10MB)
- Follow users
- News feed
- 500M DAU

**Focus On**:
- Image storage (S3 + CDN)
- Feed generation (pull vs push vs hybrid)
- Image processing pipeline
- Sharding strategy

**Additional Features**:
- Stories (ephemeral content)
- Reels (short videos)
- Direct messages

---

### Day 13: Design Facebook News Feed [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Ranking, ML, caching

**Requirements**:
- Personalized feed
- Mix of photos, videos, status
- Ranking algorithm
- 2 billion users

**Focus On**:
- Feed ranking (relevance score)
- Feed generation architecture
- Caching strategy
- Real-time updates

**Ranking Signals**:
- Affinity score (interaction history)
- Post type (video > photo > text)
- Recency
- Engagement (likes, comments, shares)

---

### Day 14: Design TikTok [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Recommendation, video, ML

**Requirements**:
- Upload short videos (< 60s)
- Infinite scroll feed
- Personalized recommendations
- 1 billion users

**Focus On**:
- Recommendation algorithm (collaborative filtering)
- Video storage and streaming
- Feed generation (different from followers-only)
- A/B testing infrastructure

---

## Week 4: E-commerce & Transactional Systems

### Day 15: Design Amazon / E-commerce [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Inventory, transactions, consistency

**Requirements**:
- Product catalog
- Shopping cart
- Checkout and payment
- Order tracking

**Focus On**:
- Inventory management (avoid overselling)
- Distributed transactions
- Payment processing
- Search and recommendations

**Challenges**:
- Flash sales (high concurrency)
- Inventory consistency
- Abandoned cart recovery

---

### Day 16: Design Payment System (Stripe/PayPal) [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: ACID, idempotency, reconciliation

**Requirements**:
- Process payments
- Handle refunds
- Support multiple currencies
- Extremely high reliability

**Focus On**:
- Exactly-once processing (idempotency keys)
- Two-phase commit
- Reconciliation
- Fraud detection

**Critical**:
- No money lost
- No double charging
- Audit trail

---

### Day 17: Design Food Delivery (DoorDash) [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Geospatial, matching, routing

**Requirements**:
- Restaurant discovery
- Order placement
- Driver assignment
- Real-time tracking

**Focus On**:
- Driver-order matching algorithm
- Route optimization
- ETA calculation
- Status updates (WebSocket)

**Similar To**: Uber but with additional complexity (3-way marketplace)

---

### Day 18: Design Ticketmaster [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Concurrency, fairness, inventory

**Requirements**:
- Browse events
- Book tickets
- Handle high concurrency (concert tickets)
- Prevent double booking

**Focus On**:
- Ticket reservation workflow
- Optimistic locking
- Queue system (virtual waiting room)
- Seat selection

**Challenges**:
- Thundering herd (Taylor Swift concert)
- Bots and scalpers
- Payment time limits

---

## Week 5-6: Infrastructure & Platform

### Day 19: Design Dropbox / Google Drive [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: File sync, chunking, conflict resolution

**Requirements**:
- Upload/download files
- Sync across devices
- Share files
- Version history

**Focus On**:
- Chunking and deduplication
- Delta sync (only changed parts)
- Conflict resolution
- Metadata database

---

### Day 20: Design Google Docs (Collaborative Editing) [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: OT, CRDT, real-time collaboration

**Requirements**:
- Multiple users editing simultaneously
- Real-time updates
- Conflict-free
- Version history

**Focus On**:
- Operational Transformation (OT)
- WebSocket for real-time
- Conflict resolution
- Presence (who's editing)

---

### Day 21: Design Notification System [ ]
**Difficulty**: ⭐⭐⭐⭐☆

**Key Concepts**: Push, SMS, email, prioritization

**Requirements**:
- Support multiple channels (push, email, SMS)
- Priority levels
- Rate limiting (don't spam)
- Delivery guarantees

**Focus On**:
- Message queue (Kafka)
- Template management
- Retry logic
- Analytics (delivery rate)

---

### Day 22: Design API Gateway [ ]
**Difficulty**: ⭐⭐⭐⭐☆

**Key Concepts**: Routing, rate limiting, authentication

**Requirements**:
- Route requests to services
- Authentication / authorization
- Rate limiting
- Logging and monitoring

**Focus On**:
- Service discovery
- Load balancing algorithms
- Circuit breaker pattern
- Request transformation

---

### Day 23: Design Distributed Cache [ ]
**Difficulty**: ⭐⭐⭐⭐☆

**Key Concepts**: Consistent hashing, replication, eviction

**Requirements**:
- Distributed key-value store
- High availability
- Low latency (< 1ms)
- Scalable

**Focus On**:
- Consistent hashing
- Replication (for fault tolerance)
- Eviction policies (LRU, LFU)
- Cache invalidation

**Like**: Redis Cluster, Memcached

---

### Day 24: Design Message Queue (Kafka) [ ]
**Difficulty**: ⭐⭐⭐⭐⭐

**Key Concepts**: Pub-sub, partitioning, ordering

**Requirements**:
- Publish messages
- Subscribe to topics
- Ordering guarantees
- High throughput (millions/sec)

**Focus On**:
- Partitioning for parallelism
- Consumer groups
- Offset management
- Replication for durability

---

## Week 7-8: Advanced & Mock Interviews

### Day 25-28: Review and Variations

Practice variations of previous problems:
- Design TikTok → Design YouTube Shorts
- Design Twitter → Design Mastodon (federated)
- Design Uber → Design Uber Eats
- Design Instagram → Design Pinterest

### Day 29-35: Mock Interviews

**Schedule**:
- 3 interviews with peers (take turns interviewing)
- 2 solo timed practice
- 2 reviewing others' designs

**Focus**:
- Communication
- Time management
- Handling curveball questions
- Confidence

---

## Self-Assessment Rubric

After each problem, rate yourself:

**Requirements (0-5)**
- [ ] Asked clarifying questions
- [ ] Defined functional requirements
- [ ] Defined non-functional requirements
- [ ] Identified scale

**Estimation (0-5)**
- [ ] Calculated traffic (QPS)
- [ ] Calculated storage
- [ ] Calculated bandwidth
- [ ] Showed all work

**Design (0-10)**
- [ ] Drew high-level architecture
- [ ] Identified key components
- [ ] Explained data flow
- [ ] Discussed APIs
- [ ] Designed data models

**Deep Dive (0-10)**
- [ ] Explained component internals
- [ ] Discussed trade-offs
- [ ] Addressed scalability
- [ ] Addressed reliability
- [ ] Handled edge cases

**Communication (0-5)**
- [ ] Thought out loud
- [ ] Listened to feedback
- [ ] Used time effectively
- [ ] Professional presentation

**Total**: ___/35

- 30-35: Excellent, ready for interviews
- 25-29: Good, practice communication
- 20-24: Decent, practice more problems
- < 20: Review fundamentals

---

## Problem Variations (Bonus)

For deeper practice, add constraints:

**Add Scale**:
- "Now design for 10x users"
- "What if 100x write traffic?"

**Add Features**:
- "Add real-time analytics"
- "Add fraud detection"
- "Add A/B testing"

**Add Constraints**:
- "Must be multi-region"
- "Must support offline mode"
- "Budget is limited"

**Add Failures**:
- "Database goes down"
- "Network partition"
- "Cache failure"

---

## Tips for Success

1. **Consistency**: Practice daily, even 30 minutes
2. **Variety**: Don't just do easy problems
3. **Review**: Study reference solutions
4. **Teach**: Explain designs to others
5. **Time**: Always use a timer
6. **Iterate**: Redo problems you struggled with
7. **Real-world**: Read engineering blogs
8. **Mock**: Practice with peers

---

*Remember: Interviewing is a skill. The more you practice, the better you get!*
