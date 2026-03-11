# Subtree of Another Tree

**LeetCode Problem #572 (Easy)**

## Problem Statement

Given the roots of two binary trees `root` and `subRoot`, return `true` if there is a subtree of `root` with the same structure and node values of `subRoot` and `false` otherwise.

A subtree of a binary tree `tree` is a tree that consists of a node in `tree` and all of this node's descendants. The tree `tree` could also be considered as a subtree of itself.

### Examples

**Example 1:**
```
Input: root = [3,4,5,1,2], subRoot = [4,1,2]

Tree:           SubRoot:
      3             4
     / \           / \
    4   5         1   2
   / \
  1   2

Output: true
```

**Example 2:**
```
Input: root = [3,4,5,1,2,null,null,null,null,0], subRoot = [4,1,2]

Tree:           SubRoot:
      3             4
     / \           / \
    4   5         1   2
   / \
  1   2
 /
0

Output: false
```

**Example 3:**
```
Input: root = [1,1], subRoot = [1]

Tree:     SubRoot:
    1         1
   /
  1

Output: true
```

### Constraints
- The number of nodes in the `root` tree is in the range `[1, 2000]`
- The number of nodes in the `subRoot` tree is in the range `[1, 1000]`
- `-10^4 <= root.val <= 10^4`
- `-10^4 <= subRoot.val <= 10^4`

---

## Pattern Recognition

This is a **Tree DFS + Tree Comparison** problem.

**Key Characteristics:**
1. Need to check if one tree is subtree of another
2. Subtree must match completely (structure + values)
3. For each node in main tree, check if subtree matches
4. Combines tree traversal with tree comparison

**Why This Pattern?**
- "Is subtree" → Need to find matching root in main tree
- "Same structure and values" → Use tree comparison (like Same Tree problem)
- Must check at every node → DFS traversal
- Two-phase: find potential roots, then verify match

**Pattern Recognition:**
- "Subtree check" → Tree traversal + comparison
- "For each node" → DFS through main tree
- "Exact match" → Use same tree comparison logic

---

## Solution Approaches

### Approach 1: DFS + Tree Comparison (Most Intuitive)

**Intuition:**
For each node in main tree, check if subtree starting there matches subRoot.

**Algorithm:**
1. Traverse main tree using DFS
2. At each node, check if subtree matches subRoot
3. Use helper function to compare two trees (like Same Tree problem)
4. Return true if any node matches

```python
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def isSubtree(self, root: Optional[TreeNode], subRoot: Optional[TreeNode]) -> bool:
        # Helper function to check if two trees are identical
        def isSameTree(p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
            # Both null - same
            if not p and not q:
                return True
            
            # One null or values differ - not same
            if not p or not q or p.val != q.val:
                return False
            
            # Check both subtrees
            return isSameTree(p.left, q.left) and isSameTree(p.right, q.right)
        
        # Main function to check subtree
        if not root:
            return False
        
        # Check if current node matches
        if isSameTree(root, subRoot):
            return True
        
        # Check left and right subtrees
        return self.isSubtree(root.left, subRoot) or self.isSubtree(root.right, subRoot)
```

**Complexity Analysis:**
- **Time Complexity:** O(m × n) where m = nodes in root, n = nodes in subRoot
  - For each of m nodes, potentially compare n nodes
  - Best case: O(m) if subRoot not found
  - Worst case: O(m × n) if many partial matches
- **Space Complexity:** O(h1 + h2) for recursion stack
  - h1 = height of root tree
  - h2 = height of subRoot tree

---

### Approach 2: Optimized with Early Termination

**Intuition:**
Same as Approach 1 but with explicit early termination logic.

```python
class Solution:
    def isSubtree(self, root: Optional[TreeNode], subRoot: Optional[TreeNode]) -> bool:
        def isSameTree(p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
            if not p and not q:
                return True
            if not p or not q or p.val != q.val:
                return False
            return isSameTree(p.left, q.left) and isSameTree(p.right, q.right)
        
        def dfs(node: Optional[TreeNode]) -> bool:
            if not node:
                return False
            
            # Found match
            if isSameTree(node, subRoot):
                return True
            
            # Search in left subtree
            if dfs(node.left):
                return True
            
            # Search in right subtree
            if dfs(node.right):
                return True
            
            return False
        
        return dfs(root)
```

**Complexity Analysis:**
- **Time Complexity:** O(m × n)
- **Space Complexity:** O(h1 + h2)

---

### Approach 3: Tree Serialization (String Matching)

**Intuition:**
Serialize both trees to strings, then check if subRoot string is substring of root string.

**Algorithm:**
1. Serialize both trees with special markers for null
2. Check if subRoot serialization is substring of root serialization
3. Use preorder traversal with markers

```python
class Solution:
    def isSubtree(self, root: Optional[TreeNode], subRoot: Optional[TreeNode]) -> bool:
        def serialize(node: Optional[TreeNode]) -> str:
            if not node:
                return "#"  # Null marker
            
            # Preorder: root, left, right
            # Use delimiters to avoid false matches like "12" matching "2"
            return f"^{node.val}$ {serialize(node.left)} {serialize(node.right)}"
        
        root_str = serialize(root)
        sub_str = serialize(subRoot)
        
        return sub_str in root_str
```

**Complexity Analysis:**
- **Time Complexity:** O(m + n) for serialization + O(m) for string matching
  - Total: O(m + n) on average
  - Worst case: O(m × n) for naive string matching
- **Space Complexity:** O(m + n) for storing serialized strings

**Note:** Use special markers (^, $) to avoid false positives.

---

### Approach 4: KMP String Matching (Optimal)

**Intuition:**
Use KMP algorithm for string matching to achieve true O(m + n) time.

```python
class Solution:
    def isSubtree(self, root: Optional[TreeNode], subRoot: Optional[TreeNode]) -> bool:
        def serialize(node: Optional[TreeNode]) -> str:
            if not node:
                return "#"
            return f"^{node.val}${serialize(node.left)}{serialize(node.right)}"
        
        def kmp_search(text: str, pattern: str) -> bool:
            # Build failure function
            def build_lps(pattern: str) -> list:
                lps = [0] * len(pattern)
                length = 0
                i = 1
                
                while i < len(pattern):
                    if pattern[i] == pattern[length]:
                        length += 1
                        lps[i] = length
                        i += 1
                    else:
                        if length != 0:
                            length = lps[length - 1]
                        else:
                            lps[i] = 0
                            i += 1
                
                return lps
            
            lps = build_lps(pattern)
            i = j = 0
            
            while i < len(text):
                if text[i] == pattern[j]:
                    i += 1
                    j += 1
                    
                    if j == len(pattern):
                        return True
                
                elif j > 0:
                    j = lps[j - 1]
                else:
                    i += 1
            
            return False
        
        root_str = serialize(root)
        sub_str = serialize(subRoot)
        
        return kmp_search(root_str, sub_str)
```

**Complexity Analysis:**
- **Time Complexity:** O(m + n)
  - Serialization: O(m + n)
  - KMP search: O(m + n)
- **Space Complexity:** O(m + n)

---

### Approach 5: Hash-Based Comparison

**Intuition:**
Use hash values to speed up tree comparison.

```python
class Solution:
    def isSubtree(self, root: Optional[TreeNode], subRoot: Optional[TreeNode]) -> bool:
        def hash_tree(node: Optional[TreeNode]) -> str:
            if not node:
                return "#"
            return f"({hash_tree(node.left)}){node.val}({hash_tree(node.right)})"
        
        target_hash = hash_tree(subRoot)
        
        def dfs(node: Optional[TreeNode]) -> bool:
            if not node:
                return False
            
            if hash_tree(node) == target_hash:
                return True
            
            return dfs(node.left) or dfs(node.right)
        
        return dfs(root)
```

**Complexity Analysis:**
- **Time Complexity:** O(m × n) worst case (hash collisions)
- **Space Complexity:** O(h1 + h2)

**Note:** Hashing can speed up in practice but doesn't improve worst-case complexity.

---

## Detailed Walkthrough

Let's trace through **Approach 1** with Example 1:

```
Tree:           SubRoot:
      3             4
     / \           / \
    4   5         1   2
   / \
  1   2
```

**Execution Flow:**

```
isSubtree(3, subRoot):
  └─ isSameTree(3, 4)?
       └─ 3.val != 4.val → False
  
  └─ Not match at root, check left
  └─ isSubtree(4, subRoot):
       └─ isSameTree(4, 4)?
            └─ 4.val == 4.val ✓
            └─ Check left: isSameTree(1, 1)
                 └─ 1.val == 1.val ✓
                 └─ Both have no children ✓
                 └─ Return True
            └─ Check right: isSameTree(2, 2)
                 └─ 2.val == 2.val ✓
                 └─ Both have no children ✓
                 └─ Return True
            └─ Return True ✓
       
       └─ Match found! Return True

Return True
```

**Key Points:**
1. First check at root (3) fails
2. Recurse to left child (4)
3. Check at node 4 succeeds
4. isSameTree verifies complete match
5. Early return - don't need to check node 5

**Example 2 (False case):**
```
Tree:           SubRoot:
      3             4
     / \           / \
    4   5         1   2
   / \
  1   2
 /
0

isSameTree(4, 4) at node 4:
  └─ Values match ✓
  └─ Check left: isSameTree(1, 1)
       └─ Values match ✓
       └─ Check left: isSameTree(0, None)
            └─ One is None, other isn't ✗
            └─ Return False
```

---

## Edge Cases

### 1. SubRoot is Single Node
```python
Input: root = [1,1], subRoot = [1]
       1         1
      /
     1
Output: True (left child matches)
```

### 2. Identical Trees
```python
Input: root = [1,2,3], subRoot = [1,2,3]
         1              1
        / \            / \
       2   3          2   3
Output: True (entire tree is subtree)
```

### 3. SubRoot Not in Tree
```python
Input: root = [1,2,3], subRoot = [4,5]
         1              4
        / \            /
       2   3          5
Output: False
```

### 4. Partial Match (Structure Differs)
```python
Input: root = [3,4,5,1,2,null,null,0], subRoot = [4,1,2]
         3              4
        / \            / \
       4   5          1   2
      / \
     1   2
    /
   0
Output: False (extra child 0)
```

### 5. Same Values, Different Structure
```python
Input: root = [1,2,3], subRoot = [1,3]
         1              1
        / \              \
       2   3              3
Output: False
```

### 6. SubRoot at Leaf
```python
Input: root = [1,null,2], subRoot = [2]
         1         2
          \
           2
Output: True
```

### 7. Multiple Candidates
```python
Input: root = [4,1,2,null,null,4,1,2], subRoot = [4,1,2]
         4
        / \
       1   2
          / \
         4   1
            /
           2
Output: True (second occurrence matches)
```

---

## Common Mistakes

### 1. **Comparing Only Root Values**
```python
# WRONG - Doesn't check entire subtree
def isSubtree(self, root, subRoot):
    if not root:
        return False
    if root.val == subRoot.val:
        return True
    return self.isSubtree(root.left, subRoot) or self.isSubtree(root.right, subRoot)
```
**Fix:** Use complete tree comparison

### 2. **Not Handling Null Correctly**
```python
# WRONG - Doesn't handle when root becomes None
def isSubtree(self, root, subRoot):
    if isSameTree(root, subRoot):  # What if root is None?
        return True
    # ...
```
**Fix:** Check `if not root: return False` first

### 3. **Incorrect Tree Comparison**
```python
# WRONG - Allows partial matches
def isSameTree(self, p, q):
    if not p or not q:
        return True  # Should check both!
    # ...
```

### 4. **String Serialization Without Markers**
```python
# WRONG - Can cause false positives
def serialize(node):
    if not node:
        return ""
    return str(node.val) + serialize(node.left) + serialize(node.right)
# Tree [12] and [1,2] would both serialize to "12"
```
**Fix:** Use delimiters and null markers

### 5. **Not Using 'or' Correctly**
```python
# WRONG - Doesn't return immediately on match
def isSubtree(self, root, subRoot):
    if not root:
        return False
    match = isSameTree(root, subRoot)
    self.isSubtree(root.left, subRoot)  # Result ignored!
    self.isSubtree(root.right, subRoot)
    return match
```
**Fix:** Use `or` to combine results and enable early return

### 6. **Confusing Subtree with Substructure**
```python
# WRONG - Allows partial matches
# A subtree must be a complete tree, not just matching structure
```

---

## Testing

### Test Cases

```python
def test_is_subtree():
    solution = Solution()
    
    # Test 1: Example 1 - True
    root1 = TreeNode(3)
    root1.left = TreeNode(4)
    root1.right = TreeNode(5)
    root1.left.left = TreeNode(1)
    root1.left.right = TreeNode(2)
    
    sub1 = TreeNode(4)
    sub1.left = TreeNode(1)
    sub1.right = TreeNode(2)
    
    assert solution.isSubtree(root1, sub1) == True
    
    # Test 2: Example 2 - False (extra child)
    root2 = TreeNode(3)
    root2.left = TreeNode(4)
    root2.right = TreeNode(5)
    root2.left.left = TreeNode(1)
    root2.left.right = TreeNode(2)
    root2.left.left.left = TreeNode(0)
    
    sub2 = TreeNode(4)
    sub2.left = TreeNode(1)
    sub2.right = TreeNode(2)
    
    assert solution.isSubtree(root2, sub2) == False
    
    # Test 3: Single node match
    root3 = TreeNode(1)
    root3.left = TreeNode(1)
    sub3 = TreeNode(1)
    assert solution.isSubtree(root3, sub3) == True
    
    # Test 4: Identical trees
    root4 = TreeNode(1)
    root4.left = TreeNode(2)
    root4.right = TreeNode(3)
    
    sub4 = TreeNode(1)
    sub4.left = TreeNode(2)
    sub4.right = TreeNode(3)
    
    assert solution.isSubtree(root4, sub4) == True
    
    # Test 5: Not a subtree
    root5 = TreeNode(1)
    root5.left = TreeNode(2)
    sub5 = TreeNode(3)
    assert solution.isSubtree(root5, sub5) == False
    
    # Test 6: SubRoot at leaf
    root6 = TreeNode(1)
    root6.right = TreeNode(2)
    sub6 = TreeNode(2)
    assert solution.isSubtree(root6, sub6) == True
    
    print("All test cases passed!")

test_is_subtree()
```

---

## Optimization Notes

### Time Optimization

**Approach Comparison:**
- **DFS + Comparison:** O(m × n) - Simple but potentially slow
- **Serialization:** O(m + n) - Faster but requires string operations
- **KMP:** O(m + n) - Optimal time complexity
- **Hash-based:** O(m × n) worst case but faster in practice

**For interviews:** Approach 1 is usually sufficient and clearest.

### Space Optimization

**Space Usage:**
- **DFS:** O(h1 + h2) - Just recursion stack
- **Serialization:** O(m + n) - Store complete strings
- **Hash:** O(h1 + h2) - Recursion + hash strings

**Best for space:** DFS approach (Approach 1)

---

## Related Problems

### Similar Problems

1. **[LeetCode 100] Same Tree (Easy)**
   - Check if two trees are identical
   - Used as subroutine in this problem

2. **[LeetCode 297] Serialize and Deserialize Binary Tree (Hard)**
   - Tree serialization techniques
   - Relevant for Approach 3

3. **[LeetCode 1367] Linked List in Binary Tree (Medium)**
   - Check if linked list path exists in tree
   - Similar pattern of checking sub-structure

4. **[LeetCode 652] Find Duplicate Subtrees (Medium)**
   - Find all duplicate subtrees
   - Uses tree hashing/serialization

### Pattern Variations

- **Find all occurrences of subtree:** Count instead of boolean
- **Find largest common subtree:** Between two trees
- **Check if tree contains pattern:** More general matching
- **Isomorphic subtree:** Allow children swapping

---

## Interview Tips

### What Interviewers Look For

1. **Reusing existing patterns:**
   - Recognizing this uses "Same Tree" logic
   - Combining multiple patterns

2. **Multiple approaches:**
   - Knowing both DFS and serialization methods
   - Understanding trade-offs

3. **Edge cases:**
   - Single nodes
   - Identical trees
   - Partial matches

4. **Code organization:**
   - Clean helper functions
   - Good separation of concerns

### Discussion Points

**Interviewer:** "What's the time complexity?"
**You:** "O(m × n) where m and n are the sizes of the two trees. For each of m nodes, we might compare n nodes. With serialization and KMP, we can achieve O(m + n)."

**Interviewer:** "Can you optimize this?"
**You:** "Yes, using tree serialization and string matching (like KMP) we can achieve O(m + n). However, the simple DFS approach is clearer and usually preferred in interviews."

**Interviewer:** "What if we need to find all occurrences?"
**You:** "Continue searching even after finding a match, collect all matching nodes instead of returning early."

**Interviewer:** "How would you handle very large trees?"
**You:** "For very large trees, serialization might use too much memory. I'd stick with DFS approach and possibly add memoization if we're checking the same subtrees multiple times."

### Follow-up Questions

1. **"Count occurrences of subtree"**
   - Don't return early, count all matches

2. **"Find all subtrees of given pattern"**
   - Collect matching nodes in list

3. **"Check if tree contains sub-pattern (not necessarily connected)"**
   - Different problem - need path finding

4. **"What if nodes can have duplicate values?"**
   - Same solution works - we check structure too

---

## Key Takeaways

1. **Combine Two Patterns:** DFS traversal + tree comparison

2. **Helper Function is Key:**
   ```python
   def isSameTree(p, q):
       if not p and not q: return True
       if not p or not q or p.val != q.val: return False
       return isSameTree(p.left, q.left) and isSameTree(p.right, q.right)
   ```

3. **Check Every Node:** Try matching subtree at each node in main tree

4. **Early Return:** Use `or` to return immediately when match found

5. **Alternative: Serialization:** Can achieve better time complexity but more complex

---

## Additional Resources

### Visual Learning
- **Tree Traversal Visualizer:** visualgo.net/en/bst
- **Pattern Matching:** algorithm-visualizer.org

### Related Concepts
- Tree traversals (DFS)
- Tree comparison
- String matching (KMP)
- Tree serialization

### Practice Progression
1. First master: LeetCode 100 (Same Tree)
2. Then try: This problem (572)
3. Advanced: LeetCode 1367 (Linked List in Tree), 652 (Duplicate Subtrees)

---

## Summary

**Problem:** Check if one tree is subtree of another

**Best Solution:** DFS + Tree Comparison
```python
def isSubtree(self, root, subRoot):
    def isSameTree(p, q):
        if not p and not q:
            return True
        if not p or not q or p.val != q.val:
            return False
        return isSameTree(p.left, q.left) and isSameTree(p.right, q.right)
    
    if not root:
        return False
    if isSameTree(root, subRoot):
        return True
    return self.isSubtree(root.left, subRoot) or self.isSubtree(root.right, subRoot)
```

**Complexity:** O(m × n) time, O(h1 + h2) space

**Key Insight:** Combine tree traversal with tree comparison; check at every node

**When to Use:** Tree pattern matching; combines multiple tree patterns!
