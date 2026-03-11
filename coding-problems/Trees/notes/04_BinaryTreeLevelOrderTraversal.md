# Binary Tree Level Order Traversal

**LeetCode Problem #102 (Medium)**

## Problem Statement

Given the root of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).

### Examples

**Example 1:**
```
Input: root = [3,9,20,null,null,15,7]
        3
       / \
      9  20
         / \
        15  7

Output: [[3],[9,20],[15,7]]
```

**Example 2:**
```
Input: root = [1]
        1

Output: [[1]]
```

**Example 3:**
```
Input: root = []
Output: []
```

### Constraints
- The number of nodes in the tree is in the range `[0, 2000]`
- `-1000 <= Node.val <= 1000`

---

## Pattern Recognition

This is a **Tree BFS (Breadth-First Search)** problem using a queue.

**Key Characteristics:**
1. Need to process nodes level by level
2. Group nodes by their depth
3. Natural fit for queue data structure
4. Process all nodes at current level before moving to next

**Why This Pattern?**
- "Level by level" → BFS with queue
- Need to group by level → Track level size
- Left to right order → Natural queue order
- Layer-wise processing → Queue maintains order

**Pattern Recognition:**
- "Level order" → BFS
- "Layer by layer" → Queue with level tracking
- "Group by depth" → Count nodes per level

---

## Solution Approaches

### Approach 1: BFS with Queue (Classic Approach)

**Intuition:**
Use a queue to process nodes level by level. Track the size of current level to know when level ends.

**Algorithm:**
1. Initialize queue with root
2. While queue not empty:
   - Get current level size
   - Process all nodes in current level
   - Collect values in current level list
   - Add children to queue for next level
3. Return result

```python
# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

from collections import deque

class Solution:
    def levelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:
        if not root:
            return []
        
        result = []
        queue = deque([root])
        
        while queue:
            level_size = len(queue)
            current_level = []
            
            # Process all nodes at current level
            for _ in range(level_size):
                node = queue.popleft()
                current_level.append(node.val)
                
                # Add children for next level
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            
            result.append(current_level)
        
        return result
```

**Complexity Analysis:**
- **Time Complexity:** O(n) - Visit each node exactly once
- **Space Complexity:** O(w) where w is maximum width of tree
  - Queue holds at most one complete level
  - Worst case: O(n/2) = O(n) for complete binary tree (last level)

---

### Approach 2: BFS with Sentinel/Marker

**Intuition:**
Use a special marker (None) to denote end of level.

**Algorithm:**
1. Add root and None (level marker) to queue
2. Process nodes until marker
3. When marker reached, add another marker if queue not empty
4. Continue until queue empty

```python
from collections import deque

class Solution:
    def levelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:
        if not root:
            return []
        
        result = []
        queue = deque([root, None])  # None marks end of level
        current_level = []
        
        while queue:
            node = queue.popleft()
            
            if node is None:
                # End of current level
                result.append(current_level)
                current_level = []
                
                # Add marker for next level if queue not empty
                if queue:
                    queue.append(None)
            else:
                current_level.append(node.val)
                
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
        
        return result
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(w)

---

### Approach 3: BFS with Level Tracking

**Intuition:**
Store (node, level) pairs in queue. Group by level in result.

```python
from collections import deque, defaultdict

class Solution:
    def levelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:
        if not root:
            return []
        
        level_map = defaultdict(list)
        queue = deque([(root, 0)])  # (node, level)
        
        while queue:
            node, level = queue.popleft()
            level_map[level].append(node.val)
            
            if node.left:
                queue.append((node.left, level + 1))
            if node.right:
                queue.append((node.right, level + 1))
        
        # Convert dict to list of lists
        return [level_map[i] for i in range(len(level_map))]
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(n) - Dictionary stores all nodes

---

### Approach 4: DFS with Level Tracking (Recursive)

**Intuition:**
Use DFS but track level. Add nodes to appropriate level list.

**Algorithm:**
1. Pass result list and current level to recursive function
2. If level list doesn't exist, create it
3. Add current node to its level
4. Recursively process children with level + 1

```python
class Solution:
    def levelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:
        result = []
        
        def dfs(node: Optional[TreeNode], level: int) -> None:
            if not node:
                return
            
            # Create new level list if needed
            if level >= len(result):
                result.append([])
            
            # Add current node to its level
            result[level].append(node.val)
            
            # Recurse on children
            dfs(node.left, level + 1)
            dfs(node.right, level + 1)
        
        dfs(root, 0)
        return result
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(h) for recursion stack + O(n) for result
  - Total: O(n)

---

### Approach 5: Two Queues (Explicit Level Separation)

**Intuition:**
Use two queues - one for current level, one for next level.

```python
class Solution:
    def levelOrder(self, root: Optional[TreeNode]) -> List[List[int]]:
        if not root:
            return []
        
        result = []
        current_level = [root]
        
        while current_level:
            next_level = []
            level_values = []
            
            for node in current_level:
                level_values.append(node.val)
                
                if node.left:
                    next_level.append(node.left)
                if node.right:
                    next_level.append(node.right)
            
            result.append(level_values)
            current_level = next_level
        
        return result
```

**Complexity Analysis:**
- **Time Complexity:** O(n)
- **Space Complexity:** O(w) - Both queues combined hold at most 2 levels

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
Initial: queue = [3], result = []

Level 0:
  level_size = 1
  current_level = []
  
  Process node 3:
    - Add 3 to current_level → [3]
    - Add left child (9) to queue → queue = [9]
    - Add right child (20) to queue → queue = [9, 20]
  
  Add current_level to result → result = [[3]]

Level 1:
  level_size = 2
  current_level = []
  
  Process node 9:
    - Add 9 to current_level → [9]
    - No children
  
  Process node 20:
    - Add 20 to current_level → [9, 20]
    - Add left child (15) to queue → queue = [15]
    - Add right child (7) to queue → queue = [15, 7]
  
  Add current_level to result → result = [[3], [9,20]]

Level 2:
  level_size = 2
  current_level = []
  
  Process node 15:
    - Add 15 to current_level → [15]
    - No children
  
  Process node 7:
    - Add 7 to current_level → [15, 7]
    - No children
  
  Add current_level to result → result = [[3], [9,20], [15,7]]

Queue empty, return result
```

**Key Points:**
1. `level_size = len(queue)` captures exactly how many nodes in current level
2. Process only `level_size` nodes before moving to next level
3. Children added to queue become next level
4. Each iteration processes one complete level

---

## Edge Cases

### 1. Empty Tree
```python
Input: root = None
Output: []
```
- Handle early with initial check

### 2. Single Node
```python
Input: root = [1]
        1
Output: [[1]]
```
- Single level with one node

### 3. Complete Binary Tree
```python
Input: root = [1,2,3,4,5,6,7]
           1
         /   \
        2     3
       / \   / \
      4   5 6   7
Output: [[1], [2,3], [4,5,6,7]]
```
- Each level doubles in size

### 4. Left-Skewed Tree
```python
Input: root = [1,2,null,3,null,4]
        1
       /
      2
     /
    3
   /
  4
Output: [[1], [2], [3], [4]]
```
- Each level has one node

### 5. Right-Skewed Tree
```python
Input: root = [1,null,2,null,3,null,4]
        1
         \
          2
           \
            3
             \
              4
Output: [[1], [2], [3], [4]]
```
- Similar to left-skewed

### 6. Unbalanced Tree
```python
Input: root = [1,2,3,4,null,null,5]
           1
         /   \
        2     3
       /       \
      4         5
Output: [[1], [2,3], [4,5]]
```
- Level 2 has gaps but same level

### 7. Very Wide Tree
```python
Input: Complete tree with 15 nodes (4 levels)
Output: [[1], [2,3], [4,5,6,7], [8,9,10,11,12,13,14,15]]
```
- Last level has 8 nodes

---

## Common Mistakes

### 1. **Not Capturing Level Size**
```python
# WRONG - Infinite loop, keeps processing children
while queue:
    node = queue.popleft()
    # ... process node ...
    # This doesn't separate levels!
```
**Fix:** Save `level_size = len(queue)` before inner loop

### 2. **Modifying Queue While Iterating**
```python
# WRONG - Can't iterate while modifying
for node in queue:  # Queue size changes during iteration!
    if node.left:
        queue.append(node.left)
    if node.right:
        queue.append(node.right)
```
**Fix:** Use range(level_size) with popleft

### 3. **Forgetting to Create New Level List**
```python
# WRONG - Reuses same list
current_level = []
while queue:
    level_size = len(queue)
    for _ in range(level_size):
        node = queue.popleft()
        current_level.append(node.val)  # Never reset!
```
**Fix:** Create new list for each level

### 4. **Using List as Queue**
```python
# WRONG - O(n) for pop(0)
queue = [root]
while queue:
    node = queue.pop(0)  # Inefficient!
```
**Fix:** Use `collections.deque` for O(1) operations

### 5. **Not Checking for Null Children**
```python
# WRONG - Adds None to queue
queue.append(node.left)   # Might be None
queue.append(node.right)  # Might be None
```
**Fix:** Check if children exist before adding

### 6. **Incorrect DFS Level Tracking**
```python
# WRONG - Might access result[level] before it exists
def dfs(node, level):
    result[level].append(node.val)  # Index error if level not in result
```
**Fix:** Check if `level >= len(result)` and create new list

---

## Testing

### Test Cases

```python
def test_level_order():
    solution = Solution()
    
    # Test 1: Empty tree
    assert solution.levelOrder(None) == []
    
    # Test 2: Single node
    root2 = TreeNode(1)
    assert solution.levelOrder(root2) == [[1]]
    
    # Test 3: Example 1
    root3 = TreeNode(3)
    root3.left = TreeNode(9)
    root3.right = TreeNode(20)
    root3.right.left = TreeNode(15)
    root3.right.right = TreeNode(7)
    assert solution.levelOrder(root3) == [[3], [9,20], [15,7]]
    
    # Test 4: Complete binary tree
    root4 = TreeNode(1)
    root4.left = TreeNode(2)
    root4.right = TreeNode(3)
    root4.left.left = TreeNode(4)
    root4.left.right = TreeNode(5)
    root4.right.left = TreeNode(6)
    root4.right.right = TreeNode(7)
    assert solution.levelOrder(root4) == [[1], [2,3], [4,5,6,7]]
    
    # Test 5: Left-skewed
    root5 = TreeNode(1)
    root5.left = TreeNode(2)
    root5.left.left = TreeNode(3)
    assert solution.levelOrder(root5) == [[1], [2], [3]]
    
    # Test 6: Right-skewed
    root6 = TreeNode(1)
    root6.right = TreeNode(2)
    root6.right.right = TreeNode(3)
    assert solution.levelOrder(root6) == [[1], [2], [3]]
    
    # Test 7: Unbalanced
    root7 = TreeNode(1)
    root7.left = TreeNode(2)
    root7.right = TreeNode(3)
    root7.left.left = TreeNode(4)
    root7.right.right = TreeNode(5)
    assert solution.levelOrder(root7) == [[1], [2,3], [4,5]]
    
    # Test 8: Two levels only
    root8 = TreeNode(1)
    root8.left = TreeNode(2)
    root8.right = TreeNode(3)
    assert solution.levelOrder(root8) == [[1], [2,3]]
    
    print("All test cases passed!")

test_level_order()
```

---

## Optimization Notes

### Space Optimization

**Queue Size:**
- Maximum queue size = maximum width of tree
- For complete binary tree: last level has ~n/2 nodes
- For skewed tree: maximum 2 nodes at a time

**Comparison:**
- BFS with queue: O(w) where w is max width
- DFS recursive: O(h) for stack + O(n) for result
- For balanced tree: width > height, so DFS uses less space
- For skewed tree: height > width, so BFS uses less space

### Time Optimization

- All approaches are O(n) - must visit every node
- Cannot optimize further
- Early termination not applicable (need all levels)

### Code Optimization

**Approach 1 is typically best:**
- Clean and intuitive
- Optimal space usage
- Easy to understand and maintain
- Standard interview solution

---

## Related Problems

### Similar Problems

1. **[LeetCode 107] Binary Tree Level Order Traversal II (Medium)**
   - Same but return levels bottom-up
   - Just reverse the result: `return result[::-1]`

2. **[LeetCode 103] Binary Tree Zigzag Level Order Traversal (Medium)**
   - Alternate left-to-right and right-to-left
   - Reverse alternate levels

3. **[LeetCode 637] Average of Levels in Binary Tree (Easy)**
   - Calculate average instead of listing all values
   - Same BFS pattern

4. **[LeetCode 199] Binary Tree Right Side View (Medium)**
   - Return last node of each level
   - BFS and take last element

5. **[LeetCode 515] Find Largest Value in Each Tree Row (Medium)**
   - Find maximum value in each level
   - BFS with max tracking

### Pattern Variations

- **Level order bottom-up:** Reverse result
- **Zigzag traversal:** Reverse alternate levels
- **Right side view:** Last node of each level
- **Left side view:** First node of each level
- **Level averages:** Compute statistics per level
- **Deepest leaves sum:** Process only last level

---

## Interview Tips

### What Interviewers Look For

1. **Understanding BFS:**
   - Know when to use queue
   - Understand level separation technique

2. **Code clarity:**
   - Clean loop structure
   - Proper variable naming
   - Good comments

3. **Edge cases:**
   - Empty tree
   - Single node
   - Skewed trees

4. **Multiple approaches:**
   - Know both BFS and DFS solutions
   - Understand trade-offs

### Discussion Points

**Interviewer:** "Why use a queue instead of recursion?"
**You:** "Queue naturally processes nodes in the order they're added, which gives us left-to-right order within each level. BFS is the most intuitive for level-order problems."

**Interviewer:** "Can you do this with DFS?"
**You:** "Yes, by tracking the level depth. We add nodes to the appropriate level list as we traverse. However, BFS is more natural for this problem."

**Interviewer:** "What's the space complexity?"
**You:** "O(w) where w is the maximum width. For a complete binary tree, the last level has about n/2 nodes, so worst case is O(n)."

**Interviewer:** "How would you modify this to return levels bottom-up?"
**You:** "Simply reverse the result at the end: `return result[::-1]`, or build result in reverse order."

### Follow-up Questions

1. **"Return only the last level"**
   - Don't add to result until queue is empty
   - Or just return `result[-1]`

2. **"Find the deepest leaves"**
   - Same as above - last level

3. **"Average of each level"**
   - Calculate sum/count for each level
   - Return list of averages

4. **"Zigzag level order"**
   - Reverse alternate levels
   - Or use different append strategy

5. **"Right side view"**
   - Return last element of each level

---

## Key Takeaways

1. **BFS Pattern:** Use queue with level size tracking

2. **Level Separation:** 
   ```python
   level_size = len(queue)
   for _ in range(level_size):
       # Process one level
   ```

3. **Use deque:** For O(1) queue operations

4. **DFS Alternative:** Track level depth, add to result[level]

5. **Foundation Problem:** Master this for all level-order variations

---

## Additional Resources

### Visual Learning
- **BFS Visualization:** visualgo.net/en/dfsbfs
- **Level Order Animation:** algorithm-visualizer.org

### Related Concepts
- Queue data structure
- BFS vs DFS
- Tree traversals
- Level-based processing

### Practice Progression
1. Master this problem first
2. Then try: LeetCode 107 (Bottom-up), 637 (Averages)
3. Advanced: LeetCode 103 (Zigzag), 199 (Right View)

---

## Summary

**Problem:** Return nodes grouped by level from top to bottom

**Best Solution:** BFS with Queue
```python
def levelOrder(self, root):
    if not root:
        return []
    
    result = []
    queue = deque([root])
    
    while queue:
        level_size = len(queue)
        current_level = []
        
        for _ in range(level_size):
            node = queue.popleft()
            current_level.append(node.val)
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
        
        result.append(current_level)
    
    return result
```

**Complexity:** O(n) time, O(w) space where w is max width

**Key Insight:** Capture level size before processing to separate levels

**When to Use:** Any level-based tree processing; foundation for many tree problems!
