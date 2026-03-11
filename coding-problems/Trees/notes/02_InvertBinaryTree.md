# Invert Binary Tree

**LeetCode Problem #226 (Easy)**

## Problem Statement

Given the root of a binary tree, invert the tree, and return its root.

Inverting a binary tree means swapping the left and right children of all nodes in the tree.

### Examples

**Example 1:**
```
Input: root = [4,2,7,1,3,6,9]

Original:           Inverted:
      4                 4
     / \               / \
    2   7             7   2
   / \ / \           / \ / \
  1  3 6  9         9  6 3  1

Output: [4,7,2,9,6,3,1]
```

**Example 2:**
```
Input: root = [2,1,3]

Original:     Inverted:
    2             2
   / \           / \
  1   3         3   1

Output: [2,3,1]
```

**Example 3:**
```
Input: root = []
Output: []
```

### Constraints
- The number of nodes in the tree is in the range `[0, 100]`
- `-100 <= Node.val <= 100`

---

## Pattern Recognition

This is a **Tree DFS/BFS Transformation** problem.

**Key Characteristics:**
1. Need to visit every node in the tree
2. Perform same operation at each node (swap children)
3. Can be solved with any tree traversal method
4. Local operation (each node independently swaps its children)

**Why This Pattern?**
- Simple transformation applied to each node
- No dependency between nodes - each swap is independent
- Naturally recursive structure
- Can use DFS (recursive/iterative) or BFS

**Pattern Recognition:**
- "Transform every node" → Consider DFS/BFS
- "Swap/modify structure" → Local operation at each node
- "Return modified tree" → In-place or create new tree

---

## Solution Approaches

### Approach 1: Recursive DFS (Most Intuitive)

**Intuition:**
To invert a tree:
1. Invert the left subtree
2. Invert the right subtree
3. Swap the left and right subtrees

**Algorithm:**
1. Base case: If node is null, return null
2. Recursively invert left subtree
3. Recursively invert right subtree
4. Swap left and right children
5. Return the root

```python
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def invertTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        # Base case: empty tree
        if not root:
            return None
        
        # Recursively invert left and right subtrees
        left_inverted = self.invertTree(root.left)
        right_inverted = self.invertTree(root.right)
        
        # Swap the children
        root.left = right_inverted
        root.right = left_inverted
        
        return root
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visit each node exactly once
- **Space Complexity:** O(h) - Recursion stack depth
  - Best case (balanced): O(log n)
  - Worst case (skewed): O(n)

---

### Approach 2: Recursive DFS (Compact Version)

**Intuition:**
Same approach but more concise - swap first, then recurse.

```python
class Solution:
    def invertTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        if not root:
            return None
        
        # Swap children first
        root.left, root.right = root.right, root.left
        
        # Then recursively invert subtrees
        self.invertTree(root.left)
        self.invertTree(root.right)
        
        return root
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(h)

---

### Approach 3: Recursive DFS (One-Liner Style)

**Intuition:**
Ultra-compact version using Python's tuple unpacking.

```python
class Solution:
    def invertTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        if root:
            root.left, root.right = self.invertTree(root.right), self.invertTree(root.left)
        return root
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(h)

---

### Approach 4: Iterative DFS (Using Stack)

**Intuition:**
Use explicit stack to avoid recursion. Push nodes onto stack and process them iteratively.

**Algorithm:**
1. Initialize stack with root
2. While stack is not empty:
   - Pop a node
   - Swap its children
   - Push non-null children onto stack
3. Return root

```python
class Solution:
    def invertTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        if not root:
            return None
        
        # Use stack for iterative traversal
        stack = [root]
        
        while stack:
            node = stack.pop()
            
            # Swap children
            node.left, node.right = node.right, node.left
            
            # Add non-null children to stack
            if node.left:
                stack.append(node.left)
            if node.right:
                stack.append(node.right)
        
        return root
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visit each node once
- **Space Complexity:** O(h) - Stack size
  - Best case: O(log n)
  - Worst case: O(n)

---

### Approach 5: BFS (Level Order Traversal)

**Intuition:**
Process tree level by level using a queue. Swap children of each node as we visit it.

**Algorithm:**
1. Initialize queue with root
2. While queue is not empty:
   - Dequeue a node
   - Swap its children
   - Enqueue non-null children
3. Return root

```python
from collections import deque

class Solution:
    def invertTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        if not root:
            return None
        
        # Use queue for level-order traversal
        queue = deque([root])
        
        while queue:
            node = queue.popleft()
            
            # Swap children
            node.left, node.right = node.right, node.left
            
            # Add non-null children to queue
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        return root
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visit each node once
- **Space Complexity:** O(w) where w is maximum width
  - Best case (skewed): O(1)
  - Worst case (complete tree): O(n/2) = O(n)

---

### Approach 6: Morris Traversal (Space Optimized)

**Intuition:**
Use Morris traversal for O(1) extra space (excluding recursion). Modify tree structure temporarily.

```python
class Solution:
    def invertTree(self, root: Optional[TreeNode]) -> Optional[TreeNode]:
        current = root
        
        while current:
            if current.left:
                # Find inorder predecessor
                predecessor = current.left
                while predecessor.right and predecessor.right != current:
                    predecessor = predecessor.right
                
                if not predecessor.right:
                    # Create thread
                    predecessor.right = current
                    # Swap before going left
                    current.left, current.right = current.right, current.left
                    current = current.right  # Now points to original left
                else:
                    # Remove thread
                    predecessor.right = None
                    current = current.left  # Now points to original right
            else:
                # Swap and move right
                current.left, current.right = current.right, current.left
                current = current.left  # Now points to original right
        
        return root
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(1) - No extra space used

**Note:** Morris traversal is more complex and rarely needed in interviews for this problem.

---

## Detailed Walkthrough

Let's trace through **Approach 2** with Example 1:

```
Original Tree:
      4
     / \
    2   7
   / \ / \
  1  3 6  9
```

**Execution Flow:**

```
invertTree(4):
  └─ Swap 4's children: 4.left=7, 4.right=2
       4
      / \
     7   2
    / \ / \
   6  9 1  3
  
  └─ invertTree(7):  // originally was 4.right
       └─ Swap 7's children: 7.left=9, 7.right=6
            7
           / \
          9   6
       └─ invertTree(9): has no children, return 9
       └─ invertTree(6): has no children, return 6
       └─ return 7
  
  └─ invertTree(2):  // originally was 4.left
       └─ Swap 2's children: 2.left=3, 2.right=1
            2
           / \
          3   1
       └─ invertTree(3): has no children, return 3
       └─ invertTree(1): has no children, return 1
       └─ return 2
  
  └─ return 4

Final Tree:
      4
     / \
    7   2
   / \ / \
  9  6 3  1
```

**Step-by-Step Process:**
1. Start at root (4)
2. Swap: left becomes 7, right becomes 2
3. Recurse on new left (7):
   - Swap: left becomes 9, right becomes 6
   - Recurse on leaves (9 and 6) - they're already inverted
4. Recurse on new right (2):
   - Swap: left becomes 3, right becomes 1
   - Recurse on leaves (3 and 1) - they're already inverted
5. Return root

---

## Edge Cases

### 1. Empty Tree
```python
Input: root = None
Output: None
```
- Base case handles this directly

### 2. Single Node
```python
Input: root = [1]
        1
Output: [1]
        1
```
- No children to swap, returns as is

### 3. Left-Skewed Tree
```python
Input: root = [1,2,null,3]
        1              1
       /                \
      2        →         2
     /                    \
    3                      3
Output: [1,null,2,null,3]
```
- Becomes right-skewed

### 4. Right-Skewed Tree
```python
Input: root = [1,null,2,null,3]
        1              1
         \            /
          2    →     2
           \        /
            3      3
Output: [1,2,null,3]
```
- Becomes left-skewed

### 5. Complete Binary Tree
```python
Input: root = [1,2,3,4,5,6,7]
           1                 1
         /   \             /   \
        2     3     →     3     2
       / \   / \         / \   / \
      4   5 6   7       7   6 5   4
```
- All levels fully swapped

### 6. Tree with Only Left Children
```python
Input: root = [1,2,null,3,null,4]
        1              1
       /                \
      2        →         2
     /                    \
    3                      3
   /                        \
  4                          4
```
- Mirror transformation

---

## Common Mistakes

### 1. **Swapping Without Saving References**
```python
# WRONG - Loses reference to original right
def invertTree(self, root):
    if not root:
        return None
    root.left = self.invertTree(root.right)
    root.right = self.invertTree(root.left)  # root.left already changed!
    return root
```
**Fix:** Save both references first or swap after recursion

### 2. **Forgetting to Return Root**
```python
# WRONG - Returns None
def invertTree(self, root):
    if not root:
        return None
    root.left, root.right = root.right, root.left
    self.invertTree(root.left)
    self.invertTree(root.right)
    # Missing: return root
```

### 3. **Not Handling Null Children in Iterative**
```python
# WRONG - Will cause error when accessing None.left
stack = [root]
while stack:
    node = stack.pop()
    node.left, node.right = node.right, node.left
    stack.append(node.left)   # Might be None
    stack.append(node.right)  # Might be None
```
**Fix:** Check if children exist before adding

### 4. **Modifying Tree During Creation**
```python
# WRONG - Creating new tree but mixing references
def invertTree(self, root):
    if not root:
        return None
    new_root = TreeNode(root.val)
    new_root.left = self.invertTree(root.right)
    new_root.right = self.invertTree(root.left)
    # This creates new tree - usually not needed
```
**Note:** Problem asks to invert in-place

### 5. **Incorrect Base Case**
```python
# WRONG - Doesn't handle leaf nodes properly
def invertTree(self, root):
    if not root.left and not root.right:  # What if root is None?
        return root
    # ...
```
**Fix:** Check if root is None first

---

## Testing

### Test Cases

```python
def test_invert_tree():
    solution = Solution()
    
    # Helper function to convert tree to list (level order)
    def tree_to_list(root):
        if not root:
            return []
        result, queue = [], [root]
        while queue:
            node = queue.pop(0)
            if node:
                result.append(node.val)
                queue.append(node.left)
                queue.append(node.right)
            else:
                result.append(None)
        # Remove trailing Nones
        while result and result[-1] is None:
            result.pop()
        return result
    
    # Test 1: Example from problem
    root1 = TreeNode(4)
    root1.left = TreeNode(2)
    root1.right = TreeNode(7)
    root1.left.left = TreeNode(1)
    root1.left.right = TreeNode(3)
    root1.right.left = TreeNode(6)
    root1.right.right = TreeNode(9)
    result1 = solution.invertTree(root1)
    assert tree_to_list(result1) == [4,7,2,9,6,3,1]
    
    # Test 2: Small tree
    root2 = TreeNode(2)
    root2.left = TreeNode(1)
    root2.right = TreeNode(3)
    result2 = solution.invertTree(root2)
    assert tree_to_list(result2) == [2,3,1]
    
    # Test 3: Empty tree
    assert solution.invertTree(None) is None
    
    # Test 4: Single node
    root4 = TreeNode(1)
    result4 = solution.invertTree(root4)
    assert tree_to_list(result4) == [1]
    
    # Test 5: Left-skewed
    root5 = TreeNode(1)
    root5.left = TreeNode(2)
    root5.left.left = TreeNode(3)
    result5 = solution.invertTree(root5)
    assert tree_to_list(result5) == [1,None,2,None,3]
    
    # Test 6: Right-skewed
    root6 = TreeNode(1)
    root6.right = TreeNode(2)
    root6.right.right = TreeNode(3)
    result6 = solution.invertTree(root6)
    assert tree_to_list(result6) == [1,2,None,3]
    
    print("All test cases passed!")

test_invert_tree()
```

---

## Optimization Notes

### Space Optimization

**Comparison:**
- **Recursive DFS:** O(h) stack space
- **Iterative DFS:** O(h) explicit stack
- **BFS:** O(w) queue space
- **Morris:** O(1) extra space (most optimal)

**When to use which:**
- **Recursive:** Cleanest code, acceptable for most trees
- **Iterative DFS:** When recursion depth is a concern
- **BFS:** When tree is very deep but not wide
- **Morris:** When space is critical (rare in practice)

### Time Optimization

- All approaches are O(n) - must visit every node
- Cannot optimize further
- Early termination not applicable (must invert entire tree)

---

## Related Problems

### Similar Problems

1. **[LeetCode 100] Same Tree (Easy)**
   - Compare two trees structurally
   - Can use inversion logic to check symmetry

2. **[LeetCode 101] Symmetric Tree (Easy)**
   - Check if tree is mirror of itself
   - Similar to checking if tree equals its inversion

3. **[LeetCode 951] Flip Equivalent Binary Trees (Medium)**
   - Check if two trees are same after any number of flips
   - Generalizes inversion concept

4. **[LeetCode 971] Flip Binary Tree To Match Preorder (Medium)**
   - Flip children to match given preorder traversal
   - Uses selective inversion

### Pattern Variations

- **Mirror tree comparison:** Check if tree is symmetric
- **Partial inversion:** Invert only subtrees meeting criteria
- **Conditional swapping:** Swap based on node values
- **N-ary tree inversion:** Reverse order of all children

---

## Interview Tips

### What Interviewers Look For

1. **Multiple approaches:**
   - Know both recursive and iterative
   - Understand trade-offs

2. **Code clarity:**
   - Clean, readable solution
   - Proper variable naming

3. **Edge case handling:**
   - Empty tree
   - Single node
   - Skewed trees

4. **In-place modification:**
   - Understanding mutation vs creation
   - Memory efficiency

### Discussion Points

**Interviewer:** "Can you do this without recursion?"
**You:** "Yes, I can use an iterative approach with a stack or queue. The logic is the same - swap children of each node."

**Interviewer:** "What's the space complexity?"
**You:** "Recursive uses O(h) for call stack. Iterative with stack is also O(h). BFS with queue is O(w). Morris traversal can achieve O(1) but is more complex."

**Interviewer:** "Can you invert just a subtree?"
**You:** "Yes, just call invertTree on that subtree's root instead of the main root."

**Interviewer:** "How would you verify the inversion is correct?"
**You:** "Invert twice should give original tree, or manually check some paths are mirrored."

### Follow-up Questions

1. **"Check if a tree is symmetric"**
   - Compare tree with its inversion, or use two-pointer approach

2. **"Invert only nodes with even values"**
   - Add condition before swapping

3. **"Count swaps needed"**
   - Count number of nodes with children

4. **"Invert every other level"**
   - Track level during traversal, swap conditionally

---

## Real-World Applications

### Practical Uses

1. **Image Processing:**
   - Mirror/flip images (represented as quad-trees)
   - Horizontal/vertical flips

2. **Game Development:**
   - Flip game boards or levels
   - Mirror character sprites

3. **Computer Graphics:**
   - Scene graph transformations
   - Reflection operations

4. **Data Structure Operations:**
   - Transform hierarchical data
   - Swap subtrees in expression trees

### Historical Context

This problem became famous when Max Howell (creator of Homebrew) was asked to solve it during a Google interview. He couldn't solve it on the whiteboard and was rejected, leading to his viral tweet:

> "Google: 90% of our engineers use the software you wrote (Homebrew), but you can't invert a binary tree on a whiteboard so f*** off."

This sparked debate about interview practices and what skills actually matter.

---

## Key Takeaways

1. **Simple Operation:** Just swap left and right children at each node

2. **Any Traversal Works:** DFS (pre/in/post-order) and BFS all work

3. **In-Place Transformation:** Modify existing tree structure

4. **Recursive is Cleanest:**
   ```python
   if root:
       root.left, root.right = root.right, root.left
       invertTree(root.left)
       invertTree(root.right)
   return root
   ```

5. **Foundation Problem:** Builds intuition for tree transformations

---

## Additional Resources

### Visual Learning
- **Visualgo:** visualgo.net/en/bst (tree operations)
- **Interactive Demo:** algorithm-visualizer.org

### Related Concepts
- Tree traversals
- Tree transformations
- In-place algorithms
- Recursion patterns

### Practice Progression
1. Start with: LeetCode 100, 101 (tree comparison)
2. Then try: LeetCode 951 (flip equivalent)
3. Advanced: LeetCode 114 (Flatten to Linked List)

---

## Summary

**Problem:** Invert/mirror a binary tree

**Best Solution:** Recursive DFS
```python
def invertTree(self, root):
    if not root:
        return None
    root.left, root.right = root.right, root.left
    self.invertTree(root.left)
    self.invertTree(root.right)
    return root
```

**Complexity:** O(n) time, O(h) space

**Key Insight:** Swap children at each node using any traversal

**When to Use:** Foundation for tree transformation problems; practice for recursion!
