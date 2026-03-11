# Reverse Linked List (Easy)

## Problem Statement
Given the `head` of a singly linked list, reverse the list, and return the reversed list.

**LeetCode Link**: [206. Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/)

---

## Examples

### Example 1:
```
Input: head = [1,2,3,4,5]
Output: [5,4,3,2,1]

Visual:
1 → 2 → 3 → 4 → 5 → null
            ↓
5 → 4 → 3 → 2 → 1 → null
```

### Example 2:
```
Input: head = [1,2]
Output: [2,1]

Visual:
1 → 2 → null
     ↓
2 → 1 → null
```

### Example 3:
```
Input: head = []
Output: []
Explanation: Empty list stays empty.
```

---

## Constraints
- The number of nodes in the list is in the range `[0, 5000]`
- `-5000 <= Node.val <= 5000`

---

## Pattern Recognition

This is an **In-Place Reversal of LinkedList** problem because:
1. We need to **reverse the direction** of all pointers
2. Must do it **in-place** (O(1) extra space for iterative)
3. Can use **three pointers** technique (prev, current, next)
4. Or use **recursion** (more elegant but O(n) space)

**Key Insight**: 
- To reverse a link: `current.next = prev`
- Need to save `next` before reversing (otherwise lose reference)
- Move all three pointers forward: `prev → current → next`

**Two Main Approaches**:
1. **Iterative**: Three pointers, reverse one by one → O(1) space
2. **Recursive**: Recursive call to reverse, then fix pointers → O(n) space

---

## Node Definition

```java
/**
 * Definition for singly-linked list.
 */
public class ListNode {
    int val;
    ListNode next;
    
    ListNode() {}
    
    ListNode(int val) {
        this.val = val;
    }
    
    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}
```

---

## Approaches

### Approach 1: Iterative with Three Pointers (OPTIMAL) ⭐
**Idea**: Use prev, current, next to reverse links one by one.

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            // Save next node before reversing link
            ListNode next = current.next;
            
            // Reverse the link
            current.next = prev;
            
            // Move pointers forward
            prev = current;
            current = next;
        }
        
        // prev is now the new head
        return prev;
    }
}
```

**Time Complexity**: O(n) - visit each node once
**Space Complexity**: O(1) - only three pointers
**Why Optimal**: Minimal space, single pass

---

### Approach 2: Recursive (Elegant)
**Idea**: Recursively reverse rest of list, then fix current node's link.

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        // Base case: empty or single node
        if (head == null || head.next == null) {
            return head;
        }
        
        // Recursively reverse the rest
        ListNode newHead = reverseList(head.next);
        
        // Fix the links
        head.next.next = head; // Reverse the link
        head.next = null;      // Break the old link
        
        return newHead;
    }
}
```

**Time Complexity**: O(n) - visit each node once
**Space Complexity**: O(n) - recursion call stack
**Note**: More elegant but uses stack space

---

### Approach 3: Iterative with Dummy Node (Alternative)
**Idea**: Build reversed list using dummy node.

```java
class Solution {
    public ListNode reverseList(ListNode head) {
        ListNode dummy = new ListNode(0);
        ListNode current = head;
        
        while (current != null) {
            ListNode next = current.next;
            
            // Insert current at front of dummy's list
            current.next = dummy.next;
            dummy.next = current;
            
            current = next;
        }
        
        return dummy.next;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(1) - constant extra space
**Note**: Different perspective, same efficiency

---

### Approach 4: Stack-Based (NOT OPTIMAL)
**Idea**: Push all nodes to stack, then pop to reverse.

```java
import java.util.*;

class Solution {
    public ListNode reverseList(ListNode head) {
        if (head == null) return null;
        
        Stack<ListNode> stack = new Stack<>();
        
        // Push all nodes to stack
        ListNode current = head;
        while (current != null) {
            stack.push(current);
            current = current.next;
        }
        
        // Pop to build reversed list
        ListNode newHead = stack.pop();
        current = newHead;
        
        while (!stack.isEmpty()) {
            current.next = stack.pop();
            current = current.next;
        }
        
        current.next = null; // Important: terminate list
        
        return newHead;
    }
}
```

**Time Complexity**: O(n) - two passes
**Space Complexity**: O(n) - stack storage
**Problem**: Unnecessary space usage, not in-place

---

## Detailed Walkthrough (Approach 1: Iterative)

### Example: head = [1, 2, 3, 4, 5]

```
Initial State:
prev = null
current = 1 → 2 → 3 → 4 → 5 → null

Step 1: At node 1
  next = 2 (save before reversing)
  current.next = prev → 1.next = null
  Move pointers:
    prev = 1
    current = 2
  
  State: null ← 1    2 → 3 → 4 → 5 → null
         prev      current

Step 2: At node 2
  next = 3 (save before reversing)
  current.next = prev → 2.next = 1
  Move pointers:
    prev = 2
    current = 3
  
  State: null ← 1 ← 2    3 → 4 → 5 → null
                  prev  current

Step 3: At node 3
  next = 4 (save before reversing)
  current.next = prev → 3.next = 2
  Move pointers:
    prev = 3
    current = 4
  
  State: null ← 1 ← 2 ← 3    4 → 5 → null
                       prev  current

Step 4: At node 4
  next = 5 (save before reversing)
  current.next = prev → 4.next = 3
  Move pointers:
    prev = 4
    current = 5
  
  State: null ← 1 ← 2 ← 3 ← 4    5 → null
                            prev  current

Step 5: At node 5
  next = null (save before reversing)
  current.next = prev → 5.next = 4
  Move pointers:
    prev = 5
    current = null
  
  State: null ← 1 ← 2 ← 3 ← 4 ← 5    null
                                 prev  current

Loop exits (current == null)
Return prev (which is node 5, the new head)

Final: 5 → 4 → 3 → 2 → 1 → null
```

---

### Visual Representation

```
Original List:
1 → 2 → 3 → 4 → 5 → null

Reversal Process (Three Pointers):

Step 0:
prev = null
current = 1 → 2 → 3 → 4 → 5 → null

Step 1: Reverse 1's link
null ← 1    2 → 3 → 4 → 5 → null
      prev  current

Step 2: Reverse 2's link
null ← 1 ← 2    3 → 4 → 5 → null
            prev current

Step 3: Reverse 3's link
null ← 1 ← 2 ← 3    4 → 5 → null
                prev current

Step 4: Reverse 4's link
null ← 1 ← 2 ← 3 ← 4    5 → null
                    prev current

Step 5: Reverse 5's link
null ← 1 ← 2 ← 3 ← 4 ← 5    null
                        prev current

Final Reversed List:
5 → 4 → 3 → 2 → 1 → null
↑
new head (prev)

Key Operations at Each Step:
1. Save next: next = current.next
2. Reverse link: current.next = prev
3. Move prev: prev = current
4. Move current: current = next
```

---

### Recursive Approach Visualization

```
reverseList([1,2,3,4,5])
  ↓
  reverseList([2,3,4,5]) → returns 5 (new head)
    ↓
    reverseList([3,4,5]) → returns 5
      ↓
      reverseList([4,5]) → returns 5
        ↓
        reverseList([5]) → returns 5 (base case)
        ← Fix 4's link: 5.next = 4, 4.next = null
      ← Fix 3's link: 4.next = 3, 3.next = null
    ← Fix 2's link: 3.next = 2, 2.next = null
  ← Fix 1's link: 2.next = 1, 1.next = null

Call Stack (deepest first):
reverseList([5]) → base case, return 5
reverseList([4,5]) → fix: 5→4, return 5
reverseList([3,4,5]) → fix: 4→3, return 5
reverseList([2,3,4,5]) → fix: 3→2, return 5
reverseList([1,2,3,4,5]) → fix: 2→1, return 5

Final: 5 → 4 → 3 → 2 → 1 → null
```

---

## Edge Cases to Consider

```java
// Test Case 1: Empty list
Input: head = null
Output: null
// Nothing to reverse

// Test Case 2: Single node
Input: head = [1]
Output: [1]
// Single node stays same

// Test Case 3: Two nodes
Input: head = [1,2]
Output: [2,1]
// Simple swap

// Test Case 4: All same values
Input: head = [5,5,5,5]
Output: [5,5,5,5]
// Values same, but links reversed

// Test Case 5: Large list
Input: head = [1,2,3,...,5000]
Output: [5000,...,3,2,1]
// Test performance

// Test Case 6: Negative values
Input: head = [-5,-4,-3,-2,-1]
Output: [-1,-2,-3,-4,-5]
// Negative values handled

// Test Case 7: Mixed positive/negative
Input: head = [-2,-1,0,1,2]
Output: [2,1,0,-1,-2]
// Order reversed

// Test Case 8: Three nodes
Input: head = [1,2,3]
Output: [3,2,1]
// Common test case

// Test Case 9: Even length
Input: head = [1,2,3,4]
Output: [4,3,2,1]
// Even number of nodes

// Test Case 10: Odd length
Input: head = [1,2,3,4,5]
Output: [5,4,3,2,1]
// Odd number of nodes
```

---

## Common Mistakes to Avoid

### Mistake 1: Not Saving Next Before Reversing
```java
// ❌ WRONG: Lost reference to rest of list!
while (current != null) {
    current.next = prev; // Now we lost the rest of the list!
    current = current.next; // This is now prev, not the next node!
}

// ✅ CORRECT: Save next first
while (current != null) {
    ListNode next = current.next; // Save before losing it
    current.next = prev;
    current = next; // Use saved reference
}
```

### Mistake 2: Forgetting to Move prev Forward
```java
// ❌ WRONG: Not updating prev
while (current != null) {
    ListNode next = current.next;
    current.next = prev;
    // Forgot: prev = current;
    current = next;
}

// ✅ CORRECT: Move all three pointers
ListNode next = current.next;
current.next = prev;
prev = current;
current = next;
```

### Mistake 3: Returning Wrong Node
```java
// ❌ WRONG: Returning head (original first node)
return head; // This is now the last node, not new head!

// ✅ CORRECT: Return prev (new head)
return prev; // After loop, prev is the new head
```

### Mistake 4: Not Handling Empty List
```java
// ❌ WRONG: Assumes list is not empty
ListNode prev = null;
ListNode current = head;
ListNode next = current.next; // NullPointerException if head is null!

// ✅ CORRECT: Check in loop condition
while (current != null) {
    ListNode next = current.next;
    // ...
}
```

### Mistake 5: Recursive Without Base Case
```java
// ❌ WRONG: Missing or wrong base case
public ListNode reverseList(ListNode head) {
    ListNode newHead = reverseList(head.next); // StackOverflowError!
}

// ✅ CORRECT: Proper base case
public ListNode reverseList(ListNode head) {
    if (head == null || head.next == null) {
        return head; // Base case
    }
    ListNode newHead = reverseList(head.next);
    // ...
}
```

### Mistake 6: Not Breaking Old Link in Recursion
```java
// ❌ WRONG: Creating cycle!
public ListNode reverseList(ListNode head) {
    if (head == null || head.next == null) return head;
    ListNode newHead = reverseList(head.next);
    head.next.next = head;
    // Forgot: head.next = null;
    return newHead; // Now we have: 1 ↔ 2 (cycle!)
}

// ✅ CORRECT: Break the old link
head.next.next = head;
head.next = null; // Break old link
```

---

## Why Three Pointers Work

### The Challenge:
```
We need to reverse: A → B → C
To become:           A ← B ← C

But when we do: B.next = A
We lose access to C!

Solution: Save C before reversing B's link
```

### The Three Pointer Pattern:
```
prev: The node that current should point to (behind)
current: The node we're currently reversing
next: The node after current (ahead, temporary storage)

Flow:
1. Save next (don't lose rest of list)
2. Reverse current's link to prev
3. Move prev to current (step forward)
4. Move current to next (step forward)
5. Repeat until current is null
```

### Why This Works:
```
Example: 1 → 2 → 3 → null

prev=null, current=1:
  next = 2 (save it!)
  1.next = null (reverse: 1 → null)
  prev = 1, current = 2
  
prev=1, current=2:
  next = 3 (save it!)
  2.next = 1 (reverse: 2 → 1 → null)
  prev = 2, current = 3
  
prev=2, current=3:
  next = null (save it!)
  3.next = 2 (reverse: 3 → 2 → 1 → null)
  prev = 3, current = null
  
Loop ends, prev points to new head (3)
```

---

## Optimization Techniques

### Optimization 1: Early Return for Small Lists
```java
// If 0 or 1 node, no reversal needed
if (head == null || head.next == null) {
    return head;
}

// Then proceed with reversal
```

### Optimization 2: Tail Recursion (If Language Supports)
```java
// Java doesn't optimize tail recursion, but conceptually:
public ListNode reverseList(ListNode head) {
    return reverseHelper(head, null);
}

private ListNode reverseHelper(ListNode current, ListNode prev) {
    if (current == null) {
        return prev;
    }
    
    ListNode next = current.next;
    current.next = prev;
    return reverseHelper(next, current);
}
```

### Optimization 3: In-Place with Single Expression (Advanced)
```java
// Compact version (less readable)
public ListNode reverseList(ListNode head) {
    ListNode prev = null;
    while (head != null) {
        ListNode next = head.next;
        head.next = prev;
        prev = head;
        head = next;
    }
    return prev;
}
```

---

## Complexity Analysis

### Iterative Approach:
**Time Complexity**: O(n)
- Visit each node exactly once
- Constant work per node
- Linear time overall

**Space Complexity**: O(1)
- Only three pointers (prev, current, next)
- No additional data structures
- True in-place reversal

### Recursive Approach:
**Time Complexity**: O(n)
- Visit each node once
- Constant work per recursive call
- Linear time overall

**Space Complexity**: O(n)
- Recursion call stack depth = n
- Each call uses constant space
- Not truly in-place due to stack

### Comparison:

| Approach | Time | Space | Pros | Cons |
|----------|------|-------|------|------|
| Iterative | O(n) | O(1) | ✅ Best space, clear | More lines |
| Recursive | O(n) | O(n) | ✅ Elegant | Stack overflow risk |
| Stack-based | O(n) | O(n) | Easy to understand | Unnecessary space |

---

## Pattern: In-Place Reversal Template

### General Template:
```java
public ListNode reverseLinkedList(ListNode head) {
    ListNode prev = null;
    ListNode current = head;
    
    while (current != null) {
        // 1. Save next
        ListNode next = current.next;
        
        // 2. Reverse link
        current.next = prev;
        
        // 3. Move pointers forward
        prev = current;
        current = next;
    }
    
    return prev; // New head
}
```

### When to Use:
- ✅ Reversing entire linked list
- ✅ Reversing portion of list (with modifications)
- ✅ Reversing in groups (K-group reversal)
- ✅ Palindrome checking (reverse second half)

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Constraints** (30 seconds)
> "I need to reverse a singly linked list. Can I confirm it's singly linked (not doubly)? Yes. Should I do it in-place? Yes, O(1) space is preferred. Empty list returns empty."

**Step 2: Explain Approach** (1-2 minutes)
> "I'll use the three-pointer technique:
> 1. prev starts at null (new tail)
> 2. current starts at head
> 3. For each node: save next, reverse link to prev, move forward
> 4. Return prev (new head)
> Time: O(n), Space: O(1)
> 
> Alternative: Recursion is more elegant but uses O(n) stack space."

**Step 3: Walk Through Example** (2 minutes)
```java
// 1 → 2 → 3 → null
// prev=null, curr=1: reverse to null←1, move forward
// prev=1, curr=2: reverse to null←1←2, move forward
// prev=2, curr=3: reverse to null←1←2←3, move forward
// prev=3, curr=null: done, return prev
// Result: 3 → 2 → 1 → null
```

**Step 4: Discuss Edge Cases** (30 seconds)
> "Edge cases:
> - Empty list: return null immediately
> - Single node: return as-is (no reversal needed)
> - Two nodes: simple swap works correctly"

**Step 5: Code** (5-10 minutes)
- Initialize prev=null, current=head
- While loop until current is null
- Save next, reverse link, move pointers
- Return prev
- Test with [1,2,3]

### Expected Follow-up Questions:

**Q**: "Can you reverse only part of the list (indices m to n)?"
**A**: "Yes! Navigate to position m-1, then apply same reversal for m to n nodes, then reconnect the parts. LeetCode #92."

**Q**: "Can you reverse in groups of k?"
**A**: "Yes! Reverse k nodes at a time, keep track of previous group's tail to connect groups. LeetCode #25."

**Q**: "How would you check if a linked list is a palindrome?"
**A**: "Find middle with slow/fast pointers, reverse second half, compare with first half. Space O(1)."

**Q**: "Can you reverse a doubly linked list?"
**A**: "Yes, swap next and prev pointers for each node. Need to handle both directions."

**Q**: "What if you needed to preserve the original list?"
**A**: "Then I'd create a new list by iterating and creating new nodes. Space O(n), but original preserved."

---

## Complete Solution with Comments

```java
/**
 * Definition for singly-linked list.
 */
class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
}

class Solution {
    /**
     * Reverses a singly linked list in-place.
     * 
     * Approach: Iterative with three pointers
     * - prev: node that current should point to (behind)
     * - current: node we're currently processing
     * - next: temporary storage for remaining list
     * 
     * @param head Head of the original list
     * @return Head of the reversed list
     * 
     * Time Complexity: O(n) - visit each node once
     * Space Complexity: O(1) - only three pointers
     */
    public ListNode reverseList(ListNode head) {
        // Edge case: empty or single node
        if (head == null || head.next == null) {
            return head;
        }
        
        // Three pointers for reversal
        ListNode prev = null;    // New tail (starts at null)
        ListNode current = head; // Current node being processed
        
        while (current != null) {
            // Step 1: Save next node before breaking link
            // (We need this to continue iteration)
            ListNode next = current.next;
            
            // Step 2: Reverse the link
            // Make current point backward to prev
            current.next = prev;
            
            // Step 3: Move prev forward
            // prev becomes current (for next iteration)
            prev = current;
            
            // Step 4: Move current forward
            // current becomes next (continue iteration)
            current = next;
        }
        
        // After loop, prev points to last node (new head)
        // current is null (past the end)
        return prev;
    }
}
```

---

## Recursive Solution with Comments

```java
class Solution {
    /**
     * Reverses a linked list recursively.
     * 
     * Approach: Recursion
     * - Base case: empty or single node
     * - Recursive case: reverse rest, then fix links
     * 
     * Time Complexity: O(n)
     * Space Complexity: O(n) - call stack
     */
    public ListNode reverseList(ListNode head) {
        // Base case: empty list or single node
        if (head == null || head.next == null) {
            return head;
        }
        
        // Recursively reverse the rest of the list
        // newHead will be the last node (new head)
        ListNode newHead = reverseList(head.next);
        
        // Fix the links
        // Current: head → head.next → ... → newHead
        // We need: head ← head.next ← ... ← newHead
        
        // Make next node point back to current
        head.next.next = head;
        
        // Break the old forward link
        // (Current node becomes tail of its portion)
        head.next = null;
        
        // Return the new head (unchanged through recursion)
        return newHead;
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public static void main(String[] args) {
    Solution solution = new Solution();
    
    // Helper to create list
    ListNode createList(int[] values) {
        if (values.length == 0) return null;
        ListNode head = new ListNode(values[0]);
        ListNode current = head;
        for (int i = 1; i < values.length; i++) {
            current.next = new ListNode(values[i]);
            current = current.next;
        }
        return head;
    }
    
    // Helper to print list
    void printList(ListNode head) {
        while (head != null) {
            System.out.print(head.val + " → ");
            head = head.next;
        }
        System.out.println("null");
    }
    
    // Test Case 1: Standard
    ListNode list1 = createList(new int[]{1,2,3,4,5});
    printList(solution.reverseList(list1)); // 5 → 4 → 3 → 2 → 1 → null
    
    // Test Case 2: Two nodes
    ListNode list2 = createList(new int[]{1,2});
    printList(solution.reverseList(list2)); // 2 → 1 → null
    
    // Test Case 3: Single node
    ListNode list3 = createList(new int[]{1});
    printList(solution.reverseList(list3)); // 1 → null
}
```

---

## Key Takeaways

1. ✅ **Three pointers (prev, current, next)** for iterative reversal
2. ✅ **Save next before reversing** to avoid losing list
3. ✅ **Iterative is O(1) space**, recursive is O(n)
4. ✅ **Return prev** as new head (not head!)
5. ✅ **Move all three pointers** forward each iteration
6. ✅ **Base case** for recursion: null or single node
7. ✅ **Break old link** in recursion (set next to null)
8. ✅ **Edge cases**: empty list, single node, two nodes
9. ✅ **Pattern extends** to partial reversal, k-group reversal
10. ✅ **Fundamental linked list skill** - must master!

---

## Variations & Extensions

After mastering Reverse Linked List, try these related problems:

1. **Reverse Linked List II** (Medium) - Reverse portion from m to n
2. **Reverse Nodes in k-Group** (Hard) - Reverse in groups of k
3. **Palindrome Linked List** (Easy) - Uses reversal to check
4. **Swap Nodes in Pairs** (Medium) - Reverse every 2 nodes
5. **Odd Even Linked List** (Medium) - Group odd/even positioned nodes

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (15-20 minutes)
- [ ] Implement both iterative and recursive
- [ ] Draw out the pointer movements
- [ ] Test with empty, single, and two-node lists
- [ ] Practice explaining three-pointer technique
- [ ] Trace through [1,2,3] step by step
- [ ] Review in 2 days

---

**Pattern Mastered**: In-Place Reversal of LinkedList ✅  
**Difficulty**: Easy (but fundamental!)  
**Time to Master**: 20-30 minutes  
**Importance**: ⭐⭐⭐⭐⭐ Core linked list operation

This is THE fundamental linked list reversal problem. Master the three-pointer technique here and you'll handle all reversal variations with confidence! 🚀
