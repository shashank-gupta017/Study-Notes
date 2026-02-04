package src;

/*

Problem Statement
You are given an array prices where prices[i] is the price of a given stock on the ith day.

You want to maximize your profit by choosing a single day to buy one stock and choosing a different day in the future to sell that stock.

Return the maximum profit you can achieve from this transaction. If you cannot achieve any profit, return 0.
*/


/*
Input: prices = [7,1,5,3,6,4]
Output: 5
Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
Note that buying on day 2 and selling on day 1 is not allowed because you must buy before you sell.
*/


/*
Complexity Analysis
Time Complexity: O(n)
- Single pass through the array: O(n)
- Each operation inside loop is O(1)
Total: O(n)

Space Complexity: O(1)
 - Only two variables: minPrice and maxProfit
 - No additional data structures
 - Constant space regardless of input size
*/

public class BestTimeToBuySellStock {

    public static int maxProfit(int[] prices) {

        // Edge case: if prices array is empty or has only one element, no profit can be made
        if(prices == null || prices.length < 2) {
            return 0;
        }

        // initialize variables to track max profit and min price
        int maxProfit = 0;
        int minPrice = prices[0];

        // iterate through prices array
        for(int i = 1; i < prices.length; i++) {
            // Calculate profit if we sell at current price
            int profit = prices[i] - minPrice;

            // Update minimum price seen so far
            minPrice = Math.min(minPrice, prices[i]);

            // Update max profit
            maxProfit = Math.max(maxProfit, profit);
        }

        return maxProfit;
    }

    public static void main(String[] args) {
        int[] prices = {7,1,5,3,6,4};
        int result = maxProfit(prices);

        System.out.println(result); // Output: 5
    }
}
