# Combination Sum - LeetCode #39

**Difficulty**: Medium  
**Pattern**: Backtracking with Reuse  
**Frequency**: Very High (Core Backtracking Pattern)

---

## Problem Statement

Given an array of **distinct** integers `candidates` and a target integer `target`, return **a list of all unique combinations** of `candidates` where the chosen numbers sum to `target`. You may return the combinations in **any order**.

The **same** number may be chosen from `candidates` an **unlimited number of times**. Two combinations are unique if the frequency of at least one of the chosen numbers is different.

**Example 1:**
```
Input: candidates = [2,3,6,7], target = 7
Output: [[2,2,3],[7]]
Explanation:
2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
7 is a candidate, and 7 = 7.
These are the only two combinations.
```

**Example 2:**
```
Input: candidates = [2,3,5], target = 8
Output: [[2,2,2,2],[2,3,3],[3,5]]
```

**Example 3:**
```
Input: candidates = [2], target = 1
Output: []
```

**Constraints:**
- `1 <= candidates.length <= 30`
- `2 <= candidates[i] <= 40`
- All elements of `candidates` are **distinct**
- `1 <= target <= 40`

---

## Core Concepts

### What Makes This Problem Unique?

**Key Difference from Regular Subsets:**
- **Unlimited reuse**: Can use same element multiple times
- **Target constraint**: Must sum to exact target
- **Early termination**: Stop when sum equals or exceeds target

**Example:** `candidates = [2,3], target = 7`

**Valid combinations:**
```
[2,2,3]  → 2+2+3 = 7 ✓
[2,3,2]  → Same as above (order doesn't matter)
[3,2,2]  → Same as above
```

**Invalid:**
```
[2,2]    → 2+2 = 4 ≠ 7
[3,3,3]  → 3+3+3 = 9 > 7
```

### Why Backtracking?

**Exhaustive Search Needed:**
- Try all possible combinations
- Unknown how many times to use each element
- Need to explore and backtrack

**Decision Tree:**
```
For [2,3], target=7:

                    []
          /                \
      choose 2            choose 3
        [2]                 [3]
      /     \              /    \
    2      3            2      3
   [2,2]  [2,3]      [3,2]   [3,3]
   /  \    / \        / \      / \
  2   3   2  3      2   3    2   3
[2,2,2] [2,2,3]  [3,2,2] [3,2,3] [3,3,2] [3,3,3]
         ✓                                    X
```

### Key Observations

1. **Can reuse elements**: Unlike subset, same element can appear multiple times
2. **Order doesn't matter**: [2,2,3] = [2,3,2] = [3,2,2]
3. **Early pruning**: Stop when sum >= target
4. **Start index**: Use start index to avoid duplicate combinations

---

## Solution Approach

### Strategy

**Backtracking Template with Modifications:**
1. **Include element** at current position (can reuse)
2. **Recurse** with same starting index (for reuse)
3. **Backtrack** and try next element
4. **Prune** when sum exceeds target

**Two Key Differences:**
```python
# Regular Subsets (No reuse)
backtrack(i + 1, current)  # Move to next

# Combination Sum (With reuse)
backtrack(i, current)      # Stay at same index
```

---

## Complete Solution

### Solution 1: Backtracking with Reuse

```python
from typing import List

class Solution:
    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:
        """
        Backtracking with ability to reuse elements.
        
        Key idea: At each position, can either:
        1. Use current element again (stay at same index)
        2. Move to next element
        
        Time: O(N^(T/M)) where N=len(candidates), T=target, M=min(candidates)
              Worst case: all elements are minimum value
        Space: O(T/M) - recursion depth
        """
        result = []
        
        def backtrack(start, current, current_sum):
            # Base case: found valid combination
            if current_sum == target:
                result.append(current[:])
                return
            
            # Pruning: exceeded target
            if current_sum > target:
                return
            
            # Try each candidate from start onwards
            for i in range(start, len(candidates)):
                current.append(candidates[i])
                # Key: Pass i (not i+1) to allow reuse
                backtrack(i, current, current_sum + candidates[i])
                current.pop()  # Backtrack
        
        backtrack(0, [], 0)
        return result
```

### Solution 2: Optimized with Early Pruning

```python
class Solution:
    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:
        """
        Optimized: Sort first and prune early.
        
        Sorting allows us to break early when candidate > remaining.
        
        Time: O(N log N + N^(T/M))
        Space: O(T/M)
        """
        result = []
        candidates.sort()  # Sort for pruning
        
        def backtrack(start, current, remaining):
            if remaining == 0:
                result.append(current[:])
                return
            
            for i in range(start, len(candidates)):
                # Pruning: if current candidate > remaining, all next will be too
                if candidates[i] > remaining:
                    break
                
                current.append(candidates[i])
                backtrack(i, current, remaining - candidates[i])
                current.pop()
        
        backtrack(0, [], target)
        return result
```

### Solution 3: Alternative - Without Sum Tracking

```python
class Solution:
    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:
        """
        Calculate sum on demand instead of tracking.
        
        Pros: Simpler parameters
        Cons: Repeated sum calculations
        
        Time: O(N^(T/M) * T/M) - extra factor for sum calculation
        Space: O(T/M)
        """
        result = []
        
        def backtrack(start, current):
            current_sum = sum(current)
            
            if current_sum == target:
                result.append(current[:])
                return
            
            if current_sum > target:
                return
            
            for i in range(start, len(candidates)):
                current.append(candidates[i])
                backtrack(i, current)
                current.pop()
        
        backtrack(0, [])
        return result
```

### Solution 4: DP Approach (Bottom-Up)

```python
class Solution:
    def combinationSum(self, candidates: List[int], target: int) -> List[List[int]]:
        """
        Dynamic Programming approach.
        
        dp[i] = all combinations that sum to i
        
        Time: O(N * T * L) where L is average length of combinations
        Space: O(T * L)
        """
        # dp[i] stores all combinations that sum to i
        dp = [[] for _ in range(target + 1)]
        dp[0] = [[]]  # Base: one way to make 0 (empty combination)
        
        for candidate in candidates:
            for i in range(candidate, target + 1):
                # For each existing combination that sums to (i - candidate),
                # add current candidate to create combination summing to i
                for combo in dp[i - candidate]:
                    dp[i].append(combo + [candidate])
        
        return dp[target]
```

---

## Detailed Walkthrough

**Input:** `candidates = [2,3,6,7], target = 7`

### Execution Trace

```
backtrack(0, [], 0)  remaining = 7
│
├─ Try candidates[0] = 2
│  └─ backtrack(0, [2], 2)  remaining = 5
│     │
│     ├─ Try 2 again
│     │  └─ backtrack(0, [2,2], 4)  remaining = 3
│     │     │
│     │     ├─ Try 2 again
│     │     │  └─ backtrack(0, [2,2,2], 6)  remaining = 1
│     │     │     └─ Try 2: 6+2=8 > 7, backtrack
│     │     │     └─ Try 3: 6+3=9 > 7, backtrack
│     │     │     └─ Try 6: 6+6=12 > 7, backtrack
│     │     │     └─ Try 7: 6+7=13 > 7, backtrack
│     │     │     
│     │     ├─ Try 3
│     │     │  └─ backtrack(1, [2,2,3], 7)  remaining = 0 ✓
│     │     │     └─ FOUND: [2,2,3]
│     │     │     
│     │     └─ Try 6: 4+6=10 > 7, skip
│     │     └─ Try 7: 4+7=11 > 7, skip
│     │
│     ├─ Try 3
│     │  └─ backtrack(1, [2,3], 5)  remaining = 2
│     │     └─ Try 3: 5+3=8 > 7, backtrack
│     │     └─ Try 6: 5+6=11 > 7, backtrack
│     │     └─ Try 7: 5+7=12 > 7, backtrack
│     │
│     └─ Try 6: 2+6=8 > 7, skip
│     └─ Try 7: 2+7=9 > 7, skip
│
├─ Try candidates[1] = 3
│  └─ backtrack(1, [3], 3)  remaining = 4
│     └─ Try 3: 3+3=6 < 7, continue
│        └─ backtrack(1, [3,3], 6)  remaining = 1
│           └─ Try 3: 6+3=9 > 7, backtrack
│           └─ Try 6: 6+6=12 > 7, backtrack
│           └─ Try 7: 6+7=13 > 7, backtrack
│     └─ Try 6: 3+6=9 > 7, skip
│     └─ Try 7: 3+7=10 > 7, skip
│
├─ Try candidates[2] = 6
│  └─ backtrack(2, [6], 6)  remaining = 1
│     └─ Try 6: 6+6=12 > 7, backtrack
│     └─ Try 7: 6+7=13 > 7, backtrack
│
└─ Try candidates[3] = 7
   └─ backtrack(3, [7], 7)  remaining = 0 ✓
      └─ FOUND: [7]

RESULT: [[2,2,3], [7]]
```

### Visual Decision Tree

```
                            []
        ┌──────┬──────┬──────┴──────┐
        2      3      6             7
       [2]    [3]    [6]           [7] ✓
    ┌──┼──┐   │      X              
    2  3  6   3                     
  [2,2] [2,3] [3,3]                 
  ┌─┼─┐  X    X                     
  2 3 6                             
[2,2,2] [2,2,3] ✓                   
   X                                
```

---

## Complexity Analysis

### Time Complexity: O(N^(T/M))

Where:
- N = number of candidates
- T = target value
- M = minimum candidate value

**Analysis:**
- Worst case: smallest candidate is used repeatedly
- Depth of recursion: T/M (target divided by min value)
- At each level: up to N choices
- Total: N^(T/M)

**Example:** candidates = [2], target = 8
- Depth: 8/2 = 4
- Branches: 1^4 = 1
- Combinations: [2,2,2,2]

**Example:** candidates = [1,2,3], target = 4
- Depth: 4/1 = 4
- Branches: up to 3 at each level
- Much larger search space

### Space Complexity: O(T/M)

**Components:**
1. Recursion call stack: O(T/M) depth
2. Current combination: O(T/M) elements max
3. Result storage: Not counted (output)

**Note:** With sorting optimization, add O(N log N) preprocessing.

---

## Pattern Variations

### Variation 1: Combination Sum II (No Reuse)

**Problem:** Each number used at most once.

```python
def combinationSum2(candidates, target):
    """
    LeetCode 40: Can't reuse elements, may have duplicates.
    """
    result = []
    candidates.sort()
    
    def backtrack(start, current, remaining):
        if remaining == 0:
            result.append(current[:])
            return
        
        for i in range(start, len(candidates)):
            # Skip duplicates at same level
            if i > start and candidates[i] == candidates[i-1]:
                continue
            
            if candidates[i] > remaining:
                break
            
            current.append(candidates[i])
            # Key difference: i+1 (no reuse)
            backtrack(i + 1, current, remaining - candidates[i])
            current.pop()
    
    backtrack(0, [], target)
    return result
```

### Variation 2: Combination Sum III

**Problem:** Find k numbers that add up to n (only use 1-9).

```python
def combinationSum3(k, n):
    """
    LeetCode 216: Exactly k numbers from 1-9 (no reuse).
    """
    result = []
    
    def backtrack(start, current, remaining):
        # Base case: found k numbers
        if len(current) == k:
            if remaining == 0:
                result.append(current[:])
            return
        
        # Pruning: too many numbers would be needed
        if remaining < 0 or len(current) > k:
            return
        
        for i in range(start, 10):  # 1-9
            if i > remaining:
                break
            
            current.append(i)
            backtrack(i + 1, current, remaining - i)
            current.pop()
    
    backtrack(1, [], n)
    return result
```

### Variation 3: Combination Sum IV (Order Matters)

**Problem:** Count combinations where order matters.

```python
def combinationSum4(nums, target):
    """
    LeetCode 377: Order matters (like coin change 2).
    This becomes a DP problem.
    """
    dp = [0] * (target + 1)
    dp[0] = 1  # One way to make 0
    
    for i in range(1, target + 1):
        for num in nums:
            if i >= num:
                dp[i] += dp[i - num]
    
    return dp[target]
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Using i+1 Instead of i

```python
# ❌ Wrong: Can't reuse elements
def backtrack(start, current, remaining):
    for i in range(start, len(candidates)):
        current.append(candidates[i])
        backtrack(i + 1, current, remaining - candidates[i])  # Wrong!
        current.pop()

# ✅ Correct: Allow reuse
def backtrack(start, current, remaining):
    for i in range(start, len(candidates)):
        current.append(candidates[i])
        backtrack(i, current, remaining - candidates[i])  # Correct!
        current.pop()
```

### Mistake 2: Not Pruning

```python
# ❌ Wrong: Continues even when exceeded
def backtrack(start, current, remaining):
    if remaining == 0:
        result.append(current[:])
        return
    
    for i in range(start, len(candidates)):  # No pruning
        current.append(candidates[i])
        backtrack(i, current, remaining - candidates[i])
        current.pop()

# ✅ Correct: Prune early
def backtrack(start, current, remaining):
    if remaining == 0:
        result.append(current[:])
        return
    
    if remaining < 0:  # Prune!
        return
    
    for i in range(start, len(candidates)):
        if candidates[i] > remaining:  # Prune!
            break
        current.append(candidates[i])
        backtrack(i, current, remaining - candidates[i])
        current.pop()
```

### Mistake 3: Duplicate Combinations

```python
# ❌ Wrong: Creates duplicates like [2,3] and [3,2]
def backtrack(current, remaining):
    if remaining == 0:
        result.append(current[:])
        return
    
    for num in candidates:  # Restart from beginning each time
        current.append(num)
        backtrack(current, remaining - num)
        current.pop()

# ✅ Correct: Use start index
def backtrack(start, current, remaining):
    if remaining == 0:
        result.append(current[:])
        return
    
    for i in range(start, len(candidates)):  # Start from start
        current.append(candidates[i])
        backtrack(i, current, remaining - candidates[i])
        current.pop()
```

### Mistake 4: Not Copying Result

```python
# ❌ Wrong: All results point to same list
result.append(current)

# ✅ Correct: Make a copy
result.append(current[:])
result.append(current.copy())
result.append(list(current))
```

---

## Optimization Techniques

### Technique 1: Sort and Break

```python
def combinationSum(candidates, target):
    result = []
    candidates.sort()  # Sort ascending
    
    def backtrack(start, current, remaining):
        if remaining == 0:
            result.append(current[:])
            return
        
        for i in range(start, len(candidates)):
            if candidates[i] > remaining:
                break  # All next candidates will be larger
            
            current.append(candidates[i])
            backtrack(i, current, remaining - candidates[i])
            current.pop()
    
    backtrack(0, [], target)
    return result
```

### Technique 2: Count-Based Approach

```python
def combinationSum(candidates, target):
    """
    For small candidates, use count-based approach.
    """
    result = []
    
    def backtrack(idx, current, remaining):
        if remaining == 0:
            result.append(current[:])
            return
        
        if idx == len(candidates) or remaining < 0:
            return
        
        # Try different counts of current candidate
        for count in range(remaining // candidates[idx] + 1):
            backtrack(idx + 1, 
                     current + [candidates[idx]] * count,
                     remaining - candidates[idx] * count)
    
    backtrack(0, [], target)
    return result
```

### Technique 3: Memoization (For Counting)

```python
def combinationSum_count(candidates, target):
    """
    If only counting combinations (not generating them).
    """
    memo = {}
    
    def backtrack(remaining):
        if remaining == 0:
            return 1
        if remaining < 0:
            return 0
        
        if remaining in memo:
            return memo[remaining]
        
        count = 0
        for num in candidates:
            count += backtrack(remaining - num)
        
        memo[remaining] = count
        return count
    
    return backtrack(target)
```

---

## Edge Cases & Testing

### Edge Cases

```python
# Single candidate equals target
candidates = [7], target = 7
# Output: [[7]]

# Single candidate, multiple uses
candidates = [2], target = 8
# Output: [[2,2,2,2]]

# No solution exists
candidates = [2], target = 3
# Output: []

# Target is 1 (minimum target)
candidates = [1,2,3], target = 1
# Output: [[1]]

# Large target with small candidates
candidates = [1], target = 40
# Output: [[1,1,1,...,1]] (40 times)

# Multiple valid combinations
candidates = [2,3,5], target = 8
# Output: [[2,2,2,2],[2,3,3],[3,5]]
```

### Test Suite

```python
def test_combination_sum():
    solution = Solution()
    
    # Test 1: Basic case
    result = solution.combinationSum([2,3,6,7], 7)
    assert sorted(result) == sorted([[2,2,3],[7]])
    
    # Test 2: Multiple solutions
    result = solution.combinationSum([2,3,5], 8)
    assert len(result) == 3
    
    # Test 3: No solution
    result = solution.combinationSum([2], 1)
    assert result == []
    
    # Test 4: Single element solution
    result = solution.combinationSum([1], 1)
    assert result == [[1]]
    
    # Test 5: Multiple uses
    result = solution.combinationSum([2], 4)
    assert result == [[2,2]]
    
    # Test 6: Large candidates
    result = solution.combinationSum([10,20,30], 40)
    assert [10,10,10,10] in result or [10,10,20] in result
    
    print("All tests passed!")

def test_edge_cases():
    solution = Solution()
    
    # Edge: All candidates larger than target
    result = solution.combinationSum([10,20,30], 5)
    assert result == []
    
    # Edge: Exact match with one candidate
    result = solution.combinationSum([5,10,15], 15)
    assert [15] in result
    
    # Edge: Multiple ways with repeats
    result = solution.combinationSum([1,2], 3)
    assert [1,1,1] in result
    assert [1,2] in result
    
    print("Edge case tests passed!")
```

---

## Interview Tips

### Questions to Ask

1. **"Can I reuse the same candidate multiple times?"**
   - For this problem: Yes
   - Critical distinction from Combination Sum II

2. **"Are all candidates positive integers?"**
   - Yes (constraint: >= 2)
   - Important for termination

3. **"Can candidates be zero?"**
   - No in this problem
   - Would cause infinite loop if allowed

4. **"Should combinations be in any specific order?"**
   - No, any order is fine

5. **"What if no solution exists?"**
   - Return empty list

### Explaining Your Solution

**Clear explanation structure:**

1. "This is a backtracking problem where we need to find all combinations that sum to target"

2. "The key difference is we can reuse candidates unlimited times"

3. "I'll use a start index to avoid duplicate combinations like [2,3] and [3,2]"

4. "When recursing, I'll pass the same index (not i+1) to allow reusing the current candidate"

5. "I'll prune branches early when the sum exceeds target"

6. "Time complexity is O(N^(T/M)) in worst case where we use smallest candidate repeatedly"

### Common Follow-ups

**Q: What if each candidate can only be used once?**
A: Change backtrack(i, ...) to backtrack(i+1, ...) and handle duplicates

**Q: Can you optimize for sorted candidates?**
A: Yes, sort first and break when candidate > remaining

**Q: How would you count combinations instead of generating them?**
A: Use DP or memoization: dp[i] += dp[i-candidate]

**Q: What if candidates can be negative?**
A: Need to track depth or path length to avoid infinite loops

**Q: Memory constraints for large targets?**
A: Use generator/iterator pattern for lazy evaluation

---

## Related Problems

### Same Pattern (Backtracking with Reuse)

1. **LeetCode 40: Combination Sum II**
   - No reuse allowed
   - May have duplicate candidates
   - Need to skip duplicates at same recursion level

2. **LeetCode 216: Combination Sum III**
   - Fixed size k
   - Use numbers 1-9 only
   - No reuse

3. **LeetCode 377: Combination Sum IV**
   - Order matters (different problem type)
   - Use DP instead of backtracking

### Similar Backtracking

4. **LeetCode 78: Subsets**
   - No target constraint
   - Generate all subsets
   - Similar structure

5. **LeetCode 46: Permutations**
   - Order matters
   - Track used elements
   - Different pattern

6. **LeetCode 22: Generate Parentheses**
   - Constraint-based generation
   - Prune invalid states

### Dynamic Programming Alternative

7. **LeetCode 322: Coin Change**
   - Minimum coins (optimization)
   - Use DP instead of backtracking

8. **LeetCode 518: Coin Change 2**
   - Count ways (DP)
   - Related to counting combinations

---

## Advanced Concepts

### Mathematical Approach

**Problem:** Count combinations (not generate)

```python
def count_combinations(candidates, target):
    """
    Use generating functions approach.
    Each candidate contributes: 1 + x^c + x^(2c) + ...
    """
    # DP approach
    dp = [0] * (target + 1)
    dp[0] = 1
    
    for candidate in candidates:
        for i in range(candidate, target + 1):
            dp[i] += dp[i - candidate]
    
    return dp[target]
```

### Generator Pattern

```python
def combination_sum_generator(candidates, target):
    """
    Generate combinations lazily (memory efficient).
    """
    def backtrack(start, current, remaining):
        if remaining == 0:
            yield current[:]
            return
        
        if remaining < 0:
            return
        
        for i in range(start, len(candidates)):
            if candidates[i] > remaining:
                break
            
            current.append(candidates[i])
            yield from backtrack(i, current, remaining - candidates[i])
            current.pop()
    
    candidates.sort()
    return backtrack(0, [], target)

# Usage
for combo in combination_sum_generator([2,3,6,7], 7):
    print(combo)  # [2,2,3], [7]
```

### Parallel Processing

```python
def combination_sum_parallel(candidates, target):
    """
    Split search space for parallel processing.
    """
    from multiprocessing import Pool
    
    def search_starting_with(start_idx):
        result = []
        
        def backtrack(idx, current, remaining):
            if remaining == 0:
                result.append(current[:])
                return
            
            for i in range(idx, len(candidates)):
                if candidates[i] > remaining:
                    break
                current.append(candidates[i])
                backtrack(i, current, remaining - candidates[i])
                current.pop()
        
        backtrack(start_idx, [], target)
        return result
    
    with Pool() as pool:
        results = pool.map(search_starting_with, range(len(candidates)))
    
    # Flatten results
    return [combo for sublist in results for combo in sublist]
```

---

## Summary

### Key Takeaways

1. **Core Pattern**: Backtracking with unlimited reuse
2. **Critical Detail**: Pass same index (i) not i+1 for reuse
3. **Pruning**: Stop when sum >= target
4. **Avoid Duplicates**: Use start index to maintain order
5. **Optimization**: Sort and break early

### Algorithm Template

```python
def combinationSum(candidates, target):
    result = []
    
    def backtrack(start, current, remaining):
        # Base case: found valid combination
        if remaining == 0:
            result.append(current[:])
            return
        
        # Pruning: exceeded target
        if remaining < 0:
            return
        
        for i in range(start, len(candidates)):
            # Pruning optimization
            if candidates[i] > remaining:
                break
            
            current.append(candidates[i])
            # Key: pass i (not i+1) for reuse
            backtrack(i, current, remaining - candidates[i])
            current.pop()
    
    candidates.sort()  # Optional: for early pruning
    backtrack(0, [], target)
    return result
```

### Complexity Summary

- **Time**: O(N^(T/M)) where N=candidates, T=target, M=min(candidates)
- **Space**: O(T/M) for recursion depth

### Key Differences from Similar Problems

| Problem | Reuse? | Duplicates? | Order? |
|---------|--------|-------------|--------|
| Combination Sum (39) | ✓ | No | No |
| Combination Sum II (40) | ✗ | Yes | No |
| Combination Sum III (216) | ✗ | No | No |
| Combination Sum IV (377) | ✓ | No | ✓ |

---

**Tags**: #backtracking #combination #reuse #target-sum #dfs #medium  
**Related**: Combination Sum II, Coin Change, Subsets  
**Companies**: Facebook, Amazon, Microsoft, Bloomberg, Apple, Google
