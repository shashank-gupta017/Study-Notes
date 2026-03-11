# Lowest Common Ancestor of a Binary Search Tree

**LeetCode Problem #235 (Medium)**

## Problem Statement

Given a binary search tree (BST), find the lowest common ancestor (LCA) of two given nodes in the BST.

According to the definition of LCA on Wikipedia: "The lowest common ancestor is defined between two nodes `p` and `q` as the lowest node in T that has both `p` and `q` as descendants (where we allow **a node to be a descendant of itself**)."

### Examples

**Example 1:**
```
Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 8
               6
             /   \
            2     8
           / \   / \
          0   4 7   9
             / \
            3   5
Output: 6
Explanation: LCA of nodes 2 and 8 is 6.
```

**Example 2:**
```
Input: root = [6,2,8,0,4,7,9,null,null,3,5], p = 2, q = 4
               6
             /   \
            2     8
           / \   / \
          0   4 7   9
             / \
            3   5
Output: 2
Explanation: LCA of nodes 2 and 4 is 2, since a node can be a descendant of itself.
```

**Example 3:**
```
Input: root = [2,1], p = 2, q = 1
        2
       /
      1
Output: 2
```

### Constraints
- The number of nodes in the tree is in the range `[2, 10^5]`
- `-10^9 <= Node.val <= 10^9`
- All `Node.val` are **unique**
- `p != q`
- `p` and `q` will exist in the BST

---

## Pattern Recognition

This is a **BST Property Exploitation** problem.

**Key Characteristics:**
1. Can use BST ordering property
2. LCA is the split point where paths to p and q diverge
3. If both nodes are smaller → go left
4. If both nodes are larger → go right
5. Otherwise → current node is LCA

**Why This Pattern?**
- BST property: left < root < right
- Can determine which subtree contains each node
- LCA is first node where p and q are in different subtrees
- O(h) solution possible without visiting all nodes

**Pattern Recognition:**
- "BST" + "ancestor" → Use BST property
- "Split point" → Where paths diverge
- "Efficient search" → Don't need full traversal

---

## Solution Approaches

### Approach 1: Recursive BST Property (Most Elegant)

**Intuition:**
Use BST property to navigate:
- If both p and q are smaller than current → LCA is in left subtree
- If both p and q are larger than current → LCA is in right subtree
- Otherwise → current node is the LCA

**Algorithm:**
1. Start at root
2. If both values < current → recurse left
3. If both values > current → recurse right
4. Else → found LCA (split point)

```python
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def lowestCommonAncestor(self, root: TreeNode, p: TreeNode, q: TreeNode) -> TreeNode:
        # If both nodes are smaller, LCA is in left subtree
        if p.val < root.val and q.val < root.val:
            return self.lowestCommonAncestor(root.left, p, q)
        
        # If both nodes are larger, LCA is in right subtree
        elif p.val > root.val and q.val > root.val:
            return self.lowestCommonAncestor(root.right, p, q)
        
        # We've found the split point (one on each side, or one is current)
        else:
            return root
```

**Complexity Analysis:**
- **Time Complexity:** O(h) where h is height
  - Best case (balanced BST): O(log n)
  - Worst case (skewed BST): O(n)
- **Space Complexity:** O(h) for recursion stack
  - Best case: O(log n)
  - Worst case: O(n)

---

### Approach 2: Iterative BST Property (Optimal Space)

**Intuition:**
Same logic as recursive but iterative to save stack space.

**Algorithm:**
1. Start at root
2. While not found:
   - If both < current → go left
   - If both > current → go right
   - Else → return current

```python
class Solution:
    def lowestCommonAncestor(self, root: TreeNode, p: TreeNode, q: TreeNode) -> TreeNode:
        current = root
        
        while current:
            # Both nodes in left subtree
            if p.val < current.val and q.val < current.val:
                current = current.left
            # Both nodes in right subtree
            elif p.val > current.val and q.val > current.val:
                current = current.right
            # Split point found
            else:
                return current
        
        return None  # Should never reach here given constraints
```

**Complexity Analysis:**
- **Time Complexity:** O(h)
  - Best case: O(log n)
  - Worst case: O(n)
- **Space Complexity:** O(1) - No recursion, just a pointer

---

### Approach 3: Recursive with Min/Max

**Intuition:**
Ensure we're checking with proper min/max of p and q values.

```python
class Solution:
    def lowestCommonAncestor(self, root: TreeNode, p: TreeNode, q: TreeNode) -> TreeNode:
        # Ensure p.val <= q.val for simpler logic
        if p.val > q.val:
            p, q = q, p
        
        # Now p.val < q.val
        def helper(node: TreeNode) -> TreeNode:
            # If current value is between p and q, this is LCA
            if p.val <= node.val <= q.val:
                return node
            
            # If both are smaller, go left
            if q.val < node.val:
                return helper(node.left)
            
            # If both are larger, go right
            else:
                return helper(node.right)
        
        return helper(root)
```

**Complexity Analysis:**
- **Time Complexity:** O(h)
- **Space Complexity:** O(h)

---

### Approach 4: Path Tracking (Generic - Works for Any Tree)

**Intuition:**
Find paths from root to both nodes, then find last common node.

```python
class Solution:
    def lowestCommonAncestor(self, root: TreeNode, p: TreeNode, q: TreeNode) -> TreeNode:
        def getPath(node: TreeNode, target: TreeNode) -> List[TreeNode]:
            path = []
            
            while node:
                path.append(node)
                
                if target.val < node.val:
                    node = node.left
                elif target.val > node.val:
                    node = node.right
                else:
                    break
            
            return path
        
        # Get paths to both nodes
        path_p = getPath(root, p)
        path_q = getPath(root, q)
        
        # Find last common node
        lca = None
        for i in range(min(len(path_p), len(path_q))):
            if path_p[i] == path_q[i]:
                lca = path_p[i]
            else:
                break
        
        return lca
```

**Complexity Analysis:**
- **Time Complexity:** O(h)
- **Space Complexity:** O(h) - Store both paths

---

### Approach 5: Generic Tree LCA (Not Optimized for BST)

**Intuition:**
Treat as regular binary tree, don't use BST property.

```python
class Solution:
    def lowestCommonAncestor(self, root: TreeNode, p: TreeNode, q: TreeNode) -> TreeNode:
        # Base case
        if not root or root == p or root == q:
            return root
        
        # Search in subtrees
        left = self.lowestCommonAncestor(root.left, p, q)
        right = self.lowestCommonAncestor(root.right, p, q)
        
        # If found in both subtrees, current is LCA
        if left and right:
            return root
        
        # Return whichever is not None
        return left if left else right
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visits all nodes
- **Space Complexity:** O(h)

**Note:** This works but doesn't leverage BST property (slower).

---

## Detailed Walkthrough

Let's trace through **Approach 2** with Example 1:

```
Tree:          6
             /   \
            2     8
           / \   / \
          0   4 7   9
             / \
            3   5

Find LCA of p=2, q=8
```

**Execution Flow:**

```
Step 1: current = 6
  - p.val (2) < current.val (6)? YES
  - q.val (8) < current.val (6)? NO
  - Not both in left
  - p.val (2) > current.val (6)? NO
  - q.val (8) > current.val (6)? YES
  - Not both in right
  - Split point! Return 6 ✓

Result: 6
```

**Why is 6 the LCA?**
- 2 is in left subtree of 6
- 8 is in right subtree of 6
- 6 is the first node where they split

**Example 2: p=2, q=4**

```
Step 1: current = 6
  - Both p (2) and q (4) < current (6)
  - Go left → current = 2

Step 2: current = 2
  - p.val (2) = current.val (2) - not less, not greater
  - q.val (4) > current.val (2)
  - Not both in same direction
  - Split point! Return 2 ✓

Result: 2
```

**Why is 2 the LCA?**
- 2 is one of the target nodes
- 4 is in right subtree of 2
- A node can be ancestor of itself

---

## Edge Cases

### 1. LCA is Root
```python
Input: root = [6,2,8], p = 2, q = 8
        6
       / \
      2   8
Output: 6
```
- Split happens at root

### 2. One Node is Ancestor of Other
```python
Input: root = [6,2,8,0,4], p = 2, q = 4
        6
       / \
      2   8
     / \
    0   4
Output: 2
```
- Node itself is LCA

### 3. Both Nodes in Left Subtree
```python
Input: root = [6,2,8,0,4], p = 0, q = 4
        6
       / \
      2   8
     / \
    0   4
Output: 2
```
- Navigate left from root

### 4. Both Nodes in Right Subtree
```python
Input: root = [6,2,8,7,9], p = 7, q = 9
        6
       / \
      2   8
         / \
        7   9
Output: 8
```
- Navigate right from root

### 5. Two-Node Tree
```python
Input: root = [2,1], p = 2, q = 1
        2
       /
      1
Output: 2
```
- Root is LCA

### 6. Deep Tree - Same Branch
```python
Input: root = [10,5,15,3,7,12,20], p = 3, q = 7
         10
        /  \
       5    15
      / \   / \
     3   7 12  20
Output: 5
```

### 7. Deep Tree - Different Branches
```python
Input: root = [10,5,15,3,7,12,20], p = 3, q = 12
         10
        /  \
       5    15
      / \   / \
     3   7 12  20
Output: 10
```

---

## Common Mistakes

### 1. **Not Using BST Property**
```python
# WRONG - O(n) solution when O(h) is possible
def lowestCommonAncestor(self, root, p, q):
    # Using generic tree LCA approach
    # Doesn't leverage BST ordering
```
**Fix:** Use value comparisons to navigate

### 2. **Incorrect Comparison Logic**
```python
# WRONG - What if p.val < root.val but q.val > root.val?
if p.val < root.val:
    return self.lowestCommonAncestor(root.left, p, q)
else:
    return self.lowestCommonAncestor(root.right, p, q)
```
**Fix:** Check BOTH p and q against current node

### 3. **Not Handling Node as Its Own Ancestor**
```python
# WRONG - Doesn't consider node as its own descendant
if p.val < root.val and q.val < root.val:
    return self.lowestCommonAncestor(root.left, p, q)
if p.val > root.val and q.val > root.val:
    return self.lowestCommonAncestor(root.right, p, q)
# Missing: return root
```

### 4. **Unnecessary Node Existence Check**
```python
# UNNECESSARY - Problem guarantees p and q exist
def lowestCommonAncestor(self, root, p, q):
    if not root:  # Root is never None given constraints
        return None
```

### 5. **Comparing Node Objects Instead of Values**
```python
# WRONG - Should compare values
if p < root:  # Compares object references!
    return self.lowestCommonAncestor(root.left, p, q)
```
**Fix:** Use `p.val` and `root.val`

### 6. **Not Handling Equal Values**
```python
# INCOMPLETE - What if p.val == root.val?
if p.val < root.val and q.val < root.val:
    # go left
elif p.val > root.val and q.val > root.val:
    # go right
# Missing else case!
```

---

## Testing

### Test Cases

```python
def test_lowest_common_ancestor():
    solution = Solution()
    
    # Helper to build tree
    def build_tree():
        root = TreeNode(6)
        root.left = TreeNode(2)
        root.right = TreeNode(8)
        root.left.left = TreeNode(0)
        root.left.right = TreeNode(4)
        root.left.right.left = TreeNode(3)
        root.left.right.right = TreeNode(5)
        root.right.left = TreeNode(7)
        root.right.right = TreeNode(9)
        return root
    
    # Test 1: Example 1
    root1 = build_tree()
    p1 = root1.left  # 2
    q1 = root1.right  # 8
    assert solution.lowestCommonAncestor(root1, p1, q1).val == 6
    
    # Test 2: Example 2
    root2 = build_tree()
    p2 = root2.left  # 2
    q2 = root2.left.right  # 4
    assert solution.lowestCommonAncestor(root2, p2, q2).val == 2
    
    # Test 3: Both in left subtree
    root3 = build_tree()
    p3 = root3.left.left  # 0
    q3 = root3.left.right  # 4
    assert solution.lowestCommonAncestor(root3, p3, q3).val == 2
    
    # Test 4: Both in right subtree
    root4 = build_tree()
    p4 = root4.right.left  # 7
    q4 = root4.right.right  # 9
    assert solution.lowestCommonAncestor(root4, p4, q4).val == 8
    
    # Test 5: Two-node tree
    root5 = TreeNode(2)
    root5.left = TreeNode(1)
    assert solution.lowestCommonAncestor(root5, root5, root5.left).val == 2
    
    # Test 6: Deep nodes
    root6 = build_tree()
    p6 = root6.left.right.left  # 3
    q6 = root6.left.right.right  # 5
    assert solution.lowestCommonAncestor(root6, p6, q6).val == 4
    
    print("All test cases passed!")

test_lowest_common_ancestor()
```

---

## Optimization Notes

### Time Optimization

**BST Property is Key:**
- Generic tree LCA: O(n) - must visit all nodes
- BST LCA: O(h) - follow a single path
- Huge difference for large trees!

**Best Case:** O(1) - LCA is root
**Worst Case:** O(n) - Completely skewed tree

### Space Optimization

**Iterative > Recursive:**
- Recursive: O(h) stack space
- Iterative: O(1) extra space
- For interview, iterative is optimal

**Comparison:**
- Approach 1 (Recursive): O(h) space
- Approach 2 (Iterative): O(1) space ✓ Best
- Approach 4 (Path): O(h) space

---

## Related Problems

### Similar Problems

1. **[LeetCode 236] Lowest Common Ancestor of Binary Tree (Medium)**
   - Same problem but for regular binary tree (not BST)
   - Can't use value comparisons
   - O(n) solution required

2. **[LeetCode 1644] Lowest Common Ancestor of Binary Tree II (Medium)**
   - Nodes might not exist in tree
   - Need to verify existence

3. **[LeetCode 1650] Lowest Common Ancestor of Binary Tree III (Medium)**
   - Given parent pointers
   - Can traverse upward

4. **[LeetCode 1676] Lowest Common Ancestor of Binary Tree IV (Medium)**
   - Find LCA of multiple nodes (not just two)
   - Generalization of original problem

### Pattern Variations

- **Distance between two nodes:** Find LCA, then calculate distances
- **Path between two nodes:** Find LCA, construct path through it
- **LCA in N-ary tree:** Similar logic with multiple children
- **LCA with parent pointers:** Use two-pointer approach

---

## Interview Tips

### What Interviewers Look For

1. **Recognizing BST optimization:**
   - Using value comparisons
   - Not treating as generic tree

2. **Clean logic:**
   - Three cases (both left, both right, split)
   - Clear comparisons

3. **Space efficiency:**
   - Knowing iterative is O(1) space
   - When to prefer it

4. **Edge cases:**
   - Node as its own ancestor
   - LCA at root
   - Deep trees

### Discussion Points

**Interviewer:** "What makes this different from regular tree LCA?"
**You:** "We can use BST's ordering property. By comparing values, we know which subtree contains each node without exploring both subtrees."

**Interviewer:** "What's the time complexity?"
**You:** "O(h) where h is height. For balanced BST, O(log n). For skewed BST, O(n). We follow a single path down the tree."

**Interviewer:** "Can you optimize space?"
**You:** "Yes, iterative solution uses O(1) space instead of O(h) for recursion. We maintain just one pointer moving down the tree."

**Interviewer:** "What if nodes might not exist?"
**You:** "We'd need to verify existence first, either by searching for both nodes, or modifying the algorithm to return null if not found."

### Follow-up Questions

1. **"Find distance between two nodes"**
   - Find LCA, calculate depth of each node from LCA
   - Distance = depth_p + depth_q

2. **"Find path between two nodes"**
   - Find LCA
   - Path = path_from_p_to_LCA + path_from_LCA_to_q

3. **"LCA of multiple nodes"**
   - Find LCA of first two, then LCA of result with third, etc.

4. **"What if it's not a BST?"**
   - Use generic tree LCA approach (O(n) time)

---

## Key Takeaways

1. **BST Property Makes It Efficient:** O(h) vs O(n)

2. **Three Cases:**
   - Both smaller → go left
   - Both larger → go right
   - Otherwise → found LCA

3. **Iterative is Optimal:**
   ```python
   while current:
       if p.val < current.val and q.val < current.val:
           current = current.left
       elif p.val > current.val and q.val > current.val:
           current = current.right
       else:
           return current
   ```

4. **Node Can Be Its Own Ancestor:** Important edge case

5. **Foundation for Path Problems:** LCA is key for many path-based queries

---

## Additional Resources

### Visual Learning
- **BST LCA Visualization:** visualgo.net/en/bst
- **Interactive Demo:** algorithm-visualizer.org

### Related Concepts
- Binary Search Tree properties
- Tree navigation
- Path finding in trees
- Ancestor relationships

### Practice Progression
1. Master this problem first
2. Then try: LeetCode 236 (Binary Tree LCA)
3. Advanced: LeetCode 1644 (with existence check), 1676 (multiple nodes)

---

## Summary

**Problem:** Find lowest common ancestor in BST

**Best Solution:** Iterative with BST Property
```python
def lowestCommonAncestor(self, root, p, q):
    current = root
    while current:
        if p.val < current.val and q.val < current.val:
            current = current.left
        elif p.val > current.val and q.val > current.val:
            current = current.right
        else:
            return current
```

**Complexity:** O(h) time, O(1) space

**Key Insight:** Use BST property to find split point where paths diverge

**When to Use:** BST ancestor queries; foundation for path problems in BSTs!
