# Phase 2: Communication Patterns

## Overview

Learn how services communicate in distributed systems, from REST APIs to event-driven architectures.

**Duration**: Week 3-4 (Days 16-22)  
**Prerequisites**: Core building blocks, Scaling patterns  
**Goal**: Master inter-service communication strategies

---

## Module 5.1: Synchronous Communication

### REST (Representational State Transfer)

**Core Principles**:
- Stateless
- Resource-based URLs
- Standard HTTP methods
- Standard status codes

**HTTP Methods**:
```http
GET    /users/123           # Read user
POST   /users               # Create user
PUT    /users/123           # Update user (full)
PATCH  /users/123           # Update user (partial)
DELETE /users/123           # Delete user
```

**Status Codes**:
```
2xx Success:
  200 OK                    # Success
  201 Created               # Resource created
  204 No Content            # Success, no body

4xx Client Error:
  400 Bad Request           # Invalid input
  401 Unauthorized          # Not authenticated
  403 Forbidden             # Not authorized
  404 Not Found             # Resource doesn't exist
  429 Too Many Requests     # Rate limited

5xx Server Error:
  500 Internal Server Error # Server bug
  502 Bad Gateway           # Upstream error
  503 Service Unavailable   # Overloaded/down
  504 Gateway Timeout       # Upstream timeout
```

---

**REST Best Practices**:

**1. Versioning**
```http
# URL versioning (recommended)
GET /api/v1/users/123
GET /api/v2/users/123

# Header versioning
GET /users/123
Accept: application/vnd.myapi.v1+json

# Query parameter (not recommended)
GET /users/123?version=1
```

**2. Pagination**
```http
# Offset-based
GET /api/v1/posts?limit=20&offset=40

Response:
{
  "data": [...],
  "pagination": {
    "limit": 20,
    "offset": 40,
    "total": 1000,
    "next": "/api/v1/posts?limit=20&offset=60"
  }
}

# Cursor-based (better for real-time data)
GET /api/v1/posts?limit=20&cursor=abc123

Response:
{
  "data": [...],
  "next_cursor": "def456"
}
```

**3. Filtering & Sorting**
```http
GET /api/v1/posts?status=published&author=john&sort=-created_at,title

# Filtering: status=published, author=john
# Sorting: -created_at (desc), title (asc)
```

**4. Error Responses**
```json
{
  "error": {
    "code": "INVALID_INPUT",
    "message": "Email address is invalid",
    "field": "email",
    "details": "Email must be in format user@domain.com"
  }
}
```

---

### GraphQL

**Problem with REST**: Multiple round trips

```
# REST: 3 requests
GET /users/123                    # Get user
GET /users/123/posts              # Get user's posts
GET /posts/456/comments           # Get post comments
```

**GraphQL: 1 request with exact data needed**
```graphql
query {
  user(id: 123) {
    name
    email
    posts(limit: 10) {
      title
      comments(limit: 5) {
        content
        author {
          name
        }
      }
    }
  }
}
```

**Benefits**:
- ✓ Single request
- ✓ No over-fetching (only requested fields)
- ✓ No under-fetching (all data in one go)
- ✓ Strongly typed schema

**Drawbacks**:
- ✗ Complex queries can be expensive
- ✗ Harder to cache (not URL-based)
- ✗ Need query complexity limits
- ✗ Steeper learning curve

**When to use**:
- Mobile apps (minimize requests)
- Complex data requirements
- Frequent UI changes

---

### gRPC (Google Remote Procedure Call)

**What**: Binary protocol, faster than JSON/REST

**Protocol Buffers (Protobuf)**:
```protobuf
// user.proto
syntax = "proto3";

message User {
  int64 id = 1;
  string name = 2;
  string email = 3;
}

service UserService {
  rpc GetUser (GetUserRequest) returns (User);
  rpc CreateUser (CreateUserRequest) returns (User);
}
```

**Comparison**:
| Feature | REST | gRPC |
|---------|------|------|
| Format | JSON (text) | Protobuf (binary) |
| Speed | Slower | 3-7x faster |
| Size | Larger | Smaller |
| Human-readable | Yes | No |
| Streaming | Limited | Built-in |
| Browser | Yes | Limited |

**When to use gRPC**:
- Microservices communication
- High-performance requirements
- Real-time streaming
- Internal APIs (not public-facing)

---

## Module 5.2: Asynchronous Communication

### Message Queues

**Pattern**: Producer → Queue → Consumer

```
Producer:                Queue:              Consumer:
Order Service    →    [Order Created]   →   Email Service
                      [Order Updated]       Inventory Service
                      [Order Shipped]       Analytics Service
```

**Benefits**:
- Decoupling (services don't need to know each other)
- Reliability (queue persists messages)
- Load leveling (consumer processes at its own pace)
- Scalability (add more consumers)

---

**Message Queue Patterns**:

**1. Work Queue (Competing Consumers)**
```
Producer → Queue → Consumer 1
                → Consumer 2
                → Consumer 3

Each message consumed by ONE consumer
Use: Load distribution, parallel processing
```

```python
# Producer
queue.send('process_order', {
    'order_id': 123,
    'items': [...]
})

# Consumers (multiple workers)
def worker():
    while True:
        message = queue.receive('process_order')
        process_order(message.data)
        message.acknowledge()
```

---

**2. Publish-Subscribe (Fanout)**
```
Publisher → Topic → Subscriber 1 (all messages)
                  → Subscriber 2 (all messages)
                  → Subscriber 3 (all messages)

Each message delivered to ALL subscribers
Use: Event broadcasting, notifications
```

```python
# Publisher
pubsub.publish('order_created', {
    'order_id': 123,
    'user_id': 456
})

# Subscriber 1: Send email
def email_subscriber():
    pubsub.subscribe('order_created', send_confirmation_email)

# Subscriber 2: Update inventory
def inventory_subscriber():
    pubsub.subscribe('order_created', update_inventory)

# Subscriber 3: Record analytics
def analytics_subscriber():
    pubsub.subscribe('order_created', track_event)
```

---

**3. Topic-Based Routing**
```
Publisher → Topic (orders.created)  → Subscriber 1
          → Topic (orders.updated)  → Subscriber 2
          → Topic (orders.*)        → Subscriber 3 (all order events)
```

```python
# Publisher
pubsub.publish('orders.created', data)
pubsub.publish('orders.updated', data)
pubsub.publish('orders.shipped', data)

# Subscribers
pubsub.subscribe('orders.created', handle_new_order)
pubsub.subscribe('orders.*', log_all_order_events)
```

---

### Event-Driven Architecture

**Event Sourcing**: Store events, not current state

```
Traditional (state):
User: { id: 123, balance: 100 }
Update → { id: 123, balance: 150 }

Event Sourcing (events):
Event 1: AccountCreated(id=123, balance=0)
Event 2: MoneyDeposited(id=123, amount=100)
Event 3: MoneyDeposited(id=123, amount=50)

Current state = Apply all events
```

**Benefits**:
- Complete audit trail
- Can replay events
- Can derive multiple views
- Time travel (state at any point)

**Example**:
```python
# Events
events = [
    {'type': 'AccountCreated', 'id': 123, 'balance': 0},
    {'type': 'Deposited', 'amount': 100},
    {'type': 'Withdrawn', 'amount': 30},
    {'type': 'Deposited', 'amount': 50},
]

# Rebuild current state
def rebuild_state(events):
    state = {}
    for event in events:
        if event['type'] == 'AccountCreated':
            state['balance'] = event['balance']
        elif event['type'] == 'Deposited':
            state['balance'] += event['amount']
        elif event['type'] == 'Withdrawn':
            state['balance'] -= event['amount']
    return state

current = rebuild_state(events)  # {'balance': 120}
```

---

**CQRS (Command Query Responsibility Segregation)**

**Idea**: Separate write model from read model

```
Commands (Write):         Events:              Queries (Read):
CreateOrder          →   OrderCreated    →    Orders Table
UpdateOrder          →   OrderUpdated    →    Order Summary View
CancelOrder          →   OrderCancelled  →    Analytics View
```

**Write Model**:
```python
# Handles commands, produces events
def create_order(command):
    # Validate
    if not valid(command):
        raise ValidationError()
    
    # Create event
    event = OrderCreated(
        order_id=generate_id(),
        user_id=command.user_id,
        items=command.items
    )
    
    # Store event
    event_store.append(event)
    
    # Publish event
    event_bus.publish(event)
```

**Read Model**:
```python
# Listens to events, updates views
def on_order_created(event):
    # Update orders table
    db.orders.insert({
        'order_id': event.order_id,
        'user_id': event.user_id,
        'status': 'created',
        'created_at': event.timestamp
    })
    
    # Update analytics
    analytics.increment('total_orders')

# Query read model
def get_user_orders(user_id):
    return db.orders.find({'user_id': user_id})
```

**Benefits**:
- Optimized for reads (denormalized views)
- Optimized for writes (event append-only)
- Scale read and write independently

---

## Module 5.3: Real-Time Communication

### WebSockets

**HTTP vs WebSocket**:
```
HTTP (Request-Response):
Client → Request → Server
Client ← Response ← Server
(Connection closes)

WebSocket (Persistent Connection):
Client ←→ Server (bi-directional, persistent)
```

**Use Cases**:
- Chat applications
- Live feeds (stock prices, sports scores)
- Collaborative editing
- Gaming
- Real-time notifications

**Example**:
```python
# Server
from flask_socketio import SocketIO, emit

socketio = SocketIO(app)

@socketio.on('send_message')
def handle_message(data):
    # Broadcast to all connected clients
    emit('new_message', {
        'user': data['user'],
        'message': data['message'],
        'timestamp': now()
    }, broadcast=True)

# Client (JavaScript)
const socket = io.connect('http://localhost:5000');

// Send message
socket.emit('send_message', {
    user: 'John',
    message: 'Hello!'
});

// Receive messages
socket.on('new_message', (data) => {
    console.log(data.message);
});
```

---

**Scaling WebSockets**:

**Problem**: User connects to Server 1, but message broadcast from Server 2
```
User A → Server 1
User B → Server 2

User A sends message → Server 1 → Only User A sees it!
```

**Solution**: Pub/Sub backend (Redis)
```
User A → Server 1 → Redis Pub/Sub → Server 1 → User A
                                  → Server 2 → User B
```

```python
# Each server subscribes to Redis
redis_pubsub = redis.pubsub()
redis_pubsub.subscribe('messages')

@socketio.on('send_message')
def handle_message(data):
    # Publish to Redis
    redis.publish('messages', json.dumps(data))

# Listen to Redis, broadcast to local clients
def listen_to_redis():
    for message in redis_pubsub.listen():
        if message['type'] == 'message':
            data = json.loads(message['data'])
            socketio.emit('new_message', data)
```

---

### Server-Sent Events (SSE)

**What**: Server pushes updates to client over HTTP

```
Client → HTTP Request (keeps connection open)
Server → Event 1
Server → Event 2
Server → Event 3
```

**vs WebSocket**:
- Simpler (HTTP-based)
- One-way (server → client only)
- Automatic reconnection
- Works through proxies/firewalls

**Use Cases**:
- Live updates (news feed)
- Notifications
- Progress updates
- Server monitoring

```python
# Server (Flask)
@app.route('/stream')
def stream():
    def event_stream():
        while True:
            # Fetch new data
            data = get_latest_update()
            
            # Send to client
            yield f"data: {json.dumps(data)}\n\n"
            
            time.sleep(1)
    
    return Response(event_stream(), mimetype='text/event-stream')

# Client (JavaScript)
const eventSource = new EventSource('/stream');
eventSource.onmessage = (event) => {
    const data = JSON.parse(event.data);
    console.log(data);
};
```

---

### Long Polling

**What**: Client polls server, server holds request until data available

```
Client → Request → Server (waits)
                    ↓ (data available)
Client ← Response ← Server
Client → Request → Server (immediately)
```

**vs Regular Polling**:
```
Regular Polling (wasteful):
Client → Request → Server → Empty response
(wait 5 seconds)
Client → Request → Server → Empty response
(wait 5 seconds)
Client → Request → Server → Data!

Long Polling (efficient):
Client → Request → Server (holds for 30s or until data)
                          → Data! (responds immediately when available)
```

**Use Cases**:
- Real-time updates when WebSocket not available
- Older browsers
- Firewalls that block WebSocket

---

## Module 5.4: API Gateway

**What**: Single entry point for all clients

```
Mobile App  →
Web App     →  API Gateway  → Service A
IoT Device  →                → Service B
                             → Service C
```

**Responsibilities**:

**1. Routing**
```
POST /api/users      → User Service
GET  /api/products   → Product Service
GET  /api/orders     → Order Service
```

**2. Authentication/Authorization**
```python
@api_gateway.before_request
def authenticate():
    token = request.headers.get('Authorization')
    if not valid_token(token):
        return error(401, "Unauthorized")
    
    user = decode_token(token)
    request.user = user  # Attach to request
```

**3. Rate Limiting**
```python
@api_gateway.before_request
def rate_limit():
    user_id = request.user.id
    
    # Allow 1000 requests per hour
    count = redis.incr(f"rate_limit:{user_id}:{current_hour()}")
    redis.expire(f"rate_limit:{user_id}:{current_hour()}", 3600)
    
    if count > 1000:
        return error(429, "Too many requests")
```

**4. Request Aggregation**
```python
# Client makes 1 request to gateway
GET /api/dashboard

# Gateway makes multiple backend requests
@app.route('/api/dashboard')
def dashboard():
    # Parallel requests
    user = user_service.get_user(user_id)
    orders = order_service.get_orders(user_id)
    recommendations = recommendation_service.get(user_id)
    
    # Aggregate response
    return {
        'user': user,
        'orders': orders,
        'recommendations': recommendations
    }
```

**5. Response Transformation**
```python
# Transform backend response for mobile
@app.route('/api/products')
def products():
    # Get from backend
    products = product_service.get_all()
    
    # Transform for mobile (smaller payload)
    return [{
        'id': p.id,
        'name': p.name,
        'price': p.price,
        'image': p.thumbnail_url  # Not full size
    } for p in products]
```

---

## Module 5.5: Service Mesh

**What**: Infrastructure layer for service-to-service communication

```
Service A → Sidecar Proxy ←→ Sidecar Proxy ← Service B
                      ↓                ↓
               Control Plane (manages policies)
```

**Features**:

**1. Traffic Management**
```yaml
# Route 90% to v1, 10% to v2 (canary deployment)
apiVersion: networking.istio.io/v1
kind: VirtualService
metadata:
  name: my-service
spec:
  hosts:
  - my-service
  http:
  - route:
    - destination:
        host: my-service
        subset: v1
      weight: 90
    - destination:
        host: my-service
        subset: v2
      weight: 10
```

**2. Circuit Breaking**
```yaml
apiVersion: networking.istio.io/v1
kind: DestinationRule
metadata:
  name: my-service
spec:
  host: my-service
  trafficPolicy:
    connectionPool:
      tcp:
        maxConnections: 100
      http:
        maxRequestsPerConnection: 10
    outlierDetection:
      consecutiveErrors: 5
      interval: 30s
      baseEjectionTime: 30s
```

**3. Observability**
- Automatic metrics (latency, errors, requests)
- Distributed tracing
- Service dependencies

**Popular Service Meshes**:
- Istio
- Linkerd
- Consul Connect

---

## Practical Exercises

### Exercise 1: API Design

Design REST API for a blog:
- Posts (CRUD)
- Comments
- Tags
- Search

Include:
- Endpoint design
- Request/response formats
- Error handling
- Versioning

---

### Exercise 2: Event Design

Design event-driven system for e-commerce:
- Order placed
- Payment processed
- Items shipped
- Order delivered

For each event:
- Event structure
- Subscribers
- Retry logic
- Error handling

---

### Exercise 3: Communication Choice

Choose communication pattern for:
1. User login (need immediate response)
2. Email sending (can be async)
3. Real-time chat
4. Product search
5. Order processing

Justify each choice (REST, GraphQL, gRPC, Message Queue, WebSocket)

---

## Key Takeaways

1. **REST**: Simple, cacheable, widespread support
2. **GraphQL**: Flexible queries, reduces round trips
3. **gRPC**: Fast, binary, internal services
4. **Message Queues**: Async, decoupled, reliable
5. **WebSocket**: Real-time, bi-directional
6. **API Gateway**: Single entry point, handles cross-cutting concerns

---

## Self-Assessment

- [ ] Can design RESTful APIs
- [ ] Understand GraphQL trade-offs
- [ ] Know when to use message queues
- [ ] Can implement WebSocket communication
- [ ] Understand API Gateway benefits
- [ ] Can choose appropriate pattern for use case

---

## Next Module

[06-data-patterns](../06-data-patterns/README.md) - Deep dive into data consistency and replication

---

**Remember**: Choose the simplest pattern that meets your needs. Don't over-engineer!
