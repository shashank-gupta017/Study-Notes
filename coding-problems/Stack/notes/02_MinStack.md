# Min Stack (Medium)

## Problem Statement
Design a stack that supports push, pop, top, and retrieving the **minimum element** in constant time.

Implement the `MinStack` class:
- `MinStack()` initializes the stack object.
- `void push(int val)` pushes the element val onto the stack.
- `void pop()` removes the element on the top of the stack.
- `int top()` gets the top element of the stack.
- `int getMin()` retrieves the minimum element in the stack.

You must implement a solution with **O(1) time complexity** for each function.

**LeetCode Link**: [155. Min Stack](https://leetcode.com/problems/min-stack/)

---

## Examples

### Example 1:
```
Input:
["MinStack","push","push","push","getMin","pop","top","getMin"]
[[],[-2],[0],[-3],[],[],[],[]]

Output:
[null,null,null,null,-3,null,0,-2]

Explanation:
MinStack minStack = new MinStack();
minStack.push(-2);
minStack.push(0);
minStack.push(-3);
minStack.getMin(); // return -3
minStack.pop();
minStack.top();    // return 0
minStack.getMin(); // return -2
```

---

## Constraints
- `-2^31 <= val <= 2^31 - 1`
- Methods pop, top and getMin operations will always be called on **non-empty** stacks
- At most `3 * 10^4` calls will be made to push, pop, top, and getMin

---

## Pattern Recognition

This is a **Stack Design / Data Structure Design** problem because:
1. We need to implement a **stack with extra functionality**
2. Constraint requires **O(1) for ALL operations** (challenging!)
3. Must **track minimum** while maintaining stack operations
4. Classic trade-off: **space vs time complexity**

**Key Insight**: 
We can't recalculate minimum each time (that would be O(n)). 
We need to **store minimum information** as we build the stack!

**Two Main Approaches**:
1. **Two Stacks**: One for values, one for minimums
2. **Single Stack with Pairs**: Store (value, current_min) pairs

---

## Approaches

### Approach 1: Two Stacks (OPTIMAL) ⭐
**Idea**: Use one stack for values, another stack to track minimum at each level.

```java
import java.util.*;

class MinStack {
    private Stack<Integer> stack;     // Main stack for values
    private Stack<Integer> minStack;  // Tracks minimum at each level
    
    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }
    
    public void push(int val) {
        stack.push(val);
        
        // Push new minimum onto minStack
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        } else {
            minStack.push(minStack.peek()); // Carry forward current min
        }
    }
    
    public void pop() {
        stack.pop();
        minStack.pop(); // Always pop from both stacks
    }
    
    public int top() {
        return stack.peek();
    }
    
    public int getMin() {
        return minStack.peek();
    }
}
```

**Time Complexity**: O(1) for all operations
**Space Complexity**: O(n) - two stacks, each stores n elements
**Why Optimal**: Meets O(1) requirement, clean implementation

---

### Approach 2: Two Stacks (Space Optimized)
**Idea**: Only push to minStack when new minimum is found.

```java
import java.util.*;

class MinStack {
    private Stack<Integer> stack;
    private Stack<Integer> minStack;
    
    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }
    
    public void push(int val) {
        stack.push(val);
        
        // Only push to minStack if it's a new minimum
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }
    
    public void pop() {
        int val = stack.pop();
        
        // Only pop from minStack if we're removing the current minimum
        if (val == minStack.peek()) {
            minStack.pop();
        }
    }
    
    public int top() {
        return stack.peek();
    }
    
    public int getMin() {
        return minStack.peek();
    }
}
```

**Time Complexity**: O(1) for all operations
**Space Complexity**: O(n) worst case, better average case
**Note**: Saves space when many duplicates or increasing sequence

---

### Approach 3: Single Stack with Pairs
**Idea**: Store (value, current_minimum) as pairs in one stack.

```java
import java.util.*;

class MinStack {
    // Stack stores pairs: [value, current_minimum]
    private Stack<int[]> stack;
    
    public MinStack() {
        stack = new Stack<>();
    }
    
    public void push(int val) {
        if (stack.isEmpty()) {
            stack.push(new int[]{val, val});
        } else {
            int currentMin = Math.min(val, stack.peek()[1]);
            stack.push(new int[]{val, currentMin});
        }
    }
    
    public void pop() {
        stack.pop();
    }
    
    public int top() {
        return stack.peek()[0];
    }
    
    public int getMin() {
        return stack.peek()[1];
    }
}
```

**Time Complexity**: O(1) for all operations
**Space Complexity**: O(n) - stores pairs
**Note**: Cleaner conceptually, each element knows its context

---

### Approach 4: Single Stack with Custom Node Class
**Idea**: Use custom class to store value and minimum.

```java
import java.util.*;

class MinStack {
    private class Node {
        int value;
        int min;
        
        Node(int value, int min) {
            this.value = value;
            this.min = min;
        }
    }
    
    private Stack<Node> stack;
    
    public MinStack() {
        stack = new Stack<>();
    }
    
    public void push(int val) {
        if (stack.isEmpty()) {
            stack.push(new Node(val, val));
        } else {
            int currentMin = Math.min(val, stack.peek().min);
            stack.push(new Node(val, currentMin));
        }
    }
    
    public void pop() {
        stack.pop();
    }
    
    public int top() {
        return stack.peek().value;
    }
    
    public int getMin() {
        return stack.peek().min;
    }
}
```

**Time Complexity**: O(1) for all operations
**Space Complexity**: O(n) - custom objects
**Note**: Most readable, OOP-style

---

### Approach 5: Difference Encoding (Advanced, Space Optimized)
**Idea**: Store difference from minimum to save space.

```java
import java.util.*;

class MinStack {
    private Stack<Long> stack;
    private long min;
    
    public MinStack() {
        stack = new Stack<>();
    }
    
    public void push(int val) {
        if (stack.isEmpty()) {
            stack.push(0L);
            min = val;
        } else {
            long diff = (long)val - min;
            stack.push(diff);
            if (diff < 0) {
                min = val; // New minimum
            }
        }
    }
    
    public void pop() {
        long diff = stack.pop();
        if (diff < 0) {
            min = min - diff; // Restore previous minimum
        }
    }
    
    public int top() {
        long diff = stack.peek();
        if (diff < 0) {
            return (int)min;
        } else {
            return (int)(min + diff);
        }
    }
    
    public int getMin() {
        return (int)min;
    }
}
```

**Time Complexity**: O(1) for all operations
**Space Complexity**: O(n) - one stack
**Note**: Clever but complex, uses long to avoid overflow

---

## Detailed Walkthrough (Approach 1: Two Stacks)

### Example: Operations sequence

```
Initial State:
stack = []
minStack = []

Operation 1: push(-2)
  stack.push(-2)
  minStack is empty, so push -2
  stack = [-2]
  minStack = [-2]
  Current min: -2

Operation 2: push(0)
  stack.push(0)
  0 > minStack.peek() (-2), so push -2 again
  stack = [-2, 0]
  minStack = [-2, -2]
  Current min: -2

Operation 3: push(-3)
  stack.push(-3)
  -3 < minStack.peek() (-2), so push -3
  stack = [-2, 0, -3]
  minStack = [-2, -2, -3]
  Current min: -3

Operation 4: getMin()
  return minStack.peek() = -3
  No change to stacks

Operation 5: pop()
  stack.pop() removes -3
  minStack.pop() removes -3
  stack = [-2, 0]
  minStack = [-2, -2]
  Current min: -2

Operation 6: top()
  return stack.peek() = 0
  No change to stacks

Operation 7: getMin()
  return minStack.peek() = -2
  No change to stacks

Final State:
stack = [-2, 0]
minStack = [-2, -2]
```

---

### Visual Representation

```
Two Stacks Approach:

Push sequence: -2, 0, -3

After push(-2):
stack:    [-2]
minStack: [-2]  ← minimum so far

After push(0):
stack:    [-2, 0]
minStack: [-2, -2]  ← still -2 is min

After push(-3):
stack:    [-2, 0, -3]
minStack: [-2, -2, -3]  ← new min is -3

getMin() → peek minStack → -3 ✓

After pop() (remove -3):
stack:    [-2, 0]
minStack: [-2, -2]  ← min restored to -2

Key Insight:
- minStack[i] = minimum of all elements from bottom to position i
- Both stacks always have same size
- minStack.peek() always gives current minimum in O(1)

---

Space-Optimized Two Stacks:

Push sequence: -2, 0, -3

After push(-2):
stack:    [-2]
minStack: [-2]  ← new min

After push(0):
stack:    [-2, 0]
minStack: [-2]  ← don't push, 0 > -2

After push(-3):
stack:    [-2, 0, -3]
minStack: [-2, -3]  ← new min

pop() removes -3:
  -3 == minStack.peek(), so pop from minStack too
stack:    [-2, 0]
minStack: [-2]

Advantage: Saves space when many non-minimum values
```

---

## Edge Cases to Consider

```java
// Test Case 1: All same values
push(5), push(5), push(5), getMin() → 5
pop(), getMin() → 5

// Test Case 2: Decreasing sequence
push(3), push(2), push(1)
getMin() → 1
pop(), getMin() → 2
pop(), getMin() → 3

// Test Case 3: Increasing sequence
push(1), push(2), push(3)
getMin() → 1 (always)
pop(), getMin() → 1

// Test Case 4: Single element
push(5), getMin() → 5, top() → 5
pop(), push(-1), getMin() → -1

// Test Case 5: Duplicate minimums
push(1), push(2), push(1)
getMin() → 1
pop(), getMin() → 1 (still 1!)

// Test Case 6: Negative numbers
push(-5), push(-10), push(-3)
getMin() → -10

// Test Case 7: Mix of operations
push(1), push(2), top() → 2, getMin() → 1
pop(), top() → 1, getMin() → 1

// Test Case 8: Large numbers
push(Integer.MAX_VALUE), push(Integer.MIN_VALUE)
getMin() → Integer.MIN_VALUE

// Test Case 9: Pop to empty then push
push(1), pop(), push(2), getMin() → 2

// Test Case 10: Multiple mins and pops
push(2), push(1), push(1), push(-1)
pop(), getMin() → 1
pop(), getMin() → 1
pop(), getMin() → 2
```

---

## Common Mistakes to Avoid

### Mistake 1: Not Handling Empty minStack on Push
```java
// ❌ WRONG: Doesn't handle first element
public void push(int val) {
    stack.push(val);
    if (val <= minStack.peek()) { // NullPointerException if empty!
        minStack.push(val);
    }
}

// ✅ CORRECT: Check if empty first
public void push(int val) {
    stack.push(val);
    if (minStack.isEmpty() || val <= minStack.peek()) {
        minStack.push(val);
    }
}
```

### Mistake 2: Using < Instead of <= for Minimum Comparison
```java
// ❌ WRONG: Fails with duplicate minimums
public void push(int val) {
    if (minStack.isEmpty() || val < minStack.peek()) {
        minStack.push(val); // What if val == current min?
    }
}

// Then on pop:
public void pop() {
    if (stack.pop() == minStack.peek()) {
        minStack.pop(); // Might pop too early!
    }
}

// Example: push(1), push(1), pop()
// After first pop, minStack is empty! (wrong)

// ✅ CORRECT: Use <= to handle duplicates
public void push(int val) {
    if (minStack.isEmpty() || val <= minStack.peek()) {
        minStack.push(val);
    }
}
```

### Mistake 3: Forgetting to Pop from minStack
```java
// ❌ WRONG: Only popping from main stack
public void pop() {
    stack.pop();
    // Forgot to pop from minStack!
}

// ✅ CORRECT: Pop from both (Approach 1)
public void pop() {
    stack.pop();
    minStack.pop();
}

// OR for space-optimized (Approach 2):
public void pop() {
    int val = stack.pop();
    if (val == minStack.peek()) {
        minStack.pop();
    }
}
```

### Mistake 4: Returning Wrong Value from top()
```java
// ❌ WRONG: Returning from minStack
public int top() {
    return minStack.peek(); // This returns minimum, not top!
}

// ✅ CORRECT: Return from main stack
public int top() {
    return stack.peek();
}
```

### Mistake 5: Recalculating Minimum on getMin()
```java
// ❌ WRONG: O(n) operation!
public int getMin() {
    int min = Integer.MAX_VALUE;
    for (int val : stack) {
        min = Math.min(min, val);
    }
    return min; // This defeats the purpose!
}

// ✅ CORRECT: O(1) with minStack
public int getMin() {
    return minStack.peek();
}
```

### Mistake 6: Not Comparing with .equals() for Integer Objects
```java
// ❌ POTENTIALLY WRONG: Reference comparison for large integers
Stack<Integer> stack = new Stack<>();
if (stack.pop() == minStack.peek()) { // Can fail for large numbers!
}

// ✅ SAFER: Use .equals() or unbox to int
int val = stack.pop();
if (val == minStack.peek()) { // Unboxed, safe
}
```

---

## Why This Design Works

### The Core Challenge:
```
Regular stack operations: push, pop, top → O(1)
Finding minimum: iterate all elements → O(n)

How to make minimum O(1)?
→ Pre-calculate and store minimum information!
```

### Two Stacks Solution Intuition:
```
Key Idea: At any point, we need to know minimum of current stack state

minStack stores: "What is the minimum if stack has N elements?"

Example:
stack:    [5,  3,  7,  1,  4]
minStack: [5,  3,  3,  1,  1]
           ↑   ↑   ↑   ↑   ↑
           min min min min min
           of  of  of  of  of
           [5] [5,3] [5,3,7] [5,3,7,1] [all]

When we pop 4:
- Remove from stack: [5, 3, 7, 1]
- Remove from minStack: [5, 3, 3, 1]
- New minimum: 1 (automatically correct!)
```

### Why Both Stacks Must Stay Synchronized:
```
If sizes differ, we lose track of what minimum corresponds to current state

Example of WRONG approach:
stack:    [5, 3, 7] (size 3)
minStack: [5, 3]    (size 2) ← not synchronized!

What is current minimum? minStack.peek() = 3
But we don't know if this applies to current stack state!
```

---

## Optimization Techniques

### Optimization 1: Deque Instead of Stack
```java
// ArrayDeque is faster than Stack (synchronized)
private Deque<Integer> stack;
private Deque<Integer> minStack;

public MinStack() {
    stack = new ArrayDeque<>();
    minStack = new ArrayDeque<>();
}

// Use push/pop/peek as normal
```

### Optimization 2: Primitive Array (For Fixed Size)
```java
// If max size is known, use arrays
private int[] stack;
private int[] minStack;
private int size;

public MinStack(int capacity) {
    stack = new int[capacity];
    minStack = new int[capacity];
    size = 0;
}

public void push(int val) {
    stack[size] = val;
    minStack[size] = (size == 0) ? val : Math.min(val, minStack[size - 1]);
    size++;
}
```

### Optimization 3: Linked List for Memory Efficiency
```java
// Each node knows its minimum
private class Node {
    int val;
    int min;
    Node next;
}

private Node head;

public void push(int val) {
    Node node = new Node();
    node.val = val;
    node.min = (head == null) ? val : Math.min(val, head.min);
    node.next = head;
    head = node;
}
```

---

## Complexity Analysis

### Time Complexity: O(1) for ALL operations
- **push()**: Two stack pushes → O(1)
- **pop()**: Two stack pops → O(1)
- **top()**: One peek → O(1)
- **getMin()**: One peek → O(1)

### Space Complexity: O(n)
- **Two Stacks (Always push to minStack)**: 2n space → O(n)
- **Two Stacks (Optimized)**: Between n and 2n → O(n)
- **Single Stack with Pairs**: 2n space (but one structure) → O(n)

### Comparison of Approaches:

| Approach | Time (all ops) | Space | Pros | Cons |
|----------|----------------|-------|------|------|
| Two Stacks (Always push) | O(1) | 2n | Simple, clear | More space |
| Two Stacks (Optimized) | O(1) | n to 2n | Saves space | Slightly complex pop |
| Single Stack (Pairs) | O(1) | 2n | Clean concept | More memory per element |
| Difference Encoding | O(1) | n | Best space | Complex, overflow issues |

---

## Pattern: Stack with Additional Tracking

### General Template:
```java
class EnhancedStack {
    private Stack<Integer> mainStack;
    private Stack<Integer> trackingStack; // Tracks some property
    
    public void push(int val) {
        mainStack.push(val);
        // Update tracking based on property
        updateTracking(val);
    }
    
    public void pop() {
        mainStack.pop();
        // Sync tracking stack
        updateTrackingOnPop();
    }
    
    public int getTrackedValue() {
        return trackingStack.peek(); // O(1)
    }
}
```

### When to Use:
- ✅ Need O(1) retrieval of aggregate value (min, max, average)
- ✅ Can afford O(n) space for tracking
- ✅ Stack operations must remain O(1)

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Requirements** (30 seconds)
> "I need to implement a stack with O(1) push, pop, top, and getMin. The key challenge is getMin - normally finding minimum is O(n). I need to track minimum information as I build the stack."

**Step 2: Explain Approach** (1-2 minutes)
> "I'll use two stacks:
> 1. Main stack: stores all values normally
> 2. Min stack: tracks minimum at each level
> When pushing: update min stack with current minimum
> When popping: pop from both stacks
> getMin: peek min stack → O(1)
> All operations remain O(1), space is O(n)."

**Step 3: Walk Through Example** (2 minutes)
```java
// push(-2): stacks = [-2], mins = [-2]
// push(0):  stacks = [-2,0], mins = [-2,-2]
// push(-3): stacks = [-2,0,-3], mins = [-2,-2,-3]
// getMin(): return mins.peek() = -3
// pop():    stacks = [-2,0], mins = [-2,-2]
// getMin(): return mins.peek() = -2
```

**Step 4: Discuss Trade-offs** (1 minute)
> "Trade-off is space for time. I use O(n) extra space to achieve O(1) getMin. Alternative: recalculate minimum each time but that's O(n) time. For most use cases, the space trade-off is worth it."

**Step 5: Mention Optimizations** (30 seconds)
> "I can optimize space by only pushing to minStack when a new minimum is found. Pop logic becomes slightly more complex, but saves space on average."

**Step 6: Code** (10-15 minutes)
- Implement constructor with two stacks
- push: update both stacks
- pop: remove from both
- top: peek main stack
- getMin: peek min stack
- Test with example

### Expected Follow-up Questions:

**Q**: "Can you implement MaxStack too?"
**A**: "Yes, exact same approach! Just track maximum instead of minimum in the second stack. Use >= for comparisons instead of <=."

**Q**: "What if we need both min and max?"
**A**: "Use three stacks: main, min, and max. Or use pairs/nodes that store (val, min, max). Still O(1) for all operations."

**Q**: "How would you implement with a linked list?"
**A**: "Each node stores (value, current_min). Head is the top. Push: create node with min = Math.min(val, head.min). All ops still O(1)."

**Q**: "What about thread safety?"
**A**: "Add synchronized keyword to methods, or use ConcurrentLinkedDeque. Need to ensure atomicity of operations involving both stacks."

**Q**: "Can you do it with just one stack?"
**A**: "Yes, store (value, min) pairs. Or use difference encoding (complex). Single stack with pairs is cleaner and still O(n) space."

---

## Complete Solution with Comments

```java
import java.util.*;

/**
 * MinStack: Stack supporting O(1) retrieval of minimum element.
 * 
 * Approach: Two Stacks
 * - Main stack: stores all values
 * - Min stack: tracks minimum at each level
 * 
 * Key Insight: minStack[i] = minimum of all elements from bottom to i
 * Both stacks stay synchronized (same size)
 */
class MinStack {
    private Stack<Integer> stack;     // Main stack for all values
    private Stack<Integer> minStack;  // Tracks minimum at each level
    
    /**
     * Initialize the stack object.
     */
    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }
    
    /**
     * Pushes element onto stack.
     * Updates minimum tracking.
     * 
     * Time: O(1)
     */
    public void push(int val) {
        // Always push to main stack
        stack.push(val);
        
        // Update min stack
        // If empty or new minimum, push val
        // Otherwise, carry forward current minimum
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        } else {
            minStack.push(minStack.peek());
        }
    }
    
    /**
     * Removes the element on top of the stack.
     * Maintains min stack synchronization.
     * 
     * Time: O(1)
     */
    public void pop() {
        // Pop from both stacks to maintain synchronization
        stack.pop();
        minStack.pop();
    }
    
    /**
     * Gets the top element of the stack.
     * 
     * Time: O(1)
     */
    public int top() {
        return stack.peek();
    }
    
    /**
     * Retrieves the minimum element in the stack.
     * 
     * Time: O(1) - just peek min stack!
     */
    public int getMin() {
        return minStack.peek();
    }
}
```

---

## Space-Optimized Solution

```java
import java.util.*;

class MinStack {
    private Stack<Integer> stack;
    private Stack<Integer> minStack;
    
    public MinStack() {
        stack = new Stack<>();
        minStack = new Stack<>();
    }
    
    public void push(int val) {
        stack.push(val);
        
        // Only push to minStack if new minimum
        // This saves space when many non-minimum values
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }
    
    public void pop() {
        int val = stack.pop();
        
        // Only pop from minStack if we're removing the current minimum
        // Use equals for safety with Integer objects
        if (val == minStack.peek()) {
            minStack.pop();
        }
    }
    
    public int top() {
        return stack.peek();
    }
    
    public int getMin() {
        return minStack.peek();
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public static void main(String[] args) {
    // Test Case 1: Basic operations
    MinStack minStack = new MinStack();
    minStack.push(-2);
    minStack.push(0);
    minStack.push(-3);
    System.out.println(minStack.getMin()); // -3
    minStack.pop();
    System.out.println(minStack.top());    // 0
    System.out.println(minStack.getMin()); // -2
    
    // Test Case 2: Duplicate minimums
    MinStack minStack2 = new MinStack();
    minStack2.push(1);
    minStack2.push(1);
    minStack2.push(1);
    System.out.println(minStack2.getMin()); // 1
    minStack2.pop();
    System.out.println(minStack2.getMin()); // 1
    
    // Test Case 3: Decreasing values
    MinStack minStack3 = new MinStack();
    minStack3.push(3);
    minStack3.push(2);
    minStack3.push(1);
    System.out.println(minStack3.getMin()); // 1
    minStack3.pop();
    System.out.println(minStack3.getMin()); // 2
}
```

---

## Key Takeaways

1. ✅ **O(1) getMin requires pre-computation** - can't calculate on demand
2. ✅ **Two stacks is clean solution** - main stack + min tracking
3. ✅ **Keep stacks synchronized** - same size ensures correct state
4. ✅ **Use <= not <** for handling duplicate minimums
5. ✅ **Space-time trade-off** - O(n) space for O(1) operations
6. ✅ **Approach extends to MaxStack** - track maximum instead
7. ✅ **Alternative: single stack with pairs** - cleaner conceptually
8. ✅ **Pop must update both stacks** - maintain synchronization
9. ✅ **Check isEmpty before push** - avoid NullPointerException
10. ✅ **Classic design problem** - tests understanding of data structures

---

## Variations & Extensions

After mastering Min Stack, try these related design problems:

1. **Max Stack** (Medium) - Track maximum instead of minimum
2. **Min/Max Stack** (Medium) - Track both min and max
3. **Stack with Increment Operation** (Medium) - Efficient bulk increment
4. **Implement Queue using Stacks** (Easy) - Stack-based queue
5. **LRU Cache** (Medium) - HashMap + Doubly Linked List

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (20-30 minutes)
- [ ] Implement both versions (always push vs space-optimized)
- [ ] Test with duplicate minimums
- [ ] Try implementing with single stack + pairs
- [ ] Practice explaining the O(1) requirement
- [ ] Draw out the two stacks on paper
- [ ] Review in 3 days

---

**Pattern Mastered**: Stack Design with O(1) Operations ✅  
**Difficulty**: Medium  
**Time to Master**: 25-35 minutes  
**Importance**: ⭐⭐⭐⭐⭐ Classic design problem

This problem teaches the essential skill of augmenting data structures to support additional operations efficiently. The space-time trade-off insight applies to many other design problems! 🚀
