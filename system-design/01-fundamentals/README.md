# Phase 1: Fundamentals

## Overview
Master the foundational concepts that underpin all system design decisions.

**Duration**: Week 1-2  
**Time Commitment**: 2-3 hours daily  
**Goal**: Understand WHY systems need to be designed, not just HOW

---

## Module 1.1: Core Principles (Day 1-2)

### 1. Scalability

**Definition**: The ability of a system to handle growing amounts of work by adding resources.

#### Types of Scalability

**Vertical Scaling (Scale Up)**
```
Before:          After:
┌──────┐        ┌──────┐
│ 4 GB │   →    │16 GB │
│2 Core│        │8 Core│
└──────┘        └──────┘
```
- **Pros**: Simple, no code changes, maintains consistency
- **Cons**: Hardware limits, single point of failure, expensive
- **When**: Starting out, stateful services, databases initially

**Horizontal Scaling (Scale Out)**
```
Before:          After:
┌──────┐        ┌──────┐ ┌──────┐ ┌──────┐
│Server│   →    │Server│ │Server│ │Server│
└──────┘        └──────┘ └──────┘ └──────┘
```
- **Pros**: Nearly unlimited scaling, fault tolerant, cost effective
- **Cons**: Complex, requires stateless design, data consistency challenges
- **When**: High traffic, need redundancy, cloud environments

#### Key Metrics
- **Throughput**: Requests per second (RPS)
- **Latency**: Time to respond (p50, p95, p99)
- **Load**: Current capacity usage (%)

#### Real-World Numbers (Memorize These!)
```
L1 cache reference:           0.5 ns
L2 cache reference:           7 ns
RAM reference:                100 ns
Send 1K bytes over network:   10,000 ns = 10 μs
SSD random read:              150,000 ns = 150 μs
Disk seek:                    10,000,000 ns = 10 ms
Send packet CA→NL→CA:         150,000,000 ns = 150 ms
```

**Why This Matters**: Designing for 1K vs 1M vs 1B users requires different architectures.

---

### 2. Reliability

**Definition**: The system continues to work correctly even when things go wrong.

#### Key Concepts

**High Availability (HA)**
- Measured in "nines": 99.9% = 8.77 hours downtime/year
- 99.99% (four nines) = 52.6 minutes downtime/year
- 99.999% (five nines) = 5.26 minutes downtime/year

```
Availability = (Uptime / (Uptime + Downtime)) × 100%

SLA Targets:
99%     → 3.65 days/year downtime
99.9%   → 8.77 hours/year downtime
99.99%  → 52.6 minutes/year downtime
99.999% → 5.26 minutes/year downtime
```

**Fault Tolerance**
- System continues working despite component failures
- Redundancy: Multiple instances
- Graceful degradation: Reduced functionality beats complete failure

**Failure Modes**
1. **Fail-fast**: Detect and report failures immediately
2. **Fail-safe**: Default to safe state
3. **Fail-over**: Switch to backup automatically

#### Reliability Patterns

**Replication**
```
       Write
         ↓
    ┌────────┐
    │Primary │
    └────────┘
       ↓  ↓  ↓
    ┌──┐┌──┐┌──┐
    │R1││R2││R3│  (Replicas)
    └──┘└──┘└──┘
```

**Health Checks**
```python
# Example health check pattern
def health_check():
    checks = {
        'database': check_db_connection(),
        'cache': check_cache_connection(),
        'disk_space': check_disk_space() > 10%,
    }
    return all(checks.values())
```

---

### 3. Maintainability

**Definition**: System should be easy to operate, understand, and modify.

#### Principles

**Operability**
- Easy to monitor (metrics, logs, alerts)
- Automation for routine tasks
- Good documentation
- Predictable behavior

**Simplicity**
- Avoid unnecessary complexity
- Use proven patterns
- Clear abstractions
- Remove accidental complexity

**Evolvability**
- Easy to make changes
- Well-tested
- Modular architecture
- Backward compatibility

---

## Module 1.2: System Design Thinking (Day 3-4)

### The Design Process

**Step 1: Understand the Problem**
```
Questions to ask:
- Who are the users? (B2C, B2B, internal)
- How many users? (scale)
- What are they doing? (use cases)
- What can we NOT do? (constraints)
```

**Step 2: Define Requirements**

*Functional Requirements (Features)*
- What the system should do
- User-facing features
- API endpoints

*Non-Functional Requirements (Qualities)*
- Performance: Latency < 200ms
- Scale: 10M daily active users
- Availability: 99.99%
- Consistency: Eventual or strong?
- Security: Authentication, encryption

**Step 3: Capacity Planning**

```
Example: Design Twitter

Users: 300M monthly active
Daily active: 150M (50% of monthly)
Tweets per day: 300M (2 tweets/active user)
Reads: 10B per day (followers reading)

Storage:
- Tweet: 280 chars × 2 bytes = 560 bytes
- Metadata: 200 bytes
- Total per tweet: ~800 bytes
- Daily storage: 300M × 800 bytes = 240 GB/day
- 5 year storage: 240 GB × 365 × 5 = 438 TB

Bandwidth:
- Write: 300M tweets/day = 3,500 tweets/sec
- Read: 10B reads/day = 115,000 reads/sec
- Read:Write ratio = 33:1
```

**Step 4: API Design**

```
POST /api/v1/tweet
{
  "userId": "123",
  "content": "Hello world!",
  "timestamp": 1234567890
}

GET /api/v1/timeline/{userId}?limit=20&cursor=xyz
Response: [list of tweets]

GET /api/v1/tweet/{tweetId}
Response: {tweet object}
```

---

## Module 1.3: Trade-offs (Day 5-6)

### The Art of Trade-offs

> "There are no solutions, only trade-offs" - Thomas Sowell

#### Common Trade-offs

**1. Consistency vs Availability (CAP Theorem)**
```
         Consistency
              ▲
             ╱ ╲
            ╱   ╲
           ╱  CP  ╲
          ╱       ╲
         ╱    △    ╲
        ╱   Network ╲
       ╱   Partition ╲
      ╱───────────────╲
  Availability ←→ Partition Tolerance

Choose 2 of 3 during network partition
```

**2. Latency vs Throughput**
- Low latency: Fast individual requests (gaming)
- High throughput: Many requests per second (batch processing)
- Often inversely related

**3. Read vs Write Optimization**
- Read-heavy: Caching, replicas, denormalization
- Write-heavy: Write-ahead logs, batch writes, sharding

**4. Normalized vs Denormalized**
| Normalized | Denormalized |
|------------|--------------|
| No redundancy | Duplicate data |
| Slower reads (joins) | Faster reads |
| Easier writes | Complex writes |
| Less storage | More storage |

**5. SQL vs NoSQL**
| SQL | NoSQL |
|-----|-------|
| Structured schema | Flexible schema |
| ACID guarantees | Eventually consistent |
| Vertical scaling | Horizontal scaling |
| Complex queries | Simple queries |
| Joins supported | No joins |

---

## Practical Exercises

### Exercise 1: Estimate Netflix Daily Bandwidth
```
Given:
- 200M subscribers
- 50% watch daily
- 2 hours average viewing
- Video quality: 3 Mbps average

Calculate:
1. Total viewing hours per day
2. Total data transferred per day
3. Peak bandwidth requirement
```

### Exercise 2: Design Trade-offs
For a **banking application**, choose and justify:
- SQL vs NoSQL?
- Consistency vs Availability during network partition?
- Horizontal vs Vertical scaling?
- Sync vs Async processing?

### Exercise 3: Back-of-envelope Calculation
Design a URL shortener:
- 100M new URLs per month
- 10:1 read:write ratio
- 5-year retention

Calculate:
- Storage needed
- Read/write QPS
- Bandwidth requirements

---

## Self-Assessment Checklist

After completing this module, you should be able to:

- [ ] Explain scalability with real examples
- [ ] Calculate availability percentages and downtime
- [ ] Estimate capacity for any system
- [ ] List 5+ common trade-offs from memory
- [ ] Design a simple API for a given problem
- [ ] Justify architectural decisions
- [ ] Draw basic system diagrams
- [ ] Explain CAP theorem with examples

---

## Key Takeaways

1. **Scalability** = handling growth (vertical vs horizontal)
2. **Reliability** = working correctly despite failures
3. **Maintainability** = easy to operate and evolve
4. **Trade-offs** = every decision has costs and benefits
5. **Estimation** = back-of-envelope math is essential
6. **Requirements** = functional + non-functional
7. **No perfect solution** = context determines best choice

---

## Next Steps

Once you've mastered these fundamentals:
1. Review your notes from memory
2. Explain each concept to someone else
3. Move to [02-core-building-blocks](../02-core-building-blocks/README.md)

---

## Additional Resources

- Martin Kleppmann - "Designing Data-Intensive Applications" (Chapter 1)
- Jeff Dean - "Numbers Everyone Should Know"
- AWS Well-Architected Framework - Reliability Pillar
- Google SRE Book - Chapters 1-3

---

*Practice Problem*: Design a system to store 1 billion user profiles. What would you choose and why?
