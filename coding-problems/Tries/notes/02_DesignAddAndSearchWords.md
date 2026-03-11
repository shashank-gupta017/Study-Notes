# LeetCode 211: Design Add and Search Words Data Structure

## Problem Classification
- **Difficulty:** Medium
- **Pattern:** Trie with Wildcard Search
- **Category:** Trie, DFS, Backtracking, String Processing
- **Related Patterns:** Prefix Tree, Word Search, Pattern Matching

## Problem Statement

Design a data structure that supports adding new words and finding if a string matches any previously added string.

Implement the `WordDictionary` class:
- `WordDictionary()` - Initializes the object.
- `void addWord(word)` - Adds word to the data structure, it can be matched later.
- `bool search(word)` - Returns true if there is any string in the data structure that matches word or false otherwise. The word may contain dots `'.'` where dots can be matched with any letter.

### Example
```
Input:
["WordDictionary","addWord","addWord","addWord","search","search","search","search"]
[[],["bad"],["dad"],["mad"],["pad"],["bad"],[".ad"],["b.."]]

Output:
[null,null,null,null,false,true,true,true]

Explanation:
WordDictionary wordDictionary = new WordDictionary();
wordDictionary.addWord("bad");
wordDictionary.addWord("dad");
wordDictionary.addWord("mad");
wordDictionary.search("pad"); // return False
wordDictionary.search("bad"); // return True
wordDictionary.search(".ad"); // return True
wordDictionary.search("b.."); // return True
```

### Constraints
- `1 <= word.length <= 25`
- `word` in addWord consists of lowercase English letters.
- `word` in search consist of `'.'` or lowercase English letters.
- There will be at most `2` dots in `word` for search queries.
- At most `10^4` calls will be made to addWord and search.

---

## Key Concepts

### Why Not Regular Trie?
A standard Trie works perfectly for exact matches but cannot handle the wildcard character `'.'` which can match any letter. The wildcard requires:
1. **DFS/Backtracking**: Explore all possible branches when encountering `'.'`
2. **Multiple Path Exploration**: A single `'.'` means we need to check all 26 children
3. **Recursive Search**: Natural fit for handling variable-length wildcards

### Core Insight
- **Add Operation**: Standard Trie insertion - O(m) where m is word length
- **Search Operation**: 
  - Without wildcards: O(m) - standard Trie search
  - With wildcards: O(26^k * m) worst case, where k is number of dots
  - Each `'.'` potentially branches into 26 recursive calls

### Pattern Recognition
This problem combines:
1. **Trie Data Structure**: For efficient prefix-based storage
2. **DFS**: To explore all matching paths for wildcards
3. **Backtracking**: Implicit through recursion when paths don't match

---

## Solution Approach

### Algorithm Overview

1. **TrieNode Structure**:
   - `children`: HashMap/Array of 26 children nodes
   - `isEndOfWord`: Boolean flag marking word completion

2. **AddWord Algorithm**:
   ```
   - Start at root
   - For each character in word:
     - If child doesn't exist, create new node
     - Move to child node
   - Mark final node as end of word
   ```

3. **Search Algorithm**:
   ```
   - Use DFS helper function
   - Base cases:
     - Reached end of word and current node is word end → true
     - Reached end of word but not word end → false
   - Recursive cases:
     - If character is letter → check that specific child
     - If character is '.' → check ALL children recursively
   ```

---

## Implementation

### Solution 1: Trie with DFS (Optimal)

```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.isEndOfWord = False

class WordDictionary:
    def __init__(self):
        """
        Initialize data structure with empty root node.
        Time: O(1)
        Space: O(1)
        """
        self.root = TrieNode()

    def addWord(self, word: str) -> None:
        """
        Add a word to the trie.
        Time: O(m) where m is length of word
        Space: O(m) worst case for new word
        
        Process:
        1. Start from root
        2. For each char, create child if needed
        3. Mark last node as word end
        """
        node = self.root
        
        for char in word:
            # Create child node if it doesn't exist
            if char not in node.children:
                node.children[char] = TrieNode()
            # Move to child node
            node = node.children[char]
        
        # Mark end of word
        node.isEndOfWord = True

    def search(self, word: str) -> bool:
        """
        Search for a word with possible wildcard '.'
        Time: O(m) best case (no wildcards)
              O(26^k * m) worst case (k wildcards)
        Space: O(m) recursion depth
        
        Strategy:
        - Use DFS to handle wildcards
        - Each '.' branches into all children
        """
        return self._dfs(word, 0, self.root)
    
    def _dfs(self, word: str, index: int, node: TrieNode) -> bool:
        """
        DFS helper to search with wildcards.
        
        Args:
            word: The search word
            index: Current position in word
            node: Current TrieNode
        
        Returns:
            True if word[index:] can be found from node
        """
        # Base case: reached end of word
        if index == len(word):
            return node.isEndOfWord
        
        char = word[index]
        
        # Case 1: Regular character
        if char != '.':
            # Check if child exists for this character
            if char not in node.children:
                return False
            # Continue search in that specific child
            return self._dfs(word, index + 1, node.children[char])
        
        # Case 2: Wildcard character '.'
        # Try all possible children
        for child in node.children.values():
            if self._dfs(word, index + 1, child):
                return True
        
        # None of the children led to a match
        return False
```

### Solution 2: With Array-Based Children (Faster)

```python
class TrieNode:
    def __init__(self):
        # Array of 26 children for 'a'-'z'
        self.children = [None] * 26
        self.isEndOfWord = False

class WordDictionary:
    def __init__(self):
        self.root = TrieNode()

    def addWord(self, word: str) -> None:
        """
        Array-based implementation for faster access.
        Time: O(m)
        Space: O(m)
        """
        node = self.root
        
        for char in word:
            # Calculate index: 'a' -> 0, 'b' -> 1, etc.
            index = ord(char) - ord('a')
            
            if node.children[index] is None:
                node.children[index] = TrieNode()
            
            node = node.children[index]
        
        node.isEndOfWord = True

    def search(self, word: str) -> bool:
        """
        Array-based search with wildcards.
        Time: O(m) best, O(26^k * m) worst
        Space: O(m) recursion
        """
        return self._dfs(word, 0, self.root)
    
    def _dfs(self, word: str, index: int, node: TrieNode) -> bool:
        if index == len(word):
            return node.isEndOfWord
        
        char = word[index]
        
        if char != '.':
            char_index = ord(char) - ord('a')
            if node.children[char_index] is None:
                return False
            return self._dfs(word, index + 1, node.children[char_index])
        
        # Wildcard: try all 26 possible children
        for i in range(26):
            if node.children[i] is not None:
                if self._dfs(word, index + 1, node.children[i]):
                    return True
        
        return False
```

### Solution 3: With Early Termination Optimization

```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.isEndOfWord = False

class WordDictionary:
    def __init__(self):
        self.root = TrieNode()

    def addWord(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.isEndOfWord = True

    def search(self, word: str) -> bool:
        """
        Optimized search with early termination.
        """
        return self._search_helper(word, 0, self.root)
    
    def _search_helper(self, word: str, start: int, node: TrieNode) -> bool:
        # Base case
        if start == len(word):
            return node.isEndOfWord
        
        # Early termination: no children means no match
        if not node.children:
            return False
        
        char = word[start]
        
        if char != '.':
            # Direct lookup
            if char not in node.children:
                return False
            return self._search_helper(word, start + 1, node.children[char])
        
        # Wildcard handling with optimization
        # Try all existing children (not all 26)
        for child_char, child_node in node.children.items():
            if self._search_helper(word, start + 1, child_node):
                return True
        
        return False
```

---

## Complexity Analysis

### Time Complexity

#### AddWord Operation
- **Best/Average/Worst Case:** O(m)
  - m = length of word
  - Must traverse/create exactly m nodes
  - Each character operation is O(1)

#### Search Operation
- **Best Case:** O(m) - No wildcards, direct path
  - Example: search("abc") when "abc" exists
  
- **Average Case:** O(m) - Few wildcards or wildcards near end
  - Most real-world queries have limited wildcards
  
- **Worst Case:** O(26^k × m) - Multiple wildcards
  - k = number of '.' characters
  - Each wildcard branches into up to 26 paths
  - Example: search("...") explores all 3-letter words
  - With constraint of max 2 dots: O(26^2 × m) = O(676m)

### Space Complexity

#### Overall Structure
- **Space:** O(n × m)
  - n = number of words added
  - m = average word length
  - Each word adds up to m new nodes

#### Per Operation
- **AddWord:** O(m) - May create m new nodes
- **Search:** O(m) - Recursion stack depth
  - Maximum recursion depth equals word length

### Detailed Analysis

```python
# Space breakdown for n words of average length m:

# 1. Trie Structure: O(n × m) worst case
#    - Shared prefixes reduce actual space
#    - Complete trie with no sharing: n × m nodes
#    - With common prefixes: can be much less

# 2. Each TrieNode:
#    HashMap: O(k) where k = number of children
#    Boolean: O(1)
#    Total per node: O(k + 1)

# 3. Search recursion stack: O(m)
#    - Maximum depth = word length
#    - Each frame stores: word, index, node reference
```

---

## Optimization Techniques

### 1. Early Termination

```python
def _dfs(self, word: str, index: int, node: TrieNode) -> bool:
    if index == len(word):
        return node.isEndOfWord
    
    # OPTIMIZATION: Check if node has any children
    if not node.children:
        return False  # Can't continue, no matches possible
    
    char = word[index]
    # ... rest of logic
```

**Benefit:** Avoids unnecessary recursion when branch is dead-end.

### 2. Character Count Tracking

```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.isEndOfWord = False
        self.childCount = 0  # Track number of children

class WordDictionary:
    def addWord(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
                node.childCount += 1  # Increment count
            node = node.children[char]
        node.isEndOfWord = True
    
    def _dfs(self, word: str, index: int, node: TrieNode) -> bool:
        if index == len(word):
            return node.isEndOfWord
        
        # OPTIMIZATION: Quick check
        if node.childCount == 0:
            return False
        
        # ... rest of logic
```

**Benefit:** O(1) check vs iterating through empty dictionary.

### 3. Iterative Wildcard Search (Advanced)

```python
def search(self, word: str) -> bool:
    """
    Iterative BFS approach for wildcard search.
    May reduce stack overhead for very long words.
    """
    from collections import deque
    
    # Queue stores (node, index) pairs
    queue = deque([(self.root, 0)])
    
    while queue:
        node, index = queue.popleft()
        
        # Reached end of word
        if index == len(word):
            if node.isEndOfWord:
                return True
            continue
        
        char = word[index]
        
        if char != '.':
            if char in node.children:
                queue.append((node.children[char], index + 1))
        else:
            # Add all children to queue
            for child in node.children.values():
                queue.append((child, index + 1))
    
    return False
```

**Trade-off:** More memory (queue) but no recursion stack.

### 4. Prefix Caching (For Multiple Queries)

```python
class WordDictionary:
    def __init__(self):
        self.root = TrieNode()
        self.cache = {}  # Cache results for exact searches
    
    def search(self, word: str) -> bool:
        # Only cache non-wildcard searches
        if '.' not in word:
            if word in self.cache:
                return self.cache[word]
            result = self._dfs(word, 0, self.root)
            self.cache[word] = result
            return result
        
        return self._dfs(word, 0, self.root)
```

**Benefit:** O(1) lookup for repeated exact searches.

### 5. Length-Based Indexing

```python
class WordDictionary:
    def __init__(self):
        self.root = TrieNode()
        self.wordsByLength = {}  # length -> set of words
    
    def addWord(self, word: str) -> None:
        # Standard trie addition
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.isEndOfWord = True
        
        # OPTIMIZATION: Index by length
        length = len(word)
        if length not in self.wordsByLength:
            self.wordsByLength[length] = set()
        self.wordsByLength[length].add(word)
```

**Use Case:** Quick filtering before expensive wildcard search.

---

## Edge Cases & Handling

### 1. Empty String
```python
# Edge case: empty word
wordDict.addWord("")  # Root marked as end
wordDict.search("")   # Should return True

# Handled by base case:
if index == len(word):
    return node.isEndOfWord
```

### 2. All Wildcards
```python
# Edge case: word is all dots
wordDict.search("...")  # Must check all 3-letter words

# Worst case performance: O(26^3)
# Explores entire trie up to depth 3
```

### 3. No Wildcards
```python
# Edge case: exact match query
wordDict.search("hello")  # O(m) - standard trie search

# Optimizes to linear search
if char != '.':
    # Single path check
```

### 4. Single Character
```python
# Edge case: single char operations
wordDict.addWord("a")
wordDict.search(".")  # Should return True
wordDict.search("a")  # Should return True
wordDict.search("b")  # Should return False
```

### 5. Long Words (Boundary)
```python
# Edge case: maximum length (25 chars)
long_word = "a" * 25
wordDict.addWord(long_word)
wordDict.search("." * 25)  # Still efficient with constraint

# With max 2 dots constraint: O(26^2 * 25) = O(16,250)
```

### 6. Duplicate Additions
```python
# Edge case: adding same word multiple times
wordDict.addWord("test")
wordDict.addWord("test")  # Idempotent operation

# Result: No duplicates in trie, isEndOfWord remains True
```

### 7. Prefix vs Complete Word
```python
# Edge case: prefix exists but not as complete word
wordDict.addWord("testing")
wordDict.search("test")  # Should return False

# Handled by: node.isEndOfWord check
```

### 8. Wildcard at Different Positions
```python
# Start: ".est"
# Middle: "t.st" 
# End: "tes."
# Multiple: ".e.t"

# All handled uniformly by DFS
```

### 9. No Matching Words
```python
# Edge case: search when dictionary is empty
wordDict = WordDictionary()
wordDict.search("word")  # Return False

# Or no words match pattern
wordDict.addWord("abc")
wordDict.search("xyz")  # Return False
```

---

## Common Mistakes & Pitfalls

### Mistake 1: Not Handling Base Case Properly
```python
# ❌ WRONG: Missing end-of-word check
def _dfs(self, word, index, node):
    if index == len(word):
        return True  # Wrong! Node might not be word end

# ✅ CORRECT:
def _dfs(self, word, index, node):
    if index == len(word):
        return node.isEndOfWord  # Must check flag
```

### Mistake 2: Modifying During Iteration
```python
# ❌ WRONG: Modifying children during search
for child in node.children:
    del node.children[child]  # Never modify during search!

# ✅ CORRECT: Read-only operations during search
for child in node.children.values():
    self._dfs(word, index + 1, child)
```

### Mistake 3: Not Trying All Wildcard Paths
```python
# ❌ WRONG: Returning after first child
if char == '.':
    for child in node.children.values():
        return self._dfs(word, index + 1, child)  # Wrong!

# ✅ CORRECT: Try all and return if any succeeds
if char == '.':
    for child in node.children.values():
        if self._dfs(word, index + 1, child):
            return True
    return False
```

### Mistake 4: Incorrect Index Management
```python
# ❌ WRONG: Not incrementing index
if char != '.':
    return self._dfs(word, index, node.children[char])

# ✅ CORRECT:
if char != '.':
    return self._dfs(word, index + 1, node.children[char])
```

### Mistake 5: Forgetting Null Checks
```python
# ❌ WRONG: Assuming child exists
if char != '.':
    return self._dfs(word, index + 1, node.children[char])

# ✅ CORRECT: Check existence first
if char != '.':
    if char not in node.children:
        return False
    return self._dfs(word, index + 1, node.children[char])
```

---

## Test Cases

### Basic Test Cases

```python
def test_basic_operations():
    wd = WordDictionary()
    
    # Test 1: Basic add and search
    wd.addWord("hello")
    assert wd.search("hello") == True
    assert wd.search("world") == False
    
    # Test 2: Single wildcard
    wd.addWord("world")
    assert wd.search(".orld") == True
    assert wd.search("w.rld") == True
    assert wd.search("worl.") == True
    
    # Test 3: Multiple wildcards
    assert wd.search(".or..") == True
    assert wd.search("..rld") == True
    
    print("Basic tests passed!")

def test_edge_cases():
    wd = WordDictionary()
    
    # Test 1: Empty word
    wd.addWord("")
    assert wd.search("") == True
    
    # Test 2: Single character
    wd.addWord("a")
    assert wd.search("a") == True
    assert wd.search(".") == True
    assert wd.search("b") == False
    
    # Test 3: All wildcards
    wd.addWord("abc")
    assert wd.search("...") == True
    assert wd.search("....") == False
    
    # Test 4: No match
    assert wd.search("xyz") == False
    
    print("Edge case tests passed!")

def test_prefix_scenarios():
    wd = WordDictionary()
    
    # Test: Prefix vs complete word
    wd.addWord("testing")
    assert wd.search("test") == False
    assert wd.search("testing") == True
    assert wd.search("test...") == True
    
    # Test: Multiple words with shared prefix
    wd.addWord("test")
    assert wd.search("test") == True
    assert wd.search("testing") == True
    
    print("Prefix tests passed!")

def test_complex_patterns():
    wd = WordDictionary()
    
    # Add diverse words
    words = ["bad", "dad", "mad", "pad", "bat", "cat"]
    for word in words:
        wd.addWord(word)
    
    # Test various patterns
    assert wd.search("bad") == True
    assert wd.search(".ad") == True  # Matches bad, dad, mad, pad
    assert wd.search("b..") == True  # Matches bad, bat
    assert wd.search(".at") == True  # Matches bat, cat
    assert wd.search("...") == True  # Matches all 3-letter words
    assert wd.search("..t") == True  # Matches bat, cat
    assert wd.search("p.d") == True  # Matches pad
    assert wd.search("..g") == False # No match
    
    print("Complex pattern tests passed!")
```

### Performance Test Cases

```python
def test_performance():
    import time
    
    wd = WordDictionary()
    
    # Test 1: Large number of additions
    start = time.time()
    for i in range(10000):
        wd.addWord(f"word{i}")
    add_time = time.time() - start
    print(f"Added 10000 words in {add_time:.3f}s")
    
    # Test 2: Exact searches (should be fast)
    start = time.time()
    for i in range(1000):
        wd.search(f"word{i}")
    exact_time = time.time() - start
    print(f"1000 exact searches in {exact_time:.3f}s")
    
    # Test 3: Wildcard searches
    start = time.time()
    for i in range(100):
        wd.search("word....")
    wildcard_time = time.time() - start
    print(f"100 wildcard searches in {wildcard_time:.3f}s")

def test_worst_case():
    wd = WordDictionary()
    
    # Create worst case: many words of same length
    for i in range(100):
        word = ''.join(chr(97 + (i % 26)) for _ in range(5))
        wd.addWord(word)
    
    # Worst case search: all wildcards
    result = wd.search(".....")
    print(f"Worst case search completed: {result}")
```

---

## Comparison with Regular Trie

### Regular Trie (No Wildcards)

```python
class RegularTrie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.isEndOfWord = True
    
    def search(self, word: str) -> bool:
        """Simple iterative search - O(m)"""
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.isEndOfWord
```

### Comparison Table

| Feature | Regular Trie | Wildcard Trie |
|---------|--------------|---------------|
| **Search Time** | O(m) always | O(m) to O(26^k × m) |
| **Space** | O(n × m) | O(n × m) |
| **Implementation** | Iterative | Recursive (DFS) |
| **Complexity** | Simple | Moderate |
| **Use Cases** | Exact match, prefix | Pattern matching |
| **Backtracking** | No | Yes (for wildcards) |

### When to Use Each

**Regular Trie:**
- Exact string matching
- Prefix queries (startsWith)
- Autocomplete features
- IP routing tables
- Simple dictionary lookups

**Wildcard Trie:**
- Pattern matching with wildcards
- Regex-like queries
- Flexible search requirements
- Game word puzzles
- Spell checker with unknown letters

---

## Related Problems & Variations

### 1. Implement Trie (Prefix Tree) - LeetCode #208
**Relation:** Foundation for this problem
```python
# Basic operations without wildcards
- insert(word)
- search(word)
- startsWith(prefix)
```

### 2. Word Search II - LeetCode #212
**Relation:** Trie + DFS on 2D board
```python
# Find all words from dictionary on board
# Uses trie for efficient word lookup during DFS
```

### 3. Replace Words - LeetCode #648
**Relation:** Trie for prefix matching
```python
# Replace words with shortest prefix from dictionary
# Demonstrates trie prefix search
```

### 4. Implement Magic Dictionary - LeetCode #676
**Relation:** Similar wildcard concept
```python
# Search with exactly one character different
# buildDict(dictionary)
# search(searchWord)
```

### 5. Palindrome Pairs - LeetCode #336
**Relation:** Advanced trie application
```python
# Find word pairs that form palindromes
# Uses trie for reverse word matching
```

### 6. Stream of Characters - LeetCode #1032
**Relation:** Trie with streaming data
```python
# Query if recent characters match any word
# Requires suffix matching with trie
```

---

## Alternative Approaches

### Approach 1: HashSet (Simple but Limited)

```python
class WordDictionary:
    def __init__(self):
        self.words = set()
    
    def addWord(self, word: str) -> None:
        self.words.add(word)  # O(1)
    
    def search(self, word: str) -> bool:
        # O(n × m) where n = number of words
        if '.' not in word:
            return word in self.words  # O(1)
        
        # Brute force: check all words
        for stored_word in self.words:
            if len(stored_word) != len(word):
                continue
            if all(w == p or p == '.' 
                   for w, p in zip(stored_word, word)):
                return True
        return False
```

**Pros:**
- Simple implementation
- O(1) add operation
- Good for small datasets

**Cons:**
- O(n × m) search with wildcards
- No prefix sharing benefits
- Poor scalability

### Approach 2: Regex

```python
import re

class WordDictionary:
    def __init__(self):
        self.words = []
    
    def addWord(self, word: str) -> None:
        self.words.append(word)
    
    def search(self, word: str) -> bool:
        # Convert '.' to regex '.'
        pattern = "^" + word + "$"
        regex = re.compile(pattern)
        
        return any(regex.match(w) for w in self.words)
```

**Pros:**
- Leverages built-in regex engine
- Can handle more complex patterns

**Cons:**
- O(n × m) search
- Regex compilation overhead
- Less efficient than trie

### Approach 3: Suffix Array (Overkill)

```python
class WordDictionary:
    def __init__(self):
        self.words = []
        self.suffix_array = None
    
    def addWord(self, word: str) -> None:
        self.words.append(word)
        # Rebuild suffix array
        # Complex implementation
    
    def search(self, word: str) -> bool:
        # Use suffix array for pattern matching
        # Complex but efficient for many queries
        pass
```

**Pros:**
- Efficient for many pattern queries
- Good for large static datasets

**Cons:**
- Complex implementation
- Requires preprocessing
- Overkill for this problem

---

## Advanced Patterns & Extensions

### Extension 1: Case-Insensitive Search

```python
class WordDictionary:
    def addWord(self, word: str) -> None:
        # Convert to lowercase
        word = word.lower()
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.isEndOfWord = True
    
    def search(self, word: str) -> bool:
        word = word.lower()  # Normalize input
        return self._dfs(word, 0, self.root)
```

### Extension 2: Wildcard Length Matching

```python
def search_with_length_wildcard(self, pattern: str) -> List[str]:
    """
    Support '*' for zero or more characters
    Example: "test*" matches "test", "testing", "tester"
    """
    # Requires modified DFS with backtracking
    # More complex than single character wildcard
    pass
```

### Extension 3: Multi-Wildcard Characters

```python
def search_advanced(self, pattern: str) -> bool:
    """
    Support multiple wildcard types:
    - '.' : matches exactly one character
    - '*' : matches zero or more characters
    - '?' : matches zero or one character
    """
    # Similar to regex matching
    # Requires dynamic programming or complex DFS
    pass
```

### Extension 4: Count Matches

```python
def count_matches(self, word: str) -> int:
    """
    Return count of words matching pattern
    """
    self.match_count = 0
    self._dfs_count(word, 0, self.root)
    return self.match_count

def _dfs_count(self, word: str, index: int, node: TrieNode):
    if index == len(word):
        if node.isEndOfWord:
            self.match_count += 1
        return
    
    char = word[index]
    if char != '.':
        if char in node.children:
            self._dfs_count(word, index + 1, node.children[char])
    else:
        for child in node.children.values():
            self._dfs_count(word, index + 1, child)
```

### Extension 5: Get All Matches

```python
def get_all_matches(self, word: str) -> List[str]:
    """
    Return all words matching the pattern
    """
    self.matches = []
    self.current_word = []
    self._dfs_collect(word, 0, self.root)
    return self.matches

def _dfs_collect(self, word: str, index: int, node: TrieNode):
    if index == len(word):
        if node.isEndOfWord:
            self.matches.append(''.join(self.current_word))
        return
    
    char = word[index]
    if char != '.':
        if char in node.children:
            self.current_word.append(char)
            self._dfs_collect(word, index + 1, node.children[char])
            self.current_word.pop()
    else:
        for child_char, child_node in node.children.items():
            self.current_word.append(child_char)
            self._dfs_collect(word, index + 1, child_node)
            self.current_word.pop()
```

---

## Interview Tips & Discussion Points

### What Interviewers Look For

1. **Understanding of Trie Structure**
   - Can you explain why trie is suitable?
   - What are the space-time tradeoffs?

2. **DFS/Recursion Skills**
   - How do you handle wildcards?
   - Can you explain the recursion tree?

3. **Edge Case Awareness**
   - What happens with all wildcards?
   - How do you handle empty strings?

4. **Optimization Thinking**
   - Can you improve the solution?
   - What about caching or pruning?

5. **Code Quality**
   - Clean, readable implementation
   - Proper variable naming
   - Good comments

### Common Interview Questions

**Q: Why use Trie instead of HashMap?**
A: Trie enables prefix sharing (space efficient) and DFS naturally handles wildcards. HashMap would require O(n) iteration for every wildcard search.

**Q: What's the worst-case time complexity?**
A: O(26^k × m) where k is number of wildcards and m is word length. With constraint of max 2 dots, it's O(676m), effectively O(m).

**Q: Can you optimize the wildcard search?**
A: Yes, through:
- Early termination when no children exist
- Caching exact searches
- Array-based children for faster access
- Length-based indexing

**Q: How would you handle multiple wildcard types?**
A: Would need more complex DFS with different branching logic for each wildcard type, possibly using dynamic programming for patterns like '*'.

**Q: What if we need to support deletion?**
A: Would add remove() method that traverses to word end, unmarks it, and potentially prunes unused nodes.

---

## Key Takeaways

1. **Trie + DFS Pattern**: Powerful combination for pattern matching
2. **Wildcard Handling**: Each '.' requires exploring all children
3. **Time Complexity**: Varies dramatically with wildcards present
4. **Space Efficiency**: Trie shares common prefixes
5. **Recursion Depth**: Equals word length, manageable for constraints
6. **Optimization**: Early termination and caching can help
7. **Real-world Usage**: Spell checkers, search engines, autocomplete

---

## Practice Problems

1. **LeetCode #208** - Implement Trie (Prerequisite)
2. **LeetCode #212** - Word Search II (Trie + DFS)
3. **LeetCode #676** - Implement Magic Dictionary (Similar pattern)
4. **LeetCode #720** - Longest Word in Dictionary (Trie application)
5. **LeetCode #648** - Replace Words (Prefix matching)
6. **LeetCode #1032** - Stream of Characters (Suffix trie)
7. **LeetCode #421** - Maximum XOR of Two Numbers (Trie with bits)

---

## Summary

Design Add and Search Words is a classic problem that combines:
- **Data Structure**: Trie for efficient word storage
- **Algorithm**: DFS for wildcard pattern matching
- **Technique**: Backtracking to explore all possible paths

The key insight is recognizing that wildcards require exploring multiple branches, making DFS the natural choice. With the constraint of at most 2 wildcards, the solution remains efficient enough for practical use.

**Core Formula:**
```
No wildcards: O(m)
With k wildcards: O(26^k × m)
Constrained (k ≤ 2): O(676m) ≈ O(m)
```

This problem is excellent preparation for understanding:
- Trie data structures
- Recursive pattern matching
- Time complexity with branching factors
- Real-world search applications
