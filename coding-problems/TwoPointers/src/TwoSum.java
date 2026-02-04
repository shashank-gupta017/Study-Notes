import java.util.Map;

public class TwoSum {


    public static Boolean twoSum(int[] nums, int target) {

     int left = 0;
     int right = nums.length - 1;

     while(left < right) {

     int currentSum = nums[left] + nums [right];

         if(currentSum == target) {
             return true;
         } else if (currentSum < target) {
             left++;
         } else {
             right--;
         }

     }
     return false;

    }

    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        Boolean sumExists = twoSum(nums, target);

        System.out.println(sumExists);
    }
}
