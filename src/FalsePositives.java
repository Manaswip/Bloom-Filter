import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.BitSet;



public class FalsePositives 
   {	
	


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
			float fRate;			
// Reading the data from file and storing in array
			BloomFilterDet.convertAndInitBitSet(relationFileName,'r',bitsPerElement);	
			BloomFilterDet.convertAndInitBitSet(comparefileName, 'f',0);
			BloomFilterDet detFilter=new BloomFilterDet(BloomFilterDet.relDataCount,bitsPerElement);
            for(int i=0;i<BloomFilterDet.relDataCount;i++)
            {
//Adding elements to the bloomfilter
// case insensitive
            	detFilter.add(BloomFilterDet.Relation1[i].toLowerCase());
            }
            BloomFilterRan.convertAndInitBitSet(relationFileName, 'r', bitsPerElement);
            BloomFilterRan.convertAndInitBitSet(comparefileName, 'f',0);
            BloomFilterRan ranFilter=new BloomFilterRan(BloomFilterRan.relDataCount,bitsPerElement);
            for(int i=0;i<BloomFilterRan.relDataCount;i++)
            {
//Adding elements to the bloomfilter
// case insensitive
            	ranFilter.add(BloomFilterRan.Relation1[i].toLowerCase());
            }
// Returns true if the string is in bloom filter
			 for(int i=0;i<BloomFilterDet.DataToCheck.length;i++)
			    {
					if(BloomFilterDet.DataToCheck[i]!=null)
					{
						boolean present=BloomFilterDet.appears(BloomFilterDet.DataToCheck[i]);					
					}
				}
			 
			   fRate= (float)BloomFilterDet.falsePositiveCount/BloomFilterDet.checkDataCount;
			   System.out.println("Information about Deterministic bloom filter");
			   System.out.println("Filter Size             : "+ BloomFilterDet.filterSize());
			   System.out.println("Data Size               : "+ BloomFilterDet.dataSize());
			   System.out.println("Number of Hash Functions: "+ BloomFilterDet.numHashes());
			   System.out.println("False Positive Count    : "+ BloomFilterDet.falsePositiveCount);
			   System.out.println("False Positive Rate     : "+ fRate);
			   System.out.println("******************************************************************");
			   
		    for(int i=0;i<BloomFilterRan.DataToCheck.length;i++)
		    {
				if(BloomFilterRan.DataToCheck[i]!=null)
				{
					boolean present=BloomFilterRan.appears(ranFilter.DataToCheck[i]);					
				}
			}
		
		   fRate= (float)BloomFilterRan.falsePositiveCount/BloomFilterRan.checkDataCount;
		   System.out.println("Information about Random bloom filter");
		   System.out.println("Filter Size             : "+ BloomFilterRan.filterSize());
		   System.out.println("Data Size               : "+ BloomFilterRan.dataSize());
		   System.out.println("Number of Hash Functions: "+ BloomFilterRan.numHashes());
		   System.out.println("False Positive Count    : "+ BloomFilterRan.falsePositiveCount);
		   System.out.println("False Positive Rate     : "+ fRate);
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
}
