package src;

public class ProductArrayExceptSelf {


    public static int[] productExceptSelf(int[] nums) {

        int n = nums.length;

        int [] leftProduct = new int[n];
        int [] rightProduct = new int[n];
        int ans[] = new int[n];

        leftProduct[0] = 1;

        for(int i = 1; i < n; i++){
            leftProduct[i] = leftProduct[i-1] * nums[i-1];
        }

        // Build right products array
        // rightProducts[i] = product of all elements to the right of i
        rightProduct[n - 1] = 1; // No elements to the right of last index

        for(int i = n-2; i>=0; i--){
            rightProduct[i] = rightProduct[i+1] * nums[i+1];
        }

        for (int i = 0; i < n; i++){
            ans[i] = leftProduct[i] * rightProduct[i];
        }

        return ans;
    }

    public static void main(String[] args) {

        int[] nums = {1,2,3,4};
        int[] result = productExceptSelf(nums);

        // Print the result
        for (int num : result) {
            System.out.print(num + " ");
        }
    }
}
