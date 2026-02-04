# Distributed Systems Fundamentals

## Overview

Understanding distributed systems is crucial for designing scalable, reliable systems. This module covers core concepts you MUST know.

---

## Module 7.1: Distributed System Challenges

### The Fallacies of Distributed Computing

**The 8 assumptions that will break your system:**

1. **The network is reliable**
   - Reality: Networks fail, packets drop, connections timeout
   - Solution: Retries, timeouts, circuit breakers

2. **Latency is zero**
   - Reality: Network calls are 100,000x slower than memory access
   - Solution: Caching, async processing, local computation

3. **Bandwidth is infinite**
   - Reality: Limited bandwidth, especially cross-region
   - Solution: Data compression, pagination, CDN

4. **The network is secure**
   - Reality: MITM attacks, packet sniffing
   - Solution: TLS/SSL, authentication, encryption

5. **Topology doesn't change**
   - Reality: Servers restart, scale up/down, fail
   - Solution: Service discovery, health checks, load balancing

6. **There is one administrator**
   - Reality: Multiple teams, cloud providers, regions
   - Solution: Clear ownership, monitoring, communication

7. **Transport cost is zero**
   - Reality: Cross-region data transfer is expensive
   - Solution: Smart routing, data locality, caching

8. **The network is homogeneous**
   - Reality: Mix of protocols, versions, vendors
   - Solution: Standard protocols, versioning, compatibility layers

---

## Module 7.2: Consensus Algorithms

### The Problem

**How do distributed nodes agree on a value when:**
- Some nodes may fail
- Messages may be delayed or lost
- No global clock exists

### Raft Consensus (Simplified)

**Roles:**
```
Leader:    Handles all writes, coordinates replication
Follower:  Passive, accepts commands from leader
Candidate: Follower trying to become leader
```

**Leader Election:**
```
1. Start: All nodes are followers
2. Timeout: No heartbeat from leader
3. Candidate: Follower becomes candidate, increments term
4. Vote Request: Sends request to all nodes
5. Vote: Nodes vote for first candidate (per term)
6. Majority: Candidate with >50% votes becomes leader
7. Heartbeat: Leader sends heartbeats to maintain authority
```

**Log Replication:**
```
Client → Leader: Write request
    ↓
Leader → Followers: Replicate log entry
    ↓
Followers: Append to log, send ACK
    ↓
Leader: Commit when majority ACKs
    ↓
Leader → Followers: Notify commit
    ↓
Followers: Apply to state machine
```

**Visual:**
```
Term 1:
Node A: [1]
Node B: [1]
Node C: [1]

Write X=5:
Node A (Leader): [1, X=5]  ← Client write
                   ↓ replicate
Node B: [1, X=5]  ✓ ACK
Node C: [1, X=5]  ✓ ACK
                   ↓ majority achieved
Node A commits X=5
```

**Properties:**
- ✓ Strong consistency
- ✓ Fault tolerance (majority survives)
- ✗ Requires majority (can't tolerate > 50% failure)

**Real Usage:**
- etcd (Kubernetes service discovery)
- Consul (service mesh)
- CockroachDB (distributed SQL)

---

### Paxos (Overview)

**Key Idea**: Two-phase protocol to agree on a value

**Phases:**
```
Phase 1: Prepare
  Proposer → Acceptors: "Can I propose value V with number N?"
  Acceptors → Proposer: "Yes" (if N > previous)

Phase 2: Accept
  Proposer → Acceptors: "Accept value V with number N"
  Acceptors → Learners: "Accepted V"
  
Learners: Once majority accepts, value is chosen
```

**Why Hard**: Complex to understand and implement correctly

**Real Usage**:
- Google Chubby (lock service)
- Apache ZooKeeper (coordination)

---

## Module 7.3: Replication

### Master-Slave Replication

```
         Writes
           ↓
      ┌────────┐
      │ Master │
      └────┬───┘
           │ Replication
      ┌────┼─────┐
      ↓    ↓     ↓
    ┌───┐┌───┐┌───┐
    │ S1││ S2││ S3│  ← Reads
    └───┘└───┘└───┘
```

**Characteristics:**
- All writes go to master
- Reads distributed across replicas
- Async replication (eventual consistency)

**Pros:**
- Simple to implement
- High read scalability
- Clear separation of concerns

**Cons:**
- Single point of failure (master)
- Replication lag (stale reads possible)
- Master bottleneck for writes

**Replication Lag Handling:**
```python
def read_your_own_writes(user_id):
    # Read from master for short time after write
    last_write = get_last_write_time(user_id)
    
    if (now() - last_write) < 5_seconds:
        return master.read(user_id)
    else:
        return replica.read(user_id)
```

---

### Master-Master Replication

```
    ┌────────┐ ←─ Bi-directional ─→ ┌────────┐
    │Master 1│      Replication      │Master 2│
    └────────┘                       └────────┘
       ↓ ↑                              ↓ ↑
    Reads/Writes                    Reads/Writes
```

**Characteristics:**
- Both nodes accept writes
- Conflict resolution required
- Active-active setup

**Pros:**
- High availability
- Better write performance
- Load distribution

**Cons:**
- Complex conflict resolution
- Risk of inconsistency
- More complex to operate

**Conflict Resolution Strategies:**

**1. Last Write Wins (LWW)**
```python
def resolve_conflict(v1, v2):
    if v1.timestamp > v2.timestamp:
        return v1
    else:
        return v2

# Problem: Can lose writes if clocks skewed
```

**2. Application-level**
```python
def merge_shopping_carts(cart1, cart2):
    # Union of items (never lose items)
    return cart1.items + cart2.items
```

---

### Quorum-based Replication

**Formula**: `W + R > N`

Where:
- N = Total replicas
- W = Write quorum (must acknowledge)
- R = Read quorum (must query)

**Example: N=3, W=2, R=2**

```
Write X=5:
  Node A: X=5 ✓
  Node B: X=5 ✓  ← 2 ACKs, write successful
  Node C: pending

Read X:
  Query 2 nodes:
  Node A: X=5
  Node B: X=5
  Return: X=5 (consistent)
```

**Configuration Trade-offs:**

| N | W | R | Consistency | Availability | Use Case |
|---|---|---|-------------|--------------|----------|
| 3 | 3 | 1 | Strong | Low | Critical data |
| 3 | 2 | 2 | Strong | Medium | Balanced |
| 3 | 1 | 1 | Eventual | High | High availability |

---

## Module 7.4: Sharding (Partitioning)

### Why Shard?

**Problem**: Single database can't handle:
- Too much data (storage limit)
- Too many requests (CPU/memory limit)
- Too much throughput (I/O limit)

**Solution**: Distribute data across multiple databases

---

### Sharding Strategies

**1. Hash-based Sharding**

```python
def get_shard(user_id, num_shards):
    return hash(user_id) % num_shards

# Example:
user_123 → hash(123) % 4 = 3 → Shard 3
user_456 → hash(456) % 4 = 0 → Shard 0
```

**Pros:**
- Even distribution
- Simple implementation

**Cons:**
- Resharding is expensive (when adding shards)
- Related data might be split

**Rehashing Problem:**
```
Before (4 shards):
user_123 → 3 % 4 = 3 → Shard 3

After adding 1 shard (5 shards):
user_123 → 3 % 5 = 3 → Shard 3 (lucky, same shard)
user_456 → 0 % 5 = 0 → Shard 0 (was also 0, lucky)
user_789 → 9 % 5 = 4 → Shard 4 (was shard 1, MOVED!)

Problem: Most keys move to different shards
```

---

**2. Consistent Hashing**

```
        Shard Ring (0-360°)
            ___
         /       \
     S1 ●         ● S2
    90°  |       | 180°
         |       |
     S4 ●         ● S3
    270° \_____/ 360°

user_123 → hash → 45° → Falls between S1 and S2 → S2
user_456 → hash → 200° → Falls between S3 and S4 → S4
```

**Adding Shard S5 at 225°:**
```
Only keys between S3 (180°) and S5 (225°) move to S5
Other keys unaffected!
```

**Pros:**
- Minimal rehashing when adding/removing nodes
- Graceful scaling

**Cons:**
- Uneven distribution (use virtual nodes to fix)

**Virtual Nodes:**
```
Instead of 1 position per shard:
S1 → S1_1, S1_2, S1_3 (multiple positions)
S2 → S2_1, S2_2, S2_3

Better distribution across the ring
```

**Used by:**
- Amazon DynamoDB
- Apache Cassandra
- Riak

---

**3. Range-based Sharding**

```
Shard 1: user_id 0 - 999,999
Shard 2: user_id 1,000,000 - 1,999,999
Shard 3: user_id 2,000,000 - 2,999,999
```

**Pros:**
- Range queries easy (all data in one shard)
- Simple to understand

**Cons:**
- Hotspots (new users all go to latest shard)
- Uneven distribution

**Example**: Instagram sharding by creation time
- Recent photos (hot data) → One shard
- Old photos (cold data) → Other shards
- Imbalance!

---

**4. Directory-based Sharding**

```
Lookup Table:
user_123 → Shard A
user_456 → Shard B
user_789 → Shard A

Flexible mapping, no formula
```

**Pros:**
- Flexible placement
- Easy rebalancing

**Cons:**
- Extra lookup (latency)
- Lookup table is single point of failure
- Scalability of lookup table itself

---

### Sharding Challenges

**1. Cross-shard Queries**

```sql
-- User 123 is on Shard A
-- User 456 is on Shard B

-- This query requires 2 shards:
SELECT * FROM posts 
WHERE user_id IN (123, 456)
ORDER BY created_at DESC
LIMIT 10;

Solution:
1. Query both shards in parallel
2. Merge results
3. Sort and limit

(More shards = more complexity)
```

**2. Transactions Across Shards**

```python
# Transfer money between accounts on different shards
def transfer(from_account, to_account, amount):
    # from_account on Shard 1
    # to_account on Shard 2
    
    # Problem: How to ensure atomicity?
    
    # Solution: Two-phase commit (2PC)
    # 1. Prepare phase: Ask both shards if ready
    # 2. Commit phase: If all ready, commit both
```

**Two-Phase Commit (2PC):**
```
Coordinator → Shard 1: Prepare
Coordinator → Shard 2: Prepare
    ↓
Shard 1 → Coordinator: Ready
Shard 2 → Coordinator: Ready
    ↓
Coordinator → Shard 1: Commit
Coordinator → Shard 2: Commit
    ↓
Done
```

**Problems with 2PC:**
- Blocking (if coordinator fails)
- Performance overhead
- Not highly available

**Better**: Avoid cross-shard transactions!
- Design schema to keep related data together
- Use eventual consistency
- Saga pattern for long-running transactions

---

**3. Hotspots**

```
Problem: Celebrity with 100M followers

All reads for celebrity's tweets hit ONE shard
→ Shard overloaded while others idle
```

**Solutions:**
```
1. Replicate hot data across shards
2. Cache aggressively (Redis)
3. Special handling for celebrities
4. Use different sharding key for reads vs writes
```

---

## Module 7.5: Distributed Transactions

### ACID vs BASE

**ACID (Traditional databases)**
```
Atomicity:   All or nothing
Consistency: Valid state always
Isolation:   Transactions don't interfere
Durability:  Committed = persisted

Example: Bank transfer
```

**BASE (Distributed systems)**
```
Basically Available:    System works most of the time
Soft state:            State may change without input (replication)
Eventual consistency:  Will be consistent eventually

Example: Social media likes count
```

### Saga Pattern

**Problem**: Long-running transactions across services

**Solution**: Chain of local transactions with compensating actions

```
Order Saga:
1. Create Order     (Compensate: Cancel Order)
2. Reserve Inventory (Compensate: Release Inventory)
3. Charge Payment   (Compensate: Refund)
4. Ship Order       (Compensate: Return)

If step 3 fails:
→ Execute compensations: Release Inventory → Cancel Order
```

**Implementation:**
```python
def create_order_saga(order):
    try:
        # Step 1
        order_id = create_order(order)
        
        # Step 2
        reservation_id = reserve_inventory(order.items)
        
        # Step 3
        payment_id = charge_payment(order.amount)
        
        # Step 4
        shipment_id = ship_order(order_id)
        
        return success(order_id)
        
    except InventoryError:
        cancel_order(order_id)
        raise
        
    except PaymentError:
        release_inventory(reservation_id)
        cancel_order(order_id)
        raise
        
    except ShipmentError:
        refund_payment(payment_id)
        release_inventory(reservation_id)
        cancel_order(order_id)
        raise
```

---

## Key Takeaways

1. **Consensus**: Raft/Paxos for agreement in distributed systems
2. **Replication**: Trade-off between consistency and availability
3. **Sharding**: Distribute data, but adds complexity
4. **Transactions**: ACID for strong consistency, BASE for scale
5. **Failures**: Design for failure from day one

---

## Practice Problems

1. **Design a distributed counter** that can be incremented millions of times per second
2. **Design distributed caching** with consistent hashing
3. **Design a distributed lock service** using Raft
4. **Handle celebrity problem** in Twitter (1 user, 100M followers)

---

*Distributed systems are hard. Start simple, add complexity only when needed.*
