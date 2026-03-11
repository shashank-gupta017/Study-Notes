# Longest Repeating Character Replacement (Medium)

## Problem Statement
You are given a string `s` and an integer `k`. You can choose any character of the string and change it to any other uppercase English character. You can perform this operation at most `k` times.

Return the **length of the longest substring** containing the same letter you can get after performing the above operations.

**LeetCode Link**: [424. Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/)

---

## Examples

### Example 1:
```
Input: s = "ABAB", k = 2
Output: 4
Explanation: Replace the two 'A's with two 'B's or vice versa.
The result is "AAAA" or "BBBB" with length 4.
```

### Example 2:
```
Input: s = "AABABBA", k = 1
Output: 4
Explanation: Replace one 'A' in the middle with 'B' and form "AABBBBA".
The substring "BBBB" has the longest repeating letters, which is 4.
```

### Example 3:
```
Input: s = "ABAA", k = 0
Output: 2
Explanation: Without replacements, the longest substring is "AA" with length 2.
```

---

## Constraints
- `1 <= s.length <= 10^5`
- `s` consists of only uppercase English letters
- `0 <= k <= s.length`

---

## Pattern Recognition

This is a **Sliding Window with Character Frequency** problem because:
1. We need to find the **longest substring** with a specific property
2. The window **validity** depends on character counts and replacements allowed
3. We can **expand** window to the right and **shrink** when invalid
4. We track **character frequencies** within the current window

**Key Insight**: A window is valid if: `window_size - max_frequency <= k`
- `max_frequency` = count of most frequent character in window
- Remaining characters need to be replaced (must be ≤ k)

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Try all possible substrings and check if each can be made repeating with k replacements.

```java
class Solution {
    public int characterReplacement(String s, int k) {
        int maxLength = 0;
        
        // Try all starting positions
        for (int i = 0; i < s.length(); i++) {
            int[] count = new int[26];
            
            // Try all ending positions
            for (int j = i; j < s.length(); j++) {
                count[s.charAt(j) - 'A']++;
                
                // Find max frequency in current window
                int maxFreq = 0;
                for (int freq : count) {
                    maxFreq = Math.max(maxFreq, freq);
                }
                
                // Check if valid: windowSize - maxFreq <= k
                int windowSize = j - i + 1;
                if (windowSize - maxFreq <= k) {
                    maxLength = Math.max(maxLength, windowSize);
                }
            }
        }
        
        return maxLength;
    }
}
```

**Time Complexity**: O(n² × 26) = O(n²) - nested loops with frequency check
**Space Complexity**: O(26) = O(1) - fixed array size
**Problem**: Too slow for large inputs!

---

### Approach 2: Sliding Window with Frequency Array (OPTIMAL) ⭐
**Idea**: Use sliding window with character frequency tracking. Expand right, shrink left when invalid.

```java
class Solution {
    public int characterReplacement(String s, int k) {
        int[] count = new int[26]; // Frequency of each character
        int maxLength = 0;
        int maxFreq = 0; // Max frequency in current window
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            // Add right character to window
            count[s.charAt(right) - 'A']++;
            
            // Update max frequency in window
            maxFreq = Math.max(maxFreq, count[s.charAt(right) - 'A']);
            
            // Check if window is valid: windowSize - maxFreq <= k
            int windowSize = right - left + 1;
            
            if (windowSize - maxFreq > k) {
                // Invalid window - shrink from left
                count[s.charAt(left) - 'A']--;
                left++;
            }
            
            // Update max length (window is guaranteed valid here)
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
}
```

**Time Complexity**: O(n) - single pass through string
**Space Complexity**: O(26) = O(1) - fixed array size
**Why Optimal**: Linear time, best possible for this problem

---

### Approach 3: Optimized Sliding Window (Avoid Recalculating MaxFreq)
**Idea**: Don't decrease maxFreq when shrinking - it's safe to keep historical max.

```java
class Solution {
    public int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int maxLength = 0;
        int maxFreq = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            count[s.charAt(right) - 'A']++;
            maxFreq = Math.max(maxFreq, count[s.charAt(right) - 'A']);
            
            // Keep expanding window if possible
            while (right - left + 1 - maxFreq > k) {
                count[s.charAt(left) - 'A']--;
                left++;
                // No need to recalculate maxFreq!
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(1) - fixed array
**Note**: Slightly cleaner with while loop, same performance

---

## Detailed Walkthrough (Approach 2: Optimal)

### Example: s = "AABABBA", k = 1

```
Step-by-step trace:

Initial: left = 0, maxFreq = 0, maxLength = 0, count = all zeros

Step 1: right = 0, char = 'A'
  count['A'] = 1
  maxFreq = 1
  windowSize = 1, check: 1 - 1 = 0 <= 1 ✓ Valid
  maxLength = 1
  Window: "A"

Step 2: right = 1, char = 'A'
  count['A'] = 2
  maxFreq = 2
  windowSize = 2, check: 2 - 2 = 0 <= 1 ✓ Valid
  maxLength = 2
  Window: "AA"

Step 3: right = 2, char = 'B'
  count['B'] = 1
  maxFreq = 2 (still 'A')
  windowSize = 3, check: 3 - 2 = 1 <= 1 ✓ Valid
  maxLength = 3
  Window: "AAB" (can replace 1 'B' with 'A')

Step 4: right = 3, char = 'A'
  count['A'] = 3
  maxFreq = 3
  windowSize = 4, check: 4 - 3 = 1 <= 1 ✓ Valid
  maxLength = 4
  Window: "AABA" (can replace 1 'B' with 'A')

Step 5: right = 4, char = 'B'
  count['B'] = 2, count['A'] = 3
  maxFreq = 3
  windowSize = 5, check: 5 - 3 = 2 > 1 ✗ Invalid!
  Need to shrink: remove s[0] = 'A'
  count['A'] = 2, left = 1
  New windowSize = 4, check: 4 - 3 = 1 <= 1 ✓ Valid
  maxLength = 4 (no change)
  Window: "ABAB"

Step 6: right = 5, char = 'B'
  count['B'] = 3, count['A'] = 2
  maxFreq = 3
  windowSize = 5, check: 5 - 3 = 2 > 1 ✗ Invalid!
  Need to shrink: remove s[1] = 'A'
  count['A'] = 1, left = 2
  New windowSize = 4, check: 4 - 3 = 1 <= 1 ✓ Valid
  maxLength = 4 (no change)
  Window: "BABB"

Step 7: right = 6, char = 'A'
  count['A'] = 2, count['B'] = 3
  maxFreq = 3
  windowSize = 5, check: 5 - 3 = 2 > 1 ✗ Invalid!
  Need to shrink: remove s[2] = 'B'
  count['B'] = 2, left = 3
  New windowSize = 4, check: 4 - 3 = 1 <= 1 ✓ Valid
  maxLength = 4 (no change)
  Window: "ABBA"

Final Result: maxLength = 4
```

---

### Visual Representation

```
s = "AABABBA", k = 1

Window Progression:

A A B A B B A
L R
maxFreq=1, size=1, need=0 ✓

A A B A B B A
L   R
maxFreq=2, size=2, need=0 ✓

A A B A B B A
L     R
maxFreq=2, size=3, need=1 ✓ (replace B with A)

A A B A B B A
L       R
maxFreq=3, size=4, need=1 ✓ (replace B with A)

A A B A B B A
L         R
maxFreq=3, size=5, need=2 ✗ (shrink!)

A A B A B B A
  L       R
maxFreq=3, size=4, need=1 ✓

The key: window_size - max_frequency = replacements needed
Must be <= k to be valid
```

---

## Edge Cases to Consider

```java
// Test Case 1: All same characters
Input: s = "AAAA", k = 2
Output: 4
// No replacements needed, entire string is valid

// Test Case 2: k = 0 (no replacements)
Input: s = "ABAB", k = 0
Output: 1
// Can only use single character substrings

// Test Case 3: k >= length
Input: s = "ABCD", k = 5
Output: 4
// Can replace everything to one character

// Test Case 4: Single character
Input: s = "A", k = 1
Output: 1
// Edge case: minimum length

// Test Case 5: Alternating characters
Input: s = "ABABAB", k = 2
Output: 5
// Replace 2 characters to extend

// Test Case 6: All different characters
Input: s = "ABCDEF", k = 2
Output: 3
// Can make "AAA" or "BBB" etc with 2 replacements

// Test Case 7: Large k
Input: s = "AABBBCCD", k = 3
Output: 7
// Can convert "AABBBCC" to all same (4+3=7)

// Test Case 8: Two character types
Input: s = "AAABBB", k = 0
Output: 3
// Either "AAA" or "BBB"

// Test Case 9: Complex case
Input: s = "ABCAABBBCCC", k = 2
Output: 5
// Multiple valid windows

// Test Case 10: Long string with pattern
Input: s = "AABACAABBBA", k = 1
Output: 5
// Find longest valid window: "AABBA" -> "AAAAA"
```

---

## Common Mistakes to Avoid

### Mistake 1: Not Tracking Max Frequency Correctly
```java
// ❌ WRONG: Recalculating max frequency every time
for (int right = 0; right < s.length(); right++) {
    count[s.charAt(right) - 'A']++;
    
    // This is O(26) per iteration = O(26n) total
    int maxFreq = 0;
    for (int freq : count) {
        maxFreq = Math.max(maxFreq, freq);
    }
}

// ✅ CORRECT: Update max frequency incrementally
int maxFreq = 0;
for (int right = 0; right < s.length(); right++) {
    count[s.charAt(right) - 'A']++;
    maxFreq = Math.max(maxFreq, count[s.charAt(right) - 'A']);
}
```

### Mistake 2: Wrong Window Validity Check
```java
// ❌ WRONG: Checking if replacements needed == k
if (windowSize - maxFreq == k) {
    // This misses cases where we need fewer than k replacements
}

// ✅ CORRECT: Check if replacements needed <= k
if (windowSize - maxFreq <= k) {
    // Valid window
}
```

### Mistake 3: Not Shrinking Window Properly
```java
// ❌ WRONG: Only moving left by 1 without checking validity
if (windowSize - maxFreq > k) {
    left++;
    // Might still be invalid!
}

// ✅ CORRECT: Keep shrinking until valid
while (windowSize - maxFreq > k) {
    count[s.charAt(left) - 'A']--;
    left++;
    windowSize = right - left + 1;
}
```

### Mistake 4: Forgetting to Update Count When Shrinking
```java
// ❌ WRONG: Not updating frequency count
if (windowSize - maxFreq > k) {
    left++; // Forgot to decrease count!
}

// ✅ CORRECT: Update count before moving pointer
if (windowSize - maxFreq > k) {
    count[s.charAt(left) - 'A']--;
    left++;
}
```

### Mistake 5: Wrong Window Size Calculation
```java
// ❌ WRONG: Forgetting +1
int windowSize = right - left; // Off by one!

// ✅ CORRECT: Include both endpoints
int windowSize = right - left + 1;
```

### Mistake 6: Updating MaxLength Before Validation
```java
// ❌ WRONG: Update before checking validity
maxLength = Math.max(maxLength, right - left + 1);
if (windowSize - maxFreq > k) {
    // shrink...
}

// ✅ CORRECT: Update after ensuring window is valid
if (windowSize - maxFreq > k) {
    // shrink...
}
maxLength = Math.max(maxLength, right - left + 1);
```

---

## Why This Window Validity Check Works

### The Formula: `window_size - max_frequency <= k`

**Intuition:**
- We want all characters in window to be the same
- Keep the most frequent character as-is
- Replace all other characters (these are the non-max-freq chars)
- Number of replacements needed = total chars - max frequent chars

**Example:**
```
Window: "AABBA" (size = 5)
Count: A=3, B=2
maxFreq = 3 (character 'A')

Replacements needed = 5 - 3 = 2
If k >= 2, we can make "AAAAA" by replacing 2 B's

Window: "ABCDE" (size = 5)
Count: A=1, B=1, C=1, D=1, E=1
maxFreq = 1

Replacements needed = 5 - 1 = 4
Need k >= 4 to make all same
```

---

## Optimization Techniques

### Optimization 1: Early Termination
```java
// If k >= s.length() - 1, entire string can be same character
if (k >= s.length() - 1) {
    return s.length();
}
```

### Optimization 2: Use If Instead of While for Shrinking
```java
// Since we only expand by 1 each iteration,
// we only need to shrink by at most 1
if (right - left + 1 - maxFreq > k) {
    count[s.charAt(left) - 'A']--;
    left++;
}
// This maintains window size or grows it by 1
```

### Optimization 3: Don't Decrease MaxFreq
```java
// We never decrease maxFreq even when shrinking
// This is safe because we only care about finding maximum window
// If historical maxFreq was higher, that window was already processed
maxFreq = Math.max(maxFreq, count[s.charAt(right) - 'A']);
// Never: maxFreq = recalculate();
```

---

## Complexity Analysis

### Time Complexity: O(n)
- **Single pass**: Right pointer goes from 0 to n-1
- **Left pointer**: Moves at most n times total (amortized)
- **Frequency updates**: O(1) per operation
- **Max frequency update**: O(1) per operation
- **Total**: O(n)

### Space Complexity: O(1)
- **Frequency array**: O(26) = O(1) - fixed size for uppercase letters
- **Variables**: O(1) - only a few integers
- **Total**: O(1) - constant space

### Why O(n) not O(n²):
```
The while loop for shrinking might seem to make it O(n²),
but consider the total work:
- Right pointer: n iterations (0 to n-1)
- Left pointer: at most n moves total across all iterations
- Total operations: 2n = O(n)

Each character is added once and removed at most once.
```

---

## Pattern: Sliding Window with Frequency

### Template:
```java
int[] count = new int[26];
int left = 0, maxFreq = 0, result = 0;

for (int right = 0; right < s.length(); right++) {
    // Add right element
    count[s.charAt(right) - 'A']++;
    maxFreq = Math.max(maxFreq, count[s.charAt(right) - 'A']);
    
    // Shrink window if invalid
    while (!isValid(left, right, maxFreq, k)) {
        count[s.charAt(left) - 'A']--;
        left++;
    }
    
    // Update result
    result = Math.max(result, right - left + 1);
}

boolean isValid(int left, int right, int maxFreq, int k) {
    return (right - left + 1) - maxFreq <= k;
}
```

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Constraints** (30 seconds)
> "The string contains only uppercase English letters, so I can use a fixed-size array of 26 for frequency counting. We can replace up to k characters. I need to find the longest substring that can be made into all same characters with at most k replacements."

**Step 2: Explain Approach** (1-2 minutes)
> "I'll use a sliding window approach:
> 1. Expand window to the right, adding characters
> 2. Track frequency of each character in current window
> 3. A window is valid if: window_size - max_frequency <= k
>    - This means we need to replace (window_size - max_frequency) characters
> 4. If window becomes invalid, shrink from left
> 5. Track maximum valid window size
> Time: O(n), Space: O(1)"

**Step 3: Walk Through Key Insight** (1 minute)
> "The key insight is that in any window, we keep the most frequent character and replace all others. So if we have 5 characters with max frequency 3, we need to replace 2 characters. This must be <= k for the window to be valid."

**Step 4: Discuss Edge Cases** (30 seconds)
> "Edge cases to consider:
> - k = 0: Only single character substrings valid
> - k >= length: Entire string can be converted
> - All same characters: No replacements needed
> - All different: Need k replacements for k+1 length"

**Step 5: Walk Through Example** (2-3 minutes)
```java
// Example: "AABABBA", k = 1
// Window grows: A → AA → AAB (valid: 3-2=1<=1)
// → AABA (valid: 4-3=1<=1) → AABAB (invalid: 5-3=2>1)
// Shrink: ABAB, then continues...
// Maximum valid window: 4
```

**Step 6: Code** (10-15 minutes)
- Initialize frequency array and pointers
- Loop with right pointer
- Update frequency and maxFreq
- Check validity and shrink if needed
- Track maximum window size
- Test with example

### Expected Follow-up Questions:

**Q**: "What if we had lowercase letters too?"
**A**: "I'd change the array size to 52 (26 + 26) or use a HashMap. Approach stays the same, just different storage. HashMap would be O(distinct characters) space."

**Q**: "Can you optimize further?"
**A**: "O(n) time is optimal - we must scan each character. Space is already O(1). We could add early termination if k >= n-1, but doesn't change worst case."

**Q**: "Why don't we need to recalculate maxFreq when shrinking?"
**A**: "Because we only care about finding the maximum window. If maxFreq was higher in past, that window was already recorded. Current window won't exceed it with lower maxFreq, so keeping historical max is safe."

**Q**: "What about lowercase and uppercase mixed?"
**A**: "If case-insensitive: convert to one case first. If case-sensitive: use HashMap or array of size 52. Logic remains identical."

**Q**: "How would you handle k operations on any character type?"
**A**: "Same approach! Use HashMap for frequency. The window validity formula (size - maxFreq <= k) works for any character set."

---

## Complete Solution with Comments

```java
class Solution {
    /**
     * Finds the length of the longest substring with same characters
     * after at most k replacements.
     * 
     * Approach: Sliding Window with Character Frequency
     * - Maintain a window with frequency count
     * - Window is valid if: window_size - max_frequency <= k
     * - Expand right, shrink left when invalid
     * 
     * @param s Input string (uppercase English letters)
     * @param k Maximum number of character replacements allowed
     * @return Length of longest valid substring
     * 
     * Time Complexity: O(n) - single pass with two pointers
     * Space Complexity: O(1) - fixed array of size 26
     */
    public int characterReplacement(String s, int k) {
        // Frequency count for each character (A-Z)
        int[] count = new int[26];
        
        int maxLength = 0;      // Result: longest valid window
        int maxFreq = 0;        // Max frequency of any char in current window
        int left = 0;           // Left boundary of window
        
        // Expand window with right pointer
        for (int right = 0; right < s.length(); right++) {
            // Add current character to window
            char currentChar = s.charAt(right);
            count[currentChar - 'A']++;
            
            // Update max frequency in current window
            // This tracks the most frequent character count
            maxFreq = Math.max(maxFreq, count[currentChar - 'A']);
            
            // Calculate current window size
            int windowSize = right - left + 1;
            
            // Check if window is valid
            // Valid: replacements needed = windowSize - maxFreq <= k
            // If invalid, we need to shrink window from left
            if (windowSize - maxFreq > k) {
                // Remove leftmost character from window
                count[s.charAt(left) - 'A']--;
                left++;
                // Note: We don't recalculate maxFreq here
                // This is an optimization - keeping historical max is safe
            }
            
            // Update maximum length found
            // Window is guaranteed valid at this point
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
}
```

---

## Alternative Implementation with While Loop

```java
class Solution {
    public int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int maxLength = 0;
        int maxFreq = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            count[s.charAt(right) - 'A']++;
            maxFreq = Math.max(maxFreq, count[s.charAt(right) - 'A']);
            
            // Use while instead of if for shrinking
            // Ensures window is always valid after shrinking
            while (right - left + 1 - maxFreq > k) {
                count[s.charAt(left) - 'A']--;
                left++;
            }
            
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public static void main(String[] args) {
    Solution solution = new Solution();
    
    // Test Case 1: Standard case
    System.out.println(solution.characterReplacement("ABAB", 2));
    // Expected: 4 (replace both A's or both B's)
    
    // Test Case 2: Complex case
    System.out.println(solution.characterReplacement("AABABBA", 1));
    // Expected: 4
    
    // Test Case 3: No replacements
    System.out.println(solution.characterReplacement("ABCD", 0));
    // Expected: 1
    
    // Test Case 4: All same
    System.out.println(solution.characterReplacement("AAAA", 2));
    // Expected: 4
    
    // Test Case 5: Large k
    System.out.println(solution.characterReplacement("ABCD", 3));
    // Expected: 4 (can make all same)
    
    // Test Case 6: Edge case
    System.out.println(solution.characterReplacement("A", 1));
    // Expected: 1
}
```

---

## Key Takeaways

1. ✅ **Window validity**: `size - max_frequency <= k` is the key formula
2. ✅ **Track max frequency** incrementally for O(1) per operation
3. ✅ **Don't recalculate maxFreq** when shrinking (safe optimization)
4. ✅ **Use fixed array** for uppercase letters (O(1) space)
5. ✅ **Shrink window** only when invalid (size - maxFreq > k)
6. ✅ **Both pointers move at most n times** total (amortized O(n))
7. ✅ **Update result** after ensuring window is valid
8. ✅ **Character frequency** is central to this sliding window variant
9. ✅ **Pattern extends** to other character replacement problems
10. ✅ **O(n) time, O(1) space** is optimal for this problem

---

## Variations & Extensions

After mastering this problem, try these related challenges:

1. **Longest Substring with At Most K Distinct Characters** (Medium)
2. **Longest Substring Without Repeating Characters** (Medium)
3. **Minimum Window Substring** (Hard)
4. **Permutation in String** (Medium)
5. **Find All Anagrams in a String** (Medium)

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (30-45 minutes for Medium)
- [ ] Trace through "AABABBA" with k=1 step by step
- [ ] Understand why we don't recalculate maxFreq
- [ ] Test all edge cases
- [ ] Try with different k values
- [ ] Practice explaining the window validity formula
- [ ] Review in 3 days

---

**Pattern Mastered**: Sliding Window with Character Frequency ✅  
**Difficulty**: Medium  
**Time to Master**: 30-45 minutes  
**Key Concept**: Window validity = replacements needed <= k

This problem teaches an important sliding window variant with frequency tracking. Master the window validity check and you'll handle many similar problems! 🚀
