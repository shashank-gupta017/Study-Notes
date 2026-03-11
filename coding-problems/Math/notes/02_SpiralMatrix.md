# Spiral Matrix - LeetCode #54 (Medium)

## Table of Contents
- [Problem Statement](#problem-statement)
- [Pattern Recognition](#pattern-recognition)
- [Core Concepts](#core-concepts)
- [Approach 1: Boundary-Based Traversal](#approach-1-boundary-based-traversal)
- [Approach 2: Direction-Based Traversal](#approach-2-direction-based-traversal)
- [Approach 3: Recursive Layer Traversal](#approach-3-recursive-layer-traversal)
- [Visual Walkthroughs](#visual-walkthroughs)
- [Handling Different Matrix Shapes](#handling-different-matrix-shapes)
- [Edge Cases & Testing](#edge-cases--testing)
- [Spiral Matrix II - Generation](#spiral-matrix-ii---generation)
- [Related Problems & Patterns](#related-problems--patterns)
- [Common Mistakes & Tips](#common-mistakes--tips)
- [Practice Problems](#practice-problems)

---

## Problem Statement

**LeetCode #54: Spiral Matrix**

Given an `m x n` matrix, return all elements of the matrix in spiral order.

**Example 1:**
```
Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [1,2,3,6,9,8,7,4,5]

Visual:
1 → 2 → 3
        ↓
4 → 5   6
↑       ↓
7 ← 8 ← 9
```

**Example 2:**
```
Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
Output: [1,2,3,4,8,12,11,10,9,5,6,7]

Visual:
1  →  2  →  3  → 4
                 ↓
5  →  6  →  7    8
↑                ↓
9  ← 10 ← 11 ← 12
```

**Constraints:**
- `m == matrix.length`
- `n == matrix[i].length`
- `1 <= m, n <= 10`
- `-100 <= matrix[i][j] <= 100`

---

## Pattern Recognition

**Pattern: Layer-by-Layer Traversal**

### Key Characteristics:
1. **Boundary Manipulation**: Track and shrink boundaries after each direction
2. **Direction Sequence**: Right → Down → Left → Up (clockwise)
3. **Termination**: When boundaries cross or become invalid
4. **State Management**: Keep track of current position and remaining elements

### When to Use This Pattern:
- Spiral/clockwise traversal problems
- Matrix layer processing
- Boundary-based iteration
- Sequential direction changes

### Similar Problems:
- Spiral Matrix II (LeetCode #59)
- Spiral Matrix III (LeetCode #885)
- Rotate Image (LeetCode #48)
- Diagonal Traverse (LeetCode #498)

---

## Core Concepts

### 1. Four Boundaries System

The key insight is maintaining four boundaries that shrink inward:

```
    left          right
      ↓              ↓
top → 1   2   3   4  ←
      5   6   7   8
      9  10  11  12  ←
      ↑              ↓
    bottom
```

- **top**: Top boundary (row index)
- **bottom**: Bottom boundary (row index)
- **left**: Left boundary (column index)
- **right**: Right boundary (column index)

### 2. Traversal Order

Each spiral cycle has 4 phases:
1. **Right**: Traverse from left to right along top boundary
2. **Down**: Traverse from top to bottom along right boundary
3. **Left**: Traverse from right to left along bottom boundary
4. **Up**: Traverse from bottom to top along left boundary

### 3. Boundary Shrinking Rules

After each direction:
- After **Right**: `top++` (exclude processed row)
- After **Down**: `right--` (exclude processed column)
- After **Left**: `bottom--` (exclude processed row)
- After **Up**: `left++` (exclude processed column)

### 4. Termination Conditions

Stop when boundaries cross:
- `top > bottom` OR
- `left > right`

---

## Approach 1: Boundary-Based Traversal

### Algorithm

**Time Complexity**: O(m × n) - visit each element once  
**Space Complexity**: O(1) - only boundary variables (output array doesn't count)

### Implementation

```python
def spiralOrder(matrix):
    """
    Boundary-based spiral traversal
    
    Args:
        matrix: List[List[int]] - m x n matrix
    
    Returns:
        List[int] - elements in spiral order
    """
    if not matrix or not matrix[0]:
        return []
    
    result = []
    top, bottom = 0, len(matrix) - 1
    left, right = 0, len(matrix[0]) - 1
    
    while top <= bottom and left <= right:
        # Phase 1: Traverse Right (along top boundary)
        for col in range(left, right + 1):
            result.append(matrix[top][col])
        top += 1  # Shrink top boundary
        
        # Phase 2: Traverse Down (along right boundary)
        for row in range(top, bottom + 1):
            result.append(matrix[row][right])
        right -= 1  # Shrink right boundary
        
        # Phase 3: Traverse Left (along bottom boundary)
        # Check if there's still a row to process
        if top <= bottom:
            for col in range(right, left - 1, -1):
                result.append(matrix[bottom][col])
            bottom -= 1  # Shrink bottom boundary
        
        # Phase 4: Traverse Up (along left boundary)
        # Check if there's still a column to process
        if left <= right:
            for row in range(bottom, top - 1, -1):
                result.append(matrix[row][left])
            left += 1  # Shrink left boundary
    
    return result
```

### Java Implementation

```java
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        if (matrix == null || matrix.length == 0) {
            return result;
        }
        
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        
        while (top <= bottom && left <= right) {
            // Traverse right
            for (int col = left; col <= right; col++) {
                result.add(matrix[top][col]);
            }
            top++;
            
            // Traverse down
            for (int row = top; row <= bottom; row++) {
                result.add(matrix[row][right]);
            }
            right--;
            
            // Traverse left (check if row exists)
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    result.add(matrix[bottom][col]);
                }
                bottom--;
            }
            
            // Traverse up (check if column exists)
            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    result.add(matrix[row][left]);
                }
                left++;
            }
        }
        
        return result;
    }
}
```

### C++ Implementation

```cpp
class Solution {
public:
    vector<int> spiralOrder(vector<vector<int>>& matrix) {
        vector<int> result;
        if (matrix.empty() || matrix[0].empty()) {
            return result;
        }
        
        int top = 0, bottom = matrix.size() - 1;
        int left = 0, right = matrix[0].size() - 1;
        
        while (top <= bottom && left <= right) {
            // Traverse right
            for (int col = left; col <= right; ++col) {
                result.push_back(matrix[top][col]);
            }
            ++top;
            
            // Traverse down
            for (int row = top; row <= bottom; ++row) {
                result.push_back(matrix[row][right]);
            }
            --right;
            
            // Traverse left (check if row exists)
            if (top <= bottom) {
                for (int col = right; col >= left; --col) {
                    result.push_back(matrix[bottom][col]);
                }
                --bottom;
            }
            
            // Traverse up (check if column exists)
            if (left <= right) {
                for (int row = bottom; row >= top; --row) {
                    result.push_back(matrix[row][left]);
                }
                ++left;
            }
        }
        
        return result;
    }
};
```

### Why Check Before Left and Up Traversals?

Consider a single row matrix: `[[1, 2, 3]]`

Without checks:
```
1. Right: [1, 2, 3], top = 1
2. Down: Nothing (top > bottom), right = 2
3. Left: Would traverse again! ❌
```

With checks:
```
1. Right: [1, 2, 3], top = 1
2. Down: Skip (top > bottom), right = 2
3. Left: Check (top > bottom) → Skip ✓
```

---

## Approach 2: Direction-Based Traversal

### Algorithm

Instead of four separate loops, use direction vectors and change direction when hitting boundaries.

**Time Complexity**: O(m × n)  
**Space Complexity**: O(m × n) - for visited tracking

### Implementation

```python
def spiralOrder(matrix):
    """
    Direction-based spiral traversal with visited tracking
    """
    if not matrix or not matrix[0]:
        return []
    
    m, n = len(matrix), len(matrix[0])
    result = []
    visited = [[False] * n for _ in range(m)]
    
    # Direction vectors: right, down, left, up
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
    current_dir = 0
    
    row, col = 0, 0
    
    for _ in range(m * n):
        result.append(matrix[row][col])
        visited[row][col] = True
        
        # Calculate next position
        dr, dc = directions[current_dir]
        next_row, next_col = row + dr, col + dc
        
        # Check if we need to change direction
        if (next_row < 0 or next_row >= m or 
            next_col < 0 or next_col >= n or 
            visited[next_row][next_col]):
            # Change direction (turn right/clockwise)
            current_dir = (current_dir + 1) % 4
            dr, dc = directions[current_dir]
            next_row, next_col = row + dr, col + dc
        
        row, col = next_row, next_col
    
    return result
```

### Direction Vector Explanation

```
Direction Index 0: (0, 1)  → Right
Direction Index 1: (1, 0)  → Down
Direction Index 2: (0, -1) → Left
Direction Index 3: (-1, 0) → Up
```

When we hit a boundary or visited cell, we turn clockwise:
```
current_dir = (current_dir + 1) % 4
```

### Comparison: Boundary vs Direction

| Aspect | Boundary-Based | Direction-Based |
|--------|---------------|-----------------|
| Space | O(1) | O(m×n) for visited |
| Code Clarity | More explicit | More generic |
| Boundary Checks | Implicit in ranges | Explicit in conditions |
| Best For | Interviews | Generic traversal |

---

## Approach 3: Recursive Layer Traversal

### Algorithm

Process matrix layer by layer recursively.

**Time Complexity**: O(m × n)  
**Space Complexity**: O(min(m, n)) - recursion depth

### Implementation

```python
def spiralOrder(matrix):
    """
    Recursive layer-by-layer traversal
    """
    def traverseLayer(top, bottom, left, right):
        if top > bottom or left > right:
            return []
        
        result = []
        
        # Single row remaining
        if top == bottom:
            return [matrix[top][col] for col in range(left, right + 1)]
        
        # Single column remaining
        if left == right:
            return [matrix[row][right] for row in range(top, bottom + 1)]
        
        # Traverse current layer
        # Right
        for col in range(left, right + 1):
            result.append(matrix[top][col])
        
        # Down
        for row in range(top + 1, bottom + 1):
            result.append(matrix[row][right])
        
        # Left
        for col in range(right - 1, left - 1, -1):
            result.append(matrix[bottom][col])
        
        # Up
        for row in range(bottom - 1, top, -1):
            result.append(matrix[row][left])
        
        # Recurse to inner layer
        result.extend(traverseLayer(top + 1, bottom - 1, left + 1, right - 1))
        
        return result
    
    if not matrix or not matrix[0]:
        return []
    
    return traverseLayer(0, len(matrix) - 1, 0, len(matrix[0]) - 1)
```

### Recursive Visualization

```
Layer 1: Outer boundary
┌─────────────┐
│ 1  2  3  4  │
│ 5        8  │
│ 9  10 11 12 │
└─────────────┘

Layer 2: Inner boundary
    ┌─────┐
    │ 6 7 │
    └─────┘
```

---

## Visual Walkthroughs

### Example 1: 3x3 Matrix

```
Initial Matrix:
┌───┬───┬───┐
│ 1 │ 2 │ 3 │
├───┼───┼───┤
│ 4 │ 5 │ 6 │
├───┼───┼───┤
│ 7 │ 8 │ 9 │
└───┴───┴───┘

Boundaries: top=0, bottom=2, left=0, right=2
```

**Step-by-Step Execution:**

```
ITERATION 1:
-----------
Phase 1 - Traverse Right (row=0, cols: 0→2):
  Add: 1, 2, 3
  top = 1
  
┌───┬───┬───┐
│ ✓ │ ✓ │ ✓ │  Result: [1, 2, 3]
├───┼───┼───┤
│ 4 │ 5 │ 6 │
├───┼───┼───┤
│ 7 │ 8 │ 9 │
└───┴───┴───┘

Phase 2 - Traverse Down (col=2, rows: 1→2):
  Add: 6, 9
  right = 1
  
┌───┬───┬───┐
│ ✓ │ ✓ │ ✓ │  Result: [1, 2, 3, 6, 9]
├───┼───┼───┤
│ 4 │ 5 │ ✓ │
├───┼───┼───┤
│ 7 │ 8 │ ✓ │
└───┴───┴───┘

Phase 3 - Traverse Left (row=2, cols: 1→0):
  Check: top <= bottom? (1 <= 2) ✓
  Add: 8, 7
  bottom = 1
  
┌───┬───┬───┐
│ ✓ │ ✓ │ ✓ │  Result: [1, 2, 3, 6, 9, 8, 7]
├───┼───┼───┤
│ 4 │ 5 │ ✓ │
├───┼───┼───┤
│ ✓ │ ✓ │ ✓ │
└───┴───┴───┘

Phase 4 - Traverse Up (col=0, rows: 1→1):
  Check: left <= right? (0 <= 1) ✓
  Add: 4
  left = 1
  
┌───┬───┬───┐
│ ✓ │ ✓ │ ✓ │  Result: [1, 2, 3, 6, 9, 8, 7, 4]
├───┼───┼───┤
│ ✓ │ 5 │ ✓ │
├───┼───┼───┤
│ ✓ │ ✓ │ ✓ │
└───┴───┴───┘

ITERATION 2:
-----------
Boundaries: top=1, bottom=1, left=1, right=1

Phase 1 - Traverse Right (row=1, cols: 1→1):
  Add: 5
  top = 2
  
┌───┬───┬───┐
│ ✓ │ ✓ │ ✓ │  Result: [1, 2, 3, 6, 9, 8, 7, 4, 5]
├───┼───┼───┤
│ ✓ │ ✓ │ ✓ │
├───┼───┼───┤
│ ✓ │ ✓ │ ✓ │
└───┴───┴───┘

Phase 2: top > bottom (2 > 1), STOP

Final Result: [1, 2, 3, 6, 9, 8, 7, 4, 5]
```

### Example 2: 4x3 Rectangle (More Rows)

```
Initial Matrix:
┌───┬───┬───┐
│ 1 │ 2 │ 3 │
├───┼───┼───┤
│ 4 │ 5 │ 6 │
├───┼───┼───┤
│ 7 │ 8 │ 9 │
├───┼───┼───┤
│10 │11 │12 │
└───┴───┴───┘

Boundaries: top=0, bottom=3, left=0, right=2
```

**Execution:**

```
Layer 1:
  Right: 1, 2, 3 (top=1)
  Down:  6, 9, 12 (right=1)
  Left:  11, 10 (bottom=2)
  Up:    7, 4 (left=1)
  
  Result: [1, 2, 3, 6, 9, 12, 11, 10, 7, 4]

Layer 2:
  Right: 5, 8 (top=2)
  Down:  (skip, top > bottom)
  
  Result: [1, 2, 3, 6, 9, 12, 11, 10, 7, 4, 5, 8]
```

---

## Handling Different Matrix Shapes

### 1. Single Row Matrix

```python
# Input: [[1, 2, 3, 4]]
# Output: [1, 2, 3, 4]

Example: 1x4 matrix
┌───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │
└───┴───┴───┴───┘

Execution:
  Phase 1 (Right): 1, 2, 3, 4 → top becomes 1
  Phase 2 (Down): Skip (top > bottom)
  Phase 3 (Left): Check fails (top > bottom)
  Phase 4 (Up): Check fails (left > right)
```

**Key Point**: The `if top <= bottom` check before left traversal prevents reprocessing.

### 2. Single Column Matrix

```python
# Input: [[1], [2], [3], [4]]
# Output: [1, 2, 3, 4]

Example: 4x1 matrix
┌───┐
│ 1 │
├───┤
│ 2 │
├───┤
│ 3 │
├───┤
│ 4 │
└───┘

Execution:
  Phase 1 (Right): 1 → top becomes 1
  Phase 2 (Down): 2, 3, 4 → right becomes -1
  Phase 3 (Left): Skip (left > right)
  Phase 4 (Up): Check fails (left > right)
```

### 3. Single Element Matrix

```python
# Input: [[5]]
# Output: [5]

Example:
┌───┐
│ 5 │
└───┘

Execution:
  Phase 1 (Right): 5 → top becomes 1
  All other phases: Skip (boundaries crossed)
```

### 4. Wide Rectangle (More Columns)

```python
# Input: [[1, 2, 3, 4, 5], [6, 7, 8, 9, 10]]

Example: 2x5 matrix
┌───┬───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │ 5 │
├───┼───┼───┼───┼───┤
│ 6 │ 7 │ 8 │ 9 │10 │
└───┴───┴───┴───┴───┘

Layer 1:
  Right: 1, 2, 3, 4, 5 (top=1)
  Down:  10 (right=3)
  Left:  9, 8, 7, 6 (bottom=0)
  Up:    Skip (top > bottom)
  
Result: [1, 2, 3, 4, 5, 10, 9, 8, 7, 6]
```

### 5. Tall Rectangle (More Rows)

```python
# Input: [[1, 2], [3, 4], [5, 6], [7, 8], [9, 10]]

Example: 5x2 matrix
┌───┬───┐
│ 1 │ 2 │
├───┼───┤
│ 3 │ 4 │
├───┼───┤
│ 5 │ 6 │
├───┼───┤
│ 7 │ 8 │
├───┼───┤
│ 9 │10 │
└───┴───┘

Layer 1:
  Right: 1, 2 (top=1)
  Down:  4, 6, 8, 10 (right=0)
  Left:  9 (bottom=3)
  Up:    7, 5, 3 (left=1)
  
Result: [1, 2, 4, 6, 8, 10, 9, 7, 5, 3]
```

### Shape-Specific Optimizations

```python
def spiralOrder(matrix):
    if not matrix or not matrix[0]:
        return []
    
    m, n = len(matrix), len(matrix[0])
    
    # Optimization for single row
    if m == 1:
        return matrix[0]
    
    # Optimization for single column
    if n == 1:
        return [matrix[i][0] for i in range(m)]
    
    # General case
    result = []
    top, bottom = 0, m - 1
    left, right = 0, n - 1
    
    while top <= bottom and left <= right:
        # ... standard boundary traversal
```

---

## Edge Cases & Testing

### Comprehensive Edge Cases

```python
def test_spiral_matrix():
    """Comprehensive test suite"""
    
    # Test 1: Empty matrix
    assert spiralOrder([]) == []
    
    # Test 2: Single element
    assert spiralOrder([[1]]) == [1]
    
    # Test 3: Single row
    assert spiralOrder([[1, 2, 3]]) == [1, 2, 3]
    
    # Test 4: Single column
    assert spiralOrder([[1], [2], [3]]) == [1, 2, 3]
    
    # Test 5: 2x2 square
    assert spiralOrder([[1, 2], [3, 4]]) == [1, 2, 4, 3]
    
    # Test 6: 3x3 square
    assert spiralOrder([
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9]
    ]) == [1, 2, 3, 6, 9, 8, 7, 4, 5]
    
    # Test 7: Rectangle (more rows)
    assert spiralOrder([
        [1, 2, 3],
        [4, 5, 6],
        [7, 8, 9],
        [10, 11, 12]
    ]) == [1, 2, 3, 6, 9, 12, 11, 10, 7, 4, 5, 8]
    
    # Test 8: Rectangle (more columns)
    assert spiralOrder([
        [1, 2, 3, 4],
        [5, 6, 7, 8],
        [9, 10, 11, 12]
    ]) == [1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7]
    
    # Test 9: 2x3 small rectangle
    assert spiralOrder([[1, 2, 3], [4, 5, 6]]) == [1, 2, 3, 6, 5, 4]
    
    # Test 10: 3x2 small rectangle
    assert spiralOrder([[1, 2], [3, 4], [5, 6]]) == [1, 2, 4, 6, 5, 3]
    
    # Test 11: Negative numbers
    assert spiralOrder([[-1, -2], [-3, -4]]) == [-1, -2, -4, -3]
    
    # Test 12: Large values
    assert spiralOrder([[100, 200], [300, 400]]) == [100, 200, 400, 300]
    
    print("All tests passed!")
```

### Common Pitfalls

#### Pitfall 1: Forgetting Boundary Checks

```python
# ❌ WRONG - Can reprocess elements
while top <= bottom and left <= right:
    # Right
    for col in range(left, right + 1):
        result.append(matrix[top][col])
    top += 1
    
    # ...
    
    # Left - Missing check!
    for col in range(right, left - 1, -1):
        result.append(matrix[bottom][col])  # May reprocess
    bottom -= 1

# ✓ CORRECT
while top <= bottom and left <= right:
    # Right
    for col in range(left, right + 1):
        result.append(matrix[top][col])
    top += 1
    
    # ...
    
    # Left - With check
    if top <= bottom:  # ← Essential!
        for col in range(right, left - 1, -1):
            result.append(matrix[bottom][col])
        bottom -= 1
```

#### Pitfall 2: Incorrect Range Boundaries

```python
# ❌ WRONG - Off by one
for col in range(left, right):  # Missing last element
    result.append(matrix[top][col])

# ✓ CORRECT
for col in range(left, right + 1):  # Include right
    result.append(matrix[top][col])
```

#### Pitfall 3: Wrong Direction in Reverse Traversals

```python
# ❌ WRONG - Incorrect step
for col in range(right, left, -1):  # Doesn't include left
    result.append(matrix[bottom][col])

# ✓ CORRECT
for col in range(right, left - 1, -1):  # Include left
    result.append(matrix[bottom][col])
```

---

## Spiral Matrix II - Generation

**LeetCode #59: Spiral Matrix II**

Given a positive integer `n`, generate an `n x n` matrix filled with elements from 1 to n² in spiral order.

### Problem Example

```
Input: n = 3
Output: 
[[1, 2, 3],
 [8, 9, 4],
 [7, 6, 5]]

Visual:
1 → 2 → 3
        ↓
8 → 9   4
↑       ↓
7 ← 6 ← 5
```

### Solution

```python
def generateMatrix(n):
    """
    Generate n x n matrix filled in spiral order
    
    Time: O(n²)
    Space: O(1) - excluding output matrix
    """
    matrix = [[0] * n for _ in range(n)]
    
    top, bottom = 0, n - 1
    left, right = 0, n - 1
    num = 1
    
    while top <= bottom and left <= right:
        # Traverse right
        for col in range(left, right + 1):
            matrix[top][col] = num
            num += 1
        top += 1
        
        # Traverse down
        for row in range(top, bottom + 1):
            matrix[row][right] = num
            num += 1
        right -= 1
        
        # Traverse left
        if top <= bottom:
            for col in range(right, left - 1, -1):
                matrix[bottom][col] = num
                num += 1
            bottom -= 1
        
        # Traverse up
        if left <= right:
            for row in range(bottom, top - 1, -1):
                matrix[row][left] = num
                num += 1
            left += 1
    
    return matrix
```

### Step-by-Step Generation (n=4)

```
Step 1: Right (top row)
┌───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │
├───┼───┼───┼───┤
│   │   │   │   │
├───┼───┼───┼───┤
│   │   │   │   │
├───┼───┼───┼───┤
│   │   │   │   │
└───┴───┴───┴───┘

Step 2: Down (right column)
┌───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │
├───┼───┼───┼───┤
│   │   │   │ 5 │
├───┼───┼───┼───┤
│   │   │   │ 6 │
├───┼───┼───┼───┤
│   │   │   │ 7 │
└───┴───┴───┴───┘

Step 3: Left (bottom row)
┌───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │
├───┼───┼───┼───┤
│   │   │   │ 5 │
├───┼───┼───┼───┤
│   │   │   │ 6 │
├───┼───┼───┼───┤
│10 │ 9 │ 8 │ 7 │
└───┴───┴───┴───┘

Step 4: Up (left column)
┌───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │
├───┼───┼───┼───┤
│12 │   │   │ 5 │
├───┼───┼───┼───┤
│11 │   │   │ 6 │
├───┼───┼───┼───┤
│10 │ 9 │ 8 │ 7 │
└───┴───┴───┴───┘

Step 5: Right (inner top row)
┌───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │
├───┼───┼───┼───┤
│12 │13 │14 │ 5 │
├───┼───┼───┼───┤
│11 │   │   │ 6 │
├───┼───┼───┼───┤
│10 │ 9 │ 8 │ 7 │
└───┴───┴───┴───┘

Step 6: Down and remaining
┌───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │
├───┼───┼───┼───┤
│12 │13 │14 │ 5 │
├───┼───┼───┼───┤
│11 │16 │15 │ 6 │
├───┼───┼───┼───┤
│10 │ 9 │ 8 │ 7 │
└───┴───┴───┴───┘
```

### Java Implementation

```java
class Solution {
    public int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        int top = 0, bottom = n - 1;
        int left = 0, right = n - 1;
        int num = 1;
        
        while (top <= bottom && left <= right) {
            // Right
            for (int col = left; col <= right; col++) {
                matrix[top][col] = num++;
            }
            top++;
            
            // Down
            for (int row = top; row <= bottom; row++) {
                matrix[row][right] = num++;
            }
            right--;
            
            // Left
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    matrix[bottom][col] = num++;
                }
                bottom--;
            }
            
            // Up
            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    matrix[row][left] = num++;
                }
                left++;
            }
        }
        
        return matrix;
    }
}
```

---

## Related Problems & Patterns

### 1. Spiral Matrix III (LeetCode #885)

**Problem**: Start from a cell and spiral outward, returning coordinates.

```python
def spiralMatrixIII(rows, cols, rStart, cStart):
    """
    Spiral outward from starting position
    Pattern: Expanding spiral with direction changes
    """
    result = [(rStart, cStart)]
    if rows * cols == 1:
        return result
    
    # Direction: East, South, West, North
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
    steps = 1
    r, c = rStart, cStart
    dir_idx = 0
    
    while len(result) < rows * cols:
        for _ in range(2):  # Change steps every 2 directions
            dr, dc = directions[dir_idx]
            for _ in range(steps):
                r, c = r + dr, c + dc
                if 0 <= r < rows and 0 <= c < cols:
                    result.append((r, c))
            dir_idx = (dir_idx + 1) % 4
        steps += 1
    
    return result
```

### 2. Rotate Image (LeetCode #48)

**Problem**: Rotate matrix 90 degrees clockwise.

```python
def rotate(matrix):
    """
    Rotate matrix 90° clockwise
    Pattern: Transpose + Reverse rows
    """
    n = len(matrix)
    
    # Step 1: Transpose (swap matrix[i][j] with matrix[j][i])
    for i in range(n):
        for j in range(i + 1, n):
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    
    # Step 2: Reverse each row
    for i in range(n):
        matrix[i].reverse()
```

### 3. Diagonal Traverse (LeetCode #498)

**Problem**: Traverse matrix diagonally, alternating direction.

```python
def findDiagonalOrder(mat):
    """
    Diagonal traversal with direction alternation
    """
    if not mat or not mat[0]:
        return []
    
    m, n = len(mat), len(mat[0])
    result = []
    
    for diagonal in range(m + n - 1):
        temp = []
        
        # Determine starting row and col for this diagonal
        if diagonal < n:
            row, col = 0, diagonal
        else:
            row, col = diagonal - n + 1, n - 1
        
        # Collect diagonal elements
        while row < m and col >= 0:
            temp.append(mat[row][col])
            row += 1
            col -= 1
        
        # Reverse every other diagonal
        if diagonal % 2 == 0:
            result.extend(temp[::-1])
        else:
            result.extend(temp)
    
    return result
```

### 4. Print Matrix in Zigzag Fashion

```python
def zigzagMatrix(matrix):
    """
    Print matrix in zigzag pattern:
    1 2 3
    6 5 4
    7 8 9
    """
    if not matrix:
        return []
    
    result = []
    for i, row in enumerate(matrix):
        if i % 2 == 0:
            result.extend(row)
        else:
            result.extend(row[::-1])
    
    return result
```

### 5. Set Matrix Zeroes (LeetCode #73)

**Problem**: If an element is 0, set its entire row and column to 0.

```python
def setZeroes(matrix):
    """
    Set row and column to zero
    Pattern: Boundary marking + in-place update
    """
    m, n = len(matrix), len(matrix[0])
    first_row_zero = any(matrix[0][j] == 0 for j in range(n))
    first_col_zero = any(matrix[i][0] == 0 for i in range(m))
    
    # Use first row and column as markers
    for i in range(1, m):
        for j in range(1, n):
            if matrix[i][j] == 0:
                matrix[i][0] = 0
                matrix[0][j] = 0
    
    # Set zeros based on markers
    for i in range(1, m):
        for j in range(1, n):
            if matrix[i][0] == 0 or matrix[0][j] == 0:
                matrix[i][j] = 0
    
    # Handle first row and column
    if first_row_zero:
        for j in range(n):
            matrix[0][j] = 0
    if first_col_zero:
        for i in range(m):
            matrix[i][0] = 0
```

---

## Common Mistakes & Tips

### Mistakes to Avoid

1. **Forgetting Boundary Validation**
   ```python
   # ❌ Wrong
   for col in range(right, left - 1, -1):
       result.append(matrix[bottom][col])
   
   # ✓ Correct
   if top <= bottom:
       for col in range(right, left - 1, -1):
           result.append(matrix[bottom][col])
   ```

2. **Incorrect Loop Ranges**
   ```python
   # ❌ Missing elements
   range(left, right)  # Excludes right
   
   # ✓ Include all elements
   range(left, right + 1)
   ```

3. **Not Updating Boundaries**
   ```python
   # ❌ Infinite loop
   for col in range(left, right + 1):
       result.append(matrix[top][col])
   # Forgot: top += 1
   ```

4. **Boundary Update Order**
   ```python
   # ❌ Update before using
   top += 1
   for col in range(left, right + 1):
       result.append(matrix[top][col])  # Wrong row!
   
   # ✓ Update after using
   for col in range(left, right + 1):
       result.append(matrix[top][col])
   top += 1
   ```

### Pro Tips

1. **Visualize Before Coding**
   - Draw the matrix with arrows
   - Mark boundaries at each step
   - Trace through small examples

2. **Check Single Row/Column First**
   ```python
   if m == 1:
       return matrix[0]
   if n == 1:
       return [matrix[i][0] for i in range(m)]
   ```

3. **Use Meaningful Variable Names**
   ```python
   # Clear intent
   top, bottom, left, right
   
   # vs confusing
   r1, r2, c1, c2
   ```

4. **Test Edge Cases Early**
   - Empty matrix: `[]`
   - Single element: `[[1]]`
   - Single row: `[[1, 2, 3]]`
   - Single column: `[[1], [2], [3]]`

5. **Remember the Pattern**
   - **R**ight → **D**own → **L**eft → **U**p
   - Shrink after each: top++, right--, bottom--, left++
   - Check before reverse directions

---

## Practice Problems

### Similar Difficulty

1. **Spiral Matrix II** (LeetCode #59) - Medium
   - Generate instead of traverse
   - Same boundary pattern

2. **Rotate Image** (LeetCode #48) - Medium
   - Layer-by-layer rotation
   - In-place manipulation

3. **Diagonal Traverse** (LeetCode #498) - Medium
   - Different traversal pattern
   - Direction alternation

### More Challenging

4. **Spiral Matrix III** (LeetCode #885) - Medium
   - Expand outward spiral
   - Handle out-of-bounds

5. **Matrix Diagonal Sum** (LeetCode #1572) - Easy
   - Boundary-based calculation
   - Avoid double counting

6. **Valid Word Square** (LeetCode #422) - Easy
   - Matrix validation
   - Boundary checks

### Advanced Variations

7. **Print Concentric Matrix** - Custom
   - Multiple spirals
   - Nested boundaries

8. **Spiral Matrix IV** (LeetCode #2326) - Medium
   - Linked list to spiral matrix
   - Fill with -1 for remaining

9. **Anti-clockwise Spiral** - Custom
   - Reverse direction
   - Left → Up → Right → Down

---

## Summary

### Key Takeaways

1. **Boundary System**: Four boundaries that shrink inward
2. **Direction Order**: Right → Down → Left → Up (clockwise)
3. **Boundary Checks**: Essential for left and up traversals
4. **Time Complexity**: O(m × n) - visit each element once
5. **Space Complexity**: O(1) - only boundary variables

### Template Code

```python
def spiralOrder(matrix):
    if not matrix or not matrix[0]:
        return []
    
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
        
        # Left (with check)
        if top <= bottom:
            for col in range(right, left - 1, -1):
                result.append(matrix[bottom][col])
            bottom -= 1
        
        # Up (with check)
        if left <= right:
            for row in range(bottom, top - 1, -1):
                result.append(matrix[row][left])
            left += 1
    
    return result
```

### When to Use This Pattern

- ✅ Spiral/concentric traversals
- ✅ Layer-by-layer processing
- ✅ Boundary-based iterations
- ✅ Direction-based matrix problems
- ❌ Random access patterns
- ❌ Diagonal-first traversals (use different pattern)

### Further Study

- Spiral Matrix variants on LeetCode
- Matrix rotation problems
- 2D array traversal patterns
- Game board traversal (chess, etc.)

---

**Last Updated**: 2024
**Difficulty**: Medium
**Pattern**: Layer-by-Layer Traversal
**Companies**: Amazon, Microsoft, Google, Facebook, Apple
