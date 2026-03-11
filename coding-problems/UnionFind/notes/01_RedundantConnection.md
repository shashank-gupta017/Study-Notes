# LeetCode 684: Redundant Connection

## Problem Classification
- **Difficulty**: Medium
- **Pattern**: Union Find (Cycle Detection)
- **Topic**: Graph Theory, Union-Find/Disjoint Set Union
- **Related Patterns**: Graph Traversal, Cycle Detection

## Problem Statement

In this problem, a tree is an **undirected graph** that is connected and has no cycles.

You are given a graph that started as a tree with `n` nodes labeled from `1` to `n`, with one additional edge added. The added edge has two different vertices chosen from `1` to `n`, and was not an edge that already exists. The graph is represented as an array `edges` of length `n` where `edges[i] = [ai, bi]` indicates that there is an edge between nodes `ai` and `bi` in the graph.

Return an edge that can be removed so that the resulting graph is a tree of `n` nodes. If there are multiple answers, return the answer that occurs last in the input.

### Example 1:
```
Input: edges = [[1,2],[1,3],[2,3]]
Output: [2,3]

Explanation: 
    1
   / \
  2---3
The edge [2,3] creates the cycle.
```

### Example 2:
```
Input: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
Output: [1,4]

Explanation:
    1---5
   /|\
  2-3-4
The edge [1,4] creates the cycle.
```

### Constraints:
- `n == edges.length`
- `3 <= n <= 1000`
- `edges[i].length == 2`
- `1 <= ai < bi <= n`
- `ai != bi`
- There are no repeated edges
- The given graph is connected

---

## Core Concept: Union-Find Data Structure

### What is Union-Find?

Union-Find (also called Disjoint Set Union or DSU) is a data structure that efficiently manages a partition of a set into disjoint subsets. It supports two primary operations:

1. **Find**: Determine which subset a particular element belongs to (typically by finding the "root" or "representative")
2. **Union**: Join two subsets into a single subset

### Why Union-Find for This Problem?

This problem is **perfectly suited** for Union-Find because:

1. **Cycle Detection**: When we try to union two nodes that already belong to the same set (same connected component), we've found a cycle
2. **Incremental Construction**: We process edges one at a time, building the graph incrementally
3. **Efficient Query**: Union-Find allows us to check connectivity in near-constant time
4. **Natural Fit**: Trees are collections of disjoint components that get merged - exactly what Union-Find manages

### The Key Insight

A tree with `n` nodes has exactly `n-1` edges. When we add the `n`-th edge:
- If it connects two **different** components → still no cycle (but now we have a tree)
- If it connects two nodes in the **same** component → **CYCLE DETECTED**!

The first edge that tries to connect nodes already in the same component is our answer.

---

## Union-Find Implementation Deep Dive

### Basic Structure

```python
class UnionFind:
    def __init__(self, n):
        # Each node is initially its own parent (self-loop)
        self.parent = list(range(n))
        # Rank tracks the approximate "depth" of each tree
        self.rank = [0] * n
```

### Operation 1: Find (with Path Compression)

```python
def find(self, x):
    """
    Find the root of the set containing x.
    Implements path compression optimization.
    """
    if self.parent[x] != x:
        # Recursively find root and compress path
        # This makes all nodes on the path point directly to root
        self.parent[x] = self.find(self.parent[x])
    return self.parent[x]
```

**Path Compression Explained:**

Without path compression:
```
Initial tree:        After find(4):
    0                    0
    |                    |
    1                    1
    |                    |
    2                    2
    |                    |
    3                    3
    |                    |
    4                    4
```

With path compression:
```
Initial tree:        After find(4):
    0                    0
    |                  / | \ \ \
    1                 1  2  3  4
    |                
    2                
    |                
    3                
    |                
    4                
```

All nodes on the path now point directly to the root! This makes future queries much faster.

**Why It Matters:**
- First find(4): O(h) where h is height
- Subsequent find(4): O(1) - directly points to root
- Amortizes the cost across operations

### Operation 2: Union (with Union by Rank)

```python
def union(self, x, y):
    """
    Union the sets containing x and y.
    Returns False if they're already in the same set (cycle detected).
    Implements union by rank optimization.
    """
    rootX = self.find(x)
    rootY = self.find(y)
    
    # Already in same set - cycle detected!
    if rootX == rootY:
        return False
    
    # Union by rank: attach smaller tree under larger tree
    if self.rank[rootX] < self.rank[rootY]:
        self.parent[rootX] = rootY
    elif self.rank[rootX] > self.rank[rootY]:
        self.parent[rootY] = rootX
    else:
        # Equal rank: arbitrarily choose one as root
        self.parent[rootY] = rootX
        self.rank[rootX] += 1
    
    return True
```

**Union by Rank Explained:**

The rank represents the upper bound on the height of the tree. By always attaching the smaller tree under the larger tree, we keep trees balanced.

Example:
```
Tree 1 (rank 2):    Tree 2 (rank 1):
      0                   3
     / \                  |
    1   2                 4

After union(2, 3) - attach Tree 2 under Tree 1:
      0
     /|\
    1 2 3
        |
        4
```

**Why It Matters:**
- Prevents trees from becoming linear (O(n) height)
- Keeps trees logarithmic in height
- Combined with path compression: near-constant time operations

### Rank vs Size

There are two common approaches for balancing:

**Union by Rank** (used here):
- Tracks approximate height
- Rank only increases when two equal-rank trees merge
- Simpler to implement
- Works perfectly with path compression

**Union by Size**:
- Tracks actual number of nodes
- Always merge smaller set into larger
- Slightly more intuitive
- Also achieves similar performance

---

## Complete Solution with Union-Find

```python
class Solution:
    def findRedundantConnection(self, edges: List[List[int]]) -> List[int]:
        """
        Find the redundant edge that creates a cycle.
        
        Approach: Union-Find with path compression and union by rank
        - Process edges in order
        - Try to union the two nodes of each edge
        - If union fails (nodes already connected), we found the cycle edge
        
        Time: O(n * α(n)) where α is inverse Ackermann function
        Space: O(n) for parent and rank arrays
        """
        n = len(edges)
        parent = list(range(n + 1))  # 1-indexed, so size n+1
        rank = [0] * (n + 1)
        
        def find(x):
            if parent[x] != x:
                parent[x] = find(parent[x])  # Path compression
            return parent[x]
        
        def union(x, y):
            rootX, rootY = find(x), find(y)
            
            if rootX == rootY:
                return False  # Cycle detected
            
            # Union by rank
            if rank[rootX] < rank[rootY]:
                parent[rootX] = rootY
            elif rank[rootX] > rank[rootY]:
                parent[rootY] = rootX
            else:
                parent[rootY] = rootX
                rank[rootX] += 1
            
            return True
        
        # Process edges in order
        for u, v in edges:
            if not union(u, v):
                return [u, v]  # This edge creates the cycle
        
        return []  # Should never reach here given problem constraints
```

### Clean Object-Oriented Approach

```python
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n + 1))
        self.rank = [0] * (n + 1)
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        rootX, rootY = self.find(x), self.find(y)
        
        if rootX == rootY:
            return False
        
        if self.rank[rootX] < self.rank[rootY]:
            self.parent[rootX] = rootY
        elif self.rank[rootX] > self.rank[rootY]:
            self.parent[rootY] = rootX
        else:
            self.parent[rootY] = rootX
            self.rank[rootX] += 1
        
        return True

class Solution:
    def findRedundantConnection(self, edges: List[List[int]]) -> List[int]:
        uf = UnionFind(len(edges))
        
        for u, v in edges:
            if not uf.union(u, v):
                return [u, v]
        
        return []
```

---

## Alternative Approach: DFS Cycle Detection

While Union-Find is optimal for this problem, let's understand the DFS approach:

```python
class Solution:
    def findRedundantConnection(self, edges: List[List[int]]) -> List[int]:
        """
        DFS approach: Build graph incrementally, check for cycle on each edge.
        
        Time: O(n²) - for each edge, we do a DFS
        Space: O(n) - for the graph and recursion stack
        """
        graph = defaultdict(list)
        
        def has_cycle(source, target):
            """Check if adding edge (source, target) creates a cycle"""
            visited = set()
            
            def dfs(node, parent):
                if node in visited:
                    return False
                
                visited.add(node)
                
                for neighbor in graph[node]:
                    if neighbor == parent:
                        continue
                    if neighbor == target:
                        return True  # Found path from source to target
                    if dfs(neighbor, node):
                        return True
                
                return False
            
            return dfs(source, -1)
        
        # Process edges in order
        for u, v in edges:
            # Check if adding this edge would create a cycle
            if graph[u] and graph[v] and has_cycle(u, v):
                return [u, v]
            
            # Add edge to graph
            graph[u].append(v)
            graph[v].append(u)
        
        return []
```

### DFS vs Union-Find Comparison

| Aspect | DFS | Union-Find |
|--------|-----|------------|
| Time Complexity | O(n²) | O(n·α(n)) ≈ O(n) |
| Space Complexity | O(n) | O(n) |
| Simplicity | More intuitive | Requires understanding DSU |
| Practical Performance | Slower for large n | Much faster |
| Best Use Case | Small graphs, educational | Production, competitive programming |

**Why Union-Find Wins:**
- Near-linear time vs quadratic
- Simpler logic once you understand the pattern
- Standard technique for connectivity problems

---

## Step-by-Step Execution Example

Let's trace through Example 1: `edges = [[1,2],[1,3],[2,3]]`

### Initial State:
```
parent = [0, 1, 2, 3]  # index 0 unused, nodes 1,2,3 are their own parents
rank   = [0, 0, 0, 0]
```

### Step 1: Process edge [1,2]
```
find(1) = 1 (parent[1] = 1)
find(2) = 2 (parent[2] = 2)
rootX(1) ≠ rootY(2) → Union them

rank[1] = rank[2] = 0 (equal)
→ parent[2] = 1
→ rank[1] = 1

Current state:
parent = [0, 1, 1, 3]
rank   = [0, 1, 0, 0]

Graph: 1---2
```

### Step 2: Process edge [1,3]
```
find(1) = 1 (parent[1] = 1)
find(3) = 3 (parent[3] = 3)
rootX(1) ≠ rootY(3) → Union them

rank[1] = 1, rank[3] = 0
rank[1] > rank[3]
→ parent[3] = 1

Current state:
parent = [0, 1, 1, 1]
rank   = [0, 1, 0, 0]

Graph: 1---2
       |
       3
```

### Step 3: Process edge [2,3]
```
find(2) → parent[2] = 1, return 1
find(3) → parent[3] = 1, return 1
rootX(1) = rootY(1) → SAME ROOT!

🔴 CYCLE DETECTED! Return [2,3]
```

Visual representation of the cycle:
```
    1
   / \
  2---3  ← This edge [2,3] completes the cycle
```

---

## Edge Cases and Considerations

### 1. Self-Loop (Not in This Problem)
```python
# If edges could be [1,1]
# Already handled: ai != bi per constraints
```

### 2. Disconnected Graph Before Final Edge
```python
# Example: [[1,2],[3,4],[1,3]]
# After [1,2]: Components {1,2} and {3,4}
# After [3,4]: Still separate components
# After [1,3]: Connects everything - no cycle
# Problem guarantees this won't happen (always connected with one extra edge)
```

### 3. Multiple Cycles
```python
# Example: [[1,2],[1,3],[2,3],[3,4],[4,5],[3,5]]
# We return the LAST edge that creates A cycle: [4,5] or [3,5]
# Our algorithm naturally handles this by processing in order
```

### 4. Large Graph
```python
# With n = 1000, edges = 1000
# Union-Find: 1000 * α(1000) ≈ 1000 * 4 ≈ 4000 operations
# DFS: 1000 * 1000 = 1,000,000 operations
# Union-Find is 250x faster!
```

### 5. Linear Chain Edge Case
```python
# edges = [[1,2],[2,3],[3,4],[4,5],[1,5]]
# Forms a cycle: 1-2-3-4-5-1
# Without path compression: O(n) per find
# With path compression: O(α(n)) amortized
```

---

## Complexity Analysis

### Time Complexity: O(n · α(n))

Where α(n) is the **inverse Ackermann function**.

**What is α(n)?**

The inverse Ackermann function grows so slowly that for all practical values of n (even n = 2^65536), α(n) ≤ 4.

```
α(n) values:
n ≤ 3:                    α(n) = 0
4 ≤ n ≤ 7:                α(n) = 1
8 ≤ n ≤ 2047:             α(n) = 2
2048 ≤ n ≤ 2^2047:        α(n) = 3
2^2048 ≤ n ≤ A(4,4):      α(n) = 4

A(4,4) ≈ 2^(2^(2^...)) with 65536 2's
```

For all practical purposes: **O(n · α(n)) ≈ O(n)** (linear time)

**Why α(n)?**

The combination of:
1. **Path compression**: Makes trees very flat
2. **Union by rank**: Keeps trees balanced

Together they achieve this near-constant amortized time per operation.

### The Ackermann Function A(m,n)

The inverse Ackermann is defined in terms of the Ackermann function:

```
A(0, n) = n + 1
A(m, 0) = A(m-1, 1)
A(m, n) = A(m-1, A(m, n-1))

Examples:
A(1, n) = n + 2
A(2, n) = 2n + 3
A(3, n) = 2^(n+3) - 3
A(4, 2) = 2^65536 - 3
```

The function grows **incredibly fast**. Its inverse grows **incredibly slowly**.

### Space Complexity: O(n)

- `parent` array: O(n)
- `rank` array: O(n)
- Recursion stack for find (with path compression): O(α(n)) ≈ O(1)

Total: **O(n)**

### Complexity Comparison Table

| Operation | Without Optimizations | With Path Compression | With Both Optimizations |
|-----------|----------------------|----------------------|------------------------|
| Find | O(n) | O(log n) | O(α(n)) ≈ O(1) |
| Union | O(n) | O(log n) | O(α(n)) ≈ O(1) |
| m operations | O(mn) | O(m log n) | O(m α(n)) |

---

## Common Mistakes and Pitfalls

### 1. Forgetting 1-Indexed Nodes
```python
# ❌ WRONG: Using size n
parent = list(range(n))

# ✅ CORRECT: Using size n+1 for 1-indexed nodes
parent = list(range(n + 1))
```

### 2. Not Implementing Path Compression
```python
# ❌ WRONG: No path compression
def find(self, x):
    while self.parent[x] != x:
        x = self.parent[x]
    return x

# ✅ CORRECT: With path compression
def find(self, x):
    if self.parent[x] != x:
        self.parent[x] = self.find(self.parent[x])
    return self.parent[x]
```

### 3. Incorrect Union Return Value
```python
# ❌ WRONG: Always returns True
def union(self, x, y):
    rootX, rootY = self.find(x), self.find(y)
    self.parent[rootX] = rootY
    return True

# ✅ CORRECT: Returns False when cycle detected
def union(self, x, y):
    rootX, rootY = self.find(x), self.find(y)
    if rootX == rootY:
        return False
    self.parent[rootX] = rootY
    return True
```

### 4. Modifying Parent Before Check
```python
# ❌ WRONG: Changes parent before checking for cycle
def union(self, x, y):
    self.parent[self.find(x)] = self.find(y)
    return self.find(x) != self.find(y)

# ✅ CORRECT: Check first, then modify
def union(self, x, y):
    rootX, rootY = self.find(x), self.find(y)
    if rootX == rootY:
        return False
    self.parent[rootX] = rootY
    return True
```

---

## Variations and Extensions

### Variation 1: Return All Redundant Edges

```python
def findAllRedundantConnections(self, edges: List[List[int]]) -> List[List[int]]:
    n = len(edges)
    parent = list(range(n + 1))
    rank = [0] * (n + 1)
    redundant = []
    
    def find(x):
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]
    
    def union(x, y):
        rootX, rootY = find(x), find(y)
        if rootX == rootY:
            return False
        if rank[rootX] < rank[rootY]:
            parent[rootX] = rootY
        elif rank[rootX] > rank[rootY]:
            parent[rootY] = rootX
        else:
            parent[rootY] = rootX
            rank[rootX] += 1
        return True
    
    for u, v in edges:
        if not union(u, v):
            redundant.append([u, v])
    
    return redundant
```

### Variation 2: Count Connected Components

```python
def countComponents(self, n: int, edges: List[List[int]]) -> int:
    parent = list(range(n))
    rank = [0] * n
    
    def find(x):
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]
    
    def union(x, y):
        rootX, rootY = find(x), find(y)
        if rootX == rootY:
            return False
        if rank[rootX] < rank[rootY]:
            parent[rootX] = rootY
        elif rank[rootX] > rank[rootY]:
            parent[rootY] = rootX
        else:
            parent[rootY] = rootX
            rank[rootX] += 1
        return True
    
    components = n
    for u, v in edges:
        if union(u, v):
            components -= 1
    
    return components
```

### Variation 3: Check if Graph is Tree

```python
def isTree(self, n: int, edges: List[List[int]]) -> bool:
    # A tree must have exactly n-1 edges and no cycles
    if len(edges) != n - 1:
        return False
    
    parent = list(range(n))
    rank = [0] * n
    
    def find(x):
        if parent[x] != x:
            parent[x] = find(parent[x])
        return parent[x]
    
    def union(x, y):
        rootX, rootY = find(x), find(y)
        if rootX == rootY:
            return False
        if rank[rootX] < rank[rootY]:
            parent[rootX] = rootY
        elif rank[rootX] > rank[rootY]:
            parent[rootY] = rootX
        else:
            parent[rootY] = rootX
            rank[rootX] += 1
        return True
    
    for u, v in edges:
        if not union(u, v):
            return False  # Cycle detected
    
    return True
```

---

## Related LeetCode Problems

### Direct Applications of Union-Find

1. **LeetCode 547: Number of Provinces** (Medium)
   - Count connected components in undirected graph
   - Same union-find pattern

2. **LeetCode 323: Number of Connected Components** (Medium)
   - Similar to 547 but with edge list
   - Direct union-find application

3. **LeetCode 685: Redundant Connection II** (Hard)
   - Directed graph version
   - More complex: handle directed edges and parent relationships

4. **LeetCode 1319: Number of Operations to Make Network Connected** (Medium)
   - Find redundant edges and reconnect components
   - Union-find to count components

5. **LeetCode 952: Largest Component Size by Common Factor** (Hard)
   - Union nodes sharing common factors
   - Creative use of union-find

### Cycle Detection Problems

6. **LeetCode 207: Course Schedule** (Medium)
   - Cycle detection in directed graph
   - Can use union-find but DFS topological sort is more natural

7. **LeetCode 261: Graph Valid Tree** (Medium)
   - Check if undirected graph is a tree
   - Perfect for union-find

8. **LeetCode 1971: Find if Path Exists in Graph** (Easy)
   - Check connectivity between two nodes
   - Union-find for preprocessing

### Advanced Union-Find

9. **LeetCode 128: Longest Consecutive Sequence** (Medium)
   - Union consecutive numbers
   - Clever union-find application

10. **LeetCode 990: Satisfiability of Equality Equations** (Medium)
    - Union equal variables, check inequalities
    - Union-find with constraint checking

11. **LeetCode 721: Accounts Merge** (Medium)
    - Union accounts with common emails
    - Union-find with strings

12. **LeetCode 839: Similar String Groups** (Hard)
    - Union similar strings
    - Union-find with custom similarity check

---

## Interview Tips and Strategies

### When to Recognize Union-Find Pattern

Look for these keywords:
- ✅ Connected components
- ✅ Cycle detection in undirected graphs
- ✅ Dynamic connectivity
- ✅ Grouping/clustering
- ✅ "Check if two elements are in the same set"

### Template for Union-Find Problems

```python
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
        # Optional: count of components
        self.components = n
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        rootX, rootY = self.find(x), self.find(y)
        if rootX == rootY:
            return False  # Already connected / cycle detected
        
        if self.rank[rootX] < self.rank[rootY]:
            self.parent[rootX] = rootY
        elif self.rank[rootX] > self.rank[rootY]:
            self.parent[rootY] = rootX
        else:
            self.parent[rootY] = rootX
            self.rank[rootX] += 1
        
        self.components -= 1
        return True
    
    def connected(self, x, y):
        return self.find(x) == self.find(y)
    
    def get_components(self):
        return self.components
```

### Common Interview Questions

**Q: "Why use Union-Find instead of DFS/BFS?"**
A: Union-Find is faster for dynamic connectivity (O(α(n)) vs O(n) per query) and handles incremental graph construction naturally.

**Q: "What's the time complexity?"**
A: O(n · α(n)) where α(n) is the inverse Ackermann function, effectively O(n) for all practical purposes.

**Q: "Can you explain path compression?"**
A: Path compression flattens the tree structure by making all nodes on the path point directly to the root during find operations, dramatically reducing future query times.

**Q: "What if we only use one optimization?"**
A: 
- Only path compression: O(log n) amortized
- Only union by rank: O(log n) worst case
- Both together: O(α(n)) amortized

**Q: "How do you handle 0-indexed vs 1-indexed?"**
A: Create parent array with size n+1 for 1-indexed (leaving index 0 unused), or map nodes to 0-indexed range.

---

## Practice Problems by Difficulty

### Easy
- 1971: Find if Path Exists in Graph
- 1627: Graph Connectivity With Threshold

### Medium
- **684: Redundant Connection** (This problem)
- 547: Number of Provinces
- 323: Number of Connected Components
- 261: Graph Valid Tree
- 990: Satisfiability of Equality Equations
- 721: Accounts Merge
- 1319: Number of Operations to Make Network Connected
- 1202: Smallest String With Swaps

### Hard
- 685: Redundant Connection II
- 952: Largest Component Size by Common Factor
- 839: Similar String Groups
- 1579: Remove Max Number of Edges to Keep Graph Fully Traversable

---

## Key Takeaways

1. **Union-Find is THE pattern for:**
   - Dynamic connectivity queries
   - Cycle detection in undirected graphs
   - Grouping/clustering problems

2. **Always implement both optimizations:**
   - Path compression in find()
   - Union by rank (or size) in union()

3. **The magic of α(n):**
   - Grows so slowly it's effectively constant
   - Makes union-find near-linear time

4. **This problem is perfect for Union-Find because:**
   - We process edges incrementally
   - We need to detect when a cycle forms
   - We need fast connectivity checks

5. **Pattern recognition:**
   - Incrementally adding connections → Union-Find
   - Need to find when components merge → Union-Find
   - Cycle detection in undirected graph → Union-Find

---

## Additional Resources

### Further Reading
- Introduction to Algorithms (CLRS) - Chapter 21: Data Structures for Disjoint Sets
- Tarjan's Union-Find Algorithm paper (1975)
- Ackermann function and its inverse

### Complexity Analysis
- Worst-case time bound for operations: O(α(n))
- Amortized analysis of path compression
- Proof of inverse Ackermann bound

### Related Algorithms
- Kruskal's Minimum Spanning Tree (uses Union-Find)
- Detecting cycles in undirected graphs
- Connected component labeling in images

---

## Summary

**Problem**: Find the edge that creates a cycle in a tree with one extra edge.

**Solution**: Use Union-Find to incrementally build the graph. The first edge that tries to connect two nodes already in the same component is the answer.

**Time Complexity**: O(n · α(n)) ≈ O(n)
**Space Complexity**: O(n)

**Key Insight**: A tree with n nodes has exactly n-1 edges. The n-th edge must create a cycle. Union-Find efficiently detects when we try to connect already-connected nodes.

**Master this pattern** and you'll easily solve dozens of graph connectivity problems!

---

*Last Updated: 2024*
*Problem Source: LeetCode #684*
*Pattern: Union-Find / Disjoint Set Union*
