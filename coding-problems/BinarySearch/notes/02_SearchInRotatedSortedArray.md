# Search in Rotated Sorted Array - LeetCode #33

**Difficulty**: Medium  
**Pattern**: Modified Binary Search  
**Frequency**: Very High (FAANG Favorite)

---

## Problem Statement

There is an integer array `nums` sorted in ascending order (with **distinct** values).

Prior to being passed to your function, `nums` is **possibly rotated** at an unknown pivot index `k` (`0 <= k < nums.length`) such that the resulting array is `[nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]` (**0-indexed**). For example, `[0,1,2,4,5,6,7]` might be rotated at pivot index `3` and become `[4,5,6,7,0,1,2]`.

Given the array `nums` **after** the possible rotation and an integer `target`, return the index of `target` if it is in `nums`, or `-1` if it is not in `nums`.

You must write an algorithm with `O(log n)` runtime complexity.

**Example 1:**
```
Input: nums = [4,5,6,7,0,1,2], target = 0
Output: 4
```

**Example 2:**
```
Input: nums = [4,5,6,7,0,1,2], target = 3
Output: -1
```

**Example 3:**
```
Input: nums = [1], target = 0
Output: -1
```

**Constraints:**
- `1 <= nums.length <= 5000`
- `-10^4 <= nums[i] <= 10^4`
- All values of `nums` are **unique**
- `nums` is an ascending array that is possibly rotated
- `-10^4 <= target <= 10^4`

---

## Core Concepts

### Understanding Array Rotation

**Original Sorted Array:**
```
[0, 1, 2, 4, 5, 6, 7]
```

**Rotated at Index 3:**
```
[4, 5, 6, 7, 0, 1, 2]
 └─right half─┘ └left half┘
```

**Key Observations:**
1. Array is split into **two sorted subarrays**
2. All elements in left subarray > all elements in right subarray
3. At any midpoint, **at least one half is properly sorted**
4. Can determine which half is sorted by comparing endpoints

### Visualization of Rotation

```
No rotation (k=0):
[0, 1, 2, 4, 5, 6, 7]
 ↑                 ↑
left             right

Rotation at k=1:
[1, 2, 4, 5, 6, 7, 0]
 ↑                 ↑
 All increasing except last drop

Rotation at k=3:
[4, 5, 6, 7, 0, 1, 2]
 ↑         ↑       ↑
sorted    pivot  sorted

Rotation at k=6:
[7, 0, 1, 2, 4, 5, 6]
 ↑ ↑                 ↑
 Big drop then sorted
```

### Modified Binary Search Strategy

**Standard Binary Search:**
- Check mid, go left or right based on comparison

**Rotated Array Binary Search:**
1. Calculate mid
2. **Determine which half is sorted** (left or right)
3. Check if target is in the sorted half
4. If yes, search sorted half; otherwise search other half

---

## Solution Approach

### Step-by-Step Strategy

**Key Insight:** At any midpoint, at least one of the two halves `[left, mid]` or `[mid, right]` must be properly sorted.

**Algorithm:**
1. Find mid element
2. Determine which half is sorted:
   - If `nums[left] <= nums[mid]`: left half is sorted
   - Else: right half is sorted
3. Check if target is in sorted half:
   - Use range check: `nums[left] <= target < nums[mid]`
4. Update pointers accordingly

### Visual Walkthrough

**Example:** `nums = [4,5,6,7,0,1,2]`, `target = 0`

```
Step 1: Initial
[4, 5, 6, 7, 0, 1, 2]
 L        M        R
left=0, right=6, mid=3
nums[left]=4 <= nums[mid]=7 → left half [4,5,6,7] is sorted
Is target 0 in [4, 7)? NO
Search right half → left = mid + 1 = 4

Step 2:
[4, 5, 6, 7, 0, 1, 2]
             L  M  R
left=4, right=6, mid=5
nums[left]=0 <= nums[mid]=1 → left half [0,1] is sorted
Is target 0 in [0, 1)? YES (0 >= 0 and 0 < 1)
Search left half → right = mid - 1 = 4

Step 3:
[4, 5, 6, 7, 0, 1, 2]
             L/M
             R
left=4, right=4, mid=4
nums[4] = 0 = target ✓ FOUND!
Return 4
```

**Example:** `nums = [4,5,6,7,0,1,2]`, `target = 3`

```
Step 1:
[4, 5, 6, 7, 0, 1, 2]
 L        M        R
Left half [4,5,6,7] sorted
3 not in [4, 7)
Search right → left = 4

Step 2:
[4, 5, 6, 7, 0, 1, 2]
             L  M  R
Left half [0,1] sorted
3 not in [0, 1)
Search right → left = 6

Step 3:
[4, 5, 6, 7, 0, 1, 2]
                   L/M/R
nums[6] = 2 ≠ 3
Search right → left = 7

left > right → NOT FOUND, return -1
```

---

## Complete Solution

### Python Implementation

```python
from typing import List

class Solution:
    def search(self, nums: List[int], target: int) -> int:
        """
        Binary search in rotated sorted array.
        
        Key: Identify which half is sorted, then check if target
        is in that sorted half.
        
        Time: O(log n)
        Space: O(1)
        """
        left, right = 0, len(nums) - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            # Found target
            if nums[mid] == target:
                return mid
            
            # Determine which half is sorted
            if nums[left] <= nums[mid]:
                # Left half is sorted [left, mid]
                if nums[left] <= target < nums[mid]:
                    # Target is in sorted left half
                    right = mid - 1
                else:
                    # Target is in right half
                    left = mid + 1
            else:
                # Right half is sorted [mid, right]
                if nums[mid] < target <= nums[right]:
                    # Target is in sorted right half
                    left = mid + 1
                else:
                    # Target is in left half
                    right = mid - 1
        
        return -1
```

### Alternative: More Explicit Version

```python
class Solution:
    def search(self, nums: List[int], target: int) -> int:
        """More explicit version with comments."""
        left, right = 0, len(nums) - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid
            
            # Check if left half is sorted
            left_sorted = nums[left] <= nums[mid]
            
            if left_sorted:
                # Left half [left, mid] is sorted
                target_in_left = nums[left] <= target < nums[mid]
                if target_in_left:
                    right = mid - 1  # Search left
                else:
                    left = mid + 1   # Search right
            else:
                # Right half [mid, right] is sorted
                target_in_right = nums[mid] < target <= nums[right]
                if target_in_right:
                    left = mid + 1   # Search right
                else:
                    right = mid - 1  # Search left
        
        return -1
```

### Clean Version

```python
class Solution:
    def search(self, nums: List[int], target: int) -> int:
        """Cleanest implementation."""
        left, right = 0, len(nums) - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid
            
            # Left half sorted
            if nums[left] <= nums[mid]:
                if nums[left] <= target < nums[mid]:
                    right = mid - 1
                else:
                    left = mid + 1
            # Right half sorted
            else:
                if nums[mid] < target <= nums[right]:
                    left = mid + 1
                else:
                    right = mid - 1
        
        return -1
```

---

## Detailed Analysis

### Why At Least One Half is Sorted?

**Proof by Cases:**

**Case 1: No Rotation**
```
[1, 2, 3, 4, 5]
 L     M     R
nums[L] <= nums[M]: Left sorted ✓
nums[M] <= nums[R]: Right sorted ✓
Both halves sorted!
```

**Case 2: Rotation in Right Half**
```
[4, 5, 6, 1, 2, 3]
 L     M        R
nums[L]=4 <= nums[M]=6: Left sorted ✓
nums[M]=6 > nums[R]=3: Right NOT sorted
```

**Case 3: Rotation in Left Half**
```
[5, 6, 1, 2, 3, 4]
 L     M        R
nums[L]=5 > nums[M]=2: Left NOT sorted
nums[M]=2 <= nums[R]=4: Right sorted ✓
```

**Conclusion:** The rotation point (pivot) divides array into two sorted parts. At any midpoint, the half WITHOUT the pivot is sorted.

### Range Check Logic

**For sorted left half** `[left, mid]`:
```python
if nums[left] <= target < nums[mid]:
    # Target in range [nums[left], nums[mid])
```

**Important:** Use `<` for mid, not `<=`, because we already checked `nums[mid] != target`

**For sorted right half** `[mid, right]`:
```python
if nums[mid] < target <= nums[right]:
    # Target in range (nums[mid], nums[right]]
```

**Why different?**
- Left check: `target < nums[mid]` (exclusive upper)
- Right check: `target <= nums[right]` (inclusive upper)
- Ensures no overlap, covers all cases

---

## Edge Cases & Special Scenarios

### Edge Case 1: Single Element

```python
nums = [1], target = 1
left = 0, right = 0, mid = 0
nums[0] = 1 = target ✓
Return 0
```

### Edge Case 2: Two Elements - No Rotation

```python
nums = [1, 3], target = 3
Step 1: left=0, right=1, mid=0
nums[0]=1 <= nums[0]=1: left sorted
1 <= 3 < 1? NO → search right
left = 1

Step 2: left=1, right=1, mid=1
nums[1] = 3 = target ✓
```

### Edge Case 3: Two Elements - Rotated

```python
nums = [3, 1], target = 1
Step 1: left=0, right=1, mid=0
nums[0]=3 > nums[0]=3? FALSE (nums[left] <= nums[mid])
Left sorted, but 1 not in [3, 3)
Search right → left = 1

Step 2: nums[1] = 1 = target ✓
```

### Edge Case 4: Target at Rotation Point

```python
nums = [4,5,6,7,0,1,2], target = 0
(See detailed walkthrough above)
```

### Edge Case 5: No Rotation

```python
nums = [1,2,3,4,5], target = 3
Standard binary search works!
Left half always sorted in early iterations
```

---

## Complexity Analysis

### Time Complexity: O(log n)

**Best Case:** O(1)
- Target is at mid in first iteration

**Average Case:** O(log n)
- Eliminate half the search space each time
- Same as standard binary search

**Worst Case:** O(log n)
```
n elements → n/2 → n/4 → ... → 1
log₂(n) iterations
```

**Why still O(log n) despite extra comparisons?**
- Extra comparisons are O(1) per iteration
- Number of iterations is still log n
- O(log n × 1) = O(log n)

### Space Complexity: O(1)

**Variables used:**
```python
left: O(1)
right: O(1)
mid: O(1)
Total: O(1)
```

**No recursion** → no call stack
**No extra data structures** → constant space

---

## Common Mistakes & Pitfalls

### Mistake 1: Wrong Sorted Half Check

```python
# ❌ Wrong: Using < instead of <=
if nums[left] < nums[mid]:
    # Fails when left = mid (single element)

# ✅ Correct
if nums[left] <= nums[mid]:
    # Works for all cases
```

**Example where it fails:**
```
nums = [3, 1], target = 1
left=0, right=1, mid=0
nums[0] < nums[0]? FALSE
Would incorrectly treat as right sorted
```

### Mistake 2: Wrong Range Check

```python
# ❌ Wrong: Using <= for both bounds
if nums[left] <= target <= nums[mid]:
    # Includes mid, but we checked mid already

# ✅ Correct
if nums[left] <= target < nums[mid]:
    # Exclude mid (already checked)
```

### Mistake 3: Forgetting Edge Cases

```python
# ❌ Wrong: No check for empty array
def search(nums, target):
    left, right = 0, len(nums) - 1
    # Fails if nums = []

# ✅ Better
def search(nums, target):
    if not nums:
        return -1
    left, right = 0, len(nums) - 1
```

Though problem guarantees `1 <= nums.length`, good to handle!

### Mistake 4: Asymmetric Range Checks

```python
# ❌ Wrong: Inconsistent bounds
# Left: nums[left] <= target < nums[mid]
# Right: nums[mid] <= target < nums[right]
# Miss target = nums[right]!

# ✅ Correct
# Left: nums[left] <= target < nums[mid]
# Right: nums[mid] < target <= nums[right]
```

---

## Pattern Variations

### Variation 1: Find Minimum Element

```python
def findMin(nums):
    """
    LeetCode 153: Find Minimum in Rotated Sorted Array
    """
    left, right = 0, len(nums) - 1
    
    while left < right:
        mid = left + (right - left) // 2
        
        # If mid > right, min is in right half
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            # Min is in left half (including mid)
            right = mid
    
    return nums[left]
```

### Variation 2: With Duplicates

```python
def search_with_duplicates(nums, target):
    """
    LeetCode 81: Search in Rotated Sorted Array II
    Worst case O(n) due to duplicates
    """
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        
        if nums[mid] == target:
            return True
        
        # Handle duplicates: can't determine which half sorted
        if nums[left] == nums[mid] == nums[right]:
            left += 1
            right -= 1
        elif nums[left] <= nums[mid]:
            # Left sorted
            if nums[left] <= target < nums[mid]:
                right = mid - 1
            else:
                left = mid + 1
        else:
            # Right sorted
            if nums[mid] < target <= nums[right]:
                left = mid + 1
            else:
                right = mid - 1
    
    return False
```

### Variation 3: Find Rotation Count

```python
def count_rotations(nums):
    """
    Find how many times array was rotated.
    Rotation count = index of minimum element
    """
    left, right = 0, len(nums) - 1
    
    while left < right:
        # Already sorted
        if nums[left] < nums[right]:
            return left
        
        mid = left + (right - left) // 2
        
        # Check if mid+1 is minimum
        if mid < right and nums[mid] > nums[mid + 1]:
            return mid + 1
        
        # Check if mid is minimum
        if mid > left and nums[mid] < nums[mid - 1]:
            return mid
        
        # Decide which half has minimum
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            right = mid
    
    return left
```

---

## Advanced Techniques

### Technique 1: Finding Pivot Point First

```python
def search_two_phase(nums, target):
    """
    Alternative: Find pivot first, then standard binary search.
    Still O(log n) but two passes.
    """
    # Phase 1: Find pivot (minimum element index)
    pivot = find_pivot(nums)
    
    # Phase 2: Standard binary search on correct half
    if target >= nums[0]:
        # Target in left sorted part
        return binary_search(nums, 0, pivot - 1, target)
    else:
        # Target in right sorted part
        return binary_search(nums, pivot, len(nums) - 1, target)

def find_pivot(nums):
    left, right = 0, len(nums) - 1
    while left < right:
        mid = left + (right - left) // 2
        if nums[mid] > nums[right]:
            left = mid + 1
        else:
            right = mid
    return left

def binary_search(nums, left, right, target):
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

**Comparison:**
- One-pass: Cleaner, slightly faster
- Two-pass: More intuitive, easier to understand

### Technique 2: Recursive Approach

```python
def search_recursive(nums, target):
    """Recursive solution."""
    return helper(nums, target, 0, len(nums) - 1)

def helper(nums, target, left, right):
    if left > right:
        return -1
    
    mid = left + (right - left) // 2
    
    if nums[mid] == target:
        return mid
    
    if nums[left] <= nums[mid]:
        # Left sorted
        if nums[left] <= target < nums[mid]:
            return helper(nums, target, left, mid - 1)
        else:
            return helper(nums, target, mid + 1, right)
    else:
        # Right sorted
        if nums[mid] < target <= nums[right]:
            return helper(nums, target, mid + 1, right)
        else:
            return helper(nums, target, left, mid - 1)
```

**Space:** O(log n) for call stack

---

## Testing & Validation

### Comprehensive Test Suite

```python
def test_search_rotated():
    solution = Solution()
    
    # Test 1: Standard rotation
    assert solution.search([4,5,6,7,0,1,2], 0) == 4
    
    # Test 2: Target not present
    assert solution.search([4,5,6,7,0,1,2], 3) == -1
    
    # Test 3: Single element - found
    assert solution.search([1], 1) == 0
    
    # Test 4: Single element - not found
    assert solution.search([1], 0) == -1
    
    # Test 5: No rotation
    assert solution.search([1,2,3,4,5], 3) == 2
    
    # Test 6: Rotated by 1
    assert solution.search([2,3,4,5,1], 1) == 4
    
    # Test 7: Rotated to last position
    assert solution.search([2,1], 1) == 1
    
    # Test 8: Target at start
    assert solution.search([4,5,6,7,0,1,2], 4) == 0
    
    # Test 9: Target at end
    assert solution.search([4,5,6,7,0,1,2], 2) == 6
    
    # Test 10: Two elements no rotation
    assert solution.search([1,3], 3) == 1
    
    # Test 11: Large rotation
    assert solution.search([7,8,9,10,1,2,3,4,5,6], 3) == 6
    
    # Test 12: Target in left sorted part
    assert solution.search([4,5,6,7,0,1,2], 5) == 1
    
    # Test 13: Target in right sorted part
    assert solution.search([4,5,6,7,0,1,2], 1) == 5
    
    print("All tests passed!")

test_search_rotated()
```

### Edge Cases Checklist

- [x] Single element
- [x] Two elements (rotated and not)
- [x] No rotation (sorted array)
- [x] Rotation by 1
- [x] Rotation to last position
- [x] Target at boundaries
- [x] Target in left sorted part
- [x] Target in right sorted part
- [x] Target not present
- [x] All negative numbers
- [x] Large array

---

## Interview Tips

### Questions to Ask Interviewer

1. **"Are there duplicate elements?"**
   - Changes algorithm complexity
   - LeetCode 81 vs 33

2. **"What's the array size range?"**
   - Very small arrays might not need binary search

3. **"Can I assume the array is rotated?"**
   - If not rotated, standard binary search works

4. **"Should I return the index or boolean?"**
   - Clarify return type

### Explaining Your Approach

**Good explanation structure:**

1. "The key insight is that after rotation, the array has two sorted subarrays"

2. "At any midpoint, at least one half must be properly sorted"

3. "I determine which half is sorted by comparing endpoints"

4. "Then I check if target falls in the sorted half's range"

5. "If yes, search that half; otherwise search the other half"

6. "This maintains O(log n) time by eliminating half the array each iteration"

### Follow-up Questions

**Q: What if array has duplicates?**
A: Need to handle case where `nums[left] == nums[mid] == nums[right]`. Worst case becomes O(n).

**Q: What if you need to find the rotation count?**
A: Find index of minimum element.

**Q: Can you do it in one pass?**
A: Yes, the solution above is one pass.

**Q: What if we want to find if any element exists?**
A: Same algorithm, just return boolean instead of index.

---

## Related Problems

### Similar Problems

1. **LeetCode 81: Search in Rotated Sorted Array II**
   - With duplicates
   - Worst case O(n)

2. **LeetCode 153: Find Minimum in Rotated Sorted Array**
   - Find rotation point
   - No target, find minimum

3. **LeetCode 154: Find Minimum in Rotated Sorted Array II**
   - With duplicates
   - Harder version

### Using Same Pattern

4. **LeetCode 162: Find Peak Element**
   - Modified binary search
   - Choose direction based on slope

5. **LeetCode 852: Peak Index in Mountain Array**
   - Similar to find peak
   - Guaranteed to have peak

6. **LeetCode 1095: Find in Mountain Array**
   - Binary search on mountain
   - Two phases

---

## Optimization & Best Practices

### Code Optimization

```python
class Solution:
    def search(self, nums: List[int], target: int) -> int:
        """Optimized version with early exits."""
        n = len(nums)
        
        # Quick checks
        if n == 0:
            return -1
        if n == 1:
            return 0 if nums[0] == target else -1
        
        left, right = 0, n - 1
        
        while left <= right:
            mid = left + (right - left) // 2
            
            if nums[mid] == target:
                return mid
            
            # Left half sorted
            if nums[left] <= nums[mid]:
                if nums[left] <= target < nums[mid]:
                    right = mid - 1
                else:
                    left = mid + 1
            # Right half sorted
            else:
                if nums[mid] < target <= nums[right]:
                    left = mid + 1
                else:
                    right = mid - 1
        
        return -1
```

### Best Practices

1. **Use clear variable names**: `left`, `right`, `mid`
2. **Avoid overflow**: `mid = left + (right - left) // 2`
3. **Comment complex conditions**: Explain sorted half logic
4. **Handle edge cases early**: Check empty, single element
5. **Test thoroughly**: All rotation scenarios

---

## Summary

### Key Takeaways

1. **At least one half is always sorted** after rotation
2. **Identify sorted half**: Compare `nums[left]` with `nums[mid]`
3. **Range check**: Determine if target in sorted half
4. **Time complexity**: Still O(log n) like regular binary search
5. **Watch boundaries**: Use `<` vs `<=` correctly

### Algorithm Template

```python
def search(nums, target):
    left, right = 0, len(nums) - 1
    
    while left <= right:
        mid = left + (right - left) // 2
        
        if nums[mid] == target:
            return mid
        
        # Determine which half is sorted
        if nums[left] <= nums[mid]:
            # Left half sorted
            if nums[left] <= target < nums[mid]:
                right = mid - 1
            else:
                left = mid + 1
        else:
            # Right half sorted
            if nums[mid] < target <= nums[right]:
                left = mid + 1
            else:
                right = mid - 1
    
    return -1
```

### Complexity
- **Time**: O(log n)
- **Space**: O(1)

---

**Tags**: #binary-search #rotated-array #modified-binary-search #medium  
**Related**: Find Minimum in Rotated Array, Search in Rotated Array II  
**Companies**: Amazon, Microsoft, Facebook, Google, Apple
