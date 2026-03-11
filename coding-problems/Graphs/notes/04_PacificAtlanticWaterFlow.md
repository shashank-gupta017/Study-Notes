# Pacific Atlantic Water Flow

## Problem Link
LeetCode #417: https://leetcode.com/problems/pacific-atlantic-water-flow/

## Difficulty
Medium

## Problem Description
There is an `m x n` rectangular island that borders both the **Pacific Ocean** and **Atlantic Ocean**. The **Pacific Ocean** touches the island's left and top edges, and the **Atlantic Ocean** touches the island's right and bottom edges.

The island is partitioned into a grid of square cells. You are given an `m x n` integer matrix `heights` where `heights[r][c]` represents the **height above sea level** of the cell at coordinate `(r, c)`.

The island receives a lot of rain, and the rain water can flow to neighboring cells directly north, south, east, and west if the neighboring cell's height is **less than or equal to** the current cell's height. Water can flow from any cell adjacent to an ocean into the ocean.

Return a **2D list** of grid coordinates `result` where `result[i] = [ri, ci]` denotes that rain water can flow from cell `(ri, ci)` to **both** the Pacific and Atlantic oceans.

**Example 1:**
```
Input: heights = [
  [1,2,2,3,5],
  [3,2,3,4,4],
  [2,4,5,3,1],
  [6,7,1,4,5],
  [5,1,1,2,4]
]
Output: [[0,4],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]

Explanation:
Pacific ~   ~   ~   ~   ~ 
       ~  1   2   2   3  (5) *
       ~  3   2  (3) (4) (4) *
       ~  2   4  (5)  3   1  *
       ~ (6) (7)  1   4   5  *
       ~ (5)  1   1   2   4  *
          *   *   *   *   * Atlantic

Cells marked with () can reach both oceans.
```

**Example 2:**
```
Input: heights = [[1]]
Output: [[0,0]]
Explanation: Single cell touches both oceans.
```

**Constraints:**
- m == heights.length
- n == heights[r].length
- 1 <= m, n <= 200
- 0 <= heights[r][c] <= 10^5

## Pattern Recognition
This is a **Multi-Source BFS/DFS** problem. Key characteristics:
1. **Reverse thinking**: Start from oceans, not from cells
2. Water flows from high to low, but we search from low to high
3. Find intersection of two reachable sets
4. Multiple starting points (all ocean borders)

**CRITICAL INSIGHT:** Instead of checking if each cell can reach both oceans (expensive), start from ocean borders and find which cells can reach each ocean. The answer is the intersection.

## Core Concept

**NAIVE APPROACH (Too Slow):**
```
For each cell:
    DFS to check if can reach Pacific
    DFS to check if can reach Atlantic
    If both reachable, add to result

Time: O(m*n * m*n) = O(m²n²) - TOO SLOW
```

**OPTIMIZED APPROACH:**
```
1. Start DFS/BFS from all Pacific border cells
   - Mark cells that can flow TO Pacific
   
2. Start DFS/BFS from all Atlantic border cells
   - Mark cells that can flow TO Atlantic
   
3. Find intersection of both sets

Time: O(m*n) - Visit each cell at most twice
```

**KEY INSIGHT: Reverse Flow**
- Water flows HIGH → LOW
- We search LOW → HIGH (reverse)
- If we can reach cell X from ocean, then X can flow to ocean

## Visual Explanation

```
Grid:
    0   1   2   3   4
  ┌───┬───┬───┬───┬───┐
0 │ 1 │ 2 │ 2 │ 3 │ 5 │ ← Pacific Ocean (top)
  ├───┼───┼───┼───┼───┤
1 │ 3 │ 2 │ 3 │ 4 │ 4 │
  ├───┼───┼───┼───┼───┤
2 │ 2 │ 4 │ 5 │ 3 │ 1 │
  ├───┼───┼───┼───┼───┤
3 │ 6 │ 7 │ 1 │ 4 │ 5 │
  ├───┼───┼───┼───┼───┤
4 │ 5 │ 1 │ 1 │ 2 │ 4 │
  └───┴───┴───┴───┴───┘
  ↑                   ↓
Pacific            Atlantic
Ocean              Ocean
(left)            (right + bottom)

Step 1: DFS from Pacific borders (top row + left column)

Pacific Reachable:
    0   1   2   3   4
  ┌───┬───┬───┬───┬───┐
0 │ ✓ │ ✓ │ ✓ │ ✓ │ ✓ │
  ├───┼───┼───┼───┼───┤
1 │ ✓ │   │ ✓ │ ✓ │ ✓ │
  ├───┼───┼───┼───┼───┤
2 │ ✓ │ ✓ │ ✓ │   │   │
  ├───┼───┼───┼───┼───┤
3 │ ✓ │ ✓ │   │   │   │
  ├───┼───┼───┼───┼───┤
4 │ ✓ │   │   │   │   │
  └───┴───┴───┴───┴───┘

Step 2: DFS from Atlantic borders (bottom row + right column)

Atlantic Reachable:
    0   1   2   3   4
  ┌───┬───┬───┬───┬───┐
0 │   │   │   │   │ ✓ │
  ├───┼───┼───┼───┼───┤
1 │   │   │ ✓ │ ✓ │ ✓ │
  ├───┼───┼───┼───┼───┤
2 │   │   │ ✓ │ ✓ │ ✓ │
  ├───┼───┼───┼───┼───┤
3 │ ✓ │ ✓ │   │ ✓ │ ✓ │
  ├───┼───┼───┼───┼───┤
4 │ ✓ │ ✓ │ ✓ │ ✓ │ ✓ │
  └───┴───┴───┴───┴───┘

Step 3: Find Intersection (Both ✓)

Result:
    0   1   2   3   4
  ┌───┬───┬───┬───┬───┐
0 │   │   │   │   │ ★ │ (0,4)
  ├───┼───┼───┼───┼───┤
1 │   │   │ ★ │ ★ │ ★ │ (1,2), (1,3), (1,4)
  ├───┼───┼───┼───┼───┤
2 │   │   │ ★ │   │   │ (2,2)
  ├───┼───┼───┼───┼───┤
3 │ ★ │ ★ │   │   │   │ (3,0), (3,1)
  ├───┼───┼───┼───┼───┤
4 │ ★ │   │   │   │   │ (4,0)
  └───┴───┴───┴───┴───┘

Answer: [[0,4],[1,2],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]
```

## Solution 1: DFS from Ocean Borders

```python
def pacificAtlantic(heights):
    """
    DFS solution starting from ocean borders.
    
    Time: O(m*n) - each cell visited at most twice
    Space: O(m*n) - two visited sets
    """
    if not heights or not heights[0]:
        return []
    
    rows, cols = len(heights), len(heights[0])
    pacific = set()
    atlantic = set()
    
    def dfs(r, c, visited):
        """DFS to mark cells reachable from ocean"""
        if (r, c) in visited:
            return
        
        visited.add((r, c))
        
        # Explore neighbors (water flows from high to low, we go low to high)
        directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
        for dr, dc in directions:
            nr, nc = r + dr, c + dc
            
            # Can visit neighbor if:
            # 1. In bounds
            # 2. Not visited
            # 3. Neighbor height >= current (water can flow down from neighbor)
            if (0 <= nr < rows and 0 <= nc < cols and 
                (nr, nc) not in visited and 
                heights[nr][nc] >= heights[r][c]):
                dfs(nr, nc, visited)
    
    # DFS from Pacific borders (top row + left column)
    for c in range(cols):
        dfs(0, c, pacific)  # Top row
    for r in range(rows):
        dfs(r, 0, pacific)  # Left column
    
    # DFS from Atlantic borders (bottom row + right column)
    for c in range(cols):
        dfs(rows - 1, c, atlantic)  # Bottom row
    for r in range(rows):
        dfs(r, cols - 1, atlantic)  # Right column
    
    # Find intersection
    return list(pacific & atlantic)
```

**Key Points:**
- Start from ocean borders
- Flow uphill (reverse of water flow)
- Find cells reachable from both oceans
- Clean intersection at the end

## Solution 2: BFS from Ocean Borders

```python
from collections import deque

def pacificAtlantic(heights):
    """
    BFS solution starting from ocean borders.
    
    Time: O(m*n)
    Space: O(m*n)
    """
    if not heights or not heights[0]:
        return []
    
    rows, cols = len(heights), len(heights[0])
    pacific = set()
    atlantic = set()
    
    def bfs(queue, visited):
        """BFS to mark cells reachable from ocean"""
        directions = [(0, 1), (0, -1), (1, 0), (-1, 0)]
        
        while queue:
            r, c = queue.popleft()
            
            if (r, c) in visited:
                continue
            
            visited.add((r, c))
            
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                
                if (0 <= nr < rows and 0 <= nc < cols and 
                    (nr, nc) not in visited and 
                    heights[nr][nc] >= heights[r][c]):
                    queue.append((nr, nc))
    
    # Initialize queues with border cells
    pacific_queue = deque()
    atlantic_queue = deque()
    
    for c in range(cols):
        pacific_queue.append((0, c))  # Top row
        atlantic_queue.append((rows - 1, c))  # Bottom row
    
    for r in range(rows):
        pacific_queue.append((r, 0))  # Left column
        atlantic_queue.append((r, cols - 1))  # Right column
    
    # BFS from both oceans
    bfs(pacific_queue, pacific)
    bfs(atlantic_queue, atlantic)
    
    # Return intersection
    return list(pacific & atlantic)
```

## Solution 3: Optimized with Single Pass

```python
def pacificAtlantic(heights):
    """
    Optimized DFS with better organization.
    
    Time: O(m*n)
    Space: O(m*n)
    """
    if not heights or not heights[0]:
        return []
    
    rows, cols = len(heights), len(heights[0])
    
    def dfs(r, c, visited, prev_height):
        """DFS with height comparison"""
        if (r < 0 or r >= rows or c < 0 or c >= cols or 
            (r, c) in visited or heights[r][c] < prev_height):
            return
        
        visited.add((r, c))
        
        # Visit all 4 neighbors
        dfs(r + 1, c, visited, heights[r][c])
        dfs(r - 1, c, visited, heights[r][c])
        dfs(r, c + 1, visited, heights[r][c])
        dfs(r, c - 1, visited, heights[r][c])
    
    pacific = set()
    atlantic = set()
    
    # Start from borders
    for i in range(rows):
        dfs(i, 0, pacific, heights[i][0])  # Pacific left
        dfs(i, cols - 1, atlantic, heights[i][cols - 1])  # Atlantic right
    
    for j in range(cols):
        dfs(0, j, pacific, heights[0][j])  # Pacific top
        dfs(rows - 1, j, atlantic, heights[rows - 1][j])  # Atlantic bottom
    
    return [[r, c] for r in range(rows) for c in range(cols) 
            if (r, c) in pacific and (r, c) in atlantic]
```

## Comprehensive Implementations

### Python Implementation (All Approaches)

```python
class Solution:
    def pacificAtlantic_dfs(self, heights):
        """DFS approach - most intuitive"""
        if not heights:
            return []
        
        rows, cols = len(heights), len(heights[0])
        pacific, atlantic = set(), set()
        
        def dfs(r, c, visited):
            visited.add((r, c))
            for dr, dc in [(0,1),(0,-1),(1,0),(-1,0)]:
                nr, nc = r + dr, c + dc
                if (0 <= nr < rows and 0 <= nc < cols and 
                    (nr, nc) not in visited and 
                    heights[nr][nc] >= heights[r][c]):
                    dfs(nr, nc, visited)
        
        # Pacific borders
        for c in range(cols): dfs(0, c, pacific)
        for r in range(rows): dfs(r, 0, pacific)
        
        # Atlantic borders
        for c in range(cols): dfs(rows-1, c, atlantic)
        for r in range(rows): dfs(r, cols-1, atlantic)
        
        return list(pacific & atlantic)
    
    def pacificAtlantic_bfs(self, heights):
        """BFS approach"""
        if not heights:
            return []
        
        from collections import deque
        rows, cols = len(heights), len(heights[0])
        
        def bfs(queue, visited):
            while queue:
                r, c = queue.popleft()
                if (r, c) in visited:
                    continue
                visited.add((r, c))
                
                for dr, dc in [(0,1),(0,-1),(1,0),(-1,0)]:
                    nr, nc = r + dr, c + dc
                    if (0 <= nr < rows and 0 <= nc < cols and 
                        (nr, nc) not in visited and 
                        heights[nr][nc] >= heights[r][c]):
                        queue.append((nr, nc))
        
        pacific, atlantic = set(), set()
        pac_q, atl_q = deque(), deque()
        
        for c in range(cols):
            pac_q.append((0, c))
            atl_q.append((rows-1, c))
        for r in range(rows):
            pac_q.append((r, 0))
            atl_q.append((r, cols-1))
        
        bfs(pac_q, pacific)
        bfs(atl_q, atlantic)
        
        return list(pacific & atlantic)
    
    def pacificAtlantic_iterative_dfs(self, heights):
        """Iterative DFS with stack"""
        if not heights:
            return []
        
        rows, cols = len(heights), len(heights[0])
        pacific, atlantic = set(), set()
        
        def iterative_dfs(start_cells, visited):
            stack = list(start_cells)
            
            while stack:
                r, c = stack.pop()
                
                if (r, c) in visited:
                    continue
                
                visited.add((r, c))
                
                for dr, dc in [(0,1),(0,-1),(1,0),(-1,0)]:
                    nr, nc = r + dr, c + dc
                    if (0 <= nr < rows and 0 <= nc < cols and 
                        (nr, nc) not in visited and 
                        heights[nr][nc] >= heights[r][c]):
                        stack.append((nr, nc))
        
        # Collect border cells
        pacific_borders = [(0, c) for c in range(cols)] + [(r, 0) for r in range(rows)]
        atlantic_borders = [(rows-1, c) for c in range(cols)] + [(r, cols-1) for r in range(rows)]
        
        iterative_dfs(pacific_borders, pacific)
        iterative_dfs(atlantic_borders, atlantic)
        
        return list(pacific & atlantic)
```

### Java Implementation

```java
class Solution {
    private int rows;
    private int cols;
    private int[][] heights;
    
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        if (heights == null || heights.length == 0) {
            return new ArrayList<>();
        }
        
        this.heights = heights;
        this.rows = heights.length;
        this.cols = heights[0].length;
        
        boolean[][] pacific = new boolean[rows][cols];
        boolean[][] atlantic = new boolean[rows][cols];
        
        // DFS from Pacific borders
        for (int c = 0; c < cols; c++) {
            dfs(0, c, pacific);
        }
        for (int r = 0; r < rows; r++) {
            dfs(r, 0, pacific);
        }
        
        // DFS from Atlantic borders
        for (int c = 0; c < cols; c++) {
            dfs(rows - 1, c, atlantic);
        }
        for (int r = 0; r < rows; r++) {
            dfs(r, cols - 1, atlantic);
        }
        
        // Find intersection
        List<List<Integer>> result = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (pacific[r][c] && atlantic[r][c]) {
                    result.add(Arrays.asList(r, c));
                }
            }
        }
        
        return result;
    }
    
    private void dfs(int r, int c, boolean[][] visited) {
        if (visited[r][c]) {
            return;
        }
        
        visited[r][c] = true;
        
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] dir : directions) {
            int nr = r + dir[0];
            int nc = c + dir[1];
            
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && 
                !visited[nr][nc] && heights[nr][nc] >= heights[r][c]) {
                dfs(nr, nc, visited);
            }
        }
    }
}
```

### C++ Implementation

```cpp
class Solution {
public:
    vector<vector<int>> pacificAtlantic(vector<vector<int>>& heights) {
        if (heights.empty() || heights[0].empty()) {
            return {};
        }
        
        int rows = heights.size();
        int cols = heights[0].size();
        
        vector<vector<bool>> pacific(rows, vector<bool>(cols, false));
        vector<vector<bool>> atlantic(rows, vector<bool>(cols, false));
        
        // DFS from borders
        for (int c = 0; c < cols; c++) {
            dfs(heights, pacific, 0, c);
            dfs(heights, atlantic, rows - 1, c);
        }
        
        for (int r = 0; r < rows; r++) {
            dfs(heights, pacific, r, 0);
            dfs(heights, atlantic, r, cols - 1);
        }
        
        // Find intersection
        vector<vector<int>> result;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (pacific[r][c] && atlantic[r][c]) {
                    result.push_back({r, c});
                }
            }
        }
        
        return result;
    }
    
private:
    void dfs(vector<vector<int>>& heights, vector<vector<bool>>& visited, 
             int r, int c) {
        int rows = heights.size();
        int cols = heights[0].size();
        
        if (visited[r][c]) {
            return;
        }
        
        visited[r][c] = true;
        
        vector<pair<int,int>> directions = {{0,1},{0,-1},{1,0},{-1,0}};
        
        for (auto [dr, dc] : directions) {
            int nr = r + dr;
            int nc = c + dc;
            
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && 
                !visited[nr][nc] && heights[nr][nc] >= heights[r][c]) {
                dfs(heights, visited, nr, nc);
            }
        }
    }
};
```

### JavaScript Implementation

```javascript
/**
 * DFS Solution
 */
var pacificAtlantic = function(heights) {
    if (!heights || heights.length === 0) {
        return [];
    }
    
    const rows = heights.length;
    const cols = heights[0].length;
    const pacific = new Set();
    const atlantic = new Set();
    
    function dfs(r, c, visited) {
        const key = `${r},${c}`;
        if (visited.has(key)) {
            return;
        }
        
        visited.add(key);
        
        const directions = [[0,1],[0,-1],[1,0],[-1,0]];
        for (const [dr, dc] of directions) {
            const nr = r + dr;
            const nc = c + dc;
            
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && 
                !visited.has(`${nr},${nc}`) && 
                heights[nr][nc] >= heights[r][c]) {
                dfs(nr, nc, visited);
            }
        }
    }
    
    // Pacific borders
    for (let c = 0; c < cols; c++) {
        dfs(0, c, pacific);
    }
    for (let r = 0; r < rows; r++) {
        dfs(r, 0, pacific);
    }
    
    // Atlantic borders
    for (let c = 0; c < cols; c++) {
        dfs(rows - 1, c, atlantic);
    }
    for (let r = 0; r < rows; r++) {
        dfs(r, cols - 1, atlantic);
    }
    
    // Find intersection
    const result = [];
    for (let r = 0; r < rows; r++) {
        for (let c = 0; c < cols; c++) {
            const key = `${r},${c}`;
            if (pacific.has(key) && atlantic.has(key)) {
                result.push([r, c]);
            }
        }
    }
    
    return result;
};
```

## Complexity Analysis

### Time Complexity: O(m × n)
- Each cell visited at most twice (once for Pacific, once for Atlantic)
- DFS/BFS from each ocean: O(m × n)
- Finding intersection: O(m × n)
- Total: O(m × n)

### Space Complexity: O(m × n)
- Two visited sets: 2 × O(m × n)
- Recursion stack (DFS): O(m × n) worst case
- Queue (BFS): O(m × n) worst case

## Edge Cases and Testing

```python
def test_pacific_atlantic():
    solution = Solution()
    
    # Test 1: Standard case
    heights1 = [
        [1,2,2,3,5],
        [3,2,3,4,4],
        [2,4,5,3,1],
        [6,7,1,4,5],
        [5,1,1,2,4]
    ]
    result1 = solution.pacificAtlantic(heights1)
    expected1 = [[0,4],[1,3],[1,4],[2,2],[3,0],[3,1],[4,0]]
    assert sorted(result1) == sorted(expected1)
    
    # Test 2: Single cell
    heights2 = [[1]]
    assert solution.pacificAtlantic(heights2) == [[0,0]]
    
    # Test 3: All same height
    heights3 = [
        [1,1,1],
        [1,1,1],
        [1,1,1]
    ]
    expected3 = [[r,c] for r in range(3) for c in range(3)]
    assert sorted(solution.pacificAtlantic(heights3)) == sorted(expected3)
    
    # Test 4: Increasing heights
    heights4 = [
        [1,2,3],
        [4,5,6],
        [7,8,9]
    ]
    assert solution.pacificAtlantic(heights4) == [[2,2]]
    
    # Test 5: Decreasing heights
    heights5 = [
        [9,8,7],
        [6,5,4],
        [3,2,1]
    ]
    expected5 = [[0,0]]
    assert solution.pacificAtlantic(heights5) == expected5
    
    # Test 6: Two rows
    heights6 = [
        [1,2,3],
        [4,5,6]
    ]
    result6 = solution.pacificAtlantic(heights6)
    expected6 = [[0,2],[1,2]]
    assert sorted(result6) == sorted(expected6)
    
    print("All test cases passed!")

test_pacific_atlantic()
```

## Common Pitfalls

### 1. Forward Flow Instead of Reverse
```python
# WRONG: Try to flow from each cell to oceans (expensive)
def pacificAtlantic(heights):
    for r in range(rows):
        for c in range(cols):
            if can_reach_pacific(r, c) and can_reach_atlantic(r, c):
                result.append([r, c])

# CORRECT: Flow from oceans to cells
def pacificAtlantic(heights):
    # Start from ocean borders
    dfs_from_pacific_borders()
    dfs_from_atlantic_borders()
    return intersection()
```

### 2. Wrong Height Comparison
```python
# WRONG: Check if neighbor is lower (forward flow)
if heights[nr][nc] <= heights[r][c]:
    dfs(nr, nc, visited)

# CORRECT: Check if neighbor is higher or equal (reverse flow)
if heights[nr][nc] >= heights[r][c]:
    dfs(nr, nc, visited)
```

### 3. Forgetting Corner Cells
```python
# WRONG: Only top/bottom OR left/right
for c in range(cols):
    dfs(0, c, pacific)

# CORRECT: Include ALL borders
for c in range(cols):
    dfs(0, c, pacific)  # Top
for r in range(rows):
    dfs(r, 0, pacific)  # Left (includes corner)
```

### 4. Not Using Set for Intersection
```python
# WRONG: Slow list comparison
result = [cell for cell in pacific if cell in atlantic]  # O(n²)

# CORRECT: Set intersection
result = list(pacific & atlantic)  # O(n)
```

## Optimization Techniques

### 1. Combined DFS Function
```python
def dfs_both_oceans(heights):
    """Single DFS function for both oceans"""
    def dfs(r, c, visited, ocean_name):
        if (r, c) in visited:
            return
        visited.add((r, c))
        
        for dr, dc in [(0,1),(0,-1),(1,0),(-1,0)]:
            nr, nc = r + dr, c + dc
            if (0 <= nr < rows and 0 <= nc < cols and 
                (nr, nc) not in visited and 
                heights[nr][nc] >= heights[r][c]):
                dfs(nr, nc, visited, ocean_name)
```

### 2. Early Termination
```python
# If a cell is in both sets already, skip processing
if (r, c) in pacific and (r, c) in atlantic:
    continue
```

### 3. Bitwise Flags Instead of Two Sets
```python
# Use single 2D array with bit flags
visited = [[0] * cols for _ in range(rows)]
PACIFIC = 1
ATLANTIC = 2

# Mark as reachable from Pacific
visited[r][c] |= PACIFIC

# Check if reachable from both
if visited[r][c] == (PACIFIC | ATLANTIC):
    result.append([r, c])
```

## Related Problems

1. **Island Perimeter** (LeetCode 463)
   - Similar grid traversal
   - Count edges instead of reachability

2. **Surrounded Regions** (LeetCode 130)
   - Multi-source BFS/DFS from borders
   - Capture surrounded regions

3. **Number of Enclaves** (LeetCode 1020)
   - Count cells that can't reach border
   - Reverse of this problem

4. **Shortest Path in Binary Matrix** (LeetCode 1091)
   - BFS in matrix
   - Find path instead of reachability

## Advanced Variations

### Variation 1: Return Sorted by Height
```python
def pacificAtlanticSorted(heights):
    """Return cells sorted by height (descending)"""
    result = pacificAtlantic(heights)
    return sorted(result, key=lambda pos: heights[pos[0]][pos[1]], reverse=True)
```

### Variation 2: Count Reachable Cells
```python
def countPacificAtlantic(heights):
    """Count cells that can reach both oceans"""
    result = pacificAtlantic(heights)
    return len(result)
```

### Variation 3: With Minimum Height Constraint
```python
def pacificAtlanticMinHeight(heights, min_height):
    """Find cells >= min_height that reach both oceans"""
    result = pacificAtlantic(heights)
    return [[r, c] for r, c in result if heights[r][c] >= min_height]
```

### Variation 4: Distance to Both Oceans
```python
def distanceToBothOceans(heights):
    """Return cells with sum of distances to both oceans"""
    # BFS to compute distances
    def bfs_distance(start_cells):
        dist = {}
        queue = deque(start_cells)
        for r, c in start_cells:
            dist[(r, c)] = 0
        
        while queue:
            r, c = queue.popleft()
            
            for dr, dc in [(0,1),(0,-1),(1,0),(-1,0)]:
                nr, nc = r + dr, c + dc
                
                if (0 <= nr < rows and 0 <= nc < cols and 
                    (nr, nc) not in dist and 
                    heights[nr][nc] >= heights[r][c]):
                    dist[(nr, nc)] = dist[(r, c)] + 1
                    queue.append((nr, nc))
        
        return dist
    
    # Compute distances from both oceans
    pac_dist = bfs_distance(pacific_borders)
    atl_dist = bfs_distance(atlantic_borders)
    
    # Return cells reachable from both with total distance
    result = []
    for r in range(rows):
        for c in range(cols):
            if (r, c) in pac_dist and (r, c) in atl_dist:
                total_dist = pac_dist[(r, c)] + atl_dist[(r, c)]
                result.append(([r, c], total_dist))
    
    return sorted(result, key=lambda x: x[1])
```

## Visualizing Water Flow

```
Example: Height matrix
[1, 2, 3]
[4, 5, 6]
[7, 8, 9]

Water Flow (High → Low):
Cell (2,2) with height 9:
  - Can flow to (2,1): 9 > 8 ✓
  - Can flow to (1,2): 9 > 6 ✓
  - Eventually reaches borders

Cell (0,0) with height 1:
  - Already at Pacific border ✓
  - Cannot flow to Atlantic (uphill)

Reverse Search (Low → High):
From Pacific (0,0):
  - Cannot climb to (0,1): 1 < 2 ✗
  - Cannot climb to (1,0): 1 < 4 ✗

From Atlantic (2,2):
  - Cannot climb anywhere (highest point)
  
Only (2,2) reaches Atlantic
Only (0,0) reaches Pacific
Intersection: None (in this case)
```

## Interview Tips

1. **Clarification Questions:**
   - Are heights always non-negative?
   - Can heights be equal?
   - Grid size constraints?
   - Return format (list of lists)?

2. **Key Insights to Mention:**
   - "Reverse flow: search from oceans TO cells"
   - "Water flows downhill, we climb uphill"
   - "Two separate DFS, then intersection"
   - "O(mn) time by visiting each cell at most twice"

3. **Common Mistakes to Avoid:**
   - Don't try to flow from each cell (too slow)
   - Don't forget corner cells in borders
   - Height comparison: >= not <=

4. **Implementation Strategy:**
   - Start with border identification
   - Implement one DFS first
   - Test with small example
   - Add second ocean
   - Compute intersection

5. **Follow-up Questions:**
   - "What if we have 3 oceans?"
   - "How to find cells closest to both oceans?"
   - "Can you parallelize this?"
   - "What if grid doesn't fit in memory?"

## Key Takeaways

1. **Reverse Thinking**: Search from destination to source
2. **Multi-Source BFS/DFS**: Start from multiple points simultaneously
3. **Set Intersection**: Efficient way to find common elements
4. **Border Initialization**: Carefully identify all border cells
5. **Height Comparison**: Flow downhill, search uphill (>= not <=)
6. **Optimal Complexity**: O(mn) by visiting each cell at most twice

This problem teaches an important pattern: when checking if all cells reach a destination, often it's better to start from the destination!
