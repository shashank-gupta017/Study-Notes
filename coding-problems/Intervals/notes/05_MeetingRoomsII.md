# Meeting Rooms II - LeetCode #253

**Difficulty**: Medium  
**Pattern**: Min Heap / Sweep Line Algorithm  
**Frequency**: Very High (Top Interview Question)

---

## Problem Statement

Given an array of meeting time intervals `intervals` where `intervals[i] = [starti, endi]`, return the **minimum number of conference rooms required**.

### Examples

#### Example 1:
```
Input: intervals = [[0,30],[5,10],[15,20]]
Output: 2
Explanation: 
- At time 0: room 1 takes [0,30]
- At time 5: room 2 takes [5,10] (room 1 occupied)
- At time 15: room 2 takes [15,20] (room 1 still occupied, room 2 free after 10)
Need 2 rooms maximum
```

#### Example 2:
```
Input: intervals = [[7,10],[2,4]]
Output: 1
Explanation: Meetings don't overlap, one room is sufficient
```

#### Example 3:
```
Input: intervals = [[1,5],[2,8],[5,10]]
Output: 2
Explanation:
- Time 1: room 1 gets [1,5]
- Time 2: room 2 gets [2,8] (room 1 occupied)
- Time 5: room 1 gets [5,10] (room 1 just freed, room 2 occupied until 8)
Maximum 2 rooms at same time
```

#### Example 4:
```
Input: intervals = [[1,2],[2,3],[3,4]]
Output: 1
Explanation: Meetings touch but don't overlap, reuse same room
```

### Constraints:
- `1 <= intervals.length <= 10^4`
- `0 <= starti < endi <= 10^6`

---

## Pattern Recognition

This is a **Meeting Scheduling / Concurrent Resource Management** problem:

1. **Key Question**: What's the maximum number of overlapping meetings at any point?
2. **Min Heap Approach**: Track end times of ongoing meetings
3. **Sweep Line Approach**: Process start/end events chronologically
4. **Core Insight**: Rooms needed = maximum concurrent meetings

### When to Use This Pattern
- Scheduling problems (rooms, resources, workers)
- Finding maximum overlap/concurrency
- Resource allocation problems
- Calendar/timeline conflicts

---

## Core Concepts

### 1. Why Min Heap?

**The Problem**: Track when rooms become available.

**The Solution**: Min heap keeps earliest ending meeting at top.

```
Meetings: [[0,30],[5,10],[15,20]]

Time 0: Start [0,30]
  Heap: [30]
  Rooms needed: 1

Time 5: Start [5,10]
  - Check heap top: 30 (meeting still ongoing)
  - Can't reuse room, add new room
  Heap: [10, 30]
  Rooms needed: 2

Time 15: Start [15,20]
  - Check heap top: 10 (meeting ended)
  - Remove 10, reuse that room
  - Add new end time 20
  Heap: [20, 30]
  Rooms needed: 2 (max remains 2)
```

**Key Insight**: Heap size = current active meetings = rooms in use

### 2. Sweep Line Algorithm

**Concept**: Process all events (start/end) in chronological order.

```
Start events: +1 room needed
End events: -1 room needed
Track maximum rooms at any point
```

**Example**: `[[0,30],[5,10],[15,20]]`
```
Events sorted:
(0, START)  → rooms = 1
(5, START)  → rooms = 2  ← Maximum!
(10, END)   → rooms = 1
(15, START) → rooms = 2
(20, END)   → rooms = 1
(30, END)   → rooms = 0

Answer: 2 (maximum observed)
```

### 3. Why Sorting Matters

**Unsorted**: Can't determine temporal order
**Sorted**: Process meetings as they occur in time

```
Critical: When start time == end time
  [1,5] and [5,10]
  
Process order matters:
  END before START: meetings don't overlap (reuse room)
  START before END: would count as overlap (wrong!)
  
Correct: Sort ends BEFORE starts at same time
```

---

## Solution 1: Min Heap Approach (Optimal)

### Algorithm

1. Sort meetings by start time
2. Create min heap to track end times of ongoing meetings
3. For each meeting:
   - Remove all meetings from heap that ended before current starts
   - Add current meeting's end time to heap
   - Track maximum heap size (max concurrent meetings)
4. Return maximum heap size

### Implementation

```python
import heapq

def minMeetingRooms(intervals):
    """
    Find minimum meeting rooms needed using min heap.
    
    Time Complexity: O(n log n) - sorting + heap operations
    Space Complexity: O(n) - heap can contain all meetings
    
    Args:
        intervals: List[List[int]] - meeting time intervals
    
    Returns:
        int - minimum number of rooms required
    """
    if not intervals:
        return 0
    
    # Sort meetings by start time
    intervals.sort(key=lambda x: x[0])
    
    # Min heap to track end times of ongoing meetings
    heap = []
    
    # Add first meeting's end time
    heapq.heappush(heap, intervals[0][1])
    
    # Process remaining meetings
    for i in range(1, len(intervals)):
        start, end = intervals[i]
        
        # If earliest ending meeting finished, reuse that room
        if heap[0] <= start:
            heapq.heappop(heap)
        
        # Add current meeting's end time
        heapq.heappush(heap, end)
    
    # Heap size = rooms needed
    return len(heap)


def minMeetingRooms_tracking_max(intervals):
    """
    Alternative: explicitly track maximum rooms.
    """
    if not intervals:
        return 0
    
    intervals.sort(key=lambda x: x[0])
    
    heap = []
    max_rooms = 0
    
    for start, end in intervals:
        # Remove meetings that ended
        while heap and heap[0] <= start:
            heapq.heappop(heap)
        
        # Add current meeting
        heapq.heappush(heap, end)
        
        # Track maximum
        max_rooms = max(max_rooms, len(heap))
    
    return max_rooms


def minMeetingRooms_verbose(intervals):
    """
    Verbose version with detailed logging.
    """
    if not intervals:
        return 0
    
    # Sort by start time
    intervals.sort(key=lambda x: x[0])
    print(f"Sorted intervals: {intervals}")
    
    # Min heap for end times
    heap = []
    max_rooms = 0
    
    for i, (start, end) in enumerate(intervals):
        print(f"\nProcessing meeting {i+1}: [{start}, {end}]")
        
        # Free up rooms for meetings that ended
        freed = []
        while heap and heap[0] <= start:
            freed.append(heapq.heappop(heap))
        
        if freed:
            print(f"  Freed rooms (meetings ended at: {freed})")
        
        # Allocate room for current meeting
        heapq.heappush(heap, end)
        print(f"  Active meetings end at: {sorted(heap)}")
        print(f"  Rooms in use: {len(heap)}")
        
        max_rooms = max(max_rooms, len(heap))
    
    print(f"\nMaximum rooms needed: {max_rooms}")
    return max_rooms
```

### Step-by-Step Walkthrough

**Input**: `intervals = [[0,30],[5,10],[15,20]]`

```
Step 1: Sort by start time
Sorted: [[0,30],[5,10],[15,20]]

Step 2: Process first meeting [0,30]
  heap = [30]
  rooms = 1

Step 3: Process meeting [5,10]
  - Check: heap[0] = 30, start = 5
  - 30 > 5: meeting [0,30] still ongoing
  - Can't reuse room, need new one
  - Push 10 to heap
  heap = [10, 30]
  rooms = 2 ← Maximum so far

Step 4: Process meeting [15,20]
  - Check: heap[0] = 10, start = 15
  - 10 <= 15: meeting ending at 10 finished!
  - Pop 10 (free that room)
  - Push 20 (reuse the freed room)
  heap = [20, 30]
  rooms = 2 (no increase)

Final: Maximum heap size = 2
Answer: 2 rooms needed
```

**Input**: `intervals = [[1,5],[2,8],[5,10]]`

```
Step 1: Sort → [[1,5],[2,8],[5,10]]

Step 2: Process [1,5]
  heap = [5]
  rooms = 1

Step 3: Process [2,8]
  - heap[0] = 5, start = 2
  - 5 > 2: ongoing
  - Add new room
  heap = [5, 8]
  rooms = 2 ← Maximum

Step 4: Process [5,10]
  - heap[0] = 5, start = 5
  - 5 <= 5: meeting ended (boundary case)
  - Pop 5 (free room)
  - Push 10 (reuse room)
  heap = [8, 10]
  rooms = 2

Answer: 2 rooms needed
```

---

## Solution 2: Sweep Line Algorithm

### Algorithm

1. Create separate arrays for start and end times
2. Sort both arrays independently
3. Use two pointers to traverse both arrays
4. Count rooms: increment on start, decrement on end
5. Track maximum count

### Implementation

```python
def minMeetingRooms_sweep(intervals):
    """
    Sweep line approach - process start/end events.
    
    Time Complexity: O(n log n) - sorting
    Space Complexity: O(n) - separate arrays for times
    
    Args:
        intervals: List[List[int]] - meeting intervals
    
    Returns:
        int - minimum rooms needed
    """
    if not intervals:
        return 0
    
    # Separate start and end times
    starts = sorted([interval[0] for interval in intervals])
    ends = sorted([interval[1] for interval in intervals])
    
    rooms = 0
    max_rooms = 0
    start_ptr = 0
    end_ptr = 0
    
    # Process all events
    while start_ptr < len(intervals):
        # If meeting starts before earliest ending
        if starts[start_ptr] < ends[end_ptr]:
            # Start event: need a room
            rooms += 1
            max_rooms = max(max_rooms, rooms)
            start_ptr += 1
        else:
            # End event: free a room
            rooms -= 1
            end_ptr += 1
    
    return max_rooms


def minMeetingRooms_sweep_events(intervals):
    """
    Sweep line with explicit event objects.
    """
    if not intervals:
        return 0
    
    events = []
    
    # Create events: (time, type)
    # type: 0 for end (process first), 1 for start
    for start, end in intervals:
        events.append((start, 1))   # Start event
        events.append((end, 0))     # End event
    
    # Sort by time, then by type (ends before starts)
    events.sort()
    
    rooms = 0
    max_rooms = 0
    
    for time, event_type in events:
        if event_type == 1:  # Start
            rooms += 1
            max_rooms = max(max_rooms, rooms)
        else:  # End
            rooms -= 1
    
    return max_rooms


def minMeetingRooms_sweep_verbose(intervals):
    """
    Verbose sweep line with detailed output.
    """
    if not intervals:
        return 0
    
    starts = sorted([i[0] for i in intervals])
    ends = sorted([i[1] for i in intervals])
    
    print(f"Start times: {starts}")
    print(f"End times: {ends}")
    print()
    
    rooms = 0
    max_rooms = 0
    s = 0
    e = 0
    
    while s < len(intervals):
        if starts[s] < ends[e]:
            rooms += 1
            print(f"Time {starts[s]}: Meeting starts → rooms = {rooms}")
            max_rooms = max(max_rooms, rooms)
            s += 1
        else:
            print(f"Time {ends[e]}: Meeting ends → rooms = {rooms - 1}")
            rooms -= 1
            e += 1
    
    print(f"\nMaximum rooms: {max_rooms}")
    return max_rooms
```

### Walkthrough: Sweep Line

**Input**: `intervals = [[0,30],[5,10],[15,20]]`

```
Start times: [0, 5, 15]
End times:   [10, 20, 30]

Time 0: Start event
  rooms = 1
  max_rooms = 1

Time 5: Start event (5 < 10)
  rooms = 2
  max_rooms = 2 ← Maximum!

Time 10: End event (15 >= 10)
  rooms = 1

Time 15: Start event (15 < 20)
  rooms = 2

Time 20: End event
  rooms = 1

Time 30: End event
  rooms = 0

Answer: 2
```

**Key**: Why sort separately?
```
Intervals: [[1,5],[5,10]]

Starts: [1, 5]
Ends:   [5, 10]

At time 5:
  - Compare: 5 (start) < 5 (end)? No
  - Process END first: room freed
  - Then START: reuse that room
  - Result: 1 room (correct!)

If we processed START first:
  - Would count as 2 rooms (wrong!)
```

---

## Solution 3: Sorting Start and End Separately

### Implementation

```python
def minMeetingRooms_simple_sweep(intervals):
    """
    Simplest sweep line implementation.
    
    Time: O(n log n)
    Space: O(n)
    """
    if not intervals:
        return 0
    
    n = len(intervals)
    starts = sorted([i[0] for i in intervals])
    ends = sorted([i[1] for i in intervals])
    
    rooms = max_rooms = 0
    end_idx = 0
    
    for start in starts:
        # Free rooms for ended meetings
        if start >= ends[end_idx]:
            end_idx += 1
        else:
            rooms += 1
        
        max_rooms = max(max_rooms, rooms)
    
    return max_rooms
```

---

## Edge Cases

### 1. Empty Input
```python
intervals = []
# Expected: 0 (no meetings, no rooms)
assert minMeetingRooms([]) == 0
```

### 2. Single Meeting
```python
intervals = [[1, 10]]
# Expected: 1 (one room for one meeting)
assert minMeetingRooms([[1, 10]]) == 1
```

### 3. All Meetings Overlap
```python
intervals = [[1,10],[2,10],[3,10],[4,10]]
# All overlap, need 4 rooms
# Time 4: all 4 meetings active
assert minMeetingRooms(intervals) == 4
```

### 4. No Meetings Overlap
```python
intervals = [[1,2],[3,4],[5,6]]
# Sequential meetings, 1 room
assert minMeetingRooms(intervals) == 1
```

### 5. Meetings Touch (Boundary)
```python
intervals = [[1,5],[5,10],[10,15]]
# Touch but don't overlap
# End time = next start time: OK to reuse
assert minMeetingRooms(intervals) == 1
```

### 6. Same Start Times
```python
intervals = [[1,5],[1,8],[1,10]]
# All start at 1, all need separate rooms
assert minMeetingRooms(intervals) == 3
```

### 7. Same End Times
```python
intervals = [[1,10],[2,10],[3,10]]
# Different starts, same end
# Maximum overlap: 3
assert minMeetingRooms(intervals) == 3
```

### 8. One Long Meeting
```python
intervals = [[1,100],[2,3],[4,5],[6,7]]
# Long meeting [1,100] overlaps all others
# Need 2 rooms max (long + one short)
assert minMeetingRooms(intervals) == 2
```

### 9. Nested Meetings
```python
intervals = [[1,10],[2,9],[3,8],[4,7]]
# Fully nested: [1,[2,[3,[4,7],8],9],10]
# At time 4: all 4 active
assert minMeetingRooms(intervals) == 4
```

### 10. Interleaved Pattern
```python
intervals = [[1,3],[2,4],[3,5],[4,6]]
# Sliding window pattern
# Max overlap at time 3: [1,3],[2,4],[3,5] = 3 rooms
assert minMeetingRooms(intervals) == 3
```

---

## Common Mistakes

### 1. Not Handling Boundary Cases
```python
# WRONG: Treating end == start as overlap
if start <= heap[0]:  # Wrong!
    heapq.heappop(heap)

# CORRECT: end == start means no overlap
if start >= heap[0]:  # Correct
    heapq.heappop(heap)
```

### 2. Forgetting to Sort
```python
# WRONG: Processing unsorted intervals
for start, end in intervals:  # Wrong if not sorted!
    # ...

# CORRECT: Sort first
intervals.sort(key=lambda x: x[0])
for start, end in intervals:
    # ...
```

### 3. Wrong Heap Comparison
```python
# WRONG: Using > instead of <=
if heap[0] < start:  # Misses boundary case

# CORRECT
if heap[0] <= start:  # Handles end == start
```

### 4. Not Tracking Maximum
```python
# WRONG: Returning final heap size
return len(heap)  # Wrong! Heap empties at end

# CORRECT: Track maximum
max_rooms = max(max_rooms, len(heap))
return max_rooms
```

### 5. Sweep Line Event Order
```python
# WRONG: Same time, wrong order
events.sort()  # Starts before ends at same time

# CORRECT: Ends before starts
events.append((start, 1))  # Type 1 for start
events.append((end, 0))    # Type 0 for end (sorts first)
events.sort()
```

---

## Complexity Analysis

### Min Heap Approach

**Time Complexity: O(n log n)**
- Sorting: O(n log n)
- Heap operations: O(n log n)
  - n insertions: O(n log n)
  - up to n deletions: O(n log n)
- Total: O(n log n)

**Space Complexity: O(n)**
- Heap size: O(n) worst case (all meetings overlap)
- Sorting: O(1) to O(n) depending on implementation

### Sweep Line Approach

**Time Complexity: O(n log n)**
- Creating start/end arrays: O(n)
- Sorting both arrays: 2 * O(n log n) = O(n log n)
- Two-pointer traversal: O(n)
- Total: O(n log n)

**Space Complexity: O(n)**
- Start array: O(n)
- End array: O(n)
- Total: O(n)

### Comparison

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Min Heap | O(n log n) | O(n) | More intuitive |
| Sweep Line | O(n log n) | O(n) | Simpler implementation |
| Brute Force | O(n²) | O(1) | Not practical |

---

## Comprehensive Test Cases

```python
def test_meeting_rooms_ii():
    """Comprehensive test suite."""
    
    # Test 1: Basic example
    assert minMeetingRooms([[0,30],[5,10],[15,20]]) == 2
    
    # Test 2: No overlap
    assert minMeetingRooms([[7,10],[2,4]]) == 1
    
    # Test 3: Touch but don't overlap
    assert minMeetingRooms([[1,5],[5,10],[10,15]]) == 1
    
    # Test 4: All overlap
    assert minMeetingRooms([[1,10],[2,10],[3,10]]) == 3
    
    # Test 5: Single meeting
    assert minMeetingRooms([[1,2]]) == 1
    
    # Test 6: Two overlapping
    assert minMeetingRooms([[1,5],[2,6]]) == 2
    
    # Test 7: Multiple groups
    assert minMeetingRooms([[1,2],[3,4],[1,2],[3,4]]) == 2
    
    # Test 8: Nested
    assert minMeetingRooms([[1,10],[2,9],[3,8]]) == 3
    
    # Test 9: One room reused
    assert minMeetingRooms([[1,2],[2,3],[3,4],[4,5]]) == 1
    
    # Test 10: Complex pattern
    assert minMeetingRooms([[1,4],[2,5],[3,6],[4,7]]) == 3
    
    print("All tests passed!")


def test_edge_cases():
    """Test edge cases."""
    
    # Empty
    assert minMeetingRooms([]) == 0
    
    # Single
    assert minMeetingRooms([[0,1]]) == 1
    
    # Same start
    assert minMeetingRooms([[1,5],[1,10]]) == 2
    
    # Same end
    assert minMeetingRooms([[1,5],[2,5]]) == 2
    
    # Exact same
    assert minMeetingRooms([[1,5],[1,5]]) == 2
    
    print("Edge cases passed!")


def test_large_numbers():
    """Test with large numbers (within constraints)."""
    
    # Large time values
    assert minMeetingRooms([[0,1000000],[1,999999]]) == 2
    
    # Many meetings
    meetings = [[i, i+1] for i in range(100)]
    assert minMeetingRooms(meetings) == 1  # All sequential
    
    # All overlapping
    meetings = [[0, 100] for _ in range(50)]
    assert minMeetingRooms(meetings) == 50
    
    print("Large input tests passed!")


# Run all tests
if __name__ == "__main__":
    test_meeting_rooms_ii()
    test_edge_cases()
    test_large_numbers()
```

---

## Variations and Related Problems

### Variation 1: Return Actual Room Assignments

```python
def assignMeetingRooms(intervals):
    """
    Assign each meeting to a room number.
    
    Returns: List of room assignments
    """
    if not intervals:
        return []
    
    # Track (end_time, room_id) for each room
    heap = []
    room_assignments = [0] * len(intervals)
    next_room = 0
    
    # Store original indices
    indexed_intervals = [(intervals[i], i) for i in range(len(intervals))]
    indexed_intervals.sort(key=lambda x: x[0][0])
    
    for (start, end), orig_idx in indexed_intervals:
        if heap and heap[0][0] <= start:
            # Reuse room
            _, room_id = heapq.heappop(heap)
            heapq.heappush(heap, (end, room_id))
            room_assignments[orig_idx] = room_id
        else:
            # New room
            heapq.heappush(heap, (end, next_room))
            room_assignments[orig_idx] = next_room
            next_room += 1
    
    return room_assignments


# Example usage
intervals = [[0,30],[5,10],[15,20]]
assignments = assignMeetingRooms(intervals)
print(f"Room assignments: {assignments}")
# Output: [0, 1, 1] - meeting 0 in room 0, meetings 1&2 in room 1
```

### Variation 2: Find Busiest Time

```python
def findBusiestTime(intervals):
    """
    Find the time period with maximum concurrent meetings.
    
    Returns: (start_time, end_time, num_meetings)
    """
    events = []
    for start, end in intervals:
        events.append((start, 1))
        events.append((end, -1))
    
    events.sort()
    
    current_meetings = 0
    max_meetings = 0
    busiest_start = 0
    
    for i, (time, delta) in enumerate(events):
        current_meetings += delta
        
        if current_meetings > max_meetings:
            max_meetings = current_meetings
            busiest_start = time
            # Find when this busy period ends
            busiest_end = events[i+1][0] if i+1 < len(events) else time
    
    return (busiest_start, busiest_end, max_meetings)
```

### Variation 3: Can Attend With K Rooms?

```python
def canAttendWithKRooms(intervals, k):
    """
    Check if all meetings can be held with k rooms.
    
    Returns: bool
    """
    return minMeetingRooms(intervals) <= k
```

### Variation 4: Find Free Time Slots

```python
def findFreeSlots(intervals, start, end):
    """
    Find time slots when no meetings are scheduled.
    
    Args:
        intervals: meeting times
        start: day start
        end: day end
    
    Returns: list of free [start, end] intervals
    """
    if not intervals:
        return [[start, end]]
    
    intervals.sort(key=lambda x: x[0])
    
    free_slots = []
    current_time = start
    
    for meeting_start, meeting_end in intervals:
        if meeting_start > current_time:
            free_slots.append([current_time, meeting_start])
        current_time = max(current_time, meeting_end)
    
    if current_time < end:
        free_slots.append([current_time, end])
    
    return free_slots
```

---

## Related LeetCode Problems

1. **Meeting Rooms (LeetCode 252)** - Easy
   - Check if one person can attend all meetings
   - Simpler: just check for any overlap

2. **Merge Intervals (LeetCode 56)** - Medium
   - Merge overlapping intervals
   - Similar sorting approach

3. **Insert Interval (LeetCode 57)** - Medium
   - Insert and merge new interval
   - Related interval manipulation

4. **Non-overlapping Intervals (LeetCode 435)** - Medium
   - Minimum removals to eliminate overlaps
   - Greedy interval selection

5. **My Calendar I (LeetCode 729)** - Medium
   - Book meetings without double booking
   - Similar overlap detection

6. **My Calendar II (LeetCode 731)** - Medium
   - Allow at most double booking
   - Track overlapping levels

7. **Employee Free Time (LeetCode 759)** - Hard
   - Find common free time across employees
   - Multiple interval merging

8. **Car Pooling (LeetCode 1094)** - Medium
   - Similar to meeting rooms with capacity
   - Sweep line with counts

9. **Maximum CPU Load (Similar)** - Hard
   - Schedule tasks with CPU loads
   - Weighted version of meeting rooms

10. **Course Schedule III (LeetCode 630)** - Hard
    - Take maximum courses before deadlines
    - Advanced interval scheduling

---

## Interview Tips

### What Interviewers Look For

1. **Pattern Recognition**: Do you identify this as a scheduling/overlap problem?
2. **Multiple Approaches**: Can you explain different solutions?
3. **Trade-offs**: Understanding time/space complexity differences
4. **Edge Cases**: Handling boundary conditions properly
5. **Code Quality**: Clean, bug-free implementation

### How to Approach in Interview

```
1. Clarify Requirements (1-2 min)
   - Can meetings touch (end == start)?
   - Empty input possible?
   - Input sorted?

2. Think Aloud (2-3 min)
   - "This is about finding maximum concurrent meetings"
   - "Two main approaches: heap or sweep line"
   - "I'll use min heap - more intuitive"

3. Walk Through Example (2 min)
   - Draw timeline
   - Show heap changes
   - Explain room allocation

4. Code (5-7 min)
   - Start with clean structure
   - Handle edge cases
   - Add comments

5. Test (2-3 min)
   - Test example cases
   - Check edge cases
   - Verify complexity
```

### Common Follow-up Questions

1. **"Can you optimize space?"**
   - Sweep line uses O(n) auxiliary space
   - Heap also O(n) worst case
   - Can't do better than O(n) for this problem

2. **"What if meetings are streamed?"**
   - Can't sort in advance
   - Need different approach (e.g., interval tree)

3. **"Return actual room assignments?"**
   - Track room IDs in heap
   - Return mapping of meeting → room

4. **"Find which meetings can be in same room?"**
   - Group non-overlapping meetings
   - Graph coloring approach

5. **"What if rooms have different capacities?"**
   - Need to track capacity per room
   - More complex scheduling problem

---

## Visual Representation

### Timeline Visualization

```
Intervals: [[0,30],[5,10],[15,20]]

Timeline:
0    5    10   15   20   25   30
|----[====1====]====|====|====|
     |[=2=]         |          |
               |[===3===]      |

Room allocation:
Room 1: [====1========================] (0-30)
Room 2:      [=2=]         [====3====]  (5-10, 15-20)

Maximum concurrent: 2 (at time 5-10)
```

### Heap State Changes

```
Intervals: [[1,5],[2,8],[5,10]]

Time 1: Meeting [1,5] starts
  Heap: [5]
  Rooms: 1

Time 2: Meeting [2,8] starts  
  Heap: [5,8]  (5 is min)
  Rooms: 2  ← Maximum

Time 5: Meeting [5,10] starts
  - Pop 5 (ended)
  - Push 10
  Heap: [8,10]  (8 is min)
  Rooms: 2  (reused room)

Answer: 2
```

### Sweep Line Events

```
Intervals: [[0,30],[5,10],[15,20]]

Events timeline:
        S=Start, E=End
        
0S      5S      10E     15S     20E     30E
↓       ↓       ↓       ↓       ↓       ↓
+1      +1      -1      +1      -1      -1
=1      =2      =1      =2      =1      =0
        ↑
        Maximum!

Track: current rooms
Result: maximum value (2)
```

---

## Summary

### Key Takeaways

1. **Problem Type**: Concurrent resource management
2. **Best Approach**: Min heap tracks end times
3. **Alternative**: Sweep line with separate start/end arrays
4. **Time**: O(n log n) - dominated by sorting
5. **Space**: O(n) - for heap or arrays
6. **Critical**: Handle boundary case (end == start) correctly

### Decision Tree

```
Need to find minimum meeting rooms?
│
├─ Can sort in advance?
│  │
│  ├─ YES → Use Min Heap or Sweep Line
│  │        - Both O(n log n) time
│  │        - Choose based on preference
│  │
│  └─ NO → Need online algorithm
│           - Interval tree
│           - More complex
│
└─ Need room assignments?
   - Track room IDs in heap
   - Return mapping
```

### Quick Reference

```python
# Min Heap Template
def minMeetingRooms(intervals):
    if not intervals:
        return 0
    
    intervals.sort(key=lambda x: x[0])
    heap = [intervals[0][1]]
    
    for i in range(1, len(intervals)):
        if heap[0] <= intervals[i][0]:
            heapq.heappop(heap)
        heapq.heappush(heap, intervals[i][1])
    
    return len(heap)

# Sweep Line Template
def minMeetingRooms(intervals):
    starts = sorted([i[0] for i in intervals])
    ends = sorted([i[1] for i in intervals])
    
    rooms = max_rooms = 0
    end_ptr = 0
    
    for start in starts:
        if start >= ends[end_ptr]:
            end_ptr += 1
        else:
            rooms += 1
        max_rooms = max(max_rooms, rooms)
    
    return max_rooms
```

### When to Use Each Approach

**Min Heap:**
- Need to track which meetings are active
- Want intuitive room allocation
- Need actual room assignments
- Building more complex scheduling system

**Sweep Line:**
- Only need final count
- Prefer simpler implementation
- Don't need to track state
- Teaching/explaining to others

Both have same complexity - choose based on context and preference!

---

**Practice Tips:**
- Draw timeline for visual understanding
- Test boundary cases (touching meetings)
- Practice both implementations
- Understand why sorting is necessary
- Consider follow-up variations

**Remember:** This is about finding the maximum number of concurrent events at any point in time!
