# Group Anagrams (Medium)

## Problem Statement
Given an array of strings `strs`, group the **anagrams** together. You can return the answer in **any order**.

An **Anagram** is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.

**LeetCode Link**: [49. Group Anagrams](https://leetcode.com/problems/group-anagrams/)

---

## Examples

### Example 1: Mixed Anagrams
```
Input: strs = ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]

Explanation:
- "eat", "tea", "ate" are anagrams (same letters: a, e, t)
- "tan", "nat" are anagrams (same letters: a, n, t)
- "bat" stands alone
```

### Example 2: Empty String
```
Input: strs = [""]
Output: [[""]]
Explanation: Empty string forms its own group.
```

### Example 3: Single Element
```
Input: strs = ["a"]
Output: [["a"]]
Explanation: Single string forms its own group.
```

### Example 4: All Same Anagrams
```
Input: strs = ["abc", "bca", "cab", "bac"]
Output: [["abc", "bca", "cab", "bac"]]

Explanation:
All strings are anagrams of each other.
Sorted form: "abc"
They all belong to the same group.
```

### Example 5: No Anagrams
```
Input: strs = ["a", "b", "c", "d"]
Output: [["a"], ["b"], ["c"], ["d"]]

Explanation:
No two strings are anagrams.
Each forms its own group.
```

### Example 6: Multiple Groups
```
Input: strs = ["listen", "silent", "hello", "world", "enlist"]
Output: [["listen", "silent", "enlist"], ["hello"], ["world"]]

Explanation:
- "listen", "silent", "enlist" are anagrams (e,i,l,n,s,t)
- "hello" and "world" are unique
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
  Input: "eat"
  Convert to char[]: ['e', 'a', 't']
  Sort: ['a', 'e', 't']
  Key: "aet"
  Check map: key "aet" not found
  Create new list: ["eat"]
  map = {"aet": ["eat"]}

Step 2: Process "tea"
  Input: "tea"
  Convert to char[]: ['t', 'e', 'a']
  Sort: ['a', 'e', 't']
  Key: "aet"  (same key as "eat"!)
  Check map: key "aet" found
  Add to existing list: ["eat", "tea"]
  map = {"aet": ["eat", "tea"]}

Step 3: Process "tan"
  Input: "tan"
  Convert to char[]: ['t', 'a', 'n']
  Sort: ['a', 'n', 't']
  Key: "ant"  (new key)
  Check map: key "ant" not found
  Create new list: ["tan"]
  map = {"aet": ["eat", "tea"],
         "ant": ["tan"]}

Step 4: Process "ate"
  Input: "ate"
  Convert to char[]: ['a', 't', 'e']
  Sort: ['a', 'e', 't']
  Key: "aet"  (matches "eat" and "tea")
  Check map: key "aet" found
  Add to existing list: ["eat", "tea", "ate"]
  map = {"aet": ["eat", "tea", "ate"],
         "ant": ["tan"]}

Step 5: Process "nat"
  Input: "nat"
  Convert to char[]: ['n', 'a', 't']
  Sort: ['a', 'n', 't']
  Key: "ant"  (matches "tan")
  Check map: key "ant" found
  Add to existing list: ["tan", "nat"]
  map = {"aet": ["eat", "tea", "ate"],
         "ant": ["tan", "nat"]}

Step 6: Process "bat"
  Input: "bat"
  Convert to char[]: ['b', 'a', 't']
  Sort: ['a', 'b', 't']
  Key: "abt"  (new key)
  Check map: key "abt" not found
  Create new list: ["bat"]
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

## Detailed Walkthrough (Approach 3: Frequency Array)

Let's trace the same example with frequency counting:

```
Initial State:
map = {}
strs = ["eat", "tea", "tan", "ate", "nat", "bat"]

Step 1: Process "eat"
  Input: "eat"
  Count array (a-z): [1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0]
                      a=1      e=1                         t=1
  Key using Arrays.toString(): "[1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0]"
  map = {key: ["eat"]}

Step 2: Process "tea"
  Input: "tea"
  Count array: [1,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0]
               Same as "eat"! ✓
  Key: Same as before
  map = {key1: ["eat", "tea"]}

Step 3: Process "tan"
  Input: "tan"
  Count array: [1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,1,0,0,0,0,0,0]
                a=1                        n=1         t=1
  Key: "[1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0]"
  map = {key1: ["eat", "tea"],
         key2: ["tan"]}

[Similar steps for "ate", "nat", "bat"...]

Result: Same as sorted approach, but computed faster!
Time: O(k) per string vs O(k log k)
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

## Edge Cases to Consider (12+ Cases)

```java
// Test Case 1: Empty strings
Input: [""]
Output: [[""]]
// Empty string is valid, forms its own group

// Test Case 2: Multiple empty strings
Input: ["", "", ""]
Output: [["", "", ""]]
// All empty strings are anagrams of each other

// Test Case 3: Single string
Input: ["a"]
Output: [["a"]]

// Test Case 4: All anagrams
Input: ["abc", "bca", "cab"]
Output: [["abc", "bca", "cab"]]
// All in one group

// Test Case 5: No anagrams
Input: ["a", "b", "c"]
Output: [["a"], ["b"], ["c"]]
// Each forms its own group

// Test Case 6: Different lengths
Input: ["abc", "ab", "abcd"]
Output: [["abc"], ["ab"], ["abcd"]]
// Different lengths cannot be anagrams

// Test Case 7: Duplicate strings
Input: ["abc", "abc", "abc"]
Output: [["abc", "abc", "abc"]]
// Same string counts as anagram of itself

// Test Case 8: Single character strings
Input: ["a", "a", "b", "b", "c"]
Output: [["a", "a"], ["b", "b"], ["c"]]
// Single chars grouped correctly

// Test Case 9: Very long strings
Input: ["abcdefghijklmnopqrstuvwxyz", "zyxwvutsrqponmlkjihgfedcba"]
Output: [["abcdefghijklmnopqrstuvwxyz", "zyxwvutsrqponmlkjihgfedcba"]]
// All 26 letters, reversed

// Test Case 10: Strings with repeated characters
Input: ["aaa", "aaa", "aa"]
Output: [["aaa", "aaa"], ["aa"]]
// Length matters

// Test Case 11: Maximum input size
Input: 10^4 strings, each 100 chars
// Test performance at constraint limits

// Test Case 12: All same letter, different counts
Input: ["a", "aa", "aaa", "aaaa"]
Output: [["a"], ["aa"], ["aaa"], ["aaaa"]]
// Each has different frequency signature

// Test Case 13: Palindromic anagrams
Input: ["aab", "aba", "baa"]
Output: [["aab", "aba", "baa"]]
// All have same characters despite being palindromes

// Test Case 14: Mixed case (if constraint allowed)
// Note: Problem states lowercase only, but good to think about
Input: ["Eat", "eat"]
// Without case handling: [["Eat"], ["eat"]]
// With case handling: [["Eat", "eat"]]
```

---

## Common Mistakes to Avoid (6+ Mistakes)

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
// "12" could mean [1,2] or [12] - ambiguous!

// ✅ CORRECT - Use delimiter or Arrays.toString()
String key = Arrays.toString(count);
```

### Mistake 3: Not creating new ArrayList when needed
```java
// ❌ WRONG - Creates list every time, overwrites existing
for (String str : strs) {
    String key = getKey(str);
    List<String> list = new ArrayList<>();  // Wasteful!
    list.add(str);
    map.put(key, list);  // Loses previous entries!
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

### Mistake 5: Forgetting to handle empty strings
```java
// ❌ WRONG - Assuming non-empty
for (String str : strs) {
    char first = str.charAt(0);  // Crashes on empty!
}

// ✅ CORRECT - Empty strings are valid
// They naturally work with both sorted and frequency approaches
if (str.isEmpty()) {
    // Key will be "" for sorted or "[0,0,...,0]" for frequency
}
```

### Mistake 6: Incorrect character counting
```java
// ❌ WRONG - Doesn't account for 'a' offset
for (char c : str.toCharArray()) {
    count[c]++;  // Wrong index! 'a' = 97, not 0
}

// ✅ CORRECT - Subtract 'a' to get 0-25 range
for (char c : str.toCharArray()) {
    count[c - 'a']++;
}
```

### Mistake 7: Using wrong data structure for result
```java
// ❌ WRONG - Trying to return the map
return map;  // Type mismatch: need List<List<String>>

// ✅ CORRECT - Return values as ArrayList
return new ArrayList<>(map.values());
```

### Mistake 8: Not considering time complexity
```java
// ❌ WRONG - Checking every pair
for (int i = 0; i < n; i++) {
    for (int j = i + 1; j < n; j++) {
        if (isAnagram(strs[i], strs[j])) {
            // O(n²) comparisons!
        }
    }
}

// ✅ CORRECT - Single pass with HashMap
for (String str : strs) {
    String key = computeKey(str);
    map.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
}
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

1. **Valid Anagram** (LeetCode #242, Easy) - Foundation for this problem
   - Check if two strings are anagrams
   - Same frequency counting technique

2. **Find All Anagrams in String** (LeetCode #438, Medium) - Sliding window + frequency
   - Find all anagram substrings in a string
   - Uses sliding window with frequency map

3. **Strings Differ by One Character** (LeetCode #1554, Medium) - Similar grouping logic
   - Group strings that differ by exactly one character
   - Similar HashMap grouping pattern

4. **Group Shifted Strings** (LeetCode #249, Medium) - Similar pattern with different key
   - Group strings by shift pattern
   - Use relative distance as key

5. **Sort Characters by Frequency** (LeetCode #451, Medium)
   - Sort characters by their frequency
   - Uses frequency counting

6. **Top K Frequent Words** (LeetCode #692, Medium)
   - Find most frequent words
   - HashMap + sorting/heap

7. **Isomorphic Strings** (LeetCode #205, Easy)
   - Check if two strings are isomorphic
   - Character mapping pattern

8. **Word Pattern** (LeetCode #290, Easy)
   - Check if string follows a pattern
   - Similar mapping concept

---

## Performance Comparison: Sorted vs Frequency

### Benchmark Results (Theoretical)

**Input**: 10,000 strings, each 100 characters

| Approach | Time per String | Total Time | Key Size | Memory |
|----------|----------------|------------|----------|--------|
| Sorted Key | O(100 log 100) ≈ 664 ops | 6.64M ops | 100 bytes | ~1MB keys |
| Frequency Count | O(100 + 26) = 126 ops | 1.26M ops | ~130 bytes | ~1.3MB keys |
| **Speedup** | **5.3x faster** | **5.3x faster** | Slightly larger | Similar |

### When Each Approach Wins:

**Use Sorted Key When:**
- ✅ Interview setting (simpler to code, less error-prone)
- ✅ Strings are short (k < 20)
- ✅ Code readability is priority
- ✅ Working with arbitrary Unicode characters

**Use Frequency Count When:**
- ✅ Strings are long (k > 100)
- ✅ Performance is critical
- ✅ Character set is known and small (a-z, A-Z)
- ✅ Processing millions of strings

### Real-World Performance Tips:

1. **Pre-allocate HashMap size** if input size is known:
   ```java
   Map<String, List<String>> map = new HashMap<>(strs.length);
   ```

2. **Use StringBuilder efficiently**:
   ```java
   StringBuilder key = new StringBuilder(26 * 3); // Pre-size for "#n#n#..."
   ```

3. **Consider parallel processing** for very large inputs:
   ```java
   Arrays.stream(strs)
         .parallel()
         .collect(Collectors.groupingBy(this::getSignature));
   ```

---

## Testing Strategy

### Unit Test Template
```java
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupAnagramsTest {
    private Solution solution = new Solution();
    
    @Test
    public void testExample1() {
        String[] input = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> result = solution.groupAnagrams(input);
        
        assertEquals(3, result.size());
        // Note: Order of groups doesn't matter, need to verify contents
        assertTrue(containsGroup(result, Arrays.asList("eat", "tea", "ate")));
        assertTrue(containsGroup(result, Arrays.asList("tan", "nat")));
        assertTrue(containsGroup(result, Arrays.asList("bat")));
    }
    
    @Test
    public void testEmptyString() {
        String[] input = {""};
        List<List<String>> result = solution.groupAnagrams(input);
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(""), result.get(0));
    }
    
    @Test
    public void testSingleString() {
        String[] input = {"a"};
        List<List<String>> result = solution.groupAnagrams(input);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testAllAnagrams() {
        String[] input = {"abc", "bca", "cab"};
        List<List<String>> result = solution.groupAnagrams(input);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
    }
    
    @Test
    public void testNoAnagrams() {
        String[] input = {"a", "b", "c"};
        List<List<String>> result = solution.groupAnagrams(input);
        assertEquals(3, result.size());
    }
    
    @Test
    public void testDuplicates() {
        String[] input = {"abc", "abc", "abc"};
        List<List<String>> result = solution.groupAnagrams(input);
        assertEquals(1, result.size());
        assertEquals(3, result.get(0).size());
    }
    
    @Test
    public void testLargeInput() {
        String[] input = new String[10000];
        Arrays.fill(input, "test");
        List<List<String>> result = solution.groupAnagrams(input);
        assertEquals(1, result.size());
        assertEquals(10000, result.get(0).size());
    }
    
    private boolean containsGroup(List<List<String>> result, List<String> expected) {
        for (List<String> group : result) {
            if (group.size() == expected.size() && 
                group.containsAll(expected)) {
                return true;
            }
        }
        return false;
    }
}
```

### Test Cases Checklist
- [ ] Empty array: `[]`
- [ ] Single empty string: `[""]`
- [ ] Single character: `["a"]`
- [ ] All same: `["abc", "abc", "abc"]`
- [ ] All anagrams: `["abc", "bca", "cab"]`
- [ ] No anagrams: `["a", "b", "c"]`
- [ ] Mixed: `["eat", "tea", "tan", "ate", "nat", "bat"]`
- [ ] Long strings: 100-character strings
- [ ] Maximum size: 10^4 strings
- [ ] Multiple empty strings: `["", "", ""]`
- [ ] Single char repeated: `["a", "a", "a"]`

---

## Interview Tips (Comprehensive Guide)

### What to Say During Interview:

1. **Clarify Constraints**: 
   - "Can I assume lowercase letters only?"
   - "Is the order of groups important?"
   - "Can strings be empty?"
   - "What's the expected size of input?"
   - "Should I handle Unicode or special characters?"

2. **Explain Approach**:
   - "I'll use a HashMap to group anagrams together"
   - "For the key, I can either sort the string or use character frequencies"
   - "Sorting is simpler but O(k log k), frequency count is O(k) but more code"

3. **Discuss Trade-offs**:
   - "Sorting is O(k log k) per string but simpler to implement"
   - "Frequency count is O(k) but requires more code"
   - "I'll go with [sorted/frequency] because..."
   - "If we had very long strings, frequency counting would be better"

4. **Code Strategy**:
   - Write sorted approach first (faster to code, less error-prone)
   - Trace through one example
   - Mention optimization if time permits
   - Use computeIfAbsent for cleaner code

5. **Edge Cases**:
   - "I'll handle empty strings"
   - "Different length strings can't be anagrams"
   - "Duplicate strings naturally group together"

### Expected Follow-up Questions:

**Q**: "Can you optimize the time complexity?"
**A**: "Yes, use frequency count instead of sorting for O(n × k) instead of O(n × k log k)"

**Q**: "What if strings contain Unicode characters?"
**A**: "Use HashMap<Character, Integer> for frequency count instead of int[26] array, since we can't predict character range"

**Q**: "How does your solution handle duplicates?"
**A**: "Duplicate strings have the same key, so they naturally group together in the same list"

**Q**: "Can you make the code cleaner?"
**A**: "Yes, use computeIfAbsent to eliminate null checks and make the logic one-liner"

**Q**: "What if memory is constrained?"
**A**: "The HashMap approach is already optimal for space. We could stream the results if needed to avoid storing all groups at once"

**Q**: "How would you handle case-insensitive anagrams?"
**A**: "Convert each string to lowercase before processing: `str.toLowerCase()` before creating the key"

**Q**: "Could you group by another property, like length?"
**A**: "Yes, this pattern works for any grouping - just change the key computation. For length: `String key = String.valueOf(str.length())`"

### Common Interview Mistakes to Avoid:

1. ❌ **Starting to code immediately** - Take 1-2 minutes to think and explain approach
2. ❌ **Not testing with examples** - Always trace through at least one example
3. ❌ **Ignoring edge cases** - Mention empty strings, single elements, all same/all different
4. ❌ **Not explaining time/space complexity** - Interviewer expects this analysis
5. ❌ **Making code too complex** - Start with sorted approach, mention optimization after
6. ❌ **Forgetting to ask clarifying questions** - Shows lack of communication skills

### Time Management (25-minute target for Medium):

- **Minutes 0-2**: Clarify problem, discuss approach
- **Minutes 2-5**: Write high-level plan, discuss trade-offs
- **Minutes 5-15**: Code solution (sorted approach)
- **Minutes 15-20**: Test with examples, fix bugs
- **Minutes 20-25**: Discuss optimization, edge cases, complexity

### What Makes a Strong Interview Performance:

✅ **Clear communication** throughout the process
✅ **Multiple approaches** discussed (even if you only code one)
✅ **Trade-offs explained** (time vs space, simplicity vs performance)
✅ **Edge cases handled** without prompting
✅ **Clean, readable code** with meaningful variable names
✅ **Testing included** - trace through examples
✅ **Complexity analysis** provided
✅ **Optimization mentioned** even if not implemented

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

## Python Solutions

### Python: Sorted Key Approach
```python
from typing import List
from collections import defaultdict

class Solution:
    def groupAnagrams(self, strs: List[str]) -> List[List[str]]:
        """
        Group anagrams using sorted string as key.
        
        Time: O(n × k log k) where n = len(strs), k = max len(str)
        Space: O(n × k)
        """
        # defaultdict automatically creates empty list for new keys
        anagram_groups = defaultdict(list)
        
        for s in strs:
            # Sort string and use as key
            # ''.join(sorted(s)) converts list back to string
            key = ''.join(sorted(s))
            anagram_groups[key].append(s)
        
        # Return values as list
        return list(anagram_groups.values())
```

### Python: Frequency Count with Tuple
```python
from typing import List
from collections import defaultdict

class Solution:
    def groupAnagrams(self, strs: List[str]) -> List[List[str]]:
        """
        Group anagrams using character frequency tuple as key.
        
        Time: O(n × k) - optimal!
        Space: O(n × k)
        """
        anagram_groups = defaultdict(list)
        
        for s in strs:
            # Count character frequencies
            count = [0] * 26
            for c in s:
                count[ord(c) - ord('a')] += 1
            
            # Tuple is hashable, can be used as dict key
            # List cannot be dict key in Python!
            key = tuple(count)
            anagram_groups[key].append(s)
        
        return list(anagram_groups.values())
```

### Python: Using Counter (Most Pythonic)
```python
from typing import List
from collections import defaultdict, Counter

class Solution:
    def groupAnagrams(self, strs: List[str]) -> List[List[str]]:
        """
        Group anagrams using Counter for frequency.
        
        Time: O(n × k)
        Space: O(n × k)
        """
        anagram_groups = defaultdict(list)
        
        for s in strs:
            # Counter creates frequency dict
            # frozenset makes it hashable
            # Or convert Counter to sorted tuple
            key = tuple(sorted(Counter(s).items()))
            anagram_groups[key].append(s)
        
        return list(anagram_groups.values())
```

---

## JavaScript Solutions

### JavaScript: Sorted Key
```javascript
/**
 * @param {string[]} strs
 * @return {string[][]}
 */
var groupAnagrams = function(strs) {
    // Map to store anagram groups
    const anagramGroups = new Map();
    
    for (let str of strs) {
        // Sort string to create key
        const key = str.split('').sort().join('');
        
        // Add to group
        if (!anagramGroups.has(key)) {
            anagramGroups.set(key, []);
        }
        anagramGroups.get(key).push(str);
    }
    
    // Return all groups as array
    return Array.from(anagramGroups.values());
};
```

### JavaScript: Frequency Count
```javascript
var groupAnagrams = function(strs) {
    const anagramGroups = new Map();
    
    for (let str of strs) {
        // Count character frequencies
        const count = new Array(26).fill(0);
        for (let char of str) {
            count[char.charCodeAt(0) - 'a'.charCodeAt(0)]++;
        }
        
        // Use array as string key
        const key = count.join('#');
        
        if (!anagramGroups.has(key)) {
            anagramGroups.set(key, []);
        }
        anagramGroups.get(key).push(str);
    }
    
    return Array.from(anagramGroups.values());
};
```

---

## C++ Solutions

### C++: Sorted Key
```cpp
#include <vector>
#include <string>
#include <unordered_map>
#include <algorithm>

class Solution {
public:
    vector<vector<string>> groupAnagrams(vector<string>& strs) {
        unordered_map<string, vector<string>> anagramGroups;
        
        for (const string& str : strs) {
            // Sort string to create key
            string key = str;
            sort(key.begin(), key.end());
            
            // Add to group
            anagramGroups[key].push_back(str);
        }
        
        // Extract all groups
        vector<vector<string>> result;
        for (auto& pair : anagramGroups) {
            result.push_back(pair.second);
        }
        
        return result;
    }
};
```

### C++: Frequency Count
```cpp
class Solution {
public:
    vector<vector<string>> groupAnagrams(vector<string>& strs) {
        unordered_map<string, vector<string>> anagramGroups;
        
        for (const string& str : strs) {
            // Count character frequencies
            vector<int> count(26, 0);
            for (char c : str) {
                count[c - 'a']++;
            }
            
            // Build key from frequency
            string key;
            for (int i = 0; i < 26; i++) {
                key += "#" + to_string(count[i]);
            }
            
            anagramGroups[key].push_back(str);
        }
        
        vector<vector<string>> result;
        for (auto& pair : anagramGroups) {
            result.push_back(pair.second);
        }
        
        return result;
    }
};
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

## Key Takeaways (10 Essential Points)

1. ✅ **HashMap excels at grouping** - O(1) lookup to find/create groups eliminates need for nested loops

2. ✅ **Sorted string** works as anagram signature - simple and intuitive, great for interviews

3. ✅ **Frequency count** is asymptotically faster - O(k) vs O(k log k) per string

4. ✅ **Use delimiters** or Arrays.toString() to avoid key collisions when building frequency signatures

5. ✅ **computeIfAbsent** makes code cleaner and more readable by eliminating manual null checks

6. ✅ **This pattern appears everywhere** - any "grouping by characteristic" problem can use this approach

7. ✅ **String immutability matters** - Creating keys from char arrays requires `new String(chars)`

8. ✅ **Trade-off between simplicity and performance** - Sorted key is easier to code; frequency count is faster

9. ✅ **Anagrams must have same length** - Different lengths can never be anagrams (could optimize with length pre-filtering)

10. ✅ **Character offset is crucial** - Always use `c - 'a'` for counting to map 'a'-'z' to 0-25

---

## Bonus Takeaways

11. ✅ **StringBuilder vs String concatenation** - Use StringBuilder when building keys in loops to avoid O(n²) string creation

12. ✅ **Empty strings are valid** - Don't special-case them; they naturally work with both approaches

13. ✅ **HashMap.values() returns Collection** - Need `new ArrayList<>(map.values())` to convert to List

14. ✅ **Group order doesn't matter** - Problem allows any order, so HashMap's unpredictable iteration is fine

15. ✅ **Duplicate strings are anagrams** - "abc" and "abc" are anagrams of each other by definition

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
