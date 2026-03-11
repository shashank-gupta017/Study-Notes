# Jump Game - LeetCode #55

**Difficulty**: Medium  
**Pattern**: Greedy (Farthest Reach)  
**Frequency**: Very High (Classic Greedy Pattern)

---

## Problem Link
[LeetCode 55: Jump Game](https://leetcode.com/problems/jump-game/)

---

## Problem Statement

You are given an integer array `nums`. You are initially positioned at the array's **first index**, and each element in the array represents your **maximum jump length** at that position.

Return `true` if you can reach the last index, or `false` otherwise.

### Examples

#### Example 1:
```
Input: nums = [2,3,1,1,4]
Output: true
Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
```

#### Example 2:
```
Input: nums = [3,2,1,0,4]
Output: false
Explanation: You will always arrive at index 3 no matter what. 
Its maximum jump length is 0, which makes it impossible to reach the last index.
```

#### Example 3:
```
Input: nums = [0]
Output: true
Explanation: Already at the last index.
```

#### Example 4:
```
Input: nums = [2,0,0]
Output: true
Explanation: Jump 2 steps from index 0 directly to the last index (index 2).
```

### Constraints:
- `1 <= nums.length <= 10^4`
- `0 <= nums[i] <= 10^5`

---

## Pattern Recognition

This is a classic **Greedy Algorithm** problem with the **Farthest Reach Pattern**:

1. **Track maximum reachable index**: Keep updating the farthest position we can reach
2. **Local decisions**: At each position, update the farthest we can go
3. **Optimal substructure**: If we can reach position i, we can reach any position before i

### Key Insight
**We don't need to track all possible paths—just track the farthest position reachable!**

```
Instead of: "Can I reach index 5 from index 2?"
Think:      "What's the farthest I can reach?"
```

### Pattern Signals
- Array traversal problem
- Need to determine reachability
- Each element gives a range of choices
- Don't need exact path, just feasibility
- Greedy approach: always track maximum reach

---

## Core Concepts

### 1. The Maximum Reach Concept

**Key Idea:** At each position `i`, we can reach any index from `i` to `i + nums[i]`

```
Position: 0  1  2  3  4
Array:   [2, 3, 1, 1, 4]

From index 0: Can reach 0, 1, 2 (maxReach = 2)
From index 1: Can reach 1, 2, 3, 4 (maxReach = 4)
From index 2: Can reach 2, 3 (maxReach = 3)
```

### 2. Greedy Strategy

**Why Greedy Works:**
- If we can reach position `i`, we can explore all jumps from `i`
- We only care about the **farthest** position reachable
- No need to track which path gets us there

**Greedy Choice:**
```python
maxReach = max(maxReach, i + nums[i])
```

### 3. Two Conditions for Success

1. **Current position must be reachable**: `i <= maxReach`
2. **Eventually reach or exceed last index**: `maxReach >= len(nums) - 1`

```
Valid:   [2,3,1,1,4]
         maxReach grows: 2 → 4 → 4 → 4 → 8
         Always i <= maxReach ✓

Invalid: [3,2,1,0,4]
         maxReach grows: 3 → 3 → 3 → 3 (stuck!)
         At i=4: 4 > maxReach=3 ✗
```

### 4. Why Not Dynamic Programming?

DP would be overkill:
```
DP: Track all reachable positions → O(n²) time, O(n) space
Greedy: Track only farthest reach → O(n) time, O(1) space
```

Both give same answer, but greedy is simpler and faster!

---

## Solutions

### Solution 1: Greedy (Optimal) - Forward Tracking

#### Approach
Track the farthest position we can reach as we iterate forward.

#### Implementation
```python
def canJump(nums):
    """
    Greedy approach: track maximum reachable index.
    
    Time Complexity: O(n) - single pass
    Space Complexity: O(1) - constant space
    
    Args:
        nums: List[int] - array of jump lengths
    
    Returns:
        bool - True if can reach last index
    """
    maxReach = 0
    
    for i in range(len(nums)):
        # If current position is unreachable, return False
        if i > maxReach:
            return False
        
        # Update maximum reachable position
        maxReach = max(maxReach, i + nums[i])
        
        # Early exit: if we can already reach the end
        if maxReach >= len(nums) - 1:
            return True
    
    return True


def canJump_verbose(nums):
    """
    Verbose version with detailed comments.
    """
    n = len(nums)
    
    # Edge case: already at last position
    if n == 1:
        return True
    
    # Track the farthest index we can reach
    farthest = 0
    
    for i in range(n):
        # Check if current position is reachable
        if i > farthest:
            # We've hit a gap we can't cross
            return False
        
        # Calculate farthest we can reach from current position
        current_max = i + nums[i]
        
        # Update global maximum reach
        farthest = max(farthest, current_max)
        
        # Optimization: check if we can already reach the end
        if farthest >= n - 1:
            return True
    
    # If we finished loop without returning False, we can reach end
    return True


def canJump_oneliner(nums):
    """
    Concise one-pass solution.
    """
    maxReach = 0
    for i in range(len(nums)):
        if i > maxReach:
            return False
        maxReach = max(maxReach, i + nums[i])
    return True
```

#### Step-by-Step Walkthrough

**Example 1:** `nums = [2,3,1,1,4]`

```
Initial State:
maxReach = 0

Step 1: i = 0, nums[0] = 2
- Check: 0 <= 0? Yes ✓
- Update: maxReach = max(0, 0+2) = 2
- Can reach: [0, 1, 2]

Step 2: i = 1, nums[1] = 3
- Check: 1 <= 2? Yes ✓
- Update: maxReach = max(2, 1+3) = 4
- Can reach: [0, 1, 2, 3, 4]
- Note: 4 >= last index (4), can return True!

Result: True
```

**Example 2:** `nums = [3,2,1,0,4]`

```
Initial State:
maxReach = 0

Step 1: i = 0, nums[0] = 3
- Check: 0 <= 0? Yes ✓
- Update: maxReach = max(0, 0+3) = 3
- Can reach: [0, 1, 2, 3]

Step 2: i = 1, nums[1] = 2
- Check: 1 <= 3? Yes ✓
- Update: maxReach = max(3, 1+2) = 3
- Can reach: [0, 1, 2, 3]

Step 3: i = 2, nums[2] = 1
- Check: 2 <= 3? Yes ✓
- Update: maxReach = max(3, 2+1) = 3
- Can reach: [0, 1, 2, 3]

Step 4: i = 3, nums[3] = 0
- Check: 3 <= 3? Yes ✓
- Update: maxReach = max(3, 3+0) = 3
- Can reach: [0, 1, 2, 3] (stuck!)

Step 5: i = 4, nums[4] = 4
- Check: 4 <= 3? NO ✗
- Return False

Result: False
```

### Solution 2: Greedy (Backward Tracking)

#### Approach
Work backwards from the end, tracking the leftmost position that can reach the goal.

#### Implementation
```python
def canJump_backward(nums):
    """
    Backward greedy approach.
    
    Time Complexity: O(n)
    Space Complexity: O(1)
    
    Strategy: Work backwards, find leftmost position that can reach goal.
    """
    n = len(nums)
    goal = n - 1  # Start with last index as goal
    
    # Work backwards from second-to-last position
    for i in range(n - 2, -1, -1):
        # Can we reach the goal from position i?
        if i + nums[i] >= goal:
            # Update goal to current position
            goal = i
    
    # If goal moved all the way to start, we can reach the end
    return goal == 0


def canJump_backward_verbose(nums):
    """
    Verbose backward approach with explanation.
    """
    if len(nums) == 1:
        return True
    
    # Start with last index as target
    last_good_position = len(nums) - 1
    
    # Check each position from right to left
    for i in range(len(nums) - 2, -1, -1):
        # Can we jump from position i to last_good_position?
        max_jump_from_i = i + nums[i]
        
        if max_jump_from_i >= last_good_position:
            # Position i can reach our target
            # So now i becomes the new target
            last_good_position = i
    
    # If we can reach position 0, we can complete the jumps
    return last_good_position == 0
```

#### Step-by-Step Walkthrough (Backward)

**Example:** `nums = [2,3,1,1,4]`

```
Initial: goal = 4 (last index)

Step 1: i = 3, nums[3] = 1
- Can reach goal? 3 + 1 = 4 >= 4? Yes ✓
- Update: goal = 3

Step 2: i = 2, nums[2] = 1
- Can reach goal? 2 + 1 = 3 >= 3? Yes ✓
- Update: goal = 2

Step 3: i = 1, nums[1] = 3
- Can reach goal? 1 + 3 = 4 >= 2? Yes ✓
- Update: goal = 1

Step 4: i = 0, nums[0] = 2
- Can reach goal? 0 + 2 = 2 >= 1? Yes ✓
- Update: goal = 0

Final: goal == 0? Yes → Return True
```

### Solution 3: Dynamic Programming (Not Optimal)

#### Approach
Use DP table to track which positions are reachable.

#### Implementation
```python
def canJump_dp(nums):
    """
    DP approach for comparison.
    
    Time Complexity: O(n²) - nested loops
    Space Complexity: O(n) - dp array
    
    Note: Greedy is better! This is just for learning.
    """
    n = len(nums)
    
    # dp[i] = True if we can reach position i
    dp = [False] * n
    dp[0] = True  # Start position is always reachable
    
    for i in range(n):
        if not dp[i]:
            # Can't reach position i, skip
            continue
        
        # From position i, mark all reachable positions
        for j in range(1, nums[i] + 1):
            if i + j < n:
                dp[i + j] = True
            else:
                break  # Beyond array bounds
    
    return dp[n - 1]


def canJump_dp_optimized(nums):
    """
    Slightly optimized DP with early exit.
    
    Time Complexity: O(n²) worst case, better with early exit
    Space Complexity: O(n)
    """
    n = len(nums)
    dp = [False] * n
    dp[0] = True
    
    for i in range(n):
        if not dp[i]:
            continue
        
        # Mark reachable positions
        max_reach = min(i + nums[i], n - 1)
        for j in range(i + 1, max_reach + 1):
            dp[j] = True
            
            # Early exit if we can reach the end
            if j == n - 1:
                return True
    
    return dp[n - 1]
```

### Solution 4: BFS (Graph Approach)

#### Approach
Treat array as a graph where each index can jump to multiple indices.

#### Implementation
```python
from collections import deque

def canJump_bfs(nums):
    """
    BFS approach: treat as graph traversal.
    
    Time Complexity: O(n²) worst case
    Space Complexity: O(n) - queue
    
    Note: Educational purpose only, greedy is much better!
    """
    if len(nums) == 1:
        return True
    
    n = len(nums)
    visited = [False] * n
    queue = deque([0])
    visited[0] = True
    
    while queue:
        pos = queue.popleft()
        
        # Try all possible jumps from current position
        for jump in range(1, nums[pos] + 1):
            next_pos = pos + jump
            
            # Check if we reached the end
            if next_pos >= n - 1:
                return True
            
            # Add unvisited positions to queue
            if not visited[next_pos]:
                visited[next_pos] = True
                queue.append(next_pos)
    
    return False


def canJump_dfs(nums):
    """
    DFS approach with memoization.
    
    Time Complexity: O(n²)
    Space Complexity: O(n)
    """
    n = len(nums)
    memo = {}
    
    def dfs(pos):
        # Base cases
        if pos >= n - 1:
            return True
        if pos in memo:
            return memo[pos]
        if nums[pos] == 0:
            return False
        
        # Try all possible jumps
        for jump in range(1, nums[pos] + 1):
            if dfs(pos + jump):
                memo[pos] = True
                return True
        
        memo[pos] = False
        return False
    
    return dfs(0)
```

---

## Edge Cases

### 1. Single Element
```python
nums = [0]
# Output: True
# Already at the last index
```

### 2. All Zeros Except First
```python
nums = [5,0,0,0,0,0]
# Output: True
# Can jump directly from first to last
```

### 3. Zero at Start (Length > 1)
```python
nums = [0,1,2]
# Output: False
# Can't move from index 0
```

### 4. Large Jump at Start
```python
nums = [10,1,1,1,1]
# Output: True
# First jump can reach any position
```

### 5. Multiple Zero Barriers
```python
nums = [2,0,0,1,4]
# Output: True
# Jump over the zeros

nums = [1,0,1,0,1]
# Output: False
# Can't get past first zero
```

### 6. All Ones
```python
nums = [1,1,1,1,1]
# Output: True
# Can hop through one at a time
```

### 7. Decreasing Values Leading to Zero
```python
nums = [4,3,2,1,0,5]
# Output: False
# Gets stuck at zero

nums = [5,4,3,2,1,0]
# Output: True
# Reaches end before hitting zero
```

### 8. Maximum Values
```python
nums = [100000] * 10000
# Output: True
# First jump reaches end
```

---

## Common Mistakes

### 1. Not Checking if Current Position is Reachable
```python
# WRONG: Doesn't check if position i is reachable
def wrong_solution(nums):
    maxReach = 0
    for i in range(len(nums)):
        maxReach = max(maxReach, i + nums[i])  # Missing check!
    return maxReach >= len(nums) - 1

# Example: [3,2,1,0,4]
# Would return True (wrong!) because it doesn't detect the gap
```

### 2. Using >= Instead of > for Reachability Check
```python
# WRONG: Off-by-one error
def wrong_solution(nums):
    maxReach = 0
    for i in range(len(nums)):
        if i >= maxReach:  # Should be >
            return False
        maxReach = max(maxReach, i + nums[i])
    return True

# Example: [2,0,0] would return False (wrong!)
```

### 3. Not Handling Single Element Array
```python
# WRONG: Doesn't handle edge case
def wrong_solution(nums):
    maxReach = 0
    for i in range(len(nums) - 1):  # Stops before last element
        if i > maxReach:
            return False
        maxReach = max(maxReach, i + nums[i])
    return maxReach >= len(nums) - 1

# Example: [0] would need special handling
```

### 4. Trying to Find Exact Path
```python
# WRONG: Overcomplicated, trying to track path
def wrong_solution(nums):
    path = [0]
    pos = 0
    while pos < len(nums) - 1:
        # Try to find best next jump...
        # This is unnecessary! Just track max reach.
```

### 5. Using Greedy with Wrong Direction
```python
# WRONG: Forward greedy but checking minimum instead of maximum
def wrong_solution(nums):
    minReach = 0
    for i in range(len(nums)):
        minReach = min(minReach, i + nums[i])  # Wrong!
    return minReach >= len(nums) - 1
```

### 6. Forgetting Early Exit Optimization
```python
# NOT WRONG, but inefficient
def suboptimal_solution(nums):
    maxReach = 0
    for i in range(len(nums)):  # Continues even after reaching end
        if i > maxReach:
            return False
        maxReach = max(maxReach, i + nums[i])
    return True

# BETTER: Add early exit
def better_solution(nums):
    maxReach = 0
    for i in range(len(nums)):
        if i > maxReach:
            return False
        maxReach = max(maxReach, i + nums[i])
        if maxReach >= len(nums) - 1:  # Early exit
            return True
    return True
```

---

## Testing

### Test Cases
```python
def test_canJump():
    """Comprehensive test cases."""
    
    # Test 1: Basic case - can reach
    assert canJump([2,3,1,1,4]) == True
    
    # Test 2: Basic case - cannot reach
    assert canJump([3,2,1,0,4]) == False
    
    # Test 3: Single element
    assert canJump([0]) == True
    assert canJump([5]) == True
    
    # Test 4: All zeros except first
    assert canJump([5,0,0,0,0,0]) == True
    
    # Test 5: Zero at start (length > 1)
    assert canJump([0,1,2]) == False
    
    # Test 6: Jump over zeros
    assert canJump([2,0,0,1,4]) == True
    
    # Test 7: Can't jump over zeros
    assert canJump([1,0,1,0,1]) == False
    
    # Test 8: All ones
    assert canJump([1,1,1,1,1]) == True
    
    # Test 9: Large first jump
    assert canJump([10,1,1,1,1]) == True
    
    # Test 10: Exact reach
    assert canJump([2,0,0]) == True
    
    # Test 11: Just short
    assert canJump([1,0,0]) == False
    
    # Test 12: Long array
    assert canJump([1] * 1000) == True
    
    # Test 13: Decreasing to zero
    assert canJump([5,4,3,2,1,0]) == True
    assert canJump([4,3,2,1,0,5]) == False
    
    # Test 14: Multiple paths
    assert canJump([2,3,1,1,4]) == True
    
    # Test 15: Two elements
    assert canJump([0,1]) == False
    assert canJump([1,0]) == True
    
    print("All test cases passed!")


def test_edge_cases():
    """Test edge cases."""
    
    # Single element (already at end)
    assert canJump([0]) == True
    
    # Two elements - can reach
    assert canJump([1,1]) == True
    
    # Two elements - cannot reach
    assert canJump([0,1]) == False
    
    # All same values
    assert canJump([3,3,3,3,3]) == True
    
    # Alternating large and zero
    assert canJump([5,0,5,0,5,0]) == True
    
    # Maximum jump at start
    assert canJump([100000,0,0,0]) == True
    
    # Boundary: exactly reach
    assert canJump([1,1,1,1]) == True
    
    # Boundary: one short
    assert canJump([1,0,1,1]) == False
    
    print("All edge case tests passed!")


def test_comparison():
    """Compare all solutions."""
    test_cases = [
        [2,3,1,1,4],
        [3,2,1,0,4],
        [0],
        [5,0,0,0,0,0],
        [0,1,2],
        [2,0,0],
        [1,0,1,0,1],
        [1] * 100
    ]
    
    for nums in test_cases:
        result1 = canJump(nums)
        result2 = canJump_backward(nums)
        result3 = canJump_dp(nums)
        result4 = canJump_bfs(nums)
        
        assert result1 == result2 == result3 == result4, \
            f"Mismatch for {nums}: {result1}, {result2}, {result3}, {result4}"
    
    print("All solutions give same results!")
```

---

## Complexity Analysis

### Solution 1: Greedy Forward (Optimal)

**Time Complexity: O(n)**
- Single pass through array: O(n)
- Constant work per element: O(1)
- Early exit possible but doesn't change big-O

```
Best case:  O(1) - nums = [100000] (first jump reaches end)
Average:    O(n) - need to check most elements
Worst case: O(n) - nums = [1,1,1,...,1] (check all)
```

**Space Complexity: O(1)**
- Only storing `maxReach`: O(1)
- No recursive calls
- No data structures

### Solution 2: Greedy Backward

**Time Complexity: O(n)**
- Single backward pass: O(n)
- Constant work per element: O(1)

**Space Complexity: O(1)**
- Only storing `goal` position: O(1)

### Solution 3: Dynamic Programming

**Time Complexity: O(n²)**
- Outer loop: O(n)
- Inner loop: O(n) worst case (when nums[i] is large)
- Total: O(n²)

```
Example: nums = [n, 0, 0, ..., 0]
First iteration marks all n positions: O(n)
```

**Space Complexity: O(n)**
- DP array of size n: O(n)

### Solution 4: BFS/DFS

**Time Complexity: O(n²)**
- Each node visited once: O(n)
- Each node can have up to n edges: O(n)
- Total: O(n²)

**Space Complexity: O(n)**
- Queue/recursion stack: O(n)
- Visited array: O(n)

### Comparison Table

```
┌─────────────┬─────────────┬─────────────┬──────────────┐
│  Solution   │    Time     │    Space    │  Practical   │
├─────────────┼─────────────┼─────────────┼──────────────┤
│  Greedy     │    O(n)     │    O(1)     │   BEST ✓     │
│  Backward   │    O(n)     │    O(1)     │   Good ✓     │
│  DP         │    O(n²)    │    O(n)     │   Slow ✗     │
│  BFS/DFS    │    O(n²)    │    O(n)     │   Slow ✗     │
└─────────────┴─────────────┴─────────────┴──────────────┘
```

---

## Why Greedy Works

### Proof of Correctness

**Claim:** Tracking maximum reachable index is sufficient to determine if we can reach the end.

**Proof:**

1. **Base Case:** Position 0 is always reachable (we start there)

2. **Inductive Hypothesis:** 
   - Assume we can reach any position up to `maxReach`
   - At position `i` where `i <= maxReach`, we can reach positions `i` through `i + nums[i]`

3. **Inductive Step:**
   - New maximum: `max(maxReach, i + nums[i])`
   - This represents the farthest position reachable from any position we've seen so far
   - We don't need to track which path gets us there—just that we *can* get there

4. **Conclusion:**
   - If at any point `i > maxReach`, we've found a gap we can't cross
   - If `maxReach >= lastIndex`, we can reach the end
   - Greedy approach correctly determines reachability

### Intuition: The Water Flow Analogy

```
Think of it like water flowing through pipes:

nums = [2, 3, 1, 1, 4]
        ↓  ↓  ↓  ↓  ↓

Water at index 0 can flow to indices 0-2
Water at index 1 can flow to indices 1-4
Water at index 2 can flow to indices 2-3

If water reaches index i, it will reach everything up to i + nums[i]

The question is: Does water reach the last index?

We only need to track the farthest position water has reached!
```

### Why Not Track All Paths?

```
Overkill:
Path 1: 0 → 1 → 4 ✓
Path 2: 0 → 2 → 3 → 4 ✓

Needed:
Can we reach position 4? ✓

Greedy tracks max reach = 4, which is sufficient!
```

---

## Variations and Follow-ups

### Variation 1: Return Minimum Number of Jumps (Jump Game II)

```python
def minJumps(nums):
    """
    LeetCode 45: Jump Game II
    Return minimum number of jumps to reach the end.
    
    Time: O(n), Space: O(1)
    """
    if len(nums) == 1:
        return 0
    
    jumps = 0
    current_max = 0  # Farthest with current jumps
    next_max = 0     # Farthest with one more jump
    
    for i in range(len(nums) - 1):
        next_max = max(next_max, i + nums[i])
        
        # Reached end of current jump range
        if i == current_max:
            jumps += 1
            current_max = next_max
            
            # Early exit
            if current_max >= len(nums) - 1:
                break
    
    return jumps


# Example
nums = [2,3,1,1,4]
print(minJumps(nums))  # Output: 2 (0→1→4)
```

### Variation 2: Return the Actual Jump Path

```python
def jumpPath(nums):
    """
    Return one valid path from start to end.
    
    Time: O(n), Space: O(n)
    """
    if len(nums) == 1:
        return [0]
    
    n = len(nums)
    path = [0]
    pos = 0
    
    while pos < n - 1:
        # Find the position we can jump to that gets us farthest
        max_reach = 0
        next_pos = pos
        
        for jump in range(1, nums[pos] + 1):
            next = pos + jump
            if next >= n - 1:
                path.append(n - 1)
                return path
            
            # Greedy: choose jump that maximizes next reach
            if next + nums[next] > max_reach:
                max_reach = next + nums[next]
                next_pos = next
        
        pos = next_pos
        path.append(pos)
    
    return path


# Example
nums = [2,3,1,1,4]
print(jumpPath(nums))  # Output: [0, 1, 4] or [0, 2, 3, 4]
```

### Variation 3: Can Jump Backwards

```python
def canJumpBidirectional(nums):
    """
    Can jump both forward and backward (each nums[i] steps).
    
    Time: O(n), Space: O(n)
    """
    if len(nums) == 1:
        return True
    
    n = len(nums)
    visited = [False] * n
    queue = deque([0])
    visited[0] = True
    
    while queue:
        pos = queue.popleft()
        
        # Try both directions
        for direction in [-1, 1]:
            for jump in range(1, nums[pos] + 1):
                next_pos = pos + direction * jump
                
                # Check bounds
                if next_pos < 0 or next_pos >= n:
                    continue
                
                # Reached end
                if next_pos == n - 1:
                    return True
                
                # Visit new position
                if not visited[next_pos]:
                    visited[next_pos] = True
                    queue.append(next_pos)
    
    return False
```

### Variation 4: Jump with Cost

```python
def minCostJump(nums, costs):
    """
    Each jump has a cost. Find minimum cost to reach end.
    
    Time: O(n²), Space: O(n)
    Requires DP, greedy doesn't work for weighted version.
    """
    n = len(nums)
    
    # dp[i] = minimum cost to reach position i
    dp = [float('inf')] * n
    dp[0] = 0
    
    for i in range(n):
        if dp[i] == float('inf'):
            continue
        
        # Try all possible jumps
        for jump in range(1, nums[i] + 1):
            next_pos = i + jump
            if next_pos >= n:
                break
            
            # Update minimum cost
            dp[next_pos] = min(dp[next_pos], dp[i] + costs[i])
    
    return dp[n-1] if dp[n-1] != float('inf') else -1


# Example
nums = [2,3,1,1,4]
costs = [1,2,3,4,5]
print(minCostJump(nums, costs))  # Output: 3 (0→1→4, cost=1+2=3)
```

### Variation 5: Jump with Obstacles

```python
def canJumpWithObstacles(nums, obstacles):
    """
    Some positions are obstacles (can't land on them).
    
    Time: O(n), Space: O(1)
    """
    obstacle_set = set(obstacles)
    maxReach = 0
    
    for i in range(len(nums)):
        if i > maxReach:
            return False
        
        # Can't use obstacle position for jumping
        if i in obstacle_set:
            continue
        
        maxReach = max(maxReach, i + nums[i])
        
        if maxReach >= len(nums) - 1:
            return True
    
    return maxReach >= len(nums) - 1


# Example
nums = [2,3,1,1,4]
obstacles = [2]  # Can't land on index 2
print(canJumpWithObstacles(nums, obstacles))  # True (0→1→4)
```

### Variation 6: Circular Array Jumps

```python
def canCompleteCircuit(nums):
    """
    Array is circular (can wrap around).
    Can we reach start from start (complete circuit)?
    
    Time: O(n), Space: O(n)
    """
    n = len(nums)
    visited = [False] * n
    queue = deque([0])
    visited[0] = True
    
    while queue:
        pos = queue.popleft()
        
        for jump in range(1, nums[pos] + 1):
            next_pos = (pos + jump) % n  # Wrap around
            
            # Back to start after visiting others
            if next_pos == 0 and any(visited[1:]):
                return True
            
            if not visited[next_pos]:
                visited[next_pos] = True
                queue.append(next_pos)
    
    return False
```

---

## Related Problems

### Direct Variants
1. **Jump Game II (LC 45)** - Minimum jumps to reach end [Medium]
2. **Jump Game III (LC 1306)** - Jump forward/backward [Medium]
3. **Jump Game IV (LC 1345)** - Jump to same value [Hard]
4. **Jump Game V (LC 1340)** - Jump with height restrictions [Hard]
5. **Jump Game VI (LC 1696)** - Maximum score path [Medium]
6. **Jump Game VII (LC 1871)** - Fixed jump length [Medium]

### Similar Patterns (Greedy)
7. **Gas Station (LC 134)** - Circular array greedy [Medium]
8. **Maximum Subarray (LC 53)** - Kadane's algorithm [Easy]
9. **Best Time to Buy and Sell Stock II (LC 122)** - Multiple transactions [Easy]
10. **Container With Most Water (LC 11)** - Two pointer greedy [Medium]

### Similar Patterns (Reachability)
11. **Word Break (LC 139)** - Can reach end of string [Medium]
12. **Minimum Path Sum (LC 64)** - Grid reachability [Medium]
13. **Unique Paths (LC 62)** - Count paths in grid [Medium]

---

## Interview Tips

### Key Points to Mention

1. **Pattern Recognition:**
   - "This is a greedy algorithm problem with the farthest reach pattern"
   - "We only need to track the maximum reachable index, not all possible paths"

2. **Time Complexity:**
   - "Optimal solution is O(n) time and O(1) space"
   - "DP solution would be O(n²) time—greedy is better here"

3. **Key Insight:**
   - "At each position, we ask: can we reach here? If yes, update farthest reach"
   - "Early exit when we know we can reach the end"

4. **Edge Cases:**
   - "Handle single element array (already at end)"
   - "Check if current position is reachable before processing"

### Common Interview Questions

**Q1: Why is greedy optimal here?**
- We only care about reachability, not the specific path
- If we can reach position i, we can explore all jumps from i
- Maximum reach tells us everything we need to know
- No benefit to tracking individual paths

**Q2: Can this be solved without greedy?**
- Yes, but less efficiently
- BFS: O(n²) time, O(n) space
- DP: O(n²) time, O(n) space
- DFS: O(n²) time, O(n) space
- Greedy is superior: O(n) time, O(1) space

**Q3: What if we need the actual path?**
- Greedy approach changes slightly
- Track which jump gave us maximum reach
- Reconstruct path backward
- Still O(n) time but O(n) space for path

**Q4: Forward vs backward greedy?**
- Both work and have same complexity
- Forward: track maximum reach from start
- Backward: track leftmost position that can reach goal
- Forward is more intuitive

**Q5: How to handle variations (costs, obstacles)?**
- Greedy may not work for weighted versions
- Obstacles: skip obstacle positions
- Costs/scores: usually need DP
- Bidirectional jumps: BFS better than greedy

**Q6: What's the most common mistake?**
- Forgetting to check if current position is reachable
- Using wrong inequality (>= vs >)
- Trying to find specific path instead of just reachability
- Not handling single element edge case

### Clarifying Questions to Ask

1. **Can the array be empty?** 
   - Usually no (constraints say length >= 1)

2. **Are all values non-negative?**
   - Yes, based on standard problem

3. **What if nums[i] is very large?**
   - Doesn't matter, just means we can jump far
   - Cap at array length for efficiency

4. **Should we return the path or just boolean?**
   - Standard: just boolean
   - Variation: may need path or minimum jumps

5. **Is the input array modifiable?**
   - Usually doesn't matter for greedy solution
   - Important if considering in-place approaches

6. **Time/space constraints?**
   - Standard: expect O(n) time, O(1) space
   - Anything worse suggests wrong approach

---

## Visual Representations

### Forward Greedy Visualization

```
nums = [2, 3, 1, 1, 4]

Step-by-step max reach:

i=0: maxReach=2
[2, 3, 1, 1, 4]
 ^
 └──►──►  (can reach 0,1,2)

i=1: maxReach=4
[2, 3, 1, 1, 4]
    ^
    └──►──►──►──►  (can reach 1,2,3,4)

Result: 4 >= 4 ✓ Can reach end!


nums = [3, 2, 1, 0, 4]

Step-by-step max reach:

i=0: maxReach=3
[3, 2, 1, 0, 4]
 ^
 └──►──►──►  (can reach 0,1,2,3)

i=1: maxReach=3
[3, 2, 1, 0, 4]
    ^
    └──►──►  (can reach 1,2,3)

i=2: maxReach=3
[3, 2, 1, 0, 4]
       ^
       └──►  (can reach 2,3)

i=3: maxReach=3
[3, 2, 1, 0, 4]
          ^
          (can't jump!)

i=4: 4 > 3 ✗
[3, 2, 1, 0, 4]
             ^
             UNREACHABLE!

Result: Cannot reach end!
```

### Backward Greedy Visualization

```
nums = [2, 3, 1, 1, 4]

Work backward from goal:

goal=4 (last index)
[2, 3, 1, 1, 4]
             ◄── Start here

i=3: 3+1=4 >= 4 ✓
     goal=3
[2, 3, 1, 1, 4]
          ◄─┘

i=2: 2+1=3 >= 3 ✓
     goal=2
[2, 3, 1, 1, 4]
       ◄─┘

i=1: 1+3=4 >= 2 ✓
     goal=1
[2, 3, 1, 1, 4]
    ◄─┘

i=0: 0+2=2 >= 1 ✓
     goal=0
[2, 3, 1, 1, 4]
 ◄─┘

goal==0 ✓ Can reach from start!
```

### Gap Detection

```
nums = [1, 0, 1, 0, 1]

Forward pass:

i=0: maxReach=1
[1, 0, 1, 0, 1]
 ^
 └──►  (reach index 1)

i=1: maxReach=1
[1, 0, 1, 0, 1]
    ^
    (stuck! nums[1]=0, can't jump)

i=2: 2 > 1
[1, 0, 1, 0, 1]
       ^
       GAP! Cannot reach!

Visual:
[1, 0, 1, 0, 1]
 └►-► X      (gap at index 2)
```

### Multiple Paths (Both Work)

```
nums = [2, 3, 1, 1, 4]

Possible paths:

Path 1 (2 jumps):
[2, 3, 1, 1, 4]
 └────────────►  (jump 2 from index 0)
    └────►────►  (jump 3 from index 1)
Result: 0 → 1 → 4

Path 2 (3 jumps):
[2, 3, 1, 1, 4]
 └────►  (jump 1 from index 0)
    └────►  (jump 1 from index 1)
       └────►  (jump 1 from index 2)
          └────►  (jump 1 from index 3)
Result: 0 → 1 → 2 → 3 → 4

Greedy only cares: Can we reach? YES!
(Doesn't need to find which path)
```

---

## Pattern Applications

### When to Use Jump Game Pattern

**Recognition Signals:**
1. Array represents maximum steps/jumps from each position
2. Need to determine if end is reachable
3. Have choices at each position (how far to jump)
4. Don't need exact path, just feasibility
5. Greedy "maximum reach" would work

**Problem Types:**
- **Reachability:** Can we reach the end?
- **Optimization:** Minimum jumps to reach end?
- **Counting:** How many positions are reachable?
- **Path finding:** What's one valid path?

### Problem Variations Categorization

```
Jump Game Family:

├── Basic Reachability (This Problem)
│   ├── Forward Greedy: Track max reach
│   ├── Backward Greedy: Track goal position
│   └── Complexity: O(n) time, O(1) space
│
├── Minimum Jumps (Jump Game II)
│   ├── BFS-like approach with levels
│   └── Complexity: O(n) time, O(1) space
│
├── Bidirectional (Jump Game III)
│   ├── Can jump forward or backward
│   └── Need BFS: O(n) time, O(n) space
│
├── Value-based (Jump Game IV)
│   ├── Jump to any position with same value
│   └── Need graph + BFS: O(n) time, O(n) space
│
└── Constrained (Jump Game V, VI, VII)
    ├── Height restrictions / fixed lengths
    └── Usually need DP: O(n²) or O(nk) time
```

---

## Summary

### Algorithm (Forward Greedy)

```
1. Initialize maxReach = 0
2. For each position i:
   a. If i > maxReach: return False (gap!)
   b. Update maxReach = max(maxReach, i + nums[i])
   c. If maxReach >= last: return True (early exit)
3. Return True
```

### Key Takeaways

1. **Greedy is Optimal:** Track maximum reachable index, not all paths
2. **Time: O(n):** Single pass with early exit
3. **Space: O(1):** Only need one variable
4. **Two checks:** Current position reachable? Can reach end?
5. **Edge case:** Single element already at end

### Decision Tree

```
Given Jump Game problem:
│
├─ Do we need minimum jumps?
│  └─ Yes → Use BFS-like approach (Jump Game II)
│  └─ No ↓
│
├─ Can only jump forward?
│  └─ Yes → Use greedy (this problem) ✓
│  └─ No → Use BFS/DFS
│
├─ Are there weights/costs?
│  └─ Yes → Use DP
│  └─ No → Greedy ✓
│
└─ Just reachability?
   └─ Yes → Greedy forward/backward ✓
```

### Complexity Comparison

```
Problem Variant          | Best Approach    | Time    | Space
─────────────────────────┼──────────────────┼─────────┼───────
Can reach end?           | Greedy           | O(n)    | O(1)
Minimum jumps?           | Greedy BFS-like  | O(n)    | O(1)
Jump forward/backward?   | BFS              | O(n)    | O(n)
Jump to same value?      | Graph + BFS      | O(n)    | O(n)
With obstacles?          | Greedy modified  | O(n)    | O(1)
With costs?              | DP               | O(n²)   | O(n)
Return path?             | Greedy + trace   | O(n)    | O(n)
```

### Mental Model

```
Think of it as a "reach wave":

Start: Can reach position 0
Wave expands: From each reachable position, expand reach
Check: Does wave reach the end?

Like water spreading:
- Water starts at position 0
- Flows to all reachable positions
- Track farthest point water reaches
- Question: Does water reach the end?
```

### Practice Strategy

1. **Master Basic Greedy:** Understand why forward tracking works
2. **Implement Backward:** Practice alternative greedy approach
3. **Compare with DP:** See why greedy is better here
4. **Study Variations:** Jump Game II-VII for different scenarios
5. **Edge Cases:** Single element, zeros, large jumps
6. **Optimize:** Add early exits, handle edge cases cleanly

### Interview Ready Checklist

- [ ] Can explain why greedy works (proof)
- [ ] Know O(n) time, O(1) space solution
- [ ] Understand forward vs backward approach
- [ ] Can handle edge cases (single element, zeros)
- [ ] Aware of Jump Game II (minimum jumps variant)
- [ ] Can compare greedy vs DP tradeoffs
- [ ] Ready to discuss time/space complexity
- [ ] Practiced coding without bugs

---

**Last Updated:** 2024
**Problem Difficulty:** Medium  
**Optimal Solution:** Greedy Forward Tracking  
**Time Complexity:** O(n)  
**Space Complexity:** O(1)
