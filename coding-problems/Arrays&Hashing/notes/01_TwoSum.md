# Two Sum (Easy)

## Problem Statement
Given an array of integers `nums` and an integer `target`, return **indices** of the two numbers such that they add up to `target`.

You may assume that each input would have **exactly one solution**, and you may not use the same element twice.

You can return the answer in any order.

**LeetCode Link**: [1. Two Sum](https://leetcode.com/problems/two-sum/)

---

## Examples

### Example 1:
```
Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
```

### Example 2:
```
Input: nums = [3,2,4], target = 6
Output: [1,2]
Explanation: nums[1] + nums[2] == 6
```

### Example 3:
```
Input: nums = [3,3], target = 6
Output: [0,1]
Explanation: nums[0] + nums[1] == 6
```

### Example 4:
```
Input: nums = [-1,-2,-3,-4,-5], target = -8
Output: [2,4]
Explanation: -3 + (-5) = -8
```

---

## Constraints
- `2 <= nums.length <= 10^4`
- `-10^9 <= nums[i] <= 10^9`
- `-10^9 <= target <= 10^9`
- **Only one valid answer exists.**

---

## Pattern Recognition

This is a classic **HashMap (Hash Table)** problem because:
1. We need to find **two numbers** that satisfy a condition (sum to target)
2. We need **O(1) lookup** to check if complement exists
3. We need to track **both value and index**
4. Brute force O(n²) can be optimized to O(n) with extra space

**Key Insight**: For each number `x`, we need to find if `target - x` exists!
- Use HashMap to store numbers we've seen with their indices
- For current number, check if its complement exists in HashMap
- If yes, return the pair; if no, add current to HashMap

**Pattern**: Complement Finding with HashMap

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Try all possible pairs using nested loops.

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        // Try every pair
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[0]; // Should never reach here per problem constraints
    }
}
```

**Time Complexity**: O(n²) - nested loops check all pairs
**Space Complexity**: O(1) - no extra space
**Problem**: Too slow! Will time out on large inputs (n = 10,000)

---

### Approach 2: HashMap - One Pass (OPTIMAL) ⭐
**Idea**: Use HashMap to store seen numbers and check for complement in one pass.

```java
import java.util.*;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        // HashMap to store: number -> index
        Map<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            
            // Check if complement exists in map
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            
            // Store current number with its index
            map.put(nums[i], i);
        }
        
        return new int[0]; // Should never reach here
    }
}
```

**Time Complexity**: O(n) - single pass through array
**Space Complexity**: O(n) - HashMap stores up to n elements
**Why Optimal**: Can't do better than O(n) - must check each element at least once

---

### Approach 3: HashMap - Two Pass (Alternative)
**Idea**: First pass builds HashMap, second pass finds complement.

```java
import java.util.*;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        
        // First pass: store all numbers
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        
        // Second pass: find complement
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement) && map.get(complement) != i) {
                return new int[]{i, map.get(complement)};
            }
        }
        
        return new int[0];
    }
}
```

**Time Complexity**: O(n) - two passes, but still linear
**Space Complexity**: O(n) - HashMap storage
**Note**: One-pass is cleaner and equally efficient

---

## Detailed Walkthrough (Approach 2: Optimal)

### Example: nums = [2, 7, 11, 15], target = 9

```
Initial state:
map = {}
target = 9

Step 1: i = 0, nums[0] = 2
  complement = 9 - 2 = 7
  Is 7 in map? NO
  Add to map: {2: 0}
  
  State: map = {2: 0}

Step 2: i = 1, nums[1] = 7
  complement = 9 - 7 = 2
  Is 2 in map? YES! ✓
  Found at index map[2] = 0
  Return [0, 1]
  
  Answer: [0, 1]
```

### Visual Representation

```
Array:  [2,  7,  11,  15]
Index:   0   1   2    3
Target: 9

Pass through array:
i=0: 2 → Need 7. Map empty, add 2
     map = {2:0}
     
i=1: 7 → Need 2. Found in map at index 0!
     Return [0, 1] ✓
```

### Example 2: nums = [3, 2, 4], target = 6

```
Initial state:
map = {}
target = 6

Step 1: i = 0, nums[0] = 3
  complement = 6 - 3 = 3
  Is 3 in map? NO
  Add to map: {3: 0}

Step 2: i = 1, nums[1] = 2
  complement = 6 - 2 = 4
  Is 4 in map? NO
  Add to map: {3: 0, 2: 1}

Step 3: i = 2, nums[2] = 4
  complement = 6 - 4 = 2
  Is 2 in map? YES! ✓
  Found at index map[2] = 1
  Return [1, 2]
  
  Answer: [1, 2]
```

---

## Edge Cases to Consider

```java
// Test Case 1: Minimum size (n = 2)
Input: nums = [1, 2], target = 3
Output: [0, 1]
// Only two elements, they must be the answer

// Test Case 2: Same number used twice (different indices)
Input: nums = [3, 3], target = 6
Output: [0, 1]
// Both elements are same value but different indices

// Test Case 3: Negative numbers
Input: nums = [-1, -2, -3, -4, -5], target = -8
Output: [2, 4]
// -3 + (-5) = -8

// Test Case 4: Zero in array
Input: nums = [0, 4, 3, 0], target = 0
Output: [0, 3]
// 0 + 0 = 0

// Test Case 5: Negative and positive
Input: nums = [-3, 4, 3, 90], target = 0
Output: [0, 2]
// -3 + 3 = 0

// Test Case 6: Large numbers
Input: nums = [1000000000, 999999999, 1], target = 1999999999
Output: [0, 1]
// Testing near integer limits

// Test Case 7: Target is negative
Input: nums = [2, 5, -3], target = -1
Output: [1, 2]
// 5 + (-3) = 2... wait, 2 + (-3) = -1

// Test Case 8: Answer at beginning
Input: nums = [2, 7, 11, 15], target = 9
Output: [0, 1]
// First two elements

// Test Case 9: Answer at end
Input: nums = [1, 2, 3, 4, 5], target = 9
Output: [3, 4]
// Last two elements

// Test Case 10: Answer in middle
Input: nums = [1, 5, 3, 7, 9], target = 10
Output: [1, 2]
// Middle elements: 5 + 3 = 8... wait, 3 + 7 = 10

// Test Case 11: Large array
Input: nums = [1, 2, 3, ..., 10000], target = varies
// Test performance with max size

// Test Case 12: Duplicate values (but not the answer pair)
Input: nums = [2, 5, 5, 11], target = 10
Output: [1, 2]
// Two 5's in array, they form the answer
```

---

## Common Mistakes to Avoid

### Mistake 1: Using Same Element Twice
```java
// ❌ WRONG: Not checking if same index
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        map.put(nums[i], i);
    }
    
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{i, map.get(complement)};
            // BUG: Could return same index twice!
        }
    }
    return new int[0];
}

// Example: nums = [3, 2, 4], target = 6
// At i=0: complement = 3, found in map at index 0
// Returns [0, 0] ❌ WRONG!

// ✅ CORRECT: Check if different index
if (map.containsKey(complement) && map.get(complement) != i) {
    return new int[]{i, map.get(complement)};
}
```

### Mistake 2: Adding to Map Before Checking
```java
// ❌ WRONG ORDER (for one-pass approach)
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    
    for (int i = 0; i < nums.length; i++) {
        map.put(nums[i], i); // Adding first
        
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
    }
    return new int[0];
}

// Example: nums = [3, 3], target = 6
// At i=0: Add 3→0, check for 3, found! Returns [0, 0] ❌

// ✅ CORRECT: Check first, then add
int complement = target - nums[i];
if (map.containsKey(complement)) {
    return new int[]{map.get(complement), i};
}
map.put(nums[i], i);
```

### Mistake 3: Returning in Wrong Order
```java
// ❌ UNCLEAR: Which index comes first?
if (map.containsKey(complement)) {
    // Is this [old_index, new_index] or [new_index, old_index]?
    return new int[]{map.get(complement), i};
}

// ✅ CORRECT: Be clear about order
// map.get(complement) is the OLD index (seen earlier)
// i is the CURRENT index
// OLD comes before CURRENT, so [map.get(complement), i] is correct
return new int[]{map.get(complement), i}; // [old, new] = [smaller, larger]
```

### Mistake 4: Not Handling Duplicates Correctly
```java
// ❌ WRONG: HashMap stores only last occurrence
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    
    // If nums = [3, 2, 3], target = 6
    // After loop: map = {3:2, 2:1} - first 3 is overwritten!
    for (int i = 0; i < nums.length; i++) {
        map.put(nums[i], i);
    }
    
    // Now checking won't find the first 3
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement) && map.get(complement) != i) {
            return new int[]{i, map.get(complement)};
        }
    }
    return new int[0];
}

// ✅ CORRECT: Use one-pass to handle duplicates
for (int i = 0; i < nums.length; i++) {
    int complement = target - nums[i];
    if (map.containsKey(complement)) {
        return new int[]{map.get(complement), i};
    }
    map.put(nums[i], i); // Add after checking
}
```

### Mistake 5: Integer Overflow
```java
// ❌ POTENTIAL ISSUE: Not checking for overflow
int complement = target - nums[i];
// If nums[i] is very negative and target is very positive,
// this could theoretically overflow (rare in practice)

// ✅ BETTER: Java handles this well, but be aware
// In languages like C++, use long or check for overflow
long complement = (long)target - nums[i];
```

### Mistake 6: Forgetting to Return Anything
```java
// ❌ WRONG: Forgetting return statement
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
        map.put(nums[i], i);
    }
    // Missing return statement!
    // Compiler error in Java
}

// ✅ CORRECT: Always have a return
return new int[0]; // or throw new IllegalArgumentException();
```

---

## Why HashMap is Essential

### Without HashMap:
```java
// Brute force: O(n²)
for (int i = 0; i < nums.length; i++) {
    for (int j = i + 1; j < nums.length; j++) {
        if (nums[i] + nums[j] == target) {
            return new int[]{i, j};
        }
    }
}
// Must check every pair: n*(n-1)/2 comparisons
```

### With HashMap:
```java
// Optimized: O(n)
for (int i = 0; i < nums.length; i++) {
    if (map.containsKey(target - nums[i])) {
        return new int[]{map.get(target - nums[i]), i};
    }
    map.put(nums[i], i);
}
// Check each element once with O(1) lookup
```

**HashMap Enables:**
1. ✅ O(1) lookup time (vs O(n) linear search)
2. ✅ Store both value and index together
3. ✅ Single pass through array
4. ✅ Trade space for time (O(n) space for O(n) time)

---

## Complexity Analysis

### Time Complexity: O(n)
- **Single pass**: Loop through array once (n iterations)
- **HashMap operations**: Both `put()` and `containsKey()` are O(1) average
- **Total**: O(n) × O(1) = O(n)
- **Best case**: O(1) if answer is first two elements
- **Worst case**: O(n) if answer is last two elements or not found

### Space Complexity: O(n)
- **HashMap storage**: Worst case stores n-1 elements (if answer is last pair)
- **Output array**: O(1) - fixed size array of 2 elements
- **Total**: O(n) dominated by HashMap
- **Best case**: O(1) if answer found immediately
- **Worst case**: O(n) if must store all elements

### Comparison with Brute Force:

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Brute Force | O(n²) | O(1) | Too slow for large n |
| Two-pass HashMap | O(n) | O(n) | Works but unnecessary |
| One-pass HashMap | O(n) | O(n) | ✅ Optimal |
| Sorting + Two Pointers | O(n log n) | O(1)* | Loses original indices |

*Sorting approach: Doesn't work for this problem because we need original indices!

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Requirements** (30 seconds)
> "Let me clarify: we need to return the indices of two numbers that sum to target. 
> Can the same element be used twice? No, different indices required.
> Is there always exactly one solution? Yes, per the problem statement.
> Can the array contain duplicates? Yes, but they have different indices.
> Can numbers be negative? Yes, and target can be negative too."

**Step 2: Explain Brute Force** (1 minute)
> "The brute force approach is to check all pairs using nested loops - O(n²) time.
> For each element, check all elements after it to see if they sum to target.
> This works but is too slow for n = 10,000."

**Step 3: Optimize with HashMap** (2 minutes)
> "I can optimize to O(n) using a HashMap. The key insight is:
> For each number x, I need to find if (target - x) exists.
> I'll use a HashMap to store numbers I've seen with their indices.
> As I iterate, I check if the complement exists in the map.
> If yes, return the indices; if no, add current number to map.
> This is a single pass with O(1) lookups."

**Step 4: Walk Through Example** (2 minutes)
```java
// nums = [2, 7, 11, 15], target = 9
// i=0: See 2, need 7. Map empty, add {2:0}
// i=1: See 7, need 2. Found at index 0! Return [0,1]
```

**Step 5: Discuss Complexity** (30 seconds)
> "Time: O(n) - single pass with O(1) HashMap operations.
> Space: O(n) - worst case store all n elements in HashMap.
> This is optimal - can't do better than O(n) time."

**Step 6: Code** (5-10 minutes)
- Write clean, commented code
- Handle edge cases
- Test with example

**Step 7: Test** (2-3 minutes)
- Test with provided examples
- Test edge cases (duplicates, negatives, minimum size)

### Expected Follow-up Questions:

**Q**: "What if there are multiple valid pairs?"
**A**: "Current problem guarantees exactly one solution. If multiple pairs exist, I could return all by storing results in a list instead of returning immediately."

**Q**: "What if we need to return values instead of indices?"
**A**: "Still use HashMap but store just the value. Or even simpler, use HashSet and just check if complement exists."

**Q**: "Can you solve without extra space?"
**A**: "We could sort and use two pointers for O(n log n) time and O(1) space, but we'd lose original indices. For this specific problem requiring indices, HashMap is necessary."

**Q**: "What about Three Sum or K Sum?"
**A**: "Three Sum extends this by fixing one element and doing Two Sum on the rest - O(n²). K Sum generalizes recursively or with nested loops - O(n^(k-1))."

**Q**: "How do you handle integer overflow?"
**A**: "In Java, integer overflow wraps around. For safety, could use long for complement calculation, though it's rare to hit this with the given constraints."

**Q**: "What if the array is sorted?"
**A**: "I could use two pointers (left and right) for O(n) time and O(1) space, but we need indices from the original array, so HashMap is still better."

---

## Complete Solution with Comments

```java
import java.util.*;

class Solution {
    /**
     * Finds two indices where nums[i] + nums[j] = target.
     * 
     * @param nums Input array of integers
     * @param target Target sum to find
     * @return Array of two indices [i, j] where nums[i] + nums[j] = target
     * 
     * Time Complexity: O(n) - single pass through array
     * Space Complexity: O(n) - HashMap storage
     * 
     * Approach: Use HashMap to store seen numbers and check for complement
     * Key Insight: For each number x, check if (target - x) exists in map
     */
    public int[] twoSum(int[] nums, int target) {
        // Edge case: null or too small array (though constraints guarantee n >= 2)
        if (nums == null || nums.length < 2) {
            return new int[0];
        }
        
        // HashMap to store: number -> index
        // Key: value in array
        // Value: index of that value
        Map<Integer, Integer> map = new HashMap<>();
        
        // Single pass through array
        for (int i = 0; i < nums.length; i++) {
            // Calculate complement needed to reach target
            int complement = target - nums[i];
            
            // Check if complement exists in map (already seen)
            if (map.containsKey(complement)) {
                // Found! Return indices (old index first, current index second)
                return new int[]{map.get(complement), i};
            }
            
            // Add current number and its index to map for future lookups
            // Do this AFTER checking to avoid using same element twice
            map.put(nums[i], i);
        }
        
        // Should never reach here per problem guarantee
        // But required for compilation
        return new int[0];
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public class TwoSumTest {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // Test Case 1: Standard case
        int[] nums1 = {2, 7, 11, 15};
        int[] result1 = solution.twoSum(nums1, 9);
        System.out.println(Arrays.toString(result1)); // Expected: [0, 1]
        
        // Test Case 2: Duplicate values
        int[] nums2 = {3, 3};
        int[] result2 = solution.twoSum(nums2, 6);
        System.out.println(Arrays.toString(result2)); // Expected: [0, 1]
        
        // Test Case 3: Negative numbers
        int[] nums3 = {-1, -2, -3, -4, -5};
        int[] result3 = solution.twoSum(nums3, -8);
        System.out.println(Arrays.toString(result3)); // Expected: [2, 4]
        
        // Test Case 4: Zero in array
        int[] nums4 = {0, 4, 3, 0};
        int[] result4 = solution.twoSum(nums4, 0);
        System.out.println(Arrays.toString(result4)); // Expected: [0, 3]
        
        // Test Case 5: Large array
        int[] nums5 = new int[10000];
        for (int i = 0; i < 10000; i++) {
            nums5[i] = i;
        }
        int[] result5 = solution.twoSum(nums5, 19997);
        System.out.println(Arrays.toString(result5)); // Expected: [9998, 9999]
    }
}
```

### Unit Tests:
```java
import org.junit.Test;
import static org.junit.Assert.*;

public class TwoSumTest {
    Solution solution = new Solution();
    
    @Test
    public void testStandardCase() {
        assertArrayEquals(new int[]{0, 1}, solution.twoSum(new int[]{2, 7, 11, 15}, 9));
    }
    
    @Test
    public void testDuplicates() {
        assertArrayEquals(new int[]{0, 1}, solution.twoSum(new int[]{3, 3}, 6));
    }
    
    @Test
    public void testNegativeNumbers() {
        assertArrayEquals(new int[]{2, 4}, solution.twoSum(new int[]{-1, -2, -3, -4, -5}, -8));
    }
    
    @Test
    public void testWithZero() {
        assertArrayEquals(new int[]{0, 3}, solution.twoSum(new int[]{0, 4, 3, 0}, 0));
    }
}
```

---

## Key Takeaways

1. ✅ **Two Sum = HashMap pattern** - Classic complement finding problem
2. ✅ **Trade space for time** - O(n) space enables O(n) time
3. ✅ **One-pass is cleaner** than two-pass HashMap approach
4. ✅ **Check before adding** to map to avoid using same element twice
5. ✅ **Return [old_index, new_index]** - old from map, new from current iteration
6. ✅ **HashMap.containsKey() is O(1)** on average - enables efficient lookup
7. ✅ **Can't do better than O(n) time** - must examine each element at least once
8. ✅ **Sorting loses indices** - can't use two pointers for this specific problem
9. ✅ **Handle duplicates carefully** - one-pass approach handles them naturally
10. ✅ **Foundation for harder problems** - Three Sum, Four Sum, Two Sum II, etc.

---

## Variations & Extensions

After mastering Two Sum, try these related problems:

1. **Two Sum II - Input Array Is Sorted** (Easy)
   - Use two pointers instead of HashMap
   - O(n) time, O(1) space

2. **Two Sum III - Data Structure Design** (Easy)
   - Design class supporting add() and find()
   - Trade-off between add and find efficiency

3. **3Sum** (Medium)
   - Find all unique triplets summing to 0
   - Sort + Two Sum approach, O(n²) time

4. **4Sum** (Medium)
   - Find all unique quadruplets summing to target
   - O(n³) with nested loops

5. **Two Sum Less Than K** (Easy)
   - Find pair with sum < K and maximum sum
   - Sort + two pointers

6. **Two Sum - Unique Pairs** (Medium)
   - Count unique pairs, not indices
   - Handle duplicates differently

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (15-20 minutes for Easy is good)
- [ ] Trace through [2,7,11,15] step by step
- [ ] Code without looking at solution
- [ ] Test all edge cases
- [ ] Explain solution out loud
- [ ] Review in 3 days (spaced repetition)
- [ ] Ready for Contains Duplicate!

---

**Pattern Mastered**: HashMap - Complement Finding ✅  
**Difficulty**: Easy  
**Time to Master**: 15-20 minutes  
**Week 1, Day 1**: Foundation problem - this pattern appears everywhere! 🚀

This is THE most important interview problem to master. It teaches the fundamental HashMap pattern used in countless other problems!
