# LeetCode #73: Set Matrix Zeroes

**Difficulty:** Medium  
**Pattern:** In-place Marking / Matrix Manipulation  
**Key Concept:** Use first row/column as markers to achieve O(1) space  
**Time Complexity:** O(m × n)  
**Space Complexity:** O(1)

---

## Table of Contents
1. [Problem Statement](#problem-statement)
2. [Key Insights](#key-insights)
3. [Approach 1: Brute Force with Matrix Copy](#approach-1-brute-force-with-matrix-copy)
4. [Approach 2: O(m+n) Space with Sets](#approach-2-omn-space-with-sets)
5. [Approach 3: O(1) Space - First Row/Column as Markers](#approach-3-o1-space---first-rowcolumn-as-markers)
6. [Visual Walkthrough](#visual-walkthrough)
7. [Edge Cases & Special Scenarios](#edge-cases--special-scenarios)
8. [Common Pitfalls](#common-pitfalls)
9. [Practice Problems](#practice-problems)
10. [Interview Tips](#interview-tips)

---

## Problem Statement

Given an `m × n` integer matrix `matrix`, if an element is `0`, set its entire row and column to `0`'s.

**You must do it in place.**

### Example 1:
```
Input: matrix = [[1,1,1],
                 [1,0,1],
                 [1,1,1]]

Output: [[1,0,1],
         [0,0,0],
         [1,0,1]]
```

### Example 2:
```
Input: matrix = [[0,1,2,0],
                 [3,4,5,2],
                 [1,3,1,5]]

Output: [[0,0,0,0],
         [0,4,5,0],
         [0,3,1,0]]
```

### Constraints:
- `m == matrix.length`
- `n == matrix[0].length`
- `1 <= m, n <= 200`
- `-2^31 <= matrix[i][j] <= 2^31 - 1`

### Follow-up:
- A straightforward solution using O(mn) space is probably a bad idea.
- A simple improvement uses O(m + n) space, but still not the best solution.
- Could you devise a constant space solution?

---

## Key Insights

### The Core Challenge
The main difficulty is that we need to:
1. **Identify** which rows and columns should be zeroed
2. **Mark** them without losing information about other zeros
3. **Zero out** the appropriate cells
4. Do this **in-place** without extra space

### Why This is Tricky
If we immediately zero out rows and columns when we find a zero, we'll create false zeros that will cascade and zero out the entire matrix.

```
Wrong approach:
[1, 0, 1]     Find 0 at (0,1)      [0, 0, 0]
[1, 1, 1]  -> Zero row 0 & col 1 -> [1, 0, 1]  -> Now (0,0) is 0, so zero row 0 again!
[1, 1, 1]                            [1, 0, 1]     This cascades incorrectly.
```

### The Breakthrough
Use the **first row and first column** of the matrix itself as storage for which rows/columns need to be zeroed. This achieves O(1) extra space!

---

## Approach 1: Brute Force with Matrix Copy

### Concept
Create a copy of the matrix, scan for zeros in the original, and mark zeros in the copy.

### Algorithm
1. Create a copy of the matrix
2. Iterate through original matrix
3. When finding a zero, mark entire row and column in the copy as zero
4. Copy the result back to original matrix

### Implementation
```python
def setZeroes(matrix):
    """
    Time Complexity: O(m * n * (m + n)) - For each zero, we traverse row and column
    Space Complexity: O(m * n) - Full matrix copy
    """
    m, n = len(matrix), len(matrix[0])
    copy = [row[:] for row in matrix]  # Deep copy
    
    # Find all zeros in original
    for i in range(m):
        for j in range(n):
            if matrix[i][j] == 0:
                # Zero out row i in copy
                for k in range(n):
                    copy[i][k] = 0
                # Zero out column j in copy
                for k in range(m):
                    copy[k][j] = 0
    
    # Copy back to original
    for i in range(m):
        for j in range(n):
            matrix[i][j] = copy[i][j]
```

### Analysis
**Pros:**
- Simple to understand and implement
- No risk of losing information

**Cons:**
- O(m × n) space complexity
- Inefficient time complexity
- Not acceptable for follow-up question

**When to Use:**
- Only as a starting point in interviews to show you understand the problem
- Never use this as a final solution

---

## Approach 2: O(m+n) Space with Sets

### Concept
Store which rows and columns need to be zeroed using sets or arrays.

### Algorithm
1. First pass: Record rows and columns that contain zeros
2. Second pass: Zero out recorded rows and columns

### Visual Example
```
Original Matrix:          Track zeros:
[1, 0, 1]                rows_to_zero = {1}
[1, 1, 1]                cols_to_zero = {1}
[0, 1, 1]

After zeroing marked rows/cols:
[0, 0, 0]   <- row 1 was marked
[0, 0, 1]   <- col 1 was marked  
[0, 0, 0]   <- row 2 was marked
```

### Implementation - Using Sets
```python
def setZeroes(matrix):
    """
    Time Complexity: O(m * n) - Two passes through matrix
    Space Complexity: O(m + n) - Sets for rows and columns
    """
    m, n = len(matrix), len(matrix[0])
    rows_to_zero = set()
    cols_to_zero = set()
    
    # First pass: Identify rows and columns to zero
    for i in range(m):
        for j in range(n):
            if matrix[i][j] == 0:
                rows_to_zero.add(i)
                cols_to_zero.add(j)
    
    # Second pass: Zero out marked rows
    for i in rows_to_zero:
        for j in range(n):
            matrix[i][j] = 0
    
    # Zero out marked columns
    for j in cols_to_zero:
        for i in range(m):
            matrix[i][j] = 0
```

### Implementation - Using Boolean Arrays
```python
def setZeroes(matrix):
    """
    Alternative with boolean arrays - same complexity
    """
    m, n = len(matrix), len(matrix[0])
    zero_row = [False] * m
    zero_col = [False] * n
    
    # Mark rows and columns
    for i in range(m):
        for j in range(n):
            if matrix[i][j] == 0:
                zero_row[i] = True
                zero_col[j] = True
    
    # Apply zeros
    for i in range(m):
        for j in range(n):
            if zero_row[i] or zero_col[j]:
                matrix[i][j] = 0
```

### Analysis
**Pros:**
- Clean and easy to understand
- Optimal time complexity O(m × n)
- Straightforward logic flow

**Cons:**
- Still uses O(m + n) extra space
- Not the optimal space solution

**When to Use:**
- Good intermediate solution in interviews
- Shows progression toward optimal solution
- Acceptable if O(1) space not explicitly required

---

## Approach 3: O(1) Space - First Row/Column as Markers

### Concept
Use the first row and first column of the matrix itself to store information about which rows and columns should be zeroed.

### Key Idea: The Storage Location
```
Matrix:                  Use first row/col as flags:
[x  x  x  x]            [R0 C1 C2 C3]  <- First row stores column flags
[x  x  x  x]            [R1  .  .  .]  <- First col stores row flags  
[x  x  x  x]            [R2  .  .  .]
                        [R3  .  .  .]

Problem: Position [0][0] overlaps! It belongs to both row 0 and column 0.
Solution: Use a separate variable for one of them (typically column 0).
```

### The Algorithm - Step by Step

#### Phase 1: Check First Row and Column
First, we need to know if the first row or first column themselves contain any zeros, because we'll be using them as markers.

```python
first_row_has_zero = any(matrix[0][j] == 0 for j in range(n))
first_col_has_zero = any(matrix[i][0] == 0 for i in range(m))
```

#### Phase 2: Mark Using First Row/Column
Scan the matrix (excluding first row/column) and use first row/column as markers.

```python
# Start from (1,1) to avoid overwriting markers
for i in range(1, m):
    for j in range(1, n):
        if matrix[i][j] == 0:
            matrix[i][0] = 0  # Mark row i should be zeroed
            matrix[0][j] = 0  # Mark column j should be zeroed
```

#### Phase 3: Zero Based on Markers
Use the markers to zero out cells (excluding first row/column).

```python
for i in range(1, m):
    for j in range(1, n):
        if matrix[i][0] == 0 or matrix[0][j] == 0:
            matrix[i][j] = 0
```

#### Phase 4: Handle First Row and Column
Finally, handle the first row and column based on flags from Phase 1.

```python
if first_row_has_zero:
    for j in range(n):
        matrix[0][j] = 0

if first_col_has_zero:
    for i in range(m):
        matrix[i][0] = 0
```

### Complete Implementation
```python
def setZeroes(matrix):
    """
    Time Complexity: O(m * n)
    Space Complexity: O(1)
    """
    if not matrix or not matrix[0]:
        return
    
    m, n = len(matrix), len(matrix[0])
    
    # Phase 1: Check if first row and first column have zeros
    first_row_has_zero = False
    first_col_has_zero = False
    
    for j in range(n):
        if matrix[0][j] == 0:
            first_row_has_zero = True
            break
    
    for i in range(m):
        if matrix[i][0] == 0:
            first_col_has_zero = True
            break
    
    # Phase 2: Use first row and column as markers
    for i in range(1, m):
        for j in range(1, n):
            if matrix[i][j] == 0:
                matrix[i][0] = 0
                matrix[0][j] = 0
    
    # Phase 3: Zero out cells based on markers
    for i in range(1, m):
        for j in range(1, n):
            if matrix[i][0] == 0 or matrix[0][j] == 0:
                matrix[i][j] = 0
    
    # Phase 4: Handle first row and column
    if first_row_has_zero:
        for j in range(n):
            matrix[0][j] = 0
    
    if first_col_has_zero:
        for i in range(m):
            matrix[i][0] = 0
```

### Alternative Implementation - Using Column 0 Flag
```python
def setZeroes(matrix):
    """
    Use matrix[0][0] for row 0, separate variable for column 0
    """
    m, n = len(matrix), len(matrix[0])
    col0_has_zero = False
    
    # Phase 1 & 2 combined: Check and mark
    for i in range(m):
        # Check if first column has zero
        if matrix[i][0] == 0:
            col0_has_zero = True
        
        # Mark from second column onwards
        for j in range(1, n):
            if matrix[i][j] == 0:
                matrix[i][0] = 0
                matrix[0][j] = 0
    
    # Phase 3: Zero based on markers (process backwards to avoid interference)
    for i in range(m - 1, -1, -1):
        for j in range(n - 1, 0, -1):
            if matrix[i][0] == 0 or matrix[0][j] == 0:
                matrix[i][j] = 0
        
        # Handle first column
        if col0_has_zero:
            matrix[i][0] = 0
```

---

## Visual Walkthrough

### Example: Step-by-Step Execution

**Initial Matrix:**
```
     j=0  j=1  j=2  j=3
i=0  [ 1    1    1    0 ]
i=1  [ 1    1    1    1 ]
i=2  [ 1    0    1    1 ]
i=3  [ 1    1    1    1 ]
```

### Phase 1: Check First Row and Column

**Check First Row:**
```
Scanning row 0: [1, 1, 1, 0]
Found 0 at position (0, 3)
first_row_has_zero = True
```

**Check First Column:**
```
Scanning column 0: [1, 1, 1, 1]
No zeros found
first_col_has_zero = False
```

### Phase 2: Mark Using First Row/Column

**Scan from (1,1) onwards:**

```
At (2, 1): Found 0!
  -> Mark matrix[2][0] = 0  (row 2 needs to be zeroed)
  -> Mark matrix[0][1] = 0  (column 1 needs to be zeroed)

Matrix after marking:
     j=0  j=1  j=2  j=3
i=0  [ 1    0    1    0 ]  <- markers set
i=1  [ 1    1    1    1 ]
i=2  [ 0    0    1    1 ]  <- markers set
i=3  [ 1    1    1    1 ]
```

**Note:** We also need to mark column 3 because original (0,3) was 0:
```
Process (0, 3) from original: It's in first row, which we handle separately
But we should mark it during phase 2 scan...

Actually, let's be more careful. In phase 2, we only scan from (1,1).
The zero at (0,3) is in the first row, so it gets handled by first_row_has_zero flag.
However, we still need to mark that column 3 should be zeroed for other rows.

Let me reconsider: The algorithm starts from (1,1), so it won't see (0,3).
We need to include first row/col in our marking phase!
```

**Corrected Algorithm** - Let's trace again:

```python
# After checking first row/col flags, we scan entire matrix including first row/col
# for marking purposes:

for i in range(1, m):  # rows 1 to m-1
    for j in range(1, n):  # cols 1 to n-1
        if matrix[i][j] == 0:
            matrix[i][0] = 0
            matrix[0][j] = 0
```

So scanning (1,1) to (3,3):
- (2,1) is 0 → mark matrix[2][0]=0 and matrix[0][1]=0

But what about (0,3)? It's in the first row, not in our scan range.
The algorithm handles this through `first_row_has_zero` flag.

Wait, there's a subtlety: (0,3) being zero should also mark column 3 for rows 1-3!

**Better Approach: Scan Including First Row/Col for Marking**

```python
for i in range(m):
    for j in range(n):
        if matrix[i][j] == 0:
            matrix[i][0] = 0  # Mark row
            matrix[0][j] = 0  # Mark column
```

But this overwrites first row/col before we check them! That's why we save flags first.

Let me trace the correct algorithm:

### Corrected Full Walkthrough

**Initial Matrix:**
```
     0    1    2    3
0  [ 1    1    1    0 ]
1  [ 1    1    1    1 ]
2  [ 1    0    1    1 ]
3  [ 1    1    1    1 ]
```

**Phase 1: Save First Row/Col Status**
```
first_row_has_zero = True   (because matrix[0][3] == 0)
first_col_has_zero = False  (no zeros in column 0)
```

**Phase 2: Mark Using First Row/Col as Markers**

Scan from (1,1) to (m-1, n-1):
```
At (2, 1): matrix[2][1] == 0
  Action: 
    - Set matrix[2][0] = 0  (mark row 2)
    - Set matrix[0][1] = 0  (mark column 1)

Matrix after phase 2:
     0    1    2    3
0  [ 1    0    1    0 ]  <- matrix[0][1] marked
1  [ 1    1    1    1 ]
2  [ 0    0    1    1 ]  <- matrix[2][0] marked
3  [ 1    1    1    1 ]
```

**Phase 3: Zero Based on Markers**

For each cell (i,j) where i≥1, j≥1:
  If matrix[i][0]==0 OR matrix[0][j]==0, set matrix[i][j]=0

```
Row 1: matrix[1][0]=1 (not marked)
  - (1,1): matrix[0][1]=0 → zero it → matrix[1][1]=0
  - (1,2): matrix[0][2]=1 → keep as is
  - (1,3): matrix[0][3]=0 → zero it → matrix[1][3]=0

Row 2: matrix[2][0]=0 (marked - zero entire row)
  - (2,1): zero it → matrix[2][1]=0
  - (2,2): zero it → matrix[2][2]=0
  - (2,3): zero it → matrix[2][3]=0

Row 3: matrix[3][0]=1 (not marked)
  - (3,1): matrix[0][1]=0 → zero it → matrix[3][1]=0
  - (3,2): matrix[0][2]=1 → keep as is
  - (3,3): matrix[0][3]=0 → zero it → matrix[3][3]=0

Matrix after phase 3:
     0    1    2    3
0  [ 1    0    1    0 ]
1  [ 1    0    1    0 ]
2  [ 0    0    0    0 ]
3  [ 1    0    1    0 ]
```

**Phase 4: Handle First Row and Column**

```
first_row_has_zero = True → Zero out entire first row
first_col_has_zero = False → Keep first column as is

Final Matrix:
     0    1    2    3
0  [ 0    0    0    0 ]  <- first row zeroed
1  [ 1    0    1    0 ]
2  [ 0    0    0    0 ]
3  [ 1    0    1    0 ]
```

**Wait, this doesn't match expected output!** Let me reconsider...

Original matrix had zeros at (0,3) and (2,1).
- (0,3) zero should zero: entire row 0, entire column 3
- (2,1) zero should zero: entire row 2, entire column 1

Expected result:
```
     0    1    2    3
0  [ 0    0    0    0 ]  <- row 0 zeroed
1  [ 1    0    1    0 ]  <- col 1 and col 3 zeroed
2  [ 0    0    0    0 ]  <- row 2 zeroed
3  [ 1    0    1    0 ]  <- col 1 and col 3 zeroed
```

My trace above is actually correct! ✓

---

## Edge Cases & Special Scenarios

### Edge Case 1: Single Row Matrix
```python
matrix = [[0, 1, 2, 0]]

# First row has zero → entire row becomes zero
result = [[0, 0, 0, 0]]
```

### Edge Case 2: Single Column Matrix
```python
matrix = [[1],
          [0],
          [3]]

# Second row has zero → entire matrix becomes zero (single column)
result = [[0],
          [0],
          [0]]
```

### Edge Case 3: 1×1 Matrix
```python
matrix = [[0]]  → [[0]]
matrix = [[1]]  → [[1]]
```

### Edge Case 4: All Zeros
```python
matrix = [[0, 0],
          [0, 0]]

# Already all zeros → no change
result = [[0, 0],
          [0, 0]]
```

### Edge Case 5: No Zeros
```python
matrix = [[1, 2],
          [3, 4]]

# No zeros → no change
result = [[1, 2],
          [3, 4]]
```

### Edge Case 6: Zeros Only in First Row
```python
matrix = [[0, 1, 2],
          [3, 4, 5]]

# Zero at (0,0) → entire row 0 and column 0 become zero
result = [[0, 0, 0],
          [0, 4, 5]]
```

### Edge Case 7: Zeros Only in First Column
```python
matrix = [[1, 2, 3],
          [0, 4, 5]]

# Zero at (1,0) → entire row 1 and column 0 become zero
result = [[0, 2, 3],
          [0, 0, 0]]
```

### Edge Case 8: Zero at (0,0)
```python
matrix = [[0, 1, 2],
          [3, 4, 5],
          [6, 7, 8]]

# Zero at (0,0) → entire first row and first column become zero
result = [[0, 0, 0],
          [0, 4, 5],
          [0, 7, 8]]
```

---

## Common Pitfalls

### Pitfall 1: Zeroing While Scanning
```python
# WRONG: Immediate zeroing creates cascading effect
for i in range(m):
    for j in range(n):
        if matrix[i][j] == 0:
            # This creates false zeros!
            for k in range(n):
                matrix[i][k] = 0
            for k in range(m):
                matrix[k][j] = 0
```

**Why It's Wrong:** New zeros created will be treated as original zeros in subsequent iterations.

### Pitfall 2: Wrong Order in Phase 4
```python
# WRONG: Handling first row/col before processing inner cells
if first_row_has_zero:
    for j in range(n):
        matrix[0][j] = 0  # This destroys our markers!

# Then trying to use matrix[0][j] as markers
for i in range(1, m):
    for j in range(1, n):
        if matrix[0][j] == 0:  # These markers are now corrupted!
            matrix[i][j] = 0
```

**Solution:** Always process inner cells first, then handle first row/column last.

### Pitfall 3: Not Saving First Row/Col Status
```python
# WRONG: Directly marking without saving original status
for i in range(m):
    for j in range(n):
        if matrix[i][j] == 0:
            matrix[i][0] = 0
            matrix[0][j] = 0
            # Now we can't tell if first row/col originally had zeros!
```

**Solution:** Check and save first row/column status before any marking.

### Pitfall 4: Off-by-One Errors
```python
# WRONG: Including first row/col in phase 3 processing
for i in range(m):  # Should be range(1, m)
    for j in range(n):  # Should be range(1, n)
        if matrix[i][0] == 0 or matrix[0][j] == 0:
            matrix[i][j] = 0
```

**Why It's Wrong:** This overwrites the marker row/column before we finish using them.

### Pitfall 5: Forgetting Edge Cases
```python
# Not handling empty matrix
if not matrix or not matrix[0]:
    return

# Not handling single row/column
if m == 1 or n == 1:
    if 0 in matrix[0] if m == 1 else any(matrix[i][0] == 0 for i in range(m)):
        # Zero entire matrix
```

### Pitfall 6: Overcomplicating the Logic
```python
# WRONG: Trying to be too clever with single pass
# This often leads to bugs and is harder to debug
for i in range(m):
    for j in range(n):
        if matrix[i][j] == 0:
            # Complex logic trying to mark and zero simultaneously
            # Usually results in errors
```

**Solution:** Keep it simple with clear phases. Readability > Cleverness.

---

## Practice Problems

### Similar Problems on LeetCode

1. **Game of Life (289)** - Medium
   - Similar in-place matrix modification
   - Use encoding to preserve state
   - Pattern: In-place state encoding

2. **Rotate Image (48)** - Medium
   - In-place matrix transformation
   - Layer-by-layer rotation
   - Pattern: In-place matrix manipulation

3. **Spiral Matrix (54)** - Medium
   - Matrix traversal with boundary tracking
   - Pattern: Layer-by-layer processing

4. **Spiral Matrix II (59)** - Medium
   - Fill matrix in spiral order
   - Pattern: Directional traversal

5. **Valid Sudoku (36)** - Medium
   - Matrix validation with constraints
   - Pattern: Row/column/box checking

6. **Search a 2D Matrix (74)** - Medium
   - Binary search in matrix
   - Pattern: Treating matrix as sorted array

7. **Search a 2D Matrix II (240)** - Medium
   - Search in row-wise and column-wise sorted matrix
   - Pattern: Staircase search

8. **Diagonal Traverse (498)** - Medium
   - Traverse matrix diagonally
   - Pattern: Directional iteration

### Related Concepts to Study

1. **In-place Algorithms**
   - Modifying data structure without extra space
   - Using existing structure for temporary storage
   - State encoding techniques

2. **Matrix Traversal Patterns**
   - Row-major vs column-major order
   - Layer-by-layer processing
   - Diagonal traversal
   - Spiral traversal

3. **Bit Manipulation for State Storage**
   - Using bits to store multiple states
   - Relevant when values are bounded

4. **Two-Pointer Techniques in 2D**
   - Extending 1D two-pointer to matrices
   - Staircase approach

---

## Interview Tips

### How to Approach in an Interview

1. **Start with Clarifying Questions**
   ```
   - Can the matrix be empty?
   - What's the range of values? (Matters for encoding approaches)
   - Should I modify in-place or can I return a new matrix?
   - Are there time/space constraints?
   ```

2. **Discuss Multiple Approaches**
   - Start with brute force (shows you understand the problem)
   - Improve to O(m+n) space (shows optimization thinking)
   - Finally present O(1) space (shows mastery)

3. **Think Aloud**
   ```
   "The challenge is we need to mark cells to zero, but if we zero them 
   immediately, we lose information about which zeros were original.
   
   One approach is to use additional space to track rows and columns...
   
   But to achieve O(1) space, we could use the matrix itself for storage.
   The first row and column can serve as markers..."
   ```

4. **Handle Edge Cases**
   - Don't forget to mention edge cases
   - Show you think about boundary conditions
   - Single row/column, all zeros, no zeros, zero at (0,0)

5. **Test Your Code**
   ```
   Walk through a small example:
   "Let me trace through this 3×3 example to verify..."
   ```

### Common Interview Variations

**Variation 1:** "What if you need to zero rows but leave columns?"
```python
def setRowsZero(matrix):
    m, n = len(matrix), len(matrix[0])
    rows = set()
    
    for i in range(m):
        for j in range(n):
            if matrix[i][j] == 0:
                rows.add(i)
    
    for i in rows:
        for j in range(n):
            matrix[i][j] = 0
```

**Variation 2:** "What if zeros should spread like a virus, iteratively?"
```python
def spreadZeros(matrix, iterations):
    # Each iteration, zeros spread to adjacent cells
    # This is similar to Game of Life
    for _ in range(iterations):
        zeros = []
        for i in range(len(matrix)):
            for j in range(len(matrix[0])):
                if matrix[i][j] == 0:
                    zeros.append((i, j))
        
        for i, j in zeros:
            for di, dj in [(0,1), (1,0), (0,-1), (-1,0)]:
                ni, nj = i + di, j + dj
                if 0 <= ni < len(matrix) and 0 <= nj < len(matrix[0]):
                    matrix[ni][nj] = 0
```

**Variation 3:** "Set to zero if sum of row + column is even/odd"
```python
def setZerosBySum(matrix):
    m, n = len(matrix), len(matrix[0])
    rows, cols = set(), set()
    
    for i in range(m):
        if sum(matrix[i]) % 2 == 0:
            rows.add(i)
    
    for j in range(n):
        if sum(matrix[i][j] for i in range(m)) % 2 == 0:
            cols.add(j)
    
    for i in rows:
        for j in range(n):
            matrix[i][j] = 0
    
    for j in cols:
        for i in range(m):
            matrix[i][j] = 0
```

### Time Complexity Analysis

**All Approaches Have Same Time Complexity: O(m × n)**

Why? Because we must examine every cell at least once.

**Detailed Breakdown for O(1) Space Approach:**
- Phase 1: O(m + n) - Check first row and column
- Phase 2: O(m × n) - Scan and mark
- Phase 3: O(m × n) - Zero based on markers
- Phase 4: O(m + n) - Handle first row and column

Total: O(m + n + m×n + m×n + m + n) = O(m × n)

### Space Complexity Analysis

| Approach | Space | Explanation |
|----------|-------|-------------|
| Brute Force | O(m × n) | Full matrix copy |
| Sets/Arrays | O(m + n) | Store row and column indices |
| First Row/Col | O(1) | Only 2 boolean variables |

**Note:** O(1) doesn't mean zero space, just constant space (not dependent on input size).

---

## Additional Insights

### Why Use First Row/Column?

1. **They're Always Present:** Every matrix has a first row and column
2. **Sufficient Storage:** We need m + n flags, first row/col provides exactly that
3. **Easy Access:** Natural indexing with matrix[i][0] and matrix[0][j]
4. **In-place:** No external data structures needed

### Alternative: Using Sentinel Values

If matrix values are bounded (e.g., only positive integers), we could use a sentinel:

```python
def setZeroes(matrix):
    # Use float('inf') as sentinel
    m, n = len(matrix), len(matrix[0])
    
    # Mark cells to be zeroed with sentinel
    for i in range(m):
        for j in range(n):
            if matrix[i][j] == 0:
                # Mark row
                for k in range(n):
                    if matrix[i][k] != 0:
                        matrix[i][k] = float('inf')
                # Mark column
                for k in range(m):
                    if matrix[k][j] != 0:
                        matrix[k][j] = float('inf')
    
    # Convert sentinels to zeros
    for i in range(m):
        for j in range(n):
            if matrix[i][j] == float('inf'):
                matrix[i][j] = 0
```

**Limitation:** Requires a sentinel value not in the original matrix.

### Optimization: Early Termination

```python
def setZeroesOptimized(matrix):
    m, n = len(matrix), len(matrix[0])
    
    # If entire first row/col is zero, entire matrix becomes zero
    all_zero_row = all(matrix[i][j] == 0 for i in range(m) for j in range(n))
    if all_zero_row:
        for i in range(m):
            for j in range(n):
                matrix[i][j] = 0
        return
    
    # Continue with normal algorithm...
```

---

## Summary Comparison Table

| Criterion | Brute Force | Sets/Arrays | First Row/Col |
|-----------|-------------|-------------|---------------|
| Time Complexity | O(m×n×(m+n)) | O(m×n) | O(m×n) |
| Space Complexity | O(m×n) | O(m+n) | O(1) |
| Passes Through Matrix | 2-3 | 2 | 3-4 |
| Ease of Implementation | Easy | Easy | Medium |
| Interview Acceptability | No | Yes | Optimal |
| Edge Case Handling | Simple | Simple | Requires Care |

---

## Key Takeaways

1. **Space-Time Tradeoff:** More space makes algorithm simpler, less space requires more careful logic

2. **Use Matrix Itself:** First row/column serving as markers is the key insight for O(1) space

3. **Order Matters:** Process inner cells before first row/column to avoid corrupting markers

4. **Save Original State:** Must save first row/column status before using them as markers

5. **Multiple Passes:** Don't try to do everything in one pass; clear phases reduce bugs

6. **Test Edge Cases:** Always consider single row/column, zeros at boundaries, all zeros, no zeros

---

## References and Further Reading

- [LeetCode Problem #73](https://leetcode.com/problems/set-matrix-zeroes/)
- Related Patterns: In-place Algorithms, Matrix Manipulation
- Similar Concepts: State Encoding, Marker-based Algorithms
- Books: "Cracking the Coding Interview" (Chapter 1: Arrays and Strings)

---

**Last Updated:** 2024
**Problem Difficulty:** Medium
**Success Rate:** ~50%
**Average Time to Solve:** 20-30 minutes

---

*This study note is part of a comprehensive LeetCode problem series. For more problems focusing on matrix manipulation and in-place algorithms, see the related problems section.*
