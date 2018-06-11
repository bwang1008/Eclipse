import java.util.*;
import java.io.*;
import java.math.BigInteger;
public class BirthdayRunner {

	public static void main(String[] args) throws IOException {
		Message m = new Message(new File("birthdayProblem.in"));
		File f = new File("Rough");
		m.decrypt(f);
		m = new Message(f);
		
		System.out.println("Phase 1 Done");
		while(!m.in.readLine().equals("Additional Input:"));
//		File f2 = new File("Rough2");
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Rough2")));
		while(m.in.ready())
		{
			StringTokenizer st = new StringTokenizer(m.in.readLine());
			while(st.hasMoreTokens())
			{
				BigInteger x = new BigInteger(st.nextToken());
				out.print((char) Message.F(x));
			}
		}
		
		out.close();
		System.out.println("Phase 2 Done");
		
		f = new File("Rough2");
		m = new Message(f);
		while(!m.in.readLine().equals("More Input!"));
		
		out = new PrintWriter(new BufferedWriter(new FileWriter("Rough4")));
		while(m.in.ready())
		{
			StringTokenizer st = new StringTokenizer(m.in.readLine());
			while(st.hasMoreTokens())
			{
				BigInteger x = new BigInteger(st.nextToken());
				out.print((char) Message.F(x));
			}
		}
		
		out.close();
		System.out.println("Phase 3 Done");
	}

}
