# Coin Change - LeetCode #322

**Difficulty**: Medium  
**Pattern**: 1D DP (Unbounded Knapsack)  
**Frequency**: Very High (Classic DP Pattern)

---

## Problem Statement

You are given an integer array `coins` representing coins of different denominations and an integer `amount` representing a total amount of money.

Return **the fewest number of coins** that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return `-1`.

You may assume that you have an **infinite number of each kind of coin**.

**Example 1:**
```
Input: coins = [1,2,5], amount = 11
Output: 3
Explanation: 11 = 5 + 5 + 1
```

**Example 2:**
```
Input: coins = [2], amount = 3
Output: -1
Explanation: Amount 3 cannot be made with only coins of value 2
```

**Example 3:**
```
Input: coins = [1], amount = 0
Output: 0
Explanation: 0 coins needed for amount 0
```

**Constraints:**
- `1 <= coins.length <= 12`
- `1 <= coins[i] <= 2^31 - 1`
- `0 <= amount <= 10^4`

---

## Core Concepts

### The Problem Type

**Unbounded Knapsack:**
- Unlimited supply of each coin (can reuse)
- Minimize number of coins (optimization)
- Must reach exact amount (constraint)

### Key Observation

**To make amount `n`, we need:**
```
Minimum of:
- 1 + minCoins(n - coin1)
- 1 + minCoins(n - coin2)
- ...
- 1 + minCoins(n - coinK)
```

**Recurrence Relation:**
```
dp[amount] = min(dp[amount], 1 + dp[amount - coin])
for each coin in coins
```

### Visual Understanding

**Example: coins = [1,2,5], amount = 11**

```
Amount 11:
├─ Use coin 1: 1 + minCoins(10)
├─ Use coin 2: 1 + minCoins(9)
└─ Use coin 5: 1 + minCoins(6)

Amount 6:
├─ Use coin 1: 1 + minCoins(5)
├─ Use coin 2: 1 + minCoins(4)
└─ Use coin 5: 1 + minCoins(1)

Amount 5:
├─ Use coin 1: 1 + minCoins(4)
├─ Use coin 2: 1 + minCoins(3)
└─ Use coin 5: 1 + minCoins(0) = 1 ✓

Build up: 11 = 5 + 5 + 1 (3 coins)
```

### Decision Tree (Small Example)

**coins = [1,2], amount = 4**

```
                    amount=4
              /              \
          use 1             use 2
        amount=3           amount=2
        /      \           /      \
     use 1   use 2      use 1   use 2
   amount=2  amount=1  amount=1  amount=0 ✓
    /  \       |          |       [2,2]
  1    2       1          1
amt=1 amt=0 amt=0      amt=0
 |     ✓     ✓           ✓
 1   [1,1,1,1] [1,1,2]  [1,2,1]
amt=0
 ✓
[1,1,1,1]

Minimum path: [2,2] with 2 coins
```

### Why Not Greedy?

**Greedy fails:**

Example: coins = [1,3,4], amount = 6
- Greedy: Use largest first → 4 + 1 + 1 = 3 coins
- Optimal: 3 + 3 = 2 coins ✓

**DP is necessary!**

---

## Solution Approach

### Approach 1: Recursion (Naive)

```python
from typing import List

class Solution:
    def coinChange(self, coins: List[int], amount: int) -> int:
        """
        Naive recursion - exponential time.
        
        Time: O(amount^n) where n = len(coins)
        Space: O(amount) - recursion depth
        """
        def dp(remaining):
            # Base cases
            if remaining == 0:
                return 0
            if remaining < 0:
                return float('inf')
            
            # Try each coin
            min_coins = float('inf')
            for coin in coins:
                result = dp(remaining - coin)
                if result != float('inf'):
                    min_coins = min(min_coins, 1 + result)
            
            return min_coins
        
        result = dp(amount)
        return result if result != float('inf') else -1
```

**Problem:** Massive recomputation of subproblems!

---

### Approach 2: Memoization (Top-Down DP)

```python
class Solution:
    def coinChange(self, coins: List[int], amount: int) -> int:
        """
        Top-down DP with memoization.
        
        Time: O(amount × len(coins))
        Space: O(amount) - memo + recursion
        """
        memo = {}
        
        def dp(remaining):
            # Base cases
            if remaining == 0:
                return 0
            if remaining < 0:
                return float('inf')
            
            # Check cache
            if remaining in memo:
                return memo[remaining]
            
            # Try each coin
            min_coins = float('inf')
            for coin in coins:
                result = dp(remaining - coin)
                if result != float('inf'):
                    min_coins = min(min_coins, 1 + result)
            
            memo[remaining] = min_coins
            return min_coins
        
        result = dp(amount)
        return result if result != float('inf') else -1
```

---

### Approach 3: Tabulation (Bottom-Up DP)

```python
class Solution:
    def coinChange(self, coins: List[int], amount: int) -> int:
        """
        Bottom-up DP with table.
        
        Time: O(amount × len(coins))
        Space: O(amount)
        """
        # dp[i] = minimum coins needed for amount i
        dp = [float('inf')] * (amount + 1)
        dp[0] = 0  # Base case: 0 coins for amount 0
        
        # Build up from 1 to amount
        for i in range(1, amount + 1):
            for coin in coins:
                if coin <= i:
                    dp[i] = min(dp[i], 1 + dp[i - coin])
        
        return dp[amount] if dp[amount] != float('inf') else -1
```

**This is the standard optimal solution!**

---

### Approach 4: Optimized Iteration Order

```python
class Solution:
    def coinChange(self, coins: List[int], amount: int) -> int:
        """
        Alternative: iterate coins first, then amounts.
        
        Time: O(amount × len(coins))
        Space: O(amount)
        """
        dp = [float('inf')] * (amount + 1)
        dp[0] = 0
        
        # For each coin
        for coin in coins:
            # Update all amounts that can use this coin
            for i in range(coin, amount + 1):
                dp[i] = min(dp[i], dp[i - coin] + 1)
        
        return dp[amount] if dp[amount] != float('inf') else -1
```

---

### Approach 5: BFS Approach

```python
from collections import deque

class Solution:
    def coinChange(self, coins: List[int], amount: int) -> int:
        """
        BFS approach: find shortest path to amount.
        
        Time: O(amount × len(coins))
        Space: O(amount)
        """
        if amount == 0:
            return 0
        
        queue = deque([0])
        visited = {0}
        coins_used = 0
        
        while queue:
            coins_used += 1
            
            for _ in range(len(queue)):
                current = queue.popleft()
                
                for coin in coins:
                    next_amount = current + coin
                    
                    if next_amount == amount:
                        return coins_used
                    
                    if next_amount < amount and next_amount not in visited:
                        visited.add(next_amount)
                        queue.append(next_amount)
        
        return -1
```

**BFS guarantees finding minimum coins naturally!**

---

## Detailed Walkthrough

### Example: coins = [1,2,5], amount = 11

**Tabulation approach:**

```
Initialize: dp = [0, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞]
                  0  1  2  3  4  5  6  7  8  9 10 11

For amount = 1:
  Try coin 1: dp[1] = min(∞, 1 + dp[0]) = min(∞, 1) = 1
  dp = [0, 1, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞]

For amount = 2:
  Try coin 1: dp[2] = min(∞, 1 + dp[1]) = min(∞, 2) = 2
  Try coin 2: dp[2] = min(2, 1 + dp[0]) = min(2, 1) = 1
  dp = [0, 1, 1, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞]

For amount = 3:
  Try coin 1: dp[3] = min(∞, 1 + dp[2]) = min(∞, 2) = 2
  Try coin 2: dp[3] = min(2, 1 + dp[1]) = min(2, 2) = 2
  dp = [0, 1, 1, 2, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞]

For amount = 4:
  Try coin 1: dp[4] = min(∞, 1 + dp[3]) = 3
  Try coin 2: dp[4] = min(3, 1 + dp[2]) = min(3, 2) = 2
  dp = [0, 1, 1, 2, 2, ∞, ∞, ∞, ∞, ∞, ∞, ∞]

For amount = 5:
  Try coin 1: dp[5] = min(∞, 1 + dp[4]) = 3
  Try coin 2: dp[5] = min(3, 1 + dp[3]) = min(3, 3) = 3
  Try coin 5: dp[5] = min(3, 1 + dp[0]) = min(3, 1) = 1
  dp = [0, 1, 1, 2, 2, 1, ∞, ∞, ∞, ∞, ∞, ∞]

For amount = 6:
  Try coin 1: dp[6] = min(∞, 1 + dp[5]) = 2
  Try coin 2: dp[6] = min(2, 1 + dp[4]) = min(2, 3) = 2
  Try coin 5: dp[6] = min(2, 1 + dp[1]) = min(2, 2) = 2
  dp = [0, 1, 1, 2, 2, 1, 2, ∞, ∞, ∞, ∞, ∞]

...continuing...

For amount = 10:
  Try coin 1: dp[10] = 1 + dp[9] = 1 + 3 = 4
  Try coin 2: dp[10] = min(4, 1 + dp[8]) = min(4, 4) = 4
  Try coin 5: dp[10] = min(4, 1 + dp[5]) = min(4, 2) = 2
  dp = [0, 1, 1, 2, 2, 1, 2, 2, 3, 3, 2, ∞]

For amount = 11:
  Try coin 1: dp[11] = 1 + dp[10] = 1 + 2 = 3
  Try coin 2: dp[11] = min(3, 1 + dp[9]) = min(3, 4) = 3
  Try coin 5: dp[11] = min(3, 1 + dp[6]) = min(3, 3) = 3
  dp = [0, 1, 1, 2, 2, 1, 2, 2, 3, 3, 2, 3]

Answer: dp[11] = 3  (using 5 + 5 + 1)
```

---

## Complexity Analysis

### Time Complexity: O(amount × coins)

**Analysis:**
- Outer loop: `amount` iterations
- Inner loop: `len(coins)` iterations
- Total: O(amount × len(coins))

**Example:** amount=10000, coins=12
- Operations: 10000 × 12 = 120,000

### Space Complexity: O(amount)

**Components:**
1. DP array: O(amount)
2. No recursion overhead in tabulation
3. Memoization adds O(amount) for call stack

**Note:** Cannot optimize to O(1) space here (unlike Fibonacci)

---

## Pattern Variations

### Variation 1: Coin Change 2 (Count Ways)

**Problem:** Count number of combinations (not minimize).

```python
def change(amount, coins):
    """
    LeetCode 518: Coin Change 2
    Count ways to make amount (combinations, not permutations).
    """
    dp = [0] * (amount + 1)
    dp[0] = 1  # One way to make 0
    
    # Iterate coins first to avoid counting permutations
    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]
    
    return dp[amount]
```

**Key difference:** Addition instead of min, coins loop outside

### Variation 2: Perfect Squares

**Problem:** Minimum perfect squares summing to n.

```python
def numSquares(n):
    """
    LeetCode 279: Same as coin change with coins = [1,4,9,16,...]
    """
    dp = [float('inf')] * (n + 1)
    dp[0] = 0
    
    # Generate "coins" (perfect squares)
    squares = [i*i for i in range(1, int(n**0.5) + 1)]
    
    for i in range(1, n + 1):
        for square in squares:
            if square <= i:
                dp[i] = min(dp[i], dp[i - square] + 1)
    
    return dp[n]
```

### Variation 3: Minimum Cost For Tickets

**Problem:** Buy tickets for travel days with different durations.

```python
def mincostTickets(days, costs):
    """
    LeetCode 983: Similar DP pattern
    costs[0] = 1-day pass, costs[1] = 7-day, costs[2] = 30-day
    """
    day_set = set(days)
    dp = [0] * (days[-1] + 1)
    
    for i in range(1, len(dp)):
        if i not in day_set:
            dp[i] = dp[i-1]
        else:
            dp[i] = min(
                dp[i-1] + costs[0],                          # 1-day
                dp[max(0, i-7)] + costs[1],                  # 7-day
                dp[max(0, i-30)] + costs[2]                  # 30-day
            )
    
    return dp[-1]
```

### Variation 4: With Limited Coins

**Problem:** Each coin type has limited quantity.

```python
def coinChange_limited(coins, counts, amount):
    """
    coins[i] with counts[i] quantity each.
    """
    dp = [float('inf')] * (amount + 1)
    dp[0] = 0
    
    for coin, count in zip(coins, counts):
        # Work backwards to avoid reusing same coin
        for i in range(amount, coin - 1, -1):
            # Try using 1, 2, ..., count of this coin
            for k in range(1, count + 1):
                if i >= k * coin and dp[i - k * coin] != float('inf'):
                    dp[i] = min(dp[i], dp[i - k * coin] + k)
    
    return dp[amount] if dp[amount] != float('inf') else -1
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Wrong Base Case

```python
# ❌ Wrong: dp[0] should be 0, not infinity
dp = [float('inf')] * (amount + 1)
# Missing: dp[0] = 0

# ✅ Correct
dp = [float('inf')] * (amount + 1)
dp[0] = 0
```

### Mistake 2: Not Checking if Coin > Amount

```python
# ❌ Wrong: Negative index!
for i in range(1, amount + 1):
    for coin in coins:
        dp[i] = min(dp[i], 1 + dp[i - coin])  # May be negative!

# ✅ Correct
for i in range(1, amount + 1):
    for coin in coins:
        if coin <= i:  # Check first!
            dp[i] = min(dp[i], 1 + dp[i - coin])
```

### Mistake 3: Wrong Return Value

```python
# ❌ Wrong: Returns infinity instead of -1
return dp[amount]

# ✅ Correct
return dp[amount] if dp[amount] != float('inf') else -1
```

### Mistake 4: Using int('inf')

```python
# ❌ Wrong: int doesn't have inf
dp = [int('inf')] * (amount + 1)  # Error!

# ✅ Correct
dp = [float('inf')] * (amount + 1)
# OR use large number
dp = [amount + 1] * (amount + 1)  # amount+1 is impossible
```

### Mistake 5: Loop Order in Combinations vs Permutations

```python
# For PERMUTATIONS (order matters) [1,2] != [2,1]
for i in range(1, amount + 1):
    for coin in coins:
        dp[i] += dp[i - coin]

# For COMBINATIONS (order doesn't matter) [1,2] == [2,1]
for coin in coins:  # Coins outer loop!
    for i in range(coin, amount + 1):
        dp[i] += dp[i - coin]
```

---

## Edge Cases & Testing

### Edge Cases

```python
# Amount is 0
coins = [1,2,5], amount = 0
# Output: 0

# Impossible to make amount
coins = [2], amount = 3
# Output: -1

# Single coin equals amount
coins = [5], amount = 5
# Output: 1

# Amount less than smallest coin
coins = [5,10], amount = 3
# Output: -1

# Large amount
coins = [1], amount = 10000
# Output: 10000

# Multiple coins, greedy fails
coins = [1,3,4], amount = 6
# Output: 2 (3+3, not 4+1+1)

# All same coin
coins = [5,5,5], amount = 15
# Output: 3
```

### Test Suite

```python
def test_coin_change():
    solution = Solution()
    
    # Test 1: Basic examples
    assert solution.coinChange([1,2,5], 11) == 3
    assert solution.coinChange([2], 3) == -1
    assert solution.coinChange([1], 0) == 0
    
    # Test 2: Edge cases
    assert solution.coinChange([1], 1) == 1
    assert solution.coinChange([1], 2) == 2
    
    # Test 3: Greedy fails
    assert solution.coinChange([1,3,4], 6) == 2  # 3+3, not 4+1+1
    assert solution.coinChange([1,5,6,9], 11) == 2  # 5+6, not 9+1+1
    
    # Test 4: Large amount
    assert solution.coinChange([1], 100) == 100
    assert solution.coinChange([1,2,5], 100) == 20  # 20 coins of 5
    
    # Test 5: Multiple solutions same length
    assert solution.coinChange([1,2], 2) == 1  # Either [2] or [1,1]
    
    # Test 6: Maximum constraints
    assert solution.coinChange([1,2,5], 10000) > 0
    
    print("All tests passed!")

def test_optimality():
    """Verify DP gives better result than greedy."""
    solution = Solution()
    
    # Case where greedy fails
    coins = [1, 3, 4]
    amount = 6
    
    # Greedy (largest first): 4 + 1 + 1 = 3 coins
    # Optimal: 3 + 3 = 2 coins
    assert solution.coinChange(coins, amount) == 2
    
    print("Optimality test passed!")
```

---

## Interview Tips

### Questions to Ask

1. **"Can coins array be empty?"**
   - Usually no (constraints say length >= 1)

2. **"Can coin values be zero or negative?"**
   - No (constraints say >= 1)

3. **"Is the coins array sorted?"**
   - Doesn't matter for correctness, might for optimization

4. **"What if amount is 0?"**
   - Return 0 (need 0 coins)

5. **"Should I prefer certain coins over others?"**
   - No, just minimize count

### Explaining Your Solution

**Clear explanation:**

1. "This is an unbounded knapsack problem - I can reuse coins"

2. "I'll use DP where dp[i] represents minimum coins for amount i"

3. "For each amount, I try using each coin and take the minimum"

4. "Recurrence: dp[i] = min(dp[i], 1 + dp[i-coin])"

5. "Time is O(amount × coins), space is O(amount)"

6. "If final value is infinity, return -1 (impossible)"

### Common Follow-ups

**Q: How to count number of ways instead of minimum?**
A: Coin Change 2 - use addition instead of min

**Q: What if each coin has limited quantity?**
A: Bounded knapsack variant - track coin usage

**Q: Can you reconstruct the actual coins used?**
A: Keep track of which coin was used at each amount

**Q: What if amount is very large (10^9)?**
A: Problem becomes harder, might need mathematical insights

**Q: Time complexity if we sort coins first?**
A: Still O(amount × coins), sorting is O(coins log coins) which is negligible

---

## Related Problems

### Same Pattern (Unbounded Knapsack)

1. **LeetCode 518: Coin Change 2**
   - Count combinations (not minimize)
   - Sum instead of min

2. **LeetCode 279: Perfect Squares**
   - Coins are perfect squares
   - Same structure

3. **LeetCode 983: Minimum Cost For Tickets**
   - Different "coin" durations
   - Similar DP pattern

### DP Optimization

4. **LeetCode 377: Combination Sum IV**
   - Count permutations (order matters)
   - Different loop order

5. **LeetCode 1449: Largest Number with Digits that Sum to Target**
   - Maximize value instead of minimize coins
   - Similar structure

6. **LeetCode 1155: Number of Dice Rolls With Target Sum**
   - Limited uses per "coin"
   - Bounded variant

---

## Advanced Techniques

### Technique 1: Reconstruct Solution

```python
def coinChange_with_path(coins, amount):
    """
    Return both minimum coins and which coins to use.
    """
    dp = [float('inf')] * (amount + 1)
    dp[0] = 0
    parent = [-1] * (amount + 1)  # Track which coin was used
    
    for i in range(1, amount + 1):
        for coin in coins:
            if coin <= i and dp[i - coin] + 1 < dp[i]:
                dp[i] = dp[i - coin] + 1
                parent[i] = coin
    
    if dp[amount] == float('inf'):
        return -1, []
    
    # Reconstruct path
    path = []
    curr = amount
    while curr > 0:
        coin = parent[curr]
        path.append(coin)
        curr -= coin
    
    return dp[amount], path
```

### Technique 2: Space Optimization (Doesn't work here!)

```python
# ❌ Cannot optimize space like Fibonacci
# Because we need dp[i - coin] where coin varies
# We need the entire array

# This is why space is O(amount), not O(1)
```

### Technique 3: Early Termination

```python
def coinChange_optimized(coins, amount):
    """
    Sort coins descending, try to prune early.
    """
    if amount == 0:
        return 0
    
    coins.sort(reverse=True)
    min_coins = float('inf')
    
    def dfs(remaining, coin_count):
        nonlocal min_coins
        
        if remaining == 0:
            min_coins = min(min_coins, coin_count)
            return
        
        if coin_count >= min_coins:  # Prune
            return
        
        for coin in coins:
            if coin <= remaining:
                dfs(remaining - coin, coin_count + 1)
    
    dfs(amount, 0)
    return min_coins if min_coins != float('inf') else -1
```

### Technique 4: BFS with Pruning

```python
def coinChange_bfs_optimized(coins, amount):
    """
    BFS with early termination.
    """
    if amount == 0:
        return 0
    
    from collections import deque
    queue = deque([(amount, 0)])  # (remaining, coins_used)
    visited = {amount}
    
    while queue:
        remaining, coins_used = queue.popleft()
        
        for coin in coins:
            next_remaining = remaining - coin
            
            if next_remaining == 0:
                return coins_used + 1
            
            if next_remaining > 0 and next_remaining not in visited:
                visited.add(next_remaining)
                queue.append((next_remaining, coins_used + 1))
    
    return -1
```

---

## Summary

### Key Takeaways

1. **Pattern**: Unbounded knapsack (unlimited coin reuse)
2. **Recurrence**: dp[i] = min(dp[i], 1 + dp[i-coin]) for each coin
3. **Base Case**: dp[0] = 0
4. **Impossible**: Return -1 if dp[amount] == infinity
5. **Complexity**: O(amount × coins) time, O(amount) space

### Algorithm Template

```python
def coinChange(coins: List[int], amount: int) -> int:
    """
    Standard DP solution.
    
    Time: O(amount × len(coins))
    Space: O(amount)
    """
    dp = [float('inf')] * (amount + 1)
    dp[0] = 0
    
    for i in range(1, amount + 1):
        for coin in coins:
            if coin <= i:
                dp[i] = min(dp[i], 1 + dp[i - coin])
    
    return dp[amount] if dp[amount] != float('inf') else -1
```

### Complexity Summary

**Optimal Solution:**
- Time: O(amount × len(coins))
- Space: O(amount)
- Cannot optimize space further

### Key Differences

| Problem | Goal | Reuse? | Formula |
|---------|------|--------|---------|
| Coin Change | Minimize | Yes | min(dp[i], 1 + dp[i-coin]) |
| Coin Change 2 | Count | Yes | dp[i] += dp[i-coin] |
| Combination Sum | Find all | Yes | Backtracking |
| 0/1 Knapsack | Maximize | No | Different pattern |

---

**Tags**: #dynamic-programming #unbounded-knapsack #optimization #medium  
**Related**: Coin Change 2, Perfect Squares, Combination Sum  
**Companies**: Amazon, Google, Microsoft, Facebook, Apple, Bloomberg
