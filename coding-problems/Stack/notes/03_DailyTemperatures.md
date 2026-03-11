# Daily Temperatures (Medium)

## Problem Statement
Given an array of integers `temperatures` represents the daily temperatures, return an array `answer` such that `answer[i]` is the number of days you have to wait after the `i`th day to get a warmer temperature. If there is no future day for which this is possible, keep `answer[i] == 0` instead.

**LeetCode Link**: [739. Daily Temperatures](https://leetcode.com/problems/daily-temperatures/)

---

## Examples

### Example 1:
```
Input: temperatures = [73,74,75,71,69,72,76,73]
Output: [1,1,4,2,1,1,0,0]
Explanation:
Day 0 (73°): next warmer is day 1 (74°) → wait 1 day
Day 1 (74°): next warmer is day 2 (75°) → wait 1 day
Day 2 (75°): next warmer is day 6 (76°) → wait 4 days
Day 3 (71°): next warmer is day 5 (72°) → wait 2 days
Day 4 (69°): next warmer is day 5 (72°) → wait 1 day
Day 5 (72°): next warmer is day 6 (76°) → wait 1 day
Day 6 (76°): no warmer day → 0
Day 7 (73°): no warmer day → 0
```

### Example 2:
```
Input: temperatures = [30,40,50,60]
Output: [1,1,1,0]
Explanation: Each day (except last) has next day warmer.
```

### Example 3:
```
Input: temperatures = [30,60,90]
Output: [1,1,0]
Explanation: Strictly increasing, each looks to next day.
```

---

## Constraints
- `1 <= temperatures.length <= 10^5`
- `30 <= temperatures[i] <= 100`

---

## Pattern Recognition

This is a **Monotonic Stack** problem because:
1. We need to find the **next greater element** for each position
2. We need to look **forward** in the array efficiently
3. We can use a **stack to track indices** of unresolved temperatures
4. When we find a warmer day, we **pop and resolve** waiting days

**Key Insight**: 
- Stack stores **indices** of days waiting for warmer temperature
- Stack is **monotonically decreasing** (temperatures, not indices)
- When current temp > stack top's temp → found answer, pop and calculate distance

**Monotonic Stack Pattern**: Perfect for "next greater/smaller element" problems!

---

## Approaches

### Approach 1: Brute Force (NOT RECOMMENDED)
**Idea**: For each day, scan forward to find next warmer temperature.

```java
class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        
        for (int i = 0; i < n; i++) {
            // Look for next warmer day
            for (int j = i + 1; j < n; j++) {
                if (temperatures[j] > temperatures[i]) {
                    answer[i] = j - i;
                    break; // Found, move to next i
                }
            }
            // If not found, answer[i] remains 0 (default)
        }
        
        return answer;
    }
}
```

**Time Complexity**: O(n²) - nested loops
**Space Complexity**: O(1) - only output array
**Problem**: Too slow for large inputs! TLE on LeetCode.

---

### Approach 2: Monotonic Stack (OPTIMAL) ⭐
**Idea**: Use stack to track indices of unresolved days, pop when warmer found.

```java
import java.util.*;

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Stack<Integer> stack = new Stack<>(); // Stores indices
        
        for (int i = 0; i < n; i++) {
            // While current temp is warmer than stack top's temp
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prevIndex = stack.pop();
                answer[prevIndex] = i - prevIndex; // Days to wait
            }
            
            // Push current index to stack
            stack.push(i);
        }
        
        // Remaining indices in stack have no warmer day (answer = 0, already default)
        return answer;
    }
}
```

**Time Complexity**: O(n) - each index pushed and popped once
**Space Complexity**: O(n) - stack can hold n indices worst case
**Why Optimal**: Linear time, best possible

---

### Approach 3: Monotonic Stack with Deque (Modern Java)
**Idea**: Use ArrayDeque instead of Stack for better performance.

```java
import java.util.*;

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prevIndex = stack.pop();
                answer[prevIndex] = i - prevIndex;
            }
            stack.push(i);
        }
        
        return answer;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(n) - deque storage
**Note**: ArrayDeque is faster than Stack in Java

---

### Approach 4: Backward Iteration (Alternative)
**Idea**: Iterate from right to left, use stack to find next warmer.

```java
import java.util.*;

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Stack<Integer> stack = new Stack<>();
        
        // Iterate from right to left
        for (int i = n - 1; i >= 0; i--) {
            // Pop elements smaller or equal to current temp
            while (!stack.isEmpty() && temperatures[i] >= temperatures[stack.peek()]) {
                stack.pop();
            }
            
            // If stack not empty, top is next warmer day
            if (!stack.isEmpty()) {
                answer[i] = stack.peek() - i;
            }
            
            stack.push(i);
        }
        
        return answer;
    }
}
```

**Time Complexity**: O(n) - single pass
**Space Complexity**: O(n) - stack storage
**Note**: Different perspective, same efficiency

---

### Approach 5: Optimized with Array (No Stack)
**Idea**: For small temperature range, track last seen index for each temp.

```java
class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        int[] next = new int[101]; // Temperature range 30-100
        
        // Iterate from right to left
        for (int i = n - 1; i >= 0; i--) {
            int warmerIndex = Integer.MAX_VALUE;
            
            // Check all temperatures warmer than current
            for (int t = temperatures[i] + 1; t <= 100; t++) {
                if (next[t] != 0) {
                    warmerIndex = Math.min(warmerIndex, next[t]);
                }
            }
            
            if (warmerIndex != Integer.MAX_VALUE) {
                answer[i] = warmerIndex - i;
            }
            
            next[temperatures[i]] = i;
        }
        
        return answer;
    }
}
```

**Time Complexity**: O(n × k) where k = temperature range (71 here)
**Space Complexity**: O(k) - next array
**Note**: Works well for small temperature range, but stack is cleaner

---

## Detailed Walkthrough (Approach 2: Monotonic Stack)

### Example: temperatures = [73,74,75,71,69,72,76,73]

```
Initial: answer = [0,0,0,0,0,0,0,0], stack = []

i=0, temp=73:
  Stack empty, push 0
  stack = [0]
  answer = [0,0,0,0,0,0,0,0]

i=1, temp=74:
  74 > temps[0]=73 → Found warmer!
  Pop 0: answer[0] = 1 - 0 = 1
  Stack empty now, push 1
  stack = [1]
  answer = [1,0,0,0,0,0,0,0]

i=2, temp=75:
  75 > temps[1]=74 → Found warmer!
  Pop 1: answer[1] = 2 - 1 = 1
  Stack empty now, push 2
  stack = [2]
  answer = [1,1,0,0,0,0,0,0]

i=3, temp=71:
  71 < temps[2]=75 → Not warmer
  Push 3
  stack = [2,3]
  answer = [1,1,0,0,0,0,0,0]

i=4, temp=69:
  69 < temps[3]=71 → Not warmer
  Push 4
  stack = [2,3,4]
  answer = [1,1,0,0,0,0,0,0]

i=5, temp=72:
  72 > temps[4]=69 → Found warmer!
  Pop 4: answer[4] = 5 - 4 = 1
  72 > temps[3]=71 → Found warmer!
  Pop 3: answer[3] = 5 - 3 = 2
  72 < temps[2]=75 → Not warmer, stop
  Push 5
  stack = [2,5]
  answer = [1,1,0,2,1,0,0,0]

i=6, temp=76:
  76 > temps[5]=72 → Found warmer!
  Pop 5: answer[5] = 6 - 5 = 1
  76 > temps[2]=75 → Found warmer!
  Pop 2: answer[2] = 6 - 2 = 4
  Stack empty now, push 6
  stack = [6]
  answer = [1,1,4,2,1,1,0,0]

i=7, temp=73:
  73 < temps[6]=76 → Not warmer
  Push 7
  stack = [6,7]
  answer = [1,1,4,2,1,1,0,0]

Final: Indices 6,7 remain in stack (no warmer day) → answer[6]=0, answer[7]=0
Result: [1,1,4,2,1,1,0,0]
```

---

### Visual Representation

```
temperatures = [73, 74, 75, 71, 69, 72, 76, 73]
indices      = [ 0,  1,  2,  3,  4,  5,  6,  7]

Processing with Monotonic Stack:

i=0 (73): stack=[0]
i=1 (74): 74>73 pop(0), stack=[1]
i=2 (75): 75>74 pop(1), stack=[2]
i=3 (71): 71<75, stack=[2,3]
i=4 (69): 69<71, stack=[2,3,4]
i=5 (72): 72>69 pop(4), 72>71 pop(3), 72<75, stack=[2,5]
i=6 (76): 76>72 pop(5), 76>75 pop(2), stack=[6]
i=7 (73): 73<76, stack=[6,7]

Stack State Visualization:

         [2]
         [2,3]
         [2,3,4]    ← Monotonic decreasing stack
         [2,5]      ← Popped 3,4 when 72 arrived
         [6]        ← Popped 2,5 when 76 arrived
         [6,7]      ← Final state

Key: Stack maintains decreasing temperature order
When warmer temp arrives, pop all cooler temps (they found their answer!)

Answer Calculation:
index 0: answer at i=1 → 1-0 = 1
index 1: answer at i=2 → 2-1 = 1
index 2: answer at i=6 → 6-2 = 4
index 3: answer at i=5 → 5-3 = 2
index 4: answer at i=5 → 5-4 = 1
index 5: answer at i=6 → 6-5 = 1
index 6: no answer → 0
index 7: no answer → 0
```

---

## Edge Cases to Consider

```java
// Test Case 1: All increasing temperatures
Input: temperatures = [30, 40, 50, 60, 70]
Output: [1, 1, 1, 1, 0]
// Each day points to next day, last day has no warmer

// Test Case 2: All decreasing temperatures
Input: temperatures = [100, 90, 80, 70, 60]
Output: [0, 0, 0, 0, 0]
// No day has a warmer future day

// Test Case 3: All same temperatures
Input: temperatures = [50, 50, 50, 50]
Output: [0, 0, 0, 0]
// No strictly warmer day exists

// Test Case 4: Single day
Input: temperatures = [75]
Output: [0]
// Only one day, no future days

// Test Case 5: Two days increasing
Input: temperatures = [30, 40]
Output: [1, 0]
// First points to second, second has none

// Test Case 6: Two days decreasing
Input: temperatures = [40, 30]
Output: [0, 0]
// No warmer days

// Test Case 7: Peak in middle
Input: temperatures = [30, 40, 50, 40, 30]
Output: [1, 1, 0, 0, 0]
// Days before peak point to peak, after peak have none

// Test Case 8: Valley in middle
Input: temperatures = [50, 40, 30, 40, 50]
Output: [4, 3, 1, 1, 0]
// Valley days point to recovery

// Test Case 9: Multiple peaks
Input: temperatures = [40, 50, 40, 60, 50, 70]
Output: [1, 2, 1, 2, 1, 0]
// Complex pattern with multiple local maxima

// Test Case 10: Large jump at end
Input: temperatures = [30, 30, 30, 30, 100]
Output: [4, 3, 2, 1, 0]
// All point to the final big jump

// Test Case 11: Zigzag pattern
Input: temperatures = [30, 40, 35, 45, 40, 50]
Output: [1, 3, 1, 2, 1, 0]
// Up and down pattern
```

---

## Common Mistakes to Avoid

### Mistake 1: Not Checking Stack Empty Before Peek
```java
// ❌ WRONG: EmptyStackException if stack is empty
while (temperatures[i] > temperatures[stack.peek()]) {
    stack.pop();
}

// ✅ CORRECT: Check isEmpty first
while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
    stack.pop();
}
```

### Mistake 2: Pushing Temperature Instead of Index
```java
// ❌ WRONG: Pushing temperature value
stack.push(temperatures[i]); // Can't calculate distance later!

// ✅ CORRECT: Push index
stack.push(i); // Can access both index and temperature
```

### Mistake 3: Wrong Distance Calculation
```java
// ❌ WRONG: Not subtracting indices correctly
int prevIndex = stack.pop();
answer[prevIndex] = prevIndex - i; // Negative! Wrong order!

// ✅ CORRECT: Current minus previous
answer[prevIndex] = i - prevIndex;
```

### Mistake 4: Using >= Instead of >
```java
// ❌ WRONG: Includes equal temperatures
while (!stack.isEmpty() && temperatures[i] >= temperatures[stack.peek()]) {
    // Equal temp doesn't count as "warmer"
}

// ✅ CORRECT: Strictly greater
while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
    // Only strictly warmer temps
}
```

### Mistake 5: Not Initializing Answer Array
```java
// ❌ WRONG: Forgetting to initialize (though Java does this)
int[] answer; // null pointer!

// ✅ CORRECT: Initialize with size
int[] answer = new int[n]; // All elements default to 0
```

### Mistake 6: Forgetting to Push Current Index
```java
// ❌ WRONG: Only popping, not pushing
while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
    stack.pop();
}
// Forgot: stack.push(i);

// ✅ CORRECT: Push after popping
while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
    stack.pop();
}
stack.push(i); // Current index needs to wait too
```

---

## Why Monotonic Stack Works

### The Core Insight:

**Problem**: For each day, find the next warmer day.

**Naive approach**: For each day, scan forward → O(n²)

**Key Observation**:
```
If day i has temp T1, and day j has temp T2 where:
- j > i (j is after i)
- T2 <= T1 (j is not warmer)

Then day j will NEVER be the answer for any day before i!

Why? Any day before i looking for warmer temp will find i first.

Example:
temps = [75, 71, 69, ...]
        
If we're at 71, we don't need to remember 75.
75 is warmer, so any future day comparing will hit 75 first.
We only need to remember "unresolved" days in decreasing order.
```

### Monotonic Stack Property:
```
Stack maintains indices in decreasing temperature order (bottom to top)

When new temp arrives:
1. If warmer than stack top → pop (found answer for that day!)
2. Keep popping while warmer (multiple days might get resolved)
3. Push current index (it's waiting for its warmer day)

The stack naturally keeps only "potentially useful" indices.
```

### Visual Example:
```
temps = [73, 74, 75, 71, 69, 72, 76, 73]

At i=4 (temp=69):
stack = [2(75), 3(71), 4(69)] ← Decreasing temps!

When i=5 (temp=72) arrives:
- 72 > 69? Yes! Pop 4, answer[4] = 1
- 72 > 71? Yes! Pop 3, answer[3] = 2
- 72 > 75? No! Stop popping
- Push 5

stack = [2(75), 5(72)] ← Still decreasing!
```

---

## Optimization Techniques

### Optimization 1: Use ArrayDeque Instead of Stack
```java
// ArrayDeque is faster (not synchronized like Stack)
Deque<Integer> stack = new ArrayDeque<>();
// Use push/pop/peek as normal
```

### Optimization 2: Early Exit for All Decreasing
```java
// Check if array is monotonically decreasing
boolean isDecreasing = true;
for (int i = 1; i < temperatures.length; i++) {
    if (temperatures[i] > temperatures[i-1]) {
        isDecreasing = false;
        break;
    }
}

if (isDecreasing) {
    return new int[temperatures.length]; // All zeros
}
```

### Optimization 3: Use Primitive Array Stack (For Performance)
```java
// For very large inputs, array-based stack can be faster
int[] stack = new int[n];
int top = -1;

for (int i = 0; i < n; i++) {
    while (top >= 0 && temperatures[i] > temperatures[stack[top]]) {
        int prevIndex = stack[top--];
        answer[prevIndex] = i - prevIndex;
    }
    stack[++top] = i;
}
```

---

## Complexity Analysis

### Time Complexity: O(n)
- **Outer loop**: Iterates n times
- **Inner while loop**: Each element pushed once and popped once
- **Total operations**: At most 2n (each index processed twice max)
- **Amortized**: O(n)

### Space Complexity: O(n)
- **Stack**: Worst case holds all n indices
- **Example**: Decreasing sequence [100,90,80,70,60]
- All indices pushed, none popped until end
- **Output array**: O(n) but doesn't count toward space complexity

### Why O(n) not O(n²):
```
The while loop might seem to make it O(n²), but:

Each index is:
1. Pushed onto stack exactly once
2. Popped from stack at most once

Total pushes: n
Total pops: at most n
Total operations: 2n = O(n)

It's amortized O(1) per element!
```

---

## Pattern: Monotonic Stack Template

### General Template for Next Greater Element:
```java
public int[] nextGreaterElement(int[] nums) {
    int n = nums.length;
    int[] answer = new int[n];
    Stack<Integer> stack = new Stack<>();
    
    for (int i = 0; i < n; i++) {
        // Pop elements smaller than current
        while (!stack.isEmpty() && nums[i] > nums[stack.peek()]) {
            int prevIndex = stack.pop();
            answer[prevIndex] = i; // or calculate distance
        }
        
        stack.push(i);
    }
    
    return answer;
}
```

### When to Use Monotonic Stack:
- ✅ Find next greater/smaller element
- ✅ Find previous greater/smaller element
- ✅ Calculate spans or distances
- ✅ Histogram/trapping water problems

### Variations:
```java
// Next Greater: pop when current > stack top
while (!stack.isEmpty() && nums[i] > nums[stack.peek()])

// Next Smaller: pop when current < stack top
while (!stack.isEmpty() && nums[i] < nums[stack.peek()])

// Previous Greater: iterate right to left with >
// Previous Smaller: iterate right to left with <
```

---

## Interview Tips

### What to Say During Interview:

**Step 1: Clarify Constraints** (30 seconds)
> "I need to find for each day, how many days until a warmer temperature. Temperature range is 30-100. Array can be up to 10^5 elements. Brute force O(n²) will likely time out."

**Step 2: Explain Approach** (1-2 minutes)
> "I'll use a Monotonic Stack (decreasing order):
> 1. Stack stores indices of days waiting for warmer temp
> 2. For each day, while current temp > stack top's temp:
>    - Pop index, calculate distance, store answer
> 3. Push current index onto stack
> 4. Remaining indices have no warmer day (answer = 0)
> Time: O(n), Space: O(n)"

**Step 3: Walk Through Key Insight** (1 minute)
> "Key insight: The stack maintains days in decreasing temperature order. When a warmer day arrives, it resolves all cooler days in the stack. Each index is pushed once and popped at most once, making it O(n) amortized."

**Step 4: Walk Through Example** (2-3 minutes)
```java
// [73,74,75,71,69,72,76,73]
// Day 0 (73): stack=[0]
// Day 1 (74): 74>73, pop 0, answer[0]=1, stack=[1]
// Day 2 (75): 75>74, pop 1, answer[1]=1, stack=[2]
// Day 3 (71): stack=[2,3]
// Day 4 (69): stack=[2,3,4]
// Day 5 (72): 72>69 pop 4, 72>71 pop 3, stack=[2,5]
// ...continues
```

**Step 5: Code** (10-15 minutes)
- Initialize answer array and stack
- Loop through temperatures
- While loop to pop smaller temps
- Calculate and store answer
- Push current index
- Test with example

### Expected Follow-up Questions:

**Q**: "How would you find previous warmer day instead?"
**A**: "Same approach but iterate right to left. Or with left-to-right, pop and store answer differently - when pushing, check if stack not empty, that's previous greater."

**Q**: "What if we need next cooler temperature?"
**A**: "Change condition from > to <. Stack becomes monotonically increasing. Same O(n) approach."

**Q**: "Can you optimize for the specific temperature range?"
**A**: "Yes, since temps are 30-100, I could track last seen index for each temp value. For each day, check temps above current to find closest. O(n × k) where k=71, works but stack is cleaner."

**Q**: "How would you handle circular array?"
**A**: "Process array twice (simulate circular). Use modulo for indices. Or process once and for remaining stack elements, check wrapped portion."

**Q**: "What about finding temperatures that are k degrees warmer?"
**A**: "Instead of checking > stack top, check if >= stack top + k. Same stack approach works."

---

## Complete Solution with Comments

```java
import java.util.*;

class Solution {
    /**
     * Finds the number of days until a warmer temperature for each day.
     * 
     * Approach: Monotonic Stack (Decreasing)
     * - Stack stores indices of days waiting for warmer temperature
     * - When warmer day found, pop and calculate distance
     * - Each index pushed once, popped at most once → O(n) amortized
     * 
     * @param temperatures Array of daily temperatures
     * @return Array of days to wait for warmer temperature
     * 
     * Time Complexity: O(n) - each element processed at most twice
     * Space Complexity: O(n) - stack can hold up to n indices
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n]; // Default all zeros (no warmer day)
        Stack<Integer> stack = new Stack<>(); // Stores indices
        
        // Process each day
        for (int i = 0; i < n; i++) {
            // Current temperature
            int currentTemp = temperatures[i];
            
            // Resolve all days cooler than current day
            // Pop while current temp is warmer than stack top's temp
            while (!stack.isEmpty() && currentTemp > temperatures[stack.peek()]) {
                int prevIndex = stack.pop();
                
                // Calculate days to wait: current day - previous day
                answer[prevIndex] = i - prevIndex;
            }
            
            // Push current index to stack (waiting for its warmer day)
            stack.push(i);
        }
        
        // Remaining indices in stack have no warmer day
        // answer[index] = 0 (already default value)
        
        return answer;
    }
}
```

---

## Alternative: Backward Iteration

```java
import java.util.*;

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Stack<Integer> stack = new Stack<>();
        
        // Iterate from right to left
        for (int i = n - 1; i >= 0; i--) {
            // Remove days that are not warmer
            while (!stack.isEmpty() && temperatures[i] >= temperatures[stack.peek()]) {
                stack.pop();
            }
            
            // If stack not empty, top is next warmer day
            if (!stack.isEmpty()) {
                answer[i] = stack.peek() - i;
            }
            
            // Push current index
            stack.push(i);
        }
        
        return answer;
    }
}
```

---

## Testing Strategy

### Test with Main Method:
```java
public static void main(String[] args) {
    Solution solution = new Solution();
    
    // Test Case 1: Standard case
    int[] temps1 = {73,74,75,71,69,72,76,73};
    System.out.println(Arrays.toString(solution.dailyTemperatures(temps1)));
    // Expected: [1,1,4,2,1,1,0,0]
    
    // Test Case 2: All increasing
    int[] temps2 = {30,40,50,60};
    System.out.println(Arrays.toString(solution.dailyTemperatures(temps2)));
    // Expected: [1,1,1,0]
    
    // Test Case 3: All decreasing
    int[] temps3 = {60,50,40,30};
    System.out.println(Arrays.toString(solution.dailyTemperatures(temps3)));
    // Expected: [0,0,0,0]
    
    // Test Case 4: Single element
    int[] temps4 = {75};
    System.out.println(Arrays.toString(solution.dailyTemperatures(temps4)));
    // Expected: [0]
}
```

---

## Key Takeaways

1. ✅ **Monotonic stack** is perfect for next greater/smaller problems
2. ✅ **Store indices** not values (need to calculate distance)
3. ✅ **Stack maintains decreasing order** (bottom to top)
4. ✅ **Each element pushed once, popped once** → O(n) amortized
5. ✅ **Check isEmpty before peek** to avoid exception
6. ✅ **Pop while current > stack top** to find all resolutions
7. ✅ **Push current index** after popping (it's waiting too)
8. ✅ **Default answer is 0** (no warmer day found)
9. ✅ **Pattern extends** to many "next element" problems
10. ✅ **Classic monotonic stack problem** - master this pattern!

---

## Variations & Extensions

After mastering Daily Temperatures, try these monotonic stack problems:

1. **Next Greater Element I** (Easy) - Find next greater in another array
2. **Next Greater Element II** (Medium) - Circular array version
3. **Online Stock Span** (Medium) - Count days with lower/equal prices
4. **Largest Rectangle in Histogram** (Hard) - Uses monotonic stack
5. **Trapping Rain Water** (Hard) - Can be solved with monotonic stack

---

## Next Steps

- [ ] Solve this problem on LeetCode
- [ ] Time yourself (25-35 minutes)
- [ ] Trace through [73,74,75,71,69,72,76,73] step by step
- [ ] Understand why each element is processed at most twice
- [ ] Draw the stack state at each step
- [ ] Try the backward iteration approach
- [ ] Practice explaining the monotonic stack pattern
- [ ] Review in 3 days

---

**Pattern Mastered**: Monotonic Stack (Next Greater Element) ✅  
**Difficulty**: Medium  
**Time to Master**: 30-40 minutes  
**Importance**: ⭐⭐⭐⭐⭐ Essential stack pattern

This problem teaches the powerful monotonic stack pattern that appears in many "next element" problems. Once you master this, you'll recognize the pattern instantly in similar problems! 🚀
