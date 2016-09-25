import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class DistributedJoin {
	public static HashMap<String,List<String>> RelationOne=new HashMap<String,List<String>>();
	public static HashMap<String,List<String>> RelationTwo=new HashMap<String,List<String>>();
	
	public static HashMap<String,List<String>> convertfileToArray(String fileName){
		HashMap<String,List<String>> tempDict=new HashMap<String,List<String>>();
		try{
		FileReader inputFile = new FileReader(fileName);
        BufferedReader bufferReader = new BufferedReader(inputFile);
        String line;
        //Input file that has to be added to bloom filter to count false positives
        //Reading data from file and storing it in an array
        int i=0;
      	    while ((line = bufferReader.readLine()) != null)  
            {
              String Key=line.split("   ")[0];
              String Value=line.split("   ")[1];
              if(tempDict.containsKey(Key)){
            	  tempDict.get(Key).add(Value);
              }
              else{
            	  List<String> tempList=new ArrayList<String>();
            	  tempList.add(Value);
            	  tempDict.put(Key, tempList);
              }
            }
        return tempDict;
		}catch(Exception ex){
			return null;
		}
		
	}
	public static void BloomJoin(BloomFilterDet filter,HashMap<String,List<String>> Relation2) {
		String[] elementsPresent=new String[Relation2.size()];
		int i=0;
		File file=new File("Join.txt");
		try {
			file.createNewFile();
			FileWriter writer=new FileWriter("Join.txt");
			for(Map.Entry<String,List<String>> pair: Relation2.entrySet()){
				
				boolean isPresent=BloomFilterDet.appears(pair.getKey().toLowerCase());
				if(isPresent)
				{
					
					List<String> Rel1Data=RelationOne.get(pair.getKey());
					if(Rel1Data!=null&&Rel1Data.size()>0){
					for(int index=0;index<Rel1Data.size();index++){
						for(int rel2index=0;rel2index<pair.getValue().size();rel2index++)
						{
							String val=pair.getValue().get(rel2index);
							if(!val.isEmpty()){
							String toAdd=Rel1Data.get(index)+"\t\t"+pair.getKey()+"\t\t"+pair.getValue().get(rel2index)+System.getProperty("line.separator");
							writer.write(toAdd);
							i++;
							continue;
							}
							else{
								break;
							}
							
						}
					}
				}
				}
			}
			writer.flush();
		      writer.close();
		      System.out.println("no of rows in join:"+i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		}
	
	public static BloomFilterDet CreateFilter(HashMap<String,List<String>> Relation1,int bitsPerElement){
		BloomFilterDet filter=new BloomFilterDet(Relation1.size(), bitsPerElement);
		for(Map.Entry<String,List<String>> pair:Relation1.entrySet()){
			//System.out.println(pair.key);
			filter.add(pair.getKey().toLowerCase());
		}
		return filter;
	}
	public static void main(String[] args){
		int BitsPerElement=0;
		BufferedReader reader=new BufferedReader(new InputStreamReader(System.in));
		
		try {

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			//System.out.println(dateFormat.format(cal.getTime())); 
			System.out.println("Enter the number of Bits per Element");
			BitsPerElement=Integer.parseInt(reader.readLine());
			String RelfileName="Relation1.txt";
			String Rel2FileName="Relation2.txt";
			DistributedJoin.RelationOne=DistributedJoin.convertfileToArray(RelfileName);
			DistributedJoin.RelationTwo=DistributedJoin.convertfileToArray(Rel2FileName);
			BloomFilterDet filterMain=CreateFilter(RelationOne, BitsPerElement);
			DistributedJoin.BloomJoin(filterMain, RelationTwo);
			System.out.println("Joins are stored in the file named Join.txt in root folder");
			System.out.println("Please do refresh on the root folder");
			cal=Calendar.getInstance();
			//System.out.println(dateFormat.format(cal.getTime()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
