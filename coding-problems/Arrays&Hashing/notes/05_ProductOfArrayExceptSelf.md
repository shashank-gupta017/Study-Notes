# Product of Array Except Self (Medium)

## Problem Statement
Given an integer array `nums`, return an array `answer` such that `answer[i]` is equal to the product of all the elements of `nums` except `nums[i]`.

The product of any prefix or suffix of `nums` is **guaranteed** to fit in a **32-bit** integer.

You must write an algorithm that runs in **O(n) time** and **without using the division operation**.

**LeetCode Link**: [238. Product of Array Except Self](https://leetcode.com/problems/product-of-array-except-self/)

---

## Examples

### Example 1:
```
Input: nums = [1,2,3,4]
Output: [24,12,8,6]

Explanation:
answer[0] = 2 × 3 × 4 = 24  (all except nums[0])
answer[1] = 1 × 3 × 4 = 12  (all except nums[1])
answer[2] = 1 × 2 × 4 = 8   (all except nums[2])
answer[3] = 1 × 2 × 3 = 6   (all except nums[3])
```

### Example 2:
```
Input: nums = [-1,1,0,-3,3]
Output: [0,0,9,0,0]

Explanation:
When nums[2] = 0, all products involving it become 0
answer[2] = (-1) × 1 × (-3) × 3 = 9 (all except the zero)
```

---

## Constraints
- `2 <= nums.length <= 10^5`
- `-30 <= nums[i] <= 30`
- The product of any prefix or suffix is guaranteed to fit in a 32-bit integer
- **Cannot use division operator**
- Must be O(n) time complexity

---

## Pattern Recognition

This is a **Prefix/Suffix Products pattern** problem because:
1. We need information from **both sides** of each element
2. For each index, we need: (product of elements before) × (product of elements after)
3. We can **precompute** products in two passes (left-to-right, right-to-left)
4. The constraint **"without division"** hints at this approach

**Key Insight**: For `answer[i]`, we need the product of everything to the LEFT of `i` multiplied by the product of everything to the RIGHT of `i`. We can build these products in two passes!

**Mental Model**: Imagine standing at each position and looking left (what's the product of everything behind me?) and looking right (what's the product of everything ahead of me?). Multiply these two values!

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: For each index, multiply all other elements.

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        
        for (int i = 0; i < n; i++) {
            int product = 1;
            // Multiply all elements except nums[i]
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    product *= nums[j];
                }
            }
            answer[i] = product;
        }
        
        return answer;
    }
}
```

**Time Complexity**: O(n²) - nested loops
**Space Complexity**: O(1) - excluding output array
**Problem**: Too slow for large inputs (will TLE on LeetCode)

---

### Approach 2: Using Division (NOT ALLOWED)
**Idea**: Calculate total product, then divide by each element.

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        
        // Calculate total product
        int totalProduct = 1;
        for (int num : nums) {
            totalProduct *= num;
        }
        
        // Divide by each element
        for (int i = 0; i < n; i++) {
            answer[i] = totalProduct / nums[i];
        }
        
        return answer;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(1) - excluding output array

**Problems:**
1. ❌ **Division is not allowed** by problem constraints
2. ❌ **Fails with zeros**: `totalProduct / 0` causes error
3. ❌ **Edge case with multiple zeros**: Logic breaks down

---

### Approach 3: Two Arrays (Prefix + Suffix)
**Idea**: Build separate arrays for left products and right products, then multiply.

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        
        // Arrays to store products
        int[] leftProducts = new int[n];
        int[] rightProducts = new int[n];
        int[] answer = new int[n];
        
        // Build left products array
        // leftProducts[i] = product of all elements to the left of i
        leftProducts[0] = 1; // No elements to the left of index 0
        for (int i = 1; i < n; i++) {
            leftProducts[i] = leftProducts[i - 1] * nums[i - 1];
        }
        
        // Build right products array
        // rightProducts[i] = product of all elements to the right of i
        rightProducts[n - 1] = 1; // No elements to the right of last index
        for (int i = n - 2; i >= 0; i--) {
            rightProducts[i] = rightProducts[i + 1] * nums[i + 1];
        }
        
        // Calculate answer
        for (int i = 0; i < n; i++) {
            answer[i] = leftProducts[i] * rightProducts[i];
        }
        
        return answer;
    }
}
```

**Time Complexity**: O(n) - three linear passes
**Space Complexity**: O(n) - two extra arrays

**Pros**: Easy to understand, correct solution
**Cons**: Uses O(n) extra space beyond the output array

---

### Approach 4: Optimized with O(1) Space (OPTIMAL) ⭐
**Idea**: Use the output array itself to store left products, then calculate right products on-the-fly.

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        
        // PHASE 1: Build left products in answer array
        // answer[i] = product of all elements to the left of i
        answer[0] = 1;
        for (int i = 1; i < n; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];
        }
        
        // PHASE 2: Multiply by right products using a variable
        // rightProduct tracks the product of all elements to the right
        int rightProduct = 1;
        for (int i = n - 1; i >= 0; i--) {
            answer[i] = answer[i] * rightProduct;
            rightProduct *= nums[i];
        }
        
        return answer;
    }
}
```

**Time Complexity**: O(n) - two linear passes
**Space Complexity**: O(1) - only one extra variable (excluding output array)

**Why Optimal**: 
- ✅ O(n) time is best possible (must examine all elements)
- ✅ O(1) extra space (output array doesn't count per problem rules)
- ✅ No division operation used
- ✅ Handles zeros naturally

---

## Detailed Walkthrough (Approach 4)

### Example: nums = [2, 3, 4, 5]

#### PHASE 1: Build Left Products in Answer Array

```
Initial: answer = [?, ?, ?, ?]

Step 1: i = 0
  answer[0] = 1 (no elements to the left)
  State: answer = [1, ?, ?, ?]

Step 2: i = 1
  answer[1] = answer[0] × nums[0] = 1 × 2 = 2
  State: answer = [1, 2, ?, ?]
  Meaning: answer[1] = product to the left of index 1 = nums[0]

Step 3: i = 2
  answer[2] = answer[1] × nums[1] = 2 × 3 = 6
  State: answer = [1, 2, 6, ?]
  Meaning: answer[2] = product to the left of index 2 = nums[0] × nums[1]

Step 4: i = 3
  answer[3] = answer[2] × nums[2] = 6 × 4 = 24
  State: answer = [1, 2, 6, 24]
  Meaning: answer[3] = product to the left of index 3 = nums[0] × nums[1] × nums[2]

After Phase 1: answer = [1, 2, 6, 24]
```

**What answer represents after Phase 1:**
- `answer[0] = 1` → (no elements to left)
- `answer[1] = 2` → (2)
- `answer[2] = 6` → (2 × 3)
- `answer[3] = 24` → (2 × 3 × 4)

#### PHASE 2: Multiply by Right Products (Backward Pass)

```
Initial rightProduct = 1

Step 1: i = 3
  answer[3] = answer[3] × rightProduct = 24 × 1 = 24
  rightProduct = rightProduct × nums[3] = 1 × 5 = 5
  State: answer = [1, 2, 6, 24], rightProduct = 5
  Meaning: answer[3] = 2×3×4 (no right elements)

Step 2: i = 2
  answer[2] = answer[2] × rightProduct = 6 × 5 = 30
  rightProduct = rightProduct × nums[2] = 5 × 4 = 20
  State: answer = [1, 2, 30, 24], rightProduct = 20
  Meaning: answer[2] = (2×3) × (5) = 2×3×5

Step 3: i = 1
  answer[1] = answer[1] × rightProduct = 2 × 20 = 40
  rightProduct = rightProduct × nums[1] = 20 × 3 = 60
  State: answer = [1, 40, 30, 24], rightProduct = 60
  Meaning: answer[1] = (2) × (4×5) = 2×4×5

Step 4: i = 0
  answer[0] = answer[0] × rightProduct = 1 × 60 = 60
  rightProduct = rightProduct × nums[0] = 60 × 2 = 120
  State: answer = [60, 40, 30, 24], rightProduct = 120
  Meaning: answer[0] = (nothing) × (3×4×5) = 3×4×5

Final Answer: [60, 40, 30, 24]
```

**Verification:**
- `answer[0] = 3 × 4 × 5 = 60` ✓
- `answer[1] = 2 × 4 × 5 = 40` ✓
- `answer[2] = 2 × 3 × 5 = 30` ✓
- `answer[3] = 2 × 3 × 4 = 24` ✓

---

## Visual Representation

```
Original:  [2,  3,  4,  5]
            ↓   ↓   ↓   ↓
            
Left:      [1,  2,  6, 24]  ← Phase 1: accumulated products from left
            ↑   ↑   ↑   ↑
           (1) (2) (2×3) (2×3×4)

Right:     [60, 20, 5,  1]  ← Phase 2: multiplied by products from right
            ↑   ↑   ↑   ↑
         (3×4×5)(4×5)(5) (1)

Result:    [60, 40, 30, 24] ← Left × Right at each position
```

---

## Edge Cases to Consider

```java
// Test Case 1: Contains zero
nums = [1, 2, 0, 4]
// Expected: [0, 0, 8, 0]
// Only nums[2] will be non-zero (product of non-zero elements)

// Test Case 2: Multiple zeros
nums = [0, 0, 1]
// Expected: [0, 0, 0]
// All results are 0 because multiple zeros

// Test Case 3: Single zero
nums = [1, 2, 3, 0]
// Expected: [0, 0, 0, 6]
// Last element is product of all non-zero: 1×2×3=6

// Test Case 4: Minimum size (n=2)
nums = [5, 9]
// Expected: [9, 5]

// Test Case 5: Negative numbers
nums = [-1, 2, -3, 4]
// Expected: [-24, 12, -8, 6]

// Test Case 6: All ones
nums = [1, 1, 1, 1]
// Expected: [1, 1, 1, 1]

// Test Case 7: Large numbers
nums = [10, 20, 30]
// Expected: [600, 300, 200]
```

---

## Common Mistakes to Avoid

### Mistake 1: Forgetting to initialize answer[0] = 1
```java
// WRONG ❌
int[] answer = new int[n];
for (int i = 1; i < n; i++) {
    answer[i] = answer[i - 1] * nums[i - 1];
}
// answer[0] will be 0, causing all subsequent values to be 0!

// CORRECT ✅
answer[0] = 1;  // No elements to the left of index 0
for (int i = 1; i < n; i++) {
    answer[i] = answer[i - 1] * nums[i - 1];
}
```

### Mistake 2: Wrong index in multiplication
```java
// WRONG ❌
answer[i] = answer[i - 1] * nums[i];  // Using nums[i] instead of nums[i-1]

// CORRECT ✅
answer[i] = answer[i - 1] * nums[i - 1];  // Use previous element
```

### Mistake 3: Updating rightProduct before using it
```java
// WRONG ❌
rightProduct *= nums[i];  // Updated first
answer[i] = answer[i] * rightProduct;  // Uses wrong value

// CORRECT ✅
answer[i] = answer[i] * rightProduct;  // Use first
rightProduct *= nums[i];  // Then update
```

### Mistake 4: Not handling the output array space complexity correctly
**Important**: The problem states "O(1) space" means O(1) **extra** space. The output array doesn't count!

---

## Why This Approach Works

**Prefix/Suffix Principle**:
For each position `i`:
- **Left Product** = `nums[0] × nums[1] × ... × nums[i-1]`
- **Right Product** = `nums[i+1] × nums[i+2] × ... × nums[n-1]`
- **Result** = `Left Product × Right Product`

This gives us the product of all elements except `nums[i]`!

**Space Optimization**:
- Instead of storing left and right products in separate arrays
- Store left products in the output array (reuse it!)
- Calculate right products on-the-fly with a single variable

---

## Complexity Analysis

### Time Complexity: O(n)
- **Phase 1**: Build left products → O(n)
- **Phase 2**: Multiply by right products → O(n)
- **Total**: O(n) + O(n) = O(n)

### Space Complexity: O(1)
- Only one extra variable: `rightProduct`
- The output array doesn't count per problem rules
- No other data structures used

**Note**: If counting output array, it would be O(n), but problem explicitly says output doesn't count toward space complexity.

---

## Pattern: Prefix/Suffix Technique

This pattern appears when you need to calculate something at each position using information from **both sides**.

### Template:
```java
public int[] prefixSuffixPattern(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    
    // Phase 1: Build prefix in result array
    result[0] = initialValue;
    for (int i = 1; i < n; i++) {
        result[i] = combineFunction(result[i-1], nums[i-1]);
    }
    
    // Phase 2: Multiply/combine with suffix
    int suffix = initialValue;
    for (int i = n - 1; i >= 0; i--) {
        result[i] = combineFunction(result[i], suffix);
        suffix = combineFunction(suffix, nums[i]);
    }
    
    return result;
}
```

### When to Use:
- ✅ Need information from **both left and right**
- ✅ Problem says "all except current"
- ✅ Can process in **two passes** (left-to-right, right-to-left)
- ✅ Want to optimize from O(n) space to O(1)

### Related Problems:
1. **Trapping Rain Water** - Uses prefix/suffix max heights
2. **Best Time to Buy/Sell Stock** - Track min from left
3. **Range Sum Query** - Prefix sums
4. **Maximum Sum Subarray** - Kadane's with prefix

---

## Complete Solution with Comments

```java
class Solution {
    /**
     * Returns product of all elements except self for each index.
     * 
     * @param nums Input array of integers
     * @return Array where answer[i] = product of all nums except nums[i]
     * 
     * Time Complexity: O(n) - two passes through array
     * Space Complexity: O(1) - one extra variable (output doesn't count)
     * 
     * Strategy: Use prefix-suffix products
     *   1. Build left products in answer array (left-to-right pass)
     *   2. Multiply by right products using variable (right-to-left pass)
     */
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        
        // PHASE 1: Calculate prefix products (product of elements to the left)
        // answer[i] will store product of all elements before index i
        answer[0] = 1; // No elements to the left of first index
        
        for (int i = 1; i < n; i++) {
            // Product of all elements to the left = 
            // (product to left of previous) × (previous element)
            answer[i] = answer[i - 1] * nums[i - 1];
        }
        
        // PHASE 2: Multiply by suffix products (product of elements to the right)
        // Use a single variable to track running product from right
        int rightProduct = 1;
        
        for (int i = n - 1; i >= 0; i--) {
            // Final answer = (left product) × (right product)
            answer[i] = answer[i] * rightProduct;
            
            // Update right product for next iteration
            rightProduct *= nums[i];
        }
        
        return answer;
    }
}
```

---

## Interview-Ready Code (Most Concise)

```java
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        
        // Build left products
        answer[0] = 1;
        for (int i = 1; i < n; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];
        }
        
        // Multiply by right products
        int right = 1;
        for (int i = n - 1; i >= 0; i--) {
            answer[i] *= right;
            right *= nums[i];
        }
        
        return answer;
    }
}
```

**This is the version to memorize!** Clean, optimal, and correct.

---

## Interview Tips

1. **Clarify Requirements**:
   > "Just to confirm - I can't use division, and need O(n) time with O(1) extra space?"

2. **Start with Simpler Approach**:
   > "I'll first explain a solution using two extra arrays for prefix and suffix 
   > products. Then I'll optimize to O(1) space by reusing the output array."

3. **Explain the Intuition**:
   > "For each index, I need the product of everything before it and everything 
   > after it. I can build these products in two passes - left-to-right for 
   > prefix, then right-to-left for suffix."

4. **Draw It Out**:
   - Show the arrays visually for [2,3,4,5]
   - Demonstrate how left and right products combine

5. **Discuss Edge Cases**:
   - What if there are zeros?
   - Minimum size (n=2)?
   - Negative numbers?

6. **Mention Complexity**:
   > "This is optimal - O(n) time since we must check all elements, and O(1) 
   > extra space as we reuse the output array."

---

## Related Problems (Practice Next)

1. **Trapping Rain Water** (Hard) - Similar prefix/suffix pattern
2. **Range Sum Query - Immutable** (Easy) - Prefix sums
3. **Maximum Subarray** (Medium) - Prefix sum variation
4. **Subarray Sum Equals K** (Medium) - Uses prefix sums with HashMap

---

## Key Takeaways

1. ✅ **Prefix/Suffix Pattern**: Process left-to-right, then right-to-left
2. ✅ **Space Optimization**: Reuse output array for intermediate results
3. ✅ **No Division Needed**: Multiplication-based approach handles all cases
4. ✅ **Handles Zeros Naturally**: No special logic needed
5. ✅ **Two-Pass is Optimal**: O(n) time, O(1) extra space

---

## Practice Checklist

- [ ] Solve this problem on LeetCode
- [ ] Implement the two-array version first
- [ ] Optimize to O(1) space version
- [ ] Trace through with [2,3,4,5] on paper
- [ ] Test with edge cases (zeros, negatives, n=2)
- [ ] Time yourself: aim for <25 minutes
- [ ] Can explain without looking at notes
- [ ] Review in 3 days (Day 8)

---

## Quick Review (Before Interview)

**Q**: What's the pattern?  
**A**: Prefix/Suffix products - build left products, then multiply by right products

**Q**: Time/Space complexity?  
**A**: O(n) time, O(1) extra space (output doesn't count)

**Q**: Key insight?  
**A**: answer[i] = (product of elements before i) × (product of elements after i)

**Q**: Why not use division?  
**A**: Problem forbids it, and it fails with zeros anyway

**Q**: How to optimize space?  
**A**: Reuse output array to store left products, calculate right on-the-fly

---

**Pattern Learned**: Prefix/Suffix Products ✅  
**Difficulty**: Medium  
**Time to Master**: 30-45 minutes  
**Week**: 1, Day 5  
**Next Review**: 2026-01-30 (Day 3)  
**Next Problem**: Group Anagrams (HashMap + Sorting pattern)
