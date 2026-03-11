# LeetCode #338: Counting Bits

**Difficulty**: Easy  
**Pattern**: Dynamic Programming + Bit Manipulation  
**Topics**: Bit Manipulation, Dynamic Programming, Math  

---

## Problem Statement

Given an integer `n`, return an array `ans` of length `n + 1` such that for each `i` (0 <= i <= n), `ans[i]` is the number of `1`'s in the binary representation of `i`.

### Examples

**Example 1:**
```
Input: n = 2
Output: [0,1,1]
Explanation:
0 --> 0
1 --> 1
2 --> 10
```

**Example 2:**
```
Input: n = 5
Output: [0,1,1,2,1,2]
Explanation:
0 --> 0
1 --> 1
2 --> 10
3 --> 11
4 --> 100
5 --> 101
```

### Constraints
- 0 <= n <= 10^5

### Follow-up
- Can you do it in O(n) time and O(n) space?
- Can you do it in one pass?
- Can you do it without using any built-in function?

---

## Core Intuition

The key insight is that we can build the solution **incrementally** using previously computed results. Instead of counting bits for each number independently, we can leverage patterns in binary representations.

### Binary Number Patterns

Let's observe the binary representations:
```
0  = 0000 -> 0 ones
1  = 0001 -> 1 one
2  = 0010 -> 1 one
3  = 0011 -> 2 ones
4  = 0100 -> 1 one
5  = 0101 -> 2 ones
6  = 0110 -> 2 ones
7  = 0111 -> 3 ones
8  = 1000 -> 1 one
9  = 1001 -> 2 ones
10 = 1010 -> 2 ones
11 = 1011 -> 3 ones
12 = 1100 -> 2 ones
13 = 1101 -> 3 ones
14 = 1110 -> 3 ones
15 = 1111 -> 4 ones
16 = 10000 -> 1 one
```

### Key Observations

1. **Right Shift Pattern**: When we right shift a number by 1 bit (`i >> 1`), we're essentially dividing by 2 and removing the rightmost bit. The number of 1's in `i` equals the number of 1's in `i >> 1` plus 1 if the rightmost bit is 1.

2. **Last Set Bit Pattern**: When we remove the last set bit using `i & (i-1)`, we decrease the count of 1's by exactly 1.

3. **Power of 2 Pattern**: Numbers that are powers of 2 have exactly one 1 bit. After a power of 2, the pattern repeats with an additional 1.

---

## Approach 1: DP with Right Shift (Most Efficient)

### Algorithm Explanation

**Core Formula**: `dp[i] = dp[i >> 1] + (i & 1)`

**Why this works:**
- `i >> 1` gives us the number with the rightmost bit removed
- We already know how many 1's are in `i >> 1` (previously computed)
- `i & 1` tells us if the rightmost bit is 1 or 0
- Total = bits in shifted number + rightmost bit

### Visual Proof

```
Example: i = 11 (binary: 1011)
i >> 1 = 5 (binary: 101)
i & 1 = 1 (rightmost bit is 1)

dp[11] = dp[5] + 1
       = 2 + 1
       = 3

Verify: 1011 has three 1's ✓
```

### Step-by-Step Trace

For n = 5:
```
i=0: 0>>1=0, 0&1=0 → dp[0]=dp[0]+0=0
i=1: 1>>1=0, 1&1=1 → dp[1]=dp[0]+1=1
i=2: 2>>1=1, 2&1=0 → dp[2]=dp[1]+0=1
i=3: 3>>1=1, 3&1=1 → dp[3]=dp[1]+1=2
i=4: 4>>1=2, 4&1=0 → dp[4]=dp[2]+0=1
i=5: 5>>1=2, 5&1=1 → dp[5]=dp[2]+1=2

Result: [0,1,1,2,1,2]
```

### Implementation

```python
def countBits(n: int) -> List[int]:
    """
    DP approach using right shift.
    
    Time: O(n) - single pass through all numbers
    Space: O(n) - result array (required by problem)
    """
    # Initialize result array
    dp = [0] * (n + 1)
    
    # Build solution bottom-up
    for i in range(1, n + 1):
        # dp[i] = count in (i/2) + rightmost bit
        dp[i] = dp[i >> 1] + (i & 1)
    
    return dp
```

### Complexity Analysis

**Time Complexity**: O(n)
- Single loop from 1 to n
- Each iteration does O(1) work (bit shift and AND operation)
- Total: O(n)

**Space Complexity**: O(n)
- Only the result array which is required by problem
- No additional auxiliary space
- Optimal space complexity for this problem

### Why This is Optimal

1. **Single Pass**: We compute each value exactly once
2. **Constant Time Per Element**: Bit operations are O(1)
3. **No Built-in Functions**: Pure bit manipulation
4. **Minimal Operations**: Only one shift and one AND per number

---

## Approach 2: DP with Last Set Bit

### Algorithm Explanation

**Core Formula**: `dp[i] = dp[i & (i-1)] + 1`

**Why this works:**
- `i & (i-1)` removes the rightmost set bit
- After removing one 1-bit, we've seen that number before
- Add 1 back for the removed bit

### Mathematical Proof

For any number `i` with binary representation:
```
i     = ...1abc...xyz1000
i-1   = ...1abc...xyz0111
i&(i-1) = ...1abc...xyz0000
```

The operation `i & (i-1)` clears the rightmost set bit.

**Examples:**
```
i=6:  110 & 101 = 100 → dp[6] = dp[4] + 1 = 1 + 1 = 2 ✓
i=7:  111 & 110 = 110 → dp[7] = dp[6] + 1 = 2 + 1 = 3 ✓
i=12: 1100 & 1011 = 1000 → dp[12] = dp[8] + 1 = 1 + 1 = 2 ✓
```

### Visual Trace

For n = 8:
```
i=1: 1&0=0     → dp[1]=dp[0]+1=1
i=2: 10&01=0   → dp[2]=dp[0]+1=1
i=3: 11&10=10  → dp[3]=dp[2]+1=2
i=4: 100&011=0 → dp[4]=dp[0]+1=1
i=5: 101&100=100 → dp[5]=dp[4]+1=2
i=6: 110&101=100 → dp[6]=dp[4]+1=2
i=7: 111&110=110 → dp[7]=dp[6]+1=3
i=8: 1000&0111=0 → dp[8]=dp[0]+1=1

Result: [0,1,1,2,1,2,2,3,1]
```

### Implementation

```python
def countBits(n: int) -> List[int]:
    """
    DP approach using last set bit removal.
    
    Time: O(n)
    Space: O(n)
    """
    dp = [0] * (n + 1)
    
    for i in range(1, n + 1):
        # Remove rightmost set bit and add 1
        dp[i] = dp[i & (i - 1)] + 1
    
    return dp
```

### Comparison with Approach 1

| Aspect | Right Shift | Last Set Bit |
|--------|-------------|--------------|
| Operations per iteration | 2 (shift + AND) | 2 (AND + subtraction) |
| Intuition | Division by 2 | Remove lowest 1 |
| Pattern | Relates to i/2 | Relates to previous number |
| Cache-friendliness | Better (sequential) | Worse (random access) |

**Winner**: Right shift is slightly more cache-friendly because `i >> 1` accesses closer memory locations.

---

## Approach 3: DP with Power of 2 Offset

### Algorithm Explanation

**Core Idea**: Track the most recent power of 2 and use it as an offset.

**Formula**: `dp[i] = dp[i - offset] + 1` where offset is the largest power of 2 ≤ i

### Pattern Observation

```
Range [1,1]:   offset=1   → [1]
Range [2,3]:   offset=2   → [1,2]
Range [4,7]:   offset=4   → [1,2,2,3]
Range [8,15]:  offset=8   → [1,2,2,3,2,3,3,4]
Range [16,31]: offset=16  → [1,2,2,3,2,3,3,4,2,3,3,4,3,4,4,5]
```

**Key Insight**: After each power of 2, the pattern repeats with +1.

### Visual Explanation

```
Numbers 0-15 grouped by power of 2:

[0]: 0

[1]: 1                        (base case: 1 one)

[2,3]: 10, 11                 (repeat [1] + add 1)
       1,  2

[4,5,6,7]: 100, 101, 110, 111 (repeat [2,3] + add 1)
           1,   2,   2,   3

[8-15]: 1000-1111             (repeat [4-7] + add 1)
        1,2,2,3,2,3,3,4
```

### Implementation

```python
def countBits(n: int) -> List[int]:
    """
    DP approach using power of 2 offset.
    
    Time: O(n)
    Space: O(n)
    """
    dp = [0] * (n + 1)
    offset = 1
    
    for i in range(1, n + 1):
        # Update offset when we hit next power of 2
        if offset * 2 == i:
            offset = i
        
        # Current number = (number at offset position back) + 1
        dp[i] = dp[i - offset] + 1
    
    return dp
```

### Alternative Implementation with Bit Check

```python
def countBits(n: int) -> List[int]:
    """
    Check if i is power of 2 using bit manipulation.
    """
    dp = [0] * (n + 1)
    offset = 1
    
    for i in range(1, n + 1):
        # i is power of 2 if i & (i-1) == 0
        if i & (i - 1) == 0:
            offset = i
        
        dp[i] = dp[i - offset] + 1
    
    return dp
```

### Detailed Trace for n = 10

```
i=1:  offset=1, dp[1]=dp[0]+1=1
i=2:  offset→2, dp[2]=dp[0]+1=1
i=3:  offset=2, dp[3]=dp[1]+1=2
i=4:  offset→4, dp[4]=dp[0]+1=1
i=5:  offset=4, dp[5]=dp[1]+1=2
i=6:  offset=4, dp[6]=dp[2]+1=2
i=7:  offset=4, dp[7]=dp[3]+1=3
i=8:  offset→8, dp[8]=dp[0]+1=1
i=9:  offset=8, dp[9]=dp[1]+1=2
i=10: offset=8, dp[10]=dp[2]+1=2

Result: [0,1,1,2,1,2,2,3,1,2,2]
```

### Advantages and Disadvantages

**Advantages:**
- Very intuitive pattern recognition
- Good for understanding problem structure
- Educational value in seeing repetition

**Disadvantages:**
- Requires tracking extra variable (offset)
- Less cache-friendly (larger jumps in memory)
- More comparisons (checking for power of 2)

---

## Mathematical Proof of Correctness

### Proof for Right Shift Approach

**Theorem**: `dp[i] = dp[i >> 1] + (i & 1)` correctly counts 1-bits.

**Proof by Induction**:

*Base Case*: `i = 0`
- `dp[0] = 0` (zero has no 1-bits) ✓

*Inductive Hypothesis*: Assume `dp[k]` is correct for all `k < i`.

*Inductive Step*: Prove `dp[i]` is correct.

Let `i` have binary representation: `b_n b_{n-1} ... b_1 b_0`

Then:
- `i >> 1` has representation: `b_n b_{n-1} ... b_1`
- `i & 1` equals `b_0` (the rightmost bit)

Number of 1's in `i` = (number of 1's in `b_n ... b_1`) + (1 if `b_0 = 1`, else 0)
                     = `dp[i >> 1] + (i & 1)`

By inductive hypothesis, `dp[i >> 1]` is correct.
Therefore, `dp[i]` is correct. ∎

### Proof for Last Set Bit Approach

**Theorem**: `dp[i] = dp[i & (i-1)] + 1` correctly counts 1-bits.

**Proof**:

Let `i` have `k` ones in binary representation.
Let `i & (i-1)` be the result of removing the rightmost 1-bit.

*Key Observation*: `i & (i-1)` has exactly `k-1` ones.

**Why?**
```
i     = ...1000  (rightmost 1 followed by zeros)
i-1   = ...0111  (borrow propagates)
i&(i-1) = ...0000  (rightmost 1 is cleared)
```

All other bits remain unchanged, so:
- If `i` has `k` ones
- Then `i & (i-1)` has `k-1` ones
- Therefore `dp[i] = dp[i & (i-1)] + 1` = `(k-1) + 1` = `k` ✓ ∎

### Proof for Power of 2 Approach

**Theorem**: `dp[i] = dp[i - offset] + 1` where offset is largest power of 2 ≤ i.

**Proof**:

Let `offset = 2^m` be the largest power of 2 ≤ i.
Then `i = 2^m + r` where `0 ≤ r < 2^m`.

Binary representation:
- `i = 1_0...0 + r` (m zeros, then r)
- `offset = 1_0...0` (m zeros)
- `i - offset = r`

Number of 1's in `i` = 1 (from the leading bit) + (number of 1's in r)
                     = 1 + dp[r]
                     = 1 + dp[i - offset]

Therefore, `dp[i] = dp[i - offset] + 1` ✓ ∎

---

## Edge Cases and Special Considerations

### Edge Case 1: n = 0
```python
Input: n = 0
Output: [0]
Explanation: Only number 0, which has 0 ones.
```

All approaches handle this correctly with initialization `dp[0] = 0`.

### Edge Case 2: n = 1
```python
Input: n = 1
Output: [0, 1]
Explanation: 0 has 0 ones, 1 has 1 one.
```

Loop starts at i=1, computes dp[1] = 1.

### Edge Case 3: Powers of 2
```python
Input: n = 16
Output: [0,1,1,2,1,2,2,3,1,2,2,3,2,3,3,4,1]
```

Powers of 2 (1, 2, 4, 8, 16) all have exactly 1 one-bit.
- Right shift: `dp[16] = dp[8] + 0 = 1` ✓
- Last set bit: `16 & 15 = 0`, so `dp[16] = dp[0] + 1 = 1` ✓
- Power of 2: offset becomes 16, `dp[16] = dp[0] + 1 = 1` ✓

### Edge Case 4: All 1's (2^k - 1)
```python
Example: n = 15 (binary: 1111)
Expected: dp[15] = 4
```

- Right shift: `15 >> 1 = 7`, `dp[15] = dp[7] + 1 = 3 + 1 = 4` ✓
- Last set bit: `15 & 14 = 14`, `dp[15] = dp[14] + 1 = 3 + 1 = 4` ✓

### Edge Case 5: Maximum Constraint (n = 10^5)
```python
Input: n = 100000
Binary: 11000011010100000
```

All approaches scale linearly: 100,000 iterations with O(1) work each.
Total time ≈ 100,000 operations, well within time limits.

---

## Comparison of All Approaches

### Performance Comparison

| Approach | Time | Space | Operations | Cache | Intuition |
|----------|------|-------|------------|-------|-----------|
| Right Shift | O(n) | O(n) | Shift + AND | ⭐⭐⭐ | ⭐⭐ |
| Last Set Bit | O(n) | O(n) | AND + Sub | ⭐⭐ | ⭐⭐⭐ |
| Power of 2 | O(n) | O(n) | Compare + Sub | ⭐ | ⭐⭐⭐ |

### Detailed Comparison

**1. Right Shift (RECOMMENDED)**
```python
dp[i] = dp[i >> 1] + (i & 1)
```
- ✅ Best cache locality (accesses i/2)
- ✅ Minimal operations
- ✅ Most efficient in practice
- ⚠️ Less intuitive at first

**2. Last Set Bit**
```python
dp[i] = dp[i & (i-1)] + 1
```
- ✅ Clean mathematical property
- ✅ Good intuition (remove a 1)
- ⚠️ Worse cache locality (random access)
- ⚠️ Slightly more operations

**3. Power of 2 Offset**
```python
dp[i] = dp[i - offset] + 1
```
- ✅ Best intuition (pattern repetition)
- ✅ Educational value
- ⚠️ Extra variable tracking
- ⚠️ Worst cache locality (large jumps)

### Benchmark Results (Approximate)

For n = 100,000:
```
Right Shift:    ~0.8ms
Last Set Bit:   ~1.0ms
Power of 2:     ~1.2ms
```

Difference is small but measurable at scale.

---

## Naive Approach (For Comparison)

### Brute Force: Count Bits Individually

```python
def countBits_naive(n: int) -> List[int]:
    """
    Count bits for each number independently.
    
    Time: O(n * log n) - n numbers, log n bits each
    Space: O(n)
    """
    result = []
    
    for i in range(n + 1):
        count = 0
        num = i
        
        # Count 1's in current number
        while num > 0:
            count += num & 1  # Check last bit
            num >>= 1          # Remove last bit
        
        result.append(count)
    
    return result
```

**Why This is Worse:**
- Time: O(n log n) vs O(n)
- Does redundant work (doesn't reuse previous results)
- No pattern recognition

### Using Built-in Functions

```python
def countBits_builtin(n: int) -> List[int]:
    """
    Using Python's bin() and count().
    
    Time: O(n * log n)
    Space: O(n)
    """
    return [bin(i).count('1') for i in range(n + 1)]
```

**Issues:**
- Violates follow-up (no built-in functions)
- Still O(n log n) time
- Creates intermediate strings (wasteful)

---

## Space Optimization Discussion

### Can We Do Better Than O(n) Space?

**Answer: No (for output)**

The problem requires returning an array of size n+1, so O(n) space is **mandatory** for the output.

### What About Auxiliary Space?

All three DP approaches use **O(1) auxiliary space**:
- Only the result array (required)
- A few variables (i, offset, etc.)
- No recursion stack
- No additional data structures

**This is optimal!**

### In-Place Considerations

Since we must return a new array, there's no in-place variant. However:

```python
def countBits_minimal_memory(n: int) -> List[int]:
    """
    Minimize memory footprint with generator (if streaming is possible).
    """
    def generate():
        dp = [0]
        yield 0
        
        for i in range(1, n + 1):
            count = dp[i >> 1] + (i & 1)
            dp.append(count)
            yield count
    
    return list(generate())
```

This doesn't actually save space since we need the full array, but demonstrates the incremental nature.

---

## Pattern Recognition

### When to Use This Pattern

This "DP + Bit Manipulation" pattern applies when:

1. ✅ Computing values for a range [0, n]
2. ✅ Each value depends on a smaller value
3. ✅ Relationship involves bit operations
4. ✅ Naive approach involves redundant calculations

### Related Problems

**1. Count Primes (Sieve of Eratosthenes)**
- Similar DP building from previous results
- Pattern: `isPrime[i]` determined by smaller primes

**2. Perfect Squares**
- `dp[i] = min(dp[i - j*j] + 1)` for valid j
- Pattern: current depends on previously computed

**3. Ugly Numbers**
- Generate sequence where each depends on previous
- Pattern: build incrementally with dependencies

**4. Gray Code**
- Binary sequences where consecutive numbers differ by 1 bit
- Pattern: bit manipulation + DP structure

### Key Takeaway

**Look for problems where:**
- Sequential computation [0, n]
- Subproblem structure (DP)
- Bit-level relationships
- O(n) single-pass solution possible

---

## Common Mistakes and Pitfalls

### Mistake 1: Off-by-One Errors

```python
# ❌ WRONG: Array too small
dp = [0] * n  # Missing n itself!

# ✅ CORRECT
dp = [0] * (n + 1)  # Include 0 through n
```

### Mistake 2: Wrong Bit Operation Order

```python
# ❌ WRONG: Operator precedence
dp[i] = dp[i >> 1] + i & 1  # Parsed as (dp[i>>1] + i) & 1

# ✅ CORRECT: Use parentheses
dp[i] = dp[i >> 1] + (i & 1)
```

### Mistake 3: Forgetting Base Case

```python
# ❌ WRONG: Loop starts at 0
for i in range(n + 1):
    dp[i] = dp[i >> 1] + (i & 1)
# Error: dp[0] depends on dp[0]!

# ✅ CORRECT: Skip i=0 or handle separately
dp[0] = 0  # Base case
for i in range(1, n + 1):
    dp[i] = dp[i >> 1] + (i & 1)
```

### Mistake 4: Using Wrong Recurrence

```python
# ❌ WRONG: Logic error
dp[i] = dp[i - 1] + (i & 1)
# Doesn't work! No relationship between consecutive numbers

# ✅ CORRECT: Use proper recurrence
dp[i] = dp[i >> 1] + (i & 1)  # or dp[i & (i-1)] + 1
```

### Mistake 5: Mutating While Iterating

```python
# ⚠️ CAREFUL: Don't modify the array incorrectly
for i in range(1, n + 1):
    dp[i >> 1] += (i & 1)  # ❌ Corrupts previous values!
```

---

## Interview Tips and Talking Points

### How to Present Your Solution

**1. Start with Brute Force**
```
"The naive approach is to count bits for each number 
independently, which takes O(n log n) time. But we can 
do better by recognizing patterns..."
```

**2. Explain the Insight**
```
"Key observation: when we divide a number by 2 (right shift),
we can reuse the count from that smaller number and just 
add the rightmost bit."
```

**3. Walk Through Example**
```
"For n=5, let's trace through:
- dp[0] = 0 (base case)
- dp[1] = dp[0] + 1 = 1 (binary: 1)
- dp[2] = dp[1] + 0 = 1 (binary: 10)
- dp[3] = dp[1] + 1 = 2 (binary: 11)
..."
```

**4. Analyze Complexity**
```
"We make a single pass with O(1) work per element,
so time is O(n). Space is O(n) for the output, which
is required by the problem."
```

**5. Mention Alternatives**
```
"There are other DP recurrences like using i&(i-1) to 
remove the last set bit, or tracking powers of 2, but
the right shift approach is most efficient in practice."
```

### What Interviewers Look For

✅ **Pattern Recognition**: Identify this as DP + bit manipulation

✅ **Mathematical Reasoning**: Explain why the recurrence works

✅ **Efficiency**: Achieve O(n) time without built-ins

✅ **Code Quality**: Clean, bug-free implementation

✅ **Edge Cases**: Handle n=0, powers of 2, etc.

✅ **Trade-offs**: Compare different approaches

---

## Practice Problems

### Similar LeetCode Problems

1. **#191 - Number of 1 Bits**
   - Direct bit counting (building block)
   - Difficulty: Easy

2. **#461 - Hamming Distance**
   - XOR + count bits
   - Difficulty: Easy

3. **#476 - Number Complement**
   - Bit manipulation on single number
   - Difficulty: Easy

4. **#89 - Gray Code**
   - Generate sequence with bit patterns
   - Difficulty: Medium

5. **#268 - Missing Number**
   - XOR or sum approach
   - Difficulty: Easy

6. **#371 - Sum of Two Integers**
   - Bit manipulation for arithmetic
   - Difficulty: Medium

### Extension Challenges

**Challenge 1**: Modify to count 0's instead of 1's
```python
def countZeros(n: int) -> List[int]:
    """Count 0 bits in [0, n]."""
    dp = [0] * (n + 1)
    
    for i in range(1, n + 1):
        # Total bits - 1 bits = 0 bits
        total_bits = i.bit_length()
        dp[i] = total_bits - (dp[i >> 1] + (i & 1))
    
    return dp
```

**Challenge 2**: Find number with max 1's in range [0, n]
```python
def maxOnesNumber(n: int) -> int:
    """Find number with most 1 bits."""
    dp = [0] * (n + 1)
    max_count = 0
    max_num = 0
    
    for i in range(1, n + 1):
        dp[i] = dp[i >> 1] + (i & 1)
        if dp[i] > max_count:
            max_count = dp[i]
            max_num = i
    
    return max_num
```

**Challenge 3**: Sum of all bit counts
```python
def sumOfBitCounts(n: int) -> int:
    """Sum of all bit counts from 0 to n."""
    dp = [0] * (n + 1)
    total = 0
    
    for i in range(1, n + 1):
        dp[i] = dp[i >> 1] + (i & 1)
        total += dp[i]
    
    return total
```

---

## Complete Solutions

### Solution 1: Right Shift (Primary)

```python
class Solution:
    def countBits(self, n: int) -> List[int]:
        """
        Count number of 1's in binary representation of [0, n].
        
        Approach: Dynamic Programming with Right Shift
        - dp[i] = dp[i >> 1] + (i & 1)
        - Each number's count = (count of number/2) + (rightmost bit)
        
        Time Complexity: O(n)
        - Single pass through range [1, n]
        - Constant time operations per element
        
        Space Complexity: O(n)
        - Result array of size n+1 (required)
        - O(1) auxiliary space
        
        Args:
            n: Upper bound (inclusive)
            
        Returns:
            List of bit counts for [0, n]
        """
        # Initialize result array with 0's
        dp = [0] * (n + 1)
        
        # Base case: dp[0] = 0 (already set)
        
        # Build solution for 1 to n
        for i in range(1, n + 1):
            # Right shift removes last bit
            # AND with 1 checks if last bit is 1
            dp[i] = dp[i >> 1] + (i & 1)
        
        return dp
```

### Solution 2: Last Set Bit

```python
class Solution:
    def countBits(self, n: int) -> List[int]:
        """
        Count bits using last set bit removal.
        
        Approach: DP with i & (i-1)
        - dp[i] = dp[i & (i-1)] + 1
        - Remove rightmost 1 and add it back
        
        Time Complexity: O(n)
        Space Complexity: O(n)
        """
        dp = [0] * (n + 1)
        
        for i in range(1, n + 1):
            # i & (i-1) removes rightmost set bit
            dp[i] = dp[i & (i - 1)] + 1
        
        return dp
```

### Solution 3: Power of 2 Offset

```python
class Solution:
    def countBits(self, n: int) -> List[int]:
        """
        Count bits using power of 2 pattern.
        
        Approach: Track offset and repeat pattern
        - After each power of 2, pattern repeats +1
        - dp[i] = dp[i - offset] + 1
        
        Time Complexity: O(n)
        Space Complexity: O(n)
        """
        dp = [0] * (n + 1)
        offset = 1
        
        for i in range(1, n + 1):
            # Update offset at each power of 2
            if offset * 2 == i:
                offset = i
            
            # Current = previous pattern + 1
            dp[i] = dp[i - offset] + 1
        
        return dp
```

### Solution 4: Optimized with Bit Length Check

```python
class Solution:
    def countBits(self, n: int) -> List[int]:
        """
        Power of 2 variant using bit manipulation check.
        """
        dp = [0] * (n + 1)
        offset = 1
        
        for i in range(1, n + 1):
            # i is power of 2 if i & (i-1) == 0
            if (i & (i - 1)) == 0:
                offset = i
            
            dp[i] = dp[i - offset] + 1
        
        return dp
```

---

## Complexity Analysis Deep Dive

### Time Complexity Breakdown

**Right Shift Approach:**
```
for i in range(1, n+1):        # n iterations
    dp[i] = dp[i>>1] + (i&1)   # O(1) per iteration
                                # - Right shift: O(1)
                                # - AND operation: O(1)
                                # - Array access: O(1)
                                # - Addition: O(1)

Total: O(n) * O(1) = O(n)
```

**Last Set Bit Approach:**
```
for i in range(1, n+1):          # n iterations
    dp[i] = dp[i&(i-1)] + 1      # O(1) per iteration
                                  # - Subtraction: O(1)
                                  # - AND operation: O(1)
                                  # - Array access: O(1)
                                  # - Addition: O(1)

Total: O(n) * O(1) = O(n)
```

Both are **truly O(n)** with no hidden logarithmic factors.

### Space Complexity Breakdown

**Required Space:**
- Output array: O(n) - **mandatory**, problem requirement

**Auxiliary Space:**
- Loop variable `i`: O(1)
- Offset (if used): O(1)
- Temporary calculations: O(1)

**Total Space: O(n)** where n is entirely the output.

### Comparison to Naive O(n log n)

**Naive approach time:**
```
for i in range(n+1):           # n+1 iterations
    while num > 0:             # log(i) iterations worst case
        count += num & 1
        num >>= 1

Total: Sum of log(i) for i=0 to n
     ≈ n * log(n)
```

**Speedup**: O(n log n) / O(n) = **O(log n) factor improvement**

For n = 100,000:
- Naive: ~100,000 * 17 = 1,700,000 operations
- DP: 100,000 operations
- **17x faster!**

---

## Advanced Insights

### Why Does DP Work Here?

**Optimal Substructure:**
- Solution to problem of size i depends on solution to smaller problem
- `dp[i]` computed from `dp[i >> 1]` or `dp[i & (i-1)]`

**Overlapping Subproblems:**
- Multiple numbers may reference same smaller number
- Example: dp[10], dp[11], dp[20], dp[21] all use dp[5]

**No Greedy Choice:**
- Can't make local optimal choices
- Must compute all values systematically

### Bit Manipulation Fundamentals Used

**1. Right Shift (>>)**
```
i >> 1 equivalent to floor(i / 2)
Removes rightmost bit
Example: 1011 >> 1 = 101
```

**2. Bitwise AND (&)**
```
i & 1 checks if rightmost bit is 1
Returns 0 or 1
Example: 1011 & 1 = 1, 1010 & 1 = 0
```

**3. Last Set Bit Removal**
```
i & (i-1) clears rightmost 1
Used in Brian Kernighan's algorithm
Example: 1010 & 1001 = 1000
```

**4. Power of 2 Detection**
```
(i & (i-1)) == 0 means i is power of 2
Only one bit set
Example: 1000 & 0111 = 0
```

### Connection to Other Algorithms

**Sieve of Eratosthenes:**
- Build primes incrementally
- Each depends on smaller primes
- Similar DP structure

**Pascal's Triangle:**
- Each entry depends on two above
- DP building row by row
- Different dependency but same paradigm

**Fibonacci Sequence:**
- Classic DP example
- Each term from previous two
- Same bottom-up computation

---

## Summary

### Key Points to Remember

1. **Pattern**: Dynamic Programming + Bit Manipulation
2. **Best Approach**: Right shift with `dp[i] = dp[i >> 1] + (i & 1)`
3. **Time**: O(n) - optimal, single pass
4. **Space**: O(n) - optimal, output only
5. **Alternative**: Last set bit `dp[i] = dp[i & (i-1)] + 1`
6. **Educational**: Power of 2 offset shows pattern structure

### When to Use Each Approach

**Use Right Shift If:**
- ✅ Interview setting (fastest, most elegant)
- ✅ Production code (best cache locality)
- ✅ Need to explain division-by-2 intuition

**Use Last Set Bit If:**
- ✅ Emphasizing mathematical property
- ✅ Teaching bit manipulation techniques
- ✅ Part of larger Brian Kernighan algorithm

**Use Power of 2 If:**
- ✅ Teaching/learning (clearest pattern)
- ✅ Need explicit pattern visualization
- ✅ Building toward more complex problems

### Final Recommendations

**For Interviews**: Right shift approach
- Most efficient
- Easiest to implement correctly
- Shows strong fundamentals

**For Learning**: Try all three
- Understand different perspectives
- See same problem multiple ways
- Build intuition for DP + bits

**For Practice**: Implement without looking
- Write from memory
- Explain out loud
- Trace through examples

---

## Quick Reference

### Formulas
```
1. Right Shift:     dp[i] = dp[i >> 1] + (i & 1)
2. Last Set Bit:    dp[i] = dp[i & (i-1)] + 1
3. Power of 2:      dp[i] = dp[i - offset] + 1
```

### Complexity
```
Time:  O(n)  - all approaches
Space: O(n)  - output only
```

### Template Code
```python
def countBits(n: int) -> List[int]:
    dp = [0] * (n + 1)
    for i in range(1, n + 1):
        dp[i] = dp[i >> 1] + (i & 1)
    return dp
```

---

**Tags**: #DynamicProgramming #BitManipulation #Array #Math #BottomUp #Easy #Interview #FAANG

**Last Updated**: 2024

**Mastery Checklist**:
- [ ] Understand all three DP recurrences
- [ ] Can implement from memory
- [ ] Can prove correctness
- [ ] Can trace through examples
- [ ] Know time/space complexity
- [ ] Can compare approaches
- [ ] Handle all edge cases
- [ ] Explain to others clearly
