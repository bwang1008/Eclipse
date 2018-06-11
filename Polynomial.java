import java.util.*;
public class Polynomial {

	int degree;
	double[] coefficients;
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		while(true)
		{
			System.out.println("Degree of polynomial : ");
			int N = in.nextInt();
			if(N < 0)
				break;
			System.out.println("Enter " + (N+1) + " coefficients");
			int[] coefficients = new int[N+1];
			for(int i = 0; i < N+1; i++)
			{
				coefficients[i] = in.nextInt();
			}
			
			Polynomial p = new Polynomial(coefficients);
			System.out.println("p = " + p);
			
			double[] zeroes = p.zero();
			
			System.out.println("Zeroes:");
			for(int i = 0; i < zeroes.length; i++)
			{
				System.out.println(zeroes[i]);
			}
		}
		
		
		
		in.close();
	}
	
	public Polynomial() {
		degree = 0;
		coefficients = new double[1];
	}
	
	public Polynomial(int[] co) {
		co = copy(co);
		if(co.length == 0)
		{
			degree = 0;
			coefficients = new double[1];
			return;
		}
		degree = co.length - 1;
		coefficients = new double[co.length];
		for(int i = 0; i < co.length; i++)
			coefficients[i] = co[i];
	}
	
	public Polynomial(double[] co) {
		co = copy(co);
		if(co.length == 0)
		{
			degree = 0;
			coefficients = new double[1];
			return;
		}
		degree = co.length - 1;
		coefficients = new double[co.length];
		for(int i = 0; i < co.length; i++)
			coefficients[i] = co[i];
	}
	
	public Polynomial add(Polynomial b) {
		double[] co = new double[Math.max(coefficients.length, b.coefficients.length)];
		
		if(coefficients.length == co.length)
			for(int i = 0; i < co.length; i++)
				co[i] += coefficients[i];
		else {
			int difference = co.length-coefficients.length;
			for(int i = 0; i < coefficients.length; i++)
				co[i+difference] += coefficients[i];
		}
		
		if(b.coefficients.length == co.length)
			for(int i = 0; i < co.length; i++)
				co[i] += b.coefficients[i];
		else {
			int difference = co.length-b.coefficients.length;
			for(int i = 0; i < b.coefficients.length; i++)
				co[i+difference] += b.coefficients[i];
		}
		
		return new Polynomial(co);
	}
	
	public double area(int a, int b) {
		Polynomial integral = integrate();
		
		return integral.valueOf(b) - integral.valueOf(a);
		
	}
	
	public Polynomial copy() {
		double[] co = new double[coefficients.length];
		for(int i = 0; i < co.length; i++)
			co[i] = coefficients[i];
		return new Polynomial(co);
	}
	
	public static int[] copy(int[] co) {
		int index = 0;
		while(co[index++] == 0) if(index == co.length) return new int[0];
		int[] result = new int[co.length-index+1];
		for(int i = 0; i < result.length; i++)
			result[i] = co[i+index-1];
		return result;
	}
	
	public static double[] copy(double[] co) {
		int index = 0;
		while(co[index++] == 0) if(index == co.length) return new double[0];
		double[] result = new double[co.length-index+1];
		for(int i = 0; i < result.length; i++)
			result[i] = co[i+index-1];
		return result;
	}
	
	public Polynomial derivative() {
		if(coefficients.length <= 1)
		{
			return new Polynomial();
		}
		
		double[] co = new double[coefficients.length-1];
		int power = degree;
		
		for(int i = 0; i < co.length; i++)
			co[i] = coefficients[i]*(power--);
		
		return new Polynomial(co);
	}
	
	public Polynomial divide(Polynomial b) {
		return divideAndRem(b)[0];
	}
	
	public Polynomial[] divideAndRem(Polynomial b) {
		Polynomial[] result = new Polynomial[2];
		result[0] = new Polynomial();
		result[1] = new Polynomial();
		if(b.degree > degree)
		{
			result[1] = this;
			return result;
		}
		
		double[] a = copy().coefficients;
		double[] co = new double[degree-b.degree+1];
		for(int i = 0; i < co.length; i++)
		{
			co[i] = a[i]/b.coefficients[0];
			for(int j = 0; j < b.coefficients.length; j++)
			{
				a[i+j] -= co[i]*b.coefficients[j];
			}
		}
		
		result[0] = new Polynomial(co);
		result[1] = new Polynomial(a);
		
		return result;
	}
	
	public double f(double x) {
		return valueOf(x);
	}
	
	public double Halleys(double left, double right) {
		System.out.println("leftH " + left + " rightH: " + right);
		HashSet<Double> hs = new HashSet<>();
		double guess = (left+right)/2;
		Polynomial d = derivative();
		double a = valueOf(guess);
		double b = d.valueOf(guess);
		double c = d.derivative().valueOf(guess);
		
		while(valueOf(guess) != 0)
		{
			guess -= (2*a*b)/(2*b*b-a*c);
			System.out.println("guess = " + guess);
			
			if(hs.contains(guess))
				break;
			hs.add(guess);
			
			a = valueOf(guess);
			b = d.valueOf(guess);
			c = d.derivative().valueOf(guess);
		}
		
		return guess;
	}
	
	public Polynomial integrate() {
		double[] co = new double[coefficients.length+1];
		int power = degree+1;
		for(int i = 0; i < co.length-1; i++)
		{
			co[i] = coefficients[i]/(power--);
		}
		return new Polynomial(co);
	}
	
	public Polynomial mod(Polynomial b) {
		return divideAndRem(b)[1];
	}
	
	public Polynomial multiply(Polynomial b) {
		double[] co = new double[degree + b.degree + 1];
		for(int i = 0; i < coefficients.length; i++)
		{
			for(int j = 0; j < b.coefficients.length; j++)
			{
				co[i+j] += coefficients[i] * b.coefficients[j];
			}
		}
		return new Polynomial(co);
	}
	
	public Polynomial negate() {
		Polynomial b = this.copy();
		for(int i = 0; i < b.coefficients.length; i++)
			b.coefficients[i] *= -1;
		
		return b;
	}
	
	public int numRootsBetween(double left, double right, ArrayList<Polynomial> chain) {
		boolean[] a = new boolean[chain.size()];
		boolean[] b = new boolean[chain.size()];
		
		for(int i = 0; i < chain.size(); i++)
		{
			if(chain.get(i).valueOf(left) > 0)
				a[i] = true;
			if(chain.get(i).valueOf(right) > 0)
				b[i] = true;
		}
		
		int count = 0;
		
		for(int i = 1; i < a.length; i++)
		{
			if(b[i-1] ^ b[i])
				count++;
			if(a[i-1] ^ a[i])
				count--;
		}
		
		return Math.abs(count);
	}
	
	public Polynomial rem(Polynomial b) {
		return divideAndRem(b)[1];
	}
	
	public void split(Queue<Double> q, ArrayList<Double> ranges, ArrayList<Polynomial> chain) {
		if(q.size() == 0)
			return;
		double left = q.poll();
		double right = q.poll();
		double mid = (left + right)/2;
		if(valueOf(mid) == 0)
		{
			double temp = right;
			q.add(left);
			left = mid - 0.000001;
			q.add(left);
			
			ranges.add(left);
			right = mid + 0.000001;
			ranges.add(right);
			
			q.add(right);
			q.add(temp);
		}
		else
		{
			int num = numRootsBetween(left, right, chain);
			if(num == 1)
			{
				ranges.add(left);
				ranges.add(right);
			}
			else if(num > 1)
			{
				q.add(left);
				q.add(mid);
				q.add(mid);
				q.add(right);
			}
		}
		split(q, ranges, chain);
	}
	
	public ArrayList<Polynomial> sturmChain() {
		ArrayList<Polynomial> chain = new ArrayList<>();
		chain.add(this);
		chain.add(derivative());
		while(chain.get(chain.size()-1).degree > 0)
		{
			Polynomial prev = chain.get(chain.size()-2);
			Polynomial now = chain.get(chain.size()-1);
			chain.add(prev.rem(now).negate());
		}
		
		return chain;
	}
	
	public Polynomial subtract(Polynomial b) {
		Polynomial b2 = b.negate();
		return this.add(b2);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int index = degree;
		for(double d : coefficients)
		{
			String next = ((int) d == d)? String.valueOf((int) d) : String.valueOf(d);
			sb.append(next + "x^" + (index--) + " + ");
		}
		return sb.toString().substring(0, sb.toString().length()-6);
	}
	
	public double valueOf(double d) {
		double sum = 0;
		double base = 1;
		for(int i = coefficients.length - 1; i >= 0; i--)
		{
			sum += coefficients[i]*base;
			base *= d;
		}
		return sum;
	}
	
	public double[] zero() {
		double[] result = new double[0];
		
		if(degree == 0)
			return result;
		
		if(degree == 1) {
			result = new double[1];
			result[0] = -coefficients[1]/coefficients[0];
			if(result[0] == 0)
				result[0] = Math.abs(result[0]);
			return result;
		}
		
		if(degree == 2) {
			double discrim = coefficients[1]*coefficients[1]-4*coefficients[0]*coefficients[2];
			
			if(discrim == 0)
			{
				result = new double[1];
				result[0] = -coefficients[1]/(2*coefficients[0]);
				if(result[0] == 0)
					result[0] = Math.abs(result[0]);
				return result;
			}
			else
			{
				result = new double[2];
				result[0] = (-coefficients[1]-Math.sqrt(discrim))/(2*coefficients[0]);
				result[1] = (-coefficients[1]+Math.sqrt(discrim))/(2*coefficients[0]);
			}
			return result;
		}
		
		double max = 0;
		for(int i = 0; i < coefficients.length; i++)
			max = Math.max(Math.abs(coefficients[i]), max);
		
		Queue<Double> q = new LinkedList<>();
		ArrayList<Double> ranges = new ArrayList<>();
		
		q.add(-max-1);
		q.add(max+1);
		
		ArrayList<Polynomial> chain = sturmChain();
		
		split(q, ranges, chain);
		
		result = new double[ranges.size()/2];
		
		for(int i = 0; i < result.length; i++)
		{
			result[i] = Halleys(ranges.get(2*i), ranges.get(2*i+1));
			if(result[i] == 0)
				result[i] = Math.abs(0);
		}
		
		Arrays.sort(result);
		
		return result;
	}

}
