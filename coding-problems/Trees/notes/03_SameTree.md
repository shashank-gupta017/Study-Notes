# Same Tree

**LeetCode Problem #100 (Easy)**

## Problem Statement

Given the roots of two binary trees `p` and `q`, write a function to check if they are the same or not.

Two binary trees are considered the same if they are structurally identical, and the nodes have the same value.

### Examples

**Example 1:**
```
Input: p = [1,2,3], q = [1,2,3]

Tree p:       Tree q:
    1            1
   / \          / \
  2   3        2   3

Output: true
```

**Example 2:**
```
Input: p = [1,2], q = [1,null,2]

Tree p:       Tree q:
    1            1
   /              \
  2                2

Output: false
```

**Example 3:**
```
Input: p = [1,2,1], q = [1,1,2]

Tree p:       Tree q:
    1            1
   / \          / \
  2   1        1   2

Output: false
```

### Constraints
- The number of nodes in both trees is in the range `[0, 100]`
- `-10^4 <= Node.val <= 10^4`

---

## Pattern Recognition

This is a **Tree DFS with Recursive Comparison** problem.

**Key Characteristics:**
1. Need to compare two trees simultaneously
2. Both structure and values must match
3. Natural recursive structure (compare roots, then subtrees)
4. Early termination possible when mismatch found

**Why This Pattern?**
- Two trees → need parallel traversal
- Structural comparison → check nodes exist in same positions
- Value comparison → check node values match
- DFS naturally compares corresponding nodes

**Pattern Recognition:**
- "Compare two trees" → Parallel DFS/BFS
- "Same structure and values" → Check both conditions
- "Return boolean" → Early termination optimization

---

## Solution Approaches

### Approach 1: Recursive DFS (Most Intuitive)

**Intuition:**
Two trees are the same if:
1. Both roots are null, OR
2. Both roots exist AND:
   - Root values are equal
   - Left subtrees are the same
   - Right subtrees are the same

**Algorithm:**
1. If both nodes are null → true (base case)
2. If only one is null → false
3. If values differ → false
4. Recursively check left subtrees
5. Recursively check right subtrees

```python
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def isSameTree(self, p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        # Case 1: Both nodes are None
        if not p and not q:
            return True
        
        # Case 2: One node is None, other is not
        if not p or not q:
            return False
        
        # Case 3: Both nodes exist but values differ
        if p.val != q.val:
            return False
        
        # Case 4: Values match, check subtrees
        return (self.isSameTree(p.left, q.left) and 
                self.isSameTree(p.right, q.right))
```

**Complexity Analysis:**
- **Time Complexity:** O(min(m, n)) where m, n are number of nodes
  - Best case: O(1) if roots differ
  - Average: O(min(m, n)) - stop when mismatch found
  - Worst case: O(n) when trees are identical
- **Space Complexity:** O(min(h1, h2)) for recursion stack
  - Best case (balanced): O(log n)
  - Worst case (skewed): O(n)

---

### Approach 2: Recursive DFS (Compact Version)

**Intuition:**
Combine all conditions into a single expression.

```python
class Solution:
    def isSameTree(self, p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        # Both null - same
        if not p and not q:
            return True
        
        # One null or values differ - not same
        if not p or not q or p.val != q.val:
            return False
        
        # Check both subtrees
        return self.isSameTree(p.left, q.left) and self.isSameTree(p.right, q.right)
```

**Complexity Analysis:**
- **Time Complexity:** O(min(m, n))
- **Space Complexity:** O(min(h1, h2))

---

### Approach 3: Recursive DFS (Ultra-Compact)

**Intuition:**
One-liner that checks all conditions.

```python
class Solution:
    def isSameTree(self, p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        if not p and not q:
            return True
        if not p or not q:
            return False
        return (p.val == q.val and 
                self.isSameTree(p.left, q.left) and 
                self.isSameTree(p.right, q.right))
```

**Alternative One-Liner:**
```python
class Solution:
    def isSameTree(self, p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        return (not p and not q) or \
               (p and q and p.val == q.val and 
                self.isSameTree(p.left, q.left) and 
                self.isSameTree(p.right, q.right))
```

**Complexity Analysis:**
- **Time Complexity:** O(min(m, n))
- **Space Complexity:** O(min(h1, h2))

---

### Approach 4: Iterative DFS (Using Stack)

**Intuition:**
Use explicit stack to avoid recursion. Push pairs of corresponding nodes.

**Algorithm:**
1. Initialize stack with (p, q)
2. While stack not empty:
   - Pop pair of nodes
   - Check if both null (continue)
   - Check if one null or values differ (return false)
   - Push children pairs onto stack
3. Return true if no mismatches found

```python
class Solution:
    def isSameTree(self, p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        # Stack stores pairs of nodes to compare
        stack = [(p, q)]
        
        while stack:
            node1, node2 = stack.pop()
            
            # Both null - continue
            if not node1 and not node2:
                continue
            
            # One null or values differ - not same
            if not node1 or not node2 or node1.val != node2.val:
                return False
            
            # Push children pairs
            stack.append((node1.left, node2.left))
            stack.append((node1.right, node2.right))
        
        return True
```

**Complexity Analysis:**
- **Time Complexity:** O(min(m, n))
- **Space Complexity:** O(min(h1, h2)) for stack

---

### Approach 5: BFS (Level Order Traversal)

**Intuition:**
Compare trees level by level using queues.

**Algorithm:**
1. Initialize queue with (p, q)
2. While queue not empty:
   - Dequeue pair
   - Check conditions (both null, one null, values)
   - Enqueue children pairs
3. Return true if all levels match

```python
from collections import deque

class Solution:
    def isSameTree(self, p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        # Queue stores pairs of nodes to compare
        queue = deque([(p, q)])
        
        while queue:
            node1, node2 = queue.popleft()
            
            # Both null - continue
            if not node1 and not node2:
                continue
            
            # One null or values differ - not same
            if not node1 or not node2 or node1.val != node2.val:
                return False
            
            # Enqueue children pairs
            queue.append((node1.left, node2.left))
            queue.append((node1.right, node2.right))
        
        return True
```

**Complexity Analysis:**
- **Time Complexity:** O(min(m, n))
- **Space Complexity:** O(min(w1, w2)) where w is max width
  - Can be O(n) for complete trees

---

### Approach 6: Serialize and Compare

**Intuition:**
Serialize both trees to strings and compare strings.

```python
class Solution:
    def isSameTree(self, p: Optional[TreeNode], q: Optional[TreeNode]) -> bool:
        def serialize(node):
            if not node:
                return "null"
            return f"{node.val},{serialize(node.left)},{serialize(node.right)}"
        
        return serialize(p) == serialize(q)
```

**Complexity Analysis:**
- **Time Complexity:** O(m + n) - serialize both trees completely
- **Space Complexity:** O(m + n) - store serialized strings

**Note:** Less efficient than direct comparison (no early termination).

---

## Detailed Walkthrough

Let's trace through **Approach 1** with Example 2:

```
Tree p:       Tree q:
    1            1
   /              \
  2                2
```

**Execution Flow:**

```
isSameTree(p=1, q=1):
  └─ p and q both exist ✓
  └─ p.val (1) == q.val (1) ✓
  
  └─ Check left: isSameTree(p.left=2, q.left=None)
       └─ p.left exists (2) but q.left is None
       └─ Return False ✗

  └─ Short circuit! Don't check right subtree
  └─ Return False
```

**Step-by-Step:**
1. Compare roots: both are 1 ✓
2. Compare left children:
   - p has left child (2)
   - q has no left child (None)
   - Mismatch! Return False
3. No need to check right children (short circuit)
4. Final result: False

**Example with matching trees:**
```
Tree p:       Tree q:
    1            1
   / \          / \
  2   3        2   3

isSameTree(p=1, q=1):
  └─ Values match (1 == 1) ✓
  └─ Check left: isSameTree(2, 2)
       └─ Values match ✓
       └─ Both have no children
       └─ Return True
  └─ Check right: isSameTree(3, 3)
       └─ Values match ✓
       └─ Both have no children
       └─ Return True
  └─ Return True (both subtrees match)
```

---

## Edge Cases

### 1. Both Trees Empty
```python
Input: p = None, q = None
Output: True
```
- Base case: both null is considered same

### 2. One Tree Empty
```python
Input: p = [1], q = None
       1
Output: False
```
- Different structure

### 3. Single Node - Same Value
```python
Input: p = [1], q = [1]
       1         1
Output: True
```

### 4. Single Node - Different Value
```python
Input: p = [1], q = [2]
       1         2
Output: False
```

### 5. Same Structure, Different Values
```python
Input: p = [1,2,3], q = [1,2,4]
         1              1
        / \            / \
       2   3          2   4
Output: False
```
- Structure same but leaf value differs

### 6. Different Structure, Same Values
```python
Input: p = [1,2], q = [1,null,2]
         1              1
        /                \
       2                  2
Output: False
```
- Values same but structure differs

### 7. Mirror Trees
```python
Input: p = [1,2,3], q = [1,3,2]
         1              1
        / \            / \
       2   3          3   2
Output: False
```
- Mirror images are not the same

### 8. Large Identical Trees
```python
Input: Both trees are complete binary trees with 100 nodes
Output: True
```
- Verify algorithm works at scale

---

## Common Mistakes

### 1. **Only Checking Root Values**
```python
# WRONG - Only compares root, not structure
def isSameTree(self, p, q):
    if not p and not q:
        return True
    if not p or not q:
        return False
    return p.val == q.val  # Missing subtree comparison!
```

### 2. **Not Handling Null Cases Properly**
```python
# WRONG - Will crash on None.val
def isSameTree(self, p, q):
    if p.val != q.val:  # What if p or q is None?
        return False
    return self.isSameTree(p.left, q.left) and self.isSameTree(p.right, q.right)
```
**Fix:** Check for None before accessing attributes

### 3. **Incorrect Base Case Order**
```python
# WRONG - Will crash
def isSameTree(self, p, q):
    if p.val != q.val:  # Should check None first!
        return False
    if not p and not q:
        return True
    # ...
```

### 4. **Using 'or' Instead of 'and'**
```python
# WRONG - Both subtrees must match
def isSameTree(self, p, q):
    # ... null checks ...
    return self.isSameTree(p.left, q.left) or self.isSameTree(p.right, q.right)
```
**Fix:** Use 'and' - both subtrees must be the same

### 5. **Not Short-Circuiting**
```python
# INEFFICIENT - Continues even after finding mismatch
def isSameTree(self, p, q):
    if not p and not q:
        return True
    if not p or not q:
        return False
    
    left_same = self.isSameTree(p.left, q.left)
    right_same = self.isSameTree(p.right, q.right)
    
    return p.val == q.val and left_same and right_same
    # Better to check p.val first for early return
```

### 6. **Comparing Node Objects Instead of Values**
```python
# WRONG - Compares object references
def isSameTree(self, p, q):
    if p != q:  # This compares object identity!
        return False
```
**Fix:** Compare `p.val != q.val`

---

## Testing

### Test Cases

```python
def test_same_tree():
    solution = Solution()
    
    # Test 1: Both empty
    assert solution.isSameTree(None, None) == True
    
    # Test 2: One empty
    p2 = TreeNode(1)
    assert solution.isSameTree(p2, None) == False
    assert solution.isSameTree(None, p2) == False
    
    # Test 3: Single node - same
    p3 = TreeNode(1)
    q3 = TreeNode(1)
    assert solution.isSameTree(p3, q3) == True
    
    # Test 4: Single node - different
    p4 = TreeNode(1)
    q4 = TreeNode(2)
    assert solution.isSameTree(p4, q4) == False
    
    # Test 5: Example 1 - same trees
    p5 = TreeNode(1, TreeNode(2), TreeNode(3))
    q5 = TreeNode(1, TreeNode(2), TreeNode(3))
    assert solution.isSameTree(p5, q5) == True
    
    # Test 6: Example 2 - different structure
    p6 = TreeNode(1, TreeNode(2), None)
    q6 = TreeNode(1, None, TreeNode(2))
    assert solution.isSameTree(p6, q6) == False
    
    # Test 7: Example 3 - different values
    p7 = TreeNode(1, TreeNode(2), TreeNode(1))
    q7 = TreeNode(1, TreeNode(1), TreeNode(2))
    assert solution.isSameTree(p7, q7) == False
    
    # Test 8: Mirror trees
    p8 = TreeNode(1, TreeNode(2), TreeNode(3))
    q8 = TreeNode(1, TreeNode(3), TreeNode(2))
    assert solution.isSameTree(p8, q8) == False
    
    # Test 9: Deeper tree - same
    p9 = TreeNode(1)
    p9.left = TreeNode(2)
    p9.right = TreeNode(3)
    p9.left.left = TreeNode(4)
    p9.left.right = TreeNode(5)
    
    q9 = TreeNode(1)
    q9.left = TreeNode(2)
    q9.right = TreeNode(3)
    q9.left.left = TreeNode(4)
    q9.left.right = TreeNode(5)
    assert solution.isSameTree(p9, q9) == True
    
    # Test 10: Deeper tree - different at leaf
    p10 = TreeNode(1)
    p10.left = TreeNode(2)
    p10.right = TreeNode(3)
    p10.left.left = TreeNode(4)
    
    q10 = TreeNode(1)
    q10.left = TreeNode(2)
    q10.right = TreeNode(3)
    q10.left.left = TreeNode(5)  # Different value
    assert solution.isSameTree(p10, q10) == False
    
    print("All test cases passed!")

test_same_tree()
```

---

## Optimization Notes

### Early Termination

**Key Insight:** Can return False as soon as mismatch is found.

```python
# Good: Short-circuit evaluation
return (p.val == q.val and 
        self.isSameTree(p.left, q.left) and 
        self.isSameTree(p.right, q.right))

# If p.val != q.val, immediately returns False
# If left subtrees differ, doesn't check right
```

### Space Optimization

**Comparison of approaches:**
- Recursive: O(h) implicit stack
- Iterative: O(h) explicit stack
- BFS: O(w) queue (worse for wide trees)
- Serialization: O(n) strings (worst)

**Best choice:** Recursive for clean code, iterative if stack depth is concern

### Avoid Redundant Comparisons

```python
# Less efficient - checks value after recursion
def isSameTree(self, p, q):
    if not p and not q:
        return True
    if not p or not q:
        return False
    
    left = self.isSameTree(p.left, q.left)
    right = self.isSameTree(p.right, q.right)
    return p.val == q.val and left and right  # Check value last!

# More efficient - check value first
def isSameTree(self, p, q):
    if not p and not q:
        return True
    if not p or not q or p.val != q.val:  # Early exit!
        return False
    return self.isSameTree(p.left, q.left) and self.isSameTree(p.right, q.right)
```

---

## Related Problems

### Similar Problems

1. **[LeetCode 101] Symmetric Tree (Easy)**
   - Check if tree is mirror of itself
   - Compare left subtree with mirror of right subtree

2. **[LeetCode 572] Subtree of Another Tree (Easy)**
   - Check if one tree is subtree of another
   - Uses isSameTree as subroutine

3. **[LeetCode 951] Flip Equivalent Binary Trees (Medium)**
   - Check if trees same after allowing flips
   - Generalization of this problem

4. **[LeetCode 1367] Linked List in Binary Tree (Medium)**
   - Check if linked list path exists in tree
   - Similar comparison logic

### Pattern Variations

- **Symmetric tree:** Compare tree with itself (mirrored)
- **Subtree check:** For each node, check if subtree matches
- **Isomorphic trees:** Allow child reordering
- **Path comparison:** Compare specific paths instead of whole trees

---

## Interview Tips

### What Interviewers Look For

1. **Handling null cases:**
   - Check for null before accessing attributes
   - Proper base cases

2. **Clean code:**
   - Clear logic flow
   - Good variable names
   - Proper spacing

3. **Efficiency:**
   - Early termination when possible
   - Appropriate data structures

4. **Multiple approaches:**
   - Know both recursive and iterative
   - Understand trade-offs

### Discussion Points

**Interviewer:** "What's the time complexity?"
**You:** "O(min(m, n)) where m and n are the sizes of the two trees. We stop as soon as we find a mismatch, so in the worst case where trees are identical, we visit all n nodes."

**Interviewer:** "Can you do this iteratively?"
**You:** "Yes, I can use a stack or queue to store pairs of nodes to compare. This avoids recursion overhead but requires explicit stack management."

**Interviewer:** "What if we want to check if trees are symmetric instead of same?"
**You:** "For symmetric, we'd compare left subtree of p with right subtree of p (mirrored). The logic is similar but we compare corresponding mirror positions."

**Interviewer:** "How would you handle very deep trees?"
**You:** "For very deep trees, recursive might cause stack overflow. Iterative approach with explicit stack would be better as it uses heap memory."

### Follow-up Questions

1. **"Check if tree is symmetric"**
   - Compare left and right subtrees (mirrored)

2. **"Find first difference between trees"**
   - Return position/value of first mismatch

3. **"Count number of differences"**
   - Continue traversal instead of early return

4. **"Check if trees are isomorphic"**
   - Allow children to be in different orders

---

## Key Takeaways

1. **Recursive Definition:** Two trees are same if roots match AND subtrees are same

2. **Base Cases Critical:**
   - Both null → true
   - One null → false
   - Values differ → false

3. **Early Termination:** Use `and` for short-circuit evaluation

4. **Multiple Approaches Work:**
   - Recursive: Cleanest
   - Iterative DFS: Avoids recursion
   - BFS: Level-by-level comparison

5. **Foundation for Other Problems:** Used as subroutine in many tree problems

---

## Additional Resources

### Visual Learning
- **Tree Comparison Visualizer:** visualgo.net/en/bst
- **Animation:** algorithm-visualizer.org

### Related Concepts
- Tree traversals (parallel traversal)
- Recursion with multiple parameters
- Short-circuit evaluation
- Structural vs value comparison

### Practice Progression
1. Master this problem first
2. Then try: LeetCode 101 (Symmetric Tree)
3. Advanced: LeetCode 572 (Subtree), 951 (Flip Equivalent)

---

## Summary

**Problem:** Check if two binary trees are identical

**Best Solution:** Recursive DFS
```python
def isSameTree(self, p, q):
    if not p and not q:
        return True
    if not p or not q or p.val != q.val:
        return False
    return (self.isSameTree(p.left, q.left) and 
            self.isSameTree(p.right, q.right))
```

**Complexity:** O(min(m,n)) time, O(min(h1,h2)) space

**Key Insight:** Compare roots, then recursively compare subtrees with early termination

**When to Use:** Foundation for tree comparison problems; building block for many algorithms!
