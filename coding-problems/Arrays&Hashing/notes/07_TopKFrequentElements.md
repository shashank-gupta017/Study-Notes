# Top K Frequent Elements (Medium)

## Problem Statement
Given an integer array `nums` and an integer `k`, return the `k` most frequent elements. You may return the answer in **any order**.

**Guaranteed Constraint**: The answer is **unique** (there is exactly one valid solution).

**Follow-up**: Your algorithm's time complexity must be **better than O(n log n)**, where n is the array's size.

**LeetCode Link**: [347. Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/)

---

## Examples

### Example 1:
```
Input: nums = [1,1,1,2,2,3], k = 2
Output: [1,2]
Explanation: 
1 appears 3 times
2 appears 2 times
3 appears 1 time
The 2 most frequent elements are [1, 2]
Order doesn't matter - [2, 1] is also acceptable
```

### Example 2:
```
Input: nums = [1], k = 1
Output: [1]
Explanation: 
Only one element, and we need 1 most frequent
```

### Example 3:
```
Input: nums = [4,1,-1,2,-1,2,3], k = 2
Output: [-1,2]
Explanation:
-1 appears 2 times
2 appears 2 times
4 appears 1 time
1 appears 1 time
3 appears 1 time
Top 2 are -1 and 2 (both have frequency 2)
```

### Example 4:
```
Input: nums = [1,2,2,3,3,3], k = 1
Output: [3]
Explanation:
3 appears 3 times (most frequent)
```

### Example 5:
```
Input: nums = [5,5,5,5,2,2,1], k = 2
Output: [5,2]
Explanation:
5 appears 4 times
2 appears 2 times
1 appears 1 time
Top 2 are [5, 2]
```

---

## Constraints
- `1 <= nums.length <= 10^5`
- `-10^4 <= nums[i] <= 10^4`
- `k` is in the range `[1, number of unique elements]`
- The answer is **guaranteed to be unique**

---

## Pattern Recognition

This is a **HashMap + Heap (or Bucket Sort)** problem because:
1. We need to **count frequency** of elements → HashMap
2. We need **top k** elements → Heap or sorting
3. **Order doesn't matter** → Flexible data structure choices
4. Must be **better than O(n log n)** → Rules out naive sorting

**Key Insight**: This is a **Top K problem** pattern!
- First phase: Count frequencies with HashMap
- Second phase: Find top k using:
  - **Min Heap** (size k): O(n log k) - GOOD ✅
  - **Max Heap** (size n): O(n log n) - Works but slower
  - **Bucket Sort**: O(n) - OPTIMAL! ⭐
  - **QuickSelect**: O(n) average - Also optimal

**Pattern Template**:
```
1. Build frequency map
2. Extract top k using one of:
   - Min heap (k elements)
   - Bucket sort (frequency buckets)
   - QuickSelect (nth element selection)
```

---

## Approaches

### Approach 1: HashMap + Sorting (BASELINE)
**Idea**: Count frequencies, sort by frequency, take top k.

```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // Step 1: Count frequencies
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Create list of (element, frequency) pairs
        List<int[]> pairs = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
            pairs.add(new int[]{entry.getKey(), entry.getValue()});
        }
        
        // Step 3: Sort by frequency (descending)
        pairs.sort((a, b) -> b[1] - a[1]);
        
        // Step 4: Take top k elements
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = pairs.get(i)[0];
        }
        
        return result;
    }
}
```

**Time Complexity**: O(n log n)
- O(n) to build frequency map
- O(n log n) to sort pairs
- O(k) to extract result

**Space Complexity**: O(n) for map and list

**Problem**: O(n log n) doesn't meet the "better than O(n log n)" requirement!

---

### Approach 2: HashMap + Min Heap (OPTIMAL for interview) ⭐
**Idea**: Use min heap of size k. Keep only top k frequent elements.

```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // Step 1: Build frequency map
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Min heap of size k (min frequency at top)
        // PriorityQueue defaults to min heap
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            (a, b) -> freqMap.get(a) - freqMap.get(b)
        );
        
        // Step 3: Maintain heap of size k
        for (int num : freqMap.keySet()) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll(); // Remove element with minimum frequency
            }
        }
        
        // Step 4: Extract result from heap
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll();
        }
        
        return result;
    }
}
```

**Time Complexity**: O(n log k)
- O(n) to build frequency map
- O(n log k) to maintain heap of size k
  - For each of n unique elements: offer is O(log k), poll is O(log k)
- O(k log k) to extract result (but k < n, so dominated by n log k)

**Space Complexity**: O(n) for map + O(k) for heap = O(n)

**Why Min Heap, not Max Heap?**
- Min heap keeps **smallest frequency at top**
- When heap exceeds size k, we **remove smallest** (least frequent)
- This ensures we keep the **k most frequent** elements!

---

### Approach 3: HashMap + Max Heap
**Idea**: Put all elements in max heap, extract top k.

```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // Step 1: Build frequency map
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Max heap (largest frequency at top)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
            (a, b) -> freqMap.get(b) - freqMap.get(a)
        );
        
        // Step 3: Add all elements to heap
        maxHeap.addAll(freqMap.keySet());
        
        // Step 4: Extract top k
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = maxHeap.poll();
        }
        
        return result;
    }
}
```

**Time Complexity**: O(n log n)
- O(n) to build frequency map
- O(n log n) to heapify all n unique elements
- O(k log n) to extract k elements

**Space Complexity**: O(n) for map + O(n) for heap = O(n)

**Problem**: O(n log n) - same as sorting! Not optimal.

---

### Approach 4: Bucket Sort (OPTIMAL - O(n)) ⭐⭐⭐
**Idea**: Use array of buckets indexed by frequency. Elements with same frequency go in same bucket.

```java
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        // Step 1: Build frequency map
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Create buckets (index = frequency)
        // Maximum frequency is nums.length (all same element)
        List<Integer>[] buckets = new List[nums.length + 1];
        
        // Step 3: Place elements in buckets by their frequency
        for (int num : freqMap.keySet()) {
            int freq = freqMap.get(num);
            if (buckets[freq] == null) {
                buckets[freq] = new ArrayList<>();
            }
            buckets[freq].add(num);
        }
        
        // Step 4: Collect top k elements from highest frequency buckets
        int[] result = new int[k];
        int index = 0;
        
        // Iterate from highest frequency to lowest
        for (int freq = buckets.length - 1; freq >= 0 && index < k; freq--) {
            if (buckets[freq] != null) {
                for (int num : buckets[freq]) {
                    result[index++] = num;
                    if (index == k) {
                        return result;
                    }
                }
            }
        }
        
        return result;
    }
}
```

**Time Complexity**: O(n)
- O(n) to build frequency map
- O(n) to place elements in buckets
- O(n) to collect top k (worst case: scan all buckets)

**Space Complexity**: O(n) for map + O(n) for buckets = O(n)

**This is OPTIMAL!** Linear time!

**Key Insight**: 
- Frequency range is limited: [1, n] where n = array length
- We can use counting sort / bucket sort strategy
- No comparisons needed!

---

### Approach 5: QuickSelect (Advanced)
**Idea**: Use partition algorithm to find kth most frequent element, similar to quicksort.

```java
class Solution {
    private Map<Integer, Integer> freqMap;
    
    public int[] topKFrequent(int[] nums, int k) {
        // Step 1: Build frequency map
        freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Create array of unique elements
        int[] unique = new int[freqMap.size()];
        int i = 0;
        for (int num : freqMap.keySet()) {
            unique[i++] = num;
        }
        
        // Step 3: Use QuickSelect to partition
        // Find (n - k)th element, so elements after it are top k
        int n = unique.length;
        quickSelect(unique, 0, n - 1, n - k);
        
        // Step 4: Return last k elements
        return Arrays.copyOfRange(unique, n - k, n);
    }
    
    private void quickSelect(int[] nums, int left, int right, int kSmallest) {
        if (left == right) return;
        
        // Partition around pivot
        int pivotIndex = partition(nums, left, right);
        
        // Check if pivot is at kSmallest position
        if (pivotIndex == kSmallest) {
            return;
        } else if (pivotIndex < kSmallest) {
            // Search right part
            quickSelect(nums, pivotIndex + 1, right, kSmallest);
        } else {
            // Search left part
            quickSelect(nums, left, pivotIndex - 1, kSmallest);
        }
    }
    
    private int partition(int[] nums, int left, int right) {
        // Choose random pivot to avoid worst case
        int randomIndex = left + new Random().nextInt(right - left + 1);
        swap(nums, randomIndex, right);
        
        int pivot = freqMap.get(nums[right]);
        int storeIndex = left;
        
        // Move elements less than pivot to left
        for (int i = left; i < right; i++) {
            if (freqMap.get(nums[i]) < pivot) {
                swap(nums, i, storeIndex);
                storeIndex++;
            }
        }
        
        // Move pivot to its final position
        swap(nums, storeIndex, right);
        return storeIndex;
    }
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
```

**Time Complexity**: 
- **Average**: O(n) - each partition reduces search space by half on average
- **Worst**: O(n²) - if bad pivots chosen (rare with random selection)

**Space Complexity**: O(n) for map and unique array

**Trade-off**: Faster average case but more complex code. Bucket sort is simpler and guaranteed O(n).

---

## Visual Step-by-Step Walkthrough (Min Heap Approach)

### Example: nums = [1,1,1,2,2,3], k = 2

```
Step 1: Build Frequency Map
nums = [1, 1, 1, 2, 2, 3]
      
freqMap = {
    1 → 3
    2 → 2
    3 → 1
}

Step 2: Process elements with Min Heap (size k = 2)

Initial: minHeap = []

Process 1 (freq=3):
    minHeap.offer(1)
    minHeap = [1]  (freq: 3)
    size = 1 ≤ k, continue

Process 2 (freq=2):
    minHeap.offer(2)
    minHeap = [2, 1]  (freq: 2 at top, 3 below)
    size = 2 ≤ k, continue

Process 3 (freq=1):
    minHeap.offer(3)
    minHeap = [3, 1, 2]  (freq: 1 at top)
    size = 3 > k! Remove min
    minHeap.poll() removes 3 (freq=1, smallest)
    minHeap = [2, 1]  (freq: 2 and 3)

Step 3: Extract Result
    result = [2, 1] or [1, 2]
    Order doesn't matter!

Final Answer: [1, 2]
```

### Detailed Heap State Changes:

```
Initial State:
freqMap: {1:3, 2:2, 3:1}
minHeap: []  (empty)
k = 2

After adding 1 (freq=3):
minHeap: [1]
         ↑
      freq=3

After adding 2 (freq=2):
minHeap:    [2]          Min at top (freq=2)
           /   
         [1]             (freq=3)
         
Heap property: parent ≤ children (by frequency)

After adding 3 (freq=1):
minHeap:    [3]          Min at top (freq=1)
           /   \
         [1]   [2]       (freq=3) (freq=2)

Size > k, so poll():
minHeap:    [2]          After removing 3
           /   
         [1]             

Final heap contains top k=2 most frequent: [1, 2]
```

### Visual: Bucket Sort Approach

```
Step 1: Build Frequency Map
nums = [1, 1, 1, 2, 2, 3]
freqMap = {1:3, 2:2, 3:1}

Step 2: Create Buckets (index = frequency)
buckets[0]: []  (no element with freq 0)
buckets[1]: [3]  (element 3 appears 1 time)
buckets[2]: [2]  (element 2 appears 2 times)
buckets[3]: [1]  (element 1 appears 3 times)
buckets[4]: []  (no element with freq 4)
buckets[5]: []
buckets[6]: []  (max possible is array length)

Step 3: Collect top k=2 from highest frequency
Start from bucket[6] → empty
         bucket[5] → empty
         bucket[4] → empty
         bucket[3] → [1] ✓ Add 1 to result, count=1
         bucket[2] → [2] ✓ Add 2 to result, count=2
         count == k, DONE!

Result: [1, 2]
```

### Comparison of Approaches on Same Example:

```
Input: [1,1,1,2,2,3], k=2

Approach 1: HashMap + Sorting
    freqMap: {1:3, 2:2, 3:1}
    pairs: [(1,3), (2,2), (3,1)]
    sorted: [(1,3), (2,2), (3,1)]
    result: [1, 2]
    Time: O(n log n)

Approach 2: HashMap + Min Heap
    freqMap: {1:3, 2:2, 3:1}
    heap: [2,1] (after processing all)
    result: [1, 2]
    Time: O(n log k)

Approach 3: HashMap + Max Heap
    freqMap: {1:3, 2:2, 3:1}
    heap: [1,2,3] (all elements)
    poll k=2 times: [1, 2]
    result: [1, 2]
    Time: O(n log n)

Approach 4: Bucket Sort
    freqMap: {1:3, 2:2, 3:1}
    buckets: [[], [3], [2], [1], [], [], []]
    scan from end: [1, 2]
    result: [1, 2]
    Time: O(n) ⭐

Approach 5: QuickSelect
    freqMap: {1:3, 2:2, 3:1}
    unique: [1, 2, 3]
    partition to find (3-2)=1st smallest
    after partition: [3, 1, 2] or similar
    return last 2: [1, 2]
    Time: O(n) average
```

---

## Edge Cases to Consider

```java
// Test Case 1: Single element
Input: nums = [1], k = 1
Output: [1]
// Only one unique element

// Test Case 2: All elements unique, k = n
Input: nums = [1, 2, 3, 4, 5], k = 5
Output: [1, 2, 3, 4, 5]
// All elements have same frequency (1), return all

// Test Case 3: All elements same
Input: nums = [1, 1, 1, 1, 1], k = 1
Output: [1]
// Only one unique element with max frequency

// Test Case 4: k = 1 (find most frequent)
Input: nums = [1, 2, 2, 3, 3, 3], k = 1
Output: [3]
// Clear winner with highest frequency

// Test Case 5: k equals number of unique elements
Input: nums = [1, 1, 2, 2, 3, 3], k = 3
Output: [1, 2, 3]
// All elements have same frequency, return all

// Test Case 6: Two elements with same max frequency
Input: nums = [1, 1, 2, 2, 3], k = 2
Output: [1, 2]
// Both 1 and 2 have frequency 2, 3 has frequency 1

// Test Case 7: Negative numbers
Input: nums = [-1, -1, -2, -2, -2], k = 1
Output: [-2]
// Works with negative numbers

// Test Case 8: Mix of positive and negative
Input: nums = [-1, -1, 0, 0, 0, 1, 1, 1, 1], k = 2
Output: [1, 0] or [0, 1]
// Order doesn't matter

// Test Case 9: Large array with clear top k
Input: nums = [1]*100 + [2]*50 + [3]*25 + [4]*10, k = 2
Output: [1, 2]
// Clear frequency distribution

// Test Case 10: All elements appear once
Input: nums = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10], k = 3
Output: any 3 elements like [1, 2, 3]
// All have same frequency, any k elements valid

// Test Case 11: k = 0 (though constraint says k ≥ 1)
Input: nums = [1, 2, 3], k = 0
Output: []
// Edge case: empty result (but problem guarantees k ≥ 1)

// Test Case 12: Very large k
Input: nums = [1, 1, 2], k = 2
Output: [1, 2]
// k equals number of unique elements

// Test Case 13: Frequency pattern with gaps
Input: nums = [1]*10 + [2]*8 + [3]*6 + [4]*4 + [5]*2 + [6]*1, k = 3
Output: [1, 2, 3]
// Clear frequency ordering

// Test Case 14: Zero in array
Input: nums = [0, 0, 0, 1, 1], k = 1
Output: [0]
// Zero is valid element

// Test Case 15: Array length exactly k
Input: nums = [1, 2, 3], k = 3
Output: [1, 2, 3]
// All unique elements, return all
```

---

## Common Mistakes to Avoid

### Mistake 1: Using Max Heap When Min Heap is Better
```java
// ❌ WRONG: Max heap stores all n elements
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
    (a, b) -> freqMap.get(b) - freqMap.get(a)
);
maxHeap.addAll(freqMap.keySet()); // O(n log n)

// ✅ CORRECT: Min heap stores only k elements
PriorityQueue<Integer> minHeap = new PriorityQueue<>(
    (a, b) -> freqMap.get(a) - freqMap.get(b)
);
for (int num : freqMap.keySet()) {
    minHeap.offer(num);
    if (minHeap.size() > k) {
        minHeap.poll(); // O(log k)
    }
}
// Total: O(n log k) which is better!
```

### Mistake 2: Incorrect Comparator
```java
// ❌ WRONG: Comparing elements instead of frequencies
PriorityQueue<Integer> heap = new PriorityQueue<>(
    (a, b) -> a - b  // This compares values, not frequencies!
);

// ✅ CORRECT: Compare frequencies
PriorityQueue<Integer> heap = new PriorityQueue<>(
    (a, b) -> freqMap.get(a) - freqMap.get(b)  // Compare by frequency
);
```

### Mistake 3: Not Handling HashMap Correctly
```java
// ❌ WRONG: NullPointerException possible
for (int num : nums) {
    freqMap.put(num, freqMap.get(num) + 1);  // NPE if num not in map!
}

// ✅ CORRECT: Use getOrDefault
for (int num : nums) {
    freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
}

// ✅ ALSO CORRECT: Check null
for (int num : nums) {
    Integer count = freqMap.get(num);
    freqMap.put(num, count == null ? 1 : count + 1);
}
```

### Mistake 4: Wrong Bucket Array Size
```java
// ❌ WRONG: Insufficient size (frequency can be nums.length)
List<Integer>[] buckets = new List[nums.length];  // Index out of bounds!

// ✅ CORRECT: Size should be nums.length + 1
List<Integer>[] buckets = new List[nums.length + 1];
// Because frequency ranges from 0 to nums.length
```

### Mistake 5: Not Initializing Bucket Lists
```java
// ❌ WRONG: NullPointerException when adding
List<Integer>[] buckets = new List[nums.length + 1];
buckets[freq].add(num);  // NPE! buckets[freq] is null

// ✅ CORRECT: Initialize before adding
if (buckets[freq] == null) {
    buckets[freq] = new ArrayList<>();
}
buckets[freq].add(num);
```

### Mistake 6: Early Termination Bug in Result Collection
```java
// ❌ WRONG: Return inside inner loop
for (int freq = buckets.length - 1; freq >= 0; freq--) {
    if (buckets[freq] != null) {
        for (int num : buckets[freq]) {
            result[index++] = num;
            return result;  // BUG: Returns after first element!
        }
    }
}

// ✅ CORRECT: Check if we have k elements
for (int freq = buckets.length - 1; freq >= 0 && index < k; freq--) {
    if (buckets[freq] != null) {
        for (int num : buckets[freq]) {
            result[index++] = num;
            if (index == k) {
                return result;  // Correct: return when k elements collected
            }
        }
    }
}
```

### Mistake 7: Overflow in Comparator
```java
// ❌ WRONG: Potential overflow if frequencies are large
PriorityQueue<Integer> heap = new PriorityQueue<>(
    (a, b) -> freqMap.get(a) - freqMap.get(b)  // Can overflow!
);

// ✅ CORRECT: Use Integer.compare to avoid overflow
PriorityQueue<Integer> heap = new PriorityQueue<>(
    (a, b) -> Integer.compare(freqMap.get(a), freqMap.get(b))
);

// ✅ ALSO CORRECT: Use compare method
PriorityQueue<Integer> heap = new PriorityQueue<>(
    Comparator.comparingInt(freqMap::get)
);
```

### Mistake 8: Modifying Map While Iterating
```java
// ❌ WRONG: ConcurrentModificationException
for (int num : freqMap.keySet()) {
    // Some logic
    freqMap.remove(anotherNum);  // Modifying while iterating!
}

// ✅ CORRECT: Iterate over copy or use iterator
List<Integer> keys = new ArrayList<>(freqMap.keySet());
for (int num : keys) {
    // Safe to modify freqMap now
}
```

---

## Interview Tips

### When to Use Each Approach:

**Use Min Heap (Approach 2)** when:
- Interviewer asks for better than O(n log n) ✓
- You want clean, understandable code ✓
- k is much smaller than n ✓
- **Best for interviews!** Most intuitive explanation

**Use Bucket Sort (Approach 4)** when:
- Interviewer pushes for O(n) solution ✓
- You want to show advanced knowledge ✓
- After implementing heap, show optimization ✓
- Frequency range is limited (always true here)

**Use QuickSelect (Approach 5)** when:
- You want to impress with advanced algorithms ✓
- Discussing trade-offs (average vs worst case) ✓
- Follow-up about selection algorithms ✓

### Discussion Points with Interviewer:

**Start with**: "I'll use a HashMap to count frequencies, then find the top k."

**Decision point**: "For finding top k, I can use:
1. Min heap of size k: O(n log k) - clean and efficient
2. Bucket sort: O(n) - optimal but slightly more complex
3. QuickSelect: O(n) average - fast but less predictable

I'll implement min heap first for clarity, then discuss bucket sort optimization."

### PriorityQueue Deep Dive:

```java
// Min Heap (default)
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
// Natural ordering: smallest at top

// Max Heap (reverse order)
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
// Largest at top

// Custom comparator by frequency
PriorityQueue<Integer> heap = new PriorityQueue<>(
    (a, b) -> freqMap.get(a) - freqMap.get(b)
);
// Element with smallest frequency at top

// Why use min heap for "top k"?
// - Keep heap size = k
// - Remove minimum when size exceeds k
// - Final heap has k largest frequencies!
```

### Key Operations:
```java
// Add element: O(log k)
heap.offer(element);

// Remove minimum: O(log k)
int min = heap.poll();

// Peek minimum: O(1)
int min = heap.peek();

// Check size: O(1)
int size = heap.size();

// Pattern for top k:
for (int element : allElements) {
    heap.offer(element);
    if (heap.size() > k) {
        heap.poll(); // Remove smallest
    }
}
// Now heap contains k largest!
```

### Expected Follow-up Questions:

**Q**: "Why not just sort the frequency map?"
**A**: "Sorting is O(n log n). With min heap, we get O(n log k), which is better when k << n. For k = 10 and n = 1,000,000, heap is ~50x faster!"

**Q**: "Can you do better than O(n log k)?"
**A**: "Yes! Bucket sort achieves O(n) by using frequency as index. Since frequency is bounded by array length, we can avoid comparisons entirely. Let me show you..." [Implement bucket sort]

**Q**: "What if k is very large, close to n?"
**A**: "If k ≈ n, max heap might be simpler - just add all and extract k. Or bucket sort is still O(n) and handles all k values equally well."

**Q**: "How do you handle ties in frequency?"
**A**: "Problem guarantees unique answer, so no ties in top k boundary. But if there were, we'd return any valid set - bucket sort or heap both handle this naturally by arbitrary ordering."

**Q**: "What's the space complexity?"
**A**: "O(n) for frequency map is required. Min heap adds O(k), bucket sort adds O(n) for buckets. Total is O(n) for all approaches."

**Q**: "Can you do it in-place?"
**A**: "No, we need the frequency map which is O(n) space. The frequency counting step fundamentally requires extra space."

**Q**: "What if the array is streamed (elements arrive one at a time)?"
**A**: "I'd maintain frequency map and min heap continuously. As new elements arrive, update frequency and adjust heap. More complex but possible with O(k) space overhead for heap."

---

## Complete Solution with Comments

```java
import java.util.*;

class Solution {
    /**
     * Finds the k most frequent elements in the array.
     * 
     * Approach: HashMap for frequency counting + Min Heap for top k selection
     * 
     * @param nums Input array of integers
     * @param k Number of most frequent elements to return
     * @return Array containing k most frequent elements in any order
     * 
     * Time Complexity: O(n log k)
     *   - O(n) to build frequency map
     *   - O(n log k) to maintain heap of size k
     *   - O(k log k) to extract result
     * 
     * Space Complexity: O(n)
     *   - O(n) for frequency map (worst case: all unique elements)
     *   - O(k) for heap
     */
    public int[] topKFrequent(int[] nums, int k) {
        // Edge case: if k equals array length, return all elements
        if (k == nums.length) {
            return nums;
        }
        
        // Step 1: Build frequency map
        // Key: element, Value: frequency count
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Create min heap ordered by frequency
        // Min heap keeps element with SMALLEST frequency at top
        // This allows us to efficiently remove least frequent when heap > k
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(
            (a, b) -> freqMap.get(a) - freqMap.get(b)
        );
        
        // Step 3: Process each unique element
        // Maintain heap of size k containing k most frequent elements
        for (int num : freqMap.keySet()) {
            minHeap.offer(num);
            
            // When heap exceeds size k, remove element with minimum frequency
            if (minHeap.size() > k) {
                minHeap.poll(); // Removes element at top (smallest frequency)
            }
        }
        
        // Step 4: Extract result from heap
        // Heap now contains exactly k elements with highest frequencies
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = minHeap.poll();
        }
        
        return result;
    }
}

/**
 * Alternative: Bucket Sort Solution (O(n) time)
 * Use when interviewer wants optimal time complexity
 */
class SolutionBucketSort {
    public int[] topKFrequent(int[] nums, int k) {
        // Step 1: Build frequency map
        Map<Integer, Integer> freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Create buckets indexed by frequency
        // buckets[i] contains all elements with frequency i
        // Maximum frequency is nums.length (all elements same)
        List<Integer>[] buckets = new List[nums.length + 1];
        
        // Step 3: Distribute elements into buckets
        for (int num : freqMap.keySet()) {
            int freq = freqMap.get(num);
            if (buckets[freq] == null) {
                buckets[freq] = new ArrayList<>();
            }
            buckets[freq].add(num);
        }
        
        // Step 4: Collect top k elements from highest frequency buckets
        int[] result = new int[k];
        int index = 0;
        
        // Iterate from highest frequency to lowest
        for (int freq = buckets.length - 1; freq >= 0 && index < k; freq--) {
            if (buckets[freq] != null) {
                // Add all elements from this frequency bucket
                for (int num : buckets[freq]) {
                    result[index++] = num;
                    if (index == k) {
                        return result; // Early termination when k elements found
                    }
                }
            }
        }
        
        return result;
    }
}

/**
 * Alternative: QuickSelect Solution (O(n) average time)
 * Advanced approach using partition algorithm
 */
class SolutionQuickSelect {
    private Map<Integer, Integer> freqMap;
    
    public int[] topKFrequent(int[] nums, int k) {
        // Step 1: Build frequency map
        freqMap = new HashMap<>();
        for (int num : nums) {
            freqMap.put(num, freqMap.getOrDefault(num, 0) + 1);
        }
        
        // Step 2: Create array of unique elements
        int n = freqMap.size();
        int[] unique = new int[n];
        int i = 0;
        for (int num : freqMap.keySet()) {
            unique[i++] = num;
        }
        
        // Step 3: Use QuickSelect to find (n-k)th smallest by frequency
        // Elements after position (n-k) will be the k most frequent
        quickSelect(unique, 0, n - 1, n - k);
        
        // Step 4: Return last k elements (most frequent)
        return Arrays.copyOfRange(unique, n - k, n);
    }
    
    /**
     * QuickSelect algorithm to partition array
     * Places kSmallest element at correct position
     */
    private void quickSelect(int[] nums, int left, int right, int kSmallest) {
        // Base case: single element
        if (left == right) {
            return;
        }
        
        // Partition array and get pivot position
        int pivotIndex = partition(nums, left, right);
        
        // Check if pivot is at target position
        if (pivotIndex == kSmallest) {
            return; // Found! Elements after pivotIndex are top k
        } else if (pivotIndex < kSmallest) {
            // Target is in right part
            quickSelect(nums, pivotIndex + 1, right, kSmallest);
        } else {
            // Target is in left part
            quickSelect(nums, left, pivotIndex - 1, kSmallest);
        }
    }
    
    /**
     * Partition array by frequency
     * Returns final position of pivot
     */
    private int partition(int[] nums, int left, int right) {
        // Choose random pivot to avoid worst case O(n²)
        int randomIndex = left + new Random().nextInt(right - left + 1);
        swap(nums, randomIndex, right);
        
        int pivotFreq = freqMap.get(nums[right]);
        int storeIndex = left;
        
        // Move elements with frequency < pivot to left
        for (int i = left; i < right; i++) {
            if (freqMap.get(nums[i]) < pivotFreq) {
                swap(nums, i, storeIndex);
                storeIndex++;
            }
        }
        
        // Place pivot at final position
        swap(nums, storeIndex, right);
        return storeIndex;
    }
    
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public static void main(String[] args) {
    Solution solution = new Solution();
    
    // Test Case 1: Standard case
    int[] nums1 = {1, 1, 1, 2, 2, 3};
    int k1 = 2;
    System.out.println(Arrays.toString(solution.topKFrequent(nums1, k1)));
    // Expected: [1, 2] or [2, 1]
    
    // Test Case 2: Single element
    int[] nums2 = {1};
    int k2 = 1;
    System.out.println(Arrays.toString(solution.topKFrequent(nums2, k2)));
    // Expected: [1]
    
    // Test Case 3: All elements same frequency
    int[] nums3 = {1, 2, 3, 4, 5};
    int k3 = 3;
    System.out.println(Arrays.toString(solution.topKFrequent(nums3, k3)));
    // Expected: any 3 elements
    
    // Test Case 4: Clear winner
    int[] nums4 = {4, 1, -1, 2, -1, 2, 3};
    int k4 = 2;
    System.out.println(Arrays.toString(solution.topKFrequent(nums4, k4)));
    // Expected: [-1, 2] or [2, -1]
    
    // Test Case 5: All elements unique
    int[] nums5 = {1, 2, 3};
    int k5 = 3;
    System.out.println(Arrays.toString(solution.topKFrequent(nums5, k5)));
    // Expected: [1, 2, 3] in any order
    
    // Test Case 6: Large frequency difference
    int[] nums6 = {1, 1, 1, 1, 1, 2, 3};
    int k6 = 1;
    System.out.println(Arrays.toString(solution.topKFrequent(nums6, k6)));
    // Expected: [1]
}
```

### Debugging Tips:
```java
// Print frequency map
System.out.println("Frequency Map: " + freqMap);

// Print heap state
System.out.println("Heap size: " + minHeap.size());
System.out.println("Heap contents: " + minHeap);

// Verify k is valid
assert k >= 1 && k <= freqMap.size();

// Check result length
assert result.length == k;

// Verify all result elements exist in input
for (int num : result) {
    assert freqMap.containsKey(num);
}
```

---

## Key Takeaways

1. ✅ **Two-phase approach**: Count frequencies first, then select top k
2. ✅ **Min heap for top k**: Keep heap size k, remove minimum when exceeded
3. ✅ **Time complexity hierarchy**: Sorting O(n log n) → Min heap O(n log k) → Bucket sort O(n)
4. ✅ **Min heap > Max heap**: For top k, min heap of size k beats max heap of size n
5. ✅ **Bucket sort is optimal**: O(n) time when frequency range is limited
6. ✅ **HashMap.getOrDefault()**: Clean way to handle missing keys
7. ✅ **PriorityQueue comparator**: Compare by frequency, not value
8. ✅ **Bucket array size**: Must be `nums.length + 1` (frequencies 0 to n)
9. ✅ **Order doesn't matter**: Can return result in any order (flexibility!)
10. ✅ **Pattern is reusable**: Works for "top k" problems with any metric

---

## Complexity Comparison Table

| Approach | Time Complexity | Space Complexity | Notes |
|----------|----------------|------------------|-------|
| HashMap + Sorting | O(n log n) | O(n) | Simple but doesn't meet requirement |
| HashMap + Min Heap | O(n log k) | O(n) | **Best for interviews** ⭐ |
| HashMap + Max Heap | O(n log n) | O(n) | No advantage over sorting |
| Bucket Sort | O(n) | O(n) | **Optimal time** ⭐⭐ |
| QuickSelect | O(n) avg, O(n²) worst | O(n) | Fast but complex |

Where n = array length, k = number of top elements to find

---

## Pattern Recognition Checklist

Use this pattern when you see:
- ✅ Find "top k" or "k most/least" of something
- ✅ Frequency or count-based selection
- ✅ Order doesn't matter in result
- ✅ Can count occurrences with HashMap
- ✅ Need better than O(n log n)

Related problems using same pattern:
1. **Top K Frequent Words** (Medium) - Same pattern with strings
2. **Kth Largest Element** (Medium) - Top k with min heap
3. **Sort Characters By Frequency** (Medium) - Frequency + sorting
4. **Reorganize String** (Medium) - Max heap with frequencies
5. **Task Scheduler** (Medium) - Greedy with frequency counting

---

## Variations & Extensions

After mastering Top K Frequent Elements, these follow naturally:

1. **Top K Frequent Words** (Medium) - Add lexicographic ordering for ties
2. **Kth Largest Element in Array** (Medium) - Min heap of size k
3. **Find K Pairs with Smallest Sums** (Medium) - Min heap with pairs
4. **Kth Smallest Element in Sorted Matrix** (Medium) - Min heap navigation
5. **Ugly Number II** (Medium) - Generate sequence with heap

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (30-40 minutes for Medium is good)
- [ ] Implement min heap approach first
- [ ] Explain why min heap, not max heap
- [ ] Optimize to bucket sort for follow-up
- [ ] Test with all edge cases
- [ ] Explain time complexity: O(n log k) vs O(n)
- [ ] Review in 3 days (spaced repetition)
- [ ] Ready for Group Anagrams next!

---

**Pattern Mastered**: HashMap + Heap / Bucket Sort ✅  
**Difficulty**: Medium  
**Time to Master**: 30-40 minutes  
**Week 2, Day 7**: Top K pattern unlocked! 🔥

This problem teaches a fundamental pattern for "top k" selection that appears frequently in interviews. The min heap approach is elegant and efficient, while bucket sort shows optimization thinking. Master both approaches!
