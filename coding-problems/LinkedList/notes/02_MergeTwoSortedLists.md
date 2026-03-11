# Merge Two Sorted Lists (Easy)

## Problem Statement
You are given the heads of two sorted linked lists `list1` and `list2`.

Merge the two lists in a one **sorted list**. The list should be made by splicing together the nodes of the first two lists.

Return the head of the merged linked list.

**LeetCode Link**: [21. Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/)

---

## Examples

### Example 1:
```
Input: list1 = [1,2,4], list2 = [1,3,4]
Output: [1,1,2,3,4,4]

Visual:
list1: 1 → 2 → 4
list2: 1 → 3 → 4
              ↓
merged: 1 → 1 → 2 → 3 → 4 → 4
```

### Example 2:
```
Input: list1 = [], list2 = []
Output: []
Explanation: Both lists are empty.
```

### Example 3:
```
Input: list1 = [], list2 = [0]
Output: [0]
Explanation: One list is empty.
```

---

## Constraints
- The number of nodes in both lists is in the range `[0, 50]`
- `-100 <= Node.val <= 100`
- Both `list1` and `list2` are sorted in **non-decreasing** order

---

## Pattern Recognition

This is a **Two Pointers on LinkedLists / Merge Pattern** problem because:
1. Both lists are **already sorted**
2. We need to **merge while maintaining sorted order**
3. Use **two pointers** to track position in each list
4. **Compare and link** smaller node each time
5. Similar to merge step in **Merge Sort**

**Key Insight**: 
- Use **dummy node** to simplify edge cases
- Compare current nodes from both lists
- Link smaller one to result
- Move pointer of list from which we took node
- Handle remaining nodes when one list exhausted

**Two Main Approaches**:
1. **Iterative with Dummy Node**: Clean, O(1) space
2. **Recursive**: Elegant, O(n) space for call stack

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

### Approach 1: Iterative with Dummy Node (OPTIMAL) ⭐
**Idea**: Use dummy node to build result, compare and link nodes one by one.

```java
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Dummy node to simplify edge cases
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        // Merge while both lists have nodes
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        
        // Attach remaining nodes (at most one list has remaining nodes)
        if (list1 != null) {
            current.next = list1;
        } else {
            current.next = list2;
        }
        
        return dummy.next; // Skip dummy node
    }
}
```

**Time Complexity**: O(m + n) - visit each node once
**Space Complexity**: O(1) - only pointers, reuse existing nodes
**Why Optimal**: Linear time, constant space, clean code

---

### Approach 2: Recursive (Elegant)
**Idea**: Recursively merge lists by picking smaller head.

```java
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Base cases
        if (list1 == null) return list2;
        if (list2 == null) return list1;
        
        // Pick smaller head and recursively merge rest
        if (list1.val <= list2.val) {
            list1.next = mergeTwoLists(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoLists(list1, list2.next);
            return list2;
        }
    }
}
```

**Time Complexity**: O(m + n) - visit each node once
**Space Complexity**: O(m + n) - recursion call stack
**Note**: More elegant but uses stack space

---

### Approach 3: Iterative Without Dummy Node (More Verbose)
**Idea**: Handle head separately, then merge rest.

```java
class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Edge cases
        if (list1 == null) return list2;
        if (list2 == null) return list1;
        
        // Determine head of merged list
        ListNode head, current;
        if (list1.val <= list2.val) {
            head = list1;
            list1 = list1.next;
        } else {
            head = list2;
            list2 = list2.next;
        }
        
        current = head;
        
        // Merge remaining nodes
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        
        // Attach remaining
        current.next = (list1 != null) ? list1 : list2;
        
        return head;
    }
}
```

**Time Complexity**: O(m + n) - single pass
**Space Complexity**: O(1) - constant space
**Note**: Works but dummy node version is cleaner

---

### Approach 4: Priority Queue (NOT OPTIMAL)
**Idea**: Add all nodes to min-heap, extract to build merged list.

```java
import java.util.*;

class Solution {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        PriorityQueue<ListNode> pq = new PriorityQueue<>((a, b) -> a.val - b.val);
        
        // Add all nodes to heap
        while (list1 != null) {
            pq.offer(list1);
            list1 = list1.next;
        }
        while (list2 != null) {
            pq.offer(list2);
            list2 = list2.next;
        }
        
        // Build merged list
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        while (!pq.isEmpty()) {
            current.next = pq.poll();
            current = current.next;
        }
        
        current.next = null; // Important: terminate list
        return dummy.next;
    }
}
```

**Time Complexity**: O((m+n) log(m+n)) - heap operations
**Space Complexity**: O(m + n) - heap storage
**Problem**: Overkill for just 2 lists, useful for k lists (LeetCode #23)

---

## Detailed Walkthrough (Approach 1: Iterative)

### Example: list1 = [1,2,4], list2 = [1,3,4]

```
Initial State:
list1: 1 → 2 → 4 → null
list2: 1 → 3 → 4 → null
dummy: 0 → null
current: 0

Step 1: Compare 1 and 1
  list1.val (1) <= list2.val (1) → pick list1
  current.next = list1 (node with value 1)
  list1 = list1.next (move to 2)
  current = current.next (move to 1)
  
  State:
  list1: 2 → 4 → null
  list2: 1 → 3 → 4 → null
  dummy: 0 → 1
  current: 1

Step 2: Compare 2 and 1
  list2.val (1) < list1.val (2) → pick list2
  current.next = list2 (node with value 1)
  list2 = list2.next (move to 3)
  current = current.next (move to 1)
  
  State:
  list1: 2 → 4 → null
  list2: 3 → 4 → null
  dummy: 0 → 1 → 1
  current: 1 (second one)

Step 3: Compare 2 and 3
  list1.val (2) < list2.val (3) → pick list1
  current.next = list1 (node with value 2)
  list1 = list1.next (move to 4)
  current = current.next (move to 2)
  
  State:
  list1: 4 → null
  list2: 3 → 4 → null
  dummy: 0 → 1 → 1 → 2
  current: 2

Step 4: Compare 4 and 3
  list2.val (3) < list1.val (4) → pick list2
  current.next = list2 (node with value 3)
  list2 = list2.next (move to 4)
  current = current.next (move to 3)
  
  State:
  list1: 4 → null
  list2: 4 → null
  dummy: 0 → 1 → 1 → 2 → 3
  current: 3

Step 5: Compare 4 and 4
  list1.val (4) <= list2.val (4) → pick list1
  current.next = list1 (node with value 4)
  list1 = list1.next (move to null)
  current = current.next (move to 4)
  
  State:
  list1: null
  list2: 4 → null
  dummy: 0 → 1 → 1 → 2 → 3 → 4
  current: 4

Step 6: list1 is null, attach remaining list2
  current.next = list2 (node with value 4)
  
  Final State:
  dummy: 0 → 1 → 1 → 2 → 3 → 4 → 4 → null

Return dummy.next: 1 → 1 → 2 → 3 → 4 → 4 → null
```

---

### Visual Representation

```
list1: 1 → 2 → 4 → null
list2: 1 → 3 → 4 → null

Merging Process (step by step):

Initial:
dummy → ?
list1 → 1 → 2 → 4
list2 → 1 → 3 → 4

Step 1: Pick 1 from list1
dummy → 1 (from list1)
list1 → 2 → 4
list2 → 1 → 3 → 4

Step 2: Pick 1 from list2
dummy → 1 → 1 (from list2)
list1 → 2 → 4
list2 → 3 → 4

Step 3: Pick 2 from list1
dummy → 1 → 1 → 2 (from list1)
list1 → 4
list2 → 3 → 4

Step 4: Pick 3 from list2
dummy → 1 → 1 → 2 → 3 (from list2)
list1 → 4
list2 → 4

Step 5: Pick 4 from list1
dummy → 1 → 1 → 2 → 3 → 4 (from list1)
list1 → null
list2 → 4

Step 6: Attach remaining list2
dummy → 1 → 1 → 2 → 3 → 4 → 4 (from list2)

Return dummy.next (skip dummy):
Result: 1 → 1 → 2 → 3 → 4 → 4 → null

Key: Compare heads, pick smaller, advance that pointer
```

---

### Recursive Approach Visualization

```
mergeTwoLists([1,2,4], [1,3,4])

Compare 1 and 1: pick list1 (1)
  1.next = mergeTwoLists([2,4], [1,3,4])
  
  Compare 2 and 1: pick list2 (1)
    1.next = mergeTwoLists([2,4], [3,4])
    
    Compare 2 and 3: pick list1 (2)
      2.next = mergeTwoLists([4], [3,4])
      
      Compare 4 and 3: pick list2 (3)
        3.next = mergeTwoLists([4], [4])
        
        Compare 4 and 4: pick list1 (4)
          4.next = mergeTwoLists(null, [4])
          
          Base case: list1 is null, return [4]
        ← return 4 → 4
      ← return 3 → 4 → 4
    ← return 2 → 3 → 4 → 4
  ← return 1 → 2 → 3 → 4 → 4
← return 1 → 1 → 2 → 3 → 4 → 4

Final: 1 → 1 → 2 → 3 → 4 → 4
```

---

## Edge Cases to Consider

```java
// Test Case 1: Both empty
Input: list1 = [], list2 = []
Output: []
// Return null or empty list

// Test Case 2: One empty
Input: list1 = [], list2 = [1,2,3]
Output: [1,2,3]
// Just return the non-empty list

// Test Case 3: Other empty
Input: list1 = [1,2,3], list2 = []
Output: [1,2,3]
// Return list1

// Test Case 4: All from list1 first
Input: list1 = [1,2,3], list2 = [4,5,6]
Output: [1,2,3,4,5,6]
// All list1 nodes come before list2

// Test Case 5: All from list2 first
Input: list1 = [4,5,6], list2 = [1,2,3]
Output: [1,2,3,4,5,6]
// All list2 nodes come before list1

// Test Case 6: Interleaved
Input: list1 = [1,3,5], list2 = [2,4,6]
Output: [1,2,3,4,5,6]
// Perfect interleaving

// Test Case 7: Duplicates
Input: list1 = [1,1,1], list2 = [1,1,1]
Output: [1,1,1,1,1,1]
// All same values

// Test Case 8: Single node each
Input: list1 = [1], list2 = [2]
Output: [1,2]
// Minimal case

// Test Case 9: Different lengths
Input: list1 = [1], list2 = [2,3,4,5]
Output: [1,2,3,4,5]
// One list much longer

// Test Case 10: Negative values
Input: list1 = [-10,-5,0], list2 = [-8,-2,1]
Output: [-10,-8,-5,-2,0,1]
// Negative values handled

// Test Case 11: Large difference
Input: list1 = [1,100], list2 = [2,3,4]
Output: [1,2,3,4,100]
// Gap in middle
```

---

## Common Mistakes to Avoid

### Mistake 1: Creating New Nodes Instead of Reusing
```java
// ❌ WRONG: Creating new nodes (wastes space)
if (list1.val <= list2.val) {
    current.next = new ListNode(list1.val); // Unnecessary!
    list1 = list1.next;
}

// ✅ CORRECT: Reuse existing nodes
if (list1.val <= list2.val) {
    current.next = list1; // Just link existing node
    list1 = list1.next;
}
```

### Mistake 2: Not Handling Both Lists Empty
```java
// ❌ WRONG: Assumes at least one list has nodes
ListNode head = (list1.val <= list2.val) ? list1 : list2;
// NullPointerException if both are null!

// ✅ CORRECT: Handle edge cases
if (list1 == null) return list2;
if (list2 == null) return list1;
```

### Mistake 3: Forgetting to Return dummy.next
```java
// ❌ WRONG: Returning dummy itself
return dummy; // This includes the dummy node (value 0)!

// ✅ CORRECT: Return actual head (skip dummy)
return dummy.next;
```

### Mistake 4: Not Attaching Remaining Nodes
```java
// ❌ WRONG: Forgetting remaining nodes
while (list1 != null && list2 != null) {
    // merge logic...
}
// Forgot to attach remaining nodes!
return dummy.next; // Incomplete list!

// ✅ CORRECT: Attach remaining
if (list1 != null) {
    current.next = list1;
} else {
    current.next = list2;
}
```

### Mistake 5: Using < Instead of <=
```java
// ❌ POTENTIALLY WRONG: Not stable for equal values
if (list1.val < list2.val) {
    // Always picks list2 when equal, might matter for stability
}

// ✅ CORRECT: Use <= for stable merge
if (list1.val <= list2.val) {
    // Picks list1 when equal (stable)
}
```

### Mistake 6: Moving Both Pointers When Picking One
```java
// ❌ WRONG: Moving both pointers
if (list1.val <= list2.val) {
    current.next = list1;
    list1 = list1.next;
    list2 = list2.next; // Wrong! Only move the one we picked!
}

// ✅ CORRECT: Only move pointer of list we picked from
if (list1.val <= list2.val) {
    current.next = list1;
    list1 = list1.next; // Only this one!
}
```

---

## Why Dummy Node Simplifies Code

### Without Dummy Node:
```java
// Need special handling for head
ListNode head = null;
ListNode current = null;

if (list1.val <= list2.val) {
    head = list1;
    current = list1;
    list1 = list1.next;
} else {
    head = list2;
    current = list2;
    list2 = list2.next;
}

// Then merge rest...
// Lots of edge case handling!
```

### With Dummy Node:
```java
// Uniform handling for all nodes
ListNode dummy = new ListNode(0);
ListNode current = dummy;

// No special case for first node!
while (list1 != null && list2 != null) {
    if (list1.val <= list2.val) {
        current.next = list1;
        list1 = list1.next;
    } else {
        current.next = list2;
        list2 = list2.next;
    }
    current = current.next;
}

return dummy.next; // Skip dummy
```

**Benefits of Dummy Node**:
- ✅ No special case for first node
- ✅ Cleaner code
- ✅ Fewer edge cases to handle
- ✅ Only O(1) extra space (negligible)

---

## Optimization Techniques

### Optimization 1: Early Exit if One List Empty
```java
// Check at start
if (list1 == null) return list2;
if (list2 == null) return list1;

// Then proceed with merge
```

### Optimization 2: Attach Remaining in One Line
```java
// Instead of if-else
if (list1 != null) {
    current.next = list1;
} else {
    current.next = list2;
}

// Use ternary
current.next = (list1 != null) ? list1 : list2;
```

### Optimization 3: Avoid Unnecessary Comparisons
```java
// If one list becomes empty, no need to check each node
while (list1 != null && list2 != null) {
    // compare and link
}
// Directly attach remaining (no need to iterate)
current.next = (list1 != null) ? list1 : list2;
```

---

## Complexity Analysis

### Time Complexity: O(m + n)
- **m** = length of list1
- **n** = length of list2
- Visit each node exactly once
- Attach remaining nodes in O(1)
- Linear in total nodes

### Space Complexity: O(1) for iterative, O(m+n) for recursive
- **Iterative**: Only a few pointers (dummy, current)
- **Recursive**: Call stack depth = min(m, n) in practice
- **Important**: We reuse existing nodes, don't create new ones!

### Why O(m + n):
```
list1: m nodes
list2: n nodes

In worst case (perfect interleaving):
- Compare each node from both lists once
- Total comparisons: min(m, n)
- But we process each node once: m + n visits
- Attaching remaining: O(1)
- Total: O(m + n)
```

---

## Pattern: Two Pointers on LinkedLists

### General Template:
```java
public ListNode mergeSortedLists(ListNode list1, ListNode list2) {
    ListNode dummy = new ListNode(0);
    ListNode current = dummy;
    
    // Process while both have nodes
    while (list1 != null && list2 != null) {
        if (list1.val <= list2.val) {
            current.next = list1;
            list1 = list1.next;
        } else {
            current.next = list2;
            list2 = list2.next;
        }
        current = current.next;
    }
    
    // Handle remaining
    current.next = (list1 != null) ? list1 : list2;
    
    return dummy.next;
}
```

### When to Use:
- ✅ Merging sorted linked lists
- ✅ Finding intersection of two lists
- ✅ Comparing two lists element by element
- ✅ Zipper merge (alternating nodes)

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Constraints** (30 seconds)
> "Both lists are sorted in non-decreasing order. I need to merge them into one sorted list. Can I confirm I should reuse existing nodes (not create new ones)? Yes. Lists can be empty? Yes."

**Step 2: Explain Approach** (1-2 minutes)
> "I'll use an iterative approach with a dummy node:
> 1. Create dummy node to simplify edge cases
> 2. Use two pointers to traverse both lists
> 3. Compare heads, link smaller one to result
> 4. Move pointer of list we took from
> 5. When one list exhausted, attach remaining
> Time: O(m+n), Space: O(1)"

**Step 3: Walk Through Example** (2 minutes)
```java
// list1=[1,2,4], list2=[1,3,4]
// Compare 1,1: take list1's 1
// Compare 2,1: take list2's 1
// Compare 2,3: take list1's 2
// Compare 4,3: take list2's 3
// Compare 4,4: take list1's 4
// list1 empty, attach list2's 4
// Result: 1→1→2→3→4→4
```

**Step 4: Discuss Edge Cases** (30 seconds)
> "Edge cases:
> - Both empty: return null
> - One empty: return the other
> - No interleaving: all from one list first
> - Duplicates: handled correctly with <="

**Step 5: Code** (5-10 minutes)
- Create dummy node
- Initialize current pointer
- While loop with comparison
- Link smaller node
- Attach remaining
- Return dummy.next
- Test with [1,2,4] and [1,3,4]

### Expected Follow-up Questions:

**Q**: "How would you merge k sorted lists?"
**A**: "Use a min-heap (priority queue) containing head of each list. Poll smallest, add to result, push that list's next node. Time: O(N log k) where N = total nodes, k = number of lists. LeetCode #23."

**Q**: "Can you do it recursively?"
**A**: "Yes! Base cases: if list1 null return list2, vice versa. Recursively merge by picking smaller head and linking its next to merge of remaining. Elegant but O(n) space."

**Q**: "What if lists aren't sorted?"
**A**: "Then I'd need to sort them first (O(n log n) each), or merge then sort result (O((m+n) log(m+n))). Sorted input enables linear merge."

**Q**: "How would you merge them in alternating fashion?"
**A**: "Instead of comparing values, just alternate: take from list1, then list2, repeat. Don't need sorted input. When one exhausts, attach remaining."

**Q**: "What about merging two arrays instead?"
**A**: "Two pointers from both ends, merge from back to front if one array has space. Or use extra array, same comparison logic. Same O(m+n) time."

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
     * Merges two sorted linked lists into one sorted list.
     * 
     * Approach: Iterative with dummy node
     * - Use dummy node to simplify edge cases
     * - Compare heads of both lists
     * - Link smaller one to result
     * - Advance pointer of list we took from
     * - Attach remaining nodes when one exhausted
     * 
     * @param list1 Head of first sorted list
     * @param list2 Head of second sorted list
     * @return Head of merged sorted list
     * 
     * Time Complexity: O(m + n) - visit each node once
     * Space Complexity: O(1) - only pointers, reuse nodes
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Dummy node to simplify edge cases
        // Helps avoid special handling for first node
        ListNode dummy = new ListNode(0);
        ListNode current = dummy; // Pointer to build result
        
        // Merge while both lists have nodes
        while (list1 != null && list2 != null) {
            // Compare current heads
            if (list1.val <= list2.val) {
                // list1's head is smaller or equal
                current.next = list1; // Link it to result
                list1 = list1.next;   // Move list1 forward
            } else {
                // list2's head is smaller
                current.next = list2; // Link it to result
                list2 = list2.next;   // Move list2 forward
            }
            
            // Move result pointer forward
            current = current.next;
        }
        
        // At most one list has remaining nodes
        // Attach remaining nodes (if any) - they're already sorted
        // No need to iterate, just link to end
        if (list1 != null) {
            current.next = list1;
        } else {
            current.next = list2;
        }
        
        // Return head of merged list (skip dummy)
        return dummy.next;
    }
}
```

---

## Recursive Solution with Comments

```java
class Solution {
    /**
     * Merges two sorted lists recursively.
     * 
     * Approach: Recursion
     * - Base cases: if one list empty, return the other
     * - Pick smaller head, recursively merge rest
     * - Link smaller head to result of recursive call
     * 
     * Time Complexity: O(m + n)
     * Space Complexity: O(m + n) - call stack
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        // Base case 1: list1 is empty
        if (list1 == null) {
            return list2; // Return all of list2
        }
        
        // Base case 2: list2 is empty
        if (list2 == null) {
            return list1; // Return all of list1
        }
        
        // Recursive case: pick smaller head
        if (list1.val <= list2.val) {
            // Use list1's head
            // Recursively merge list1.next with list2
            list1.next = mergeTwoLists(list1.next, list2);
            return list1; // list1 is head of merged list
        } else {
            // Use list2's head
            // Recursively merge list1 with list2.next
            list2.next = mergeTwoLists(list1, list2.next);
            return list2; // list2 is head of merged list
        }
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
    
    // Test Case 1: Standard merge
    ListNode l1 = createList(new int[]{1,2,4});
    ListNode l2 = createList(new int[]{1,3,4});
    printList(solution.mergeTwoLists(l1, l2));
    // Expected: 1 → 1 → 2 → 3 → 4 → 4 → null
    
    // Test Case 2: One empty
    ListNode l3 = createList(new int[]{});
    ListNode l4 = createList(new int[]{0});
    printList(solution.mergeTwoLists(l3, l4));
    // Expected: 0 → null
    
    // Test Case 3: Both empty
    ListNode l5 = createList(new int[]{});
    ListNode l6 = createList(new int[]{});
    printList(solution.mergeTwoLists(l5, l6));
    // Expected: null
}
```

---

## Key Takeaways

1. ✅ **Dummy node simplifies** edge case handling
2. ✅ **Compare and link** smaller node each iteration
3. ✅ **Move only one pointer** per iteration (the one we picked)
4. ✅ **Attach remaining nodes** in O(1) when one list exhausted
5. ✅ **Return dummy.next** to skip dummy node
6. ✅ **Reuse existing nodes** - don't create new ones
7. ✅ **Use <= for stability** when values equal
8. ✅ **O(m+n) time, O(1) space** is optimal for iterative
9. ✅ **Pattern extends to k lists** with heap
10. ✅ **Similar to merge in merge sort** - fundamental algorithm

---

## Variations & Extensions

After mastering Merge Two Sorted Lists, try these related problems:

1. **Merge k Sorted Lists** (Hard) - Use min-heap to merge k lists
2. **Merge Sorted Array** (Easy) - Array version, merge from back
3. **Sort List** (Medium) - Uses merge sort with linked lists
4. **Add Two Numbers** (Medium) - Similar two-pointer technique
5. **Intersection of Two Linked Lists** (Easy) - Two pointers on lists

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (15-20 minutes)
- [ ] Implement both iterative and recursive
- [ ] Draw out the merging process on paper
- [ ] Test with both lists empty, one empty
- [ ] Practice explaining why dummy node helps
- [ ] Trace through [1,2,4] and [1,3,4]
- [ ] Review in 2 days

---

**Pattern Mastered**: Two Pointers on LinkedLists (Merge) ✅  
**Difficulty**: Easy  
**Time to Master**: 15-25 minutes  
**Importance**: ⭐⭐⭐⭐⭐ Core merge operation

This problem teaches the fundamental merge operation used in merge sort and many other algorithms. Master this and you'll handle all sorted list problems with ease! 🚀
