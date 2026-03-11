# LeetCode 208: Implement Trie (Prefix Tree)

**Difficulty:** Medium  
**Pattern:** Trie Data Structure  
**Key Concept:** TrieNode with children map and isEnd flag  
**Time Complexity:** O(m) per operation where m is the key length  
**Space Complexity:** O(n*m) where n is number of keys and m is average key length

---

## Problem Statement

A **trie** (pronounced as "try") or **prefix tree** is a tree data structure used to efficiently store and retrieve keys in a dataset of strings. There are various applications of this data structure, such as autocomplete and spellchecker.

Implement the Trie class:

- `Trie()` Initializes the trie object.
- `void insert(String word)` Inserts the string `word` into the trie.
- `boolean search(String word)` Returns `true` if the string `word` is in the trie (i.e., was inserted before), and `false` otherwise.
- `boolean startsWith(String prefix)` Returns `true` if there is a previously inserted string `word` that has the prefix `prefix`, and `false` otherwise.

### Example 1:

```
Input
["Trie", "insert", "search", "search", "startsWith", "insert", "search"]
[[], ["apple"], ["apple"], ["app"], ["app"], ["app"], ["app"]]

Output
[null, null, true, false, true, null, true]

Explanation
Trie trie = new Trie();
trie.insert("apple");
trie.search("apple");   // return True
trie.search("app");     // return False
trie.startsWith("app"); // return True
trie.insert("app");
trie.search("app");     // return True
```

### Constraints:

- `1 <= word.length, prefix.length <= 2000`
- `word` and `prefix` consist only of lowercase English letters.
- At most `3 * 10^4` calls **in total** will be made to `insert`, `search`, and `startsWith`.

---

## Understanding Trie Data Structure

### What is a Trie?

A Trie (from re**trie**val) is a tree-like data structure where:
- Each node represents a character
- The root is empty
- Each path from root to a node represents a prefix
- Words are marked by a flag at the terminal node

### Visual Representation

```
Example: Insert "cat", "car", "card", "dog"

                root
               /    \
              c      d
              |      |
              a      o
             / \     |
            t   r    g*
            |   |
            *   d
                |
                *
                
* = end of word marker (isEnd = true)
```

### Key Properties

1. **Prefix Sharing**: Common prefixes are stored only once
2. **Fast Lookup**: O(m) time for word of length m
3. **Space Efficient**: For large datasets with common prefixes
4. **Ordered**: Words can be retrieved in lexicographical order

### Trie vs Other Data Structures

| Operation | Hash Table | Binary Search Tree | Trie |
|-----------|------------|-------------------|------|
| Insert | O(m) | O(m log n) | O(m) |
| Search | O(m) | O(m log n) | O(m) |
| Prefix Search | O(n*m) | O(m log n + k) | O(m) |
| Space | O(n*m) | O(n*m) | O(ALPHABET_SIZE * n * m) |
| Ordered Traversal | No | Yes | Yes |

---

## Core Concept: TrieNode Structure

### Basic TrieNode Components

```python
class TrieNode:
    def __init__(self):
        # Dictionary to store child nodes
        # Key: character, Value: TrieNode
        self.children = {}
        
        # Flag to mark end of a word
        self.isEnd = False
```

### Why These Components?

1. **children (dict/map)**: 
   - Stores references to child nodes
   - Each key is a character, value is next TrieNode
   - Allows efficient character lookup

2. **isEnd (boolean)**:
   - Marks if current node is end of a valid word
   - Distinguishes between prefix and complete word
   - Essential for search vs startsWith differentiation

---

## Implementation Approaches

### Approach 1: Dictionary-Based (Recommended)

**Pros**: Clean, flexible, easy to understand  
**Cons**: Slightly more memory per node due to dictionary overhead

```python
class TrieNode:
    def __init__(self):
        self.children = {}  # char -> TrieNode
        self.isEnd = False

class Trie:
    def __init__(self):
        """
        Initialize the trie with an empty root node.
        The root represents an empty string.
        """
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        """
        Inserts a word into the trie.
        
        Time Complexity: O(m) where m is length of word
        Space Complexity: O(m) in worst case (all new nodes)
        
        Args:
            word: String to insert (lowercase letters only)
        """
        node = self.root
        
        # Traverse/create path for each character
        for char in word:
            # If character doesn't exist, create new node
            if char not in node.children:
                node.children[char] = TrieNode()
            
            # Move to child node
            node = node.children[char]
        
        # Mark end of word
        node.isEnd = True
    
    def search(self, word: str) -> bool:
        """
        Returns if the word is in the trie.
        
        Time Complexity: O(m) where m is length of word
        Space Complexity: O(1)
        
        Args:
            word: String to search for
            
        Returns:
            True if word exists as complete word, False otherwise
        """
        node = self.root
        
        # Traverse the trie following the word
        for char in word:
            # If character doesn't exist, word not in trie
            if char not in node.children:
                return False
            
            # Move to child node
            node = node.children[char]
        
        # Word exists only if we end at a marked node
        return node.isEnd
    
    def startsWith(self, prefix: str) -> bool:
        """
        Returns if there is any word in the trie that starts with prefix.
        
        Time Complexity: O(m) where m is length of prefix
        Space Complexity: O(1)
        
        Args:
            prefix: String prefix to search for
            
        Returns:
            True if any word has this prefix, False otherwise
        """
        node = self.root
        
        # Traverse the trie following the prefix
        for char in prefix:
            # If character doesn't exist, no word with this prefix
            if char not in node.children:
                return False
            
            # Move to child node
            node = node.children[char]
        
        # If we successfully traversed prefix, it exists
        return True
```

### Approach 2: Array-Based (Memory Optimized for Small Alphabets)

**Pros**: Faster lookup (direct array access), fixed memory per node  
**Cons**: Wastes space if alphabet is large or sparse

```python
class TrieNode:
    def __init__(self):
        # Array of 26 elements for 'a' to 'z'
        self.children = [None] * 26
        self.isEnd = False

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def _char_to_index(self, char: str) -> int:
        """Convert character to index (0-25)"""
        return ord(char) - ord('a')
    
    def insert(self, word: str) -> None:
        """
        Time Complexity: O(m)
        Space Complexity: O(m * 26) = O(m) for new nodes
        """
        node = self.root
        
        for char in word:
            index = self._char_to_index(char)
            
            # Create new node if doesn't exist
            if node.children[index] is None:
                node.children[index] = TrieNode()
            
            node = node.children[index]
        
        node.isEnd = True
    
    def search(self, word: str) -> bool:
        """
        Time Complexity: O(m)
        Space Complexity: O(1)
        """
        node = self.root
        
        for char in word:
            index = self._char_to_index(char)
            
            # Character doesn't exist
            if node.children[index] is None:
                return False
            
            node = node.children[index]
        
        return node.isEnd
    
    def startsWith(self, prefix: str) -> bool:
        """
        Time Complexity: O(m)
        Space Complexity: O(1)
        """
        node = self.root
        
        for char in prefix:
            index = self._char_to_index(char)
            
            if node.children[index] is None:
                return False
            
            node = node.children[index]
        
        return True
```

### Approach 3: Optimized with Helper Method

**Pros**: Reduces code duplication, cleaner  
**Cons**: Slightly more method calls

```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.isEnd = False

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.isEnd = True
    
    def _find_node(self, prefix: str) -> TrieNode:
        """
        Helper method to find the node corresponding to prefix.
        Returns None if prefix doesn't exist.
        
        Time Complexity: O(m) where m is length of prefix
        Space Complexity: O(1)
        """
        node = self.root
        for char in prefix:
            if char not in node.children:
                return None
            node = node.children[char]
        return node
    
    def search(self, word: str) -> bool:
        """Search for complete word"""
        node = self._find_node(word)
        return node is not None and node.isEnd
    
    def startsWith(self, prefix: str) -> bool:
        """Check if prefix exists"""
        node = self._find_node(prefix)
        return node is not None
```

### Approach 4: Using defaultdict

**Pros**: More Pythonic, cleaner insert  
**Cons**: Less explicit about structure

```python
from collections import defaultdict

class TrieNode:
    def __init__(self):
        self.children = defaultdict(TrieNode)
        self.isEnd = False

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        node = self.root
        for char in word:
            # defaultdict automatically creates TrieNode if not exists
            node = node.children[char]
        node.isEnd = True
    
    def search(self, word: str) -> bool:
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.isEnd
    
    def startsWith(self, prefix: str) -> bool:
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        return True
```

---

## Operation Deep Dive

### Insert Operation

**Purpose**: Add a word to the trie

**Algorithm**:
1. Start at root
2. For each character in word:
   - If child for character exists, move to it
   - If not, create new node and move to it
3. Mark final node as end of word

**Example**: Inserting "cat" into empty trie

```
Step 0: Start at root
        root
        
Step 1: Insert 'c'
        root
         |
         c
         
Step 2: Insert 'a'
        root
         |
         c
         |
         a
         
Step 3: Insert 't' and mark end
        root
         |
         c
         |
         a
         |
         t*  (* = isEnd = True)
```

**Edge Cases**:
- Empty string (if allowed)
- Inserting duplicate word (just marks isEnd again)
- Inserting prefix of existing word
- Inserting word that is extension of existing word

### Search Operation

**Purpose**: Check if exact word exists in trie

**Algorithm**:
1. Start at root
2. For each character in word:
   - If child for character exists, move to it
   - If not, return False
3. Return True only if final node has isEnd = True

**Example**: Searching "cat" and "ca"

```
Trie contains: "cat"

        root
         |
         c
         |
         a
         |
         t*

Search "cat":
- Follow c -> a -> t
- t.isEnd = True
- Return True ✓

Search "ca":
- Follow c -> a
- a.isEnd = False
- Return False ✗ (prefix exists but not as word)
```

**Key Difference from startsWith**:
- Must check `isEnd` flag
- Path must lead to terminal node

### StartsWith Operation

**Purpose**: Check if any word has given prefix

**Algorithm**:
1. Start at root
2. For each character in prefix:
   - If child for character exists, move to it
   - If not, return False
3. Return True (don't check isEnd)

**Example**: Checking startsWith "ca"

```
Trie contains: "cat", "car"

        root
         |
         c
         |
         a
        / \
       t*  r*

startsWith "ca":
- Follow c -> a
- Successfully traversed entire prefix
- Return True ✓ (both "cat" and "car" have prefix "ca")
```

---

## Complexity Analysis

### Time Complexity

| Operation | Complexity | Explanation |
|-----------|-----------|-------------|
| Insert | O(m) | Traverse/create m nodes, m = word length |
| Search | O(m) | Traverse m nodes, m = word length |
| StartsWith | O(m) | Traverse m nodes, m = prefix length |

**Key Points**:
- Time is independent of number of words in trie (n)
- Only depends on length of word/prefix (m)
- Dictionary lookup: O(1) average, O(26) = O(1) worst for lowercase letters
- Array lookup: O(1) always

### Space Complexity

**Overall Trie Space**: O(ALPHABET_SIZE × n × m)

Where:
- ALPHABET_SIZE = 26 for lowercase letters
- n = number of words
- m = average word length

**Breakdown**:
- Each node: O(ALPHABET_SIZE) for children storage
- Total nodes: O(n × m) in worst case (no shared prefixes)
- Best case: O(m_longest) with maximum prefix sharing
- Practical case: Between best and worst

**Per Operation**:
- Insert: O(m) space for new nodes (worst case)
- Search: O(1) no extra space
- StartsWith: O(1) no extra space

**Dictionary vs Array**:
- Dictionary: Less space if sparse (few children per node)
- Array: Fixed space per node (26 pointers), better for dense tries

---

## Advanced Operations and Extensions

### 1. Delete Operation

```python
def delete(self, word: str) -> bool:
    """
    Delete a word from the trie.
    Returns True if word was deleted, False if not found.
    
    Time Complexity: O(m)
    Space Complexity: O(m) for recursion stack
    """
    def _delete_helper(node: TrieNode, word: str, index: int) -> bool:
        """
        Returns True if current node should be deleted.
        """
        # Base case: reached end of word
        if index == len(word):
            # Word doesn't exist
            if not node.isEnd:
                return False
            
            # Unmark end of word
            node.isEnd = False
            
            # Delete node if it has no children
            return len(node.children) == 0
        
        char = word[index]
        
        # Character doesn't exist
        if char not in node.children:
            return False
        
        child = node.children[char]
        should_delete_child = _delete_helper(child, word, index + 1)
        
        # Delete child if needed
        if should_delete_child:
            del node.children[char]
        
        # Delete current node if:
        # 1. It's not end of another word
        # 2. It has no other children
        return len(node.children) == 0 and not node.isEnd
    
    return _delete_helper(self.root, word, 0)
```

### 2. Get All Words with Prefix

```python
def get_words_with_prefix(self, prefix: str) -> List[str]:
    """
    Get all words in trie that start with given prefix.
    
    Time Complexity: O(m + k) where k is number of nodes in subtree
    Space Complexity: O(k) for result and recursion
    """
    result = []
    node = self._find_node(prefix)
    
    if node is None:
        return result
    
    def _dfs(node: TrieNode, path: str):
        """Collect all words from current node"""
        if node.isEnd:
            result.append(prefix + path)
        
        for char, child in node.children.items():
            _dfs(child, path + char)
    
    _dfs(node, "")
    return result
```

### 3. Count Words with Prefix

```python
def count_words_with_prefix(self, prefix: str) -> int:
    """
    Count how many words have the given prefix.
    
    Time Complexity: O(m + k)
    Space Complexity: O(h) for recursion, h = height
    """
    node = self._find_node(prefix)
    
    if node is None:
        return 0
    
    def _count_words(node: TrieNode) -> int:
        """Count words in subtree"""
        count = 1 if node.isEnd else 0
        
        for child in node.children.values():
            count += _count_words(child)
        
        return count
    
    return _count_words(node)
```

### 4. Longest Common Prefix

```python
def longest_common_prefix(self) -> str:
    """
    Find longest common prefix of all words in trie.
    
    Time Complexity: O(m) where m is length of LCP
    Space Complexity: O(1)
    """
    if not self.root.children:
        return ""
    
    node = self.root
    prefix = []
    
    # Continue while there's exactly one child and not end of word
    while len(node.children) == 1 and not node.isEnd:
        char = next(iter(node.children.keys()))
        prefix.append(char)
        node = node.children[char]
    
    return ''.join(prefix)
```

---

## Applications of Tries

### 1. Autocomplete Systems

```python
class AutocompleteSystem:
    """
    Autocomplete system that suggests words based on prefix.
    """
    def __init__(self, words: List[str]):
        self.trie = Trie()
        for word in words:
            self.trie.insert(word)
    
    def suggest(self, prefix: str, limit: int = 5) -> List[str]:
        """Get top suggestions for prefix"""
        return self.trie.get_words_with_prefix(prefix)[:limit]
```

### 2. Spell Checker

```python
def is_spelled_correctly(self, word: str) -> bool:
    """Check if word exists in dictionary"""
    return self.search(word)

def suggest_corrections(self, word: str) -> List[str]:
    """Suggest corrections for misspelled word"""
    suggestions = []
    
    # Check words with one character difference
    for i in range(len(word)):
        for c in 'abcdefghijklmnopqrstuvwxyz':
            candidate = word[:i] + c + word[i+1:]
            if self.search(candidate) and candidate != word:
                suggestions.append(candidate)
    
    return suggestions
```

### 3. IP Routing (Longest Prefix Matching)

```python
class IPRouter:
    """
    Use trie for IP address routing table lookups.
    Finds longest matching prefix for routing.
    """
    def __init__(self):
        self.trie = Trie()
        self.routes = {}  # prefix -> next_hop
    
    def add_route(self, ip_prefix: str, next_hop: str):
        """Add routing entry"""
        self.trie.insert(ip_prefix)
        self.routes[ip_prefix] = next_hop
    
    def lookup(self, ip: str) -> str:
        """Find longest matching prefix"""
        longest_match = ""
        node = self.trie.root
        current = ""
        
        for char in ip:
            if char not in node.children:
                break
            current += char
            node = node.children[char]
            if node.isEnd:
                longest_match = current
        
        return self.routes.get(longest_match, "default")
```

### 4. Word Games (Boggle Solver)

```python
def find_words_in_board(self, board: List[List[str]], 
                        dictionary: Trie) -> Set[str]:
    """
    Find all dictionary words in a Boggle board.
    Trie makes it efficient to prune invalid prefixes.
    """
    rows, cols = len(board), len(board[0])
    found_words = set()
    
    def dfs(r: int, c: int, node: TrieNode, path: str, visited: Set):
        if node.isEnd:
            found_words.add(path)
        
        if r < 0 or r >= rows or c < 0 or c >= cols:
            return
        if (r, c) in visited:
            return
        
        char = board[r][c]
        if char not in node.children:
            return  # Prefix doesn't exist - prune!
        
        visited.add((r, c))
        
        # Explore all 8 directions
        for dr, dc in [(-1,-1),(-1,0),(-1,1),(0,-1),(0,1),(1,-1),(1,0),(1,1)]:
            dfs(r+dr, c+dc, node.children[char], path+char, visited)
        
        visited.remove((r, c))
    
    # Start DFS from each cell
    for r in range(rows):
        for c in range(cols):
            dfs(r, c, dictionary.root, "", set())
    
    return found_words
```

---

## Edge Cases and Gotchas

### Edge Case 1: Empty String

```python
# Should empty string be allowed?
trie.insert("")  # Edge case
trie.search("")  # Should return True if inserted

# Implementation consideration:
# Root node with isEnd=True represents empty string
```

### Edge Case 2: Prefix vs Complete Word

```python
trie.insert("apple")

# These are different:
trie.search("app")      # False - not a complete word
trie.startsWith("app")  # True - valid prefix

# Must check isEnd flag in search
```

### Edge Case 3: Duplicate Insertions

```python
trie.insert("cat")
trie.insert("cat")  # Inserting again

# Should work fine - just marks isEnd=True again
# No duplicate nodes created
```

### Edge Case 4: Word is Prefix of Another

```python
trie.insert("car")
trie.insert("card")

# Both should work:
trie.search("car")   # True
trie.search("card")  # True

# Tree structure:
#   c -> a -> r* -> d*
```

### Edge Case 5: Overlapping Words

```python
trie.insert("cat")
trie.insert("car")
trie.insert("cart")

# Trie shares common prefix "ca":
#        c
#        |
#        a
#       / \
#      t*  r* -> t*
```

### Edge Case 6: Single Character Words

```python
trie.insert("a")
trie.insert("i")

trie.search("a")  # Should return True
```

### Edge Case 7: Case Sensitivity

```python
# Problem states lowercase only, but be aware:
trie.insert("Cat")  # If uppercase allowed?

# Solution: normalize input
def insert(self, word: str):
    word = word.lower()  # Normalize
    # ... rest of insert
```

---

## Test Cases

### Basic Test Cases

```python
def test_basic_operations():
    trie = Trie()
    
    # Test 1: Insert and search single word
    trie.insert("apple")
    assert trie.search("apple") == True
    assert trie.search("app") == False
    
    # Test 2: StartsWith
    assert trie.startsWith("app") == True
    assert trie.startsWith("appl") == True
    assert trie.startsWith("apple") == True
    assert trie.startsWith("apples") == False
    
    # Test 3: Insert prefix as word
    trie.insert("app")
    assert trie.search("app") == True
    assert trie.search("apple") == True
```

### Comprehensive Test Cases

```python
def test_comprehensive():
    trie = Trie()
    
    # Test multiple words with common prefixes
    words = ["cat", "car", "card", "care", "careful", "dog", "dodge"]
    for word in words:
        trie.insert(word)
    
    # Test all words found
    for word in words:
        assert trie.search(word) == True, f"Failed to find {word}"
    
    # Test non-existent words
    assert trie.search("ca") == False
    assert trie.search("dogs") == False
    assert trie.search("cares") == False
    
    # Test prefixes
    assert trie.startsWith("ca") == True
    assert trie.startsWith("car") == True
    assert trie.startsWith("do") == True
    assert trie.startsWith("cat") == True
    assert trie.startsWith("xyz") == False
```

### Edge Case Tests

```python
def test_edge_cases():
    trie = Trie()
    
    # Test 1: Empty trie
    assert trie.search("anything") == False
    assert trie.startsWith("anything") == False
    
    # Test 2: Single character
    trie.insert("a")
    assert trie.search("a") == True
    assert trie.startsWith("a") == True
    assert trie.search("ab") == False
    
    # Test 3: Duplicate insertion
    trie.insert("hello")
    trie.insert("hello")
    assert trie.search("hello") == True
    
    # Test 4: Word is prefix of another
    trie.insert("test")
    trie.insert("testing")
    assert trie.search("test") == True
    assert trie.search("testing") == True
    assert trie.search("testin") == False
    
    # Test 5: Long word
    long_word = "a" * 2000  # Max length
    trie.insert(long_word)
    assert trie.search(long_word) == True
```

### Performance Test Cases

```python
def test_performance():
    import time
    
    trie = Trie()
    
    # Test with 10000 words
    words = [f"word{i}" for i in range(10000)]
    
    # Insert performance
    start = time.time()
    for word in words:
        trie.insert(word)
    insert_time = time.time() - start
    print(f"Insert 10k words: {insert_time:.3f}s")
    
    # Search performance
    start = time.time()
    for word in words:
        assert trie.search(word) == True
    search_time = time.time() - start
    print(f"Search 10k words: {search_time:.3f}s")
    
    # StartsWith performance
    start = time.time()
    for word in words[:1000]:
        assert trie.startsWith(word[:4]) == True
    prefix_time = time.time() - start
    print(f"Prefix search 1k: {prefix_time:.3f}s")
```

---

## Optimizations

### Optimization 1: Compressed Trie (Patricia Trie)

**Problem**: Each node stores single character, wastes space for unique paths

**Solution**: Store strings instead of single characters

```python
class CompressedTrieNode:
    def __init__(self):
        self.children = {}  # edge_label (string) -> Node
        self.isEnd = False

# Example:
# Normal Trie: r->o->m->a->n->c->e*
# Compressed: romance*
# Saves 6 nodes!
```

### Optimization 2: Lazy Node Creation

**Current**: Create all 26 array slots even if unused

**Better**: Use dictionary (already implemented in Approach 1)

### Optimization 3: Memory Pool for Nodes

**Problem**: Many small allocations for nodes

**Solution**: Pre-allocate node pool

```python
class TrieNodePool:
    def __init__(self, size=10000):
        self.pool = [TrieNode() for _ in range(size)]
        self.index = 0
    
    def get_node(self) -> TrieNode:
        if self.index >= len(self.pool):
            self.pool.extend([TrieNode() for _ in range(10000)])
        node = self.pool[self.index]
        self.index += 1
        return node
```

### Optimization 4: Ternary Search Tree

**Problem**: Trie nodes waste space with many empty children

**Solution**: Use ternary tree (left, equal, right)

```python
class TSTNode:
    def __init__(self, char):
        self.char = char
        self.left = None    # Less than char
        self.equal = None   # Next char in word
        self.right = None   # Greater than char
        self.isEnd = False

# More space efficient than array-based trie
# Good middle ground between trie and BST
```

### Optimization 5: Alphabet Reduction

**Problem**: Storing 26 pointers per node

**Solution**: If vocabulary is limited, use smaller alphabet

```python
# Example: Only vowels and common consonants
ALPHABET = "aeiorstn"  # 8 chars instead of 26

def char_to_index(char):
    return ALPHABET.index(char)
```

### Optimization 6: Path Compression During Insert

```python
def insert_compressed(self, word: str) -> None:
    """Insert with path compression for single-child paths"""
    node = self.root
    i = 0
    
    while i < len(word):
        char = word[i]
        
        # If single child path, compress
        if len(node.children) == 1 and not node.isEnd:
            # Store substring instead of single char
            pass
        
        if char not in node.children:
            node.children[char] = TrieNode()
        node = node.children[char]
        i += 1
    
    node.isEnd = True
```

---

## Common Mistakes and How to Avoid Them

### Mistake 1: Forgetting to Check isEnd in Search

```python
# ❌ WRONG
def search(self, word: str) -> bool:
    node = self.root
    for char in word:
        if char not in node.children:
            return False
        node = node.children[char]
    return True  # BUG: Doesn't check isEnd!

# ✓ CORRECT
def search(self, word: str) -> bool:
    node = self.root
    for char in word:
        if char not in node.children:
            return False
        node = node.children[char]
    return node.isEnd  # Must check isEnd flag
```

### Mistake 2: Not Initializing Root Node

```python
# ❌ WRONG
class Trie:
    def __init__(self):
        pass  # No root!

# ✓ CORRECT
class Trie:
    def __init__(self):
        self.root = TrieNode()
```

### Mistake 3: Mutating Node Reference

```python
# ❌ WRONG
def insert(self, word: str) -> None:
    node = self.root
    for char in word:
        # Creates new node but doesn't link it!
        node = TrieNode()
        node.children[char] = node

# ✓ CORRECT
def insert(self, word: str) -> None:
    node = self.root
    for char in word:
        if char not in node.children:
            node.children[char] = TrieNode()
        node = node.children[char]  # Move to child
```

### Mistake 4: Incorrect Array Index Calculation

```python
# ❌ WRONG
index = ord(char) - 97  # Hardcoded, fragile

# ✓ CORRECT
index = ord(char) - ord('a')  # Clear intent
```

---

## Related Problems

### 1. **Design Add and Search Words Data Structure** (LeetCode 211)
- **Difficulty**: Medium
- **Variation**: Trie with wildcard search ('.' matches any char)
- **Key Difference**: Requires DFS for wildcard matching

### 2. **Word Search II** (LeetCode 212)
- **Difficulty**: Hard
- **Pattern**: Trie + DFS on 2D grid
- **Application**: Find all words from dictionary in grid

### 3. **Replace Words** (LeetCode 648)
- **Difficulty**: Medium
- **Pattern**: Trie for finding shortest prefix
- **Use Case**: Text processing with dictionary

### 4. **Implement Magic Dictionary** (LeetCode 676)
- **Difficulty**: Medium
- **Variation**: Trie with fuzzy search (one char difference)
- **Extension**: Modified search logic

### 5. **Longest Word in Dictionary** (LeetCode 720)
- **Difficulty**: Medium
- **Pattern**: Trie with prefix validation
- **Challenge**: Find longest word built by prefixes

### 6. **Top K Frequent Words** (LeetCode 692)
- **Difficulty**: Medium
- **Combination**: Trie + Heap/Sorting
- **Extension**: Frequency tracking in trie

### 7. **Word Squares** (LeetCode 425)
- **Difficulty**: Hard
- **Pattern**: Trie + Backtracking
- **Complex**: Multiple constraints

### 8. **Concatenated Words** (LeetCode 472)
- **Difficulty**: Hard
- **Pattern**: Trie + DP
- **Challenge**: Check if word is concatenation of others

### 9. **Palindrome Pairs** (LeetCode 336)
- **Difficulty**: Hard
- **Pattern**: Trie + Palindrome checking
- **Advanced**: Reverse trie technique

### 10. **Stream of Characters** (LeetCode 1032)
- **Difficulty**: Hard
- **Pattern**: Reverse trie
- **Use Case**: Real-time pattern matching

---

## Interview Tips

### What Interviewers Look For

1. **Understanding of Trie Structure**
   - Can explain why trie is better than hash table for prefix queries
   - Knows time/space tradeoffs

2. **Clean Implementation**
   - Proper node structure
   - Clear method organization
   - Handles edge cases

3. **Optimization Awareness**
   - Dictionary vs array tradeoffs
   - When to use trie vs alternatives

4. **Extension Capability**
   - Can add delete operation
   - Can modify for variations (wildcard, frequency)

### Discussion Points

**"Why not just use a HashSet?"**
- HashSet can't efficiently handle prefix queries
- Trie has O(m) prefix search vs O(n*m) for HashSet
- Trie enables autocomplete, spell check, etc.

**"Why not use Binary Search Tree?"**
- BST is O(m log n) vs Trie O(m)
- Trie shares prefixes, BST doesn't
- Trie is simpler for string operations

**"What about space complexity?"**
- Trie uses more space per node (26 pointers)
- But shares prefixes, good for large dictionaries
- Dictionary implementation reduces space for sparse tries

**"Can you make it faster?"**
- Array vs dictionary tradeoff
- Compressed trie for memory
- Ternary search tree as alternative

### Common Follow-ups

1. "Add a delete operation"
2. "Support wildcard search with '.'"
3. "Find longest word with given prefix"
4. "Count words with prefix"
5. "Return all words in lexicographical order"

---

## Summary

### Key Takeaways

1. **Core Concept**: Trie is a tree where each path represents a string prefix
2. **Node Structure**: Children map + isEnd flag
3. **Time Complexity**: O(m) for all operations (m = word length)
4. **Space Complexity**: O(ALPHABET_SIZE × n × m)
5. **Use Cases**: Autocomplete, spell check, IP routing, word games

### When to Use Trie

✅ **Use Trie when:**
- Need fast prefix queries
- Have many strings with common prefixes
- Implementing autocomplete/search suggestions
- Word games (Scrabble, Boggle)
- IP routing tables

❌ **Don't use Trie when:**
- Only need exact string matching (use HashSet)
- Strings have no common prefixes
- Memory is severely constrained
- String alphabet is very large (Unicode)

### Implementation Checklist

- [ ] TrieNode with children and isEnd
- [ ] Root node initialized in constructor
- [ ] Insert creates new nodes as needed
- [ ] Search checks isEnd flag
- [ ] StartsWith doesn't check isEnd
- [ ] Handle empty strings appropriately
- [ ] Consider dictionary vs array implementation
- [ ] Test edge cases (prefix, duplicate, empty)

---

## Quick Reference

```python
# Standard Implementation Template
class TrieNode:
    def __init__(self):
        self.children = {}
        self.isEnd = False

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word: str) -> None:
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.isEnd = True
    
    def search(self, word: str) -> bool:
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.isEnd
    
    def startsWith(self, prefix: str) -> bool:
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        return True
```

### Complexity Cheat Sheet

| Operation | Time | Space | Notes |
|-----------|------|-------|-------|
| Insert | O(m) | O(m) | m = word length |
| Search | O(m) | O(1) | Just traversal |
| Prefix | O(m) | O(1) | Just traversal |
| Delete | O(m) | O(m) | Recursion stack |
| Get All | O(m+k) | O(k) | k = result size |

---

**Last Updated**: 2024  
**LeetCode Link**: https://leetcode.com/problems/implement-trie-prefix-tree/  
**Pattern**: Trie Data Structure  
**Difficulty**: Medium
