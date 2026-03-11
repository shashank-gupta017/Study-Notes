# LeetCode #48: Rotate Image (Medium)

## Problem Statement

You are given an n x n 2D matrix representing an image, rotate the image by 90 degrees (clockwise).

You have to rotate the image **in-place**, which means you have to modify the input 2D matrix directly. **DO NOT** allocate another 2D matrix and do the rotation.

### Example 1:
```
Input: matrix = [[1,2,3],[4,5,6],[7,8,9]]
Output: [[7,4,1],[8,5,2],[9,6,3]]

Visualization:
1 2 3       7 4 1
4 5 6  -->  8 5 2
7 8 9       9 6 3
```

### Example 2:
```
Input: matrix = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
Output: [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]

Visualization:
5  1  9  11       15 13 2  5
2  4  8  10  -->  14 3  4  1
13 3  6  7        12 6  8  9
15 14 12 16       16 7  10 11
```

### Constraints:
- n == matrix.length == matrix[i].length
- 1 <= n <= 20
- -1000 <= matrix[i][j] <= 1000

---

## Pattern Recognition

**Pattern Category:** Matrix Transformation / In-Place Matrix Manipulation

**Key Pattern:** Transpose + Reverse

**Related Patterns:**
- Matrix traversal
- In-place array manipulation
- Coordinate transformation
- Layer-by-layer processing

**When to use this pattern:**
- Matrix rotation problems
- Matrix transformation problems
- Image processing operations
- Coordinate system transformations

---

## Core Concept: Understanding Matrix Rotation

### What Happens During 90° Clockwise Rotation?

Let's track what happens to each position:
```
Position Mapping (for 3x3 matrix):
[0,0] -> [0,2]    [0,1] -> [1,2]    [0,2] -> [2,2]
[1,0] -> [0,1]    [1,1] -> [1,1]    [1,2] -> [2,1]
[2,0] -> [0,0]    [2,1] -> [1,0]    [2,2] -> [2,0]

Pattern: matrix[i][j] -> matrix[j][n-1-i]
```

### Visual Representation:
```
Original:           After 90° CW:
1 2 3               7 4 1
4 5 6      -->      8 5 2
7 8 9               9 6 3

Element Tracking:
1[0,0] -> [0,2]
2[0,1] -> [1,2]
3[0,2] -> [2,2]
4[1,0] -> [0,1]
5[1,1] -> [1,1] (center stays)
6[1,2] -> [2,1]
7[2,0] -> [0,0]
8[2,1] -> [1,0]
9[2,2] -> [2,0]
```

---

## Approach 1: Transpose + Reverse (Most Elegant)

### Key Insight

Rotating 90° clockwise = Transpose + Reverse each row

**Mathematical Proof:**
```
Original: matrix[i][j]
Transpose: matrix[j][i]
Reverse rows: matrix[j][n-1-i]

This matches our rotation formula: matrix[i][j] -> matrix[j][n-1-i]
```

### Step-by-Step Visualization

```
Step 1: Original Matrix
1 2 3
4 5 6
7 8 9

Step 2: Transpose (swap across diagonal)
1 4 7
2 5 8
3 6 9

Step 3: Reverse each row
7 4 1
8 5 2
9 6 3
```

### Algorithm Steps

1. **Transpose the matrix** (swap matrix[i][j] with matrix[j][i])
2. **Reverse each row** (reverse elements in each row)

### Implementation

```python
def rotate(matrix):
    """
    Rotate matrix 90 degrees clockwise using transpose + reverse.
    
    Time Complexity: O(n²) - visit each element twice
    Space Complexity: O(1) - in-place modification
    """
    n = len(matrix)
    
    # Step 1: Transpose the matrix
    for i in range(n):
        for j in range(i, n):  # Only traverse upper triangle
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    
    # Step 2: Reverse each row
    for i in range(n):
        matrix[i].reverse()
        # Or manually: matrix[i] = matrix[i][::-1]
        # Or two-pointer: left, right = 0, n-1; swap while left < right

# Example with detailed steps
def rotate_with_visualization(matrix):
    n = len(matrix)
    print("Original:", matrix)
    
    # Transpose
    for i in range(n):
        for j in range(i, n):
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    print("After Transpose:", matrix)
    
    # Reverse each row
    for i in range(n):
        matrix[i].reverse()
    print("After Reverse:", matrix)
```

### Java Implementation

```java
class Solution {
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        
        // Step 1: Transpose
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
        
        // Step 2: Reverse each row
        for (int i = 0; i < n; i++) {
            int left = 0, right = n - 1;
            while (left < right) {
                int temp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = temp;
                left++;
                right--;
            }
        }
    }
}
```

### C++ Implementation

```cpp
class Solution {
public:
    void rotate(vector<vector<int>>& matrix) {
        int n = matrix.size();
        
        // Transpose
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                swap(matrix[i][j], matrix[j][i]);
            }
        }
        
        // Reverse each row
        for (int i = 0; i < n; i++) {
            reverse(matrix[i].begin(), matrix[i].end());
        }
    }
};
```

### Complexity Analysis

- **Time Complexity:** O(n²)
  - Transpose: O(n²/2) comparisons
  - Reverse: O(n²/2) operations
  - Total: O(n²)

- **Space Complexity:** O(1)
  - Only using constant extra space for swapping

---

## Approach 2: Layer-by-Layer Rotation

### Key Insight

Process the matrix in concentric layers from outside to inside. For each layer, rotate the four edges simultaneously.

### Visual Representation

```
4x4 Matrix Layers:
Layer 0 (outer):
* * * *
*     *
*     *
* * * *

Layer 1 (inner):
    
  * *
  * *
    

3x3 Matrix has 2 layers (0 and 1)
4x4 Matrix has 2 layers (0 and 1)
5x5 Matrix has 3 layers (0, 1, and 2)

Number of layers = n // 2
```

### Algorithm

For each layer:
1. Define boundaries: top, bottom, left, right
2. For each position in the top edge:
   - Save top element
   - Move left to top
   - Move bottom to left
   - Move right to bottom
   - Move saved top to right

### Detailed Visualization

```
Processing Layer 0 of 3x3 matrix:

Initial:
1 2 3
4 5 6
7 8 9

For position (0,0):
- Save top: temp = 1
- Left->Top: 7 -> position of 1
- Bottom->Left: 9 -> position of 7
- Right->Top: 3 -> position of 9
- Temp->Right: 1 -> position of 3

Result after first element:
7 2 3
4 5 1
9 8 3... (in progress)
```

### Implementation

```python
def rotate_layer_by_layer(matrix):
    """
    Rotate matrix 90 degrees clockwise using layer-by-layer approach.
    
    Time Complexity: O(n²)
    Space Complexity: O(1)
    """
    n = len(matrix)
    
    # Process each layer
    for layer in range(n // 2):
        first = layer
        last = n - 1 - layer
        
        # Process each element in the current layer
        for i in range(first, last):
            offset = i - first
            
            # Save top element
            top = matrix[first][i]
            
            # Left -> Top
            matrix[first][i] = matrix[last - offset][first]
            
            # Bottom -> Left
            matrix[last - offset][first] = matrix[last][last - offset]
            
            # Right -> Bottom
            matrix[last][last - offset] = matrix[i][last]
            
            # Top -> Right
            matrix[i][last] = top

# Alternative with clearer variable names
def rotate_layer_by_layer_v2(matrix):
    n = len(matrix)
    
    for layer in range(n // 2):
        top_row = layer
        bottom_row = n - 1 - layer
        left_col = layer
        right_col = n - 1 - layer
        
        for col in range(left_col, right_col):
            offset = col - left_col
            
            # Save top
            temp = matrix[top_row][col]
            
            # Move left to top
            matrix[top_row][col] = matrix[bottom_row - offset][left_col]
            
            # Move bottom to left
            matrix[bottom_row - offset][left_col] = matrix[bottom_row][right_col - offset]
            
            # Move right to bottom
            matrix[bottom_row][right_col - offset] = matrix[top_row + offset][right_col]
            
            # Move temp (top) to right
            matrix[top_row + offset][right_col] = temp
```

### Java Implementation

```java
class Solution {
    public void rotate(int[][] matrix) {
        int n = matrix.length;
        
        for (int layer = 0; layer < n / 2; layer++) {
            int first = layer;
            int last = n - 1 - layer;
            
            for (int i = first; i < last; i++) {
                int offset = i - first;
                
                // Save top
                int top = matrix[first][i];
                
                // Left -> Top
                matrix[first][i] = matrix[last - offset][first];
                
                // Bottom -> Left
                matrix[last - offset][first] = matrix[last][last - offset];
                
                // Right -> Bottom
                matrix[last][last - offset] = matrix[i][last];
                
                // Top -> Right
                matrix[i][last] = top;
            }
        }
    }
}
```

---

## Approach 3: Four-Way Swap (Direct Position Mapping)

### Key Insight

Directly swap elements in groups of 4 using the rotation formula:
- matrix[i][j] -> matrix[j][n-1-i]

### Mathematical Derivation

```
For 90° clockwise rotation:
Position (i, j) goes to (j, n-1-i)

Following the cycle:
(i, j) -> (j, n-1-i) -> (n-1-i, n-1-j) -> (n-1-j, i) -> (i, j)

These 4 positions form a rotation cycle.
```

### Visual Cycle Example (3x3 matrix)

```
Cycle 1: Corners
(0,0) -> (0,2) -> (2,2) -> (2,0) -> (0,0)
  1   ->   3   ->   9   ->   7   ->   1

Cycle 2: Middle edges
(0,1) -> (1,2) -> (2,1) -> (1,0) -> (0,1)
  2   ->   6   ->   8   ->   4   ->   2
```

### Implementation

```python
def rotate_four_way_swap(matrix):
    """
    Rotate using direct four-way position swapping.
    
    Time Complexity: O(n²)
    Space Complexity: O(1)
    """
    n = len(matrix)
    
    # Only need to process n//2 rows and (n+1)//2 columns
    for i in range(n // 2):
        for j in range((n + 1) // 2):
            # Perform 4-way swap
            temp = matrix[i][j]
            
            # Move values in rotation cycle
            matrix[i][j] = matrix[n-1-j][i]
            matrix[n-1-j][i] = matrix[n-1-i][n-1-j]
            matrix[n-1-i][n-1-j] = matrix[j][n-1-i]
            matrix[j][n-1-i] = temp

# Alternative: Process by calculating all positions
def rotate_four_way_explicit(matrix):
    n = len(matrix)
    
    for i in range(n // 2):
        for j in range((n + 1) // 2):
            # Calculate all four positions
            pos1 = (i, j)
            pos2 = (j, n - 1 - i)
            pos3 = (n - 1 - i, n - 1 - j)
            pos4 = (n - 1 - j, i)
            
            # Rotate: 1->2, 2->3, 3->4, 4->1
            temp = matrix[pos1[0]][pos1[1]]
            matrix[pos1[0]][pos1[1]] = matrix[pos4[0]][pos4[1]]
            matrix[pos4[0]][pos4[1]] = matrix[pos3[0]][pos3[1]]
            matrix[pos3[0]][pos3[1]] = matrix[pos2[0]][pos2[1]]
            matrix[pos2[0]][pos2[1]] = temp
```

---

## Different Rotation Directions

### 90° Counterclockwise Rotation

**Formula:** matrix[i][j] -> matrix[n-1-j][i]

**Method 1: Transpose + Reverse Columns**
```python
def rotate_90_ccw(matrix):
    n = len(matrix)
    
    # Transpose
    for i in range(n):
        for j in range(i, n):
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    
    # Reverse columns (or reverse matrix row-wise)
    for j in range(n):
        for i in range(n // 2):
            matrix[i][j], matrix[n-1-i][j] = matrix[n-1-i][j], matrix[i][j]

# Or simply reverse the matrix rows
def rotate_90_ccw_v2(matrix):
    n = len(matrix)
    
    # Transpose
    for i in range(n):
        for j in range(i, n):
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    
    # Reverse the matrix (top to bottom)
    matrix.reverse()
```

**Visualization:**
```
Original:       Transpose:      Reverse Rows:
1 2 3           1 4 7           3 6 9
4 5 6     ->    2 5 8     ->    2 5 8
7 8 9           3 6 9           1 4 7
```

### 180° Rotation

**Formula:** matrix[i][j] -> matrix[n-1-i][n-1-j]

**Method 1: Reverse rows, then reverse each row**
```python
def rotate_180(matrix):
    n = len(matrix)
    
    # Reverse the entire matrix (top to bottom)
    matrix.reverse()
    
    # Reverse each row (left to right)
    for i in range(n):
        matrix[i].reverse()

# Method 2: Direct swapping
def rotate_180_v2(matrix):
    n = len(matrix)
    
    for i in range(n // 2):
        for j in range(n):
            matrix[i][j], matrix[n-1-i][n-1-j] = matrix[n-1-i][n-1-j], matrix[i][j]
    
    # Handle middle row for odd n
    if n % 2 == 1:
        mid = n // 2
        for j in range(n // 2):
            matrix[mid][j], matrix[mid][n-1-j] = matrix[mid][n-1-j], matrix[mid][j]
```

**Visualization:**
```
Original:       180° Rotation:
1 2 3           9 8 7
4 5 6     ->    6 5 4
7 8 9           3 2 1
```

### 270° Clockwise (Same as 90° CCW)

**Formula:** Same as 90° counterclockwise

```python
def rotate_270_cw(matrix):
    # 270° clockwise = 90° counterclockwise
    rotate_90_ccw(matrix)
```

---

## Complete Rotation Suite

```python
class MatrixRotation:
    """Comprehensive matrix rotation utilities."""
    
    @staticmethod
    def rotate_90_cw(matrix):
        """Rotate 90° clockwise."""
        n = len(matrix)
        
        # Transpose
        for i in range(n):
            for j in range(i, n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        
        # Reverse each row
        for row in matrix:
            row.reverse()
    
    @staticmethod
    def rotate_90_ccw(matrix):
        """Rotate 90° counterclockwise."""
        n = len(matrix)
        
        # Transpose
        for i in range(n):
            for j in range(i, n):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        
        # Reverse matrix
        matrix.reverse()
    
    @staticmethod
    def rotate_180(matrix):
        """Rotate 180°."""
        matrix.reverse()
        for row in matrix:
            row.reverse()
    
    @staticmethod
    def rotate_arbitrary(matrix, degrees):
        """Rotate by arbitrary degrees (must be multiple of 90)."""
        if degrees % 90 != 0:
            raise ValueError("Degrees must be multiple of 90")
        
        degrees = degrees % 360
        rotations = degrees // 90
        
        for _ in range(rotations):
            MatrixRotation.rotate_90_cw(matrix)
```

---

## Mathematical Intuition

### Understanding Transpose

**Transpose** swaps elements across the main diagonal (top-left to bottom-right).

```
Main diagonal: positions where i == j

Original:       Transpose:
a b c           a d g
d e f     ->    b e h
g h i           c f i

Formula: matrix[i][j] ↔ matrix[j][i]
```

### Understanding Reverse

**Reverse** flips elements horizontally within each row.

```
Original:       Reversed:
a b c           c b a
d e f     ->    f e d
g h i           i h g

Formula: matrix[i][j] ↔ matrix[i][n-1-j]
```

### Combining Operations

```
Transpose:      matrix[i][j] -> matrix[j][i]
Reverse:        matrix[j][i] -> matrix[j][n-1-i]
Combined:       matrix[i][j] -> matrix[j][n-1-i]

This is exactly the 90° clockwise rotation formula!
```

### Coordinate System Transformation

```
Standard Cartesian coordinates:
    y
    ^
    |
    +----> x

90° CW rotation in coordinate system:
(x, y) -> (y, -x)

In matrix indices (where y increases downward):
(i, j) -> (j, n-1-i)
```

---

## Edge Cases and Corner Cases

### Case 1: 1×1 Matrix
```python
matrix = [[1]]
# No rotation needed, matrix remains [[1]]
```

### Case 2: 2×2 Matrix
```python
matrix = [[1,2],[3,4]]
# Rotated: [[3,1],[4,2]]

# Special: Only 1 layer, 1 element per edge
```

### Case 3: Even vs Odd Dimensions

```
Even (4x4): 2 complete layers
* * * *
* * * *
* * * *
* * * *

Odd (5x5): 2 complete layers + 1 center element
* * * * *
* * * * *
* * # * *  (# is center, doesn't move)
* * * * *
* * * * *
```

### Case 4: Negative Numbers
```python
matrix = [[-1,-2],[-3,-4]]
# Works identically, rotation doesn't depend on values
```

### Case 5: Duplicates
```python
matrix = [[1,1],[1,1]]
# Result: [[1,1],[1,1]]
# Duplicates don't affect correctness
```

### Case 6: Maximum Constraints
```python
# n = 20, values up to ±1000
# Should handle efficiently with O(n²) time
```

---

## Detailed Examples

### Example 1: 3×3 Matrix Step-by-Step

```
Original Matrix:
1 2 3
4 5 6
7 8 9

Step 1: Transpose
Swap [0][1] and [1][0]: 2 ↔ 4
Swap [0][2] and [2][0]: 3 ↔ 7
Swap [1][2] and [2][1]: 6 ↔ 8

Result after transpose:
1 4 7
2 5 8
3 6 9

Step 2: Reverse Each Row
Row 0: [1,4,7] -> [7,4,1]
Row 1: [2,5,8] -> [8,5,2]
Row 2: [3,6,9] -> [9,6,3]

Final Result:
7 4 1
8 5 2
9 6 3
```

### Example 2: 4×4 Matrix Layer-by-Layer

```
Original:
1  2  3  4
5  6  7  8
9  10 11 12
13 14 15 16

Layer 0 (outer layer):
Process positions: (0,0), (0,1), (0,2)

Position (0,0):
1 -> saved
13 -> position of 1
16 -> position of 13
4 -> position of 16
saved(1) -> position of 4

After processing layer 0:
13 9  5  1
14 6  7  2
15 10 11 3
16 12 8  4

Layer 1 (inner 2x2):
Process position: (1,1)

Final:
13 9  5  1
14 10 6  2
15 11 7  3
16 12 8  4
```

### Example 3: 5×5 Matrix with Center

```
Original:
1  2  3  4  5
6  7  8  9  10
11 12 13 14 15
16 17 18 19 20
21 22 23 24 25

Note: Element 13 at [2][2] is the center
After rotation, 13 stays at [2][2]

After 90° CW:
21 16 11 6  1
22 17 12 7  2
23 18 13 8  3
24 19 14 9  4
25 20 15 10 5
```

---

## Common Mistakes and Pitfalls

### Mistake 1: Wrong Transpose Range
```python
# WRONG: This transposes twice!
for i in range(n):
    for j in range(n):
        matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]

# CORRECT: Only upper triangle
for i in range(n):
    for j in range(i, n):
        matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
```

### Mistake 2: Creating New Matrix
```python
# WRONG: Problem requires in-place
def rotate(matrix):
    n = len(matrix)
    result = [[0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            result[j][n-1-i] = matrix[i][j]
    return result  # Not in-place!

# CORRECT: Modify original matrix
```

### Mistake 3: Incorrect Layer Boundaries
```python
# WRONG: Processes too many elements
for layer in range(n):  # Should be n // 2
    ...

# CORRECT:
for layer in range(n // 2):
    ...
```

### Mistake 4: Off-by-One Errors
```python
# WRONG: Range includes last element twice
for i in range(first, last + 1):  # Incorrect

# CORRECT:
for i in range(first, last):  # Correct
```

---

## Optimization Techniques

### Optimization 1: In-Place Reverse
```python
# Instead of matrix[i].reverse() which may create temporary space
# Use two-pointer approach
def reverse_row(row):
    left, right = 0, len(row) - 1
    while left < right:
        row[left], row[right] = row[right], row[left]
        left += 1
        right -= 1
```

### Optimization 2: Minimize Swaps
```python
# In transpose, only process upper triangle
for i in range(n):
    for j in range(i + 1, n):  # Start from i+1, skip diagonal
        matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
```

### Optimization 3: Cache-Friendly Access
```python
# Access memory in row-major order when possible
# This is naturally done in transpose + reverse approach
```

---

## Testing Strategy

### Test Cases

```python
def test_rotate():
    # Test 1: Basic 3x3
    matrix1 = [[1,2,3],[4,5,6],[7,8,9]]
    rotate(matrix1)
    assert matrix1 == [[7,4,1],[8,5,2],[9,6,3]]
    
    # Test 2: 4x4
    matrix2 = [[5,1,9,11],[2,4,8,10],[13,3,6,7],[15,14,12,16]]
    rotate(matrix2)
    assert matrix2 == [[15,13,2,5],[14,3,4,1],[12,6,8,9],[16,7,10,11]]
    
    # Test 3: 1x1
    matrix3 = [[1]]
    rotate(matrix3)
    assert matrix3 == [[1]]
    
    # Test 4: 2x2
    matrix4 = [[1,2],[3,4]]
    rotate(matrix4)
    assert matrix4 == [[3,1],[4,2]]
    
    # Test 5: Negative numbers
    matrix5 = [[-1,-2],[-3,-4]]
    rotate(matrix5)
    assert matrix5 == [[-3,-1],[-4,-2]]
    
    # Test 6: Duplicates
    matrix6 = [[1,1],[1,1]]
    rotate(matrix6)
    assert matrix6 == [[1,1],[1,1]]
    
    # Test 7: 5x5 with center
    matrix7 = [[1,2,3,4,5],[6,7,8,9,10],[11,12,13,14,15],[16,17,18,19,20],[21,22,23,24,25]]
    rotate(matrix7)
    expected = [[21,16,11,6,1],[22,17,12,7,2],[23,18,13,8,3],[24,19,14,9,4],[25,20,15,10,5]]
    assert matrix7 == expected
    
    print("All tests passed!")
```

---

## Related Problems

### 1. Transpose Matrix (LeetCode 867)
```python
def transpose(matrix):
    """
    Transpose a matrix (not necessarily square).
    For m×n matrix, result is n×m.
    """
    m, n = len(matrix), len(matrix[0])
    result = [[0] * m for _ in range(n)]
    
    for i in range(m):
        for j in range(n):
            result[j][i] = matrix[i][j]
    
    return result
```

### 2. Rotate Image Counterclockwise
```python
def rotate_ccw(matrix):
    """90° counterclockwise = transpose + reverse rows"""
    n = len(matrix)
    
    # Transpose
    for i in range(n):
        for j in range(i, n):
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    
    # Reverse rows
    matrix.reverse()
```

### 3. Determine if Matrix is Rotation
```python
def is_rotation(matrix1, matrix2):
    """Check if matrix2 is any 90° rotation of matrix1."""
    if len(matrix1) != len(matrix2):
        return False
    
    # Check 0°, 90°, 180°, 270°
    for _ in range(4):
        if matrix1 == matrix2:
            return True
        rotate_90_cw(matrix2)
    
    return False
```

### 4. Spiral Matrix (LeetCode 54)
Similar layer-by-layer processing technique.

### 5. Flip Image (LeetCode 832)
```python
def flipAndInvertImage(image):
    """Flip horizontally then invert (0->1, 1->0)."""
    for row in image:
        row.reverse()
        for i in range(len(row)):
            row[i] ^= 1  # XOR to flip bit
    return image
```

---

## Practice Problems

### Beginner Level
1. LeetCode 867: Transpose Matrix
2. LeetCode 832: Flipping an Image
3. Rotate 2×2 matrix

### Intermediate Level
4. LeetCode 48: Rotate Image (this problem)
5. Rotate matrix 180°
6. Rotate matrix counterclockwise
7. Check if one matrix is rotation of another

### Advanced Level
8. Rotate arbitrary rectangular matrix
9. LeetCode 54: Spiral Matrix
10. LeetCode 59: Spiral Matrix II
11. Rotate 3D matrix
12. Minimum rotations to match pattern

---

## Interview Tips

### What Interviewers Look For

1. **Pattern Recognition:** Can you identify this as transpose + reverse?
2. **Multiple Solutions:** Can you explain different approaches?
3. **In-Place Modification:** Understanding space constraints
4. **Edge Cases:** Handling 1×1, 2×2, odd/even dimensions
5. **Code Quality:** Clean, bug-free implementation

### How to Approach in Interview

1. **Clarify Requirements:**
   - Confirm 90° clockwise rotation
   - Confirm in-place requirement
   - Ask about matrix size constraints

2. **Explain Multiple Approaches:**
   - Start with transpose + reverse (simplest)
   - Mention layer-by-layer as alternative
   - Discuss trade-offs

3. **Choose Implementation:**
   - Transpose + reverse is clearest and easiest to code
   - Less prone to off-by-one errors

4. **Code Carefully:**
   - Write clean, readable code
   - Add comments for clarity
   - Test with example

5. **Test Thoroughly:**
   - Test with given examples
   - Test edge cases (1×1, 2×2)
   - Walk through your code

### Time Management
- 2 min: Clarify problem
- 3 min: Explain approach
- 10 min: Code solution
- 5 min: Test and verify

---

## Complexity Comparison

| Approach | Time | Space | Readability | Error-Prone |
|----------|------|-------|-------------|-------------|
| Transpose + Reverse | O(n²) | O(1) | ⭐⭐⭐⭐⭐ | Low |
| Layer-by-Layer | O(n²) | O(1) | ⭐⭐⭐ | Medium |
| Four-Way Swap | O(n²) | O(1) | ⭐⭐ | High |

**Recommendation:** Use Transpose + Reverse for interviews unless specifically asked for alternatives.

---

## Visual Cheat Sheet

```
90° CLOCKWISE ROTATION

Method 1: Transpose + Reverse Rows
┌─────┐    Transpose    ┌─────┐    Reverse    ┌─────┐
│ 1 2 │    ========>    │ 1 3 │    =======>   │ 3 1 │
│ 3 4 │                 │ 2 4 │               │ 4 2 │
└─────┘                 └─────┘               └─────┘

90° COUNTERCLOCKWISE ROTATION

Method: Transpose + Reverse Columns (or reverse rows of matrix)
┌─────┐    Transpose    ┌─────┐    Reverse    ┌─────┐
│ 1 2 │    ========>    │ 1 3 │    Columns    │ 2 4 │
│ 3 4 │                 │ 2 4 │    =======>   │ 1 3 │
└─────┘                 └─────┘               └─────┘

180° ROTATION

Method: Reverse Matrix + Reverse Rows
┌─────┐    Reverse     ┌─────┐    Reverse    ┌─────┐
│ 1 2 │     Matrix     │ 3 4 │     Rows      │ 4 3 │
│ 3 4 │    =======>    │ 1 2 │    =======>   │ 2 1 │
└─────┘                └─────┘               └─────┘

POSITION MAPPING (3×3)
[0,0] [0,1] [0,2]        [2,0] [1,0] [0,0]
[1,0] [1,1] [1,2]   =>   [2,1] [1,1] [0,1]
[2,0] [2,1] [2,2]        [2,2] [1,2] [0,2]

Formula: (i,j) -> (j, n-1-i)
```

---

## Summary

### Key Takeaways

1. **Best Approach:** Transpose + Reverse (simplest and most elegant)
2. **Formula:** matrix[i][j] -> matrix[j][n-1-i]
3. **Time Complexity:** O(n²) for all approaches
4. **Space Complexity:** O(1) in-place modification
5. **Pattern:** Matrix transformation problems

### When to Use This Pattern
- Matrix rotation problems
- Image transformation
- Coordinate system changes
- In-place matrix manipulation

### Remember
- Transpose: Swap across main diagonal
- Reverse: Flip horizontally
- Combined: 90° clockwise rotation
- For counterclockwise: Transpose + Reverse matrix rows
- For 180°: Reverse matrix + Reverse each row

---

## Additional Resources

### Further Reading
- Introduction to Algorithms (CLRS) - Matrix Operations
- Linear Algebra and Matrix Transformations
- Computer Graphics - Rotation Matrices

### Related Topics
- Matrix multiplication
- Determinants and inversions
- Affine transformations
- Image processing algorithms

### Practice Platforms
- LeetCode: Matrix section
- HackerRank: Arrays and Matrices
- CodeForces: 2D Array problems

---

## Code Templates

### Python Template
```python
def rotate(matrix):
    """Rotate matrix 90° clockwise in-place."""
    n = len(matrix)
    
    # Transpose
    for i in range(n):
        for j in range(i, n):
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    
    # Reverse each row
    for i in range(n):
        matrix[i].reverse()
```

### Java Template
```java
public void rotate(int[][] matrix) {
    int n = matrix.length;
    
    // Transpose
    for (int i = 0; i < n; i++) {
        for (int j = i; j < n; j++) {
            int temp = matrix[i][j];
            matrix[i][j] = matrix[j][i];
            matrix[j][i] = temp;
        }
    }
    
    // Reverse each row
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n / 2; j++) {
            int temp = matrix[i][j];
            matrix[i][j] = matrix[i][n - 1 - j];
            matrix[i][n - 1 - j] = temp;
        }
    }
}
```

### C++ Template
```cpp
void rotate(vector<vector<int>>& matrix) {
    int n = matrix.size();
    
    // Transpose
    for (int i = 0; i < n; i++) {
        for (int j = i; j < n; j++) {
            swap(matrix[i][j], matrix[j][i]);
        }
    }
    
    // Reverse each row
    for (int i = 0; i < n; i++) {
        reverse(matrix[i].begin(), matrix[i].end());
    }
}
```

---

**Last Updated:** 2024
**Difficulty:** Medium
**Category:** Matrix Manipulation, Array Transformation
**Companies:** Amazon, Microsoft, Google, Facebook, Apple, Adobe
