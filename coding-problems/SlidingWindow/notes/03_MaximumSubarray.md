# Maximum Subarray (Easy)

## Problem Statement
Given an integer array `nums`, find the **contiguous subarray** (containing at least one number) which has the largest sum and return its sum.

A **subarray** is a contiguous part of an array.

**LeetCode Link**: [53. Maximum Subarray](https://leetcode.com/problems/maximum-subarray/)

---

## Examples

### Example 1:
```
Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
Output: 6
Explanation: The subarray [4,-1,2,1] has the largest sum 6.
```

### Example 2:
```
Input: nums = [1]
Output: 1
Explanation: The subarray [1] has the largest sum 1.
```

### Example 3:
```
Input: nums = [5,4,-1,7,8]
Output: 23
Explanation: The subarray [5,4,-1,7,8] has the largest sum 23.
```

---

## Constraints
- `1 <= nums.length <= 10^5`
- `-10^4 <= nums[i] <= 10^4`

---

## Pattern Recognition

This is a **Kadane's Algorithm / Dynamic Programming / Sliding Window** problem because:
1. We need to find the **maximum sum** of a contiguous subarray
2. We can make a **greedy decision** at each step: add to current sum or start fresh
3. **Local optimum** (best ending here) leads to **global optimum** (best overall)
4. Can be solved in **O(n) single pass**

**Key Insight**: At each position, decide:
- Continue the existing subarray (add current to sum)
- Start a new subarray from current position (reset sum)

If current sum becomes negative, starting fresh is always better!

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Try all possible subarrays and track maximum sum.

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int maxSum = Integer.MIN_VALUE;
        
        // Try all starting positions
        for (int i = 0; i < nums.length; i++) {
            int currentSum = 0;
            
            // Try all ending positions from i
            for (int j = i; j < nums.length; j++) {
                currentSum += nums[j];
                maxSum = Math.max(maxSum, currentSum);
            }
        }
        
        return maxSum;
    }
}
```

**Time Complexity**: O(n²) - nested loops
**Space Complexity**: O(1) - only variables
**Problem**: Too slow for large arrays!

---

### Approach 2: Kadane's Algorithm (OPTIMAL) ⭐
**Idea**: Track current sum, reset when it goes negative. Keep maximum seen.

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int maxSum = nums[0];       // Global maximum
        int currentSum = nums[0];   // Maximum ending at current position
        
        for (int i = 1; i < nums.length; i++) {
            // Key decision: add to current or start fresh
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            
            // Update global maximum
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(1) - only two variables
**Why Optimal**: Linear time, best possible

---

### Approach 3: Kadane's with Reset Logic (Alternative View)
**Idea**: Same as Kadane's but with explicit reset when sum goes negative.

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int maxSum = Integer.MIN_VALUE;
        int currentSum = 0;
        
        for (int num : nums) {
            // Add current number to sum
            currentSum += num;
            
            // Update maximum
            maxSum = Math.max(maxSum, currentSum);
            
            // Reset if sum becomes negative
            if (currentSum < 0) {
                currentSum = 0;
            }
        }
        
        return maxSum;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(1) - constant space
**Note**: Clearer logic for some people, same efficiency

---

### Approach 4: Dynamic Programming (Conceptual)
**Idea**: DP array where dp[i] = maximum subarray sum ending at index i.

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        
        // dp[i] = max sum of subarray ending at i
        dp[0] = nums[0];
        int maxSum = dp[0];
        
        for (int i = 1; i < n; i++) {
            // Either extend previous subarray or start new one
            dp[i] = Math.max(nums[i], dp[i - 1] + nums[i]);
            maxSum = Math.max(maxSum, dp[i]);
        }
        
        return maxSum;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(n) - dp array
**Note**: Shows DP nature but uses extra space. Kadane's is space-optimized version.

---

## Detailed Walkthrough (Approach 2: Kadane's Algorithm)

### Example: nums = [-2, 1, -3, 4, -1, 2, 1, -5, 4]

```
Initial State:
maxSum = -2, currentSum = -2

Step-by-step trace:

Index 0: num = -2
  currentSum = -2 (initial)
  maxSum = -2
  Decision: Start with -2

Index 1: num = 1
  currentSum = max(1, -2 + 1) = max(1, -1) = 1
  Decision: Start fresh from 1 (better than -1)
  maxSum = max(-2, 1) = 1
  Subarray: [1]

Index 2: num = -3
  currentSum = max(-3, 1 + (-3)) = max(-3, -2) = -2
  Decision: Continue from 1, even though sum is negative
  maxSum = max(1, -2) = 1 (no change)
  Subarray: [1, -3]

Index 3: num = 4
  currentSum = max(4, -2 + 4) = max(4, 2) = 4
  Decision: Start fresh from 4 (better than 2)
  maxSum = max(1, 4) = 4
  Subarray: [4]

Index 4: num = -1
  currentSum = max(-1, 4 + (-1)) = max(-1, 3) = 3
  Decision: Continue (3 is positive, keep going)
  maxSum = max(4, 3) = 4 (no change)
  Subarray: [4, -1]

Index 5: num = 2
  currentSum = max(2, 3 + 2) = max(2, 5) = 5
  Decision: Continue (building up)
  maxSum = max(4, 5) = 5
  Subarray: [4, -1, 2]

Index 6: num = 1
  currentSum = max(1, 5 + 1) = max(1, 6) = 6
  Decision: Continue (still building)
  maxSum = max(5, 6) = 6
  Subarray: [4, -1, 2, 1]

Index 7: num = -5
  currentSum = max(-5, 6 + (-5)) = max(-5, 1) = 1
  Decision: Continue (sum still positive)
  maxSum = max(6, 1) = 6 (no change)
  Subarray: [4, -1, 2, 1, -5]

Index 8: num = 4
  currentSum = max(4, 1 + 4) = max(4, 5) = 5
  Decision: Continue (extending)
  maxSum = max(6, 5) = 6 (no change)
  Subarray: [4, -1, 2, 1, -5, 4]

Final Result: maxSum = 6
Best subarray: [4, -1, 2, 1] from index 3 to 6
```

---

### Visual Representation

```
nums:        [-2,  1, -3,  4, -1,  2,  1, -5,  4]
currentSum:  [-2,  1, -2,  4,  3,  5,  6,  1,  5]
maxSum:      [-2,  1,  1,  4,  4,  5,  6,  6,  6]

Decision at each step:
[-2]: Start with -2
[1]:  Start fresh (1 > -1)
[-3]: Continue from 1 (-2 > -3)
[4]:  Start fresh (4 > 2)
[-1]: Continue (3 > -1)
[2]:  Continue (5 > 2)
[1]:  Continue (6 > 1)
[-5]: Continue (1 > -5)
[4]:  Continue (5 > 4)

Visualization of the winning subarray:
[-2,  1, -3, |4, -1,  2,  1|, -5,  4]
              └────────┬────────┘
                   sum = 6 (maximum)

Why this works:
- We reset when sum goes negative (starting fresh is better)
- We keep extending while sum is positive or neutral
- We track the maximum seen at any point
```

---

## Edge Cases to Consider

```java
// Test Case 1: All negative numbers
Input: nums = [-3, -2, -5, -1, -4]
Output: -1
// Choose the least negative number

// Test Case 2: Single element positive
Input: nums = [5]
Output: 5
// The only element

// Test Case 3: Single element negative
Input: nums = [-5]
Output: -5
// Must take at least one element

// Test Case 4: All positive numbers
Input: nums = [1, 2, 3, 4, 5]
Output: 15
// Sum of entire array

// Test Case 5: Mix with zeros
Input: nums = [0, -3, 1, 1]
Output: 2
// [1, 1] gives maximum

// Test Case 6: Large numbers
Input: nums = [Integer.MAX_VALUE, -1, Integer.MAX_VALUE]
Output: Integer.MAX_VALUE * 2 - 1
// Watch for overflow (not in this problem's constraints)

// Test Case 7: Alternating positive/negative
Input: nums = [5, -3, 5, -3, 5]
Output: 9
// [5, -3, 5, -3, 5] = 9, entire array

// Test Case 8: Negative then positive
Input: nums = [-2, -3, 4, -1, -2, 1, 5, -3]
Output: 7
// [4, -1, -2, 1, 5] = 7

// Test Case 9: Large array with one big positive
Input: nums = [-1, -1, -1, 100, -1, -1, -1]
Output: 100
// The single large number

// Test Case 10: All zeros
Input: nums = [0, 0, 0, 0]
Output: 0
// Sum is 0
```

---

## Common Mistakes to Avoid

### Mistake 1: Initializing maxSum to 0
```java
// ❌ WRONG: Fails for all-negative arrays
int maxSum = 0;
// If all numbers are negative, maxSum stays 0 (incorrect!)

// ✅ CORRECT: Initialize with first element or MIN_VALUE
int maxSum = nums[0]; // or Integer.MIN_VALUE
```

### Mistake 2: Not Considering Starting Fresh
```java
// ❌ WRONG: Always adding to current sum
for (int num : nums) {
    currentSum += num;
    maxSum = Math.max(maxSum, currentSum);
    // This doesn't start fresh when beneficial
}

// ✅ CORRECT: Decide whether to continue or start fresh
currentSum = Math.max(num, currentSum + num);
maxSum = Math.max(maxSum, currentSum);
```

### Mistake 3: Resetting currentSum to 0 Instead of num
```java
// ❌ WRONG: When starting fresh, don't reset to 0
if (currentSum < 0) {
    currentSum = 0; // Then add num next iteration
}

// ✅ CORRECT: Start fresh with current number
currentSum = Math.max(num, currentSum + num);
// This implicitly handles starting fresh
```

### Mistake 4: Checking currentSum < 0 Before Updating maxSum
```java
// ❌ WRONG: Missing valid maximum
currentSum += num;
if (currentSum < 0) {
    currentSum = 0;
}
maxSum = Math.max(maxSum, currentSum);
// If we reset to 0 before updating maxSum, we might miss the actual max

// ✅ CORRECT: Update maxSum before reset
currentSum += num;
maxSum = Math.max(maxSum, currentSum);
if (currentSum < 0) {
    currentSum = 0;
}
```

### Mistake 5: Returning currentSum Instead of maxSum
```java
// ❌ WRONG: Returning current instead of maximum
return currentSum; // This is the sum ending at last element

// ✅ CORRECT: Return the maximum seen
return maxSum;
```

### Mistake 6: Off-by-One in Loop Start
```java
// ❌ WRONG: Starting from 0 with currentSum = 0
int currentSum = 0;
for (int i = 0; i < nums.length; i++) {
    // Logic...
}

// ✅ CORRECT: Initialize with first element, start from 1
int currentSum = nums[0];
int maxSum = nums[0];
for (int i = 1; i < nums.length; i++) {
    // Logic...
}
```

---

## Why Kadane's Algorithm Works

### The Intuition:

**Key Question at each element**: Should I extend the previous subarray or start fresh?

**Decision Rule**:
```
If (currentSum + num) > num:
    Extend previous subarray (keep building)
Else:
    Start new subarray from current number
```

**Simplifies to**:
```java
currentSum = Math.max(num, currentSum + num);
```

**Why this is greedy and correct**:
- If current sum is negative, adding it to next number makes it worse
- Starting fresh from next number is always better
- If current sum is positive, keep it (helps future numbers)
- By tracking maximum seen, we never lose the best answer

### Example Showing the Greedy Choice:
```
nums = [-2, 3, -1, 4]

At 3: currentSum was -2
  Continue: -2 + 3 = 1
  Start fresh: 3
  Choose: 3 (better!)

At -1: currentSum is 3
  Continue: 3 + (-1) = 2
  Start fresh: -1
  Choose: 2 (better!)

At 4: currentSum is 2
  Continue: 2 + 4 = 6
  Start fresh: 4
  Choose: 6 (better!)

Result: 6
```

---

## Optimization Techniques

### Optimization 1: Early Exit for All Positive
```java
// Check if all numbers are positive
boolean allPositive = true;
for (int num : nums) {
    if (num < 0) {
        allPositive = false;
        break;
    }
}

if (allPositive) {
    // Sum entire array
    int sum = 0;
    for (int num : nums) {
        sum += num;
    }
    return sum;
}

// Otherwise, use Kadane's algorithm
```

### Optimization 2: Track Subarray Indices (If Needed)
```java
// If we need to return the actual subarray, not just sum
int maxSum = nums[0];
int currentSum = nums[0];
int start = 0, end = 0, tempStart = 0;

for (int i = 1; i < nums.length; i++) {
    if (nums[i] > currentSum + nums[i]) {
        currentSum = nums[i];
        tempStart = i; // Potential new start
    } else {
        currentSum = currentSum + nums[i];
    }
    
    if (currentSum > maxSum) {
        maxSum = currentSum;
        start = tempStart;
        end = i;
    }
}

// Now we have: start index, end index, and maxSum
```

### Optimization 3: In-place Modification (If Allowed)
```java
// Modify nums array to store currentSum at each position
for (int i = 1; i < nums.length; i++) {
    nums[i] = Math.max(nums[i], nums[i] + nums[i - 1]);
}

// Find maximum in modified array
int maxSum = nums[0];
for (int num : nums) {
    maxSum = Math.max(maxSum, num);
}
return maxSum;

// Note: This modifies the input array!
```

---

## Complexity Analysis

### Time Complexity: O(n)
- **Single pass**: We iterate through array once
- **Per element**: O(1) operations (comparison, max)
- **Total**: O(n)
- **Optimal**: Must examine each element at least once

### Space Complexity: O(1)
- **Variables**: Only `maxSum` and `currentSum`
- **No extra data structures**: No arrays or lists
- **Total**: O(1) - constant space

### Comparison of Approaches:

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Brute Force | O(n²) | O(1) | Try all subarrays, TLE |
| Kadane's Algorithm | O(n) | O(1) | ✅ Optimal |
| DP Array | O(n) | O(n) | Same logic, more space |
| Divide & Conquer | O(n log n) | O(log n) | Overcomplicated |

---

## Pattern: Kadane's Algorithm Template

### Generic Template:
```java
public int kadaneAlgorithm(int[] nums) {
    // Initialize with first element
    int maxEndingHere = nums[0];  // Max sum ending at current position
    int maxSoFar = nums[0];       // Global maximum
    
    // Process remaining elements
    for (int i = 1; i < nums.length; i++) {
        // Key decision: extend or start fresh
        maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
        
        // Update global maximum
        maxSoFar = Math.max(maxSoFar, maxEndingHere);
    }
    
    return maxSoFar;
}
```

### Variations:
```java
// Maximum Product Subarray (multiply instead of add)
maxProduct = Math.max(num, maxProduct * num);
minProduct = Math.min(num, minProduct * num); // Track min for negative numbers

// Maximum Sum Circular Subarray (handle wrap-around)
// Find max subarray sum and min subarray sum
// Answer is max(maxSum, totalSum - minSum)

// Maximum Sum with At Least K Elements
// Use Kadane's with sliding window of size k
```

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Constraints** (30 seconds)
> "The array has at least one element, and we need to return the maximum sum of any contiguous subarray. Can I confirm that we must include at least one element? Yes. Can the array be empty? No, minimum length is 1."

**Step 2: Explain Approach** (1-2 minutes)
> "I'll use Kadane's Algorithm, a classic dynamic programming approach. The idea is to iterate through the array once, and at each position, decide whether to:
> 1. Extend the current subarray (add current number to running sum)
> 2. Start a new subarray from current position
> We choose whichever gives a larger sum. We also track the maximum sum seen so far.
> Time: O(n), Space: O(1)"

**Step 3: Walk Through Key Insight** (1 minute)
> "The key insight is: if our current sum becomes negative, it's better to start fresh from the next number. A negative sum only makes future sums worse. We use the formula: 
> currentSum = max(num, currentSum + num)
> This automatically handles starting fresh when beneficial."

**Step 4: Discuss Edge Cases** (30 seconds)
> "Edge cases:
> - All negative: return the least negative number
> - All positive: entire array is the answer
> - Single element: return that element
> - Mix: Kadane's handles naturally"

**Step 5: Walk Through Example** (2-3 minutes)
```java
// Example: [-2, 1, -3, 4, -1, 2, 1, -5, 4]
// At index 0: sum = -2
// At index 1: sum = max(1, -2+1) = 1 (start fresh)
// At index 3: sum = max(4, -2+4) = 4 (start fresh)
// Continue building: 4 → 3 → 5 → 6
// Maximum found: 6 from subarray [4,-1,2,1]
```

**Step 6: Code** (5-10 minutes)
- Initialize maxSum and currentSum with first element
- Loop from index 1
- Apply Kadane's decision rule
- Update maximum
- Test with example

### Expected Follow-up Questions:

**Q**: "Can you find the actual subarray, not just the sum?"
**A**: "Yes! I'll track start and end indices. When starting fresh, update tempStart. When finding new max, update start=tempStart and end=currentIndex."

**Q**: "What about maximum product subarray?"
**A**: "Similar approach but track both max and min products (for handling negative numbers). Multiply instead of add. Negatives can flip max to min and vice versa."

**Q**: "What if we need maximum sum of subarray with at least k elements?"
**A**: "First, calculate sum of first k elements. Then use modified Kadane's, ensuring window size >= k. Shrink from left when beneficial but maintain minimum size."

**Q**: "Can you do this with divide and conquer?"
**A**: "Yes, O(n log n) approach: divide array in half, find max in left half, right half, and crossing middle. But Kadane's O(n) is better."

**Q**: "What about circular array where end connects to beginning?"
**A**: "Two cases: max subarray doesn't wrap (normal Kadane's), or it wraps (total sum - min subarray). Return max of both."

---

## Complete Solution with Comments

```java
class Solution {
    /**
     * Finds the maximum sum of any contiguous subarray using Kadane's Algorithm.
     * 
     * Approach: Dynamic Programming / Greedy
     * At each position, decide:
     * - Extend the current subarray (add to running sum)
     * - Start a new subarray from current position
     * Choose whichever gives larger sum.
     * 
     * Key Insight: If current sum is negative, starting fresh is always better.
     * 
     * @param nums Array of integers (at least one element)
     * @return Maximum sum of contiguous subarray
     * 
     * Time Complexity: O(n) - single pass through array
     * Space Complexity: O(1) - only two variables
     */
    public int maxSubArray(int[] nums) {
        // Edge case: single element (though guaranteed by constraints)
        if (nums.length == 1) {
            return nums[0];
        }
        
        // Initialize with first element
        // maxSum: best sum found so far (global maximum)
        // currentSum: best sum ending at current position (local maximum)
        int maxSum = nums[0];
        int currentSum = nums[0];
        
        // Process remaining elements
        for (int i = 1; i < nums.length; i++) {
            // Kadane's key decision:
            // Either extend previous subarray or start fresh
            // If currentSum + nums[i] < nums[i], starting fresh is better
            // This happens when currentSum is negative
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            
            // Update global maximum
            // Track the best sum we've seen at any point
            maxSum = Math.max(maxSum, currentSum);
        }
        
        return maxSum;
    }
}
```

---

## Alternative Implementation (Reset Style)

```java
class Solution {
    public int maxSubArray(int[] nums) {
        int maxSum = Integer.MIN_VALUE;
        int currentSum = 0;
        
        for (int num : nums) {
            // Add current number to running sum
            currentSum += num;
            
            // Update maximum before potentially resetting
            maxSum = Math.max(maxSum, currentSum);
            
            // Reset if sum goes negative
            // Starting fresh from next number will be better
            if (currentSum < 0) {
                currentSum = 0;
            }
        }
        
        return maxSum;
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
    int[] nums1 = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
    System.out.println(solution.maxSubArray(nums1));
    // Expected: 6 → [4,-1,2,1]
    
    // Test Case 2: All positive
    int[] nums2 = {1, 2, 3, 4, 5};
    System.out.println(solution.maxSubArray(nums2));
    // Expected: 15 → entire array
    
    // Test Case 3: All negative
    int[] nums3 = {-3, -2, -5, -1};
    System.out.println(solution.maxSubArray(nums3));
    // Expected: -1 → least negative
    
    // Test Case 4: Single element
    int[] nums4 = {5};
    System.out.println(solution.maxSubArray(nums4));
    // Expected: 5
    
    // Test Case 5: Mixed
    int[] nums5 = {5, 4, -1, 7, 8};
    System.out.println(solution.maxSubArray(nums5));
    // Expected: 23 → entire array
}
```

---

## Key Takeaways

1. ✅ **Kadane's Algorithm** is the gold standard for maximum subarray sum
2. ✅ **Greedy choice**: Extend if beneficial, start fresh if not
3. ✅ **Formula**: `currentSum = max(num, currentSum + num)`
4. ✅ **Initialize** maxSum and currentSum with first element
5. ✅ **Track global maximum** separate from current sum
6. ✅ **Negative sums**: Starting fresh is always better
7. ✅ **O(n) time, O(1) space** is optimal
8. ✅ **All negative case**: Return least negative number
9. ✅ **Pattern extends** to product, circular, and other variations
10. ✅ **Classic interview problem** - must know this cold!

---

## Variations & Extensions

After mastering Maximum Subarray, try these related problems:

1. **Maximum Product Subarray** (Medium) - Multiply instead of add
2. **Maximum Sum Circular Subarray** (Medium) - Array wraps around
3. **Best Time to Buy and Sell Stock** (Easy) - Similar DP pattern
4. **House Robber** (Medium) - Can't take adjacent elements
5. **Maximum Length of Repeated Subarray** (Medium) - Different constraint

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (15-20 minutes for Easy, but important!)
- [ ] Trace through [-2,1,-3,4,-1,2,1,-5,4] step by step
- [ ] Understand why negative sum means start fresh
- [ ] Test all edge cases (all negative, all positive, single)
- [ ] Implement both styles (max style and reset style)
- [ ] Practice explaining Kadane's algorithm
- [ ] Review in 2 days

---

**Pattern Mastered**: Kadane's Algorithm (Maximum Subarray) ✅  
**Difficulty**: Easy (but fundamental!)  
**Time to Master**: 20-30 minutes  
**Importance**: ⭐⭐⭐⭐⭐ Classic DP/Greedy problem

This is one of the most important algorithm patterns to know. Kadane's Algorithm appears in many variations and forms the foundation for understanding dynamic programming and greedy approaches! 🚀
