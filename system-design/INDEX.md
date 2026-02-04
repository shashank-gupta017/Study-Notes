# System Design Mastery - Complete Index

**Total Content**: ~21,000 words | ~150 pages | 8-week program

---

## 📖 How to Navigate This Program

### Start Here (First 30 Minutes)
1. **[OVERVIEW.md](OVERVIEW.md)** - What you'll learn and why
2. **[GETTING-STARTED.md](GETTING-STARTED.md)** - Your first steps
3. **[README.md](README.md)** - Full program details

### Essential References
- **[CHEAT-SHEET.md](CHEAT-SHEET.md)** - Quick reference (bookmark this!)
- **[PROGRESS-TRACKER.md](PROGRESS-TRACKER.md)** - Track your journey

---

## 📚 Curriculum (8 Weeks)

### Week 1-2: Foundations

#### [01-fundamentals/README.md](01-fundamentals/README.md)
**Core Principles** (~8,500 words)
- ✅ Scalability (vertical vs horizontal)
- ✅ Reliability and availability (SLA, fault tolerance)
- ✅ Maintainability and operability
- ✅ System design thinking process
- ✅ Trade-offs and decision making
- ✅ Back-of-envelope calculations
- ✅ CAP theorem introduction

**Key Numbers to Memorize**:
- Latency: RAM (100ns), SSD (100μs), Disk (10ms), Network (150ms)
- Time: 1 day = 86,400 sec, 1 month = 2.5M sec
- Availability: 99.9% = 8.77 hours downtime/year

**Exercises**:
- Calculate Netflix daily bandwidth
- Design trade-offs for banking app
- Estimate URL shortener capacity

---

#### [02-core-building-blocks/README.md](02-core-building-blocks/README.md)
**Building Blocks** (~12,000 words)
- ✅ Load Balancers (algorithms, L4 vs L7, health checks)
- ✅ Caching (strategies, eviction, invalidation)
- ✅ Databases (SQL vs NoSQL, indexing, scaling)
- ✅ Message Queues (patterns, reliability, use cases)

**Deep Dives**:
- Load balancing algorithms comparison
- 4 caching strategies with code examples
- Database selection decision tree
- Message queue patterns (work queue vs pub-sub)

**Exercises**:
- Design caching for e-commerce
- Choose database for 5 scenarios
- Configure load balancer for different apps

---

### Week 3-4: Essential Patterns

#### [06-data-patterns/CAP-THEOREM.md](06-data-patterns/CAP-THEOREM.md)
**CAP Theorem & Consistency** (~10,000 words)
- ✅ CAP theorem deep dive (CP vs AP systems)
- ✅ Consistency models (strong, eventual, causal)
- ✅ Quorum-based replication (W + R > N)
- ✅ Conflict resolution (LWW, vector clocks, CRDTs)

**Real Examples**:
- Facebook posts (AP) - why eventual consistency
- Banking transfers (CP) - why strong consistency
- Amazon cart (AP) - conflict resolution

**Decision Framework**:
- When to choose CP
- When to choose AP
- Hybrid approaches

---

### Week 5-6: Advanced Concepts

#### [07-distributed-systems/README.md](07-distributed-systems/README.md)
**Distributed Systems** (~12,500 words)
- ✅ 8 Fallacies of distributed computing
- ✅ Consensus algorithms (Raft, Paxos)
- ✅ Replication strategies (master-slave, master-master)
- ✅ Sharding (hash, range, consistent hashing)
- ✅ Distributed transactions (2PC, Saga pattern)

**Key Concepts**:
- Raft leader election process
- Consistent hashing with virtual nodes
- Cross-shard query challenges
- Handling celebrity users (hotspots)

**Code Examples**:
- Raft implementation overview
- Consistent hashing algorithm
- Two-phase commit
- Saga pattern with compensations

---

### Week 7-8: Real-World Systems

#### [10-classic-designs/URL-SHORTENER.md](10-classic-designs/URL-SHORTENER.md)
**URL Shortener - Complete Design** (~13,000 words)
- ✅ Requirements gathering
- ✅ Capacity estimation (100M URLs/month)
- ✅ API design
- ✅ Database schema
- ✅ 3 encoding strategies (Base62, Random, Hash)
- ✅ Caching strategy
- ✅ Analytics pipeline

**Includes**:
- Step-by-step walkthrough
- Code examples (Python)
- Architecture diagrams
- Trade-off discussions
- Scaling to 10x

---

#### [10-classic-designs/TWITTER.md](10-classic-designs/TWITTER.md)
**Twitter - Advanced Design** (~16,700 words)
- ✅ Full requirements (300M users)
- ✅ Capacity estimation (3,500 writes/sec, 173K reads/sec)
- ✅ Complete API design
- ✅ Database schema (SQL + Cassandra)
- ✅ Fan-out problem (hybrid approach)
- ✅ Timeline generation
- ✅ Snowflake ID generation

**Core Challenge**:
- Fan-out on write vs read
- Celebrity problem (100M followers)
- Hybrid solution explained

**Deep Dives**:
- Sharding strategy
- Redis data structures
- Caching invalidation
- Scaling to 1B users

---

### Interview Preparation

#### [12-interview-practice/FRAMEWORK.md](12-interview-practice/FRAMEWORK.md)
**Interview Framework** (~13,300 words)
- ✅ The 45-minute structure
- ✅ Phase-by-phase breakdown
- ✅ Template questions to ask
- ✅ Capacity estimation formulas
- ✅ API design patterns
- ✅ Common bottlenecks & solutions
- ✅ Sample dialogue

**The 6 Phases**:
1. Requirements (5 min) - Clarify scope
2. Estimation (5 min) - Back-of-envelope math
3. API & Data (5 min) - Define interfaces
4. High-Level (10 min) - Draw architecture
5. Deep Dive (15 min) - Solve bottlenecks
6. Wrap-up (5 min) - Scale & edge cases

**Includes**:
- Time management tips
- Common mistakes to avoid
- Mental checklist
- Practice routine

---

#### [12-interview-practice/PRACTICE-WORKBOOK.md](12-interview-practice/PRACTICE-WORKBOOK.md)
**Practice Workbook** (~13,400 words)
- ✅ 35+ practice problems organized by difficulty
- ✅ Week-by-week practice schedule
- ✅ Self-assessment rubric (0-35 points)
- ✅ Problem variations and extensions

**Problems by Difficulty**:

**Simple (⭐⭐)**:
- URL Shortener
- Pastebin

**Medium (⭐⭐⭐)**:
- Key-Value Store
- Rate Limiter
- Unique ID Generator
- Search Autocomplete
- Web Crawler

**Hard (⭐⭐⭐⭐)**:
- Chat Application (WhatsApp)
- Uber/Lyft
- Food Delivery (DoorDash)
- API Gateway
- Notification System

**Very Hard (⭐⭐⭐⭐⭐)**:
- Twitter (social graph, fan-out)
- Instagram (image storage, feed)
- YouTube (video streaming, CDN)
- TikTok (recommendations, ML)
- E-commerce (inventory, transactions)
- Payment System (exactly-once, reconciliation)
- Google Docs (collaborative editing)
- Dropbox (file sync, deduplication)

**Each Problem Includes**:
- Difficulty rating
- Key concepts tested
- Requirements
- Focus areas
- Success criteria

---

## 🎯 Quick Reference

### [CHEAT-SHEET.md](CHEAT-SHEET.md)
**One-Page Reference** (~8,000 words)

**Contains**:
- ✅ Key latency numbers
- ✅ Capacity estimation formulas
- ✅ Database selection guide
- ✅ Caching strategies quick ref
- ✅ Load balancing algorithms
- ✅ Sharding strategies
- ✅ CAP theorem summary
- ✅ Common patterns
- ✅ Bottlenecks & solutions
- ✅ Interview do's and don'ts

**Use Cases**:
- Quick review before interviews
- Reference during practice
- Memory refresh
- Pattern lookup

---

## 📊 Progress Tracking

### [PROGRESS-TRACKER.md](PROGRESS-TRACKER.md)
**Track Your Journey** (~9,000 words)

**Includes**:
- ✅ Daily checklists
- ✅ Weekly goals
- ✅ Concept mastery checkboxes (50+ concepts)
- ✅ Practice problems log (24+ problems)
- ✅ Skills self-assessment (16 skills)
- ✅ Mock interview log
- ✅ Final pre-interview checklist

**Milestones**:
- Week 2: Basic concepts mastered
- Week 4: Simple designs (25+/35 score)
- Week 8: Complex designs (30+/35 score)

---

## 📈 Learning Path

### Beginner Path (No Prior Experience)
```
Week 1-2: Focus on fundamentals
        └─> Complete 01-fundamentals thoroughly
        └─> Read 02-core-building-blocks
        └─> Design 2-3 simple systems

Week 3-4: Learn patterns
        └─> Study CAP-THEOREM.md deeply
        └─> Practice medium problems
        └─> Design 5+ systems

Week 5-6: Advanced concepts
        └─> Study distributed-systems
        └─> Design complex systems
        └─> Compare with solutions

Week 7-8: Interview prep
        └─> Follow PRACTICE-WORKBOOK.md
        └─> Do 3+ mock interviews
        └─> Review all concepts
```

### Intermediate Path (Some Experience)
```
Week 1: Quick fundamentals review
Week 2-3: Focus on gaps + patterns
Week 4-5: Advanced concepts
Week 6-8: Practice + mock interviews
```

### Advanced Path (Refresh for Interviews)
```
Week 1: Review CHEAT-SHEET.md + fundamentals
Week 2-3: Practice 10+ designs
Week 4: Mock interviews + refinement
```

---

## 🎓 Concepts Coverage

### Fundamentals ✓
- Scalability (vertical, horizontal)
- Reliability (availability, fault tolerance)
- Maintainability
- Trade-offs
- Capacity estimation

### Building Blocks ✓
- Load Balancers
- Caching
- Databases (SQL, NoSQL)
- Message Queues
- CDN
- Blob Storage

### Patterns ✓
- Replication
- Sharding
- Consistent Hashing
- Fan-out (push/pull/hybrid)
- Circuit Breaker
- Rate Limiting

### Advanced ✓
- CAP Theorem
- Consensus (Raft, Paxos)
- Distributed Transactions
- Conflict Resolution
- Observability
- Security

### Real Systems ✓
- URL Shortener
- Twitter
- Instagram
- YouTube
- Uber
- Chat Apps
- E-commerce
- Payment Systems

---

## 💡 Best Practices

### Daily Study (2-3 hours)
```
✅ 30 min: Study new concept
✅ 30 min: Practice problem (timed)
✅ 30 min: Review & diagrams
✅ 30 min: Code implementation
✅ 30 min: Spaced repetition
```

### Weekly Review
```
✅ Review all concepts from week
✅ Redo 1-2 struggling problems
✅ Update progress tracker
✅ Identify knowledge gaps
```

### Before Interview
```
✅ Review CHEAT-SHEET.md
✅ Practice 1 design (confidence)
✅ Get good sleep
✅ Stay calm
```

---

## 📦 What's Included

**Total Files**: 13 comprehensive guides
**Total Words**: ~21,000 words
**Total Pages**: ~150 pages (estimated)

**Content Breakdown**:
- 📘 Fundamentals: 8,500 words
- 📘 Building Blocks: 12,000 words
- 📘 Data Patterns: 10,000 words
- 📘 Distributed Systems: 12,500 words
- 📘 URL Shortener: 13,000 words
- 📘 Twitter Design: 16,700 words
- 📘 Interview Framework: 13,300 words
- 📘 Practice Workbook: 13,400 words
- 📘 Cheat Sheet: 8,000 words
- 📘 Getting Started: 11,200 words
- 📘 Progress Tracker: 9,000 words

**Plus**:
- Architecture diagrams
- Code examples (Python)
- Calculation templates
- Decision frameworks
- Self-assessment rubrics

---

## 🚀 Quick Start

**Right now** (5 minutes):
1. ✅ Read [OVERVIEW.md](OVERVIEW.md)
2. ⬜ Bookmark [CHEAT-SHEET.md](CHEAT-SHEET.md)
3. ⬜ Open [GETTING-STARTED.md](GETTING-STARTED.md)

**Today** (2 hours):
1. ⬜ Read [01-fundamentals/README.md](01-fundamentals/README.md)
2. ⬜ Do first capacity estimation exercise
3. ⬜ Setup [PROGRESS-TRACKER.md](PROGRESS-TRACKER.md)

**This Week**:
1. ⬜ Complete fundamentals
2. ⬜ Design URL Shortener
3. ⬜ Review your design

---

## 🎯 Success Metrics

### After Week 2
- Can explain scalability
- Can estimate capacity
- Can draw 3-tier architecture
- Score: 20+/35 on simple designs

### After Week 4
- Can design URL shortener in 30 min
- Can explain CAP theorem
- Can choose database types
- Score: 25+/35 on medium designs

### After Week 8
- Can design complex systems (Twitter, Uber)
- Can handle curveball questions
- Can explain all trade-offs
- Score: 30+/35 consistently
- **Interview Ready! 🎉**

---

## 📚 Additional Materials Needed

**To Complete Program**:
- Whiteboard or paper
- Timer (for 45-min practice)
- Study buddy (for mocks)
- Motivation!

**Recommended Reading**:
- "Designing Data-Intensive Applications" - Martin Kleppmann
- "System Design Interview" - Alex Xu
- Engineering blogs (Netflix, Uber, Airbnb)

---

## 🏆 Completion Certificate

After finishing this program, you will have:
- ✅ Studied 50+ concepts
- ✅ Designed 20+ systems
- ✅ Practiced 35+ problems
- ✅ Completed 3+ mock interviews
- ✅ Built a portfolio of designs

**You'll be ready for**:
- MAANG system design interviews
- Staff+ level discussions
- Architecture decisions
- Technical leadership

---

## 📞 Support

**Questions?**
1. Re-read relevant section
2. Check [GETTING-STARTED.md](GETTING-STARTED.md)
3. Review [CHEAT-SHEET.md](CHEAT-SHEET.md)
4. Find study buddy
5. Join online communities

---

## 🎉 Ready to Start?

**Your journey begins here:**

➡️ Open [GETTING-STARTED.md](GETTING-STARTED.md) now!

---

*Built with ❤️ for aspiring Staff+ engineers*

**"System design is a skill, not knowledge. Practice beats theory."**

