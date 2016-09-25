
import java.io.*;


public class Prime {
	
	public static int prime(int num)
	{
		
		while(true)
		{
			boolean isPrime=true;
			num = num+1;
			int sqt = (int)Math.sqrt(num);
			for(int i=2;i<=sqt;i++)
			{
				if(num%i==0)
				{
					isPrime=false;
					break;
				}
			}
			
			if(isPrime)
			{
				break;
			}
			
	}
		return num;
	}
	
    public static void main(String[] args) throws Exception {
    	BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
    	System.out.println("Enter bits per element");
		int num=Integer.parseInt(r.readLine());
		
		
		}

 	 
    	
    }

