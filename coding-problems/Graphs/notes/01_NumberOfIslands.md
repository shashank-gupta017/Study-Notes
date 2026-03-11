# Number of Islands

## Problem Link
LeetCode #200: https://leetcode.com/problems/number-of-islands/

## Difficulty
Medium

## Problem Description
Given an `m x n` 2D binary grid `grid` which represents a map of '1's (land) and '0's (water), return the number of islands.

An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.

**Example 1:**
```
Input: grid = [
  ["1","1","1","1","0"],
  ["1","1","0","1","0"],
  ["1","1","0","0","0"],
  ["0","0","0","0","0"]
]
Output: 1
```

**Example 2:**
```
Input: grid = [
  ["1","1","0","0","0"],
  ["1","1","0","0","0"],
  ["0","0","1","0","0"],
  ["0","0","0","1","1"]
]
Output: 3
```

**Constraints:**
- m == grid.length
- n == grid[i].length
- 1 <= m, n <= 300
- grid[i][j] is '0' or '1'

## Pattern Recognition
This is a classic **Graph DFS/BFS (Matrix)** problem. Key characteristics:
1. Matrix traversal
2. Connected component counting
3. Mark visited cells to avoid revisiting
4. 4-directional exploration (up, down, left, right)

The problem is asking us to count connected components in an undirected graph represented as a 2D matrix.

## Core Concept
**FUNDAMENTAL IDEA:**
- Each '1' is a node in a graph
- Two '1's are connected if they're adjacent (4-directional)
- An island is a connected component
- Count distinct connected components

**APPROACH:**
1. Iterate through each cell in the grid
2. When we find an unvisited '1', we found a new island
3. Use DFS/BFS to mark all connected '1's as visited
4. Increment island counter
5. Continue until all cells are processed

## Visual Explanation

```
Grid Example:
    0   1   2   3   4
  ┌───┬───┬───┬───┬───┐
0 │ 1 │ 1 │ 0 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
1 │ 1 │ 1 │ 0 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
2 │ 0 │ 0 │ 1 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
3 │ 0 │ 0 │ 0 │ 1 │ 1 │
  └───┴───┴───┴───┴───┘

Step-by-step DFS:

Step 1: Start at (0,0) = '1' → Island 1 found
DFS explores: (0,0) → (0,1) → (1,0) → (1,1)
Mark all as visited

    0   1   2   3   4
  ┌───┬───┬───┬───┬───┐
0 │ X │ X │ 0 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
1 │ X │ X │ 0 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
2 │ 0 │ 0 │ 1 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
3 │ 0 │ 0 │ 0 │ 1 │ 1 │
  └───┴───┴───┴───┴───┘

Step 2: Continue scanning, find (2,2) = '1' → Island 2 found
DFS explores: (2,2) only
Mark as visited

    0   1   2   3   4
  ┌───┬───┬───┬───┬───┐
0 │ X │ X │ 0 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
1 │ X │ X │ 0 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
2 │ 0 │ 0 │ X │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
3 │ 0 │ 0 │ 0 │ 1 │ 1 │
  └───┴───┴───┴───┴───┘

Step 3: Continue scanning, find (3,3) = '1' → Island 3 found
DFS explores: (3,3) → (3,4)
Mark both as visited

    0   1   2   3   4
  ┌───┬───┬───┬───┬───┐
0 │ X │ X │ 0 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
1 │ X │ X │ 0 │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
2 │ 0 │ 0 │ X │ 0 │ 0 │
  ├───┼───┼───┼───┼───┤
3 │ 0 │ 0 │ 0 │ X │ X │
  └───┴───┴───┴───┴───┘

Result: 3 islands
```

## Solution 1: DFS with Grid Modification

```python
def numIslands(grid):
    """
    DFS solution that modifies the grid to mark visited cells.
    
    Time: O(m*n) - visit each cell once
    Space: O(m*n) - recursion stack in worst case (all land)
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    islands = 0
    
    def dfs(r, c):
        # Base cases: out of bounds or water
        if r < 0 or r >= rows or c < 0 or c >= cols or grid[r][c] == '0':
            return
        
        # Mark current cell as visited (sink the island)
        grid[r][c] = '0'
        
        # Explore all 4 directions
        dfs(r + 1, c)  # down
        dfs(r - 1, c)  # up
        dfs(r, c + 1)  # right
        dfs(r, c - 1)  # left
    
    # Scan entire grid
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == '1':
                islands += 1
                dfs(r, c)  # Sink entire island
    
    return islands
```

**Key Points:**
- Modifies input grid (marks visited as '0')
- Recursion depth can be O(m*n) in worst case
- Simple and intuitive
- No extra space for visited set

## Solution 2: DFS with Visited Set

```python
def numIslands(grid):
    """
    DFS solution that uses a visited set (doesn't modify input).
    
    Time: O(m*n)
    Space: O(m*n) - visited set + recursion stack
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    visited = set()
    islands = 0
    
    def dfs(r, c):
        # Base cases
        if (r < 0 or r >= rows or c < 0 or c >= cols or 
            grid[r][c] == '0' or (r, c) in visited):
            return
        
        visited.add((r, c))
        
        # Explore all 4 directions
        directions = [(1, 0), (-1, 0), (0, 1), (0, -1)]
        for dr, dc in directions:
            dfs(r + dr, c + dc)
    
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == '1' and (r, c) not in visited:
                islands += 1
                dfs(r, c)
    
    return islands
```

**Advantages:**
- Preserves original grid
- Explicit visited tracking
- Cleaner for debugging

## Solution 3: BFS Implementation

```python
from collections import deque

def numIslands(grid):
    """
    BFS solution using a queue.
    
    Time: O(m*n)
    Space: O(min(m,n)) - queue size in worst case
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    islands = 0
    
    def bfs(start_r, start_c):
        queue = deque([(start_r, start_c)])
        grid[start_r][start_c] = '0'  # Mark as visited
        
        while queue:
            r, c = queue.popleft()
            
            # Check all 4 directions
            directions = [(1, 0), (-1, 0), (0, 1), (0, -1)]
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                
                # If valid land cell, add to queue
                if (0 <= nr < rows and 0 <= nc < cols and 
                    grid[nr][nc] == '1'):
                    queue.append((nr, nc))
                    grid[nr][nc] = '0'  # Mark immediately to avoid duplicates
    
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == '1':
                islands += 1
                bfs(r, c)
    
    return islands
```

**BFS vs DFS:**
- BFS explores level by level
- Better space complexity in practice (queue size)
- Iterative (no stack overflow risk)

## Solution 4: Union-Find (Disjoint Set)

```python
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
        self.count = 0
    
    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # Path compression
        return self.parent[x]
    
    def union(self, x, y):
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x != root_y:
            # Union by rank
            if self.rank[root_x] < self.rank[root_y]:
                self.parent[root_x] = root_y
            elif self.rank[root_x] > self.rank[root_y]:
                self.parent[root_y] = root_x
            else:
                self.parent[root_y] = root_x
                self.rank[root_x] += 1
            self.count -= 1

def numIslands(grid):
    """
    Union-Find solution.
    
    Time: O(m*n * α(m*n)) where α is inverse Ackermann (nearly constant)
    Space: O(m*n)
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    
    # Count initial land cells
    land_count = sum(row.count('1') for row in grid)
    uf = UnionFind(rows * cols)
    uf.count = land_count
    
    def get_id(r, c):
        return r * cols + c
    
    # Union adjacent land cells
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == '1':
                # Check right and down (to avoid duplicates)
                if r + 1 < rows and grid[r + 1][c] == '1':
                    uf.union(get_id(r, c), get_id(r + 1, c))
                if c + 1 < cols and grid[r][c + 1] == '1':
                    uf.union(get_id(r, c), get_id(r, c + 1))
    
    return uf.count
```

## Comprehensive Implementations

### Python Implementation (All Approaches)

```python
class Solution:
    def numIslands_dfs_modify(self, grid):
        """DFS modifying grid - Most concise"""
        if not grid:
            return 0
        
        def dfs(i, j):
            if (i < 0 or i >= len(grid) or j < 0 or j >= len(grid[0]) or 
                grid[i][j] != '1'):
                return
            grid[i][j] = '0'
            dfs(i+1, j)
            dfs(i-1, j)
            dfs(i, j+1)
            dfs(i, j-1)
        
        count = 0
        for i in range(len(grid)):
            for j in range(len(grid[0])):
                if grid[i][j] == '1':
                    count += 1
                    dfs(i, j)
        return count
    
    def numIslands_bfs(self, grid):
        """BFS implementation"""
        if not grid:
            return 0
        
        from collections import deque
        rows, cols = len(grid), len(grid[0])
        count = 0
        
        for i in range(rows):
            for j in range(cols):
                if grid[i][j] == '1':
                    count += 1
                    queue = deque([(i, j)])
                    grid[i][j] = '0'
                    
                    while queue:
                        r, c = queue.popleft()
                        for dr, dc in [(1,0),(-1,0),(0,1),(0,-1)]:
                            nr, nc = r + dr, c + dc
                            if (0 <= nr < rows and 0 <= nc < cols and 
                                grid[nr][nc] == '1'):
                                queue.append((nr, nc))
                                grid[nr][nc] = '0'
        return count
    
    def numIslands_iterative_dfs(self, grid):
        """Iterative DFS using stack"""
        if not grid:
            return 0
        
        rows, cols = len(grid), len(grid[0])
        count = 0
        
        for i in range(rows):
            for j in range(cols):
                if grid[i][j] == '1':
                    count += 1
                    stack = [(i, j)]
                    
                    while stack:
                        r, c = stack.pop()
                        if (r < 0 or r >= rows or c < 0 or c >= cols or 
                            grid[r][c] != '1'):
                            continue
                        
                        grid[r][c] = '0'
                        stack.extend([(r+1,c), (r-1,c), (r,c+1), (r,c-1)])
        
        return count
```

### Java Implementation

```java
class Solution {
    // DFS Solution
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private void dfs(char[][] grid, int r, int c) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != '1') {
            return;
        }
        
        grid[r][c] = '0';  // Mark as visited
        
        // Explore 4 directions
        dfs(grid, r + 1, c);
        dfs(grid, r - 1, c);
        dfs(grid, r, c + 1);
        dfs(grid, r, c - 1);
    }
    
    // BFS Solution
    public int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        
        int rows = grid.length;
        int cols = grid[0].length;
        int count = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    bfs(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
    private void bfs(char[][] grid, int r, int c) {
        int rows = grid.length;
        int cols = grid[0].length;
        
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{r, c});
        grid[r][c] = '0';
        
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            
            for (int[] dir : directions) {
                int nr = curr[0] + dir[0];
                int nc = curr[1] + dir[1];
                
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && 
                    grid[nr][nc] == '1') {
                    queue.offer(new int[]{nr, nc});
                    grid[nr][nc] = '0';
                }
            }
        }
    }
}
```

### C++ Implementation

```cpp
class Solution {
public:
    int numIslands(vector<vector<char>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int rows = grid.size();
        int cols = grid[0].size();
        int count = 0;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j);
                }
            }
        }
        
        return count;
    }
    
private:
    void dfs(vector<vector<char>>& grid, int r, int c) {
        int rows = grid.size();
        int cols = grid[0].size();
        
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != '1') {
            return;
        }
        
        grid[r][c] = '0';
        
        dfs(grid, r + 1, c);
        dfs(grid, r - 1, c);
        dfs(grid, r, c + 1);
        dfs(grid, r, c - 1);
    }
    
    // BFS version
    int numIslandsBFS(vector<vector<char>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int rows = grid.size();
        int cols = grid[0].size();
        int count = 0;
        
        vector<pair<int,int>> directions = {{1,0},{-1,0},{0,1},{0,-1}};
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    queue<pair<int,int>> q;
                    q.push({i, j});
                    grid[i][j] = '0';
                    
                    while (!q.empty()) {
                        auto [r, c] = q.front();
                        q.pop();
                        
                        for (auto [dr, dc] : directions) {
                            int nr = r + dr;
                            int nc = c + dc;
                            
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && 
                                grid[nr][nc] == '1') {
                                q.push({nr, nc});
                                grid[nr][nc] = '0';
                            }
                        }
                    }
                }
            }
        }
        
        return count;
    }
};
```

### JavaScript Implementation

```javascript
/**
 * DFS Solution
 */
var numIslands = function(grid) {
    if (!grid || grid.length === 0) {
        return 0;
    }
    
    const rows = grid.length;
    const cols = grid[0].length;
    let count = 0;
    
    function dfs(r, c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] !== '1') {
            return;
        }
        
        grid[r][c] = '0';
        
        dfs(r + 1, c);
        dfs(r - 1, c);
        dfs(r, c + 1);
        dfs(r, c - 1);
    }
    
    for (let i = 0; i < rows; i++) {
        for (let j = 0; j < cols; j++) {
            if (grid[i][j] === '1') {
                count++;
                dfs(i, j);
            }
        }
    }
    
    return count;
};

/**
 * BFS Solution
 */
var numIslandsBFS = function(grid) {
    if (!grid || grid.length === 0) {
        return 0;
    }
    
    const rows = grid.length;
    const cols = grid[0].length;
    let count = 0;
    
    const directions = [[1, 0], [-1, 0], [0, 1], [0, -1]];
    
    for (let i = 0; i < rows; i++) {
        for (let j = 0; j < cols; j++) {
            if (grid[i][j] === '1') {
                count++;
                const queue = [[i, j]];
                grid[i][j] = '0';
                
                while (queue.length > 0) {
                    const [r, c] = queue.shift();
                    
                    for (const [dr, dc] of directions) {
                        const nr = r + dr;
                        const nc = c + dc;
                        
                        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && 
                            grid[nr][nc] === '1') {
                            queue.push([nr, nc]);
                            grid[nr][nc] = '0';
                        }
                    }
                }
            }
        }
    }
    
    return count;
};
```

## Complexity Analysis

### Time Complexity: O(m × n)
- We visit each cell at most once
- DFS/BFS from each land cell visits all connected cells
- Total cells visited across all searches = m × n

### Space Complexity:
**DFS (Recursive):** O(m × n)
- Worst case: all cells are land forming a snake pattern
- Recursion depth = m × n

**BFS:** O(min(m, n))
- Queue size is bounded by the "width" of the island
- In worst case (diagonal island), queue size = min(m, n)

**DFS (Iterative):** O(m × n)
- Stack can grow to m × n in worst case

**Union-Find:** O(m × n)
- Parent array of size m × n

## Edge Cases and Testing

```python
def test_number_of_islands():
    solution = Solution()
    
    # Test 1: Single island
    grid1 = [
        ["1","1","1"],
        ["1","1","1"]
    ]
    assert solution.numIslands(grid1) == 1
    
    # Test 2: Multiple islands
    grid2 = [
        ["1","0","1"],
        ["0","0","0"],
        ["1","0","1"]
    ]
    assert solution.numIslands(grid2) == 4
    
    # Test 3: No islands
    grid3 = [
        ["0","0"],
        ["0","0"]
    ]
    assert solution.numIslands(grid3) == 0
    
    # Test 4: All land
    grid4 = [
        ["1","1"],
        ["1","1"]
    ]
    assert solution.numIslands(grid4) == 1
    
    # Test 5: Single cell
    grid5 = [["1"]]
    assert solution.numIslands(grid5) == 1
    
    # Test 6: Large island with hole
    grid6 = [
        ["1","1","1"],
        ["1","0","1"],
        ["1","1","1"]
    ]
    assert solution.numIslands(grid6) == 1
    
    # Test 7: Diagonal cells (not connected)
    grid7 = [
        ["1","0","1"],
        ["0","1","0"],
        ["1","0","1"]
    ]
    assert solution.numIslands(grid7) == 5
    
    print("All test cases passed!")

test_number_of_islands()
```

## Common Pitfalls

1. **Forgetting to mark cells as visited**
   ```python
   # WRONG: Infinite loop
   def dfs(r, c):
       if grid[r][c] == '1':
           dfs(r+1, c)  # Will revisit same cell!
   
   # CORRECT: Mark as visited
   def dfs(r, c):
       if grid[r][c] == '1':
           grid[r][c] = '0'
           dfs(r+1, c)
   ```

2. **Adding duplicates to BFS queue**
   ```python
   # WRONG: Mark after processing
   while queue:
       r, c = queue.popleft()
       grid[r][c] = '0'  # Too late! May have duplicates in queue
   
   # CORRECT: Mark when adding
   queue.append((nr, nc))
   grid[nr][nc] = '0'  # Mark immediately
   ```

3. **Not checking bounds properly**
   ```python
   # WRONG: May access invalid indices
   if grid[r][c] == '1':
       dfs(r+1, c)
   
   # CORRECT: Check bounds first
   if 0 <= r < rows and 0 <= c < cols and grid[r][c] == '1':
       dfs(r+1, c)
   ```

4. **Treating diagonal cells as connected**
   - Only 4-directional movement allowed
   - Diagonals are NOT connected

## Optimization Techniques

### 1. Early Termination
```python
def numIslands(grid):
    # Quick checks
    if not grid or not grid[0]:
        return 0
    
    # If all water, return 0
    if all(cell == '0' for row in grid for cell in row):
        return 0
```

### 2. Direction Array Pattern
```python
# Clean, maintainable direction handling
DIRECTIONS = [(0, 1), (1, 0), (0, -1), (-1, 0)]

for dr, dc in DIRECTIONS:
    nr, nc = r + dr, c + dc
    if is_valid(nr, nc):
        dfs(nr, nc)
```

### 3. Iterative DFS (Stack Overflow Prevention)
```python
def dfs_iterative(grid, start_r, start_c):
    stack = [(start_r, start_c)]
    
    while stack:
        r, c = stack.pop()
        
        if not is_valid(r, c):
            continue
        
        grid[r][c] = '0'
        stack.extend([(r+1,c), (r-1,c), (r,c+1), (r,c-1)])
```

## Related Problems

1. **Max Area of Island** (LeetCode 695)
   - Find the largest island
   - Track size during DFS

2. **Surrounded Regions** (LeetCode 130)
   - Capture regions surrounded by 'X'
   - DFS from borders

3. **Number of Closed Islands** (LeetCode 1254)
   - Islands not touching border
   - Exclude border-connected components

4. **Count Sub Islands** (LeetCode 1905)
   - Two grids comparison
   - Island in grid2 is subset of island in grid1

5. **Number of Distinct Islands** (LeetCode 694)
   - Count unique island shapes
   - Use path signature for comparison

## Advanced Variations

### Variation 1: Count Islands with Size Constraint
```python
def numIslandsWithMinSize(grid, min_size):
    """Count islands with at least min_size cells"""
    def dfs(r, c):
        if (r < 0 or r >= len(grid) or c < 0 or c >= len(grid[0]) or 
            grid[r][c] != '1'):
            return 0
        
        grid[r][c] = '0'
        size = 1
        
        for dr, dc in [(0,1),(1,0),(0,-1),(-1,0)]:
            size += dfs(r + dr, c + dc)
        
        return size
    
    count = 0
    for i in range(len(grid)):
        for j in range(len(grid[0])):
            if grid[i][j] == '1':
                island_size = dfs(i, j)
                if island_size >= min_size:
                    count += 1
    
    return count
```

### Variation 2: Find Island Perimeter
```python
def islandPerimeter(grid):
    """Calculate perimeter of all islands combined"""
    perimeter = 0
    
    for i in range(len(grid)):
        for j in range(len(grid[0])):
            if grid[i][j] == 1:
                perimeter += 4  # Each land cell contributes 4
                
                # Subtract shared edges
                if i > 0 and grid[i-1][j] == 1:
                    perimeter -= 2
                if j > 0 and grid[i][j-1] == 1:
                    perimeter -= 2
    
    return perimeter
```

### Variation 3: Largest Island After One Flip
```python
def largestIsland(grid):
    """Find largest island after changing one 0 to 1"""
    n = len(grid)
    
    # Step 1: Label each island with unique ID and track sizes
    island_id = 2
    island_sizes = {}
    
    def dfs(r, c, island_id):
        if r < 0 or r >= n or c < 0 or c >= n or grid[r][c] != 1:
            return 0
        
        grid[r][c] = island_id
        size = 1
        
        for dr, dc in [(0,1),(1,0),(0,-1),(-1,0)]:
            size += dfs(r + dr, c + dc, island_id)
        
        return size
    
    # Label all islands
    for i in range(n):
        for j in range(n):
            if grid[i][j] == 1:
                island_sizes[island_id] = dfs(i, j, island_id)
                island_id += 1
    
    # Step 2: Try flipping each 0
    max_size = max(island_sizes.values()) if island_sizes else 0
    
    for i in range(n):
        for j in range(n):
            if grid[i][j] == 0:
                # Find neighboring islands
                neighbors = set()
                for dr, dc in [(0,1),(1,0),(0,-1),(-1,0)]:
                    nr, nc = i + dr, j + dc
                    if 0 <= nr < n and 0 <= nc < n and grid[nr][nc] > 1:
                        neighbors.add(grid[nr][nc])
                
                # Calculate potential size
                size = 1 + sum(island_sizes[island_id] for island_id in neighbors)
                max_size = max(max_size, size)
    
    return max_size
```

## Practice Problems

1. **Number of Islands** (LeetCode 200) - This problem
2. **Max Area of Island** (LeetCode 695) - Easy variation
3. **Number of Closed Islands** (LeetCode 1254) - Medium
4. **Number of Distinct Islands** (LeetCode 694) - Medium
5. **Making A Large Island** (LeetCode 827) - Hard
6. **Number of Enclaves** (LeetCode 1020) - Medium
7. **Count Sub Islands** (LeetCode 1905) - Medium
8. **Surrounded Regions** (LeetCode 130) - Medium

## Key Takeaways

1. **Pattern Recognition**: Matrix connectivity → DFS/BFS
2. **Visit Marking**: Prevent infinite loops and double counting
3. **Direction Handling**: Use direction arrays for clean code
4. **Space-Time Tradeoff**: DFS (simple) vs BFS (better space)
5. **Modification Choice**: Modify grid vs use visited set
6. **Boundary Checking**: Always validate indices before access

## Interview Tips

1. **Start with clarification:**
   - What counts as connected? (4-directional vs 8-directional)
   - Can I modify the input?
   - Grid size constraints?

2. **Discuss approach:**
   - Mention both DFS and BFS options
   - Explain why marking visited is crucial
   - Discuss space complexity tradeoffs

3. **Implementation tips:**
   - Use direction arrays for cleaner code
   - Consider iterative DFS for large grids
   - Handle empty grid edge case

4. **Follow-up questions to expect:**
   - What if diagonal connections count?
   - How to find the largest island?
   - Can you parallelize this?
   - What if grid doesn't fit in memory?

This problem is fundamental for understanding graph traversal in matrices!
