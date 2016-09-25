import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Random;





public class BloomFilterRan 
   {
	static String[] Relation1;
	static String[] DataToCheck;	
	static BitSet BloomFilter;
	static int nearestPrime;
	static int numberOfHashes;
	static int setSize;
	static int bitsPerElement;
	static int falsePositiveCount=0;
	static int checkDataCount;
	static int relDataCount;
	static long upperBitsInLong= 0L;
	static long lowerBitsInLong = 0L;
	
	public BloomFilterRan(int setSize, int bitsPerElement)
	{
// Calculating size of bloom filter 
	  BloomFilterRan.bitsPerElement=bitsPerElement;
	  BloomFilterRan.setSize=setSize;
	  nearestPrime = Prime.prime(setSize*bitsPerElement);
// Initiating bloom filter
	  BloomFilterRan.BloomFilter=new BitSet(nearestPrime);	
// Calculating number of hash functions
	  this.numHashes();
	}
	

// Adding elements to the Bloom filter
	public void add(String s)
	{
		Random random = new Random();
		long a,b;
		int kHashedValue = 0;
		for(int i=0;i<BloomFilterRan.numberOfHashes;i++)
	  {
			 a = random.nextInt(nearestPrime)+0;
			 b = random.nextInt(nearestPrime)+0;
//Hashing using h(x) = ax+b%p where p is the nearest prime to the size of the filter 
// a and b are random numbers
	 	kHashedValue= (int)(a *((long)s.hashCode())+b)% nearestPrime;
// Setting Bitset in bloom filter to 1 based on the hash code returned by k hash hash functions for each element
	 	BloomFilterRan.BloomFilter.set(Math.abs(kHashedValue));
	  }
	}

    
//Checking if the string passed in the bloom filter or not	
public static boolean contains(String s) {
       boolean check=true;
       int kHashedValue = 0;
       long a,b;
       Random random = new Random();
// Checking if the k hashed cells of the passed string are already  to 1 are not
// if yes then it is a false positive and increase the count of falsePositiveCount
	   for(int i=0;i<BloomFilterRan.numberOfHashes;i++)
	   {
		  a = random.nextInt(nearestPrime)+0;
		  b = random.nextInt(nearestPrime)+0;
	 	  kHashedValue= (int)(a*((long)s.hashCode())+b)% nearestPrime;
	 		
	 		if(BloomFilterRan.BloomFilter.get(Math.abs(kHashedValue)))
	 		{
	 			continue;
	 		}
	 		else
	 		{
	 			check=false;
	 			break;
	 		}
	 	}
// if all the k hashed cells of the newly passed strings are set to 1, then increase the count of falsePositiveCount
		 if(check)
		 {
		    falsePositiveCount++;
		  }
	    return check;
	    
    }

// Reading data from file and storing it in an array
      public static void convertAndInitBitSet(String fileName,char op,int bitsPerElement)
      {
		int i=0;
		int fileDataSize=0;
		try
		{
		  FileReader inputFile = new FileReader(fileName);
          BufferedReader bufferReader = new BufferedReader(inputFile);
          String line;
// Input file that has to be added to bloom filter to count false positives
// Reading data from file and storing it in an array
          if(op=='r')
          {
        	  Relation1=new String[3000000];
              while ((line = bufferReader.readLine()) != null)  
              {
                Relation1[i]=line;
	            i++;
	            fileDataSize++;
              }
          relDataCount=fileDataSize;
// Initializing Bloomfilter class 
             BloomFilterRan filter=new BloomFilterRan(fileDataSize,bitsPerElement);
             for(i=0;i<fileDataSize;i++)
             {
// Adding elements to the bloomfilter
 // case insensitive
        	  filter.add(Relation1[i].toLowerCase());
             }
          }
//Input file to count the false positives
// Reading data from file and storing it in an array
          else if(op=='f')
          {
        	  DataToCheck=new String[3000000];
        	  while ((line = bufferReader.readLine()) != null) 
        	  {
                  DataToCheck[i]=line;
      	          i++;
      	          fileDataSize++;
        	  }
        	  checkDataCount=fileDataSize;
          }
          bufferReader.close();
       }
		
		catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}	
	}
		
// Returns true if the string is already present in bloom filter or else returns false	
// case insensitive
	public static boolean appears(String s)
	{
		return contains(s.toLowerCase());
	} 
	

// size of the bloom filter
	public static long filterSize() 
    {
		
		return nearestPrime;
	}


//Number of bits in the filter that are set to one
	public static long dataSize() 
    {
		
		return BloomFilterRan.BloomFilter.cardinality();
	}


// Method to calculate number of hash functions based on filter size, size of data and bits per element
	public static long numHashes()
    {
		BloomFilterRan.numberOfHashes=(int)(( Math.log(2))*bitsPerElement);
		return BloomFilterRan.numberOfHashes;
	}

	
    public static void main(String[] args)
    {
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		try 
		{
			System.out.println("Enter bits per element");
			int bitsPerElement=Integer.parseInt(reader.readLine());
// test.txt is the file to add data to bloom filter
			String relationFileName="Relation1.txt";
//check.txt is the file to calculate the false positives
			String comparefileName="Relation2.txt";
// Reading the data from file and storing in array
			BloomFilterRan.convertAndInitBitSet(relationFileName,'r',bitsPerElement);	
			BloomFilterRan.convertAndInitBitSet(comparefileName, 'f',0);
// Returns true if the string is in bloom filter			
		    for(int i=0;i<DataToCheck.length;i++)
		    {
				if(DataToCheck[i]!=null)
				{
					boolean present=BloomFilterRan.appears(DataToCheck[i]);					
				}
			}
		
		   float fRate= (float)falsePositiveCount/checkDataCount;
		   System.out.println("Filter Size             : "+ BloomFilterRan.filterSize());
		   System.out.println("Data Size               : "+ BloomFilterRan.dataSize());
		   System.out.println("Number of Hash Functions: "+ BloomFilterRan.numHashes());
		   System.out.println("False Positive Count    : "+ falsePositiveCount);
		   System.out.println("False Positive Rate     : "+ fRate);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
}
