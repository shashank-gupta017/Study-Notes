# Best Time to Buy and Sell Stock (Easy)

## Problem Statement
You are given an array `prices` where `prices[i]` is the price of a given stock on the `i`th day.

You want to maximize your profit by choosing a **single day** to buy one stock and choosing a **different day in the future** to sell that stock.

Return the **maximum profit** you can achieve from this transaction. If you cannot achieve any profit, return `0`.

**LeetCode Link**: [121. Best Time to Buy and Sell Stock](https://leetcode.com/problems/best-time-to-buy-and-sell-stock/)

---

## Examples

### Example 1:
```
Input: prices = [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
```

### Example 2:
```
Input: prices = [7,6,4,3,1]
Output: 0
Explanation: In this case, no transactions are done and the max profit = 0.
```

### Example 3:
```
Input: prices = [2,4,1]
Output: 2
Explanation: Buy on day 1 (price = 2) and sell on day 2 (price = 4), profit = 4-2 = 2.
```

---

## Constraints
- `1 <= prices.length <= 10^5`
- `0 <= prices[i] <= 10^4`

---

## Pattern Recognition

This is a **One-Pass Tracking pattern** problem because:
1. We need to process array **sequentially** (can't sell before buying)
2. We track the **minimum seen so far** (best buy price)
3. We calculate **current profit** at each step
4. We keep the **maximum profit** encountered

**Key Insight**: At each price point, we ask: "If I sell today, what's the best profit I could make?" The best profit is selling at today's price minus the cheapest price we've seen before today.

**Mental Model**: Think of it as walking through prices left to right, always remembering the lowest price you've seen, and calculating potential profit if you sold today.

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Try every possible buy-sell combination.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        
        // Try every buy day
        for (int buy = 0; buy < prices.length; buy++) {
            // Try every sell day after buy
            for (int sell = buy + 1; sell < prices.length; sell++) {
                int profit = prices[sell] - prices[buy];
                maxProfit = Math.max(maxProfit, profit);
            }
        }
        
        return maxProfit;
    }
}
```

**Time Complexity**: O(n²) - nested loops
**Space Complexity**: O(1)
**Problem**: Too slow for large inputs (will TLE on LeetCode)

---

### Approach 2: One-Pass Tracking (OPTIMAL) ⭐
**Idea**: Track the minimum price seen so far, and calculate profit at each step.

```java
class Solution {
    public int maxProfit(int[] prices) {
        // Edge case: no prices or only one price
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        
        // Track minimum price seen so far (best buy price)
        int minPrice = prices[0];
        // Track maximum profit seen so far
        int maxProfit = 0;
        
        // Iterate through each price
        for (int i = 1; i < prices.length; i++) {
            int currentPrice = prices[i];
            
            // Calculate profit if we sell today
            int profit = currentPrice - minPrice;
            
            // Update max profit
            maxProfit = Math.max(maxProfit, profit);
            
            // Update minimum price seen so far
            minPrice = Math.min(minPrice, currentPrice);
        }
        
        return maxProfit;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(1) - only two variables
**Why Optimal**: Can't do better than O(n) since we must examine all prices

---

### Approach 3: Compact Version
```java
class Solution {
    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        
        for (int price : prices) {
            minPrice = Math.min(minPrice, price);
            maxProfit = Math.max(maxProfit, price - minPrice);
        }
        
        return maxProfit;
    }
}
```

**Note**: This is the most concise version, preferred in interviews for its clarity.

---

## Detailed Walkthrough

### Example: prices = [7, 1, 5, 3, 6, 4]

```
Initial State:
minPrice = Integer.MAX_VALUE (or prices[0] = 7)
maxProfit = 0

Step 1: price = 7
- profit = 7 - MAX_VALUE = negative (or 7 - 7 = 0)
- maxProfit = max(0, 0) = 0
- minPrice = min(MAX_VALUE, 7) = 7
State: minPrice=7, maxProfit=0

Step 2: price = 1
- profit = 1 - 7 = -6
- maxProfit = max(0, -6) = 0
- minPrice = min(7, 1) = 1
State: minPrice=1, maxProfit=0
💡 Found new best buy price!

Step 3: price = 5
- profit = 5 - 1 = 4
- maxProfit = max(0, 4) = 4
- minPrice = min(1, 5) = 1
State: minPrice=1, maxProfit=4
💡 If we bought at 1 and sold at 5, profit = 4

Step 4: price = 3
- profit = 3 - 1 = 2
- maxProfit = max(4, 2) = 4
- minPrice = min(1, 3) = 1
State: minPrice=1, maxProfit=4
💡 Profit today (2) is less than max (4), so keep max

Step 5: price = 6
- profit = 6 - 1 = 5
- maxProfit = max(4, 5) = 5
- minPrice = min(1, 6) = 1
State: minPrice=1, maxProfit=5
💡 New best profit! Buy at 1, sell at 6

Step 6: price = 4
- profit = 4 - 1 = 3
- maxProfit = max(5, 3) = 5
- minPrice = min(1, 4) = 1
State: minPrice=1, maxProfit=5

Final Answer: 5
```

---

## Visual Representation

```
Prices:  [7,  1,  5,  3,  6,  4]
          ↓   ↓   ↑       ↑
        Start BUY        SELL
        
        minPrice tracks: 7 → 1 → 1 → 1 → 1 → 1
        maxProfit:       0 → 0 → 4 → 4 → 5 → 5
        
        Best trade: Buy at 1, Sell at 6 = Profit 5
```

---

## Edge Cases to Consider

```java
// Test Case 1: Prices always decreasing
prices = [5, 4, 3, 2, 1]
// Expected: 0 (no profit possible)

// Test Case 2: Prices always increasing
prices = [1, 2, 3, 4, 5]
// Expected: 4 (buy at 1, sell at 5)

// Test Case 3: Single price
prices = [5]
// Expected: 0 (can't buy and sell on same day)

// Test Case 4: Two prices - profit possible
prices = [1, 5]
// Expected: 4

// Test Case 5: Two prices - no profit
prices = [5, 1]
// Expected: 0

// Test Case 6: All same prices
prices = [3, 3, 3, 3]
// Expected: 0

// Test Case 7: Profit at the end
prices = [7, 6, 4, 3, 1, 10]
// Expected: 9 (buy at 1, sell at 10)

// Test Case 8: Best profit not at extremes
prices = [3, 1, 4, 1, 5, 9]
// Expected: 8 (buy at 1, sell at 9)
```

---

## Common Mistakes to Avoid

### Mistake 1: Updating minPrice before calculating profit
```java
// WRONG ❌
for (int price : prices) {
    minPrice = Math.min(minPrice, price);  // Updated first
    maxProfit = Math.max(maxProfit, price - minPrice);  // Will be 0!
}

// CORRECT ✅
for (int price : prices) {
    maxProfit = Math.max(maxProfit, price - minPrice);  // Calculate first
    minPrice = Math.min(minPrice, price);  // Then update
}
```

**Why it matters**: If you update minPrice first, you might calculate profit as (price - price) = 0 on the same day.

**Actually, this order doesn't matter!** Because:
- If current price becomes new minPrice, then profit = 0 (which is fine)
- If current price is not new minPrice, calculation uses old minPrice (correct)

Both orders work! But calculating profit first is more intuitive.

### Mistake 2: Trying to track buy and sell indices
```java
// UNNECESSARY ❌
int buyDay = 0, sellDay = 0;  // Don't need these!
// Problem only asks for profit amount, not the days

// CORRECT ✅
// Just track minPrice and maxProfit
```

### Mistake 3: Forgetting that profit can be 0
```java
// WRONG ❌
int maxProfit = Integer.MIN_VALUE;  // Don't use MIN_VALUE!

// CORRECT ✅
int maxProfit = 0;  // Start at 0, no profit is valid answer
```

### Mistake 4: Allowing buy and sell on same day
```java
// WRONG ❌
for (int i = 0; i < prices.length; i++) {
    for (int j = i; j < prices.length; j++) {  // j starts at i
        // This allows same-day transactions
    }
}

// CORRECT ✅
for (int i = 0; i < prices.length; i++) {
    for (int j = i + 1; j < prices.length; j++) {  // j starts at i+1
        // Must sell AFTER buying
    }
}
```

---

## Why This Approach Works

**Greedy Principle**: At each step, we want to:
1. Know the **cheapest price so far** (best opportunity to buy)
2. Calculate **profit if we sell today** (current price - cheapest)
3. Keep track of the **best profit** we've seen

**Correctness Proof**:
- We consider every possible selling day
- For each selling day, we know the minimum buy price before it
- Therefore, we find the maximum profit possible

---

## Pattern Variations

This **"Track minimum/maximum so far"** pattern appears in:

1. **Best Time to Buy/Sell Stock**: Track minimum price
2. **Trapping Rain Water**: Track maximum heights
3. **Product of Array Except Self**: Track products
4. **Maximum Subarray (Kadane's)**: Track maximum sum

---

## Complexity Analysis

### Time Complexity: O(n)
- Single pass through the array: O(n)
- Each operation inside loop is O(1)
- Total: O(n)

### Space Complexity: O(1)
- Only two variables: minPrice and maxProfit
- No additional data structures
- Constant space regardless of input size

---

## Alternative Perspective: Kadane's Algorithm Connection

This problem is related to **Kadane's Algorithm** for maximum subarray sum!

```java
// Converting to "difference array" approach
class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int currentProfit = 0;
        
        // Calculate profit as difference from previous day
        for (int i = 1; i < prices.length; i++) {
            int gain = prices[i] - prices[i - 1];
            currentProfit = Math.max(0, currentProfit + gain);
            maxProfit = Math.max(maxProfit, currentProfit);
        }
        
        return maxProfit;
    }
}
```

**Explanation**: 
- Calculate daily gains: [1-7=-6, 5-1=4, 3-5=-2, 6-3=3, 4-6=-2]
- Find maximum subarray sum of gains
- This is Kadane's algorithm!

---

## Complete Solution with Comments

```java
class Solution {
    /**
     * Finds maximum profit from buying and selling stock once.
     * 
     * @param prices Array of daily stock prices
     * @return Maximum profit possible, or 0 if no profit
     * 
     * Time Complexity: O(n) - single pass
     * Space Complexity: O(1) - constant space
     * 
     * Strategy: Track minimum price seen so far (best buy opportunity)
     *           and calculate potential profit at each price point.
     */
    public int maxProfit(int[] prices) {
        // Edge case: can't make profit with 0 or 1 price
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        
        // Track the minimum price seen so far (best buy price)
        int minPrice = Integer.MAX_VALUE;
        
        // Track the maximum profit we can achieve
        int maxProfit = 0;
        
        // Examine each price
        for (int price : prices) {
            // Update minimum price (cheapest day to buy so far)
            minPrice = Math.min(minPrice, price);
            
            // Calculate profit if we sell at current price
            int profit = price - minPrice;
            
            // Update maximum profit
            maxProfit = Math.max(maxProfit, profit);
        }
        
        return maxProfit;
    }
}
```

---

## Interview-Ready Code (Most Concise)

```java
class Solution {
    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        
        for (int price : prices) {
            minPrice = Math.min(minPrice, price);
            maxProfit = Math.max(maxProfit, price - minPrice);
        }
        
        return maxProfit;
    }
}
```

**This is the version to memorize!** Clean, concise, and optimal.

---

## Interview Tips

1. **Clarify Constraints**: 
   - "Can I buy and sell on the same day?" → No
   - "What if prices always decrease?" → Return 0
   - "Can prices be negative?" → Check constraints

2. **Start with Brute Force**:
   > "I could try all buy-sell pairs in O(n²), but that's slow..."

3. **Optimize with Insight**:
   > "Actually, I can track the minimum price as I go. At each step, I calculate 
   > profit if I sell today. This gives O(n) time and O(1) space."

4. **Walk Through Example**:
   - Show how minPrice and maxProfit update
   - Demonstrate the logic clearly

5. **Discuss Edge Cases**:
   - Decreasing prices → return 0
   - Single element → return 0
   - All same → return 0

6. **Mention Trade-offs**:
   > "This is optimal - O(n) time is best possible since we must check all prices, 
   > and O(1) space is ideal."

---

## Related Problems (Practice Next)

1. **Best Time to Buy and Sell Stock II** (Multiple transactions allowed)
2. **Best Time to Buy and Sell Stock III** (At most 2 transactions)
3. **Best Time to Buy and Sell Stock with Cooldown**
4. **Best Time to Buy and Sell Stock with Transaction Fee**

---

## Key Takeaways

1. ✅ **One-Pass Pattern**: Track minimum/maximum as you iterate
2. ✅ **Greedy Approach**: Make local optimal choice at each step
3. ✅ **O(n) time, O(1) space** is optimal for this problem
4. ✅ **Order matters**: Must buy before sell (can't go backwards)
5. ✅ **Default to 0**: No profit is a valid answer

---

## Problem Solving Template

For "track minimum/maximum" problems:
```java
int extremeValue = Integer.MAX_VALUE; // or MIN_VALUE
int result = 0; // or appropriate initial value

for (element : array) {
    // Calculate result using extreme value
    result = Math.max/min(result, calculation);
    
    // Update extreme value
    extremeValue = Math.max/min(extremeValue, element);
}

return result;
```

---

## Practice Checklist

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (<20 minutes for Easy)
- [ ] Solve without looking at notes
- [ ] Explain solution out loud
- [ ] Try the Kadane's algorithm variation
- [ ] Review in 3 days (Day 6)

---

## Quick Review (Before Interview)

**Q**: What's the pattern?  
**A**: Track minimum price, calculate profit at each step

**Q**: Time/Space complexity?  
**A**: O(n) time, O(1) space

**Q**: Key insight?  
**A**: At each price, best profit = current price - cheapest price before it

**Q**: Edge cases?  
**A**: Decreasing prices (return 0), single price (return 0)

---

**Pattern Learned**: One-Pass Min/Max Tracking ✅  
**Difficulty**: Easy  
**Time to Master**: 15-20 minutes  
**Next Problem**: Product of Array Except Self (Prefix/Suffix pattern)
