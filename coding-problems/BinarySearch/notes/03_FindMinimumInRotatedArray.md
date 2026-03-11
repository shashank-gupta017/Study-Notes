# Find Minimum in Rotated Sorted Array - LeetCode #153

**Difficulty**: Medium  
**Pattern**: Binary Search for Minimum  
**Frequency**: High (Core Concept)

---

## Problem Statement

Suppose an array of length `n` sorted in ascending order is **rotated** between `1` and `n` times. For example, the array `nums = [0,1,2,4,5,6,7]` might become:

- `[4,5,6,7,0,1,2]` if it was rotated `4` times.
- `[0,1,2,4,5,6,7]` if it was rotated `7` times.

Notice that **rotating** an array `[a[0], a[1], a[2], ..., a[n-1]]` 1 time results in the array `[a[n-1], a[0], a[1], a[2], ..., a[n-2]]`.

Given the sorted rotated array `nums` of **unique** elements, return the minimum element of this array.

You must write an algorithm that runs in `O(log n)` time.

**Example 1:**
```
Input: nums = [3,4,5,1,2]
Output: 1
Explanation: The original array was [1,2,3,4,5] rotated 3 times.
```

**Example 2:**
```
Input: nums = [4,5,6,7,0,1,2]
Output: 0
Explanation: The original array was [0,1,2,4,5,6,7] and it was rotated 4 times.
```

**Example 3:**
```
Input: nums = [11,13,15,17]
Output: 11
Explanation: The original array was [11,13,15,17] and it was rotated 4 times. 
```

**Constraints:**
- `n == nums.length`
- `1 <= n <= 5000`
- `-5000 <= nums[i] <= 5000`
- All the integers of `nums` are **unique**
- `nums` is sorted and rotated between `1` and `n` times

---

## Core Concepts

### Understanding the Problem

**Key Observations:**

1. **Original array is sorted**: `[0,1,2,3,4,5,6]`
2. **After rotation**: Two sorted subarrays appear
   ```
   [4,5,6,0,1,2,3]
    └─►─┘ └──►──┘
    larger  smaller
   ```
3. **Minimum element**: The "pivot" where rotation occurred
4. **Property**: `nums[i-1] > nums[i]` at minimum (the "drop")

### Visual Representation

**No Rotation:**
```
[1, 2, 3, 4, 5, 6, 7]
 ↑
min (already sorted)
```

**Rotated Once:**
```
[7, 1, 2, 3, 4, 5, 6]
 ↓  ↑
drop min
```

**Rotated Multiple Times:**
```
[4, 5, 6, 7, 1, 2, 3]
          ↓  ↑
        drop min
```

**Graph View:**
```
     7
    6     
   5    
  4       
 3         3
2          2
1           1
  Before    After rotation
```

### Key Insight

**Compare mid with right endpoint:**

```
If nums[mid] > nums[right]:
    Minimum is in RIGHT half
    [4, 5, 6, 7, 1, 2]
            M        R
    7 > 2 → min is right →

If nums[mid] <= nums[right]:
    Minimum is in LEFT half (including mid)
    [4, 5, 1, 2, 3]
         M     R
    1 < 3 → min is left or mid
```

**Why compare with right, not left?**

If we compare with left:
```
[3, 4, 5, 1, 2]
 L     M     R

nums[mid]=5 > nums[left]=3
But we can't determine which half has minimum!

Left half [3,4,5] increasing
Right half [5,1,2] has drop
```

With right comparison, we know:
- `mid > right` → drop is to the right
- `mid <= right` → this half is sorted, min is left

---

## Solution Approach

### Algorithm Steps

1. Initialize `left = 0`, `right = n - 1`
2. While `left < right` (not `<=`):
   - Calculate `mid`
   - If `nums[mid] > nums[right]`:
     - Minimum in right half
     - `left = mid + 1`
   - Else:
     - Minimum in left half or is mid
     - `right = mid` (keep mid)
3. Return `nums[left]`

**Why `left < right` not `left <= right`?**
- We're not searching for target, we're narrowing to ONE element
- When `left == right`, we found the minimum
- No need to check further

### Visual Walkthrough

**Example:** `nums = [4,5,6,7,0,1,2]`

```
Step 1: Initial
[4, 5, 6, 7, 0, 1, 2]
 L        M        R
left=0, right=6, mid=3
nums[mid]=7 > nums[right]=2 → min in right half
left = mid + 1 = 4

Step 2:
[4, 5, 6, 7, 0, 1, 2]
             L  M  R
left=4, right=6, mid=5
nums[mid]=1 <= nums[right]=2 → min in left half (including mid)
right = mid = 5

Step 3:
[4, 5, 6, 7, 0, 1, 2]
             L/M
             R
left=4, right=5, mid=4
nums[mid]=0 <= nums[right]=1 → min in left half
right = mid = 4

Step 4:
[4, 5, 6, 7, 0, 1, 2]
             L/R
left=4, right=4
left == right → FOUND!
Return nums[4] = 0
```

**Example:** `nums = [3,4,5,1,2]`

```
Step 1:
[3, 4, 5, 1, 2]
 L     M     R
left=0, right=4, mid=2
nums[2]=5 > nums[4]=2 → min in right
left = 3

Step 2:
[3, 4, 5, 1, 2]
          L/M R
left=3, right=4, mid=3
nums[3]=1 <= nums[4]=2 → min in left
right = 3

Step 3:
left=3, right=3 → Return nums[3] = 1
```

---

## Complete Solution

### Python Implementation

```python
from typing import List

class Solution:
    def findMin(self, nums: List[int]) -> int:
        """
        Find minimum in rotated sorted array using binary search.
        
        Key insight: Compare mid with right endpoint.
        - If mid > right: minimum is in right half
        - If mid <= right: minimum is in left half (including mid)
        
        Time: O(log n)
        Space: O(1)
        """
        left, right = 0, len(nums) - 1
        
        # Use < not <=, we're narrowing to single element
        while left < right:
            mid = left + (right - left) // 2
            
            # Mid element > right element
            # Rotation point (minimum) is in right half
            if nums[mid] > nums[right]:
                left = mid + 1
            else:
                # Mid element <= right element
                # Minimum is in left half or is mid itself
                right = mid
        
        # When left == right, we found minimum
        return nums[left]
```

### Alternative: Early Exit Optimization

```python
class Solution:
    def findMin(self, nums: List[int]) -> int:
        """
        Version with early exit for sorted arrays.
        """
        left, right = 0, len(nums) - 1
        
        while left < right:
            # Early exit: if array segment is sorted
            if nums[left] < nums[right]:
                return nums[left]
            
            mid = left + (right - left) // 2
            
            if nums[mid] > nums[right]:
                left = mid + 1
            else:
                right = mid
        
        return nums[left]
```

**Optimization benefit:**
- For sorted or nearly sorted arrays, returns immediately
- Still O(log n) worst case

### Alternative: Find Pivot Explicitly

```python
class Solution:
    def findMin(self, nums: List[int]) -> int:
        """
        Find the rotation pivot point.
        """
        n = len(nums)
        left, right = 0, n - 1
        
        while left < right:
            mid = left + (right - left) // 2
            
            # Check if mid+1 is the minimum
            if mid < n - 1 and nums[mid] > nums[mid + 1]:
                return nums[mid + 1]
            
            # Check if mid is the minimum
            if mid > 0 and nums[mid] < nums[mid - 1]:
                return nums[mid]
            
            # Decide which half to search
            if nums[mid] > nums[right]:
                left = mid + 1
            else:
                right = mid
        
        return nums[left]
```

---

## Why This Works: Detailed Proof

### Invariant Maintained

**Loop Invariant:** "The minimum element is in range `[left, right]`"

**Proof by Cases:**

**Case 1: nums[mid] > nums[right]**
```
[4, 5, 6, 7, 0, 1, 2]
 L        M        R
```
- Array is rotated
- Mid (7) > Right (2)
- Drop must be between mid and right
- Minimum CANNOT be in [left, mid]
- Set `left = mid + 1` maintains invariant

**Case 2: nums[mid] <= nums[right]**
```
[4, 5, 6, 0, 1, 2, 3]
             M     R
```
- Range [mid, right] is sorted
- Minimum is either:
  - At mid itself, OR
  - In [left, mid)
- Set `right = mid` maintains invariant

**Termination:**
- Each iteration reduces search space
- Eventually `left == right`
- Invariant guarantees this is minimum

### Why Not Compare with Left?

```python
# ❌ If we compare with left:
if nums[mid] > nums[left]:
    # Can't determine which half has min!
    
Example: [3, 4, 5, 1, 2]
          L     M     R
nums[mid]=5 > nums[left]=3
Is min in left or right? Can't tell!
```

### Why `right = mid` Not `right = mid - 1`?

```python
# Mid could be the minimum!
[4, 5, 1, 2, 3]
       M

nums[mid]=1 <= nums[right]=3
If we do right = mid - 1, we'd skip the minimum!

# Correct: right = mid keeps mid in search space
```

---

## Complexity Analysis

### Time Complexity: O(log n)

**Proof:**
```
Iteration 1: n elements
Iteration 2: n/2 elements
Iteration 3: n/4 elements
...
Iteration k: 1 element

n / 2^k = 1
k = log₂(n)
```

**Comparison with Linear Search:**
| Array Size | Linear | Binary |
|------------|--------|--------|
| 10         | 10     | 4      |
| 100        | 100    | 7      |
| 1000       | 1000   | 10     |
| 1000000    | 1000000| 20     |

### Space Complexity: O(1)

**Variables used:**
- `left`: O(1)
- `right`: O(1)
- `mid`: O(1)
- Total: O(1)

**No recursion**, no extra data structures

---

## Edge Cases & Special Scenarios

### Edge Case 1: No Rotation (Sorted Array)

```python
nums = [1, 2, 3, 4, 5]
 L              M     R

nums[mid]=3 <= nums[right]=5
right = mid

Continue until left=0, right=0
Return nums[0] = 1 ✓
```

**Optimization:** Early exit when `nums[left] < nums[right]`

### Edge Case 2: Single Element

```python
nums = [1]
left=0, right=0
left == right → return nums[0] = 1 ✓
```

### Edge Case 3: Two Elements - No Rotation

```python
nums = [1, 2]
 L     R
 M

mid=0
nums[0]=1 <= nums[1]=2
right = 0

left=0, right=0 → return 1 ✓
```

### Edge Case 4: Two Elements - Rotated

```python
nums = [2, 1]
 L     R
 M

mid=0
nums[0]=2 > nums[1]=1
left = 1

left=1, right=1 → return 1 ✓
```

### Edge Case 5: Rotated to Last Position

```python
nums = [2, 3, 4, 5, 1]
 L           M     R

nums[4]=5 > nums[4]=1
left = 3

nums = [2, 3, 4, 5, 1]
             L  M  R
nums[4]=5 > nums[4]=1
left = 4

left=4, right=4 → return 1 ✓
```

### Edge Case 6: All Negative Numbers

```python
nums = [-5, -3, -1, -7, -6]
Result: -7 (same algorithm)
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Using `<=` Instead of `<`

```python
# ❌ Wrong
while left <= right:
    # Need additional checks to avoid infinite loop
    
# ✅ Correct
while left < right:
    # Terminates when left == right
```

### Mistake 2: Comparing with Left

```python
# ❌ Wrong
if nums[mid] > nums[left]:
    left = mid + 1
# Can't determine which side has minimum

# ✅ Correct
if nums[mid] > nums[right]:
    left = mid + 1
```

### Mistake 3: `right = mid - 1` Instead of `right = mid`

```python
# ❌ Wrong
else:
    right = mid - 1  # Might skip the minimum!

# ✅ Correct
else:
    right = mid  # Keep mid in search space
```

### Mistake 4: Returning `nums[right]` Instead of `nums[left]`

```python
# Both work when left == right
# But left is more intuitive as we move left toward minimum
return nums[left]  # Preferred
```

### Mistake 5: Not Handling Sorted Arrays

```python
# Not necessarily wrong, but inefficient
# Can add early exit:
if nums[left] < nums[right]:
    return nums[left]
```

---

## Pattern Variations

### Variation 1: Find Rotation Count

```python
def findRotationCount(nums):
    """
    Number of rotations = index of minimum element.
    """
    left, right = 0, len(nums) - 1
    
    while left < right:
        if nums[left] < nums[right]:
            return left  # Not rotated
        
        mid = left + (right - left) // 2
        
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            right = mid
    
    return left
```

**Example:**
```
[4,5,6,7,0,1,2]
Minimum at index 4 → rotated 4 times ✓
```

### Variation 2: With Duplicates (LeetCode 154)

```python
def findMinWithDuplicates(nums):
    """
    When duplicates exist, worst case O(n).
    """
    left, right = 0, len(nums) - 1
    
    while left < right:
        mid = left + (right - left) // 2
        
        if nums[mid] > nums[right]:
            left = mid + 1
        elif nums[mid] < nums[right]:
            right = mid
        else:
            # nums[mid] == nums[right]
            # Can't determine which side, reduce right
            right -= 1
    
    return nums[left]
```

**Example with duplicates:**
```
[2, 2, 2, 0, 1]
 L     M     R
nums[mid]=2 == nums[right]=1? No, continue

[1, 1, 1, 1, 1]
 L     M     R
nums[mid]=1 == nums[right]=1
Can't tell which side → right -= 1
Worst case: O(n)
```

### Variation 3: Find Maximum in Rotated Array

```python
def findMax(nums):
    """
    Maximum is just before minimum (wrapping around).
    """
    n = len(nums)
    min_idx = findMinIndex(nums)
    
    if min_idx == 0:
        return nums[n - 1]
    else:
        return nums[min_idx - 1]

def findMinIndex(nums):
    left, right = 0, len(nums) - 1
    
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            right = mid
    
    return left
```

---

## Related Problems

### Direct Applications

1. **LeetCode 154: Find Minimum in Rotated Sorted Array II**
   - With duplicates
   - Worst case O(n)

2. **LeetCode 33: Search in Rotated Sorted Array**
   - Use similar logic to search target
   - Need to identify sorted half

3. **LeetCode 81: Search in Rotated Sorted Array II**
   - Search with duplicates
   - Harder version

### Similar Patterns

4. **LeetCode 162: Find Peak Element**
   - Similar binary search approach
   - Compare with neighbors

5. **LeetCode 852: Peak Index in a Mountain Array**
   - Find peak in mountain
   - Similar comparison logic

6. **LeetCode 1095: Find in Mountain Array**
   - Binary search variations
   - Multiple phases

---

## Advanced Techniques

### Technique 1: Recursive Implementation

```python
def findMin(nums):
    """Recursive version."""
    return helper(nums, 0, len(nums) - 1)

def helper(nums, left, right):
    # Base case: single element
    if left == right:
        return nums[left]
    
    # Already sorted
    if nums[left] < nums[right]:
        return nums[left]
    
    mid = left + (right - left) // 2
    
    if nums[mid] > nums[right]:
        return helper(nums, mid + 1, right)
    else:
        return helper(nums, left, mid)
```

**Complexity:**
- Time: O(log n)
- Space: O(log n) - call stack

### Technique 2: Finding Both Min and Max

```python
def findMinAndMax(nums):
    """
    Find both minimum and maximum efficiently.
    """
    n = len(nums)
    min_idx = findMinIndex(nums)
    
    min_val = nums[min_idx]
    max_val = nums[(min_idx - 1) % n]
    
    return min_val, max_val

def findMinIndex(nums):
    left, right = 0, len(nums) - 1
    
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            right = mid
    
    return left
```

### Technique 3: Check if Array is Rotated

```python
def isRotated(nums):
    """
    Check if array is rotated or just sorted.
    """
    return nums[0] > nums[-1]

def findMinOptimized(nums):
    """
    Quick check before binary search.
    """
    # Not rotated
    if nums[0] <= nums[-1]:
        return nums[0]
    
    # Rotated, use binary search
    left, right = 0, len(nums) - 1
    
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            right = mid
    
    return nums[left]
```

---

## Testing & Validation

### Comprehensive Test Suite

```python
def test_find_min():
    solution = Solution()
    
    # Test 1: Standard rotation
    assert solution.findMin([3,4,5,1,2]) == 1
    
    # Test 2: Another rotation
    assert solution.findMin([4,5,6,7,0,1,2]) == 0
    
    # Test 3: No rotation
    assert solution.findMin([11,13,15,17]) == 11
    
    # Test 4: Single element
    assert solution.findMin([1]) == 1
    
    # Test 5: Two elements, not rotated
    assert solution.findMin([1,2]) == 1
    
    # Test 6: Two elements, rotated
    assert solution.findMin([2,1]) == 1
    
    # Test 7: Rotated by 1
    assert solution.findMin([2,3,4,5,1]) == 1
    
    # Test 8: Rotated to last
    assert solution.findMin([2,1]) == 1
    
    # Test 9: All negative
    assert solution.findMin([-5,-3,-1,-7,-6]) == -7
    
    # Test 10: Large array
    assert solution.findMin([10,11,12,13,1,2,3,4,5,6,7,8,9]) == 1
    
    # Test 11: Min at start (no rotation effectively)
    assert solution.findMin([0,1,2,3,4,5]) == 0
    
    # Test 12: Three elements
    assert solution.findMin([3,1,2]) == 1
    
    print("All tests passed!")

test_find_min()
```

### Edge Cases Checklist

- [x] Single element
- [x] Two elements (rotated and not)
- [x] No rotation
- [x] Rotated by 1
- [x] Rotated to maximum
- [x] All negative
- [x] Large array
- [x] Minimum at start
- [x] Minimum at end

---

## Interview Tips

### Questions to Ask

1. **"Can the array have duplicates?"**
   - Changes algorithm (LeetCode 154)
   - Worst case becomes O(n)

2. **"What's the size range?"**
   - Very small might not need binary search

3. **"Can the array be empty?"**
   - Problem states n >= 1, but good to check

4. **"Should I return value or index?"**
   - Usually value, but clarify

### Explaining Your Approach

**Clear explanation:**

1. "After rotation, array has two sorted parts"

2. "Minimum is at the rotation point"

3. "I compare mid with right endpoint to determine which half has minimum"

4. "If mid > right, minimum is in right half"

5. "Otherwise, minimum is in left half or is mid itself"

6. "Continue until left equals right"

### Follow-up Questions

**Q: What if there are duplicates?**
A: Need to handle `nums[mid] == nums[right]` by decrementing right. Worst case O(n).

**Q: Can you find the rotation count?**
A: Yes, rotation count equals index of minimum element.

**Q: Can you do better than O(log n)?**
A: No, need to examine at least log n elements to find minimum in rotated array.

**Q: What if array is not rotated?**
A: Can check `nums[0] < nums[-1]` early and return `nums[0]`.

---

## Summary

### Key Takeaways

1. **Compare mid with right**, not left
2. **Use `left < right`**, not `left <= right`
3. **Set `right = mid`**, not `right = mid - 1`
4. **Minimum is the rotation pivot point**
5. **Time O(log n), Space O(1)**

### Algorithm Template

```python
def findMin(nums):
    left, right = 0, len(nums) - 1
    
    while left < right:
        mid = left + (right - left) // 2
        
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            right = mid
    
    return nums[left]
```

### Complexity
- **Time**: O(log n)
- **Space**: O(1)

---

**Tags**: #binary-search #rotated-array #find-minimum #medium  
**Related**: Search in Rotated Array, Find Minimum II  
**Companies**: Amazon, Microsoft, Facebook, Bloomberg
