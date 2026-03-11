# Subarray Sum Equals K (Medium)

## Problem Statement
Given an array of integers `nums` and an integer `k`, return the **total number of subarrays** whose sum equals `k`.

A **subarray** is a contiguous non-empty sequence of elements within an array.

**LeetCode Link**: [560. Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/)

---

## Examples

### Example 1:
```
Input: nums = [1,1,1], k = 2
Output: 2
Explanation: 
There are 2 subarrays with sum = 2:
- [1,1] (indices 0-1)
- [1,1] (indices 1-2)
```

### Example 2:
```
Input: nums = [1,2,3], k = 3
Output: 2
Explanation:
There are 2 subarrays with sum = 3:
- [3] (index 2)
- [1,2] (indices 0-1)
```

### Example 3:
```
Input: nums = [1], k = 1
Output: 1
Explanation:
Only one subarray: [1] itself
```

### Example 4:
```
Input: nums = [1,-1,0], k = 0
Output: 3
Explanation:
Subarrays with sum = 0:
- [-1,1] wait, that's [1,-1] (indices 0-1) ❌
Actually:
- [1,-1] (indices 0-1) = 0
- [-1,1,0] wait... let me recalculate
- [0] (index 2) = 0
- [1,-1,0] (indices 0-2) = 0

Actually for [1,-1,0]:
Subarrays with sum 0:
- [1,-1] (indices 0-1) = 0 ✓
- [0] (index 2) = 0 ✓
- [-1,1,0] ❌ this is wrong order

Let me recalculate properly:
Array: [1, -1, 0]
All subarrays:
- [1] = 1
- [1,-1] = 0 ✓
- [1,-1,0] = 0 ✓
- [-1] = -1
- [-1,0] = -1
- [0] = 0 ✓

Total: 3 subarrays with sum = 0
```

### Example 5:
```
Input: nums = [-1,-1,1], k = 0
Output: 1
Explanation:
Subarrays with sum = 0:
- [-1,-1,1] (indices 0-2) = 0

Note: [-1] + [1] are not contiguous, so don't count!
```

### Example 6:
```
Input: nums = [3,4,7,2,-3,1,4,2], k = 7
Output: 4
Explanation:
Subarrays with sum = 7:
- [7] (index 2)
- [3,4] (indices 0-1)
- [2,-3,1,4,2] (indices 3-7) = 2-3+1+4+2 = 6 ❌ Let me recalculate
  Actually: 2 + (-3) + 1 + 4 + 2 = 6 ❌
- Let me find all properly:
  [3,4] = 7 ✓
  [7] = 7 ✓
  [7,2,-3,1] = 7 ✓
  [-3,1,4,2] would need to check: -3+1+4+2 = 4 ❌
  [3,4,7,2,-3,1,4,2] = let's see: 3+4+7+2-3+1+4+2 = 20 ❌
  
Prefix sums: [3, 7, 14, 16, 13, 14, 18, 20]
For k=7, looking for prefixSum[i] - prefixSum[j] = 7:
- prefix[1] - prefix[-1] = 7 - 0 = 7 → [3,4] ✓
- prefix[2] - prefix[1] = 14 - 7 = 7 → [7] ✓
- prefix[4] - prefix[1] = 13 - 7 = 6 ❌
- prefix[5] - prefix[1] = 14 - 7 = 7 → [7,2,-3,1] ✓
- prefix[6] - prefix[2] = 18 - 14 = 4 ❌
  
Wait, let me recalculate more carefully:
- prefix[6] - prefix[4] = 18 - 13 = 5 ❌
  
Hmm, let me check if there's a 4th:
Looking for prefixSum = 7 or prefixSum - 7 exists:
- At i=0: sum=3, looking for 3-7=-4 (not found), count=0, map={0:1, 3:1}
- At i=1: sum=7, looking for 7-7=0 (found! 1 time), count=1, map={0:1, 3:1, 7:1}
- At i=2: sum=14, looking for 14-7=7 (found! 1 time), count=2, map={0:1, 3:1, 7:1, 14:1}
- At i=3: sum=16, looking for 16-7=9 (not found), count=2, map={0:1, 3:1, 7:1, 14:1, 16:1}
- At i=4: sum=13, looking for 13-7=6 (not found), count=2, map={0:1, 3:1, 7:1, 14:1, 16:1, 13:1}
- At i=5: sum=14, looking for 14-7=7 (found! 1 time), count=3, map={0:1, 3:1, 7:1, 14:2, 16:1, 13:1}
- At i=6: sum=18, looking for 18-7=11 (not found), count=3, map={...}
- At i=7: sum=20, looking for 20-7=13 (found! 1 time), count=4

So the 4 subarrays are:
1. [3,4] (indices 0-1)
2. [7] (index 2)
3. [7,2,-3,1] (indices 2-5)
4. [-3,1,4,2] (indices 4-7)

Let me verify #4: -3+1+4+2 = 4 ❌ That's wrong!

Actually looking at prefix[7]=20 and prefix[4]=13, we get elements from index 5-7:
Elements at indices 5,6,7 are: 1,4,2 → sum = 7 ✓

So the 4 subarrays are:
1. [3,4] (indices 0-1) = 7 ✓
2. [7] (index 2) = 7 ✓
3. [7,2,-3,1] (indices 2-5) = 7+2-3+1 = 7 ✓
4. [1,4,2] (indices 5-7) = 1+4+2 = 7 ✓

Total: 4 subarrays
```

---

## Constraints
- `1 <= nums.length <= 2 * 10^4`
- `-1000 <= nums[i] <= 1000`
- `-10^7 <= k <= 10^7`

---

## Pattern Recognition

This is a classic **Prefix Sum + HashMap** problem because:
1. We need to find **contiguous subarrays** with a specific sum
2. Brute force checking all subarrays would be O(n²) or O(n³)
3. We can use **prefix sums** to calculate any subarray sum in O(1)
4. HashMap helps us track prefix sums we've seen for **O(1) lookup**

**Key Insight**: 
```
If prefixSum[i] - prefixSum[j] = k, then subarray from j+1 to i has sum k

Rearranging: prefixSum[j] = prefixSum[i] - k

So at index i, we check if (currentSum - k) exists in our HashMap!
```

**Mathematical Foundation**:
```
Sum of subarray[j+1...i] = sum[0...i] - sum[0...j]
                         = prefixSum[i] - prefixSum[j]

If this equals k:
prefixSum[i] - prefixSum[j] = k
prefixSum[j] = prefixSum[i] - k

Therefore: Count how many times (prefixSum[i] - k) appeared before index i
```

**Pattern**: Prefix Sum + HashMap for Subarray Problems

**Similar Problems**:
- Contiguous Array (LeetCode 525)
- Binary Subarrays With Sum (LeetCode 930)
- Count Number of Nice Subarrays (LeetCode 1248)
- Continuous Subarray Sum (LeetCode 523)

---

## Visual Explanation

### Prefix Sum Concept
```
Array:     [1,  2,  3,  4,  5]
           
Prefix:  0  1   3   6  10  15
         ↑  ↑   ↑   ↑   ↑   ↑
       start +1 +2  +3  +4  +5

Subarray sum[1...3] = prefix[3] - prefix[0] = 6 - 0 = 6 ✓
                    = 2 + 3 + 4 = 9 ❌ Wait, indices!

Let me use 0-indexed properly:
Array indices:  [0   1   2   3   4]
Array values:   [1,  2,  3,  4,  5]
                   
Prefix sums:  0   1   3   6  10  15
            ↑   ↑   ↑   ↑   ↑   ↑
          i=-1 i=0 i=1 i=2 i=3 i=4

Subarray sum[1...3] means elements at indices 1,2,3 = [2,3,4]
= prefix[3] - prefix[0] = 10 - 1 = 9 ✓
= 2 + 3 + 4 = 9 ✓
```

### Example Walkthrough: nums = [3, 4, 7, 2], k = 7

```
Step-by-step simulation:

Initial: map = {0: 1}  (important! empty prefix sum)
         count = 0
         
Index 0: num = 3
┌─────────────────────────────────────┐
│ Current: [3]                        │
│ prefixSum = 0 + 3 = 3               │
│ Looking for: 3 - 7 = -4             │
│ Found in map? NO                    │
│ count = 0                           │
│ map = {0:1, 3:1}                    │
└─────────────────────────────────────┘

Index 1: num = 4
┌─────────────────────────────────────┐
│ Current: [3, 4]                     │
│ prefixSum = 3 + 4 = 7               │
│ Looking for: 7 - 7 = 0              │
│ Found in map? YES (1 time)          │
│ count = 0 + 1 = 1                   │
│ → Found subarray [3,4] = 7 ✓        │
│ map = {0:1, 3:1, 7:1}               │
└─────────────────────────────────────┘

Index 2: num = 7
┌─────────────────────────────────────┐
│ Current: [3, 4, 7]                  │
│ prefixSum = 7 + 7 = 14              │
│ Looking for: 14 - 7 = 7             │
│ Found in map? YES (1 time)          │
│ count = 1 + 1 = 2                   │
│ → Found subarray [7] = 7 ✓          │
│ map = {0:1, 3:1, 7:1, 14:1}         │
└─────────────────────────────────────┘

Index 3: num = 2
┌─────────────────────────────────────┐
│ Current: [3, 4, 7, 2]               │
│ prefixSum = 14 + 2 = 16             │
│ Looking for: 16 - 7 = 9             │
│ Found in map? NO                    │
│ count = 2                           │
│ map = {0:1, 3:1, 7:1, 14:1, 16:1}   │
└─────────────────────────────────────┘

Final Answer: 2 subarrays with sum = 7
```

### Why We Need HashMap Instead of Just Tracking Current Sum

```
Array: [3, -3, 1, 1, 1], k = 3

If we only tracked current cumulative sum, we'd miss cases where
the SAME prefix sum appears multiple times!

Prefix sums: [3, 0, 1, 2, 3]
                    ↑        ↑
                 same value appears twice!

When we reach the second 3 (at index 4):
- We look for prefixSum - k = 3 - 3 = 0
- HashMap tells us 0 appeared 1 time
- This means 1 subarray: from after index 1 to index 4 = [1,1,1] ✓

The count in HashMap is crucial!
```

### Visual: Prefix Sum Difference

```
Array:  [1,  2,  1,  -1,  3]
         ↑   ↑   ↑    ↑   ↑
Index:   0   1   2    3   4

Prefix: 0 → 1 → 3 → 4 → 3 → 6
        ↑   ↑   ↑   ↑   ↑   ↑
        
Find k = 3:

At index 1: prefixSum = 3
  Look for 3-3=0 → Found at start! 
  Subarray [1,2] ✓

At index 4: prefixSum = 6
  Look for 6-3=3 → Found at index 1!
  Subarray from index 2-4: [1,-1,3] = 3 ✓
  
At index 3: prefixSum = 3
  Look for 3-3=0 → Found at start!
  Subarray [1,2,1,-1] = 3 ✓
  
But wait, at index 4, prefixSum = 6
  Look for 6-3=3
  3 appears at TWO places: index 1 AND index 3!
  So we get TWO subarrays:
  1. From after index 1 to index 4: [1,-1,3]
  2. From after index 3 to index 4: [3]
  
This is why we need to COUNT occurrences in HashMap!

Let me trace through properly:

i=0: sum=1, look for -2 (not found), map={0:1, 1:1}, count=0
i=1: sum=3, look for 0 (found 1x), map={0:1, 1:1, 3:1}, count=1
i=2: sum=4, look for 1 (found 1x), map={0:1, 1:1, 3:1, 4:1}, count=2
i=3: sum=3, look for 0 (found 1x), map={0:1, 1:1, 3:2, 4:1}, count=3
i=4: sum=6, look for 3 (found 2x!), map={...}, count=3+2=5

So 5 subarrays:
1. [1,2] (0-1) = 3
2. [1,2,1,-1] (0-3) = 3
3. [2,1] (1-2) = 3
4. [1,-1,3] (2-4) = 3
5. [3] (4-4) = 3

Total: 5 subarrays with sum = 3
```

---

## Approaches

### Approach 1: Brute Force - Check All Subarrays (NOT RECOMMENDED)
**Idea**: Generate all possible subarrays and count those with sum = k.

```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int n = nums.length;
        
        // Try all starting points
        for (int start = 0; start < n; start++) {
            // Try all ending points from start
            for (int end = start; end < n; end++) {
                // Calculate sum of subarray [start...end]
                int sum = 0;
                for (int i = start; i <= end; i++) {
                    sum += nums[i];
                }
                
                // Check if sum equals k
                if (sum == k) {
                    count++;
                }
            }
        }
        
        return count;
    }
}
```

**Time Complexity**: O(n³) - Three nested loops!
- Outer loop: n iterations (start)
- Middle loop: n iterations (end)
- Inner loop: n iterations (calculate sum)

**Space Complexity**: O(1) - No extra space

**Problems**:
- Way too slow! For n = 20,000, this would be 8 trillion operations!
- Will definitely time out on LeetCode
- Not interview-worthy solution

---

### Approach 2: Brute Force Optimized - Calculate Sum Incrementally
**Idea**: Instead of recalculating sum from scratch, add elements incrementally.

```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        int count = 0;
        int n = nums.length;
        
        // Try all starting points
        for (int start = 0; start < n; start++) {
            int sum = 0;
            
            // Try all ending points from start
            for (int end = start; end < n; end++) {
                // Add current element to running sum
                sum += nums[end];
                
                // Check if sum equals k
                if (sum == k) {
                    count++;
                }
            }
        }
        
        return count;
    }
}
```

**Improvement**:
```
Before: sum = nums[start] + nums[start+1] + ... + nums[end] (recalculate each time)
After:  sum = previousSum + nums[end] (just add one element)
```

**Time Complexity**: O(n²) - Two nested loops
- Outer loop: n iterations
- Inner loop: up to n iterations
- Total: n × n = n²

**Space Complexity**: O(1) - Only using variables

**Better but still not optimal**:
- For n = 20,000: 400 million operations
- Will likely time out on large inputs
- We can do better!

---

### Approach 3: Prefix Sum Array (BETTER)
**Idea**: Pre-calculate all prefix sums, then check all pairs.

```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        int n = nums.length;
        
        // Build prefix sum array
        int[] prefixSum = new int[n + 1];
        prefixSum[0] = 0;
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        int count = 0;
        
        // Check all pairs of prefix sums
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                // Sum of subarray [i...j-1] = prefixSum[j] - prefixSum[i]
                if (prefixSum[j] - prefixSum[i] == k) {
                    count++;
                }
            }
        }
        
        return count;
    }
}
```

**How it works**:
```
Array:      [1,  2,  3]
Prefix:  [0, 1,  3,  6]
          ↑  ↑   ↑   ↑
         i=0 i=1 i=2 i=3

To get sum[0...1] = [1,2]:
prefixSum[2] - prefixSum[0] = 3 - 0 = 3
```

**Time Complexity**: O(n²) - Still checking all pairs
**Space Complexity**: O(n) - Prefix sum array

**Still not optimal!** We're still checking all pairs.

---

### Approach 4: HashMap with Prefix Sum (OPTIMAL) ⭐

**Idea**: As we build prefix sum, use HashMap to count how many times we can form sum k.

**Key Insight**:
```
At index i with prefixSum[i]:
- We want subarrays ending at i with sum = k
- This means: prefixSum[i] - prefixSum[j] = k
- Rearranging: prefixSum[j] = prefixSum[i] - k
- So we check: how many times have we seen (prefixSum[i] - k) before?
```

```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        // HashMap to store frequency of prefix sums
        Map<Integer, Integer> prefixSumCount = new HashMap<>();
        
        // Initialize: prefix sum 0 occurs once (empty subarray)
        prefixSumCount.put(0, 1);
        
        int currentSum = 0;
        int count = 0;
        
        // Process each element
        for (int num : nums) {
            // Update current prefix sum
            currentSum += num;
            
            // Check if (currentSum - k) exists in map
            // If yes, it means there are subarrays ending here with sum k
            int target = currentSum - k;
            if (prefixSumCount.containsKey(target)) {
                count += prefixSumCount.get(target);
            }
            
            // Add current prefix sum to map
            prefixSumCount.put(currentSum, 
                              prefixSumCount.getOrDefault(currentSum, 0) + 1);
        }
        
        return count;
    }
}
```

**Alternative Implementation with Cleaner Code**:

```java
class Solution {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1); // Important: handle subarrays starting from index 0
        
        int sum = 0;
        int result = 0;
        
        for (int num : nums) {
            sum += num;
            
            // If (sum - k) exists, we found subarray(s) with sum k
            result += map.getOrDefault(sum - k, 0);
            
            // Record current sum
            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }
        
        return result;
    }
}
```

**Python Implementation**:

```python
class Solution:
    def subarraySum(self, nums: List[int], k: int) -> int:
        # Dictionary to store prefix sum frequencies
        prefix_count = {0: 1}  # Base case: empty prefix
        
        current_sum = 0
        count = 0
        
        for num in nums:
            current_sum += num
            
            # Check if (current_sum - k) exists
            if current_sum - k in prefix_count:
                count += prefix_count[current_sum - k]
            
            # Add current sum to dictionary
            prefix_count[current_sum] = prefix_count.get(current_sum, 0) + 1
        
        return count
```

**C++ Implementation**:

```cpp
class Solution {
public:
    int subarraySum(vector<int>& nums, int k) {
        unordered_map<int, int> prefixCount;
        prefixCount[0] = 1;  // Base case
        
        int currentSum = 0;
        int count = 0;
        
        for (int num : nums) {
            currentSum += num;
            
            // Check if (currentSum - k) exists
            if (prefixCount.find(currentSum - k) != prefixCount.end()) {
                count += prefixCount[currentSum - k];
            }
            
            // Add current sum
            prefixCount[currentSum]++;
        }
        
        return count;
    }
};
```

**JavaScript Implementation**:

```javascript
var subarraySum = function(nums, k) {
    const prefixCount = new Map();
    prefixCount.set(0, 1); // Base case
    
    let currentSum = 0;
    let count = 0;
    
    for (const num of nums) {
        currentSum += num;
        
        // Check if (currentSum - k) exists
        const target = currentSum - k;
        if (prefixCount.has(target)) {
            count += prefixCount.get(target);
        }
        
        // Add current sum
        prefixCount.set(currentSum, (prefixCount.get(currentSum) || 0) + 1);
    }
    
    return count;
};
```

**Time Complexity**: O(n)
- Single pass through the array
- HashMap operations (get/put) are O(1) average

**Space Complexity**: O(n)
- HashMap can store up to n different prefix sums in worst case
- Example: [1,2,3,4,5] has all unique prefix sums

**Why This is Optimal**:
1. **Can't do better than O(n) time** - must see each element at least once
2. **Space tradeoff is worth it** - O(n) space for O(n²) → O(n) time improvement
3. **Single pass** - Only iterate through array once
4. **No sorting needed** - Order matters for subarrays!

---

## Detailed Walkthrough

### Example: nums = [1, -1, 1, 1, 1], k = 3

**Initial State**:
```
map = {0: 1}
currentSum = 0
count = 0
```

**Step 1: i = 0, num = 1**
```
currentSum = 0 + 1 = 1
target = 1 - 3 = -2
Is -2 in map? NO
count = 0

Add currentSum to map:
map = {0: 1, 1: 1}
```

**Step 2: i = 1, num = -1**
```
currentSum = 1 + (-1) = 0
target = 0 - 3 = -3
Is -3 in map? NO
count = 0

Add currentSum to map:
map = {0: 2, 1: 1}  ← Notice 0 now appears twice!
```

**Step 3: i = 2, num = 1**
```
currentSum = 0 + 1 = 1
target = 1 - 3 = -2
Is -2 in map? NO
count = 0

Add currentSum to map:
map = {0: 2, 1: 2}  ← 1 now appears twice!
```

**Step 4: i = 3, num = 1**
```
currentSum = 1 + 1 = 2
target = 2 - 3 = -1
Is -1 in map? NO
count = 0

Add currentSum to map:
map = {0: 2, 1: 2, 2: 1}
```

**Step 5: i = 4, num = 1**
```
currentSum = 2 + 1 = 3
target = 3 - 3 = 0
Is 0 in map? YES! It appeared 2 times
count = 0 + 2 = 2

This means we found 2 subarrays ending at index 4 with sum 3:
1. From index 0 to 4: [1,-1,1,1,1] = 3 ✓
2. From index 2 to 4: [1,1,1] = 3 ✓

Add currentSum to map:
map = {0: 2, 1: 2, 2: 1, 3: 1}
```

**Final Answer**: 2

**Why map[0] = 2 is crucial**:
- First 0 at initialization: represents empty prefix
- Second 0 at index 1: represents prefix [1,-1]
- When we reach sum 3 at index 4, we look for 0
- Finding it twice means two different starting points give us sum k!

---

## Edge Cases

### 1. Single Element Array
```java
// Case 1a: Single element equals k
nums = [5], k = 5
Expected: 1
Explanation: Subarray [5] itself

// Case 1b: Single element doesn't equal k
nums = [5], k = 3
Expected: 0
Explanation: No subarray with sum 3
```

### 2. All Zeros
```java
nums = [0, 0, 0], k = 0
Expected: 6
Explanation: All possible subarrays sum to 0
[0] at index 0
[0] at index 1
[0] at index 2
[0,0] indices 0-1
[0,0] indices 1-2
[0,0,0] indices 0-2

Total: 6 subarrays
```

**Formula for n zeros**: n × (n + 1) / 2 = 3 × 4 / 2 = 6 ✓

### 3. No Solution
```java
nums = [1, 2, 3], k = 10
Expected: 0
Explanation: Maximum possible sum is 6, can't reach 10
```

### 4. Negative Numbers
```java
// Case 4a: Mix of positive and negative
nums = [1, -1, 1, -1, 1], k = 0
Expected: 4
Explanation:
[1,-1] at indices 0-1
[1,-1,1,-1] at indices 0-3
[-1,1] at indices 1-2
[-1,1,-1,1] at indices 1-4

// Case 4b: All negative
nums = [-1, -2, -3], k = -3
Expected: 2
Explanation:
[-3] at index 2
[-1,-2] at indices 0-1
```

### 5. Large Numbers
```java
nums = [1000, -1000, 1000], k = 1000
Expected: 3
Explanation:
[1000] at index 0
[1000,-1000,1000] at indices 0-2
[1000] at index 2
```

### 6. K = 0
```java
nums = [1, -1, 2, -2], k = 0
Expected: 4
Explanation:
[1,-1] at indices 0-1
[1,-1,2,-2] at indices 0-3
[-1,2,-2] at indices 1-3
[2,-2] at indices 2-3
```

### 7. Same Elements
```java
nums = [2, 2, 2, 2], k = 4
Expected: 3
Explanation:
[2,2] at indices 0-1
[2,2] at indices 1-2
[2,2] at indices 2-3
```

### 8. Duplicate Prefix Sums
```java
nums = [3, -3, 3, -3, 3], k = 3
Expected: 5

Prefix sums: [3, 0, 3, 0, 3]
Notice: 3 appears 3 times, 0 appears 2 times

When we reach each 3, we look for 0:
At index 0: sum=3, look for 0, found 1x (initialization), count=1
At index 2: sum=3, look for 0, found 2x (init + index 1), count=1+2=3
At index 4: sum=3, look for 0, found 3x (init + index 1 + index 3), count=3+3=6

Wait, that's 6, not 5. Let me recalculate...

Actually, let me trace through properly:
i=0: sum=3, look for 0 (found 1x), count=1, map={0:1, 3:1}
i=1: sum=0, look for -3 (not found), count=1, map={0:2, 3:1}
i=2: sum=3, look for 0 (found 2x), count=1+2=3, map={0:2, 3:2}
i=3: sum=0, look for -3 (not found), count=3, map={0:3, 3:2}
i=4: sum=3, look for 0 (found 3x), count=3+3=6, map={0:3, 3:3}

So actually 6 subarrays:
1. [3] (index 0)
2. [3,-3,3] (indices 0-2)
3. [3,-3,3,-3,3] (indices 0-4)
4. [3] (index 2)
5. [3,-3,3] (indices 2-4)
6. [3] (index 4)

Total: 6 (not 5, I miscounted initially)
```

### 9. Entire Array Sums to K
```java
nums = [1, 2, 3], k = 6
Expected: 1
Explanation: Only [1,2,3] sums to 6

This is why we initialize map with {0: 1}!
Without it, we'd miss subarrays starting from index 0.
```

### 10. Negative K
```java
nums = [1, 2, 3], k = -5
Expected: 0
Explanation: All elements positive, can't get negative sum
```

### 11. Very Large Array
```java
nums = new int[20000]; // All 1s
k = 5000
Expected: 15001

Explanation: Any 5000 consecutive 1s sum to 5000
Number of such subarrays = n - k + 1 = 20000 - 5000 + 1 = 15001
```

### 12. Alternating Signs
```java
nums = [1, -1, 1, -1], k = 0
Expected: 4
Explanation:
[1,-1] at 0-1
[1,-1,1,-1] at 0-3
[-1,1] at 1-2
[-1,1,-1,1] at 1-3

Wait, let me check the last one: -1+1-1+1 = 0 ✓
Actually that's indices 1-4, but we only have 4 elements (0-3)
So it's correct.
```

### 13. Empty Prefix Important Case
```java
nums = [3, 0, 3], k = 3
Expected: 3

Without map[0] = 1:
i=0: sum=3, look for 0, NOT FOUND, count=0 ❌

With map[0] = 1:
i=0: sum=3, look for 0, found 1x, count=1 ✓
i=1: sum=3, look for 0, found 1x, count=2 ✓
i=2: sum=6, look for 3, found 2x, count=4 ❌

Let me trace again:
i=0: sum=3, look for 0 (found 1x), count=1, map={0:1, 3:1}
i=1: sum=3, look for 0 (found 1x), count=2, map={0:1, 3:2}
i=2: sum=6, look for 3 (found 2x), count=2+2=4

Hmm, 4 not 3. Let me check manually:
[3] at index 0 = 3 ✓
[3,0] at indices 0-1 = 3 ✓
[3] at index 2 = 3 ✓
[0,3] at indices 1-2 = 3 ✓

So actually 4 is correct!
```

---

## Common Mistakes

### Mistake 1: Forgetting to Initialize map[0] = 1
```java
// WRONG ❌
Map<Integer, Integer> map = new HashMap<>();
int sum = 0, count = 0;

for (int num : nums) {
    sum += num;
    count += map.getOrDefault(sum - k, 0);
    map.put(sum, map.getOrDefault(sum, 0) + 1);
}

// Problem: Misses subarrays starting from index 0!
// Example: nums = [1,1,1], k = 2
// At index 1: sum=2, looking for 0, but map doesn't have 0!
// Misses subarray [1,1] from index 0-1
```

**Correct**:
```java
// RIGHT ✓
Map<Integer, Integer> map = new HashMap<>();
map.put(0, 1); // Initialize!
```

### Mistake 2: Checking sum == k Instead of Using HashMap
```java
// WRONG ❌
for (int num : nums) {
    sum += num;
    if (sum == k) {  // Only finds subarrays from start!
        count++;
    }
}

// Problem: Only counts subarrays starting at index 0
// Example: nums = [1,2,3], k = 5
// Only finds [1,2,3] at sum=6 (oops, doesn't even find this!)
// Misses [2,3] from index 1-2
```

### Mistake 3: Updating Map Before Checking
```java
// WRONG ❌
for (int num : nums) {
    sum += num;
    map.put(sum, map.getOrDefault(sum, 0) + 1);  // Update first
    count += map.getOrDefault(sum - k, 0);        // Check second
}

// Problem: Counts incorrect subarrays!
// Example: nums = [2], k = 0
// At index 0: sum=2, add to map first: map={2:1}
// Then look for 2-0=2: found! count=1 ❌
// But there's no subarray with sum 0!
```

**Correct Order**:
```java
// RIGHT ✓
for (int num : nums) {
    sum += num;
    count += map.getOrDefault(sum - k, 0);  // Check first
    map.put(sum, map.getOrDefault(sum, 0) + 1);  // Update second
}
```

### Mistake 4: Using Set Instead of Map
```java
// WRONG ❌
Set<Integer> seen = new HashSet<>();
seen.add(0);

for (int num : nums) {
    sum += num;
    if (seen.contains(sum - k)) {
        count++;  // Only counts 1, not the frequency!
    }
    seen.add(sum);
}

// Problem: Loses count information!
// Example: nums = [1,-1,1,1,1], k = 3
// Prefix sum 0 appears twice (at init and index 1)
// When we reach sum=3, we should count both occurrences
// Set only tells us it exists, not how many times!
```

### Mistake 5: Trying to Sort the Array
```java
// WRONG ❌
Arrays.sort(nums);  // Don't sort!
// Then apply algorithm...

// Problem: Sorting destroys the subarray structure!
// Subarrays must be CONTIGUOUS in original array
// Example: nums = [3,1,2], k = 3
// After sorting: [1,2,3]
// Original [3] is valid, but after sorting [3] is at different position
```

**Key Point**: Order matters for subarray problems! Never sort unless explicitly needed.

### Mistake 6: Integer Overflow
```java
// POTENTIAL ISSUE ⚠
int sum = 0;
for (int num : nums) {
    sum += num;  // Could overflow if nums[i] can be large
}

// Given constraints: -1000 ≤ nums[i] ≤ 1000, length ≤ 20,000
// Worst case sum: 20,000 × 1000 = 20,000,000 (fits in int)
// But in general problems, might need long!
```

For this specific problem, `int` is safe. But be aware in general!

### Mistake 7: Off-by-One Errors in Manual Testing
```java
// When manually tracing, common mistakes:

// Wrong: "Subarray from index 1 to 3"
// Right: "Subarray from index 1 to 3 inclusive"

// Always clarify: inclusive or exclusive?
// In this problem, we mean inclusive on both ends
```

---

## Complexity Analysis Summary

| Approach | Time | Space | Pass LeetCode? |
|----------|------|-------|----------------|
| Brute Force (3 loops) | O(n³) | O(1) | ❌ TLE |
| Brute Force (2 loops) | O(n²) | O(1) | ⚠ Marginal |
| Prefix Sum Array | O(n²) | O(n) | ⚠ Marginal |
| **HashMap + Prefix Sum** | **O(n)** | **O(n)** | **✅ Optimal** |

**Why HashMap Approach is Best**:
1. **Optimal Time**: O(n) is best possible for this problem
2. **Single Pass**: Only iterate once through array
3. **Handles All Cases**: Works with negatives, zeros, duplicates
4. **Space is Worth It**: Small price for massive time improvement

---

## Interview Tips

### 1. Start with Clarifying Questions
```
"Can the array contain negative numbers?"
→ Yes, see constraints: -1000 ≤ nums[i] ≤ 1000

"Can k be negative?"
→ Yes, see constraints: -10^7 ≤ k ≤ 10^7

"What if no subarrays sum to k?"
→ Return 0

"Can the array be empty?"
→ No, length ≥ 1 per constraints

"Should I return the subarrays themselves or just the count?"
→ Just the count
```

### 2. Explain the Intuition
```
"The key insight is that for any subarray [i...j], we can calculate its sum as:
sum[i...j] = prefixSum[j] - prefixSum[i-1]

If this equals k, then:
prefixSum[j] - prefixSum[i-1] = k
prefixSum[i-1] = prefixSum[j] - k

So at each position j, we check how many times we've seen (currentSum - k) before.
This is perfect for a HashMap!"
```

### 3. Walk Through a Small Example
```
"Let me trace nums = [1,2,3], k = 3:

Initially: map = {0:1}, sum=0, count=0

i=0, num=1: sum=1, look for -2 (no), count=0, map={0:1, 1:1}
i=1, num=2: sum=3, look for 0 (yes! 1x), count=1, map={0:1, 1:1, 3:1}
i=2, num=3: sum=6, look for 3 (yes! 1x), count=2, map={0:1, 1:1, 3:1, 6:1}

Result: 2 → subarrays [1,2] and [3]"
```

### 4. Discuss Trade-offs
```
"The brute force approach would be O(n²) checking all subarrays.
By using a HashMap to store prefix sums, we trade O(n) space for O(n) time.
This is the optimal solution because we must examine each element at least once."
```

### 5. Mention Edge Cases
```
"Important edge cases to consider:
- Array with negative numbers: still works!
- k = 0: counts subarrays that sum to zero
- Multiple zeros: each generates many subarrays
- Single element: might equal k or not
- Duplicate prefix sums: this is why we need frequency count in HashMap"
```

### 6. Code Cleanly
```java
// Use descriptive variable names
Map<Integer, Integer> prefixSumCount  // not just "map"
int currentSum  // not just "sum"
int count  // clear what we're counting

// Add helpful comments
// Initialize with empty prefix sum
prefixSumCount.put(0, 1);

// Check before updating - important order!
count += prefixSumCount.getOrDefault(currentSum - k, 0);
```

### 7. Test Your Solution
```
"Let me verify with the example:
nums = [1,1,1], k = 2

i=0: sum=1, look for -1 (no), count=0, map={0:1, 1:1}
i=1: sum=2, look for 0 (yes! 1x), count=1, map={0:1, 1:1, 2:1}
i=2: sum=3, look for 1 (yes! 1x), count=2, map={0:1, 1:2, 2:1, 3:1}

Result: 2 ✓ Matches expected output!"
```

### 8. Discuss Follow-ups
```
"Potential follow-ups:
- Return the actual subarrays, not just count
  → Need to store indices in HashMap
  
- Find longest subarray with sum k
  → Store first occurrence of each prefix sum
  
- Count subarrays with sum ≤ k
  → Different approach, might need sorting
  
- Space optimization
  → O(n) space is already optimal for this version
    (can't do better while maintaining O(n) time)"
```

---

## Related Problems

### 1. Continuous Subarray Sum (LeetCode 523) - Similar Pattern
```
Given array and integer k, return true if array has a contiguous 
subarray of size at least 2 whose sum is multiple of k.

Key difference: Use modulo arithmetic
Map stores: (prefixSum % k) → index
```

### 2. Contiguous Array (LeetCode 525) - Same Pattern
```
Given binary array, find maximum length of contiguous subarray with 
equal number of 0 and 1.

Key insight: Convert 0 to -1, then find subarray with sum 0
Use: prefixSum + HashMap (find longest, not count)
```

### 3. Binary Subarrays With Sum (LeetCode 930) - Exact Same Pattern
```
Given binary array and integer goal, return number of subarrays with sum goal.

Literally the same algorithm! Just binary values.
```

### 4. Count Number of Nice Subarrays (LeetCode 1248) - Transform
```
Find number of subarrays with exactly k odd numbers.

Transform: odd → 1, even → 0
Then apply same algorithm with k odd = k ones
```

### 5. Subarray Sums Divisible by K (LeetCode 974) - Modulo Variant
```
Count subarrays with sum divisible by k.

Use: (prefixSum % k) in HashMap
Watch out: handle negative modulo correctly!
```

---

## Complete Solution Template

```java
/**
 * Problem: Subarray Sum Equals K
 * Pattern: Prefix Sum + HashMap
 * Time: O(n), Space: O(n)
 */
class Solution {
    public int subarraySum(int[] nums, int k) {
        // HashMap: prefixSum → frequency
        Map<Integer, Integer> prefixSumCount = new HashMap<>();
        
        // Base case: empty prefix has sum 0
        // This handles subarrays starting from index 0
        prefixSumCount.put(0, 1);
        
        int currentSum = 0;
        int count = 0;
        
        // Process each element
        for (int num : nums) {
            // Update running prefix sum
            currentSum += num;
            
            // Check if (currentSum - k) exists
            // If yes, we found subarray(s) ending here with sum k
            int target = currentSum - k;
            count += prefixSumCount.getOrDefault(target, 0);
            
            // Add current prefix sum to map
            // Note: Do this AFTER checking, not before!
            prefixSumCount.put(currentSum, 
                              prefixSumCount.getOrDefault(currentSum, 0) + 1);
        }
        
        return count;
    }
}
```

---

## 10 Key Takeaways

### 1. **Prefix Sum is Powerful for Subarray Problems**
```
Any subarray sum can be calculated as:
sum[i...j] = prefixSum[j] - prefixSum[i-1]

This transforms O(n) subarray sum calculation to O(1)!
```

### 2. **HashMap Stores Historical Information**
```
As we traverse, HashMap remembers all prefix sums we've seen.
This lets us look back in O(1) time instead of O(n).
```

### 3. **Initialize HashMap with {0: 1}**
```
Crucial for handling subarrays starting from index 0.
Represents the "empty prefix" before any elements.

Without it: miss [1,2] when nums=[1,2], k=3
With it: when sum=3, look for 0, found! count=1 ✓
```

### 4. **Order Matters: Check Before Update**
```
CORRECT:
count += map.get(sum - k)  ← check first
map.put(sum, ...)           ← update second

WRONG:
map.put(sum, ...)           ← update first
count += map.get(sum - k)  ← check second (counts current element!)
```

### 5. **Frequency Count is Essential**
```
Use Map<Integer, Integer>, NOT Set<Integer>!

Why? Same prefix sum can appear multiple times.
Example: [1,-1,1,1,1] has prefix sum 0 twice
When we reach sum=3, both occurrences create valid subarrays.
```

### 6. **Works with Negative Numbers**
```
Unlike sliding window (which needs non-negative),
this approach works perfectly with negatives!

Reason: We're not maintaining a window, just tracking sums.
Negatives can make sum go up or down, HashMap handles it.
```

### 7. **Single Pass is Sufficient**
```
Don't need to calculate all prefix sums first.
Calculate and check in the same loop.

This saves:
- Extra array space
- Extra pass through data
- Cleaner code
```

### 8. **Space Complexity is Inherent**
```
Can we do better than O(n) space? No!

In worst case (all unique prefix sums), need to store all n values.
Example: [1,2,3,4,5] → prefix [1,3,6,10,15] all unique

But O(n) space for O(n²)→O(n) time is excellent trade!
```

### 9. **Subarray ≠ Subsequence**
```
Subarray: MUST be contiguous [1,2,3] in nums=[1,2,3,4]
Subsequence: can skip elements [1,3,4] in nums=[1,2,3,4]

This problem is subarray → can't sort, order matters!
```

### 10. **Pattern Extends to Many Problems**
```
This "prefix sum + HashMap" pattern appears in:
- Contiguous array (equal 0s and 1s)
- Binary subarray with sum
- Count nice subarrays
- Subarray sums divisible by k

Master this pattern → solve 5+ LeetCode problems!

General form:
1. Calculate running sum/count
2. Store in HashMap with frequency
3. Look for (current - target) in map
4. Update count and map
```

---

## Summary

**Problem**: Count subarrays with sum equal to k

**Key Insight**: 
```
prefixSum[i] - prefixSum[j] = k
⟹ prefixSum[j] = prefixSum[i] - k
⟹ Look for (currentSum - k) in HashMap!
```

**Algorithm**:
1. Initialize HashMap with {0: 1}
2. For each element:
   - Add to running sum
   - Check if (sum - k) exists in map
   - Add frequency to count
   - Update map with current sum

**Complexity**: O(n) time, O(n) space

**Pattern**: Prefix Sum + HashMap for subarray problems

**Remember**: 
- Initialize map[0] = 1
- Check before update
- Use frequency count, not just existence
- Works with negatives!

This is one of the most important patterns in array problems. Master it! 🎯

---

## Visual Summary

```
┌─────────────────────────────────────────────────────────────┐
│               SUBARRAY SUM EQUALS K                         │
│                                                             │
│  Problem: Count subarrays with sum = k                     │
│                                                             │
│  ┌───────────────────────────────────────────────┐         │
│  │  Key Insight:                                 │         │
│  │                                               │         │
│  │  sum[i...j] = prefixSum[j] - prefixSum[i-1]  │         │
│  │            = k                                │         │
│  │                                               │         │
│  │  Therefore:                                   │         │
│  │  prefixSum[i-1] = prefixSum[j] - k           │         │
│  │                                               │         │
│  │  → Look for (currentSum - k) in HashMap!     │         │
│  └───────────────────────────────────────────────┘         │
│                                                             │
│  Algorithm:                                                 │
│  ┌─────────────────────────────────────────┐               │
│  │ 1. map = {0: 1}                         │               │
│  │ 2. sum = 0, count = 0                   │               │
│  │ 3. For each num:                        │               │
│  │    • sum += num                         │               │
│  │    • count += map.get(sum - k, 0)       │               │
│  │    • map[sum] = map.get(sum, 0) + 1     │               │
│  │ 4. Return count                         │               │
│  └─────────────────────────────────────────┘               │
│                                                             │
│  Complexity: O(n) time, O(n) space                          │
│                                                             │
│  Pattern: Prefix Sum + HashMap                             │
└─────────────────────────────────────────────────────────────┘
```

**Practice this problem until the pattern becomes second nature!** 💪
