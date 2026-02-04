package src;// Given an integer array nums, return true if any value appears at least twice in the array, and return false if every element is distinct.

/*
Input: nums = [1,2,3,1]
Output: true
Explanation: The element 1 appears at indices 0 and 3.
*/


// Key Insight: Use a HashSet to track elements we've already seen. If we encounter an element that's already in the set, we found a duplicate!
// Time Complexity: O(n) - Single pass through array
// Space Complexity: O(n) - HashSet stores up to n elements


/*Use HashSet when:

        ✅ You need to check for existence or uniqueness
        ✅ You don't need to store associated values (use HashMap for that)
        ✅ Order doesn't matter
        ✅ You want O(1) lookup time
    */


import java.util.HashSet;
import java.util.Set;

public class ContainsDuplicate {

    public static boolean containsDuplicate(int[] nums) {
        Set<Integer> numberSet = new HashSet<>();

        for(int num : nums) {
            if(numberSet.contains(num)) {
                return true;
            }
            numberSet.add(num);
        }
        return false;
    }

    public static void main(String[] args) {

        int[] nums = {1, 2, 3, 1};
        boolean result = containsDuplicate(nums);

        System.out.println(result); // Output: true

        int[] nums2 = {2, 7, 11, 15};
        boolean result2 = containsDuplicate(nums2);
        System.out.println(result2); // Output: false
    }
}

