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
Explanation: The element 1 appears at indices 0 and 3.
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
```

---

## Constraints
- `1 <= nums.length <= 10^5`
- `-10^9 <= nums[i] <= 10^9`

---

## Pattern Recognition

This is a **HashSet pattern** problem because:
1. We need to check for **existence** (have we seen this before?)
2. We don't need to store values or indices - just track presence
3. We need **O(1)** lookup time for efficiency

**Key Insight**: Use a HashSet to track elements we've already seen. If we encounter an element that's already in the set, we found a duplicate!

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Compare every element with every other element.

```java
class Solution {
    public boolean containsDuplicate(int[] nums) {
        // Check each pair of elements
        for (int i = 0; i < nums.length; i++) {
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
**Problem**: Too slow for large inputs (fails on LeetCode)

---

### Approach 2: Sorting
**Idea**: Sort the array first. Duplicates will be adjacent.

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

**Time Complexity**: O(n log n) - sorting dominates
**Space Complexity**: O(1) or O(n) depending on sorting algorithm
**Note**: This modifies the original array!

---

### Approach 3: HashSet (OPTIMAL) ⭐
**Idea**: Use a HashSet to track elements we've seen.

```java
import java.util.HashSet;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        // Create a HashSet to track seen elements
        HashSet<Integer> seen = new HashSet<>();
        
        // Iterate through each element
        for (int num : nums) {
            // If element already in set, we found a duplicate
            if (seen.contains(num)) {
                return true;
            }
            // Otherwise, add it to the set
            seen.add(num);
        }
        
        // No duplicates found
        return false;
    }
}
```

**Time Complexity**: O(n) - single pass through array
**Space Complexity**: O(n) - worst case, all elements are unique
**Why Optimal**: Best time complexity, doesn't modify input

---

### Approach 4: One-Liner (Using Java Streams)
```java
import java.util.Arrays;
import java.util.stream.Collectors;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        // Convert to set and compare sizes
        return Arrays.stream(nums).boxed().collect(Collectors.toSet()).size() 
               < nums.length;
    }
}
```

**Explanation**: If the set size is less than array length, there are duplicates.
**Note**: Less efficient in practice due to stream overhead, but concise.

---

## Detailed Walkthrough (Approach 3)

### Example: nums = [1, 2, 3, 1]

```
Initial State:
seen = {}
nums = [1, 2, 3, 1]

Step 1: num = 1
- Is 1 in seen? No
- Add 1 to seen: seen = {1}

Step 2: num = 2
- Is 2 in seen? No
- Add 2 to seen: seen = {1, 2}

Step 3: num = 3
- Is 3 in seen? No
- Add 3 to seen: seen = {1, 2, 3}

Step 4: num = 1
- Is 1 in seen? YES! 
- Return true (duplicate found)
```

---

## Edge Cases to Consider

```java
// Test Case 1: Single element
nums = [1]
// Expected: false (no duplicates possible)

// Test Case 2: All same elements
nums = [5, 5, 5, 5]
// Expected: true

// Test Case 3: Duplicates at the end
nums = [1, 2, 3, 4, 5, 5]
// Expected: true

// Test Case 4: Negative numbers
nums = [-1, -2, -3, -1]
// Expected: true

// Test Case 5: Large numbers
nums = [1000000000, 999999999, 1000000000]
// Expected: true

// Test Case 6: Two elements only - duplicate
nums = [1, 1]
// Expected: true

// Test Case 7: Two elements - unique
nums = [1, 2]
// Expected: false
```

---

## Common Mistakes to Avoid

### Mistake 1: Forgetting to check before adding
```java
// WRONG ❌
for (int num : nums) {
    seen.add(num);
    if (seen.contains(num)) {  // This will always be true!
        return true;
    }
}
```

### Mistake 2: Using contains() then add() separately
```java
// INEFFICIENT (but works)
for (int num : nums) {
    if (seen.contains(num)) {
        return true;
    }
    seen.add(num);
}

// BETTER: Use add() return value
for (int num : nums) {
    if (!seen.add(num)) {  // add() returns false if element exists
        return true;
    }
}
```

**Pro Tip**: `HashSet.add()` returns `false` if element already exists!

### Mistake 3: Using HashMap instead of HashSet
```java
// UNNECESSARY ❌
HashMap<Integer, Integer> map = new HashMap<>();
for (int num : nums) {
    if (map.containsKey(num)) return true;
    map.put(num, 1);  // We don't need the value!
}

// BETTER ✅
HashSet<Integer> set = new HashSet<>();
for (int num : nums) {
    if (!set.add(num)) return true;
}
```

---

## Optimized Version (Using add() return value)

```java
import java.util.HashSet;

class Solution {
    public boolean containsDuplicate(int[] nums) {
        HashSet<Integer> seen = new HashSet<>();
        
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

**Why this is better**:
- One operation instead of two (contains + add)
- Slightly more efficient
- Cleaner code

---

## Complexity Analysis

### Time Complexity: O(n)
- We iterate through the array once: O(n)
- Each HashSet operation (add/contains) is O(1) average case
- Total: O(n) × O(1) = O(n)

### Space Complexity: O(n)
- Worst case: all elements are unique
- We store all n elements in the HashSet
- Best case: O(1) if duplicate is found early

---

## When to Use This Pattern

Use **HashSet** when:
1. ✅ You need to check for **existence** or **uniqueness**
2. ✅ You don't need to store associated values (use HashMap for that)
3. ✅ Order doesn't matter
4. ✅ You want O(1) lookup time

Examples of similar problems:
- Happy Number
- Linked List Cycle (with nodes)
- Jewels and Stones
- First Unique Character

---

## Comparison: HashMap vs HashSet

| Feature | HashSet | HashMap |
|---------|---------|---------|
| Stores | Keys only | Key-Value pairs |
| When to use | Check existence | Store associations |
| Space | Less | More |
| This problem | ✅ Perfect fit | ❌ Overkill |

---

## Practice Variations

Try solving these after mastering this problem:

1. **Contains Duplicate II**: Return true if duplicates are within k distance
2. **Contains Duplicate III**: Check if duplicates are within value range
3. **Remove Duplicates from Sorted Array**: In-place removal
4. **Find All Duplicates**: Return the duplicate elements

---

## Complete Solution with Comments

```java
import java.util.HashSet;

class Solution {
    /**
     * Checks if array contains any duplicate elements.
     * 
     * @param nums Input array of integers
     * @return true if duplicate exists, false otherwise
     * 
     * Time Complexity: O(n) - single pass through array
     * Space Complexity: O(n) - worst case all elements unique
     */
    public boolean containsDuplicate(int[] nums) {
        // Edge case: array with 0 or 1 element has no duplicates
        if (nums == null || nums.length <= 1) {
            return false;
        }
        
        // HashSet to track elements we've seen
        HashSet<Integer> seen = new HashSet<>();
        
        // Check each element
        for (int num : nums) {
            // If add returns false, element already exists
            if (!seen.add(num)) {
                return true;
            }
        }
        
        // No duplicates found
        return false;
    }
}
```

---

## Interview Tips

1. **Clarify Constraints**: Ask about array size, value ranges
2. **Start Simple**: Mention brute force, then optimize
3. **Think Aloud**: "I need fast lookup → HashSet"
4. **Code Clean**: Use meaningful variable names
5. **Test Edge Cases**: Empty, single element, all duplicates
6. **Discuss Trade-offs**: Time vs Space

**Good Response in Interview**:
> "I need to check if elements repeat. A HashSet gives me O(1) lookup, so I'll 
> iterate once and check each element. If it's already in the set, return true. 
> This is O(n) time and O(n) space, which is optimal since I need to check all 
> elements in worst case."

---

## Key Takeaways

1. ✅ **HashSet** for existence checks (no values needed)
2. ✅ Use `add()` return value for cleaner code
3. ✅ O(n) time is optimal for this problem
4. ✅ Space-time trade-off: O(n) space for O(n) time
5. ✅ Always consider edge cases

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (<20 minutes for Easy)
- [ ] Try the optimized version using `add()` return value
- [ ] Attempt without looking at notes
- [ ] Review in 3 days (Day 5)
- [ ] Move to next problem: Best Time to Buy/Sell Stock

---

**Pattern Learned**: HashSet for Duplicate Detection ✅
**Difficulty**: Easy
**Time to Master**: 15-20 minutes
