import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;



public class BloomFilterDet 
   {
	// 64 Bit FNV Prime
	private static final long FNVPrime64=0x100000001b3L;
	// 64 Bit FNV Offset
	private static final long FNV_64_INIT=0xcbf29ce484222325L;
	static String[] Relation1;
	static String[] DataToCheck;	
	static BitSet BloomFilter;
	static int numberOfHashes;
	static int setSize;
	static int bitsPerElement;
	static int falsePositiveCount=0;
	static int checkDataCount;
	static int relDataCount;
	static long upperBitsInLong= 0L;
	static long lowerBitsInLong = 0L;
	
	public BloomFilterDet(int setSize, int bitsPerElement)
	{
// Calculating size of bloom filter 
	  BloomFilterDet.bitsPerElement=bitsPerElement;
	  BloomFilterDet.setSize=setSize;
// Initiating bloom filter
	  BloomFilterDet.BloomFilter=new BitSet(setSize*bitsPerElement);	
// Calculating number of hash functions
	  this.numHashes();
	}
	

// Adding elements to the Bloom filter
	public void add(String s)
	{
		int kHashedValue = 0;
// Getting first 32 bits and last 32 bits of 64-bit generated FNV hash value
	    HashReturn hashedRetObj=createHashed(s.getBytes());
	    upperBitsInLong=hashedRetObj.upperBits;
	    lowerBitsInLong=hashedRetObj.lowerBits;
/* Generating K hash functions by doing this h(x) = g(x) + f(x)*i + i*i
   Where g(x) is first 32 bits of 64-bit generated FNV hash value
   f(x) is last 32 bits of 64-bit generated FNV hash value
   i loops from "0" to "number of hash functions-1" */
		for(int i=0;i<BloomFilterDet.numberOfHashes;i++)
	  {
	 	kHashedValue=(int)(((hashedRetObj.upperBits)+(hashedRetObj.lowerBits*i)+(i*i))%
	 			     (filterSize()));
// Setting Bitset in bloom filter to 1 based on the hash code returned by k hash hash functions for each element
	 	BloomFilterDet.BloomFilter.set(kHashedValue);
	  }
	}

// Doing FNV Hash Algorithm and getting first 32 bits and last 32 bits of 64 bit generated Hash value.
    public static HashReturn createHashed(byte[] data)
	{
		BitSet fnvHashed = new BitSet();
    	BitSet upperBits = new BitSet();
    	BitSet lowerBits= new BitSet();
    	int index =0;
    	HashReturn hashRetObj=new HashReturn();
    	hashRetObj.lowerBits=0L;
    	hashRetObj.upperBits=0L;
// FNV Algorithm
// Initiating Hash value to 64 Bit FNV Offset: first step of FNV algorithm
    	long hashedval=BloomFilterDet.FNV_64_INIT;
// FNV Algorithm
    	for (int i=0;i<data.length;i++)
    	{
    		hashedval = (BloomFilterDet.FNVPrime64 * hashedval) ;
    		hashedval = hashedval ^ data[i];    		
    		    		
    	}
// Converting long to Bitset in order to take first 32 bits and last 32 bits of 64 bit generated FNV hash code
    	while (hashedval != 0L) 
    	{
	   	      if (hashedval % 2L != 0) 
	   	      {
	   	    	fnvHashed.set(index);
	   	      }
	   	      ++index;
	   	      hashedval = hashedval >>> 1;
	   	}

//Taking first 32 bits of 64 bit generated hash value 
    	upperBits = fnvHashed.get(0, 32);
//Taking last 32 bits of 64 bit generated hash value
    	lowerBits = fnvHashed.get(32, 64);
//converting upper bits to FNV to long	 
 	    for (int i = 0; i < upperBits.length(); ++i) 
 	    {
 		  hashRetObj.upperBits += upperBits.get(i) ? (1L << i) : 0L;
  	    }
//converting lower bits to FNV to long
 	    for (int i = 0; i < lowerBits.length(); ++i)
 	    {
 		 hashRetObj.lowerBits += lowerBits.get(i) ? (1L << i) : 0L;
 	    }
     
 	return hashRetObj;
 	  
	}
    
//Checking if the string passed in the bloom filter or not	
public static boolean contains(String s) {
       boolean check=true;
       byte[] bytes=s.getBytes();
       HashReturn hashedRetObj=createHashed(bytes);
       int kHashedValue = 0;
// Checking if the k hashed cells of the passed string are already  to 1 are not
// if yes then it is a false positive and increase the count of falsePositiveCount
	   for(int i=0;i<BloomFilterDet.numberOfHashes;i++)
	   {
             kHashedValue=(int)(((hashedRetObj.upperBits)+(hashedRetObj.lowerBits*i)+(i*i))%
            		              (BloomFilterDet.bitsPerElement*BloomFilterDet.setSize));
	 		
	 		if(BloomFilterDet.BloomFilter.get(kHashedValue))
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
             BloomFilterDet filter=new BloomFilterDet(fileDataSize,bitsPerElement);
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
		
		return BloomFilterDet.setSize*BloomFilterDet.bitsPerElement;
	}


//Number of bits in the filter that are set to one
	public static long dataSize() 
    {
		
		return BloomFilterDet.BloomFilter.cardinality();
	}


// Method to calculate number of hash functions based on filter size, size of data and bits per element
	public static long numHashes()
    {
		BloomFilterDet.numberOfHashes=(int)(( Math.log(2))*bitsPerElement);
		return BloomFilterDet.numberOfHashes;
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
			BloomFilterDet.convertAndInitBitSet(relationFileName,'r',bitsPerElement);	
			BloomFilterDet.convertAndInitBitSet(comparefileName, 'f',0);
// Returns true if the string is in bloom filter			
		    for(int i=0;i<DataToCheck.length;i++)
		    {
				if(DataToCheck[i]!=null)
				{
					boolean present=BloomFilterDet.appears(DataToCheck[i]);					
				}
			}
		
		   float fRate= (float)falsePositiveCount/checkDataCount;
		   System.out.println("Filter Size             : "+ BloomFilterDet.filterSize());
		   System.out.println("Data Size               : "+ BloomFilterDet.dataSize());
		   System.out.println("Number of Hash Functions: "+ BloomFilterDet.numHashes());
		   System.out.println("False Positive Count    : "+ falsePositiveCount);
		   System.out.println("False Positive Rate     : "+ fRate);
		   System.out.println("Size in File 2          : "+ relDataCount);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
}
