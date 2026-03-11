# K Closest Points to Origin - LeetCode #973

**Difficulty**: Medium  
**Pattern**: Max Heap of Size K / QuickSelect  
**Frequency**: Very High (Top Interview Question)

---

## Problem Statement

Given an array of `points` where `points[i] = [xi, yi]` represents a point on the **X-Y** plane and an integer `k`, return the `k` closest points to the origin `(0, 0)`.

The distance between two points on the **X-Y** plane is the Euclidean distance (i.e., `√(x² + y²)`).

You may return the answer in **any order**. The answer is **guaranteed** to be **unique** (except for the order that it is in).

**Example 1:**
```
Input: points = [[1,3],[-2,2]], k = 1
Output: [[-2,2]]
Explanation:
Distance from (1,3) to origin = √(1² + 3²) = √10
Distance from (-2,2) to origin = √(4 + 4) = √8
Since √8 < √10, (-2,2) is closer to origin
```

**Example 2:**
```
Input: points = [[3,3],[5,-1],[-2,4]], k = 2
Output: [[3,3],[-2,4]]
Explanation: The answer [[-2,4],[3,3]] would also be accepted.
```

**Constraints:**
- `1 <= k <= points.length <= 10^4`
- `-10^4 < xi, yi < 10^4`

---

## Core Concepts

### Euclidean Distance

**Formula:** Distance from `(x, y)` to origin `(0, 0)` = `√(x² + y²)`

**Key Optimization:** Don't need actual distance, just comparison!
- `√(x² + y²)` vs `√(a² + b²)`
- Equivalent to comparing: `x² + y²` vs `a² + b²`
- **Square root not needed** for comparisons

**Example:**
```python
Point (3, 4): distance = √(9 + 16) = √25 = 5
Point (1, 2): distance = √(1 + 4) = √5 ≈ 2.236

Compare: 25 vs 5 → (3,4) is farther
No need to compute actual √!
```

### Problem Analysis

**Similar to:** Kth Largest Element

| Aspect           | Kth Largest | K Closest Points |
|------------------|-------------|------------------|
| Input            | Array       | 2D array         |
| Comparison       | Value       | Distance         |
| Heap type        | Min heap    | Max heap         |
| Return           | Single value| K points         |

### Multiple Approaches

1. **Max Heap of Size K**: O(n log k) ⭐ Best for small k
2. **Sort by Distance**: O(n log n)
3. **QuickSelect**: O(n) average
4. **Binary Search on Distance**: O(n log D)

---

## Solution 1: Max Heap (Recommended)

### Why Max Heap?

**Finding K closest = Finding K smallest distances**

Use **max heap** to maintain K smallest distances:
- Top of max heap = largest of K smallest
- If new point closer, it replaces top
- Final heap contains K closest points

### Algorithm

1. Build max heap of first K points (by distance)
2. For remaining points:
   - If closer than heap top: replace top
3. Return all points in heap

### Implementation

```python
import heapq
from typing import List

class Solution:
    def kClosest(self, points: List[List[int]], k: int) -> List[List[int]]:
        """
        Max heap approach - maintain K closest points.
        
        Time: O(n log k)
        Space: O(k)
        """
        # Max heap: use negative distance
        # Heap element: (-distance, [x, y])
        max_heap = []
        
        for x, y in points:
            dist = x * x + y * y  # Don't need sqrt
            
            if len(max_heap) < k:
                # Heap not full, add point
                heapq.heappush(max_heap, (-dist, [x, y]))
            elif dist < -max_heap[0][0]:
                # Point closer than farthest in heap
                heapq.heapreplace(max_heap, (-dist, [x, y]))
        
        # Extract points (discard distances)
        return [point for _, point in max_heap]
```

### Alternative: Cleaner Version

```python
class Solution:
    def kClosest(self, points: List[List[int]], k: int) -> List[List[int]]:
        """
        Simpler logic - push all, then keep k.
        """
        max_heap = []
        
        for x, y in points:
            dist = x * x + y * y
            heapq.heappush(max_heap, (-dist, [x, y]))
            
            if len(max_heap) > k:
                heapq.heappop(max_heap)
        
        return [point for _, point in max_heap]
```

### Step-by-Step Example

**Input:** `points = [[3,3],[5,-1],[-2,4]]`, `k = 2`

**Distances:**
- [3,3]: 3² + 3² = 18
- [5,-1]: 5² + 1² = 26
- [-2,4]: 4 + 16 = 20

```
Step 1: Process [3,3]
heap = [(-18, [3,3])]

Step 2: Process [5,-1]
heap = [(-26, [5,-1]), (-18, [3,3])]
Heapify → [(-26, [5,-1]), (-18, [3,3])]
Size = k, heap full

Step 3: Process [-2,4]
dist = 20
Top = (-26, [5,-1]) → 26
20 < 26 → replace
heap = [(-20, [-2,4]), (-18, [3,3])]

Result: [[3,3], [-2,4]] ✓
```

---

## Solution 2: Sorting

### Algorithm

1. Calculate distance for each point
2. Sort by distance
3. Return first K points

### Implementation

```python
class Solution:
    def kClosest(self, points: List[List[int]], k: int) -> List[List[int]]:
        """
        Sort by distance.
        
        Time: O(n log n)
        Space: O(n) for sorting
        """
        # Sort by distance squared
        points.sort(key=lambda p: p[0] * p[0] + p[1] * p[1])
        return points[:k]
```

### One-liner

```python
class Solution:
    def kClosest(self, points: List[List[int]], k: int) -> List[List[int]]:
        return sorted(points, key=lambda p: p[0]**2 + p[1]**2)[:k]
```

**Pros:**
- Simplest to code
- Good for interviews if time-pressed

**Cons:**
- Not optimal: O(n log n) vs O(n log k)
- Sorts entire array when only need K

---

## Solution 3: QuickSelect (Optimal Average)

### Algorithm

Partition-based selection (like QuickSort):

1. Choose pivot point
2. Partition by distance
3. If partition size = k: done
4. Else recurse on correct partition

### Implementation

```python
import random

class Solution:
    def kClosest(self, points: List[List[int]], k: int) -> List[List[int]]:
        """
        QuickSelect - optimal average case.
        
        Time: O(n) average, O(n²) worst
        Space: O(1)
        """
        def distance(point):
            return point[0] ** 2 + point[1] ** 2
        
        def partition(left, right, pivot_idx):
            pivot_dist = distance(points[pivot_idx])
            # Move pivot to end
            points[pivot_idx], points[right] = points[right], points[pivot_idx]
            
            store_idx = left
            for i in range(left, right):
                if distance(points[i]) < pivot_dist:
                    points[i], points[store_idx] = points[store_idx], points[i]
                    store_idx += 1
            
            # Move pivot to final position
            points[store_idx], points[right] = points[right], points[store_idx]
            return store_idx
        
        def quickselect(left, right, k):
            if left >= right:
                return
            
            # Random pivot for better average case
            pivot_idx = random.randint(left, right)
            pivot_idx = partition(left, right, pivot_idx)
            
            if pivot_idx == k:
                return
            elif pivot_idx < k:
                quickselect(pivot_idx + 1, right, k)
            else:
                quickselect(left, pivot_idx - 1, k)
        
        quickselect(0, len(points) - 1, k)
        return points[:k]
```

### QuickSelect Example

**Find 2 closest in** `[[3,3], [5,-1], [-2,4]]`

**Distances:** [18, 26, 20]

```
Step 1: Partition with pivot=26 (point [5,-1])
Before: [[3,3], [5,-1], [-2,4]]
After:  [[3,3], [-2,4], [5,-1]]
Pivot at index 2

k=2, pivot=2 → recurse left [0,1]

Step 2: Partition left part [[3,3], [-2,4]]
After: [[3,3], [-2,4]]
Pivot at index 0 or 1

k=2, found 2 elements
Return [[3,3], [-2,4]] ✓
```

---

## Solution 4: Python's nsmallest (Built-in)

### Implementation

```python
class Solution:
    def kClosest(self, points: List[List[int]], k: int) -> List[List[int]]:
        """
        Use Python's heapq.nsmallest.
        
        Time: O(n log k)
        Space: O(k)
        """
        return heapq.nsmallest(k, points, 
                              key=lambda p: p[0]**2 + p[1]**2)
```

**Pros:**
- One line, very clean
- Optimal complexity
- Well-tested library code

**Cons:**
- Doesn't show understanding of algorithms
- Not available in all languages

---

## Complexity Comparison

### Time Complexity

| Approach       | Time          | Best For        |
|----------------|---------------|-----------------|
| Sorting        | O(n log n)    | Small n         |
| Max Heap       | O(n log k)    | Small k ⭐      |
| QuickSelect    | O(n) avg      | Large n, k ⭐   |
| nsmallest      | O(n log k)    | Quick solution  |

### Space Complexity

| Approach    | Space | In-place? |
|-------------|-------|-----------|
| Sorting     | O(n)  | No*       |
| Max Heap    | O(k)  | No        |
| QuickSelect | O(1)  | Yes       |
| nsmallest   | O(k)  | No        |

*Depends on sorting algorithm

### When to Use Each?

**Max Heap (Interview Recommended):**
- ✅ Optimal for k << n
- ✅ Clear algorithm
- ✅ Doesn't modify input
- ✅ Works for streaming data

**QuickSelect:**
- ✅ Best average case
- ✅ In-place
- ⚠️ Modifies input
- ⚠️ Worst case O(n²)

**Sorting:**
- ✅ Simplest code
- ✅ Order preserved
- ⚠️ Not optimal

---

## Detailed Complexity Analysis

### Max Heap Time Breakdown

```python
for point in points:           # n iterations
    if len(heap) < k:
        heappush(heap, ...)     # O(log k)
    elif dist < heap[0]:
        heapreplace(heap, ...)  # O(log k)
```

**Analysis:**
- First k insertions: k × O(log k) = O(k log k)
- Remaining n-k: (n-k) × O(log k)
- Total: O(k log k + (n-k) log k) = **O(n log k)**

**Space:** O(k) for heap

### QuickSelect Analysis

**Best/Average case:**
```
T(n) = T(n/2) + O(n)
     = O(n + n/2 + n/4 + ...)
     = O(2n)
     = O(n)
```

**Worst case** (bad pivots):
```
T(n) = T(n-1) + O(n)
     = O(n²)
```

**Random pivot → O(n) with high probability**

---

## Why Distance Squared?

### Mathematical Proof

**Need to compare:** `√(x₁² + y₁²)` vs `√(x₂² + y₂²)`

**Since sqrt is monotonic:**
```
√a < √b  ⟺  a < b  (for a, b ≥ 0)

Therefore:
√(x₁² + y₁²) < √(x₂² + y₂²)
  ⟺
x₁² + y₁² < x₂² + y₂²
```

**Benefit:** Avoid expensive sqrt computation!

### Performance Impact

```python
# Slower: O(n) sqrt operations
for x, y in points:
    dist = math.sqrt(x*x + y*y)  # Slow!

# Faster: No sqrt needed
for x, y in points:
    dist = x*x + y*y  # Fast!
```

**Speedup:** ~2-3x faster in practice

---

## Edge Cases & Special Scenarios

### Edge Case 1: k = 1 (Closest Point)

```python
points = [[1,3],[-2,2]], k = 1
Distances: [10, 8]
Result: [[-2,2]] ✓
```

### Edge Case 2: k = n (All Points)

```python
points = [[1,3],[-2,2]], k = 2
Result: [[1,3],[-2,2]] (any order)
```

### Edge Case 3: Points at Origin

```python
points = [[0,0],[1,1]], k = 1
Distances: [0, 2]
Result: [[0,0]] ✓
```

### Edge Case 4: Negative Coordinates

```python
points = [[-5,-5],[3,3]], k = 1
Distances: [50, 18]
Result: [[3,3]] ✓
```

### Edge Case 5: Same Distance

```python
points = [[1,1],[-1,-1],[1,-1],[-1,1]], k = 2
All distance = 2
Result: Any 2 points ✓
```

### Edge Case 6: Large Coordinates

```python
points = [[10000,10000],[1,1]], k = 1
Distances: [200000000, 2]
Result: [[1,1]]
Note: Using dist² avoids overflow in other languages
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Using Min Heap Instead of Max Heap

```python
# ❌ Wrong: Min heap for k closest
min_heap = []
heappush(min_heap, (dist, point))
if len(min_heap) > k:
    heappop(min_heap)  # Removes smallest!

# Result: k farthest points, not closest!

# ✅ Correct: Max heap (negate distances)
max_heap = []
heappush(max_heap, (-dist, point))
```

### Mistake 2: Forgetting to Negate for Max Heap

```python
# ❌ Wrong: Python heapq is min heap
heappush(heap, (dist, point))  # Creates min heap

# ✅ Correct: Negate for max heap
heappush(heap, (-dist, point))
```

### Mistake 3: Computing sqrt Unnecessarily

```python
# ❌ Slower
dist = math.sqrt(x*x + y*y)

# ✅ Faster and sufficient
dist = x*x + y*y
```

### Mistake 4: Returning Distances Instead of Points

```python
# ❌ Wrong
return max_heap  # Returns [(dist, point), ...]

# ✅ Correct
return [point for dist, point in max_heap]
```

### Mistake 5: Not Handling Heap Size

```python
# ❌ Wrong: Heap grows unbounded
for x, y in points:
    dist = x*x + y*y
    heappush(max_heap, (-dist, [x, y]))

# ✅ Correct: Maintain size k
for x, y in points:
    dist = x*x + y*y
    heappush(max_heap, (-dist, [x, y]))
    if len(max_heap) > k:
        heappop(max_heap)
```

---

## Pattern Recognition

### This Pattern Applies To:

**"Top K" problems with custom comparison:**

1. **K Closest Points** (this problem)
2. **Top K Frequent Elements**
3. **K Weakest Rows in Matrix**
4. **K Closest Elements to X**
5. **Find K Pairs with Smallest Sums**

### General Top K Template

```python
def topK(elements, k, key_func, maximize=False):
    """
    Generic top K template.
    
    maximize=True: max heap, keep k largest
    maximize=False: min heap, keep k smallest
    """
    heap = []
    
    for elem in elements:
        value = key_func(elem)
        
        if maximize:
            value = -value  # Max heap
        
        heappush(heap, (value, elem))
        if len(heap) > k:
            heappop(heap)
    
    return [elem for _, elem in heap]
```

**Usage:**
```python
# K closest points
result = topK(points, k, 
              lambda p: p[0]**2 + p[1]**2,
              maximize=False)

# K largest numbers
result = topK(nums, k,
              lambda x: x,
              maximize=True)
```

---

## Optimization Techniques

### Optimization 1: Early Heap Build

```python
# Slightly faster for k close to n
def kClosest(points, k):
    # Build initial heap with all points
    heap = [(-p[0]**2 - p[1]**2, p) for p in points]
    heapq.heapify(heap)  # O(n)
    
    # Pop until k remain
    while len(heap) > k:
        heapq.heappop(heap)
    
    return [p for _, p in heap]
```

**When better:** k > n/2

### Optimization 2: Distance Caching

```python
def kClosest(points, k):
    """Cache distances to avoid recomputation."""
    # Precompute all distances
    distances = [(p[0]**2 + p[1]**2, p) for p in points]
    
    max_heap = []
    for dist, point in distances:
        heappush(max_heap, (-dist, point))
        if len(max_heap) > k:
            heappop(max_heap)
    
    return [p for _, p in max_heap]
```

### Optimization 3: Partial Sort

```python
import numpy as np

def kClosest(points, k):
    """Use numpy's partition for O(n) average."""
    distances = np.array([p[0]**2 + p[1]**2 for p in points])
    # Partition around kth smallest
    idx = np.argpartition(distances, k)[:k]
    return [points[i] for i in idx]
```

**Note:** Requires numpy, rarely needed

---

## Related Problems

### Direct Applications

1. **LeetCode 347: Top K Frequent Elements**
   - Heap by frequency
   - Same pattern

2. **LeetCode 692: Top K Frequent Words**
   - Heap with custom comparison
   - Lexicographic tiebreaker

3. **LeetCode 1057: Campus Bikes**
   - Multiple distance comparisons
   - Priority queue

### Similar Patterns

4. **LeetCode 658: Find K Closest Elements**
   - Binary search + two pointers
   - Or heap approach

5. **LeetCode 786: K-th Smallest Prime Fraction**
   - Min heap of fractions
   - Merge K sorted lists variant

6. **LeetCode 373: Find K Pairs with Smallest Sums**
   - Min heap of pairs
   - Multiple arrays

---

## Interview Discussion

### Questions to Ask

1. **"Can the answer be in any order?"**
   - Yes, makes problem easier
   - No need to maintain sorted output

2. **"Are coordinates guaranteed to be integers?"**
   - Affects distance calculation
   - Integer overflow considerations

3. **"Can points overlap or be at origin?"**
   - Distance = 0 is valid

4. **"Memory constraints?"**
   - Heap O(k) vs sorting O(n)

### Explaining Your Solution

**For Max Heap:**

1. "I need to find K points with smallest distances"

2. "I'll use a max heap to maintain the K closest points"

3. "The max heap stores distances as negative values in Python"

4. "For each point, if heap not full, add it"

5. "If full and point closer than top, replace top"

6. "Time is O(n log k), space is O(k)"

### Follow-up Questions

**Q: What if coordinates are floating point?**
A: Same algorithm, just use float arithmetic.

**Q: What if we need points in sorted order by distance?**
A: Sort final K points, adds O(k log k).

**Q: What if k is very large?**
A: QuickSelect might be better, or find (n-k) farthest and exclude.

**Q: What about 3D or higher dimensions?**
A: Same algorithm, distance = x² + y² + z² + ...

---

## Testing & Validation

### Test Cases

```python
def test_k_closest():
    solution = Solution()
    
    # Test 1: Basic case
    result = solution.kClosest([[1,3],[-2,2]], 1)
    assert result == [[-2,2]]
    
    # Test 2: Multiple points
    result = solution.kClosest([[3,3],[5,-1],[-2,4]], 2)
    assert set(tuple(p) for p in result) == {(3,3), (-2,4)}
    
    # Test 3: k = n
    result = solution.kClosest([[1,1],[2,2]], 2)
    assert len(result) == 2
    
    # Test 4: Single point
    result = solution.kClosest([[1,1]], 1)
    assert result == [[1,1]]
    
    # Test 5: Origin included
    result = solution.kClosest([[0,0],[1,1]], 1)
    assert result == [[0,0]]
    
    # Test 6: Negative coordinates
    result = solution.kClosest([[-5,-5],[3,3]], 1)
    assert result == [[3,3]]
    
    # Test 7: Same distances
    result = solution.kClosest([[1,1],[-1,-1],[1,-1]], 2)
    assert len(result) == 2
    
    # Test 8: Large coordinates
    result = solution.kClosest([[10000,10000],[1,1]], 1)
    assert result == [[1,1]]
    
    print("All tests passed!")
```

---

## Summary

### Key Takeaways

1. **Use max heap for K closest** (smallest distances)
2. **Don't compute sqrt** - use distance squared
3. **Negate distances** for max heap in Python
4. **Time O(n log k)** beats sorting O(n log n) when k << n
5. **Works for streaming data** unlike QuickSelect

### Recommended Solution

```python
def kClosest(points, k):
    """Max heap approach."""
    max_heap = []
    
    for x, y in points:
        dist = x*x + y*y
        heappush(max_heap, (-dist, [x, y]))
        
        if len(max_heap) > k:
            heappop(max_heap)
    
    return [point for _, point in max_heap]
```

### Complexity
- **Time**: O(n log k)
- **Space**: O(k)

---

**Tags**: #heap #k-closest #distance #priority-queue #medium  
**Related**: Kth Largest Element, Top K Frequent, K Closest Elements  
**Companies**: Amazon, Facebook, Google, Microsoft, Uber
