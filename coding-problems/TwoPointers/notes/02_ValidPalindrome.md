# Valid Palindrome (Easy)

## Problem Statement
A phrase is a **palindrome** if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward. Alphanumeric characters include letters and numbers.

Given a string `s`, return `true` if it is a palindrome, or `false` otherwise.

**LeetCode Link**: [125. Valid Palindrome](https://leetcode.com/problems/valid-palindrome/)

---

## Examples

### Example 1:
```
Input: s = "A man, a plan, a canal: Panama"
Output: true
Explanation: "amanaplanacanalpanama" is a palindrome.
```

### Example 2:
```
Input: s = "race a car"
Output: false
Explanation: "raceacar" is not a palindrome.
```

### Example 3:
```
Input: s = " "
Output: true
Explanation: s is an empty string "" after removing non-alphanumeric characters.
Since an empty string reads the same forward and backward, it is a palindrome.
```

---

## Constraints
- `1 <= s.length <= 2 * 10^5`
- `s` consists only of printable ASCII characters

---

## Pattern Recognition

This is a **Two Pointers (Convergent)** problem because:
1. We need to check **symmetry** (reads same forward and backward)
2. We can **compare from both ends** moving toward center
3. We need **O(1) space** (in-place checking)
4. Linear **O(n) time** is optimal

**Key Insight**: Instead of creating a cleaned string (O(n) space), use two pointers to skip invalid characters on-the-fly and compare valid ones directly!

---

## Approaches

### Approach 1: Two-Pass (Create Cleaned String) - NOT OPTIMAL
**Idea**: First clean the string, then check if it's a palindrome.

```java
class Solution {
    public boolean isPalindrome(String s) {
        // Step 1: Build cleaned string
        StringBuilder cleaned = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                cleaned.append(Character.toLowerCase(c));
            }
        }
        
        // Step 2: Check if palindrome
        String cleanStr = cleaned.toString();
        int left = 0;
        int right = cleanStr.length() - 1;
        
        while (left < right) {
            if (cleanStr.charAt(left) != cleanStr.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        
        return true;
    }
}
```

**Time Complexity**: O(n) - two passes through string
**Space Complexity**: O(n) - store cleaned string
**Problem**: Uses extra space, can be improved!

---

### Approach 2: Two Pointers In-Place (OPTIMAL) ⭐
**Idea**: Use two pointers on original string, skip non-alphanumeric characters dynamically.

```java
class Solution {
    public boolean isPalindrome(String s) {
        // Initialize pointers at both ends
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            // Skip non-alphanumeric from left
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            
            // Skip non-alphanumeric from right
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            
            // Compare characters (case-insensitive)
            if (Character.toLowerCase(s.charAt(left)) != 
                Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            
            // Move both pointers
            left++;
            right--;
        }
        
        return true;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(1) - only use pointer variables
**Why Optimal**: Single pass, no extra space, clean logic

---

### Approach 3: Optimized with Helper Method
**Idea**: Extract validation logic to a helper method for cleaner code.

```java
class Solution {
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        
        while (left < right) {
            // Move left pointer to next valid character
            while (left < right && !isAlphanumeric(s.charAt(left))) {
                left++;
            }
            
            // Move right pointer to next valid character
            while (left < right && !isAlphanumeric(s.charAt(right))) {
                right--;
            }
            
            // Compare characters
            if (toLowerCase(s.charAt(left)) != toLowerCase(s.charAt(right))) {
                return false;
            }
            
            left++;
            right--;
        }
        
        return true;
    }
    
    // Helper: Check if character is alphanumeric
    private boolean isAlphanumeric(char c) {
        return (c >= 'A' && c <= 'Z') || 
               (c >= 'a' && c <= 'z') || 
               (c >= '0' && c <= '9');
    }
    
    // Helper: Convert to lowercase
    private char toLowerCase(char c) {
        if (c >= 'A' && c <= 'Z') {
            return (char)(c + 32); // 'A' = 65, 'a' = 97
        }
        return c;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(1)
**Advantage**: No dependency on Character class, shows understanding of ASCII

---

### Approach 4: Using Regex (Concise but Slower)
**Idea**: Use regex to clean string first.

```java
class Solution {
    public boolean isPalindrome(String s) {
        // Clean string: remove non-alphanumeric, convert to lowercase
        String cleaned = s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
        
        // Check palindrome with two pointers
        int left = 0;
        int right = cleaned.length() - 1;
        
        while (left < right) {
            if (cleaned.charAt(left) != cleaned.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        
        return true;
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(n) - creates new string
**Note**: Slower in practice due to regex overhead, uses extra space

---

### Approach 5: Reverse and Compare (Simple but O(n) space)
```java
class Solution {
    public boolean isPalindrome(String s) {
        // Clean string
        StringBuilder cleaned = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                cleaned.append(Character.toLowerCase(c));
            }
        }
        
        // Compare with reverse
        String str = cleaned.toString();
        String reversed = cleaned.reverse().toString();
        
        return str.equals(reversed);
    }
}
```

**Time Complexity**: O(n)
**Space Complexity**: O(n)
**Problem**: Creates two strings, not space-optimal

---

## Detailed Walkthrough (Approach 2: Optimal)

### Example: s = "A man, a plan, a canal: Panama"

```
Initial State:
s = "A man, a plan, a canal: Panama"
     ↑                            ↑
    left                        right

Step 1: Check positions
left = 0 → 'A' (valid alphanumeric)
right = 30 → 'a' (valid alphanumeric)
Compare: 'A' (lowercase) == 'a' ✓ Match!
left++, right--

Step 2:
s = "A man, a plan, a canal: Panama"
      ↑                          ↑
     left                      right
left = 1 → ' ' (not alphanumeric) → left++ → ' ' → left++
left = 3 → 'm' (valid)
right = 29 → 'm' (valid)
Compare: 'm' == 'm' ✓ Match!
left++, right--

Step 3:
s = "A man, a plan, a canal: Panama"
       ↑                        ↑
      left                    right
left = 4 → 'a' (valid)
right = 28 → 'a' (valid)
Compare: 'a' == 'a' ✓ Match!
left++, right--

... continue until left >= right

Final: All characters matched → return true
```

### Visual Comparison:
```
Original:  "A man, a plan, a canal: Panama"
Cleaned:   "amanaplanacanalpanama"
           ↓                     ↓
           Same forward & backward → Palindrome!
```

---

## Edge Cases to Consider

```java
// Test Case 1: Empty string
Input: s = ""
Output: true
// Empty string is considered palindrome

// Test Case 2: Single character
Input: s = "a"
Output: true
// Single character is always palindrome

// Test Case 3: Only spaces and punctuation
Input: s = "   !!! ..."
Output: true
// After filtering, becomes empty string → palindrome

// Test Case 4: Only non-alphanumeric
Input: s = ".,;'"
Output: true
// All characters filtered → empty → palindrome

// Test Case 5: Numbers included
Input: s = "0P"
Output: false
// '0' and 'P' are different

// Test Case 6: Mixed numbers and letters
Input: s = "A1B2C2B1A"
Output: true
// Reads same forward/backward

// Test Case 7: Case sensitivity
Input: s = "Ab"
Output: false
// After lowercase: 'a' != 'b'

// Test Case 8: Long palindrome
Input: s = "Able was I ere I saw Elba"
Output: true
// Classic palindrome sentence

// Test Case 9: Similar but not palindrome
Input: s = "race a car"
Output: false
// "raceacar" != "racaecar" (reversed)

// Test Case 10: Multiple consecutive spaces
Input: s = "a    a"
Output: true
// Spaces ignored → "aa" is palindrome
```

---

## Common Mistakes to Avoid

### Mistake 1: Using `left <= right` instead of `left < right`
```java
// ❌ WRONG: Can cause issues with odd-length palindromes
while (left <= right) {
    // When left == right, we compare middle character with itself
    // This works but is redundant
}

// ✅ CORRECT: Stop when pointers meet
while (left < right) {
    // More efficient, no redundant check
}
```

### Mistake 2: Not checking bounds in inner while loops
```java
// ❌ WRONG: Can cause IndexOutOfBoundsException
while (!Character.isLetterOrDigit(s.charAt(left))) {
    left++; // What if we go past right?
}

// ✅ CORRECT: Always check left < right
while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
    left++;
}
```

### Mistake 3: Forgetting to convert to lowercase
```java
// ❌ WRONG: Case-sensitive comparison
if (s.charAt(left) != s.charAt(right)) {
    return false;
}
// Fails on "Aa" (should be true)

// ✅ CORRECT: Case-insensitive
if (Character.toLowerCase(s.charAt(left)) != 
    Character.toLowerCase(s.charAt(right))) {
    return false;
}
```

### Mistake 4: Moving pointers before character check
```java
// ❌ WRONG: Move pointers first
left++;
right--;
if (s.charAt(left) != s.charAt(right)) { // Wrong positions!
    return false;
}

// ✅ CORRECT: Check first, then move
if (s.charAt(left) != s.charAt(right)) {
    return false;
}
left++;
right--;
```

### Mistake 5: Not handling empty string edge case
```java
// ❌ WRONG: No null/empty check
public boolean isPalindrome(String s) {
    int left = 0;
    int right = s.length() - 1; // Crash if s is null!
    // ...
}

// ✅ CORRECT: Handle edge cases
public boolean isPalindrome(String s) {
    if (s == null || s.length() == 0) {
        return true; // Empty string is palindrome
    }
    // ...
}
// Note: Problem guarantees s.length >= 1, but good practice
```

---

## Key Character Methods in Java

### Character.isLetterOrDigit(char c)
```java
// Returns true if c is a letter (A-Z, a-z) or digit (0-9)
Character.isLetterOrDigit('a'); // true
Character.isLetterOrDigit('5'); // true
Character.isLetterOrDigit(' '); // false
Character.isLetterOrDigit(','); // false
```

### Character.toLowerCase(char c)
```java
// Converts uppercase letter to lowercase
Character.toLowerCase('A'); // 'a'
Character.toLowerCase('a'); // 'a' (no change)
Character.toLowerCase('5'); // '5' (no change)
```

### Character.isLetter(char c)
```java
// Returns true only for letters (not digits)
Character.isLetter('a'); // true
Character.isLetter('5'); // false
```

### Character.isDigit(char c)
```java
// Returns true only for digits
Character.isDigit('5'); // true
Character.isDigit('a'); // false
```

### Manual ASCII Check (Alternative)
```java
// Check if alphanumeric without Character class
boolean isAlphanumeric(char c) {
    return (c >= 'A' && c <= 'Z') ||  // Uppercase letter
           (c >= 'a' && c <= 'z') ||  // Lowercase letter
           (c >= '0' && c <= '9');    // Digit
}

// Convert to lowercase manually
char toLowerCase(char c) {
    if (c >= 'A' && c <= 'Z') {
        return (char)(c + 32); // ASCII: 'A'=65, 'a'=97, diff=32
    }
    return c;
}
```

---

## Complexity Analysis

### Time Complexity: O(n)
- We traverse the string once with two pointers
- Each character is visited at most once
- Character validation and comparison are O(1) operations
- Even with inner while loops, each index is processed once
- Total: O(n) where n = string length

### Space Complexity: O(1)
- Only use two pointer variables (left, right)
- No additional data structures created
- No recursion stack
- String itself doesn't count toward space complexity (input)

### Comparison with Other Approaches:

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Two Pointers (Optimal) | O(n) | O(1) | ✅ Best approach |
| Create Cleaned String | O(n) | O(n) | Simpler but uses space |
| Regex | O(n) | O(n) | Slow due to regex overhead |
| Reverse String | O(n) | O(n) | Simple but not optimal |

---

## When to Use This Pattern

Use **Two Pointers (Convergent)** for palindrome checking when:
1. ✅ Need to check **symmetry** (same forward/backward)
2. ✅ Want **O(1) space** solution
3. ✅ Can compare elements from both ends
4. ✅ Need to **skip/filter** certain characters on-the-fly

**Similar Problems**:
- Valid Palindrome II (can delete one character)
- Palindrome Linked List
- Longest Palindromic Substring (different approach)
- Valid Palindrome III (can delete k characters)

---

## Comparison: Two Pointers vs String Manipulation

| Aspect | Two Pointers | String Building |
|--------|--------------|-----------------|
| Space | O(1) | O(n) |
| Time | O(n) | O(n) |
| Code Complexity | Moderate | Simple |
| Interview Preference | ✅ Preferred | ❌ Not optimal |
| Real-world Use | Memory-constrained | Quick prototyping |

**Interview Tip**: Always mention the space-optimal solution first, then offer simpler alternatives if asked.

---

## Practice Variations

After mastering this problem, try these progressions:

1. **Valid Palindrome II** (Medium) - Can remove at most one character
2. **Palindrome Linked List** (Easy) - Apply same concept to linked list
3. **Longest Palindromic Substring** (Medium) - Find longest palindrome in string
4. **Palindrome Permutation** (Easy) - Check if string can form palindrome
5. **Valid Palindrome III** (Hard) - Can remove at most k characters

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Constraints** (30 seconds)
> "Let me clarify: we consider only alphanumeric characters and ignore case, correct? 
> Empty string is a palindrome? Can the string be null?"

**Step 2: Explain Approach** (1 minute)
> "I'll use the two pointers technique - one starting from the left and one from the right. 
> I'll skip non-alphanumeric characters and compare valid characters case-insensitively. 
> This gives us O(n) time with O(1) space instead of creating a new cleaned string."

**Step 3: Mention Complexity** (30 seconds)
> "This is O(n) time since we traverse the string once, and O(1) space since we only 
> use two pointer variables. The alternative of building a cleaned string would use O(n) space."

**Step 4: Walk Through Example** (1-2 minutes)
> "For 'A man, a plan': we compare 'A' with last 'n', both alphanumeric, convert to 
> lowercase 'a' and 'n', they don't match... wait, let me trace more carefully..."

**Step 5: Code** (3-5 minutes)
- Start with the structure (pointers, while loop)
- Add character skipping logic
- Add comparison logic
- Handle edge cases

**Step 6: Test** (1-2 minutes)
```java
// Test with given examples
isPalindrome("A man, a plan, a canal: Panama") → true
isPalindrome("race a car") → false
isPalindrome(" ") → true

// Test edge cases
isPalindrome("a") → true
isPalindrome("ab") → false
```

### Expected Follow-up Questions:

**Q**: "What if we need to check for palindrome considering only letters (ignore numbers)?"
**A**: "I'd modify the isLetterOrDigit check to use Character.isLetter instead"

**Q**: "Can you solve it without using the Character class?"
**A**: "Yes, I can check ASCII values manually" (show Approach 3)

**Q**: "What if the string is very large and mostly non-alphanumeric?"
**A**: "My solution is already optimal - we skip non-alphanumeric in O(1) per character"

**Q**: "How would you handle Unicode characters?"
**A**: "Character.isLetterOrDigit already handles Unicode. For manual ASCII checks, 
we'd need to expand the range or use Unicode categories"

---

## Visual Debugging Tips

### When Stuck, Visualize:
```
String: "A,b:a"

Step-by-step:
"A,b:a"
 ↑   ↑
 L   R

L='A' (valid), R='a' (valid)
Compare: 'a' == 'a' ✓
L++, R--

"A,b:a"
   ↑↑
   LR

L=',' (skip), R='b' (valid)
L++

"A,b:a"
   ↑↑
   LR

L='b' (valid), R='b' (valid)
Compare: 'b' == 'b' ✓
L++, R--

Now left > right → STOP → return true
```

---

## Complete Solution with Comments

```java
class Solution {
    /**
     * Checks if string is a palindrome after filtering non-alphanumeric
     * and converting to lowercase.
     * 
     * @param s Input string to check
     * @return true if palindrome, false otherwise
     * 
     * Time Complexity: O(n) - single pass through string
     * Space Complexity: O(1) - only use two pointer variables
     */
    public boolean isPalindrome(String s) {
        // Edge case: null or empty string (though problem guarantees length >= 1)
        if (s == null || s.length() == 0) {
            return true;
        }
        
        // Initialize pointers at both ends
        int left = 0;
        int right = s.length() - 1;
        
        // Move pointers toward center
        while (left < right) {
            // Skip non-alphanumeric characters from left
            // Check left < right to prevent going out of bounds
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            
            // Skip non-alphanumeric characters from right
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            
            // Compare valid characters (case-insensitive)
            char leftChar = Character.toLowerCase(s.charAt(left));
            char rightChar = Character.toLowerCase(s.charAt(right));
            
            if (leftChar != rightChar) {
                return false; // Mismatch found
            }
            
            // Move both pointers toward center
            left++;
            right--;
        }
        
        // All characters matched
        return true;
    }
}
```

---

## One-Liner Solution (For Fun, Not Interview!)

```java
// Using streams and regex (NOT recommended for interviews)
public boolean isPalindrome(String s) {
    String clean = s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
    return clean.equals(new StringBuilder(clean).reverse().toString());
}
```

---

## Key Takeaways

1. ✅ **Two pointers convergent** is perfect for palindrome checking
2. ✅ **Skip invalid characters** on-the-fly for O(1) space
3. ✅ Always check **bounds** in inner while loops (`left < right`)
4. ✅ **Case-insensitive** comparison is critical
5. ✅ **Character.isLetterOrDigit()** handles both letters and numbers
6. ✅ **Character.toLowerCase()** for case conversion
7. ✅ Inner while loops don't increase time complexity - still O(n)
8. ✅ This pattern extends to many symmetry-checking problems
9. ✅ Always prefer **O(1) space** solution in interviews
10. ✅ Trace through examples carefully to avoid off-by-one errors

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (<20 minutes for Easy)
- [ ] Try both with and without Character class
- [ ] Attempt without looking at notes
- [ ] Explain solution out loud
- [ ] Review in 3 days (Day 4: 2026-02-01)
- [ ] Move to next problem: Two Sum II (Sorted)

---

**Pattern Learned**: Two Pointers (Convergent) for Palindrome ✅  
**Difficulty**: Easy  
**Time to Master**: 20-25 minutes  
**Week 2, Day 1**: Complete! 🎉

Next: Two Sum II - applying two pointers on sorted array for pair finding!
