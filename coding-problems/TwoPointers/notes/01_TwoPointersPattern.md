# Two Pointers Pattern - Comprehensive Study Guide

## 📋 Pattern Overview

The **Two Pointers** pattern is a powerful technique that uses two references (pointers) to traverse a data structure, typically moving toward or away from each other. This pattern is especially effective for problems involving arrays or strings where you need to find pairs, check symmetry, or perform in-place operations.

**Core Philosophy**: Instead of using nested loops (O(n²)), use two pointers to solve problems in O(n) time by intelligently moving pointers based on the problem's logic.

---

## 🎯 When to Use Two Pointers

### Primary Indicators:
1. ✅ **Sorted array** or **string** (or can be sorted)
2. ✅ Need to find **pairs/triplets** that satisfy a condition
3. ✅ **Palindrome** or symmetry checking
4. ✅ **In-place** modifications (O(1) space)
5. ✅ Working from both **ends** of the data structure
6. ✅ Need to **optimize** from O(n²) to O(n)

### Key Questions That Suggest Two Pointers:
- "Find two numbers that sum to target in a **sorted** array"
- "Check if string is a palindrome"
- "Remove duplicates **in-place**"
- "Find container with most water"
- "Reverse string/array"

---

## 🔄 Two Pointers Variations

### Variation 1: Opposite Direction (Converging Pointers)
**Pattern**: Start from both ends, move toward center

```java
public void convergingPointers(int[] arr) {
    int left = 0;
    int right = arr.length - 1;
    
    while (left < right) {
        // Process elements at both pointers
        // Move based on condition
        if (someCondition) {
            left++;
        } else {
            right--;
        }
    }
}
```

**Use Cases**:
- Palindrome checking
- Two Sum II (sorted array)
- Container With Most Water
- Trapping Rain Water

**Visual Example** (Finding pair with target sum):
```
arr = [1, 2, 3, 4, 6, 8, 9], target = 10

Step 1:  [1, 2, 3, 4, 6, 8, 9]
          ↑                 ↑
         left             right
         Sum = 1 + 9 = 10 ✓ Found!

If sum < target: move left++
If sum > target: move right--
If sum == target: found the pair!
```

---

### Variation 2: Same Direction (Fast & Slow Pointers)
**Pattern**: Both pointers move left to right, but at different speeds

```java
public void sameDirPointers(int[] arr) {
    int slow = 0;
    int fast = 0;
    
    while (fast < arr.length) {
        // Process with fast pointer
        if (someCondition) {
            // Move slow pointer conditionally
            slow++;
        }
        // Fast pointer always moves
        fast++;
    }
}
```

**Use Cases**:
- Remove duplicates from sorted array
- Move zeros to end
- Partition array
- Linked List cycle detection

**Visual Example** (Remove duplicates):
```
arr = [1, 1, 2, 2, 3, 4]

Initial: [1, 1, 2, 2, 3, 4]
          ↑  ↑
         slow fast

Process: slow points to last unique position
         fast explores ahead
         Copy unique elements to slow position

Result: [1, 2, 3, 4, _, _]
             ↑
            slow (new length = 4)
```

---

### Variation 3: Sliding Window (Special Case)
**Pattern**: Two pointers define a window that expands/contracts

```java
public void slidingWindow(int[] arr) {
    int left = 0;
    
    for (int right = 0; right < arr.length; right++) {
        // Add arr[right] to window
        
        while (windowInvalid) {
            // Shrink window from left
            left++;
        }
        
        // Process valid window [left, right]
    }
}
```

**Use Cases**:
- Longest substring without repeating characters
- Minimum window substring
- Maximum sum subarray of size k

---

## 🎓 Core Concepts & Techniques

### 1. Movement Strategy

**For Sum-Based Problems (Sorted Array)**:
```java
while (left < right) {
    int currentSum = arr[left] + arr[right];
    
    if (currentSum == target) {
        return new int[]{left, right};
    } else if (currentSum < target) {
        left++;  // Need larger sum
    } else {
        right--; // Need smaller sum
    }
}
```

**For Palindrome/Symmetry Problems**:
```java
while (left < right) {
    if (arr[left] != arr[right]) {
        return false; // Not symmetric
    }
    left++;
    right--;
}
return true;
```

**For In-Place Modification**:
```java
int slow = 0;
for (int fast = 0; fast < arr.length; fast++) {
    if (shouldKeep(arr[fast])) {
        arr[slow] = arr[fast];
        slow++;
    }
}
return slow; // New length
```

---

### 2. Boundary Conditions

**Critical Rules**:
- **Convergent**: Loop while `left < right` (not `left <= right` unless you need to process middle element)
- **Same Direction**: Loop while `fast < arr.length`
- **Array Access**: Always check bounds before accessing `arr[left]` or `arr[right]`
- **Empty Array**: Handle `arr.length == 0` case
- **Single Element**: Handle `arr.length == 1` case

---

### 3. Common Patterns

#### Pattern A: Find Pair (Sorted Array)
```java
/**
 * Two Sum II: Find two numbers that add up to target
 * Array is sorted in ascending order
 */
public int[] twoSum(int[] numbers, int target) {
    int left = 0;
    int right = numbers.length - 1;
    
    while (left < right) {
        int sum = numbers[left] + numbers[right];
        
        if (sum == target) {
            return new int[]{left + 1, right + 1}; // 1-indexed
        } else if (sum < target) {
            left++;  // Need larger value
        } else {
            right--; // Need smaller value
        }
    }
    
    return new int[]{-1, -1}; // Not found
}
```

**Time**: O(n), **Space**: O(1)

---

#### Pattern B: Palindrome Checking
```java
/**
 * Valid Palindrome: Check if string is palindrome
 * Ignore non-alphanumeric, case-insensitive
 */
public boolean isPalindrome(String s) {
    int left = 0;
    int right = s.length() - 1;
    
    while (left < right) {
        // Skip non-alphanumeric from left
        while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
            left++;
        }
        
        // Skip non-alphanumeric from right
        while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
            right--;
        }
        
        // Compare characters (case-insensitive)
        if (Character.toLowerCase(s.charAt(left)) != 
            Character.toLowerCase(s.charAt(right))) {
            return false;
        }
        
        left++;
        right--;
    }
    
    return true;
}
```

**Time**: O(n), **Space**: O(1)

---

#### Pattern C: In-Place Removal
```java
/**
 * Remove Duplicates: Remove duplicates from sorted array
 * Return new length, modify array in-place
 */
public int removeDuplicates(int[] nums) {
    if (nums.length == 0) return 0;
    
    int slow = 0; // Points to last unique element
    
    for (int fast = 1; fast < nums.length; fast++) {
        // If found new unique element
        if (nums[fast] != nums[slow]) {
            slow++;
            nums[slow] = nums[fast];
        }
    }
    
    return slow + 1; // Length of unique elements
}
```

**Time**: O(n), **Space**: O(1)

---

#### Pattern D: Container/Area Problems
```java
/**
 * Container With Most Water: Find max area between two lines
 * Height at index i is height[i]
 */
public int maxArea(int[] height) {
    int left = 0;
    int right = height.length - 1;
    int maxWater = 0;
    
    while (left < right) {
        // Width between pointers
        int width = right - left;
        // Height is limited by shorter line
        int h = Math.min(height[left], height[right]);
        // Calculate area
        int area = width * h;
        maxWater = Math.max(maxWater, area);
        
        // Move pointer at shorter line (greedy choice)
        if (height[left] < height[right]) {
            left++;
        } else {
            right--;
        }
    }
    
    return maxWater;
}
```

**Time**: O(n), **Space**: O(1)

**Why Move Shorter Line?**: Moving the taller line would only decrease width without possibility of increasing height, so always move the shorter one.

---

## 🔍 Detailed Examples

### Example 1: Two Sum II (Sorted Array Input)

**Problem**: Given a sorted array, find two numbers that add up to target. Return their indices (1-indexed).

**Input**: `numbers = [2, 7, 11, 15], target = 9`  
**Output**: `[1, 2]` (numbers[0] + numbers[1] = 2 + 7 = 9)

**Solution Strategy**:
1. Start with pointers at both ends
2. Calculate sum at current positions
3. If sum == target: Found!
4. If sum < target: Need larger value → move left++
5. If sum > target: Need smaller value → move right--

**Walkthrough**:
```
numbers = [2, 7, 11, 15], target = 9

Step 1:
[2, 7, 11, 15]
 ↑          ↑
left      right
Sum = 2 + 15 = 17 > 9  →  right--

Step 2:
[2, 7, 11, 15]
 ↑      ↑
left   right
Sum = 2 + 11 = 13 > 9  →  right--

Step 3:
[2, 7, 11, 15]
 ↑  ↑
left right
Sum = 2 + 7 = 9 == 9  ✓ Found!
Return [1, 2] (1-indexed)
```

**Complete Code**:
```java
public int[] twoSum(int[] numbers, int target) {
    int left = 0;
    int right = numbers.length - 1;
    
    while (left < right) {
        int sum = numbers[left] + numbers[right];
        
        if (sum == target) {
            return new int[]{left + 1, right + 1}; // 1-indexed
        } else if (sum < target) {
            left++;
        } else {
            right--;
        }
    }
    
    // Problem guarantees exactly one solution
    return new int[]{-1, -1};
}
```

**Why This Works**:
- Array is sorted, so moving left increases sum, moving right decreases sum
- We never miss the solution because we systematically explore all possibilities
- O(n) time vs O(n²) brute force

---

### Example 2: 3Sum (Finding Triplets)

**Problem**: Find all unique triplets that sum to zero.

**Input**: `nums = [-1, 0, 1, 2, -1, -4]`  
**Output**: `[[-1, -1, 2], [-1, 0, 1]]`

**Solution Strategy**:
1. Sort the array first: O(n log n)
2. Fix one number (outer loop)
3. Use two pointers on remaining array to find pair
4. Skip duplicates to avoid duplicate triplets

**Walkthrough**:
```
Sorted: [-4, -1, -1, 0, 1, 2]

Fix i = 0, target = -(-4) = 4
[-4, -1, -1, 0, 1, 2]
  i   L            R
Sum = -1 + 2 = 1 < 4  →  L++

Fix i = 1, target = -(-1) = 1
[-4, -1, -1, 0, 1, 2]
      i   L        R
Sum = -1 + 2 = 1 == 1  ✓ Found: [-1, -1, 2]
Move both: L++, R--

[-4, -1, -1, 0, 1, 2]
      i      L  R
Sum = 0 + 1 = 1 == 1  ✓ Found: [-1, 0, 1]
```

**Complete Code**:
```java
import java.util.*;

public List<List<Integer>> threeSum(int[] nums) {
    List<List<Integer>> result = new ArrayList<>();
    Arrays.sort(nums); // Sort first
    
    for (int i = 0; i < nums.length - 2; i++) {
        // Skip duplicates for first number
        if (i > 0 && nums[i] == nums[i - 1]) {
            continue;
        }
        
        int left = i + 1;
        int right = nums.length - 1;
        int target = -nums[i]; // We want nums[left] + nums[right] = target
        
        while (left < right) {
            int sum = nums[left] + nums[right];
            
            if (sum == target) {
                result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                
                // Skip duplicates for second number
                while (left < right && nums[left] == nums[left + 1]) {
                    left++;
                }
                // Skip duplicates for third number
                while (left < right && nums[right] == nums[right - 1]) {
                    right--;
                }
                
                left++;
                right--;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
    }
    
    return result;
}
```

**Time**: O(n²) - O(n) for each of n fixed positions  
**Space**: O(1) ignoring output array

---

### Example 3: Container With Most Water

**Problem**: Given heights array, find two lines that form container with maximum water.

**Input**: `height = [1, 8, 6, 2, 5, 4, 8, 3, 7]`  
**Output**: `49` (between index 1 and 8, area = 7 × 7 = 49)

**Key Insight**: 
- Area = width × min(height[left], height[right])
- Width decreases as pointers converge
- To maximize area, always move the shorter line (hoping to find taller line)

**Visual**:
```
Height: [1, 8, 6, 2, 5, 4, 8, 3, 7]
         0  1  2  3  4  5  6  7  8

        8|    █           █      
        7|    █           █     █
        6|    █  █        █     █
        5|    █  █     █  █     █
        4|    █  █     █  █  █  █
        3|    █  █     █  █  █  █ █
        2|    █  █  █  █  █  █  █ █
        1|  █ █  █  █  █  █  █  █ █
         0  1  2  3  4  5  6  7  8

Start: left=0, right=8
Area = 8 × min(1, 7) = 8 × 1 = 8
Move left++ (shorter line)

Next: left=1, right=8
Area = 7 × min(8, 7) = 7 × 7 = 49 ✓ Best!
```

**Complete Code**:
```java
public int maxArea(int[] height) {
    int left = 0;
    int right = height.length - 1;
    int maxWater = 0;
    
    while (left < right) {
        // Calculate current area
        int width = right - left;
        int h = Math.min(height[left], height[right]);
        int area = width * h;
        maxWater = Math.max(maxWater, area);
        
        // Move pointer at shorter line
        if (height[left] < height[right]) {
            left++;
        } else {
            right--;
        }
    }
    
    return maxWater;
}
```

**Why Move Shorter Line?**
- If we move taller line: width decreases, height can only stay same or decrease → area definitely decreases
- If we move shorter line: width decreases, but height might increase → area might increase
- Greedy choice: always try to improve by moving shorter line

---

## ⚠️ Common Mistakes & Pitfalls

### Mistake 1: Wrong Loop Condition
```java
// ❌ WRONG: Using <= for convergent pointers
while (left <= right) {  // Will process middle element twice!
    left++;
    right--;
}

// ✅ CORRECT: Use < for convergent pointers
while (left < right) {
    left++;
    right--;
}
```

### Mistake 2: Not Checking Bounds
```java
// ❌ WRONG: Can cause IndexOutOfBoundsException
while (left < right) {
    left++;  // Might make left >= arr.length
    if (arr[left] == target) { ... } // Crash!
}

// ✅ CORRECT: Check bounds first
while (left < right) {
    if (someCondition) {
        left++;
    }
}
```

### Mistake 3: Forgetting to Skip Duplicates (3Sum)
```java
// ❌ WRONG: Will produce duplicate triplets
for (int i = 0; i < nums.length; i++) {
    // No duplicate check - will count [-1, -1, 2] multiple times
}

// ✅ CORRECT: Skip duplicates
for (int i = 0; i < nums.length; i++) {
    if (i > 0 && nums[i] == nums[i - 1]) {
        continue; // Skip duplicate first element
    }
}
```

### Mistake 4: Moving Both Pointers Incorrectly
```java
// ❌ WRONG: Always moving both pointers
while (left < right) {
    if (sum == target) return true;
    left++;   // Always move both
    right--;  // Might miss solution!
}

// ✅ CORRECT: Move based on condition
while (left < right) {
    if (sum == target) return true;
    else if (sum < target) left++;
    else right--;
}
```

### Mistake 5: Not Handling Character Filtering (Palindrome)
```java
// ❌ WRONG: Comparing without filtering
while (left < right) {
    if (s.charAt(left) != s.charAt(right)) return false;
    left++;
    right--;
}
// Fails on "A man, a plan, a canal: Panama"

// ✅ CORRECT: Skip non-alphanumeric
while (left < right) {
    while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
        left++;
    }
    while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
        right--;
    }
    if (Character.toLowerCase(s.charAt(left)) != 
        Character.toLowerCase(s.charAt(right))) {
        return false;
    }
    left++;
    right--;
}
```

---

## 🎯 Pattern Recognition Flowchart

```
Problem involves array/string?
         |
         ↓ YES
         |
Is array sorted OR can be sorted?
         |
    ┌────┴────┐
    |         |
   YES       NO → Consider other patterns
    |
    ↓
Need to find pairs/triplets?
         |
    ┌────┴────┐
    |         |
   YES       NO
    |         |
    |         ↓
    |    Checking symmetry (palindrome)?
    |         |
    |    ┌────┴────┐
    |    |         |
    |   YES       NO
    |    |         |
    |    |         ↓
    |    |    In-place modification?
    |    |         |
    |    |    ┌────┴────┐
    |    |    |         |
    |    |   YES       NO → Try other patterns
    |    |    |
    ↓    ↓    ↓
    |    |    |
    └────┴────┘
         |
         ↓
    USE TWO POINTERS!
```

---

## 📊 Complexity Analysis

### Time Complexity:
- **Convergent Pointers**: O(n) - each pointer moves at most n times
- **Same Direction**: O(n) - fast pointer traverses once
- **3Sum (nested)**: O(n²) - O(n) for outer loop × O(n) for two pointers
- **With Sorting**: O(n log n) + O(n) = O(n log n)

### Space Complexity:
- Typically **O(1)** - only use pointer variables
- Exception: If creating result array (doesn't count toward space complexity)

### Comparison with Brute Force:

| Problem | Brute Force | Two Pointers | Improvement |
|---------|-------------|--------------|-------------|
| Two Sum II | O(n²) | O(n) | 🚀 n times faster |
| Valid Palindrome | O(n) with extra space | O(n) with O(1) space | 💾 Space savings |
| 3Sum | O(n³) | O(n²) | 🚀 n times faster |
| Container | O(n²) | O(n) | 🚀 n times faster |

---

## 🧩 Problem Classification

### Easy Problems:
1. **Two Sum II** - Basic convergent pointers
2. **Valid Palindrome** - Character filtering + symmetry
3. **Remove Duplicates** - Same direction pointers
4. **Reverse String** - Simple swap with convergent pointers
5. **Squares of Sorted Array** - Merge-like with two pointers

### Medium Problems:
1. **3Sum** - Nested two pointers with duplicate handling
2. **Container With Most Water** - Greedy pointer movement
3. **3Sum Closest** - Variation with closest sum
4. **Sort Colors** - Dutch National Flag (3 pointers)
5. **Subarray Product Less Than K** - Sliding window variant

### Hard Problems:
1. **Trapping Rain Water** - Advanced area calculation
2. **4Sum** - Triple nested with two pointers
3. **Minimum Window Substring** - Complex sliding window

---

## 🎓 Interview Strategy

### Step 1: Recognize Pattern (30 seconds)
Ask yourself:
- Is input sorted? → Likely two pointers
- Need pairs with condition? → Likely two pointers
- Palindrome/symmetry? → Convergent pointers
- In-place modification? → Same direction pointers

### Step 2: Choose Variation (30 seconds)
- **Find pairs**: Convergent (opposite ends)
- **Remove elements**: Same direction (slow/fast)
- **Symmetry**: Convergent (compare from ends)
- **Window**: Sliding window (special case)

### Step 3: Code Template (3-5 minutes)
```java
// Convergent Template
int left = 0, right = arr.length - 1;
while (left < right) {
    // Calculate/compare
    if (condition) {
        // Found or process
    } else if (needLarger) {
        left++;
    } else {
        right--;
    }
}

// Same Direction Template
int slow = 0;
for (int fast = 0; fast < arr.length; fast++) {
    if (shouldKeep) {
        arr[slow] = arr[fast];
        slow++;
    }
}
```

### Step 4: Handle Edge Cases (1-2 minutes)
- Empty array: `arr.length == 0`
- Single element: `arr.length == 1`
- All same elements: `[5, 5, 5, 5]`
- No solution exists: return default value
- Duplicates: skip to avoid processing same element twice

### Step 5: Dry Run (2-3 minutes)
Pick a small example and trace through:
```
Example: [1, 2, 3, 4], target = 6

left=0, right=3: 1+4=5 < 6 → left++
left=1, right=3: 2+4=6 ✓ Found!
```

### What to Say:
> "I notice this array is sorted and we need to find pairs. This is a classic two pointers problem. I'll use convergent pointers starting from both ends. If the sum is too small, I'll move the left pointer right to get a larger value. If it's too large, I'll move the right pointer left. This gives us O(n) time instead of O(n²) brute force."

---

## 🔗 Related Patterns

### Two Pointers vs Other Patterns:

| Pattern | When to Use | Example |
|---------|-------------|---------|
| **Two Pointers** | Sorted array, pairs, symmetry | Two Sum II, Valid Palindrome |
| **Sliding Window** | Contiguous subarray, dynamic window | Longest Substring |
| **Binary Search** | Sorted, find specific element | Search in Rotated Array |
| **Fast & Slow** | Linked list cycle, middle element | Linked List Cycle |

### Pattern Combinations:
- **Two Pointers + HashMap**: Two Sum (unsorted) - O(n) time
- **Two Pointers + Sorting**: 3Sum - sort first, then two pointers
- **Two Pointers + Greedy**: Container With Most Water
- **Two Pointers + Sliding Window**: Subarray problems

---

## 📚 Practice Progression

### Week 1: Foundations (Easy)
- [ ] Two Sum II (Sorted)
- [ ] Valid Palindrome
- [ ] Remove Duplicates from Sorted Array
- [ ] Reverse String
- [ ] Move Zeroes

### Week 2: Core Patterns (Medium)
- [ ] 3Sum
- [ ] Container With Most Water
- [ ] 3Sum Closest
- [ ] Sort Colors (Dutch Flag)
- [ ] Subarray Product Less Than K

### Week 3: Advanced (Medium/Hard)
- [ ] Trapping Rain Water
- [ ] 4Sum
- [ ] Longest Mountain in Array
- [ ] Boats to Save People

### Mastery Checklist:
- [ ] Can identify two pointers pattern in < 30 seconds
- [ ] Know when to use convergent vs same direction
- [ ] Handle duplicates correctly (3Sum)
- [ ] Understand greedy movement logic (Container)
- [ ] Can explain time/space complexity
- [ ] Code without syntax errors in first attempt
- [ ] Complete easy problems in < 20 min
- [ ] Complete medium problems in < 30 min

---

## 🎯 Key Takeaways

1. ✅ **Two pointers optimize** from O(n²) to O(n) by avoiding nested loops
2. ✅ **Sorted arrays** are the strongest indicator for this pattern
3. ✅ **Convergent pointers** (opposite direction) for pairs and symmetry
4. ✅ **Same direction** (fast/slow) for in-place modifications
5. ✅ **Greedy movement**: always move pointer that might improve result
6. ✅ **Loop condition**: `left < right` for convergent, `fast < length` for same direction
7. ✅ **Space complexity**: Almost always O(1) - that's the power of two pointers!
8. ✅ **3Sum pattern**: Fix one element, use two pointers on rest
9. ✅ **Skip duplicates**: Critical for unique results in sum problems
10. ✅ **Practice makes perfect**: Pattern recognition improves with repetition

---

## 🚀 Next Steps

After mastering Two Pointers:
1. **Practice**: Solve 5-10 problems from practice list
2. **Speed**: Time yourself, aim for < 20 min (Easy), < 30 min (Medium)
3. **Variation**: Try all three variations (convergent, same direction, sliding window)
4. **Explanation**: Practice explaining approach out loud
5. **Pattern Chaining**: Combine with other patterns (Binary Search, HashMap)
6. **Move Forward**: Progress to Sliding Window pattern (natural extension)

---

## 📖 Quick Reference Card

```java
// CONVERGENT TEMPLATE
int left = 0, right = arr.length - 1;
while (left < right) {
    if (arr[left] + arr[right] == target) {
        return new int[]{left, right};
    } else if (arr[left] + arr[right] < target) {
        left++;
    } else {
        right--;
    }
}

// SAME DIRECTION TEMPLATE  
int slow = 0;
for (int fast = 0; fast < arr.length; fast++) {
    if (arr[fast] != valueToRemove) {
        arr[slow] = arr[fast];
        slow++;
    }
}
return slow; // new length

// PALINDROME TEMPLATE
int left = 0, right = s.length() - 1;
while (left < right) {
    // Skip invalid characters
    while (left < right && !valid(s.charAt(left))) left++;
    while (left < right && !valid(s.charAt(right))) right--;
    
    // Compare
    if (s.charAt(left) != s.charAt(right)) return false;
    left++;
    right--;
}
return true;

// 3SUM TEMPLATE
Arrays.sort(nums);
for (int i = 0; i < nums.length - 2; i++) {
    if (i > 0 && nums[i] == nums[i-1]) continue; // skip duplicates
    
    int left = i + 1, right = nums.length - 1;
    while (left < right) {
        int sum = nums[i] + nums[left] + nums[right];
        if (sum == 0) {
            // Found triplet
            // Skip duplicates for left and right
        } else if (sum < 0) {
            left++;
        } else {
            right--;
        }
    }
}
```

---

**Pattern Mastered**: Two Pointers ⭐  
**Time to Master**: 3-5 hours across 5-7 problems  
**Difficulty Range**: Easy to Hard  
**Interview Frequency**: ⭐⭐⭐⭐⭐ (Very High)

**Ready for**: Week 2 Problems - Starting with Valid Palindrome! 🚀
