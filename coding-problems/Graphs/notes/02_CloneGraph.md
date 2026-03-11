# Clone Graph

## Problem Link
LeetCode #133: https://leetcode.com/problems/clone-graph/

## Difficulty
Medium

## Problem Description
Given a reference of a node in a **connected** undirected graph, return a **deep copy** (clone) of the graph.

Each node in the graph contains a value (`int`) and a list (`List[Node]`) of its neighbors.

```
class Node {
    public int val;
    public List<Node> neighbors;
}
```

**Test case format:**

For simplicity, each node's value is the same as the node's index (1-indexed). For example, the first node with `val == 1`, the second node with `val == 2`, and so on. The graph is represented in the test case using an adjacency list.

**Adjacency list** is a collection of unordered **lists** used to represent a finite graph. Each list describes the set of neighbors of a node in the graph.

**Example 1:**
```
Input: adjList = [[2,4],[1,3],[2,4],[1,3]]
Output: [[2,4],[1,3],[2,4],[1,3]]

Explanation: 
Graph:
    1 --- 2
    |     |
    4 --- 3

Node 1's neighbors are [2, 4]
Node 2's neighbors are [1, 3]
Node 3's neighbors are [2, 4]
Node 4's neighbors are [1, 3]
```

**Example 2:**
```
Input: adjList = [[]]
Output: [[]]
Explanation: Single node with no neighbors
```

**Example 3:**
```
Input: adjList = []
Output: []
Explanation: Empty graph
```

**Constraints:**
- The number of nodes in the graph is in the range [0, 100]
- 1 <= Node.val <= 100
- Node.val is unique for each node
- There are no repeated edges and no self-loops in the graph
- The Graph is connected and all nodes can be visited starting from the given node

## Pattern Recognition
This is a **Graph DFS/BFS with HashMap** problem. Key characteristics:
1. Deep copy requirement (can't just copy references)
2. Need to track original → clone mapping
3. Graph traversal (DFS or BFS)
4. Handle cycles (visited tracking)

**CRITICAL INSIGHT:** Use HashMap to store original→clone mapping to:
- Avoid creating duplicate clones
- Handle cycles in the graph
- Connect clones properly

## Core Concept

**PROBLEM BREAKDOWN:**
1. Create a new node for each original node
2. Connect the cloned nodes the same way originals are connected
3. Return the clone of the starting node

**CHALLENGE: Cycles**
```
Original Graph:
    1 ←→ 2
    
Without visited tracking:
- Clone node 1
- Clone its neighbor (node 2)
- Clone node 2's neighbor (node 1)
- Clone node 1's neighbor (node 2)
- INFINITE LOOP!

Solution: Use HashMap
- Map original node → cloned node
- Before cloning, check if already cloned
```

## Visual Explanation

```
Original Graph:
      1 ────── 2
      │        │
      │        │
      4 ────── 3

Step-by-Step DFS Cloning:

Step 1: Start at node 1
┌─────────────────┐
│ HashMap:        │
│ 1 → clone(1)    │
└─────────────────┘
Create clone of node 1

Step 2: Visit neighbor 2
┌─────────────────┐
│ HashMap:        │
│ 1 → clone(1)    │
│ 2 → clone(2)    │
└─────────────────┘
Create clone of node 2
Add clone(2) to clone(1).neighbors

Step 3: From node 2, visit neighbor 1
┌─────────────────┐
│ HashMap:        │
│ 1 → clone(1)    │  ← Already exists!
│ 2 → clone(2)    │
└─────────────────┘
Don't create new clone, use existing
Add clone(1) to clone(2).neighbors

Step 4: From node 2, visit neighbor 3
┌─────────────────┐
│ HashMap:        │
│ 1 → clone(1)    │
│ 2 → clone(2)    │
│ 3 → clone(3)    │
└─────────────────┘
Create clone of node 3
Add clone(3) to clone(2).neighbors

... Continue until all nodes processed

Final Cloned Graph:
      clone(1) ────── clone(2)
         │               │
         │               │
      clone(4) ────── clone(3)
```

## Solution 1: DFS with HashMap (Recursive)

```python
class Node:
    def __init__(self, val=0, neighbors=None):
        self.val = val
        self.neighbors = neighbors if neighbors is not None else []

def cloneGraph(node):
    """
    DFS solution using recursion and HashMap.
    
    Time: O(V + E) - visit each vertex and edge once
    Space: O(V) - HashMap stores V nodes, recursion stack O(V)
    """
    if not node:
        return None
    
    # HashMap: original node → cloned node
    clones = {}
    
    def dfs(node):
        # If already cloned, return the clone
        if node in clones:
            return clones[node]
        
        # Create clone for current node
        clone = Node(node.val)
        clones[node] = clone
        
        # Recursively clone all neighbors
        for neighbor in node.neighbors:
            clone.neighbors.append(dfs(neighbor))
        
        return clone
    
    return dfs(node)
```

**Key Points:**
- HashMap prevents duplicate cloning
- Handles cycles automatically
- Recursive approach is intuitive
- Returns immediately if node already cloned

## Solution 2: BFS with HashMap

```python
from collections import deque

def cloneGraph(node):
    """
    BFS solution using queue and HashMap.
    
    Time: O(V + E)
    Space: O(V) - queue and HashMap
    """
    if not node:
        return None
    
    # Create clone of starting node
    clones = {node: Node(node.val)}
    queue = deque([node])
    
    while queue:
        curr = queue.popleft()
        
        # Process all neighbors
        for neighbor in curr.neighbors:
            # If neighbor not cloned yet
            if neighbor not in clones:
                # Clone the neighbor
                clones[neighbor] = Node(neighbor.val)
                # Add to queue for processing
                queue.append(neighbor)
            
            # Add cloned neighbor to current clone's neighbors
            clones[curr].neighbors.append(clones[neighbor])
    
    return clones[node]
```

**BFS Advantages:**
- Iterative (no stack overflow risk)
- Level-by-level processing
- Easier to visualize

## Solution 3: DFS Iterative (Stack)

```python
def cloneGraph(node):
    """
    Iterative DFS using stack.
    
    Time: O(V + E)
    Space: O(V)
    """
    if not node:
        return None
    
    clones = {node: Node(node.val)}
    stack = [node]
    
    while stack:
        curr = stack.pop()
        
        for neighbor in curr.neighbors:
            if neighbor not in clones:
                clones[neighbor] = Node(neighbor.val)
                stack.append(neighbor)
            
            clones[curr].neighbors.append(clones[neighbor])
    
    return clones[node]
```

## Solution 4: Clean Two-Pass Approach

```python
def cloneGraph(node):
    """
    Two-pass approach:
    1. Create all nodes
    2. Connect all edges
    
    Time: O(V + E)
    Space: O(V)
    """
    if not node:
        return None
    
    clones = {}
    visited = set()
    
    # Pass 1: Create all clones
    def create_clones(node):
        if not node or node in visited:
            return
        
        visited.add(node)
        clones[node] = Node(node.val)
        
        for neighbor in node.neighbors:
            create_clones(neighbor)
    
    # Pass 2: Connect neighbors
    def connect_neighbors(node):
        if not node or node in clones[node].neighbors:
            return
        
        for neighbor in node.neighbors:
            clones[node].neighbors.append(clones[neighbor])
            connect_neighbors(neighbor)
    
    create_clones(node)
    connect_neighbors(node)
    
    return clones[node]
```

## Comprehensive Implementations

### Python Implementation (Multiple Approaches)

```python
class Node:
    def __init__(self, val=0, neighbors=None):
        self.val = val
        self.neighbors = neighbors if neighbors is not None else []

class Solution:
    def cloneGraph_dfs_recursive(self, node):
        """Most concise DFS solution"""
        if not node:
            return None
        
        clones = {}
        
        def dfs(node):
            if node in clones:
                return clones[node]
            
            clone = Node(node.val)
            clones[node] = clone
            
            for neighbor in node.neighbors:
                clone.neighbors.append(dfs(neighbor))
            
            return clone
        
        return dfs(node)
    
    def cloneGraph_bfs(self, node):
        """BFS solution"""
        if not node:
            return None
        
        from collections import deque
        
        clones = {node: Node(node.val)}
        queue = deque([node])
        
        while queue:
            curr = queue.popleft()
            
            for neighbor in curr.neighbors:
                if neighbor not in clones:
                    clones[neighbor] = Node(neighbor.val)
                    queue.append(neighbor)
                
                clones[curr].neighbors.append(clones[neighbor])
        
        return clones[node]
    
    def cloneGraph_dfs_iterative(self, node):
        """Iterative DFS with stack"""
        if not node:
            return None
        
        clones = {node: Node(node.val)}
        stack = [node]
        visited = set()
        
        while stack:
            curr = stack.pop()
            
            if curr in visited:
                continue
            
            visited.add(curr)
            
            for neighbor in curr.neighbors:
                if neighbor not in clones:
                    clones[neighbor] = Node(neighbor.val)
                    stack.append(neighbor)
                
                clones[curr].neighbors.append(clones[neighbor])
        
        return clones[node]
    
    def cloneGraph_elegant(self, node):
        """Most elegant one-liner approach"""
        if not node:
            return None
        
        clones = {}
        
        def clone(node):
            if node not in clones:
                clones[node] = Node(node.val)
                clones[node].neighbors = [clone(n) for n in node.neighbors]
            return clones[node]
        
        return clone(node)
```

### Java Implementation

```java
class Node {
    public int val;
    public List<Node> neighbors;
    
    public Node() {
        val = 0;
        neighbors = new ArrayList<Node>();
    }
    
    public Node(int _val) {
        val = _val;
        neighbors = new ArrayList<Node>();
    }
    
    public Node(int _val, ArrayList<Node> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
}

class Solution {
    // DFS Recursive Solution
    private HashMap<Node, Node> clones = new HashMap<>();
    
    public Node cloneGraph(Node node) {
        if (node == null) {
            return null;
        }
        
        return dfs(node);
    }
    
    private Node dfs(Node node) {
        // If already cloned, return clone
        if (clones.containsKey(node)) {
            return clones.get(node);
        }
        
        // Create clone
        Node clone = new Node(node.val);
        clones.put(node, clone);
        
        // Clone all neighbors
        for (Node neighbor : node.neighbors) {
            clone.neighbors.add(dfs(neighbor));
        }
        
        return clone;
    }
    
    // BFS Solution
    public Node cloneGraphBFS(Node node) {
        if (node == null) {
            return null;
        }
        
        HashMap<Node, Node> clones = new HashMap<>();
        Queue<Node> queue = new LinkedList<>();
        
        // Clone starting node
        clones.put(node, new Node(node.val));
        queue.offer(node);
        
        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            
            for (Node neighbor : curr.neighbors) {
                // If not cloned yet, clone it
                if (!clones.containsKey(neighbor)) {
                    clones.put(neighbor, new Node(neighbor.val));
                    queue.offer(neighbor);
                }
                
                // Add cloned neighbor to current clone
                clones.get(curr).neighbors.add(clones.get(neighbor));
            }
        }
        
        return clones.get(node);
    }
}
```

### C++ Implementation

```cpp
class Node {
public:
    int val;
    vector<Node*> neighbors;
    
    Node() {
        val = 0;
        neighbors = vector<Node*>();
    }
    
    Node(int _val) {
        val = _val;
        neighbors = vector<Node*>();
    }
    
    Node(int _val, vector<Node*> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
};

class Solution {
public:
    // DFS Recursive
    Node* cloneGraph(Node* node) {
        if (!node) return nullptr;
        
        unordered_map<Node*, Node*> clones;
        return dfs(node, clones);
    }
    
private:
    Node* dfs(Node* node, unordered_map<Node*, Node*>& clones) {
        if (clones.count(node)) {
            return clones[node];
        }
        
        Node* clone = new Node(node->val);
        clones[node] = clone;
        
        for (Node* neighbor : node->neighbors) {
            clone->neighbors.push_back(dfs(neighbor, clones));
        }
        
        return clone;
    }
    
    // BFS Solution
    Node* cloneGraphBFS(Node* node) {
        if (!node) return nullptr;
        
        unordered_map<Node*, Node*> clones;
        queue<Node*> q;
        
        clones[node] = new Node(node->val);
        q.push(node);
        
        while (!q.empty()) {
            Node* curr = q.front();
            q.pop();
            
            for (Node* neighbor : curr->neighbors) {
                if (!clones.count(neighbor)) {
                    clones[neighbor] = new Node(neighbor->val);
                    q.push(neighbor);
                }
                
                clones[curr]->neighbors.push_back(clones[neighbor]);
            }
        }
        
        return clones[node];
    }
};
```

### JavaScript Implementation

```javascript
/**
 * Node definition
 */
function Node(val, neighbors) {
    this.val = val === undefined ? 0 : val;
    this.neighbors = neighbors === undefined ? [] : neighbors;
}

/**
 * DFS Recursive Solution
 */
var cloneGraph = function(node) {
    if (!node) {
        return null;
    }
    
    const clones = new Map();
    
    function dfs(node) {
        if (clones.has(node)) {
            return clones.get(node);
        }
        
        const clone = new Node(node.val);
        clones.set(node, clone);
        
        for (const neighbor of node.neighbors) {
            clone.neighbors.push(dfs(neighbor));
        }
        
        return clone;
    }
    
    return dfs(node);
};

/**
 * BFS Solution
 */
var cloneGraphBFS = function(node) {
    if (!node) {
        return null;
    }
    
    const clones = new Map();
    const queue = [node];
    
    clones.set(node, new Node(node.val));
    
    while (queue.length > 0) {
        const curr = queue.shift();
        
        for (const neighbor of curr.neighbors) {
            if (!clones.has(neighbor)) {
                clones.set(neighbor, new Node(neighbor.val));
                queue.push(neighbor);
            }
            
            clones.get(curr).neighbors.push(clones.get(neighbor));
        }
    }
    
    return clones.get(node);
};

/**
 * Elegant One-Liner Style
 */
var cloneGraphElegant = function(node) {
    if (!node) return null;
    
    const clones = new Map();
    
    const clone = (n) => {
        if (!clones.has(n)) {
            clones.set(n, new Node(n.val));
            clones.get(n).neighbors = n.neighbors.map(clone);
        }
        return clones.get(n);
    };
    
    return clone(node);
};
```

## Complexity Analysis

### Time Complexity: O(V + E)
- **V**: Number of vertices (nodes)
- **E**: Number of edges
- Visit each node once: O(V)
- Process each edge once: O(E)
- Total: O(V + E)

### Space Complexity: O(V)
- **HashMap**: Stores V nodes → O(V)
- **Recursion Stack (DFS)**: O(V) in worst case (linear graph)
- **Queue (BFS)**: O(V) in worst case
- **Cloned Graph**: O(V + E) but this is output, not auxiliary

## Edge Cases and Testing

```python
def create_graph_from_adjacency_list(adj_list):
    """Helper to create graph from adjacency list"""
    if not adj_list:
        return None
    
    nodes = {}
    
    # Create all nodes
    for i in range(len(adj_list)):
        nodes[i + 1] = Node(i + 1)
    
    # Connect neighbors
    for i, neighbors in enumerate(adj_list):
        for neighbor_val in neighbors:
            nodes[i + 1].neighbors.append(nodes[neighbor_val])
    
    return nodes[1] if nodes else None

def graph_to_adjacency_list(node):
    """Convert graph back to adjacency list"""
    if not node:
        return []
    
    visited = set()
    adj_list = {}
    
    def dfs(node):
        if not node or node.val in visited:
            return
        
        visited.add(node.val)
        adj_list[node.val] = [n.val for n in node.neighbors]
        
        for neighbor in node.neighbors:
            dfs(neighbor)
    
    dfs(node)
    
    # Convert to list format
    if not adj_list:
        return []
    
    max_val = max(adj_list.keys())
    result = []
    for i in range(1, max_val + 1):
        result.append(sorted(adj_list.get(i, [])))
    
    return result

def test_clone_graph():
    solution = Solution()
    
    # Test 1: Square graph
    adj_list1 = [[2,4],[1,3],[2,4],[1,3]]
    node1 = create_graph_from_adjacency_list(adj_list1)
    cloned1 = solution.cloneGraph(node1)
    assert graph_to_adjacency_list(cloned1) == adj_list1
    assert cloned1 is not node1  # Different object
    
    # Test 2: Single node
    adj_list2 = [[]]
    node2 = create_graph_from_adjacency_list(adj_list2)
    cloned2 = solution.cloneGraph(node2)
    assert graph_to_adjacency_list(cloned2) == adj_list2
    
    # Test 3: Empty graph
    cloned3 = solution.cloneGraph(None)
    assert cloned3 is None
    
    # Test 4: Two connected nodes
    adj_list4 = [[2], [1]]
    node4 = create_graph_from_adjacency_list(adj_list4)
    cloned4 = solution.cloneGraph(node4)
    assert graph_to_adjacency_list(cloned4) == adj_list4
    
    # Test 5: Linear graph
    adj_list5 = [[2], [1,3], [2,4], [3]]
    node5 = create_graph_from_adjacency_list(adj_list5)
    cloned5 = solution.cloneGraph(node5)
    assert graph_to_adjacency_list(cloned5) == adj_list5
    
    print("All test cases passed!")

test_clone_graph()
```

## Common Pitfalls

### 1. Forgetting to Use HashMap
```python
# WRONG: Creates duplicate clones
def cloneGraph(node):
    if not node:
        return None
    
    clone = Node(node.val)
    
    for neighbor in node.neighbors:
        clone.neighbors.append(cloneGraph(neighbor))  # Infinite recursion!
    
    return clone

# CORRECT: Use HashMap to track clones
def cloneGraph(node):
    if not node:
        return None
    
    clones = {}
    
    def dfs(node):
        if node in clones:  # Check if already cloned
            return clones[node]
        
        clone = Node(node.val)
        clones[node] = clone
        
        for neighbor in node.neighbors:
            clone.neighbors.append(dfs(neighbor))
        
        return clone
    
    return dfs(node)
```

### 2. Cloning After Adding to HashMap
```python
# WRONG: May create cycle before fully cloning
def dfs(node):
    clone = Node(node.val)
    
    for neighbor in node.neighbors:
        clone.neighbors.append(dfs(neighbor))
    
    clones[node] = clone  # Too late!
    return clone

# CORRECT: Add to HashMap immediately after creation
def dfs(node):
    if node in clones:
        return clones[node]
    
    clone = Node(node.val)
    clones[node] = clone  # Add immediately!
    
    for neighbor in node.neighbors:
        clone.neighbors.append(dfs(neighbor))
    
    return clone
```

### 3. Shallow Copy vs Deep Copy
```python
# WRONG: Shallow copy (shares same neighbor objects)
clone = Node(node.val)
clone.neighbors = node.neighbors  # Shallow copy!

# CORRECT: Deep copy (creates new neighbor clones)
clone = Node(node.val)
for neighbor in node.neighbors:
    clone.neighbors.append(dfs(neighbor))  # Deep copy
```

### 4. Not Handling Empty Graph
```python
# WRONG: Crashes on None input
def cloneGraph(node):
    clones = {}
    return dfs(node)  # Crashes if node is None

# CORRECT: Check for None
def cloneGraph(node):
    if not node:
        return None
    
    clones = {}
    return dfs(node)
```

## Optimization Techniques

### 1. Early Return Pattern
```python
def dfs(node):
    # Combine null check and visited check
    if not node or node in clones:
        return clones.get(node)
    
    clone = Node(node.val)
    clones[node] = clone
    
    clone.neighbors = [dfs(n) for n in node.neighbors]
    
    return clone
```

### 2. List Comprehension for Neighbors
```python
# Clean and concise
clone.neighbors = [dfs(neighbor) for neighbor in node.neighbors]

# Instead of loop
for neighbor in node.neighbors:
    clone.neighbors.append(dfs(neighbor))
```

### 3. Combining Clone Creation and Storage
```python
def dfs(node):
    if node in clones:
        return clones[node]
    
    # Create and store in one line
    clones[node] = clone = Node(node.val)
    
    clone.neighbors = [dfs(n) for n in node.neighbors]
    
    return clone
```

## Related Problems

1. **Copy List with Random Pointer** (LeetCode 138)
   - Similar cloning pattern
   - Linked list instead of graph
   - Two pointers per node

2. **Clone N-ary Tree** (LeetCode 1490)
   - Tree cloning (no cycles)
   - Multiple children per node

3. **Clone Binary Tree With Random Pointer** (LeetCode 1485)
   - Binary tree + random pointer
   - Combines tree and graph cloning

4. **Serialize and Deserialize Binary Tree** (LeetCode 297)
   - Different form of graph representation
   - Encoding/decoding graph structure

## Advanced Variations

### Variation 1: Clone with Modified Values
```python
def cloneGraphWithTransform(node, transform_func):
    """Clone graph and transform node values"""
    if not node:
        return None
    
    clones = {}
    
    def dfs(node):
        if node in clones:
            return clones[node]
        
        # Transform value during cloning
        clone = Node(transform_func(node.val))
        clones[node] = clone
        
        for neighbor in node.neighbors:
            clone.neighbors.append(dfs(neighbor))
        
        return clone
    
    return dfs(node)

# Usage: Double all values
cloned = cloneGraphWithTransform(node, lambda x: x * 2)
```

### Variation 2: Clone Directed Graph
```python
def cloneDirectedGraph(node):
    """Clone directed graph (same approach, just note direction matters)"""
    if not node:
        return None
    
    clones = {}
    
    def dfs(node):
        if node in clones:
            return clones[node]
        
        clone = Node(node.val)
        clones[node] = clone
        
        # Direction: current -> neighbor
        for neighbor in node.neighbors:
            clone.neighbors.append(dfs(neighbor))
        
        return clone
    
    return dfs(node)
```

### Variation 3: Clone with Additional Attributes
```python
class WeightedNode:
    def __init__(self, val=0):
        self.val = val
        self.neighbors = []  # List of (node, weight) tuples

def cloneWeightedGraph(node):
    """Clone graph with edge weights"""
    if not node:
        return None
    
    clones = {}
    
    def dfs(node):
        if node in clones:
            return clones[node]
        
        clone = WeightedNode(node.val)
        clones[node] = clone
        
        for neighbor, weight in node.neighbors:
            cloned_neighbor = dfs(neighbor)
            clone.neighbors.append((cloned_neighbor, weight))
        
        return clone
    
    return dfs(node)
```

### Variation 4: Count Nodes While Cloning
```python
def cloneAndCount(node):
    """Clone graph and return (cloned_graph, node_count, edge_count)"""
    if not node:
        return None, 0, 0
    
    clones = {}
    node_count = 0
    edge_count = 0
    
    def dfs(node):
        nonlocal node_count, edge_count
        
        if node in clones:
            return clones[node]
        
        clone = Node(node.val)
        clones[node] = clone
        node_count += 1
        
        for neighbor in node.neighbors:
            clone.neighbors.append(dfs(neighbor))
            edge_count += 1
        
        return clone
    
    cloned = dfs(node)
    
    # Each edge counted twice in undirected graph
    return cloned, node_count, edge_count // 2
```

## Comparison: DFS vs BFS

```
Aspect              | DFS (Recursive)      | BFS (Queue)
--------------------|---------------------|-------------------
Code Simplicity     | ⭐⭐⭐⭐⭐         | ⭐⭐⭐⭐
Space Complexity    | O(V) stack          | O(V) queue
Stack Overflow Risk | Yes (deep graphs)   | No
Memory Pattern      | Depth-first         | Breadth-first
Implementation      | Recursive           | Iterative
Debugging           | Harder (recursion)  | Easier (step-by-step)
When to Use         | Default choice      | Very deep graphs
```

## Memory Deep Dive

```python
# Original Graph Memory Layout:
node1 = Node(1)  # Address: 0x1000
node2 = Node(2)  # Address: 0x2000
node3 = Node(3)  # Address: 0x3000

node1.neighbors = [node2, node3]  # References: [0x2000, 0x3000]
node2.neighbors = [node1]          # Reference: [0x1000]

# Cloned Graph Memory Layout:
clone1 = Node(1)  # Address: 0x4000 (DIFFERENT!)
clone2 = Node(2)  # Address: 0x5000 (DIFFERENT!)
clone3 = Node(3)  # Address: 0x6000 (DIFFERENT!)

clone1.neighbors = [clone2, clone3]  # References: [0x5000, 0x6000]
clone2.neighbors = [clone1]          # Reference: [0x4000]

# HashMap stores mapping:
clones = {
    0x1000: 0x4000,  # node1 -> clone1
    0x2000: 0x5000,  # node2 -> clone2
    0x3000: 0x6000,  # node3 -> clone3
}
```

## Interview Tips

1. **Clarification Questions:**
   - Is the graph directed or undirected?
   - Can the graph have cycles?
   - Are there multiple connected components?
   - What should I return for an empty graph?

2. **Approach Discussion:**
   - Explain why HashMap is necessary
   - Mention both DFS and BFS options
   - Discuss cycle handling

3. **Code Strategy:**
   - Start with null check
   - Create HashMap
   - Choose DFS (simpler) or BFS (safer for deep graphs)
   - Clone node before processing neighbors

4. **Complexity Analysis:**
   - Time: O(V + E) - explain why
   - Space: O(V) for HashMap + O(V) for recursion/queue

5. **Follow-up Questions:**
   - "How would you clone a graph with weighted edges?"
   - "What if the graph doesn't fit in memory?"
   - "Can you clone without modifying the original?"
   - "How would you verify the clone is correct?"

6. **Testing:**
   - Empty graph
   - Single node
   - Two nodes connected
   - Cycle
   - Complex connected graph

## Key Takeaways

1. **HashMap is Essential**: Prevents duplicates and infinite loops
2. **Clone Before Recursing**: Add to HashMap immediately after creation
3. **Handle Cycles**: HashMap automatically handles cycles
4. **Deep Copy vs Shallow**: Must recursively clone neighbors
5. **DFS vs BFS**: Both work, DFS is simpler, BFS safer for deep graphs
6. **Pattern**: Create node → Store in map → Clone neighbors

This problem teaches the fundamental pattern of graph cloning with cycle detection!
