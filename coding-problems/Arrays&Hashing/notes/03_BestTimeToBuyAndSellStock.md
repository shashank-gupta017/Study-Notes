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
Explanation: 
Buy on day 2 (price = 1) and sell on day 5 (price = 6)
Profit = 6 - 1 = 5
Note: Buying on day 2 and selling on day 1 is not allowed 
because you must buy before you sell.
```

### Example 2:
```
Input: prices = [7,6,4,3,1]
Output: 0
Explanation: 
In this case, no transactions are done and the max profit = 0.
The prices are strictly decreasing, so we can never sell for 
a profit.
```

### Example 3:
```
Input: prices = [2,4,1]
Output: 2
Explanation:
Buy on day 1 (price = 2) and sell on day 2 (price = 4)
Profit = 4 - 2 = 2
Don't wait for day 3, even though price is lower, because 
we would need to travel back in time to sell before day 3.
```

### Example 4:
```
Input: prices = [1,2,3,4,5]
Output: 4
Explanation:
Buy on day 1 (price = 1) and sell on day 5 (price = 5)
Profit = 5 - 1 = 4
Maximum profit from continuously increasing prices.
```

### Example 5:
```
Input: prices = [3,3,5,0,0,3,1,4]
Output: 4
Explanation:
Buy on day 4 (price = 0) and sell on day 6 (price = 4)
Profit = 4 - 0 = 4
Multiple valleys and peaks - need to find best pair.
```

---

## Constraints
- `1 <= prices.length <= 10^5`
- `0 <= prices[i] <= 10^4`

---

## Pattern Recognition

This is a **Single Pass Tracking** problem because:
1. We need to find **two values** (buy and sell prices) with maximum difference
2. The buy day must come **before** the sell day (temporal constraint)
3. We can solve it in **one pass** by tracking minimum seen so far
4. Brute force O(n²) can be optimized to **O(n)**

**Key Insight**: At each day, we only care about:
- The **minimum price seen so far** (best buy opportunity)
- The **maximum profit** we can get selling today at current price

**Why This Pattern Works**:
```
For any selling day i:
  Best profit = prices[i] - (minimum price in prices[0...i-1])
  
We don't need to know WHEN the minimum occurred, just its value!
```

**Visual Intuition**:
```
Prices: [7, 1, 5, 3, 6, 4]

Think of it as finding the maximum vertical distance:
  7 ┤●
  6 ┤         ●
  5 ┤    ●
  4 ┤              ●
  3 ┤       ●
  2 ┤
  1 ┤ ●                      ← Min point
  0 ┤──────────────────────
    Days: 1  2  3  4  5  6

Best: Buy at day 2 (min=1), Sell at day 5 (price=6)
Profit = 6 - 1 = 5
```

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Try all possible buy-sell pairs where buy comes before sell.

```java
class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        
        // Try every buy day
        for (int buy = 0; buy < prices.length - 1; buy++) {
            // Try every sell day after buy day
            for (int sell = buy + 1; sell < prices.length; sell++) {
                int profit = prices[sell] - prices[buy];
                maxProfit = Math.max(maxProfit, profit);
            }
        }
        
        return maxProfit;
    }
}
```

**Time Complexity**: O(n²) - nested loops checking all pairs
**Space Complexity**: O(1) - only use variables
**Problem**: Too slow! Will time out on large inputs (10^5 elements).

---

### Approach 2: Single Pass Tracking (OPTIMAL) ⭐
**Idea**: Track minimum price seen so far, calculate profit at each step.

```java
class Solution {
    public int maxProfit(int[] prices) {
        // Edge case: empty or single day
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        
        int minPrice = Integer.MAX_VALUE;  // Track minimum price seen so far
        int maxProfit = 0;                 // Track maximum profit found
        
        // Single pass through prices
        for (int i = 0; i < prices.length; i++) {
            // Update minimum price (best buy opportunity)
            if (prices[i] < minPrice) {
                minPrice = prices[i];
            }
            
            // Calculate profit if we sell today
            int profit = prices[i] - minPrice;
            
            // Update maximum profit
            if (profit > maxProfit) {
                maxProfit = profit;
            }
        }
        
        return maxProfit;
    }
}
```

**Time Complexity**: O(n) - single pass through array
**Space Complexity**: O(1) - only use two variables
**Why Optimal**: Best possible - must examine each price at least once

---

### Approach 3: Compact Single Pass (Alternative) ⭐
**Idea**: Same logic, more concise with Math.min/max.

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

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(1) - two variables
**Note**: Same as Approach 2, just cleaner syntax

---

### Approach 4: Kadane's Algorithm Adaptation
**Idea**: Transform to maximum subarray problem using price differences.

```java
class Solution {
    public int maxProfit(int[] prices) {
        if (prices.length <= 1) return 0;
        
        // Transform to difference array
        // profit[i] = prices[i+1] - prices[i]
        // Find maximum sum of consecutive elements
        
        int maxCurrent = 0;  // Max profit ending at current position
        int maxGlobal = 0;   // Overall max profit
        
        for (int i = 1; i < prices.length; i++) {
            // Add today's price change to running profit
            maxCurrent = Math.max(0, maxCurrent + prices[i] - prices[i - 1]);
            maxGlobal = Math.max(maxGlobal, maxCurrent);
        }
        
        return maxGlobal;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(1) - constant space
**Why Interesting**: Shows connection to maximum subarray problem!

**How it works**:
```
Original: [7, 1, 5, 3, 6, 4]
Diffs:    [  -6, 4,-2, 3,-2]  (price changes)

Find max sum subarray of diffs:
  [4, -2, 3] = 5 (corresponds to buy at 1, sell at 6)
```

---

## Detailed Walkthrough (Approach 2: Optimal)

### Example: prices = [7, 1, 5, 3, 6, 4]

```
Step-by-step single pass:

Initial state:
  minPrice = ∞ (Integer.MAX_VALUE)
  maxProfit = 0

Step 1: i=0, price=7
  minPrice = min(∞, 7) = 7        (found first price)
  profit = 7 - 7 = 0
  maxProfit = max(0, 0) = 0
  
  [7, 1, 5, 3, 6, 4]
   ↑ buy here? profit = 0

Step 2: i=1, price=1
  minPrice = min(7, 1) = 1        (better buy opportunity!)
  profit = 1 - 1 = 0
  maxProfit = max(0, 0) = 0
  
  [7, 1, 5, 3, 6, 4]
      ↑ better buy here! profit = 0

Step 3: i=2, price=5
  minPrice = min(1, 5) = 1        (still 1)
  profit = 5 - 1 = 4              (sell today at 5)
  maxProfit = max(0, 4) = 4
  
  [7, 1, 5, 3, 6, 4]
      ↑  ↑ buy at 1, sell at 5: profit = 4

Step 4: i=3, price=3
  minPrice = min(1, 3) = 1        (still 1)
  profit = 3 - 1 = 2              (worse than selling at 5)
  maxProfit = max(4, 2) = 4       (keep previous best)
  
  [7, 1, 5, 3, 6, 4]
      ↑     ↑ buy at 1, sell at 3: profit = 2 (not better)

Step 5: i=4, price=6
  minPrice = min(1, 6) = 1        (still 1)
  profit = 6 - 1 = 5              (NEW BEST!)
  maxProfit = max(4, 5) = 5
  
  [7, 1, 5, 3, 6, 4]
      ↑        ↑ buy at 1, sell at 6: profit = 5 ✓

Step 6: i=5, price=4
  minPrice = min(1, 4) = 1        (still 1)
  profit = 4 - 1 = 3              (not better than 5)
  maxProfit = max(5, 3) = 5       (keep best)
  
  [7, 1, 5, 3, 6, 4]
      ↑        ↑ final answer: buy at 1, sell at 6

Final Result: maxProfit = 5
```

---

### Visual Representation with State Tracking

```
Prices:    [7,  1,  5,  3,  6,  4]
Index:      0   1   2   3   4   5

Tracking:
Day 0: min=7, profit=0  [no reference point yet]
Day 1: min=1, profit=0  [new minimum, can't sell same day]
Day 2: min=1, profit=4  [5-1=4, good profit!]
Day 3: min=1, profit=4  [3-1=2, not better]
Day 4: min=1, profit=5  [6-1=5, NEW BEST!]
Day 5: min=1, profit=5  [4-1=3, not better]

Graph visualization:
   7 ┤●
   6 ┤         ●  ← Sell here!
   5 ┤    ●
   4 ┤              ●
   3 ┤       ●
   2 ┤
   1 ┤ ●  ← Buy here!
   0 ┤──────────────────
     Days: 0  1  2  3  4  5

Maximum vertical gap: 6 - 1 = 5
```

---

### Another Example: prices = [7, 6, 4, 3, 1]

```
Strictly decreasing prices - no profit possible:

Step 1: i=0, price=7
  minPrice = 7, profit = 0, maxProfit = 0

Step 2: i=1, price=6
  minPrice = min(7, 6) = 6
  profit = 6 - 6 = 0
  maxProfit = 0

Step 3: i=2, price=4
  minPrice = min(6, 4) = 4
  profit = 4 - 4 = 0
  maxProfit = 0

Step 4: i=3, price=3
  minPrice = min(4, 3) = 3
  profit = 3 - 3 = 0
  maxProfit = 0

Step 5: i=4, price=1
  minPrice = min(3, 1) = 1
  profit = 1 - 1 = 0
  maxProfit = 0

Result: 0 (no profit - prices always decreasing)

Visualization:
  7 ┤●
  6 ┤ ●
  5 ┤
  4 ┤  ●
  3 ┤   ●
  2 ┤
  1 ┤    ●
  0 ┤──────────
    Days: 0 1 2 3 4

No upward movement = No profit!
```

---

## Edge Cases to Consider

```java
// Test Case 1: Single day
Input: prices = [5]
Output: 0
// Can't buy and sell on same day - need at least 2 days

// Test Case 2: Two days - profit possible
Input: prices = [1, 5]
Output: 4
// Buy day 1, sell day 2: 5 - 1 = 4

// Test Case 3: Two days - no profit
Input: prices = [5, 1]
Output: 0
// Price decreases, can't profit

// Test Case 4: All same prices
Input: prices = [3, 3, 3, 3]
Output: 0
// No price change, no profit

// Test Case 5: Strictly increasing
Input: prices = [1, 2, 3, 4, 5]
Output: 4
// Buy day 1, sell last day: 5 - 1 = 4

// Test Case 6: Strictly decreasing
Input: prices = [5, 4, 3, 2, 1]
Output: 0
// Always decreasing, no profit possible

// Test Case 7: Valley then peak
Input: prices = [5, 1, 6]
Output: 5
// Buy at valley (1), sell at peak (6): 6 - 1 = 5

// Test Case 8: Multiple peaks and valleys
Input: prices = [3, 3, 5, 0, 0, 3, 1, 4]
Output: 4
// Buy at 0, sell at 4: 4 - 0 = 4 (not first valley!)

// Test Case 9: Profit at beginning, then drop
Input: prices = [2, 4, 1, 0]
Output: 2
// Best: buy at 2, sell at 4 = 2
// Don't wait for later prices if they're worse

// Test Case 10: Maximum values
Input: prices = [10000, 0, 10000]
Output: 10000
// Buy at 0, sell at 10000

// Test Case 11: Minimum values
Input: prices = [0, 1]
Output: 1
// Buy at 0, sell at 1

// Test Case 12: Large array with single best pair
Input: prices = [10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 100]
Output: 99
// Mostly decreasing, but huge jump at end: 100 - 1 = 99

// Test Case 13: Best profit in middle
Input: prices = [5, 1, 5, 3, 7, 2]
Output: 6
// Buy at 1, sell at 7: 7 - 1 = 6
// Not at the end!

// Test Case 14: Zero price valid
Input: prices = [0, 10, 5, 8]
Output: 10
// Buy at 0, sell at 10 (first peak after minimum)
```

---

## Common Mistakes to Avoid

### Mistake 1: Not Handling Single Element Array
```java
// ❌ WRONG: Will access out of bounds
public int maxProfit(int[] prices) {
    int minPrice = prices[0];
    int maxProfit = 0;
    
    for (int i = 1; i < prices.length; i++) {
        // What if prices.length == 1? No issue but unnecessary computation
    }
}

// ✅ CORRECT: Handle edge case explicitly
public int maxProfit(int[] prices) {
    if (prices == null || prices.length <= 1) {
        return 0;  // Can't profit with 0 or 1 day
    }
    
    int minPrice = Integer.MAX_VALUE;
    int maxProfit = 0;
    // Rest of logic...
}
```

### Mistake 2: Initializing minPrice to 0
```java
// ❌ WRONG: All prices will be >= 0, so minPrice never updates!
int minPrice = 0;
for (int price : prices) {
    minPrice = Math.min(minPrice, price);  // Always 0!
    // profit calculation will be wrong
}

// ✅ CORRECT: Initialize to first price or MAX_VALUE
int minPrice = Integer.MAX_VALUE;
// OR
int minPrice = prices[0];
```

### Mistake 3: Trying to Track Buy and Sell Days
```java
// ❌ WRONG: Overcomplicating - we don't need actual days!
int buyDay = 0, sellDay = 0;  // Not needed!
// Problem only asks for max profit, not when to trade

// ✅ CORRECT: Only track values, not indices
int minPrice = Integer.MAX_VALUE;
int maxProfit = 0;
// This is all we need!
```

### Mistake 4: Updating Profit Before minPrice
```java
// ❌ WRONG: Order matters!
for (int price : prices) {
    int profit = price - minPrice;  // minPrice might be wrong!
    maxProfit = Math.max(maxProfit, profit);
    minPrice = Math.min(minPrice, price);  // Updated too late
}

// ✅ CORRECT: Update minPrice first
for (int price : prices) {
    minPrice = Math.min(minPrice, price);  // Update first
    int profit = price - minPrice;         // Then calculate
    maxProfit = Math.max(maxProfit, profit);
}
```

### Mistake 5: Allowing Same-Day Buy and Sell
```java
// ❌ WRONG: This actually works for this problem!
// But conceptually, you can't buy and sell same day
int minPrice = prices[0];
for (int i = 0; i < prices.length; i++) {
    // Even if i=0 is min, profit = prices[0] - prices[0] = 0
    // So it's safe, but let's be explicit
}

// ✅ BETTER: Start from day 1 (more explicit)
int minPrice = prices[0];
int maxProfit = 0;
for (int i = 1; i < prices.length; i++) {
    maxProfit = Math.max(maxProfit, prices[i] - minPrice);
    minPrice = Math.min(minPrice, prices[i]);
}
```

### Mistake 6: Using Two Pointers (Wrong Pattern!)
```java
// ❌ WRONG: Two pointers doesn't apply here!
int left = 0, right = prices.length - 1;
while (left < right) {
    // This assumes sorted array or specific ordering
    // But prices are chronological, not sorted!
}

// ✅ CORRECT: Single pass tracking
int minPrice = Integer.MAX_VALUE;
for (int price : prices) {
    // Track minimum and calculate profit
}
```

### Mistake 7: Returning Negative Profit
```java
// ❌ WRONG: Forgetting we can choose not to trade
for (int price : prices) {
    minPrice = Math.min(minPrice, price);
    maxProfit = price - minPrice;  // Could be negative!
}
// If prices = [5, 4, 3], maxProfit = -2 (wrong!)

// ✅ CORRECT: Max with 0 to avoid negative profit
for (int price : prices) {
    minPrice = Math.min(minPrice, price);
    maxProfit = Math.max(maxProfit, price - minPrice);
}
// If no profit possible, return 0
```

### Mistake 8: Overthinking with Complex Data Structures
```java
// ❌ WRONG: Unnecessary complexity
class Trade {
    int buyDay, sellDay, profit;
}
List<Trade> allTrades = new ArrayList<>();
// Too complex! Problem only needs max profit value

// ✅ CORRECT: Two simple variables
int minPrice = Integer.MAX_VALUE;
int maxProfit = 0;
// Simple and efficient!
```

---

## Why Single Pass Works

### Key Insight: Greedy Choice is Optimal

**Observation 1**: For any sell day `i`, the best buy day is the day with minimum price before `i`.

```
Prices: [7, 1, 5, 3, 6, 4]
         
To sell at day 4 (price=6):
  Best buy = min(7, 1, 5, 3) = 1 (day 1)
  Profit = 6 - 1 = 5

We don't need to try all buy days, just track the minimum!
```

**Observation 2**: We don't need to know future prices to make greedy choice.

```
At each day:
  1. Update minimum price seen so far
  2. Calculate profit if we sell today
  3. Update maximum profit
  
This works because:
  - We're building up knowledge of best buy price
  - We try selling at every possible day
  - We keep the best result
```

**Why Not Two Pointers?**
```
Two pointers works when:
  - Array is sorted, OR
  - We can freely rearrange elements

Stock prices are:
  - Chronological (time-ordered)
  - Can't be rearranged
  - Must buy before sell (temporal constraint)

Single pass respects time ordering!
```

---

## Complexity Analysis

### Time Complexity: O(n)

**Breakdown**:
- **Single loop**: Iterate through each price exactly once
- **Constant work per iteration**: 
  - Update minPrice: O(1)
  - Calculate profit: O(1)
  - Update maxProfit: O(1)
- **Total**: O(n) where n = number of days

**Why Optimal**: 
- Must examine each price at least once to find maximum profit
- Can't skip any day (might be the best sell day)
- Therefore O(n) is the best possible

### Space Complexity: O(1)

**Space Used**:
- `minPrice`: one integer = O(1)
- `maxProfit`: one integer = O(1)
- `price` loop variable: one integer = O(1)
- **Total**: O(1) constant extra space

**No Additional Structures**:
- No arrays or lists
- No recursion stack
- No hash maps
- Just a few variables!

### Comparison Table

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Brute Force | O(n²) | O(1) | Try all pairs - TLE |
| Single Pass | O(n) | O(1) | ✅ Optimal solution |
| Kadane's | O(n) | O(1) | Same complexity, different view |
| Dynamic Programming | O(n) | O(n) | Overkill for this problem |

### Scalability Analysis

```
Input Size    Brute Force    Single Pass
n = 100       ~10,000 ops    ~100 ops
n = 1,000     ~1,000,000     ~1,000 ops
n = 10,000    ~100,000,000   ~10,000 ops (✓)
n = 100,000   ~10^10 (TLE)   ~100,000 ops (✓)

Constraint: n ≤ 10^5
  Brute Force: 10^10 operations ❌ Too slow
  Single Pass: 10^5 operations ✅ Instant
```

---

## Pattern Connection: Single Pass vs Sliding Window

### This Problem: Fixed Buy + Variable Sell
```java
// Single Pass Pattern
int minBuy = ∞;
for (int sellPrice : prices) {
    minBuy = min(minBuy, sellPrice);
    profit = max(profit, sellPrice - minBuy);
}

Key: We don't need a "window" - just track minimum
```

### Sliding Window Problems: Variable Window Size
```java
// Sliding Window Pattern (different!)
int left = 0;
for (int right = 0; right < n; right++) {
    // Expand window
    while (windowInvalid) {
        // Shrink window
        left++;
    }
}

Key: Dynamic window that expands and shrinks
```

**Difference**:
- **Stock Problem**: Buy is implicit (minimum), sell is current day
- **Sliding Window**: Both left and right pointers explicit and moving

**When to Use Each**:
- **Single Pass**: Finding max/min over all prefixes
- **Sliding Window**: Finding subarray meeting certain criteria

---

## Interview Tips

### What to Say During Interview:

**Step 1: Understand the Problem** (30 seconds)
> "So I need to find the maximum profit from one buy and one sell transaction,
> where I must buy before I sell. If no profit is possible, I return 0.
> The key constraint is the temporal ordering - I can't sell before buying."

**Step 2: Discuss Brute Force** (1 minute)
> "The brute force approach would be to try all pairs - for each buy day,
> try all sell days after it. That's O(n²) with nested loops.
> For the given constraints (up to 10^5 elements), this would be too slow."

**Step 3: Identify Optimization** (2 minutes)
> "The key insight is that for any sell day, I want to buy at the minimum
> price seen before that day. I don't need to know which day - just the value.
> 
> So I can track:
> 1. Minimum price seen so far (best buy opportunity)
> 2. Maximum profit by selling at current price
> 
> This reduces the problem to a single pass: O(n) time, O(1) space."

**Step 4: Walk Through Example** (2 minutes)
```java
// prices = [7, 1, 5, 3, 6, 4]
// 
// Day 0: price=7, min=7, profit=0
// Day 1: price=1, min=1, profit=0 (new minimum!)
// Day 2: price=5, min=1, profit=4 (5-1=4)
// Day 3: price=3, min=1, profit=4 (3-1=2, not better)
// Day 4: price=6, min=1, profit=5 (6-1=5, new best!)
// Day 5: price=4, min=1, profit=5 (4-1=3, not better)
//
// Result: 5
```

**Step 5: Code** (5-7 minutes)
- Initialize minPrice to MAX_VALUE, maxProfit to 0
- Single loop through prices
- Update minimum, calculate profit, update maximum
- Return maxProfit

**Step 6: Test Edge Cases** (2 minutes)
- Single day: return 0
- Decreasing prices: return 0
- Increasing prices: last - first
- All same: return 0

### Expected Follow-up Questions:

**Q**: "What if you could make multiple transactions?"
**A**: "That's Stock II - different problem! With unlimited transactions, I'd buy before every price increase and sell before every decrease. Sum all positive differences."

**Q**: "What if you could make at most k transactions?"
**A**: "That's Stock III (k=2) and Stock IV (general k). Requires dynamic programming with states for each transaction. Time complexity becomes O(n*k)."

**Q**: "How would you find the actual buy and sell days, not just profit?"
**A**: "Track two additional variables: buyDay and sellDay. Update them when we update minPrice and maxProfit respectively."

**Q**: "What if prices could be negative?"
**A**: "Interesting! Negative prices don't make real-world sense for stocks, but algorithmically, the solution still works. We'd still track minimum and calculate profit correctly."

**Q**: "Could you use dynamic programming?"
**A**: "Yes, but it's overkill. DP would track max profit up to each day, but since we only need two states (min so far, max profit), simple variables are cleaner and more efficient."

**Q**: "What about Kadane's algorithm connection?"
**A**: "Great observation! We can transform this to a maximum subarray problem. Convert prices to daily changes, then find maximum sum subarray. Same O(n) time."

**Q**: "Is there a way to do better than O(n)?"
**A**: "No, O(n) is optimal. We must examine each price at least once - can't determine max profit without seeing all prices. This is information-theoretic lower bound."

---

## Complete Solution with Extensive Comments

```java
/**
 * Best Time to Buy and Sell Stock
 * 
 * Problem: Given an array of stock prices, find the maximum profit from
 *          one buy transaction followed by one sell transaction.
 * 
 * Approach: Single pass tracking
 *   - Track minimum price seen so far (best buy opportunity)
 *   - For each price, calculate profit if we sell today
 *   - Keep track of maximum profit found
 * 
 * Time Complexity: O(n) - single pass through prices
 * Space Complexity: O(1) - only use two variables
 * 
 * @author Your Name
 * @date 2024
 */
class Solution {
    /**
     * Calculates maximum profit from single buy-sell transaction.
     * 
     * @param prices Array of stock prices where prices[i] is price on day i
     * @return Maximum profit achievable, or 0 if no profit possible
     * 
     * Key Insights:
     *   1. For any sell day, best buy day is the day with minimum price before it
     *   2. We don't need to know when minimum occurred, just its value
     *   3. Try selling at every day, keep track of best result
     *   4. If no profit possible (strictly decreasing), return 0
     * 
     * Examples:
     *   prices = [7,1,5,3,6,4] → 5 (buy at 1, sell at 6)
     *   prices = [7,6,4,3,1]   → 0 (no profit possible)
     */
    public int maxProfit(int[] prices) {
        // Edge case: null or single day
        // Need at least 2 days to buy and sell
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        
        // Track the minimum price seen so far
        // This represents the best buying opportunity up to current day
        // Initialize to MAX_VALUE so first price will update it
        int minPrice = Integer.MAX_VALUE;
        
        // Track the maximum profit found so far
        // This is our answer - the best profit from any buy-sell pair
        // Initialize to 0 (if no profit possible, we don't trade)
        int maxProfit = 0;
        
        // Single pass through all prices
        // For each day, we consider:
        //   1. Is this the best day to buy? (new minimum)
        //   2. If we sell today, what's the profit?
        for (int i = 0; i < prices.length; i++) {
            int currentPrice = prices[i];
            
            // Update minimum price seen so far
            // If today's price is lower than any previous day,
            // this becomes our new best buying opportunity
            if (currentPrice < minPrice) {
                minPrice = currentPrice;
            }
            
            // Calculate profit if we sell today at current price
            // We bought at minPrice (best price seen before today)
            // We sell at currentPrice (today's price)
            int profitToday = currentPrice - minPrice;
            
            // Update maximum profit if today's profit is better
            // This ensures we always keep the best result
            // Note: profitToday could be 0 (if currentPrice == minPrice)
            //       but will never be negative (since minPrice ≤ currentPrice)
            if (profitToday > maxProfit) {
                maxProfit = profitToday;
            }
        }
        
        // Return the maximum profit found
        // If prices always decreased, maxProfit stays 0 (correct!)
        // If we found profitable pairs, maxProfit has the best one
        return maxProfit;
    }
    
    /**
     * Alternative implementation using Math.min and Math.max
     * Same logic, more concise code
     */
    public int maxProfitConcise(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        
        for (int price : prices) {
            // Update minimum price (best buy opportunity)
            minPrice = Math.min(minPrice, price);
            
            // Calculate and update maximum profit
            // profit = current price - best buy price
            maxProfit = Math.max(maxProfit, price - minPrice);
        }
        
        return maxProfit;
    }
    
    /**
     * Kadane's Algorithm adaptation
     * Transform to maximum subarray problem
     */
    public int maxProfitKadane(int[] prices) {
        if (prices.length <= 1) return 0;
        
        // Instead of tracking prices, track daily price changes
        // Maximum profit = maximum sum of consecutive price increases
        int maxCurrent = 0;  // Max profit ending at current day
        int maxGlobal = 0;   // Overall maximum profit
        
        for (int i = 1; i < prices.length; i++) {
            // Daily price change
            int change = prices[i] - prices[i - 1];
            
            // Either extend previous profit or start fresh
            maxCurrent = Math.max(0, maxCurrent + change);
            
            // Update global maximum
            maxGlobal = Math.max(maxGlobal, maxCurrent);
        }
        
        return maxGlobal;
    }
}
```

---

## Testing Strategy

### Comprehensive Test Suite:

```java
public class TestBestTimeToBuyAndSellStock {
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        int passed = 0, failed = 0;
        
        // Test Case 1: Standard case with profit
        int[] test1 = {7, 1, 5, 3, 6, 4};
        int result1 = solution.maxProfit(test1);
        if (result1 == 5) {
            System.out.println("✓ Test 1 passed: " + result1);
            passed++;
        } else {
            System.out.println("✗ Test 1 failed: expected 5, got " + result1);
            failed++;
        }
        
        // Test Case 2: Decreasing prices - no profit
        int[] test2 = {7, 6, 4, 3, 1};
        int result2 = solution.maxProfit(test2);
        if (result2 == 0) {
            System.out.println("✓ Test 2 passed: " + result2);
            passed++;
        } else {
            System.out.println("✗ Test 2 failed: expected 0, got " + result2);
            failed++;
        }
        
        // Test Case 3: Single element
        int[] test3 = {5};
        int result3 = solution.maxProfit(test3);
        if (result3 == 0) {
            System.out.println("✓ Test 3 passed: " + result3);
            passed++;
        } else {
            System.out.println("✗ Test 3 failed: expected 0, got " + result3);
            failed++;
        }
        
        // Test Case 4: Two elements - profit
        int[] test4 = {1, 5};
        int result4 = solution.maxProfit(test4);
        if (result4 == 4) {
            System.out.println("✓ Test 4 passed: " + result4);
            passed++;
        } else {
            System.out.println("✗ Test 4 failed: expected 4, got " + result4);
            failed++;
        }
        
        // Test Case 5: Two elements - no profit
        int[] test5 = {5, 1};
        int result5 = solution.maxProfit(test5);
        if (result5 == 0) {
            System.out.println("✓ Test 5 passed: " + result5);
            passed++;
        } else {
            System.out.println("✗ Test 5 failed: expected 0, got " + result5);
            failed++;
        }
        
        // Test Case 6: All same prices
        int[] test6 = {3, 3, 3, 3};
        int result6 = solution.maxProfit(test6);
        if (result6 == 0) {
            System.out.println("✓ Test 6 passed: " + result6);
            passed++;
        } else {
            System.out.println("✗ Test 6 failed: expected 0, got " + result6);
            failed++;
        }
        
        // Test Case 7: Strictly increasing
        int[] test7 = {1, 2, 3, 4, 5};
        int result7 = solution.maxProfit(test7);
        if (result7 == 4) {
            System.out.println("✓ Test 7 passed: " + result7);
            passed++;
        } else {
            System.out.println("✗ Test 7 failed: expected 4, got " + result7);
            failed++;
        }
        
        // Test Case 8: Multiple peaks and valleys
        int[] test8 = {3, 3, 5, 0, 0, 3, 1, 4};
        int result8 = solution.maxProfit(test8);
        if (result8 == 4) {
            System.out.println("✓ Test 8 passed: " + result8);
            passed++;
        } else {
            System.out.println("✗ Test 8 failed: expected 4, got " + result8);
            failed++;
        }
        
        // Test Case 9: Valley then peak
        int[] test9 = {5, 1, 6};
        int result9 = solution.maxProfit(test9);
        if (result9 == 5) {
            System.out.println("✓ Test 9 passed: " + result9);
            passed++;
        } else {
            System.out.println("✗ Test 9 failed: expected 5, got " + result9);
            failed++;
        }
        
        // Test Case 10: Large values
        int[] test10 = {10000, 0, 10000};
        int result10 = solution.maxProfit(test10);
        if (result10 == 10000) {
            System.out.println("✓ Test 10 passed: " + result10);
            passed++;
        } else {
            System.out.println("✗ Test 10 failed: expected 10000, got " + result10);
            failed++;
        }
        
        // Test Case 11: Zero price
        int[] test11 = {0, 10, 5};
        int result11 = solution.maxProfit(test11);
        if (result11 == 10) {
            System.out.println("✓ Test 11 passed: " + result11);
            passed++;
        } else {
            System.out.println("✗ Test 11 failed: expected 10, got " + result11);
            failed++;
        }
        
        // Test Case 12: Best profit in middle
        int[] test12 = {5, 1, 5, 3, 7, 2};
        int result12 = solution.maxProfit(test12);
        if (result12 == 6) {
            System.out.println("✓ Test 12 passed: " + result12);
            passed++;
        } else {
            System.out.println("✗ Test 12 failed: expected 6, got " + result12);
            failed++;
        }
        
        System.out.println("\n" + passed + " passed, " + failed + " failed");
    }
}
```

---

## Key Takeaways

1. ✅ **Single pass is sufficient** - don't need to check all pairs
2. ✅ **Track minimum, not buy day** - only the value matters, not when
3. ✅ **Greedy choice works** - best buy for any sell day is global minimum before it
4. ✅ **Initialize minPrice to MAX_VALUE** - ensures first price updates it
5. ✅ **maxProfit starts at 0** - represents "don't trade" option
6. ✅ **Order matters** - update minPrice before calculating profit
7. ✅ **Temporal constraint is key** - must buy before sell (can't rearrange)
8. ✅ **O(n) time, O(1) space is optimal** - can't do better
9. ✅ **No negative profit** - always compare with 0 (can choose not to trade)
10. ✅ **Pattern extends to Stock II, III, IV** - foundation for series

---

## Variations & Extensions

After mastering this problem, these follow naturally:

1. **Best Time to Buy and Sell Stock II** (Medium)
   - Unlimited transactions
   - Sum all positive price differences
   - Greedy approach: buy before every increase

2. **Best Time to Buy and Sell Stock III** (Hard)
   - At most 2 transactions
   - Dynamic programming with 4 states
   - Track buy1, sell1, buy2, sell2

3. **Best Time to Buy and Sell Stock IV** (Hard)
   - At most k transactions
   - Generalize Stock III to k transactions
   - DP with O(n*k) time

4. **Best Time to Buy and Sell Stock with Cooldown** (Medium)
   - After selling, must wait 1 day before buying again
   - State machine DP

5. **Best Time to Buy and Sell Stock with Transaction Fee** (Medium)
   - Pay fee on each transaction
   - Modify profit calculation

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (aim for 10-15 minutes for Easy)
- [ ] Trace through [7,1,5,3,6,4] step by step on paper
- [ ] Understand why single pass works (no need for nested loops)
- [ ] Test with edge cases: single element, decreasing, increasing
- [ ] Explain solution out loud without looking at code
- [ ] Draw the price graph and identify buy/sell points visually
- [ ] Compare with Stock II (unlimited transactions)
- [ ] Review in 3 days (spaced repetition)
- [ ] Ready for Contains Duplicate!

---

**Pattern Mastered**: Single Pass Tracking with Min/Max ✅  
**Difficulty**: Easy  
**Time to Master**: 10-15 minutes  
**Arrays & Hashing Foundation**: Critical problem! 🚀

This is a fundamental problem that teaches single-pass optimization. Master this pattern - it appears in many problems! The key is recognizing that we don't need all historical data, just strategic tracking of min/max values.
