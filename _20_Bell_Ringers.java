import java.util.*;
public class _20_Bell_Ringers {
	
	public static int[] prime = {1, 2, 3, 4, 5, 7, 8, 9, 11, 13, 16, 17, 19, 23, 25, 27, 29, 31, 32, 37, 41, 43, 47, 49};
	public static int N;  //^Single Factors
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		
		N = in.nextInt();
		while(N != 0)
		{
			int[] bells = new int[N];
			for(int i = 0; i < N; i++)
				bells[i] = in.nextInt();
			
			long result = endTime(bells);
			System.out.println(result != -1 ? result : "They go forever!");
			
			N = in.nextInt();
		}
		
		in.close();
		
	}
	
	public static long endTime(int[] bells) {
		int sum = 0;
		
		int[][] mods = new int[N][2];
		for(int i = 0; i < N; i++)
		{
			mods[i][0] = sum % bells[i];
			mods[i][1] = bells[i];
			sum += bells[i];
		}
		
		int[] list = new int[prime.length];
		Arrays.fill(list, -1);
		
		for(int i = 0; i < N; i++)
		{
			int temp = bells[i];
			for(int j = prime.length-1; j >= 0; j--)
			{
				if(temp == 1) break;
				if(temp % prime[j] == 0)
				{
					list[j] = mods[i][0];  //i.e., 5 mod 24 -> 5 mod 8 and 5 mod 3, but not 5 mod 4 or 2
					temp /= prime[j];
				}
			}
		}
		
		long first = 0; //Overall: first mod second
		int second = 1;
		
		for(int i = list.length-1; i >= 0; i--)
		{
			if(list[i] == -1) continue;
			if(second % prime[i] == 0) continue; //Prevent conflict between say 1 mod 4 and 6 mod 8
			
			int[] xy = xy(second, prime[i]);
			
			first = first * prime[i] * xy[2] + list[i] * second * xy[1]; //In Chinese Remainder Theorem
			second *= prime[i];
			first %= second;
		}
		
		while(first < sum) //Last synchronized bell must occur at or after the last person rings his bell first time
			first += second;
		
		for(int i = 0; i < N; i++)
			if(first % mods[i][1] != mods[i][0]) //Resolves conflict between 1 mod 4 and 6 mod 8; can never work
				return -1;
			
		return first;
	}
	
	public static int[] xy(int a, int b) { //Extended Euclidean Algorithm
		int[] prev = {a, 1, 0};
		int[] next = {b, 0, 1};
		
		while(next[0] != 0)
		{
			int q = prev[0] / next[0];
			int[] temp = new int[3];
			
			for(int i = 0; i < 3; i++)
				temp[i] = prev[i] - q * next[i];
			
			prev = next;
			next = temp;
		}
		
		return prev;
	}

}
/*
4 5 3 7 8
8 4 5 16 2 3 7 8 9
3 1 2 3
8 2 6 4 8 3 5 7 1
5 25 24 23 19 17
6 25 24 23 19 17 13
0

575
They go forever!
9
308
2215225
37874425
*/
