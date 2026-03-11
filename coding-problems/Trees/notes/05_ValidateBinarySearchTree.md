# Validate Binary Search Tree

**LeetCode Problem #98 (Medium)**

## Problem Statement

Given the root of a binary tree, determine if it is a valid binary search tree (BST).

A valid BST is defined as follows:
- The left subtree of a node contains only nodes with keys **less than** the node's key
- The right subtree of a node contains only nodes with keys **greater than** the node's key
- Both the left and right subtrees must also be binary search trees

### Examples

**Example 1:**
```
Input: root = [2,1,3]
        2
       / \
      1   3
Output: true
```

**Example 2:**
```
Input: root = [5,1,4,null,null,3,6]
        5
       / \
      1   4
         / \
        3   6
Output: false
Explanation: The root node's value is 5 but its right child's value is 4.
```

**Example 3:**
```
Input: root = [5,4,6,null,null,3,7]
         5
        / \
       4   6
          / \
         3   7
Output: false
Explanation: Node with value 3 is in right subtree of 5, violating BST property.
```

### Constraints
- The number of nodes in the tree is in the range `[1, 10^4]`
- `-2^31 <= Node.val <= 2^31 - 1`

---

## Pattern Recognition

This is a **Tree DFS with Range Validation** problem.

**Key Characteristics:**
1. Need to validate property at every node
2. Each node has valid range constraints from ancestors
3. Range narrows as we go deeper in tree
4. Must check global property, not just local parent-child

**Why This Pattern?**
- BST property is relative to ALL ancestors, not just parent
- Each node must fall within a valid range
- Range propagates down: left child < parent, right child > parent
- Classic range-based validation problem

**Pattern Recognition:**
- "Valid BST" → Range validation (not just parent check)
- "All ancestors matter" → Pass range down recursively
- "Subtree constraint" → Recursive validation

---

## Solution Approaches

### Approach 1: DFS with Range Validation (Optimal)

**Intuition:**
Each node must be within a valid range [min, max]. As we traverse:
- Going left: update max to current node's value
- Going right: update min to current node's value

**Algorithm:**
1. Start with range (-∞, +∞)
2. For each node, check if value is within valid range
3. Recursively validate left subtree with updated max
4. Recursively validate right subtree with updated min

```python
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def isValidBST(self, root: Optional[TreeNode]) -> bool:
        def validate(node: Optional[TreeNode], min_val: float, max_val: float) -> bool:
            # Empty tree is valid BST
            if not node:
                return True
            
            # Check if current node violates range
            if node.val <= min_val or node.val >= max_val:
                return False
            
            # Validate left subtree: all values must be < node.val
            # Validate right subtree: all values must be > node.val
            return (validate(node.left, min_val, node.val) and
                    validate(node.right, node.val, max_val))
        
        return validate(root, float('-inf'), float('inf'))
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visit each node once
- **Space Complexity:** O(h) - Recursion stack depth
  - Best case (balanced): O(log n)
  - Worst case (skewed): O(n)

---

### Approach 2: Inorder Traversal (Using BST Property)

**Intuition:**
Inorder traversal of a BST produces sorted sequence. Validate that sequence is strictly increasing.

**Algorithm:**
1. Perform inorder traversal (left → root → right)
2. Track previous node value
3. Check that current value > previous value
4. If any violation found, return False

```python
class Solution:
    def isValidBST(self, root: Optional[TreeNode]) -> bool:
        self.prev = None
        
        def inorder(node: Optional[TreeNode]) -> bool:
            if not node:
                return True
            
            # Check left subtree
            if not inorder(node.left):
                return False
            
            # Check current node
            if self.prev is not None and node.val <= self.prev:
                return False
            
            self.prev = node.val
            
            # Check right subtree
            return inorder(node.right)
        
        return inorder(root)
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(h)

---

### Approach 3: Inorder Traversal with List

**Intuition:**
Collect inorder traversal in list, then check if sorted.

```python
class Solution:
    def isValidBST(self, root: Optional[TreeNode]) -> bool:
        def inorder(node: Optional[TreeNode]) -> List[int]:
            if not node:
                return []
            return inorder(node.left) + [node.val] + inorder(node.right)
        
        values = inorder(root)
        
        # Check if strictly increasing
        for i in range(1, len(values)):
            if values[i] <= values[i-1]:
                return False
        
        return True
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(n) - Store all values

**Note:** Less efficient than Approach 2 due to extra space.

---

### Approach 4: Iterative Inorder Traversal

**Intuition:**
Iterative version using explicit stack.

```python
class Solution:
    def isValidBST(self, root: Optional[TreeNode]) -> bool:
        stack = []
        prev = None
        current = root
        
        while stack or current:
            # Go to leftmost node
            while current:
                stack.append(current)
                current = current.left
            
            # Process node
            current = stack.pop()
            
            # Check BST property
            if prev is not None and current.val <= prev:
                return False
            
            prev = current.val
            
            # Move to right subtree
            current = current.right
        
        return True
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(h)

---

### Approach 5: Iterative Range Validation (Stack)

**Intuition:**
Iterative version of Approach 1 using explicit stack.

```python
class Solution:
    def isValidBST(self, root: Optional[TreeNode]) -> bool:
        if not root:
            return True
        
        # Stack stores (node, min_val, max_val)
        stack = [(root, float('-inf'), float('inf'))]
        
        while stack:
            node, min_val, max_val = stack.pop()
            
            if not node:
                continue
            
            # Check range constraint
            if node.val <= min_val or node.val >= max_val:
                return False
            
            # Add children with updated ranges
            stack.append((node.left, min_val, node.val))
            stack.append((node.right, node.val, max_val))
        
        return True
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(h)

---

## Detailed Walkthrough

Let's trace through **Approach 1** with Example 2 (invalid BST):

```
Tree:     5
         / \
        1   4
           / \
          3   6

This is INVALID because 4 < 5 (right child should be > parent)
```

**Execution Flow:**

```
validate(5, -∞, +∞):
  └─ 5 in range (-∞, +∞)? YES ✓
  └─ Check left: validate(1, -∞, 5)
       └─ 1 in range (-∞, 5)? YES ✓
       └─ Check left: validate(None, -∞, 1) → True
       └─ Check right: validate(None, 1, 5) → True
       └─ Return True ✓
  
  └─ Check right: validate(4, 5, +∞)
       └─ 4 in range (5, +∞)? NO! 4 <= 5 ✗
       └─ Return False
  
  └─ Return False (right subtree invalid)
```

**Why does this catch the error?**
- Node 4 is right child of 5
- All nodes in right subtree must be > 5
- But 4 <= 5, violating BST property
- Range validation catches this immediately

**Example with valid BST:**
```
Tree:     2
         / \
        1   3

validate(2, -∞, +∞):
  └─ 2 in range (-∞, +∞)? YES ✓
  └─ validate(1, -∞, 2):
       └─ 1 in range (-∞, 2)? YES ✓
       └─ Return True
  └─ validate(3, 2, +∞):
       └─ 3 in range (2, +∞)? YES ✓
       └─ Return True
  └─ Return True ✓
```

---

## Edge Cases

### 1. Single Node
```python
Input: root = [1]
        1
Output: True
```
- Single node is always valid BST

### 2. Two Nodes - Valid
```python
Input: root = [2,1]
        2
       /
      1
Output: True
```

### 3. Two Nodes - Invalid
```python
Input: root = [1,2]
        1
       /
      2
Output: False (left child should be < parent)
```

### 4. Duplicate Values
```python
Input: root = [2,2,2]
        2
       / \
      2   2
Output: False (BST requires strictly < and >)
```

### 5. Valid BST with Negative Numbers
```python
Input: root = [0,-1,1]
         0
        / \
      -1   1
Output: True
```

### 6. Tricky Case - Looks Valid Locally
```python
Input: root = [5,1,4,null,null,3,6]
         5
        / \
       1   4
          / \
         3   6
Output: False
Explanation: 3 is in right subtree of 5, but 3 < 5
```
- This is why we need range validation!
- Checking only parent-child would miss this

### 7. Integer Overflow Edge Case
```python
Input: root = [2147483647]  # Max integer value
Output: True
```
- Use float('inf') to handle boundary values

### 8. Left-Skewed BST
```python
Input: root = [5,4,null,3,null,2,null,1]
        5
       /
      4
     /
    3
   /
  2
 /
1
Output: True (all satisfy BST property)
```

---

## Common Mistakes

### 1. **Only Checking Parent-Child Relationship**
```python
# WRONG - Doesn't check global constraint
def isValidBST(self, root):
    if not root:
        return True
    
    if root.left and root.left.val >= root.val:
        return False
    if root.right and root.right.val <= root.val:
        return False
    
    return self.isValidBST(root.left) and self.isValidBST(root.right)
```
**Issue:** Misses cases like node 3 in right subtree of 5

### 2. **Using Integer Min/Max Instead of Infinity**
```python
# PROBLEM - What if tree contains Integer.MIN_VALUE?
def validate(node, min_val, max_val):
    # If min_val = -2^31 and node.val = -2^31, should be invalid
    # But comparison fails
    if node.val <= min_val or node.val >= max_val:
        return False
```
**Fix:** Use `float('-inf')` and `float('inf')`

### 3. **Allowing Equal Values**
```python
# WRONG - BST requires strict inequality
if node.val < min_val or node.val > max_val:  # Should be <= and >=
    return False
```

### 4. **Incorrect Inorder Comparison**
```python
# WRONG - Should be strictly greater
if self.prev is not None and node.val < self.prev:  # Should be <=
    return False
```

### 5. **Not Handling None in Inorder**
```python
# WRONG - Will crash on first node
def inorder(node):
    # ...
    if node.val <= self.prev:  # self.prev is None initially!
        return False
```
**Fix:** Check `if self.prev is not None`

### 6. **Range Update Error**
```python
# WRONG - Swapped ranges
return (validate(node.left, min_val, max_val) and  # max should be node.val
        validate(node.right, min_val, max_val))    # min should be node.val
```

---

## Testing

### Test Cases

```python
def test_valid_bst():
    solution = Solution()
    
    # Test 1: Valid BST - Example 1
    root1 = TreeNode(2)
    root1.left = TreeNode(1)
    root1.right = TreeNode(3)
    assert solution.isValidBST(root1) == True
    
    # Test 2: Invalid BST - Example 2
    root2 = TreeNode(5)
    root2.left = TreeNode(1)
    root2.right = TreeNode(4)
    root2.right.left = TreeNode(3)
    root2.right.right = TreeNode(6)
    assert solution.isValidBST(root2) == False
    
    # Test 3: Single node
    root3 = TreeNode(1)
    assert solution.isValidBST(root3) == True
    
    # Test 4: Duplicate values
    root4 = TreeNode(2)
    root4.left = TreeNode(2)
    assert solution.isValidBST(root4) == False
    
    # Test 5: All left children
    root5 = TreeNode(5)
    root5.left = TreeNode(4)
    root5.left.left = TreeNode(3)
    root5.left.left.left = TreeNode(2)
    assert solution.isValidBST(root5) == True
    
    # Test 6: All right children
    root6 = TreeNode(1)
    root6.right = TreeNode(2)
    root6.right.right = TreeNode(3)
    root6.right.right.right = TreeNode(4)
    assert solution.isValidBST(root6) == True
    
    # Test 7: Tricky case
    root7 = TreeNode(5)
    root7.left = TreeNode(1)
    root7.right = TreeNode(4)
    root7.right.left = TreeNode(3)
    root7.right.right = TreeNode(6)
    assert solution.isValidBST(root7) == False
    
    # Test 8: With negative numbers
    root8 = TreeNode(0)
    root8.left = TreeNode(-1)
    root8.right = TreeNode(1)
    assert solution.isValidBST(root8) == True
    
    # Test 9: Maximum integer value
    root9 = TreeNode(2147483647)
    assert solution.isValidBST(root9) == True
    
    # Test 10: Invalid - left child greater
    root10 = TreeNode(1)
    root10.left = TreeNode(2)
    assert solution.isValidBST(root10) == False
    
    print("All test cases passed!")

test_valid_bst()
```

---

## Optimization Notes

### Space Optimization

**Approach Comparison:**
- **Range validation (recursive):** O(h) - Just recursion stack
- **Inorder with prev:** O(h) - Recursion stack only
- **Inorder with list:** O(n) - Stores all values (worst)
- **Iterative versions:** O(h) - Explicit stack

**Best:** Range validation or inorder with prev (both O(h))

### Time Optimization

- All approaches are O(n) worst case
- **Early termination:** Return False as soon as violation found
- **Best case:** O(1) if root violates constraint
- **Average case:** Depends on where violation occurs

### Code Clarity

**Range validation is typically preferred:**
- Most intuitive
- Easy to understand and explain
- Handles all edge cases naturally
- Clean recursive structure

---

## Related Problems

### Similar Problems

1. **[LeetCode 530] Minimum Absolute Difference in BST (Easy)**
   - Use inorder traversal to find min difference
   - Similar inorder pattern

2. **[LeetCode 99] Recover Binary Search Tree (Medium)**
   - Find two swapped nodes in BST
   - Uses inorder traversal

3. **[LeetCode 235] Lowest Common Ancestor of BST (Medium)**
   - Uses BST property for efficient search
   - Related BST navigation

4. **[LeetCode 701] Insert into BST (Medium)**
   - Maintain BST property while inserting
   - Uses range-based logic

5. **[LeetCode 450] Delete Node in BST (Medium)**
   - Maintain BST property while deleting
   - More complex BST manipulation

### Pattern Variations

- **Count valid BSTs:** Given n, count possible BSTs (Catalan number)
- **Convert sorted array to BST:** Construct balanced BST
- **BST Iterator:** Implement inorder traversal iterator
- **Kth smallest in BST:** Use inorder traversal

---

## Interview Tips

### What Interviewers Look For

1. **Understanding BST property:**
   - Not just parent-child, but ALL ancestors
   - Strictly less/greater (no equality)

2. **Correct approach:**
   - Range validation is gold standard
   - Inorder traversal is acceptable alternative

3. **Edge cases:**
   - Duplicate values
   - Integer boundaries
   - Single node
   - Skewed trees

4. **Code correctness:**
   - Proper range updates
   - Handling None values
   - Using infinity for bounds

### Discussion Points

**Interviewer:** "Why not just check if left < parent < right?"
**You:** "That only checks immediate children. Consider a node deep in the right subtree - it must be greater than ALL ancestors in the path, not just its parent."

**Interviewer:** "What's the time complexity?"
**You:** "O(n) because we might need to visit every node. We can't determine validity without checking all nodes in the worst case."

**Interviewer:** "Can we do better than O(n)?"
**You:** "No, we must examine every node. Even if we find an invalid node early, worst case is when the entire tree is valid."

**Interviewer:** "What if there are duplicate values?"
**You:** "Standard BST doesn't allow duplicates. If we need to handle them, we'd modify the condition to allow <= for one direction."

### Follow-up Questions

1. **"Allow duplicates on left or right"**
   - Change comparison to `<=` or `>=` for one side

2. **"Return the invalid node"**
   - Return node instead of boolean when violation found

3. **"Count violations"**
   - Continue traversal, increment counter

4. **"Fix the BST"**
   - Identify swapped nodes and swap back

---

## Key Takeaways

1. **Range Validation Pattern:**
   ```python
   def validate(node, min_val, max_val):
       if not node:
           return True
       if node.val <= min_val or node.val >= max_val:
           return False
       return (validate(node.left, min_val, node.val) and
               validate(node.right, node.val, max_val))
   ```

2. **Global Property:** BST property is relative to ALL ancestors, not just parent

3. **Use Infinity:** For initial bounds to handle all integer values

4. **Inorder Alternative:** Inorder traversal gives sorted sequence

5. **Strict Inequality:** BST requires strictly < and >, no equality

---

## Additional Resources

### Visual Learning
- **BST Visualization:** visualgo.net/en/bst
- **Validation Animation:** algorithm-visualizer.org

### Related Concepts
- Binary Search Tree properties
- Tree traversals (inorder)
- Range-based validation
- Recursive constraints

### Practice Progression
1. Master this problem first
2. Then try: LeetCode 530 (Min Difference), 700 (Search BST)
3. Advanced: LeetCode 99 (Recover BST), 108 (Sorted Array to BST)

---

## Summary

**Problem:** Validate if tree satisfies BST property

**Best Solution:** DFS with Range Validation
```python
def isValidBST(self, root):
    def validate(node, min_val, max_val):
        if not node:
            return True
        if node.val <= min_val or node.val >= max_val:
            return False
        return (validate(node.left, min_val, node.val) and
                validate(node.right, node.val, max_val))
    
    return validate(root, float('-inf'), float('inf'))
```

**Complexity:** O(n) time, O(h) space

**Key Insight:** Each node must satisfy range constraint from ALL ancestors

**When to Use:** Core BST validation; foundation for BST problems!
