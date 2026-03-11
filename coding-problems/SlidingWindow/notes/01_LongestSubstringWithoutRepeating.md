# Longest Substring Without Repeating Characters (Medium)

## Problem Statement
Given a string `s`, find the length of the **longest substring** without repeating characters.

A **substring** is a contiguous sequence of characters within a string.

**LeetCode Link**: [3. Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/)

---

## Examples

### Example 1:
```
Input: s = "abcabcbb"
Output: 3
Explanation: The longest substring without repeating characters is "abc", with length 3.

Visual:
"abcabcbb"
 ^^^       → "abc" (length 3) ✓
   ^^^     → "cab" (has 'c' and 'a' from earlier) 
    ^^     → "ab" (length 2)
```

### Example 2:
```
Input: s = "bbbbb"
Output: 1
Explanation: The longest substring is "b", with length 1.

Visual:
"bbbbb"
 ^     → "b" (length 1) ✓
  ^    → "b" (length 1)
All substrings are single character.
```

### Example 3:
```
Input: s = "pwwkew"
Output: 3
Explanation: The longest substring is "wke", with length 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.

Visual:
"pwwkew"
 ^^     → "pw" (length 2)
  ^     → "w" (duplicate at index 2)
   ^^^  → "wke" (length 3) ✓
    ^^  → "ke" (length 2)
```

### Example 4:
```
Input: s = "abcdefg"
Output: 7
Explanation: The entire string has no repeating characters.
```

### Example 5:
```
Input: s = "tmmzuxt"
Output: 5
Explanation: "mzuxt" has length 5, no repeating characters.
```

---

## Constraints
- `0 <= s.length <= 5 * 10^4`
- `s` consists of English letters, digits, symbols and spaces

---

## Pattern Recognition

This is a **Sliding Window (Variable Size)** problem because:
1. We need to find a **contiguous substring** (window)
2. Window size **changes dynamically** based on condition
3. We **expand** window by moving right pointer
4. We **shrink** window when duplicate found (move left pointer)
5. Track **maximum window size** seen

**Key Insight**: 
- Use HashMap/Set to track characters in current window
- Expand right pointer to add new characters
- When duplicate found, shrink from left until no duplicates
- Update maximum length after each valid window

**Why Sliding Window**:
```
Instead of checking all O(n²) substrings:
  - Maintain a window with no duplicates
  - Expand when safe, shrink when duplicate
  - Each character added/removed at most once
  - O(n) time complexity
```

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: Check all possible substrings and validate each for duplicates.

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        int maxLen = 0;
        
        // Try all starting positions
        for (int i = 0; i < s.length(); i++) {
            // Try all ending positions from i
            for (int j = i; j < s.length(); j++) {
                // Check if substring s[i..j] has duplicates
                if (allUnique(s, i, j)) {
                    maxLen = Math.max(maxLen, j - i + 1);
                }
            }
        }
        
        return maxLen;
    }
    
    private boolean allUnique(String s, int start, int end) {
        Set<Character> set = new HashSet<>();
        for (int i = start; i <= end; i++) {
            char c = s.charAt(i);
            if (set.contains(c)) {
                return false;
            }
            set.add(c);
        }
        return true;
    }
}
```

**Time Complexity**: O(n³) - O(n²) substrings × O(n) validation each
**Space Complexity**: O(min(n, m)) - set for validation, m = charset size
**Problem**: Way too slow! Will timeout.

---

### Approach 2: Sliding Window with HashSet (GOOD) ⭐
**Idea**: Use set to track characters in current window, shrink when duplicate found.

```java
import java.util.*;

class Solution {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> window = new HashSet<>();
        int maxLen = 0;
        int left = 0;
        
        // Expand window with right pointer
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            // Shrink window until no duplicate
            while (window.contains(rightChar)) {
                window.remove(s.charAt(left));
                left++;
            }
            
            // Add current character to window
            window.add(rightChar);
            
            // Update maximum length
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
}
```

**Time Complexity**: O(n) - each character added and removed at most once
**Space Complexity**: O(min(n, m)) - set size limited by string length or charset
**Why Good**: Simple, clean, optimal time complexity

---

### Approach 3: Sliding Window with HashMap (OPTIMAL) ⭐⭐
**Idea**: Store character indices, jump left pointer directly to after duplicate.

```java
import java.util.*;

class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> charIndex = new HashMap<>();
        int maxLen = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            // If character seen before and in current window
            if (charIndex.containsKey(rightChar)) {
                // Jump left pointer to after the duplicate
                left = Math.max(left, charIndex.get(rightChar) + 1);
            }
            
            // Update character's latest index
            charIndex.put(rightChar, right);
            
            // Update maximum length
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
}
```

**Time Complexity**: O(n) - single pass through string
**Space Complexity**: O(min(n, m)) - map size limited by charset
**Why Optimal**: Fewer operations, no while loop for shrinking

---

### Approach 4: Optimized with Array (For Limited Charset)
**Idea**: Use array instead of HashMap for ASCII characters.

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        // Array for ASCII characters (256 possible)
        int[] charIndex = new int[256];
        Arrays.fill(charIndex, -1);
        
        int maxLen = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            // If character seen in current window
            if (charIndex[rightChar] >= left) {
                left = charIndex[rightChar] + 1;
            }
            
            // Update character's latest index
            charIndex[rightChar] = right;
            
            // Update maximum length
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(1) - fixed array size (256)
**Note**: Only works for ASCII; use HashMap for Unicode

---

## Detailed Walkthrough (Approach 3: HashMap)

### Example: s = "abcabcbb"

```
Step 0: Initialize
maxLen = 0
left = 0
charIndex = {}

String: "abcabcbb"
         L
         R
         0


Step 1: Process 'a' at index 0
right = 0, rightChar = 'a'
  - 'a' not in map
  - Add: charIndex = {'a': 0}
  - maxLen = max(0, 0 - 0 + 1) = 1
  - Window: "a"

String: "abcabcbb"
         LR
         0


Step 2: Process 'b' at index 1
right = 1, rightChar = 'b'
  - 'b' not in map
  - Add: charIndex = {'a': 0, 'b': 1}
  - maxLen = max(1, 1 - 0 + 1) = 2
  - Window: "ab"

String: "abcabcbb"
         L R
         0 1


Step 3: Process 'c' at index 2
right = 2, rightChar = 'c'
  - 'c' not in map
  - Add: charIndex = {'a': 0, 'b': 1, 'c': 2}
  - maxLen = max(2, 2 - 0 + 1) = 3
  - Window: "abc"

String: "abcabcbb"
         L   R
         0   2


Step 4: Process 'a' at index 3
right = 3, rightChar = 'a'
  - 'a' in map at index 0!
  - Duplicate found!
  - left = max(0, 0 + 1) = 1
  - Update: charIndex['a'] = 3
  - maxLen = max(3, 3 - 1 + 1) = 3
  - Window: "bca"

String: "abcabcbb"
          L   R
          1   3

charIndex = {'a': 3, 'b': 1, 'c': 2}


Step 5: Process 'b' at index 4
right = 4, rightChar = 'b'
  - 'b' in map at index 1!
  - Duplicate found!
  - left = max(1, 1 + 1) = 2
  - Update: charIndex['b'] = 4
  - maxLen = max(3, 4 - 2 + 1) = 3
  - Window: "cab"

String: "abcabcbb"
            L   R
            2   4

charIndex = {'a': 3, 'b': 4, 'c': 2}


Step 6: Process 'c' at index 5
right = 5, rightChar = 'c'
  - 'c' in map at index 2!
  - Duplicate found!
  - left = max(2, 2 + 1) = 3
  - Update: charIndex['c'] = 5
  - maxLen = max(3, 5 - 3 + 1) = 3
  - Window: "abc"

String: "abcabcbb"
               L  R
               3  5

charIndex = {'a': 3, 'b': 4, 'c': 5}


Step 7: Process 'b' at index 6
right = 6, rightChar = 'b'
  - 'b' in map at index 4!
  - Duplicate found!
  - left = max(3, 4 + 1) = 5
  - Update: charIndex['b'] = 6
  - maxLen = max(3, 6 - 5 + 1) = 2
  - Window: "cb"

String: "abcabcbb"
                  LR
                  56

charIndex = {'a': 3, 'b': 6, 'c': 5}


Step 8: Process 'b' at index 7
right = 7, rightChar = 'b'
  - 'b' in map at index 6!
  - Duplicate found!
  - left = max(5, 6 + 1) = 7
  - Update: charIndex['b'] = 7
  - maxLen = max(3, 7 - 7 + 1) = 1
  - Window: "b"

String: "abcabcbb"
                   LR
                   77

charIndex = {'a': 3, 'b': 7, 'c': 5}


Final Answer: maxLen = 3
```

---

### Visual Representation

```
String: "abcabcbb"

Window progression:
Step 1: [a]bcabcbb         → length 1
Step 2: [ab]cabcbb         → length 2
Step 3: [abc]abcbb         → length 3 ✓ maxLen = 3
Step 4: a[bca]bcbb         → length 3 (skip 'a')
Step 5: ab[cab]cbb         → length 3 (skip 'b')
Step 6: abc[abc]bb         → length 3 (skip 'c')
Step 7: abcab[cb]b         → length 2 (skip 'b')
Step 8: abcabc[b]b         → length 1 (skip 'b')

Maximum length found: 3 ("abc")
```

---

### Why HashMap Jump is Efficient

```
Without HashMap (HashSet approach):
"abcabcbb"
 abc      → Find duplicate 'a', shrink one by one
 ^  ^
 L  R

Remove 'a': left = 1
 bc       → Still need to check
  ^  ^
  L  R

With HashMap (direct jump):
"abcabcbb"
 abc      → Find duplicate 'a' at index 0
 ^  ^
 L  R

Jump directly: left = 0 + 1 = 1
  bc       → Done in one operation!
   ^ ^
   L R

Saved operations: No need to shrink one by one!
```

---

## Edge Cases to Consider

```java
// Test Case 1: Empty string
Input: s = ""
Output: 0
// No characters, length is 0

// Test Case 2: Single character
Input: s = "a"
Output: 1
// Entire string is the answer

// Test Case 3: All same characters
Input: s = "aaaa"
Output: 1
// Maximum is single character

// Test Case 4: All unique characters
Input: s = "abcdefgh"
Output: 8
// Entire string has no duplicates

// Test Case 5: Duplicate at start
Input: s = "aab"
Output: 2
// "ab" is the longest

// Test Case 6: Duplicate at end
Input: s = "abc"
Output: 3
// Entire string

// Test Case 7: Space in string
Input: s = "a b a"
Output: 3
// "a b" or "b a", both length 3 (space counts!)

// Test Case 8: Special characters
Input: s = "a!b@c#a"
Output: 6
// "!b@c#a" length 6

// Test Case 9: Numbers
Input: s = "12321"
Output: 3
// "123" or "321", length 3

// Test Case 10: Long string with repeating pattern
Input: s = "abcabcabcabc"
Output: 3
// "abc" repeats, max is 3

// Test Case 11: Two characters alternating
Input: s = "abababab"
Output: 2
// "ab" repeated

// Test Case 12: Duplicate far apart
Input: s = "abcdefga"
Output: 7
// "bcdefga" after first 'a'
```

---

## Common Mistakes to Avoid

### Mistake 1: Not Handling Left Pointer Correctly
```java
// ❌ WRONG: Always jumping to duplicate + 1
if (charIndex.containsKey(rightChar)) {
    left = charIndex.get(rightChar) + 1;  // May go backwards!
}

// Example: "abba"
// When at second 'b', left should stay at 2, not jump to 1+1=2
// But what if we saw 'a' before current window?

// ✅ CORRECT: Use Math.max to prevent going backwards
if (charIndex.containsKey(rightChar)) {
    left = Math.max(left, charIndex.get(rightChar) + 1);
}
```

### Mistake 2: Updating Map Before Checking
```java
// ❌ WRONG: Update index before using it
charIndex.put(rightChar, right);
if (charIndex.containsKey(rightChar)) {
    left = Math.max(left, charIndex.get(rightChar) + 1);
    // Will always find character (just added it!)
}

// ✅ CORRECT: Check first, then update
if (charIndex.containsKey(rightChar)) {
    left = Math.max(left, charIndex.get(rightChar) + 1);
}
charIndex.put(rightChar, right);
```

### Mistake 3: Wrong Window Length Calculation
```java
// ❌ WRONG: Forgetting +1
int windowLen = right - left;  // Off by one!

// ✅ CORRECT: Include both endpoints
int windowLen = right - left + 1;

// Example: right=2, left=0 → "abc" has length 3, not 2
```

### Mistake 4: Not Handling Empty String
```java
// ❌ WRONG: No empty check
public int lengthOfLongestSubstring(String s) {
    // If s is empty, should return 0
    // Code might work but worth checking explicitly
}

// ✅ CORRECT: Handle edge case
public int lengthOfLongestSubstring(String s) {
    if (s == null || s.length() == 0) {
        return 0;
    }
    // Rest of code
}
```

### Mistake 5: Using Wrong Data Structure
```java
// ❌ INEFFICIENT: Using List
List<Character> window = new ArrayList<>();
if (window.contains(rightChar)) {  // O(n) lookup!
    // Very slow
}

// ✅ EFFICIENT: Using Set or Map
Set<Character> window = new HashSet<>();
if (window.contains(rightChar)) {  // O(1) lookup!
    // Fast
}
```

### Mistake 6: Not Removing from Set Properly (HashSet approach)
```java
// ❌ WRONG: Remove wrong character
while (window.contains(rightChar)) {
    window.remove(rightChar);  // Removes the duplicate immediately!
    left++;
}

// ✅ CORRECT: Remove character at left pointer
while (window.contains(rightChar)) {
    window.remove(s.charAt(left));  // Remove from left
    left++;
}
```

---

## Why Sliding Window is Essential

### Without Sliding Window (Brute Force):
```java
for (int i = 0; i < n; i++) {
    for (int j = i; j < n; j++) {
        if (allUnique(i, j)) {  // O(n) validation
            // Update max
        }
    }
}
// O(n³) - check all n² substrings, O(n) each
```

### With Sliding Window:
```java
for (int right = 0; right < n; right++) {
    // Expand window
    while (duplicate) {
        // Shrink window
        left++;
    }
    // Update max
}
// O(n) - each character added and removed once
```

**Sliding Window Enables:**
1. ✅ **Incremental validation** (no need to recheck entire window)
2. ✅ **Reuse computation** (maintain state in set/map)
3. ✅ **Linear time** O(n) instead of cubic O(n³)
4. ✅ **Natural shrink/expand** logic

**Key Insight**:
```
When we move right:
  - Add one character
  - If duplicate, remove from left until no duplicate
  - Each character processed at most twice (added once, removed once)
  - Total: O(2n) = O(n)
```

---

## Optimization Techniques

### Optimization 1: Early Termination
```java
public int lengthOfLongestSubstring(String s) {
    if (s == null || s.length() == 0) return 0;
    
    Map<Character, Integer> charIndex = new HashMap<>();
    int maxLen = 0;
    int left = 0;
    int n = s.length();
    
    for (int right = 0; right < n; right++) {
        char rightChar = s.charAt(right);
        
        if (charIndex.containsKey(rightChar)) {
            left = Math.max(left, charIndex.get(rightChar) + 1);
        }
        
        charIndex.put(rightChar, right);
        maxLen = Math.max(maxLen, right - left + 1);
        
        // Early termination: if remaining characters can't improve
        if (maxLen >= n - left) {
            break;  // Can't get longer than remaining string
        }
    }
    
    return maxLen;
}
```

### Optimization 2: Use Array for ASCII
```java
public int lengthOfLongestSubstring(String s) {
    // For ASCII only - faster than HashMap
    int[] lastIndex = new int[128];  // ASCII characters
    Arrays.fill(lastIndex, -1);
    
    int maxLen = 0;
    int left = 0;
    
    for (int right = 0; right < s.length(); right++) {
        char c = s.charAt(right);
        
        // If seen in current window
        if (lastIndex[c] >= left) {
            left = lastIndex[c] + 1;
        }
        
        lastIndex[c] = right;
        maxLen = Math.max(maxLen, right - left + 1);
    }
    
    return maxLen;
}
```

### Optimization 3: Avoid Repeated charAt Calls
```java
public int lengthOfLongestSubstring(String s) {
    if (s == null || s.length() == 0) return 0;
    
    // Convert to char array for faster access
    char[] chars = s.toCharArray();
    Map<Character, Integer> charIndex = new HashMap<>();
    int maxLen = 0;
    int left = 0;
    
    for (int right = 0; right < chars.length; right++) {
        char rightChar = chars[right];  // Direct array access
        
        if (charIndex.containsKey(rightChar)) {
            left = Math.max(left, charIndex.get(rightChar) + 1);
        }
        
        charIndex.put(rightChar, right);
        maxLen = Math.max(maxLen, right - left + 1);
    }
    
    return maxLen;
}
```

---

## Complexity Analysis

### Time Complexity: O(n)
- **Single pass**: Right pointer moves from 0 to n-1
- **Left pointer**: Moves at most n times total
- **HashMap operations**: O(1) for put and get
- **Total**: O(n) where n = length of string

### Space Complexity: O(min(n, m))
- **HashMap/Set**: Stores at most min(n, m) characters
  - n = length of string
  - m = size of character set (26 for lowercase, 128 for ASCII, etc.)
- **Worst case**: O(n) if all characters unique
- **Best case with array**: O(1) for fixed charset (e.g., 128 for ASCII)

### Comparison of Approaches:

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Brute Force | O(n³) | O(min(n,m)) | Check all substrings, TLE |
| Sliding Window + Set | O(n) | O(min(n,m)) | Clean, two passes per char |
| Sliding Window + HashMap | O(n) | O(min(n,m)) | ✅ Optimal, one pass |
| Array (ASCII only) | O(n) | O(1) | Fastest, limited charset |

### Detailed Analysis for n = 50,000 (max):
```
Brute Force:
  Substrings: n²/2 = 1.25 × 10^9
  Validation: O(n) each
  Total: ~10^14 operations (way too slow!)

Sliding Window:
  Operations: 2n = 100,000
  HashMap ops: O(1) each
  Total: ~100,000 operations (fast!)
  
Speedup: ~10^9 times faster!
```

---

## Pattern Connection: Similar Sliding Window Problems

### Longest Substring Without Repeating (This Problem):
```java
// Variable window, shrink when duplicate found
Set<Character> window = new HashSet<>();
for (int right = 0; right < n; right++) {
    while (window.contains(s.charAt(right))) {
        window.remove(s.charAt(left++));
    }
    window.add(s.charAt(right));
    maxLen = Math.max(maxLen, right - left + 1);
}
```

### Longest Repeating Character Replacement:
```java
// Variable window with replacement budget
int[] count = new int[26];
int maxCount = 0;
for (int right = 0; right < n; right++) {
    maxCount = Math.max(maxCount, ++count[s.charAt(right) - 'A']);
    // Shrink if replacements > k
    if (right - left + 1 - maxCount > k) {
        count[s.charAt(left++) - 'A']--;
    }
    maxLen = Math.max(maxLen, right - left + 1);
}
```

### Minimum Window Substring:
```java
// Variable window to find minimum containing all characters
Map<Character, Integer> need = new HashMap<>();
for (char c : t.toCharArray()) {
    need.put(c, need.getOrDefault(c, 0) + 1);
}
// Expand to satisfy, shrink to minimize
```

**Common Pattern**:
- Maintain a window with two pointers
- Expand right to include more elements
- Shrink left when condition violated or to optimize
- Track result during traversal

---

## Interview Tips

### What to Say During Interview:

**Step 1: Understand the Problem** (1 minute)
> "We need to find the longest contiguous substring with all unique characters. Key points: substring means contiguous, and we want the maximum length."

**Step 2: Clarify Constraints** (30 seconds)
> "The string can contain any ASCII characters including spaces and symbols. Empty string returns 0. String length up to 50,000."

**Step 3: Discuss Brute Force** (1 minute)
> "Brute force would check all O(n²) substrings and validate each for duplicates in O(n), giving O(n³) total. That's too slow for n=50,000."

**Step 4: Explain Sliding Window Approach** (2 minutes)
> "I'll use a sliding window with two pointers:
> - Right pointer expands the window, adding characters
> - When we encounter a duplicate, we shrink from the left
> - Use HashMap to track the last index of each character
> - This allows us to jump the left pointer directly to after the duplicate
> - Track the maximum window size seen
> Time complexity: O(n), Space: O(min(n, charset size))"

**Step 5: Walk Through Example** (2-3 minutes)
```
s = "abcabcbb"

Window: "abc" → length 3
Found duplicate 'a' at index 3
Jump left to index 1
Window: "bca" → length 3

Continue shrinking and expanding...
Maximum: 3
```

**Step 6: Mention Complexity** (30 seconds)
> "Time: O(n) - each character visited at most twice (once by right, once by left). Space: O(min(n,m)) where m is charset size, for the HashMap."

**Step 7: Code** (10-12 minutes)
- Initialize HashMap and pointers
- Loop with right pointer
- Check for duplicates and update left
- Update HashMap with current index
- Track maximum length

### Expected Follow-up Questions:

**Q**: "Can you do it without the HashMap?"
**A**: "Yes, with a HashSet. We'd need a while loop to shrink the window character by character instead of jumping directly. Same O(n) time but slightly more operations."

**Q**: "What if the string only contains lowercase letters?"
**A**: "We could use an int array of size 26 instead of HashMap, which is faster. Space becomes O(1). For ASCII, use array of size 128."

**Q**: "How would you modify this to find all longest substrings?"
**A**: "Track all substrings that achieve the maximum length. Store them in a list when we find a window matching maxLen. Space becomes O(n × k) where k is number of such substrings."

**Q**: "What if we want to allow k duplicate characters?"
**A**: "That's the 'Longest Substring with At Most K Distinct Characters' problem. Similar approach but track count of distinct characters instead of checking for any duplicates."

**Q**: "Can you optimize the space complexity?"
**A**: "If we know the charset is limited (like ASCII), we can use a fixed-size array instead of HashMap, making space O(1). Otherwise, O(min(n,m)) is optimal."

**Q**: "How do you handle Unicode characters?"
**A**: "HashMap handles Unicode naturally. If using array, we'd need a much larger array or stick with HashMap, which is the cleaner solution for Unicode."

---

## Complete Solution with Comments

```java
import java.util.*;

class Solution {
    /**
     * Finds the length of the longest substring without repeating characters.
     * 
     * Uses sliding window with HashMap to track character indices.
     * When duplicate found, jump left pointer to after the previous occurrence.
     * 
     * @param s Input string
     * @return Length of longest substring with unique characters
     * 
     * Time Complexity: O(n) - single pass through string
     * Space Complexity: O(min(n, m)) - HashMap size limited by string length or charset
     */
    public int lengthOfLongestSubstring(String s) {
        // Edge case: empty or null string
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // Map to store the last seen index of each character
        Map<Character, Integer> charIndex = new HashMap<>();
        
        int maxLen = 0;    // Maximum length found so far
        int left = 0;      // Left pointer of sliding window
        
        // Expand window with right pointer
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            // If character was seen before and is in current window
            if (charIndex.containsKey(rightChar)) {
                // Move left pointer to after the previous occurrence
                // Use Math.max to prevent left from going backwards
                // (in case the previous occurrence was before current window)
                left = Math.max(left, charIndex.get(rightChar) + 1);
            }
            
            // Update the character's latest index
            charIndex.put(rightChar, right);
            
            // Calculate current window size and update maximum
            int currentLen = right - left + 1;
            maxLen = Math.max(maxLen, currentLen);
        }
        
        return maxLen;
    }
}
```

### Alternative Implementation with HashSet:

```java
import java.util.*;

class Solution {
    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        // Set to track characters in current window
        Set<Character> window = new HashSet<>();
        int maxLen = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            // Shrink window from left until no duplicate
            while (window.contains(rightChar)) {
                window.remove(s.charAt(left));
                left++;
            }
            
            // Add current character to window
            window.add(rightChar);
            
            // Update maximum length
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
}
```

### Optimized Implementation with Array (ASCII Only):

```java
class Solution {
    public int lengthOfLongestSubstring(String s) {
        // Array to store last index of each ASCII character
        int[] lastIndex = new int[128];
        Arrays.fill(lastIndex, -1);
        
        int maxLen = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            
            // If character was seen in current window
            if (lastIndex[c] >= left) {
                left = lastIndex[c] + 1;
            }
            
            // Update last index
            lastIndex[c] = right;
            
            // Update maximum length
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> charIndex = new HashMap<>();
        int maxLen = 0;
        int left = 0;
        
        for (int right = 0; right < s.length(); right++) {
            char rightChar = s.charAt(right);
            
            if (charIndex.containsKey(rightChar)) {
                left = Math.max(left, charIndex.get(rightChar) + 1);
            }
            
            charIndex.put(rightChar, right);
            maxLen = Math.max(maxLen, right - left + 1);
        }
        
        return maxLen;
    }
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // Test Case 1: Standard case
        System.out.println("Test 1: " + solution.lengthOfLongestSubstring("abcabcbb"));
        // Expected: 3 ("abc")
        
        // Test Case 2: All same
        System.out.println("Test 2: " + solution.lengthOfLongestSubstring("bbbbb"));
        // Expected: 1
        
        // Test Case 3: Mixed
        System.out.println("Test 3: " + solution.lengthOfLongestSubstring("pwwkew"));
        // Expected: 3 ("wke")
        
        // Test Case 4: Empty
        System.out.println("Test 4: " + solution.lengthOfLongestSubstring(""));
        // Expected: 0
        
        // Test Case 5: All unique
        System.out.println("Test 5: " + solution.lengthOfLongestSubstring("abcdef"));
        // Expected: 6
        
        // Test Case 6: With spaces
        System.out.println("Test 6: " + solution.lengthOfLongestSubstring("a b a"));
        // Expected: 3
        
        // Test Case 7: Single character
        System.out.println("Test 7: " + solution.lengthOfLongestSubstring("a"));
        // Expected: 1
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
        assertEquals(3, solution.lengthOfLongestSubstring("abcabcbb"));
    }
    
    @Test
    public void testAllSame() {
        assertEquals(1, solution.lengthOfLongestSubstring("bbbbb"));
    }
    
    @Test
    public void testMixed() {
        assertEquals(3, solution.lengthOfLongestSubstring("pwwkew"));
    }
    
    @Test
    public void testEmptyString() {
        assertEquals(0, solution.lengthOfLongestSubstring(""));
    }
    
    @Test
    public void testAllUnique() {
        assertEquals(6, solution.lengthOfLongestSubstring("abcdef"));
    }
    
    @Test
    public void testWithSpaces() {
        assertEquals(3, solution.lengthOfLongestSubstring("a b a"));
    }
    
    @Test
    public void testSingleCharacter() {
        assertEquals(1, solution.lengthOfLongestSubstring("a"));
    }
}
```

---

## Key Takeaways

1. ✅ **Sliding window** with variable size for substring problems
2. ✅ **HashMap stores indices** for direct left pointer jump
3. ✅ **Math.max prevents backwards movement** of left pointer
4. ✅ **Each character processed twice max** (added once, removed once)
5. ✅ **O(n) time complexity** optimal for this problem
6. ✅ **HashSet works too** but slightly more operations
7. ✅ **Array optimization** possible for limited charsets (ASCII)
8. ✅ **Window length formula**: right - left + 1
9. ✅ **Update map after checking** for duplicates
10. ✅ **Pattern applies to many substring problems** with variations

---

## Variations & Extensions

After mastering this problem, try these related challenges:

1. **Longest Substring with At Most K Distinct Characters** (Medium) - Track distinct count
2. **Longest Repeating Character Replacement** (Medium) - Allow k replacements
3. **Minimum Window Substring** (Hard) - Find minimum window containing all chars
4. **Permutation in String** (Medium) - Check if window is permutation
5. **Find All Anagrams in a String** (Medium) - Find all anagram windows
6. **Longest Substring with At Most Two Distinct Characters** (Medium) - K=2 version

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (30-40 minutes for Medium)
- [ ] Trace through "abcabcbb" manually
- [ ] Implement both HashMap and HashSet versions
- [ ] Test with edge cases (empty, single char, all unique)
- [ ] Explain the sliding window pattern out loud
- [ ] Compare with other sliding window problems
- [ ] Review in 3 days for retention
- [ ] Ready for Longest Repeating Character Replacement!

---

**Pattern Mastered**: Sliding Window (Variable Size) ✅  
**Difficulty**: Medium  
**Time to Master**: 30-40 minutes  
**Core Concept**: Expand right, shrink left when duplicate

This problem is a classic sliding window application! The key insight is using HashMap to jump the left pointer directly, avoiding unnecessary shrinking operations. Master this pattern - it appears in many interview questions! 🚀
