import java.util.*;
import java.io.*;
import java.math.BigInteger;
public class SubarraysProduct {
	/*All Contests > World CodeSprint 13 Watson's Love for Arrays
	  Find number of subarrays of given array such that product of all
	  values in subarray is equivalnt to k % m (both given)
	  
	  Watson loves arrays of integers, especially good ones.

Given integers k and m where m is a prime number, Watson considers an array good if the product of all the elements of the array modulo m is k.

He gives Sherlock multiple queries. A single query consists of an array A and two integers k and m. In each query, Sherlock has to find the number of nonempty (contiguous) subarrays of A that are good.

Sherlock is very busy with one of his cases and needs your help to solve the problem.

Complete the function howManyGoodSubarrays which takes in the integer array A and two integers m and k and returns the number of good subarrays of A.

Input Format

The first line of the input contains a single integer t denoting the number of queries to answer.

The descriptions of the queries follow. A single query is described by two lines:

The first line contains three space-separated integers n, m, k
The second line contains  space-separated integers denoting the elements of the array A.
	  
	  1 <= t <= 100
	  1 <= n <= 10^5
	  Sum of n over all queries is <= than 10^5
	  1 < m < 10^9
	  0 <= k < m
	  0 <= A[i] <= 10^9
	  m is prime
	  
	  O(N)
	 */
	
	public static int kinv;
	  
    public static void main(String[] args) throws IOException {
 //   	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    	BufferedReader in = new BufferedReader(new FileReader("subArray.in"));
    	
    	int t = Integer.parseInt(in.readLine());
    	
    	for(int T = 0; T < t; T++)
    	{
    		StringTokenizer st = new StringTokenizer(in.readLine());
    		int n = Integer.parseInt(st.nextToken());
    		int m = Integer.parseInt(st.nextToken());
    		int k = Integer.parseInt(st.nextToken());
    		
    		kinv = BigInteger.valueOf(k).modPow(BigInteger.valueOf(m-2), BigInteger.valueOf(m)).intValue();
    		
    		int[] A = new int[n];
    		st = new StringTokenizer(in.readLine());
    		
    		for(int i = 0; i < A.length; i++)
    			A[i] = Integer.parseInt(st.nextToken()) % m;
    		
    		long result = howManyGoodSubarrays(A, m, k, true);
    		System.out.println(result);
    	}
    	
    	in.close();
    }
    
    public static long howManyGoodSubarrays(int[] A, int m, int k, boolean has0) {
        // Return the number of good subarrays of A.
    	
    	long count = 0;
    	
    	int low = 0; int high = 0;
    	
    	if(k == 0)  //If k == 0, count = total subarrays - all subarrays that don't contain 0's
    	{
    		long temp = A.length; //Without a long temp, the long count still performs integer overflow of A.length * (A.length+1)/2
    		count += temp * (temp+1) / 2;
    		
    		while(low < A.length && high < A.length) //Low = first non-0, high = first 0 after
    		{
    			while(low < A.length && A[low] == 0) low++;
    			high = low;
    			while(high < A.length && A[high] != 0) high++;
    			
    			long size = high - low;
    			count -= size*(size + 1) / 2;
    			low = high;
    		}
    		
    		return count;
    	}
    	
    	if(has0) //Only first call to method is has0 true: it separates out A into separate arrays that do not contain 0's
    	{
    		while(low < A.length && high < A.length)
    		{
    			while(low < A.length && A[low] == 0) low++;
    			high = low;
    			while(high < A.length && A[high] != 0) high++;
    			
    			int[] temp = Arrays.copyOfRange(A, low, high);
    			count += howManyGoodSubarrays(temp, m, k, false);
    			low = high;
    		}
    		
    		return count;
    	}
    	
    	//No 0's in A
    	/* for all i > j, cul[i] / cul[j] = cul[i] * cul inverse[j] = products of numbers between them
    	   cul[i] * culinv[j] = k mod m
    	   cul[i] * kinv = cul[j] mod m, store cul[j] into HashMap
    	   All int put = cul[i] * kinv % m that appears in map (map.get(cul[i])) times, increment count
    	 */
    	
    	HashMap<Integer, Integer> map = new HashMap<>();
    	
    	long[] cul = new long[A.length+1];
    	cul[0] = 1;
    	
    	for(int i = 1; i < cul.length; i++) cul[i] = (cul[i-1] * A[i-1]) % m;
    	
    	map.put(1, 1); //store cul[0] (which is 1)
    	
    	for(int i = 1; i < cul.length; i++)
    	{
    		int put = (int) ((cul[i] * kinv) % m);
    		if(map.containsKey(put))
    			count += map.get(put);
    		
    		if(map.containsKey((int) cul[i])) //Update map appropriately
    			map.put((int) cul[i], map.get((int) cul[i]) + 1);
    		else
    			map.put((int) cul[i], 1);
    	}
    	
    	return count;
    }
    
}
/*
2
5 3 0
1 0 3 0 4
3 11 1
1 10 10

13
3

2
1 11 0
11
5 3 2
2 2 2 2 2

1
9
*/
