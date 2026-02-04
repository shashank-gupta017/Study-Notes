# Coding Pattern Recognition Guide

## How to Use This Guide

When you see a problem, ask yourself these questions to identify the pattern:
1. What is the input structure? (Array, String, Tree, Graph, etc.)
2. What are we looking for? (Subset, Max/Min, Count, Yes/No)
3. Are there constraints? (Sorted, Positive numbers, etc.)
4. What's the brute force? Can we optimize?

---

## Pattern 1: Arrays & Hashing

### When to Use
- Need O(1) lookup
- Counting frequencies
- Finding pairs/complements
- Grouping elements

### Key Indicators
- "Find pair that sums to target"
- "Count occurrences"
- "Group by property"
- "Check if exists"

### Template
```python
def hash_pattern(arr):
    hash_map = {}  # or set()
    
    for num in arr:
        # Check if complement/pair exists
        if target - num in hash_map:
            return True
        hash_map[num] = True
    
    return False
```

### Classic Problems
- Two Sum
- Group Anagrams
- Top K Frequent Elements
- Contains Duplicate

### Time/Space
- Time: O(n)
- Space: O(n)

---

## Pattern 2: Two Pointers

### When to Use
- Array/String is sorted
- Need to find pairs
- Remove duplicates in-place
- Palindrome checking

### Key Indicators
- "Sorted array"
- "Find pair/triplet"
- "In-place"
- "Palindrome"

### Template
```python
def two_pointers(arr):
    left, right = 0, len(arr) - 1
    
    while left < right:
        if condition_met(arr[left], arr[right]):
            return True
        elif should_move_left:
            left += 1
        else:
            right -= 1
    
    return False
```

### Classic Problems
- Two Sum II (Sorted)
- 3Sum
- Container With Most Water
- Valid Palindrome

### Time/Space
- Time: O(n)
- Space: O(1)

---

## Pattern 3: Sliding Window

### When to Use
- Contiguous subarray/substring problems
- Finding max/min in subarrays
- All subarrays of size k

### Key Indicators
- "Contiguous"
- "Subarray" or "Substring"
- "Window"
- "All subarrays of size k"

### Template (Fixed Size)
```python
def sliding_window_fixed(arr, k):
    window_sum = sum(arr[:k])
    max_sum = window_sum
    
    for i in range(k, len(arr)):
        window_sum += arr[i] - arr[i - k]
        max_sum = max(max_sum, window_sum)
    
    return max_sum
```

### Template (Variable Size)
```python
def sliding_window_variable(arr, target):
    left = 0
    window_sum = 0
    min_length = float('inf')
    
    for right in range(len(arr)):
        window_sum += arr[right]
        
        while window_sum >= target:
            min_length = min(min_length, right - left + 1)
            window_sum -= arr[left]
            left += 1
    
    return min_length if min_length != float('inf') else 0
```

### Classic Problems
- Maximum Sum Subarray of Size K
- Longest Substring Without Repeating Characters
- Minimum Window Substring
- Permutation in String

### Time/Space
- Time: O(n)
- Space: O(k) for hash map

---

## Pattern 4: Fast & Slow Pointers

### When to Use
- Detect cycles in LinkedList
- Find middle element
- Find kth element from end

### Key Indicators
- "LinkedList"
- "Cycle"
- "Middle element"
- "Kth from end"

### Template
```python
def fast_slow_pointers(head):
    slow = fast = head
    
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        
        if slow == fast:  # Cycle detected
            return True
    
    return False
```

### Classic Problems
- Linked List Cycle
- Find Middle of LinkedList
- Happy Number
- Palindrome LinkedList

### Time/Space
- Time: O(n)
- Space: O(1)

---

## Pattern 5: LinkedList In-place Reversal

### When to Use
- Reverse entire LinkedList
- Reverse part of LinkedList
- Reverse in k-groups

### Key Indicators
- "Reverse"
- "LinkedList"
- "In-place"

### Template
```python
def reverse_linkedlist(head):
    prev = None
    current = head
    
    while current:
        next_node = current.next
        current.next = prev
        prev = current
        current = next_node
    
    return prev
```

### Classic Problems
- Reverse LinkedList
- Reverse LinkedList II
- Reverse Nodes in k-Group
- Reorder List

### Time/Space
- Time: O(n)
- Space: O(1)

---

## Pattern 6: Tree BFS (Level Order)

### When to Use
- Level-by-level traversal
- Find level averages
- Minimum depth
- Zigzag traversal

### Key Indicators
- "Level order"
- "Each level"
- "Layer by layer"
- "Breadth-first"

### Template
```python
from collections import deque

def bfs(root):
    if not root:
        return []
    
    result = []
    queue = deque([root])
    
    while queue:
        level_size = len(queue)
        level = []
        
        for _ in range(level_size):
            node = queue.popleft()
            level.append(node.val)
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        result.append(level)
    
    return result
```

### Classic Problems
- Binary Tree Level Order Traversal
- Zigzag Traversal
- Minimum Depth of Binary Tree
- Level Order Successor

### Time/Space
- Time: O(n)
- Space: O(n)

---

## Pattern 7: Tree DFS (Depth First)

### When to Use
- Path finding
- Sum calculations
- Tree validation
- Recursive solutions

### Key Indicators
- "Path"
- "Root to leaf"
- "Sum"
- "All paths"

### Template (Preorder)
```python
def dfs(root):
    if not root:
        return None
    
    # Process current node
    result = root.val
    
    # Recurse on children
    left = dfs(root.left)
    right = dfs(root.right)
    
    return result + left + right
```

### Classic Problems
- Path Sum
- Maximum Depth of Binary Tree
- Validate Binary Search Tree
- Lowest Common Ancestor

### Time/Space
- Time: O(n)
- Space: O(h) where h is height

---

## Pattern 8: Binary Search

### When to Use
- Sorted array
- Find target in O(log n)
- Find insertion point
- Search in rotated array

### Key Indicators
- "Sorted array"
- "O(log n)"
- "Find target"
- "Peak element"

### Template
```python
def binary_search(arr, target):
    left, right = 0, len(arr) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        
        if arr[mid] == target:
            return mid
        elif arr[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return -1
```

### Classic Problems
- Binary Search
- Search in Rotated Sorted Array
- Find Peak Element
- First Bad Version

### Time/Space
- Time: O(log n)
- Space: O(1)

---

## Pattern 9: Top K Elements (Heap)

### When to Use
- Find K largest/smallest
- K most frequent
- Median finding
- Merge K sorted

### Key Indicators
- "Top K"
- "Kth largest/smallest"
- "Most frequent"
- "Median"

### Template
```python
import heapq

def top_k_elements(arr, k):
    min_heap = []
    
    for num in arr:
        heapq.heappush(min_heap, num)
        if len(min_heap) > k:
            heapq.heappop(min_heap)
    
    return min_heap
```

### Classic Problems
- Kth Largest Element
- Top K Frequent Elements
- Find Median from Data Stream
- Merge K Sorted Lists

### Time/Space
- Time: O(n log k)
- Space: O(k)

---

## Pattern 10: Graph BFS/DFS

### When to Use
- Graph traversal
- Connected components
- Shortest path (BFS)
- Detect cycles

### Key Indicators
- "Graph"
- "Connected"
- "Island"
- "Shortest path"

### Template (BFS)
```python
from collections import deque

def bfs_graph(graph, start):
    visited = set()
    queue = deque([start])
    visited.add(start)
    
    while queue:
        node = queue.popleft()
        
        for neighbor in graph[node]:
            if neighbor not in visited:
                visited.add(neighbor)
                queue.append(neighbor)
    
    return visited
```

### Template (DFS)
```python
def dfs_graph(graph, node, visited=None):
    if visited is None:
        visited = set()
    
    visited.add(node)
    
    for neighbor in graph[node]:
        if neighbor not in visited:
            dfs_graph(graph, neighbor, visited)
    
    return visited
```

### Classic Problems
- Number of Islands
- Clone Graph
- Course Schedule
- Pacific Atlantic Water Flow

### Time/Space
- Time: O(V + E)
- Space: O(V)

---

## Pattern 11: Backtracking

### When to Use
- Generate all combinations/permutations
- Solve constraint satisfaction
- Find all solutions
- Decision problems

### Key Indicators
- "All possible"
- "Generate"
- "Combinations"
- "Permutations"

### Template
```python
def backtrack(nums, path, result):
    # Base case
    if is_solution(path):
        result.append(path[:])
        return
    
    # Try all possibilities
    for choice in get_choices(nums, path):
        # Make choice
        path.append(choice)
        
        # Recurse
        backtrack(nums, path, result)
        
        # Undo choice (backtrack)
        path.pop()
```

### Classic Problems
- Subsets
- Permutations
- Combination Sum
- Word Search

### Time/Space
- Time: O(2^n) or O(n!)
- Space: O(n) for recursion

---

## Pattern 12: Dynamic Programming

### When to Use
- Optimization problems (max/min)
- Count ways
- Overlapping subproblems
- Optimal substructure

### Key Indicators
- "Maximum/Minimum"
- "Count ways"
- "Optimal"
- Can be broken into subproblems

### Template (1D DP)
```python
def dp_1d(nums):
    n = len(nums)
    dp = [0] * (n + 1)
    dp[0] = base_case
    
    for i in range(1, n + 1):
        dp[i] = transition_function(dp, i)
    
    return dp[n]
```

### Template (2D DP)
```python
def dp_2d(s1, s2):
    m, n = len(s1), len(s2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # Initialize base cases
    for i in range(m + 1):
        dp[i][0] = initialize(i)
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            dp[i][j] = transition_function(dp, i, j)
    
    return dp[m][n]
```

### Classic Problems
- Climbing Stairs
- Coin Change
- Longest Common Subsequence
- Edit Distance

### Time/Space
- Time: O(n) or O(n^2)
- Space: O(n) or O(n^2) (can optimize)

---

## Pattern 13: Intervals

### When to Use
- Overlapping intervals
- Merge/insert intervals
- Scheduling problems

### Key Indicators
- "Intervals"
- "Meetings"
- "Overlapping"
- "Merge"

### Template
```python
def merge_intervals(intervals):
    if not intervals:
        return []
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    merged = [intervals[0]]
    
    for current in intervals[1:]:
        last = merged[-1]
        
        if current[0] <= last[1]:  # Overlapping
            merged[-1] = [last[0], max(last[1], current[1])]
        else:
            merged.append(current)
    
    return merged
```

### Classic Problems
- Merge Intervals
- Insert Interval
- Meeting Rooms II
- Non-overlapping Intervals

### Time/Space
- Time: O(n log n)
- Space: O(n)

---

## Pattern 14: Topological Sort

### When to Use
- Dependency resolution
- Course scheduling
- Build order
- Directed acyclic graph (DAG)

### Key Indicators
- "Prerequisites"
- "Dependencies"
- "Order"
- "Schedule"

### Template (Kahn's Algorithm)
```python
from collections import deque, defaultdict

def topological_sort(n, edges):
    graph = defaultdict(list)
    in_degree = [0] * n
    
    # Build graph
    for u, v in edges:
        graph[u].append(v)
        in_degree[v] += 1
    
    # Start with nodes having no dependencies
    queue = deque([i for i in range(n) if in_degree[i] == 0])
    result = []
    
    while queue:
        node = queue.popleft()
        result.append(node)
        
        for neighbor in graph[node]:
            in_degree[neighbor] -= 1
            if in_degree[neighbor] == 0:
                queue.append(neighbor)
    
    return result if len(result) == n else []
```

### Classic Problems
- Course Schedule
- Course Schedule II
- Alien Dictionary
- Minimum Height Trees

### Time/Space
- Time: O(V + E)
- Space: O(V + E)

---

## Pattern 15: Stack

### When to Use
- Matching pairs (parentheses, brackets)
- Next greater/smaller element
- Monotonic stack problems
- Expression evaluation
- Backtracking with history

### Key Indicators
- "Valid parentheses"
- "Next greater/smaller"
- "Temperature"
- "Nested structure"
- "Most recent"

### Template
```python
def stack_pattern(arr):
    stack = []
    result = []
    
    for i, num in enumerate(arr):
        # Pop elements while condition is met
        while stack and condition(stack[-1], num):
            stack.pop()
        
        # Process current element
        result.append(process(stack, num))
        
        # Push current element
        stack.append(num)
    
    return result
```

### Template (Monotonic Stack)
```python
def monotonic_stack(arr):
    stack = []  # Store indices
    result = [-1] * len(arr)
    
    for i in range(len(arr)):
        # Maintain monotonic property
        while stack and arr[stack[-1]] < arr[i]:
            idx = stack.pop()
            result[idx] = arr[i]
        
        stack.append(i)
    
    return result
```

### Classic Problems
- Valid Parentheses
- Min Stack
- Daily Temperatures
- Next Greater Element
- Trapping Rain Water
- Largest Rectangle in Histogram

### Time/Space
- Time: O(n)
- Space: O(n)

---

## Pattern 16: Bit Manipulation

### When to Use
- Problems involving binary representation
- Optimize space complexity
- Find single/duplicate numbers
- Set operations
- Power of two checks

### Key Indicators
- "Single number"
- "Missing number"
- "Power of 2"
- "Bit count"
- "XOR operations"

### Template
```python
def bit_manipulation():
    # Common operations
    
    # Check if ith bit is set
    is_set = (num & (1 << i)) != 0
    
    # Set ith bit
    num |= (1 << i)
    
    # Clear ith bit
    num &= ~(1 << i)
    
    # Toggle ith bit
    num ^= (1 << i)
    
    # Count set bits
    count = 0
    while num:
        count += num & 1
        num >>= 1
    
    # Check power of 2
    is_power_of_2 = num > 0 and (num & (num - 1)) == 0
    
    return num
```

### Key Bit Tricks
```python
# XOR properties
a ^ a = 0
a ^ 0 = a
a ^ b ^ b = a

# Get rightmost set bit
rightmost = num & -num

# Clear rightmost set bit
num & (num - 1)

# Swap two numbers
a ^= b
b ^= a
a ^= b
```

### Classic Problems
- Single Number
- Number of 1 Bits
- Counting Bits
- Missing Number
- Reverse Bits
- Sum of Two Integers

### Time/Space
- Time: O(1) to O(32) for integers
- Space: O(1)

---

## Pattern 17: Union Find (Disjoint Set)

### When to Use
- Connected components in undirected graph
- Detect cycles
- Dynamic connectivity
- Grouping elements

### Key Indicators
- "Connected components"
- "Number of islands" (alternative approach)
- "Redundant connection"
- "Network connectivity"
- "Accounts merge"

### Template
```python
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [1] * n
        self.components = n
    
    def find(self, x):
        # Path compression
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return False  # Already connected
        
        # Union by rank
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
        
        self.components -= 1
        return True
    
    def connected(self, x, y):
        return self.find(x) == self.find(y)
```

### Classic Problems
- Number of Islands (optimized)
- Redundant Connection
- Accounts Merge
- Graph Valid Tree
- Number of Provinces

### Time/Space
- Time: O(α(n)) ≈ O(1) amortized per operation
- Space: O(n)

---

## Pattern 18: Greedy Algorithms

### When to Use
- Local optimal choice leads to global optimal
- Sorting helps make decisions
- No need to reconsider past choices
- Scheduling/interval problems

### Key Indicators
- "Maximum/Minimum" with constraints
- "Scheduling"
- "Can reach"
- "Gas station"
- "Activity selection"

### Template
```python
def greedy_pattern(items):
    # Sort by the greedy criterion
    items.sort(key=lambda x: criterion(x))
    
    result = 0
    current_state = initial_state
    
    for item in items:
        # Make greedy choice
        if can_take(item, current_state):
            result += process(item)
            current_state = update(current_state, item)
    
    return result
```

### Classic Problems
- Jump Game
- Jump Game II
- Gas Station
- Non-overlapping Intervals
- Partition Labels
- Task Scheduler

### Time/Space
- Time: O(n log n) due to sorting
- Space: O(1) typically

---

## Pattern 19: Tries (Prefix Trees)

### When to Use
- Prefix-based searching
- Dictionary operations
- Autocomplete
- Spell checker
- Word games

### Key Indicators
- "Prefix"
- "Dictionary"
- "Word search"
- "Autocomplete"
- "Longest common prefix"

### Template
```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True
    
    def search(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.is_end
    
    def starts_with(self, prefix):
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        return True
```

### Classic Problems
- Implement Trie
- Design Add and Search Words
- Word Search II
- Longest Word in Dictionary
- Replace Words

### Time/Space
- Time: O(m) where m is word length
- Space: O(n * m) where n is number of words

---

## Pattern 20: Math & Geometry

### When to Use
- Matrix manipulation
- Geometric calculations
- Mathematical formulas
- Number theory

### Key Indicators
- "Rotate"
- "Spiral"
- "Matrix"
- "Prime"
- "Power"
- "Square root"

### Template (Matrix Rotation)
```python
def rotate_matrix(matrix):
    n = len(matrix)
    
    # Transpose
    for i in range(n):
        for j in range(i + 1, n):
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    
    # Reverse rows
    for i in range(n):
        matrix[i].reverse()
    
    return matrix
```

### Template (Spiral Matrix)
```python
def spiral_order(matrix):
    result = []
    top, bottom = 0, len(matrix) - 1
    left, right = 0, len(matrix[0]) - 1
    
    while top <= bottom and left <= right:
        # Right
        for col in range(left, right + 1):
            result.append(matrix[top][col])
        top += 1
        
        # Down
        for row in range(top, bottom + 1):
            result.append(matrix[row][right])
        right -= 1
        
        # Left
        if top <= bottom:
            for col in range(right, left - 1, -1):
                result.append(matrix[bottom][col])
            bottom -= 1
        
        # Up
        if left <= right:
            for row in range(bottom, top - 1, -1):
                result.append(matrix[row][left])
            left += 1
    
    return result
```

### Classic Problems
- Rotate Image
- Spiral Matrix
- Set Matrix Zeroes
- Happy Number
- Pow(x, n)
- Sqrt(x)

### Time/Space
- Time: O(n²) for matrix operations
- Space: O(1) typically (in-place)

---

## Pattern Decision Tree

```
Start
  |
  ├─ Array/String?
  │   ├─ Find pair/complement? → Hashing
  │   ├─ Sorted? → Two Pointers or Binary Search
  │   ├─ Subarray/Substring? → Sliding Window
  │   ├─ All combinations? → Backtracking
  │   ├─ Optimize? → Dynamic Programming
  │   ├─ Parentheses/brackets? → Stack
  │   ├─ Next greater/smaller? → Monotonic Stack
  │   └─ Binary operations? → Bit Manipulation
  │
  ├─ LinkedList?
  │   ├─ Cycle? → Fast/Slow Pointers
  │   ├─ Reverse? → In-place Reversal
  │   └─ Merge? → Two Pointers
  │
  ├─ Tree?
  │   ├─ Level by level? → BFS
  │   ├─ Path/Sum? → DFS
  │   ├─ Validate? → DFS
  │   └─ Word search? → Trie
  │
  ├─ Graph?
  │   ├─ Traversal? → BFS/DFS
  │   ├─ Shortest path? → BFS or Dijkstra
  │   ├─ Dependencies? → Topological Sort
  │   └─ Connected components? → Union Find or BFS/DFS
  │
  ├─ Matrix?
  │   ├─ Rotate/Spiral? → Math & Geometry
  │   └─ Search/Path? → BFS/DFS
  │
  ├─ Top K elements? → Heap
  ├─ Intervals? → Sort + Merge (Greedy)
  ├─ Scheduling? → Greedy
  ├─ Single/Missing number? → Bit Manipulation
  └─ Optimization? → Dynamic Programming
```

---

## Quick Reference Cheat Sheet

| Problem Type | Pattern | Time | Space |
|-------------|---------|------|-------|
| Find pair summing to target | Hashing | O(n) | O(n) |
| Find triplet summing to target | Two Pointers | O(n²) | O(1) |
| Max sum subarray size K | Sliding Window | O(n) | O(1) |
| LinkedList cycle | Fast/Slow | O(n) | O(1) |
| Reverse LinkedList | In-place | O(n) | O(1) |
| Level order traversal | BFS | O(n) | O(n) |
| Tree path sum | DFS | O(n) | O(h) |
| Search in sorted array | Binary Search | O(log n) | O(1) |
| Kth largest | Heap | O(n log k) | O(k) |
| Connected components | Graph DFS/BFS | O(V+E) | O(V) |
| All combinations | Backtracking | O(2^n) | O(n) |
| Coin change | DP | O(n*m) | O(n) |
| Merge intervals | Intervals | O(n log n) | O(n) |
| Course schedule | Topological Sort | O(V+E) | O(V+E) |
| Valid parentheses | Stack | O(n) | O(n) |
| Next greater element | Monotonic Stack | O(n) | O(n) |
| Single number | Bit Manipulation | O(n) | O(1) |
| Dynamic connectivity | Union Find | O(α(n)) | O(n) |
| Jump game | Greedy | O(n) | O(1) |
| Prefix search | Trie | O(m) | O(n*m) |
| Rotate matrix | Math/Geometry | O(n²) | O(1) |

---

## Common Edge Cases to Always Check

1. **Empty input**: `arr = []`, `s = ""`
2. **Single element**: `arr = [1]`
3. **Two elements**: `arr = [1, 2]`
4. **Duplicates**: `arr = [1, 1, 1]`
5. **Negative numbers**: `arr = [-1, -2]`
6. **Zero**: `arr = [0, 1, 2]`
7. **Large numbers**: Integer overflow
8. **Null/None**: `root = None`
9. **Cycles**: In graphs/LinkedLists
10. **Sorted vs unsorted**: Different approaches

---

**Pro Tip**: Print this guide and keep it next to you while solving problems. After each problem, identify which pattern(s) you used!

---

## Pattern Summary by Frequency in FAANG Interviews

### 🔥 **VERY HIGH** (Master These First)
1. Arrays & Hashing
2. Two Pointers
3. Sliding Window
4. Tree DFS/BFS
5. Dynamic Programming
6. Stack

### 🔴 **HIGH**
7. Binary Search
8. Graph BFS/DFS
9. LinkedList (Reversal, Fast/Slow)
10. Backtracking
11. Heap (Top K)

### 🟡 **MEDIUM**
12. Intervals & Greedy
13. Topological Sort
14. Bit Manipulation
15. Tries

### 🟢 **GOOD TO KNOW**
16. Union Find
17. Math & Geometry
18. Monotonic Stack (advanced)

---

**Remember**: Focus on understanding **WHY** a pattern works, not just memorizing solutions. Pattern recognition comes from solving 5-10 problems per pattern! 🚀
