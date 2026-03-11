# LeetCode 212: Word Search II

## Problem Statement
Given an `m x n` board of characters and a list of strings `words`, return all words on the board.

Each word must be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once in a word.

**Example 1:**
```
Input: board = [["o","a","a","n"],
                ["e","t","a","e"],
                ["i","h","k","r"],
                ["i","f","l","v"]], 
       words = ["oath","pea","eat","rain"]
Output: ["eat","oath"]
```

**Example 2:**
```
Input: board = [["a","b"],
                ["c","d"]], 
       words = ["abcb"]
Output: []
```

**Constraints:**
- `m == board.length`
- `n == board[i].length`
- `1 <= m, n <= 12`
- `board[i][j]` is a lowercase English letter
- `1 <= words.length <= 3 * 10^4`
- `1 <= words[i].length <= 10`
- `words[i]` consists of lowercase English letters
- All strings in `words` are unique

**Difficulty:** Hard

---

## Pattern Recognition
This problem combines two fundamental patterns:
1. **Trie (Prefix Tree)**: Efficient prefix-based word lookup
2. **Backtracking/DFS**: Grid traversal with state restoration

The key insight is that naive approach (searching each word individually) is inefficient. By building a trie of all words, we can:
- Search multiple words simultaneously
- Prune search paths early (no matching prefix)
- Share common prefixes among words

---

## Core Concepts

### Why Trie + Backtracking?

**Naive Approach Problems:**
```
For each word in words:
    For each cell in board:
        DFS to find word
Time: O(words * m * n * 4^maxLen)
```

**Trie Approach Benefits:**
```
Build Trie of all words: O(total_chars)
DFS once from each cell with Trie guidance: O(m * n * 4^maxLen)
```

The trie allows us to:
1. **Prefix sharing**: "eat" and "eaten" share prefix "eat"
2. **Early pruning**: If "xyz" not in trie, stop immediately
3. **Single pass**: One DFS explores all words simultaneously

### Trie Structure for This Problem

```python
class TrieNode:
    def __init__(self):
        self.children = {}  # char -> TrieNode
        self.word = None    # Store complete word at end node
        # Alternative: self.is_end = False (but storing word is better)
```

**Why store the word at end node?**
- Avoids rebuilding word during DFS (performance)
- Makes duplicate detection easier
- Simplifies result collection

---

## Solution Approaches

### Approach 1: Basic Trie + Backtracking

**Algorithm:**
1. Build trie from all words
2. For each cell in board:
   - Start DFS with root of trie
3. During DFS:
   - Check if current char exists in trie children
   - If word found, add to result
   - Mark cell as visited, explore 4 directions
   - Unmark cell (backtrack)

```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.word = None

class Solution:
    def findWords(self, board: List[List[str]], words: List[str]) -> List[str]:
        # Build Trie
        root = TrieNode()
        for word in words:
            node = root
            for char in word:
                if char not in node.children:
                    node.children[char] = TrieNode()
                node = node.children[char]
            node.word = word
        
        # Initialize result and board dimensions
        result = []
        m, n = len(board), len(board[0])
        
        # DFS function
        def dfs(i, j, node):
            # Boundary check
            if i < 0 or i >= m or j < 0 or j >= n:
                return
            
            char = board[i][j]
            
            # Check if visited or char not in trie
            if char == '#' or char not in node.children:
                return
            
            # Move to next trie node
            node = node.children[char]
            
            # Found a word
            if node.word:
                result.append(node.word)
                node.word = None  # Avoid duplicates
            
            # Mark as visited
            board[i][j] = '#'
            
            # Explore 4 directions
            dfs(i + 1, j, node)
            dfs(i - 1, j, node)
            dfs(i, j + 1, node)
            dfs(i, j - 1, node)
            
            # Backtrack
            board[i][j] = char
        
        # Start DFS from each cell
        for i in range(m):
            for j in range(n):
                dfs(i, j, root)
        
        return result
```

**Time Complexity:** O(m * n * 4^L)
- m * n: Starting positions
- 4^L: Worst case branches (L = max word length)
- In practice, much better due to trie pruning

**Space Complexity:** O(W * L + m * n)
- W * L: Trie storage (W words, avg length L)
- m * n: Recursion stack depth

---

### Approach 2: Optimized with Trie Node Removal

**Optimization: Remove matched words from trie**

After finding a word, we can prune the entire branch if it's a leaf node. This prevents:
- Future redundant searches
- Memory waste on processed words

```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.word = None

class Solution:
    def findWords(self, board: List[List[str]], words: List[str]) -> List[str]:
        # Build Trie
        root = TrieNode()
        for word in words:
            node = root
            for char in word:
                if char not in node.children:
                    node.children[char] = TrieNode()
                node = node.children[char]
            node.word = word
        
        result = []
        m, n = len(board), len(board[0])
        
        def dfs(i, j, parent, node):
            char = board[i][j]
            node = node.children[char]
            
            # Check if we found a word
            if node.word:
                result.append(node.word)
                node.word = None  # Mark as found
            
            # Mark cell as visited
            board[i][j] = '#'
            
            # Explore all 4 directions
            for di, dj in [(1, 0), (-1, 0), (0, 1), (0, -1)]:
                ni, nj = i + di, j + dj
                if (0 <= ni < m and 0 <= nj < n and 
                    board[ni][nj] != '#' and 
                    board[ni][nj] in node.children):
                    dfs(ni, nj, node, node)
            
            # Restore cell
            board[i][j] = char
            
            # Optimization: Remove leaf nodes
            if not node.children:
                del parent.children[char]
        
        # Start DFS from each cell
        for i in range(m):
            for j in range(n):
                if board[i][j] in root.children:
                    dfs(i, j, root, root)
        
        return result
```

**Key Optimization:**
```python
# After exploring all paths from this node
if not node.children:
    del parent.children[char]
```

This prunes dead branches, improving both time and space.

---

### Approach 3: Most Optimized Version

**Additional Optimizations:**
1. Early termination: Stop if all words found
2. Direction array for cleaner code
3. Inline boundary checks

```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.word = None
        self.refs = 0  # Track references to this node

class Solution:
    def findWords(self, board: List[List[str]], words: List[str]) -> List[str]:
        # Build Trie with reference counting
        root = TrieNode()
        for word in words:
            node = root
            for char in word:
                if char not in node.children:
                    node.children[char] = TrieNode()
                    node.refs += 1  # Track number of children
                node = node.children[char]
            node.word = word
        
        result = []
        m, n = len(board), len(board[0])
        directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
        
        def dfs(i, j, parent):
            char = board[i][j]
            node = parent.children[char]
            
            # Found a word
            if node.word:
                result.append(node.word)
                node.word = None
            
            # Mark as visited
            board[i][j] = '#'
            
            # Explore 4 directions
            for di, dj in directions:
                ni, nj = i + di, j + dj
                if (0 <= ni < m and 0 <= nj < n and 
                    board[ni][nj] in node.children):
                    dfs(ni, nj, node)
            
            # Restore
            board[i][j] = char
            
            # Prune: Remove leaf node
            if not node.children:
                del parent.children[char]
        
        # Iterate through board
        for i in range(m):
            for j in range(n):
                if board[i][j] in root.children:
                    dfs(i, j, root)
        
        return result
```

---

## Deep Dive: Technical Details

### 1. Duplicate Prevention Strategies

**Problem:** Same word can be found multiple times from different paths.

**Solution 1: Mark word as found**
```python
if node.word:
    result.append(node.word)
    node.word = None  # Prevent future matches
```

**Solution 2: Use set**
```python
result = set()  # Instead of list
if node.word:
    result.add(node.word)
# Return list(result) at end
```

**Comparison:**
- Solution 1: Better performance (no set overhead)
- Solution 2: Simpler if trie can't be modified

### 2. Visited Cell Marking

**Approach 1: In-place marking (Recommended)**
```python
board[i][j] = '#'  # Mark
dfs(...)
board[i][j] = char  # Restore
```

**Pros:**
- No extra space
- Fast

**Cons:**
- Modifies input (but restored)

**Approach 2: Visited set**
```python
visited = set()
visited.add((i, j))
dfs(..., visited)
visited.remove((i, j))
```

**Pros:**
- Doesn't modify input
- Explicit state tracking

**Cons:**
- O(m*n) extra space per recursion level
- Slower (set operations)

### 3. Trie Node Deletion Details

**Why delete nodes?**
```
Example: words = ["oath", "oat"]
Trie: root -> o -> a -> t -> h
                      |
                    word="oat"

After finding "oat" and "oath":
- Both word fields set to None
- "h" node has no children -> delete
- "t" node now has no children -> delete
- "a" node now has no children -> delete
- "o" node now has no children -> delete
```

**Implementation:**
```python
def dfs(i, j, parent, node):
    char = board[i][j]
    node = node.children[char]
    
    # ... DFS logic ...
    
    # Prune dead branches
    if not node.children:
        del parent.children[char]
```

**Why pass parent?**
- Need parent reference to delete child
- Alternative: return boolean indicating if node should be deleted

### 4. Direction Traversal Patterns

**Pattern 1: Explicit (Verbose)**
```python
dfs(i + 1, j, node)
dfs(i - 1, j, node)
dfs(i, j + 1, node)
dfs(i, j - 1, node)
```

**Pattern 2: Loop (Clean)**
```python
directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
for di, dj in directions:
    ni, nj = i + di, j + dj
    if is_valid(ni, nj):
        dfs(ni, nj, node)
```

**Pattern 3: Condensed**
```python
for di, dj in [(1,0), (-1,0), (0,1), (0,-1)]:
    if 0 <= i+di < m and 0 <= j+dj < n:
        dfs(i+di, j+dj, node)
```

---

## Complexity Analysis

### Time Complexity

**Building Trie: O(W * L)**
- W = number of words
- L = average word length
- Each character inserted once

**DFS Search: O(m * n * 4^L)**
- m * n: Starting cells
- 4^L: Worst case branches (4 directions, L depth)

**Effective Time with Pruning:**
- Early stopping when prefix not in trie
- Node deletion reduces search space
- Practical: O(m * n * 3^L) or better
  - After first step, only 3 directions (can't go back)

**Overall: O(W * L + m * n * 4^L)**

### Space Complexity

**Trie Storage: O(W * L)**
- Worst case: No shared prefixes
- Best case: O(W) if all words share long prefix

**Recursion Stack: O(L)**
- Maximum depth = longest word length
- Each call stores position + trie node

**Board Visited Tracking: O(1)**
- In-place marking (no extra space)
- If using visited set: O(m * n)

**Overall: O(W * L + L) = O(W * L)**

### Comparison with Word Search I

| Aspect | Word Search I | Word Search II |
|--------|---------------|----------------|
| Words | 1 word | Multiple words |
| Time | O(m*n*4^L) | O(m*n*4^L + W*L) |
| Space | O(L) | O(W*L + L) |
| Strategy | DFS only | Trie + DFS |
| Optimization | Early termination | Trie pruning |

**Why Trie is essential for Word Search II:**
- Searching K words individually: O(K * m * n * 4^L)
- With Trie: O(W * L + m * n * 4^L)
- Speedup: When K is large and words share prefixes

---

## Edge Cases and Handling

### Edge Case 1: Empty Inputs
```python
if not board or not board[0] or not words:
    return []
```

### Edge Case 2: Single Cell Board
```python
board = [["a"]]
words = ["a"]
# Should return ["a"]
```

### Edge Case 3: Word Longer Than Board Path
```python
board = [["a", "b"]]  # 2 cells
words = ["abc"]       # Length 3
# Impossible to form, return []
```

### Edge Case 4: Duplicate Words in Input
```python
words = ["oath", "oath", "eat"]
# Constraint: All words are unique
# No need to handle this case
```

### Edge Case 5: Word as Prefix of Another
```python
words = ["eat", "eaten"]
# Trie: e -> a -> t -> (word="eat") -> e -> n (word="eaten")
# Both should be found correctly
```

**Handling:**
```python
if node.word:
    result.append(node.word)
    node.word = None  # Mark as found
# Continue DFS (don't return)
```

### Edge Case 6: All Board Cells Same Character
```python
board = [["a", "a"],
         ["a", "a"]]
words = ["aaaa"]
# Should find "aaaa"
```

### Edge Case 7: Cyclic Prevention
```python
# Visited marking prevents cycles
board = [["a", "b"],
         ["c", "d"]]
words = ["abdc"]
# Can't revisit 'b' after visiting it
```

---

## Common Pitfalls and Mistakes

### Pitfall 1: Forgetting to Restore Board State
```python
# WRONG
def dfs(i, j, node):
    board[i][j] = '#'
    dfs(...)
    # Forgot to restore!
```

**Correct:**
```python
def dfs(i, j, node):
    temp = board[i][j]
    board[i][j] = '#'
    dfs(...)
    board[i][j] = temp  # Restore
```

### Pitfall 2: Not Preventing Duplicate Results
```python
# WRONG
if node.word:
    result.append(node.word)
    # Can be added multiple times!
```

**Correct:**
```python
if node.word:
    result.append(node.word)
    node.word = None  # Mark as found
```

### Pitfall 3: Incorrect Boundary Checks
```python
# WRONG
if i < 0 or i > m or j < 0 or j > n:
    return
```

**Correct:**
```python
if i < 0 or i >= m or j < 0 or j >= n:
    return
```

### Pitfall 4: Not Checking Visited Before Accessing Node
```python
# WRONG
node = node.children[char]
if char == '#':
    return
```

**Correct:**
```python
if char == '#':
    return
node = node.children[char]
```

### Pitfall 5: Modifying Node During Iteration
```python
# WRONG
for char in node.children:
    if should_delete:
        del node.children[char]  # Modifies dict during iteration
```

**Correct:**
```python
# Delete after iteration, or collect keys first
keys_to_delete = []
for char in node.children:
    if should_delete:
        keys_to_delete.append(char)
for char in keys_to_delete:
    del node.children[char]
```

---

## Optimization Techniques

### Optimization 1: Early Termination
```python
def findWords(self, board, words):
    # ... setup ...
    total_words = len(words)
    
    def dfs(i, j, node):
        # ... dfs logic ...
        if len(result) == total_words:
            return True  # All words found
    
    # Check after each cell
    for i in range(m):
        for j in range(n):
            if dfs(i, j, root):
                break  # Early exit
```

### Optimization 2: Trie Node Reference Counting
```python
class TrieNode:
    def __init__(self):
        self.children = {}
        self.word = None
        self.refs = 0  # Number of words using this prefix

def insert(root, word):
    node = root
    for char in word:
        if char not in node.children:
            node.children[char] = TrieNode()
            root.refs += 1  # Increment for new path
        node = node.children[char]
    node.word = word

def dfs(i, j, parent):
    # ... dfs logic ...
    if not node.children:
        del parent.children[char]
        parent.refs -= 1  # Decrement reference
```

### Optimization 3: Pre-filter Impossible Words
```python
def findWords(self, board, words):
    # Count character frequencies in board
    board_chars = {}
    for row in board:
        for char in row:
            board_chars[char] = board_chars.get(char, 0) + 1
    
    # Filter words that have characters not in board
    filtered_words = []
    for word in words:
        word_chars = {}
        for char in word:
            word_chars[char] = word_chars.get(char, 0) + 1
        
        # Check if word is possible
        possible = True
        for char, count in word_chars.items():
            if board_chars.get(char, 0) < count:
                possible = False
                break
        
        if possible:
            filtered_words.append(word)
    
    # Build trie from filtered words
    # ... rest of solution ...
```

### Optimization 4: Start DFS Only from Valid Characters
```python
# Instead of checking every cell
for i in range(m):
    for j in range(n):
        dfs(i, j, root)

# Check if character is in trie first
for i in range(m):
    for j in range(n):
        if board[i][j] in root.children:
            dfs(i, j, root)
```

---

## Step-by-Step Example Walkthrough

**Input:**
```python
board = [["o","a","a","n"],
         ["e","t","a","e"],
         ["i","h","k","r"],
         ["i","f","l","v"]]
words = ["oath","pea","eat","rain"]
```

### Step 1: Build Trie
```
root
├── o -> a -> t -> h (word="oath")
├── p -> e -> a (word="pea")
├── e -> a -> t (word="eat")
└── r -> a -> i -> n (word="rain")
```

### Step 2: DFS from board[0][0] = 'o'
```
Current: (0,0) 'o', node = root.children['o']
  ├── Try (1,0) 'e' - not in node.children, skip
  ├── Try (0,1) 'a' - in node.children!
      Current: (0,1) 'a', node = node.children['a']
        ├── Try (0,0) 'o' - visited, skip
        ├── Try (1,1) 't' - in node.children!
            Current: (1,1) 't', node = node.children['t']
              ├── Try (0,1) 'a' - visited, skip
              ├── Try (2,1) 'h' - in node.children!
                  Current: (2,1) 'h'
                  Found word: "oath" ✓
```

### Step 3: Continue DFS from other cells
```
Start from (1,0) 'e':
  Try (1,1) 't' - not in root.children['e'], skip
  Try (0,0) 'o' - not in root.children['e'], skip

Start from (1,1) 't' - not in root.children, skip

Start from (1,0) 'e':
  Try (1,1) 't' - not directly reachable via 'e' path
  Try (2,0) 'i' - not in root.children['e'], skip

Eventually find "eat":
Start from (1,0) 'e':
  Try (0,0) 'o' - not in path
  Try (1,1) 't' - not in direct path
  
Start from (0,1) 'a' - but 'a' not in root.children

Start from (1,0) 'e':
  -> (1,1) 't' -> wrong path

Start from (1,0) 'e':
  -> (0,1) 'a' -> (1,1) 't'
  Found word: "eat" ✓
```

### Step 4: Check "pea" and "rain"
```
"pea": Start from 'p' - no 'p' in board
"rain": Start from 'r' at (2,3)
  -> Need 'a' adjacent to 'r'
  -> (1,3) 'e' not 'a', (2,2) 'k' not 'a'
  -> Not found
```

**Result:** `["oath", "eat"]`

---

## Alternative Implementations

### Implementation 1: Without Modifying Board
```python
class Solution:
    def findWords(self, board: List[List[str]], words: List[str]) -> List[str]:
        # Build Trie
        root = TrieNode()
        for word in words:
            node = root
            for char in word:
                if char not in node.children:
                    node.children[char] = TrieNode()
                node = node.children[char]
            node.word = word
        
        result = []
        m, n = len(board), len(board[0])
        
        def dfs(i, j, node, visited):
            if (i, j) in visited:
                return
            if i < 0 or i >= m or j < 0 or j >= n:
                return
            
            char = board[i][j]
            if char not in node.children:
                return
            
            node = node.children[char]
            
            if node.word:
                result.append(node.word)
                node.word = None
            
            visited.add((i, j))
            
            for di, dj in [(1,0), (-1,0), (0,1), (0,-1)]:
                dfs(i + di, j + dj, node, visited)
            
            visited.remove((i, j))
        
        for i in range(m):
            for j in range(n):
                if board[i][j] in root.children:
                    dfs(i, j, root, set())
        
        return result
```

**Trade-off:**
- Doesn't modify input (cleaner)
- Uses O(L) extra space for visited set
- Slightly slower (set operations)

### Implementation 2: Iterative with Stack
```python
class Solution:
    def findWords(self, board: List[List[str]], words: List[str]) -> List[str]:
        # Build Trie
        root = TrieNode()
        for word in words:
            node = root
            for char in word:
                if char not in node.children:
                    node.children[char] = TrieNode()
                node = node.children[char]
            node.word = word
        
        result = []
        m, n = len(board), len(board[0])
        
        for i in range(m):
            for j in range(n):
                if board[i][j] not in root.children:
                    continue
                
                # Stack: (row, col, trie_node, visited_set)
                stack = [(i, j, root, set())]
                
                while stack:
                    r, c, node, visited = stack.pop()
                    
                    if (r, c) in visited:
                        continue
                    if r < 0 or r >= m or c < 0 or c >= n:
                        continue
                    
                    char = board[r][c]
                    if char not in node.children:
                        continue
                    
                    node = node.children[char]
                    
                    if node.word:
                        result.append(node.word)
                        node.word = None
                    
                    new_visited = visited | {(r, c)}
                    
                    for dr, dc in [(1,0), (-1,0), (0,1), (0,-1)]:
                        stack.append((r+dr, c+dc, node, new_visited))
        
        return result
```

**Trade-off:**
- Iterative (no recursion stack limit)
- More complex code
- Higher memory usage (visited set copies)

---

## Testing Strategy

### Test Case Categories

**1. Basic Functionality**
```python
board = [["a","b"],["c","d"]]
words = ["ab","cd"]
# Should find both words
```

**2. No Words Found**
```python
board = [["a","b"],["c","d"]]
words = ["xyz"]
# Should return []
```

**3. Overlapping Words**
```python
board = [["a","a","a"],["a","a","a"]]
words = ["aaa", "aaaa"]
# Both should be found
```

**4. Words with Common Prefix**
```python
board = [["a","b","c"],["d","e","f"]]
words = ["abef", "abef", "abc"]
# Test trie prefix sharing
```

**5. Maximum Constraints**
```python
board = 12x12 grid
words = 30000 words of length 10
# Performance test
```

**6. Single Letter Words**
```python
board = [["a"]]
words = ["a"]
# Edge case
```

**7. Word Requiring Full Board Traversal**
```python
board = [["a","b","c"],
         ["d","e","f"],
         ["g","h","i"]]
words = ["abcfedghi"]  # Snake pattern
```

### Test Implementation
```python
def test_word_search_ii():
    solution = Solution()
    
    # Test 1: Example from problem
    board1 = [["o","a","a","n"],
              ["e","t","a","e"],
              ["i","h","k","r"],
              ["i","f","l","v"]]
    words1 = ["oath","pea","eat","rain"]
    result1 = solution.findWords(board1, words1)
    assert sorted(result1) == ["eat", "oath"]
    
    # Test 2: No matches
    board2 = [["a","b"],["c","d"]]
    words2 = ["abcb"]
    result2 = solution.findWords(board2, words2)
    assert result2 == []
    
    # Test 3: Single cell
    board3 = [["a"]]
    words3 = ["a"]
    result3 = solution.findWords(board3, words3)
    assert result3 == ["a"]
    
    print("All tests passed!")
```

---

## Variations and Related Problems

### Variation 1: Return Paths Not Just Words
```python
# Return list of (word, path) where path is list of coordinates
def findWordsWithPaths(board, words):
    # During DFS, track path
    def dfs(i, j, node, path):
        # ... 
        if node.word:
            result.append((node.word, path[:]))
        
        path.append((i, j))
        # ... explore ...
        path.pop()
```

### Variation 2: Count Occurrences of Each Word
```python
# Some words might appear multiple times
def countWords(board, words):
    # Don't set node.word = None
    # Count all occurrences
```

### Variation 3: Maximum Length Word
```python
# Find longest word that can be formed
def findLongestWord(board, words):
    result = []
    # Track word length during DFS
    # Return max length word
```

### Related Problems

1. **LeetCode 79: Word Search** (Medium)
   - Single word search
   - No trie needed
   - Pure backtracking

2. **LeetCode 208: Implement Trie** (Medium)
   - Trie data structure basics
   - Insert, search, startsWith

3. **LeetCode 211: Design Add and Search Words Data Structure** (Medium)
   - Trie with wildcard search
   - Similar to this problem's trie

4. **LeetCode 472: Concatenated Words** (Hard)
   - Trie + DP
   - Word breaking with trie

---

## Key Takeaways

### Must Remember
1. **Trie eliminates redundant searches** for multiple words
2. **Store complete word at trie leaf** for easy retrieval
3. **Mark visited cells in-place** using special character
4. **Set word to None after finding** to prevent duplicates
5. **Delete trie nodes** after use for optimization

### Pattern Recognition
- Multiple string matching → Consider Trie
- Grid traversal with constraints → Backtracking
- Prefix-based search → Trie is ideal

### Optimization Priority
1. Build trie (mandatory)
2. Prevent duplicates (node.word = None)
3. Prune trie nodes (delete empty branches)
4. Early termination (if all words found)
5. Pre-filter impossible words (optional)

### Common Mistakes to Avoid
- Forgetting to restore board state
- Not preventing duplicate results
- Incorrect boundary checks
- Accessing children before checking visited
- Modifying trie during iteration

---

## Complexity Comparison Table

| Approach | Time | Space | Notes |
|----------|------|-------|-------|
| Naive (search each word) | O(W*m*n*4^L) | O(L) | Very slow for many words |
| Trie + DFS (basic) | O(W*L + m*n*4^L) | O(W*L) | Standard solution |
| Trie + DFS + pruning | O(W*L + m*n*3^L) | O(W*L) | Practical best |
| Pre-filtering + Trie | O(W*L + m*n*3^L) | O(W*L) | Slight improvement |

Where:
- W = number of words
- L = average word length
- m, n = board dimensions

---

## Interview Tips

### What Interviewers Look For
1. **Recognize pattern**: Trie for multiple string matching
2. **Explain trade-offs**: Why not search each word individually?
3. **Handle edge cases**: Empty board, no matches, single cell
4. **Optimize**: Node deletion, duplicate prevention
5. **Clean code**: Clear variable names, modular structure

### Discussion Points
- "Why is trie better than hash set for this problem?"
  - Prefix-based pruning, shared prefixes, early stopping
  
- "What's the space-time trade-off?"
  - Trie uses O(W*L) space but saves O(W) factor in time
  
- "How would you handle very large boards?"
  - Parallel DFS from different cells
  - Divide board into regions
  - Trie node caching

### Code Quality Tips
```python
# Good: Clear structure
def findWords(self, board, words):
    root = self.buildTrie(words)
    result = self.searchBoard(board, root)
    return result

# Better: Handle edge cases first
def findWords(self, board, words):
    if not board or not board[0] or not words:
        return []
    
    root = self.buildTrie(words)
    return self.searchBoard(board, root)
```

---

## Summary

**Problem:** Find all words from a list that exist in a 2D board.

**Key Insight:** Use Trie to search multiple words simultaneously, eliminating redundant DFS paths.

**Algorithm:**
1. Build trie from all words
2. DFS from each board cell with trie guidance
3. Mark cells visited, restore after exploration
4. Collect words when found, mark as found to prevent duplicates
5. Prune trie nodes after use for optimization

**Complexity:**
- Time: O(W*L + m*n*4^L) with optimizations → O(W*L + m*n*3^L)
- Space: O(W*L + L)

**Critical Points:**
- Trie enables prefix sharing and early pruning
- Backtracking ensures valid paths (no revisiting)
- Node deletion and duplicate prevention are key optimizations
- In-place board marking avoids extra space

This problem beautifully combines trie data structure with backtracking, demonstrating how appropriate data structures can dramatically improve algorithmic efficiency.
