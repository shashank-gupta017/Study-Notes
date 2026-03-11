# Meeting Rooms

## Problem Link
[LeetCode 252: Meeting Rooms](https://leetcode.com/problems/meeting-rooms/) (Premium)

## Difficulty
Easy

## Problem Description
Given an array of meeting time intervals where `intervals[i] = [starti, endi]`, determine if a person could attend all meetings.

### Examples

#### Example 1:
```
Input: intervals = [[0,30],[5,10],[15,20]]
Output: false
Explanation: Person cannot attend [0,30] and [5,10] at the same time.
```

#### Example 2:
```
Input: intervals = [[7,10],[2,4]]
Output: true
Explanation: No overlapping meetings.
```

#### Example 3:
```
Input: intervals = [[1,5],[5,8]]
Output: true
Explanation: Meetings touch but don't overlap (5 is end of first, start of second).
```

#### Example 4:
```
Input: intervals = []
Output: true
Explanation: No meetings, person is free.
```

### Constraints:
- `0 <= intervals.length <= 10^4`
- `intervals[i].length == 2`
- `0 <= starti < endi <= 10^6`

## Pattern Recognition
This is an **Interval Overlap Detection** problem:
1. **Sort by start time**: Process meetings chronologically
2. **Check consecutive pairs**: If sorted, overlaps only occur between adjacent meetings
3. **Simple comparison**: Compare end of one with start of next

Key insight: After sorting, need only check if any consecutive meetings overlap.

## Core Concepts

### 1. Overlap Definition
Two intervals `[a,b]` and `[c,d]` overlap if:
- After sorting by start: `c < b`
- This means second meeting starts before first ends

Important: Touching intervals don't overlap
- `[1,5]` and `[5,8]` → No overlap (5 == 5)
- `[1,5]` and `[4,8]` → Overlap (4 < 5)

### 2. Why Sorting Works
```
Unsorted: Need to check all pairs → O(n²)
Sorted: Only check consecutive → O(n)

After sorting by start time:
- If interval i overlaps with j (j > i+1)
- Then interval i also overlaps with i+1
- So checking consecutive pairs is sufficient
```

### 3. Edge Cases to Consider
```
Empty list → True (no meetings)
Single meeting → True (can attend)
Same start time → False (immediate overlap)
Touching boundaries → True (no overlap)
```

## Solutions

### Solution 1: Sort and Check (Optimal)

#### Approach
1. Sort intervals by start time
2. Check each consecutive pair for overlap
3. Return false if any overlap found

#### Implementation
```python
def canAttendMeetings(intervals):
    """
    Check if person can attend all meetings.
    
    Time Complexity: O(n log n) - dominated by sorting
    Space Complexity: O(1) - excluding sorting space
    
    Args:
        intervals: List[List[int]] - meeting time intervals
    
    Returns:
        bool - True if can attend all meetings
    """
    if not intervals or len(intervals) <= 1:
        return True
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    
    # Check consecutive pairs
    for i in range(1, len(intervals)):
        # If current starts before previous ends → overlap
        if intervals[i][0] < intervals[i-1][1]:
            return False
    
    return True


def canAttendMeetings_verbose(intervals):
    """
    Verbose version with detailed comments.
    """
    # Edge case: 0 or 1 meeting
    if len(intervals) <= 1:
        return True
    
    # Sort meetings by start time
    intervals.sort(key=lambda x: x[0])
    
    # Check each adjacent pair
    for i in range(len(intervals) - 1):
        current_end = intervals[i][1]
        next_start = intervals[i + 1][0]
        
        # Overlap if next starts before current ends
        if next_start < current_end:
            return False
    
    return True


def canAttendMeetings_explicit(intervals):
    """
    More explicit version showing all checks.
    """
    if not intervals:
        return True
    
    if len(intervals) == 1:
        return True
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    
    # Check all consecutive pairs
    for i in range(1, len(intervals)):
        prev_start, prev_end = intervals[i-1]
        curr_start, curr_end = intervals[i]
        
        # Check for overlap
        # Overlap condition: curr_start < prev_end
        if curr_start < prev_end:
            print(f"Overlap found: [{prev_start},{prev_end}] and [{curr_start},{curr_end}]")
            return False
    
    return True
```

#### Step-by-Step Walkthrough
Input: `intervals = [[0,30],[5,10],[15,20]]`

```
Step 1: Sort by start time
Original: [[0,30],[5,10],[15,20]]
Sorted:   [[0,30],[5,10],[15,20]]
(Already sorted)

Step 2: Check pair 1: [0,30] and [5,10]
- prev_end = 30
- curr_start = 5
- Check: 5 < 30? Yes → OVERLAP FOUND!
- Return False

Result: false (cannot attend all meetings)
```

Example 2: `intervals = [[7,10],[2,4]]`
```
Step 1: Sort by start time
Original: [[7,10],[2,4]]
Sorted:   [[2,4],[7,10]]

Step 2: Check pair 1: [2,4] and [7,10]
- prev_end = 4
- curr_start = 7
- Check: 7 < 4? No → No overlap
- Continue

Step 3: All pairs checked
Result: true (can attend all meetings)
```

### Solution 2: Brute Force (For Comparison)

#### Approach
Check all pairs of intervals for overlap.

#### Implementation
```python
def canAttendMeetings_bruteforce(intervals):
    """
    Brute force: check all pairs.
    
    Time Complexity: O(n²)
    Space Complexity: O(1)
    
    Not recommended but shows the naive approach.
    """
    n = len(intervals)
    
    # Check every pair
    for i in range(n):
        for j in range(i + 1, n):
            # Check if intervals[i] and intervals[j] overlap
            start1, end1 = intervals[i]
            start2, end2 = intervals[j]
            
            # Two intervals overlap if:
            # start1 < end2 AND start2 < end1
            if start1 < end2 and start2 < end1:
                return False
    
    return True


def intervals_overlap(interval1, interval2):
    """
    Helper function to check if two intervals overlap.
    """
    start1, end1 = interval1
    start2, end2 = interval2
    
    # Overlap if one starts before the other ends
    return start1 < end2 and start2 < end1
```

### Solution 3: Using Min Heap (Over-engineered)

#### Implementation
```python
import heapq

def canAttendMeetings_heap(intervals):
    """
    Using min heap (overkill for this problem).
    
    Time Complexity: O(n log n)
    Space Complexity: O(n)
    """
    if not intervals:
        return True
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    
    # Min heap of end times
    heap = []
    
    for start, end in intervals:
        # If heap not empty and earliest ending meeting hasn't finished
        if heap and heap[0] > start:
            return False
        
        # Add current meeting's end time
        heapq.heappush(heap, end)
    
    return True
```

### Solution 4: Sweep Line Algorithm

#### Implementation
```python
def canAttendMeetings_sweepline(intervals):
    """
    Sweep line approach (more useful for Meeting Rooms II).
    
    Time Complexity: O(n log n)
    Space Complexity: O(n)
    """
    if not intervals:
        return True
    
    # Create events: +1 for start, -1 for end
    events = []
    for start, end in intervals:
        events.append((start, 1))   # Meeting starts
        events.append((end, -1))    # Meeting ends
    
    # Sort events
    events.sort()
    
    # Track concurrent meetings
    concurrent = 0
    
    for time, delta in events:
        concurrent += delta
        
        # If more than 1 meeting at any time
        if concurrent > 1:
            return False
    
    return True
```

## Edge Cases

### 1. Empty Intervals
```python
intervals = []
# Output: True
# No meetings to conflict
```

### 2. Single Meeting
```python
intervals = [[1,5]]
# Output: True
# Only one meeting, no conflicts possible
```

### 3. Two Non-Overlapping
```python
intervals = [[1,2],[3,4]]
# Output: True
# No overlap
```

### 4. Two Overlapping
```python
intervals = [[1,3],[2,4]]
# Output: False
# Overlap: 2 < 3
```

### 5. Touching Meetings
```python
intervals = [[1,5],[5,8]]
# Output: True
# No overlap: 5 == 5 (one ends as other starts)
```

### 6. Same Start Time
```python
intervals = [[1,4],[1,5]]
# Output: False
# Both start at 1, immediate overlap
```

### 7. One Inside Another
```python
intervals = [[1,10],[3,5]]
# Output: False
# [3,5] completely inside [1,10]
```

### 8. Multiple Meetings
```python
intervals = [[1,2],[2,3],[3,4],[4,5]]
# Output: True
# Chain of non-overlapping meetings
```

## Common Mistakes

### 1. Using Wrong Overlap Condition
```python
# WRONG: Using <= instead of <
if intervals[i][0] <= intervals[i-1][1]:  # Should be <
    # Would incorrectly flag touching intervals as overlapping
    return False

# Example: [1,5] and [5,8] would incorrectly return False
```

### 2. Not Sorting First
```python
# WRONG: Checking without sorting
def wrong_solution(intervals):
    for i in range(1, len(intervals)):
        if intervals[i][0] < intervals[i-1][1]:
            return False
    return True

# Fails on: [[7,10],[2,4]] → would return True incorrectly
```

### 3. Checking All Pairs When Not Needed
```python
# WRONG: O(n²) when O(n log n) is sufficient
def inefficient_solution(intervals):
    for i in range(len(intervals)):
        for j in range(i+1, len(intervals)):
            if overlaps(intervals[i], intervals[j]):
                return False
    return True

# Works but unnecessarily slow
```

### 4. Not Handling Empty Input
```python
# WRONG: Crashes on empty input
def wrong_solution(intervals):
    intervals.sort(key=lambda x: x[0])
    for i in range(1, len(intervals)):  # OK, handles empty
        if intervals[i][0] < intervals[i-1][1]:
            return False
    return True

# Actually this is OK, but good to be explicit
```

### 5. Comparing Wrong Intervals
```python
# WRONG: Comparing with first instead of previous
def wrong_solution(intervals):
    intervals.sort(key=lambda x: x[0])
    first_end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        if intervals[i][0] < first_end:  # Should compare with previous!
            return False
    return True
```

## Testing

### Test Cases
```python
def test_can_attend_meetings():
    """Comprehensive test cases."""
    
    # Test 1: Overlapping meetings
    assert canAttendMeetings([[0,30],[5,10],[15,20]]) == False
    
    # Test 2: Non-overlapping
    assert canAttendMeetings([[7,10],[2,4]]) == True
    
    # Test 3: Touching meetings
    assert canAttendMeetings([[1,5],[5,8]]) == True
    
    # Test 4: Empty input
    assert canAttendMeetings([]) == True
    
    # Test 5: Single meeting
    assert canAttendMeetings([[1,5]]) == True
    
    # Test 6: Same start time
    assert canAttendMeetings([[1,4],[1,5]]) == False
    
    # Test 7: Chain of meetings
    assert canAttendMeetings([[1,2],[2,3],[3,4]]) == True
    
    # Test 8: One inside another
    assert canAttendMeetings([[1,10],[3,5]]) == False
    
    # Test 9: All at same time
    assert canAttendMeetings([[1,3],[1,3],[1,3]]) == False
    
    # Test 10: Reverse sorted input
    assert canAttendMeetings([[9,10],[7,8],[5,6]]) == True
    
    print("All test cases passed!")


def test_edge_cases():
    """Test edge cases."""
    
    # Two intervals, no overlap
    assert canAttendMeetings([[1,2],[3,4]]) == True
    
    # Two intervals, overlap
    assert canAttendMeetings([[1,3],[2,4]]) == False
    
    # Multiple same intervals
    assert canAttendMeetings([[5,10],[5,10]]) == False
    
    # Large time values
    assert canAttendMeetings([[0,1000000],[500000,999999]]) == False
    
    # Many non-overlapping
    intervals = [[i*10, i*10+5] for i in range(100)]
    assert canAttendMeetings(intervals) == True
    
    # Many overlapping
    intervals = [[i, i+10] for i in range(100)]
    assert canAttendMeetings(intervals) == False
    
    print("All edge case tests passed!")


def test_boundary_conditions():
    """Test boundary conditions."""
    
    # Exactly touching at boundary
    assert canAttendMeetings([[1,5],[5,10],[10,15]]) == True
    
    # Off by one overlap
    assert canAttendMeetings([[1,5],[4,10]]) == False
    
    # Zero-length meeting
    assert canAttendMeetings([[5,5],[5,10]]) == True
    
    print("All boundary tests passed!")
```

## Complexity Analysis

### Time Complexity: O(n log n)
- **Sorting**: O(n log n)
- **Checking pairs**: O(n)
- **Overall**: O(n log n) - dominated by sorting

Breakdown:
```
Best case:  O(n log n) - must sort regardless
Average:    O(n log n) - same
Worst case: O(n log n) - same
```

### Space Complexity: O(1)
- **Auxiliary space**: O(1) - only need constant variables
- **Sorting space**: O(log n) - recursive stack for in-place sort
- **Overall**: O(1) excluding sorting

Note: Some languages/implementations may use O(n) for sorting

## Variations and Follow-ups

### Variation 1: Return Conflicting Meetings
```python
def find_conflicting_meetings(intervals):
    """
    Return pairs of conflicting meetings.
    
    Time: O(n log n), Space: O(k) where k is conflicts
    """
    if not intervals:
        return []
    
    intervals.sort(key=lambda x: x[0])
    conflicts = []
    
    for i in range(1, len(intervals)):
        if intervals[i][0] < intervals[i-1][1]:
            conflicts.append((intervals[i-1], intervals[i]))
    
    return conflicts
```

### Variation 2: Count Maximum Overlapping
```python
def max_overlapping_meetings(intervals):
    """
    Find maximum number of meetings happening at once.
    Related to Meeting Rooms II.
    
    Time: O(n log n), Space: O(n)
    """
    if not intervals:
        return 0
    
    events = []
    for start, end in intervals:
        events.append((start, 1))
        events.append((end, -1))
    
    events.sort()
    
    max_concurrent = 0
    current = 0
    
    for time, delta in events:
        current += delta
        max_concurrent = max(max_concurrent, current)
    
    return max_concurrent
```

### Variation 3: Find Free Time Slots
```python
def find_free_time(intervals, day_start, day_end):
    """
    Find free time slots given meeting schedule.
    
    Time: O(n log n), Space: O(n)
    """
    if not intervals:
        return [[day_start, day_end]]
    
    intervals.sort(key=lambda x: x[0])
    free_slots = []
    
    # Check before first meeting
    if intervals[0][0] > day_start:
        free_slots.append([day_start, intervals[0][0]])
    
    # Check between meetings
    for i in range(1, len(intervals)):
        if intervals[i][0] > intervals[i-1][1]:
            free_slots.append([intervals[i-1][1], intervals[i][0]])
    
    # Check after last meeting
    if intervals[-1][1] < day_end:
        free_slots.append([intervals[-1][1], day_end])
    
    return free_slots
```

### Variation 4: Merge Overlapping Meetings
```python
def merge_overlapping_meetings(intervals):
    """
    Merge overlapping meetings into consolidated schedule.
    
    Time: O(n log n), Space: O(n)
    """
    if not intervals:
        return []
    
    intervals.sort(key=lambda x: x[0])
    merged = [intervals[0]]
    
    for i in range(1, len(intervals)):
        if intervals[i][0] <= merged[-1][1]:
            # Overlap or touch: merge
            merged[-1][1] = max(merged[-1][1], intervals[i][1])
        else:
            # No overlap: add new
            merged.append(intervals[i])
    
    return merged
```

### Variation 5: Can Attend K Meetings
```python
def can_attend_k_meetings(intervals, k):
    """
    Check if person can attend at least k meetings.
    
    Time: O(n log n), Space: O(1)
    """
    if not intervals or k == 0:
        return True
    
    if k > len(intervals):
        return False
    
    intervals.sort(key=lambda x: x[0])
    
    attended = 1
    end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        if intervals[i][0] >= end:
            attended += 1
            end = intervals[i][1]
            
            if attended >= k:
                return True
    
    return attended >= k
```

## Related Problems

### Similar LeetCode Problems
1. **Meeting Rooms II (LC 253)** - Minimum meeting rooms needed
2. **Merge Intervals (LC 56)** - Merge overlapping intervals
3. **Non-overlapping Intervals (LC 435)** - Remove minimum intervals
4. **My Calendar I (LC 729)** - Book events without conflicts
5. **Employee Free Time (LC 759)** - Find common free time
6. **Interval List Intersections (LC 986)** - Find intersections

## Interview Tips

### Key Points to Mention
1. **Sort by start time**: Enables efficient checking
2. **Check consecutive pairs**: Sufficient after sorting
3. **Time complexity**: O(n log n) due to sorting
4. **Overlap condition**: start < prev_end (not <=)
5. **Touching is OK**: Meetings can touch at boundaries

### Common Interview Questions

**Q1: Why is sorting necessary?**
- Without sorting, need to check all pairs: O(n²)
- With sorting, only consecutive pairs: O(n)
- Sorting cost: O(n log n), still better than O(n²)

**Q2: What if intervals are already sorted?**
- Then O(n) time complexity
- No need to sort again
- Just check consecutive pairs

**Q3: How to handle same start times?**
- Both meetings start together → overlap
- Sort handles this naturally
- Any order works since both overlap

**Q4: What about meetings that touch?**
- Touching is not overlapping
- Use `<` not `<=` in comparison
- Example: [1,5] and [5,8] → OK

**Q5: Can we optimize space complexity?**
- Already O(1) auxiliary space
- Sorting space is unavoidable
- Can't do better than this

### Clarifying Questions to Ask
1. Can meetings have same start/end times?
2. Are intervals guaranteed to be valid (start < end)?
3. Can intervals have negative times?
4. Is input array modifiable (for sorting)?
5. What about zero-length meetings ([5,5])?
6. Should touching meetings be considered overlapping?

## Visual Representation

### Overlap vs Non-Overlap
```
Overlap:
[0,30]  ──────────────────────────────
  [5,10]  ────
Result: Cannot attend both ✗

Non-Overlap:
[2,4] ────
          [7,10] ────
Result: Can attend both ✓

Touching (No Overlap):
[1,5] ────
         [5,8] ────
Result: Can attend both ✓
```

### Sorting Importance
```
Before Sort:
[7,10]     ────
[2,4]  ────
Hard to check!

After Sort:
[2,4]  ────
         [7,10] ────
Easy: check consecutive pairs
```

## Pattern Applications

### When to Use Meeting Rooms Pattern
1. **Scheduling**: Can person attend all meetings?
2. **Conflict detection**: Find conflicting events
3. **Availability**: Check if schedule is valid
4. **Calendar systems**: Validate bookings

### Recognition Signals
- Given time intervals
- Need to check for conflicts
- All-or-nothing attendance
- Simple yes/no answer needed

## Summary

### Algorithm Steps
1. Handle edge cases (empty, single meeting)
2. Sort intervals by start time: O(n log n)
3. Check each consecutive pair: O(n)
4. Return false if any overlap found
5. Return true if all checks pass

### Key Takeaways
- **Sort first**: Enables O(n) checking
- **Consecutive pairs**: Only need to check adjacent
- **Overlap condition**: `start < prev_end`
- **Touching OK**: Use `<` not `<=`
- **Optimal**: O(n log n) time, O(1) space

### Comparison with Other Problems
| Problem | Goal | Time | Key Difference |
|---------|------|------|----------------|
| Meeting Rooms | Can attend all? | O(n log n) | Binary answer |
| Meeting Rooms II | Min rooms? | O(n log n) | Count needed |
| Merge Intervals | Merge overlaps | O(n log n) | Combine intervals |
| Non-overlapping | Min removals? | O(n log n) | Optimization |

### Practice Strategy
1. Master the sorting + checking pattern
2. Practice identifying overlaps correctly
3. Handle touching intervals properly
4. Compare with Meeting Rooms II
5. Try related interval problems
