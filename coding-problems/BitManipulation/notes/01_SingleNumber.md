# LeetCode #136: Single Number

## Problem Statement

**Difficulty**: Easy  
**Pattern**: XOR Properties / Bit Manipulation  
**Company Tags**: Amazon, Microsoft, Google, Facebook, Apple

Given a **non-empty** array of integers `nums` where every element appears **twice** except for one, find that single element.

You must implement a solution with a **linear runtime complexity** and use **only constant extra space**.

### Examples

```
Input: nums = [2,2,1]
Output: 1

Input: nums = [4,1,2,1,2]
Output: 4

Input: nums = [1]
Output: 1
```

### Constraints

- `1 <= nums.length <= 3 * 10^4`
- `-3 * 10^4 <= nums[i] <= 3 * 10^4`
- Each element in the array appears twice except for one element which appears only once.

---

## Table of Contents

1. [Intuition & Key Insight](#intuition--key-insight)
2. [Bit Manipulation Fundamentals](#bit-manipulation-fundamentals)
3. [XOR Properties - Deep Dive](#xor-properties---deep-dive)
4. [Solution Approaches](#solution-approaches)
5. [Optimal Solution - XOR](#optimal-solution---xor)
6. [Mathematical Proof of Correctness](#mathematical-proof-of-correctness)
7. [Implementation Details](#implementation-details)
8. [Edge Cases & Testing](#edge-cases--testing)
9. [Complexity Analysis](#complexity-analysis)
10. [Common Mistakes & Pitfalls](#common-mistakes--pitfalls)
11. [Problem Variations](#problem-variations)
12. [Related Problems](#related-problems)
13. [Practice Problems](#practice-problems)

---

## Intuition & Key Insight

### The Problem Breakdown

We need to find the element that appears only once while all others appear twice. The constraints are:
- **Time Complexity**: O(n) - Can only traverse the array once or a constant number of times
- **Space Complexity**: O(1) - Cannot use extra data structures that grow with input

### Why This Problem is Special

This is a **classic bit manipulation problem** that showcases the power of XOR operation. Most developers' first instinct would be to use a HashSet or HashMap, but the space constraint eliminates that option.

### The Key Insight: XOR Magic ✨

The XOR operation has two special properties that make it perfect for this problem:

1. **Self-Cancellation**: `a ^ a = 0`
2. **Identity**: `a ^ 0 = a`

When you XOR all numbers together:
- Duplicate pairs cancel out (become 0)
- The single number XORed with 0 gives itself

**Visual Example**:
```
[4, 1, 2, 1, 2]

Step by step XOR:
4 ^ 1 = 5
5 ^ 2 = 7
7 ^ 1 = 6
6 ^ 2 = 4  ← Back to our single number!

Or thinking about it differently:
4 ^ 1 ^ 2 ^ 1 ^ 2
= 4 ^ (1 ^ 1) ^ (2 ^ 2)  [Commutative & Associative]
= 4 ^ 0 ^ 0
= 4
```

---

## Bit Manipulation Fundamentals

Before diving into XOR, let's understand the basics of bit manipulation.

### Binary Number System

Every integer can be represented as a sequence of bits (0s and 1s).

```
Decimal: 5
Binary:  101  (1×2² + 0×2¹ + 1×2⁰ = 4 + 0 + 1 = 5)

Decimal: 13
Binary:  1101 (1×2³ + 1×2² + 0×2¹ + 1×2⁰ = 8 + 4 + 0 + 1 = 13)
```

### Bitwise Operators

#### 1. AND (&)
Returns 1 only if both bits are 1.

```
  5:  101
& 3:  011
-------
  1:  001
```

**Truth Table**:
```
a | b | a & b
--|---|------
0 | 0 |  0
0 | 1 |  0
1 | 0 |  0
1 | 1 |  1
```

**Use Cases**: Masking, checking if bit is set, clearing bits

#### 2. OR (|)
Returns 1 if at least one bit is 1.

```
  5:  101
| 3:  011
-------
  7:  111
```

**Truth Table**:
```
a | b | a | b
--|---|------
0 | 0 |  0
0 | 1 |  1
1 | 0 |  1
1 | 1 |  1
```

**Use Cases**: Setting bits, combining flags

#### 3. XOR (^)
Returns 1 if bits are different.

```
  5:  101
^ 3:  011
-------
  6:  110
```

**Truth Table**:
```
a | b | a ^ b
--|---|------
0 | 0 |  0
0 | 1 |  1
1 | 0 |  1
1 | 1 |  0
```

**Use Cases**: Toggle bits, find unique elements, swap without temp variable

#### 4. NOT (~)
Inverts all bits (0→1, 1→0).

```
~5: ~0101 = 1010 (in 4-bit representation)
```

**Use Cases**: Creating bit masks, complement operations

#### 5. Left Shift (<<)
Shifts bits to the left, filling with 0s.

```
5 << 1:  101 → 1010 (5 × 2 = 10)
5 << 2:  101 → 10100 (5 × 4 = 20)
```

**Effect**: Multiplies by 2^n where n is shift amount

#### 6. Right Shift (>>)
Shifts bits to the right, discarding rightmost bits.

```
5 >> 1:  101 → 10 (5 / 2 = 2)
5 >> 2:  101 → 1  (5 / 4 = 1)
```

**Effect**: Divides by 2^n where n is shift amount

---

## XOR Properties - Deep Dive

XOR is the **hero** of this problem. Let's explore all its properties in detail.

### Property 1: Commutative
**Order doesn't matter**

```
a ^ b = b ^ a

Example:
5 ^ 3 = 6
3 ^ 5 = 6
```

**Binary Proof**:
```
  5: 101
^ 3: 011
------
  6: 110

  3: 011
^ 5: 101
------
  6: 110
```

### Property 2: Associative
**Grouping doesn't matter**

```
(a ^ b) ^ c = a ^ (b ^ c)

Example:
(5 ^ 3) ^ 2 = 5 ^ (3 ^ 2)
(6) ^ 2 = 5 ^ (1)
4 = 4 ✓
```

**Why This Matters**: We can XOR numbers in ANY order!

### Property 3: Identity Element
**Zero is the identity**

```
a ^ 0 = a
0 ^ a = a
```

**Proof**:
```
Any bit XOR 0 = Same bit
  101 (5)
^ 000 (0)
------
  101 (5)
```

### Property 4: Self-Inverse (Most Important!)
**A number XOR itself is zero**

```
a ^ a = 0
```

**Proof**:
```
Every bit XOR itself = 0
  101 (5)
^ 101 (5)
------
  000 (0)
```

**This is THE key property for our problem!**

### Property 5: Double Negation
**XORing twice with same value gives original**

```
a ^ b ^ b = a
(a ^ b) ^ b = a ^ (b ^ b) = a ^ 0 = a
```

### Combined Properties Example

```
Given: [2, 5, 2, 7, 5]
Find: Single number (7)

Step-by-step:
2 ^ 5 ^ 2 ^ 7 ^ 5

Rearrange (Commutative):
2 ^ 2 ^ 5 ^ 5 ^ 7

Group pairs (Associative):
(2 ^ 2) ^ (5 ^ 5) ^ 7

Self-inverse:
0 ^ 0 ^ 7

Identity:
7 ✓
```

### XOR as "Toggle" Operation

Think of XOR as a toggle switch:
```
Start: 0
^ 1 → 1  (turn on)
^ 1 → 0  (turn off)
^ 1 → 1  (turn on)
```

**Application**: Swapping without temp variable
```python
a = 5  # 101
b = 3  # 011

a = a ^ b  # a = 110 (6)
b = a ^ b  # b = 110 ^ 011 = 101 (5)
a = a ^ b  # a = 110 ^ 101 = 011 (3)

# a and b are swapped!
```

### XOR in Real World

- **Error Detection**: Parity bits in data transmission
- **Cryptography**: Stream ciphers use XOR for encryption
- **Graphics**: XOR drawing mode (draw once to show, again to erase)
- **Networking**: Checksums and error correction codes

---

## Solution Approaches

Let's explore MULTIPLE approaches from brute force to optimal.

### Approach 1: Brute Force (Nested Loop)

**Idea**: For each element, count occurrences. Return the one with count = 1.

```python
def singleNumber(nums):
    for num in nums:
        count = 0
        for n in nums:
            if n == num:
                count += 1
        if count == 1:
            return num
    return -1
```

**Complexity**:
- Time: O(n²) - For each element, scan entire array
- Space: O(1) - No extra space

**Problems**:
- Too slow for large inputs
- Doesn't meet O(n) time requirement

---

### Approach 2: Sorting

**Idea**: Sort array, then check consecutive pairs.

```python
def singleNumber(nums):
    nums.sort()
    
    # Check pairs
    for i in range(0, len(nums) - 1, 2):
        if nums[i] != nums[i + 1]:
            return nums[i]
    
    # If not found, last element is single
    return nums[-1]
```

**Example Walkthrough**:
```
Input: [4, 1, 2, 1, 2]
After sort: [1, 1, 2, 2, 4]

Check pairs:
i=0: nums[0]=1, nums[1]=1 ✓ (match, continue)
i=2: nums[2]=2, nums[3]=2 ✓ (match, continue)
i=4: Out of bounds, return nums[4]=4 ✓
```

**Complexity**:
- Time: O(n log n) - Sorting dominates
- Space: O(1) or O(n) depending on sort algorithm

**Problems**:
- Modifies original array
- Still not O(n) time

---

### Approach 3: Hash Set

**Idea**: Add/remove elements. What remains is the single number.

```python
def singleNumber(nums):
    seen = set()
    
    for num in nums:
        if num in seen:
            seen.remove(num)  # Remove duplicate
        else:
            seen.add(num)      # Add first occurrence
    
    return seen.pop()  # Only element left
```

**Visual Process**:
```
Input: [4, 1, 2, 1, 2]

Step 1: Process 4 → set = {4}
Step 2: Process 1 → set = {4, 1}
Step 3: Process 2 → set = {4, 1, 2}
Step 4: Process 1 → set = {4, 2} (removed 1)
Step 5: Process 2 → set = {4} (removed 2)

Result: 4 ✓
```

**Complexity**:
- Time: O(n) - Single pass
- Space: O(n) - In worst case, store n/2 + 1 elements

**Problems**:
- Uses O(n) extra space
- Doesn't meet space requirement

---

### Approach 4: Hash Map (Frequency Count)

**Idea**: Count frequencies, find element with count = 1.

```python
def singleNumber(nums):
    freq = {}
    
    # Count frequencies
    for num in nums:
        freq[num] = freq.get(num, 0) + 1
    
    # Find single occurrence
    for num, count in freq.items():
        if count == 1:
            return num
```

**Complexity**:
- Time: O(n) - Two passes
- Space: O(n) - Store all unique elements

**Problems**:
- Uses O(n) extra space
- Two passes (though still O(n))

---

### Approach 5: Mathematical Trick

**Idea**: `2 × (sum of unique) - sum of all = single number`

```python
def singleNumber(nums):
    return 2 * sum(set(nums)) - sum(nums)
```

**Mathematical Proof**:
```
Let's say array has:
- Duplicate pairs: a, a, b, b, c, c
- Single element: x

Sum of all = 2a + 2b + 2c + x
Sum of unique = a + b + c + x

2 × (sum of unique) - sum of all
= 2(a + b + c + x) - (2a + 2b + 2c + x)
= 2a + 2b + 2c + 2x - 2a - 2b - 2c - x
= x ✓
```

**Example**:
```
Input: [4, 1, 2, 1, 2]

Sum of all = 4 + 1 + 2 + 1 + 2 = 10
Unique = {4, 1, 2}
Sum of unique = 4 + 1 + 2 = 7

Result = 2 × 7 - 10 = 14 - 10 = 4 ✓
```

**Complexity**:
- Time: O(n) - Creating set and summing
- Space: O(n) - Set of unique elements

**Problems**:
- Uses O(n) extra space for set
- Risk of integer overflow for large numbers

---

### Approach 6: XOR (Optimal!) ⭐

**Idea**: XOR all numbers together. Duplicates cancel out!

```python
def singleNumber(nums):
    result = 0
    for num in nums:
        result ^= num
    return result
```

**Why This Works**:
```
Properties used:
1. a ^ a = 0 (self-inverse)
2. a ^ 0 = a (identity)
3. Commutative & Associative (order doesn't matter)

Process:
- All duplicate pairs XOR to 0
- 0 XOR single number = single number
```

**Complexity**:
- Time: O(n) - Single pass
- Space: O(1) - Only one variable

**Advantages**: ✅
- Meets all requirements
- Simple and elegant
- No risk of overflow
- Works with negative numbers

---

## Optimal Solution - XOR

### Complete Implementation

```python
class Solution:
    def singleNumber(self, nums: List[int]) -> int:
        """
        Find the single number that appears once while all others appear twice.
        
        Approach: XOR all numbers
        - Duplicate pairs cancel out (a ^ a = 0)
        - Single number remains (a ^ 0 = a)
        
        Time Complexity: O(n)
        Space Complexity: O(1)
        """
        result = 0
        for num in nums:
            result ^= num
        return result
```

### Alternative One-Liner

```python
from functools import reduce
import operator

class Solution:
    def singleNumber(self, nums: List[int]) -> int:
        return reduce(operator.xor, nums)
```

Or even simpler (Python 3.8+):
```python
class Solution:
    def singleNumber(self, nums: List[int]) -> int:
        return reduce(lambda x, y: x ^ y, nums, 0)
```

### Java Implementation

```java
class Solution {
    public int singleNumber(int[] nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
}
```

### C++ Implementation

```cpp
class Solution {
public:
    int singleNumber(vector<int>& nums) {
        int result = 0;
        for (int num : nums) {
            result ^= num;
        }
        return result;
    }
};
```

### JavaScript Implementation

```javascript
var singleNumber = function(nums) {
    let result = 0;
    for (let num of nums) {
        result ^= num;
    }
    return result;
};

// Or using reduce
var singleNumber = function(nums) {
    return nums.reduce((acc, num) => acc ^ num, 0);
};
```

---

## Mathematical Proof of Correctness

### Formal Proof

**Given**: Array A with n elements where n-1 elements appear twice and 1 appears once.

**To Prove**: `XOR(A[0], A[1], ..., A[n-1]) = single element`

**Proof**:

1. **Let's denote**:
   - Duplicate elements: d₁, d₂, ..., dₖ (each appears twice)
   - Single element: s (appears once)
   - Array: [d₁, d₁, d₂, d₂, ..., dₖ, dₖ, s]

2. **XOR all elements**:
   ```
   Result = d₁ ^ d₁ ^ d₂ ^ d₂ ^ ... ^ dₖ ^ dₖ ^ s
   ```

3. **Apply Commutative & Associative Properties**:
   ```
   Result = (d₁ ^ d₁) ^ (d₂ ^ d₂) ^ ... ^ (dₖ ^ dₖ) ^ s
   ```

4. **Apply Self-Inverse Property** (a ^ a = 0):
   ```
   Result = 0 ^ 0 ^ ... ^ 0 ^ s
   ```

5. **Apply Identity Property** (0 ^ a = a):
   ```
   Result = s
   ```

**Q.E.D.** ✓

### Bit-Level Proof

Let's prove it works at the bit level:

**Example**: [5, 3, 5]
```
5 in binary: 101
3 in binary: 011

XOR all:
  101 (5)
^ 011 (3)
------
  110

^ 101 (5)
------
  011 (3) ✓
```

**Bit by bit**:
```
Bit position 0 (rightmost):
1 ^ 1 ^ 1 = 1  (odd number of 1s = 1)

Bit position 1:
0 ^ 1 ^ 0 = 1  (odd number of 1s = 1)

Bit position 2:
1 ^ 0 ^ 1 = 0  (even number of 1s = 0)

Result: 011 = 3 ✓
```

**Key Insight**: At each bit position:
- If duplicate pairs have that bit set/unset, they cancel out (even count)
- Only the single number's bit determines the result (makes it odd/even)

### Induction Proof

**Base Case**: n = 1 (single element)
```
XOR(a) = 0 ^ a = a ✓
```

**Inductive Hypothesis**: Assume true for k pairs + 1 single

**Inductive Step**: Prove for k+1 pairs + 1 single
```
XOR(d₁, d₁, ..., dₖ, dₖ, dₖ₊₁, dₖ₊₁, s)
= XOR(d₁, d₁, ..., dₖ, dₖ, s) ^ dₖ₊₁ ^ dₖ₊₁
= s ^ (dₖ₊₁ ^ dₖ₊₁)  [by hypothesis]
= s ^ 0
= s ✓
```

---

## Implementation Details

### Handling Edge Cases in Code

```python
def singleNumber(nums):
    # Edge case: Empty array (though problem states non-empty)
    if not nums:
        return None
    
    # Edge case: Single element
    if len(nums) == 1:
        return nums[0]
    
    # General case
    result = 0
    for num in nums:
        result ^= num
    
    return result
```

### Optimization: Reduce Function Calls

```python
# Slower (function call overhead for each XOR)
result = reduce(operator.xor, nums)

# Faster (direct XOR operation)
result = 0
for num in nums:
    result ^= num
```

### Working with Negative Numbers

XOR works perfectly with negative numbers because:
- Negative numbers are stored in **two's complement**
- XOR operates bit-by-bit, regardless of sign

```python
nums = [-1, -1, -2]
result = 0
for num in nums:
    result ^= num
# result = -2 ✓

Binary representation (8-bit example):
-1: 11111111
-2: 11111110

11111111 ^ 11111111 ^ 11111110 = 11111110 = -2 ✓
```

---

## Edge Cases & Testing

### Test Cases

#### 1. Minimum Input
```python
Input: [1]
Output: 1
Explanation: Only one element
```

#### 2. Two Elements
```python
Input: [2, 2, 1]
Output: 1
```

#### 3. Many Duplicates
```python
Input: [1, 1, 2, 2, 3, 3, 4]
Output: 4
```

#### 4. Single at Beginning
```python
Input: [5, 1, 1, 2, 2]
Output: 5
```

#### 5. Single at End
```python
Input: [1, 1, 2, 2, 5]
Output: 5
```

#### 6. Single in Middle
```python
Input: [1, 1, 5, 2, 2]
Output: 5
```

#### 7. Negative Numbers
```python
Input: [-1, -1, -2]
Output: -2
```

#### 8. Mixed Positive and Negative
```python
Input: [-1, 1, -1, 2, 2]
Output: 1
```

#### 9. Zero Present
```python
Input: [0, 1, 1]
Output: 0
```

#### 10. Large Numbers
```python
Input: [30000, -30000, 30000]
Output: -30000
```

### Comprehensive Test Suite

```python
def test_single_number():
    solution = Solution()
    
    # Test 1: Single element
    assert solution.singleNumber([1]) == 1
    
    # Test 2: Basic case
    assert solution.singleNumber([2, 2, 1]) == 1
    
    # Test 3: Longer array
    assert solution.singleNumber([4, 1, 2, 1, 2]) == 4
    
    # Test 4: Negative numbers
    assert solution.singleNumber([-1, -1, -2]) == -2
    
    # Test 5: Zero
    assert solution.singleNumber([0, 1, 1]) == 0
    
    # Test 6: Large numbers
    assert solution.singleNumber([30000, -30000, 30000]) == -30000
    
    # Test 7: Position variations
    assert solution.singleNumber([5, 1, 1, 2, 2]) == 5  # Beginning
    assert solution.singleNumber([1, 1, 5, 2, 2]) == 5  # Middle
    assert solution.singleNumber([1, 1, 2, 2, 5]) == 5  # End
    
    print("All tests passed! ✓")

test_single_number()
```

---

## Complexity Analysis

### Time Complexity: O(n)

**Analysis**:
- Single pass through the array
- Each XOR operation is O(1)
- Total: O(n)

**Proof**:
```
For array of size n:
- Loop iterations: n
- Work per iteration: 1 XOR operation = O(1)
- Total: n × O(1) = O(n)
```

### Space Complexity: O(1)

**Analysis**:
- Only one variable `result` used
- No data structures that grow with input
- Total: O(1)

**Detailed Breakdown**:
```
Memory used:
- result variable: 4 or 8 bytes (int)
- Loop variable: 4 or 8 bytes
- Total: Constant, independent of n
```

### Comparison with Other Approaches

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Brute Force | O(n²) | O(1) | Too slow |
| Sorting | O(n log n) | O(1) or O(n) | Modifies array |
| Hash Set | O(n) | O(n) | Extra space |
| Hash Map | O(n) | O(n) | Extra space |
| Math | O(n) | O(n) | Overflow risk |
| **XOR** | **O(n)** | **O(1)** | **Optimal!** ✅ |

---

## Common Mistakes & Pitfalls

### Mistake 1: Using HashMap/HashSet

```python
# ❌ Wrong: Uses O(n) space
def singleNumber(nums):
    seen = set()
    for num in nums:
        if num in seen:
            seen.remove(num)
        else:
            seen.add(num)
    return list(seen)[0]
```

**Why it's wrong**: Violates O(1) space constraint.

### Mistake 2: Sorting First

```python
# ❌ Wrong: O(n log n) time
def singleNumber(nums):
    nums.sort()
    for i in range(0, len(nums) - 1, 2):
        if nums[i] != nums[i + 1]:
            return nums[i]
    return nums[-1]
```

**Why it's wrong**: Violates O(n) time constraint.

### Mistake 3: Initializing with nums[0]

```python
# ❌ Potential issue
def singleNumber(nums):
    result = nums[0]  # What if nums[0] appears twice?
    for i in range(1, len(nums)):
        result ^= nums[i]
    return result
```

**Why it's problematic**: 
- Works but less clean
- Edge case handling harder
- Better to start with 0

```python
# ✅ Correct
def singleNumber(nums):
    result = 0  # Identity element for XOR
    for num in nums:
        result ^= num
    return result
```

### Mistake 4: Not Understanding XOR Properties

```python
# ❌ Wrong thinking
# "XOR will always give the missing number"
```

**Correction**: XOR works specifically because:
- Pairs cancel (a ^ a = 0)
- Identity with 0 (a ^ 0 = a)
- Not magic, it's mathematics!

### Mistake 5: Overthinking

```python
# ❌ Unnecessarily complex
def singleNumber(nums):
    freq = {}
    for num in nums:
        if num not in freq:
            freq[num] = 0
        freq[num] += 1
    
    for num in freq:
        if freq[num] == 1:
            return num
```

**Better**: When you know XOR, use it! Simple is better.

---

## Problem Variations

### Variation 1: Single Number II (LeetCode #137)

**Problem**: Every element appears **3 times** except for one (appears once).

**Approach**: Cannot use simple XOR. Need to count bits!

```python
def singleNumber(nums):
    ones, twos = 0, 0
    
    for num in nums:
        # Add to ones if not in twos
        ones = (ones ^ num) & ~twos
        # Add to twos if not in ones
        twos = (twos ^ num) & ~ones
    
    return ones
```

**Alternative - Bit Counting**:
```python
def singleNumber(nums):
    result = 0
    for i in range(32):
        bit_sum = 0
        for num in nums:
            bit_sum += (num >> i) & 1
        # If not divisible by 3, single number has this bit
        if bit_sum % 3:
            result |= (1 << i)
    
    # Handle negative numbers
    if result >= 2**31:
        result -= 2**32
    
    return result
```

### Variation 2: Single Number III (LeetCode #260)

**Problem**: Every element appears **twice** except for **two** elements.

**Approach**: Use XOR to find `a ^ b`, then partition and XOR separately.

```python
def singleNumber(nums):
    # XOR all numbers: result = a ^ b
    xor_all = 0
    for num in nums:
        xor_all ^= num
    
    # Find rightmost set bit (difference between a and b)
    rightmost_bit = xor_all & (-xor_all)
    
    # Partition numbers and XOR separately
    a, b = 0, 0
    for num in nums:
        if num & rightmost_bit:
            a ^= num
        else:
            b ^= num
    
    return [a, b]
```

**Explanation**:
1. `a ^ b` gives us XOR of two singles
2. Any set bit in `a ^ b` means a and b differ at that position
3. Partition array based on that bit
4. XOR each partition separately

### Variation 3: Missing Number (LeetCode #268)

**Problem**: Array contains n distinct numbers from 0 to n. Find missing.

**Approach 1 - XOR**:
```python
def missingNumber(nums):
    result = len(nums)
    for i in range(len(nums)):
        result ^= i ^ nums[i]
    return result
```

**Approach 2 - Math**:
```python
def missingNumber(nums):
    n = len(nums)
    expected_sum = n * (n + 1) // 2
    actual_sum = sum(nums)
    return expected_sum - actual_sum
```

### Variation 4: Find Duplicate (LeetCode #287)

**Problem**: Array contains n+1 numbers in range [1, n]. One number repeats.

**Cannot use XOR directly** because not all numbers appear same number of times.

**Solution**: Floyd's Cycle Detection (not bit manipulation)

---

## Related Problems

### 1. **Hamming Distance** (LeetCode #461)
Count positions where bits differ.

```python
def hammingDistance(x, y):
    xor = x ^ y
    count = 0
    while xor:
        count += xor & 1
        xor >>= 1
    return count
```

### 2. **Number of 1 Bits** (LeetCode #191)
Count set bits in integer.

```python
def hammingWeight(n):
    count = 0
    while n:
        count += n & 1
        n >>= 1
    return count

# Optimized: O(number of set bits)
def hammingWeight(n):
    count = 0
    while n:
        n &= n - 1  # Clear rightmost set bit
        count += 1
    return count
```

### 3. **Power of Two** (LeetCode #231)
Check if number is power of 2.

```python
def isPowerOfTwo(n):
    return n > 0 and (n & (n - 1)) == 0
```

### 4. **Reverse Bits** (LeetCode #190)
Reverse bits of 32-bit integer.

```python
def reverseBits(n):
    result = 0
    for i in range(32):
        result = (result << 1) | (n & 1)
        n >>= 1
    return result
```

### 5. **Maximum XOR of Two Numbers** (LeetCode #421)
Find maximum XOR of any two numbers in array.

```python
def findMaximumXOR(nums):
    max_xor = 0
    mask = 0
    
    for i in range(31, -1, -1):
        mask |= (1 << i)
        prefixes = {num & mask for num in nums}
        temp = max_xor | (1 << i)
        
        for prefix in prefixes:
            if temp ^ prefix in prefixes:
                max_xor = temp
                break
    
    return max_xor
```

---

## Practice Problems

### Easy Level
1. **Single Number** (LeetCode #136) ⭐ This problem
2. **Missing Number** (LeetCode #268)
3. **Power of Two** (LeetCode #231)
4. **Number of 1 Bits** (LeetCode #191)
5. **Reverse Bits** (LeetCode #190)
6. **Hamming Distance** (LeetCode #461)

### Medium Level
7. **Single Number II** (LeetCode #137) - 3 occurrences
8. **Single Number III** (LeetCode #260) - 2 singles
9. **Bitwise AND of Numbers Range** (LeetCode #201)
10. **Sum of Two Integers** (LeetCode #371) - No + or -
11. **Counting Bits** (LeetCode #338)
12. **Maximum XOR** (LeetCode #421)

### Hard Level
13. **Maximum XOR With Element from Array** (LeetCode #1707)
14. **Minimum XOR Sum of Two Arrays** (LeetCode #1879)

---

## Summary & Key Takeaways

### Why XOR is Perfect for This Problem

1. **Self-Cancellation**: Duplicate pairs eliminate themselves
2. **Identity**: Single number survives XOR with 0
3. **Order Independence**: Commutative and associative
4. **Space Efficient**: Only need one variable
5. **Time Efficient**: Single pass through array

### When to Think About XOR

- Finding unique/missing elements
- Pairs that cancel out
- Toggle operations
- Bit difference problems
- Encryption/decryption scenarios

### General Bit Manipulation Pattern

```
Problem: Find element(s) with different occurrence count
Technique: Use bit properties to cancel out duplicates

Key Questions:
1. What cancels out? (pairs, triplets, etc.)
2. What remains? (singles, differences, etc.)
3. Can I use XOR, AND, OR, or bit counting?
```

### The Power of Simplicity

This problem teaches an important lesson:
> **The most elegant solutions often come from understanding fundamental properties.**

- Don't immediately reach for HashMap/HashSet
- Think about the mathematical properties
- Simple bit operations can be powerful

### Interview Tips

1. **Start with Constraints**: O(n) time, O(1) space → Think bit manipulation
2. **Explain XOR Properties**: Show you understand WHY it works
3. **Walk Through Example**: Demonstrate step-by-step XOR
4. **Discuss Alternatives**: Mention other approaches and their drawbacks
5. **Handle Edge Cases**: Show thoroughness

---

## Additional Resources

### Visualizations
- [Bit Manipulation Visualizer](https://visualgo.net/)
- [XOR Interactive Demo](https://www.mathsisfun.com/binary-number-system.html)

### Further Reading
- Introduction to Algorithms (CLRS) - Chapter on Bit Manipulation
- Hacker's Delight by Henry S. Warren Jr.
- [LeetCode Discuss - Single Number](https://leetcode.com/problems/single-number/discuss/)

### Related Concepts
- Boolean Algebra
- Digital Logic Design
- Computer Architecture - ALU Operations
- Cryptography - Stream Ciphers

---

## Practice Template

```python
"""
XOR PROBLEM TEMPLATE

When you encounter:
- Elements appearing even number of times (2, 4, 6, ...)
- Need to find element(s) appearing odd times (1, 3, ...)
- O(1) space constraint
- O(n) time allowed

Think: XOR!

Key Properties:
a ^ a = 0
a ^ 0 = a
a ^ b ^ a = b (pairs cancel)
"""

def solve_xor_problem(nums):
    result = 0
    for num in nums:
        result ^= num
    return result

# Time: O(n)
# Space: O(1)
```

---

**Last Updated**: 2024
**Problem Source**: LeetCode #136
**Difficulty**: Easy
**Pattern**: Bit Manipulation / XOR Properties

---

Good luck with your coding interview preparation! Remember: **Understanding > Memorization** 🚀
