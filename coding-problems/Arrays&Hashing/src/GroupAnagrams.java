package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupAnagrams {

    public static List<String> groupAnagrams(String[] strs) {

        Map<String, List<String>> frequencyMap = new HashMap<>();

        for (String str : strs) {

            char[] chars = str.toCharArray();

            // Sort the character array to form the key
            Arrays.sort(chars);

            String key = new String(chars);

            if(!frequencyMap.containsKey(key)) {
                frequencyMap.put(key, new ArrayList<>());
            }
            frequencyMap.get(key).add(str);
        }

        return new ArrayList(frequencyMap.values());
    }

    public static void main(String[] args) {

        String[] strs = {"eat","tea","tan","ate","nat","bat"};

        System.out.println(groupAnagrams(strs));

    }
}
