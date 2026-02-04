# Valid Anagram (Easy)

## Problem Statement
Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, and return `false` otherwise.

An **anagram** is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

**LeetCode Link**: [242. Valid Anagram](https://leetcode.com/problems/valid-anagram/)

---

## Examples

### Example 1:
```
Input: s = "anagram", t = "nagaram"
Output: true
Explanation: Both strings have the same characters with same frequencies:
  a: 3, n: 1, g: 1, r: 1, m: 1
```

### Example 2:
```
Input: s = "rat", t = "car"
Output: false
Explanation: Different characters (rat has 't', car has 'c')
```

### Example 3:
```
Input: s = "listen", t = "silent"
Output: true
Explanation: Both strings have the same characters: l, i, s, t, e, n
```

---

## Constraints
- `1 <= s.length, t.length <= 5 * 10^4`
- `s` and `t` consist of lowercase English letters

---

## Pattern Recognition

This is a **Frequency Counter pattern** problem because:
1. We need to **count occurrences** of each character
2. We need to **compare frequencies** between two collections
3. Order doesn't matter - only the frequency of each character
4. We need O(1) lookup for character counts

**Key Insight**: Two strings are anagrams if and only if they have the exact same character frequencies. We can count characters in one string and verify against the other.

**Mental Model**: Think of it like having two bags of Scrabble tiles. They're anagrams if both bags contain exactly the same tiles (same letters, same counts).

---

## Approaches

### Approach 1: Sorting
**Idea**: If two strings are anagrams, they will be identical after sorting.

```java
import java.util.Arrays;

class Solution {
    public boolean isAnagram(String s, String t) {
        // Early exit: different lengths can't be anagrams
        if (s.length() != t.length()) {
            return false;
        }
        
        // Convert to char arrays
        char[] sChars = s.toCharArray();
        char[] tChars = t.toCharArray();
        
        // Sort both arrays
        Arrays.sort(sChars);
        Arrays.sort(tChars);
        
        // Compare sorted arrays
        return Arrays.equals(sChars, tChars);
    }
}
```

**Time Complexity**: O(n log n) - sorting dominates
**Space Complexity**: O(n) - char arrays created from strings
**Pros**: Simple, concise, easy to understand
**Cons**: Not optimal time complexity

---

### Approach 2: HashMap Frequency Counter
**Idea**: Count character frequencies using HashMap, then verify.

```java
import java.util.HashMap;

class Solution {
    public boolean isAnagram(String s, String t) {
        // Early exit: different lengths can't be anagrams
        if (s.length() != t.length()) {
            return false;
        }
        
        // Step 1: Count frequencies in first string
        HashMap<Character, Integer> charCount = new HashMap<>();
        
        for (char c : s.toCharArray()) {
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        
        // Step 2: Decrement frequencies using second string
        for (char c : t.toCharArray()) {
            // If character not in map, can't be anagram
            if (!charCount.containsKey(c)) {
                return false;
            }
            
            // Decrement count
            charCount.put(c, charCount.get(c) - 1);
            
            // If count goes negative, t has more of this char than s
            if (charCount.get(c) < 0) {
                return false;
            }
        }
        
        // Step 3: Verify all counts are zero
        // (Actually not needed if lengths are equal and no negative counts)
        for (int count : charCount.values()) {
            if (count != 0) {
                return false;
            }
        }
        
        return true;
    }
}
```

**Time Complexity**: O(n) - three linear passes
**Space Complexity**: O(k) where k = number of unique characters (max 26 for lowercase)
**Pros**: Optimal time complexity, flexible for Unicode
**Cons**: HashMap overhead, more verbose than needed for lowercase letters

---

### Approach 3: Array Frequency Counter (OPTIMAL ⭐)
**Idea**: Use a fixed-size array for counting since we only have 26 lowercase letters.

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        // Early exit: different lengths can't be anagrams
        if (s.length() != t.length()) {
            return false;
        }
        
        // Frequency array for 26 lowercase letters
        int[] count = new int[26];
        
        // Single pass: increment for s, decrement for t
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;  // Count characters in s
            count[t.charAt(i) - 'a']--;  // Decount characters in t
        }
        
        // Check if all frequencies are zero
        for (int freq : count) {
            if (freq != 0) {
                return false;
            }
        }
        
        return true;
    }
}
```

**Time Complexity**: O(n) - single pass through strings + O(26) check = O(n)
**Space Complexity**: O(1) - fixed array of size 26
**Pros**: Most efficient, simple, minimal space
**Cons**: Only works for known character set (lowercase a-z)

---

## Deep Dive: Why the Array Approach Works

### Character to Index Mapping
```java
'a' - 'a' = 0
'b' - 'a' = 1
'c' - 'a' = 2
...
'z' - 'a' = 25
```

This ASCII arithmetic maps each character to a unique index in our array.

### The Counting Logic
```
For s = "anagram", t = "nagaram"

Initial: count = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
         Index:  a b c d e f g h i j k l m n o p q r s t u v w x y z

Process i=0:
  s[0]='a' → count[0]++ → count[0] = 1
  t[0]='n' → count[13]-- → count[13] = -1
  
Process i=1:
  s[1]='n' → count[13]++ → count[13] = 0
  t[1]='a' → count[0]-- → count[0] = 0

Process i=2:
  s[2]='a' → count[0]++ → count[0] = 1
  t[2]='g' → count[6]-- → count[6] = -1

... and so on ...

Final count = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]
All zeros → they're anagrams!
```

**Why this works**: 
- Each character in `s` adds +1 to its position
- Each character in `t` subtracts -1 from its position
- If they're anagrams, every +1 is canceled by a -1
- Result: all zeros

---

## Detailed Walkthrough: Step by Step

Let's trace `s = "rat"`, `t = "car"` (should return false)

```java
// Initial state
s.length() = 3, t.length() = 3  ✓ lengths equal
count = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]

// i = 0
s.charAt(0) = 'r' → index = 'r' - 'a' = 17
  count[17]++ → count[17] = 1
t.charAt(0) = 'c' → index = 'c' - 'a' = 2
  count[2]-- → count[2] = -1

// i = 1
s.charAt(1) = 'a' → index = 'a' - 'a' = 0
  count[0]++ → count[0] = 1
t.charAt(1) = 'a' → index = 'a' - 'a' = 0
  count[0]-- → count[0] = 0

// i = 2
s.charAt(2) = 't' → index = 't' - 'a' = 19
  count[19]++ → count[19] = 1
t.charAt(2) = 'r' → index = 'r' - 'a' = 17
  count[17]-- → count[17] = 0

// Final check
count[2] = -1 (not zero!) → return false ✓
```

**Explanation**: 
- `s` has 't' but `t` has 'c' → count[19] = 1 (extra 't')
- `t` has 'c' but `s` doesn't → count[2] = -1 (missing 'c')
- Not all zeros → NOT anagrams

---

## Alternative: Two-Pass Array Approach

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        int[] count = new int[26];
        
        // Pass 1: Count characters in s
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        // Pass 2: Decrement using t
        for (char c : t.toCharArray()) {
            count[c - 'a']--;
            // Early exit optimization
            if (count[c - 'a'] < 0) {
                return false;
            }
        }
        
        // All counts should be zero
        // (Actually guaranteed if lengths equal and no negatives)
        return true;
    }
}
```

**Difference**: Counts first, then decrements. Same complexity, slightly different logic flow.

---

## Edge Cases to Consider

```java
// Test case 1: Empty strings
s = "", t = ""
→ true (both empty are anagrams)

// Test case 2: Single character match
s = "a", t = "a"
→ true

// Test case 3: Single character mismatch
s = "a", t = "b"
→ false

// Test case 4: Different lengths (handled early)
s = "abc", t = "ab"
→ false (caught by length check)

// Test case 5: Same characters, different counts
s = "aab", t = "abb"
→ false (count[0]=1, count[1]=-1)

// Test case 6: All same character
s = "aaaa", t = "aaaa"
→ true

// Test case 7: Long strings with one difference
s = "aaaaaaaaaaaaaaaaaaaaaaaab", t = "aaaaaaaaaaaaaaaaaaaaaaaac"
→ false (count[1]=1, count[2]=-1)
```

---

## Common Mistakes to Avoid

### Mistake 1: Not checking length first
```java
// ❌ WRONG - wastes time if lengths differ
public boolean isAnagram(String s, String t) {
    int[] count = new int[26];
    for (char c : s.toCharArray()) count[c - 'a']++;
    for (char c : t.toCharArray()) count[c - 'a']--;
    // ... continue even though lengths might differ
}

// ✅ CORRECT - early exit
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    // ... continue with counting
}
```

### Mistake 2: Forgetting the final check
```java
// ❌ WRONG - assumes all zeros without checking
for (int i = 0; i < s.length(); i++) {
    count[s.charAt(i) - 'a']++;
    count[t.charAt(i) - 'a']--;
}
return true;  // ❌ Should check if all counts are zero!

// ✅ CORRECT
for (int freq : count) {
    if (freq != 0) return false;
}
return true;
```

### Mistake 3: Wrong index calculation
```java
// ❌ WRONG - forgot to subtract 'a'
count[s.charAt(i)]++;  // This gives ASCII value, not 0-25!

// ✅ CORRECT
count[s.charAt(i) - 'a']++;
```

---

## Follow-up Questions

### Q1: What if inputs contain Unicode characters?
**Answer**: Use HashMap instead of array since we can't predict the character set.

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        
        HashMap<Character, Integer> count = new HashMap<>();
        
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }
        
        for (char c : t.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) - 1);
            if (count.get(c) < 0) return false;
        }
        
        return true;
    }
}
```

### Q2: What if strings are very long but only a few unique characters?
**Answer**: Array approach is still optimal for lowercase letters. For other cases, HashMap with early exit on negative count is best.

### Q3: Can we do it in one pass without the final check?
**Answer**: Yes! With equal lengths and checking for negatives during decrement:

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        
        int[] count = new int[26];
        
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        for (char c : t.toCharArray()) {
            count[c - 'a']--;
            if (count[c - 'a'] < 0) return false;  // Early exit!
        }
        
        return true;  // No final check needed!
    }
}
```

If lengths are equal and no count went negative, all counts must be zero.

---

## Complexity Analysis Summary

| Approach | Time | Space | Best For |
|----------|------|-------|----------|
| Sorting | O(n log n) | O(n) | Quick implementation |
| HashMap | O(n) | O(k) | Unicode/unknown charset |
| Array | O(n) | O(1) | Lowercase letters (optimal) |

**Where:**
- n = length of strings
- k = number of unique characters (max 26 for lowercase)

---

## Related Problems

After mastering this problem, try:
1. **Group Anagrams** (Medium) - Group strings that are anagrams
2. **Find All Anagrams in String** (Medium) - Sliding window + frequency
3. **Permutation in String** (Medium) - Similar pattern with sliding window
4. **Valid Palindrome** (Easy) - Similar two-pointer checking

---

## Key Takeaways

1. **Pattern Recognition**: Frequency counting problems use HashMap or array
2. **Optimization**: Use array for fixed character set (26 lowercase letters)
3. **Early Exit**: Check lengths first to avoid unnecessary work
4. **Character Mapping**: `char - 'a'` maps 'a'-'z' to indices 0-25
5. **Increment/Decrement**: Smart way to verify matching frequencies in one structure

---

## Practice Checklist

- [ ] Implement all three approaches
- [ ] Test with all edge cases
- [ ] Can explain why array approach is O(1) space
- [ ] Understand character-to-index mapping
- [ ] Can modify for Unicode support
- [ ] Time yourself: aim for <15 minutes

---

## Template for Frequency Counter Pattern

```java
// Template: Frequency Counter using Array (fixed charset)
public boolean frequencyPattern(String s, String t) {
    if (s.length() != t.length()) return false;
    
    int[] count = new int[26];  // For lowercase a-z
    
    for (int i = 0; i < s.length(); i++) {
        count[s.charAt(i) - 'a']++;
        count[t.charAt(i) - 'a']--;
    }
    
    for (int freq : count) {
        if (freq != 0) return false;
    }
    
    return true;
}
```

```java
// Template: Frequency Counter using HashMap (any charset)
public boolean frequencyPattern(String s, String t) {
    if (s.length() != t.length()) return false;
    
    HashMap<Character, Integer> count = new HashMap<>();
    
    for (char c : s.toCharArray()) {
        count.put(c, count.getOrDefault(c, 0) + 1);
    }
    
    for (char c : t.toCharArray()) {
        count.put(c, count.getOrDefault(c, 0) - 1);
        if (count.get(c) < 0) return false;
    }
    
    return true;
}
```

---

**Next Problem**: Product of Array Except Self (Medium) - Prefix/Suffix pattern 🚀

**Study Time**: 45-60 minutes recommended
**Difficulty Jump**: Easy → Stay on Easy pattern variations
**Pattern Evolution**: Frequency Counter → will lead to Group Anagrams next!
