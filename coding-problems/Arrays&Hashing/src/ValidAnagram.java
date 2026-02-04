package src;


/*
 *
Problem Statement
Given two strings s and t, return true if t is an anagram of s, and return false otherwise.

An anagram is a word or phrase formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.
* */

public class ValidAnagram {


    public static boolean isAnagram(String s, String t) {

        if(s.length() != t.length()) {
            return false;
        }

        if(s.isEmpty() && t.isEmpty()) {
            return true;
        }

        int []frequencyCount = new int[26];

        // Build frequency count for both strings
        // If both strings are anagrams, frequencyCount should be all zeros
        // frequencyCount[s.charAt(i) - 'a']++ --> Increment count for character in s
        // frequencyCount[t.charAt(i) - 'a']-- --> Decrement count for character in t
        for(int i = 0; i < s.length(); i++) {
            frequencyCount[s.charAt(i) - 'a']++;
            frequencyCount[t.charAt(i) - 'a']--;
        }

        for (int count: frequencyCount){
            if(count<0 ) {
                return false;
            }
        }

        return true;

    }

    public static void main(String[] args) {

        String s = "anagram";
        String t = "nagaram";

        boolean result = isAnagram(s, t);

        System.out.println(result); // Output: true

        String s2 = "rat";
        String t2 = "car";

        boolean result2 = isAnagram(s2, t2);

        System.out.println(result2); // Output: false
    }

}
