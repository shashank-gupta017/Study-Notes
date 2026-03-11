# Merge Intervals

## Problem Link
[LeetCode 56: Merge Intervals](https://leetcode.com/problems/merge-intervals/)

## Difficulty
Medium

## Problem Description
Given an array of `intervals` where `intervals[i] = [starti, endi]`, merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.

### Examples

#### Example 1:
```
Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
```

#### Example 2:
```
Input: intervals = [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals [1,4] and [4,5] are considered overlapping.
```

#### Example 3:
```
Input: intervals = [[1,4],[0,4]]
Output: [[0,4]]
```

#### Example 4:
```
Input: intervals = [[1,4],[2,3]]
Output: [[1,4]]
```

### Constraints:
- `1 <= intervals.length <= 10^4`
- `intervals[i].length == 2`
- `0 <= starti <= endi <= 10^4`

## Pattern Recognition
This is a classic **Interval Merging** problem that requires:
1. **Sorting**: Sort intervals by start time
2. **Merging Logic**: Check if consecutive intervals overlap
3. **Greedy Approach**: Process intervals in order, merging when possible

Key insight: After sorting by start time, we only need to check consecutive intervals for overlaps.

## Core Concepts

### 1. Interval Overlap Detection
Two intervals `[a, b]` and `[c, d]` overlap if:
- `c <= b` (when intervals are sorted by start time)

Examples:
```
[1,3] and [2,6] overlap because 2 <= 3
[1,4] and [4,5] overlap because 4 <= 4
[1,3] and [5,7] don't overlap because 5 > 3
```

### 2. Merging Strategy
When intervals overlap:
- Keep the minimum start time (already guaranteed by sorting)
- Take the maximum end time

```
merge([1,3], [2,6]) = [1, max(3,6)] = [1,6]
merge([1,5], [2,3]) = [1, max(5,3)] = [1,5]
```

### 3. Why Sorting Works
After sorting by start time:
- All intervals that can overlap with interval `i` must appear after `i`
- We can process intervals linearly, maintaining the current merged interval

## Solutions

### Solution 1: Sort and Merge (Optimal)

#### Approach
1. Sort intervals by start time
2. Initialize result with first interval
3. For each remaining interval:
   - If it overlaps with last merged interval, merge them
   - Otherwise, add it as a new interval

#### Implementation
```python
def merge(intervals):
    """
    Merge overlapping intervals.
    
    Time Complexity: O(n log n) - dominated by sorting
    Space Complexity: O(n) - for result array (O(log n) for sorting)
    
    Args:
        intervals: List[List[int]] - array of intervals
    
    Returns:
        List[List[int]] - merged intervals
    """
    # Edge case: empty input
    if not intervals:
        return []
    
    # Step 1: Sort by start time
    intervals.sort(key=lambda x: x[0])
    
    # Step 2: Initialize result with first interval
    merged = [intervals[0]]
    
    # Step 3: Process remaining intervals
    for current in intervals[1:]:
        last_merged = merged[-1]
        
        # Check if current overlaps with last merged
        if current[0] <= last_merged[1]:
            # Merge by updating end time
            last_merged[1] = max(last_merged[1], current[1])
        else:
            # No overlap, add as new interval
            merged.append(current)
    
    return merged


def merge_verbose(intervals):
    """
    Merge with detailed comments for learning.
    """
    if not intervals:
        return []
    
    # Sort: [[1,3],[2,6],[8,10],[15,18]]
    intervals.sort(key=lambda x: x[0])
    
    # Start with first interval: [[1,3]]
    merged = [intervals[0]]
    
    for i in range(1, len(intervals)):
        current_start, current_end = intervals[i]
        last_start, last_end = merged[-1]
        
        # Check overlap: does current start before last ends?
        if current_start <= last_end:
            # Overlapping: extend the end time
            merged[-1][1] = max(last_end, current_end)
        else:
            # Non-overlapping: add as new interval
            merged.append(intervals[i])
    
    return merged
```

#### Alternative Implementation (More Explicit)
```python
def merge_explicit(intervals):
    """
    More explicit version showing merge logic clearly.
    """
    if not intervals:
        return []
    
    intervals.sort(key=lambda x: x[0])
    result = []
    
    current_interval = intervals[0]
    
    for i in range(1, len(intervals)):
        next_interval = intervals[i]
        
        # Check if they overlap
        if next_interval[0] <= current_interval[1]:
            # Merge: extend current interval
            current_interval[1] = max(current_interval[1], next_interval[1])
        else:
            # No overlap: save current and move to next
            result.append(current_interval)
            current_interval = next_interval
    
    # Don't forget the last interval
    result.append(current_interval)
    
    return result
```

#### Step-by-Step Walkthrough
Input: `[[1,3],[2,6],[8,10],[15,18]]`

```
Step 1: Sort by start time
Already sorted: [[1,3],[2,6],[8,10],[15,18]]

Step 2: Initialize merged with first interval
merged = [[1,3]]

Step 3: Process interval [2,6]
- Current: [2,6]
- Last merged: [1,3]
- Check overlap: 2 <= 3? Yes!
- Merge: [1, max(3,6)] = [1,6]
- merged = [[1,6]]

Step 4: Process interval [8,10]
- Current: [8,10]
- Last merged: [1,6]
- Check overlap: 8 <= 6? No
- Add as new: [[1,6], [8,10]]

Step 5: Process interval [15,18]
- Current: [15,18]
- Last merged: [8,10]
- Check overlap: 15 <= 10? No
- Add as new: [[1,6], [8,10], [15,18]]

Final Result: [[1,6],[8,10],[15,18]]
```

### Solution 2: In-Place Merging

#### Approach
Modify the input array directly to save space.

#### Implementation
```python
def merge_in_place(intervals):
    """
    Merge intervals in-place (modifying input).
    
    Time Complexity: O(n log n)
    Space Complexity: O(1) - excluding sorting space
    """
    if not intervals:
        return []
    
    intervals.sort(key=lambda x: x[0])
    
    # Use write pointer for result
    write_idx = 0
    
    for i in range(1, len(intervals)):
        # Check if current overlaps with interval at write_idx
        if intervals[i][0] <= intervals[write_idx][1]:
            # Merge by extending end
            intervals[write_idx][1] = max(
                intervals[write_idx][1], 
                intervals[i][1]
            )
        else:
            # No overlap: move write pointer and copy
            write_idx += 1
            intervals[write_idx] = intervals[i]
    
    # Return only the merged intervals
    return intervals[:write_idx + 1]
```

### Solution 3: Using Stack

#### Approach
Use a stack to maintain merged intervals.

#### Implementation
```python
def merge_with_stack(intervals):
    """
    Merge using stack data structure.
    
    Time Complexity: O(n log n)
    Space Complexity: O(n)
    """
    if not intervals:
        return []
    
    intervals.sort(key=lambda x: x[0])
    stack = [intervals[0]]
    
    for i in range(1, len(intervals)):
        current = intervals[i]
        top = stack[-1]
        
        if current[0] <= top[1]:
            # Merge: pop and push merged interval
            stack.pop()
            merged = [top[0], max(top[1], current[1])]
            stack.append(merged)
        else:
            # No overlap: push current
            stack.append(current)
    
    return stack
```

## Edge Cases

### 1. Single Interval
```python
intervals = [[1,4]]
# Output: [[1,4]]
# No merging needed
```

### 2. No Overlaps
```python
intervals = [[1,2],[3,4],[5,6]]
# Output: [[1,2],[3,4],[5,6]]
# All intervals remain separate
```

### 3. All Intervals Merge Into One
```python
intervals = [[1,3],[2,4],[3,5],[4,6]]
# Output: [[1,6]]
# All merge into single interval
```

### 4. Intervals Touch at Boundary
```python
intervals = [[1,4],[4,5]]
# Output: [[1,5]]
# Touching intervals (4==4) are merged
```

### 5. One Interval Contains Another
```python
intervals = [[1,5],[2,3]]
# Output: [[1,5]]
# [2,3] is completely inside [1,5]
```

### 6. Unsorted Input
```python
intervals = [[2,6],[1,3],[8,10]]
# After sort: [[1,3],[2,6],[8,10]]
# Output: [[1,6],[8,10]]
```

### 7. Multiple Overlaps
```python
intervals = [[1,4],[2,5],[3,6]]
# Process: [1,4] -> [1,5] -> [1,6]
# Output: [[1,6]]
```

## Common Mistakes

### 1. Forgetting to Sort
```python
# WRONG: Processing unsorted intervals
def merge_wrong(intervals):
    merged = [intervals[0]]
    for interval in intervals[1:]:
        # This won't work correctly for unsorted input
        if interval[0] <= merged[-1][1]:
            merged[-1][1] = max(merged[-1][1], interval[1])
    return merged

# Example: [[2,6],[1,3]] would give [[2,6],[1,3]] instead of [[1,6]]
```

### 2. Not Handling Equal Boundaries
```python
# WRONG: Using < instead of <=
if current[0] < last[1]:  # Should be <=
    # Would miss touching intervals like [1,4],[4,5]
```

### 3. Incorrect Merge Logic
```python
# WRONG: Not taking max of end times
merged[-1][1] = current[1]  # Should be max(last[1], current[1])

# Example: [[1,5],[2,3]] would give [[1,3]] instead of [[1,5]]
```

### 4. Forgetting Last Interval
```python
# WRONG: In some implementations
for i in range(1, len(intervals)):
    if overlaps:
        merge()
    else:
        result.append(current_interval)
        current_interval = intervals[i]
# Missing: result.append(current_interval) after loop
```

### 5. Modifying While Iterating
```python
# WRONG: Modifying list during iteration
for interval in intervals:
    if overlaps:
        intervals.remove(last)  # Bad!
        intervals.append(merged)
```

## Testing

### Test Cases
```python
def test_merge_intervals():
    """Comprehensive test cases."""
    
    # Test 1: Basic overlap
    assert merge([[1,3],[2,6],[8,10],[15,18]]) == [[1,6],[8,10],[15,18]]
    
    # Test 2: Touching intervals
    assert merge([[1,4],[4,5]]) == [[1,5]]
    
    # Test 3: No overlaps
    assert merge([[1,2],[3,4],[5,6]]) == [[1,2],[3,4],[5,6]]
    
    # Test 4: Complete overlap
    assert merge([[1,5],[2,3]]) == [[1,5]]
    
    # Test 5: All merge into one
    assert merge([[1,3],[2,4],[3,5],[4,6]]) == [[1,6]]
    
    # Test 6: Single interval
    assert merge([[1,4]]) == [[1,4]]
    
    # Test 7: Unsorted input
    assert merge([[2,6],[1,3],[8,10],[0,2]]) == [[0,6],[8,10]]
    
    # Test 8: Multiple merges
    assert merge([[1,10],[2,3],[4,5],[6,7],[8,9]]) == [[1,10]]
    
    # Test 9: Large gaps
    assert merge([[0,1],[100,101],[1000,1001]]) == [[0,1],[100,101],[1000,1001]]
    
    # Test 10: Reverse sorted
    assert merge([[8,10],[2,6],[1,3]]) == [[1,6],[8,10]]
    
    print("All test cases passed!")


def test_edge_cases():
    """Test edge cases."""
    
    # Empty input
    assert merge([]) == []
    
    # Same intervals
    assert merge([[1,3],[1,3],[1,3]]) == [[1,3]]
    
    # Zero length intervals
    assert merge([[1,1],[2,2],[3,3]]) == [[1,1],[2,2],[3,3]]
    
    # Large numbers
    assert merge([[0,10000],[5000,15000]]) == [[0,15000]]
    
    print("All edge case tests passed!")
```

## Complexity Analysis

### Time Complexity: O(n log n)
- **Sorting**: O(n log n)
- **Merging**: O(n) - single pass through sorted intervals
- **Overall**: O(n log n) - dominated by sorting

Breakdown:
```
Sort:  O(n log n)  - comparing and arranging intervals
Scan:  O(n)        - one pass through sorted array
Total: O(n log n)  - bottleneck is sorting
```

### Space Complexity: O(n)
- **Result Array**: O(n) - worst case, no merging happens
- **Sorting Space**: O(log n) - recursive stack for quicksort/mergesort
- **Overall**: O(n) for output, O(log n) auxiliary

Best case space: O(1) if all intervals merge into one
Worst case space: O(n) if no intervals merge

## Variations and Follow-ups

### Variation 1: Insert Interval
**Problem**: Insert a new interval and merge if necessary.

```python
def insert(intervals, newInterval):
    """
    Insert and merge a new interval.
    
    Time: O(n), Space: O(n)
    """
    result = []
    i = 0
    n = len(intervals)
    
    # Add all intervals before newInterval
    while i < n and intervals[i][1] < newInterval[0]:
        result.append(intervals[i])
        i += 1
    
    # Merge overlapping intervals
    while i < n and intervals[i][0] <= newInterval[1]:
        newInterval[0] = min(newInterval[0], intervals[i][0])
        newInterval[1] = max(newInterval[1], intervals[i][1])
        i += 1
    
    result.append(newInterval)
    
    # Add remaining intervals
    while i < n:
        result.append(intervals[i])
        i += 1
    
    return result
```

### Variation 2: Count Overlaps
**Problem**: Count how many pairs of intervals overlap.

```python
def count_overlaps(intervals):
    """
    Count number of overlapping pairs.
    
    Time: O(n log n), Space: O(1)
    """
    intervals.sort(key=lambda x: x[0])
    count = 0
    
    for i in range(len(intervals)):
        for j in range(i + 1, len(intervals)):
            if intervals[j][0] <= intervals[i][1]:
                count += 1
            else:
                break  # No more overlaps for intervals[i]
    
    return count
```

### Variation 3: Find Gaps
**Problem**: Find intervals that are not covered.

```python
def find_gaps(intervals, start, end):
    """
    Find uncovered intervals in range [start, end].
    
    Time: O(n log n), Space: O(n)
    """
    intervals.sort(key=lambda x: x[0])
    gaps = []
    current = start
    
    for interval in intervals:
        if interval[0] > current:
            gaps.append([current, interval[0]])
        current = max(current, interval[1])
    
    if current < end:
        gaps.append([current, end])
    
    return gaps
```

### Variation 4: Maximum Overlapping Intervals
**Problem**: Find maximum number of overlapping intervals at any point.

```python
def max_overlapping(intervals):
    """
    Find maximum number of intervals overlapping at any point.
    
    Time: O(n log n), Space: O(n)
    """
    events = []
    for start, end in intervals:
        events.append((start, 1))   # Interval starts
        events.append((end, -1))    # Interval ends
    
    events.sort()
    
    max_overlap = 0
    current_overlap = 0
    
    for time, delta in events:
        current_overlap += delta
        max_overlap = max(max_overlap, current_overlap)
    
    return max_overlap
```

### Variation 5: Merge K Sorted Interval Lists
**Problem**: Merge multiple sorted interval lists.

```python
import heapq

def merge_k_interval_lists(interval_lists):
    """
    Merge K sorted interval lists.
    
    Time: O(n log k), Space: O(k)
    where n is total intervals, k is number of lists
    """
    # Collect all intervals
    all_intervals = []
    for intervals in interval_lists:
        all_intervals.extend(intervals)
    
    # Use standard merge
    return merge(all_intervals)


def merge_k_optimal(interval_lists):
    """
    Optimal version using heap for streaming.
    """
    heap = []
    
    # Initialize heap with first interval from each list
    for i, intervals in enumerate(interval_lists):
        if intervals:
            heapq.heappush(heap, (intervals[0][0], i, 0))
    
    result = []
    
    while heap:
        _, list_idx, interval_idx = heapq.heappop(heap)
        interval = interval_lists[list_idx][interval_idx]
        
        # Merge or add
        if result and interval[0] <= result[-1][1]:
            result[-1][1] = max(result[-1][1], interval[1])
        else:
            result.append(interval[:])
        
        # Add next interval from same list
        if interval_idx + 1 < len(interval_lists[list_idx]):
            next_interval = interval_lists[list_idx][interval_idx + 1]
            heapq.heappush(heap, (next_interval[0], list_idx, interval_idx + 1))
    
    return result
```

## Related Problems

### Similar LeetCode Problems
1. **Insert Interval (LC 57)** - Insert new interval into sorted list
2. **Meeting Rooms (LC 252)** - Check if person can attend all meetings
3. **Meeting Rooms II (LC 253)** - Minimum meeting rooms needed
4. **Non-overlapping Intervals (LC 435)** - Remove minimum intervals
5. **Minimum Number of Arrows (LC 452)** - Burst balloons
6. **Employee Free Time (LC 759)** - Find common free time
7. **Interval List Intersections (LC 986)** - Find intersections

## Optimization Techniques

### 1. Early Termination
```python
def merge_optimized(intervals):
    """
    Optimize with early termination when possible.
    """
    if not intervals or len(intervals) <= 1:
        return intervals
    
    intervals.sort(key=lambda x: x[0])
    merged = [intervals[0]]
    
    for i in range(1, len(intervals)):
        # If remaining intervals can't possibly overlap with current
        # (This doesn't apply to this problem but shows the concept)
        current = intervals[i]
        last = merged[-1]
        
        if current[0] <= last[1]:
            last[1] = max(last[1], current[1])
        else:
            merged.append(current)
    
    return merged
```

### 2. Custom Sorting for Special Cases
```python
def merge_presorted(intervals):
    """
    If intervals are already sorted, skip sorting.
    
    Time: O(n), Space: O(n)
    """
    if not intervals:
        return []
    
    merged = [intervals[0]]
    
    for current in intervals[1:]:
        if current[0] <= merged[-1][1]:
            merged[-1][1] = max(merged[-1][1], current[1])
        else:
            merged.append(current)
    
    return merged
```

### 3. Parallel Processing (Conceptual)
```python
def merge_parallel_concept(intervals):
    """
    Conceptual parallel approach (not practical in Python).
    
    1. Divide intervals into chunks
    2. Merge each chunk in parallel
    3. Merge results from all chunks
    """
    # Sort first
    intervals.sort(key=lambda x: x[0])
    
    # This would require actual parallel processing libraries
    # and would only help with very large datasets
    
    return merge(intervals)
```

## Interview Tips

### Key Points to Mention
1. **Sorting is Essential**: Must sort by start time first
2. **Overlap Condition**: Current start <= Last end
3. **Merge Strategy**: Keep min start, max end
4. **Time Complexity**: O(n log n) due to sorting
5. **Space Complexity**: O(n) for result

### Common Interview Questions
1. **Why sort by start time?**
   - Ensures we process intervals in order
   - All overlapping intervals will be adjacent after sorting

2. **Can we do better than O(n log n)?**
   - No, if intervals are unsorted (comparison-based sorting lower bound)
   - Yes, if intervals are presorted: O(n)

3. **How to handle touching intervals?**
   - Use `<=` instead of `<` for overlap check
   - `[1,4]` and `[4,5]` should merge to `[1,5]`

4. **What if intervals are given as objects?**
   ```python
   class Interval:
       def __init__(self, start, end):
           self.start = start
           self.end = end
   
   # Sort using: intervals.sort(key=lambda x: x.start)
   ```

5. **Can we merge in-place to save space?**
   - Yes, but result size may differ from input
   - Need to keep track of write pointer

### Clarifying Questions to Ask
1. Are intervals always valid (start <= end)?
2. Can intervals have negative numbers?
3. Is the input guaranteed to be non-empty?
4. Should we modify the input array or create new?
5. Are intervals sorted or unsorted?
6. How should we handle duplicate intervals?

## Visual Representation

### Before and After Sorting
```
Before Sort:
[2,6]  ----
            [8,10]  ----
[1,3]  ---
                    [15,18]  ---

After Sort:
[1,3]  ---
[2,6]  ----
            [8,10]  ----
                    [15,18]  ---
```

### Merging Process
```
Step 1: Start with [1,3]
Result: [1,3]

Step 2: Check [2,6]
[1,3]  ---
[2,6]  ----
Overlap! (2 <= 3)
Merge: [1,6]
Result: [1,6]

Step 3: Check [8,10]
[1,6]  ------
              [8,10]  ----
No overlap! (8 > 6)
Add: [8,10]
Result: [1,6], [8,10]

Step 4: Check [15,18]
              [8,10]  ----
                           [15,18]  ---
No overlap! (15 > 10)
Add: [15,18]
Final: [1,6], [8,10], [15,18]
```

## Pattern Applications

### When to Use Interval Merging
1. **Timeline consolidation**: Merging time slots
2. **Resource allocation**: Combining usage periods
3. **Range queries**: Consolidating ranges
4. **Scheduling**: Combining overlapping events

### Recognition Signals
- Input is a list of intervals/ranges
- Need to combine overlapping periods
- Output should be non-overlapping
- Sorting would help organize the data

## Summary

### Algorithm Steps
1. Sort intervals by start time: O(n log n)
2. Initialize result with first interval
3. For each remaining interval:
   - If overlaps: merge by updating end time
   - If not: add as new interval
4. Return merged intervals

### Key Takeaways
- **Sorting first** simplifies the merging logic
- Only need to check **consecutive intervals** after sorting
- **Greedy approach** works: process left to right
- **Time complexity**: O(n log n) - optimal for unsorted input
- **Space complexity**: O(n) - for storing result

### Practice Strategy
1. Master the basic merge algorithm
2. Practice identifying overlap conditions
3. Handle edge cases (touching, contained intervals)
4. Try variations (insert, count, gaps)
5. Optimize for specific scenarios (presorted, streaming)
