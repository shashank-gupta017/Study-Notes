# Kth Largest Element in an Array - LeetCode #215

**Difficulty**: Medium  
**Pattern**: Min Heap of Size K / QuickSelect  
**Frequency**: Very High (Top Interview Question)

---

## Problem Statement

Given an integer array `nums` and an integer `k`, return the `kth` largest element in the array.

Note that it is the `kth` largest element in the sorted order, not the `kth` distinct element.

Can you solve it without sorting?

**Example 1:**
```
Input: nums = [3,2,1,5,6,4], k = 2
Output: 5
Explanation: When sorted [1,2,3,4,5,6], 2nd largest is 5
```

**Example 2:**
```
Input: nums = [3,2,3,1,2,4,5,5,6], k = 4
Output: 4
Explanation: When sorted [1,2,2,3,3,4,5,5,6], 4th largest is 4
```

**Constraints:**
- `1 <= k <= nums.length <= 10^5`
- `-10^4 <= nums[i] <= 10^4`

---

## Core Concepts

### Understanding "Kth Largest"

**Sorted array** (ascending): `[1, 2, 3, 4, 5, 6]`

| Position | Element | Largest |
|----------|---------|---------|
| Index 5  | 6       | 1st     |
| Index 4  | 5       | 2nd     |
| Index 3  | 4       | 3rd     |
| Index 2  | 3       | 4th     |
| Index 1  | 2       | 5th     |
| Index 0  | 1       | 6th     |

**Kth largest = nums[n - k]** in sorted array

### Multiple Approaches

This problem has 4 main approaches:

1. **Sorting**: O(n log n) time
2. **Min Heap of size K**: O(n log k) time ⭐ Optimal for small k
3. **Max Heap**: O(n + k log n) time
4. **QuickSelect**: O(n) average time ⭐ Optimal for large k

### Why Min Heap of Size K?

**Key Insight:** To find Kth largest, maintain K largest elements seen so far.

**Min Heap Property:**
- Top element is the smallest in heap
- If heap size = K, top = Kth largest overall

**Example:** Find 3rd largest in `[3, 2, 1, 5, 6, 4]`

```
Process 3: heap = [3]                     (size 1)
Process 2: heap = [2, 3]                  (size 2)
Process 1: heap = [1, 2, 3]               (size 3, full!)
Process 5: 5 > 1 (top), replace → [2, 3, 5]
Process 6: 6 > 2 (top), replace → [3, 5, 6]
Process 4: 4 > 3 (top), replace → [4, 5, 6]

Final heap: [4, 5, 6]
Top (min) = 4 = 3rd largest ✓
```

---

## Solution 1: Min Heap Approach (Recommended)

### Algorithm

1. Create min heap of size K
2. For each element:
   - If heap size < K: add element
   - Else if element > heap top: replace top with element
3. Return heap top (Kth largest)

### Implementation

```python
import heapq
from typing import List

class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        """
        Min heap approach - optimal for small k.
        
        Maintain heap of K largest elements.
        Top of min heap is Kth largest.
        
        Time: O(n log k) - n insertions, each O(log k)
        Space: O(k) - heap of size k
        """
        # Build initial heap with first k elements
        min_heap = nums[:k]
        heapq.heapify(min_heap)  # O(k)
        
        # Process remaining elements
        for i in range(k, len(nums)):
            if nums[i] > min_heap[0]:
                heapq.heapreplace(min_heap, nums[i])
        
        # Top of min heap is kth largest
        return min_heap[0]
```

### Alternative: Cleaner Version

```python
class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        """
        Process all elements uniformly.
        """
        min_heap = []
        
        for num in nums:
            heapq.heappush(min_heap, num)
            
            # Keep heap size = k
            if len(min_heap) > k:
                heapq.heappop(min_heap)
        
        return min_heap[0]
```

### Step-by-Step Example

**Input:** `nums = [3,2,1,5,6,4]`, `k = 2`

```
Goal: Find 2nd largest

Step 1: Build initial heap with first 2 elements
heap = [3, 2]
heapify → [2, 3]  (min heap)

Step 2: Process 1
1 < 2 (top) → ignore
heap = [2, 3]

Step 3: Process 5
5 > 2 (top) → replace
heap = [5, 3] → heapify → [3, 5]

Step 4: Process 6
6 > 3 (top) → replace
heap = [6, 5] → heapify → [5, 6]

Step 5: Process 4
4 < 5 (top) → ignore
heap = [5, 6]

Final: top = 5 ✓
```

---

## Solution 2: Max Heap Approach

### Algorithm

1. Heapify all elements into max heap: O(n)
2. Pop K-1 times: O(k log n)
3. Return top (Kth largest)

### Implementation

```python
class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        """
        Max heap approach - good for large k close to n.
        
        Time: O(n + k log n)
        Space: O(n)
        """
        # Python heapq is min heap, negate for max heap
        max_heap = [-num for num in nums]
        heapq.heapify(max_heap)  # O(n)
        
        # Pop k-1 times
        for _ in range(k - 1):
            heapq.heappop(max_heap)
        
        # Top is kth largest
        return -max_heap[0]
```

**When to use Max Heap:**
- K is large (close to n)
- Need to access multiple largest elements
- Building entire heap is acceptable

---

## Solution 3: QuickSelect (Optimal Average)

### Algorithm

QuickSelect is like QuickSort but only recurses on one partition.

**Target:** Find element at position `n - k` in sorted order

1. Choose pivot
2. Partition array around pivot
3. If pivot at position `n - k`: done
4. Else recurse on correct partition

### Implementation

```python
class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        """
        QuickSelect algorithm - optimal average case.
        
        Time: O(n) average, O(n^2) worst
        Space: O(1)
        """
        target = len(nums) - k  # kth largest = (n-k)th in sorted order
        
        def quickselect(left, right):
            # Base case
            if left == right:
                return nums[left]
            
            # Partition
            pivot_idx = partition(left, right)
            
            if pivot_idx == target:
                return nums[pivot_idx]
            elif pivot_idx < target:
                return quickselect(pivot_idx + 1, right)
            else:
                return quickselect(left, pivot_idx - 1)
        
        def partition(left, right):
            # Choose rightmost as pivot
            pivot = nums[right]
            i = left
            
            # Move elements < pivot to left
            for j in range(left, right):
                if nums[j] < pivot:
                    nums[i], nums[j] = nums[j], nums[i]
                    i += 1
            
            # Place pivot in correct position
            nums[i], nums[right] = nums[right], nums[i]
            return i
        
        return quickselect(0, len(nums) - 1)
```

### QuickSelect Example

**Find 2nd largest in** `[3,2,1,5,6,4]` → target index = 4

```
Step 1: Partition with pivot=4
[3,2,1,5,6,4]
After: [3,2,1,4,6,5]  pivot at index 3

target=4, pivot=3 → recurse right [6,5]

Step 2: Partition right part [6,5] (indices 4-5)
pivot=5
After: [5,6]  pivot at index 4

target=4, pivot=4 → FOUND!
Return nums[4] = 5 ✓
```

### Optimized: Random Pivot

```python
import random

class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        """
        QuickSelect with random pivot for better average case.
        """
        target = len(nums) - k
        
        def quickselect(left, right):
            if left == right:
                return nums[left]
            
            # Random pivot
            pivot_idx = random.randint(left, right)
            pivot_idx = partition(left, right, pivot_idx)
            
            if pivot_idx == target:
                return nums[pivot_idx]
            elif pivot_idx < target:
                return quickselect(pivot_idx + 1, right)
            else:
                return quickselect(left, pivot_idx - 1)
        
        def partition(left, right, pivot_idx):
            pivot = nums[pivot_idx]
            # Move pivot to end
            nums[pivot_idx], nums[right] = nums[right], nums[pivot_idx]
            
            i = left
            for j in range(left, right):
                if nums[j] < pivot:
                    nums[i], nums[j] = nums[j], nums[i]
                    i += 1
            
            nums[i], nums[right] = nums[right], nums[i]
            return i
        
        return quickselect(0, len(nums) - 1)
```

---

## Solution 4: Simple Sorting

### Implementation

```python
class Solution:
    def findKthLargest(self, nums: List[int], k: int) -> int:
        """
        Simple sorting approach.
        
        Time: O(n log n)
        Space: O(1) or O(n) depending on sort
        """
        nums.sort()
        return nums[-k]  # or nums[len(nums) - k]
```

**When to use:**
- Quick implementation needed
- Array is small
- Acceptable time complexity

---

## Complexity Comparison

### Time Complexity

| Approach        | Time          | Best For           |
|-----------------|---------------|--------------------|
| Sorting         | O(n log n)    | Small n            |
| Min Heap (K)    | O(n log k)    | Small k ⭐         |
| Max Heap        | O(n + k log n)| Large k            |
| QuickSelect     | O(n) avg      | Large n ⭐         |

### Space Complexity

| Approach     | Space  | In-place? |
|--------------|--------|-----------|
| Sorting      | O(1)*  | Yes*      |
| Min Heap     | O(k)   | No        |
| Max Heap     | O(n)   | No        |
| QuickSelect  | O(1)   | Yes       |

*Depends on sort implementation

### Which Approach to Use?

**Min Heap (Recommended for interviews):**
- ✅ Optimal when k << n
- ✅ Easy to explain
- ✅ Guaranteed O(n log k)
- ✅ Works online (streaming data)

**QuickSelect:**
- ✅ Optimal average case O(n)
- ⚠️ Worst case O(n²)
- ⚠️ More complex to implement
- ⚠️ Modifies input array

**Sorting:**
- ✅ Simplest to code
- ⚠️ Not optimal
- Good for small arrays

---

## Detailed Complexity Analysis

### Min Heap Analysis

**Building initial heap:** O(k)
```python
min_heap = nums[:k]
heapq.heapify(min_heap)
```

**Processing remaining n-k elements:**
```python
for i in range(k, n):
    if nums[i] > min_heap[0]:  # O(1)
        heapreplace(min_heap, nums[i])  # O(log k)
```
- Worst case: all n-k elements larger than current top
- Time: O((n-k) log k)

**Total:** O(k + (n-k) log k) = **O(n log k)**

**Space:** O(k) for heap

### QuickSelect Analysis

**Average case:**
```
T(n) = T(n/2) + O(n)
     = O(n + n/2 + n/4 + ...)
     = O(2n)
     = O(n)
```

**Worst case** (always bad pivot):
```
T(n) = T(n-1) + O(n)
     = O(n + (n-1) + (n-2) + ...)
     = O(n²)
```

**With random pivot:** O(n) with high probability

---

## Edge Cases & Special Scenarios

### Edge Case 1: k = 1 (Largest Element)

```python
nums = [3,2,1,5,6,4], k = 1
Expected: 6

Min heap: [6]
Return 6 ✓
```

### Edge Case 2: k = n (Smallest Element)

```python
nums = [3,2,1,5,6,4], k = 6
Expected: 1

Min heap of size 6: [1,2,3,4,5,6]
Return 1 ✓
```

### Edge Case 3: All Same Elements

```python
nums = [5,5,5,5,5], k = 2
Expected: 5

Any approach returns 5 ✓
```

### Edge Case 4: Two Elements

```python
nums = [1,2], k = 1
Expected: 2

nums = [1,2], k = 2
Expected: 1
```

### Edge Case 5: Negative Numbers

```python
nums = [-1,-5,-3,-2], k = 2
Sorted: [-5,-3,-2,-1]
2nd largest: -2 ✓
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Confusing Min Heap with Max Heap

```python
# ❌ Wrong: Using max heap of size k
# Would give k smallest, not k largest!

# ✅ Correct: Min heap of k largest
# Top is smallest of k largest = kth largest
```

### Mistake 2: Off-by-One Errors

```python
# ❌ Wrong
return nums[k]  # k is 1-indexed

# ✅ Correct
return nums[n - k]  # After sorting
return min_heap[0]  # Top of heap
```

### Mistake 3: Not Heapifying Initial Elements

```python
# ❌ Wrong: Just assign
min_heap = nums[:k]
# Heap property not maintained!

# ✅ Correct
min_heap = nums[:k]
heapq.heapify(min_heap)
```

### Mistake 4: Comparing When Heap Not Full

```python
# ❌ Wrong
for num in nums:
    if num > min_heap[0]:  # Fails if heap empty!
        heappush(min_heap, num)

# ✅ Correct
for num in nums:
    heappush(min_heap, num)
    if len(min_heap) > k:
        heappop(min_heap)
```

---

## Pattern Recognition: When to Use Heaps

### Use Heap When:

1. **Finding Kth largest/smallest**
   - This problem
   - Kth smallest element

2. **Top K elements**
   - Top K frequent elements
   - K closest points

3. **Streaming/online data**
   - Running median
   - Sliding window maximum

4. **Merge K sorted lists**
   - Merge K sorted arrays
   - Find smallest range

### Heap Choice:

**Min Heap for K largest:**
- Keep K largest elements
- Remove smallest when full
- Top = Kth largest

**Max Heap for K smallest:**
- Keep K smallest elements
- Remove largest when full
- Top = Kth smallest

---

## Optimization Techniques

### Optimization 1: heapreplace()

```python
# Slower
if nums[i] > min_heap[0]:
    heapq.heappop(min_heap)
    heapq.heappush(min_heap, nums[i])

# Faster: O(log k) instead of 2×O(log k)
if nums[i] > min_heap[0]:
    heapq.heapreplace(min_heap, nums[i])
```

### Optimization 2: Early Termination

```python
def findKthLargest(nums, k):
    """
    If k is close to n, find (n-k+1)th smallest instead.
    """
    n = len(nums)
    
    # Find kth largest = find (n-k+1)th smallest
    if k > n // 2:
        # Use max heap for small number of smallest
        return findNminusKthSmallest(nums, n - k + 1)
    else:
        # Use min heap for k largest
        return findKthLargestMinHeap(nums, k)
```

### Optimization 3: Median of Medians (QuickSelect)

```python
def median_of_medians(nums, left, right):
    """
    Guaranteed O(n) worst case pivot selection.
    More complex but theoretically optimal.
    """
    if right - left < 5:
        return sorted(nums[left:right+1])[len(nums[left:right+1])//2]
    
    # Divide into groups of 5, find medians
    medians = []
    for i in range(left, right + 1, 5):
        group = sorted(nums[i:min(i+5, right+1)])
        medians.append(group[len(group)//2])
    
    return median_of_medians(medians, 0, len(medians) - 1)
```

---

## Related Problems

### Direct Applications

1. **LeetCode 703: Kth Largest Element in a Stream**
   - Maintain min heap of size K
   - Add elements dynamically

2. **LeetCode 973: K Closest Points to Origin**
   - Max heap of size K (by distance)
   - Same pattern

3. **LeetCode 347: Top K Frequent Elements**
   - Min heap by frequency
   - Bucket sort alternative

### Similar Patterns

4. **LeetCode 295: Find Median from Data Stream**
   - Two heaps (min + max)
   - Balance sizes

5. **LeetCode 480: Sliding Window Median**
   - Combination of techniques
   - Harder version

6. **LeetCode 378: Kth Smallest Element in Sorted Matrix**
   - Min heap with coordinates
   - 2D variation

---

## Interview Discussion

### Questions to Ask

1. **"Can I modify the input array?"**
   - QuickSelect modifies, others don't

2. **"Is k guaranteed to be valid?"**
   - Problem states yes, but good to check

3. **"What about memory constraints?"**
   - Heap uses O(k) vs O(1) for QuickSelect

4. **"Is the data streaming or static?"**
   - Streaming → must use heap
   - Static → can use QuickSelect

### Explaining Your Solution

**For Min Heap Approach:**

1. "I'll use a min heap of size K to track the K largest elements"

2. "The top of the min heap will always be the smallest of these K largest elements"

3. "That makes it the Kth largest overall"

4. "Time complexity is O(n log k), which is better than sorting when k is small"

### Follow-up Questions

**Q: What if array is too large for memory?**
A: External sorting or streaming with heap.

**Q: What if k is very large?**
A: QuickSelect or find (n-k)th smallest instead.

**Q: Can you do it in O(n) guaranteed?**
A: Yes, using Median of Medians for pivot selection in QuickSelect.

**Q: What if we need multiple queries?**
A: Sort once, then O(1) per query.

---

## Testing & Validation

### Test Cases

```python
def test_kth_largest():
    solution = Solution()
    
    # Test 1: Basic case
    assert solution.findKthLargest([3,2,1,5,6,4], 2) == 5
    
    # Test 2: With duplicates
    assert solution.findKthLargest([3,2,3,1,2,4,5,5,6], 4) == 4
    
    # Test 3: k=1 (largest)
    assert solution.findKthLargest([1,2,3,4,5], 1) == 5
    
    # Test 4: k=n (smallest)
    assert solution.findKthLargest([1,2,3,4,5], 5) == 1
    
    # Test 5: Single element
    assert solution.findKthLargest([1], 1) == 1
    
    # Test 6: Two elements
    assert solution.findKthLargest([1,2], 1) == 2
    assert solution.findKthLargest([1,2], 2) == 1
    
    # Test 7: All same
    assert solution.findKthLargest([5,5,5,5], 2) == 5
    
    # Test 8: Negative numbers
    assert solution.findKthLargest([-1,-5,-3], 1) == -1
    assert solution.findKthLargest([-1,-5,-3], 3) == -5
    
    # Test 9: Large array
    nums = list(range(1000))
    assert solution.findKthLargest(nums, 100) == 900
    
    print("All tests passed!")
```

---

## Summary

### Key Takeaways

1. **Min heap of size K** is optimal for small k: O(n log k)
2. **QuickSelect** is optimal average case: O(n)
3. **Top of min heap** = Kth largest (smallest of K largest)
4. **Maintain heap size K** by removing smallest when full
5. **Works for streaming data** unlike QuickSelect

### Recommended Approach

```python
def findKthLargest(nums, k):
    """Min heap approach - best for interviews."""
    min_heap = []
    
    for num in nums:
        heapq.heappush(min_heap, num)
        if len(min_heap) > k:
            heapq.heappop(min_heap)
    
    return min_heap[0]
```

### Complexity
- **Time**: O(n log k)
- **Space**: O(k)

---

**Tags**: #heap #kth-element #quickselect #priority-queue #medium  
**Related**: Kth Largest in Stream, K Closest Points, Top K Frequent  
**Companies**: Amazon, Facebook, Microsoft, Google, Apple
