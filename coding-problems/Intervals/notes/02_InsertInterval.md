# Insert Interval

## Problem Link
[LeetCode 57: Insert Interval](https://leetcode.com/problems/insert-interval/)

## Difficulty
Medium

## Problem Description
You are given an array of non-overlapping intervals `intervals` where `intervals[i] = [starti, endi]` represent the start and the end of the `ith` interval and `intervals` is sorted in ascending order by `starti`. You are also given an interval `newInterval = [start, end]` that represents the start and end of another interval.

Insert `newInterval` into `intervals` such that `intervals` is still sorted in ascending order by `starti` and `intervals` still does not have any overlapping intervals (merge overlapping intervals if necessary).

Return `intervals` after the insertion.

### Examples

#### Example 1:
```
Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
Output: [[1,5],[6,9]]
```

#### Example 2:
```
Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
Output: [[1,2],[3,10],[12,16]]
Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
```

#### Example 3:
```
Input: intervals = [], newInterval = [5,7]
Output: [[5,7]]
```

#### Example 4:
```
Input: intervals = [[1,5]], newInterval = [2,3]
Output: [[1,5]]
```

#### Example 5:
```
Input: intervals = [[1,5]], newInterval = [6,8]
Output: [[1,5],[6,8]]
```

### Constraints:
- `0 <= intervals.length <= 10^4`
- `intervals[i].length == 2`
- `0 <= starti <= endi <= 10^5`
- `intervals` is sorted by `starti` in ascending order
- `newInterval.length == 2`
- `0 <= start <= end <= 10^5`

## Pattern Recognition
This is a **Three-Part Linear Scan** problem:
1. **Before**: Add all intervals that end before newInterval starts
2. **Overlapping**: Merge all intervals that overlap with newInterval
3. **After**: Add all remaining intervals

Key insight: Since input is sorted, we can solve in O(n) time without sorting.

## Core Concepts

### 1. Three-Phase Algorithm
The problem naturally divides into three phases:

```
Phase 1: Before newInterval (no overlap possible)
Phase 2: Overlapping with newInterval (merge all)
Phase 3: After newInterval (no overlap possible)
```

Example:
```
intervals = [[1,2], [3,5], [6,7], [8,10], [12,16]]
newInterval = [4,8]

Phase 1 (before):  [1,2]              - ends before 4
Phase 2 (overlap): [3,5],[6,7],[8,10] - overlap with [4,8]
Phase 3 (after):   [12,16]            - starts after 8
```

### 2. Overlap Detection
Two intervals overlap if:
- `interval.start <= newInterval.end` AND
- `interval.end >= newInterval.start`

Simplified (since we process in order):
- Before: `interval.end < newInterval.start`
- After: `interval.start > newInterval.end`
- Overlap: Everything else

### 3. Merging Strategy
When merging overlapping intervals:
```python
merged_start = min(newInterval.start, interval.start)
merged_end = max(newInterval.end, interval.end)
```

Keep updating `newInterval` as we find more overlaps.

## Solutions

### Solution 1: Three-Phase Linear Scan (Optimal)

#### Approach
1. Add all intervals ending before newInterval starts
2. Merge all overlapping intervals with newInterval
3. Add all intervals starting after newInterval ends

#### Implementation
```python
def insert(intervals, newInterval):
    """
    Insert interval with three-phase approach.
    
    Time Complexity: O(n) - single pass through intervals
    Space Complexity: O(n) - for result array
    
    Args:
        intervals: List[List[int]] - sorted non-overlapping intervals
        newInterval: List[int] - interval to insert
    
    Returns:
        List[List[int]] - merged intervals
    """
    result = []
    i = 0
    n = len(intervals)
    
    # Phase 1: Add all intervals before newInterval
    while i < n and intervals[i][1] < newInterval[0]:
        result.append(intervals[i])
        i += 1
    
    # Phase 2: Merge all overlapping intervals
    while i < n and intervals[i][0] <= newInterval[1]:
        # Expand newInterval to include current interval
        newInterval[0] = min(newInterval[0], intervals[i][0])
        newInterval[1] = max(newInterval[1], intervals[i][1])
        i += 1
    
    # Add the merged interval
    result.append(newInterval)
    
    # Phase 3: Add all intervals after newInterval
    while i < n:
        result.append(intervals[i])
        i += 1
    
    return result


def insert_verbose(intervals, newInterval):
    """
    Verbose version with detailed comments.
    """
    result = []
    i = 0
    n = len(intervals)
    start, end = newInterval
    
    # Phase 1: Process intervals that end before newInterval starts
    # Condition: interval.end < newInterval.start (no overlap possible)
    while i < n and intervals[i][1] < start:
        result.append(intervals[i])
        i += 1
    
    # Phase 2: Process overlapping intervals
    # Condition: interval.start <= newInterval.end (overlap exists)
    while i < n and intervals[i][0] <= end:
        # Merge by taking min start and max end
        start = min(start, intervals[i][0])
        end = max(end, intervals[i][1])
        i += 1
    
    # Add merged interval
    result.append([start, end])
    
    # Phase 3: Process intervals that start after newInterval ends
    # These intervals don't overlap with merged interval
    while i < n:
        result.append(intervals[i])
        i += 1
    
    return result
```

#### Step-by-Step Walkthrough
Input: `intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]]`, `newInterval = [4,8]`

```
Initial state:
result = []
i = 0
newInterval = [4,8]

Phase 1: Find intervals before [4,8]
-----------------------------------------
i=0: [1,2], ends at 2
Check: 2 < 4? Yes → Add [1,2]
result = [[1,2]]
i = 1

i=1: [3,5], ends at 5
Check: 5 < 4? No → Exit Phase 1

Phase 2: Merge overlapping intervals
-----------------------------------------
i=1: [3,5]
Check: 3 <= 8? Yes → Overlap!
Merge: start = min(4,3) = 3
       end = max(8,5) = 8
newInterval = [3,8]
i = 2

i=2: [6,7]
Check: 6 <= 8? Yes → Overlap!
Merge: start = min(3,6) = 3
       end = max(8,7) = 8
newInterval = [3,8]
i = 3

i=3: [8,10]
Check: 8 <= 8? Yes → Overlap!
Merge: start = min(3,8) = 3
       end = max(8,10) = 10
newInterval = [3,10]
i = 4

i=4: [12,16]
Check: 12 <= 10? No → Exit Phase 2

Add merged interval: [3,10]
result = [[1,2], [3,10]]

Phase 3: Add remaining intervals
-----------------------------------------
i=4: [12,16]
Add to result
result = [[1,2], [3,10], [12,16]]

Final Result: [[1,2],[3,10],[12,16]]
```

### Solution 2: Using Binary Search for Optimization

#### Approach
Use binary search to find insertion point, then merge.

#### Implementation
```python
def insert_with_binary_search(intervals, newInterval):
    """
    Use binary search to find starting position.
    
    Time Complexity: O(n) - still need to merge and shift
    Space Complexity: O(n)
    
    Note: Binary search helps but doesn't improve overall complexity
    since we still need O(n) for merging.
    """
    if not intervals:
        return [newInterval]
    
    n = len(intervals)
    
    # Binary search for insertion point
    left, right = 0, n - 1
    insert_pos = n
    
    while left <= right:
        mid = (left + right) // 2
        if intervals[mid][0] < newInterval[0]:
            left = mid + 1
        else:
            insert_pos = mid
            right = mid - 1
    
    # Now perform standard merge starting from insert_pos
    result = []
    i = 0
    
    # Add intervals before
    while i < n and intervals[i][1] < newInterval[0]:
        result.append(intervals[i])
        i += 1
    
    # Merge overlapping
    while i < n and intervals[i][0] <= newInterval[1]:
        newInterval[0] = min(newInterval[0], intervals[i][0])
        newInterval[1] = max(newInterval[1], intervals[i][1])
        i += 1
    
    result.append(newInterval)
    
    # Add remaining
    while i < n:
        result.append(intervals[i])
        i += 1
    
    return result
```

### Solution 3: Functional Approach

#### Implementation
```python
def insert_functional(intervals, newInterval):
    """
    Functional programming style solution.
    
    Time Complexity: O(n)
    Space Complexity: O(n)
    """
    def is_before(interval):
        """Check if interval ends before newInterval starts."""
        return interval[1] < newInterval[0]
    
    def is_after(interval):
        """Check if interval starts after newInterval ends."""
        return interval[0] > newInterval[1]
    
    def overlaps(interval):
        """Check if interval overlaps with newInterval."""
        return not (is_before(interval) or is_after(interval))
    
    # Separate intervals into three categories
    before = [iv for iv in intervals if is_before(iv)]
    overlapping = [iv for iv in intervals if overlaps(iv)]
    after = [iv for iv in intervals if is_after(iv)]
    
    # Merge overlapping intervals with newInterval
    if overlapping:
        merged_start = min(newInterval[0], min(iv[0] for iv in overlapping))
        merged_end = max(newInterval[1], max(iv[1] for iv in overlapping))
        merged = [merged_start, merged_end]
    else:
        merged = newInterval
    
    return before + [merged] + after
```

### Solution 4: In-Place Modification (If Allowed)

#### Implementation
```python
def insert_in_place(intervals, newInterval):
    """
    Modify intervals list in-place if allowed.
    
    Time Complexity: O(n)
    Space Complexity: O(1) - excluding result space
    """
    # Find insertion point and merge
    i = 0
    
    # Skip intervals before newInterval
    while i < len(intervals) and intervals[i][1] < newInterval[0]:
        i += 1
    
    # Merge overlapping intervals
    merge_start = i
    start, end = newInterval
    
    while i < len(intervals) and intervals[i][0] <= end:
        start = min(start, intervals[i][0])
        end = max(end, intervals[i][1])
        i += 1
    
    merge_end = i
    
    # Replace merged intervals with single interval
    intervals[merge_start:merge_end] = [[start, end]]
    
    return intervals
```

## Edge Cases

### 1. Empty Intervals List
```python
intervals = []
newInterval = [5,7]
# Output: [[5,7]]
# Simply return newInterval
```

### 2. Insert at Beginning
```python
intervals = [[3,5],[6,9]]
newInterval = [1,2]
# Output: [[1,2],[3,5],[6,9]]
# No overlap, insert at start
```

### 3. Insert at End
```python
intervals = [[1,2],[3,5]]
newInterval = [6,9]
# Output: [[1,2],[3,5],[6,9]]
# No overlap, insert at end
```

### 4. NewInterval Completely Inside Existing
```python
intervals = [[1,5]]
newInterval = [2,3]
# Output: [[1,5]]
# newInterval absorbed by existing interval
```

### 5. NewInterval Covers Multiple Intervals
```python
intervals = [[2,3],[4,5],[6,7]]
newInterval = [1,8]
# Output: [[1,8]]
# All existing intervals absorbed
```

### 6. NewInterval Touches at Boundary
```python
intervals = [[1,3],[6,9]]
newInterval = [3,6]
# Output: [[1,9]]
# Merge all three intervals
```

### 7. NewInterval Between Non-Overlapping
```python
intervals = [[1,2],[5,6]]
newInterval = [3,4]
# Output: [[1,2],[3,4],[5,6]]
# Insert in middle without merging
```

### 8. Single Interval in List
```python
intervals = [[1,5]]
newInterval = [0,0]
# Output: [[0,0],[1,5]]

intervals = [[1,5]]
newInterval = [6,8]
# Output: [[1,5],[6,8]]
```

## Common Mistakes

### 1. Not Handling Empty Input
```python
# WRONG: Assumes intervals is non-empty
def insert_wrong(intervals, newInterval):
    result = []
    # ... process intervals
    # Fails when intervals = []
```

### 2. Incorrect Overlap Condition
```python
# WRONG: Using < instead of <=
while i < n and intervals[i][0] < newInterval[1]:  # Should be <=
    # Misses touching intervals
```

### 3. Forgetting to Add Merged Interval
```python
# WRONG: Forgot to add merged newInterval
def insert_wrong(intervals, newInterval):
    result = []
    # ... add before intervals
    # ... merge overlapping
    # Missing: result.append(newInterval)
    # ... add after intervals
    return result
```

### 4. Modifying newInterval Without Copy
```python
# WRONG: Modifying input parameter
def insert_wrong(intervals, newInterval):
    # Directly modifying newInterval affects caller's data
    newInterval[0] = min(newInterval[0], intervals[i][0])
    # Should work with copy or be careful
```

### 5. Not Handling All Three Phases
```python
# WRONG: Only handling overlap, missing before/after
def insert_wrong(intervals, newInterval):
    result = []
    for interval in intervals:
        if overlaps(interval, newInterval):
            merge()
    # Missing intervals before and after
```

### 6. Wrong Merge Logic
```python
# WRONG: Not taking min/max correctly
newInterval[0] = intervals[i][0]  # Should be min
newInterval[1] = intervals[i][1]  # Should be max
```

## Testing

### Test Cases
```python
def test_insert_interval():
    """Comprehensive test cases."""
    
    # Test 1: Basic overlap
    assert insert([[1,3],[6,9]], [2,5]) == [[1,5],[6,9]]
    
    # Test 2: Multiple overlaps
    assert insert([[1,2],[3,5],[6,7],[8,10],[12,16]], [4,8]) == \
           [[1,2],[3,10],[12,16]]
    
    # Test 3: Empty intervals
    assert insert([], [5,7]) == [[5,7]]
    
    # Test 4: NewInterval inside existing
    assert insert([[1,5]], [2,3]) == [[1,5]]
    
    # Test 5: NewInterval covers all
    assert insert([[2,3],[4,5],[6,7]], [1,8]) == [[1,8]]
    
    # Test 6: No overlap, insert at beginning
    assert insert([[3,5],[6,9]], [1,2]) == [[1,2],[3,5],[6,9]]
    
    # Test 7: No overlap, insert at end
    assert insert([[1,2],[3,5]], [6,9]) == [[1,2],[3,5],[6,9]]
    
    # Test 8: No overlap, insert in middle
    assert insert([[1,2],[5,6]], [3,4]) == [[1,2],[3,4],[5,6]]
    
    # Test 9: Touch at boundaries
    assert insert([[1,3],[6,9]], [3,6]) == [[1,9]]
    
    # Test 10: Single interval
    assert insert([[1,5]], [0,0]) == [[0,0],[1,5]]
    assert insert([[1,5]], [6,8]) == [[1,5],[6,8]]
    
    print("All test cases passed!")


def test_edge_cases():
    """Test edge cases."""
    
    # All intervals merge into one
    assert insert([[1,2],[3,4],[5,6]], [0,7]) == [[0,7]]
    
    # NewInterval has zero length
    assert insert([[1,3],[6,9]], [4,4]) == [[1,3],[4,4],[6,9]]
    
    # Intervals with zero length
    assert insert([[1,1],[3,3],[5,5]], [2,4]) == [[1,1],[2,4],[5,5]]
    
    # Large numbers
    assert insert([[1,100000]], [50000,75000]) == [[1,100000]]
    
    # Many intervals
    intervals = [[i, i+1] for i in range(0, 100, 2)]
    result = insert(intervals, [10, 20])
    # Should merge [10,11], [12,13], ..., [20,21]
    
    print("All edge case tests passed!")
```

## Complexity Analysis

### Time Complexity: O(n)
- **Phase 1 (Before)**: O(k) where k ≤ n
- **Phase 2 (Merge)**: O(m) where m ≤ n
- **Phase 3 (After)**: O(p) where p ≤ n
- **Total**: O(k + m + p) = O(n)

This is optimal since we must examine each interval at least once.

Breakdown:
```
Best case:  O(1) - newInterval at beginning/end, no merging
Average:    O(n) - need to check all intervals
Worst case: O(n) - all intervals overlap, or newInterval at end
```

### Space Complexity: O(n)
- **Result Array**: O(n) - worst case, no merging
- **Variables**: O(1) - constant space for pointers
- **Overall**: O(n) for output

Best case: O(1) if all intervals merge into one
Worst case: O(n) if newInterval inserted without merging

## Variations and Follow-ups

### Variation 1: Insert Multiple Intervals
**Problem**: Insert multiple new intervals efficiently.

```python
def insert_multiple(intervals, new_intervals):
    """
    Insert multiple intervals.
    
    Time: O(n + m log m) where m is number of new intervals
    Space: O(n + m)
    """
    # Merge new_intervals first
    new_intervals.sort(key=lambda x: x[0])
    merged_new = merge(new_intervals)
    
    # Insert each merged interval
    result = intervals
    for new_interval in merged_new:
        result = insert(result, new_interval)
    
    return result


def insert_multiple_optimal(intervals, new_intervals):
    """
    More optimal: combine all and merge.
    
    Time: O((n+m) log(n+m))
    Space: O(n+m)
    """
    all_intervals = intervals + new_intervals
    return merge(all_intervals)
```

### Variation 2: Find All Overlapping Intervals
**Problem**: Find which existing intervals overlap with newInterval.

```python
def find_overlapping(intervals, newInterval):
    """
    Find indices of intervals that overlap with newInterval.
    
    Time: O(n), Space: O(k) where k is number of overlaps
    """
    overlapping_indices = []
    
    for i, interval in enumerate(intervals):
        # Check overlap
        if not (interval[1] < newInterval[0] or 
                interval[0] > newInterval[1]):
            overlapping_indices.append(i)
    
    return overlapping_indices
```

### Variation 3: Remove Interval
**Problem**: Remove a given interval from the list.

```python
def remove_interval(intervals, to_remove):
    """
    Remove an interval and adjust overlapping ones.
    
    Time: O(n), Space: O(n)
    """
    result = []
    remove_start, remove_end = to_remove
    
    for start, end in intervals:
        # No overlap: keep as is
        if end < remove_start or start > remove_end:
            result.append([start, end])
        # Partial overlap: split if needed
        else:
            if start < remove_start:
                result.append([start, remove_start])
            if end > remove_end:
                result.append([remove_end, end])
    
    return result
```

### Variation 4: Check If Can Insert Without Merging
**Problem**: Check if newInterval can be inserted without merging.

```python
def can_insert_without_merge(intervals, newInterval):
    """
    Check if newInterval can be inserted without merging.
    
    Time: O(log n) using binary search
    Space: O(1)
    """
    if not intervals:
        return True
    
    # Binary search for position
    left, right = 0, len(intervals) - 1
    
    # Check if can be inserted at beginning
    if newInterval[1] < intervals[0][0]:
        return True
    
    # Check if can be inserted at end
    if newInterval[0] > intervals[-1][1]:
        return True
    
    # Binary search for gap
    while left < right - 1:
        mid = (left + right) // 2
        
        # Check if can fit in gap
        if intervals[mid][1] < newInterval[0] and \
           intervals[mid + 1][0] > newInterval[1]:
            return True
        
        if intervals[mid][1] < newInterval[0]:
            left = mid
        else:
            right = mid
    
    # Check the gap between left and right
    if intervals[left][1] < newInterval[0] and \
       intervals[left + 1][0] > newInterval[1]:
        return True
    
    return False
```

### Variation 5: Insert with Priority/Weight
**Problem**: Insert intervals with weights, prefer keeping higher weight.

```python
def insert_with_weight(intervals, newInterval, weights, new_weight):
    """
    Insert interval considering weights.
    
    Time: O(n), Space: O(n)
    """
    result = []
    result_weights = []
    i = 0
    n = len(intervals)
    
    # Add intervals before
    while i < n and intervals[i][1] < newInterval[0]:
        result.append(intervals[i])
        result_weights.append(weights[i])
        i += 1
    
    # Handle overlapping intervals
    current = newInterval[:]
    current_weight = new_weight
    
    while i < n and intervals[i][0] <= current[1]:
        # Keep higher weight interval
        if weights[i] > current_weight:
            # Existing interval has higher priority
            result.append(intervals[i])
            result_weights.append(weights[i])
        else:
            # Merge with current
            current[0] = min(current[0], intervals[i][0])
            current[1] = max(current[1], intervals[i][1])
        i += 1
    
    result.append(current)
    result_weights.append(current_weight)
    
    # Add remaining
    while i < n:
        result.append(intervals[i])
        result_weights.append(weights[i])
        i += 1
    
    return result, result_weights
```

## Related Problems

### Similar LeetCode Problems
1. **Merge Intervals (LC 56)** - Merge all overlapping intervals
2. **Interval List Intersections (LC 986)** - Find intersections
3. **Employee Free Time (LC 759)** - Find common free intervals
4. **Range Module (LC 715)** - Track and query ranges
5. **Data Stream as Disjoint Intervals (LC 352)** - Build intervals from stream
6. **My Calendar I/II/III (LC 729/731/732)** - Booking systems

## Interview Tips

### Key Points to Mention
1. **Three-Phase Approach**: Before, Merge, After
2. **Linear Time**: O(n) because input is sorted
3. **No Extra Sorting**: Leverage sorted input
4. **Overlap Condition**: start <= end
5. **Merge Strategy**: min start, max end

### Common Interview Questions

**Q1: Why is this O(n) while Merge Intervals is O(n log n)?**
- Input is already sorted, no sorting needed
- Just one linear pass through intervals

**Q2: How would you handle unsorted intervals?**
- Sort first: O(n log n)
- Then apply same algorithm
- Or use merge intervals approach

**Q3: Can we use binary search to improve complexity?**
- Can find insertion point in O(log n)
- But still need O(n) to merge and build result
- Overall remains O(n)

**Q4: What if we need to insert many intervals?**
- Option 1: Insert one by one - O(n * m)
- Option 2: Combine all and merge - O((n+m) log(n+m))
- Option 2 is better for large m

**Q5: How to handle interval objects instead of arrays?**
```python
class Interval:
    def __init__(self, start, end):
        self.start = start
        self.end = end

# Access using: interval.start, interval.end
# Same algorithm applies
```

### Clarifying Questions to Ask
1. Are intervals guaranteed to be sorted?
2. Can intervals have negative values?
3. Should we modify input or create new array?
4. Are intervals guaranteed to be non-overlapping initially?
5. How to handle invalid intervals (start > end)?
6. Can newInterval be None or empty?

## Visual Representation

### Three-Phase Process
```
Input intervals: [1,2] [3,5] [6,7] [8,10] [12,16]
New interval:                 [4,8]

Phase 1 - Before (end < 4):
[1,2] ✓ Add to result

Phase 2 - Overlap (start <= 8):
[3,5]   ✓ Merge: [3,8]
[6,7]   ✓ Merge: [3,8]
[8,10]  ✓ Merge: [3,10]

Phase 3 - After (start > 10):
[12,16] ✓ Add to result

Result: [1,2] [3,10] [12,16]
```

### Overlap Scenarios
```
Scenario 1: No overlap (before)
[1,2]    [newInterval: 5,7]
Result: [1,2] [5,7]

Scenario 2: No overlap (after)
[newInterval: 1,2]    [5,7]
Result: [1,2] [5,7]

Scenario 3: Partial overlap (left)
[1,5]
   [newInterval: 3,7]
Result: [1,7]

Scenario 4: Partial overlap (right)
        [5,9]
[newInterval: 1,6]
Result: [1,9]

Scenario 5: Complete overlap (newInterval inside)
[1,10]
  [newInterval: 3,7]
Result: [1,10]

Scenario 6: Complete overlap (interval inside)
[newInterval: 1,10]
  [3,7]
Result: [1,10]
```

## Pattern Applications

### When to Use Insert Interval Pattern
1. **Booking systems**: Add reservation to calendar
2. **Task scheduling**: Insert new task into schedule
3. **Resource allocation**: Add new resource usage
4. **Time management**: Add event to timeline

### Recognition Signals
- Sorted non-overlapping intervals given
- Need to insert one new interval
- Must maintain sorted and non-overlapping property
- Three distinct phases in processing

## Summary

### Algorithm Steps
1. Add intervals ending before newInterval (no overlap)
2. Merge all intervals overlapping with newInterval
3. Add intervals starting after merged interval
4. Return result

### Key Takeaways
- **Linear time O(n)**: No sorting needed
- **Three phases**: Before, merge, after
- **Overlap check**: `start <= end`
- **Merge strategy**: min start, max end
- **Optimal complexity**: Can't do better than O(n)

### Comparison with Merge Intervals
| Aspect | Merge Intervals | Insert Interval |
|--------|----------------|-----------------|
| Input | Unsorted, overlapping | Sorted, non-overlapping |
| Time | O(n log n) | O(n) |
| Sorting | Required | Not needed |
| Approach | Sort then merge | Three-phase scan |

### Practice Strategy
1. Master the three-phase algorithm
2. Handle all edge cases (empty, single, boundaries)
3. Practice overlap detection logic
4. Try variations (multiple inserts, weighted)
5. Compare with merge intervals approach
