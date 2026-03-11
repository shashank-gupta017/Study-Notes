# Longest Common Subsequence - LeetCode #1143

**Difficulty**: Medium  
**Pattern**: 2D DP (String Matching)  
**Frequency**: Very High (Classic 2D DP)

---

## Problem Statement

Given two strings `text1` and `text2`, return **the length of their longest common subsequence**. If there is no common subsequence, return `0`.

A **subsequence** of a string is a new string generated from the original string with some characters (can be none) deleted without changing the relative order of the remaining characters.

- For example, `"ace"` is a subsequence of `"abcde"`.

A **common subsequence** of two strings is a subsequence that is common to both strings.

**Example 1:**
```
Input: text1 = "abcde", text2 = "ace" 
Output: 3
Explanation: The longest common subsequence is "ace" and its length is 3.
```

**Example 2:**
```
Input: text1 = "abc", text2 = "abc"
Output: 3
Explanation: The longest common subsequence is "abc" and its length is 3.
```

**Example 3:**
```
Input: text1 = "abc", text2 = "def"
Output: 0
Explanation: There is no such common subsequence, so the result is 0.
```

**Constraints:**
- `1 <= text1.length, text2.length <= 1000`
- `text1` and `text2` consist of only lowercase English characters

---

## Core Concepts

### What is a Subsequence?

**Subsequence:** Characters from original string in same order (but not necessarily contiguous)

**Examples from "abcde":**
```
✓ "ace"     - Valid subsequence
✓ "bd"      - Valid subsequence
✓ "abcde"   - Valid (entire string)
✓ ""        - Valid (empty)
✗ "aec"     - Invalid (order changed)
✗ "abf"     - Invalid ('f' not in original)
```

### Subsequence vs Substring vs Subarray

| Type | Contiguous? | Order Preserved? | Example from "abc" |
|------|-------------|------------------|-------------------|
| Substring | Yes | Yes | "ab", "bc", "abc" |
| Subsequence | No | Yes | "a", "ac", "abc" |
| Subset | No | No | {a,c,b} = {b,a,c} |

### Problem Intuition

**Given:** text1 = "abcde", text2 = "ace"

**All subsequences of text1:**
```
"", "a", "b", "c", "d", "e", "ab", "ac", "ad", "ae", "bc", ...
```

**All subsequences of text2:**
```
"", "a", "c", "e", "ac", "ae", "ce", "ace"
```

**Common subsequences:**
```
"", "a", "c", "e", "ac", "ae", "ce", "ace"
```

**Longest:** "ace" (length 3)

### The Recurrence Relation

**Key Observation:**

For strings text1[0..i] and text2[0..j]:

```
If text1[i] == text2[j]:
    LCS(i, j) = 1 + LCS(i-1, j-1)
    (Match found! Add 1 and look at remaining strings)

Else:
    LCS(i, j) = max(LCS(i-1, j), LCS(i, j-1))
    (No match - try skipping character from either string)
```

### Visual Understanding

**text1 = "ace", text2 = "ace"**

```
    ""  a  c  e
""  0   0  0  0
a   0   1  1  1   (match 'a': 1 + LCS("", ""))
c   0   1  2  2   (match 'c': 1 + LCS("a", "a"))
e   0   1  2  3   (match 'e': 1 + LCS("ac", "ac"))
```

**text1 = "abcde", text2 = "ace"**

```
      ""  a  c  e
""    0   0  0  0
a     0   1  1  1
b     0   1  1  1
c     0   1  2  2
d     0   1  2  2
e     0   1  2  3
```

---

## Solution Approach

### Approach 1: Recursion (Naive)

```python
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        """
        Naive recursion - exponential time.
        
        Time: O(2^(m+n)) - each position has 2 choices
        Space: O(m+n) - recursion depth
        """
        def dp(i, j):
            # Base case: empty string
            if i < 0 or j < 0:
                return 0
            
            # Match found
            if text1[i] == text2[j]:
                return 1 + dp(i - 1, j - 1)
            
            # No match - try both options
            return max(dp(i - 1, j), dp(i, j - 1))
        
        return dp(len(text1) - 1, len(text2) - 1)
```

**Problem:** Exponential time due to overlapping subproblems!

---

### Approach 2: Memoization (Top-Down DP)

```python
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        """
        Top-down DP with memoization.
        
        Time: O(m × n)
        Space: O(m × n) - memo + recursion
        """
        memo = {}
        
        def dp(i, j):
            # Base case
            if i < 0 or j < 0:
                return 0
            
            # Check cache
            if (i, j) in memo:
                return memo[(i, j)]
            
            # Compute
            if text1[i] == text2[j]:
                result = 1 + dp(i - 1, j - 1)
            else:
                result = max(dp(i - 1, j), dp(i, j - 1))
            
            memo[(i, j)] = result
            return result
        
        return dp(len(text1) - 1, len(text2) - 1)
```

---

### Approach 3: Tabulation (Bottom-Up DP)

```python
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        """
        Bottom-up DP with 2D table.
        
        Time: O(m × n)
        Space: O(m × n)
        """
        m, n = len(text1), len(text2)
        
        # dp[i][j] = LCS of text1[0..i-1] and text2[0..j-1]
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if text1[i-1] == text2[j-1]:
                    dp[i][j] = 1 + dp[i-1][j-1]
                else:
                    dp[i][j] = max(dp[i-1][j], dp[i][j-1])
        
        return dp[m][n]
```

**This is the standard solution!**

---

### Approach 4: Space-Optimized (1D Array)

```python
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        """
        Space-optimized: use only two rows.
        
        Time: O(m × n)
        Space: O(min(m, n))
        """
        # Make text2 the shorter one
        if len(text1) < len(text2):
            text1, text2 = text2, text1
        
        m, n = len(text1), len(text2)
        prev = [0] * (n + 1)
        curr = [0] * (n + 1)
        
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if text1[i-1] == text2[j-1]:
                    curr[j] = 1 + prev[j-1]
                else:
                    curr[j] = max(prev[j], curr[j-1])
            
            prev, curr = curr, prev
        
        return prev[n]
```

---

### Approach 5: Further Optimized (Single Array)

```python
class Solution:
    def longestCommonSubsequence(self, text1: str, text2: str) -> int:
        """
        Single array optimization (more complex).
        
        Time: O(m × n)
        Space: O(min(m, n))
        """
        if len(text1) < len(text2):
            text1, text2 = text2, text1
        
        m, n = len(text1), len(text2)
        dp = [0] * (n + 1)
        
        for i in range(1, m + 1):
            prev_diag = 0
            for j in range(1, n + 1):
                temp = dp[j]
                if text1[i-1] == text2[j-1]:
                    dp[j] = 1 + prev_diag
                else:
                    dp[j] = max(dp[j], dp[j-1])
                prev_diag = temp
        
        return dp[n]
```

---

## Detailed Walkthrough

### Example: text1 = "abcde", text2 = "ace"

**Building the DP table:**

```
Step 1: Initialize
       ""  a  c  e
    "" 0   0  0  0
    a  0   ?  ?  ?
    b  0   ?  ?  ?
    c  0   ?  ?  ?
    d  0   ?  ?  ?
    e  0   ?  ?  ?

Step 2: Fill row 1 (text1[0] = 'a')
       ""  a  c  e
    "" 0   0  0  0
    a  0   1  1  1
           ↑  ↑  ↑
         match | carry over

    i=1, j=1: text1[0]='a' == text2[0]='a' ✓
              dp[1][1] = 1 + dp[0][0] = 1
    
    i=1, j=2: text1[0]='a' != text2[1]='c'
              dp[1][2] = max(dp[0][2], dp[1][1]) = max(0, 1) = 1
    
    i=1, j=3: text1[0]='a' != text2[2]='e'
              dp[1][3] = max(dp[0][3], dp[1][2]) = max(0, 1) = 1

Step 3: Fill row 2 (text1[1] = 'b')
       ""  a  c  e
    "" 0   0  0  0
    a  0   1  1  1
    b  0   1  1  1
           ↑  ↑  ↑
         no match | carry over
    
    i=2, j=1: text1[1]='b' != text2[0]='a'
              dp[2][1] = max(dp[1][1], dp[2][0]) = max(1, 0) = 1
    
    i=2, j=2: text1[1]='b' != text2[1]='c'
              dp[2][2] = max(dp[1][2], dp[2][1]) = max(1, 1) = 1
    
    i=2, j=3: text1[1]='b' != text2[2]='e'
              dp[2][3] = max(dp[1][3], dp[2][2]) = max(1, 1) = 1

Step 4: Fill row 3 (text1[2] = 'c')
       ""  a  c  e
    "" 0   0  0  0
    a  0   1  1  1
    b  0   1  1  1
    c  0   1  2  2
           ↑  ↑  ↑
         no | match | carry
    
    i=3, j=1: text1[2]='c' != text2[0]='a'
              dp[3][1] = max(dp[2][1], dp[3][0]) = 1
    
    i=3, j=2: text1[2]='c' == text2[1]='c' ✓
              dp[3][2] = 1 + dp[2][1] = 1 + 1 = 2
    
    i=3, j=3: text1[2]='c' != text2[2]='e'
              dp[3][3] = max(dp[2][3], dp[3][2]) = max(1, 2) = 2

Step 5: Fill row 4 (text1[3] = 'd')
       ""  a  c  e
    "" 0   0  0  0
    a  0   1  1  1
    b  0   1  1  1
    c  0   1  2  2
    d  0   1  2  2
    
    (All mismatches, carry over previous max)

Step 6: Fill row 5 (text1[4] = 'e')
       ""  a  c  e
    "" 0   0  0  0
    a  0   1  1  1
    b  0   1  1  1
    c  0   1  2  2
    d  0   1  2  2
    e  0   1  2  3
                 ↑
              match! 1 + dp[4][2] = 1 + 2 = 3

Final Answer: dp[5][3] = 3
```

---

## Complexity Analysis

### Time Complexity: O(m × n)

Where m = len(text1), n = len(text2)

**Analysis:**
- Nested loops: m iterations × n iterations
- Each cell: O(1) computation
- Total: O(m × n)

**Example:** m=1000, n=1000 → 1,000,000 operations

### Space Complexity

| Approach | Space | Explanation |
|----------|-------|-------------|
| Recursion | O(m+n) | Call stack |
| Memoization | O(m×n) | Memo + stack |
| Tabulation | O(m×n) | 2D DP table |
| Two Rows | O(n) | Two arrays |
| Single Row | O(min(m,n)) | One array |

**Best space: O(min(m,n)) with single array optimization**

---

## Pattern Variations

### Variation 1: Print the LCS

**Problem:** Return the actual subsequence, not just length.

```python
def longestCommonSubsequence_with_string(text1, text2):
    """
    Return the actual LCS string.
    """
    m, n = len(text1), len(text2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # Build DP table
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                dp[i][j] = 1 + dp[i-1][j-1]
            else:
                dp[i][j] = max(dp[i-1][j], dp[i][j-1])
    
    # Reconstruct LCS
    lcs = []
    i, j = m, n
    while i > 0 and j > 0:
        if text1[i-1] == text2[j-1]:
            lcs.append(text1[i-1])
            i -= 1
            j -= 1
        elif dp[i-1][j] > dp[i][j-1]:
            i -= 1
        else:
            j -= 1
    
    return ''.join(reversed(lcs))
```

### Variation 2: Edit Distance

**Problem:** Minimum operations to convert text1 to text2.

```python
def minDistance(word1, word2):
    """
    LeetCode 72: Edit Distance
    Operations: insert, delete, replace
    """
    m, n = len(word1), len(word2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # Base cases
    for i in range(m + 1):
        dp[i][0] = i  # Delete all
    for j in range(n + 1):
        dp[0][j] = j  # Insert all
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if word1[i-1] == word2[j-1]:
                dp[i][j] = dp[i-1][j-1]  # No operation
            else:
                dp[i][j] = 1 + min(
                    dp[i-1][j],    # Delete
                    dp[i][j-1],    # Insert
                    dp[i-1][j-1]   # Replace
                )
    
    return dp[m][n]
```

### Variation 3: Longest Palindromic Subsequence

**Problem:** Longest palindromic subsequence in a string.

```python
def longestPalindromeSubseq(s):
    """
    LeetCode 516: LCS of s and reverse(s)
    """
    return longestCommonSubsequence(s, s[::-1])
```

### Variation 4: Shortest Common Supersequence

**Problem:** Shortest string containing both strings as subsequences.

```python
def shortestCommonSupersequence(str1, str2):
    """
    LeetCode 1092: Length = len(str1) + len(str2) - LCS
    """
    lcs_length = longestCommonSubsequence(str1, str2)
    return len(str1) + len(str2) - lcs_length
```

### Variation 5: Uncrossed Lines

**Problem:** Maximum uncrossed lines connecting matching elements.

```python
def maxUncrossedLines(nums1, nums2):
    """
    LeetCode 1035: Exactly same as LCS!
    """
    return longestCommonSubsequence(nums1, nums2)
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Wrong Indexing

```python
# ❌ Wrong: Off-by-one error
if text1[i] == text2[j]:  # Should be i-1, j-1
    dp[i][j] = 1 + dp[i-1][j-1]

# ✅ Correct
if text1[i-1] == text2[j-1]:
    dp[i][j] = 1 + dp[i-1][j-1]
```

### Mistake 2: Wrong DP Table Size

```python
# ❌ Wrong: Not enough space for base case
dp = [[0] * n for _ in range(m)]

# ✅ Correct: Need (m+1) × (n+1)
dp = [[0] * (n + 1) for _ in range(m + 1)]
```

### Mistake 3: Comparing Indices Not Characters

```python
# ❌ Wrong: Comparing positions
if i == j:
    dp[i][j] = ...

# ✅ Correct: Compare characters
if text1[i-1] == text2[j-1]:
    dp[i][j] = ...
```

### Mistake 4: Wrong Base Case

```python
# ❌ Wrong: Initialize to -1 or None
dp = [[-1] * (n + 1) for _ in range(m + 1)]

# ✅ Correct: Initialize to 0
dp = [[0] * (n + 1) for _ in range(m + 1)]
```

---

## Edge Cases & Testing

### Edge Cases

```python
# Both strings empty
text1 = "", text2 = ""
# Output: 0

# One string empty
text1 = "abc", text2 = ""
# Output: 0

# Identical strings
text1 = "abc", text2 = "abc"
# Output: 3

# No common characters
text1 = "abc", text2 = "def"
# Output: 0

# One character each
text1 = "a", text2 = "a"
# Output: 1

text1 = "a", text2 = "b"
# Output: 0

# Entire first is subsequence of second
text1 = "ace", text2 = "abcde"
# Output: 3

# Reverse strings
text1 = "abc", text2 = "cba"
# Output: 1 (any single character)

# Long strings
text1 = "a" * 1000, text2 = "a" * 1000
# Output: 1000
```

### Test Suite

```python
def test_lcs():
    solution = Solution()
    
    # Test 1: Basic examples
    assert solution.longestCommonSubsequence("abcde", "ace") == 3
    assert solution.longestCommonSubsequence("abc", "abc") == 3
    assert solution.longestCommonSubsequence("abc", "def") == 0
    
    # Test 2: Edge cases
    assert solution.longestCommonSubsequence("", "") == 0
    assert solution.longestCommonSubsequence("a", "") == 0
    assert solution.longestCommonSubsequence("", "a") == 0
    
    # Test 3: Single character
    assert solution.longestCommonSubsequence("a", "a") == 1
    assert solution.longestCommonSubsequence("a", "b") == 0
    
    # Test 4: Partial overlap
    assert solution.longestCommonSubsequence("abcde", "abe") == 3
    assert solution.longestCommonSubsequence("abc", "ac") == 2
    
    # Test 5: Reversed
    assert solution.longestCommonSubsequence("abc", "cba") == 1
    
    # Test 6: Repeating characters
    assert solution.longestCommonSubsequence("aaa", "aaa") == 3
    assert solution.longestCommonSubsequence("aaa", "aa") == 2
    
    # Test 7: Long strings
    result = solution.longestCommonSubsequence("a" * 100, "a" * 100)
    assert result == 100
    
    print("All tests passed!")

def test_reconstruction():
    """Test reconstructing the actual LCS."""
    text1 = "abcde"
    text2 = "ace"
    
    lcs_str = longestCommonSubsequence_with_string(text1, text2)
    assert lcs_str == "ace"
    
    text1 = "abc"
    text2 = "def"
    lcs_str = longestCommonSubsequence_with_string(text1, text2)
    assert lcs_str == ""
    
    print("Reconstruction test passed!")
```

---

## Interview Tips

### Questions to Ask

1. **"Can the strings be empty?"**
   - Yes, return 0 for empty strings

2. **"Are the strings only lowercase letters?"**
   - Usually yes (constraint)

3. **"Do you want just the length or the actual subsequence?"**
   - Usually just length (easier)

4. **"What's the maximum length of strings?"**
   - Determines if O(m×n) space is acceptable

5. **"Can I modify the input strings?"**
   - Usually doesn't matter

### Explaining Your Solution

**Clear explanation:**

1. "This is a classic 2D DP problem for string matching"

2. "I create a table where dp[i][j] is the LCS length for first i and j characters"

3. "If characters match, I add 1 to the diagonal value"

4. "If they don't match, I take the max from left or top"

5. "Time is O(m×n) and I can optimize space to O(n) using rolling arrays"

6. "Final answer is in dp[m][n]"

### Common Follow-ups

**Q: Can you print the actual LCS?**
A: Yes, backtrack through DP table following max values

**Q: What if strings are very long?**
A: Use space optimization with two rows: O(min(m,n))

**Q: How is this related to edit distance?**
A: Edit distance uses similar DP but allows more operations

**Q: Can you find ALL longest common subsequences?**
A: Yes, but more complex - need to track all paths

**Q: What about longest common substring (contiguous)?**
A: Different problem - reset to 0 when mismatch

---

## Related Problems

### Same Pattern (2D DP)

1. **LeetCode 72: Edit Distance**
   - Similar structure
   - Three operations instead of two choices

2. **LeetCode 516: Longest Palindromic Subsequence**
   - LCS of string and its reverse

3. **LeetCode 1092: Shortest Common Supersequence**
   - Uses LCS in solution

### String DP

4. **LeetCode 718: Longest Common Substring**
   - Contiguous version
   - Reset to 0 on mismatch

5. **LeetCode 583: Delete Operation**
   - Use LCS to find minimum deletions

6. **LeetCode 1035: Maximum Uncrossed Lines**
   - Exactly same as LCS

---

## Advanced Techniques

### Technique 1: Multiple Sequences

```python
def lcs_three_strings(s1, s2, s3):
    """
    LCS of three strings - 3D DP.
    """
    l1, l2, l3 = len(s1), len(s2), len(s3)
    dp = [[[0] * (l3 + 1) for _ in range(l2 + 1)] for _ in range(l1 + 1)]
    
    for i in range(1, l1 + 1):
        for j in range(1, l2 + 1):
            for k in range(1, l3 + 1):
                if s1[i-1] == s2[j-1] == s3[k-1]:
                    dp[i][j][k] = 1 + dp[i-1][j-1][k-1]
                else:
                    dp[i][j][k] = max(
                        dp[i-1][j][k],
                        dp[i][j-1][k],
                        dp[i][j][k-1]
                    )
    
    return dp[l1][l2][l3]
```

### Technique 2: Longest Common Substring

```python
def longestCommonSubstring(text1, text2):
    """
    Contiguous version - different from subsequence.
    """
    m, n = len(text1), len(text2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    max_length = 0
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                dp[i][j] = 1 + dp[i-1][j-1]
                max_length = max(max_length, dp[i][j])
            else:
                dp[i][j] = 0  # Reset! (key difference)
    
    return max_length
```

### Technique 3: With Weighted Characters

```python
def lcs_weighted(text1, text2, weights):
    """
    Each character has a weight, maximize total weight.
    """
    m, n = len(text1), len(text2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                char = text1[i-1]
                dp[i][j] = weights[char] + dp[i-1][j-1]
            else:
                dp[i][j] = max(dp[i-1][j], dp[i][j-1])
    
    return dp[m][n]
```

### Technique 4: Memoization with Tuple Keys

```python
def lcs_memo_cleaner(text1, text2):
    """
    Cleaner memoization using lru_cache.
    """
    from functools import lru_cache
    
    @lru_cache(maxsize=None)
    def dp(i, j):
        if i < 0 or j < 0:
            return 0
        
        if text1[i] == text2[j]:
            return 1 + dp(i - 1, j - 1)
        
        return max(dp(i - 1, j), dp(i, j - 1))
    
    return dp(len(text1) - 1, len(text2) - 1)
```

---

## Summary

### Key Takeaways

1. **Pattern**: 2D DP for string matching problems
2. **Match**: If chars match, dp[i][j] = 1 + dp[i-1][j-1]
3. **No Match**: Take max(dp[i-1][j], dp[i][j-1])
4. **Base Case**: Empty string has LCS of 0
5. **Complexity**: O(m×n) time, O(min(m,n)) space optimized

### Algorithm Template

```python
def longestCommonSubsequence(text1: str, text2: str) -> int:
    """
    Standard 2D DP solution.
    
    Time: O(m × n)
    Space: O(m × n)
    """
    m, n = len(text1), len(text2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i-1] == text2[j-1]:
                dp[i][j] = 1 + dp[i-1][j-1]
            else:
                dp[i][j] = max(dp[i-1][j], dp[i][j-1])
    
    return dp[m][n]
```

### Complexity Summary

**Standard Solution:**
- Time: O(m × n)
- Space: O(m × n)

**Space-Optimized:**
- Time: O(m × n)
- Space: O(min(m, n))

### Pattern Recognition

**Use this pattern when:**
- Comparing two sequences
- Finding optimal alignment
- Order must be preserved
- Can skip elements (non-contiguous)

---

**Tags**: #dynamic-programming #2d-dp #string #medium  
**Related**: Edit Distance, Longest Palindromic Subsequence, Shortest Common Supersequence  
**Companies**: Amazon, Microsoft, Google, Facebook, Bloomberg, Apple
