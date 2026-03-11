# Permutations - LeetCode #46

**Difficulty**: Medium  
**Pattern**: Backtracking (Permutations)  
**Frequency**: Very High (Core Interview Pattern)

---

## Problem Statement

Given an array `nums` of **distinct** integers, return **all the possible permutations**. You can return the answer in **any order**.

**Example 1:**
```
Input: nums = [1,2,3]
Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
```

**Example 2:**
```
Input: nums = [0,1]
Output: [[0,1],[1,0]]
```

**Example 3:**
```
Input: nums = [1]
Output: [[1]]
```

**Constraints:**
- `1 <= nums.length <= 6`
- `-10 <= nums[i] <= 10`
- All the integers of `nums` are **unique**

---

## Core Concepts

### What is a Permutation?

**Permutation:** An arrangement of elements where **order matters**.

**Example:** `nums = [1,2,3]`

**Key Difference from Combinations:**
```
Combination: {1,2,3} = {3,2,1}  (order doesn't matter)
Permutation: [1,2,3] ≠ [3,2,1]  (order matters)
```

**All permutations of [1,2,3]:**
```
[1,2,3]  [1,3,2]
[2,1,3]  [2,3,1]
[3,1,2]  [3,2,1]
```

**Total:** n! = 3! = 6 permutations

### Permutation Properties

**Formula:** For n distinct elements, there are **n!** permutations

**Why?**
- First position: n choices
- Second position: n-1 choices (one already used)
- Third position: n-2 choices
- Total: n × (n-1) × (n-2) × ... × 1 = n!

**Example factorial growth:**
```
n = 1:  1! = 1
n = 2:  2! = 2
n = 3:  3! = 6
n = 4:  4! = 24
n = 5:  5! = 120
n = 6:  6! = 720
n = 10: 10! = 3,628,800
```

### Permutation vs Combination vs Subset

| Type | Order | Count (n=3) | Example |
|------|-------|-------------|---------|
| Permutation | Matters | n! = 6 | [1,2,3], [1,3,2], [2,1,3], ... |
| Combination | Doesn't | C(n,k) | Size 2: {1,2}, {1,3}, {2,3} |
| Subset | Doesn't | 2^n = 8 | [], [1], [2], [3], [1,2], ... |

---

## Solution Approach

### Decision Tree Visualization

**For [1,2,3]:**

```
                        []
         ┌──────────────┼──────────────┐
         1              2              3
        [1]            [2]            [3]
      ┌──┴──┐        ┌──┴──┐        ┌──┴──┐
      2     3        1     3        1     2
    [1,2] [1,3]    [2,1] [2,3]    [3,1] [3,2]
     │     │        │     │        │     │
     3     2        3     1        2     1
  [1,2,3] [1,3,2] [2,1,3] [2,3,1] [3,1,2] [3,2,1]
```

**Height:** n levels  
**Branching:** Decreases at each level (n, n-1, n-2, ...)  
**Leaves:** n! permutations

### Backtracking Strategy

**Key Idea:** At each position, try every unused element

**Steps:**
1. Mark element as used
2. Add to current permutation
3. Recurse to fill next position
4. Backtrack: remove element and mark as unused

**Difference from Subsets/Combinations:**
- **No start index**: Can use any element at any position
- **Track used elements**: Need to know what's available
- **Fixed length**: Stop when length equals n

---

## Complete Solution

### Solution 1: Backtracking with Used Set

```python
from typing import List

class Solution:
    def permute(self, nums: List[int]) -> List[List[int]]:
        """
        Backtracking with explicit used tracking.
        
        At each position, try every unused element.
        
        Time: O(n! × n) - n! permutations, O(n) to copy each
        Space: O(n) - recursion depth + used set
        """
        result = []
        n = len(nums)
        
        def backtrack(current, used):
            # Base case: permutation complete
            if len(current) == n:
                result.append(current[:])
                return
            
            # Try each unused element
            for i in range(n):
                if i not in used:
                    # Choose
                    current.append(nums[i])
                    used.add(i)
                    
                    # Explore
                    backtrack(current, used)
                    
                    # Unchoose (backtrack)
                    current.pop()
                    used.remove(i)
        
        backtrack([], set())
        return result
```

### Solution 2: Backtracking with Boolean Array

```python
class Solution:
    def permute(self, nums: List[int]) -> List[List[int]]:
        """
        Use boolean array for faster lookups than set.
        
        Time: O(n! × n)
        Space: O(n)
        """
        result = []
        n = len(nums)
        used = [False] * n
        
        def backtrack(current):
            if len(current) == n:
                result.append(current[:])
                return
            
            for i in range(n):
                if not used[i]:
                    # Choose
                    current.append(nums[i])
                    used[i] = True
                    
                    # Explore
                    backtrack(current)
                    
                    # Unchoose
                    current.pop()
                    used[i] = False
        
        backtrack([])
        return result
```

### Solution 3: In-place Swapping

```python
class Solution:
    def permute(self, nums: List[int]) -> List[List[int]]:
        """
        Swap elements to generate permutations in-place.
        
        More space efficient - no extra used tracking needed.
        
        Time: O(n! × n)
        Space: O(n) - only recursion stack
        """
        result = []
        
        def backtrack(start):
            # Base case: reached end
            if start == len(nums):
                result.append(nums[:])
                return
            
            # Try each element at position 'start'
            for i in range(start, len(nums)):
                # Swap to place nums[i] at position 'start'
                nums[start], nums[i] = nums[i], nums[start]
                
                # Recurse for next position
                backtrack(start + 1)
                
                # Backtrack: restore original order
                nums[start], nums[i] = nums[i], nums[start]
        
        backtrack(0)
        return result
```

### Solution 4: Iterative (Build Up)

```python
class Solution:
    def permute(self, nums: List[int]) -> List[List[int]]:
        """
        Build permutations iteratively.
        
        Start with first element, insert next element at all positions.
        
        Time: O(n! × n)
        Space: O(n!)
        """
        result = [[]]  # Start with empty permutation
        
        for num in nums:
            new_perms = []
            for perm in result:
                # Insert num at each position in perm
                for i in range(len(perm) + 1):
                    new_perm = perm[:i] + [num] + perm[i:]
                    new_perms.append(new_perm)
            result = new_perms
        
        return result
```

**Example walkthrough:**
```
nums = [1,2,3]

Start: result = [[]]

After 1: result = [[1]]
         (insert 1 at position 0)

After 2: result = [[2,1], [1,2]]
         From [1]: insert 2 at position 0 → [2,1]
         From [1]: insert 2 at position 1 → [1,2]

After 3: result = [[3,2,1], [2,3,1], [2,1,3],
                   [3,1,2], [1,3,2], [1,2,3]]
         From [2,1]: insert 3 at positions 0,1,2 → [3,2,1], [2,3,1], [2,1,3]
         From [1,2]: insert 3 at positions 0,1,2 → [3,1,2], [1,3,2], [1,2,3]
```

### Solution 5: Using Library (Python)

```python
from itertools import permutations

class Solution:
    def permute(self, nums: List[int]) -> List[List[int]]:
        """
        Python's built-in permutations function.
        
        Time: O(n! × n)
        Space: O(n!)
        """
        return [list(p) for p in permutations(nums)]
```

---

## Detailed Walkthrough

**Input:** `nums = [1,2,3]`

### Execution Trace (Used Set Approach)

```
backtrack([], {})
│
├─ Try i=0 (nums[0]=1)
│  └─ backtrack([1], {0})
│     │
│     ├─ Try i=1 (nums[1]=2)
│     │  └─ backtrack([1,2], {0,1})
│     │     │
│     │     ├─ Try i=2 (nums[2]=3)
│     │     │  └─ backtrack([1,2,3], {0,1,2})
│     │     │     └─ len=3, FOUND: [1,2,3] ✓
│     │     │
│     │     └─ Backtrack to [1,2], {0,1}
│     │
│     ├─ Try i=2 (nums[2]=3)
│     │  └─ backtrack([1,3], {0,2})
│     │     │
│     │     ├─ Try i=1 (nums[1]=2)
│     │     │  └─ backtrack([1,3,2], {0,1,2})
│     │     │     └─ len=3, FOUND: [1,3,2] ✓
│     │     │
│     │     └─ Backtrack to [1,3], {0,2}
│     │
│     └─ Backtrack to [1], {0}
│
├─ Try i=1 (nums[1]=2)
│  └─ backtrack([2], {1})
│     │
│     ├─ Try i=0 (nums[0]=1)
│     │  └─ backtrack([2,1], {0,1})
│     │     │
│     │     ├─ Try i=2 (nums[2]=3)
│     │     │  └─ backtrack([2,1,3], {0,1,2})
│     │     │     └─ len=3, FOUND: [2,1,3] ✓
│     │     │
│     │     └─ Backtrack
│     │
│     ├─ Try i=2 (nums[2]=3)
│     │  └─ backtrack([2,3], {1,2})
│     │     │
│     │     ├─ Try i=0 (nums[0]=1)
│     │     │  └─ backtrack([2,3,1], {0,1,2})
│     │     │     └─ len=3, FOUND: [2,3,1] ✓
│     │     │
│     │     └─ Backtrack
│     │
│     └─ Backtrack to [2], {1}
│
└─ Try i=2 (nums[2]=3)
   └─ backtrack([3], {2})
      │
      ├─ Try i=0 (nums[0]=1)
      │  └─ backtrack([3,1], {0,2})
      │     │
      │     ├─ Try i=1 (nums[1]=2)
      │     │  └─ backtrack([3,1,2], {0,1,2})
      │     │     └─ len=3, FOUND: [3,1,2] ✓
      │     │
      │     └─ Backtrack
      │
      ├─ Try i=1 (nums[1]=2)
      │  └─ backtrack([3,2], {1,2})
      │     │
      │     ├─ Try i=0 (nums[0]=1)
      │     │  └─ backtrack([3,2,1], {0,1,2})
      │     │     └─ len=3, FOUND: [3,2,1] ✓
      │     │
      │     └─ Backtrack
      │
      └─ Backtrack

RESULT: [[1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]]
```

### Swap Approach Visualization

```
Initial: [1,2,3]

backtrack(0):
├─ swap(0,0): [1,2,3] → backtrack(1)
│  ├─ swap(1,1): [1,2,3] → backtrack(2)
│  │  └─ swap(2,2): [1,2,3] ✓
│  └─ swap(1,2): [1,3,2] → backtrack(2)
│     └─ swap(2,2): [1,3,2] ✓
│
├─ swap(0,1): [2,1,3] → backtrack(1)
│  ├─ swap(1,1): [2,1,3] → backtrack(2)
│  │  └─ swap(2,2): [2,1,3] ✓
│  └─ swap(1,2): [2,3,1] → backtrack(2)
│     └─ swap(2,2): [2,3,1] ✓
│
└─ swap(0,2): [3,2,1] → backtrack(1)
   ├─ swap(1,1): [3,2,1] → backtrack(2)
   │  └─ swap(2,2): [3,2,1] ✓
   └─ swap(1,2): [3,1,2] → backtrack(2)
      └─ swap(2,2): [3,1,2] ✓
```

---

## Complexity Analysis

### Time Complexity: O(n! × n)

**Components:**
1. **Number of permutations**: n!
2. **Copy each permutation**: O(n)
3. **Total**: O(n! × n)

**Detailed analysis:**
```
Level 0: n choices
Level 1: (n-1) choices for each
Level 2: (n-2) choices for each
...
Total nodes: n × (n-1) × ... × 1 = n!
```

**Growth rate:**
```
n=3:  6 × 3 = 18 operations
n=4:  24 × 4 = 96 operations
n=5:  120 × 5 = 600 operations
n=6:  720 × 6 = 4,320 operations
```

### Space Complexity: O(n)

**Components:**
1. Recursion call stack: O(n) depth
2. Current permutation: O(n) space
3. Used tracking: O(n) space
4. **Total**: O(n) excluding output

**Note:** Output requires O(n! × n) space but not counted.

---

## Pattern Variations

### Variation 1: Permutations II (With Duplicates)

**Problem:** Array may contain duplicates.

```python
def permuteUnique(nums):
    """
    LeetCode 47: Permutations with duplicates.
    """
    result = []
    nums.sort()  # Sort to identify duplicates
    used = [False] * len(nums)
    
    def backtrack(current):
        if len(current) == len(nums):
            result.append(current[:])
            return
        
        for i in range(len(nums)):
            # Skip if used
            if used[i]:
                continue
            
            # Skip duplicate at same recursion level
            if i > 0 and nums[i] == nums[i-1] and not used[i-1]:
                continue
            
            current.append(nums[i])
            used[i] = True
            backtrack(current)
            current.pop()
            used[i] = False
    
    backtrack([])
    return result
```

**Key insight:** Skip duplicate when previous same element not used yet.

### Variation 2: Next Permutation

**Problem:** Find next lexicographically greater permutation.

```python
def nextPermutation(nums):
    """
    LeetCode 31: In-place next permutation.
    
    Algorithm:
    1. Find first decreasing element from right
    2. Find smallest element to its right that's larger
    3. Swap them
    4. Reverse the suffix
    """
    n = len(nums)
    
    # Find first decreasing element from right
    i = n - 2
    while i >= 0 and nums[i] >= nums[i + 1]:
        i -= 1
    
    if i >= 0:  # Not last permutation
        # Find smallest element larger than nums[i]
        j = n - 1
        while nums[j] <= nums[i]:
            j -= 1
        
        # Swap
        nums[i], nums[j] = nums[j], nums[i]
    
    # Reverse suffix
    left, right = i + 1, n - 1
    while left < right:
        nums[left], nums[right] = nums[right], nums[left]
        left += 1
        right -= 1
```

### Variation 3: Permutation Sequence

**Problem:** Find k-th permutation directly.

```python
def getPermutation(n, k):
    """
    LeetCode 60: Get k-th permutation without generating all.
    
    Use factorial number system.
    """
    import math
    
    numbers = list(range(1, n + 1))
    result = []
    k -= 1  # Convert to 0-indexed
    
    for i in range(n, 0, -1):
        factorial = math.factorial(i - 1)
        index = k // factorial
        result.append(str(numbers[index]))
        numbers.pop(index)
        k %= factorial
    
    return ''.join(result)
```

### Variation 4: Palindrome Permutation II

**Problem:** Generate all palindrome permutations.

```python
def generatePalindromes(s):
    """
    LeetCode 267: Palindrome permutations.
    """
    from collections import Counter
    
    counter = Counter(s)
    odd_char = ""
    half = []
    
    # Check if palindrome permutation possible
    odd_count = 0
    for char, count in counter.items():
        if count % 2:
            odd_count += 1
            odd_char = char
        half.extend([char] * (count // 2))
    
    if odd_count > 1:
        return []
    
    # Generate permutations of half
    result = []
    used = [False] * len(half)
    half.sort()
    
    def backtrack(current):
        if len(current) == len(half):
            palindrome = current + [odd_char] + current[::-1]
            result.append(''.join(palindrome))
            return
        
        for i in range(len(half)):
            if used[i]:
                continue
            if i > 0 and half[i] == half[i-1] and not used[i-1]:
                continue
            
            current.append(half[i])
            used[i] = True
            backtrack(current)
            current.pop()
            used[i] = False
    
    backtrack([])
    return result
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Using Start Index

```python
# ❌ Wrong: Uses start index (generates combinations, not permutations)
def backtrack(start, current):
    for i in range(start, len(nums)):  # Wrong for permutations
        current.append(nums[i])
        backtrack(i + 1, current)
        current.pop()

# ✅ Correct: No start index for permutations
def backtrack(current, used):
    for i in range(len(nums)):  # Check all elements
        if i not in used:
            current.append(nums[i])
            used.add(i)
            backtrack(current, used)
            current.pop()
            used.remove(i)
```

### Mistake 2: Not Tracking Used Elements

```python
# ❌ Wrong: Allows reusing elements
def backtrack(current):
    if len(current) == len(nums):
        result.append(current[:])
        return
    
    for num in nums:  # Can use same element multiple times
        current.append(num)
        backtrack(current)
        current.pop()

# ✅ Correct: Track what's been used
def backtrack(current, used):
    if len(current) == len(nums):
        result.append(current[:])
        return
    
    for i in range(len(nums)):
        if i not in used:  # Check if already used
            current.append(nums[i])
            used.add(i)
            backtrack(current, used)
            current.pop()
            used.remove(i)
```

### Mistake 3: Not Copying Result

```python
# ❌ Wrong: All results point to same list
result.append(current)

# ✅ Correct: Make a copy
result.append(current[:])
result.append(list(current))
result.append(current.copy())
```

### Mistake 4: Swap Without Backtrack

```python
# ❌ Wrong: Doesn't restore array
def backtrack(start):
    if start == len(nums):
        result.append(nums[:])
        return
    
    for i in range(start, len(nums)):
        nums[start], nums[i] = nums[i], nums[start]
        backtrack(start + 1)
        # Missing: swap back!

# ✅ Correct: Restore array state
def backtrack(start):
    if start == len(nums):
        result.append(nums[:])
        return
    
    for i in range(start, len(nums)):
        nums[start], nums[i] = nums[i], nums[start]
        backtrack(start + 1)
        nums[start], nums[i] = nums[i], nums[start]  # Backtrack!
```

---

## Edge Cases & Testing

### Edge Cases

```python
# Single element
nums = [1]
# Output: [[1]]

# Two elements
nums = [1,2]
# Output: [[1,2], [2,1]]

# Three elements (given example)
nums = [1,2,3]
# Output: 6 permutations

# With negative numbers
nums = [-1,0,1]
# Output: 6 permutations (same as positive)

# Maximum size (constraint: n <= 6)
nums = [1,2,3,4,5,6]
# Output: 720 permutations
```

### Test Suite

```python
def test_permute():
    solution = Solution()
    
    # Test 1: Single element
    result = solution.permute([1])
    assert result == [[1]]
    
    # Test 2: Two elements
    result = solution.permute([1,2])
    assert len(result) == 2
    assert [1,2] in result
    assert [2,1] in result
    
    # Test 3: Three elements
    result = solution.permute([1,2,3])
    assert len(result) == 6
    expected = [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
    for perm in expected:
        assert perm in result
    
    # Test 4: With negative
    result = solution.permute([-1,0,1])
    assert len(result) == 6
    
    # Test 5: Factorial check
    for n in range(1, 6):
        nums = list(range(n))
        result = solution.permute(nums)
        factorial = 1
        for i in range(1, n + 1):
            factorial *= i
        assert len(result) == factorial
    
    print("All tests passed!")

def test_all_unique():
    """Verify all permutations are unique."""
    solution = Solution()
    result = solution.permute([1,2,3,4])
    
    # Convert to tuples for hashing
    result_set = {tuple(perm) for perm in result}
    
    # Should be 24 unique permutations
    assert len(result) == 24
    assert len(result_set) == 24
    
    print("Uniqueness test passed!")
```

---

## Interview Tips

### Questions to Ask

1. **"Can the array contain duplicates?"**
   - For LeetCode 46: No
   - For LeetCode 47: Yes (different problem)

2. **"What's the maximum size of the array?"**
   - Usually n <= 10 (factorial grows very fast)

3. **"Does order of permutations in output matter?"**
   - Usually no

4. **"Should I return them in lexicographic order?"**
   - Usually not required

5. **"Can I use extra space?"**
   - Usually yes (need to track used elements)

### Explaining Your Solution

**Clear explanation:**

1. "This is a classic backtracking problem for generating all permutations"

2. "Unlike combinations, order matters, so [1,2,3] and [3,2,1] are different"

3. "At each position, I try every element that hasn't been used yet"

4. "I track used elements with a set/array to avoid reusing them"

5. "When I've built a complete permutation (length n), I add it to results"

6. "Time complexity is O(n! × n) because there are n! permutations"

### Common Follow-ups

**Q: What if array has duplicates?**
A: Sort first, skip duplicates at same level (LeetCode 47)

**Q: Can you generate only k-th permutation?**
A: Yes, use factorial number system (LeetCode 60)

**Q: How to generate next permutation?**
A: Find first decreasing from right, swap, reverse suffix (LeetCode 31)

**Q: Can you do it iteratively?**
A: Yes, insert each element at all positions incrementally

**Q: Memory optimization?**
A: Swap in-place approach uses O(1) extra space

---

## Related Problems

### Same Pattern (Backtracking)

1. **LeetCode 47: Permutations II**
   - With duplicate elements
   - Need to skip duplicates at same level

2. **LeetCode 784: Letter Case Permutation**
   - Toggle letter case
   - Similar backtracking structure

3. **LeetCode 78: Subsets**
   - Order doesn't matter
   - 2^n instead of n!

### Permutation Variants

4. **LeetCode 31: Next Permutation**
   - Find next lexicographic permutation
   - O(n) algorithm

5. **LeetCode 60: Permutation Sequence**
   - Get k-th permutation directly
   - Use factorial system

6. **LeetCode 266/267: Palindrome Permutation I/II**
   - Check if palindrome possible
   - Generate palindrome permutations

### Related Concepts

7. **LeetCode 17: Letter Combinations**
   - Multiple choices at each position
   - Similar structure

8. **LeetCode 22: Generate Parentheses**
   - Constraint-based generation
   - Pruning invalid states

---

## Advanced Techniques

### Optimization: Pruning

```python
def permute_with_constraint(nums, constraint):
    """
    Generate permutations with constraint (e.g., sum < target).
    """
    result = []
    used = [False] * len(nums)
    
    def backtrack(current, current_sum):
        if len(current) == len(nums):
            result.append(current[:])
            return
        
        for i in range(len(nums)):
            if used[i]:
                continue
            
            # Pruning based on constraint
            if current_sum + nums[i] > constraint:
                continue
            
            current.append(nums[i])
            used[i] = True
            backtrack(current, current_sum + nums[i])
            current.pop()
            used[i] = False
    
    backtrack([], 0)
    return result
```

### Lexicographic Order

```python
def permute_lexicographic(nums):
    """
    Generate permutations in lexicographic order.
    """
    nums.sort()  # Start with smallest
    result = []
    used = [False] * len(nums)
    
    def backtrack(current):
        if len(current) == len(nums):
            result.append(current[:])
            return
        
        for i in range(len(nums)):  # Try in sorted order
            if used[i]:
                continue
            
            current.append(nums[i])
            used[i] = True
            backtrack(current)
            current.pop()
            used[i] = False
    
    backtrack([])
    return result
```

### Generator Pattern

```python
def permute_generator(nums):
    """
    Generate permutations lazily for memory efficiency.
    """
    used = [False] * len(nums)
    
    def backtrack(current):
        if len(current) == len(nums):
            yield current[:]
            return
        
        for i in range(len(nums)):
            if not used[i]:
                current.append(nums[i])
                used[i] = True
                yield from backtrack(current)
                current.pop()
                used[i] = False
    
    return backtrack([])

# Usage
for perm in permute_generator([1,2,3]):
    print(perm)
```

### Heaps Algorithm (Efficient)

```python
def heaps_algorithm(nums):
    """
    Heap's algorithm: generates permutations with minimal swaps.
    More efficient than standard backtracking.
    """
    result = []
    
    def generate(k):
        if k == 1:
            result.append(nums[:])
            return
        
        for i in range(k):
            generate(k - 1)
            
            if k % 2 == 0:
                nums[i], nums[k-1] = nums[k-1], nums[i]
            else:
                nums[0], nums[k-1] = nums[k-1], nums[0]
    
    generate(len(nums))
    return result
```

---

## Performance Comparison

### Approach Comparison

| Approach | Time | Space | Pros | Cons |
|----------|------|-------|------|------|
| Used Set | O(n!×n) | O(n) | Clear logic | Set operations |
| Boolean Array | O(n!×n) | O(n) | Faster lookups | Extra array |
| Swap In-place | O(n!×n) | O(n) | Space efficient | Modifies input |
| Iterative | O(n!×n) | O(n!) | No recursion | Complex logic |
| Library | O(n!×n) | O(n!) | Concise | Less learning |

### Which to Use?

**For interviews: Boolean Array or Swap**
- Clear and efficient
- Easy to explain
- Shows good understanding

**For production: Library (itertools)**
- Well-tested
- Optimized
- Less error-prone

**For learning: Used Set**
- Most intuitive
- Clear backtracking pattern
- Easy to debug

---

## Summary

### Key Takeaways

1. **Permutations**: Order matters, n! total
2. **No start index**: Can use any element at any position
3. **Track used**: Must avoid reusing elements
4. **Backtrack**: Restore state after recursion
5. **Time**: O(n! × n), Space: O(n)

### Algorithm Template

```python
def permute(nums):
    result = []
    used = [False] * len(nums)
    
    def backtrack(current):
        # Base case: complete permutation
        if len(current) == len(nums):
            result.append(current[:])
            return
        
        # Try each unused element
        for i in range(len(nums)):
            if not used[i]:
                # Choose
                current.append(nums[i])
                used[i] = True
                
                # Explore
                backtrack(current)
                
                # Unchoose
                current.pop()
                used[i] = False
    
    backtrack([])
    return result
```

### Key Differences

| Problem | Order? | Reuse? | Start Index? | Count |
|---------|--------|--------|--------------|-------|
| Subsets | No | No | Yes | 2^n |
| Combinations | No | No | Yes | C(n,k) |
| Permutations | Yes | No | No | n! |
| Combination Sum | No | Yes | Yes | Varies |

---

**Tags**: #backtracking #permutations #factorial #dfs #medium  
**Related**: Permutations II, Next Permutation, Subsets  
**Companies**: Facebook, Amazon, Microsoft, Google, Bloomberg, Apple
