

public class ValidPalindrome {

    public static boolean isValidPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;

        while(left < right) {
            // Skip non-alphanumeric from left
            while(left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            
            // Skip non-alphanumeric from right
            while(left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            
            // Compare characters (case-insensitive)
            if(Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            
            left++;
            right--;
        }
        return true;
    }

    public static void main(String[] args) {
        // Test cases
        System.out.println(isValidPalindrome("A man, a plan, a canal: Panama")); // true
        System.out.println(isValidPalindrome("race a car")); // false
        System.out.println(isValidPalindrome(" ")); // true
        System.out.println(isValidPalindrome("a")); // true
        System.out.println(isValidPalindrome("ab")); // false
    }
}
