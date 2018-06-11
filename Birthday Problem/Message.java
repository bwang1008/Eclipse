import java.util.*;
import java.io.*;
import java.math.*;
public class Message {
	
	static HashMap<BigInteger, Integer> hm = new HashMap<>();
	
	static final int p = 127;
	
	static int[] choose = {1, 10, 45, 120, 83, 125, 83, 120, 45, 10, 1};
	
	static int temp = 0;
	
	BufferedReader in;
	List<BigInteger> list;
	
	static {
		hm.put(BigInteger.valueOf(-10), 72);
		hm.put(BigInteger.valueOf(-9), 34);
		hm.put(BigInteger.valueOf(-8), 106);
		hm.put(BigInteger.valueOf(-7), 13);
		hm.put(BigInteger.valueOf(-6), 119);
		hm.put(BigInteger.valueOf(-5), 5);
		hm.put(BigInteger.valueOf(-4), 124);
		hm.put(BigInteger.valueOf(-3), 2);
		hm.put(BigInteger.valueOf(-2), 126);
		hm.put(BigInteger.valueOf(-1), 1);
		hm.put(BigInteger.valueOf(0), 0);
		hm.put(BigInteger.valueOf(1), 1);
		hm.put(BigInteger.valueOf(2), 1);
		hm.put(BigInteger.valueOf(3), 2);
		hm.put(BigInteger.valueOf(4), 3);
		hm.put(BigInteger.valueOf(5), 5);
		hm.put(BigInteger.valueOf(6), 8);
		hm.put(BigInteger.valueOf(7), 13);
		hm.put(BigInteger.valueOf(8), 21);
		hm.put(BigInteger.valueOf(9), 34);
	}
	
	public Message() {
		in = new BufferedReader(new InputStreamReader(System.in));
		list = new ArrayList<>();
	}
	
	public Message(File input) throws IOException {
		in = new BufferedReader(new FileReader(input));
		list = new ArrayList<>(12000);
	}
	
	public void decrypt() throws IOException {
		while(in.ready())
		{
			StringTokenizer st = new StringTokenizer(in.readLine());
			while(st.hasMoreTokens())
			{
				String g = st.nextToken();
				BigInteger x = new BigInteger(g);
				System.out.print((char) F(x));
			}
		}
	}
	
	public void decrypt(File output) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(output)));
//		int temp = 0;
//		int temp2 = 0;
		while(in.ready())
		{
			StringTokenizer st = new StringTokenizer(in.readLine());
			temp++;
//			if(temp % 100 == 0) System.out.println(temp);
			while(st.hasMoreTokens())
			{
				String g = st.nextToken();
				BigInteger x = new BigInteger(g);
//				temp2++;
				out.print((char) F(x));
			}
		}
//		System.out.println("There are " + temp2 + " numbers in input");
		out.close();
	}
	
	public void encrypt(long size) throws IOException {
		while(in.ready())
		{
			String line = in.readLine();
			System.out.println(temp++);
			for(int i = 0; i < line.length(); i++)
			{
				char a = line.charAt(i);
				long rand = (long) (Math.random() * size);
				BigInteger next = getNext(String.valueOf(rand), a);
				if(next.intValue() > size)
					next = getNext(next.divide(BigInteger.valueOf(2)).toString(), a);
				list.add(next);
			}
			long rand = (long) (Math.random() * size/2);
			BigInteger next = getNext(String.valueOf(rand), '\n');
			list.add(next);
		}
	}
	
	public void encrypt2(int digits) throws IOException {
		while(in.ready())
		{
			String line = in.readLine();
			for(int i = 0; i < line.length(); i++)
			{
				char a = line.charAt(i);
				String g = buildRandom(digits);
				BigInteger next = getNext(g, a);
				if(next.toString().length() > digits)
				{
					next = getNext(next.divide(BigInteger.valueOf(10)).toString(), a);
				}
				list.add(next);
			}
			String g = buildRandom(digits*9/10);
			BigInteger next = getNext(g, '\n');
			list.add(next);
//			System.out.println(list.size());
		}
	}
	
	public String buildRandom(int digits) {
		StringBuilder sb = new StringBuilder();
		sb.append((int) (Math.random()*9) + 1);
		for(int i = 0; i < digits - 1; i++)
			sb.append((int) (Math.random() * 10));
		
		return sb.toString();
	}
	
	public void toFile() {
		for(BigInteger x : list)
			System.out.println(x);
	}
	
	public void toFile(int x) {
		while(list.size() > 0)
		{
			StringBuilder sb = new StringBuilder();
			int size = list.size();
			for(int i = 0; i < Math.min(x-1, size-1); i++)
				sb.append(list.remove(0) + " ");
			sb.append(list.remove(0));
			System.out.println(sb.toString());
		}
	}
	
	public void toFile(int x, File output) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(output)));
		StringBuilder sb = new StringBuilder();
/*		while(list.size() > 0)
		{
			int size = list.size();
			System.out.println(size);
			for(int i = 0; i < Math.min(x-1, size-1); i++)
			{
				sb.append(list.remove(0));
				sb.append(" ");
			}
			sb.append(list.remove(0));
			sb.append("\n");
		} */
		int index = 0;
		while(true)
		{
			if((index + 1) % x == 0)
				sb.append(list.get(index) + "\n");
			else
				sb.append(list.get(index) + " ");
			index++;
			if(index % 1000 == 0) System.out.println(index);
			if(index == list.size()) break;
		}
		out.println(sb.toString());
		out.close();
	}
	
	public long size() {
		long count = 0;

		try {
			while(in.ready())
			{
				StringTokenizer st = new StringTokenizer(in.readLine());
				while(st.hasMoreTokens())
				{
					st.nextToken();
					count++;
				}
			}
			
		}
		catch(IOException e) {
			System.out.println("File does not exist");
		}
		
		return count;
	}
	
	public static int F(BigInteger x) {
		int a = 2*fib(x.add(BigInteger.valueOf(2)));
		a -= x.mod(BigInteger.valueOf(p)).intValue();
		a -= 2;
		a %= p;
		if(a < 0)
			a += p;
		return a;
	}
	
	public static int fib(BigInteger x) {
		if(hm.containsKey(x))
			return hm.get(x);
		
		String g = x.toString();
		
		BigInteger m = new BigInteger(g.substring(0, g.length()-1));
		BigInteger m1 = m.add(BigInteger.ONE);
		int digit = g.charAt(g.length()-1) - '0';
		
		int n = fib(m);
		int n1 = fib(m1);
		
		int[][] power = new int[2][11];
		power[0][0] = 1;
		power[1][0] = 1;
		
		for(int i = 1; i < power[0].length; i++)
		{
			power[0][i] = power[0][i-1] * n % p;
			power[1][i] = power[1][i-1] * n1 % p;
		}
		
		int result = 0;
		
		for(int i = 0; i <= 10; i++)
		{
			result += choose[i] * fib(BigInteger.valueOf(digit-i)) * power[0][i] * power[1][10-i];
			result %= p;
		}
		
		hm.put(x, result);
		
		return result;
	}
	
	public static BigInteger getNext(String g, char val) {
		BigInteger x = new BigInteger(g);
		while(F(x) != val)
		{
			x = x.add(BigInteger.ONE);
		}
		return x;
	}
	
}
