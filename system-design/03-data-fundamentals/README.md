# Phase 1: Data Fundamentals

## Overview

Deep dive into data storage, processing, and retrieval patterns that form the backbone of modern systems.

**Duration**: Week 2 (Days 8-14)  
**Prerequisites**: 01-fundamentals, 02-core-building-blocks  
**Goal**: Master data modeling, storage strategies, and data processing patterns

---

## Module 3.1: Data Modeling

### Relational Data Modeling

**Normalization Forms**

**1NF (First Normal Form)**
```
❌ Bad:
orders
├─ order_id: 1
├─ customer: "John"
└─ items: "Apple, Banana, Orange"  ← Multiple values in one column

✅ Good:
orders                  order_items
├─ order_id: 1         ├─ order_id: 1, item: "Apple"
├─ customer: "John"    ├─ order_id: 1, item: "Banana"
                       └─ order_id: 1, item: "Orange"
```

**2NF (Second Normal Form)**
```
Remove partial dependencies - all non-key columns depend on entire primary key

❌ Bad (partial dependency):
order_items (order_id, product_id, product_name, quantity)
└─ product_name depends only on product_id, not full key

✅ Good:
order_items (order_id, product_id, quantity)
products (product_id, product_name)
```

**3NF (Third Normal Form)**
```
Remove transitive dependencies

❌ Bad:
employees (emp_id, dept_id, dept_name)
└─ dept_name depends on dept_id (transitive)

✅ Good:
employees (emp_id, dept_id)
departments (dept_id, dept_name)
```

**When to Denormalize**

```python
# Normalized (multiple joins):
SELECT u.name, p.title, p.content
FROM users u
JOIN posts p ON u.user_id = p.user_id
WHERE u.user_id = 123;

# Denormalized (single query):
SELECT user_name, title, content
FROM posts_denormalized
WHERE user_id = 123;
```

**Trade-offs**:
- **Normalized**: Less storage, easier updates, slower reads
- **Denormalized**: More storage, complex updates, faster reads

---

### NoSQL Data Modeling

**Document Model (MongoDB)**

```javascript
// Embedded (denormalized)
{
  "_id": "user_123",
  "name": "John Doe",
  "posts": [
    {
      "post_id": "post_1",
      "title": "Hello World",
      "content": "...",
      "created_at": "2024-01-01"
    },
    {
      "post_id": "post_2",
      "title": "Second Post",
      "content": "...",
      "created_at": "2024-01-02"
    }
  ],
  "followers": [456, 789, 101]
}

// vs Referenced (normalized)
// Users Collection
{
  "_id": "user_123",
  "name": "John Doe"
}

// Posts Collection
{
  "_id": "post_1",
  "user_id": "user_123",
  "title": "Hello World",
  "content": "...",
  "created_at": "2024-01-01"
}
```

**When to Embed vs Reference**:

**Embed when**:
- Data accessed together
- 1-to-few relationships
- Data doesn't change often
- Example: User profile with address

**Reference when**:
- 1-to-many relationships
- Data accessed independently
- Data changes frequently
- Document size would exceed limits
- Example: User with thousands of posts

---

**Wide-Column Model (Cassandra)**

```
User Activity Table:
Row Key: user_id
Columns: activity:timestamp → event_data

user_123 → activity:2024-01-01:10:00 → "login"
         → activity:2024-01-01:10:05 → "view_page"
         → activity:2024-01-01:10:10 → "click_button"
         → activity:2024-01-02:09:00 → "login"

Query: "Get all activities for user_123 in date range"
→ Efficient: Single partition, range scan on columns
```

---

## Module 3.2: Database Indexing

### Index Types

**1. B-Tree Index (Default)**

```
Structure:
         [50]
        /    \
    [25]      [75]
    /  \      /  \
[10][30] [60][90]

Search: O(log n)
Insert: O(log n)
```

**When to use**:
- Range queries (`WHERE age BETWEEN 25 AND 35`)
- Sorting (`ORDER BY created_at`)
- Prefix matching (`WHERE name LIKE 'John%'`)

```sql
CREATE INDEX idx_created_at ON posts(created_at);

-- Efficient:
SELECT * FROM posts WHERE created_at > '2024-01-01';
SELECT * FROM posts ORDER BY created_at DESC LIMIT 10;
```

---

**2. Hash Index**

```
Hash Function:
user_id: 12345 → hash → slot 7 → [12345, data_pointer]
user_id: 67890 → hash → slot 3 → [67890, data_pointer]

Search: O(1) average
Range queries: Not supported
```

**When to use**:
- Exact match queries only
- High-performance key lookups
- Example: Session store, cache

```sql
-- PostgreSQL
CREATE INDEX idx_user_id ON sessions USING HASH (user_id);

-- Efficient:
SELECT * FROM sessions WHERE user_id = 12345;

-- NOT efficient (hash doesn't support range):
SELECT * FROM sessions WHERE user_id > 12345;
```

---

**3. Composite Index**

```sql
CREATE INDEX idx_user_created ON posts(user_id, created_at);

-- Queries that benefit:
✓ WHERE user_id = 123
✓ WHERE user_id = 123 AND created_at > '2024-01-01'
✓ WHERE user_id = 123 ORDER BY created_at

-- Queries that DON'T benefit:
✗ WHERE created_at > '2024-01-01'  (doesn't start with user_id)
```

**Index Column Order Matters!**
```
Index on (A, B, C):
✓ WHERE A = ?
✓ WHERE A = ? AND B = ?
✓ WHERE A = ? AND B = ? AND C = ?
✗ WHERE B = ?
✗ WHERE C = ?
```

---

**4. Full-Text Index**

```sql
CREATE FULLTEXT INDEX idx_content ON posts(content);

-- Search for words:
SELECT * FROM posts 
WHERE MATCH(content) AGAINST('system design distributed');

-- Supports:
- Word stemming (run, running, ran)
- Relevance ranking
- Boolean operators (AND, OR, NOT)
```

---

### Index Performance Considerations

**Index Selectivity**

```
High Selectivity (Good for indexing):
- user_id: 1 million unique values in 1 million rows
- email: Almost all unique

Low Selectivity (Bad for indexing):
- gender: Only 2-3 values
- is_active: Only 2 values (true/false)

Rule: Don't index columns with < 5% unique values
```

**Index Size**

```
Table: 1 million rows
Column: VARCHAR(50)
Index size: ~50 MB

Impact:
- Memory usage (index in RAM for speed)
- Disk space
- Write performance (must update index)
```

**Covering Index**

```sql
-- Index includes all needed columns
CREATE INDEX idx_user_email_name ON users(user_id, email, name);

-- Query can be satisfied entirely from index (no table lookup):
SELECT user_id, email, name 
FROM users 
WHERE user_id = 123;
```

---

## Module 3.3: Database Transactions

### ACID Properties

**Atomicity**
```python
def transfer_money(from_account, to_account, amount):
    # All or nothing
    begin_transaction()
    try:
        deduct(from_account, amount)  # Step 1
        add(to_account, amount)        # Step 2
        commit()  # Both succeed
    except:
        rollback()  # Both fail
```

**Consistency**
```python
# Database maintains invariants
# Invariant: account_balance >= 0

# Before transaction: A=100, B=50
transfer(A, B, 150)  # Would make A = -50
# Transaction rejected - maintains consistency
```

**Isolation**
```python
# Concurrent transactions don't interfere

Transaction 1: Read balance (100) → Deduct 50 → Write (50)
Transaction 2: Read balance (100) → Deduct 30 → Write (70)

Without isolation: Both read 100, final balance could be 70 (lost update!)
With isolation: Transactions serialize, final balance = 20 ✓
```

**Durability**
```python
# Committed data survives crashes

commit()  # Data written to disk
<system crashes>
<system restarts>
# Data still there ✓
```

---

### Isolation Levels

**1. Read Uncommitted**
```
Problem: Dirty reads

T1: Write A = 10 (uncommitted)
T2: Read A = 10  ← Sees uncommitted data
T1: Rollback
T2: Has read data that never existed!
```

**2. Read Committed**
```
Solution: Only read committed data

T1: Write A = 10 (uncommitted)
T2: Read A = 5  ← Sees old committed value
T1: Commit
T2: Read A = 10  ← Now sees new value

Problem: Non-repeatable reads
```

**3. Repeatable Read**
```
Solution: Same reads within transaction

T1: Read A = 5
T2: Write A = 10, Commit
T1: Read A = 5  ← Still sees 5 (snapshot)

Problem: Phantom reads (new rows)
```

**4. Serializable**
```
Solution: Complete isolation (as if serial execution)

T1 and T2 execute as if one after the other

Cost: Lowest concurrency, highest latency
```

**Trade-off Table**:
| Level | Dirty Read | Non-Repeatable | Phantom | Performance |
|-------|-----------|----------------|---------|-------------|
| Read Uncommitted | Yes | Yes | Yes | Highest |
| Read Committed | No | Yes | Yes | High |
| Repeatable Read | No | No | Yes | Medium |
| Serializable | No | No | No | Lowest |

---

## Module 3.4: Data Processing Patterns

### Batch Processing

**Use Cases**:
- Daily report generation
- Log aggregation
- ETL (Extract, Transform, Load)
- Machine learning training

**Example: MapReduce**
```
Input: 1 TB of log files
Map: Extract relevant fields, emit key-value pairs
Shuffle: Group by key
Reduce: Aggregate values per key
Output: Summary statistics

Timeline: Hours to days
```

```python
# Word Count Example
def map(document):
    for word in document.split():
        emit(word, 1)

def reduce(word, counts):
    emit(word, sum(counts))

# Input: "hello world hello"
# Map: ("hello", 1), ("world", 1), ("hello", 1)
# Shuffle: "hello" → [1, 1], "world" → [1]
# Reduce: "hello" → 2, "world" → 1
```

---

### Stream Processing

**Use Cases**:
- Real-time analytics
- Fraud detection
- Live dashboards
- Event-driven actions

**Example: Apache Kafka Streams**
```python
# Process events as they arrive
stream = kafka.stream('user_clicks')

# Transform
stream.map(click => {
    'user_id': click.user_id,
    'timestamp': click.timestamp,
    'page': extract_page(click.url)
})

# Aggregate (windowed)
.window(tumbling=5_minutes)
.groupBy('page')
.count()

# Output: page view counts every 5 minutes
```

**Windowing Types**:
```
Tumbling Window (non-overlapping):
[0-5min] [5-10min] [10-15min]

Sliding Window (overlapping):
[0-5min]
  [1-6min]
    [2-7min]

Session Window (activity-based):
[active] [gap] [active] [gap] [active]
```

---

### Lambda Architecture

**Problem**: Need both batch (complete, accurate) and stream (real-time, approximate)

**Solution**: Run both in parallel
```
            Raw Data
           /        \
    Batch Layer   Speed Layer
    (complete)    (recent)
          \        /
        Serving Layer
        (merge both)
```

**Example: View Counter**
```
Batch: Count all views from yesterday (accurate)
Speed: Count views in last hour (real-time)
Serving: total = batch_count + speed_count

Query "views in last 24 hours":
= batch_views(yesterday) + stream_views(today)
```

---

### Kappa Architecture (Simplified)

**Idea**: Everything is a stream (no separate batch layer)
```
         Raw Events
              ↓
       Stream Processor
              ↓
         Databases
         (multiple views)
```

**Reprocessing**:
```python
# To fix bug or change logic:
# 1. Deploy new version of stream processor
# 2. Replay events from beginning
# 3. Generate new output

kafka.seek_to_beginning()
for event in kafka.stream():
    process_with_new_logic(event)
```

---

## Module 3.5: Data Partitioning Strategies

### Horizontal Partitioning (Sharding)

Already covered in depth in [07-distributed-systems](../07-distributed-systems/README.md)

Quick recap:
- Hash-based: Even distribution
- Range-based: Easy range queries, hotspots risk
- Consistent hashing: Easy rebalancing

---

### Vertical Partitioning

**Split table by columns**:

```sql
-- Original (wide table):
users (id, name, email, profile_picture, bio, preferences, settings)

-- Split:
users_core (id, name, email)              -- Frequently accessed
users_profile (id, profile_picture, bio)  -- Occasionally accessed
users_settings (id, preferences, settings) -- Rarely accessed

Benefits:
- Smaller row size → more rows in memory
- Faster queries for common columns
- Different storage for different columns (SSD vs HDD)
```

---

### Time-Based Partitioning

```sql
-- Partition by month
CREATE TABLE logs_2024_01 (...);
CREATE TABLE logs_2024_02 (...);
CREATE TABLE logs_2024_03 (...);

-- Query specific month:
SELECT * FROM logs_2024_01 WHERE ...;

-- Query range (partition pruning):
SELECT * FROM logs_2024_* WHERE date BETWEEN '2024-01-01' AND '2024-03-31';
```

**Benefits**:
- Easy to drop old partitions (delete old data)
- Query pruning (only scan relevant partitions)
- Parallel queries across partitions

**Use cases**:
- Log data
- Time-series data
- Metrics and events

---

## Practical Exercises

### Exercise 1: Data Modeling

Design schema for a blog platform:
- Users, posts, comments, tags
- Posts can have multiple tags
- Users can follow other users
- Design both SQL and MongoDB versions

Questions:
- What to normalize/denormalize?
- What indexes to create?
- How to handle high-traffic celebrity users?

---

### Exercise 2: Index Design

Given table:
```sql
CREATE TABLE orders (
    order_id BIGINT,
    user_id BIGINT,
    product_id BIGINT,
    status VARCHAR(20),
    created_at TIMESTAMP,
    total_amount DECIMAL(10,2)
);
```

Design indexes for:
1. Get user's orders: `SELECT * FROM orders WHERE user_id = ?`
2. Get recent orders: `SELECT * FROM orders ORDER BY created_at DESC LIMIT 10`
3. Get pending orders for user: `WHERE user_id = ? AND status = 'pending'`
4. Get orders in date range: `WHERE created_at BETWEEN ? AND ?`

What indexes would you create? Why?

---

### Exercise 3: Batch vs Stream

For each scenario, choose batch or stream processing:
1. Calculate daily revenue report
2. Detect credit card fraud in real-time
3. Train recommendation model on all user history
4. Show trending topics in last 5 minutes
5. Generate monthly email newsletter

Justify each choice.

---

## Key Takeaways

1. **Data Modeling**: Normalized for writes, denormalized for reads
2. **Indexes**: Essential for performance, but cost storage and writes
3. **Transactions**: ACID guarantees, understand isolation levels
4. **Batch vs Stream**: Complete/accurate vs real-time/approximate
5. **Partitioning**: Horizontal (sharding), vertical (columns), time-based

---

## Self-Assessment

- [ ] Can design normalized and denormalized schemas
- [ ] Can choose appropriate indexes for queries
- [ ] Understand ACID and isolation levels
- [ ] Can explain batch vs stream processing
- [ ] Can choose partitioning strategy for given use case
- [ ] Can estimate index size and impact

---

## Next Module

[04-scaling-patterns](../04-scaling-patterns/README.md) - Learn how to scale systems horizontally and vertically

---

**Remember**: The right data model makes everything else easier. Take time to design it well!
