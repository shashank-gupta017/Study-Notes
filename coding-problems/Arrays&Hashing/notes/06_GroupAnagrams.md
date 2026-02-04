# Group Anagrams (Medium)

## Problem Statement
Given an array of strings `strs`, group the **anagrams** together. You can return the answer in **any order**.

An **Anagram** is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

**LeetCode Link**: [49. Group Anagrams](https://leetcode.com/problems/group-anagrams/)

---

## Examples

### Example 1:
```
Input: strs = ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]

Explanation:
- "eat", "tea", "ate" are anagrams (same letters: a, e, t)
- "tan", "nat" are anagrams (same letters: a, n, t)
- "bat" stands alone
```

### Example 2:
```
Input: strs = [""]
Output: [[""]]
Explanation: Empty string forms its own group.
```

### Example 3:
```
Input: strs = ["a"]
Output: [["a"]]
Explanation: Single string forms its own group.
```

---

## Constraints
- `1 <= strs.length <= 10^4`
- `0 <= strs[i].length <= 100`
- `strs[i]` consists of lowercase English letters

---

## Pattern Recognition

This is a **HashMap + Grouping pattern** problem because:
1. We need to **group related items** (anagrams belong together)
2. We need a **unique identifier** for each group (anagram signature)
3. We need **O(1) lookup** to add items to existing groups
4. Order doesn't matter within or across groups

**Key Insight**: Anagrams have the same characters with the same frequencies. We can create a unique "signature" for each anagram group using either:
- **Sorted string**: "eat" → "aet", "tea" → "aet" (same signature!)
- **Frequency count**: "eat" → "#1#0#0#0#1#0...#1#..." (a=1, e=1, t=1)

**Mental Model**: Think of sorting students into teams based on their uniform color. Students with matching uniforms go to the same team - here, strings with matching character signatures go to the same group.

---

## Approaches

### Approach 1: Brute Force - Compare All Pairs
**Idea**: Compare every string with every other string to check if they're anagrams.

```java
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> result = new ArrayList<>();
        boolean[] used = new boolean[strs.length];
        
        for (int i = 0; i < strs.length; i++) {
            if (used[i]) continue;
            
            List<String> group = new ArrayList<>();
            group.add(strs[i]);
            used[i] = true;
            
            // Compare with all remaining strings
            for (int j = i + 1; j < strs.length; j++) {
                if (!used[j] && areAnagrams(strs[i], strs[j])) {
                    group.add(strs[j]);
                    used[j] = true;
                }
            }
            result.add(group);
        }
        
        return result;
    }
    
    private boolean areAnagrams(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        char[] arr1 = s1.toCharArray();
        char[] arr2 = s2.toCharArray();
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        return Arrays.equals(arr1, arr2);
    }
}
```

**Time Complexity**: O(n² × k log k) where n = number of strings, k = max string length
**Space Complexity**: O(n × k) for result
**Problem**: O(n²) comparisons with sorting each time is too slow!

---

### Approach 2: HashMap with Sorted String Key ⭐
**Idea**: Use sorted string as a unique key. All anagrams produce the same sorted key.

```java
import java.util.*;

class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        // Map: sorted string -> list of original strings
        Map<String, List<String>> map = new HashMap<>();
        
        for (String str : strs) {
            // Create key by sorting characters
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            
            // Add to group (create group if doesn't exist)
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(str);
        }
        
        // Return all groups as a list
        return new ArrayList<>(map.values());
    }
}
```

**Time Complexity**: O(n × k log k) - n strings, each sorted in O(k log k)
**Space Complexity**: O(n × k) - storing all strings and keys
**Pros**: Simple, intuitive, works for any character set
**Cons**: Sorting is expensive for long strings

---

### Approach 3: HashMap with Frequency Count Key (OPTIMAL)
**Idea**: Use character frequency as key to avoid sorting overhead.

```java
import java.util.*;

class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        
        for (String str : strs) {
            // Count character frequencies
            int[] count = new int[26];
            for (char c : str.toCharArray()) {
                count[c - 'a']++;
            }
            
            // Build key from frequency array
            // Use delimiter to avoid collisions
            StringBuilder keyBuilder = new StringBuilder();
            for (int i = 0; i < 26; i++) {
                keyBuilder.append('#');
                keyBuilder.append(count[i]);
            }
            String key = keyBuilder.toString();
            
            // Group by key
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(str);
        }
        
        return new ArrayList<>(map.values());
    }
}
```

**Time Complexity**: O(n × k) - n strings, O(k) to count + O(26) to build key
**Space Complexity**: O(n × k)
**Pros**: Optimal time complexity, no sorting needed
**Cons**: More code, only works for lowercase letters without modification

---

### Approach 4: Clean Version with computeIfAbsent (BEST) ⭐
**Idea**: Use Java 8's `computeIfAbsent` for cleaner code.

```java
import java.util.*;

class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        
        for (String str : strs) {
            // Create sorted key
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            
            // computeIfAbsent creates list if key doesn't exist
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
        }
        
        return new ArrayList<>(map.values());
    }
}
```

**Why this is better**:
- Cleaner, more readable code
- Single line for grouping logic
- No manual null checking needed

---

### Approach 5: Using Arrays.toString() for Key
**Idea**: Use `Arrays.toString()` to convert frequency array to string key.

```java
import java.util.*;

class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        
        for (String str : strs) {
            // Count frequencies
            int[] count = new int[26];
            for (char c : str.toCharArray()) {
                count[c - 'a']++;
            }
            
            // Arrays.toString creates: "[1, 0, 0, 0, 1, ..., 1, 0, 0]"
            // This is collision-safe and very clean
            String key = Arrays.toString(count);
            
            map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
        }
        
        return new ArrayList<>(map.values());
    }
}
```

**Pros**: Cleanest code, no collision worries, optimal time
**Cons**: Slightly more space for key strings with brackets and commas

---

## Detailed Walkthrough (Approach 2: Sorted Key)

Let's trace `strs = ["eat", "tea", "tan", "ate", "nat", "bat"]`

```
Initial State:
map = {}
strs = ["eat", "tea", "tan", "ate", "nat", "bat"]

Step 1: Process "eat"
  Sort "eat" → "aet"
  map = {"aet": ["eat"]}

Step 2: Process "tea"
  Sort "tea" → "aet"  (same key as "eat"!)
  map = {"aet": ["eat", "tea"]}

Step 3: Process "tan"
  Sort "tan" → "ant"  (new key)
  map = {"aet": ["eat", "tea"],
         "ant": ["tan"]}

Step 4: Process "ate"
  Sort "ate" → "aet"  (matches "eat" and "tea")
  map = {"aet": ["eat", "tea", "ate"],
         "ant": ["tan"]}

Step 5: Process "nat"
  Sort "nat" → "ant"  (matches "tan")
  map = {"aet": ["eat", "tea", "ate"],
         "ant": ["tan", "nat"]}

Step 6: Process "bat"
  Sort "bat" → "abt"  (new key)
  map = {"aet": ["eat", "tea", "ate"],
         "ant": ["tan", "nat"],
         "abt": ["bat"]}

Final Result:
Convert map.values() to list:
[
  ["eat", "tea", "ate"],
  ["tan", "nat"],
  ["bat"]
]
```

---

## Visual: Key Generation Methods

### Example: "eat", "tea", "ate"

**Method 1: Sorted String**
```
"eat" → sort → "aet"
"tea" → sort → "aet"  ✓ Same key
"ate" → sort → "aet"  ✓ Same key
```

**Method 2: Frequency String (with delimiter)**
```
"eat" → count → "#1#0#0#0#1#0#0#0#0#0#0#0#0#0#0#0#0#0#0#1#0#0#0#0#0#0"
                  a=1 b=0 c=0 d=0 e=1..................t=1..........

"tea" → count → "#1#0#0#0#1#0#0#0#0#0#0#0#0#0#0#0#0#0#0#1#0#0#0#0#0#0"
                  ✓ Same key

"ate" → count → "#1#0#0#0#1#0#0#0#0#0#0#0#0#0#0#0#0#0#0#1#0#0#0#0#0#0"
                  ✓ Same key
```

**Method 3: Arrays.toString()**
```
"eat" → count → "[1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0]"
"tea" → count → "[1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0]"
                  ✓ Same key (automatic formatting with brackets)
```

---

## Deep Dive: Why Delimiters Matter

### Collision Risk Without Delimiters:
```java
// WRONG - Can cause collisions!
StringBuilder key = new StringBuilder();
for (int i = 0; i < 26; i++) {
    key.append(count[i]);  // No delimiter!
}

// Example collision:
// "ab" → count = [1,1,0,0,...] → key = "110000..."
// "k"  → count = [0,0,0,0,0,0,0,0,0,0,11,0,...] → key = "00000000001100..." 
// If we concatenate: could be confused as "110..." vs "0...110..."
```

### Safe with Delimiters:
```java
// CORRECT - No collisions possible
StringBuilder key = new StringBuilder();
for (int i = 0; i < 26; i++) {
    key.append('#');
    key.append(count[i]);
}

// "ab" → "#1#1#0#0#0..."
// "k"  → "#0#0#0#0#0#0#0#0#0#0#11#0..."
// These are clearly different!
```

---

## Edge Cases to Consider

```java
// Test Case 1: Empty strings
Input: [""]
Output: [[""]]
// Empty string is valid, forms its own group

// Test Case 2: Single string
Input: ["a"]
Output: [["a"]]

// Test Case 3: All anagrams
Input: ["abc", "bca", "cab"]
Output: [["abc", "bca", "cab"]]
// All in one group

// Test Case 4: No anagrams
Input: ["a", "b", "c"]
Output: [["a"], ["b"], ["c"]]
// Each forms its own group

// Test Case 5: Different lengths
Input: ["abc", "ab", "abcd"]
Output: [["abc"], ["ab"], ["abcd"]]
// Different lengths cannot be anagrams

// Test Case 6: Duplicate strings
Input: ["abc", "abc"]
Output: [["abc", "abc"]]
// Same string counts as anagram of itself

// Test Case 7: Mixed case (if constraint allowed)
// Note: Problem states lowercase only, but good to think about
Input: ["Eat", "eat"]
// Without case handling: [["Eat"], ["eat"]]
// With case handling: [["Eat", "eat"]]
```

---

## Common Mistakes to Avoid

### Mistake 1: Not using HashMap for grouping
```java
// ❌ WRONG - Trying to use List/Array
List<List<String>> result = new ArrayList<>();
// How do we find existing groups? O(n) search each time!

// ✅ CORRECT - HashMap for O(1) group lookup
Map<String, List<String>> map = new HashMap<>();
```

### Mistake 2: Creating keys without delimiters
```java
// ❌ WRONG - Collision risk
String key = "";
for (int c : count) key += c;

// ✅ CORRECT - Use delimiter or Arrays.toString()
String key = Arrays.toString(count);
```

### Mistake 3: Not creating new ArrayList when needed
```java
// ❌ WRONG - Creates list every time
for (String str : strs) {
    String key = getKey(str);
    List<String> list = new ArrayList<>();  // Wasteful!
    list.add(str);
    map.put(key, list);
}

// ✅ CORRECT - Check if key exists first
map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
```

### Mistake 4: Modifying input array
```java
// ❌ WRONG - Sorting changes the input
Arrays.sort(strs);  // Don't modify input!

// ✅ CORRECT - Only sort individual strings for keys
char[] chars = str.toCharArray();
Arrays.sort(chars);
```

---

## Optimization Techniques

### Technique 1: Early Length Grouping (Advanced)
```java
// Pre-group by length first (optional optimization)
Map<Integer, List<String>> lengthMap = new HashMap<>();
for (String str : strs) {
    lengthMap.computeIfAbsent(str.length(), k -> new ArrayList<>()).add(str);
}

// Then group within each length group
// Anagrams must have same length, so this reduces comparisons
```

### Technique 2: Use computeIfAbsent
```java
// Instead of:
if (!map.containsKey(key)) {
    map.put(key, new ArrayList<>());
}
List<String> list = map.get(key);
list.add(str);

// Use:
map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
```

### Technique 3: StringBuilder vs String concatenation
```java
// ❌ Inefficient
String key = "";
for (int i = 0; i < 26; i++) {
    key += "#" + count[i];  // Creates new String each time!
}

// ✅ Efficient
StringBuilder key = new StringBuilder();
for (int i = 0; i < 26; i++) {
    key.append('#').append(count[i]);
}
```

---

## Complexity Analysis

### Approach 2: Sorted Key
- **Time**: O(n × k log k)
  - n strings to process
  - Each string of length k takes O(k log k) to sort
  - O(k) to create String from char array
  - HashMap operations: O(1) average
- **Space**: O(n × k)
  - HashMap stores all n strings
  - Each key takes O(k) space

### Approach 3 & 5: Frequency Count
- **Time**: O(n × k)
  - n strings to process
  - Each string takes O(k) to count characters
  - Building key: O(26) = O(1) or O(k) for StringBuilder
  - HashMap operations: O(1) average
- **Space**: O(n × k)
  - HashMap stores all n strings
  - Keys are O(26) = O(1) each

**Winner**: Frequency count is asymptotically better for time!

---

## When to Use Each Approach

| Approach | When to Use | Trade-offs |
|----------|-------------|------------|
| Sorted Key | Interview-friendly, simpler to code | O(n × k log k) time |
| Frequency Count | Long strings, performance critical | More code, optimal O(n × k) |
| Arrays.toString() | Want clean code, optimal time | Slightly more space for key |

**Recommendation**: 
- **Start with Sorted Key** in interviews (simpler, less bug-prone)
- **Mention optimization** to frequency count if asked
- **Use computeIfAbsent** for clean code

---

## Related Problems

After mastering this problem, try:

1. **Valid Anagram** (Easy) - Foundation for this problem
2. **Find All Anagrams in String** (Medium) - Sliding window + frequency
3. **Strings Differ by One Character** (Medium) - Similar grouping logic
4. **Group Shifted Strings** (Medium) - Similar pattern with different key

---

## Interview Tips

### What to Say During Interview:

1. **Clarify Constraints**: 
   - "Can I assume lowercase letters only?"
   - "Is the order of groups important?"
   - "Can strings be empty?"

2. **Explain Approach**:
   - "I'll use a HashMap to group anagrams together"
   - "For the key, I can either sort the string or use character frequencies"

3. **Discuss Trade-offs**:
   - "Sorting is O(k log k) per string but simpler to implement"
   - "Frequency count is O(k) but requires more code"
   - "I'll go with [sorted/frequency] because..."

4. **Code Strategy**:
   - Write sorted approach first (faster to code)
   - Trace through one example
   - Mention optimization if time permits

5. **Edge Cases**:
   - "I'll handle empty strings"
   - "Different length strings can't be anagrams"

### Expected Follow-up Questions:

**Q**: "Can you optimize the time complexity?"
**A**: "Yes, use frequency count instead of sorting for O(n × k) instead of O(n × k log k)"

**Q**: "What if strings contain Unicode characters?"
**A**: "Use HashMap for frequency count instead of array, since we can't predict character range"

**Q**: "How does your solution handle duplicates?"
**A**: "Duplicate strings have the same key, so they naturally group together"

**Q**: "Can you make the code cleaner?"
**A**: "Yes, use computeIfAbsent to eliminate null checks"

---

## Comparison with Valid Anagram

| Aspect | Valid Anagram | Group Anagrams |
|--------|---------------|----------------|
| Input | 2 strings | Array of n strings |
| Output | Boolean | List of groups |
| Pattern | Frequency count | HashMap + Grouping |
| Key Structure | None needed | Sorted string or frequency |
| Time | O(k) | O(n × k) or O(n × k log k) |
| Complexity | Check equality | Create identifier + group |

**Connection**: Valid Anagram checks if two strings are anagrams. Group Anagrams extends this to find all anagram groups in an array.

---

## Complete Solution with Comments

```java
import java.util.*;

class Solution {
    /**
     * Groups anagrams together from array of strings.
     * 
     * @param strs Array of strings to group
     * @return List of anagram groups
     * 
     * Time Complexity: O(n × k log k) where n = number of strings, k = max string length
     * Space Complexity: O(n × k) for storing all strings and keys
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        // Edge case: null or empty input
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }
        
        // Map: anagram signature -> list of strings with that signature
        Map<String, List<String>> anagramGroups = new HashMap<>();
        
        for (String str : strs) {
            // Create signature by sorting characters
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String signature = new String(chars);
            
            // Add string to its anagram group
            // computeIfAbsent creates new list if key doesn't exist
            anagramGroups.computeIfAbsent(signature, k -> new ArrayList<>()).add(str);
        }
        
        // Return all groups as a list
        return new ArrayList<>(anagramGroups.values());
    }
}
```

### Alternative: Frequency Count Version

```java
import java.util.*;

class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) {
            return new ArrayList<>();
        }
        
        Map<String, List<String>> anagramGroups = new HashMap<>();
        
        for (String str : strs) {
            // Count character frequencies (lowercase a-z only)
            int[] charCount = new int[26];
            for (char c : str.toCharArray()) {
                charCount[c - 'a']++;
            }
            
            // Create unique signature using Arrays.toString()
            // Format: "[1, 0, 3, 0, 1, ...]" - no collision risk
            String signature = Arrays.toString(charCount);
            
            // Group strings by signature
            anagramGroups.computeIfAbsent(signature, k -> new ArrayList<>()).add(str);
        }
        
        return new ArrayList<>(anagramGroups.values());
    }
}
```

---

## Practice Checklist

- [ ] Implement sorted key approach
- [ ] Implement frequency count approach
- [ ] Understand why delimiters prevent collisions
- [ ] Test with all edge cases
- [ ] Can explain O(n × k) vs O(n × k log k) difference
- [ ] Practice using computeIfAbsent
- [ ] Time yourself: aim for <25 minutes

---

## Template for HashMap Grouping Pattern

```java
// Template: Group items by computed key
public List<List<TYPE>> groupItems(TYPE[] items) {
    Map<String, List<TYPE>> groups = new HashMap<>();
    
    for (TYPE item : items) {
        // Compute unique key for this item's group
        String key = computeKey(item);
        
        // Add item to its group (create group if needed)
        groups.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
    }
    
    // Return all groups
    return new ArrayList<>(groups.values());
}

private String computeKey(TYPE item) {
    // Create unique identifier for grouping
    // For anagrams: sort string or count frequencies
    // For other problems: use appropriate logic
}
```

---

## Key Takeaways

1. ✅ **HashMap excels at grouping** - O(1) lookup to find/create groups
2. ✅ **Sorted string** works as anagram signature - simple and intuitive
3. ✅ **Frequency count** is asymptotically faster - O(k) vs O(k log k)
4. ✅ **Use delimiters** or Arrays.toString() to avoid key collisions
5. ✅ **computeIfAbsent** makes code cleaner and more readable
6. ✅ This pattern appears in many "grouping by characteristic" problems

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (<25 minutes for Medium)
- [ ] Try both sorted and frequency approaches
- [ ] Attempt without looking at notes
- [ ] Review in 3 days (Day 9 from first attempt)
- [ ] Practice explaining approach out loud
- [ ] Ready for Week 1 Day 7 Review!

---

**Pattern Mastered**: HashMap + Grouping with Signature Key ✅  
**Difficulty**: Medium  
**Time to Master**: 45-60 minutes  
**Week 1 Progress**: 6/6 problems completed! 🎉

**Next**: Week 1, Day 7 - Review & Consolidation Day
