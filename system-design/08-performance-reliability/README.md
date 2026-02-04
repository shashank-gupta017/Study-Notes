# Phase 3: Performance & Reliability

## Overview

Build systems that are fast, resilient, and observable at scale.

**Duration**: Week 6 (Days 35-42)  
**Prerequisites**: All previous modules  
**Goal**: Master production-grade reliability patterns

---

## Module 8.1: Circuit Breaker Pattern

### The Problem

**Cascading Failures**:
```
Service A → Service B (slow/down) → Service C
     ↓
Waits, timeouts,
threads exhausted,
Service A fails too!
```

---

### The Solution: Circuit Breaker

**States**:
```
┌─────────┐  Errors    ┌──────┐  Timeout  ┌───────────┐
│ CLOSED  │ > threshold│ OPEN │ ────────→ │HALF-OPEN  │
│(Normal) │ ──────────→│(Fail │           │(Testing)  │
└─────────┘            │ fast)│           └───────────┘
     ↑                 └──────┘                 ↓
     │                                          │
     └──────────────Success─────────────────────┘
```

**Closed State** (Normal):
- Requests pass through
- Monitor errors
- If errors > threshold → Open

**Open State** (Failing Fast):
- Immediately reject requests (don't call service)
- Return error or fallback
- After timeout → Half-Open

**Half-Open State** (Testing):
- Allow limited requests through
- If success → Close
- If failure → Open

---

### Implementation

```python
class CircuitBreaker:
    def __init__(self, failure_threshold=5, timeout=60):
        self.failure_threshold = failure_threshold
        self.timeout = timeout
        self.failures = 0
        self.last_failure_time = None
        self.state = 'CLOSED'
    
    def call(self, func, *args, **kwargs):
        if self.state == 'OPEN':
            # Check if timeout passed
            if time.time() - self.last_failure_time > self.timeout:
                self.state = 'HALF-OPEN'
            else:
                raise CircuitBreakerOpenError("Circuit is open")
        
        try:
            # Make the call
            result = func(*args, **kwargs)
            
            # Success
            if self.state == 'HALF-OPEN':
                self.state = 'CLOSED'
                self.failures = 0
            
            return result
            
        except Exception as e:
            self.failures += 1
            self.last_failure_time = time.time()
            
            if self.failures >= self.failure_threshold:
                self.state = 'OPEN'
            
            raise

# Usage
circuit_breaker = CircuitBreaker(failure_threshold=5, timeout=60)

def get_user(user_id):
    return circuit_breaker.call(user_service.get, user_id)
```

---

### Fallback Strategies

```python
def get_user_with_fallback(user_id):
    try:
        return circuit_breaker.call(user_service.get, user_id)
    except CircuitBreakerOpenError:
        # Fallback 1: Return cached data
        cached = cache.get(f"user:{user_id}")
        if cached:
            return cached
        
        # Fallback 2: Return default
        return {
            'id': user_id,
            'name': 'User',
            'email': 'unavailable@example.com',
            '_cached': True
        }
```

---

## Module 8.2: Rate Limiting

### Why Rate Limit?

- Prevent abuse (DDoS, scraping)
- Ensure fair usage
- Protect backend services
- Cost control (API limits)

---

### Algorithms

**1. Token Bucket**

```
Concept: Bucket holds tokens, refilled at constant rate

Bucket (capacity: 10):
[T][T][T][T][T][T][T][T][T][T]
         ↑
    Refill: 5 tokens/second

Request → Takes 1 token → Allow
No tokens → Reject
```

```python
class TokenBucket:
    def __init__(self, capacity, refill_rate):
        self.capacity = capacity
        self.tokens = capacity
        self.refill_rate = refill_rate  # tokens per second
        self.last_refill = time.time()
    
    def allow_request(self):
        # Refill tokens
        now = time.time()
        elapsed = now - self.last_refill
        tokens_to_add = elapsed * self.refill_rate
        
        self.tokens = min(self.capacity, self.tokens + tokens_to_add)
        self.last_refill = now
        
        # Check if request allowed
        if self.tokens >= 1:
            self.tokens -= 1
            return True
        else:
            return False

# Usage
bucket = TokenBucket(capacity=100, refill_rate=10)  # 100 burst, 10/sec sustained

if bucket.allow_request():
    process_request()
else:
    return error(429, "Too many requests")
```

**Pros**:
- Allows bursts (up to capacity)
- Smooth rate limiting
- Simple to implement

---

**2. Leaky Bucket**

```
Concept: Requests enter bucket, leak out at constant rate

Bucket:
[Req1]
[Req2] ← Leak out at constant rate
[Req3]
  ↓
Process 1 request/second

New request:
- If bucket not full → Add
- If full → Reject
```

```python
from collections import deque

class LeakyBucket:
    def __init__(self, capacity, leak_rate):
        self.capacity = capacity
        self.leak_rate = leak_rate  # requests per second
        self.queue = deque()
        self.last_leak = time.time()
    
    def allow_request(self):
        # Leak requests
        now = time.time()
        elapsed = now - self.last_leak
        leaks = int(elapsed * self.leak_rate)
        
        for _ in range(min(leaks, len(self.queue))):
            self.queue.popleft()
        
        self.last_leak = now
        
        # Check if request allowed
        if len(self.queue) < self.capacity:
            self.queue.append(now)
            return True
        else:
            return False
```

**Pros**:
- Ensures constant output rate
- Smooths traffic spikes

---

**3. Fixed Window**

```
Window 1 (0-60s): [Request counts]
Window 2 (60-120s): [Request counts]

If count in current window < limit → Allow
Else → Reject
```

```python
def fixed_window_rate_limit(user_id, limit=100):
    current_minute = int(time.time() / 60)
    key = f"rate_limit:{user_id}:{current_minute}"
    
    count = redis.incr(key)
    redis.expire(key, 60)  # Expire after 1 minute
    
    if count <= limit:
        return True
    else:
        return False
```

**Problem**: Burst at window boundaries
```
Window 1: 100 requests at t=59s ✓
Window 2: 100 requests at t=60s ✓
Total: 200 requests in 2 seconds!
```

---

**4. Sliding Window Log**

```
Keep log of all request timestamps
Count requests in last N seconds

Log: [t1, t2, t3, ..., tn]
Now: t
Count requests where t - request_time <= window
```

```python
def sliding_window_log(user_id, limit=100, window=60):
    now = time.time()
    key = f"rate_limit:{user_id}"
    
    # Remove old requests
    redis.zremrangebyscore(key, 0, now - window)
    
    # Count requests in window
    count = redis.zcard(key)
    
    if count < limit:
        # Add current request
        redis.zadd(key, {f"{now}": now})
        redis.expire(key, window)
        return True
    else:
        return False
```

**Pros**:
- Accurate
- No boundary burst

**Cons**:
- Memory intensive (stores all requests)

---

**5. Sliding Window Counter (Hybrid)**

```
Combine fixed window + sliding estimation

Current window:  100 requests (0-60s)
Previous window: 80 requests (60-120s)

Estimate at t=90s (30s into current):
= 80 * (30/60) + 100 = 40 + 100 = 140 requests
```

```python
def sliding_window_counter(user_id, limit=100, window=60):
    now = time.time()
    current_window = int(now / window)
    previous_window = current_window - 1
    
    # Get counts
    current_count = redis.get(f"rate_limit:{user_id}:{current_window}") or 0
    previous_count = redis.get(f"rate_limit:{user_id}:{previous_window}") or 0
    
    # Calculate weighted count
    elapsed_in_current = now % window
    weight = (window - elapsed_in_current) / window
    estimated_count = previous_count * weight + current_count
    
    if estimated_count < limit:
        redis.incr(f"rate_limit:{user_id}:{current_window}")
        redis.expire(f"rate_limit:{user_id}:{current_window}", window * 2)
        return True
    else:
        return False
```

**Pros**:
- Memory efficient
- Smooth rate limiting
- No boundary burst

---

### Distributed Rate Limiting

**Challenge**: Multiple servers

```
Server 1: Counts 50 requests
Server 2: Counts 60 requests
Total: 110 (over limit of 100)

But each server thinks it's fine!
```

**Solution**: Centralized counter (Redis)

```python
def distributed_rate_limit(user_id, limit=100, window=60):
    # Use Redis for atomic operations
    current_window = int(time.time() / window)
    key = f"rate_limit:{user_id}:{current_window}"
    
    # Lua script for atomic inc + get
    lua_script = """
    local current = redis.call('INCR', KEYS[1])
    redis.call('EXPIRE', KEYS[1], ARGV[1])
    return current
    """
    
    count = redis.eval(lua_script, 1, key, window)
    
    return count <= limit
```

---

## Module 8.3: Retry Logic

### Retry with Exponential Backoff

**Problem**: Immediately retrying can make things worse

**Solution**: Wait longer between each retry

```python
def retry_with_backoff(func, max_retries=3, base_delay=1):
    for attempt in range(max_retries):
        try:
            return func()
        except TransientError as e:
            if attempt == max_retries - 1:
                raise  # Last attempt, give up
            
            # Exponential backoff: 1s, 2s, 4s, 8s, ...
            delay = base_delay * (2 ** attempt)
            
            # Add jitter (randomness)
            jitter = random.uniform(0, delay * 0.1)
            
            time.sleep(delay + jitter)
    
    raise MaxRetriesExceeded()

# Usage
result = retry_with_backoff(lambda: api.call_remote_service())
```

**Why Jitter?**
```
Without jitter:
Server recovers at t=10s
All clients retry at exactly t=10s → Thundering herd!

With jitter:
Clients retry at t=10.1s, t=10.3s, t=10.7s, ... → Spread out
```

---

### Retry Decision Tree

```python
def should_retry(error):
    # Network errors → Retry
    if isinstance(error, (ConnectionError, TimeoutError)):
        return True
    
    # Server errors → Maybe retry
    if isinstance(error, ServerError):
        if error.status_code == 503:  # Service Unavailable
            return True
        if error.status_code == 502:  # Bad Gateway
            return True
        return False
    
    # Client errors → Don't retry
    if isinstance(error, ClientError):
        return False  # 400, 401, 403, 404 won't fix themselves
    
    return False
```

---

## Module 8.4: Timeouts

### Why Timeouts?

**Without timeout**:
```
Client → Server (slow/hung)
Client waits forever...
Resources exhausted
System crashes
```

**With timeout**:
```
Client → Server (slow)
After 5s → Timeout
Client fails fast
Try alternative or return error
```

---

### Timeout Strategies

**1. Connection Timeout**
```python
# How long to wait for connection to establish
requests.get(url, timeout=(3, None))
#                     ↑
#           3 seconds to connect
```

**2. Read Timeout**
```python
# How long to wait for response after connection
requests.get(url, timeout=(None, 10))
#                          ↑
#              10 seconds to read response
```

**3. Combined Timeout**
```python
# (connect_timeout, read_timeout)
requests.get(url, timeout=(3, 10))

# Or total timeout
requests.get(url, timeout=13)  # 13 seconds total
```

---

### Timeout Values

**How to choose?**

```
Measure p99 latency of service
Add buffer (20-50%)
Set as timeout

Example:
Service p99: 500ms
Timeout: 750ms (500 + 50%)
```

**Cascade Timeouts**:
```
User → API Gateway → Service A → Service B

Timeouts:
Gateway: 5s
Service A: 3s (leaves 2s for Gateway overhead)
Service B: 1s (leaves 2s for Service A overhead)
```

---

## Module 8.5: Bulkhead Pattern

### The Problem

**No Isolation**:
```
Thread Pool (100 threads):
Service A: 90 threads (hung)
Service B: 10 threads
Service C: 0 threads (starved!)
```

---

### The Solution: Bulkheads

**Isolate Resources**:
```
Thread Pool 1 (50): Service A
Thread Pool 2 (30): Service B
Thread Pool 3 (20): Service C

Service A hangs → Only affects its pool
Service B and C continue working!
```

```python
# Using separate thread pools
executor_a = ThreadPoolExecutor(max_workers=50)
executor_b = ThreadPoolExecutor(max_workers=30)
executor_c = ThreadPoolExecutor(max_workers=20)

# Submit tasks to appropriate pool
future_a = executor_a.submit(service_a.call)
future_b = executor_b.submit(service_b.call)
future_c = executor_c.submit(service_c.call)
```

---

## Module 8.6: Observability

### The Three Pillars

**1. Metrics** (What's happening?)
```
- Request rate (requests/second)
- Error rate (errors/second)
- Latency (p50, p95, p99)
- Saturation (CPU, memory, disk)
```

**2. Logs** (What happened?)
```
2024-01-01 10:00:00 INFO User 123 logged in
2024-01-01 10:00:05 ERROR Failed to connect to database
2024-01-01 10:00:10 WARN High memory usage: 85%
```

**3. Traces** (Where did request go?)
```
Request → Service A (50ms)
           → Service B (200ms)
              → Database (150ms)
              → Cache (10ms)
           → Service C (30ms)
Total: 280ms
```

---

### The Golden Signals (Google SRE)

**1. Latency**
```python
# Track request duration
@app.route('/api/users')
def get_users():
    start = time.time()
    try:
        users = fetch_users()
        return users
    finally:
        duration = time.time() - start
        metrics.histogram('api.users.latency', duration)
```

**2. Traffic**
```python
# Track request rate
@app.before_request
def track_traffic():
    metrics.increment('api.requests', tags=['endpoint:' + request.path])
```

**3. Errors**
```python
# Track errors
@app.errorhandler(Exception)
def track_errors(error):
    metrics.increment('api.errors', tags=[
        'type:' + error.__class__.__name__,
        'endpoint:' + request.path
    ])
```

**4. Saturation**
```python
# Track resource usage
def collect_system_metrics():
    metrics.gauge('system.cpu', psutil.cpu_percent())
    metrics.gauge('system.memory', psutil.virtual_memory().percent)
    metrics.gauge('system.disk', psutil.disk_usage('/').percent)

schedule.every(10).seconds.do(collect_system_metrics)
```

---

### Distributed Tracing

**Trace ID**: Unique ID for entire request journey

```python
import uuid

@app.before_request
def start_trace():
    # Generate or extract trace ID
    trace_id = request.headers.get('X-Trace-ID') or str(uuid.uuid4())
    request.trace_id = trace_id
    request.start_time = time.time()

@app.after_request
def end_trace(response):
    duration = time.time() - request.start_time
    
    # Log span
    span = {
        'trace_id': request.trace_id,
        'span_id': str(uuid.uuid4()),
        'service': 'api-service',
        'operation': f"{request.method} {request.path}",
        'duration_ms': duration * 1000,
        'tags': {
            'http.status': response.status_code,
            'http.method': request.method
        }
    }
    
    send_to_tracing(span)
    
    # Propagate trace ID
    response.headers['X-Trace-ID'] = request.trace_id
    return response
```

---

## Practical Exercises

### Exercise 1: Implement Circuit Breaker

Implement circuit breaker for payment service:
- Failure threshold: 5 errors
- Timeout: 30 seconds
- Fallback: Queue payment for retry

---

### Exercise 2: Rate Limiting Strategy

Design rate limiting for API:
- Free tier: 100 req/hour
- Pro tier: 1000 req/hour
- Enterprise: 10000 req/hour

Choose algorithm and justify.

---

### Exercise 3: Timeout Configuration

Service chain: A → B → C → D

Given:
- D: p99 = 100ms
- C: p99 = 50ms
- B: p99 = 30ms

Set appropriate timeouts for each.

---

## Key Takeaways

1. **Circuit Breaker**: Fail fast, prevent cascades
2. **Rate Limiting**: Protect resources, ensure fairness
3. **Retry**: Exponential backoff with jitter
4. **Timeouts**: Always set, cascade appropriately
5. **Bulkhead**: Isolate resources
6. **Observability**: Metrics, logs, traces

---

## Self-Assessment

- [ ] Can implement circuit breaker pattern
- [ ] Understand rate limiting algorithms
- [ ] Can design retry strategies
- [ ] Can set appropriate timeouts
- [ ] Understand observability pillars
- [ ] Can use the Golden Signals

---

## Next Module

[09-security-privacy](../09-security-privacy/README.md) - Secure systems at scale

---

**Remember**: Design for failure. Failure is not an exception, it's the norm.
