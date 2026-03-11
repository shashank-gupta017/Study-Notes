# Non-overlapping Intervals (See Greedy)

## Note
This problem is identical to the one in the Greedy pattern.

## Problem Link
[LeetCode 435: Non-overlapping Intervals](https://leetcode.com/problems/non-overlapping-intervals/)

## Difficulty
Medium

## Cross-Reference
For the complete solution and notes, see:
- **Greedy/notes/01_NonOverlappingIntervals.md**

## Why This Problem Appears in Multiple Categories

This problem is a perfect example of how algorithm patterns overlap:

### As an Intervals Problem
- Input consists of intervals
- Need to handle overlap detection
- Requires interval manipulation

### As a Greedy Problem
- Uses greedy strategy (activity selection)
- Optimal substructure
- Local optimal choices → global optimum

## Quick Summary

**Pattern**: Greedy Activity Selection
**Key Insight**: Sort by end time, keep earliest-ending intervals
**Time**: O(n log n)
**Space**: O(1)

## Implementation
```python
def eraseOverlapIntervals(intervals):
    if not intervals:
        return 0
    
    # Sort by end time (greedy choice)
    intervals.sort(key=lambda x: x[1])
    
    count = 1  # Keep first interval
    end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        if intervals[i][0] >= end:
            count += 1
            end = intervals[i][1]
    
    return len(intervals) - count
```

For detailed explanation, examples, and variations, see the complete notes in the Greedy section.
