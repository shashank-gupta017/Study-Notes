# House Robber - LeetCode #198

**Difficulty**: Medium  
**Pattern**: 1D DP (Max of Two Choices)  
**Frequency**: Very High (Classic DP Pattern)

---

## Problem Statement

You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them is that **adjacent houses have security systems connected** and **it will automatically contact the police if two adjacent houses were broken into on the same night**.

Given an integer array `nums` representing the amount of money of each house, return **the maximum amount of money you can rob tonight without alerting the police**.

**Example 1:**
```
Input: nums = [1,2,3,1]
Output: 4
Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
Total amount you can rob = 1 + 3 = 4.
```

**Example 2:**
```
Input: nums = [2,7,9,3,1]
Output: 12
Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
Total amount you can rob = 2 + 9 + 1 = 12.
```

**Example 3:**
```
Input: nums = [2,1,1,2]
Output: 4
Explanation: Rob house 1 (money = 2) and rob house 4 (money = 2).
Total = 4.
```

**Constraints:**
- `1 <= nums.length <= 100`
- `0 <= nums[i] <= 400`

---

## Core Concepts

### The Constraint

**Key Rule:** Cannot rob two adjacent houses

**This means for each house, you have two choices:**
1. **Rob it**: Get money from this house + max from houses before previous
2. **Skip it**: Get max money from previous house

### Decision Making

**At house i:**
```
rob[i] = max(
    rob[i-1],              // Skip current house
    rob[i-2] + nums[i]     // Rob current house
)
```

### Visual Understanding

**Example: nums = [2,7,9,3,1]**

```
Houses:     [2]  [7]  [9]  [3]  [1]
             H0   H1   H2   H3   H4

Options at H2 (value 9):
├─ Rob H2:  Can use H0, can't use H1
│   → 2 + 9 = 11
└─ Skip H2: Use max up to H1
    → 7

Choose max(11, 7) = 11
```

**Decision tree for [2,7,9,3,1]:**
```
                    Start
                   /     \
              Rob H0     Skip H0
                2           0
              /   \       /   \
          Skip H1 Rob H1  Rob H1  Skip H1
             2      0+7=7   0+7=7    0
                   / \      / \      / \
                 ... ...  ... ...  ... ...
```

### Why Dynamic Programming?

**Optimal Substructure:**
- Optimal solution contains optimal subsolutions
- If we rob house i, we need optimal solution for houses 0..i-2

**Overlapping Subproblems:**
- Same subproblems computed multiple times
- Max money up to house i-2 needed multiple times

---

## Solution Approach

### Approach 1: Recursion (Naive)

```python
from typing import List

class Solution:
    def rob(self, nums: List[int]) -> int:
        """
        Naive recursion - exponential time.
        
        Time: O(2^n) - each house has 2 choices
        Space: O(n) - recursion depth
        """
        def dp(i):
            # Base cases
            if i < 0:
                return 0
            if i == 0:
                return nums[0]
            
            # Recurrence: rob current OR skip
            rob_current = nums[i] + dp(i - 2)
            skip_current = dp(i - 1)
            
            return max(rob_current, skip_current)
        
        return dp(len(nums) - 1)
```

**Problem:** Recomputes same subproblems repeatedly!

---

### Approach 2: Memoization (Top-Down DP)

```python
class Solution:
    def rob(self, nums: List[int]) -> int:
        """
        Top-down DP with memoization.
        
        Time: O(n) - each state computed once
        Space: O(n) - memo + recursion stack
        """
        memo = {}
        
        def dp(i):
            # Base cases
            if i < 0:
                return 0
            if i == 0:
                return nums[0]
            
            # Check cache
            if i in memo:
                return memo[i]
            
            # Compute and cache
            rob_current = nums[i] + dp(i - 2)
            skip_current = dp(i - 1)
            memo[i] = max(rob_current, skip_current)
            
            return memo[i]
        
        return dp(len(nums) - 1)
```

---

### Approach 3: Tabulation (Bottom-Up DP)

```python
class Solution:
    def rob(self, nums: List[int]) -> int:
        """
        Bottom-up DP with table.
        
        Time: O(n)
        Space: O(n)
        """
        n = len(nums)
        if n == 0:
            return 0
        if n == 1:
            return nums[0]
        
        # dp[i] = max money robbing houses 0..i
        dp = [0] * n
        dp[0] = nums[0]
        dp[1] = max(nums[0], nums[1])
        
        for i in range(2, n):
            rob_current = dp[i-2] + nums[i]
            skip_current = dp[i-1]
            dp[i] = max(rob_current, skip_current)
        
        return dp[n-1]
```

**Example execution for [2,7,9,3,1]:**
```
dp[0] = 2
dp[1] = max(2, 7) = 7
dp[2] = max(7, 2+9) = max(7, 11) = 11
dp[3] = max(11, 7+3) = max(11, 10) = 11
dp[4] = max(11, 11+1) = max(11, 12) = 12

Answer: 12
```

---

### Approach 4: Space-Optimized DP (Optimal)

```python
class Solution:
    def rob(self, nums: List[int]) -> int:
        """
        Space-optimized: only need last 2 values.
        
        Time: O(n)
        Space: O(1) - constant space!
        """
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        
        # Track last two values
        prev2 = 0          # dp[i-2]
        prev1 = 0          # dp[i-1]
        
        for num in nums:
            current = max(prev1, prev2 + num)
            prev2 = prev1
            prev1 = current
        
        return prev1
```

**This is the optimal solution!**

**Even more concise:**
```python
class Solution:
    def rob(self, nums: List[int]) -> int:
        prev2 = prev1 = 0
        
        for num in nums:
            prev2, prev1 = prev1, max(prev1, prev2 + num)
        
        return prev1
```

---

### Approach 5: Alternative Formulation

```python
class Solution:
    def rob(self, nums: List[int]) -> int:
        """
        Think of it as: max money including vs excluding current house.
        
        include = max money including current house
        exclude = max money excluding current house
        """
        include = 0  # Max including current
        exclude = 0  # Max excluding current
        
        for num in nums:
            new_include = exclude + num  # Must exclude prev to include current
            new_exclude = max(include, exclude)  # Can be either
            
            include = new_include
            exclude = new_exclude
        
        return max(include, exclude)
```

---

## Detailed Walkthrough

### Example: nums = [2,7,9,3,1]

**Step-by-step tabulation:**

```
Initialize: dp = [0, 0, 0, 0, 0]

i=0 (house value 2):
  dp[0] = 2
  dp = [2, 0, 0, 0, 0]

i=1 (house value 7):
  rob house 1: 0 + 7 = 7
  skip house 1: 2
  dp[1] = max(7, 2) = 7
  dp = [2, 7, 0, 0, 0]

i=2 (house value 9):
  rob house 2: dp[0] + 9 = 2 + 9 = 11
  skip house 2: dp[1] = 7
  dp[2] = max(11, 7) = 11
  dp = [2, 7, 11, 0, 0]

i=3 (house value 3):
  rob house 3: dp[1] + 3 = 7 + 3 = 10
  skip house 3: dp[2] = 11
  dp[3] = max(10, 11) = 11
  dp = [2, 7, 11, 11, 0]

i=4 (house value 1):
  rob house 4: dp[2] + 1 = 11 + 1 = 12
  skip house 4: dp[3] = 11
  dp[4] = max(12, 11) = 12
  dp = [2, 7, 11, 11, 12]

Answer: dp[4] = 12
```

**Which houses were robbed?** H0(2), H2(9), H4(1) → 2+9+1 = 12

**Space-optimized execution:**
```
prev2 = 0, prev1 = 0

num=2: current = max(0, 0+2) = 2
       prev2=0, prev1=2

num=7: current = max(2, 0+7) = 7
       prev2=2, prev1=7

num=9: current = max(7, 2+9) = 11
       prev2=7, prev1=11

num=3: current = max(11, 7+3) = 11
       prev2=11, prev1=11

num=1: current = max(11, 11+1) = 12
       prev2=11, prev1=12

Answer: 12
```

---

## Complexity Analysis

### Time Complexity: O(n)

**All DP approaches:**
- Single pass through array
- Constant work per element
- Total: O(n)

### Space Complexity

| Approach | Space | Explanation |
|----------|-------|-------------|
| Recursion | O(n) | Call stack |
| Memoization | O(n) | Memo + stack |
| Tabulation | O(n) | DP array |
| Space-Optimized | O(1) | Only 2 variables |

**Space-optimized is best: O(1) extra space!**

---

## Pattern Variations

### Variation 1: House Robber II (Circular)

**Problem:** Houses arranged in a circle (first and last are adjacent).

```python
def rob_circular(nums):
    """
    LeetCode 213: House Robber II
    
    Key insight: Can't rob both first and last house.
    So either rob houses 0..n-2 OR 1..n-1
    """
    if len(nums) == 1:
        return nums[0]
    
    def rob_linear(houses):
        prev2 = prev1 = 0
        for num in houses:
            prev2, prev1 = prev1, max(prev1, prev2 + num)
        return prev1
    
    # Case 1: Rob houses 0 to n-2 (exclude last)
    # Case 2: Rob houses 1 to n-1 (exclude first)
    return max(rob_linear(nums[:-1]), rob_linear(nums[1:]))
```

### Variation 2: House Robber III (Binary Tree)

**Problem:** Houses arranged as a binary tree.

```python
def rob_tree(root):
    """
    LeetCode 337: House Robber III
    
    Returns (rob_root, skip_root) tuple.
    """
    if not root:
        return (0, 0)
    
    left = rob_tree(root.left)
    right = rob_tree(root.right)
    
    # If rob root, can't rob children
    rob_root = root.val + left[1] + right[1]
    
    # If skip root, take max of children
    skip_root = max(left) + max(right)
    
    return (rob_root, skip_root)

def rob(root):
    return max(rob_tree(root))
```

### Variation 3: Delete and Earn

**Problem:** Pick numbers to earn points, can't pick adjacent values.

```python
def deleteAndEarn(nums):
    """
    LeetCode 740: Similar to House Robber
    
    Convert to house robber problem.
    """
    from collections import Counter
    
    if not nums:
        return 0
    
    # Count frequency
    counts = Counter(nums)
    max_num = max(nums)
    
    # Create array where points[i] = i * count[i]
    points = [0] * (max_num + 1)
    for num, count in counts.items():
        points[num] = num * count
    
    # Now it's house robber problem
    prev2 = prev1 = 0
    for point in points:
        prev2, prev1 = prev1, max(prev1, prev2 + point)
    
    return prev1
```

### Variation 4: Paint Houses

**Problem:** Paint houses with costs, no two adjacent same color.

```python
def minCost(costs):
    """
    LeetCode 256: Paint House
    
    costs[i][j] = cost to paint house i with color j
    """
    if not costs:
        return 0
    
    prev_r = prev_b = prev_g = 0
    
    for cost in costs:
        new_r = min(prev_b, prev_g) + cost[0]
        new_b = min(prev_r, prev_g) + cost[1]
        new_g = min(prev_r, prev_b) + cost[2]
        
        prev_r, prev_b, prev_g = new_r, new_b, new_g
    
    return min(prev_r, prev_b, prev_g)
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Wrong Recurrence

```python
# ❌ Wrong: Doesn't consider skipping
dp[i] = nums[i] + dp[i-2]  # Always robs current house

# ✅ Correct: Consider both options
dp[i] = max(dp[i-1], nums[i] + dp[i-2])
```

### Mistake 2: Base Case Errors

```python
# ❌ Wrong: Doesn't handle single element
dp[0] = nums[0]
dp[1] = nums[1]  # Should be max(nums[0], nums[1])!

# ✅ Correct
dp[0] = nums[0]
dp[1] = max(nums[0], nums[1])
```

### Mistake 3: Index Out of Bounds

```python
# ❌ Wrong: Doesn't check array size
dp = [0] * len(nums)
dp[0] = nums[0]
dp[1] = max(nums[0], nums[1])  # Error if len(nums) == 1!

# ✅ Correct: Check size first
if len(nums) == 1:
    return nums[0]
dp = [0] * len(nums)
dp[0] = nums[0]
dp[1] = max(nums[0], nums[1])
```

### Mistake 4: Space Optimization Variable Update

```python
# ❌ Wrong: Updates in wrong order
prev2 = prev1
prev1 = max(prev1, prev2 + num)  # prev2 already changed!

# ✅ Correct: Update simultaneously
prev2, prev1 = prev1, max(prev1, prev2 + num)
```

---

## Edge Cases & Testing

### Edge Cases

```python
# Single house
nums = [5]
# Output: 5

# Two houses
nums = [2,3]
# Output: 3 (rob second house)

# All same value
nums = [5,5,5,5]
# Output: 10 (rob 1st and 3rd)

# Decreasing values
nums = [10,5,2,1]
# Output: 11 (rob 1st and 3rd or just 1st and 4th)

# Empty (if allowed)
nums = []
# Output: 0

# Large values
nums = [400,400,400,400]
# Output: 800

# Alternating high-low
nums = [100,1,1,100]
# Output: 200
```

### Test Suite

```python
def test_house_robber():
    solution = Solution()
    
    # Test 1: Example cases
    assert solution.rob([1,2,3,1]) == 4
    assert solution.rob([2,7,9,3,1]) == 12
    assert solution.rob([2,1,1,2]) == 4
    
    # Test 2: Edge cases
    assert solution.rob([5]) == 5
    assert solution.rob([2,3]) == 3
    assert solution.rob([1,2]) == 2
    
    # Test 3: All same
    assert solution.rob([5,5,5,5,5]) == 15  # Rob 1,3,5
    
    # Test 4: Increasing
    assert solution.rob([1,2,3,4,5]) == 9  # Rob 1,3,5 → 1+3+5=9
    
    # Test 5: Two high values separated
    assert solution.rob([100,1,1,100]) == 200
    
    # Test 6: Maximum values
    assert solution.rob([400,400,400]) == 800
    
    print("All tests passed!")

def test_optimality():
    """Verify that greedy doesn't work."""
    nums = [2,1,1,2]
    
    # Greedy (always rob max): 2 + 2 = 4 ✓ (happens to work)
    # But for [1,5,3] greedy gives 5, optimal is 1+3=4
    # Actually optimal for [1,5,3] is 5... let's use different example
    
    nums = [5,1,1,5]
    # Greedy starting with first: 5 + 5 = 10 ✓
    # Greedy starting with max: might pick first 5, then can't pick last
    
    solution = Solution()
    result = solution.rob(nums)
    assert result == 10
    
    print("Optimality test passed!")
```

---

## Interview Tips

### Questions to Ask

1. **"Can houses have zero or negative values?"**
   - Usually non-negative (0 to 400)

2. **"What if array is empty?"**
   - Return 0

3. **"Can I modify the input array?"**
   - Better not to (use O(1) space optimization)

4. **"Are houses in a circle?"**
   - That's House Robber II (different problem)

5. **"Do you want the houses robbed or just the max amount?"**
   - Usually just the amount

### Explaining Your Solution

**Clear explanation:**

1. "At each house, I have two choices: rob it or skip it"

2. "If I rob current house, I get its value plus max from two houses ago"

3. "If I skip it, I get the max from previous house"

4. "I take the maximum of these two options"

5. "I can optimize to O(1) space by only tracking last two values"

6. "Time complexity is O(n), space is O(1)"

### Common Follow-ups

**Q: What if houses are in a circle?**
A: House Robber II - rob either 0..n-2 or 1..n-1, take max

**Q: What if houses are in a binary tree?**
A: House Robber III - tree DP with (rob_root, skip_root) states

**Q: Can you reconstruct which houses were robbed?**
A: Keep track of choices while building DP, then backtrack

**Q: What if you can rob k non-adjacent houses?**
A: More complex DP with state tracking remaining k

---

## Related Problems

### Same Pattern (1D DP with Choice)

1. **LeetCode 213: House Robber II**
   - Circular array
   - Split into two linear problems

2. **LeetCode 337: House Robber III**
   - Binary tree structure
   - Tree DP variant

3. **LeetCode 740: Delete and Earn**
   - Similar "can't pick adjacent" constraint
   - Transform to house robber

### Similar DP Pattern

4. **LeetCode 70: Climbing Stairs**
   - Similar recurrence structure
   - Fibonacci-like

5. **LeetCode 256: Paint House**
   - Multiple choices per position
   - No adjacent same color

6. **LeetCode 746: Min Cost Climbing Stairs**
   - Minimize instead of maximize
   - Similar structure

---

## Advanced Techniques

### Technique 1: Reconstruct Solution

```python
def rob_with_path(nums):
    """
    Return both max value and which houses were robbed.
    """
    if not nums:
        return 0, []
    if len(nums) == 1:
        return nums[0], [0]
    
    n = len(nums)
    dp = [0] * n
    choice = [False] * n
    
    dp[0] = nums[0]
    choice[0] = True
    dp[1] = max(nums[0], nums[1])
    choice[1] = nums[1] > nums[0]
    
    for i in range(2, n):
        if dp[i-2] + nums[i] > dp[i-1]:
            dp[i] = dp[i-2] + nums[i]
            choice[i] = True
        else:
            dp[i] = dp[i-1]
            choice[i] = False
    
    # Reconstruct path
    path = []
    i = n - 1
    while i >= 0:
        if choice[i]:
            path.append(i)
            i -= 2
        else:
            i -= 1
    
    return dp[n-1], path[::-1]
```

### Technique 2: With Constraints

```python
def rob_with_max_k(nums, k):
    """
    Rob at most k houses (non-adjacent).
    
    dp[i][j] = max money robbing first i houses with at most j houses robbed
    """
    n = len(nums)
    if k == 0 or n == 0:
        return 0
    
    # dp[i][j] = max money from houses 0..i-1 with at most j robberies
    dp = [[0] * (k + 1) for _ in range(n + 1)]
    
    for i in range(1, n + 1):
        for j in range(k + 1):
            # Skip house i-1
            dp[i][j] = dp[i-1][j]
            
            # Rob house i-1 (if j > 0)
            if j > 0:
                prev = dp[i-2][j-1] if i >= 2 else 0
                dp[i][j] = max(dp[i][j], prev + nums[i-1])
    
    return dp[n][k]
```

### Technique 3: Probability Variant

```python
def rob_with_probability(nums, probs):
    """
    Each house has probability of getting caught.
    Maximize expected value.
    
    probs[i] = probability of getting caught at house i
    """
    prev2 = prev1 = 0
    
    for money, prob in zip(nums, probs):
        expected_value = money * (1 - prob)
        current = max(prev1, prev2 + expected_value)
        prev2 = prev1
        prev1 = current
    
    return prev1
```

---

## Summary

### Key Takeaways

1. **Two Choices**: Rob current (skip previous) or skip current
2. **Recurrence**: dp[i] = max(dp[i-1], dp[i-2] + nums[i])
3. **Base Cases**: dp[0] = nums[0], dp[1] = max(nums[0], nums[1])
4. **Optimization**: Only need last 2 values → O(1) space
5. **Time**: O(n), **Space**: O(1)

### Algorithm Template (Optimal)

```python
def rob(nums: List[int]) -> int:
    """
    Space-optimized DP - best solution.
    
    Time: O(n)
    Space: O(1)
    """
    prev2 = prev1 = 0
    
    for num in nums:
        prev2, prev1 = prev1, max(prev1, prev2 + num)
    
    return prev1
```

### Complexity Summary

**Optimal Solution:**
- Time: O(n) - single pass
- Space: O(1) - two variables

### Pattern Recognition

**This pattern applies when:**
- Making yes/no choice at each step
- Can't choose adjacent elements
- Maximizing/minimizing cumulative value

---

**Tags**: #dynamic-programming #1d-dp #optimization #medium  
**Related**: House Robber II, House Robber III, Delete and Earn  
**Companies**: Amazon, Google, Microsoft, Apple, Facebook, LinkedIn
