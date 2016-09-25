import java.util.*;
import java.lang.*;
import java.io.*;

class GFG {
	public static void main (String[] args) {
	    
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("Enter number of test cases");
	num_test_cases = Integer.parseInt(reader.readLine());
	int num_users[] = new int[num_test_cases];
	String user_links = new String[num_test_cases];

	for (int i=0;i<num_test_cases;i++) 
	{
	    System.out.println("Enter number of users");
	    num_users[i] = Integer.parseInt(reader.readLine());
	    System.out.println("Enter user links");
	    user_links[i] = reader.readLine();
	}

	for (int i=0;i<num_test_cases;i++)
	{
		int parts[] = user_links[i].split(" ");

	}


	    
	}
}