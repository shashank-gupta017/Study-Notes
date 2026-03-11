# Climbing Stairs - LeetCode #70

**Difficulty**: Easy  
**Pattern**: 1D DP (Fibonacci)  
**Frequency**: Very High (Classic DP Introduction)

---

## Problem Statement

You are climbing a staircase. It takes `n` steps to reach the top.

Each time you can either climb **1 or 2 steps**. In how many distinct ways can you climb to the top?

**Example 1:**
```
Input: n = 2
Output: 2
Explanation: There are two ways to climb to the top.
1. 1 step + 1 step
2. 2 steps
```

**Example 2:**
```
Input: n = 3
Output: 3
Explanation: There are three ways to climb to the top.
1. 1 step + 1 step + 1 step
2. 1 step + 2 steps
3. 2 steps + 1 step
```

**Example 3:**
```
Input: n = 4
Output: 5
Explanation: There are five ways:
1. 1 + 1 + 1 + 1
2. 1 + 1 + 2
3. 1 + 2 + 1
4. 2 + 1 + 1
5. 2 + 2
```

**Constraints:**
- `1 <= n <= 45`

---

## Core Concepts

### Why Dynamic Programming?

**Key Observation:** To reach step n, you must come from either:
- Step n-1 (then take 1 step)
- Step n-2 (then take 2 steps)

**Recurrence Relation:**
```
ways(n) = ways(n-1) + ways(n-2)
```

This is the **Fibonacci sequence**!

### Visual Understanding

**For n = 5:**

```
Step 5:  Can reach from step 3 or step 4
         ↑
       /   \
     /       \
Step 4       Step 3
   ↑           ↑
 /   \       /   \
Step 3 Step 2  Step 2  Step 1
```

**Build up from base:**
```
Step 0: 1 way  (start, do nothing)
Step 1: 1 way  (one 1-step)
Step 2: 2 ways (1+1 or 2)
Step 3: 3 ways (ways(2) + ways(1) = 2 + 1 = 3)
Step 4: 5 ways (ways(3) + ways(2) = 3 + 2 = 5)
Step 5: 8 ways (ways(4) + ways(3) = 5 + 3 = 8)
```

**Pattern:** 1, 1, 2, 3, 5, 8, 13, 21, ... (Fibonacci!)

### Decision Tree (Small Example)

**For n = 4:**

```
                      0
                   /     \
               +1 /       \ +2
                 /         \
                1           2
              /   \       /   \
          +1 /    \ +2 +1/    \ +2
            /      \     /      \
           2        3   3        4 ✓
         /  \      /   /
      +1/   \+2  /   /
       /     \  /   /
      3       4✓   4✓
     /
  +1/
   /
  4 ✓

Leaves (reaching step 4): 5 paths
```

### Why Not Greedy or Backtracking?

**Greedy fails:**
- No local optimal choice leads to global optimum
- Taking 2 steps isn't always better

**Backtracking works but is slow:**
- Would explore all paths (exponential)
- Many overlapping subproblems

**DP is optimal:**
- Overlapping subproblems (ways(3) computed multiple times)
- Optimal substructure (optimal solution contains optimal subsolutions)

---

## Solution Approach

### Approach 1: Recursion (Top-Down, Naive)

**Intuition:** Directly implement recurrence relation.

```python
def climbStairs(n: int) -> int:
    """
    Naive recursion - computes same values repeatedly.
    
    Time: O(2^n) - exponential!
    Space: O(n) - recursion depth
    """
    # Base cases
    if n <= 1:
        return 1
    
    # Recurrence: ways to reach n from n-1 and n-2
    return climbStairs(n - 1) + climbStairs(n - 2)
```

**Problem:** Exponential time complexity!

**Example for n=5:**
```
climbStairs(5)
├─ climbStairs(4)
│  ├─ climbStairs(3)
│  │  ├─ climbStairs(2)
│  │  │  ├─ climbStairs(1) → 1
│  │  │  └─ climbStairs(0) → 1
│  │  └─ climbStairs(1) → 1
│  └─ climbStairs(2)  [RECOMPUTED]
│     ├─ climbStairs(1) → 1
│     └─ climbStairs(0) → 1
└─ climbStairs(3)  [RECOMPUTED]
   ├─ climbStairs(2)  [RECOMPUTED AGAIN]
   └─ climbStairs(1) → 1
```

---

### Approach 2: Memoization (Top-Down DP)

**Intuition:** Cache computed results to avoid recomputation.

```python
def climbStairs(n: int) -> int:
    """
    Top-down DP with memoization.
    
    Time: O(n) - each subproblem computed once
    Space: O(n) - memo array + recursion stack
    """
    memo = {}
    
    def dp(i):
        # Base cases
        if i <= 1:
            return 1
        
        # Check cache
        if i in memo:
            return memo[i]
        
        # Compute and cache
        memo[i] = dp(i - 1) + dp(i - 2)
        return memo[i]
    
    return dp(n)
```

**Optimization:** Each state computed exactly once!

---

### Approach 3: Tabulation (Bottom-Up DP)

**Intuition:** Build solution iteratively from base cases.

```python
def climbStairs(n: int) -> int:
    """
    Bottom-up DP with table.
    
    Time: O(n)
    Space: O(n)
    """
    if n <= 1:
        return 1
    
    # Create DP table
    dp = [0] * (n + 1)
    
    # Base cases
    dp[0] = 1  # One way to stay at ground
    dp[1] = 1  # One way to reach step 1
    
    # Fill table bottom-up
    for i in range(2, n + 1):
        dp[i] = dp[i - 1] + dp[i - 2]
    
    return dp[n]
```

**Example for n=5:**
```
i=0: dp[0] = 1
i=1: dp[1] = 1
i=2: dp[2] = dp[1] + dp[0] = 1 + 1 = 2
i=3: dp[3] = dp[2] + dp[1] = 2 + 1 = 3
i=4: dp[4] = dp[3] + dp[2] = 3 + 2 = 5
i=5: dp[5] = dp[4] + dp[3] = 5 + 3 = 8

Answer: 8
```

---

### Approach 4: Space-Optimized DP

**Intuition:** Only need last two values, not entire array.

```python
def climbStairs(n: int) -> int:
    """
    Space-optimized DP using two variables.
    
    Time: O(n)
    Space: O(1) - constant space!
    """
    if n <= 1:
        return 1
    
    # Only track last two values
    prev2 = 1  # dp[i-2]
    prev1 = 1  # dp[i-1]
    
    for i in range(2, n + 1):
        current = prev1 + prev2
        prev2 = prev1
        prev1 = current
    
    return prev1
```

**This is the optimal solution!**

---

### Approach 5: Fibonacci Formula (Mathematical)

**Intuition:** Use Binet's formula for Fibonacci numbers.

```python
def climbStairs(n: int) -> int:
    """
    Closed-form Fibonacci formula.
    
    Time: O(1) if we ignore pow() complexity
    Space: O(1)
    
    Note: May have precision issues for large n
    """
    import math
    
    sqrt5 = math.sqrt(5)
    phi = (1 + sqrt5) / 2
    psi = (1 - sqrt5) / 2
    
    # Fibonacci formula: F(n+1)
    return int((phi**(n+1) - psi**(n+1)) / sqrt5)
```

**Pros:** O(1) time  
**Cons:** Floating point precision issues, less intuitive

---

### Approach 6: Matrix Exponentiation

**Intuition:** Use matrix power to compute Fibonacci.

```python
def climbStairs(n: int) -> int:
    """
    Matrix exponentiation method.
    
    [F(n+1)]   [1 1]^n   [1]
    [F(n)  ] = [1 0]   * [0]
    
    Time: O(log n) - using fast exponentiation
    Space: O(1)
    """
    def matrix_mult(A, B):
        """Multiply 2x2 matrices."""
        return [
            [A[0][0]*B[0][0] + A[0][1]*B[1][0], A[0][0]*B[0][1] + A[0][1]*B[1][1]],
            [A[1][0]*B[0][0] + A[1][1]*B[1][0], A[1][0]*B[0][1] + A[1][1]*B[1][1]]
        ]
    
    def matrix_power(M, n):
        """Fast matrix exponentiation."""
        if n == 1:
            return M
        
        if n % 2 == 0:
            half = matrix_power(M, n // 2)
            return matrix_mult(half, half)
        else:
            return matrix_mult(M, matrix_power(M, n - 1))
    
    if n <= 1:
        return 1
    
    M = [[1, 1], [1, 0]]
    result = matrix_power(M, n)
    return result[0][0]
```

**Use case:** When n is extremely large and modulo is needed.

---

## Detailed Walkthrough

### Example: n = 5

**Recursion Tree (Naive):**
```
                    climbStairs(5)
                    /             \
            climbStairs(4)    climbStairs(3)
             /         \          /         \
      climbStairs(3) climbStairs(2) climbStairs(2) climbStairs(1)
         /      \       /     \        /     \          |
   climb(2) climb(1) climb(1) climb(0) climb(1) climb(0)  return 1
     /    \     |       |        |       |        |
climb(1) climb(0) ret1  ret1    ret1   ret1     ret1
   |       |
  ret1    ret1
```

**Memoization (Top-Down):**
```
Call dp(5):
  → Call dp(4):
      → Call dp(3):
          → Call dp(2):
              → Call dp(1): return 1
              → Call dp(0): return 1
              → Compute: 1 + 1 = 2, memo[2] = 2
          → Call dp(1): return 1 (cached)
          → Compute: 2 + 1 = 3, memo[3] = 3
      → Call dp(2): return 2 (cached!)
      → Compute: 3 + 2 = 5, memo[4] = 5
  → Call dp(3): return 3 (cached!)
  → Compute: 5 + 3 = 8

Answer: 8
```

**Tabulation (Bottom-Up):**
```
Initialize: dp = [0, 0, 0, 0, 0, 0]
Base cases: dp = [1, 1, 0, 0, 0, 0]

i=2: dp[2] = dp[1] + dp[0] = 1 + 1 = 2
     dp = [1, 1, 2, 0, 0, 0]

i=3: dp[3] = dp[2] + dp[1] = 2 + 1 = 3
     dp = [1, 1, 2, 3, 0, 0]

i=4: dp[4] = dp[3] + dp[2] = 3 + 2 = 5
     dp = [1, 1, 2, 3, 5, 0]

i=5: dp[5] = dp[4] + dp[3] = 5 + 3 = 8
     dp = [1, 1, 2, 3, 5, 8]

Answer: dp[5] = 8
```

**Space-Optimized:**
```
Initialize: prev2 = 1, prev1 = 1

i=2: current = 1 + 1 = 2
     prev2 = 1, prev1 = 2

i=3: current = 2 + 1 = 3
     prev2 = 2, prev1 = 3

i=4: current = 3 + 2 = 5
     prev2 = 3, prev1 = 5

i=5: current = 5 + 3 = 8
     prev2 = 5, prev1 = 8

Answer: 8
```

---

## Complexity Analysis

### Time Complexity

| Approach | Time | Explanation |
|----------|------|-------------|
| Naive Recursion | O(2^n) | Binary tree of calls |
| Memoization | O(n) | Each state computed once |
| Tabulation | O(n) | Single loop through n states |
| Space-Optimized | O(n) | Single loop |
| Matrix Power | O(log n) | Fast exponentiation |
| Formula | O(1) | Direct calculation |

### Space Complexity

| Approach | Space | Explanation |
|----------|-------|-------------|
| Naive Recursion | O(n) | Call stack depth |
| Memoization | O(n) | Memo + call stack |
| Tabulation | O(n) | DP array |
| Space-Optimized | O(1) | Only 2 variables |
| Matrix Power | O(1) | Fixed size matrices |
| Formula | O(1) | No extra space |

### Growth Rate Comparison

```
n=10:  Naive: 1,024 calls    vs    DP: 10 computations
n=20:  Naive: 1,048,576      vs    DP: 20
n=30:  Naive: 1,073,741,824  vs    DP: 30
n=40:  Naive: ~1 trillion    vs    DP: 40
```

---

## Pattern Variations

### Variation 1: Variable Steps (1, 2, or 3)

**Problem:** Can take 1, 2, or 3 steps at a time.

```python
def climbStairs_3steps(n):
    """
    Modified: can take 1, 2, or 3 steps.
    """
    if n <= 0:
        return 0
    if n == 1:
        return 1
    if n == 2:
        return 2
    if n == 3:
        return 4  # 1+1+1, 1+2, 2+1, 3
    
    # Space-optimized
    prev3, prev2, prev1 = 1, 2, 4
    
    for i in range(4, n + 1):
        current = prev1 + prev2 + prev3
        prev3, prev2, prev1 = prev2, prev1, current
    
    return prev1
```

### Variation 2: Cost to Climb

**Problem:** Each step has a cost, minimize total cost.

```python
def minCostClimbingStairs(cost):
    """
    LeetCode 746: Min Cost Climbing Stairs
    """
    n = len(cost)
    
    # dp[i] = minimum cost to reach step i
    prev2 = 0  # Before first step
    prev1 = 0  # At first step
    
    for i in range(2, n + 1):
        # Either come from i-1 or i-2
        current = min(prev1 + cost[i-1], prev2 + cost[i-2])
        prev2, prev1 = prev1, current
    
    return prev1
```

### Variation 3: Count Paths with Obstacles

**Problem:** Some steps are broken (can't step on them).

```python
def climbStairs_obstacles(n, broken):
    """
    broken: set of step numbers that are broken
    """
    if n in broken:
        return 0
    
    dp = [0] * (n + 1)
    dp[0] = 1
    
    for i in range(1, n + 1):
        if i in broken:
            dp[i] = 0  # Can't reach broken step
        else:
            dp[i] = dp[i-1]
            if i >= 2:
                dp[i] += dp[i-2]
    
    return dp[n]
```

### Variation 4: Ways with Exact K Steps

**Problem:** Count ways using exactly k steps.

```python
def climbStairs_k_steps(n, k):
    """
    n: total steps
    k: must use exactly k moves
    
    dp[i][j] = ways to reach step i in exactly j moves
    """
    if k < (n + 1) // 2:  # Impossible (minimum moves)
        return 0
    if k > n:  # Impossible (maximum moves)
        return 0
    
    dp = [[0] * (k + 1) for _ in range(n + 1)]
    dp[0][0] = 1  # Start: 0 steps, 0 moves
    
    for moves in range(1, k + 1):
        for step in range(n + 1):
            # Take 1 step from step-1
            if step >= 1:
                dp[step][moves] += dp[step-1][moves-1]
            # Take 2 steps from step-2
            if step >= 2:
                dp[step][moves] += dp[step-2][moves-1]
    
    return dp[n][k]
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Wrong Base Cases

```python
# ❌ Wrong: Missing base case
def climbStairs(n):
    if n == 1:
        return 1
    return climbStairs(n-1) + climbStairs(n-2)  # Error for n=2!

# ✅ Correct: Handle both base cases
def climbStairs(n):
    if n <= 1:
        return 1
    return climbStairs(n-1) + climbStairs(n-2)
```

### Mistake 2: Off-by-One in Array

```python
# ❌ Wrong: Array size
dp = [0] * n  # Not enough space for dp[n]

# ✅ Correct
dp = [0] * (n + 1)  # Need indices 0 to n
```

### Mistake 3: Not Handling Edge Cases

```python
# ❌ Wrong: Doesn't handle n=0
def climbStairs(n):
    dp = [0] * (n + 1)
    dp[1] = 1  # IndexError if n=0!
    
# ✅ Correct
def climbStairs(n):
    if n <= 1:
        return 1
    dp = [0] * (n + 1)
    dp[0] = dp[1] = 1
```

### Mistake 4: Forgetting to Update Variables

```python
# ❌ Wrong: Doesn't update prev2
prev2 = 1
prev1 = 1
for i in range(2, n + 1):
    current = prev1 + prev2
    prev1 = current  # Forgot prev2!

# ✅ Correct
prev2 = 1
prev1 = 1
for i in range(2, n + 1):
    current = prev1 + prev2
    prev2 = prev1
    prev1 = current
```

---

## Edge Cases & Testing

### Edge Cases

```python
# Minimum input
n = 1  → 1

# Small values
n = 2  → 2
n = 3  → 3

# Typical values
n = 5  → 8
n = 10 → 89

# Maximum constraint
n = 45 → 1,836,311,903
```

### Test Suite

```python
def test_climbing_stairs():
    solution = Solution()
    
    # Test 1: Base cases
    assert solution.climbStairs(1) == 1
    assert solution.climbStairs(2) == 2
    
    # Test 2: Small values
    assert solution.climbStairs(3) == 3
    assert solution.climbStairs(4) == 5
    assert solution.climbStairs(5) == 8
    
    # Test 3: Fibonacci verification
    fib = [1, 1, 2, 3, 5, 8, 13, 21, 34, 55]
    for i, expected in enumerate(fib):
        assert solution.climbStairs(i) == expected
    
    # Test 4: Larger value
    assert solution.climbStairs(10) == 89
    assert solution.climbStairs(20) == 10946
    
    # Test 5: Near maximum
    result = solution.climbStairs(45)
    assert result == 1836311903
    
    print("All tests passed!")

def benchmark_approaches():
    """Compare different approaches."""
    import time
    
    n = 30
    
    # Space-optimized (best)
    start = time.time()
    result1 = climbStairs_optimized(n)
    time1 = time.time() - start
    
    # Tabulation
    start = time.time()
    result2 = climbStairs_tabulation(n)
    time2 = time.time() - start
    
    # Memoization
    start = time.time()
    result3 = climbStairs_memoization(n)
    time3 = time.time() - start
    
    print(f"Space-optimized: {time1:.6f}s")
    print(f"Tabulation: {time2:.6f}s")
    print(f"Memoization: {time3:.6f}s")
    
    assert result1 == result2 == result3
```

---

## Interview Tips

### Questions to Ask

1. **"What's the range of n?"**
   - Determines if O(2^n) is acceptable (it's not!)

2. **"Can I use O(n) space or need O(1)?"**
   - Shows awareness of optimization

3. **"Do you want the most efficient solution?"**
   - Clarify if space optimization matters

4. **"Should I handle n=0?"**
   - Edge case handling

### Explaining Your Solution

**Optimal explanation flow:**

1. "This is a classic DP problem with Fibonacci pattern"

2. "To reach step n, I must come from step n-1 or n-2"

3. "So ways(n) = ways(n-1) + ways(n-2)"

4. "I'll use two variables to track last two values"

5. "Time is O(n), space is O(1)"

### Common Follow-ups

**Q: What if you can take 1, 2, or 3 steps?**
A: Extend to dp[i] = dp[i-1] + dp[i-2] + dp[i-3]

**Q: What if steps have different costs?**
A: Min Cost Climbing Stairs (LeetCode 746)

**Q: Can you do it in O(log n)?**
A: Yes, using matrix exponentiation

**Q: What if n is very large (10^9)?**
A: Use matrix exponentiation with modulo

**Q: Prove this is Fibonacci?**
A: Show base cases match and recurrence is identical

---

## Related Problems

### Same Pattern (1D DP)

1. **LeetCode 746: Min Cost Climbing Stairs**
   - Similar but with costs
   - Minimize instead of count

2. **LeetCode 198: House Robber**
   - Similar recurrence
   - Can't use adjacent

3. **LeetCode 509: Fibonacci Number**
   - Exact same pattern
   - Direct Fibonacci

### Extended Patterns

4. **LeetCode 91: Decode Ways**
   - Similar DP structure
   - More complex conditions

5. **LeetCode 62: Unique Paths**
   - 2D grid version
   - Extends to 2D DP

6. **LeetCode 1137: N-th Tribonacci**
   - Three previous values
   - dp[i] = dp[i-1] + dp[i-2] + dp[i-3]

---

## Advanced Concepts

### Mathematical Proof

**Theorem:** climbStairs(n) = F(n+1) where F is Fibonacci

**Proof by induction:**

Base cases:
- n=1: climbStairs(1) = 1 = F(2) ✓
- n=2: climbStairs(2) = 2 = F(3) ✓

Inductive step:
- Assume true for k < n
- climbStairs(n) = climbStairs(n-1) + climbStairs(n-2)
- = F(n) + F(n-1)  (by hypothesis)
- = F(n+1)  (by Fibonacci definition) ✓

### Generating Function

The generating function for Fibonacci is:
```
F(x) = x / (1 - x - x²)

Coefficient of x^n in F(x) expansion = ways to reach step n
```

### Connection to Other Problems

**This pattern appears in:**
- Tiling problems (domino tiling)
- Lattice paths
- Binary string problems (no consecutive 1s)
- Combinatorial optimization

---

## Summary

### Key Takeaways

1. **Pattern Recognition**: Fibonacci sequence in disguise
2. **Recurrence**: dp[i] = dp[i-1] + dp[i-2]
3. **Base Cases**: dp[0] = dp[1] = 1
4. **Optimization**: Only need last 2 values (O(1) space)
5. **Time**: O(n) is optimal for standard approach

### Algorithm Template (Optimal)

```python
def climbStairs(n: int) -> int:
    """
    Space-optimized DP - best solution.
    
    Time: O(n)
    Space: O(1)
    """
    if n <= 1:
        return 1
    
    prev2, prev1 = 1, 1
    
    for i in range(2, n + 1):
        current = prev1 + prev2
        prev2, prev1 = prev1, current
    
    return prev1
```

### Complexity Summary

**Recommended Solution:**
- Time: O(n)
- Space: O(1)

**Alternative for huge n:**
- Matrix exponentiation: O(log n) time

### Key Insights

- **Overlapping subproblems** → Use DP
- **Optimal substructure** → Fibonacci recurrence
- **Space optimization** → Only track last 2 states
- **Bottom-up** generally faster than top-down

---

**Tags**: #dynamic-programming #fibonacci #1d-dp #easy #math  
**Related**: Min Cost Climbing Stairs, House Robber, Fibonacci Number  
**Companies**: Amazon, Google, Microsoft, Apple, Facebook, Adobe
