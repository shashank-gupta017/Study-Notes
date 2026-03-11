# Maximum Depth of Binary Tree

**LeetCode Problem #104 (Easy)**

## Problem Statement

Given the root of a binary tree, return its maximum depth.

A binary tree's maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.

### Examples

**Example 1:**
```
Input: root = [3,9,20,null,null,15,7]
        3
       / \
      9  20
         / \
        15  7
Output: 3
```

**Example 2:**
```
Input: root = [1,null,2]
        1
         \
          2
Output: 2
```

**Example 3:**
```
Input: root = []
Output: 0
```

**Example 4:**
```
Input: root = [0]
Output: 1
```

### Constraints
- The number of nodes in the tree is in the range `[0, 10^4]`
- `-100 <= Node.val <= 100`

---

## Pattern Recognition

This is a **Tree DFS (Depth-First Search)** problem with a recursive approach.

**Key Characteristics:**
1. Need to explore all paths to find the deepest one
2. Naturally recursive problem - depth of tree = max depth of subtrees + 1
3. Post-order traversal pattern (process children before parent)

**Why This Pattern?**
- DFS naturally explores paths to their end
- Recursive definition: max depth = max(left depth, right depth) + 1
- Each node needs information from its children (bottom-up approach)

---

## Solution Approaches

### Approach 1: Recursive DFS (Most Intuitive)

**Intuition:**
The depth of a tree is 1 + the maximum depth of its left or right subtree. Base case: empty tree has depth 0.

**Algorithm:**
1. If tree is empty, return 0
2. Recursively find left subtree depth
3. Recursively find right subtree depth
4. Return max of both depths + 1

```python
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def maxDepth(self, root: Optional[TreeNode]) -> int:
        # Base case: empty tree
        if not root:
            return 0
        
        # Recursive case: get depth of left and right subtrees
        left_depth = self.maxDepth(root.left)
        right_depth = self.maxDepth(root.right)
        
        # Return max depth + 1 (for current node)
        return max(left_depth, right_depth) + 1
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visit each node exactly once
- **Space Complexity:** O(h) - Recursion stack depth where h is tree height
  - Best case (balanced tree): O(log n)
  - Worst case (skewed tree): O(n)

---

### Approach 2: Recursive DFS (Compact)

**Intuition:**
More concise version of the same approach.

```python
class Solution:
    def maxDepth(self, root: Optional[TreeNode]) -> int:
        if not root:
            return 0
        return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(h)

---

### Approach 3: Iterative DFS (Using Stack)

**Intuition:**
Simulate recursive DFS using an explicit stack. Store node with its current depth.

**Algorithm:**
1. Use stack to store (node, depth) pairs
2. Track maximum depth seen
3. Process nodes depth-first

```python
class Solution:
    def maxDepth(self, root: Optional[TreeNode]) -> int:
        if not root:
            return 0
        
        # Stack stores (node, current_depth)
        stack = [(root, 1)]
        max_depth = 0
        
        while stack:
            node, depth = stack.pop()
            
            if node:
                max_depth = max(max_depth, depth)
                # Push children with incremented depth
                stack.append((node.left, depth + 1))
                stack.append((node.right, depth + 1))
        
        return max_depth
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visit each node once
- **Space Complexity:** O(h) - Stack size in worst case
  - Best case: O(log n) for balanced tree
  - Worst case: O(n) for skewed tree

---

### Approach 4: BFS (Level Order Traversal)

**Intuition:**
Count the number of levels in the tree using breadth-first search.

**Algorithm:**
1. Use queue for level-order traversal
2. Count number of levels processed
3. Each level increments the depth

```python
from collections import deque

class Solution:
    def maxDepth(self, root: Optional[TreeNode]) -> int:
        if not root:
            return 0
        
        queue = deque([root])
        depth = 0
        
        while queue:
            depth += 1
            # Process all nodes at current level
            level_size = len(queue)
            
            for _ in range(level_size):
                node = queue.popleft()
                
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
        
        return depth
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visit each node once
- **Space Complexity:** O(w) where w is maximum width of tree
  - Best case: O(1) for skewed tree
  - Worst case: O(n/2) = O(n) for complete tree (last level has ~n/2 nodes)

---

### Approach 5: DFS with Global Variable

**Intuition:**
Track maximum depth using a class/global variable during traversal.

```python
class Solution:
    def maxDepth(self, root: Optional[TreeNode]) -> int:
        self.max_depth = 0
        
        def dfs(node: Optional[TreeNode], depth: int) -> None:
            if not node:
                return
            
            # Update max depth
            self.max_depth = max(self.max_depth, depth)
            
            # Recurse on children
            dfs(node.left, depth + 1)
            dfs(node.right, depth + 1)
        
        dfs(root, 1)
        return self.max_depth
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(h)

---

## Detailed Walkthrough

Let's trace through **Approach 1** with Example 1:

```
Tree:     3
         / \
        9  20
           / \
          15  7
```

**Execution Flow:**

```
maxDepth(3):
  └─ left_depth = maxDepth(9):
       └─ left_depth = maxDepth(null) = 0
       └─ right_depth = maxDepth(null) = 0
       └─ return max(0, 0) + 1 = 1
  └─ right_depth = maxDepth(20):
       └─ left_depth = maxDepth(15):
            └─ left_depth = maxDepth(null) = 0
            └─ right_depth = maxDepth(null) = 0
            └─ return max(0, 0) + 1 = 1
       └─ right_depth = maxDepth(7):
            └─ left_depth = maxDepth(null) = 0
            └─ right_depth = maxDepth(null) = 0
            └─ return max(0, 0) + 1 = 1
       └─ return max(1, 1) + 1 = 2
  └─ return max(1, 2) + 1 = 3
```

**Step-by-Step:**
1. Start at root (3), depth currently unknown
2. Go left to node 9
   - Both children are null
   - Depth of 9's subtree = max(0, 0) + 1 = 1
3. Go right to node 20
   - Check left child (15)
     - Both children are null
     - Depth of 15's subtree = 1
   - Check right child (7)
     - Both children are null
     - Depth of 7's subtree = 1
   - Depth of 20's subtree = max(1, 1) + 1 = 2
4. Depth of whole tree = max(1, 2) + 1 = 3

---

## Edge Cases

### 1. Empty Tree
```python
Input: root = None
Output: 0
```
- Base case handles this directly

### 2. Single Node
```python
Input: root = [1]
        1
Output: 1
```
- max(0, 0) + 1 = 1

### 3. Left-Skewed Tree
```python
Input: root = [1,2,null,3,null,4]
        1
       /
      2
     /
    3
   /
  4
Output: 4
```
- Recursion depth equals tree height

### 4. Right-Skewed Tree
```python
Input: root = [1,null,2,null,3,null,4]
        1
         \
          2
           \
            3
             \
              4
Output: 4
```
- Similar to left-skewed

### 5. Complete Binary Tree
```python
Input: root = [1,2,3,4,5,6,7]
           1
         /   \
        2     3
       / \   / \
      4   5 6   7
Output: 3
```
- Balanced tree case

### 6. Perfect Binary Tree
```python
Input: root = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
Output: 4
```
- All levels completely filled

---

## Common Mistakes

### 1. **Forgetting Base Case**
```python
# WRONG - Will cause infinite recursion
def maxDepth(self, root):
    return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))
```
**Fix:** Always check if node is None first

### 2. **Not Adding 1 for Current Node**
```python
# WRONG - Forgets to count current node
def maxDepth(self, root):
    if not root:
        return 0
    return max(self.maxDepth(root.left), self.maxDepth(root.right))
```
**Fix:** Add 1 to the maximum depth of children

### 3. **Off-by-One Error in BFS**
```python
# WRONG - Initializes depth to 1 for empty tree
def maxDepth(self, root):
    if not root:
        return 0
    queue = [root]
    depth = 1  # Should start at 0 before loop
    # ... rest of code
```

### 4. **Not Handling None Children in Iterative Approach**
```python
# WRONG - Will add None to stack
stack = [(root, 1)]
while stack:
    node, depth = stack.pop()
    max_depth = max(max_depth, depth)
    stack.append((node.left, depth + 1))   # node.left might be None
    stack.append((node.right, depth + 1))  # node.right might be None
```
**Fix:** Check if children exist before adding to stack

### 5. **Using Wrong Data Structure for BFS**
```python
# WRONG - Using list as queue (inefficient)
queue = [root]
while queue:
    node = queue.pop(0)  # O(n) operation
```
**Fix:** Use `collections.deque` for O(1) operations

---

## Testing

### Test Cases

```python
def test_maximum_depth():
    solution = Solution()
    
    # Test 1: Example from problem
    root1 = TreeNode(3)
    root1.left = TreeNode(9)
    root1.right = TreeNode(20)
    root1.right.left = TreeNode(15)
    root1.right.right = TreeNode(7)
    assert solution.maxDepth(root1) == 3
    
    # Test 2: Single node
    root2 = TreeNode(1)
    assert solution.maxDepth(root2) == 1
    
    # Test 3: Empty tree
    assert solution.maxDepth(None) == 0
    
    # Test 4: Left-skewed tree
    root4 = TreeNode(1)
    root4.left = TreeNode(2)
    root4.left.left = TreeNode(3)
    assert solution.maxDepth(root4) == 3
    
    # Test 5: Right-skewed tree
    root5 = TreeNode(1)
    root5.right = TreeNode(2)
    root5.right.right = TreeNode(3)
    assert solution.maxDepth(root5) == 3
    
    # Test 6: Complete tree
    root6 = TreeNode(1)
    root6.left = TreeNode(2)
    root6.right = TreeNode(3)
    root6.left.left = TreeNode(4)
    root6.left.right = TreeNode(5)
    root6.right.left = TreeNode(6)
    root6.right.right = TreeNode(7)
    assert solution.maxDepth(root6) == 3
    
    print("All test cases passed!")

test_maximum_depth()
```

---

## Optimization Notes

### Space Optimization

**Recursive Approach:**
- Cannot optimize further without changing algorithm
- Stack space is unavoidable for recursive solutions
- For very deep trees, might hit stack limit

**Iterative Approach:**
- Already optimal for DFS
- BFS uses more space for wide trees but same for skewed trees

### Time Optimization

- All approaches are O(n) - must visit every node
- Cannot optimize further as we need complete information
- Early termination not possible (need to check all paths)

---

## Related Problems

### Similar Problems
1. **[LeetCode 111] Minimum Depth of Binary Tree (Easy)**
   - Similar pattern but find minimum instead of maximum
   - Need to be careful with one-child nodes

2. **[LeetCode 110] Balanced Binary Tree (Easy)**
   - Checks if depth difference between subtrees ≤ 1
   - Uses similar recursive depth calculation

3. **[LeetCode 543] Diameter of Binary Tree (Easy)**
   - Find longest path between any two nodes
   - Uses depth calculation as subroutine

4. **[LeetCode 559] Maximum Depth of N-ary Tree (Easy)**
   - Same problem but for n-ary trees
   - Need to check all children instead of just left/right

### Pattern Variations
- **Count nodes at depth k:** Modify to count nodes at specific level
- **Find all paths of length k:** Track paths while calculating depth
- **Average of levels:** Combine depth calculation with value aggregation

---

## Interview Tips

### What Interviewers Look For

1. **Understanding of recursion:**
   - Can you identify base case?
   - Can you formulate recursive relation?

2. **Multiple approaches:**
   - Know both recursive and iterative solutions
   - Understand when to use DFS vs BFS

3. **Complexity analysis:**
   - Space complexity for skewed vs balanced trees
   - Stack space vs heap space trade-offs

4. **Edge case handling:**
   - Empty tree
   - Single node
   - Skewed trees

### Discussion Points

**Interviewer:** "Can we do better than O(n) time?"
**You:** "No, we must visit each node at least once to determine the maximum depth. We can't know if a deeper node exists without checking."

**Interviewer:** "Which approach is better - recursive or iterative?"
**You:** "Recursive is more intuitive and cleaner code, but iterative avoids stack overflow for very deep trees. For most practical cases, recursive is preferred."

**Interviewer:** "What if the tree is very wide vs very deep?"
**You:** "For wide trees, BFS uses O(w) space which could be large. For deep trees, DFS uses O(h) space. Choose based on tree shape if known."

### Follow-up Questions

1. **"Find the diameter of the tree"**
   - Use similar recursion but return both depth and diameter

2. **"Find minimum depth"**
   - Need to handle case where one subtree is empty

3. **"Count nodes at depth k"**
   - Modify to check depth == k condition

4. **"Find depth of specific node"**
   - Track current depth in recursion, return when found

---

## Key Takeaways

1. **Recursive Definition:** `depth(node) = 1 + max(depth(left), depth(right))`

2. **Base Case is Critical:** Empty tree has depth 0

3. **Multiple Valid Approaches:**
   - Recursive DFS: Most intuitive
   - Iterative DFS: Avoids recursion overhead
   - BFS: Natural for level-based thinking

4. **Space-Time Tradeoff:** All O(n) time, but space varies by approach

5. **Foundation Problem:** Understanding this helps with many tree problems

---

## Additional Resources

### Visual Learning
- **Tree Recursion Visualizer:** pythontutor.com
- **Animation:** visualgo.net/en/bst

### Related Concepts
- Tree traversals (preorder, inorder, postorder)
- Recursion and call stack
- BFS vs DFS

### Practice Problems
- Start with: LeetCode 111, 559 (easier variations)
- Then try: LeetCode 110, 543 (uses depth as building block)
- Advanced: LeetCode 124 (Binary Tree Maximum Path Sum)

---

## Summary

**Problem:** Find maximum depth of binary tree

**Best Solution:** Recursive DFS
```python
def maxDepth(self, root):
    if not root:
        return 0
    return 1 + max(self.maxDepth(root.left), self.maxDepth(root.right))
```

**Complexity:** O(n) time, O(h) space

**Key Insight:** Depth = 1 + max depth of subtrees

**When to Use:** Foundation for many tree problems; master this pattern first!
