# Course Schedule

## Problem Link
LeetCode #207: https://leetcode.com/problems/course-schedule/

## Difficulty
Medium

## Problem Description
There are a total of `numCourses` courses you have to take, labeled from `0` to `numCourses - 1`. You are given an array `prerequisites` where `prerequisites[i] = [ai, bi]` indicates that you **must** take course `bi` first if you want to take course `ai`.

- For example, the pair `[0, 1]`, indicates that to take course `0` you have to first take course `1`.

Return `true` if you can finish all courses. Otherwise, return `false`.

**Example 1:**
```
Input: numCourses = 2, prerequisites = [[1,0]]
Output: true
Explanation: There are 2 courses total.
To take course 1, you need to first take course 0. This is possible.
```

**Example 2:**
```
Input: numCourses = 2, prerequisites = [[1,0],[0,1]]
Output: false
Explanation: There are 2 courses total.
To take course 1, you need course 0, and to take course 0, you need course 1.
This creates a cycle, so it's impossible.
```

**Example 3:**
```
Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
Output: true
Explanation: There are 4 courses. Multiple valid orderings exist.
```

**Constraints:**
- 1 <= numCourses <= 2000
- 0 <= prerequisites.length <= 5000
- prerequisites[i].length == 2
- 0 <= ai, bi < numCourses
- All the pairs prerequisites[i] are **unique**

## Pattern Recognition
This is a **Topological Sort (Cycle Detection)** problem. Key characteristics:
1. Directed graph (prerequisites have direction)
2. Need to detect cycles
3. If cycle exists → impossible to complete all courses
4. If no cycle → valid ordering exists

**FUNDAMENTAL INSIGHT:** 
- Course prerequisites form a Directed Acyclic Graph (DAG) if possible
- A cycle means circular dependency (impossible)
- Return true if graph is DAG, false if cycle exists

## Core Concept

**PROBLEM AS GRAPH:**
```
Courses: 0, 1, 2, 3
Prerequisites: [[1,0], [2,0], [3,1], [3,2]]

Graph representation:
    0 → 1 → 3
    ↓     ↗
    2 ────

Course 0 has no prerequisites (can take first)
Course 1 requires course 0
Course 2 requires course 0
Course 3 requires courses 1 and 2

Valid? YES (no cycle)
```

**CYCLE EXAMPLE:**
```
Prerequisites: [[1,0], [0,1]]

Graph:
    0 ⟷ 1

Circular dependency! IMPOSSIBLE
```

**TWO MAIN APPROACHES:**
1. **DFS with Cycle Detection** (using recursion stack)
2. **BFS with Topological Sort** (Kahn's Algorithm)

## Visual Explanation

```
Example: numCourses = 6, prerequisites = [[1,0],[2,0],[3,1],[3,2],[4,3],[5,4]]

Graph:
    0
   ╱ ╲
  1   2
   ╲ ╱
    3
    │
    4
    │
    5

Topological Order (valid): 0 → 1 → 2 → 3 → 4 → 5
Or: 0 → 2 → 1 → 3 → 4 → 5
Multiple valid orders exist!

Example with CYCLE:
Prerequisites: [[1,0],[2,1],[0,2]]

Graph:
    0 → 2
    ↑   ↓
    └── 1

Cycle detected: 0 → 2 → 1 → 0
IMPOSSIBLE to complete all courses!
```

## Solution 1: DFS with Cycle Detection

```python
def canFinish(numCourses, prerequisites):
    """
    DFS approach using three states:
    - UNVISITED (0): Not processed
    - VISITING (1): Currently in DFS path (recursion stack)
    - VISITED (2): Completely processed
    
    If we encounter VISITING node again → CYCLE
    
    Time: O(V + E) where V = courses, E = prerequisites
    Space: O(V + E) for graph + O(V) for recursion
    """
    # Build adjacency list
    graph = [[] for _ in range(numCourses)]
    for course, prereq in prerequisites:
        graph[course].append(prereq)  # course depends on prereq
    
    # States: 0 = unvisited, 1 = visiting, 2 = visited
    state = [0] * numCourses
    
    def has_cycle(course):
        """Returns True if cycle detected"""
        if state[course] == 1:  # Currently visiting → cycle!
            return True
        
        if state[course] == 2:  # Already visited → no cycle
            return False
        
        # Mark as visiting
        state[course] = 1
        
        # Check all dependencies
        for prereq in graph[course]:
            if has_cycle(prereq):
                return True
        
        # Mark as visited
        state[course] = 2
        return False
    
    # Check each course
    for course in range(numCourses):
        if state[course] == 0:  # Unvisited
            if has_cycle(course):
                return False
    
    return True
```

**Key Points:**
- Three states to track cycle
- State 1 (visiting) = currently in DFS recursion stack
- Encountering visiting node again = cycle
- Clean and intuitive

## Solution 2: BFS - Kahn's Algorithm (Topological Sort)

```python
from collections import deque

def canFinish(numCourses, prerequisites):
    """
    Kahn's Algorithm for topological sort.
    
    Idea:
    1. Calculate in-degree for each node
    2. Start with nodes having in-degree 0 (no prerequisites)
    3. Process nodes, decrement in-degree of neighbors
    4. If all nodes processed → no cycle
    
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
    courses_taken = 0
    
    while queue:
        course = queue.popleft()
        courses_taken += 1
        
        # Remove this course and update dependent courses
        for next_course in graph[course]:
            in_degree[next_course] -= 1
            
            # If no more prerequisites, can take this course
            if in_degree[next_course] == 0:
                queue.append(next_course)
    
    # If we took all courses, no cycle exists
    return courses_taken == numCourses
```

**Key Points:**
- In-degree = number of prerequisites
- Process nodes with in-degree 0 first
- Gradually reduce in-degrees
- If can't process all nodes → cycle exists

## Solution 3: DFS with Visited Set

```python
def canFinish(numCourses, prerequisites):
    """
    Simpler DFS using visiting set for cycle detection.
    
    Time: O(V + E)
    Space: O(V + E)
    """
    # Build graph
    graph = [[] for _ in range(numCourses)]
    for course, prereq in prerequisites:
        graph[course].append(prereq)
    
    visited = set()
    visiting = set()
    
    def has_cycle(course):
        if course in visiting:  # Cycle detected!
            return True
        
        if course in visited:  # Already checked
            return False
        
        visiting.add(course)
        
        for prereq in graph[course]:
            if has_cycle(prereq):
                return True
        
        visiting.remove(course)
        visited.add(course)
        return False
    
    for course in range(numCourses):
        if course not in visited:
            if has_cycle(course):
                return False
    
    return True
```

## Comprehensive Implementations

### Python Implementation (All Approaches)

```python
class Solution:
    def canFinish_dfs_three_states(self, numCourses, prerequisites):
        """DFS with three states - most robust"""
        graph = [[] for _ in range(numCourses)]
        for course, prereq in prerequisites:
            graph[course].append(prereq)
        
        # 0: unvisited, 1: visiting, 2: visited
        state = [0] * numCourses
        
        def has_cycle(course):
            if state[course] == 1:
                return True
            if state[course] == 2:
                return False
            
            state[course] = 1
            for prereq in graph[course]:
                if has_cycle(prereq):
                    return True
            
            state[course] = 2
            return False
        
        return not any(has_cycle(i) for i in range(numCourses) if state[i] == 0)
    
    def canFinish_bfs_kahn(self, numCourses, prerequisites):
        """BFS using Kahn's algorithm"""
        from collections import deque
        
        graph = [[] for _ in range(numCourses)]
        in_degree = [0] * numCourses
        
        for course, prereq in prerequisites:
            graph[prereq].append(course)
            in_degree[course] += 1
        
        queue = deque([i for i in range(numCourses) if in_degree[i] == 0])
        count = 0
        
        while queue:
            course = queue.popleft()
            count += 1
            
            for next_course in graph[course]:
                in_degree[next_course] -= 1
                if in_degree[next_course] == 0:
                    queue.append(next_course)
        
        return count == numCourses
    
    def canFinish_dfs_sets(self, numCourses, prerequisites):
        """DFS with visiting and visited sets"""
        graph = [[] for _ in range(numCourses)]
        for course, prereq in prerequisites:
            graph[course].append(prereq)
        
        visited = set()
        visiting = set()
        
        def has_cycle(course):
            if course in visiting:
                return True
            if course in visited:
                return False
            
            visiting.add(course)
            
            for prereq in graph[course]:
                if has_cycle(prereq):
                    return True
            
            visiting.remove(course)
            visited.add(course)
            return False
        
        return not any(has_cycle(i) for i in range(numCourses) if i not in visited)
    
    def canFinish_iterative_dfs(self, numCourses, prerequisites):
        """Iterative DFS - no recursion"""
        graph = [[] for _ in range(numCourses)]
        for course, prereq in prerequisites:
            graph[course].append(prereq)
        
        state = [0] * numCourses
        
        for start in range(numCourses):
            if state[start] != 0:
                continue
            
            stack = [start]
            path = set()
            
            while stack:
                course = stack[-1]
                
                if course in path:
                    # Back edge found → cycle
                    return False
                
                if state[course] == 2:
                    stack.pop()
                    continue
                
                if state[course] == 0:
                    state[course] = 1
                    path.add(course)
                    
                    all_visited = True
                    for prereq in graph[course]:
                        if state[prereq] == 0:
                            stack.append(prereq)
                            all_visited = False
                        elif state[prereq] == 1:
                            return False  # Cycle
                    
                    if all_visited:
                        state[course] = 2
                        path.remove(course)
                        stack.pop()
        
        return True
```

### Java Implementation

```java
class Solution {
    // DFS Approach
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] prereq : prerequisites) {
            graph.get(prereq[0]).add(prereq[1]);
        }
        
        int[] state = new int[numCourses]; // 0: unvisited, 1: visiting, 2: visited
        
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0) {
                if (hasCycle(i, graph, state)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    private boolean hasCycle(int course, List<List<Integer>> graph, int[] state) {
        if (state[course] == 1) {
            return true; // Cycle detected
        }
        
        if (state[course] == 2) {
            return false; // Already visited
        }
        
        state[course] = 1; // Mark as visiting
        
        for (int prereq : graph.get(course)) {
            if (hasCycle(prereq, graph, state)) {
                return true;
            }
        }
        
        state[course] = 2; // Mark as visited
        return false;
    }
    
    // BFS Approach (Kahn's Algorithm)
    public boolean canFinishBFS(int numCourses, int[][] prerequisites) {
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
        
        int count = 0;
        
        while (!queue.isEmpty()) {
            int course = queue.poll();
            count++;
            
            for (int next : graph.get(course)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return count == numCourses;
    }
}
```

### C++ Implementation

```cpp
class Solution {
public:
    // DFS Approach
    bool canFinish(int numCourses, vector<vector<int>>& prerequisites) {
        vector<vector<int>> graph(numCourses);
        
        for (auto& prereq : prerequisites) {
            graph[prereq[0]].push_back(prereq[1]);
        }
        
        vector<int> state(numCourses, 0);
        
        for (int i = 0; i < numCourses; i++) {
            if (state[i] == 0) {
                if (hasCycle(i, graph, state)) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
private:
    bool hasCycle(int course, vector<vector<int>>& graph, vector<int>& state) {
        if (state[course] == 1) {
            return true;
        }
        
        if (state[course] == 2) {
            return false;
        }
        
        state[course] = 1;
        
        for (int prereq : graph[course]) {
            if (hasCycle(prereq, graph, state)) {
                return true;
            }
        }
        
        state[course] = 2;
        return false;
    }
    
    // BFS Approach
    bool canFinishBFS(int numCourses, vector<vector<int>>& prerequisites) {
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
        
        int count = 0;
        
        while (!q.empty()) {
            int course = q.front();
            q.pop();
            count++;
            
            for (int next : graph[course]) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    q.push(next);
                }
            }
        }
        
        return count == numCourses;
    }
};
```

### JavaScript Implementation

```javascript
/**
 * DFS Approach
 */
var canFinish = function(numCourses, prerequisites) {
    const graph = Array.from({length: numCourses}, () => []);
    
    for (const [course, prereq] of prerequisites) {
        graph[course].push(prereq);
    }
    
    const state = new Array(numCourses).fill(0);
    
    function hasCycle(course) {
        if (state[course] === 1) {
            return true;
        }
        
        if (state[course] === 2) {
            return false;
        }
        
        state[course] = 1;
        
        for (const prereq of graph[course]) {
            if (hasCycle(prereq)) {
                return true;
            }
        }
        
        state[course] = 2;
        return false;
    }
    
    for (let i = 0; i < numCourses; i++) {
        if (state[i] === 0) {
            if (hasCycle(i)) {
                return false;
            }
        }
    }
    
    return true;
};

/**
 * BFS Approach (Kahn's Algorithm)
 */
var canFinishBFS = function(numCourses, prerequisites) {
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
    
    let count = 0;
    
    while (queue.length > 0) {
        const course = queue.shift();
        count++;
        
        for (const next of graph[course]) {
            inDegree[next]--;
            if (inDegree[next] === 0) {
                queue.push(next);
            }
        }
    }
    
    return count === numCourses;
};
```

## Complexity Analysis

### Time Complexity: O(V + E)
- **V** = numCourses (vertices)
- **E** = prerequisites.length (edges)
- Build graph: O(E)
- DFS/BFS visits each vertex once: O(V)
- Process each edge once: O(E)
- Total: O(V + E)

### Space Complexity: O(V + E)
- **Graph (adjacency list)**: O(V + E)
- **State/Visited array**: O(V)
- **Recursion stack (DFS)**: O(V) worst case
- **Queue (BFS)**: O(V) worst case

## Edge Cases and Testing

```python
def test_course_schedule():
    solution = Solution()
    
    # Test 1: Simple valid case
    assert solution.canFinish(2, [[1,0]]) == True
    
    # Test 2: Simple cycle
    assert solution.canFinish(2, [[1,0],[0,1]]) == False
    
    # Test 3: No prerequisites
    assert solution.canFinish(5, []) == True
    
    # Test 4: Complex valid
    assert solution.canFinish(4, [[1,0],[2,0],[3,1],[3,2]]) == True
    
    # Test 5: Self loop
    assert solution.canFinish(1, [[0,0]]) == False
    
    # Test 6: Long chain
    assert solution.canFinish(5, [[1,0],[2,1],[3,2],[4,3]]) == True
    
    # Test 7: Multiple cycles
    assert solution.canFinish(3, [[0,1],[1,2],[2,0]]) == False
    
    # Test 8: Disconnected components
    assert solution.canFinish(4, [[1,0],[3,2]]) == True
    
    # Test 9: All depend on one
    assert solution.canFinish(4, [[1,0],[2,0],[3,0]]) == True
    
    # Test 10: Diamond pattern
    assert solution.canFinish(4, [[2,0],[2,1],[3,2]]) == True
    
    print("All test cases passed!")

test_course_schedule()
```

## Common Pitfalls

### 1. Wrong Graph Direction
```python
# WRONG: Reversed edge direction
for course, prereq in prerequisites:
    graph[prereq].append(course)  # Should be opposite for DFS cycle detection

# CORRECT: For DFS cycle detection
for course, prereq in prerequisites:
    graph[course].append(prereq)  # course depends on prereq
```

### 2. Not Handling Disconnected Components
```python
# WRONG: Only check from node 0
if has_cycle(0):
    return False

# CORRECT: Check all nodes
for i in range(numCourses):
    if not visited[i]:
        if has_cycle(i):
            return False
```

### 3. Forgetting to Remove from Visiting Set
```python
# WRONG: Visiting node never removed
visiting.add(course)
for prereq in graph[course]:
    if has_cycle(prereq):
        return True
visited.add(course)

# CORRECT: Remove after processing
visiting.add(course)
for prereq in graph[course]:
    if has_cycle(prereq):
        return True
visiting.remove(course)  # Important!
visited.add(course)
```

### 4. In-Degree Calculation Error in BFS
```python
# WRONG: In-degree calculation
for course, prereq in prerequisites:
    in_degree[prereq] += 1  # Wrong direction!

# CORRECT:
for course, prereq in prerequisites:
    in_degree[course] += 1  # course has one more prerequisite
```

## DFS vs BFS Comparison

```
Aspect               | DFS (Cycle Detection)  | BFS (Kahn's Algorithm)
---------------------|------------------------|------------------------
Approach             | Detect back edges      | Remove nodes with in-degree 0
Intuition            | Find cycle in path     | Topological sort attempt
State Tracking       | 3 states or 2 sets     | In-degree array
Memory               | O(V) recursion stack   | O(V) queue
Implementation       | Recursive (cleaner)    | Iterative
When to Use          | Default (simpler)      | Need actual ordering
Returns Ordering     | No                     | Can be modified to return
```

## Related Problems

1. **Course Schedule II** (LeetCode 210)
   - Return the actual course ordering
   - Same cycle detection + build result

2. **Course Schedule IV** (LeetCode 1462)
   - Check reachability between courses
   - Use transitive closure

3. **Minimum Height Trees** (LeetCode 310)
   - Tree-specific topological sort
   - Remove leaves iteratively

4. **Alien Dictionary** (LeetCode 269)
   - Build graph from character ordering
   - Topological sort to find order

5. **Sequence Reconstruction** (LeetCode 444)
   - Verify unique topological order
   - Check if sequences match

## Advanced Variations

### Variation 1: Return Cycle Path
```python
def findCycle(numCourses, prerequisites):
    """Return the cycle if it exists"""
    graph = [[] for _ in range(numCourses)]
    for course, prereq in prerequisites:
        graph[course].append(prereq)
    
    state = [0] * numCourses
    parent = [-1] * numCourses
    
    def find_cycle_dfs(course, path):
        if state[course] == 1:
            # Found cycle, extract it
            cycle = []
            idx = len(path) - 1
            while idx >= 0 and path[idx] != course:
                idx -= 1
            return path[idx:]
        
        if state[course] == 2:
            return None
        
        state[course] = 1
        path.append(course)
        
        for prereq in graph[course]:
            cycle = find_cycle_dfs(prereq, path)
            if cycle:
                return cycle
        
        path.pop()
        state[course] = 2
        return None
    
    for i in range(numCourses):
        if state[i] == 0:
            cycle = find_cycle_dfs(i, [])
            if cycle:
                return cycle
    
    return []  # No cycle
```

### Variation 2: Count Valid Orderings
```python
def countValidOrderings(numCourses, prerequisites):
    """Count number of valid course orderings (expensive!)"""
    # Build graph
    graph = [[] for _ in range(numCourses)]
    in_degree = [0] * numCourses
    
    for course, prereq in prerequisites:
        graph[prereq].append(course)
        in_degree[course] += 1
    
    def backtrack(remaining):
        if not remaining:
            return 1
        
        count = 0
        available = [c for c in remaining if in_degree[c] == 0]
        
        for course in available:
            # Take this course
            for next_course in graph[course]:
                in_degree[next_course] -= 1
            
            count += backtrack(remaining - {course})
            
            # Backtrack
            for next_course in graph[course]:
                in_degree[next_course] += 1
        
        return count
    
    return backtrack(set(range(numCourses)))
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

## Interview Tips

1. **Clarification Questions:**
   - Can a course depend on itself? (Usually no, but check)
   - Are prerequisites unique?
   - Can there be multiple valid orderings?
   - Need to return ordering or just boolean?

2. **Approach Selection:**
   - "This is cycle detection in directed graph"
   - "Two approaches: DFS or BFS topological sort"
   - "I'll use DFS with three states" (explain why)

3. **Implementation Strategy:**
   - Build adjacency list first
   - Explain state meanings clearly
   - Handle disconnected components

4. **Common Mistakes:**
   - Graph direction matters!
   - Must check all components
   - Remember to remove from visiting set in DFS

5. **Follow-up Questions:**
   - "Return the actual ordering?" → Course Schedule II
   - "Find the cycle?" → Track parent/path
   - "Minimum time to complete?" → BFS with levels
   - "Multiple valid orderings count?" → Exponential (backtracking)

6. **Testing Strategy:**
   - Empty prerequisites
   - Self-loop
   - Simple cycle
   - Complex valid graph
   - Disconnected components

## Key Takeaways

1. **Problem Type**: Cycle detection in directed graph
2. **DFS Approach**: Three states (unvisited, visiting, visited)
3. **BFS Approach**: Kahn's algorithm (topological sort)
4. **Key Insight**: Cycle = impossible, DAG = possible
5. **Graph Direction**: Edge from course to its prerequisite (DFS) or reverse (BFS)
6. **Complexity**: O(V + E) optimal for both approaches

This problem teaches the fundamental pattern of topological sorting and cycle detection!
