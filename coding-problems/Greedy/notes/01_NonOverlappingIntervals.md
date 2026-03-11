# Non-overlapping Intervals

## Problem Link
[LeetCode 435: Non-overlapping Intervals](https://leetcode.com/problems/non-overlapping-intervals/)

## Difficulty
Medium

## Problem Description
Given an array of intervals `intervals` where `intervals[i] = [starti, endi]`, return the minimum number of intervals you need to remove to make the rest of the intervals non-overlapping.

### Examples

#### Example 1:
```
Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
Output: 1
Explanation: [1,3] can be removed and the rest of the intervals are non-overlapping.
```

#### Example 2:
```
Input: intervals = [[1,2],[1,2],[1,2]]
Output: 2
Explanation: You need to remove two [1,2] to make the rest of the intervals non-overlapping.
```

#### Example 3:
```
Input: intervals = [[1,2],[2,3]]
Output: 0
Explanation: You don't need to remove any of the intervals since they're already non-overlapping.
```

### Constraints:
- `1 <= intervals.length <= 10^5`
- `intervals[i].length == 2`
- `-5 * 10^4 <= starti < endi <= 5 * 10^4`

## Pattern Recognition
This is a classic **Greedy Algorithm** problem, specifically the **Activity Selection Problem**:
1. **Sort by end time**: Choose intervals that finish earliest
2. **Greedy choice**: Keep interval with earliest end, remove others that overlap
3. **Optimal substructure**: Local optimal choices lead to global optimum

Key insight: Always keep the interval that ends earliest among overlapping intervals.

## Core Concepts

### 1. Activity Selection Pattern
The problem is equivalent to:
- **Goal**: Select maximum number of non-overlapping intervals
- **Strategy**: Always choose the interval that ends earliest
- **Result**: Minimum removals = Total intervals - Maximum non-overlapping

Why this works:
- Interval with earliest end leaves most room for future intervals
- Greedy choice is optimal

### 2. Overlap Definition
Two intervals `[a,b]` and `[c,d]` overlap if:
- `c < b` (when sorted by end time)
- Note: Touching intervals `[1,2]` and `[2,3]` don't overlap

### 3. Sorting Strategy
Sort by **end time**, not start time:
```
Wrong (sort by start):
[1,10], [2,3], [4,5]
→ Keep [1,10], remove [2,3] and [4,5] (2 removals)

Correct (sort by end):
[2,3], [4,5], [1,10]
→ Keep [2,3] and [4,5], remove [1,10] (1 removal)
```

## Solutions

### Solution 1: Greedy (Sort by End Time) - Optimal

#### Approach
1. Sort intervals by end time
2. Keep first interval
3. For each subsequent interval:
   - If it doesn't overlap with last kept interval, keep it
   - Otherwise, skip it (implicit removal)
4. Return total - kept

#### Implementation
```python
def eraseOverlapIntervals(intervals):
    """
    Greedy approach: keep intervals with earliest end times.
    
    Time Complexity: O(n log n) - dominated by sorting
    Space Complexity: O(1) - excluding sorting space
    
    Args:
        intervals: List[List[int]] - array of intervals
    
    Returns:
        int - minimum number of intervals to remove
    """
    if not intervals:
        return 0
    
    # Sort by end time
    intervals.sort(key=lambda x: x[1])
    
    # Track count of non-overlapping intervals
    count = 1  # First interval is always kept
    end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        # If current doesn't overlap, keep it
        if intervals[i][0] >= end:
            count += 1
            end = intervals[i][1]
        # If overlaps, skip (implicit removal)
    
    # Minimum removals = total - kept
    return len(intervals) - count


def eraseOverlapIntervals_verbose(intervals):
    """
    Verbose version with detailed comments.
    """
    if not intervals:
        return 0
    
    # Sort by end time (key insight!)
    intervals.sort(key=lambda x: x[1])
    
    # Count maximum non-overlapping intervals we can keep
    non_overlapping_count = 0
    last_end = float('-inf')  # End time of last kept interval
    
    for start, end in intervals:
        # Check if current interval overlaps with last kept
        if start >= last_end:
            # No overlap: keep this interval
            non_overlapping_count += 1
            last_end = end
        # If overlaps: skip (don't update last_end)
    
    # Calculate removals
    removals = len(intervals) - non_overlapping_count
    return removals


def eraseOverlapIntervals_explicit(intervals):
    """
    More explicit version showing kept and removed intervals.
    """
    if not intervals:
        return 0
    
    intervals.sort(key=lambda x: x[1])
    
    kept = [intervals[0]]
    removed = []
    
    for i in range(1, len(intervals)):
        current = intervals[i]
        last_kept = kept[-1]
        
        # Check overlap
        if current[0] >= last_kept[1]:
            # No overlap: keep
            kept.append(current)
        else:
            # Overlap: remove
            removed.append(current)
    
    return len(removed)
```

#### Step-by-Step Walkthrough
Input: `intervals = [[1,2],[2,3],[3,4],[1,3]]`

```
Step 1: Sort by end time
Original: [[1,2],[2,3],[3,4],[1,3]]
Sorted:   [[1,2],[2,3],[1,3],[3,4]]
          (end=2) (end=2) (end=3) (end=4)

Step 2: Initialize
count = 1 (keep first interval)
end = 2 (end time of [1,2])

Step 3: Process [2,3]
- Start = 2, end = 2
- Check: 2 >= 2? Yes (no overlap)
- Keep it: count = 2, end = 3

Step 4: Process [1,3]
- Start = 1, end = 3
- Check: 1 >= 3? No (overlaps!)
- Skip it (implicit removal)

Step 5: Process [3,4]
- Start = 3, end = 4
- Check: 3 >= 3? Yes (no overlap)
- Keep it: count = 3, end = 4

Step 6: Calculate removals
- Total intervals = 4
- Kept intervals = 3
- Removals = 4 - 3 = 1

Result: 1
```

### Solution 2: Sort by Start Time (Alternative)

#### Approach
Sort by start time and track overlaps differently.

#### Implementation
```python
def eraseOverlapIntervals_by_start(intervals):
    """
    Alternative: sort by start time.
    Less intuitive but also works.
    
    Time Complexity: O(n log n)
    Space Complexity: O(1)
    """
    if not intervals:
        return 0
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    
    removals = 0
    end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        start, curr_end = intervals[i]
        
        # Check overlap
        if start < end:
            # Overlap detected
            removals += 1
            # Keep interval with smaller end (greedy choice)
            end = min(end, curr_end)
        else:
            # No overlap
            end = curr_end
    
    return removals
```

### Solution 3: Dynamic Programming (Not Optimal)

#### Approach
DP solution for comparison (not recommended for this problem).

#### Implementation
```python
def eraseOverlapIntervals_dp(intervals):
    """
    DP approach: finds longest increasing subsequence-like solution.
    
    Time Complexity: O(n^2)
    Space Complexity: O(n)
    
    Note: Greedy is better for this problem!
    """
    if not intervals:
        return 0
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    n = len(intervals)
    
    # dp[i] = max non-overlapping intervals ending at i
    dp = [1] * n
    
    for i in range(1, n):
        for j in range(i):
            # If intervals[j] doesn't overlap with intervals[i]
            if intervals[j][1] <= intervals[i][0]:
                dp[i] = max(dp[i], dp[j] + 1)
    
    # Maximum non-overlapping intervals
    max_keep = max(dp)
    return n - max_keep
```

## Edge Cases

### 1. No Intervals
```python
intervals = []
# Output: 0
# Nothing to remove
```

### 2. Single Interval
```python
intervals = [[1,2]]
# Output: 0
# Can't have overlaps with one interval
```

### 3. No Overlaps
```python
intervals = [[1,2],[2,3],[3,4]]
# Output: 0
# Already non-overlapping
```

### 4. All Overlap
```python
intervals = [[1,5],[2,6],[3,7],[4,8]]
# Output: 3
# Keep one, remove three
```

### 5. Identical Intervals
```python
intervals = [[1,2],[1,2],[1,2]]
# Output: 2
# Keep one, remove two
```

### 6. One Inside Another
```python
intervals = [[1,10],[2,3],[4,5]]
# Output: 1
# Remove [1,10], keep [2,3] and [4,5]
```

### 7. Touching Intervals
```python
intervals = [[1,2],[2,3]]
# Output: 0
# Not overlapping (touching is OK)
```

### 8. Negative Numbers
```python
intervals = [[-50,-20],[-30,-10],[-10,5]]
# Output: 1
# Remove one overlapping interval
```

## Common Mistakes

### 1. Sorting by Start Time Without Adjustment
```python
# WRONG: Simply sorting by start and removing
def wrong_solution(intervals):
    intervals.sort(key=lambda x: x[0])
    removals = 0
    end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        if intervals[i][0] < end:
            removals += 1
            # WRONG: Not updating end correctly!
        else:
            end = intervals[i][1]
    
    return removals
```

### 2. Using Wrong Overlap Condition
```python
# WRONG: Using <= instead of <
if intervals[i][0] <= end:  # Should be <
    # Removes non-overlapping touching intervals
```

### 3. Not Considering Greedy Choice
```python
# WRONG: When overlap, not choosing optimal interval
if start < end:
    removals += 1
    end = intervals[i][1]  # Should keep min(end, curr_end)
```

### 4. Counting Kept Instead of Removed
```python
# WRONG: Returning count instead of removals
return count  # Should be len(intervals) - count
```

### 5. Not Handling Empty Input
```python
# WRONG: Assumes non-empty
def wrong_solution(intervals):
    intervals.sort(key=lambda x: x[1])
    end = intervals[0][1]  # Crashes on empty input
```

## Testing

### Test Cases
```python
def test_erase_overlap_intervals():
    """Comprehensive test cases."""
    
    # Test 1: Basic case
    assert eraseOverlapIntervals([[1,2],[2,3],[3,4],[1,3]]) == 1
    
    # Test 2: All identical
    assert eraseOverlapIntervals([[1,2],[1,2],[1,2]]) == 2
    
    # Test 3: No overlaps
    assert eraseOverlapIntervals([[1,2],[2,3]]) == 0
    
    # Test 4: One contains others
    assert eraseOverlapIntervals([[1,10],[2,3],[4,5]]) == 1
    
    # Test 5: Chain of overlaps
    assert eraseOverlapIntervals([[1,5],[2,6],[3,7],[4,8]]) == 3
    
    # Test 6: Single interval
    assert eraseOverlapIntervals([[1,2]]) == 0
    
    # Test 7: Negative numbers
    assert eraseOverlapIntervals([[-50,-20],[-30,-10],[-10,5]]) == 1
    
    # Test 8: Multiple non-overlapping groups
    assert eraseOverlapIntervals([[1,2],[1,2],[5,6],[5,6]]) == 2
    
    # Test 9: Adjacent (touching)
    assert eraseOverlapIntervals([[0,2],[1,3],[2,4],[3,5],[4,6]]) == 2
    
    # Test 10: Reverse order input
    assert eraseOverlapIntervals([[5,6],[3,4],[1,2]]) == 0
    
    print("All test cases passed!")


def test_edge_cases():
    """Test edge cases."""
    
    # Two intervals, complete overlap
    assert eraseOverlapIntervals([[1,5],[2,3]]) == 1
    
    # Many intervals, one removal
    assert eraseOverlapIntervals([[1,2],[2,3],[3,4],[4,5]]) == 0
    
    # All overlap at one point
    assert eraseOverlapIntervals([[1,2],[1,3],[1,4],[1,5]]) == 3
    
    # Large numbers
    assert eraseOverlapIntervals([[0,100000],[50000,100000]]) == 1
    
    print("All edge case tests passed!")
```

## Complexity Analysis

### Time Complexity: O(n log n)
- **Sorting**: O(n log n)
- **Linear scan**: O(n)
- **Overall**: O(n log n) - dominated by sorting

Breakdown:
```
Sort:  O(n log n)  - sort intervals by end time
Scan:  O(n)        - one pass through sorted array
Total: O(n log n)  - sorting is bottleneck
```

### Space Complexity: O(1)
- **Auxiliary space**: O(1) - only need constant variables
- **Sorting space**: O(log n) - recursive stack for in-place sort
- **Overall**: O(1) excluding sorting

Best case: O(1)
Worst case: O(log n) for sorting recursion

## Why Greedy Works

### Proof of Optimality

**Claim**: Sorting by end time and greedily selecting non-overlapping intervals is optimal.

**Proof by Exchange Argument**:

1. Let OPT be an optimal solution
2. Let GREEDY be our greedy solution
3. Suppose first difference at position k:
   - GREEDY chose interval G_k
   - OPT chose interval O_k
   - G_k ends before O_k (by greedy choice)

4. We can replace O_k with G_k in OPT:
   - G_k ends earlier, so won't create new overlaps
   - All intervals after O_k that don't overlap with O_k also won't overlap with G_k
   - Modified OPT is still valid and no worse

5. By induction, GREEDY is optimal

### Intuition
```
Why earliest end time?

Consider: [1,10] vs [2,3]
If we keep [1,10], we block [2,3], [4,5], [6,7], etc.
If we keep [2,3], we can still fit more intervals after

Earliest end time = maximum future opportunity
```

## Variations and Follow-ups

### Variation 1: Return Which Intervals to Remove
```python
def get_intervals_to_remove(intervals):
    """
    Return actual intervals to remove.
    
    Time: O(n log n), Space: O(n)
    """
    if not intervals:
        return []
    
    # Sort with indices
    indexed = [(interval, i) for i, interval in enumerate(intervals)]
    indexed.sort(key=lambda x: x[0][1])
    
    kept_indices = set()
    kept_indices.add(indexed[0][1])
    end = indexed[0][0][1]
    
    for i in range(1, len(indexed)):
        interval, idx = indexed[i]
        if interval[0] >= end:
            kept_indices.add(idx)
            end = interval[1]
    
    # Return intervals not in kept set
    to_remove = []
    for i, interval in enumerate(intervals):
        if i not in kept_indices:
            to_remove.append(interval)
    
    return to_remove
```

### Variation 2: Maximum Non-overlapping Intervals
```python
def max_non_overlapping(intervals):
    """
    Return maximum number of non-overlapping intervals.
    
    Time: O(n log n), Space: O(1)
    """
    if not intervals:
        return 0
    
    intervals.sort(key=lambda x: x[1])
    
    count = 1
    end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        if intervals[i][0] >= end:
            count += 1
            end = intervals[i][1]
    
    return count
```

### Variation 3: Weighted Intervals
```python
def max_weight_non_overlapping(intervals, weights):
    """
    Select non-overlapping intervals with maximum total weight.
    
    Time: O(n^2), Space: O(n)
    Requires DP, greedy doesn't work for weighted version.
    """
    n = len(intervals)
    
    # Sort by end time with weights
    combined = sorted(zip(intervals, weights), key=lambda x: x[0][1])
    intervals = [x[0] for x in combined]
    weights = [x[1] for x in combined]
    
    # dp[i] = max weight using intervals 0..i
    dp = [0] * n
    dp[0] = weights[0]
    
    for i in range(1, n):
        # Option 1: Don't include current
        exclude = dp[i-1]
        
        # Option 2: Include current
        include = weights[i]
        
        # Find latest non-overlapping interval
        for j in range(i-1, -1, -1):
            if intervals[j][1] <= intervals[i][0]:
                include += dp[j]
                break
        
        dp[i] = max(exclude, include)
    
    return dp[-1]
```

### Variation 4: Minimum Platforms/Meeting Rooms
```python
def min_platforms(intervals):
    """
    Find minimum platforms needed for all intervals.
    Related but different problem.
    
    Time: O(n log n), Space: O(n)
    """
    if not intervals:
        return 0
    
    # Separate start and end times
    starts = sorted([interval[0] for interval in intervals])
    ends = sorted([interval[1] for interval in intervals])
    
    platforms = 0
    max_platforms = 0
    i = j = 0
    
    while i < len(starts):
        if starts[i] < ends[j]:
            platforms += 1
            max_platforms = max(max_platforms, platforms)
            i += 1
        else:
            platforms -= 1
            j += 1
    
    return max_platforms
```

### Variation 5: Partition into Non-overlapping Groups
```python
def partition_non_overlapping(intervals):
    """
    Partition intervals into minimum number of non-overlapping groups.
    
    Time: O(n log n), Space: O(n)
    """
    if not intervals:
        return []
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    
    groups = []
    
    for interval in intervals:
        # Try to add to existing group
        placed = False
        for group in groups:
            if interval[0] >= group[-1][1]:
                group.append(interval)
                placed = True
                break
        
        # Create new group if needed
        if not placed:
            groups.append([interval])
    
    return groups
```

## Related Problems

### Similar LeetCode Problems
1. **Meeting Rooms II (LC 253)** - Minimum meeting rooms
2. **Minimum Number of Arrows (LC 452)** - Burst balloons
3. **Video Stitching (LC 1024)** - Stitch video clips
4. **Interval List Intersections (LC 986)** - Find intersections
5. **Maximum Length of Pair Chain (LC 646)** - Similar greedy
6. **Queue Reconstruction by Height (LC 406)** - Greedy sorting

## Interview Tips

### Key Points to Mention
1. **Greedy approach**: Sort by end time
2. **Activity selection**: Classic problem
3. **Time complexity**: O(n log n)
4. **Why end time**: Leaves most room for future
5. **Return total - kept**: Calculate removals

### Common Interview Questions

**Q1: Why sort by end time instead of start time?**
- End time determines how much room left for future intervals
- Interval ending earliest leaves most opportunities
- Example: [1,10] vs [2,3] - keeping [2,3] is better

**Q2: Can we solve without sorting?**
- No, need to process intervals in optimal order
- Unsorted processing can give wrong answer
- Sorting is necessary: O(n log n) is optimal

**Q3: How to handle equal end times?**
- Doesn't matter, both are equivalent choices
- Our algorithm handles it correctly
- Can break ties by start time if desired

**Q4: Is DP better than greedy here?**
- No, greedy is optimal and faster
- DP is O(n²), greedy is O(n log n)
- DP useful for weighted version

**Q5: What if we want to maximize total interval length?**
- Greedy doesn't work for that variant
- Need DP for weighted problems
- This problem: maximize count, not length

### Clarifying Questions to Ask
1. Are intervals guaranteed to be valid (start < end)?
2. Can intervals have negative coordinates?
3. Are touching intervals ([1,2], [2,3]) overlapping?
4. Should we return count or actual intervals?
5. Is input list modifiable?
6. Are there duplicate intervals?

## Visual Representation

### Why End Time Matters
```
Intervals: [1,10], [2,3], [4,5]

Sort by Start:
[1,10] ──────────────
  [2,3] ──
     [4,5] ──
Keep [1,10], remove 2 ✗ WRONG

Sort by End:
  [2,3] ──
     [4,5] ──
[1,10] ──────────────
Keep [2,3] and [4,5], remove 1 ✓ CORRECT
```

### Greedy Selection Process
```
Sorted by end: [1,2], [1,3], [2,4], [3,5]

Step 1: Keep [1,2]
[1,2] ──
end = 2

Step 2: Check [1,3]
[1,2] ──
[1,3] ───
1 < 2? Yes, overlaps → Skip

Step 3: Check [2,4]
[1,2] ──
     [2,4] ───
2 >= 2? Yes, no overlap → Keep
end = 4

Step 4: Check [3,5]
     [2,4] ───
       [3,5] ───
3 < 4? Yes, overlaps → Skip

Kept: 2, Removed: 2
```

## Pattern Applications

### When to Use Non-overlapping Intervals Pattern
1. **Scheduling**: Maximize meetings attended
2. **Resource allocation**: Minimize conflicts
3. **Event planning**: Optimize event selection
4. **Interval optimization**: Maximize non-overlapping count

### Recognition Signals
- Given intervals, minimize removals
- Maximize non-overlapping selection
- Activity selection type problem
- Greedy approach applicable

## Summary

### Algorithm Steps
1. Sort intervals by end time: O(n log n)
2. Keep first interval
3. For each interval:
   - If no overlap with last kept: keep it
   - Otherwise: skip (implicit removal)
4. Return total - kept

### Key Takeaways
- **Sort by end time**: Critical for greedy approach
- **Greedy is optimal**: Proven by exchange argument
- **Activity selection**: Classic greedy problem
- **Time: O(n log n)**: Optimal complexity
- **Calculate removals**: Total minus kept

### Practice Strategy
1. Understand why end time sorting works
2. Practice greedy proof techniques
3. Compare with start time sorting
4. Try weighted interval variations
5. Study related scheduling problems
