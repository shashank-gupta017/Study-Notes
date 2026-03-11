# Jump Game II

## Problem Link
[LeetCode 45: Jump Game II](https://leetcode.com/problems/jump-game-ii/)

## Difficulty
Medium

## Problem Description
You are given a 0-indexed array of integers `nums` of length `n`. You are initially positioned at `nums[0]`.

Each element `nums[i]` represents the maximum length of a forward jump from index `i`. In other words, if you are at `nums[i]`, you can jump to any `nums[i + j]` where:
- `0 <= j <= nums[i]`
- `i + j < n`

Return the minimum number of jumps to reach `nums[n - 1]`. The test cases are generated such that you can reach `nums[n - 1]`.

### Examples

#### Example 1:
```
Input: nums = [2,3,1,1,4]
Output: 2
Explanation: The minimum number of jumps to reach the last index is 2.
Jump 1 step from index 0 to 1, then 3 steps to the last index.
```

#### Example 2:
```
Input: nums = [2,3,0,1,4]
Output: 2
Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
```

#### Example 3:
```
Input: nums = [1,2,3,4,5]
Output: 3
Explanation: One optimal path is: 0 -> 1 -> 3 -> 5
```

#### Example 4:
```
Input: nums = [10,9,8,7,6,5,4,3,2,1,1,0]
Output: 1
Explanation: Jump from index 0 directly to the last index (10 steps).
```

### Constraints:
- `1 <= nums.length <= 10^4`
- `0 <= nums[i] <= 1000`
- It's guaranteed that you can reach `nums[n-1]`

## Pattern Recognition
This is a **Greedy Algorithm** problem with a **BFS-like level traversal** pattern:

1. **Level-by-level exploration**: Treat each jump as a "level" in BFS
2. **Greedy range tracking**: Track current reach and next reach boundaries
3. **Optimal substructure**: Always expand to the farthest reachable position
4. **Implicit BFS**: No queue needed, use range boundaries instead

**Key Insight**: Think of it as BFS where each "level" represents positions reachable with the same number of jumps.

## Core Concepts

### 1. BFS-like Greedy Approach
The optimal solution treats the problem as **implicit BFS**:

```
nums = [2, 3, 1, 1, 4]
         0  1  2  3  4

Level 0 (0 jumps): [0]
  → From index 0, can reach indices 1-2

Level 1 (1 jump): [1, 2]
  → From index 1, can reach indices 2-4
  → From index 2, can reach index 3
  → Maximum reach after 1 jump: index 4

Level 2 (2 jumps): [3, 4]
  → Reached the end!

Answer: 2 jumps
```

### 2. Range Tracking Strategy
Instead of maintaining a queue, track two boundaries:
- **Current Range End**: The farthest index reachable with current number of jumps
- **Next Range End**: The farthest index reachable with one more jump

```
currentEnd = boundary of current level
nextEnd = farthest we can reach from current level
jumps = number of levels/jumps taken
```

### 3. Why Greedy Works
**Greedy Choice**: Always explore the farthest reachable position
- At each level, we track the maximum reach from all positions in that level
- This ensures we find the minimum jumps
- Proof: If we could reach the end in fewer jumps, we would have already tracked it

### 4. Level Completion
When we reach `currentEnd`, we've finished exploring one level:
- Increment jump counter
- Set `currentEnd = nextEnd` (move to next level)
- Continue until we reach or pass the last index

## Solutions

### Solution 1: Greedy (Level-by-level) - Optimal

#### Approach
1. Track two boundaries: `currentEnd` and `nextEnd`
2. Iterate through array (except last element)
3. Update `nextEnd` with farthest reachable position
4. When reaching `currentEnd`, increment jumps and update level
5. Stop when `currentEnd` reaches or passes last index

#### Implementation
```python
def jump(nums):
    """
    Greedy BFS-like approach with range tracking.
    
    Time Complexity: O(n) - single pass through array
    Space Complexity: O(1) - only a few variables
    
    Args:
        nums: List[int] - array of jump lengths
    
    Returns:
        int - minimum number of jumps to reach last index
    """
    n = len(nums)
    
    # Edge case: already at last index
    if n == 1:
        return 0
    
    jumps = 0
    current_end = 0  # End of current level
    farthest = 0     # Farthest reachable in next level
    
    # Don't need to check last index
    for i in range(n - 1):
        # Update farthest reachable position
        farthest = max(farthest, i + nums[i])
        
        # If we've reached end of current level
        if i == current_end:
            jumps += 1
            current_end = farthest
            
            # Early termination if we can reach the end
            if current_end >= n - 1:
                break
    
    return jumps


# Alternative implementation with clearer variable names
def jump_v2(nums):
    """
    Same algorithm with more descriptive variable names.
    """
    n = len(nums)
    if n == 1:
        return 0
    
    jumps = 0
    current_level_end = 0
    next_level_max_reach = 0
    
    for i in range(n - 1):
        # Explore all positions in current level
        next_level_max_reach = max(next_level_max_reach, i + nums[i])
        
        # Finished exploring current level
        if i == current_level_end:
            jumps += 1
            current_level_end = next_level_max_reach
            
            # Can reach the end
            if current_level_end >= n - 1:
                break
    
    return jumps
```

#### Why We Don't Check Last Index
```python
# We iterate to n-1, not n, because:
# 1. We're already counting jumps that reach/pass last index
# 2. Checking nums[n-1] would be redundant
# 3. When currentEnd >= n-1, we're done

for i in range(n - 1):  # Not range(n)
    # ...
```

### Solution 2: Explicit BFS with Queue

#### Approach
Use actual BFS with a queue to explore each level.

#### Implementation
```python
from collections import deque

def jump_bfs(nums):
    """
    Explicit BFS approach using queue.
    
    Time Complexity: O(n^2) worst case - might revisit positions
    Space Complexity: O(n) - queue storage
    
    Note: Less efficient than greedy approach!
    """
    n = len(nums)
    if n == 1:
        return 0
    
    queue = deque([0])
    visited = {0}
    jumps = 0
    
    while queue:
        # Process entire level
        level_size = len(queue)
        
        for _ in range(level_size):
            pos = queue.popleft()
            
            # Try all possible jumps from current position
            for next_pos in range(pos + 1, min(pos + nums[pos] + 1, n)):
                if next_pos == n - 1:
                    return jumps + 1
                
                if next_pos not in visited:
                    visited.add(next_pos)
                    queue.append(next_pos)
        
        jumps += 1
    
    return jumps


# Optimized BFS: process ranges instead of individual positions
def jump_bfs_optimized(nums):
    """
    BFS with range processing (similar to greedy).
    
    Time Complexity: O(n)
    Space Complexity: O(1)
    """
    n = len(nums)
    if n == 1:
        return 0
    
    jumps = 0
    current_level_start = 0
    current_level_end = 0
    
    while current_level_end < n - 1:
        jumps += 1
        next_level_end = current_level_end
        
        # Process all positions in current level
        for i in range(current_level_start, current_level_end + 1):
            next_level_end = max(next_level_end, i + nums[i])
            
            # Early termination
            if next_level_end >= n - 1:
                return jumps
        
        current_level_start = current_level_end + 1
        current_level_end = next_level_end
    
    return jumps
```

### Solution 3: Dynamic Programming (Bottom-up)

#### Approach
Build up solution from smallest subproblems.

#### Implementation
```python
def jump_dp(nums):
    """
    Dynamic Programming approach.
    
    Time Complexity: O(n^2) - nested loops
    Space Complexity: O(n) - dp array
    
    Note: Not optimal for this problem!
    """
    n = len(nums)
    
    # dp[i] = minimum jumps to reach index i
    dp = [float('inf')] * n
    dp[0] = 0
    
    for i in range(n):
        # Try all possible jumps from position i
        for j in range(1, nums[i] + 1):
            if i + j < n:
                dp[i + j] = min(dp[i + j], dp[i] + 1)
    
    return dp[n - 1]


# Optimized DP: iterate forward
def jump_dp_optimized(nums):
    """
    DP with forward iteration (slightly better).
    
    Time Complexity: O(n * k) where k is average jump length
    Space Complexity: O(n)
    """
    n = len(nums)
    dp = [float('inf')] * n
    dp[0] = 0
    
    for i in range(n):
        if dp[i] == float('inf'):
            continue
        
        # Update all reachable positions
        for j in range(i + 1, min(i + nums[i] + 1, n)):
            dp[j] = min(dp[j], dp[i] + 1)
            
            # Early termination
            if j == n - 1:
                return dp[n - 1]
    
    return dp[n - 1]
```

### Solution 4: Recursive with Memoization

#### Approach
Top-down recursive approach with caching.

#### Implementation
```python
def jump_recursive(nums):
    """
    Recursive approach with memoization.
    
    Time Complexity: O(n^2)
    Space Complexity: O(n) - recursion stack + memo
    """
    n = len(nums)
    memo = {}
    
    def min_jumps(pos):
        # Base case: reached the end
        if pos >= n - 1:
            return 0
        
        # Check memo
        if pos in memo:
            return memo[pos]
        
        # Try all possible jumps
        min_jump = float('inf')
        for jump in range(1, nums[pos] + 1):
            if pos + jump < n:
                min_jump = min(min_jump, 1 + min_jumps(pos + jump))
        
        memo[pos] = min_jump
        return min_jump
    
    return min_jumps(0)
```

## Step-by-Step Walkthrough

### Example: nums = [2, 3, 1, 1, 4]

#### Greedy Approach Walkthrough

```
Initial State:
nums        = [2, 3, 1, 1, 4]
indices     = [0, 1, 2, 3, 4]
jumps       = 0
current_end = 0
farthest    = 0
target      = index 4

───────────────────────────────────────

Iteration 1: i = 0
├─ Calculate reach: i + nums[i] = 0 + 2 = 2
├─ Update farthest: max(0, 2) = 2
├─ Check if at current_end: i == 0? YES
├─ Complete level 0, start level 1:
│  ├─ jumps = 1
│  └─ current_end = 2
└─ State: jumps=1, current_end=2, farthest=2

Level 0: [0] → Can reach indices 1-2

───────────────────────────────────────

Iteration 2: i = 1
├─ Calculate reach: i + nums[i] = 1 + 3 = 4
├─ Update farthest: max(2, 4) = 4
├─ Check if at current_end: i == 2? NO
└─ State: jumps=1, current_end=2, farthest=4

Exploring level 1...

───────────────────────────────────────

Iteration 3: i = 2
├─ Calculate reach: i + nums[i] = 2 + 1 = 3
├─ Update farthest: max(4, 3) = 4
├─ Check if at current_end: i == 2? YES
├─ Complete level 1, start level 2:
│  ├─ jumps = 2
│  └─ current_end = 4
├─ Check if reached end: current_end >= 4? YES
└─ BREAK (reached target)

Level 1: [1, 2] → Can reach indices 2-4

───────────────────────────────────────

Result: 2 jumps
Path: 0 → 1 → 4
```

### Detailed Level Analysis

```
Level 0 (0 jumps):
Position: 0
Value: 2
Range: indices 0-0
Next reach: 0 + 2 = 2 (indices 1, 2)

Level 1 (1 jump):
Positions: 1, 2
Values: 3, 1
Range: indices 1-2
Next reach:
  - From position 1: 1 + 3 = 4
  - From position 2: 2 + 1 = 3
  - Maximum: 4 (includes target at index 4)

Level 2 (2 jumps):
We can reach index 4 → Done!

Total jumps: 2
```

### Visual Representation

```
Array:  [2,  3,  1,  1,  4]
Index:   0   1   2   3   4

Jump 0: ●━━━━━━━━━━━━┓
        0            ┃
                     ┃
Jump 1: ............●━━━━━━━━━━━━━━━━━━━━━━━━━━┓
                    1                           ┃
                                                ┃
Jump 2: .......................................●
                                               4

Legend:
● = Position
━ = Can reach with one jump
. = Skipped position
```

### Alternative Path Example

```
nums = [2, 3, 1, 1, 4]

Path 1 (optimal): 0 → 1 → 4 (2 jumps)
  0: jump to 1 (1 step)
  1: jump to 4 (3 steps)

Path 2 (optimal): 0 → 2 → 4 (2 jumps)
  0: jump to 2 (2 steps)
  2: jump to 3 (1 step)
  3: jump to 4 (1 step)
  
Wait, that's 3 jumps! Let's recalculate:

Path 2: 0 → 2 → 3 → 4 (3 jumps) ✗ Not optimal

The greedy approach naturally finds the optimal path
by always tracking the farthest reach.
```

## Edge Cases

### 1. Single Element
```python
nums = [0]
# Output: 0
# Already at the target

# Code handles this:
if n == 1:
    return 0
```

### 2. Direct Jump to End
```python
nums = [5, 1, 1, 1, 1]
# Output: 1
# Can jump directly from index 0 to index 4

# Walkthrough:
# i=0: farthest = 0+5 = 5 >= 4, jumps=1, break
```

### 3. Must Jump Every Step
```python
nums = [1, 1, 1, 1, 1]
# Output: 4
# Must make 4 jumps to reach end

# Level 0: [0] → reach 1
# Level 1: [1] → reach 2
# Level 2: [2] → reach 3
# Level 3: [3] → reach 4
# Total: 4 jumps
```

### 4. Decreasing Values
```python
nums = [5, 4, 3, 2, 1]
# Output: 1
# First element can reach the end directly

# farthest = 0 + 5 = 5 >= 4
# Result: 1 jump
```

### 5. Zero in Middle (Still Reachable)
```python
nums = [2, 3, 0, 1, 4]
# Output: 2
# Zero doesn't block us if we jump over it

# Path: 0 → 1 → 4
# We jump over the zero at index 2
```

### 6. Large First Jump
```python
nums = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1, 0]
# Output: 1
# Can reach index 11 (last) from index 0

# 0 + 10 = 10 (can reach indices 1-10)
# Index 11 is reached in next jump from any position 1-10
# Actually: 0 + 10 = 10, so can reach up to index 10
# Need one more jump to reach 11
```

### 7. Two Elements
```python
nums = [1, 1]
# Output: 1
# One jump from index 0 to index 1

nums = [2, 1]
# Output: 1
# Can jump 1 or 2 steps, only need 1
```

## Common Mistakes

### Mistake 1: Including Last Index in Loop
```python
# ❌ WRONG: Including last index
for i in range(n):
    farthest = max(farthest, i + nums[i])
    if i == current_end:
        jumps += 1
        current_end = farthest

# Problem: This adds an extra unnecessary jump
# when i = n-1 and i == current_end

# ✅ CORRECT: Stop before last index
for i in range(n - 1):
    farthest = max(farthest, i + nums[i])
    if i == current_end:
        jumps += 1
        current_end = farthest
```

### Mistake 2: Not Handling Single Element
```python
# ❌ WRONG: No edge case check
def jump(nums):
    jumps = 0
    current_end = 0
    farthest = 0
    
    for i in range(len(nums) - 1):
        # ... logic
    
    return jumps

# For nums = [0], returns 0 (correct by luck)
# But logic is unclear

# ✅ CORRECT: Explicit edge case
def jump(nums):
    if len(nums) == 1:
        return 0
    # ... rest of logic
```

### Mistake 3: Updating current_end Every Iteration
```python
# ❌ WRONG: Updating too frequently
for i in range(n - 1):
    farthest = max(farthest, i + nums[i])
    current_end = farthest  # WRONG!
    jumps += 1

# This counts every step as a jump, not just level transitions

# ✅ CORRECT: Update only at level boundaries
for i in range(n - 1):
    farthest = max(farthest, i + nums[i])
    if i == current_end:  # Level boundary
        jumps += 1
        current_end = farthest
```

### Mistake 4: Not Checking if Reached End
```python
# ❌ WRONG: No early termination
for i in range(n - 1):
    farthest = max(farthest, i + nums[i])
    if i == current_end:
        jumps += 1
        current_end = farthest
    # Continues even if we can reach the end

# ✅ CORRECT: Early termination (optional but efficient)
for i in range(n - 1):
    farthest = max(farthest, i + nums[i])
    if i == current_end:
        jumps += 1
        current_end = farthest
        if current_end >= n - 1:
            break
```

### Mistake 5: Confusing with Jump Game I
```python
# Jump Game I: Can we reach the end? (Boolean)
# Jump Game II: Minimum jumps to reach end (Integer)

# ❌ WRONG: Using Jump Game I logic
def jump(nums):
    max_reach = 0
    for i in range(len(nums)):
        if i > max_reach:
            return False  # Wrong problem!
        max_reach = max(max_reach, i + nums[i])
    return True

# ✅ CORRECT: Count jumps, not just check reachability
```

### Mistake 6: Off-by-One with Farthest Calculation
```python
# ❌ WRONG: Incorrect range
farthest = max(farthest, nums[i])  # Missing index!

# ✅ CORRECT: Add current index to jump length
farthest = max(farthest, i + nums[i])
```

## Test Cases

```python
def test_jump_game_ii():
    """Comprehensive test cases for Jump Game II."""
    
    # Test Case 1: Basic example
    assert jump([2, 3, 1, 1, 4]) == 2
    
    # Test Case 2: Another basic example
    assert jump([2, 3, 0, 1, 4]) == 2
    
    # Test Case 3: Single element
    assert jump([0]) == 0
    
    # Test Case 4: Two elements
    assert jump([1, 1]) == 1
    assert jump([2, 1]) == 1
    
    # Test Case 5: Direct jump to end
    assert jump([5, 1, 1, 1, 1]) == 1
    assert jump([10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1]) == 2
    
    # Test Case 6: Must jump every step
    assert jump([1, 1, 1, 1, 1]) == 4
    
    # Test Case 7: Increasing sequence
    assert jump([1, 2, 3, 4, 5]) == 3
    
    # Test Case 8: Decreasing sequence
    assert jump([5, 4, 3, 2, 1]) == 1
    
    # Test Case 9: Zero in middle (but reachable)
    assert jump([2, 3, 0, 1, 4]) == 2
    
    # Test Case 10: Large jumps
    assert jump([7, 0, 9, 6, 9, 6, 1, 7, 9, 0, 1, 2, 9, 0, 3]) == 2
    
    # Test Case 11: All same values
    assert jump([2, 2, 2, 2, 2]) == 2
    
    # Test Case 12: Alternating pattern
    assert jump([1, 2, 1, 2, 1, 2, 1]) == 4
    
    # Test Case 13: One big jump at end
    assert jump([1, 1, 1, 1, 10]) == 4
    
    # Test Case 14: Multiple optimal paths
    assert jump([2, 3, 1, 1, 4]) == 2  # Both 0→1→4 and 0→2→4 work
    
    # Test Case 15: Maximum length array
    large_array = [1] * 10000
    assert jump(large_array) == 9999
    
    print("All test cases passed!")


def test_edge_cases():
    """Test specific edge cases."""
    
    # Already at destination
    assert jump([0]) == 0
    
    # Minimum array size with jump
    assert jump([1, 0]) == 1
    
    # Can just barely reach end
    assert jump([1, 1, 1, 0]) == 3
    
    # Large first element
    assert jump([100, 1, 1, 1]) == 1
    
    # All zeros except first
    assert jump([3, 0, 0, 0]) == 1
    
    print("All edge case tests passed!")


def test_performance():
    """Test performance on large inputs."""
    import time
    
    # Large array with single steps
    n = 10000
    nums = [1] * n
    
    start = time.time()
    result = jump(nums)
    end = time.time()
    
    assert result == n - 1
    print(f"Large array test passed in {end - start:.4f} seconds")
    
    # Large array with variable jumps
    nums = [i % 10 + 1 for i in range(n)]
    
    start = time.time()
    result = jump(nums)
    end = time.time()
    
    print(f"Variable jumps test passed in {end - start:.4f} seconds")


if __name__ == "__main__":
    test_jump_game_ii()
    test_edge_cases()
    test_performance()
```

## Complexity Analysis

### Greedy Solution (Optimal)

#### Time Complexity: O(n)
- Single pass through array: O(n)
- Constant work per iteration: O(1)
- No nested loops
- Early termination possible but doesn't change worst case

```
Worst case: nums = [1, 1, 1, ..., 1]
- Must visit every element except last
- Each operation is O(1)
- Total: O(n)
```

#### Space Complexity: O(1)
- Only three variables: `jumps`, `current_end`, `farthest`
- No additional data structures
- No recursion stack

### BFS Solution (with Queue)

#### Time Complexity: O(n²)
- In worst case, might add all positions to queue: O(n)
- For each position, might check up to n jumps: O(n)
- Total: O(n²)

```
Worst case: nums = [n, n-1, n-2, ..., 1]
- Each position can reach many others
- Lots of queue operations
```

#### Space Complexity: O(n)
- Queue can hold up to n elements
- Visited set can have up to n elements

### Dynamic Programming Solution

#### Time Complexity: O(n²)
- Outer loop: n iterations
- Inner loop: up to n iterations (for large jump values)
- Total: O(n × max_jump) ≈ O(n²) worst case

#### Space Complexity: O(n)
- DP array of size n

### Comparison Table

| Solution | Time | Space | Optimal? |
|----------|------|-------|----------|
| Greedy | O(n) | O(1) | ✅ Yes |
| BFS (optimized) | O(n) | O(1) | ✅ Yes |
| BFS (queue) | O(n²) | O(n) | ❌ No |
| DP | O(n²) | O(n) | ❌ No |
| Recursive + Memo | O(n²) | O(n) | ❌ No |

## Why Greedy is Optimal

### Proof of Correctness

**Claim**: The greedy algorithm always finds the minimum number of jumps.

**Proof**:
1. **Invariant**: After k jumps, `current_end` represents the farthest position reachable with k jumps.

2. **Greedy Choice**: At each level, we track the farthest position reachable from any position in that level.

3. **Optimal Substructure**: If position p is reachable in k jumps, then:
   - All positions ≤ p are also reachable in ≤ k jumps
   - Any position > current_end requires > k jumps

4. **Contradiction Assumption**: Suppose there's a solution with fewer jumps.
   - Then there exists a level where we could reach farther
   - But we always track the maximum reach at each level
   - Contradiction!

Therefore, the greedy approach is optimal.

## Variations and Follow-ups

### Variation 1: Jump Game I
**Problem**: Can you reach the last index?
**Difference**: Boolean return instead of counting jumps
**Solution**: Track max_reach, return true if max_reach >= n-1

```python
def canJump(nums):
    max_reach = 0
    for i in range(len(nums)):
        if i > max_reach:
            return False
        max_reach = max(max_reach, i + nums[i])
    return True
```

### Variation 2: Return the Actual Path
**Problem**: Return the indices of the optimal path
**Modification**: Track parent pointers or reconstruct path

```python
def jump_with_path(nums):
    n = len(nums)
    if n == 1:
        return 0, [0]
    
    jumps = 0
    current_end = 0
    farthest = 0
    path = [0]  # Start at index 0
    
    for i in range(n - 1):
        farthest = max(farthest, i + nums[i])
        
        if i == current_end:
            jumps += 1
            current_end = farthest
            path.append(current_end)
            
            if current_end >= n - 1:
                path[-1] = n - 1  # Ensure last element is n-1
                break
    
    return jumps, path

# Example: nums = [2,3,1,1,4]
# Returns: (2, [0, 2, 4])
```

### Variation 3: Jump Backward Allowed
**Problem**: Can jump backward and forward
**Solution**: Use BFS with bidirectional search

### Variation 4: Minimum Cost Instead of Jumps
**Problem**: Each jump has a cost, minimize total cost
**Solution**: Dijkstra's algorithm or DP

### Variation 5: Jump Game III
**Problem**: Can jump forward or backward by nums[i] steps
**Solution**: BFS/DFS to find if target value is reachable

```python
def canReach(arr, start):
    """Jump Game III: Can reach any 0 value?"""
    n = len(arr)
    visited = set()
    
    def dfs(pos):
        if pos < 0 or pos >= n or pos in visited:
            return False
        if arr[pos] == 0:
            return True
        
        visited.add(pos)
        return dfs(pos + arr[pos]) or dfs(pos - arr[pos])
    
    return dfs(start)
```

### Variation 6: Maximum Jumps with k Constraint
**Problem**: Maximum jumps allowed is k, can you reach end?
**Solution**: Track levels in BFS, stop at k levels

## Related Problems

1. **Jump Game I** (LeetCode 55) - Boolean version
2. **Jump Game III** (LeetCode 1306) - Bidirectional jumps
3. **Jump Game IV** (LeetCode 1345) - Jump to same value
4. **Jump Game V** (LeetCode 1340) - Jump with maximum score
5. **Minimum Number of Taps** (LeetCode 1326) - Similar interval coverage
6. **Video Stitching** (LeetCode 1024) - Interval merging
7. **Reach a Number** (LeetCode 754) - Jump to target on number line
8. **Frog Jump** (LeetCode 403) - Specific jump rules
9. **Minimum Jumps to Reach Home** (LeetCode 1654) - Constraints on jumps

## Interview Tips

### What Interviewers Look For

1. **Pattern Recognition**
   - Recognize it as BFS/Greedy problem
   - Identify level-by-level structure
   - Understand why greedy works

2. **Optimization Awareness**
   - Know DP solution exists but isn't optimal
   - Explain why O(n) is better than O(n²)
   - Understand space optimization

3. **Edge Case Handling**
   - Single element array
   - Direct jump to end
   - Large arrays

4. **Code Quality**
   - Clean variable names
   - Proper edge case handling
   - No off-by-one errors

### Discussion Points

**Start with clarifying questions:**
- "Can I assume the input is always valid (guaranteed to reach end)?"
- "What's the expected size of the input array?"
- "Should I optimize for time or space, or both?"

**Explain your approach:**
- "This is similar to BFS where each level represents positions reachable with same jumps"
- "Instead of using a queue, I'll track range boundaries"
- "The key insight is greedy choice: always track farthest reach"

**Complexity discussion:**
- "This runs in O(n) time with single pass"
- "Space is O(1) with just a few variables"
- "Alternative DP solution would be O(n²) time and O(n) space"

**Follow-up preparation:**
- Be ready to code the path reconstruction
- Discuss Jump Game I as comparison
- Explain why greedy is optimal

### Common Interview Mistakes to Avoid

1. ❌ Starting with DP solution without considering greedy
2. ❌ Not explaining the BFS connection
3. ❌ Including last index in the loop
4. ❌ Forgetting single element edge case
5. ❌ Not being able to explain why greedy works
6. ❌ Inefficient BFS with actual queue
7. ❌ Confusion between Jump Game I and II

### Time Management

- **2 minutes**: Clarify problem, discuss approach
- **5 minutes**: Write optimal solution
- **2 minutes**: Test with examples
- **1 minute**: Discuss complexity and alternatives

## Visual Representations

### Level-by-level Visualization

```
nums = [2, 3, 1, 1, 4]

                    ┌─ START
                    │
         ┌──────────▼──────────┐
         │  Level 0 (0 jumps)  │
         │      Position 0      │
         │       Value: 2       │
         └──────────┬──────────┘
                    │
         ┌──────────▼──────────┐
         │  Level 1 (1 jump)   │
         │   Positions: 1, 2   │
         │    Values: 3, 1     │
         └──────────┬──────────┘
                    │
         ┌──────────▼──────────┐
         │  Level 2 (2 jumps)  │
         │   Positions: 3, 4   │
         │    Values: 1, 4     │
         └──────────┬──────────┘
                    │
                    ▼
                  END ✓
```

### Range Tracking Visualization

```
nums:    [2,  3,  1,  1,  4]
indices:  0   1   2   3   4

Step 1: i=0, current_end=0, farthest=0
        ●═══════════════════════╗
        ▲                       ║
        current_end             ║ farthest after: 2
                                ▼
                               [0,1,2]

Step 2: i=1, current_end=2, farthest=2
            ●═══════════════════════════════════╗
            ▲                                   ║
            exploring                           ║ farthest: 4
                                                ▼
                                          [0,1,2,3,4]

Step 3: i=2, current_end=2, farthest=4
                ●═══════════════════════════════╗
                ▲ reached current_end           ║
                  JUMP! (jumps=2)               ║
                  new current_end=4 ──────────▶ ║
                                                ▼
                                               [4] ✓ DONE
```

### Jump Paths Comparison

```
Array: [2, 3, 1, 1, 4]

Optimal Path 1:
0 ──(jump 1)──▶ 1 ──(jump 3)──▶ 4
●═══════════════●═══════════════●
Total: 2 jumps

Optimal Path 2:
0 ──(jump 2)──▶ 2 ──(jump 1)──▶ 3 ──(jump 1)──▶ 4
●═══════════════●═══════════════●═══════════════●
Total: 3 jumps (NOT optimal!)

Note: The algorithm finds minimum 2 jumps
by tracking maximum reach at each level.
```

## Summary

### Key Takeaways

1. **Pattern**: Greedy + BFS-like level traversal
2. **Core Idea**: Track range boundaries instead of individual positions
3. **Time Complexity**: O(n) with single pass
4. **Space Complexity**: O(1) with three variables
5. **Why Greedy Works**: Always tracking maximum reach ensures minimum jumps

### Algorithm Essence
```
For each position in current level:
  1. Track farthest reachable position
  2. When level ends, increment jump counter
  3. Move to next level (update boundary)
  4. Stop when boundary reaches or passes end
```

### When to Use This Pattern
- Minimum steps/jumps to reach target
- Level-by-level exploration needed
- Greedy choice leads to optimal solution
- Can track ranges instead of individual elements

### Comparison with Similar Patterns

| Pattern | Use Case | Example |
|---------|----------|---------|
| Jump Game II | Min jumps with variable steps | This problem |
| BFS | Shortest path unweighted | Word Ladder |
| Dijkstra | Shortest path weighted | Network Delay |
| DP | Overlapping subproblems | Coin Change |
| Greedy | Local optimal → Global | Activity Selection |

### Final Checklist for Interviews

✅ Understand BFS connection  
✅ Explain why greedy works  
✅ Handle edge cases (single element)  
✅ Avoid including last index in loop  
✅ Use descriptive variable names  
✅ Discuss time/space complexity  
✅ Know alternative solutions (DP, BFS)  
✅ Ready for follow-ups (path reconstruction)  
✅ Test with multiple examples  
✅ Explain optimization benefits  

### Practice Progression

1. **Beginner**: Implement greedy solution
2. **Intermediate**: Explain why greedy works, handle edge cases
3. **Advanced**: Implement path reconstruction, compare with alternatives
4. **Expert**: Optimize for specific constraints, solve variations

---

**Pattern Category**: Greedy  
**Related Patterns**: BFS, Dynamic Programming, Range Tracking  
**Difficulty**: Medium  
**Time to Master**: 2-3 hours with practice  
**Interview Frequency**: High (FAANG favorite)
