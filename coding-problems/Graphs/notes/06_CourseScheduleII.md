# Course Schedule II

## Problem Link
LeetCode #210: https://leetcode.com/problems/course-schedule-ii/

## Difficulty
Medium

## Problem Description
There are a total of `numCourses` courses you have to take, labeled from `0` to `numCourses - 1`. You are given an array `prerequisites` where `prerequisites[i] = [ai, bi]` indicates that you **must** take course `bi` first if you want to take course `ai`.

- For example, the pair `[0, 1]`, indicates that to take course `0` you have to first take course `1`.

Return **the ordering of courses** you should take to finish all courses. If there are many valid answers, return **any** of them. If it is impossible to finish all courses, return **an empty array**.

**Example 1:**
```
Input: numCourses = 2, prerequisites = [[1,0]]
Output: [0,1]
Explanation: There are 2 courses. To take course 1, you should have finished course 0.
So the correct course order is [0,1].
```

**Example 2:**
```
Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
Output: [0,2,1,3] or [0,1,2,3]
Explanation: There are 4 courses. To take course 3, you should have finished both courses 1 and 2.
Both [0,1,2,3] and [0,2,1,3] are valid answers.
```

**Example 3:**
```
Input: numCourses = 1, prerequisites = []
Output: [0]
```

**Constraints:**
- 1 <= numCourses <= 2000
- 0 <= prerequisites.length <= numCourses * (numCourses - 1)
- prerequisites[i].length == 2
- 0 <= ai, bi < numCourses
- ai != bi
- All the pairs [ai, bi] are **distinct**

## Pattern Recognition
This is a **Topological Sort** problem. Key characteristics:
1. Return ordering (not just boolean like Course Schedule I)
2. Directed Acyclic Graph (DAG) required
3. Multiple valid orderings may exist
4. Empty array if cycle detected

**RELATIONSHIP TO COURSE SCHEDULE I:**
- Course Schedule I: "Can we finish?" → Cycle detection
- Course Schedule II: "What's the order?" → Topological sort + ordering

## Core Concept

**TOPOLOGICAL SORT:**
A linear ordering of vertices such that for every directed edge (u → v), vertex u comes before v in the ordering.

```
Graph:        Topological Order:
  0 → 1       [0, 1, 2, 3]
  0 → 2       or [0, 2, 1, 3]
  1 → 3       
  2 → 3       Both valid!
```

**KEY INSIGHT:**
- If graph has cycle → NO topological ordering exists
- If graph is DAG → At least one topological ordering exists
- May have multiple valid orderings

**TWO MAIN APPROACHES:**
1. **DFS with Post-order** (add to result after visiting all dependencies)
2. **BFS - Kahn's Algorithm** (process nodes with in-degree 0)

## Visual Explanation

```
Example: numCourses = 6, prerequisites = [[1,0],[2,0],[3,1],[3,2],[4,3],[5,4]]

Graph (Adjacency List):
    0 → 1
    0 → 2
    1 → 3
    2 → 3
    3 → 4
    4 → 5

Visual:
    0
   ╱ ╲
  1   2
   ╲ ╱
    3
    ↓
    4
    ↓
    5

DFS Post-order Approach:
1. Start DFS from 0
2. Visit 1, then 3, then 4, then 5 (deepest first)
3. Add to result in post-order (after all children visited)
4. Result built backwards: [5,4,3,1,2,0]
5. Reverse for correct order: [0,2,1,3,4,5] or [0,1,2,3,4,5]

BFS Kahn's Algorithm:
1. In-degrees: [0→0, 1→1, 2→1, 3→2, 4→1, 5→1]
2. Queue: [0] (in-degree 0)
3. Process 0, add 1,2 to queue: [1,2]
4. Process 1, decrement 3: [2]
5. Process 2, add 3 to queue: [3]
6. Process 3, add 4: [4]
7. Process 4, add 5: [5]
8. Process 5
9. Result: [0,1,2,3,4,5] or [0,2,1,3,4,5]
```

## Solution 1: BFS - Kahn's Algorithm (Recommended)

```python
from collections import deque

def findOrder(numCourses, prerequisites):
    """
    BFS topological sort using Kahn's algorithm.
    Most intuitive for this problem - builds order naturally.
    
    Time: O(V + E)
    Space: O(V + E)
    """
    # Build graph and calculate in-degrees
    graph = [[] for _ in range(numCourses)]
    in_degree = [0] * numCourses
    
    for course, prereq in prerequisites:
        graph[prereq].append(course)  # prereq → course
        in_degree[course] += 1
    
    # Start with courses having no prerequisites
    queue = deque([i for i in range(numCourses) if in_degree[i] == 0])
    result = []
    
    while queue:
        course = queue.popleft()
        result.append(course)
        
        # Process dependent courses
        for next_course in graph[course]:
            in_degree[next_course] -= 1
            
            # If all prerequisites satisfied, add to queue
            if in_degree[next_course] == 0:
                queue.append(next_course)
    
    # If we couldn't take all courses, cycle exists
    return result if len(result) == numCourses else []
```

**Key Points:**
- Natural order building (add courses as we "take" them)
- In-degree tracks remaining prerequisites
- Easy to verify completion (count == numCourses)
- Intuitive and clean

## Solution 2: DFS with Post-order

```python
def findOrder(numCourses, prerequisites):
    """
    DFS approach building result in reverse post-order.
    
    Time: O(V + E)
    Space: O(V + E)
    """
    # Build graph
    graph = [[] for _ in range(numCourses)]
    for course, prereq in prerequisites:
        graph[course].append(prereq)  # course depends on prereq
    
    # States: 0 = unvisited, 1 = visiting, 2 = visited
    state = [0] * numCourses
    result = []
    
    def has_cycle(course):
        """DFS that adds to result in post-order"""
        if state[course] == 1:  # Cycle detected
            return True
        
        if state[course] == 2:  # Already processed
            return False
        
        state[course] = 1  # Mark as visiting
        
        # Visit all prerequisites first
        for prereq in graph[course]:
            if has_cycle(prereq):
                return True
        
        state[course] = 2  # Mark as visited
        result.append(course)  # Add after all dependencies processed
        return False
    
    # Process all courses
    for course in range(numCourses):
        if state[course] == 0:
            if has_cycle(course):
                return []
    
    return result
```

**Key Points:**
- Add to result AFTER visiting all dependencies
- Result naturally in correct topological order
- Cycle detection included
- May produce different valid ordering than BFS

## Solution 3: DFS with Reverse Post-order

```python
def findOrder(numCourses, prerequisites):
    """
    DFS approach - reverse the result at end.
    
    Time: O(V + E)
    Space: O(V + E)
    """
    graph = [[] for _ in range(numCourses)]
    for course, prereq in prerequisites:
        graph[prereq].append(course)  # prereq → course
    
    state = [0] * numCourses
    result = []
    
    def dfs(course):
        if state[course] == 1:
            return False  # Cycle
        
        if state[course] == 2:
            return True  # Already visited
        
        state[course] = 1
        
        for next_course in graph[course]:
            if not dfs(next_course):
                return False
        
        state[course] = 2
        result.append(course)
        return True
    
    for course in range(numCourses):
        if state[course] == 0:
            if not dfs(course):
                return []
    
    return result[::-1]  # Reverse for correct order
```

## Comprehensive Implementations

### Python Implementation (All Approaches)

```python
class Solution:
    def findOrder_bfs_kahn(self, numCourses, prerequisites):
        """BFS - Most intuitive"""
        from collections import deque
        
        graph = [[] for _ in range(numCourses)]
        in_degree = [0] * numCourses
        
        for course, prereq in prerequisites:
            graph[prereq].append(course)
            in_degree[course] += 1
        
        queue = deque([i for i in range(numCourses) if in_degree[i] == 0])
        result = []
        
        while queue:
            course = queue.popleft()
            result.append(course)
            
            for next_course in graph[course]:
                in_degree[next_course] -= 1
                if in_degree[next_course] == 0:
                    queue.append(next_course)
        
        return result if len(result) == numCourses else []
    
    def findOrder_dfs_postorder(self, numCourses, prerequisites):
        """DFS with post-order"""
        graph = [[] for _ in range(numCourses)]
        for course, prereq in prerequisites:
            graph[course].append(prereq)
        
        state = [0] * numCourses
        result = []
        
        def dfs(course):
            if state[course] == 1:
                return False
            if state[course] == 2:
                return True
            
            state[course] = 1
            
            for prereq in graph[course]:
                if not dfs(prereq):
                    return False
            
            state[course] = 2
            result.append(course)
            return True
        
        for i in range(numCourses):
            if state[i] == 0:
                if not dfs(i):
                    return []
        
        return result
    
    def findOrder_dfs_sets(self, numCourses, prerequisites):
        """DFS with sets (cleaner for some)"""
        graph = [[] for _ in range(numCourses)]
        for course, prereq in prerequisites:
            graph[course].append(prereq)
        
        visited = set()
        visiting = set()
        result = []
        
        def dfs(course):
            if course in visiting:
                return False
            if course in visited:
                return True
            
            visiting.add(course)
            
            for prereq in graph[course]:
                if not dfs(prereq):
                    return False
            
            visiting.remove(course)
            visited.add(course)
            result.append(course)
            return True
        
        for i in range(numCourses):
            if i not in visited:
                if not dfs(i):
                    return []
        
        return result
    
    def findOrder_iterative(self, numCourses, prerequisites):
        """Iterative DFS"""
        graph = [[] for _ in range(numCourses)]
        for course, prereq in prerequisites:
            graph[prereq].append(course)
        
        state = [0] * numCourses
        result = []
        
        for start in range(numCourses):
            if state[start] != 0:
                continue
            
            stack = [start]
            path = []
            
            while stack:
                course = stack[-1]
                
                if state[course] == 2:
                    stack.pop()
                    continue
                
                if state[course] == 0:
                    state[course] = 1
                    path.append(course)
                    
                    for next_course in graph[course]:
                        if state[next_course] == 1:
                            return []  # Cycle
                        if state[next_course] == 0:
                            stack.append(next_course)
                
                elif state[course] == 1:
                    # All children visited
                    state[course] = 2
                    result.append(course)
                    stack.pop()
        
        return result[::-1]
```

### Java Implementation

```java
class Solution {
    // BFS Approach (Kahn's Algorithm)
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[numCourses];
        
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] prereq : prerequisites) {
            graph.get(prereq[1]).add(prereq[0]);
            inDegree[prereq[0]]++;
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int[] result = new int[numCourses];
        int index = 0;
        
        while (!queue.isEmpty()) {
            int course = queue.poll();
            result[index++] = course;
            
            for (int next : graph.get(course)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return index == numCourses ? result : new int[0];
    }
    
    // DFS Approach
    public int[] findOrderDFS(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] prereq : prerequisites) {
            graph.get(prereq[0]).add(prereq[1]);
        }
        
        int[] state = new int[numCourses];
        List<Integer> result = new ArrayList<>();
        
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0) {
                if (!dfs(i, graph, state, result)) {
                    return new int[0];
                }
            }
        }
        
        int[] resultArray = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            resultArray[i] = result.get(i);
        }
        
        return resultArray;
    }
    
    private boolean dfs(int course, List<List<Integer>> graph, 
                       int[] state, List<Integer> result) {
        if (state[course] == 1) {
            return false; // Cycle
        }
        
        if (state[course] == 2) {
            return true;
        }
        
        state[course] = 1;
        
        for (int prereq : graph.get(course)) {
            if (!dfs(prereq, graph, state, result)) {
                return false;
            }
        }
        
        state[course] = 2;
        result.add(course);
        return true;
    }
}
```

### C++ Implementation

```cpp
class Solution {
public:
    // BFS Approach
    vector<int> findOrder(int numCourses, vector<vector<int>>& prerequisites) {
        vector<vector<int>> graph(numCourses);
        vector<int> inDegree(numCourses, 0);
        
        for (auto& prereq : prerequisites) {
            graph[prereq[1]].push_back(prereq[0]);
            inDegree[prereq[0]]++;
        }
        
        queue<int> q;
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                q.push(i);
            }
        }
        
        vector<int> result;
        
        while (!q.empty()) {
            int course = q.front();
            q.pop();
            result.push_back(course);
            
            for (int next : graph[course]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    q.push(next);
                }
            }
        }
        
        return result.size() == numCourses ? result : vector<int>();
    }
    
    // DFS Approach
    vector<int> findOrderDFS(int numCourses, vector<vector<int>>& prerequisites) {
        vector<vector<int>> graph(numCourses);
        
        for (auto& prereq : prerequisites) {
            graph[prereq[0]].push_back(prereq[1]);
        }
        
        vector<int> state(numCourses, 0);
        vector<int> result;
        
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0) {
                if (!dfs(i, graph, state, result)) {
                    return {};
                }
            }
        }
        
        return result;
    }
    
private:
    bool dfs(int course, vector<vector<int>>& graph, 
             vector<int>& state, vector<int>& result) {
        if (state[course] == 1) {
            return false;
        }
        
        if (state[course] == 2) {
            return true;
        }
        
        state[course] = 1;
        
        for (int prereq : graph[course]) {
            if (!dfs(prereq, graph, state, result)) {
                return false;
            }
        }
        
        state[course] = 2;
        result.push_back(course);
        return true;
    }
};
```

### JavaScript Implementation

```javascript
/**
 * BFS Approach (Kahn's Algorithm)
 */
var findOrder = function(numCourses, prerequisites) {
    const graph = Array.from({length: numCourses}, () => []);
    const inDegree = new Array(numCourses).fill(0);
    
    for (const [course, prereq] of prerequisites) {
        graph[prereq].push(course);
        inDegree[course]++;
    }
    
    const queue = [];
    for (let i = 0; i < numCourses; i++) {
        if (inDegree[i] === 0) {
            queue.push(i);
        }
    }
    
    const result = [];
    
    while (queue.length > 0) {
        const course = queue.shift();
        result.push(course);
        
        for (const next of graph[course]) {
            inDegree[next]--;
            if (inDegree[next] === 0) {
                queue.push(next);
            }
        }
    }
    
    return result.length === numCourses ? result : [];
};

/**
 * DFS Approach
 */
var findOrderDFS = function(numCourses, prerequisites) {
    const graph = Array.from({length: numCourses}, () => []);
    
    for (const [course, prereq] of prerequisites) {
        graph[course].push(prereq);
    }
    
    const state = new Array(numCourses).fill(0);
    const result = [];
    
    function dfs(course) {
        if (state[course] === 1) {
            return false;
        }
        
        if (state[course] === 2) {
            return true;
        }
        
        state[course] = 1;
        
        for (const prereq of graph[course]) {
            if (!dfs(prereq)) {
                return false;
            }
        }
        
        state[course] = 2;
        result.push(course);
        return true;
    }
    
    for (let i = 0; i < numCourses; i++) {
        if (state[i] === 0) {
            if (!dfs(i)) {
                return [];
            }
        }
    }
    
    return result;
};
```

## Complexity Analysis

### Time Complexity: O(V + E)
- **V** = numCourses
- **E** = prerequisites.length
- Build graph: O(E)
- Visit each vertex once: O(V)
- Process each edge once: O(E)
- Total: O(V + E)

### Space Complexity: O(V + E)
- **Graph**: O(V + E)
- **In-degree array**: O(V)
- **Queue/Stack**: O(V)
- **Result array**: O(V)
- **Recursion stack (DFS)**: O(V)

## Edge Cases and Testing

```python
def test_course_schedule_ii():
    solution = Solution()
    
    # Test 1: Simple linear
    result1 = solution.findOrder(2, [[1,0]])
    assert result1 == [0,1]
    
    # Test 2: Cycle
    result2 = solution.findOrder(2, [[1,0],[0,1]])
    assert result2 == []
    
    # Test 3: No prerequisites
    result3 = solution.findOrder(3, [])
    assert len(result3) == 3
    
    # Test 4: Multiple valid orders
    result4 = solution.findOrder(4, [[1,0],[2,0],[3,1],[3,2]])
    assert len(result4) == 4
    assert result4.index(0) < result4.index(1)
    assert result4.index(0) < result4.index(2)
    assert result4.index(1) < result4.index(3)
    assert result4.index(2) < result4.index(3)
    
    # Test 5: Single course
    result5 = solution.findOrder(1, [])
    assert result5 == [0]
    
    # Test 6: Long chain
    result6 = solution.findOrder(4, [[1,0],[2,1],[3,2]])
    assert result6 == [0,1,2,3]
    
    # Test 7: Diamond pattern
    result7 = solution.findOrder(4, [[2,0],[2,1],[3,2]])
    assert len(result7) == 4
    assert result7.index(0) < result7.index(2)
    assert result7.index(1) < result7.index(2)
    assert result7.index(2) < result7.index(3)
    
    print("All test cases passed!")

test_course_schedule_ii()
```

## Common Pitfalls

### 1. Wrong Graph Direction for DFS
```python
# WRONG: For DFS post-order, need course → prereq
for course, prereq in prerequisites:
    graph[prereq].append(course)  # Wrong for DFS!

# CORRECT: For DFS post-order
for course, prereq in prerequisites:
    graph[course].append(prereq)  # course depends on prereq
```

### 2. Forgetting to Check Full Completion
```python
# WRONG: Return result without checking
return result

# CORRECT: Check all courses processed
return result if len(result) == numCourses else []
```

### 3. Not Reversing DFS Result (if needed)
```python
# Depends on graph direction and when you add to result
# Make sure final order is valid topological sort
```

### 4. Empty Prerequisites Handling
```python
# Should still return valid ordering [0, 1, 2, ...]
# Don't return [] for empty prerequisites!
```

## BFS vs DFS Comparison

```
Aspect               | BFS (Kahn's)           | DFS (Post-order)
---------------------|------------------------|---------------------
Result Building      | Natural order          | Reverse post-order
Intuition            | "Take courses"         | "Finish dependencies"
Cycle Detection      | Count < numCourses     | State check
Memory               | Queue                  | Recursion stack
Implementation       | Iterative              | Recursive (cleaner)
Ordering Difference  | Yes (different valid)  | Yes
When to Use          | Need natural build     | Recursive cleaner
```

## Related Problems

1. **Course Schedule** (LeetCode 207)
   - Boolean version (can finish?)
   - Same cycle detection

2. **Alien Dictionary** (LeetCode 269)
   - Build graph from strings
   - Topological sort for character order

3. **Sequence Reconstruction** (LeetCode 444)
   - Verify unique topological order
   - Check if sequences match

4. **Parallel Courses** (LeetCode 1136)
   - Minimum semesters needed
   - Level-based topological sort

5. **Minimum Height Trees** (LeetCode 310)
   - Find tree centers
   - Reverse topological sort

## Advanced Variations

### Variation 1: Return All Valid Orderings
```python
def findAllOrders(numCourses, prerequisites):
    """Return all valid topological orderings (exponential!)"""
    graph = [[] for _ in range(numCourses)]
    in_degree = [0] * numCourses
    
    for course, prereq in prerequisites:
        graph[prereq].append(course)
        in_degree[course] += 1
    
    def backtrack(current_order, remaining_in_degree):
        if len(current_order) == numCourses:
            return [current_order[:]]
        
        results = []
        available = [i for i in range(numCourses) 
                    if remaining_in_degree[i] == 0 and i not in current_order]
        
        for course in available:
            current_order.append(course)
            new_in_degree = remaining_in_degree[:]
            new_in_degree[course] = -1  # Mark as taken
            
            for next_course in graph[course]:
                new_in_degree[next_course] -= 1
            
            results.extend(backtrack(current_order, new_in_degree))
            current_order.pop()
        
        return results
    
    return backtrack([], in_degree)
```

### Variation 2: Lexicographically Smallest Order
```python
def findLexicalOrder(numCourses, prerequisites):
    """Return lexicographically smallest valid ordering"""
    import heapq
    
    graph = [[] for _ in range(numCourses)]
    in_degree = [0] * numCourses
    
    for course, prereq in prerequisites:
        graph[prereq].append(course)
        in_degree[course] += 1
    
    # Use min-heap instead of queue
    heap = [i for i in range(numCourses) if in_degree[i] == 0]
    heapq.heapify(heap)
    
    result = []
    
    while heap:
        course = heapq.heappop(heap)
        result.append(course)
        
        for next_course in graph[course]:
            in_degree[next_course] -= 1
            if in_degree[next_course] == 0:
                heapq.heappush(heap, next_course)
    
    return result if len(result) == numCourses else []
```

### Variation 3: Minimum Semesters
```python
def minimumSemesters(numCourses, prerequisites):
    """Find minimum number of semesters needed"""
    from collections import deque
    
    graph = [[] for _ in range(numCourses)]
    in_degree = [0] * numCourses
    
    for course, prereq in prerequisites:
        graph[prereq].append(course)
        in_degree[course] += 1
    
    queue = deque([i for i in range(numCourses) if in_degree[i] == 0])
    semesters = 0
    courses_taken = 0
    
    while queue:
        semesters += 1
        
        # Take all courses available this semester
        for _ in range(len(queue)):
            course = queue.popleft()
            courses_taken += 1
            
            for next_course in graph[course]:
                in_degree[next_course] -= 1
                if in_degree[next_course] == 0:
                    queue.append(next_course)
    
    return semesters if courses_taken == numCourses else -1
```

### Variation 4: Course Schedule with Costs
```python
def minimumCostSchedule(numCourses, prerequisites, costs):
    """Find ordering that minimizes total cost"""
    # Use topological sort + greedy selection based on cost
    graph = [[] for _ in range(numCourses)]
    in_degree = [0] * numCourses
    
    for course, prereq in prerequisites:
        graph[prereq].append(course)
        in_degree[course] += 1
    
    # Priority queue: (cost, course)
    import heapq
    pq = [(costs[i], i) for i in range(numCourses) if in_degree[i] == 0]
    heapq.heapify(pq)
    
    result = []
    total_cost = 0
    
    while pq:
        cost, course = heapq.heappop(pq)
        result.append(course)
        total_cost += cost
        
        for next_course in graph[course]:
            in_degree[next_course] -= 1
            if in_degree[next_course] == 0:
                heapq.heappush(pq, (costs[next_course], next_course))
    
    return (result, total_cost) if len(result) == numCourses else ([], -1)
```

## Interview Tips

1. **Clarification Questions:**
   - Multiple valid orderings? (Any is fine)
   - Return format? (Array or list)
   - Guaranteed unique prerequisites?
   - What to return if impossible? (Empty array)

2. **Approach Discussion:**
   - "Need topological sort to find ordering"
   - "Two main approaches: BFS (Kahn's) or DFS (post-order)"
   - "I'll use BFS as it's more intuitive"

3. **Implementation Tips:**
   - BFS builds order naturally
   - DFS requires understanding post-order
   - Both detect cycles automatically

4. **Common Mistakes:**
   - Wrong graph direction
   - Forgetting to check completion
   - Not handling empty prerequisites

5. **Follow-ups:**
   - "All possible orderings?" → Backtracking (exponential)
   - "Lexicographically smallest?" → Min-heap instead of queue
   - "Minimum semesters?" → Level-by-level BFS
   - "With course costs?" → Priority queue

6. **Testing:**
   - Simple chain
   - Cycle detection
   - Multiple valid orders
   - Empty prerequisites
   - Single course

## Key Takeaways

1. **Problem Type**: Topological sort with ordering
2. **BFS (Kahn's)**: Natural order building, intuitive
3. **DFS (Post-order)**: Add after dependencies, elegant
4. **Multiple Valid Orders**: Problem allows any valid ordering
5. **Cycle Detection**: Automatically handled by counting/states
6. **Graph Direction**: Depends on DFS vs BFS approach

This problem is the definitive example of topological sorting in action!
