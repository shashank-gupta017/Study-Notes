# Coding Interview Progress Tracker

## How to Use This Tracker

**Status Legend:**
- ⬜ Not Started
- 🟨 In Progress (attempted but not solved)
- 🟩 Solved (got it working)
- ✅ Mastered (can solve in <20 min without help)

**Review Schedule:**
- Day 1 → Day 3 → Day 7 → Day 14 → Day 30

**Track Format:**
`[Status] Problem Name | First Attempt: Date | Last Review: Date | Time Taken | Notes`

---

## Week 1: Arrays & Hashing

### Arrays Basics
- [x] Two Sum | First: 2026-01-26 | Review: 2026-01-29 | Time: N/A | Pattern: HashMap
- [x] Contains Duplicate | First: 2026-01-26 | Review: 2026-01-29 | Time: N/A | Pattern: HashSet
- [x] Best Time to Buy/Sell Stock | First: 2026-01-26 | Review: 2026-01-29 | Time: N/A | Pattern: One Pass
- [x] Product of Array Except Self | First: 2026-01-28 | Review: 2026-01-31 | Time: ~60min | Pattern: Prefix/Suffix

**Notes:**
- Two Sum: Solved using HashMap complement pattern - O(n) time, O(n) space. Next review: Day 3 (2026-01-29)
- Contains Duplicate: Solved using HashSet existence check - O(n) time, O(n) space. Next review: Day 3 (2026-01-29)
- Best Time to Buy/Sell Stock: Solved using min/max tracking pattern - O(n) time, O(1) space. Next review: Day 3 (2026-01-29)
- Product of Array Except Self: Solved using Prefix/Suffix pattern - O(n) time, O(1) space. Build prefix products in output array, then multiply by suffix products on-the-fly. Key insight: answer[i] = (product of all before i) × (product of all after i). Next review: Day 3 (2026-01-31)

### HashMap Mastery
- [x] Valid Anagram | First: 2026-01-26 | Review: 2026-01-29 | Time: ~45min | Pattern: Frequency Counter
- [x] Group Anagrams | First: 2026-01-28 | Review: 2026-01-31 | Time: ~60min | Pattern: HashMap + Sorting
- [ ] Top K Frequent Elements | First: ___ | Review: ___ | Time: ___ | Pattern: HashMap + Heap
- [ ] Subarray Sum Equals K | First: ___ | Review: ___ | Time: ___ | Pattern: Prefix Sum

**Notes:**
- Valid Anagram: ✅ VALIDATED - Solution uses optimal array frequency counter approach with O(n) time, O(1) space. Implementation correctly handles all edge cases including empty strings, single characters, and different lengths. Uses char-to-index mapping (c-'a'). Note: checks for count<0 which works due to length check, though count!=0 is more complete. Next review: Day 3 (2026-01-29)
- Group Anagrams: ✅ VALIDATED - Solution uses HashMap with sorted string as key approach - O(n × k log k) time, O(n × k) space. Creates sorted char array as unique key, groups strings by key. Tested with ["eat","tea","tan","ate","nat","bat"] producing correct output: [[eat, tea, ate], [bat], [tan, nat]]. Minor note: return type declared as List<String> but works as List<List<String>>. Alternative frequency count approach available for O(n × k) time. Next review: Day 3 (2026-01-31)

### Two Pointers
- [x] Valid Palindrome | First: 2026-01-29 | Review: 2026-02-01 | Time: N/A | Pattern: Two Pointers
- [x] Two Sum II | First: 2026-02-02 | Review: 2026-02-05 | Time: N/A | Pattern: Two Pointers (Sorted)
- [ ] 3Sum | First: ___ | Review: ___ | Time: ___ | Pattern: Two Pointers + Loop
- [ ] Container With Most Water | First: ___ | Review: ___ | Time: ___ | Pattern: Two Pointers (Greedy)

**Notes:**
- Valid Palindrome: ✅ VALIDATED - Solution uses optimal two pointers convergent approach with O(n) time, O(1) space. Fixed initial implementation to use nested while loops for character skipping instead of if-else chain. Correctly skips non-alphanumeric characters from both ends using `Character.isLetterOrDigit()` and compares case-insensitively with `Character.toLowerCase()`. Key learning: Must use nested while loops with `left < right` check to prevent index out of bounds when skipping characters. Pattern: Start pointers at both ends, skip invalid chars, compare valid chars, converge toward center. Next review: Day 3 (2026-02-01)
- Two Sum II: ✅ VALIDATED - Solution uses optimal two pointers on sorted array with O(n) time, O(1) space. Correctly implements convergent pointer pattern: left starts at 0, right at end. If sum < target, move left++ (need larger); if sum > target, move right-- (need smaller); if sum == target, found! Implementation returns boolean (exists check) which is valid, though LeetCode #167 asks for indices. Key insight: Sorted array property allows greedy pointer movement without missing solutions. All test cases pass including edge cases (negatives, duplicates, extremes). Next review: Day 3 (2026-02-05)

---

## Week 2: Two Pointers & More Hashing

### Two Pointers
- [x] Valid Palindrome | First: 2026-01-29 | Review: 2026-02-01 | Time: N/A | Pattern: Two Pointers
- [x] Two Sum II | First: 2026-02-02 | Review: 2026-02-05 | Time: N/A | Pattern: Two Pointers (Sorted)
- [ ] 3Sum | First: ___ | Review: ___ | Time: ___ | Pattern: Two Pointers + Loop
- [ ] Container With Most Water | First: ___ | Review: ___ | Time: ___ | Pattern: Two Pointers (Greedy)

### HashMap Advanced
- [ ] Top K Frequent Elements | First: ___ | Review: ___ | Time: ___ | Pattern: HashMap + Heap
- [ ] Subarray Sum Equals K | First: ___ | Review: ___ | Time: ___ | Pattern: Prefix Sum

**Notes:**
- Valid Palindrome: ✅ VALIDATED - Solution uses optimal two pointers convergent approach with O(n) time, O(1) space. Fixed initial implementation to use nested while loops for character skipping instead of if-else chain. Correctly skips non-alphanumeric characters from both ends using `Character.isLetterOrDigit()` and compares case-insensitively with `Character.toLowerCase()`. Key learning: Must use nested while loops with `left < right` check to prevent index out of bounds when skipping characters. Pattern: Start pointers at both ends, skip invalid chars, compare valid chars, converge toward center. Next review: Day 3 (2026-02-01)
- Two Sum II: ✅ VALIDATED - Solution uses optimal two pointers on sorted array with O(n) time, O(1) space. Correctly implements convergent pointer pattern: left starts at 0, right at end. If sum < target, move left++ (need larger); if sum > target, move right-- (need smaller); if sum == target, found! Implementation returns boolean (exists check) which is valid, though LeetCode #167 asks for indices. Key insight: Sorted array property allows greedy pointer movement without missing solutions. All test cases pass including edge cases (negatives, duplicates, extremes). Next review: Day 3 (2026-02-05)

---

## Week 3: Stack & Sliding Window

### Stack Pattern
- [ ] Valid Parentheses | First: ___ | Review: ___ | Time: ___ | Pattern: Stack
- [ ] Min Stack | First: ___ | Review: ___ | Time: ___ | Pattern: Stack (Design)
- [ ] Daily Temperatures | First: ___ | Review: ___ | Time: ___ | Pattern: Monotonic Stack

### Sliding Window
- [ ] Longest Substring Without Repeating | First: ___ | Review: ___ | Time: ___ | Pattern: Sliding Window
- [ ] Longest Repeating Character Replacement | First: ___ | Review: ___ | Time: ___ | Pattern: Sliding Window
- [ ] Maximum Subarray | First: ___ | Review: ___ | Time: ___ | Pattern: Kadane's

**Notes:**
- 

---

## Week 4: LinkedList & Fast/Slow Pointers

- [ ] Reverse Linked List | First: ___ | Review: ___ | Time: ___ | Pattern: Iterative/Recursive
- [ ] Merge Two Sorted Lists | First: ___ | Review: ___ | Time: ___ | Pattern: Two Pointers
- [ ] Linked List Cycle | First: ___ | Review: ___ | Time: ___ | Pattern: Fast/Slow Pointers
- [ ] Reorder List | First: ___ | Review: ___ | Time: ___ | Pattern: Fast/Slow + Reverse
- [ ] Remove Nth Node From End | First: ___ | Review: ___ | Time: ___ | Pattern: Two Pointers
- [ ] Copy List with Random Pointer | First: ___ | Review: ___ | Time: ___ | Pattern: HashMap
- [ ] Merge K Sorted Lists | First: ___ | Review: ___ | Time: ___ | Pattern: Heap

**Notes:**
- 

---

## Week 5: Trees & BFS/DFS

- [ ] Invert Binary Tree | First: ___ | Review: ___ | Time: ___ | Pattern: DFS/BFS
- [ ] Maximum Depth of Binary Tree | First: ___ | Review: ___ | Time: ___ | Pattern: DFS
- [ ] Same Tree | First: ___ | Review: ___ | Time: ___ | Pattern: DFS
- [ ] Binary Tree Level Order Traversal | First: ___ | Review: ___ | Time: ___ | Pattern: BFS
- [ ] Validate Binary Search Tree | First: ___ | Review: ___ | Time: ___ | Pattern: DFS (Range)
- [ ] Lowest Common Ancestor of BST | First: ___ | Review: ___ | Time: ___ | Pattern: BST Property
- [ ] Binary Tree Maximum Path Sum | First: ___ | Review: ___ | Time: ___ | Pattern: DFS (Bottom-up)

**Notes:**
- 

---

## Week 6: Graphs

- [ ] Number of Islands | First: ___ | Review: ___ | Time: ___ | Pattern: DFS/BFS
- [ ] Clone Graph | First: ___ | Review: ___ | Time: ___ | Pattern: DFS + HashMap
- [ ] Pacific Atlantic Water Flow | First: ___ | Review: ___ | Time: ___ | Pattern: Multi-source BFS
- [ ] Course Schedule | First: ___ | Review: ___ | Time: ___ | Pattern: Topological Sort
- [ ] Word Ladder | First: ___ | Review: ___ | Time: ___ | Pattern: BFS
- [ ] Alien Dictionary | First: ___ | Review: ___ | Time: ___ | Pattern: Topological Sort

**Notes:**
- 

---

## Week 6: Binary Search & Heaps

### Binary Search
- [ ] Binary Search | First: ___ | Review: ___ | Time: ___ | Pattern: Binary Search
- [ ] Search in Rotated Sorted Array | First: ___ | Review: ___ | Time: ___ | Pattern: Modified Binary Search
- [ ] Find Minimum in Rotated Array | First: ___ | Review: ___ | Time: ___ | Pattern: Modified Binary Search
- [ ] Time Based Key-Value Store | First: ___ | Review: ___ | Time: ___ | Pattern: Binary Search

### Heaps
- [ ] Kth Largest Element | First: ___ | Review: ___ | Time: ___ | Pattern: Min Heap
- [ ] Find Median from Data Stream | First: ___ | Review: ___ | Time: ___ | Pattern: Two Heaps
- [ ] Merge K Sorted Lists | First: ___ | Review: ___ | Time: ___ | Pattern: Min Heap

**Notes:**
- 

---

## Week 7: Backtracking & DP Intro

### Backtracking
- [ ] Subsets | First: ___ | Review: ___ | Time: ___ | Pattern: Backtracking
- [ ] Combination Sum | First: ___ | Review: ___ | Time: ___ | Pattern: Backtracking
- [ ] Permutations | First: ___ | Review: ___ | Time: ___ | Pattern: Backtracking
- [ ] Word Search | First: ___ | Review: ___ | Time: ___ | Pattern: Backtracking + DFS

### DP Basics
- [ ] Climbing Stairs | First: ___ | Review: ___ | Time: ___ | Pattern: 1D DP
- [ ] Coin Change | First: ___ | Review: ___ | Time: ___ | Pattern: 1D DP (Unbounded Knapsack)
- [ ] Longest Increasing Subsequence | First: ___ | Review: ___ | Time: ___ | Pattern: 1D DP
- [ ] House Robber | First: ___ | Review: ___ | Time: ___ | Pattern: 1D DP

**Notes:**
- 

---

## Week 8: Dynamic Programming

- [ ] Longest Common Subsequence | First: ___ | Review: ___ | Time: ___ | Pattern: 2D DP
- [ ] Word Break | First: ___ | Review: ___ | Time: ___ | Pattern: 1D DP
- [ ] Unique Paths | First: ___ | Review: ___ | Time: ___ | Pattern: 2D DP
- [ ] Jump Game | First: ___ | Review: ___ | Time: ___ | Pattern: Greedy/DP
- [ ] Decode Ways | First: ___ | Review: ___ | Time: ___ | Pattern: 1D DP
- [ ] Edit Distance | First: ___ | Review: ___ | Time: ___ | Pattern: 2D DP

**Notes:**
- 

---

## Week 13: Intervals & Greedy

### Intervals
- [ ] Merge Intervals | First: ___ | Review: ___ | Time: ___ | Pattern: Sort + Merge
- [ ] Insert Interval | First: ___ | Review: ___ | Time: ___ | Pattern: Merge
- [ ] Non-overlapping Intervals | First: ___ | Review: ___ | Time: ___ | Pattern: Greedy
- [ ] Meeting Rooms | First: ___ | Review: ___ | Time: ___ | Pattern: Sorting
- [ ] Meeting Rooms II | First: ___ | Review: ___ | Time: ___ | Pattern: Heap/Sweep Line

### Greedy
- [ ] Jump Game | First: ___ | Review: ___ | Time: ___ | Pattern: Greedy

**Notes:**
- 

---

## Week 14: Tries & Bit Manipulation

### Tries
- [ ] Implement Trie | First: ___ | Review: ___ | Time: ___ | Pattern: Trie
- [ ] Design Add and Search Words | First: ___ | Review: ___ | Time: ___ | Pattern: Trie + DFS
- [ ] Word Search II | First: ___ | Review: ___ | Time: ___ | Pattern: Trie + Backtracking

### Bit Manipulation
- [ ] Single Number | First: ___ | Review: ___ | Time: ___ | Pattern: XOR
- [ ] Number of 1 Bits | First: ___ | Review: ___ | Time: ___ | Pattern: Bit Counting
- [ ] Counting Bits | First: ___ | Review: ___ | Time: ___ | Pattern: DP + Bits

**Notes:**
- 

---

## Week 15: Union Find & Math

### Union Find
- [ ] Number of Islands (Union Find) | First: ___ | Review: ___ | Time: ___ | Pattern: Union Find
- [ ] Redundant Connection | First: ___ | Review: ___ | Time: ___ | Pattern: Union Find
- [ ] Graph Valid Tree | First: ___ | Review: ___ | Time: ___ | Pattern: Union Find

### Math & Geometry
- [ ] Rotate Image | First: ___ | Review: ___ | Time: ___ | Pattern: Matrix Rotation
- [ ] Spiral Matrix | First: ___ | Review: ___ | Time: ___ | Pattern: Matrix Traversal
- [ ] Set Matrix Zeroes | First: ___ | Review: ___ | Time: ___ | Pattern: In-place

**Notes:**
- 

---

## Week 16: Additional Patterns

### Bit Manipulation (continued)
- [ ] Missing Number | First: ___ | Review: ___ | Time: ___ | Pattern: XOR/Math
- [ ] Reverse Bits | First: ___ | Review: ___ | Time: ___ | Pattern: Bit Operations

### Greedy (continued)
- [ ] Jump Game II | First: ___ | Review: ___ | Time: ___ | Pattern: Greedy
- [ ] Partition Labels | First: ___ | Review: ___ | Time: ___ | Pattern: Greedy + Intervals

**Notes:**
- 

---

## Weeks 17-18: Mock Interviews

Track your mock interview attempts here:

### Mock 1 (Date: ___)
- Problem 1: ___ | Solved: ⬜ | Time: ___ | Feedback:
- Problem 2: ___ | Solved: ⬜ | Time: ___ | Feedback:

### Mock 2 (Date: ___)
- Problem 1: ___ | Solved: ⬜ | Time: ___ | Feedback:
- Problem 2: ___ | Solved: ⬜ | Time: ___ | Feedback:

### Mock 3 (Date: ___)
- Problem 1: ___ | Solved: ⬜ | Time: ___ | Feedback:
- Problem 2: ___ | Solved: ⬜ | Time: ___ | Feedback:

---

## Statistics & Milestones

### Overall Progress
- Total Problems Attempted: 8
- Total Problems Solved: 8
- Total Problems Mastered: 0
- Success Rate: 100%

### By Difficulty
- Easy: Solved 6 / 6
- Medium: Solved 2 / 2
- Hard: Solved 0 / 0

### By Pattern (Track which patterns you're strongest/weakest at)
- Arrays/Hashing: 6/6 (HashMap + Sorting pattern added!)
- Two Pointers: 2/4
- Sliding Window: 0/0
- Stack: 0/0
- LinkedList: 0/0
- Trees: 0/0
- Graphs: 0/0
- Binary Search: 0/0
- Heaps: 0/0
- Backtracking: 0/0
- Dynamic Programming: 0/0
- Tries: 0/0
- Intervals: 0/0
- Greedy: 0/0
- Bit Manipulation: 0/0
- Union Find: 0/0
- Math & Geometry: 0/0

### Weak Areas to Focus On
1. 
2. 
3. 

### Achievements 🏆
- [x] First problem solved
- [ ] First Medium solved without hints
- [ ] First Hard solved
- [ ] 10 problems mastered
- [ ] 50 problems mastered
- [ ] 100 problems solved
- [ ] Completed Week 5
- [ ] Completed Week 10
- [ ] First successful mock interview
- [ ] Can solve Easy in <15 min consistently
- [ ] Can solve Medium in <30 min consistently

---

## Daily Log

### Week 1
**Mon**: Problems attempted: ___ | Time spent: ___ | Notes:
**Tue**: Problems attempted: ___ | Time spent: ___ | Notes:
**Wed**: Problems attempted: ___ | Time spent: ___ | Notes:
**Thu**: Problems attempted: ___ | Time spent: ___ | Notes:
**Fri**: Problems attempted: ___ | Time spent: ___ | Notes:
**Sat**: Problems attempted: ___ | Time spent: ___ | Notes:
**Sun**: Problems attempted: ___ | Time spent: ___ | Notes:

*(Continue for subsequent weeks)*

---

## Review Queue (Spaced Repetition)

### Due Today
-

### Due This Week
- Product of Array Except Self (Day 3 review: 2026-01-31) - OVERDUE
- Group Anagrams (Day 3 review: 2026-01-31) - OVERDUE
- Valid Palindrome (Day 3 review: 2026-02-01) - OVERDUE
- Two Sum II (Day 3 review: 2026-02-05)

### Due This Month
-

---

## Common Mistakes & Learnings

Track patterns in your mistakes to avoid repeating them:

1. **Mistake**: ___ | **Solution**: ___
2. **Mistake**: ___ | **Solution**: ___
3. **Mistake**: ___ | **Solution**: ___

---

## Interview Preparation Checklist

### Technical Readiness
- [ ] Can solve 80%+ of Easy problems
- [ ] Can solve 60%+ of Medium problems independently
- [ ] Attempted at least 10 Hard problems
- [ ] Comfortable with all major patterns
- [ ] Can code without IDE assistance
- [ ] Can explain time/space complexity

### Interview Skills
- [ ] Practice thinking aloud
- [ ] Can ask good clarifying questions
- [ ] Handle edge cases systematically
- [ ] Comfortable with whiteboard/paper coding
- [ ] Completed 5+ mock interviews
- [ ] Can optimize brute force solutions

### Company Specific
- [ ] Reviewed company-tagged problems
- [ ] Studied company culture
- [ ] Prepared behavioral stories (STAR format)
- [ ] Prepared questions for interviewer
- [ ] Know the interview format

---

**Remember**: Update this tracker daily. Consistency is key! 🎯
