# CAP Theorem & Data Consistency

## The CAP Theorem

### Core Concept

In a distributed system with network partitions, you can only guarantee 2 of 3:

```
            Consistency
                 ▲
                ╱ ╲
               ╱   ╲
              ╱ CP  ╲
             ╱       ╲
            ╱    △    ╲
           ╱  Partition ╲
          ╱   Tolerance  ╲
         ╱───────────────╲
    Availability      Partition
                      Tolerance
```

### Definitions

**Consistency (C)**
- Every read gets the most recent write
- All nodes see the same data at the same time
- Single, up-to-date copy of data

**Availability (A)**
- Every request gets a response (success or failure)
- No guarantees about data recency
- System operational even if nodes are down

**Partition Tolerance (P)**
- System continues despite network splits
- Nodes can't communicate with each other
- **Reality: Network partitions WILL happen**

### Why Only 2 of 3?

**During a Network Partition:**

```
        Network Partition
              ↓
    Node A  ╳╳╳╳╳  Node B
    [V1]            [V2]

Choice 1 (CP): Reject writes → Consistency maintained, Availability lost
Choice 2 (AP): Accept both writes → Available, Consistency lost
```

**You MUST choose**: Consistent or Available during partition.

---

## System Classifications

### CP Systems (Consistency + Partition Tolerance)

**Characteristics**:
- Returns errors during partitions
- Waits for consensus before responding
- Prioritizes correctness over availability

**Examples**:
- **MongoDB** (default): Stops accepting writes if can't reach majority
- **HBase**: Waits for ZooKeeper consensus
- **Redis Cluster**: Returns errors if can't reach master
- **Banking systems**: Cannot show wrong balance

**Use Cases**:
- Financial transactions
- Inventory management
- Systems where stale data is unacceptable

**Trade-off**: Better to show error than wrong data

```python
# CP Example
def transfer_money(from_account, to_account, amount):
    # Requires strong consistency
    with distributed_lock():
        if not can_reach_majority_nodes():
            raise ServiceUnavailable("System unavailable")
        
        # Atomic operation across nodes
        from_balance = get_balance(from_account)
        if from_balance >= amount:
            deduct(from_account, amount)
            add(to_account, amount)
        else:
            raise InsufficientFunds()
```

---

### AP Systems (Availability + Partition Tolerance)

**Characteristics**:
- Always accepts reads/writes
- May return stale data
- Eventually converges to consistency

**Examples**:
- **Cassandra**: Accepts writes even with node failures
- **DynamoDB**: Available even during partitions
- **Riak**: Highly available distributed database
- **DNS**: Serves potentially stale records

**Use Cases**:
- Social media posts
- Shopping cart
- Session storage
- Analytics/logging

**Trade-off**: Better to show stale data than no data

```python
# AP Example
def add_to_cart(user_id, item_id):
    # Always available, eventually consistent
    try:
        # Write to local node immediately
        local_node.write(f"cart:{user_id}", item_id)
        
        # Async replication to other nodes
        async_replicate(f"cart:{user_id}", item_id)
        
        return success()
    except:
        # Even on failures, accept the write
        write_to_local_log(user_id, item_id)
        return success()  # Always available
```

---

### CA Systems (Consistency + Availability)

**Reality**: Cannot exist in distributed systems with partitions.

**Why?**: Network partitions are inevitable in distributed systems.

**Examples of "CA"**:
- **Single-node databases** (PostgreSQL, MySQL on one server)
- **Traditional RDBMS** (before distributed)

**Note**: Once you distribute, you MUST handle partitions (P), so choose C or A.

---

## Consistency Models

### Strong Consistency

**Definition**: Read always returns most recent write.

```
Time →
T1: Write(X=1)     ✓
T2: Read(X)        → Returns 1 (guaranteed)
T3: Write(X=2)     ✓
T4: Read(X)        → Returns 2 (guaranteed)
```

**Implementation**: 
- Synchronous replication
- Quorum reads/writes
- Distributed locks

**Cost**: Higher latency, lower availability

**Examples**: Traditional SQL, Spanner, etcd

---

### Eventual Consistency

**Definition**: If no new updates, all replicas will eventually converge.

```
Time →
T1: Write(X=1) to Node A     ✓
T2: Read(X) from Node B      → Returns 0 (stale)
T3: Read(X) from Node B      → Returns 0 (still stale)
T4: Read(X) from Node B      → Returns 1 (converged)
```

**Characteristics**:
- Allows temporary inconsistency
- Asynchronous replication
- High availability

**Read-your-writes consistency**:
```python
def read_your_writes(user_id, key):
    # Route read to same node as write
    node = hash(user_id) % num_nodes
    return node.read(key)
```

**Examples**: DynamoDB, Cassandra, S3

---

### Causal Consistency

**Definition**: Causally related operations are seen in order.

```
Alice: Post "Going to movies"     (Event A)
Bob:   Read A, Reply "Have fun!"  (Event B, depends on A)

All users see: A before B (causal order)
But: Event C (unrelated) can appear anywhere
```

**Use Case**: Social media, collaborative editing

---

## Quorum-Based Replication

### The Formula

```
N = Total replicas
W = Write quorum (nodes that must acknowledge write)
R = Read quorum (nodes queried for read)

Strong Consistency requires: W + R > N

Example with N=3:
- W=2, R=2 → 2+2 > 3 ✓ (Strong consistency)
- W=1, R=1 → 1+1 < 3 ✗ (Eventual consistency)
```

### How It Works

**Write Process (W=2, N=3)**:
```
Client writes X=1
    ↓
┌───────┐
│ Node1 │ ✓ Ack
├───────┤
│ Node2 │ ✓ Ack  ← Wait for 2 acks
├───────┤
│ Node3 │ (async)
└───────┘
   ↓
Success returned to client
```

**Read Process (R=2, N=3)**:
```
Client reads X
    ↓
Query 2 nodes:
Node1: X=1 (timestamp: T1)
Node2: X=1 (timestamp: T1)
    ↓
Return latest value: X=1
```

**Why W+R > N ensures consistency**:
- Write and read sets MUST overlap
- Read will see at least one up-to-date replica

---

## Real-World Examples

### Example 1: Facebook Posts (AP)

**Scenario**: User posts status update

**Design**:
```
User → Write to local datacenter (immediate)
    ↓
Async replication to other regions
    ↓
Friends may see post at different times (eventual)
```

**Why AP?**:
- Better to show feed quickly (even if slightly stale)
- Post won't disappear if network partitions
- Consistency not critical (it's social, not financial)

---

### Example 2: Banking Transfer (CP)

**Scenario**: Transfer $100 from Account A to Account B

**Design**:
```
1. Lock both accounts (distributed lock)
2. Check balance on majority of nodes
3. Perform transfer on majority
4. Release locks
5. Async replicate to remaining nodes

If can't reach majority:
    Return "Service unavailable"
    (Better than incorrect balance)
```

**Why CP?**:
- Cannot show incorrect balance
- Cannot double-spend
- Availability less critical than correctness

---

### Example 3: Amazon Shopping Cart (AP)

**Scenario**: User adds item to cart

**Design**:
```
Add to cart → Local node writes immediately
           → Async replicate
           → Eventual consistency

Conflict resolution:
- Multiple carts merged (union of items)
- Never lose items (add wins over remove)
```

**Why AP?**:
- Cart must always work (availability critical)
- Worst case: User sees duplicate items (can remove)
- Better than blocking "Add to cart" button

---

## Conflict Resolution Strategies

### Last Write Wins (LWW)

```
T1: Node A writes X=1 (timestamp: 100)
T2: Node B writes X=2 (timestamp: 101)

Resolution: X=2 (latest timestamp wins)

Problem: Lost update if clocks differ
```

**Use**: Cassandra, Riak (with vector clocks)

---

### Vector Clocks

```
Track version per node:

Node A: {A:1, B:0, C:0} → X=1
Node B: {A:1, B:1, C:0} → X=2  (descendent of A)

Node C: {A:0, B:0, C:1} → X=3  (concurrent with A,B)

Requires manual conflict resolution
```

---

### CRDTs (Conflict-free Replicated Data Types)

**Example: G-Counter (Grow-only counter)**
```
Node A: increment → {A:1, B:0}
Node B: increment → {A:0, B:1}

Merge: {A:1, B:1}
Total: 1+1 = 2 ✓ (mathematically correct)
```

**Types**:
- G-Counter (grow-only counter)
- PN-Counter (increment/decrement)
- G-Set (grow-only set)
- OR-Set (observed-remove set)

**Use**: Redis, Riak, collaborative editors

---

## Decision Framework

### Choose CP When:
- ✓ Data correctness is critical
- ✓ Can tolerate downtime during partitions
- ✓ Examples: banking, inventory, booking systems

### Choose AP When:
- ✓ Availability is critical
- ✓ Can tolerate stale reads
- ✓ Examples: social media, caching, shopping cart

### Hybrid Approaches

**Multi-level consistency**:
```
User writes → Strong consistency (CP)
Public reads → Eventual consistency (AP)

Example: Twitter
- Your timeline: CP (see your tweets immediately)
- Followers' timeline: AP (eventual delivery)
```

---

## Practical Exercise

### Exercise 1: Classify Systems

For each, choose CP or AP and explain:

1. Uber ride booking
2. Instagram likes counter
3. Stock trading platform
4. Email delivery
5. DNS resolution
6. Video streaming view count
7. Multi-player game leaderboard
8. Healthcare records

### Exercise 2: Design Trade-offs

Design consistency model for:

**System**: E-commerce platform

**Requirements**:
- Product inventory
- User reviews
- Shopping cart
- Order history

For each component:
- Choose consistency model
- Justify with CAP theorem
- Describe conflict resolution

---

## Key Takeaways

1. **CAP Theorem**: Choose 2 of 3 (but P is mandatory in distributed systems)
2. **CP**: Consistency over availability (banking, inventory)
3. **AP**: Availability over consistency (social media, caching)
4. **Quorum**: W + R > N for strong consistency
5. **Eventual Consistency**: High availability, temporary inconsistency
6. **Context Matters**: Different parts of system can have different consistency

---

*Remember*: There's no "best" choice. It depends on your use case. Think trade-offs!
