package src;/*  🎯 Problem: Two Sum (Easy)

   Given: An array of integers nums and an integer target
   Find: Indices of two numbers that add up to target
   Return: The two indices [i, j]

   Example:

     Input: nums = [2, 7, 11, 15], target = 9
     Output: [0, 1]
     Explanation: nums[0] + nums[1] = 2 + 7 = 9
 */



/*
🎓 Key Takeaways

     - When to use: "Find pair", "sum to target", "complement exists"
        - The trick: Store as you go, check for complement
     - Why it works: Trading space (O(n)) for time (O(n²) → O(n))
        - Edge cases:
        - Empty array: []
        - No solution: return []
        - Same number twice: [3,3] target 6 → HashMap stores index, so works!
*/

// Time Complexity: O(n) - Single pass through array
// Space Complexity: O(n) - HashMap stores up to n elements

import java.util.*;

public class TwoSum {


    public static int[] twoSum(int[] nums, int target) {

        Map<Integer, Integer> occurenceMap = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];

            if(occurenceMap.containsKey(complement)) {
                return new int[] {occurenceMap.get(complement), i};
            }
            occurenceMap.put(nums[i], i);
        }
        return new int[] {};

    }


    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = twoSum(nums, target);

        System.out.println(Arrays.toString(result));

    }
}




