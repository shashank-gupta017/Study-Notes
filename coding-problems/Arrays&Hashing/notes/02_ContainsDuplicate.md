# Contains Duplicate (Easy)

## Problem Statement
Given an integer array `nums`, return `true` if any value appears **at least twice** in the array, and return `false` if every element is distinct.

**LeetCode Link**: [217. Contains Duplicate](https://leetcode.com/problems/contains-duplicate/)

---

## Examples

### Example 1:
```
Input: nums = [1,2,3,1]
Output: true
Explanation: The element 1 occurs twice (at index 0 and 3).
```

### Example 2:
```
Input: nums = [1,2,3,4]
Output: false
Explanation: All elements are distinct.
```

### Example 3:
```
Input: nums = [1,1,1,3,3,4,3,2,4,2]
Output: true
Explanation: Multiple duplicates exist.
```

### Example 4:
```
Input: nums = [1]
Output: false
Explanation: Single element array has no duplicates.
```

---

## Constraints
- `1 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`

---

## Pattern Recognition

This is a **HashSet** problem because:
1. We need to check if an element **has been seen before**
2. We need **O(1) lookup** to check existence
3. We don't care about indices or counts, just existence
4. We can return early as soon as duplicate found

**Key Insight**: Use HashSet to track seen elements!
- As we iterate, check if element exists in set
- If yes → duplicate found, return true
- If no → add to set and continue
- If loop completes → no duplicates, return false

**Pattern**: Duplicate Detection with HashSet

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Compare every element with every other element.

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        // Compare each pair
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == nums[j]) {
                    return true;
                }
            }
        }
        return false;
    }
}
```

**Time Complexity**: O(n²) - nested loops
**Space Complexity**: O(1) - no extra space
**Problem**: Too slow! Will time out on large inputs

---

### Approach 2: Sorting (ACCEPTABLE)
**Idea**: Sort array, then duplicates will be adjacent.

```java
import java.util.Arrays;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        // Sort the array
        Arrays.sort(nums);
        
        // Check adjacent elements
        for (int i = 0; i < nums.length - 1; i++) {
            if (nums[i] == nums[i + 1]) {
                return true;
            }
        }
        
        return false;
    }
}
```

**Time Complexity**: O(n log n) - dominated by sorting
**Space Complexity**: O(1) or O(n) depending on sort implementation
**Note**: Works but not optimal for this problem

---

### Approach 3: HashSet (OPTIMAL) ⭐
**Idea**: Use HashSet to track seen elements in one pass.

```java
import java.util.*;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        
        for (int num : nums) {
            // If already in set, duplicate found
            if (seen.contains(num)) {
                return true;
            }
            // Add to set
            seen.add(num);
        }
        
        // No duplicates found
        return false;
    }
}
```

**Time Complexity**: O(n) - single pass with O(1) operations
**Space Complexity**: O(n) - worst case store all elements
**Why Optimal**: Can't do better than O(n) - must check each element

---

### Approach 4: HashSet - Compact Version
**Idea**: Leverage Set.add() return value (returns false if element already exists).

```java
import java.util.*;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        
        for (int num : nums) {
            // add() returns false if element already exists
            if (!seen.add(num)) {
                return true;
            }
        }
        
        return false;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(n)
**Note**: More compact, same efficiency

---

### Approach 5: Stream API (Java 8+)
**Idea**: Compare original length with distinct count.

```java
import java.util.Arrays;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        // If distinct count < original count, duplicates exist
        return Arrays.stream(nums).distinct().count() < nums.length;
    }
}
```

**Time Complexity**: O(n) - stream processes all elements
**Space Complexity**: O(n) - distinct() uses HashSet internally
**Note**: Concise but may be slower in practice, processes entire array

---

## Detailed Walkthrough (Approach 3: Optimal)

### Example: nums = [1, 2, 3, 1]

```
Initial state:
seen = {}

Step 1: num = 1
  Is 1 in seen? NO
  Add 1: seen = {1}

Step 2: num = 2
  Is 2 in seen? NO
  Add 2: seen = {1, 2}

Step 3: num = 3
  Is 3 in seen? NO
  Add 3: seen = {1, 2, 3}

Step 4: num = 1
  Is 1 in seen? YES! ✓
  Return true
  
Answer: true (duplicate found at index 3)
```

### Visual Representation

```
Array:  [1,  2,  3,  1]
Index:   0   1   2   3

Pass through array:
i=0: 1 → Not in set, add {1}
i=1: 2 → Not in set, add {1,2}
i=2: 3 → Not in set, add {1,2,3}
i=3: 1 → IN SET! Return true ✓
```

### Example 2: nums = [1, 2, 3, 4]

```
Initial state:
seen = {}

Step 1: num = 1
  Is 1 in seen? NO
  Add 1: seen = {1}

Step 2: num = 2
  Is 2 in seen? NO
  Add 2: seen = {1, 2}

Step 3: num = 3
  Is 3 in seen? NO
  Add 3: seen = {1, 2, 3}

Step 4: num = 4
  Is 4 in seen? NO
  Add 4: seen = {1, 2, 3, 4}

Loop complete, no duplicates
Return false

Answer: false (no duplicates)
```

---

## Edge Cases to Consider

```java
// Test Case 1: Single element
Input: nums = [1]
Output: false
// Can't have duplicate with only one element

// Test Case 2: Two elements, duplicate
Input: nums = [1, 1]
Output: true
// Minimum case with duplicate

// Test Case 3: Two elements, no duplicate
Input: nums = [1, 2]
Output: false
// Minimum case without duplicate

// Test Case 4: All same elements
Input: nums = [5, 5, 5, 5, 5]
Output: true
// Duplicate found immediately at index 1

// Test Case 5: Duplicate at end
Input: nums = [1, 2, 3, 4, 5, 1]
Output: true
// Worst case for HashSet, must check many elements

// Test Case 6: All unique large array
Input: nums = [1, 2, 3, ..., 100000]
Output: false
// Worst case space usage O(n)

// Test Case 7: Negative numbers
Input: nums = [-1, -2, -3, -1]
Output: true
// Negatives work same as positive

// Test Case 8: Zeros
Input: nums = [0, 0]
Output: true
// Zero is a valid duplicate

// Test Case 9: Mix of positive, negative, zero
Input: nums = [-1, 0, 1, -1]
Output: true
// Negative duplicate

// Test Case 10: Large numbers
Input: nums = [1000000000, 999999999, 1000000000]
Output: true
// Near integer limits

// Test Case 11: Maximum size array
Input: nums = new int[100000]; // all unique
Output: false
// Test performance with max size

// Test Case 12: Maximum size array with duplicate
Input: nums = new int[100000]; // one duplicate
Output: true
// Test early termination
```

---

## Common Mistakes to Avoid

### Mistake 1: Forgetting to Check Before Adding
```java
// ❌ WRONG: Always adds then checks
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    
    for (int num : nums) {
        seen.add(num);
        if (seen.contains(num)) {
            // This will ALWAYS be true after adding!
            return true;
        }
    }
    return false;
}

// ✅ CORRECT: Check first, then add
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    
    for (int num : nums) {
        if (seen.contains(num)) {
            return true; // Found duplicate!
        }
        seen.add(num);
    }
    return false;
}
```

### Mistake 2: Using add() Return Value Incorrectly
```java
// ❌ WRONG: Confused logic
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    
    for (int num : nums) {
        // add() returns true if element is NEW
        // returns false if element ALREADY EXISTS
        if (seen.add(num)) {
            return true; // BUG: Returns true for NEW elements!
        }
    }
    return false;
}

// ✅ CORRECT: Negate the return value
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    
    for (int num : nums) {
        if (!seen.add(num)) { // ! negates it
            return true; // Found duplicate
        }
    }
    return false;
}
```

### Mistake 3: Not Returning Early
```java
// ❌ INEFFICIENT: Processes entire array even after finding duplicate
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    boolean hasDuplicate = false;
    
    for (int num : nums) {
        if (seen.contains(num)) {
            hasDuplicate = true; // Found it but keeps going!
        }
        seen.add(num);
    }
    return hasDuplicate;
}

// ✅ CORRECT: Return immediately
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    
    for (int num : nums) {
        if (seen.contains(num)) {
            return true; // Return immediately!
        }
        seen.add(num);
    }
    return false;
}
```

### Mistake 4: Using HashMap Instead of HashSet
```java
// ❌ INEFFICIENT: HashMap unnecessary
public boolean containsDuplicate(int[] nums) {
    Map<Integer, Integer> map = new HashMap<>();
    
    for (int num : nums) {
        if (map.containsKey(num)) {
            return true;
        }
        map.put(num, 1); // Don't need to store count
    }
    return false;
}

// ✅ CORRECT: Use HashSet
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    
    for (int num : nums) {
        if (seen.contains(num)) {
            return true;
        }
        seen.add(num);
    }
    return false;
}
```

### Mistake 5: Null Check Missing
```java
// ❌ POTENTIAL BUG: No null check
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    
    for (int num : nums) { // NPE if nums is null!
        if (seen.contains(num)) {
            return true;
        }
        seen.add(num);
    }
    return false;
}

// ✅ BETTER: Add null check (though LeetCode guarantees non-null)
public boolean containsDuplicate(int[] nums) {
    if (nums == null || nums.length == 0) {
        return false; // or throw exception
    }
    
    Set<Integer> seen = new HashSet<>();
    for (int num : nums) {
        if (seen.contains(num)) {
            return true;
        }
        seen.add(num);
    }
    return false;
}
```

### Mistake 6: Stream API but Checking Wrong Condition
```java
// ❌ WRONG: Incorrect comparison
public boolean containsDuplicate(int[] nums) {
    return Arrays.stream(nums).distinct().count() > nums.length;
    // This can NEVER be true! Distinct count can't exceed original
}

// ✅ CORRECT: Check if distinct count is LESS than original
public boolean containsDuplicate(int[] nums) {
    return Arrays.stream(nums).distinct().count() < nums.length;
}
```

---

## Why HashSet is Essential

### Comparison of Approaches:

```java
// Brute Force: O(n²) time, O(1) space
for (int i = 0; i < n; i++) {
    for (int j = i + 1; j < n; j++) {
        if (nums[i] == nums[j]) return true;
    }
}
// Must compare every pair

// Sorting: O(n log n) time, O(1) space
Arrays.sort(nums);
for (int i = 0; i < n - 1; i++) {
    if (nums[i] == nums[i + 1]) return true;
}
// Sorting takes time

// HashSet: O(n) time, O(n) space
Set<Integer> seen = new HashSet<>();
for (int num : nums) {
    if (seen.contains(num)) return true;
    seen.add(num);
}
// Optimal trade-off!
```

**HashSet Advantages:**
1. ✅ O(1) lookup and insertion (average)
2. ✅ Single pass through array
3. ✅ Early termination when duplicate found
4. ✅ No modification to original array

---

## Complexity Analysis

### Time Complexity: O(n)
- **Single pass**: Loop through array once (n iterations)
- **HashSet operations**: Both `contains()` and `add()` are O(1) average
- **Best case**: O(1) if duplicate is first two elements
- **Average case**: O(n/2) if duplicate in middle
- **Worst case**: O(n) if no duplicates (must check all)
- **Total**: O(n) × O(1) = O(n)

### Space Complexity: O(n)
- **HashSet storage**: Worst case stores all n elements (no duplicates)
- **Best case**: O(1) if duplicate found immediately
- **Average case**: O(n/2) if duplicate in middle
- **Worst case**: O(n) if no duplicates
- **Note**: Early termination saves space when duplicate found early

### Comparison Table:

| Approach | Time | Space | Early Exit | Modifies Array |
|----------|------|-------|------------|----------------|
| Brute Force | O(n²) | O(1) | ✅ Yes | ❌ No |
| Sorting | O(n log n) | O(1)* | ✅ Yes | ⚠️ Yes |
| HashSet | O(n) | O(n) | ✅ Yes | ❌ No |
| Stream API | O(n) | O(n) | ❌ No | ❌ No |

*Sorting space depends on implementation (QuickSort in-place vs MergeSort O(n))

**Optimal Choice**: HashSet for best time complexity with early exit!

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Requirements** (20 seconds)
> "Need to check if any element appears twice or more.
> Return true if duplicate exists, false if all unique.
> Can I modify the array? Let's assume no to preserve original data.
> Can there be negative numbers? Yes.
> What's the size range? Up to 100,000 elements."

**Step 2: Explain Approaches** (2 minutes)
> "I can think of three approaches:
> 1. Brute force with nested loops - O(n²), too slow
> 2. Sort then check adjacent - O(n log n), acceptable but not optimal
> 3. Use HashSet - O(n) time and space, optimal for this problem
> 
> I'll use HashSet. As I iterate through the array, I'll check if each element
> is already in the set. If yes, return true immediately. If no, add it and continue.
> If I complete the loop without finding duplicates, return false."

**Step 3: Discuss Complexity** (30 seconds)
> "Time: O(n) - single pass with O(1) HashSet operations.
> Space: O(n) - worst case store all elements if no duplicates.
> This is optimal - can't do better than O(n) time since we must check each element."

**Step 4: Walk Through Example** (1 minute)
```java
// nums = [1, 2, 3, 1]
// i=0: See 1, not in set, add {1}
// i=1: See 2, not in set, add {1,2}
// i=2: See 3, not in set, add {1,2,3}
// i=3: See 1, FOUND in set! Return true
```

**Step 5: Code** (5 minutes)
- Write clean, commented code
- Use descriptive variable names
- Handle edge cases

**Step 6: Test** (2 minutes)
- Test with provided examples
- Test edge cases (single element, all same, all unique)

### Expected Follow-up Questions:

**Q**: "What if you need to find the duplicate element, not just detect it?"
**A**: "I'd use HashMap instead to count occurrences, or return the element when found: `if (seen.contains(num)) return num;`"

**Q**: "What if you need to find ALL duplicate elements?"
**A**: "I'd use two sets: `seen` and `duplicates`. When an element is in `seen`, add to `duplicates`. Return `duplicates` at the end."

**Q**: "Can you solve without extra space?"
**A**: "Yes, by sorting first then checking adjacent elements - O(n log n) time, O(1) space. But this modifies the array and is slower."

**Q**: "What if the array is already sorted?"
**A**: "Then I'd just check adjacent elements in O(n) time, O(1) space - no need for HashSet!"

**Q**: "How does HashSet handle collisions?"
**A**: "HashSet uses hashing with collision handling (chaining or open addressing). Average case O(1), worst case O(n) if all elements hash to same bucket, but that's extremely rare with good hash function."

**Q**: "What about Contains Duplicate II (within k distance)?"
**A**: "I'd use sliding window with HashSet of size k, or HashMap to store indices and check if |i - j| <= k."

**Q**: "What about Contains Duplicate III (value difference)?"
**A**: "That's harder - use TreeSet to check if any element in range [x-t, x+t] exists, or use buckets."

---

## Complete Solution with Comments

```java
import java.util.*;

class Solution {
    /**
     * Checks if array contains any duplicate elements.
     * 
     * @param nums Input array of integers
     * @return true if any element appears at least twice, false otherwise
     * 
     * Time Complexity: O(n) - single pass through array
     * Space Complexity: O(n) - HashSet storage in worst case
     * 
     * Approach: Use HashSet to track seen elements
     * Key Insight: If element already in set, duplicate found
     */
    public boolean containsDuplicate(int[] nums) {
        // Edge case: null or empty array (though constraints guarantee n >= 1)
        if (nums == null || nums.length == 0) {
            return false; // No elements means no duplicates
        }
        
        // Single element can't have duplicate
        if (nums.length == 1) {
            return false;
        }
        
        // HashSet to track elements we've seen
        Set<Integer> seen = new HashSet<>();
        
        // Single pass through array
        for (int num : nums) {
            // Check if element already exists in set
            if (seen.contains(num)) {
                // Duplicate found! Return immediately
                return true;
            }
            
            // Add current element to set for future checks
            seen.add(num);
        }
        
        // Completed loop without finding duplicates
        return false;
    }
    
    /**
     * Alternative compact version using Set.add() return value
     */
    public boolean containsDuplicateCompact(int[] nums) {
        Set<Integer> seen = new HashSet<>();
        
        for (int num : nums) {
            // add() returns false if element already exists
            if (!seen.add(num)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Alternative using Java 8 Stream API
     */
    public boolean containsDuplicateStream(int[] nums) {
        // If distinct count < original count, duplicates exist
        return Arrays.stream(nums).distinct().count() < nums.length;
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public class ContainsDuplicateTest {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // Test Case 1: Has duplicate
        System.out.println(solution.containsDuplicate(new int[]{1, 2, 3, 1})); 
        // Expected: true
        
        // Test Case 2: No duplicate
        System.out.println(solution.containsDuplicate(new int[]{1, 2, 3, 4})); 
        // Expected: false
        
        // Test Case 3: All same
        System.out.println(solution.containsDuplicate(new int[]{1, 1, 1, 1})); 
        // Expected: true
        
        // Test Case 4: Single element
        System.out.println(solution.containsDuplicate(new int[]{1})); 
        // Expected: false
        
        // Test Case 5: Two elements duplicate
        System.out.println(solution.containsDuplicate(new int[]{1, 1})); 
        // Expected: true
        
        // Test Case 6: Negative numbers
        System.out.println(solution.containsDuplicate(new int[]{-1, -2, -3, -1})); 
        // Expected: true
    }
}
```

### Unit Tests:
```java
import org.junit.Test;
import static org.junit.Assert.*;

public class ContainsDuplicateTest {
    Solution solution = new Solution();
    
    @Test
    public void testHasDuplicate() {
        assertTrue(solution.containsDuplicate(new int[]{1, 2, 3, 1}));
    }
    
    @Test
    public void testNoDuplicate() {
        assertFalse(solution.containsDuplicate(new int[]{1, 2, 3, 4}));
    }
    
    @Test
    public void testAllSame() {
        assertTrue(solution.containsDuplicate(new int[]{1, 1, 1, 1}));
    }
    
    @Test
    public void testSingleElement() {
        assertFalse(solution.containsDuplicate(new int[]{1}));
    }
    
    @Test
    public void testNegativeNumbers() {
        assertTrue(solution.containsDuplicate(new int[]{-1, -2, -3, -1}));
    }
    
    @Test
    public void testWithZero() {
        assertTrue(solution.containsDuplicate(new int[]{0, 0}));
    }
}
```

---

## Key Takeaways

1. ✅ **HashSet for existence checking** - Don't need counts, just presence
2. ✅ **Return early** when duplicate found - Don't process entire array
3. ✅ **Check before adding** - Or use `!set.add(num)` for compact version
4. ✅ **O(1) HashSet operations** enable O(n) solution
5. ✅ **Trade space for time** - O(n) space gets us O(n) time
6. ✅ **Simpler than HashMap** - Only need existence, not frequency
7. ✅ **Can't beat O(n) time** - Must examine each element at least once
8. ✅ **Sorting alternative exists** but slower at O(n log n)
9. ✅ **Early termination is key** - Best/average case much better than worst
10. ✅ **Foundation for variants** - Contains Duplicate II, III build on this

---

## Variations & Extensions

After mastering Contains Duplicate, try these related problems:

1. **Contains Duplicate II** (Easy)
   - Find if duplicate exists within k distance
   - Use sliding window with HashSet or HashMap

2. **Contains Duplicate III** (Medium)
   - Find if two numbers differ by at most t and indices differ by at most k
   - Use TreeSet or Bucket approach

3. **Find All Duplicates in an Array** (Medium)
   - Find all elements that appear twice
   - Can solve in O(n) time, O(1) space using index marking

4. **Find All Numbers Disappeared** (Easy)
   - Find missing numbers in range [1, n]
   - Related concept of tracking seen elements

5. **Single Number** (Easy)
   - Find element that appears once while others appear twice
   - Use XOR bit manipulation

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (10-15 minutes for Easy)
- [ ] Implement all three versions (check first, !add, stream)
- [ ] Test all edge cases
- [ ] Explain solution out loud
- [ ] Compare with sorting approach
- [ ] Review in 3 days (Day 5 from first attempt)
- [ ] Ready for Best Time to Buy and Sell Stock!

---

**Pattern Mastered**: HashSet - Duplicate Detection ✅  
**Difficulty**: Easy  
**Time to Master**: 10-15 minutes  
**Week 1, Day 2**: Simple but important - teaches HashSet usage! 🚀

This problem teaches the fundamental concept of using HashSet for O(1) lookups, which appears in many more complex problems!
