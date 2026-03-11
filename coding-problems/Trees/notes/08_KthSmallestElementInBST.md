# Kth Smallest Element in BST

**LeetCode Problem #230 (Medium)**

## Problem Statement

Given the root of a binary search tree, and an integer `k`, return the `kth` smallest value (**1-indexed**) of all the values of the nodes in the tree.

### Examples

**Example 1:**
```
Input: root = [3,1,4,null,2], k = 1
        3
       / \
      1   4
       \
        2
Output: 1
```

**Example 2:**
```
Input: root = [5,3,6,2,4,null,null,1], k = 3
           5
          / \
         3   6
        / \
       2   4
      /
     1
Output: 3
Explanation: Inorder traversal: [1, 2, 3, 4, 5, 6]
3rd smallest is 3
```

### Constraints
- The number of nodes in the tree is `n`
- `1 <= k <= n <= 10^4`
- `0 <= Node.val <= 10^4`

**Follow-up:** If the BST is modified often (i.e., we can do insert and delete operations) and you need to find the kth smallest frequently, how would you optimize?

---

## Pattern Recognition

This is an **Inorder Traversal of BST** problem.

**Key Characteristics:**
1. BST property: inorder traversal gives sorted sequence
2. Need kth element from sorted order
3. Can stop early after finding kth element
4. Natural fit for inorder traversal

**Why This Pattern?**
- BST inorder = sorted order
- "Kth smallest" = kth element in sorted sequence
- Don't need to sort - BST structure provides order
- Can optimize with early termination

**Pattern Recognition:**
- "BST" + "smallest/largest" → Inorder traversal
- "Kth element" → Count during traversal
- "Ordered sequence" → Leverage BST property

---

## Solution Approaches

### Approach 1: Inorder Traversal with Counter (Recursive)

**Intuition:**
Perform inorder traversal (left → root → right), count elements until we reach the kth.

**Algorithm:**
1. Do inorder traversal
2. Maintain counter
3. When counter reaches k, save result
4. Return result

```python
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def kthSmallest(self, root: Optional[TreeNode], k: int) -> int:
        self.count = 0
        self.result = None
        
        def inorder(node: Optional[TreeNode]) -> None:
            if not node or self.result is not None:
                return
            
            # Traverse left subtree
            inorder(node.left)
            
            # Process current node
            self.count += 1
            if self.count == k:
                self.result = node.val
                return
            
            # Traverse right subtree
            inorder(node.right)
        
        inorder(root)
        return self.result
```

**Complexity Analysis:**
- **Time Complexity:** O(h + k) where h is height
  - Best case: O(k) if tree is balanced and k is small
  - Worst case: O(n) if k = n or tree is skewed
- **Space Complexity:** O(h) for recursion stack
  - Best case: O(log n) for balanced tree
  - Worst case: O(n) for skewed tree

---

### Approach 2: Inorder Traversal - Collect All Elements

**Intuition:**
Collect all elements via inorder traversal, then return kth element.

```python
class Solution:
    def kthSmallest(self, root: Optional[TreeNode], k: int) -> int:
        def inorder(node: Optional[TreeNode]) -> List[int]:
            if not node:
                return []
            return inorder(node.left) + [node.val] + inorder(node.right)
        
        sorted_vals = inorder(root)
        return sorted_vals[k - 1]  # k is 1-indexed
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visit all nodes
- **Space Complexity:** O(n) - Store all values

**Note:** Less efficient than Approach 1 (no early termination).

---

### Approach 3: Iterative Inorder with Stack

**Intuition:**
Iterative version using explicit stack for inorder traversal.

```python
class Solution:
    def kthSmallest(self, root: Optional[TreeNode], k: int) -> int:
        stack = []
        current = root
        count = 0
        
        while current or stack:
            # Go to leftmost node
            while current:
                stack.append(current)
                current = current.left
            
            # Process node
            current = stack.pop()
            count += 1
            
            if count == k:
                return current.val
            
            # Move to right subtree
            current = current.right
        
        return -1  # Should never reach here given constraints
```

**Complexity Analysis:**
- **Time Complexity:** O(h + k)
- **Space Complexity:** O(h) for stack

---

### Approach 4: Morris Traversal (Optimal Space)

**Intuition:**
Use Morris traversal for O(1) extra space inorder traversal.

```python
class Solution:
    def kthSmallest(self, root: Optional[TreeNode], k: int) -> int:
        current = root
        count = 0
        
        while current:
            if not current.left:
                # Process current node
                count += 1
                if count == k:
                    return current.val
                current = current.right
            else:
                # Find inorder predecessor
                predecessor = current.left
                while predecessor.right and predecessor.right != current:
                    predecessor = predecessor.right
                
                if not predecessor.right:
                    # Create thread
                    predecessor.right = current
                    current = current.left
                else:
                    # Remove thread and process
                    predecessor.right = None
                    count += 1
                    if count == k:
                        return current.val
                    current = current.right
        
        return -1
```

**Complexity Analysis:**
- **Time Complexity:** O(h + k)
- **Space Complexity:** O(1) - No extra space!

**Note:** More complex but optimal space.

---

### Approach 5: Augmented BST (For Multiple Queries)

**Intuition:**
For frequent queries, augment each node with size of its subtree.

```python
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right
        self.left_count = 0  # Number of nodes in left subtree

class Solution:
    def kthSmallest(self, root: Optional[TreeNode], k: int) -> int:
        # First, compute left_count for all nodes (one-time preprocessing)
        def count_nodes(node: Optional[TreeNode]) -> int:
            if not node:
                return 0
            
            left_size = count_nodes(node.left)
            right_size = count_nodes(node.right)
            node.left_count = left_size
            
            return left_size + right_size + 1
        
        count_nodes(root)
        
        # Now find kth smallest using left_count
        def find_kth(node: Optional[TreeNode], k: int) -> int:
            if node.left_count == k - 1:
                # Current node is the kth smallest
                return node.val
            elif node.left_count >= k:
                # kth is in left subtree
                return find_kth(node.left, k)
            else:
                # kth is in right subtree
                # Adjust k: remove left subtree + current node
                return find_kth(node.right, k - node.left_count - 1)
        
        return find_kth(root, k)
```

**Complexity Analysis:**
- **Preprocessing:** O(n) to compute left_count
- **Query Time:** O(h) per query
  - Best case: O(log n)
  - Worst case: O(n)
- **Space Complexity:** O(n) for left_count + O(h) for recursion

**When to use:** When tree is modified frequently and queries are frequent.

---

### Approach 6: Binary Search on BST (Alternative)

**Intuition:**
Use BST property to binary search for kth element.

```python
class Solution:
    def kthSmallest(self, root: Optional[TreeNode], k: int) -> int:
        def count_nodes(node: Optional[TreeNode]) -> int:
            if not node:
                return 0
            return 1 + count_nodes(node.left) + count_nodes(node.right)
        
        left_count = count_nodes(root.left)
        
        if k <= left_count:
            # kth is in left subtree
            return self.kthSmallest(root.left, k)
        elif k == left_count + 1:
            # Current node is kth
            return root.val
        else:
            # kth is in right subtree
            return self.kthSmallest(root.right, k - left_count - 1)
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Need to count nodes at each level
- **Space Complexity:** O(h)

**Note:** Less efficient than inorder approach due to repeated counting.

---

## Detailed Walkthrough

Let's trace through **Approach 1** with Example 2:

```
Tree:      5
          / \
         3   6
        / \
       2   4
      /
     1

k = 3
```

**Inorder Traversal Flow:**

```
inorder(5):
  └─ inorder(3):  // left of 5
       └─ inorder(2):  // left of 3
            └─ inorder(1):  // left of 2
                 └─ inorder(None):  // left of 1 → return
                 └─ Process 1: count = 1, result = None
                 └─ inorder(None):  // right of 1 → return
            └─ Process 2: count = 2, result = None
            └─ inorder(None):  // right of 2 → return
       └─ Process 3: count = 3, result = 3 ✓ FOUND!
       └─ Early return (result is not None)
  └─ Early return (result is not None)

Return 3
```

**Sequence of processing:**
1. Go left as far as possible → reach node 1
2. Process 1 (count = 1)
3. Backtrack to 2, process 2 (count = 2)
4. Backtrack to 3, process 3 (count = 3) → Found!
5. Early termination

**Why this works:**
- Inorder traversal visits nodes in sorted order: 1, 2, 3, 4, 5, 6
- 3rd element (k=3) is 3
- Early termination after finding kth

---

## Edge Cases

### 1. k = 1 (Smallest Element)
```python
Input: root = [3,1,4], k = 1
        3
       / \
      1   4
Output: 1 (leftmost node)
```

### 2. k = n (Largest Element)
```python
Input: root = [3,1,4], k = 3
        3
       / \
      1   4
Output: 4 (rightmost node)
```

### 3. Single Node Tree
```python
Input: root = [1], k = 1
        1
Output: 1
```

### 4. Left-Skewed Tree
```python
Input: root = [5,4,null,3,null,2,null,1], k = 3
        5
       /
      4
     /
    3
   /
  2
 /
1
Output: 3
Inorder: [1, 2, 3, 4, 5]
```

### 5. Right-Skewed Tree
```python
Input: root = [1,null,2,null,3,null,4,null,5], k = 3
        1
         \
          2
           \
            3
             \
              4
               \
                5
Output: 3
Inorder: [1, 2, 3, 4, 5]
```

### 6. Balanced Tree
```python
Input: root = [4,2,6,1,3,5,7], k = 4
           4
         /   \
        2     6
       / \   / \
      1   3 5   7
Output: 4 (root itself)
Inorder: [1, 2, 3, 4, 5, 6, 7]
```

### 7. Large k
```python
Input: tree with 10000 nodes, k = 9999
Output: Second largest element
```

---

## Common Mistakes

### 1. **Using 0-indexing Instead of 1-indexing**
```python
# WRONG - k is 1-indexed!
sorted_vals = inorder(root)
return sorted_vals[k]  # Should be k-1
```

### 2. **Not Handling Early Termination**
```python
# INEFFICIENT - Visits all nodes even after finding kth
def inorder(node):
    if not node:
        return []
    return inorder(node.left) + [node.val] + inorder(node.right)
```
**Fix:** Use counter and stop when k is reached

### 3. **Incorrect Inorder Implementation**
```python
# WRONG - This is preorder, not inorder!
def inorder(node):
    result.append(node.val)  # Process before left
    inorder(node.left)
    inorder(node.right)
```

### 4. **Not Using BST Property**
```python
# INEFFICIENT - Sorting defeats purpose of BST
def kthSmallest(self, root, k):
    values = []
    # ... collect all values ...
    values.sort()  # Unnecessary! BST already ordered
    return values[k-1]
```

### 5. **Forgetting to Check Result in Recursion**
```python
# WRONG - Continues after finding result
def inorder(node):
    if not node:  # Should also check if result found
        return
    inorder(node.left)
    # ... process ...
    inorder(node.right)  # Continues even if result found
```

### 6. **Incorrect Count Update in Binary Search Approach**
```python
# WRONG - Doesn't adjust k properly
if k > left_count:
    return self.kthSmallest(root.right, k)  # Should be k - left_count - 1
```

---

## Testing

### Test Cases

```python
def test_kth_smallest():
    solution = Solution()
    
    # Test 1: Example 1
    root1 = TreeNode(3)
    root1.left = TreeNode(1)
    root1.right = TreeNode(4)
    root1.left.right = TreeNode(2)
    assert solution.kthSmallest(root1, 1) == 1
    
    # Test 2: Example 2
    root2 = TreeNode(5)
    root2.left = TreeNode(3)
    root2.right = TreeNode(6)
    root2.left.left = TreeNode(2)
    root2.left.right = TreeNode(4)
    root2.left.left.left = TreeNode(1)
    assert solution.kthSmallest(root2, 3) == 3
    
    # Test 3: Single node
    root3 = TreeNode(1)
    assert solution.kthSmallest(root3, 1) == 1
    
    # Test 4: k = n (largest)
    root4 = TreeNode(2)
    root4.left = TreeNode(1)
    root4.right = TreeNode(3)
    assert solution.kthSmallest(root4, 3) == 3
    
    # Test 5: Left-skewed
    root5 = TreeNode(3)
    root5.left = TreeNode(2)
    root5.left.left = TreeNode(1)
    assert solution.kthSmallest(root5, 2) == 2
    
    # Test 6: Right-skewed
    root6 = TreeNode(1)
    root6.right = TreeNode(2)
    root6.right.right = TreeNode(3)
    assert solution.kthSmallest(root6, 2) == 2
    
    # Test 7: Balanced tree - middle element
    root7 = TreeNode(4)
    root7.left = TreeNode(2)
    root7.right = TreeNode(6)
    root7.left.left = TreeNode(1)
    root7.left.right = TreeNode(3)
    root7.right.left = TreeNode(5)
    root7.right.right = TreeNode(7)
    assert solution.kthSmallest(root7, 4) == 4
    
    print("All test cases passed!")

test_kth_smallest()
```

---

## Optimization Notes

### Time Optimization

**Early Termination:**
- Stop traversal after finding kth element
- Don't visit unnecessary nodes
- Best case: O(k) when kth is in leftmost path

**Approach Comparison:**
- **Inorder with counter:** O(h + k) - Optimal
- **Collect all:** O(n) - No early termination
- **Morris:** O(h + k) - Same as inorder but O(1) space
- **Augmented BST:** O(h) per query after O(n) preprocessing

### Space Optimization

**Space Usage:**
- **Recursive inorder:** O(h) - Call stack
- **Iterative inorder:** O(h) - Explicit stack
- **Morris traversal:** O(1) - No extra space! ✓ Best
- **Collect all:** O(n) - Store all values (worst)

**For interviews:** Iterative or recursive inorder is usually preferred (clearer than Morris).

---

## Related Problems

### Similar Problems

1. **[LeetCode 671] Second Minimum Node in Binary Tree (Easy)**
   - Find specific ordered element
   - Similar traversal pattern

2. **[LeetCode 285] Inorder Successor in BST (Medium)**
   - Find next element in inorder
   - Uses inorder traversal

3. **[LeetCode 173] Binary Search Tree Iterator (Medium)**
   - Implement inorder traversal iterator
   - Similar stack-based approach

4. **[LeetCode 530] Minimum Absolute Difference in BST (Easy)**
   - Use inorder traversal
   - Process consecutive elements

5. **[LeetCode 783] Minimum Distance Between BST Nodes (Easy)**
   - Similar to #530
   - Inorder traversal

### Pattern Variations

- **Kth largest:** Reverse inorder (right → root → left)
- **Range sum in BST:** Inorder with condition
- **Find median:** Find middle element(s)
- **Closest value:** Inorder with distance tracking

---

## Interview Tips

### What Interviewers Look For

1. **Understanding BST property:**
   - Inorder gives sorted sequence
   - Leveraging this for efficiency

2. **Early termination:**
   - Not visiting all nodes when unnecessary
   - Optimizing common cases

3. **Multiple approaches:**
   - Recursive and iterative
   - Understanding trade-offs

4. **Follow-up handling:**
   - Augmented BST for multiple queries
   - Balancing preprocessing vs query time

### Discussion Points

**Interviewer:** "What's the time complexity?"
**You:** "O(h + k) where h is height and k is the parameter. In best case (balanced tree with small k), this is O(log n + k). Worst case is O(n) for skewed tree or when k = n."

**Interviewer:** "Can you optimize space?"
**You:** "Yes, I can use Morris traversal for O(1) extra space, but it's more complex. For most practical cases, the iterative approach with O(h) stack is preferred."

**Interviewer:** "What if we need to call this function many times with different k values?"
**You:** "I'd augment the BST with subtree sizes. This takes O(n) preprocessing but then each query is O(h), which is much better for frequent queries."

**Interviewer:** "How would you find the kth largest instead?"
**You:** "Use reverse inorder traversal (right → root → left) instead of normal inorder."

### Follow-up Questions

1. **"Find kth largest"**
   - Reverse inorder: right → root → left

2. **"Find median of BST"**
   - Find middle element(s): n/2 and (n/2)+1 for even n

3. **"Support insert/delete and frequent kth queries"**
   - Use augmented BST with subtree sizes

4. **"Find closest k values to target"**
   - Inorder traversal with distance calculation

---

## Key Takeaways

1. **BST Inorder = Sorted:** Key insight for this problem

2. **Early Termination:** Stop after finding kth element

3. **Best Approach for Interviews:**
   ```python
   def kthSmallest(self, root, k):
       stack, current, count = [], root, 0
       while current or stack:
           while current:
               stack.append(current)
               current = current.left
           current = stack.pop()
           count += 1
           if count == k:
               return current.val
           current = current.right
   ```

4. **For Multiple Queries:** Use augmented BST with subtree sizes

5. **Remember 1-indexing:** k=1 is smallest, not k=0

---

## Additional Resources

### Visual Learning
- **BST Visualization:** visualgo.net/en/bst
- **Inorder Traversal:** algorithm-visualizer.org

### Related Concepts
- Inorder traversal
- BST properties
- Tree augmentation
- Morris traversal

### Practice Progression
1. Master: LeetCode 94 (Inorder Traversal)
2. Then try: This problem (230)
3. Advanced: LeetCode 173 (BST Iterator), 285 (Inorder Successor)

---

## Summary

**Problem:** Find kth smallest element in BST

**Best Solution:** Iterative Inorder with Counter
```python
def kthSmallest(self, root, k):
    stack, current, count = [], root, 0
    
    while current or stack:
        while current:
            stack.append(current)
            current = current.left
        
        current = stack.pop()
        count += 1
        if count == k:
            return current.val
        current = current.right
```

**Complexity:** O(h + k) time, O(h) space

**Key Insight:** Inorder traversal of BST produces sorted sequence; count to find kth

**When to Use:** Finding ordered elements in BST; leverage BST property for efficiency!
