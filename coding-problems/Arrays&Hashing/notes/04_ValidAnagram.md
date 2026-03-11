# Valid Anagram (Easy)

## Problem Statement
Given two strings `s` and `t`, return `true` if `t` is an anagram of `s`, and `false` otherwise.

An **Anagram** is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

**LeetCode Link**: [242. Valid Anagram](https://leetcode.com/problems/valid-anagram/)

---

## Examples

### Example 1: Basic Anagram
```
Input: s = "anagram", t = "nagaram"
Output: true
Explanation: 
Both strings have exactly the same characters with same frequencies:
s: a=3, n=1, g=1, r=1, m=1
t: a=3, n=1, g=1, r=1, m=1
Rearranging "anagram" gives "nagaram"
```

### Example 2: Not an Anagram
```
Input: s = "rat", t = "car"
Output: false
Explanation:
Different character frequencies:
s: r=1, a=1, t=1
t: c=1, a=1, r=1
Character 't' is in s but not in t
Character 'c' is in t but not in s
```

### Example 3: Different Lengths
```
Input: s = "hello", t = "hi"
Output: false
Explanation:
Different lengths (5 vs 2) cannot be anagrams
Quick rejection without checking characters
```

### Example 4: Same Character Different Frequency
```
Input: s = "aab", t = "aba"
Output: true
Explanation:
Same characters, same frequency:
s: a=2, b=1
t: a=2, b=1
Order doesn't matter, only frequency
```

### Example 5: Empty Strings
```
Input: s = "", t = ""
Output: true
Explanation:
Both empty strings are considered anagrams
Edge case that's often forgotten
```

### Example 6: Single Character
```
Input: s = "a", t = "a"
Output: true
Explanation:
Single identical characters are anagrams
```

### Example 7: Unicode Characters
```
Input: s = "café", t = "éfac"
Output: true
Explanation:
Works with Unicode/accented characters
é appears in both with same frequency
HashMap approach handles this naturally
```

### Example 8: Case Sensitivity
```
Input: s = "Listen", t = "Silent"
Output: false (if case-sensitive)
Output: true (if case-insensitive)
Explanation:
'L' != 'l' in case-sensitive comparison
Problem typically assumes case-sensitive
```

---

## Constraints
- `1 <= s.length, t.length <= 5 * 10^4`
- `s` and `t` consist of lowercase English letters (typically)
- Follow-up: What if inputs contain Unicode characters?

---

## Pattern Recognition

This is a **Frequency Counter** problem because:

1. **Order doesn't matter** - only the count of each character
2. **Need to track character occurrences** - HashMap or array
3. **Comparison based** - two data structures need to match
4. **No positional constraints** - characters can be anywhere

**Key Insight**: Two strings are anagrams if and only if they have the **identical character frequency distributions**.

### Visual Intuition:
```
s = "anagram"
t = "nagaram"

Character Frequency Table:
┌─────┬───┬───┬───┬───┬───┬───┬───┐
│Char │ a │ n │ g │ r │ m │ ... │
├─────┼───┼───┼───┼───┼───┼───┼───┤
│ s   │ 3 │ 1 │ 1 │ 1 │ 1 │ ... │
│ t   │ 3 │ 1 │ 1 │ 1 │ 1 │ ... │
└─────┴───┴───┴───┴───┴───┴───┴───┘
           ↑ Tables match → Anagram!
```

### Why Frequency Counter?
```
NOT about:
  ✗ Finding subsequences
  ✗ Preserving order
  ✗ Pattern matching
  ✗ Position tracking

IS about:
  ✓ Counting occurrences
  ✓ Character distribution
  ✓ Set equality (multiset)
  ✓ Permutation validation
```

---

## Approach 1: Sorting

### Intuition
If two strings are anagrams, sorting them will produce identical strings. This transforms the frequency comparison into a simple string equality check.

```
s = "anagram" → sort → "aaagmnr"
t = "nagaram" → sort → "aaagmnr"
                        ↑ Equal!
```

### Algorithm Steps
1. Check if lengths are equal (quick rejection)
2. Sort both strings into character arrays
3. Compare sorted arrays for equality
4. Return true if equal, false otherwise

### Implementation

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        // Quick length check - O(1) rejection
        if (s.length() != t.length()) {
            return false;
        }
        
        // Convert to char arrays and sort - O(n log n)
        char[] sArray = s.toCharArray();
        char[] tArray = t.toCharArray();
        
        Arrays.sort(sArray);
        Arrays.sort(tArray);
        
        // Compare sorted arrays - O(n)
        return Arrays.equals(sArray, tArray);
    }
}

// Alternative: Using String sorting
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        
        // Convert to char array, sort, and create new string
        char[] sChars = s.toCharArray();
        char[] tChars = t.toCharArray();
        Arrays.sort(sChars);
        Arrays.sort(tChars);
        
        return new String(sChars).equals(new String(tChars));
    }
}

// One-liner (less efficient due to string creation)
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        return Arrays.equals(
            s.chars().sorted().toArray(),
            t.chars().sorted().toArray()
        );
    }
}
```

### Complexity Analysis
- **Time Complexity**: O(n log n)
  - Sorting both strings: O(n log n + m log m) = O(n log n) when n = m
  - Comparison: O(n)
  - Total: O(n log n) dominates
  
- **Space Complexity**: O(n) or O(log n)
  - O(n) for char arrays (explicit space)
  - O(log n) for sorting (stack space, depending on sort algorithm)
  - Java's Arrays.sort() uses Dual-Pivot Quicksort for primitives

### Pros and Cons

**Pros:**
- ✓ Simple and intuitive
- ✓ Minimal code
- ✓ No need to understand frequency counting
- ✓ Works with any character set (Unicode)
- ✓ Built-in sorting is well-optimized

**Cons:**
- ✗ Not optimal time complexity
- ✗ Creates additional arrays
- ✗ Modifies data (needs copies)
- ✗ O(n log n) when O(n) is possible

### When to Use
- Quick interviews when optimization isn't asked
- Unicode/variable character sets
- Code simplicity is valued
- Input size is small

---

## Approach 2: HashMap (Frequency Counter)

### Intuition
Count the frequency of each character in both strings using a HashMap. Two strings are anagrams if their frequency maps are identical.

```
s = "anagram"
HashMap: {a:3, n:1, g:1, r:1, m:1}

t = "nagaram"
HashMap: {a:3, n:1, g:1, r:1, m:1}

Maps equal → Anagram!
```

### Algorithm Steps
1. Check if lengths are equal
2. Build frequency map for string `s`
3. Build frequency map for string `t`
4. Compare both maps for equality
5. Return comparison result

**Optimization**: Use single map - increment for s, decrement for t

### Implementation

```java
// Approach 2A: Two HashMaps
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        // Build frequency maps
        HashMap<Character, Integer> sMap = new HashMap<>();
        HashMap<Character, Integer> tMap = new HashMap<>();
        
        // Count characters in s
        for (char c : s.toCharArray()) {
            sMap.put(c, sMap.getOrDefault(c, 0) + 1);
        }
        
        // Count characters in t
        for (char c : t.toCharArray()) {
            tMap.put(c, tMap.getOrDefault(c, 0) + 1);
        }
        
        // Compare maps
        return sMap.equals(tMap);
    }
}

// Approach 2B: Single HashMap (Optimized)
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        HashMap<Character, Integer> charCount = new HashMap<>();
        
        // Increment for s, decrement for t
        for (int i = 0; i < s.length(); i++) {
            char sChar = s.charAt(i);
            char tChar = t.charAt(i);
            
            charCount.put(sChar, charCount.getOrDefault(sChar, 0) + 1);
            charCount.put(tChar, charCount.getOrDefault(tChar, 0) - 1);
        }
        
        // Check if all counts are zero
        for (int count : charCount.values()) {
            if (count != 0) {
                return false;
            }
        }
        
        return true;
    }
}

// Approach 2C: Single HashMap with Early Exit
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        
        Map<Character, Integer> map = new HashMap<>();
        
        // Count s characters
        for (char c : s.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        
        // Decrement for t characters
        for (char c : t.toCharArray()) {
            int count = map.getOrDefault(c, 0);
            if (count == 0) {
                return false; // Character not in s or already exhausted
            }
            map.put(c, count - 1);
        }
        
        return true; // All characters matched
    }
}

// Approach 2D: Using Java 8 Streams
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        
        Map<Integer, Long> sMap = s.chars()
            .boxed()
            .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
            
        Map<Integer, Long> tMap = t.chars()
            .boxed()
            .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
            
        return sMap.equals(tMap);
    }
}
```

### Complexity Analysis
- **Time Complexity**: O(n)
  - Length check: O(1)
  - Building frequency map(s): O(n)
  - Comparing maps: O(k) where k = number of unique characters
  - Total: O(n + k) = O(n) since k ≤ n
  
- **Space Complexity**: O(k)
  - HashMap stores at most k unique characters
  - k ≤ 26 for lowercase English letters (constant)
  - k ≤ n for general Unicode
  - Practical: O(1) for English, O(n) for Unicode

### Pros and Cons

**Pros:**
- ✓ Optimal O(n) time complexity
- ✓ Works with any character set
- ✓ Natural approach for frequency problems
- ✓ Early exit optimization possible
- ✓ Scalable to Unicode

**Cons:**
- ✗ More code than sorting
- ✗ Extra space for HashMap
- ✗ HashMap overhead (not cache-friendly)
- ✗ Overkill for lowercase English only

### When to Use
- Unicode or variable character sets
- When asked for optimal solution
- Need to explain frequency counting
- Extensible to related problems (Group Anagrams)

---

## Approach 3: Fixed Array Counter (Optimal for English)

### Intuition
For lowercase English letters (a-z), we can use a fixed-size array of 26 elements instead of a HashMap. Each index represents a letter (0='a', 1='b', etc.). This is faster and more space-efficient.

```
s = "anagram"
Array[26]: [3,0,0,0,0,0,1,0,0,0,0,0,1,1,0,0,0,1,0,1,0,0,0,0,0,0]
           a b c d e f g h i j k l m n o p q r s t u v w x y z
           3       0       1           1 1       1   1
```

### Algorithm Steps
1. Check if lengths are equal
2. Create array of size 26
3. For each character in s: increment count[c - 'a']
4. For each character in t: decrement count[c - 'a']
5. Check if all counts are zero

### Implementation

```java
// Approach 3A: Standard Array Counter
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        int[] count = new int[26];
        
        // Count characters in s
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        // Subtract characters in t
        for (char c : t.toCharArray()) {
            count[c - 'a']--;
        }
        
        // Check if all counts are zero
        for (int val : count) {
            if (val != 0) {
                return false;
            }
        }
        
        return true;
    }
}

// Approach 3B: Single Loop (More Efficient)
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        int[] count = new int[26];
        
        // Single loop - increment for s, decrement for t
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
            count[t.charAt(i) - 'a']--;
        }
        
        // Check all counts are zero
        for (int val : count) {
            if (val != 0) {
                return false;
            }
        }
        
        return true;
    }
}

// Approach 3C: Early Exit Optimization
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        int[] count = new int[26];
        
        // Count s
        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }
        
        // Check t and decrement
        for (char c : t.toCharArray()) {
            int index = c - 'a';
            count[index]--;
            if (count[index] < 0) {
                return false; // Character appears more in t
            }
        }
        
        return true;
    }
}

// Approach 3D: Two Arrays (Alternative)
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        
        int[] sCount = new int[26];
        int[] tCount = new int[26];
        
        for (int i = 0; i < s.length(); i++) {
            sCount[s.charAt(i) - 'a']++;
            tCount[t.charAt(i) - 'a']++;
        }
        
        // Use Arrays.equals for comparison
        return Arrays.equals(sCount, tCount);
    }
}

// Approach 3E: Bit Manipulation for Validation (Advanced)
class Solution {
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        
        int[] count = new int[26];
        
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
            count[t.charAt(i) - 'a']--;
        }
        
        // Check if all are zero using bitwise OR
        int result = 0;
        for (int val : count) {
            result |= val;
        }
        
        return result == 0;
    }
}
```

### Complexity Analysis
- **Time Complexity**: O(n)
  - Iterate through both strings once: O(n)
  - Verify array of 26: O(26) = O(1)
  - Total: O(n)
  
- **Space Complexity**: O(1)
  - Array of size 26 (constant)
  - Independent of input size
  - Most space-efficient solution

### Pros and Cons

**Pros:**
- ✓ Optimal O(n) time, O(1) space
- ✓ Fastest in practice (cache-friendly)
- ✓ Simple array operations
- ✓ No HashMap overhead
- ✓ Best for interviews

**Cons:**
- ✗ Only works for fixed character set
- ✗ Doesn't handle Unicode
- ✗ Assumes lowercase English letters
- ✗ Needs modification for case-insensitive

### When to Use
- **Most interview scenarios** (typically lowercase English)
- When optimization is important
- Space constraint is critical
- Character set is known and small

---

## Approach Comparison

| Approach | Time | Space | Pros | Cons | Best For |
|----------|------|-------|------|------|----------|
| **Sorting** | O(n log n) | O(n) or O(log n) | Simple, works with Unicode | Not optimal time | Quick solution, Unicode |
| **HashMap** | O(n) | O(k) | Optimal, flexible | HashMap overhead | Unicode, learning |
| **Array Counter** | O(n) | O(1) | Fastest, optimal | Fixed charset only | English letters, interviews |

### Decision Tree
```
Is character set known and small (e.g., a-z)?
├─ Yes → Use Array Counter (Approach 3) ✓
└─ No → Unicode or variable charset?
    ├─ Yes → Use HashMap (Approach 2)
    └─ No → Quick solution needed?
        ├─ Yes → Use Sorting (Approach 1)
        └─ No → Use HashMap (Approach 2)
```

---

## Edge Cases

### 1. Empty Strings
```java
Input: s = "", t = ""
Output: true
Why: Two empty strings are anagrams by definition
Test: if (s.isEmpty() && t.isEmpty()) return true;
```

### 2. Different Lengths
```java
Input: s = "abc", t = "abcd"
Output: false
Why: Different lengths cannot be anagrams
Test: Always check s.length() != t.length() first
```

### 3. Single Character Match
```java
Input: s = "a", t = "a"
Output: true
Why: Trivial anagram case
Test: Minimum valid input
```

### 4. Single Character Mismatch
```java
Input: s = "a", t = "b"
Output: false
Why: Different characters
Test: Simplest false case
```

### 5. All Same Characters
```java
Input: s = "aaaa", t = "aaaa"
Output: true
Why: Same character repeated same times
Test: Frequency count edge case
```

### 6. Same Characters Different Frequency
```java
Input: s = "aab", t = "aaa"
Output: false
Why: Same length, same chars, but different frequency
Test: Must verify counts, not just character presence
```

### 7. Permutation of Same String
```java
Input: s = "abc", t = "cba"
Output: true
Why: Complete reversal is still an anagram
Test: Order doesn't matter
```

### 8. Identical Strings
```java
Input: s = "hello", t = "hello"
Output: true
Why: String is anagram of itself
Test: Identity case
```

### 9. Case Sensitivity
```java
Input: s = "Hello", t = "hello"
Output: false (typically case-sensitive)
Why: 'H' != 'h' in frequency count
Test: Problem usually case-sensitive unless stated
Variation: Convert to lowercase if case-insensitive needed
```

### 10. Whitespace Handling
```java
Input: s = "anagram", t = "nag a ram"
Output: false (with spaces), true (ignoring spaces)
Why: Space is a character that affects frequency
Test: Check if problem includes spaces in character set
Solution: Filter spaces if anagram ignores them
```

### 11. Unicode Characters
```java
Input: s = "café", t = "éfac"
Output: true
Why: Unicode characters (é) count as single characters
Test: Use HashMap, not array
Note: Array approach fails with non-ASCII
```

### 12. Special Characters
```java
Input: s = "a-b", t = "b-a"
Output: true
Why: Special characters (-) are treated like letters
Test: All characters count in frequency
```

### 13. Numbers in Strings
```java
Input: s = "a1b2", t = "b2a1"
Output: true
Why: Digits are characters too
Test: Alphanumeric anagrams
```

### 14. Very Long Strings
```java
Input: s = "a" * 50000, t = "a" * 50000
Output: true
Why: Performance test for O(n) algorithms
Test: Ensure no timeout (constraints: length ≤ 5*10^4)
```

### 15. Maximum Unique Characters
```java
Input: s = "abcdefghijklmnopqrstuvwxyz", t = "zyxwvutsrqponmlkjihgfedcba"
Output: true
Why: All 26 letters used once
Test: Array fully utilized
```

---

## Common Mistakes

### 1. Not Checking Length First
```java
// ❌ WRONG: Missing length check
public boolean isAnagram(String s, String t) {
    int[] count = new int[26];
    for (char c : s.toCharArray()) {
        count[c - 'a']++;
    }
    for (char c : t.toCharArray()) {
        count[c - 'a']--;
    }
    // ... check count
}

// ✅ CORRECT: Check length first (O(1) rejection)
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) {
        return false; // Quick exit
    }
    // ... rest of logic
}

Why Wrong: Wastes time processing when lengths differ
Impact: Unnecessary iterations, fails edge cases
```

### 2. Using Array for Unicode
```java
// ❌ WRONG: Array size 26 for Unicode
public boolean isAnagram(String s, String t) {
    int[] count = new int[26]; // Only works for a-z!
    for (char c : s.toCharArray()) {
        count[c - 'a']++; // Crashes on Unicode like 'é'
    }
    // ...
}

// ✅ CORRECT: Use HashMap for Unicode
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    
    Map<Character, Integer> map = new HashMap<>();
    for (char c : s.toCharArray()) {
        map.put(c, map.getOrDefault(c, 0) + 1);
    }
    // ...
}

Why Wrong: ArrayIndexOutOfBounds for non-English characters
Fix: Ask interviewer about character set constraints
```

### 3. Forgetting to Verify All Counts are Zero
```java
// ❌ WRONG: Only building frequency, not verifying
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    
    int[] count = new int[26];
    for (int i = 0; i < s.length(); i++) {
        count[s.charAt(i) - 'a']++;
        count[t.charAt(i) - 'a']--;
    }
    
    return true; // WRONG! Didn't check counts
}

// ✅ CORRECT: Verify all counts are zero
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    
    int[] count = new int[26];
    for (int i = 0; i < s.length(); i++) {
        count[s.charAt(i) - 'a']++;
        count[t.charAt(i) - 'a']--;
    }
    
    for (int val : count) {
        if (val != 0) return false; // Must verify!
    }
    
    return true;
}

Why Wrong: Returns true for non-anagrams like s="ab", t="cd"
Test Case: s = "ab", t = "cd" should return false
```

### 4. Comparing Character Arrays Incorrectly
```java
// ❌ WRONG: Using == for array comparison
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    
    char[] sArr = s.toCharArray();
    char[] tArr = t.toCharArray();
    Arrays.sort(sArr);
    Arrays.sort(tArr);
    
    return sArr == tArr; // WRONG! Compares references, not content
}

// ✅ CORRECT: Use Arrays.equals()
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    
    char[] sArr = s.toCharArray();
    char[] tArr = t.toCharArray();
    Arrays.sort(sArr);
    Arrays.sort(tArr);
    
    return Arrays.equals(sArr, tArr); // Correct content comparison
}

Why Wrong: == compares references, not array contents
Test: s = "ab", t = "ba" would incorrectly return false
```

### 5. Inefficient String Concatenation in Sorting
```java
// ❌ WRONG: Inefficient sorting with streams
public boolean isAnagram(String s, String t) {
    String sortedS = s.chars()
                      .sorted()
                      .collect(StringBuilder::new, 
                               StringBuilder::appendCodePoint,
                               StringBuilder::append)
                      .toString();
    // Overly complex and inefficient
}

// ✅ CORRECT: Simple array sorting
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) return false;
    
    char[] sArr = s.toCharArray();
    char[] tArr = t.toCharArray();
    Arrays.sort(sArr);
    Arrays.sort(tArr);
    
    return Arrays.equals(sArr, tArr);
}

Why Wrong: Unnecessary complexity, poor performance
Keep it simple: toCharArray() → sort() → equals()
```

### 6. Not Handling Null Inputs
```java
// ❌ WRONG: NullPointerException risk
public boolean isAnagram(String s, String t) {
    if (s.length() != t.length()) { // NPE if s or t is null
        return false;
    }
    // ...
}

// ✅ CORRECT: Handle null cases
public boolean isAnagram(String s, String t) {
    if (s == null || t == null) {
        return s == t; // Both null → true, one null → false
    }
    
    if (s.length() != t.length()) {
        return false;
    }
    // ...
}

Why Wrong: LeetCode may test null inputs (edge case)
Best Practice: Always validate inputs first
Note: LeetCode constraints usually guarantee non-null
```

### 7. Using Wrong Data Type for Count
```java
// ❌ WRONG: Using byte/short for large counts
public boolean isAnagram(String s, String t) {
    byte[] count = new byte[26]; // Overflow if count > 127!
    // ...
}

// ✅ CORRECT: Use int for counts
public boolean isAnagram(String s, String t) {
    int[] count = new int[26]; // Safe for all counts
    // ...
}

Why Wrong: Byte overflow for strings like "aaaa...a" (128+ chars)
Constraints: String length up to 5*10^4 → need int
```

### 8. Case-Sensitivity Confusion
```java
// ❌ WRONG: Assuming case-insensitive without confirmation
public boolean isAnagram(String s, String t) {
    s = s.toLowerCase(); // May not be required!
    t = t.toLowerCase();
    // ...
}

// ✅ CORRECT: Follow problem requirements
public boolean isAnagram(String s, String t) {
    // Default: case-sensitive
    // Only convert if problem states case-insensitive
    
    // If case-insensitive required:
    // s = s.toLowerCase();
    // t = t.toLowerCase();
    
    // ... rest of solution
}

Why Wrong: Problem typically IS case-sensitive
Always clarify: "Should we treat 'A' and 'a' as same?"
```

---

## Interview Tips

### 1. Clarify Constraints First
```
Questions to ask:
❓ "Are the strings guaranteed to be lowercase English letters only?"
   → Determines Array vs HashMap approach

❓ "Should we handle Unicode characters?"
   → Array approach won't work

❓ "Is the comparison case-sensitive?"
   → Affects preprocessing

❓ "Do we need to handle null inputs?"
   → Edge case handling

❓ "Should we consider spaces as characters?"
   → Anagram definition clarity
```

### 2. Start with Brute Force, Then Optimize
```
Interview Flow:
1. Explain brute force: "We could sort both strings..." (30 sec)
2. State complexity: "That's O(n log n) time, O(n) space" (15 sec)
3. Propose optimization: "We can do better with O(n) using frequency counter" (30 sec)
4. Choose optimal approach: Array for English, HashMap for Unicode (30 sec)
5. Implement and test (5-7 min)

Don't jump to code immediately!
```

### 3. Discuss Trade-offs
```
Show thinking process:

"I see three approaches here:

1. Sorting: O(n log n), simplest code
   - Good when simplicity matters
   
2. HashMap: O(n) time, O(k) space
   - Best for Unicode/unknown charset
   
3. Array: O(n) time, O(1) space
   - Optimal for lowercase English letters
   
Since the problem mentions lowercase English letters,
I'll use the array approach for O(1) space..."
```

### 4. Write Clean, Production-Quality Code
```java
public boolean isAnagram(String s, String t) {
    // 1. Early exit for different lengths
    if (s.length() != t.length()) {
        return false;
    }
    
    // 2. Frequency counter array
    int[] charCount = new int[26];
    
    // 3. Count characters in both strings
    for (int i = 0; i < s.length(); i++) {
        charCount[s.charAt(i) - 'a']++;
        charCount[t.charAt(i) - 'a']--;
    }
    
    // 4. Verify all counts are zero
    for (int count : charCount) {
        if (count != 0) {
            return false;
        }
    }
    
    return true;
}

✓ Clear variable names
✓ Comments for sections
✓ Clean formatting
✓ Early exits
```

### 5. Test with Edge Cases
```
"Let me test a few cases:

1. Empty strings: s="", t="" → true ✓
2. Different lengths: s="abc", t="ab" → false ✓
3. Same chars, different freq: s="aab", t="aaa" → false ✓
4. Reversed: s="abc", t="cba" → true ✓
5. Identical: s="test", t="test" → true ✓

The solution handles all edge cases correctly."
```

### 6. Mention Follow-ups
```
Proactive thinking:

"If we needed to find all anagrams in a list of strings,
we could extend this to use a sorted string as a key
in a HashMap, grouping anagrams together. That's the
'Group Anagrams' problem (LeetCode 49)."

Shows you understand related problems!
```

### 7. Complexity Analysis is Crucial
```
Always state clearly:

"Time Complexity: O(n) where n is the length of the strings
 - We iterate through each string once
 - Checking the 26-element array is O(1)

Space Complexity: O(1)
 - Array size is constant (26)
 - Independent of input size
 
This is optimal as we need to examine every character."
```

### 8. Handle Modifications Gracefully
```
Interviewer: "What if strings contain Unicode?"

Response:
"Great question! The array approach wouldn't work for Unicode.
I'd switch to a HashMap where keys are characters and values
are frequencies. Same O(n) time, but space becomes O(k) where
k is the number of unique characters. Let me show you..."

[Smoothly transition to HashMap implementation]
```

### 9. Code Without IDE
```
Practice writing on paper/whiteboard:

Common mistakes without autocomplete:
- Forgetting 'new' keyword: int[] count = int[26]; ❌
- Wrong array method: Arrays.equal() vs Arrays.equals() ❌
- Typos: charAt() not chatAt() ❌
- Loop bounds: i <= s.length() vs i < s.length() ❌

Practice handwriting to avoid these!
```

### 10. Explain While Coding
```
Narrate your thought process:

"I'm creating an array of size 26 for each letter...
Now I'll iterate through both strings simultaneously...
For each character in s, I increment the corresponding index...
For each character in t, I decrement...
Finally, I verify all counts are zero..."

Helps interviewer follow along and shows communication skills.
```

---

## Related Problems

### Similar Pattern
1. **Group Anagrams** (LeetCode 49) - Medium
   - Extension: Group multiple strings that are anagrams
   - Use sorted string or frequency as HashMap key

2. **Find All Anagrams in String** (LeetCode 438) - Medium
   - Sliding window + frequency counter
   - Find all start indices of anagram substrings

3. **Permutation in String** (LeetCode 567) - Medium
   - Check if s1's permutation is substring of s2
   - Similar to anagram checking with sliding window

4. **Minimum Window Substring** (LeetCode 76) - Hard
   - Advanced frequency counter with two pointers
   - More complex version of character matching

### Frequency Counter Pattern
1. **First Unique Character in String** (LeetCode 387) - Easy
2. **Ransom Note** (LeetCode 383) - Easy
3. **Sort Characters By Frequency** (LeetCode 451) - Medium
4. **Top K Frequent Elements** (LeetCode 347) - Medium

---

## Key Takeaways

### 1. Anagram = Same Character Frequencies
```
Core Concept:
Two strings are anagrams ⟺ Identical character distributions
Order doesn't matter, only counts matter

Example:
"listen" and "silent" both have:
{l:1, i:1, s:1, t:1, e:1, n:1}
```

### 2. Length Check is Essential
```
if (s.length() != t.length()) return false;

Why crucial:
- O(1) rejection for obvious non-anagrams
- Prevents unnecessary processing
- Catches edge cases early

Always do this first!
```

### 3. Choose Data Structure Based on Character Set
```
Decision Matrix:

Known lowercase English (a-z)?
→ Use int[26] array (O(1) space)

Unicode or unknown charset?
→ Use HashMap<Character, Integer> (O(k) space)

Need simplicity over optimization?
→ Sort and compare (O(n log n) time)
```

### 4. Array Indexing: char - 'a'
```
Key technique for letter arrays:

char c = 'c';
int index = c - 'a'; // index = 2

Maps: a→0, b→1, c→2, ..., z→25

Remember: Only works for contiguous character ranges
```

### 5. Frequency Counter: Increment and Decrement Pattern
```
Efficient single-map approach:

for s characters: map[char]++
for t characters: map[char]--

If all counts become 0 → anagram!

Saves space and iterations
```

### 6. Sorting is Simple but Not Optimal
```
Trade-off:
✓ Simplest code: 2-3 lines
✗ O(n log n) time complexity

Use when:
- Quick solution needed
- Optimization not required
- Unicode without HashMap knowledge
```

### 7. Early Exit Optimizations Matter
```
Pattern:

while (processing) {
    if (impossible_condition) {
        return false; // Exit immediately
    }
}

Examples:
- Different lengths
- Character not in frequency map
- Count goes negative
```

### 8. Arrays.equals() vs == for Arrays
```
Critical difference:

char[] a1 = {'a', 'b'};
char[] a2 = {'a', 'b'};

a1 == a2           → false (different references)
Arrays.equals(a1, a2) → true (same content)

Always use Arrays.equals() for array comparison!
```

### 9. Handle Edge Cases Systematically
```
Edge case checklist:
□ Empty strings
□ Single character
□ Different lengths
□ Identical strings
□ All same characters
□ Case sensitivity
□ Unicode characters
□ Null inputs (if allowed)

Test each before submitting!
```

### 10. Frequency Counter is a Fundamental Pattern
```
Master this pattern - appears in many problems:

Core idea:
- Count occurrences of elements
- Use array (fixed range) or HashMap (general)
- Compare, validate, or transform based on frequencies

Used in:
- Anagrams
- Substring matching
- Character/element uniqueness
- Top K problems
- Histogram-based solutions

Understanding this pattern unlocks dozens of problems!
```

---

## Pattern Template

### Frequency Counter Template (Array - Fixed Character Set)
```java
public boolean hasPropertyX(String s, String t) {
    // 1. Quick validations
    if (s.length() != t.length()) return false;
    
    // 2. Initialize frequency counter
    int[] count = new int[26]; // or other fixed size
    
    // 3. Build frequency map
    for (char c : s.toCharArray()) {
        count[c - 'a']++; // Map character to index
    }
    
    // 4. Verify/modify using second string
    for (char c : t.toCharArray()) {
        count[c - 'a']--;
        // Optional: early exit
        if (count[c - 'a'] < 0) return false;
    }
    
    // 5. Final verification
    for (int val : count) {
        if (val != 0) return false;
    }
    
    return true;
}
```

### Frequency Counter Template (HashMap - Variable Character Set)
```java
public boolean hasPropertyX(String s, String t) {
    // 1. Quick validations
    if (s.length() != t.length()) return false;
    
    // 2. Initialize frequency counter
    Map<Character, Integer> map = new HashMap<>();
    
    // 3. Build frequency map
    for (char c : s.toCharArray()) {
        map.put(c, map.getOrDefault(c, 0) + 1);
    }
    
    // 4. Verify using second string
    for (char c : t.toCharArray()) {
        int count = map.getOrDefault(c, 0);
        if (count == 0) return false; // Not in map or exhausted
        map.put(c, count - 1);
    }
    
    return true;
}
```

---

## Time and Space Complexity Summary

| Approach | Time Complexity | Space Complexity | Notes |
|----------|----------------|------------------|-------|
| Sorting | O(n log n) | O(n) or O(log n) | Simplest, works universally |
| HashMap (Two maps) | O(n) | O(k) | k = unique chars |
| HashMap (One map) | O(n) | O(k) | More efficient |
| Array Counter | O(n) | O(1) | Best for fixed charset |
| Stream API | O(n) | O(k) | Functional style |

**Where:**
- n = length of strings
- k = number of unique characters (k ≤ n)
- For English lowercase: k ≤ 26, so O(k) = O(1)

---

## Conclusion

Valid Anagram is a fundamental **frequency counter** problem that teaches essential skills for many string and array problems. The key insight is recognizing that anagrams have identical character distributions, which can be verified efficiently using:

1. **Array counting** for fixed character sets (optimal)
2. **HashMap** for variable character sets (flexible)
3. **Sorting** for quick solutions (simple)

Master this pattern - it's the foundation for Group Anagrams, substring matching, and many other problems!

**Remember:** The best approach depends on constraints. Always clarify character set and choose accordingly!

---

**Practice Problems:** Try solving Group Anagrams (LeetCode 49) next to extend this pattern!
