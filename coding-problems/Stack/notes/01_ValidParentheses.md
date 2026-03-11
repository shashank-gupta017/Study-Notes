# Valid Parentheses (Easy)

## Problem Statement
Given a string `s` containing just the characters `'('`, `')'`, `'{'`, `'}'`, `'['` and `']'`, determine if the input string is valid.

An input string is valid if:
1. Open brackets must be closed by the same type of brackets.
2. Open brackets must be closed in the correct order.
3. Every close bracket has a corresponding open bracket of the same type.

**LeetCode Link**: [20. Valid Parentheses](https://leetcode.com/problems/valid-parentheses/)

---

## Examples

### Example 1:
```
Input: s = "()"
Output: true
Explanation: The string contains a matching pair of parentheses.
```

### Example 2:
```
Input: s = "()[]{}"
Output: true
Explanation: All three types of brackets are properly matched.
```

### Example 3:
```
Input: s = "(]"
Output: false
Explanation: Mismatched bracket types.
```

### Example 4:
```
Input: s = "([)]"
Output: false
Explanation: Brackets are not closed in the correct order.
```

### Example 5:
```
Input: s = "{[]}"
Output: true
Explanation: Nested brackets are properly matched.
```

---

## Constraints
- `1 <= s.length <= 10^4`
- `s` consists of parentheses only: `'(){}[]'`

---

## Pattern Recognition

This is a **Stack (Matching Pairs)** problem because:
1. We need to match **opening and closing** brackets
2. The **most recent unmatched** opening bracket should match the closing bracket (LIFO)
3. **Stack** is perfect for Last-In-First-Out matching
4. We can **push** opening brackets and **pop** to match closing brackets

**Key Insight**: 
- Opening brackets: `(`, `{`, `[` → **Push** onto stack
- Closing brackets: `)`, `}`, `]` → **Pop** and check if it matches

Stack naturally handles the "most recent" matching requirement!

---

## Approaches

### Approach 1: Brute Force with String Replacement (NOT RECOMMENDED)
**Idea**: Repeatedly remove matching pairs until string is empty or no more removals possible.

```java
class Solution {
    public boolean isValid(String s) {
        // Keep removing matching pairs
        while (s.contains("()") || s.contains("{}") || s.contains("[]")) {
            s = s.replace("()", "");
            s = s.replace("{}", "");
            s = s.replace("[]", "");
        }
        
        // Valid if string becomes empty
        return s.isEmpty();
    }
}
```

**Time Complexity**: O(n²) - multiple passes, string operations
**Space Complexity**: O(n) - string manipulation creates new strings
**Problem**: Very inefficient, fails on large inputs!

---

### Approach 2: Stack with HashMap (OPTIMAL) ⭐
**Idea**: Use stack to track opening brackets, use map for matching pairs.

```java
import java.util.*;

class Solution {
    public boolean isValid(String s) {
        // Map closing brackets to their opening counterparts
        Map<Character, Character> pairs = new HashMap<>();
        pairs.put(')', '(');
        pairs.put('}', '{');
        pairs.put(']', '[');
        
        Stack<Character> stack = new Stack<>();
        
        for (char c : s.toCharArray()) {
            if (pairs.containsKey(c)) {
                // Closing bracket
                // Check if stack is empty or top doesn't match
                if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                    return false;
                }
            } else {
                // Opening bracket - push onto stack
                stack.push(c);
            }
        }
        
        // Valid if all brackets matched (stack empty)
        return stack.isEmpty();
    }
}
```

**Time Complexity**: O(n) - single pass through string
**Space Complexity**: O(n) - stack can hold n/2 opening brackets
**Why Optimal**: Linear time, minimal operations per character

---

### Approach 3: Stack without HashMap (Cleaner for Small Set)
**Idea**: Direct if-else checks for matching, slightly more readable.

```java
import java.util.*;

class Solution {
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        
        for (char c : s.toCharArray()) {
            // Push opening brackets
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } 
            // Handle closing brackets
            else {
                // Empty stack means no matching opening bracket
                if (stack.isEmpty()) {
                    return false;
                }
                
                char top = stack.pop();
                
                // Check if brackets match
                if (c == ')' && top != '(') return false;
                if (c == '}' && top != '{') return false;
                if (c == ']' && top != '[') return false;
            }
        }
        
        // Valid if all brackets matched (stack empty)
        return stack.isEmpty();
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(n) - stack storage
**Note**: More verbose but easier to understand

---

### Approach 4: Deque (Modern Java)
**Idea**: Use ArrayDeque instead of Stack (more efficient).

```java
import java.util.*;

class Solution {
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        Map<Character, Character> pairs = Map.of(
            ')', '(',
            '}', '{',
            ']', '['
        );
        
        for (char c : s.toCharArray()) {
            if (pairs.containsKey(c)) {
                // Closing bracket
                if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                    return false;
                }
            } else {
                // Opening bracket
                stack.push(c);
            }
        }
        
        return stack.isEmpty();
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(n) - deque storage
**Note**: ArrayDeque is more efficient than Stack in Java

---

## Detailed Walkthrough (Approach 2: Stack with HashMap)

### Example: s = "([{}])"

```
Initial: stack = [], pairs = {')':'(', '}':'{', ']':'['}

Step 1: c = '('
  Not a closing bracket (not in pairs map)
  → Opening bracket, push to stack
  stack = ['(']

Step 2: c = '['
  Not a closing bracket
  → Opening bracket, push to stack
  stack = ['(', '[']

Step 3: c = '{'
  Not a closing bracket
  → Opening bracket, push to stack
  stack = ['(', '[', '{']

Step 4: c = '}'
  Closing bracket (in pairs map)
  pairs['}'] = '{'
  Stack not empty, pop: top = '{'
  Check: top == '{' ✓ Match!
  stack = ['(', '[']

Step 5: c = ']'
  Closing bracket
  pairs[']'] = '['
  Stack not empty, pop: top = '['
  Check: top == '[' ✓ Match!
  stack = ['(']

Step 6: c = ')'
  Closing bracket
  pairs[')'] = '('
  Stack not empty, pop: top = '('
  Check: top == '(' ✓ Match!
  stack = []

Final Check: stack.isEmpty() = true ✓
Result: true (valid)
```

---

### Visual Representation

```
String: "([{}])"

Processing with Stack:

'(' → Push        Stack: [(]
'[' → Push        Stack: [(, []
'{' → Push        Stack: [(, [, {]
'}' → Pop & Match Stack: [(, [] (matched { with })
']' → Pop & Match Stack: [(]    (matched [ with ])
')' → Pop & Match Stack: []     (matched ( with ))

Final: Stack is empty ✓ Valid!

---

Example of INVALID: "(]"

'(' → Push        Stack: [(]
']' → Pop & Match Stack: []
      Popped: '('
      Expected for ']': '['
      Got: '('
      ✗ Mismatch! Return false

---

Example of INVALID: "((("

'(' → Push        Stack: [(]
'(' → Push        Stack: [(, (]
'(' → Push        Stack: [(, (, (]

Final: Stack not empty ✗ Invalid!
(Unmatched opening brackets)

---

Example of INVALID: ")))"

')' → Try to pop  Stack: []
      Stack is empty!
      ✗ No matching opening bracket
      Return false immediately
```

---

## Edge Cases to Consider

```java
// Test Case 1: Empty string
Input: s = ""
Output: true
// By definition, empty string is valid (no brackets to match)
// Note: Constraints say length >= 1, but good to handle

// Test Case 2: Single opening bracket
Input: s = "("
Output: false
// Unmatched opening bracket

// Test Case 3: Single closing bracket
Input: s = ")"
Output: false
// No matching opening bracket

// Test Case 4: All opening brackets
Input: s = "((("
Output: false
// Stack not empty at end

// Test Case 5: All closing brackets
Input: s = ")))"
Output: false
// Stack empty when trying to pop

// Test Case 6: Correct nesting
Input: s = "{[()]}"
Output: true
// Proper nesting

// Test Case 7: Wrong order
Input: s = "([)]"
Output: false
// Interleaved but wrong order

// Test Case 8: Long valid string
Input: s = "()[]{}()[]{}()[]{}()"
Output: true
// Multiple correct pairs

// Test Case 9: Complex nesting
Input: s = "((((((((((()))))))))))"
Output: true
// Deep nesting, all matched

// Test Case 10: Mismatched types
Input: s = "((((()))))"
Output: false
// 5 opening but 6 closing

// Test Case 11: Mixed valid
Input: s = "{[({[()]})]}"
Output: true
// Complex but valid nesting

// Test Case 12: Extra closing
Input: s = "())"
Output: false
// Extra closing bracket
```

---

## Common Mistakes to Avoid

### Mistake 1: Not Checking if Stack is Empty Before Pop
```java
// ❌ WRONG: Will throw EmptyStackException
for (char c : s.toCharArray()) {
    if (isClosing(c)) {
        char top = stack.pop(); // What if stack is empty?
        if (top != getMatch(c)) return false;
    }
}

// ✅ CORRECT: Check before popping
for (char c : s.toCharArray()) {
    if (isClosing(c)) {
        if (stack.isEmpty()) return false; // Check first!
        char top = stack.pop();
        if (top != getMatch(c)) return false;
    }
}
```

### Mistake 2: Not Checking if Stack is Empty at End
```java
// ❌ WRONG: Forgetting to check final state
for (char c : s.toCharArray()) {
    // ... push and pop logic
}
return true; // Wrong! Stack might have unmatched opening brackets

// ✅ CORRECT: Check if all brackets matched
for (char c : s.toCharArray()) {
    // ... push and pop logic
}
return stack.isEmpty(); // All brackets must be matched
```

### Mistake 3: Pushing Closing Brackets
```java
// ❌ WRONG: Pushing all brackets
for (char c : s.toCharArray()) {
    stack.push(c); // Don't push closing brackets!
    // Then try to match...
}

// ✅ CORRECT: Only push opening brackets
for (char c : s.toCharArray()) {
    if (isOpening(c)) {
        stack.push(c);
    } else {
        // Match with stack top
    }
}
```

### Mistake 4: Wrong Mapping Direction
```java
// ❌ WRONG: Mapping opening to closing
Map<Character, Character> pairs = new HashMap<>();
pairs.put('(', ')'); // Backwards!
pairs.put('{', '}');
pairs.put('[', ']');

// Then checking: if (pairs.get(top) == c) ... // Confusing logic!

// ✅ CORRECT: Map closing to opening
Map<Character, Character> pairs = new HashMap<>();
pairs.put(')', '(');
pairs.put('}', '{');
pairs.put(']', '[');

// Then: if (stack.pop() != pairs.get(c)) ... // Clear logic!
```

### Mistake 5: Returning False Immediately on Empty Stack at End
```java
// ❌ WRONG: Empty string case
if (s.isEmpty()) return false; // Empty should be valid!

// ✅ CORRECT: Empty string is valid
if (s.isEmpty()) return true;
// Or just let it return stack.isEmpty() naturally
```

### Mistake 6: Using Equality Operator Wrong
```java
// ❌ WRONG: Comparing with wrong bracket
if (c == ')' && top != ')') return false; // Should compare with '('!

// ✅ CORRECT: Compare closing with its opening counterpart
if (c == ')' && top != '(') return false;
```

---

## Why Stack is the Perfect Data Structure

### Stack Properties Match Problem Requirements:

**1. LIFO (Last In, First Out)**
```
Most recent opening bracket should match current closing bracket
Example: "([)]"
  When we see ']', we should match with '[' (most recent opening)
  But '[' is not the most recent - '(' is!
  Stack naturally gives us most recent unmatched opening
```

**2. Push Operation**
```
When we see opening bracket, save it for later matching
Push: O(1) - perfect for tracking openings
```

**3. Pop Operation**
```
When we see closing bracket, match with most recent opening
Pop: O(1) - perfect for retrieving and removing
```

**4. isEmpty Check**
```
At end, all brackets should be matched
Empty stack = all matched
Non-empty = some opening brackets never closed
```

### Why Not Other Data Structures?

**Queue**: FIFO doesn't match our needs
```
"([)]"
With queue: '(' enters first, would match first
But ']' should match '[', not '('!
```

**Array**: Would need to track index, less clean
```
Possible but messy: track last unmatched position
Need to handle removal from middle
```

**HashMap**: Can't track order
```
Can count brackets but not order
"([)]" would seem balanced but isn't!
```

---

## Optimization Techniques

### Optimization 1: Early Exit on Odd Length
```java
// Odd length can never be valid (each opening needs a closing)
if (s.length() % 2 != 0) {
    return false;
}
```

### Optimization 2: Use Array Instead of Stack for Small Strings
```java
// For known small size, array can be slightly faster
char[] stack = new char[s.length()];
int top = -1;

for (char c : s.toCharArray()) {
    if (isOpening(c)) {
        stack[++top] = c;
    } else {
        if (top == -1 || stack[top--] != getMatch(c)) {
            return false;
        }
    }
}

return top == -1;
```

### Optimization 3: Switch Statement for Matching
```java
// Switch can be faster than HashMap for small fixed set
switch(c) {
    case ')':
        if (stack.isEmpty() || stack.pop() != '(') return false;
        break;
    case '}':
        if (stack.isEmpty() || stack.pop() != '{') return false;
        break;
    case ']':
        if (stack.isEmpty() || stack.pop() != '[') return false;
        break;
    default:
        stack.push(c); // Opening bracket
}
```

---

## Complexity Analysis

### Time Complexity: O(n)
- **Single pass**: We iterate through string once
- **Per character**: O(1) operations (push, pop, map lookup)
- **Total**: O(n) where n = string length
- **Optimal**: Must examine each character at least once

### Space Complexity: O(n)
- **Worst case**: All opening brackets → O(n/2) = O(n)
- **Example**: "(((((" requires stack of size 5
- **HashMap**: O(1) - only 3 pairs, constant
- **Total**: O(n) dominated by stack

### Best/Worst Cases:

**Best Case**: O(1)
- First char is closing bracket with empty stack → return false immediately
- Example: ")abc"

**Worst Case**: O(n)
- All opening brackets followed by all closing → process entire string
- Example: "((((())))) "

---

## Pattern: Stack for Matching/Pairing Problems

### General Template:
```java
public boolean isValidPattern(String s) {
    Stack<Character> stack = new Stack<>();
    Map<Character, Character> pairs = createPairsMap();
    
    for (char c : s.toCharArray()) {
        if (isClosing(c)) {
            // Check if we can match
            if (stack.isEmpty() || stack.pop() != pairs.get(c)) {
                return false;
            }
        } else {
            // Save opening element
            stack.push(c);
        }
    }
    
    // Check if all matched
    return stack.isEmpty();
}
```

### When to Use This Pattern:
- ✅ Matching pairs (parentheses, quotes, tags)
- ✅ Nested structures (HTML, XML validation)
- ✅ Expression evaluation
- ✅ Backtracking with most recent state

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Constraints** (30 seconds)
> "The string contains only bracket characters: (), {}, []. I need to check if they're properly matched and in correct order. Can I confirm: empty string is valid? The constraints say length >= 1, so I'll handle that case too."

**Step 2: Explain Approach** (1-2 minutes)
> "I'll use a Stack because of the Last-In-First-Out matching requirement:
> 1. When I see an opening bracket: (, {, [ → push onto stack
> 2. When I see a closing bracket: ), }, ] → pop from stack and verify it matches
> 3. At the end, stack must be empty (all brackets matched)
> Time: O(n), Space: O(n)"

**Step 3: Walk Through Example** (1-2 minutes)
```java
// Example: "([{}])"
// Push '(' → Push '[' → Push '{'
// See '}' → pop '{' (match!) → continue
// See ']' → pop '[' (match!) → continue
// See ')' → pop '(' (match!) → stack empty → valid!
```

**Step 4: Discuss Edge Cases** (30 seconds)
> "Edge cases:
> - Empty string: valid (no brackets to match)
> - Single bracket: invalid (unmatched)
> - All opening: invalid (stack not empty)
> - All closing: invalid (stack empty when trying to pop)
> - Wrong order: "([)]" → invalid (pop '[' but need '(' for ')')"

**Step 5: Code** (5-10 minutes)
- Create pairs map (closing → opening)
- Initialize stack
- Iterate through string
- Handle opening vs closing brackets
- Check stack empty at end
- Test with example

### Expected Follow-up Questions:

**Q**: "What if the string contains other characters too?"
**A**: "I'd skip non-bracket characters or define which characters are valid brackets. The matching logic stays the same, just filter input first."

**Q**: "Can you validate HTML/XML tags?"
**A**: "Yes! Similar approach but match full tag names, not just single characters. Push opening tags, pop and compare entire tag string when seeing closing tags."

**Q**: "What about finding the longest valid parentheses substring?"
**A**: "Different problem - track indices in stack instead of characters. When valid pair found, calculate length. LeetCode #32."

**Q**: "How would you generate all valid parentheses?"
**A**: "That's backtracking (LeetCode #22). Build string recursively, tracking count of opening/closing brackets. Add '(' if count < n, add ')' if closing < opening."

**Q**: "What if we need to find the minimum additions to make valid?"
**A**: "Track unmatched opening brackets (stack size at end) and unmatched closing brackets (when stack empty during processing). Sum = minimum additions needed."

---

## Complete Solution with Comments

```java
import java.util.*;

class Solution {
    /**
     * Validates if a string of brackets is properly matched.
     * 
     * Approach: Stack (LIFO matching)
     * - Opening brackets: push onto stack
     * - Closing brackets: pop and verify match
     * - Final: stack must be empty
     * 
     * @param s String containing only bracket characters
     * @return true if brackets are properly matched, false otherwise
     * 
     * Time Complexity: O(n) - single pass through string
     * Space Complexity: O(n) - stack can hold up to n/2 opening brackets
     */
    public boolean isValid(String s) {
        // Edge case: odd length can never be valid
        if (s.length() % 2 != 0) {
            return false;
        }
        
        // Map closing brackets to their opening counterparts
        // This makes matching logic cleaner
        Map<Character, Character> pairs = new HashMap<>();
        pairs.put(')', '(');
        pairs.put('}', '{');
        pairs.put(']', '[');
        
        // Stack to track opening brackets (waiting to be matched)
        Stack<Character> stack = new Stack<>();
        
        for (char c : s.toCharArray()) {
            if (pairs.containsKey(c)) {
                // CLOSING BRACKET CASE
                // Must have matching opening bracket on stack
                
                if (stack.isEmpty()) {
                    // No opening bracket to match with
                    return false;
                }
                
                char top = stack.pop();
                
                // Check if popped opening bracket matches current closing bracket
                if (top != pairs.get(c)) {
                    return false; // Mismatched bracket types
                }
                
            } else {
                // OPENING BRACKET CASE
                // Save for later matching
                stack.push(c);
            }
        }
        
        // Valid only if all brackets were matched
        // Non-empty stack means some opening brackets were never closed
        return stack.isEmpty();
    }
}
```

---

## Alternative Implementation (Without HashMap)

```java
import java.util.*;

class Solution {
    public boolean isValid(String s) {
        if (s.length() % 2 != 0) return false;
        
        Stack<Character> stack = new Stack<>();
        
        for (char c : s.toCharArray()) {
            // Opening brackets - push
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
                continue;
            }
            
            // Closing brackets - pop and match
            if (stack.isEmpty()) {
                return false; // No matching opening
            }
            
            char top = stack.pop();
            
            // Check match for each type
            if (c == ')' && top != '(') return false;
            if (c == '}' && top != '{') return false;
            if (c == ']' && top != '[') return false;
        }
        
        return stack.isEmpty();
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public static void main(String[] args) {
    Solution solution = new Solution();
    
    // Test Case 1: Simple valid
    System.out.println(solution.isValid("()"));        // true
    
    // Test Case 2: All types
    System.out.println(solution.isValid("()[]{}"));    // true
    
    // Test Case 3: Nested
    System.out.println(solution.isValid("{[()]}"));    // true
    
    // Test Case 4: Mismatched
    System.out.println(solution.isValid("(]"));        // false
    
    // Test Case 5: Wrong order
    System.out.println(solution.isValid("([)]"));      // false
    
    // Test Case 6: Extra closing
    System.out.println(solution.isValid("())"));       // false
    
    // Test Case 7: Extra opening
    System.out.println(solution.isValid("(()"));       // false
    
    // Test Case 8: Single bracket
    System.out.println(solution.isValid("("));         // false
}
```

---

## Key Takeaways

1. ✅ **Stack is perfect** for Last-In-First-Out matching problems
2. ✅ **Push opening brackets**, pop to match closing brackets
3. ✅ **Check stack empty** before popping (avoid exception)
4. ✅ **Check stack empty** at end (all brackets matched)
5. ✅ **HashMap for mapping** closing → opening (cleaner logic)
6. ✅ **O(n) time, O(n) space** is optimal
7. ✅ **Odd length strings** are always invalid (quick exit)
8. ✅ **Order matters**: "([)]" is invalid even though counts match
9. ✅ **Pattern extends** to HTML/XML validation, expression parsing
10. ✅ **Classic interview problem** - must know this pattern!

---

## Variations & Extensions

After mastering Valid Parentheses, try these related stack problems:

1. **Generate Parentheses** (Medium) - Backtracking to generate all valid combinations
2. **Longest Valid Parentheses** (Hard) - Find longest valid substring
3. **Minimum Add to Make Parentheses Valid** (Medium) - Count additions needed
4. **Remove Invalid Parentheses** (Hard) - Remove minimum to make valid
5. **Score of Parentheses** (Medium) - Calculate score based on nesting

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (10-15 minutes)
- [ ] Trace through "([{}])" step by step
- [ ] Understand why stack is perfect here
- [ ] Test all edge cases
- [ ] Try without HashMap for practice
- [ ] Practice explaining the LIFO matching concept
- [ ] Review in 2 days

---

**Pattern Mastered**: Stack (Matching Pairs) ✅  
**Difficulty**: Easy  
**Time to Master**: 15-20 minutes  
**Importance**: ⭐⭐⭐⭐⭐ Fundamental stack pattern

This is THE classic stack problem that teaches the matching pairs pattern. Master this and you'll recognize stack patterns in many other problems! 🚀
