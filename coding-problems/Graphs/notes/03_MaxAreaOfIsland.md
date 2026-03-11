# Max Area of Island

## Problem Link
LeetCode #695: https://leetcode.com/problems/max-area-of-island/

## Difficulty
Medium

## Problem Description
You are given an `m x n` binary matrix `grid`. An island is a group of `1`'s (representing land) connected **4-directionally** (horizontal or vertical). You may assume all four edges of the grid are surrounded by water.

The **area** of an island is the number of cells with a value `1` in the island.

Return the **maximum area** of an island in `grid`. If there is no island, return `0`.

**Example 1:**
```
Input: grid = [
  [0,0,1,0,0,0,0,1,0,0,0,0,0],
  [0,0,0,0,0,0,0,1,1,1,0,0,0],
  [0,1,1,0,1,0,0,0,0,0,0,0,0],
  [0,1,0,0,1,1,0,0,1,0,1,0,0],
  [0,1,0,0,1,1,0,0,1,1,1,0,0],
  [0,0,0,0,0,0,0,0,0,0,1,0,0],
  [0,0,0,0,0,0,0,1,1,1,0,0,0],
  [0,0,0,0,0,0,0,1,1,0,0,0,0]
]
Output: 6
Explanation: The answer is not 11, because the island must be connected 4-directionally.
```

**Example 2:**
```
Input: grid = [[0,0,0,0,0,0,0,0]]
Output: 0
```

**Constraints:**
- m == grid.length
- n == grid[i].length
- 1 <= m, n <= 50
- grid[i][j] is either 0 or 1

## Pattern Recognition
This is a **Graph DFS/BFS with Area Calculation** problem. Key characteristics:
1. Matrix traversal (similar to Number of Islands)
2. Connected component analysis
3. Size/area tracking during traversal
4. 4-directional connectivity

**DIFFERENCE FROM "NUMBER OF ISLANDS":**
- Number of Islands: Count components
- Max Area of Island: Find largest component size

## Core Concept

**FUNDAMENTAL IDEA:**
1. Explore each island using DFS/BFS
2. Count cells while exploring
3. Track maximum area seen
4. Return the maximum

**APPROACH:**
```
For each unvisited land cell:
    1. Start DFS/BFS
    2. Count all connected land cells
    3. Update max_area if current > max_area
    4. Mark all cells as visited

Return max_area
```

## Visual Explanation

```
Grid Example:
  0 1 2 3 4 5 6 7 8
0 [0,0,1,0,0,0,0,1,0]
1 [0,0,0,0,0,0,0,1,1]
2 [0,1,1,0,1,0,0,0,0]
3 [0,1,0,0,1,1,0,0,1]
4 [0,1,0,0,1,1,0,0,1]

Islands Found:
┌──────────────────────┐
│ Island 1 (col 2-3):  │
│ Cells: (0,2)         │
│ Area: 1              │
└──────────────────────┘

┌──────────────────────┐
│ Island 2 (col 7-8):  │
│ Cells: (0,7)         │
│        (1,7), (1,8)  │
│ Area: 3              │
└──────────────────────┘

┌──────────────────────┐
│ Island 3 (col 1-4):  │
│ Cells: (2,1), (2,2)  │
│        (3,1)         │
│        (4,1)         │
│ Area: 4              │
└──────────────────────┘

┌──────────────────────┐
│ Island 4 (col 4-5):  │
│ Cells: (2,4)         │
│        (3,4), (3,5)  │
│        (4,4), (4,5)  │
│ Area: 5              │
└──────────────────────┘

┌──────────────────────┐
│ Island 5 (col 8):    │
│ Cells: (3,8)         │
│        (4,8)         │
│ Area: 2              │
└──────────────────────┘

Maximum Area: 5

Step-by-Step DFS:
Start at (2,4) - Island with area 5

Step 1: Visit (2,4)
  ↓
Step 2: Visit (3,4) from (2,4)
  ↓
Step 3: Visit (3,5) from (3,4)
  ↓
Step 4: Visit (4,5) from (3,5)
  ↓
Step 5: Visit (4,4) from (4,5)
  ↓
All cells marked, return count = 5
```

## Solution 1: DFS with Grid Modification

```python
def maxAreaOfIsland(grid):
    """
    DFS solution that modifies grid to mark visited cells.
    
    Time: O(m*n) - visit each cell once
    Space: O(m*n) - recursion stack in worst case
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    max_area = 0
    
    def dfs(r, c):
        # Base case: out of bounds or water
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 0:
            return 0
        
        # Mark as visited
        grid[r][c] = 0
        
        # Count current cell + all connected cells
        area = 1
        area += dfs(r + 1, c)
        area += dfs(r - 1, c)
        area += dfs(r, c + 1)
        area += dfs(r, c - 1)
        
        return area
    
    # Check each cell
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                current_area = dfs(r, c)
                max_area = max(max_area, current_area)
    
    return max_area
```

**Key Points:**
- Returns area count during DFS
- Accumulates area from all directions
- Simple and efficient

## Solution 2: DFS with Visited Set

```python
def maxAreaOfIsland(grid):
    """
    DFS solution using visited set (preserves grid).
    
    Time: O(m*n)
    Space: O(m*n) - visited set + recursion stack
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    visited = set()
    max_area = 0
    
    def dfs(r, c):
        if (r < 0 or r >= rows or c < 0 or c >= cols or 
            grid[r][c] == 0 or (r, c) in visited):
            return 0
        
        visited.add((r, c))
        
        area = 1
        area += dfs(r + 1, c)
        area += dfs(r - 1, c)
        area += dfs(r, c + 1)
        area += dfs(r, c - 1)
        
        return area
    
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1 and (r, c) not in visited:
                current_area = dfs(r, c)
                max_area = max(max_area, current_area)
    
    return max_area
```

## Solution 3: BFS Implementation

```python
from collections import deque

def maxAreaOfIsland(grid):
    """
    BFS solution using queue.
    
    Time: O(m*n)
    Space: O(min(m,n)) - queue size
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    max_area = 0
    
    def bfs(start_r, start_c):
        queue = deque([(start_r, start_c)])
        grid[start_r][start_c] = 0
        area = 0
        
        while queue:
            r, c = queue.popleft()
            area += 1
            
            directions = [(1, 0), (-1, 0), (0, 1), (0, -1)]
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                
                if (0 <= nr < rows and 0 <= nc < cols and 
                    grid[nr][nc] == 1):
                    queue.append((nr, nc))
                    grid[nr][nc] = 0
        
        return area
    
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                current_area = bfs(r, c)
                max_area = max(max_area, current_area)
    
    return max_area
```

## Solution 4: Iterative DFS with Stack

```python
def maxAreaOfIsland(grid):
    """
    Iterative DFS using stack.
    
    Time: O(m*n)
    Space: O(m*n) - stack size
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    max_area = 0
    
    def dfs_iterative(start_r, start_c):
        stack = [(start_r, start_c)]
        area = 0
        
        while stack:
            r, c = stack.pop()
            
            if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 0:
                continue
            
            grid[r][c] = 0
            area += 1
            
            stack.extend([(r+1,c), (r-1,c), (r,c+1), (r,c-1)])
        
        return area
    
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                current_area = dfs_iterative(r, c)
                max_area = max(max_area, current_area)
    
    return max_area
```

## Comprehensive Implementations

### Python Implementation (All Approaches)

```python
class Solution:
    def maxAreaOfIsland_dfs_clean(self, grid):
        """Most concise DFS solution"""
        if not grid:
            return 0
        
        rows, cols = len(grid), len(grid[0])
        
        def dfs(r, c):
            if (r < 0 or r >= rows or c < 0 or c >= cols or 
                grid[r][c] == 0):
                return 0
            
            grid[r][c] = 0
            return 1 + dfs(r+1,c) + dfs(r-1,c) + dfs(r,c+1) + dfs(r,c-1)
        
        return max(dfs(r, c) for r in range(rows) for c in range(cols))
    
    def maxAreaOfIsland_bfs(self, grid):
        """BFS implementation"""
        if not grid:
            return 0
        
        from collections import deque
        rows, cols = len(grid), len(grid[0])
        max_area = 0
        
        for i in range(rows):
            for j in range(cols):
                if grid[i][j] == 1:
                    area = 0
                    queue = deque([(i, j)])
                    grid[i][j] = 0
                    
                    while queue:
                        r, c = queue.popleft()
                        area += 1
                        
                        for dr, dc in [(1,0),(-1,0),(0,1),(0,-1)]:
                            nr, nc = r + dr, c + dc
                            if (0 <= nr < rows and 0 <= nc < cols and 
                                grid[nr][nc] == 1):
                                queue.append((nr, nc))
                                grid[nr][nc] = 0
                    
                    max_area = max(max_area, area)
        
        return max_area
    
    def maxAreaOfIsland_with_all_areas(self, grid):
        """Return list of all island areas (for analysis)"""
        if not grid:
            return []
        
        rows, cols = len(grid), len(grid[0])
        areas = []
        
        def dfs(r, c):
            if (r < 0 or r >= rows or c < 0 or c >= cols or 
                grid[r][c] == 0):
                return 0
            
            grid[r][c] = 0
            return 1 + dfs(r+1,c) + dfs(r-1,c) + dfs(r,c+1) + dfs(r,c-1)
        
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] == 1:
                    areas.append(dfs(r, c))
        
        return sorted(areas, reverse=True)
```

### Java Implementation

```java
class Solution {
    private int rows;
    private int cols;
    
    public int maxAreaOfIsland(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        
        rows = grid.length;
        cols = grid[0].length;
        int maxArea = 0;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    maxArea = Math.max(maxArea, dfs(grid, r, c));
                }
            }
        }
        
        return maxArea;
    }
    
    private int dfs(int[][] grid, int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 0) {
            return 0;
        }
        
        grid[r][c] = 0;
        
        int area = 1;
        area += dfs(grid, r + 1, c);
        area += dfs(grid, r - 1, c);
        area += dfs(grid, r, c + 1);
        area += dfs(grid, r, c - 1);
        
        return area;
    }
    
    // BFS Version
    public int maxAreaOfIslandBFS(int[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        
        int rows = grid.length;
        int cols = grid[0].length;
        int maxArea = 0;
        
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1) {
                    int area = 0;
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[]{i, j});
                    grid[i][j] = 0;
                    
                    while (!queue.isEmpty()) {
                        int[] curr = queue.poll();
                        area++;
                        
                        for (int[] dir : directions) {
                            int nr = curr[0] + dir[0];
                            int nc = curr[1] + dir[1];
                            
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && 
                                grid[nr][nc] == 1) {
                                queue.offer(new int[]{nr, nc});
                                grid[nr][nc] = 0;
                            }
                        }
                    }
                    
                    maxArea = Math.max(maxArea, area);
                }
            }
        }
        
        return maxArea;
    }
}
```

### C++ Implementation

```cpp
class Solution {
public:
    int maxAreaOfIsland(vector<vector<int>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int rows = grid.size();
        int cols = grid[0].size();
        int maxArea = 0;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 1) {
                    maxArea = max(maxArea, dfs(grid, r, c));
                }
            }
        }
        
        return maxArea;
    }
    
private:
    int dfs(vector<vector<int>>& grid, int r, int c) {
        int rows = grid.size();
        int cols = grid[0].size();
        
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 0) {
            return 0;
        }
        
        grid[r][c] = 0;
        
        return 1 + dfs(grid, r+1, c) + dfs(grid, r-1, c) + 
               dfs(grid, r, c+1) + dfs(grid, r, c-1);
    }
    
    // BFS Version
    int maxAreaOfIslandBFS(vector<vector<int>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int rows = grid.size();
        int cols = grid[0].size();
        int maxArea = 0;
        
        vector<pair<int,int>> directions = {{1,0},{-1,0},{0,1},{0,-1}};
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1) {
                    int area = 0;
                    queue<pair<int,int>> q;
                    q.push({i, j});
                    grid[i][j] = 0;
                    
                    while (!q.empty()) {
                        auto [r, c] = q.front();
                        q.pop();
                        area++;
                        
                        for (auto [dr, dc] : directions) {
                            int nr = r + dr;
                            int nc = c + dc;
                            
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && 
                                grid[nr][nc] == 1) {
                                q.push({nr, nc});
                                grid[nr][nc] = 0;
                            }
                        }
                    }
                    
                    maxArea = max(maxArea, area);
                }
            }
        }
        
        return maxArea;
    }
};
```

### JavaScript Implementation

```javascript
/**
 * DFS Solution
 */
var maxAreaOfIsland = function(grid) {
    if (!grid || grid.length === 0) {
        return 0;
    }
    
    const rows = grid.length;
    const cols = grid[0].length;
    let maxArea = 0;
    
    function dfs(r, c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] === 0) {
            return 0;
        }
        
        grid[r][c] = 0;
        
        return 1 + dfs(r+1, c) + dfs(r-1, c) + dfs(r, c+1) + dfs(r, c-1);
    }
    
    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            if (grid[r][c] === 1) {
                maxArea = Math.max(maxArea, dfs(r, c));
            }
        }
    }
    
    return maxArea;
};

/**
 * BFS Solution
 */
var maxAreaOfIslandBFS = function(grid) {
    if (!grid || grid.length === 0) {
        return 0;
    }
    
    const rows = grid.length;
    const cols = grid[0].length;
    let maxArea = 0;
    
    const directions = [[1, 0], [-1, 0], [0, 1], [0, -1]];
    
    for (let i = 0; i < rows; i++) {
        for (let j = 0; j < cols; j++) {
            if (grid[i][j] === 1) {
                let area = 0;
                const queue = [[i, j]];
                grid[i][j] = 0;
                
                while (queue.length > 0) {
                    const [r, c] = queue.shift();
                    area++;
                    
                    for (const [dr, dc] of directions) {
                        const nr = r + dr;
                        const nc = c + dc;
                        
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && 
                            grid[nr][nc] === 1) {
                            queue.push([nr, nc]);
                            grid[nr][nc] = 0;
                        }
                    }
                }
                
                maxArea = Math.max(maxArea, area);
            }
        }
    }
    
    return maxArea;
};
```

## Complexity Analysis

### Time Complexity: O(m × n)
- Visit each cell exactly once
- DFS/BFS from each land cell explores connected component
- Total operations across all searches: m × n

### Space Complexity:
**DFS (Recursive):** O(m × n)
- Worst case: recursion stack depth = m × n (all land)

**BFS:** O(min(m, n))
- Queue size bounded by island "width"

**Iterative DFS:** O(m × n)
- Stack can grow to m × n

## Edge Cases and Testing

```python
def test_max_area_of_island():
    solution = Solution()
    
    # Test 1: Large island
    grid1 = [
        [1,1,0,0,0],
        [1,1,0,0,0],
        [0,0,0,1,1],
        [0,0,0,1,1]
    ]
    assert solution.maxAreaOfIsland(grid1) == 4
    
    # Test 2: All water
    grid2 = [
        [0,0,0],
        [0,0,0]
    ]
    assert solution.maxAreaOfIsland(grid2) == 0
    
    # Test 3: All land
    grid3 = [
        [1,1],
        [1,1]
    ]
    assert solution.maxAreaOfIsland(grid3) == 4
    
    # Test 4: Single cell
    grid4 = [[1]]
    assert solution.maxAreaOfIsland(grid4) == 1
    
    # Test 5: Snake pattern (worst case)
    grid5 = [
        [1,0,0],
        [1,1,0],
        [0,1,1]
    ]
    assert solution.maxAreaOfIsland(grid5) == 5
    
    # Test 6: Multiple small islands
    grid6 = [
        [1,0,1],
        [0,0,0],
        [1,0,1]
    ]
    assert solution.maxAreaOfIsland(grid6) == 1
    
    # Test 7: Diagonal cells (not connected)
    grid7 = [
        [1,0,1],
        [0,1,0],
        [1,0,1]
    ]
    assert solution.maxAreaOfIsland(grid7) == 1
    
    print("All test cases passed!")

test_max_area_of_island()
```

## Common Pitfalls

### 1. Forgetting to Return Area Count
```python
# WRONG: Returns nothing
def dfs(r, c):
    if grid[r][c] == 0:
        return
    
    grid[r][c] = 0
    dfs(r+1, c)  # No return value!

# CORRECT: Return and accumulate count
def dfs(r, c):
    if grid[r][c] == 0:
        return 0
    
    grid[r][c] = 0
    return 1 + dfs(r+1, c) + dfs(r-1, c) + dfs(r,c+1) + dfs(r,c-1)
```

### 2. Not Tracking Maximum
```python
# WRONG: Returns last area, not maximum
def maxAreaOfIsland(grid):
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                area = dfs(r, c)
    return area  # Only returns last!

# CORRECT: Track maximum
def maxAreaOfIsland(grid):
    max_area = 0
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                area = dfs(r, c)
                max_area = max(max_area, area)
    return max_area
```

### 3. Counting Cells Multiple Times in BFS
```python
# WRONG: May count cells twice
while queue:
    r, c = queue.popleft()
    area += 1
    grid[r][c] = 0  # Too late!

# CORRECT: Mark when adding to queue
queue.append((nr, nc))
grid[nr][nc] = 0  # Mark immediately
```

## Optimization Techniques

### 1. One-Liner DFS
```python
def dfs(r, c):
    return (0 if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 0
            else (grid[r][c] := 0) or 
                 1 + dfs(r+1,c) + dfs(r-1,c) + dfs(r,c+1) + dfs(r,c-1))
```

### 2. Early Pruning
```python
def maxAreaOfIsland(grid):
    max_area = 0
    
    # Early exit if already found maximum possible
    if max_area == len(grid) * len(grid[0]):
        return max_area
```

### 3. Generator Expression for Maximum
```python
def maxAreaOfIsland(grid):
    rows, cols = len(grid), len(grid[0])
    
    def dfs(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 0:
            return 0
        grid[r][c] = 0
        return 1 + dfs(r+1,c) + dfs(r-1,c) + dfs(r,c+1) + dfs(r,c-1)
    
    return max((dfs(r, c) for r in range(rows) for c in range(cols)), default=0)
```

## Related Problems

1. **Number of Islands** (LeetCode 200)
   - Count islands instead of area
   - Same traversal pattern

2. **Island Perimeter** (LeetCode 463)
   - Calculate perimeter instead of area
   - Count exposed edges

3. **Number of Distinct Islands** (LeetCode 694)
   - Track island shapes
   - Use path signature

4. **Largest Island** (LeetCode 827)
   - Can flip one 0 to 1
   - Find maximum resulting area

5. **Count Sub Islands** (LeetCode 1905)
   - Compare islands in two grids
   - Check subset relationship

## Advanced Variations

### Variation 1: Find All Island Areas
```python
def getAllIslandAreas(grid):
    """Return sorted list of all island areas"""
    if not grid:
        return []
    
    rows, cols = len(grid), len(grid[0])
    areas = []
    
    def dfs(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 0:
            return 0
        
        grid[r][c] = 0
        return 1 + dfs(r+1,c) + dfs(r-1,c) + dfs(r,c+1) + dfs(r,c-1)
    
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                areas.append(dfs(r, c))
    
    return sorted(areas, reverse=True)
```

### Variation 2: Find Smallest Island
```python
def minAreaOfIsland(grid):
    """Find smallest island area (> 0)"""
    if not grid:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    min_area = float('inf')
    
    def dfs(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 0:
            return 0
        
        grid[r][c] = 0
        return 1 + dfs(r+1,c) + dfs(r-1,c) + dfs(r,c+1) + dfs(r,c-1)
    
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                area = dfs(r, c)
                min_area = min(min_area, area)
    
    return min_area if min_area != float('inf') else 0
```

### Variation 3: Island with Coordinates
```python
def maxAreaIslandWithCoords(grid):
    """Return max area and coordinates of that island"""
    if not grid:
        return 0, []
    
    rows, cols = len(grid), len(grid[0])
    max_area = 0
    max_coords = []
    
    def dfs(r, c, coords):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 0:
            return 0
        
        grid[r][c] = 0
        coords.append((r, c))
        
        area = 1
        area += dfs(r+1, c, coords)
        area += dfs(r-1, c, coords)
        area += dfs(r, c+1, coords)
        area += dfs(r, c-1, coords)
        
        return area
    
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                coords = []
                area = dfs(r, c, coords)
                if area > max_area:
                    max_area = area
                    max_coords = coords
    
    return max_area, max_coords
```

### Variation 4: Count Islands by Size Range
```python
def countIslandsBySizeRange(grid, min_size, max_size):
    """Count islands within size range [min_size, max_size]"""
    if not grid:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    count = 0
    
    def dfs(r, c):
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == 0:
            return 0
        
        grid[r][c] = 0
        return 1 + dfs(r+1,c) + dfs(r-1,c) + dfs(r,c+1) + dfs(r,c-1)
    
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 1:
                area = dfs(r, c)
                if min_size <= area <= max_size:
                    count += 1
    
    return count
```

### Variation 5: Island Density
```python
def islandDensity(grid):
    """Calculate percentage of grid covered by islands"""
    if not grid:
        return 0.0
    
    rows, cols = len(grid), len(grid[0])
    total_cells = rows * cols
    land_cells = sum(row.count(1) for row in grid)
    
    return (land_cells / total_cells) * 100
```

## Performance Comparison

```python
import time

def benchmark_approaches(grid, iterations=1000):
    """Compare DFS vs BFS performance"""
    
    # DFS Recursive
    start = time.time()
    for _ in range(iterations):
        grid_copy = [row[:] for row in grid]
        maxAreaOfIsland_dfs(grid_copy)
    dfs_time = time.time() - start
    
    # BFS
    start = time.time()
    for _ in range(iterations):
        grid_copy = [row[:] for row in grid]
        maxAreaOfIsland_bfs(grid_copy)
    bfs_time = time.time() - start
    
    print(f"DFS: {dfs_time:.4f}s")
    print(f"BFS: {bfs_time:.4f}s")
    print(f"BFS is {dfs_time/bfs_time:.2f}x {'faster' if bfs_time < dfs_time else 'slower'}")
```

## Interview Tips

1. **Clarification Questions:**
   - Can I modify the input grid?
   - What if grid is empty?
   - Should I handle invalid input?
   - Is diagonal connection allowed?

2. **Approach Discussion:**
   - "Similar to Number of Islands but track area"
   - "Use DFS to count cells in connected component"
   - "Return sum during recursion"

3. **Code Strategy:**
   - Start with DFS (simpler)
   - Explain area accumulation
   - Track maximum across all islands

4. **Complexity:**
   - Time: O(m*n) - each cell visited once
   - Space: O(m*n) - recursion stack

5. **Follow-ups:**
   - "Find smallest island?"
   - "Return all areas sorted?"
   - "What if grid doesn't fit in memory?"
   - "Can you parallelize this?"

6. **Testing:**
   - Empty grid
   - All water
   - All land
   - Multiple islands of different sizes
   - Single cell island

## Key Takeaways

1. **Area Counting**: Return count during DFS/BFS
2. **Accumulation**: Sum from all 4 directions
3. **Maximum Tracking**: Update max after each island
4. **Pattern**: Same as Number of Islands + size tracking
5. **Efficiency**: O(m*n) is optimal (must visit each cell)

This problem teaches connected component size calculation in matrices!
