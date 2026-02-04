# Phase 4: Advanced System Designs

## Overview

Real-world complex system designs that combine all previous concepts.

**Duration**: Week 8 (Days 50-56)  
**Prerequisites**: All previous modules + classic designs  
**Goal**: Master complex, production-grade system designs

---

## Design Index

This module contains complete, end-to-end designs for complex systems. Each design follows the interview framework and includes:

- ✅ Complete requirements gathering
- ✅ Detailed capacity estimation
- ✅ API design
- ✅ Database schema
- ✅ High-level and detailed architecture
- ✅ Deep dives into challenges
- ✅ Scaling strategies
- ✅ Trade-off discussions

---

## Available Designs

### Social Media & Content

1. **[Twitter](../10-classic-designs/TWITTER.md)** ⭐⭐⭐⭐⭐ (Already covered)
   - Fan-out patterns (push, pull, hybrid)
   - Timeline generation
   - Snowflake ID generation
   - 300M users, billions of tweets

2. **Instagram** ⭐⭐⭐⭐⭐
   - Image storage and CDN
   - Feed ranking algorithm
   - Stories (ephemeral content)
   - 500M DAU

3. **TikTok** ⭐⭐⭐⭐⭐
   - Video processing pipeline
   - Recommendation algorithm
   - Infinite scroll feed
   - 1B users

4. **Facebook News Feed** ⭐⭐⭐⭐⭐
   - Personalized ranking
   - Multi-media content
   - Real-time updates
   - 2B users

---

### Video & Streaming

5. **YouTube** ⭐⭐⭐⭐⭐
   - Video upload and processing
   - Adaptive bitrate streaming
   - CDN distribution
   - Search and recommendations

6. **Netflix** ⭐⭐⭐⭐⭐
   - Content delivery network
   - Recommendation engine
   - Viewing analytics
   - Global distribution

7. **Twitch (Live Streaming)** ⭐⭐⭐⭐⭐
   - Real-time video streaming
   - Chat at scale
   - Stream quality adaptation
   - Low latency

---

### Marketplace & E-commerce

8. **Amazon** ⭐⭐⭐⭐⭐
   - Product catalog (billions of items)
   - Shopping cart
   - Checkout and payment
   - Order fulfillment
   - Inventory management

9. **Uber** ⭐⭐⭐⭐⭐
   - Real-time matching (riders & drivers)
   - Geo-spatial indexing (QuadTree)
   - ETA calculation
   - Surge pricing
   - Trip tracking

10. **Airbnb** ⭐⭐⭐⭐⭐
    - Search with geo-filtering
    - Booking system
    - Payment processing
    - Review system
    - Calendar availability

---

### Communication

11. **WhatsApp/Messenger** ⭐⭐⭐⭐⭐
    - 1-on-1 and group chat
    - Message delivery guarantees
    - End-to-end encryption
    - Presence (online/offline)
    - 2B users

12. **Slack** ⭐⭐⭐⭐⭐
    - Channels and DMs
    - Message search
    - File sharing
    - Integrations
    - Real-time collaboration

13. **Zoom** ⭐⭐⭐⭐⭐
    - Video conferencing
    - Screen sharing
    - Recording
    - WebRTC infrastructure

---

### Collaboration

14. **Google Docs** ⭐⭐⭐⭐⭐
    - Collaborative editing
    - Operational Transformation (OT)
    - Conflict resolution
    - Version history
    - Real-time presence

15. **Dropbox** ⭐⭐⭐⭐⭐
    - File sync across devices
    - Chunking and deduplication
    - Conflict resolution
    - Sharing and permissions
    - Delta sync

16. **Notion** ⭐⭐⭐⭐⭐
    - Block-based editor
    - Real-time collaboration
    - Databases and views
    - Permissions

---

### Infrastructure & Platform

17. **Distributed Cache (Redis)** ⭐⭐⭐⭐⭐
    - Consistent hashing
    - Replication
    - Persistence
    - Cluster management

18. **Message Queue (Kafka)** ⭐⭐⭐⭐⭐
    - Partitioning
    - Consumer groups
    - Exactly-once semantics
    - Durability

19. **API Gateway** ⭐⭐⭐⭐
    - Routing
    - Authentication
    - Rate limiting
    - Request aggregation

20. **Service Mesh** ⭐⭐⭐⭐⭐
    - Service discovery
    - Load balancing
    - Circuit breaking
    - Observability

---

### Financial

21. **Payment System (Stripe/PayPal)** ⭐⭐⭐⭐⭐
    - Payment processing
    - Idempotency
    - Reconciliation
    - Fraud detection
    - Multi-currency

22. **Stock Trading Platform** ⭐⭐⭐⭐⭐
    - Order matching engine
    - Real-time pricing
    - Settlement
    - Market data distribution

---

### Search & Discovery

23. **Google Search** ⭐⭐⭐⭐⭐
    - Web crawler
    - Inverted index
    - PageRank
    - Query processing
    - Result ranking

24. **Yelp** ⭐⭐⭐⭐⭐
    - Geo-spatial search
    - Review aggregation
    - Ranking algorithm
    - Real-time updates

---

## Design Template

Each design follows this structure:

### 1. Problem Statement (2 min)
- What to build
- Functional requirements
- Non-functional requirements
- Out of scope

### 2. Capacity Estimation (5 min)
- Traffic (QPS)
- Storage (GB/TB)
- Bandwidth (MB/s)
- Cache size

### 3. API Design (5 min)
- Core endpoints
- Request/response formats
- Error handling

### 4. Database Schema (5 min)
- Tables/collections
- Indexes
- Relationships

### 5. High-Level Design (10 min)
- Component diagram
- Data flow
- Technology choices

### 6. Detailed Design (15 min)
- Core algorithms
- Challenges and solutions
- Scaling strategies
- Trade-offs

### 7. Deep Dives (varies)
- Specific component internals
- Performance optimization
- Reliability patterns

### 8. Extensions (5 min)
- Additional features
- Scaling to 10x
- Monitoring

---

## How to Use This Module

### Week 8 Study Plan

**Day 50**: Instagram
- Study the design
- Draw architecture from memory
- Identify 3 key challenges
- Practice in 45 minutes

**Day 51**: YouTube
- Complete design exercise
- Compare with reference
- Note what you missed

**Day 52**: Uber
- Focus on geo-spatial indexing
- Understand matching algorithm
- Practice estimations

**Day 53**: WhatsApp
- Message delivery guarantees
- Scaling WebSocket connections
- Encryption

**Day 54**: Google Docs
- Operational Transformation
- Conflict resolution
- Real-time collaboration

**Day 55**: Payment System
- Idempotency
- Reconciliation
- Distributed transactions

**Day 56**: Review & Practice
- Pick 2 designs randomly
- Practice under time pressure
- Self-assess

---

## Practice Approach

### For Each Design:

**Step 1: Read** (30 min)
- Read the complete design
- Take notes on key concepts
- Understand trade-offs

**Step 2: Recreate** (45 min)
- Close the reference
- Design from scratch
- Time yourself (45 min)
- Draw all diagrams

**Step 3: Compare** (15 min)
- Compare with reference
- Identify gaps
- Note what you missed

**Step 4: Iterate** (varies)
- Focus on weak areas
- Re-design specific components
- Practice explaining out loud

---

## Common Patterns Across Designs

### Pattern 1: Fan-Out
**Used in**: Twitter, Instagram, Facebook
- Pre-compute vs on-demand
- Hybrid approaches
- Celebrity problem

### Pattern 2: Geo-Spatial Indexing
**Used in**: Uber, Airbnb, Yelp
- QuadTree
- Geohashing
- Proximity search

### Pattern 3: Real-Time Collaboration
**Used in**: Google Docs, Notion, Figma
- Operational Transformation
- CRDTs
- Conflict resolution

### Pattern 4: Video Processing
**Used in**: YouTube, Netflix, TikTok
- Encoding pipeline
- Adaptive bitrate
- CDN distribution

### Pattern 5: Distributed Consensus
**Used in**: Payment systems, distributed databases
- Two-phase commit
- Saga pattern
- Idempotency

---

## Interview-Specific Tips

### When Asked to Design [System]

**1. Clarify Scope** (Don't assume!)
```
"Should I focus on [specific feature]?"
"Are we designing for mobile, web, or both?"
"What scale are we targeting?"
"Any specific constraints?"
```

**2. State Assumptions**
```
"I'm assuming [assumption]. Is that correct?"
"For estimation, I'll use [numbers]. Does that sound right?"
```

**3. Think Out Loud**
```
"I'm thinking about two approaches..."
"The trade-off here is..."
"Let me weigh the pros and cons..."
```

**4. Ask for Direction**
```
"Would you like me to dive deeper into [component]?"
"Is there a specific area you'd like to focus on?"
```

**5. Manage Time**
```
"I'm going to spend 5 minutes on high-level design"
"Let me quickly cover the API before diving into architecture"
```

---

## Advanced Challenges

### Challenge 1: Combine Designs
Design a system that combines:
- Twitter (social graph)
- YouTube (video hosting)
- Slack (real-time chat)

**Hint**: Think about shared infrastructure, data consistency, scalability

### Challenge 2: Migration
You have an existing monolithic Twitter (1M users). Design migration to microservices for 100M users.

**Hint**: Incremental migration, strangler pattern, data consistency

### Challenge 3: Multi-Region
Design Netflix for global deployment (US, EU, Asia). Handle:
- Content licensing (geo-restrictions)
- Data sovereignty (GDPR)
- Low latency
- Disaster recovery

---

## Key Learnings

After completing this module, you should:

1. **Recognize Patterns**: See similar challenges across different systems
2. **Think Trade-offs**: Every decision has pros and cons
3. **Scale Progressively**: Start simple, add complexity as needed
4. **Communicate Clearly**: Explain your reasoning
5. **Handle Ambiguity**: Ask questions, state assumptions

---

## Next Steps

**After Advanced Designs**:

1. **Mock Interviews** (Week 8+)
   - Schedule with peers
   - Use [interview-practice](../12-interview-practice/) module
   - Get feedback

2. **Read Engineering Blogs**
   - Netflix Tech Blog
   - Uber Engineering
   - Airbnb Engineering
   - Facebook Engineering

3. **Build Side Projects**
   - Implement mini versions
   - Learn by doing
   - Add to portfolio

4. **Stay Current**
   - New technologies
   - Architecture trends
   - Best practices

---

## Design Complexity Matrix

| System | Scale | Complexity | Key Challenge |
|--------|-------|------------|---------------|
| Twitter | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Fan-out |
| Instagram | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Image storage |
| YouTube | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Video encoding |
| Uber | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Geo-matching |
| WhatsApp | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Message delivery |
| Google Docs | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Collaboration |
| Amazon | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Inventory |
| Payment System | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Exactly-once |

---

## Resources

**Engineering Blogs**:
- [Netflix Tech Blog](https://netflixtechblog.com)
- [Uber Engineering](https://eng.uber.com)
- [Airbnb Engineering](https://airbnb.io)
- [Instagram Engineering](https://instagram-engineering.com)
- [Dropbox Tech Blog](https://dropbox.tech)

**Papers**:
- Google: Bigtable, MapReduce, Spanner
- Amazon: Dynamo
- Facebook: TAO, Memcache

**Books**:
- "Designing Data-Intensive Applications" - Martin Kleppmann
- "System Design Interview" Vol 1 & 2 - Alex Xu

---

## Self-Assessment

After this module:

- [ ] Designed 5+ advanced systems
- [ ] Can handle complex requirements
- [ ] Understand trade-offs deeply
- [ ] Can scale from 1K to 1B users
- [ ] Comfortable with 45-minute time limit
- [ ] Ready for Staff+ interviews

**If yes to all**: You're ready! 🎉

**If no**: Focus on gaps, practice more, iterate.

---

**Note**: Detailed designs for each system will be added progressively. Start with Twitter (already complete) and work through the list.

---

**Remember**: You won't be asked to design these systems perfectly. The goal is to demonstrate your thought process, communication, and ability to make trade-offs.

**Good luck with your interviews! 🚀**
