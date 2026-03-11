# Binary Search - LeetCode #704

**Difficulty**: Easy  
**Pattern**: Binary Search Template  
**Frequency**: Very High (Core Algorithm)

---

## Problem Statement

Given an array of integers `nums` which is sorted in ascending order, and an integer `target`, write a function to search `target` in `nums`. If `target` exists, return its index. Otherwise, return `-1`.

You must write an algorithm with `O(log n)` runtime complexity.

**Example 1:**
```
Input: nums = [-1,0,3,5,9,12], target = 9
Output: 4
Explanation: 9 exists in nums and its index is 4
```

**Example 2:**
```
Input: nums = [-1,0,3,5,9,12], target = 2
Output: -1
Explanation: 2 does not exist in nums so return -1
```

**Constraints:**
- `1 <= nums.length <= 10^4`
- `-10^4 < nums[i], target < 10^4`
- All the integers in `nums` are **unique**
- `nums` is sorted in ascending order

---

## Core Concepts

### Binary Search Overview
Binary Search is a fundamental algorithm that efficiently finds a target value within a **sorted** array by repeatedly dividing the search interval in half.

**Key Characteristics:**
1. Requires **sorted** input
2. O(log n) time complexity
3. Repeatedly halves the search space
4. Two main templates: inclusive and exclusive bounds

### Why Binary Search?

**Linear Search (O(n)):**
```python
def linear_search(nums, target):
    for i in range(len(nums)):
        if nums[i] == target:
            return i
    return -1
```

**Binary Search (O(log n)):**
- For array of size 1,000,000:
  - Linear: up to 1,000,000 comparisons
  - Binary: maximum 20 comparisons (log₂(1,000,000) ≈ 20)

### The Binary Search Invariant

The algorithm maintains this invariant:
- **Target is in range [left, right]** (inclusive template)
- At each step, eliminate half the search space
- Continue until target found or range empty

---

## Solution Approach

### Template Pattern

The **standard inclusive template**:

```python
def binarySearch(nums, target):
    left, right = 0, len(nums) - 1
    
    while left <= right:  # Note: <= for inclusive
        mid = left + (right - left) // 2  # Avoid overflow
        
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1  # Search right half
        else:
            right = mid - 1  # Search left half
    
    return -1  # Not found
```

**Key Template Elements:**

1. **Initialization**: `left = 0, right = len(nums) - 1`
2. **Loop Condition**: `while left <= right`
3. **Mid Calculation**: `mid = left + (right - left) // 2`
4. **Update Rules**:
   - If too small: `left = mid + 1`
   - If too large: `right = mid - 1`

### Visual Walkthrough

**Example**: `nums = [2, 5, 8, 12, 16, 23, 38, 45, 56, 67, 78]`, `target = 23`

```
Step 1: Initial state
[2, 5, 8, 12, 16, 23, 38, 45, 56, 67, 78]
 L                  M                   R
left=0, right=10, mid=5, nums[5]=23 ✓ FOUND!
```

**Example**: `nums = [2, 5, 8, 12, 16, 23, 38, 45, 56, 67, 78]`, `target = 38`

```
Step 1:
[2, 5, 8, 12, 16, 23, 38, 45, 56, 67, 78]
 L                  M                   R
left=0, right=10, mid=5
nums[5]=23 < 38, search right → left=6

Step 2:
[2, 5, 8, 12, 16, 23, 38, 45, 56, 67, 78]
                      L   M           R
left=6, right=10, mid=8
nums[8]=56 > 38, search left → right=7

Step 3:
[2, 5, 8, 12, 16, 23, 38, 45, 56, 67, 78]
                      L/M R
left=6, right=7, mid=6
nums[6]=38 ✓ FOUND!
```

---

## Complete Solution

### Python Implementation

```python
from typing import List

class Solution:
    def search(self, nums: List[int], target: int) -> int:
        """
        Binary search with inclusive bounds [left, right].
        
        Time: O(log n) - halves search space each iteration
        Space: O(1) - constant extra space
        """
        left, right = 0, len(nums) - 1
        
        while left <= right:
            # Calculate mid to avoid integer overflow
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid
            elif nums[mid] < target:
                # Target is in right half
                left = mid + 1
            else:
                # Target is in left half
                right = mid - 1
        
        # Target not found
        return -1
```

### Alternative: Recursive Implementation

```python
class Solution:
    def search(self, nums: List[int], target: int) -> int:
        """Recursive binary search."""
        return self._binary_search(nums, target, 0, len(nums) - 1)
    
    def _binary_search(self, nums, target, left, right):
        # Base case: search space exhausted
        if left > right:
            return -1
        
        mid = left + (right - left) // 2
        
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            return self._binary_search(nums, target, mid + 1, right)
        else:
            return self._binary_search(nums, target, left, mid - 1)
```

**Recursive Complexity:**
- Time: O(log n) - same as iterative
- Space: O(log n) - call stack depth

---

## Binary Search Templates Comparison

### Template 1: Inclusive Bounds [left, right]

```python
def binarySearch1(nums, target):
    left, right = 0, len(nums) - 1
    
    while left <= right:  # <=
        mid = left + (right - left) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return -1
```

**When to use:**
- Standard exact match search
- Most common template
- Clear termination condition

### Template 2: Exclusive Right Bound [left, right)

```python
def binarySearch2(nums, target):
    left, right = 0, len(nums)  # right = len(nums)
    
    while left < right:  # <
        mid = left + (right - left) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid  # mid (not mid - 1)
    
    return -1
```

**When to use:**
- Finding insertion position
- Lower/upper bound searches

### Template 3: Three-way Partition [left+1, right-1]

```python
def binarySearch3(nums, target):
    if len(nums) == 0:
        return -1
    
    left, right = 0, len(nums) - 1
    
    while left + 1 < right:  # left and right not adjacent
        mid = left + (right - left) // 2
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid
        else:
            right = mid
    
    # Post-processing
    if nums[left] == target:
        return left
    if nums[right] == target:
        return right
    
    return -1
```

**When to use:**
- Avoiding index out of bounds
- Finding boundaries
- More complex search conditions

---

## Common Pitfalls & Edge Cases

### 1. Integer Overflow

**❌ Wrong:**
```python
mid = (left + right) // 2
# Can overflow if left + right > MAX_INT
```

**✅ Correct:**
```python
mid = left + (right - left) // 2
# Mathematically equivalent, no overflow
```

In Python, integers don't overflow, but good practice for other languages!

### 2. Infinite Loop

**❌ Wrong:**
```python
while left < right:  # Should be <=
    mid = left + (right - left) // 2
    if nums[mid] < target:
        left = mid  # Should be mid + 1
    else:
        right = mid - 1
# Can loop forever when left = mid
```

**✅ Correct:**
```python
while left <= right:
    mid = left + (right - left) // 2
    if nums[mid] < target:
        left = mid + 1  # Always move
    else:
        right = mid - 1
```

### 3. Off-by-One Errors

```python
# Template 1: Inclusive
left, right = 0, len(nums) - 1
while left <= right:
    ...
    left = mid + 1
    right = mid - 1

# Template 2: Exclusive right
left, right = 0, len(nums)
while left < right:
    ...
    left = mid + 1
    right = mid  # Not mid - 1
```

### 4. Empty Array

```python
def search(nums, target):
    if not nums:  # Check empty
        return -1
    
    left, right = 0, len(nums) - 1
    # ... rest of algorithm
```

### 5. Single Element

```python
nums = [5], target = 5
left = 0, right = 0
mid = 0, nums[0] = 5 ✓
# Works correctly with <= condition
```

---

## Complexity Analysis

### Time Complexity: O(log n)

**Why logarithmic?**
```
Iteration 1: n elements
Iteration 2: n/2 elements
Iteration 3: n/4 elements
...
Iteration k: 1 element

n / 2^k = 1
2^k = n
k = log₂(n)
```

**Comparison:**
| Array Size | Linear (O(n)) | Binary (O(log n)) |
|------------|---------------|-------------------|
| 10         | 10            | 4                 |
| 100        | 100           | 7                 |
| 1,000      | 1,000         | 10                |
| 1,000,000  | 1,000,000     | 20                |
| 1,000,000,000 | 1,000,000,000 | 30             |

### Space Complexity

**Iterative: O(1)**
```python
# Only uses constant extra variables
left, right, mid = 0, len(nums) - 1, 0
```

**Recursive: O(log n)**
```python
# Call stack depth = number of recursive calls
# Maximum depth = log n
```

---

## Pattern Recognition

### When to Use Binary Search

**Characteristics of Binary Search Problems:**

1. **Sorted array/data structure**
   ```
   [1, 3, 5, 7, 9, 11] ✓
   [3, 1, 5, 9, 7, 11] ✗
   ```

2. **Monotonic function** (searching for transition point)
   ```
   [F, F, F, T, T, T] - find first True
   ```

3. **Search space can be reduced by half**

4. **Need O(log n) time**

### Binary Search Variants

**1. Exact Match** (This problem)
```python
if nums[mid] == target:
    return mid
```

**2. First/Last Occurrence**
```python
# Find first occurrence
if nums[mid] == target:
    right = mid - 1  # Keep searching left
else:
    # normal binary search
```

**3. Closest Element**
```python
# Track best match
best = nums[mid]
if abs(nums[mid] - target) < abs(best - target):
    best = nums[mid]
```

**4. Insertion Position**
```python
# Return left at end
# left is insertion position
return left
```

**5. Search in Rotated Array**
```python
# First determine which half is sorted
if nums[left] <= nums[mid]:
    # Left half sorted
else:
    # Right half sorted
```

---

## Related Problems

### Direct Applications

1. **LeetCode 35: Search Insert Position**
   - Find position to insert target
   - Return `left` at end instead of -1

2. **LeetCode 374: Guess Number Higher or Lower**
   - API-based binary search
   - Same template, different comparison

3. **LeetCode 278: First Bad Version**
   - Find first occurrence
   - Modified boundary update

### Modified Binary Search

4. **LeetCode 33: Search in Rotated Sorted Array**
   - Determine sorted half first
   - Apply binary search on correct half

5. **LeetCode 153: Find Minimum in Rotated Sorted Array**
   - Compare mid with right endpoint
   - No explicit target

6. **LeetCode 162: Find Peak Element**
   - Compare mid with neighbors
   - Guarantee peak in one half

### Advanced Applications

7. **LeetCode 4: Median of Two Sorted Arrays**
   - Binary search on partition point
   - Hard problem

8. **LeetCode 410: Split Array Largest Sum**
   - Binary search on answer
   - Check if split possible with max sum

---

## Implementation Variations

### 1. Finding Any Occurrence

```python
def search_any(nums, target):
    """Find any index where target appears."""
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] == target:
            return mid  # Return immediately
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return -1
```

### 2. Finding First Occurrence

```python
def search_first(nums, target):
    """Find leftmost index where target appears."""
    left, right = 0, len(nums) - 1
    result = -1
    
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] == target:
            result = mid
            right = mid - 1  # Continue searching left
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return result
```

### 3. Finding Last Occurrence

```python
def search_last(nums, target):
    """Find rightmost index where target appears."""
    left, right = 0, len(nums) - 1
    result = -1
    
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] == target:
            result = mid
            left = mid + 1  # Continue searching right
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return result
```

### 4. Count Occurrences

```python
def count_occurrences(nums, target):
    """Count how many times target appears."""
    first = search_first(nums, target)
    if first == -1:
        return 0
    
    last = search_last(nums, target)
    return last - first + 1
```

---

## Testing & Validation

### Test Cases

```python
def test_binary_search():
    solution = Solution()
    
    # Test 1: Target in middle
    assert solution.search([-1,0,3,5,9,12], 9) == 4
    
    # Test 2: Target not present
    assert solution.search([-1,0,3,5,9,12], 2) == -1
    
    # Test 3: Single element - found
    assert solution.search([5], 5) == 0
    
    # Test 4: Single element - not found
    assert solution.search([5], 3) == -1
    
    # Test 5: Two elements - first
    assert solution.search([1, 3], 1) == 0
    
    # Test 6: Two elements - second
    assert solution.search([1, 3], 3) == 1
    
    # Test 7: Two elements - not found (left)
    assert solution.search([1, 3], 0) == -1
    
    # Test 8: Two elements - not found (right)
    assert solution.search([1, 3], 5) == -1
    
    # Test 9: Target at start
    assert solution.search([1,2,3,4,5], 1) == 0
    
    # Test 10: Target at end
    assert solution.search([1,2,3,4,5], 5) == 4
    
    # Test 11: Large array
    nums = list(range(0, 1000000, 2))
    assert solution.search(nums, 500000) == 250000
    
    # Test 12: Negative numbers
    assert solution.search([-10,-5,-2,0,3,7], -5) == 1
    
    print("All tests passed!")
```

### Edge Cases Checklist

- [ ] Empty array (if possible)
- [ ] Single element (target found)
- [ ] Single element (target not found)
- [ ] Two elements
- [ ] Target at boundaries (first/last)
- [ ] Target in middle
- [ ] Target not in array
- [ ] All negative numbers
- [ ] All same numbers
- [ ] Large array size

---

## Optimization Notes

### 1. Cache Locality

Binary search has **poor cache performance** compared to linear search for small arrays:

```python
# For n < 100, linear search might be faster
def hybrid_search(nums, target):
    if len(nums) < 100:
        return linear_search(nums, target)
    return binary_search(nums, target)
```

### 2. Branch Prediction

Modern CPUs predict branches. Too many mispredictions slow down:

```python
# Minimize branches in hot path
def optimized_search(nums, target):
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    # Check after loop
    if left < len(nums) and nums[left] == target:
        return left
    return -1
```

### 3. Interpolation Search

For **uniformly distributed** data, interpolation search can be O(log log n):

```python
def interpolation_search(nums, target):
    left, right = 0, len(nums) - 1
    
    while left <= right and nums[left] <= target <= nums[right]:
        if nums[right] == nums[left]:
            return left if nums[left] == target else -1
        
        # Interpolate position
        pos = left + ((target - nums[left]) * (right - left) // 
                      (nums[right] - nums[left]))
        
        if nums[pos] == target:
            return pos
        elif nums[pos] < target:
            left = pos + 1
        else:
            right = pos - 1
    
    return -1
```

---

## Interview Discussion Points

### Questions to Ask

1. **"Is the array guaranteed to be sorted?"**
   - If not, cannot use binary search
   - Might need to sort first (O(n log n))

2. **"Can there be duplicates?"**
   - Changes first/last occurrence logic
   - May need to handle ties

3. **"What should I return if target not found?"**
   - -1, None, False, insertion position?

4. **"Are there any memory constraints?"**
   - Iterative vs recursive
   - O(1) vs O(log n) space

### Explaining Your Solution

**Step-by-step explanation:**

1. "Since the array is sorted, I'll use binary search for O(log n) time"

2. "I'll maintain two pointers, left and right, representing the search space"

3. "At each step, I calculate the middle index and compare with target"

4. "If the middle element equals target, I return its index"

5. "If middle is less than target, target must be in right half, so I move left pointer"

6. "If middle is greater, target is in left half, so I move right pointer"

7. "Continue until left exceeds right, meaning target not found"

### Complexity Justification

"The time complexity is O(log n) because we eliminate half the search space in each iteration. Starting with n elements, we have n, n/2, n/4, ..., 1, which is log₂(n) steps."

"Space complexity is O(1) for iterative, as we only use a fixed number of variables regardless of input size."

---

## Common Mistakes to Avoid

### Mistake 1: Wrong Loop Condition

```python
# ❌ Wrong: Misses single element case
while left < right:
    ...

# ✅ Correct
while left <= right:
    ...
```

### Mistake 2: Incorrect Mid Calculation

```python
# ❌ Risky: Can overflow in some languages
mid = (left + right) // 2

# ✅ Safe
mid = left + (right - left) // 2
```

### Mistake 3: Not Moving Pointers

```python
# ❌ Wrong: Can cause infinite loop
if nums[mid] < target:
    left = mid  # Should be mid + 1
else:
    right = mid  # Should be mid - 1
```

### Mistake 4: Forgetting to Return

```python
# ❌ Wrong: Falls through
def search(nums, target):
    while left <= right:
        if nums[mid] == target:
            return mid
        ...
    # Missing return -1

# ✅ Correct
def search(nums, target):
    while left <= right:
        if nums[mid] == target:
            return mid
        ...
    return -1
```

---

## Advanced Concepts

### Binary Search on Answer

Sometimes we binary search on the **answer range** rather than array indices:

```python
def find_answer():
    """Template for binary search on answer."""
    left, right = min_answer, max_answer
    
    while left <= right:
        mid = left + (right - left) // 2
        if is_valid(mid):
            # mid works, try smaller
            right = mid - 1
        else:
            # mid doesn't work, need larger
            left = mid + 1
    
    return left
```

**Example: LeetCode 875 - Koko Eating Bananas**

### Binary Search with Custom Comparator

```python
def binary_search_custom(arr, target, compare):
    """
    compare(arr[mid], target) returns:
        -1 if arr[mid] < target
         0 if arr[mid] == target
         1 if arr[mid] > target
    """
    left, right = 0, len(arr) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        cmp = compare(arr[mid], target)
        
        if cmp == 0:
            return mid
        elif cmp < 0:
            left = mid + 1
        else:
            right = mid - 1
    
    return -1
```

### Bitwise Optimization

```python
def binary_search_bitwise(nums, target):
    """Using bitwise operations for performance."""
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = (left + right) >> 1  # Right shift by 1 = divide by 2
        
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return -1
```

---

## Practice Problems

### Beginner
1. LeetCode 35: Search Insert Position
2. LeetCode 374: Guess Number Higher or Lower
3. LeetCode 367: Valid Perfect Square

### Intermediate
4. LeetCode 278: First Bad Version
5. LeetCode 34: Find First and Last Position of Element
6. LeetCode 69: Sqrt(x)

### Advanced
7. LeetCode 33: Search in Rotated Sorted Array
8. LeetCode 4: Median of Two Sorted Arrays
9. LeetCode 410: Split Array Largest Sum

---

## Summary

### Key Takeaways

1. **Binary search requires sorted input** and achieves O(log n) time
2. **Standard template**: `left <= right`, `mid = left + (right - left) // 2`
3. **Always move pointers**: `left = mid + 1`, `right = mid - 1`
4. **Three common templates** for different scenarios
5. **Watch for off-by-one errors** and infinite loops

### Template Reference

```python
def binary_search(nums, target):
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        
        if nums[mid] == target:
            return mid
        elif nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return -1
```

### Time & Space Complexity
- **Time**: O(log n)
- **Space**: O(1) iterative, O(log n) recursive

---

## Additional Resources

- **Book**: "Introduction to Algorithms" (CLRS) - Chapter 2
- **Video**: MIT OpenCourseWare - Binary Search
- **Practice**: LeetCode Binary Search Tag
- **Article**: "Binary Search Explained" on GeeksforGeeks

---

**Tags**: #binary-search #searching #logarithmic #divide-conquer #template  
**Difficulty**: Easy  
**Related**: Search Insert Position, First Bad Version, Sqrt(x)
