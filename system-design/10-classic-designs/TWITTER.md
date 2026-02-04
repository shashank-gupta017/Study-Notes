# Design Twitter (Complete Guide)

## Problem Statement

Design a simplified version of Twitter where users can post tweets, follow other users, and view their timeline.

**Similar To**: Facebook Feed, Instagram, Mastodon

---

## Step 1: Requirements Clarification

### Functional Requirements

**Core Features**:
1. Post tweets (text, 280 characters max)
2. Follow/unfollow users
3. View timeline (tweets from people you follow)
4. View user profile (user's tweets)

**Out of Scope** (mention these):
- Likes, retweets, replies
- Direct messages
- Trending topics
- Ads, analytics
- Media uploads (focus on text)

### Non-Functional Requirements

**Scale**:
- 300M monthly active users (MAU)
- 150M daily active users (DAU) - 50% of MAU
- Each user posts 2 tweets/day on average
- Each user reads 100 tweets/day

**Performance**:
- Timeline load: < 200ms
- Tweet post: < 500ms
- High availability: 99.9%

**Consistency**:
- Eventual consistency acceptable
- Can show stale tweets (few seconds old)

---

## Step 2: Capacity Estimation

### Traffic

```
WRITE (Posting Tweets):
- Daily tweets: 150M DAU × 2 tweets = 300M tweets/day
- Tweets per second: 300M / 86,400 ≈ 3,500 tweets/sec
- Peak (3x): 10,500 tweets/sec

READ (Timeline Views):
- Daily timeline loads: 150M DAU × 20 loads = 3B loads/day
- Daily tweet reads: 150M DAU × 100 tweets = 15B tweet views/day
- Read QPS: 15B / 86,400 ≈ 173,000 reads/sec

Read:Write Ratio = 173,000 / 3,500 ≈ 50:1 (Read-heavy)
```

### Storage

```
TWEET STORAGE:
- Average tweet size:
  - Text: 280 chars × 2 bytes = 560 bytes
  - Metadata (user_id, timestamp, etc.): 200 bytes
  - Total: ~760 bytes per tweet

- Daily storage: 300M tweets × 760 bytes = 228 GB/day
- 5-year storage: 228 GB × 365 × 5 ≈ 416 TB
- With replication (3x): 1.2 PB

USER & FOLLOW DATA:
- 300M users × 1 KB/user = 300 GB
- Follow relationships (avg 200 follows per user):
  - 300M users × 200 follows × 16 bytes = 960 GB
  - Total: ~1.3 TB (negligible compared to tweets)
```

### Bandwidth

```
WRITE:
- 228 GB/day / 86,400 sec = 2.6 MB/sec
- Peak: 7.8 MB/sec

READ:
- 15B reads × 760 bytes = 11.4 TB/day
- 11.4 TB / 86,400 = 132 MB/sec
- Peak: 396 MB/sec
```

### Cache

```
20% of users generate 80% of traffic (power law)

Cache daily active timelines:
- 150M users × 100 tweets × 760 bytes = 11.4 TB (full dataset)
- Cache 20% hot data: 2.3 TB
- Cache popular tweets separately: ~500 GB

Total cache needed: ~3 TB (distributed across Redis cluster)
```

---

## Step 3: API Design

### Core APIs

```http
# 1. Post Tweet
POST /api/v1/tweets
Authorization: Bearer {token}
Content-Type: application/json

Request:
{
  "userId": "123",
  "content": "Hello Twitter!",
  "timestamp": 1704326400
}

Response: 201 Created
{
  "tweetId": "789",
  "userId": "123",
  "content": "Hello Twitter!",
  "createdAt": "2024-01-04T00:00:00Z"
}

# 2. Get Timeline
GET /api/v1/timeline/{userId}?limit=20&cursor=xyz
Authorization: Bearer {token}

Response: 200 OK
{
  "tweets": [
    {
      "tweetId": "789",
      "userId": "456",
      "content": "Tweet content",
      "createdAt": "2024-01-04T00:00:00Z"
    },
    ...
  ],
  "nextCursor": "abc123"
}

# 3. Get User Tweets
GET /api/v1/users/{userId}/tweets?limit=20&cursor=xyz

Response: 200 OK
{
  "tweets": [...],
  "nextCursor": "def456"
}

# 4. Follow User
POST /api/v1/users/{userId}/follow
Authorization: Bearer {token}

Request:
{
  "followeeId": "456"
}

Response: 201 Created

# 5. Unfollow User
DELETE /api/v1/users/{userId}/follow/{followeeId}
Authorization: Bearer {token}

Response: 204 No Content
```

---

## Step 4: Database Schema

### SQL Schema (PostgreSQL)

```sql
-- Users Table
CREATE TABLE users (
    user_id BIGINT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    display_name VARCHAR(100),
    bio TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username)
);

-- Tweets Table (Sharded by user_id)
CREATE TABLE tweets (
    tweet_id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content VARCHAR(280) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_created (user_id, created_at DESC),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Followers Table (Who follows whom)
CREATE TABLE followers (
    follower_id BIGINT NOT NULL,     -- User who follows
    followee_id BIGINT NOT NULL,     -- User being followed
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (follower_id, followee_id),
    INDEX idx_follower (follower_id),
    INDEX idx_followee (followee_id)
);

-- Timelines Cache Table (Pre-computed timelines)
-- This is actually stored in Redis, shown here for clarity
```

### NoSQL Alternative (Cassandra)

```
-- Tweets by User (for user profile)
CREATE TABLE tweets_by_user (
    user_id BIGINT,
    tweet_id BIGINT,
    content TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY (user_id, created_at, tweet_id)
) WITH CLUSTERING ORDER BY (created_at DESC);

-- Timeline (pre-computed)
CREATE TABLE timeline (
    user_id BIGINT,
    tweet_id BIGINT,
    author_id BIGINT,
    content TEXT,
    created_at TIMESTAMP,
    PRIMARY KEY (user_id, created_at, tweet_id)
) WITH CLUSTERING ORDER BY (created_at DESC);

-- Followers
CREATE TABLE followers (
    followee_id BIGINT,
    follower_id BIGINT,
    created_at TIMESTAMP,
    PRIMARY KEY (followee_id, follower_id)
);

CREATE TABLE following (
    follower_id BIGINT,
    followee_id BIGINT,
    created_at TIMESTAMP,
    PRIMARY KEY (follower_id, followee_id)
);
```

---

## Step 5: High-Level Architecture

```
                    ┌─────────────┐
                    │   Users     │
                    └──────┬──────┘
                           │
                    ┌──────┴──────┐
                    │     CDN     │
                    │  (Static)   │
                    └──────┬──────┘
                           │
                    ┌──────┴──────────┐
                    │  Load Balancer  │
                    │   (Layer 7)     │
                    └──────┬──────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
   ┌────┴─────┐      ┌────┴─────┐      ┌────┴─────┐
   │   API    │      │   API    │      │   API    │
   │ Server 1 │      │ Server 2 │      │ Server N │
   └────┬─────┘      └────┬─────┘      └────┬─────┘
        │                  │                  │
        └──────────────────┼──────────────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
   ┌────┴─────┐      ┌────┴─────┐      ┌────┴─────┐
   │  Tweet   │      │ Timeline │      │   User   │
   │ Service  │      │ Service  │      │ Service  │
   └────┬─────┘      └────┬─────┘      └────┬─────┘
        │                  │                  │
   ┌────┴─────┐      ┌────┴─────┐      ┌────┴─────┐
   │ Tweet DB │      │ Timeline │      │ User DB  │
   │(Sharded) │      │  Cache   │      │          │
   └──────────┘      │ (Redis)  │      └──────────┘
                     └──────────┘
                           │
                     ┌─────┴──────┐
                     │   Kafka    │
                     │  (Events)  │
                     └─────┬──────┘
                           │
                     ┌─────┴──────┐
                     │  Fan-out   │
                     │  Workers   │
                     └────────────┘
```

---

## Step 6: Core Flows

### Flow 1: Post Tweet (Write Path)

```
1. User posts tweet via API
   ↓
2. API Server validates and authenticates
   ↓
3. Tweet Service:
   a. Generate tweet_id (Snowflake ID)
   b. Store tweet in database
   c. Publish event to Kafka: "TweetCreated"
   ↓
4. Return success to user (async fan-out happens in background)
   ↓
5. Fan-out Workers (subscribe to Kafka):
   For each follower of tweet author:
     a. Fetch followers from cache/DB
     b. Insert tweet into follower's timeline (Redis)
   ↓
6. Timeline cache updated (eventually consistent)
```

**Code Example**:
```python
def post_tweet(user_id, content):
    # 1. Validate
    if len(content) > 280:
        raise ValidationError("Tweet too long")
    
    # 2. Generate ID (Snowflake)
    tweet_id = snowflake.generate_id()
    
    # 3. Store in database
    db.tweets.insert({
        'tweet_id': tweet_id,
        'user_id': user_id,
        'content': content,
        'created_at': now()
    })
    
    # 4. Publish event for fan-out
    kafka.publish('tweet_created', {
        'tweet_id': tweet_id,
        'user_id': user_id,
        'content': content,
        'created_at': now()
    })
    
    # 5. Return immediately (don't wait for fan-out)
    return {'tweet_id': tweet_id, 'status': 'posted'}
```

---

### Flow 2: Get Timeline (Read Path)

```
1. User requests timeline via API
   ↓
2. Timeline Service checks Redis cache:
   Key: "timeline:{user_id}"
   ↓
3. If cache hit:
   a. Return cached timeline (sorted by timestamp)
   b. Update with any new tweets (merge with real-time)
   ↓
4. If cache miss (rare):
   a. Fetch user's following list
   b. Fetch recent tweets from all followees
   c. Merge and sort by timestamp
   d. Cache result in Redis
   e. Return timeline
   ↓
5. Return response to user
```

**Code Example**:
```python
def get_timeline(user_id, limit=20, cursor=None):
    # 1. Try cache first
    cache_key = f"timeline:{user_id}"
    timeline = redis.zrevrange(cache_key, 0, limit - 1, withscores=True)
    
    if timeline:
        # Cache hit - return cached timeline
        tweets = fetch_tweet_details([t[0] for t in timeline])
        return {
            'tweets': tweets,
            'next_cursor': timeline[-1][1] if len(timeline) == limit else None
        }
    
    # 2. Cache miss - build timeline (rare)
    # Fetch following list
    following = db.followers.query(
        "SELECT followee_id FROM followers WHERE follower_id = ?",
        user_id
    )
    
    # Fetch recent tweets from all followees
    tweet_ids = db.tweets.query(
        "SELECT tweet_id, created_at FROM tweets WHERE user_id IN ? ORDER BY created_at DESC LIMIT ?",
        following, limit
    )
    
    # Cache for next time
    for tweet_id, created_at in tweet_ids:
        redis.zadd(cache_key, {tweet_id: created_at.timestamp()})
    redis.expire(cache_key, 3600)  # 1 hour TTL
    
    # Return timeline
    tweets = fetch_tweet_details([t[0] for t in tweet_ids])
    return {'tweets': tweets}
```

---

## Step 7: The Core Challenge - Fan-Out Problem

### The Problem

When user with 10M followers tweets:
- Fan-out on write: Write to 10M timelines (slow!)
- Fan-out on read: Read from 10M users' tweets (slow!)

### Solution: Hybrid Approach

**Strategy**:
1. **Regular users** (< 100K followers): Fan-out on write
2. **Celebrities** (> 100K followers): Fan-out on read
3. **Timeline merge**: Combine pre-computed + real-time

```python
# Fan-out Worker
def fanout_tweet(tweet):
    author = tweet['user_id']
    
    # Get follower count
    follower_count = get_follower_count(author)
    
    if follower_count < 100_000:
        # Fan-out on write (push to all followers' timelines)
        followers = get_followers(author)
        for follower_id in followers:
            redis.zadd(
                f"timeline:{follower_id}",
                {tweet['tweet_id']: tweet['created_at']}
            )
    else:
        # Celebrity - mark for fan-out on read
        redis.sadd(f"celebrity_tweets", tweet['tweet_id'])
```

```python
# Timeline Generation (Hybrid)
def get_timeline_hybrid(user_id, limit=20):
    # 1. Get pre-computed timeline (regular users)
    timeline = redis.zrevrange(f"timeline:{user_id}", 0, limit * 2)
    
    # 2. Get tweets from celebrities user follows
    celebrities_following = get_celebrity_following(user_id)
    celebrity_tweets = []
    for celeb_id in celebrities_following:
        tweets = db.tweets.query(
            "SELECT * FROM tweets WHERE user_id = ? ORDER BY created_at DESC LIMIT ?",
            celeb_id, limit
        )
        celebrity_tweets.extend(tweets)
    
    # 3. Merge and sort
    all_tweets = merge_and_sort(timeline, celebrity_tweets)
    
    # 4. Return top N
    return all_tweets[:limit]
```

---

## Step 8: Deep Dive Topics

### 8.1 Sharding Strategy

**Shard by user_id**:
```python
def get_shard(user_id, num_shards=1000):
    return user_id % num_shards

# Examples:
user_12345 → Shard 345
user_67890 → Shard 890
```

**Why shard by user_id?**
- All user's tweets on same shard
- Easy to fetch user profile
- Followers data co-located

**Trade-off**:
- Cross-shard queries for timeline (mitigated by cache)
- Celebrity users create hot shards (mitigated by replication)

---

### 8.2 Caching Strategy

**Redis Data Structures**:

```
Timeline Cache (Sorted Set):
Key: "timeline:{user_id}"
Value: ZSET of tweet_ids sorted by timestamp

Example:
"timeline:123" → {
  tweet_789: 1704326400,
  tweet_456: 1704326350,
  tweet_123: 1704326300
}

Tweet Cache (Hash):
Key: "tweet:{tweet_id}"
Value: Hash of tweet fields

Follower Cache (Set):
Key: "followers:{user_id}"
Value: Set of follower_ids
```

**Cache Invalidation**:
```python
def on_new_tweet(tweet):
    # Invalidate author's profile cache
    redis.delete(f"user_tweets:{tweet['user_id']}")
    
    # Timeline caches updated by fan-out worker (no invalidation needed)

def on_follow(follower_id, followee_id):
    # Invalidate follower's timeline
    redis.delete(f"timeline:{follower_id}")
    
    # Update follower count cache
    redis.incr(f"follower_count:{followee_id}")
```

---

### 8.3 ID Generation (Twitter Snowflake)

```
64-bit ID Structure:
┌─────────────┬──────────┬──────────┬─────────────┐
│  Timestamp  │ Datacenter│ Worker ID│  Sequence   │
│   41 bits   │  5 bits  │  5 bits  │   12 bits   │
└─────────────┴──────────┴──────────┴─────────────┘

- 41 bits timestamp: ~69 years
- 5 bits datacenter: 32 datacenters
- 5 bits worker: 32 workers per datacenter
- 12 bits sequence: 4096 IDs per millisecond

Benefits:
✓ Sortable by time
✓ Roughly time-ordered
✓ Distributed generation (no coordination)
✓ 64 bits (fits in BIGINT)
```

```python
class Snowflake:
    def __init__(self, datacenter_id, worker_id):
        self.datacenter_id = datacenter_id
        self.worker_id = worker_id
        self.sequence = 0
        self.last_timestamp = 0
        self.epoch = 1704067200000  # Custom epoch
    
    def generate_id(self):
        timestamp = current_timestamp_ms()
        
        if timestamp == self.last_timestamp:
            self.sequence = (self.sequence + 1) & 0xFFF  # 12 bits
            if self.sequence == 0:
                # Sequence overflow - wait for next millisecond
                timestamp = wait_next_millis(timestamp)
        else:
            self.sequence = 0
        
        self.last_timestamp = timestamp
        
        # Construct 64-bit ID
        id = ((timestamp - self.epoch) << 22) | \
             (self.datacenter_id << 17) | \
             (self.worker_id << 12) | \
             self.sequence
        
        return id
```

---

## Step 9: Scaling Considerations

### Handle 10x Traffic

**Current**: 150M DAU  
**10x**: 1.5B DAU

**Changes Needed**:

1. **Database Sharding**: 1,000 → 10,000 shards
2. **Cache Cluster**: 3 TB → 30 TB Redis cluster
3. **Fan-out Workers**: 100 → 1,000 workers
4. **Kafka Partitions**: 100 → 1,000 partitions
5. **API Servers**: 1,000 → 10,000 servers
6. **Datacenters**: 3 → 10+ regions

**Cost Optimization**:
- Aggressive caching (reduce DB queries)
- CDN for static content
- Compress older tweets
- Archive old timelines

---

## Step 10: Additional Considerations

### Monitoring

```
Key Metrics:
- Tweet post latency (p95 < 500ms)
- Timeline load latency (p95 < 200ms)
- Cache hit ratio (> 80%)
- Fan-out lag (< 5 seconds)
- Error rate (< 0.1%)

Alerts:
- Fan-out queue depth > 1M
- Cache hit ratio < 50%
- Database slow queries
- Kafka consumer lag
```

### Security

```
- Rate limiting: 100 tweets/hour per user
- Authentication: OAuth 2.0, JWT tokens
- Input validation: Sanitize tweet content
- DDoS protection: Cloudflare, WAF
```

---

## Summary

**Architecture**: Microservices + Redis + Sharded DB + Kafka

**Key Decisions**:
1. **Hybrid fan-out**: Push for regular, pull for celebrities
2. **Redis caching**: Pre-computed timelines
3. **Sharding**: By user_id for data locality
4. **Snowflake IDs**: Time-ordered, distributed generation
5. **Eventual consistency**: Acceptable for social media

**Scalability**: Handles billions of users through horizontal scaling

---

*This is a production-grade design. Practice explaining it in 45 minutes!*
