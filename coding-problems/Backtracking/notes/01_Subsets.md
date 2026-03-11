# Subsets - LeetCode #78

**Difficulty**: Medium  
**Pattern**: Backtracking (Combinations / Power Set)  
**Frequency**: Very High (Core Backtracking Pattern)

---

## Problem Statement

Given an integer array `nums` of **unique** elements, return **all possible subsets** (the power set).

The solution set **must not** contain duplicate subsets. Return the solution in **any order**.

**Example 1:**
```
Input: nums = [1,2,3]
Output: [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
```

**Example 2:**
```
Input: nums = [0]
Output: [[],[0]]
```

**Constraints:**
- `1 <= nums.length <= 10`
- `-10 <= nums[i] <= 10`
- All the numbers of `nums` are **unique**

---

## Core Concepts

### What is a Subset?

**Subset:** A collection of elements from the original set, **order doesn't matter**.

**Example:** `nums = [1,2,3]`

**All subsets:**
```
[]           - empty set
[1]          - single element
[2]
[3]
[1,2]        - two elements
[1,3]
[2,3]
[1,2,3]      - all elements (the set itself)
```

**Total:** 2³ = 8 subsets

### Power Set

**Power Set:** The set of all subsets of a set.

**Formula:** For set of size n, power set has **2ⁿ** subsets

**Why?** Each element has 2 choices: **include** or **exclude**

```
n = 3 elements [1,2,3]
Element 1: include or not (2 choices)
Element 2: include or not (2 choices)
Element 3: include or not (2 choices)
Total: 2 × 2 × 2 = 8 subsets
```

### Subset vs Subsequence vs Subarray

| Type        | Order Matters | Contiguous |
|-------------|---------------|------------|
| Subset      | No            | No         |
| Subsequence | Yes           | No         |
| Subarray    | Yes           | Yes        |

**Example:** `[1,2,3]`
- Subset: `[1,3]` = `[3,1]` (same)
- Subsequence: `[1,3]` ≠ `[3,1]` (different)
- Subarray: `[1,2]` ✓, `[1,3]` ✗ (not contiguous)

---

## Solution Approach

### Decision Tree / Recursion Tree

**For each element:** Make a binary decision - include or exclude

```
                    []
                /        \
           include 1    exclude 1
              [1]           []
            /    \        /    \
        include 2  exclude 2  include 2  exclude 2
          [1,2]      [1]        [2]         []
         /    \     /   \      /   \       /   \
        3    -3    3   -3     3   -3      3   -3
     [1,2,3] [1,2] [1,3] [1] [2,3] [2]   [3]  []
```

**Leaves:** All 8 subsets

### Backtracking Strategy

**Template:**
1. Make a choice (include current element)
2. Explore (recurse)
3. Undo choice (backtrack)
4. Make opposite choice (exclude)
5. Explore again

**OR: Build incrementally**
1. At each position, decide to include or not
2. Move to next position
3. When reach end, add current subset to result

---

## Complete Solution

### Solution 1: Backtracking (Include/Exclude)

```python
from typing import List

class Solution:
    def subsets(self, nums: List[int]) -> List[List[int]]:
        """
        Backtracking approach with explicit include/exclude.
        
        For each element, try two branches:
        1. Include it in current subset
        2. Exclude it from current subset
        
        Time: O(n × 2^n) - 2^n subsets, O(n) to copy each
        Space: O(n) - recursion depth
        """
        result = []
        
        def backtrack(index, current):
            # Base case: processed all elements
            if index == len(nums):
                result.append(current[:])  # Make a copy
                return
            
            # Choice 1: Include nums[index]
            current.append(nums[index])
            backtrack(index + 1, current)
            current.pop()  # Backtrack
            
            # Choice 2: Exclude nums[index]
            backtrack(index + 1, current)
        
        backtrack(0, [])
        return result
```

### Solution 2: Backtracking (Start Index)

```python
class Solution:
    def subsets(self, nums: List[int]) -> List[List[int]]:
        """
        More intuitive: decide at each position what to add next.
        
        Time: O(n × 2^n)
        Space: O(n)
        """
        result = []
        
        def backtrack(start, current):
            # Add current subset at every recursion level
            result.append(current[:])
            
            # Try adding each remaining element
            for i in range(start, len(nums)):
                current.append(nums[i])
                backtrack(i + 1, current)  # Next element
                current.pop()  # Backtrack
        
        backtrack(0, [])
        return result
```

### Solution 3: Iterative (Build Up)

```python
class Solution:
    def subsets(self, nums: List[int]) -> List[List[int]]:
        """
        Iterative approach: build subsets incrementally.
        
        Start with [[]]
        For each number, add it to all existing subsets
        
        Time: O(n × 2^n)
        Space: O(1) excluding output
        """
        result = [[]]  # Start with empty subset
        
        for num in nums:
            # For each existing subset, create new subset by adding num
            result += [curr + [num] for curr in result]
        
        return result
```

**Example walkthrough:**
```
nums = [1,2,3]

Start: result = [[]]

After 1: result = [[], [1]]
         (add 1 to each existing)

After 2: result = [[], [1], [2], [1,2]]
         (add 2 to each existing)

After 3: result = [[], [1], [2], [1,2], [3], [1,3], [2,3], [1,2,3]]
         (add 3 to each existing)
```

### Solution 4: Bit Manipulation

```python
class Solution:
    def subsets(self, nums: List[int]) -> List[List[int]]:
        """
        Bit manipulation: each subset corresponds to binary number.
        
        For n elements, generate all numbers from 0 to 2^n - 1
        Each bit represents include (1) or exclude (0)
        
        Time: O(n × 2^n)
        Space: O(1) excluding output
        """
        n = len(nums)
        result = []
        
        # Generate all numbers from 0 to 2^n - 1
        for mask in range(1 << n):  # 1 << n = 2^n
            subset = []
            for i in range(n):
                # Check if i-th bit is set
                if mask & (1 << i):
                    subset.append(nums[i])
            result.append(subset)
        
        return result
```

**Example:** `nums = [1,2,3]`, n = 3, 2³ = 8 combinations

```
Mask (binary) | Subset
-------------|--------
000 (0)      | []
001 (1)      | [1]
010 (2)      | [2]
011 (3)      | [1,2]
100 (4)      | [3]
101 (5)      | [1,3]
110 (6)      | [2,3]
111 (7)      | [1,2,3]
```

---

## Detailed Walkthrough: Backtracking

**Input:** `nums = [1,2,3]`

### Recursion Tree (Start Index Approach)

```
backtrack(0, [])
├─ result += [[]]                    → [[]]
├─ i=0: [1]
│   ├─ backtrack(1, [1])
│   │   ├─ result += [[1]]           → [[],[1]]
│   │   ├─ i=1: [1,2]
│   │   │   ├─ backtrack(2, [1,2])
│   │   │   │   ├─ result += [[1,2]] → [[],[1],[1,2]]
│   │   │   │   ├─ i=2: [1,2,3]
│   │   │   │   │   ├─ backtrack(3, [1,2,3])
│   │   │   │   │   │   └─ result += [[1,2,3]] → [[],[1],[1,2],[1,2,3]]
│   │   │   │   │   └─ pop: [1,2]
│   │   │   │   └─ loop end
│   │   │   └─ pop: [1]
│   │   ├─ i=2: [1,3]
│   │   │   ├─ backtrack(3, [1,3])
│   │   │   │   └─ result += [[1,3]] → [[],[1],[1,2],[1,2,3],[1,3]]
│   │   │   └─ pop: [1]
│   │   └─ loop end
│   └─ pop: []
├─ i=1: [2]
│   ├─ backtrack(2, [2])
│   │   ├─ result += [[2]]           → [[],[1],[1,2],[1,2,3],[1,3],[2]]
│   │   ├─ i=2: [2,3]
│   │   │   ├─ backtrack(3, [2,3])
│   │   │   │   └─ result += [[2,3]] → [[],[1],[1,2],[1,2,3],[1,3],[2],[2,3]]
│   │   │   └─ pop: [2]
│   │   └─ loop end
│   └─ pop: []
├─ i=2: [3]
│   ├─ backtrack(3, [3])
│   │   └─ result += [[3]]           → [[],[1],[1,2],[1,2,3],[1,3],[2],[2,3],[3]]
│   └─ pop: []
└─ loop end

Final: [[],[1],[1,2],[1,2,3],[1,3],[2],[2,3],[3]]
```

---

## Complexity Analysis

### Time Complexity: O(n × 2ⁿ)

**Why?**
- **Number of subsets:** 2ⁿ
- **Each subset:** O(n) to copy to result
- **Total:** O(n × 2ⁿ)

**Breakdown:**
```
Subsets of size 0: C(n,0) = 1, cost = 0
Subsets of size 1: C(n,1) = n, cost = n × 1
Subsets of size 2: C(n,2) = n(n-1)/2, cost = n(n-1)/2 × 2
...
Subsets of size n: C(n,n) = 1, cost = n

Total: Σ k × C(n,k) = n × 2^(n-1) = O(n × 2^n)
```

### Space Complexity

**Backtracking:** O(n)
- Recursion depth: O(n)
- Current subset: O(n)
- Total: O(n)

**Iterative:** O(1) excluding output

**Output space:** O(n × 2ⁿ) for storing all subsets

### Growth Rate

| n | 2ⁿ | n × 2ⁿ (operations) |
|---|----|--------------------|
| 1 | 2  | 2                  |
| 5 | 32 | 160                |
| 10| 1,024 | 10,240          |
| 15| 32,768 | 491,520         |
| 20| 1,048,576 | 20,971,520   |

**Constraint:** n ≤ 10 makes this acceptable

---

## Pattern Variations

### Variation 1: Subsets with Duplicates (LeetCode 90)

**Problem:** `nums = [1,2,2]` → no duplicate subsets

```python
def subsetsWithDup(nums):
    """
    Sort first, skip duplicates at same level.
    """
    nums.sort()  # Important!
    result = []
    
    def backtrack(start, current):
        result.append(current[:])
        
        for i in range(start, len(nums)):
            # Skip duplicates at same recursion level
            if i > start and nums[i] == nums[i-1]:
                continue
            
            current.append(nums[i])
            backtrack(i + 1, current)
            current.pop()
    
    backtrack(0, [])
    return result
```

**Key:** Skip duplicate at **same recursion level**, not different levels

### Variation 2: Subsets of Size K (Combinations)

**Problem:** Generate all subsets of size exactly k

```python
def combine(n, k):
    """
    LeetCode 77: Combinations
    All subsets of size k from [1..n]
    """
    result = []
    
    def backtrack(start, current):
        # Found valid combination
        if len(current) == k:
            result.append(current[:])
            return
        
        # Pruning: not enough elements left
        need = k - len(current)
        remain = n - start + 1
        if remain < need:
            return
        
        for i in range(start, n + 1):
            current.append(i)
            backtrack(i + 1, current)
            current.pop()
    
    backtrack(1, [])
    return result
```

### Variation 3: Letter Combinations

```python
def letterCombinations(digits):
    """
    LeetCode 17: Similar pattern
    """
    if not digits:
        return []
    
    phone = {
        '2': 'abc', '3': 'def', '4': 'ghi',
        '5': 'jkl', '6': 'mno', '7': 'pqrs',
        '8': 'tuv', '9': 'wxyz'
    }
    
    result = []
    
    def backtrack(index, current):
        if index == len(digits):
            result.append(current)
            return
        
        for letter in phone[digits[index]]:
            backtrack(index + 1, current + letter)
    
    backtrack(0, "")
    return result
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Not Making a Copy

```python
# ❌ Wrong
result.append(current)  # Reference added, will be modified

# ✅ Correct
result.append(current[:])  # Copy the list
result.append(current.copy())
result.append(list(current))
```

**Why it fails:**
```python
current = []
result.append(current)  # result = [[]]
current.append(1)       # result = [[1]] - modified!
```

### Mistake 2: Wrong Base Case

```python
# ❌ Wrong: Only adds full subsets
def backtrack(start, current):
    if len(current) == len(nums):  # Only n-sized subset
        result.append(current[:])
        return

# ✅ Correct: Add at every level
def backtrack(start, current):
    result.append(current[:])
    for i in range(start, len(nums)):
        ...
```

### Mistake 3: Not Backtracking

```python
# ❌ Wrong: Doesn't restore state
def backtrack(start, current):
    for i in range(start, len(nums)):
        current.append(nums[i])
        backtrack(i + 1, current)
        # Missing: current.pop()

# ✅ Correct
def backtrack(start, current):
    for i in range(start, len(nums)):
        current.append(nums[i])
        backtrack(i + 1, current)
        current.pop()  # Backtrack!
```

### Mistake 4: Allowing Reuse

```python
# ❌ Wrong: Can reuse elements
backtrack(i, current)  # Same index

# ✅ Correct: No reuse
backtrack(i + 1, current)  # Next index
```

---

## Approach Comparison

### Which Solution is Best?

**For interviews: Backtracking (Start Index)**
- ✅ Most intuitive
- ✅ Easy to explain
- ✅ Extends to variations
- ✅ Standard pattern

**For contests: Iterative**
- ✅ Faster to code
- ✅ No recursion overhead
- ⚠️ Less flexible

**For fun: Bit Manipulation**
- ✅ Clever and elegant
- ✅ Shows understanding
- ⚠️ Harder to extend

---

## Edge Cases & Testing

### Edge Cases

```python
# Single element
nums = [1] → [[], [1]]

# Two elements
nums = [1,2] → [[], [1], [2], [1,2]]

# Negative numbers
nums = [-1,0,1] → [[], [-1], [0], [-1,0], [1], [-1,1], [0,1], [-1,0,1]]

# All same (with constraint of unique elements, won't happen)

# Maximum size
nums = [1,2,3,4,5,6,7,8,9,10]  # 2^10 = 1024 subsets
```

### Test Suite

```python
def test_subsets():
    solution = Solution()
    
    # Test 1: Basic case
    result = solution.subsets([1,2,3])
    assert len(result) == 8
    assert [] in result
    assert [1,2,3] in result
    
    # Test 2: Single element
    result = solution.subsets([0])
    assert len(result) == 2
    assert [] in result
    assert [0] in result
    
    # Test 3: Two elements
    result = solution.subsets([1,2])
    assert len(result) == 4
    
    # Test 4: With negative
    result = solution.subsets([-1,0,1])
    assert len(result) == 8
    
    # Test 5: Power of 2 check
    for n in range(1, 6):
        nums = list(range(n))
        result = solution.subsets(nums)
        assert len(result) == 2**n
    
    print("All tests passed!")
```

---

## Interview Tips

### Questions to Ask

1. **"Can I assume all elements are unique?"**
   - Yes in this problem (LeetCode 78)
   - No in LeetCode 90

2. **"Does order matter in output?"**
   - Usually no for subsets

3. **"Are there memory constraints?"**
   - Output itself is O(n × 2ⁿ)

4. **"Should I return in any specific order?"**
   - Usually any order is fine

### Explaining Your Solution

**Clear explanation:**

1. "This is a classic backtracking problem generating all subsets"

2. "At each position, I decide whether to include that element or not"

3. "I maintain a current subset and add it to results at each step"

4. "Then I try adding each remaining element and recurse"

5. "After recursion, I backtrack by removing the element"

6. "Time is O(n × 2^n) since we generate 2^n subsets"

### Follow-up Questions

**Q: What if we want only subsets of size k?**
A: Add condition `if len(current) == k: add and return`

**Q: What if there are duplicates?**
A: Sort first, skip duplicates at same level (LeetCode 90)

**Q: Can you do it iteratively?**
A: Yes, build up by adding each number to existing subsets

**Q: What if array is very large?**
A: Can't avoid exponential time, but can optimize space with generators

---

## Related Problems

### Same Pattern

1. **LeetCode 90: Subsets II**
   - With duplicates
   - Need to skip duplicates

2. **LeetCode 77: Combinations**
   - Subsets of specific size k
   - Add size constraint

3. **LeetCode 39: Combination Sum**
   - With target sum
   - Can reuse elements

### Similar Backtracking

4. **LeetCode 46: Permutations**
   - Order matters
   - No start index

5. **LeetCode 17: Letter Combinations**
   - Multiple choices per position
   - Phone keypad

6. **LeetCode 131: Palindrome Partitioning**
   - Partition instead of subset
   - Validation needed

---

## Advanced Techniques

### Optimization: Pruning

```python
def subsets_pruned(nums):
    """
    With additional constraints, can prune branches.
    Example: Only subsets with sum < target
    """
    result = []
    target = 10  # Example constraint
    
    def backtrack(start, current, current_sum):
        if current_sum >= target:  # Prune
            return
        
        result.append(current[:])
        
        for i in range(start, len(nums)):
            current.append(nums[i])
            backtrack(i + 1, current, current_sum + nums[i])
            current.pop()
    
    backtrack(0, [], 0)
    return result
```

### Generator for Memory Efficiency

```python
def subsets_generator(nums):
    """
    Generate subsets one at a time (lazy evaluation).
    """
    def backtrack(start, current):
        yield current[:]
        
        for i in range(start, len(nums)):
            current.append(nums[i])
            yield from backtrack(i + 1, current)
            current.pop()
    
    return backtrack(0, [])

# Usage
for subset in subsets_generator([1,2,3]):
    print(subset)
```

---

## Summary

### Key Takeaways

1. **Subsets problem** generates **power set** (2ⁿ subsets)
2. **Backtracking pattern**: Make choice, recurse, backtrack
3. **Add at every level**, not just leaves
4. **Copy current subset** when adding to result
5. **Time O(n × 2ⁿ)**, Space O(n) for recursion

### Algorithm Template

```python
def subsets(nums):
    result = []
    
    def backtrack(start, current):
        result.append(current[:])  # Add at every level
        
        for i in range(start, len(nums)):
            current.append(nums[i])
            backtrack(i + 1, current)
            current.pop()  # Backtrack
    
    backtrack(0, [])
    return result
```

### Complexity
- **Time**: O(n × 2ⁿ)
- **Space**: O(n) recursion depth

---

**Tags**: #backtracking #subsets #powerset #combinations #dfs #medium  
**Related**: Subsets II, Combinations, Permutations  
**Companies**: Facebook, Amazon, Microsoft, Bloomberg, Google
