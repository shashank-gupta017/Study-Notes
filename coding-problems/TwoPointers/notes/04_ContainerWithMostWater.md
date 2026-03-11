# Container With Most Water (Medium)

## Problem Statement
You are given an integer array `height` of length `n`. There are `n` vertical lines drawn such that the two endpoints of the `i`th line are `(i, 0)` and `(i, height[i])`.

Find two lines that together with the x-axis form a container, such that the container contains the **most water**.

Return the **maximum amount of water** a container can store.

**Notice** that you may not slant the container.

**LeetCode Link**: [11. Container With Most Water](https://leetcode.com/problems/container-with-most-water/)

---

## Examples

### Example 1:
```
Input: height = [1,8,6,2,5,4,8,3,7]
Output: 49
Explanation: The vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. 
In this case, the max area of water (blue section) the container can contain is 49.
The lines are at indices 1 (height=8) and 8 (height=7).
Area = min(8, 7) * (8 - 1) = 7 * 7 = 49

Visual:
    |
    |       |
    |   |   |   |
    | | | | | | | |
   1 8 6 2 5 4 8 3 7
   0 1 2 3 4 5 6 7 8
   
   Choose lines at index 1 and 8:
   Height = min(8, 7) = 7
   Width = 8 - 1 = 7
   Area = 7 * 7 = 49
```

### Example 2:
```
Input: height = [1,1]
Output: 1
Explanation: Only one container possible with area = min(1,1) * (1-0) = 1
```

### Example 3:
```
Input: height = [4,3,2,1,4]
Output: 16
Explanation: Choose first and last lines (both height 4)
Area = min(4, 4) * (4 - 0) = 4 * 4 = 16
```

### Example 4:
```
Input: height = [1,2,1]
Output: 2
Explanation: Choose lines at index 1 and 2
Area = min(2, 1) * (2 - 1) = 1 * 1 = 1 (wrong!)
Actually: Choose lines at index 0 and 1: min(1,2) * 1 = 1
Or 1 and 2: min(2,1) * 1 = 1
Or 0 and 2: min(1,1) * 2 = 2 ✓ (maximum)
```

---

## Constraints
- `n == height.length`
- `2 <= n <= 10^5`
- `0 <= height[i] <= 10^4`

---

## Pattern Recognition

This is a **Two Pointers (Greedy)** problem because:
1. We need to find **two positions** that maximize area
2. We can use **greedy choice**: always move the pointer with smaller height
3. Starting from **widest possible** container and narrowing down
4. **Single pass** O(n) solution exists

**Key Insight**: 
- **Area = min(left_height, right_height) × width**
- Start with maximum width (both ends)
- Move pointer at shorter height (only way to potentially increase area)
- The taller line stays because moving it can only decrease area

**Why Greedy Works**:
```
If left_height < right_height:
  - Moving right pointer reduces width AND can't increase height (limited by left)
  - Moving left pointer reduces width BUT might increase height
  - Therefore, only moving left pointer has potential to improve area
```

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Try all possible pairs of lines and calculate area for each.

```java
class Solution {
    public int maxArea(int[] height) {
        int maxArea = 0;
        
        // Try all pairs (i, j) where i < j
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                // Calculate area for this pair
                int h = Math.min(height[i], height[j]);
                int w = j - i;
                int area = h * w;
                
                maxArea = Math.max(maxArea, area);
            }
        }
        
        return maxArea;
    }
}
```

**Time Complexity**: O(n²) - nested loops checking all pairs
**Space Complexity**: O(1) - only use variables
**Problem**: Too slow! Will time out for large inputs (10^5 elements).

---

### Approach 2: Two Pointers (OPTIMAL) ⭐
**Idea**: Start with widest container, greedily move pointer at shorter height.

```java
class Solution {
    public int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        
        while (left < right) {
            // Calculate current area
            int h = Math.min(height[left], height[right]);
            int w = right - left;
            int area = h * w;
            
            // Update maximum
            maxArea = Math.max(maxArea, area);
            
            // Move pointer at shorter height
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
}
```

**Time Complexity**: O(n) - single pass with two pointers
**Space Complexity**: O(1) - only use pointer variables
**Why Optimal**: Can't do better than O(n) - must consider each line at least once

---

### Approach 3: Two Pointers with Optimization
**Idea**: Skip lines that can't possibly improve the area.

```java
class Solution {
    public int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        
        while (left < right) {
            int leftHeight = height[left];
            int rightHeight = height[right];
            int h = Math.min(leftHeight, rightHeight);
            int w = right - left;
            int area = h * w;
            
            maxArea = Math.max(maxArea, area);
            
            // Move pointer at shorter height
            // Skip all lines shorter than or equal to current height
            if (leftHeight < rightHeight) {
                // Skip lines on left that won't improve
                while (left < right && height[left] <= leftHeight) {
                    left++;
                }
            } else {
                // Skip lines on right that won't improve
                while (left < right && height[right] <= rightHeight) {
                    right--;
                }
            }
        }
        
        return maxArea;
    }
}
```

**Time Complexity**: O(n) - still single pass, but fewer iterations
**Space Complexity**: O(1)
**Note**: This optimization doesn't change worst-case complexity but improves average case

---

## Detailed Walkthrough (Approach 2: Optimal)

### Example: height = [1,8,6,2,5,4,8,3,7]

```
Step 1: Initialize pointers
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
          L                       R
          0  1  2  3  4  5  6  7  8

Left = 0, Right = 8
Height = min(1, 7) = 1
Width = 8 - 0 = 8
Area = 1 * 8 = 8
maxArea = 8

Decision: height[L]=1 < height[R]=7, move L++


Step 2: Move left pointer
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
             L                    R
          0  1  2  3  4  5  6  7  8

Left = 1, Right = 8
Height = min(8, 7) = 7
Width = 8 - 1 = 7
Area = 7 * 7 = 49
maxArea = 49 ✓

Decision: height[L]=8 > height[R]=7, move R--


Step 3: Move right pointer
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
             L                 R
          0  1  2  3  4  5  6  7  8

Left = 1, Right = 7
Height = min(8, 3) = 3
Width = 7 - 1 = 6
Area = 3 * 6 = 18
maxArea = 49 (no change)

Decision: height[L]=8 > height[R]=3, move R--


Step 4: Move right pointer
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
             L              R
          0  1  2  3  4  5  6  7  8

Left = 1, Right = 6
Height = min(8, 8) = 8
Width = 6 - 1 = 5
Area = 8 * 5 = 40
maxArea = 49 (no change)

Decision: height[L]=8 == height[R]=8, move R-- (or L++)


Step 5: Move right pointer
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
             L           R
          0  1  2  3  4  5  6  7  8

Left = 1, Right = 5
Height = min(8, 4) = 4
Width = 5 - 1 = 4
Area = 4 * 4 = 16
maxArea = 49 (no change)

Decision: height[L]=8 > height[R]=4, move R--


Step 6: Move right pointer
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
             L        R
          0  1  2  3  4  5  6  7  8

Left = 1, Right = 4
Height = min(8, 5) = 5
Width = 4 - 1 = 3
Area = 5 * 3 = 15
maxArea = 49 (no change)

Decision: height[L]=8 > height[R]=5, move R--


Step 7: Move right pointer
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
             L     R
          0  1  2  3  4  5  6  7  8

Left = 1, Right = 3
Height = min(8, 2) = 2
Width = 3 - 1 = 2
Area = 2 * 2 = 4
maxArea = 49 (no change)

Decision: height[L]=8 > height[R]=2, move R--


Step 8: Move right pointer
height = [1, 8, 6, 2, 5, 4, 8, 3, 7]
             L  R
          0  1  2  3  4  5  6  7  8

Left = 1, Right = 2
Height = min(8, 6) = 6
Width = 2 - 1 = 1
Area = 6 * 1 = 6
maxArea = 49 (no change)

Decision: left < right is now false (1 < 2)
Actually, 1 < 2 is true! Move R--


Step 9: Pointers meet
Left = 1, Right = 1
Loop exits (left < right is false)

Final Answer: maxArea = 49
```

---

### Visual Representation

```
Initial State (Widest Container):
    |
    |       |
    |   |   |   |
    | | | | | | | |
   1 8 6 2 5 4 8 3 7
   L               R
   
   Area = min(1,7) * 8 = 1 * 8 = 8


Best State Found:
    |
    |       |
    |   |   |   |
    | | | | | | | |
   1 8 6 2 5 4 8 3 7
     L             R
     
   Area = min(8,7) * 7 = 7 * 7 = 49


Why Greedy Works:
If we move the taller line (8), we:
  - Reduce width: 7 → 6
  - Keep height limited by 7
  - Can only get worse area
  
If we move the shorter line (7), we:
  - Reduce width: 7 → 6
  - Might find taller line > 7
  - Potential to improve (even if rare)
```

---

### Why Moving Shorter Line is Correct

```
Current: height[L] = 3, height[R] = 8
Area = min(3, 8) * width = 3 * width

Case 1: Move L (shorter)
  New height could be > 3
  New area = min(new_L, 8) * (width-1)
  If new_L > 3: area might improve (new height * smaller width)
  Possibility to improve ✓

Case 2: Move R (taller)  
  New height still limited by 3
  New area = min(3, new_R) * (width-1) = 3 * (width-1)
  Always worse than current! (same height, less width)
  No possibility to improve ✗

Conclusion: Always move pointer at shorter line!
```

---

## Edge Cases to Consider

```java
// Test Case 1: Minimum size
Input: height = [1, 2]
Output: 1
// Only one container: min(1,2) * (1-0) = 1

// Test Case 2: All same height
Input: height = [5, 5, 5, 5]
Output: 15
// Best is first and last: 5 * 3 = 15

// Test Case 3: Increasing heights
Input: height = [1, 2, 3, 4, 5]
Output: 6
// Best is 2 and 5: min(2,5) * (4-1) = 2 * 3 = 6
// Or 1 and 5: min(1,5) * 4 = 4? 
// Actually: 1 and 4: min(1,4) * 3 = 3
// Check all: max is at indices 0 and 4: min(1,5) * 4 = 4
// Or 1 and 4: min(2,5) * 3 = 6 ✓

// Test Case 4: Decreasing heights
Input: height = [5, 4, 3, 2, 1]
Output: 6
// Best is 0 and 3: min(5,2) * 3 = 6

// Test Case 5: One very tall line
Input: height = [1, 100, 1]
Output: 2
// Best is 0 and 2: min(1,1) * 2 = 2
// Even though middle is tall, width limits area

// Test Case 6: Two tall lines far apart
Input: height = [10, 1, 1, 1, 1, 1, 10]
Output: 60
// Best is 0 and 6: min(10,10) * 6 = 60

// Test Case 7: All zeros
Input: height = [0, 0, 0, 0]
Output: 0
// No water can be contained

// Test Case 8: One zero
Input: height = [0, 5, 10]
Output: 5
// Best is 1 and 2: min(5,10) * 1 = 5

// Test Case 9: Large array with peak in middle
Input: height = [1, 2, 3, 4, 5, 4, 3, 2, 1]
Output: 8
// Best is 0 and 8: min(1,1) * 8 = 8

// Test Case 10: Alternating high and low
Input: height = [10, 1, 10, 1, 10]
Output: 40
// Best is 0 and 4: min(10,10) * 4 = 40

// Test Case 11: Two elements equal
Input: height = [7, 7]
Output: 7
// Only option: min(7,7) * 1 = 7

// Test Case 12: Maximum values
Input: height = [10000, 10000]
Output: 10000
// min(10000, 10000) * 1 = 10000
```

---

## Common Mistakes to Avoid

### Mistake 1: Moving the Wrong Pointer
```java
// ❌ WRONG: Move pointer at taller height
if (height[left] > height[right]) {
    left++;  // This will never improve area!
} else {
    right--;
}

// ✅ CORRECT: Move pointer at shorter height
if (height[left] < height[right]) {
    left++;  // Shorter line, potential to improve
} else {
    right--; // Shorter line, potential to improve
}
```

### Mistake 2: Calculating Area Incorrectly
```java
// ❌ WRONG: Using max instead of min
int h = Math.max(height[left], height[right]); // Water would spill!
int area = h * (right - left);

// ✅ CORRECT: Water fills to shorter line
int h = Math.min(height[left], height[right]);
int area = h * (right - left);
```

### Mistake 3: Wrong Loop Condition
```java
// ❌ WRONG: Using <= 
while (left <= right) {
    // When left == right, it's a single line (no container)
}

// ✅ CORRECT: Need two different lines
while (left < right) {
    // Stops when pointers meet
}
```

### Mistake 4: Not Starting at Ends
```java
// ❌ WRONG: Starting elsewhere
int left = 1;  // Should start at 0
int right = height.length - 2;  // Should start at length - 1

// ✅ CORRECT: Start with widest possible container
int left = 0;
int right = height.length - 1;
```

### Mistake 5: Forgetting to Update Max
```java
// ❌ WRONG: Only calculating, not tracking max
while (left < right) {
    int area = Math.min(height[left], height[right]) * (right - left);
    // Forgot to update maxArea!
    if (height[left] < height[right]) left++;
    else right--;
}

// ✅ CORRECT: Always update max
while (left < right) {
    int area = Math.min(height[left], height[right]) * (right - left);
    maxArea = Math.max(maxArea, area);
    if (height[left] < height[right]) left++;
    else right--;
}
```

### Mistake 6: Moving Both Pointers Simultaneously
```java
// ❌ WRONG: Moving both pointers
if (height[left] < height[right]) {
    left++;
}
right--;  // Always moving right - skips valid containers!

// ✅ CORRECT: Move only one pointer based on condition
if (height[left] < height[right]) {
    left++;
} else {
    right--;
}
```

---

## Why Two Pointers is Essential

### Without Two Pointers (Brute Force):
```java
for (int i = 0; i < n; i++) {
    for (int j = i + 1; j < n; j++) {
        // Check every pair: O(n²)
    }
}
// Must check all n*(n-1)/2 pairs
```

### With Two Pointers:
```java
int left = 0, right = n - 1;
while (left < right) {
    // Check n pairs: O(n)
    // Greedy eliminates need to check all pairs
}
```

**Two Pointers Enables:**
1. ✅ **Start with maximum width** (most promising)
2. ✅ **Greedy decision** (move shorter line)
3. ✅ **Eliminate impossible cases** (don't check all pairs)
4. ✅ **Single pass** O(n) solution

**Mathematical Proof of Correctness**:
```
Let optimal solution be at positions (i*, j*) with i* < j*

Our algorithm starts at (0, n-1).

Claim: We will consider position pair (i*, j*) or find better area.

Proof:
- We start at widest position
- At each step, we eliminate one line that cannot be part of optimal solution
- When we move left from i to i+1, we know:
  * height[i] <= height[right]
  * Any container using i and a position < right has:
    - Same or less height (limited by height[i])
    - Less width
    - Therefore, worse area
  * So position i can be safely eliminated
- By elimination, we will reach (i*, j*) unless we found better
```

---

## Optimization Techniques

### Optimization 1: Skip Shorter Lines
```java
public int maxArea(int[] height) {
    int maxArea = 0;
    int left = 0, right = height.length - 1;
    
    while (left < right) {
        int leftHeight = height[left];
        int rightHeight = height[right];
        int area = Math.min(leftHeight, rightHeight) * (right - left);
        maxArea = Math.max(maxArea, area);
        
        // Skip lines that won't improve
        if (leftHeight < rightHeight) {
            while (left < right && height[left] <= leftHeight) {
                left++;
            }
        } else {
            while (left < right && height[right] <= rightHeight) {
                right--;
            }
        }
    }
    
    return maxArea;
}
```

### Optimization 2: Early Termination
```java
public int maxArea(int[] height) {
    int maxArea = 0;
    int left = 0, right = height.length - 1;
    int maxHeight = 0;
    
    // Find maximum height in array (preprocessing)
    for (int h : height) {
        maxHeight = Math.max(maxHeight, h);
    }
    
    while (left < right) {
        int area = Math.min(height[left], height[right]) * (right - left);
        maxArea = Math.max(maxArea, area);
        
        // If current area equals theoretical maximum, we're done
        if (maxArea == maxHeight * (right - left)) {
            break;
        }
        
        if (height[left] < height[right]) {
            left++;
        } else {
            right--;
        }
    }
    
    return maxArea;
}
```

### Optimization 3: Avoid Redundant Calculations
```java
public int maxArea(int[] height) {
    int maxArea = 0;
    int left = 0, right = height.length - 1;
    
    while (left < right) {
        // Cache values to avoid array access
        int leftHeight = height[left];
        int rightHeight = height[right];
        int width = right - left;
        
        // Calculate area based on shorter height
        int area;
        if (leftHeight < rightHeight) {
            area = leftHeight * width;
            left++;
        } else {
            area = rightHeight * width;
            right--;
        }
        
        maxArea = Math.max(maxArea, area);
    }
    
    return maxArea;
}
```

---

## Complexity Analysis

### Time Complexity: O(n)
- **Single pass**: Each element visited at most once
- **Pointer movements**: Left moves rightward, right moves leftward
- **No nested loops**: Linear time
- **Total**: O(n) where n = height.length

### Space Complexity: O(1)
- **Variables**: Only use left, right, maxArea, area
- **No extra data structures**: No arrays, lists, or maps
- **In-place**: Only pointer manipulation

### Comparison with Brute Force:

| Approach | Time | Space | Iterations | Notes |
|----------|------|-------|------------|-------|
| Brute Force | O(n²) | O(1) | n*(n-1)/2 | Check all pairs, TLE |
| Two Pointers | O(n) | O(1) | n | ✅ Optimal, greedy |
| Two Pointers + Skip | O(n) | O(1) | < n | Same worst case, better average |

### Detailed Time Analysis:
```
Input size: n = 10^5 (maximum)

Brute Force:
  Operations: 10^5 * 10^5 / 2 = 5 * 10^9
  Time: ~5 seconds (too slow!)

Two Pointers:
  Operations: 10^5
  Time: ~1 millisecond ✓
  
Speedup: 50,000x faster!
```

---

## Pattern Connection: Container vs Other Two Pointer Problems

### Container With Most Water (This Problem):
```java
// Start at both ends, move based on value comparison
int left = 0, right = n - 1;
while (left < right) {
    // Calculate current value
    // Move pointer at smaller value
    if (height[left] < height[right]) left++;
    else right--;
}
// Find maximum area
```

### Two Sum II (Sorted Array):
```java
// Start at both ends, move based on sum comparison
int left = 0, right = n - 1;
while (left < right) {
    int sum = nums[left] + nums[right];
    if (sum == target) return; // Found!
    else if (sum < target) left++;  // Need larger
    else right--;  // Need smaller
}
// Find exact target
```

### Trapping Rain Water:
```java
// Two pointers + track max heights
int left = 0, right = n - 1;
int leftMax = 0, rightMax = 0;
while (left < right) {
    // Water trapped depends on min of max heights
    if (height[left] < height[right]) {
        leftMax = Math.max(leftMax, height[left]);
        water += leftMax - height[left];
        left++;
    } else {
        rightMax = Math.max(rightMax, height[right]);
        water += rightMax - height[right];
        right--;
    }
}
// Calculate trapped water
```

**Common Pattern**:
- Start with two pointers at extremes
- Move based on comparison/condition
- Single pass O(n) solution
- Greedy approach eliminates unnecessary checks

**Key Differences**:
- Container: Move shorter line (area calculation)
- Two Sum II: Move based on sum vs target
- Trapping Water: Track max heights, move shorter side

---

## Interview Tips

### What to Say During Interview:

**Step 1: Understand the Problem** (1 minute)
> "So we need to find two lines that form the largest container. The area is determined by the shorter line (water level) times the distance between them. We want to maximize this product."

**Step 2: Clarify Constraints** (30 seconds)
> "The array has at least 2 elements, and heights are non-negative. We're looking for the maximum possible area, and we can't tilt the container."

**Step 3: Discuss Brute Force** (1 minute)
> "The naive approach would be to check every pair of lines - that's O(n²) with nested loops. For n = 10^5, that's 5 billion operations, which will timeout."

**Step 4: Explain Optimal Approach** (2 minutes)
> "I'll use two pointers starting at both ends. This gives us the maximum width initially. The key insight is: we should move the pointer at the shorter line, because:
> - Moving the taller line will only decrease area (less width, same height limit)
> - Moving the shorter line might find a taller line and improve area
> This greedy approach guarantees we find the optimal solution in O(n) time."

**Step 5: Walk Through Example** (2-3 minutes)
```
height = [1,8,6,2,5,4,8,3,7]

Start: left=0 (h=1), right=8 (h=7)
  Area = min(1,7) * 8 = 8
  Move left (shorter line)

Step 2: left=1 (h=8), right=8 (h=7)
  Area = min(8,7) * 7 = 49 ← Maximum!
  Move right (shorter line)

Continue until pointers meet...
Final answer: 49
```

**Step 6: Mention Complexity** (30 seconds)
> "Time complexity is O(n) - we visit each element at most once. Space complexity is O(1) - we only use a few variables. This is optimal since we must at least look at every line."

**Step 7: Code** (10-12 minutes)
- Initialize pointers at both ends
- Loop while left < right
- Calculate area, update maximum
- Move pointer at shorter height
- Return maximum area

### Expected Follow-up Questions:

**Q**: "Why do we move the pointer at the shorter line?"
**A**: "Because the area is limited by the shorter line. If we move the taller line, width decreases but height limit stays the same - guaranteed worse. Moving shorter line has potential to find taller line even though width decreases."

**Q**: "Could we use binary search or divide and conquer?"
**A**: "Not efficiently. The problem doesn't have the monotonic property needed for binary search. The maximum could be anywhere. Two pointers with greedy choice is the optimal O(n) approach."

**Q**: "What if we need to find all containers above certain area?"
**A**: "We'd still use two pointers but collect all results where area >= threshold, not just the maximum. Same O(n) time, O(k) space where k is number of valid containers."

**Q**: "Can you prove the greedy approach is correct?"
**A**: "Yes! At each step, we eliminate the line that cannot be part of any better solution than what we've seen. When we move left from position i, any container using i with positions < right has less width and same/less height, so worse area."

**Q**: "What if there are duplicate maximum areas?"
**A**: "We only need to return the maximum value, not the positions. But if positions were needed, we could track them when updating maxArea."

**Q**: "How would you handle negative heights?"
**A**: "Per constraints, heights are non-negative. If negatives were allowed, we'd treat them as 0 since negative heights don't make physical sense for containers."

---

## Complete Solution with Comments

```java
class Solution {
    /**
     * Finds the maximum area of water that can be contained between two lines.
     * 
     * Uses two pointers starting at both ends, moving the pointer at the shorter
     * line inward. This greedy approach guarantees finding the optimal solution.
     * 
     * @param height Array of line heights
     * @return Maximum area of container
     * 
     * Time Complexity: O(n) - single pass with two pointers
     * Space Complexity: O(1) - only use pointer variables
     */
    public int maxArea(int[] height) {
        // Edge case: need at least 2 lines
        if (height == null || height.length < 2) {
            return 0;
        }
        
        int maxArea = 0;
        
        // Initialize two pointers at both ends (maximum width)
        int left = 0;
        int right = height.length - 1;
        
        // Continue until pointers meet
        while (left < right) {
            // Calculate area for current container
            // Height is limited by shorter line (water level)
            // Width is distance between lines
            int currentHeight = Math.min(height[left], height[right]);
            int currentWidth = right - left;
            int currentArea = currentHeight * currentWidth;
            
            // Update maximum area found so far
            maxArea = Math.max(maxArea, currentArea);
            
            // Move pointer at shorter line
            // This is the greedy choice: only shorter line has potential to improve
            if (height[left] < height[right]) {
                left++;   // Left is shorter, move it inward
            } else {
                right--;  // Right is shorter (or equal), move it inward
            }
        }
        
        return maxArea;
    }
}
```

### Alternative Implementation with Optimization:

```java
class Solution {
    public int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0;
        int right = height.length - 1;
        
        while (left < right) {
            // Cache heights to avoid repeated array access
            int leftHeight = height[left];
            int rightHeight = height[right];
            int width = right - left;
            
            // Calculate area and move pointer in one step
            if (leftHeight < rightHeight) {
                maxArea = Math.max(maxArea, leftHeight * width);
                
                // Optimization: skip lines shorter than or equal to current
                int currentLeft = leftHeight;
                while (left < right && height[left] <= currentLeft) {
                    left++;
                }
            } else {
                maxArea = Math.max(maxArea, rightHeight * width);
                
                // Optimization: skip lines shorter than or equal to current
                int currentRight = rightHeight;
                while (left < right && height[right] <= currentRight) {
                    right--;
                }
            }
        }
        
        return maxArea;
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public class Solution {
    public int maxArea(int[] height) {
        int maxArea = 0;
        int left = 0, right = height.length - 1;
        
        while (left < right) {
            int area = Math.min(height[left], height[right]) * (right - left);
            maxArea = Math.max(maxArea, area);
            
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // Test Case 1: Example from problem
        int[] test1 = {1,8,6,2,5,4,8,3,7};
        System.out.println("Test 1: " + solution.maxArea(test1));
        // Expected: 49
        
        // Test Case 2: Minimum size
        int[] test2 = {1, 1};
        System.out.println("Test 2: " + solution.maxArea(test2));
        // Expected: 1
        
        // Test Case 3: All same height
        int[] test3 = {5, 5, 5, 5};
        System.out.println("Test 3: " + solution.maxArea(test3));
        // Expected: 15
        
        // Test Case 4: Increasing
        int[] test4 = {1, 2, 3, 4, 5};
        System.out.println("Test 4: " + solution.maxArea(test4));
        // Expected: 6
        
        // Test Case 5: Two tall lines far apart
        int[] test5 = {10, 1, 1, 1, 1, 1, 10};
        System.out.println("Test 5: " + solution.maxArea(test5));
        // Expected: 60
        
        // Test Case 6: Alternating high and low
        int[] test6 = {10, 1, 10, 1, 10};
        System.out.println("Test 6: " + solution.maxArea(test6));
        // Expected: 40
        
        // Test Case 7: Peak in middle
        int[] test7 = {1, 2, 3, 4, 5, 4, 3, 2, 1};
        System.out.println("Test 7: " + solution.maxArea(test7));
        // Expected: 8
    }
}
```

### JUnit Test Cases:
```java
import org.junit.Test;
import static org.junit.Assert.*;

public class SolutionTest {
    Solution solution = new Solution();
    
    @Test
    public void testBasicCase() {
        assertEquals(49, solution.maxArea(new int[]{1,8,6,2,5,4,8,3,7}));
    }
    
    @Test
    public void testMinimumSize() {
        assertEquals(1, solution.maxArea(new int[]{1, 1}));
    }
    
    @Test
    public void testAllSameHeight() {
        assertEquals(15, solution.maxArea(new int[]{5, 5, 5, 5}));
    }
    
    @Test
    public void testTwoTallLines() {
        assertEquals(60, solution.maxArea(new int[]{10, 1, 1, 1, 1, 1, 10}));
    }
    
    @Test
    public void testWithZeros() {
        assertEquals(0, solution.maxArea(new int[]{0, 0, 0}));
    }
}
```

---

## Key Takeaways

1. ✅ **Greedy approach**: Always move pointer at shorter height
2. ✅ **Start with maximum width**: Begin at both ends
3. ✅ **Area formula**: min(height[left], height[right]) × width
4. ✅ **Single pass**: O(n) time with two pointers
5. ✅ **No extra space**: O(1) space complexity
6. ✅ **Why greedy works**: Moving taller line can only decrease area
7. ✅ **Loop condition**: left < right (need two different lines)
8. ✅ **Optimization**: Skip lines that won't improve area
9. ✅ **Pattern**: Similar to Two Sum II but comparing heights, not sum
10. ✅ **Proof of correctness**: At each step, eliminate line that can't be optimal

---

## Variations & Extensions

After mastering Container With Most Water, try these related problems:

1. **Trapping Rain Water** (Hard) - Calculate trapped water between bars
2. **Largest Rectangle in Histogram** (Hard) - Similar but different area calculation
3. **Maximal Rectangle** (Hard) - 2D version of histogram problem
4. **Two Sum II** (Easy) - Similar two pointers technique
5. **3Sum** (Medium) - Extend two pointers to three elements
6. **Minimum Size Subarray Sum** (Medium) - Two pointers with variable window

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (30-40 minutes for Medium is good)
- [ ] Trace through [1,8,6,2,5,4,8,3,7] step by step
- [ ] Understand why greedy approach works
- [ ] Draw diagrams for visual understanding
- [ ] Explain solution out loud to verify understanding
- [ ] Compare with Trapping Rain Water (similar but different)
- [ ] Review in 3 days for retention
- [ ] Ready for Longest Substring Without Repeating Characters!

---

**Pattern Mastered**: Two Pointers (Greedy) ✅  
**Difficulty**: Medium  
**Time to Master**: 30-40 minutes  
**Core Concept**: Start wide, move shorter line

This problem beautifully demonstrates the power of greedy algorithms with two pointers. The key insight - that moving the shorter line is always the right choice - makes this an elegant O(n) solution! 🚀
