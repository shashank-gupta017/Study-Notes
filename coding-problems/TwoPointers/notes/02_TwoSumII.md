# Two Sum II - Input Array Is Sorted (Medium)

## Problem Statement
Given a **1-indexed** array of integers `numbers` that is already **sorted in non-decreasing order**, find two numbers such that they add up to a specific `target` number. Let these two numbers be `numbers[index1]` and `numbers[index2]` where `1 <= index1 < index2 <= numbers.length`.

Return the **indices** of the two numbers, `index1` and `index2`, **added by one** as an integer array `[index1, index2]` of length 2.

The tests are generated such that there is **exactly one solution**. You **may not** use the same element twice.

Your solution must use only **constant extra space**.

**LeetCode Link**: [167. Two Sum II - Input Array Is Sorted](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/)

---

## Examples

### Example 1:
```
Input: numbers = [2,7,11,15], target = 9
Output: [1,2]
Explanation: The sum of 2 and 7 is 9. Therefore, index1 = 1, index2 = 2.
We return [1, 2] (1-indexed).
```

### Example 2:
```
Input: numbers = [2,3,4], target = 6
Output: [1,3]
Explanation: The sum of 2 and 4 is 6. Therefore, index1 = 1, index2 = 3.
We return [1, 3].
```

### Example 3:
```
Input: numbers = [-1,0], target = -1
Output: [1,2]
Explanation: The sum of -1 and 0 is -1. Therefore, index1 = 1, index2 = 2.
We return [1, 2].
```

### Example 4:
```
Input: numbers = [5,25,75], target = 100
Output: [2,3]
Explanation: The sum of 25 and 75 is 100. Therefore, index1 = 2, index2 = 3.
We return [2, 3].
```

### Example 5:
```
Input: numbers = [1,2,3,4,4,9,56,90], target = 8
Output: [4,5]
Explanation: The sum of 4 and 4 is 8. Therefore, index1 = 4, index2 = 5.
We return [4, 5]. Note: duplicate values are allowed.
```

---

## Constraints
- `2 <= numbers.length <= 3 * 10^4`
- `-1000 <= numbers[i] <= 1000`
- `numbers` is sorted in **non-decreasing order**
- `-1000 <= target <= 1000`
- The tests are generated such that there is **exactly one solution**

---

## Pattern Recognition

This is a **Two Pointers on Sorted Array** problem because:
1. Array is **already sorted** (key enabler!)
2. We need to find **exactly one pair** satisfying sum condition
3. Must use **O(1) space** (constant extra space requirement)
4. Two pointers can efficiently converge to solution

**Key Insight**: Sorted array enables predictable pointer movement!
- If `sum < target`: Move **left** pointer right (need larger values)
- If `sum > target`: Move **right** pointer left (need smaller values)
- If `sum == target`: Found the solution!

**Critical Note**: Return **1-indexed** positions, not 0-indexed!

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Try all possible pairs using nested loops.

```java
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        // Try all pairs
        for (int i = 0; i < numbers.length - 1; i++) {
            for (int j = i + 1; j < numbers.length; j++) {
                if (numbers[i] + numbers[j] == target) {
                    return new int[]{i + 1, j + 1}; // 1-indexed!
                }
            }
        }
        return new int[]{-1, -1}; // Should never reach here
    }
}
```

**Time Complexity**: O(n²) - nested loops
**Space Complexity**: O(1) - only result array
**Problem**: Doesn't use the "sorted" property! Too slow for large inputs.

---

### Approach 2: Two Pointers (OPTIMAL) ⭐
**Idea**: Use two pointers at both ends, move based on sum comparison.

```java
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int left = 0;
        int right = numbers.length - 1;
        
        while (left < right) {
            int sum = numbers[left] + numbers[right];
            
            if (sum == target) {
                // Found! Return 1-indexed positions
                return new int[]{left + 1, right + 1};
            } else if (sum < target) {
                left++;  // Need larger sum
            } else {
                right--; // Need smaller sum
            }
        }
        
        // Should never reach here per problem constraints
        return new int[]{-1, -1};
    }
}
```

**Time Complexity**: O(n) - single pass with two pointers
**Space Complexity**: O(1) - only pointer variables
**Why Optimal**: Linear time, constant space, uses sorted property perfectly!

---

### Approach 3: Binary Search (Alternative)
**Idea**: For each element, binary search for its complement.

```java
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        for (int i = 0; i < numbers.length - 1; i++) {
            int complement = target - numbers[i];
            
            // Binary search for complement in remaining array
            int left = i + 1;
            int right = numbers.length - 1;
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                
                if (numbers[mid] == complement) {
                    return new int[]{i + 1, mid + 1}; // 1-indexed
                } else if (numbers[mid] < complement) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return new int[]{-1, -1};
    }
}
```

**Time Complexity**: O(n log n) - O(n) loop × O(log n) binary search
**Space Complexity**: O(1) - only variables
**Note**: Works but slower than two pointers. Good to mention in interview though!

---

### Approach 4: HashMap (Works but Violates Space Constraint)
**Idea**: Store seen numbers and check for complement (like original Two Sum).

```java
import java.util.*;

class Solution {
    public int[] twoSum(int[] numbers, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        
        for (int i = 0; i < numbers.length; i++) {
            int complement = target - numbers[i];
            
            if (map.containsKey(complement)) {
                // Found! Return 1-indexed
                return new int[]{map.get(complement) + 1, i + 1};
            }
            
            map.put(numbers[i], i);
        }
        
        return new int[]{-1, -1};
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(n) - HashMap storage
**Problem**: Violates "constant extra space" requirement! ❌

---

## Detailed Walkthrough (Approach 2: Two Pointers - Optimal)

### Example: numbers = [2, 7, 11, 15], target = 9

```
Step 1: Initialize two pointers
numbers = [2, 7, 11, 15], target = 9
           L           R
           
left = 0, right = 3
numbers[left] = 2, numbers[right] = 15

Step 2: Calculate sum
sum = 2 + 15 = 17
17 > 9, so sum is too large
Action: Move right pointer left (right--)

Step 3: New position
numbers = [2, 7, 11, 15], target = 9
           L       R
           
left = 0, right = 2
numbers[left] = 2, numbers[right] = 11

Step 4: Calculate sum
sum = 2 + 11 = 13
13 > 9, so sum is still too large
Action: Move right pointer left (right--)

Step 5: New position
numbers = [2, 7, 11, 15], target = 9
           L   R
           
left = 0, right = 1
numbers[left] = 2, numbers[right] = 7

Step 6: Calculate sum
sum = 2 + 7 = 9
9 == 9 ✓ Found the target!

Step 7: Return 1-indexed positions
left + 1 = 0 + 1 = 1
right + 1 = 1 + 1 = 2
Return: [1, 2]
```

---

### Visual Representation

```
Example 1: numbers = [2, 7, 11, 15], target = 9

Initial State:
┌───┬───┬────┬────┐
│ 2 │ 7 │ 11 │ 15 │
└───┴───┴────┴────┘
  ↑              ↑
  L              R
  sum = 17 > 9 → R--

After Move 1:
┌───┬───┬────┬────┐
│ 2 │ 7 │ 11 │ 15 │
└───┴───┴────┴────┘
  ↑       ↑
  L       R
  sum = 13 > 9 → R--

After Move 2:
┌───┬───┬────┬────┐
│ 2 │ 7 │ 11 │ 15 │
└───┴───┴────┴────┘
  ↑   ↑
  L   R
  sum = 9 == 9 ✓ Found!
  Return [1, 2]
```

```
Example 2: numbers = [-1, 0, 1, 2], target = 1

Initial State:
┌────┬───┬───┬───┐
│ -1 │ 0 │ 1 │ 2 │
└────┴───┴───┴───┘
   ↑           ↑
   L           R
   sum = 1 == 1 ✓ Found!
   Return [1, 4]
```

```
Example 3: numbers = [1, 2, 3, 4, 5], target = 9

Initial State:
┌───┬───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │ 5 │
└───┴───┴───┴───┴───┘
  ↑               ↑
  L               R
  sum = 6 < 9 → L++

After Move 1:
┌───┬───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │ 5 │
└───┴───┴───┴───┴───┘
      ↑           ↑
      L           R
  sum = 7 < 9 → L++

After Move 2:
┌───┬───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │ 5 │
└───┴───┴───┴───┴───┘
          ↑       ↑
          L       R
  sum = 8 < 9 → L++

After Move 3:
┌───┬───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │ 5 │
└───┴───┴───┴───┴───┘
              ↑   ↑
              L   R
  sum = 9 == 9 ✓ Found!
  Return [4, 5]
```

---

## Why Two Pointers Works on Sorted Arrays

### The Logic Behind Pointer Movement:

**Sorted Array Property**:
```
numbers = [a, b, c, d, e, f]  where a ≤ b ≤ c ≤ d ≤ e ≤ f
```

**Case 1: sum < target** (need to increase sum)
```
   L           R
   a    ...    f
   sum = a + f < target

What happens if we move R left?
   L       R
   a   ... e
   sum = a + e < a + f (because e ≤ f)
   This makes sum even SMALLER! ❌

What happens if we move L right?
       L       R
       b   ... f
   sum = b + f > a + f (because b ≥ a)
   This makes sum LARGER! ✓

Conclusion: When sum < target, move L right (L++)
```

**Case 2: sum > target** (need to decrease sum)
```
   L           R
   a    ...    f
   sum = a + f > target

What happens if we move L right?
       L       R
       b   ... f
   sum = b + f > a + f (because b ≥ a)
   This makes sum even LARGER! ❌

What happens if we move R left?
   L       R
   a   ... e
   sum = a + e < a + f (because e ≤ f)
   This makes sum SMALLER! ✓

Conclusion: When sum > target, move R left (R--)
```

**Case 3: sum == target**
```
Found the answer! Return [L+1, R+1] (1-indexed)
```

---

## Edge Cases to Consider

```java
// Test Case 1: Minimum size (2 elements)
Input: numbers = [1, 2], target = 3
Output: [1, 2]
// Only one possible pair

// Test Case 2: Negative numbers
Input: numbers = [-5, -3, -1, 0, 2, 4], target = -4
Output: [2, 3]
// -3 + (-1) = -4

// Test Case 3: All negative
Input: numbers = [-10, -5, -3, -1], target = -8
Output: [2, 3]
// -5 + (-3) = -8

// Test Case 4: Mix of negative and positive
Input: numbers = [-1, 0], target = -1
Output: [1, 2]
// -1 + 0 = -1

// Test Case 5: Large positive numbers
Input: numbers = [100, 200, 300, 400], target = 700
Output: [3, 4]
// 300 + 400 = 700

// Test Case 6: Duplicate values allowed
Input: numbers = [1, 2, 3, 3, 4, 5], target = 6
Output: [3, 4] or [2, 6] (multiple valid answers, return any)
// 3 + 3 = 6 or 2 + 4 = 6

// Test Case 7: Target at extremes (first + last)
Input: numbers = [1, 2, 3, 4, 5], target = 6
Output: [1, 5]
// 1 + 5 = 6 (found immediately)

// Test Case 8: Target in middle
Input: numbers = [1, 2, 3, 4, 5], target = 5
Output: [1, 4] or [2, 3]
// 1 + 4 = 5 or 2 + 3 = 5

// Test Case 9: Large array
Input: numbers = [1, 2, 3, ..., 10000], target = 19999
Output: [9999, 10000]
// 9999 + 10000 = 19999

// Test Case 10: Zero in array
Input: numbers = [-3, 0, 1, 3], target = 0
Output: [1, 4]
// -3 + 3 = 0

// Test Case 11: Target is zero with negatives
Input: numbers = [-4, -1, 0, 3, 5], target = 0
Output: [2, 3]
// -1 + 1 doesn't exist, but 0 + 0 or -4 + 4...
// Actually: no valid pair if numbers don't match

// Test Case 12: Same element twice (if duplicates exist)
Input: numbers = [0, 0, 3, 4], target = 0
Output: [1, 2]
// 0 + 0 = 0

// Test Case 13: Very close numbers
Input: numbers = [1, 1, 1, 1, 1, 5], target = 6
Output: [1, 6] or [2, 6] or [3, 6] or [4, 6] or [5, 6]
// 1 + 5 = 6 (multiple valid with duplicates)

// Test Case 14: Immediate solution at extremes
Input: numbers = [2, 7, 11, 15], target = 17
Output: [1, 4]
// 2 + 15 = 17 (first iteration)
```

---

## Common Mistakes to Avoid

### Mistake 1: Returning 0-Indexed Instead of 1-Indexed ❌
```java
// ❌ WRONG: 0-indexed return
if (sum == target) {
    return new int[]{left, right}; // 0-indexed!
}

// ✅ CORRECT: 1-indexed return
if (sum == target) {
    return new int[]{left + 1, right + 1}; // Add 1 for 1-indexed!
}

/**
 * This is THE MOST COMMON mistake!
 * Problem explicitly states 1-indexed output.
 * Always read problem statement carefully!
 */
```

### Mistake 2: Wrong Pointer Movement Logic
```java
// ❌ WRONG: Backwards logic
if (sum < target) {
    right--; // This decreases sum further!
} else if (sum > target) {
    left++;  // This increases sum further!
}

// ✅ CORRECT: Proper movement
if (sum < target) {
    left++;  // Need larger sum
} else if (sum > target) {
    right--; // Need smaller sum
}
```

### Mistake 3: Not Handling Edge Case of Exact Match
```java
// ❌ WRONG: Continuing after finding answer
if (sum == target) {
    result[0] = left + 1;
    result[1] = right + 1;
    // Forgot to return! Loop continues unnecessarily
}

// ✅ CORRECT: Return immediately
if (sum == target) {
    return new int[]{left + 1, right + 1};
}
```

### Mistake 4: Off-by-One Error in Loop Condition
```java
// ❌ WRONG: Using <=
while (left <= right) {
    // If left == right, we're using same element twice!
}

// ✅ CORRECT: Strictly less than
while (left < right) {
    // Ensures we always use two different elements
}
```

### Mistake 5: Integer Overflow (Rare but Possible)
```java
// ❌ POTENTIAL ISSUE: Overflow with large numbers
int sum = numbers[left] + numbers[right];
// If both are close to Integer.MAX_VALUE, this overflows!

// ✅ SAFE: Use long for sum
long sum = (long) numbers[left] + numbers[right];

// OR check constraints (if -1000 to 1000, no overflow possible)
```

### Mistake 6: Forgetting Array is Already Sorted
```java
// ❌ WRONG: Unnecessarily sorting
Arrays.sort(numbers); // Array is already sorted!
// Wasting O(n log n) time

// ✅ CORRECT: Trust problem statement
// Array is guaranteed sorted, no need to sort again
```

### Mistake 7: Trying to Use HashMap When Space is Constrained
```java
// ❌ VIOLATES CONSTRAINT: Using O(n) space
Map<Integer, Integer> map = new HashMap<>();
// Problem requires O(1) space!

// ✅ CORRECT: Use two pointers (O(1) space)
int left = 0, right = numbers.length - 1;
```

---

## Proof of Correctness

### Why Two Pointers Never Misses the Answer:

**Claim**: If a solution exists, two pointers will find it.

**Proof by Contradiction**:

Assume solution exists at indices `(i, j)` where `i < j` and `numbers[i] + numbers[j] == target`.

Suppose our two pointers algorithm doesn't find this solution.

At some point during execution, we have pointers at `(L, R)`.

**Case 1**: `L < i` and `R > j`
```
[... L ... i ... j ... R ...]
```
Since array is sorted: `numbers[L] ≤ numbers[i]` and `numbers[j] ≤ numbers[R]`

Current sum: `numbers[L] + numbers[R]`
Compare with target: `numbers[i] + numbers[j]`

- If `numbers[L] + numbers[R] < target`:
  - We move L right, eventually reaching i
- If `numbers[L] + numbers[R] > target`:
  - We move R left, eventually reaching j
- If `numbers[L] + numbers[R] == target`:
  - We found a solution (might not be (i,j) but still valid)

**Case 2**: `L == i` and `R > j`
```
[... i ... j ... R ...]
     L
```
Current sum: `numbers[i] + numbers[R]`

Since `numbers[j] ≤ numbers[R]` (sorted) and `numbers[i] + numbers[j] == target`:
- If `numbers[i] + numbers[R] > target`: Move R left toward j
- If `numbers[i] + numbers[R] == target`: Found solution
- Eventually R reaches j

**Case 3**: `L < i` and `R == j`
```
[... L ... i ... j ...]
                 R
```
Similar logic: L will move right toward i.

**Case 4**: `L == i` and `R == j`
```
[... i ... j ...]
     L     R
```
We evaluate `numbers[i] + numbers[j] == target` and return the answer!

**Conclusion**: The algorithm must encounter and find any valid solution pair. ✅

---

## Comparison with Original Two Sum

### Two Sum (LeetCode #1) - Unsorted Array:
```java
// Array: [2, 7, 11, 15], target = 9
// Problem: Array is NOT sorted
// Solution: Use HashMap
// Time: O(n), Space: O(n)

public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i}; // 0-indexed
        }
        map.put(nums[i], i);
    }
    return new int[]{};
}
```

### Two Sum II (LeetCode #167) - Sorted Array:
```java
// Array: [2, 7, 11, 15], target = 9
// Key Difference: Array IS sorted
// Solution: Use Two Pointers
// Time: O(n), Space: O(1)

public int[] twoSum(int[] numbers, int target) {
    int left = 0, right = numbers.length - 1;
    while (left < right) {
        int sum = numbers[left] + numbers[right];
        if (sum == target) {
            return new int[]{left + 1, right + 1}; // 1-indexed!
        } else if (sum < target) {
            left++;
        } else {
            right--;
        }
    }
    return new int[]{};
}
```

### Key Differences:

| Aspect | Two Sum | Two Sum II |
|--------|---------|------------|
| Input | Unsorted array | **Sorted** array |
| Index Format | 0-indexed return | **1-indexed** return |
| Space Constraint | No constraint | O(1) space required |
| Optimal Solution | HashMap | **Two Pointers** |
| Time Complexity | O(n) | O(n) |
| Space Complexity | O(n) | **O(1)** |
| Duplicate Elements | Can use same element? No | Different indices required |
| Solution Guarantee | May or may not exist | **Exactly one** solution |

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Constraints** (30 seconds)
> "I see the array is already sorted and we need to return 1-indexed positions. 
> The problem guarantees exactly one solution exists, and we need O(1) space.
> Since it's sorted, I can use two pointers instead of a HashMap."

**Step 2: Explain Approach** (1 minute)
> "I'll use the two pointers technique:
> - Start with left at index 0, right at last index
> - Calculate sum of elements at both pointers
> - If sum equals target, we found it - return [left+1, right+1] for 1-indexed
> - If sum is too small, move left pointer right to get larger values
> - If sum is too large, move right pointer left to get smaller values
> - The sorted property guarantees we'll find the solution in one pass."

**Step 3: Mention Time/Space Complexity** (20 seconds)
> "Time complexity is O(n) since we traverse the array once with both pointers.
> Space complexity is O(1) since we only use two pointer variables.
> This is optimal given the constraints."

**Step 4: Highlight Critical Point** (20 seconds)
> "The most important thing to remember: return 1-indexed positions, not 0-indexed!
> So I'll return [left + 1, right + 1], not [left, right]."

**Step 5: Walk Through Example** (2 minutes)
```java
// Example: numbers = [2, 7, 11, 15], target = 9
// Start: L=0, R=3 → sum=17 > 9 → R--
// Next:  L=0, R=2 → sum=13 > 9 → R--
// Next:  L=0, R=1 → sum=9 == 9 → Return [1, 2]
```

**Step 6: Code and Test** (5-7 minutes)
- Write clean code with proper variable names
- Add comment about 1-indexed return
- Test with provided example
- Test edge case (e.g., [1,2], target=3)

---

### Expected Follow-up Questions:

**Q**: "What if the array wasn't sorted?"
**A**: "Then I'd use a HashMap approach like the original Two Sum problem. Store each element with its index as we iterate, and check if the complement (target - current) exists in the map. That would be O(n) time but O(n) space."

**Q**: "Can you do better than O(n) time?"
**A**: "No, O(n) is optimal because in the worst case, we need to check every element at least once to find the pair. Even binary search approach would be O(n log n) which is slower."

**Q**: "What if there were multiple solutions?"
**A**: "The current approach returns the first valid pair found. If we needed all pairs, we'd continue after finding a match instead of returning immediately, and handle duplicates carefully. But this problem guarantees exactly one solution."

**Q**: "Why not use binary search?"
**A**: "Binary search would work - for each element, search for its complement in the remaining array. But that's O(n log n) time, slower than two pointers which is O(n). Two pointers is cleaner and faster."

**Q**: "How do you handle duplicates in the array?"
**A**: "Duplicates are fine with this approach. We just need two different indices with the target sum. For example, [1,2,3,3,4] with target 6 could return indices for 3+3. The algorithm naturally handles this."

**Q**: "What if we wanted 0-indexed output instead?"
**A**: "Simply remove the +1 when returning: return new int[]{left, right}. The algorithm logic stays the same, just the output format changes."

---

## Complete Solution with Comments

```java
/**
 * Two Sum II - Input Array Is Sorted
 * LeetCode #167 (Medium)
 * 
 * Time Complexity: O(n) - single pass with two pointers
 * Space Complexity: O(1) - only pointer variables
 */
class Solution {
    /**
     * Finds two numbers that add up to target in a sorted array.
     * 
     * @param numbers Sorted array of integers (non-decreasing order)
     * @param target Target sum to find
     * @return 1-indexed positions [index1, index2] where index1 < index2
     * 
     * Approach: Two Pointers
     * - Left pointer starts at beginning, right at end
     * - Move left right if sum too small (need larger value)
     * - Move right left if sum too large (need smaller value)
     * - Return when sum equals target
     * 
     * Key: Array is sorted, so pointer movement is predictable!
     */
    public int[] twoSum(int[] numbers, int target) {
        // Initialize two pointers
        int left = 0;
        int right = numbers.length - 1;
        
        // Converge pointers toward center
        while (left < right) {
            // Calculate current sum
            int sum = numbers[left] + numbers[right];
            
            // Check if we found the target
            if (sum == target) {
                // Found! Return 1-indexed positions (add 1 to each)
                return new int[]{left + 1, right + 1};
                
            } else if (sum < target) {
                // Sum too small, need larger value
                // Move left pointer right (toward larger values)
                left++;
                
            } else {
                // Sum too large, need smaller value
                // Move right pointer left (toward smaller values)
                right--;
            }
        }
        
        // Should never reach here given problem guarantees one solution
        // But included for completeness
        return new int[]{-1, -1};
    }
}
```

---

## Alternative Solutions (For Discussion)

### Solution 2: Binary Search Approach
```java
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        // For each element, binary search for its complement
        for (int i = 0; i < numbers.length - 1; i++) {
            int complement = target - numbers[i];
            
            // Binary search in remaining array
            int index = binarySearch(numbers, i + 1, numbers.length - 1, complement);
            
            if (index != -1) {
                return new int[]{i + 1, index + 1}; // 1-indexed
            }
        }
        
        return new int[]{-1, -1};
    }
    
    private int binarySearch(int[] numbers, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (numbers[mid] == target) {
                return mid;
            } else if (numbers[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return -1;
    }
}
```
**Time**: O(n log n), **Space**: O(1)
**Note**: Slower than two pointers but good to mention as alternative!

---

### Solution 3: Recursive Two Pointers (Educational)
```java
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        return helper(numbers, 0, numbers.length - 1, target);
    }
    
    private int[] helper(int[] numbers, int left, int right, int target) {
        // Base case: pointers crossed
        if (left >= right) {
            return new int[]{-1, -1};
        }
        
        int sum = numbers[left] + numbers[right];
        
        if (sum == target) {
            return new int[]{left + 1, right + 1}; // 1-indexed
        } else if (sum < target) {
            return helper(numbers, left + 1, right, target); // Move left
        } else {
            return helper(numbers, left, right - 1, target); // Move right
        }
    }
}
```
**Time**: O(n), **Space**: O(n) call stack
**Note**: Same logic as iterative but uses recursion. Educational but less practical.

---

## Testing Strategy

### Comprehensive Test Suite:
```java
public class TwoSumIITest {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // Test Case 1: Standard case
        int[] nums1 = {2, 7, 11, 15};
        int[] result1 = solution.twoSum(nums1, 9);
        System.out.println("Test 1: " + Arrays.toString(result1));
        // Expected: [1, 2]
        
        // Test Case 2: Small array
        int[] nums2 = {2, 3, 4};
        int[] result2 = solution.twoSum(nums2, 6);
        System.out.println("Test 2: " + Arrays.toString(result2));
        // Expected: [1, 3]
        
        // Test Case 3: Negative numbers
        int[] nums3 = {-1, 0};
        int[] result3 = solution.twoSum(nums3, -1);
        System.out.println("Test 3: " + Arrays.toString(result3));
        // Expected: [1, 2]
        
        // Test Case 4: All negative
        int[] nums4 = {-10, -5, -3, -1};
        int[] result4 = solution.twoSum(nums4, -8);
        System.out.println("Test 4: " + Arrays.toString(result4));
        // Expected: [2, 3] → -5 + (-3) = -8
        
        // Test Case 5: Duplicates
        int[] nums5 = {1, 2, 3, 3, 4, 5};
        int[] result5 = solution.twoSum(nums5, 6);
        System.out.println("Test 5: " + Arrays.toString(result5));
        // Expected: [3, 4] or [2, 6] (3+3=6 or 2+4=6)
        
        // Test Case 6: Solution at extremes
        int[] nums6 = {1, 2, 3, 4, 5};
        int[] result6 = solution.twoSum(nums6, 6);
        System.out.println("Test 6: " + Arrays.toString(result6));
        // Expected: [1, 5] → 1 + 5 = 6
        
        // Test Case 7: Large numbers
        int[] nums7 = {100, 200, 300, 400};
        int[] result7 = solution.twoSum(nums7, 700);
        System.out.println("Test 7: " + Arrays.toString(result7));
        // Expected: [3, 4] → 300 + 400 = 700
        
        // Test Case 8: Minimum array size
        int[] nums8 = {1, 2};
        int[] result8 = solution.twoSum(nums8, 3);
        System.out.println("Test 8: " + Arrays.toString(result8));
        // Expected: [1, 2]
        
        System.out.println("\n✅ All tests completed!");
    }
}
```

---

## Key Takeaways

1. ✅ **Sorted array enables two pointers** - predictable pointer movement based on sum
2. ✅ **Return 1-indexed positions** - THE most common mistake is returning 0-indexed!
3. ✅ **Move left when sum < target** - need larger values (move toward right)
4. ✅ **Move right when sum > target** - need smaller values (move toward left)
5. ✅ **O(n) time, O(1) space is optimal** - better than HashMap's O(n) space
6. ✅ **Loop condition: left < right** - never left <= right (same element)
7. ✅ **Problem guarantees one solution** - no need to handle "no solution" case strictly
8. ✅ **Binary search works but slower** - O(n log n) vs O(n) for two pointers
9. ✅ **Foundation for 3Sum and 4Sum** - this pattern extends to k-sum problems
10. ✅ **Sorted property is KEY** - without sorting, must use HashMap (different problem)

---

## Related Problems & Patterns

After mastering Two Sum II, these problems follow naturally:

### Easier (Practice Foundation):
1. **Two Sum** (Easy) - Original problem with unsorted array, HashMap approach
2. **Valid Palindrome** (Easy) - Two pointers on string, similar convergence

### Same Difficulty (Apply Pattern):
3. **3Sum** (Medium) - Fix one element + Two Sum II on remaining array
4. **Container With Most Water** (Medium) - Two pointers with area calculation
5. **Trapping Rain Water** (Hard) - Two pointers with tracking max heights

### Harder (Extend Concepts):
6. **4Sum** (Medium) - Fix two elements + Two Sum II pattern
7. **3Sum Closest** (Medium) - Find triplet closest to target (not exact match)
8. **Two Sum IV - BST** (Easy) - Two Sum in Binary Search Tree

---

## Pattern Summary: Two Pointers on Sorted Array

```
Template for Two Pointers on Sorted Array:

1. Check if array is sorted (or sort if allowed)
2. Initialize: left = 0, right = n - 1
3. While left < right:
   a. Calculate current value (sum, area, etc.)
   b. If matches condition: return/store result
   c. If need larger value: left++
   d. If need smaller value: right--
4. Return result

Key Requirements:
✅ Array must be sorted (or sortable)
✅ Need to compare values at two ends
✅ Can eliminate one element per comparison
✅ Works when solution involves pair/triplet

Time: O(n) single pass
Space: O(1) just pointers
```

---

## Complexity Analysis Deep Dive

### Time Complexity: O(n)

**Detailed Breakdown**:
```
- Initialization: O(1) - setting left=0, right=n-1
- While loop: Each iteration moves one pointer
  - Left can move right at most n times
  - Right can move left at most n times
  - Total pointer movements: ≤ n
- Operations per iteration: O(1)
  - Sum calculation: O(1)
  - Comparison: O(1)
  - Pointer update: O(1)
- Total: O(n)
```

**Why not O(n²)?**
```
- NOT nested loops! Single while loop.
- Pointers move monotonically toward each other
- Once a pointer moves, it never goes back
- They meet in the middle after ≤ n moves
```

**Comparison with alternatives**:
```
Brute Force:     O(n²) - check all pairs
Two Pointers:    O(n)  - check ≤ n positions ✅ Best!
Binary Search:   O(n log n) - n iterations × log n search
HashMap:         O(n)  - but O(n) space
```

---

### Space Complexity: O(1)

**Space Usage**:
```
- left pointer: 4 bytes (int)
- right pointer: 4 bytes (int)
- sum variable: 4 bytes (int)
- result array: 8 bytes (2 ints) - considered output, not extra space
- Total extra space: 12 bytes = O(1)
```

**What doesn't count**:
```
✅ Input array: given, not created by us
✅ Output array: return value, not extra space
❌ HashMap: would be O(n) extra space
❌ Recursion: would be O(n) call stack
```

---

## Visual Summary: Two Pointers Decision Tree

```
                    Start: L=0, R=n-1
                           |
                    Calculate sum
                           |
        ┌──────────────────┼──────────────────┐
        │                  │                  │
    sum < target      sum == target      sum > target
        │                  │                  │
    Need LARGER          FOUND!           Need SMALLER
        │                  │                  │
     Move L→           Return               Move ←R
  (L increases)      [L+1, R+1]         (R decreases)
        │                                    │
    Back to top                          Back to top


Visual Flow:
[... L ... ... ... R ...]
     ↑             ↑
     |             |
   sum < target: L moves →
   sum > target: R moves ←
   sum == target: RETURN!

Convergence:
[... L ... ... ... R ...]  Initial
[... ... L ... ... R ...]  After moves
[... ... ... L R ... ...]  Close
[... ... ... LR ... ...]   Adjacent → done
```

---

## Debug Checklist

When your solution doesn't work, check:

```
☐ Are you returning 1-indexed positions? (left+1, right+1)
☐ Is loop condition left < right (not <=)?
☐ Are you moving left when sum < target?
☐ Are you moving right when sum > target?
☐ Are you returning immediately when found?
☐ Did you handle negative numbers correctly?
☐ Did you test with minimum array size (2 elements)?
☐ Did you test with duplicates in array?
☐ Are you avoiding integer overflow (if needed)?
☐ Did you consider all edge cases?
```

---

## Mind Map: Problem Solving Flow

```
Two Sum II Problem
        |
        ├─ Sorted Array? ──→ YES ──→ Use Two Pointers
        |                              |
        |                              ├─ Why? O(1) space
        |                              └─ Predictable movement
        |
        ├─ Return Format? ──→ 1-indexed! ──→ Add 1 to indices
        |
        ├─ Space Constraint? ──→ O(1) required ──→ No HashMap
        |
        ├─ Time Target? ──→ O(n) optimal ──→ Single pass
        |
        └─ Edge Cases?
              |
              ├─ Min size (n=2)
              ├─ Negative numbers
              ├─ Duplicates allowed
              ├─ Solution guaranteed
              └─ Zero in array
```

---

## Advanced Insights

### Why This Problem is a Great Learning Tool:

1. **Foundation for Complex Problems**:
   - 3Sum directly uses this pattern
   - 4Sum extends it further
   - Understanding this makes harder problems easier

2. **Multiple Valid Approaches**:
   - Two Pointers (optimal)
   - Binary Search (alternative)
   - HashMap (different trade-off)
   - Teaches trade-off analysis

3. **Demonstrates Greedy Algorithm**:
   - Make optimal choice at each step
   - Never need to backtrack
   - Locally optimal → globally optimal

4. **Tests Attention to Detail**:
   - 1-indexed vs 0-indexed
   - < vs <= in loop
   - Pointer movement direction
   - These details matter in real code!

---

## Final Interview Script

**Complete 5-minute explanation**:

> "This is Two Sum II where the array is already sorted and we need to return 1-indexed positions with O(1) space.
>
> I'll use two pointers starting at both ends. For each iteration:
> - Calculate sum of elements at left and right pointers
> - If sum equals target, we found it - return [left+1, right+1] for 1-indexed
> - If sum is less than target, move left right to increase sum
> - If sum is greater, move right left to decrease sum
>
> This works because the array is sorted - moving left increases values, moving right decreases values. We're guaranteed to find the solution in one pass.
>
> Time complexity is O(n) since each pointer moves at most n positions. Space is O(1) with just two pointer variables.
>
> The most critical thing to remember: return 1-indexed positions by adding 1 to each index.
>
> Let me code this up..."

[Write clean solution in 5-7 minutes]

---

## Conclusion

Two Sum II is a **perfect** two pointers problem that:
- ✅ Teaches fundamental two pointers technique
- ✅ Emphasizes importance of sorted arrays
- ✅ Tests attention to detail (1-indexed!)
- ✅ Prepares you for 3Sum, 4Sum, and k-Sum problems
- ✅ Demonstrates optimal O(n) time, O(1) space solution

**Master this problem** and you'll have a solid foundation for many interview problems!

---

**Next Steps**:
- [ ] Solve on LeetCode (aim for < 15 minutes)
- [ ] Trace through examples on paper
- [ ] Code without looking at solution
- [ ] Test all edge cases
- [ ] Explain solution out loud
- [ ] Compare with original Two Sum
- [ ] Move on to 3Sum!

---

**Pattern Mastered**: Two Pointers on Sorted Array ✅  
**Difficulty**: Medium  
**Time to Master**: 20-30 minutes  
**Foundation for**: 3Sum, 4Sum, Container With Most Water, Trapping Rain Water

Remember: **Sorted arrays unlock the power of two pointers!** 🚀
