package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

public class CalculatePrecisionAndRecall__ 
{
	static String path = System.getProperty("user.dir") + File.separator;
	static Integer int_treeDepth=7;
	static boolean isEntity =true;
	static FileWriter fileWriter;
	static File log;
	static BufferedWriter bf_Writer;
	public static  void main(String fileName) throws IOException 
	{
			
		log = new File(System.getProperty("user.dir")+"temp_PrecisionAndRecall");
		
		if (log.exists()) 
		{
			log.delete();
		}
		log.createNewFile();
		
		fileWriter = new FileWriter(log,true);
		bf_Writer = new BufferedWriter(fileWriter);
		
		try (BufferedReader br = new BufferedReader(new FileReader(path+fileName));)
			{
				String str_line = null;
				
				HashSet<String> hset_groundTruth = new HashSet<>();
				LinkedList<LinkedHashSet<String>> llist_depthElements = new LinkedList<LinkedHashSet<String>>();
				
				Integer int_depth=int_treeDepth;
				
				Integer int_indexOfHash=0;
				
				str_line = br.readLine();
				while ((str_line = br.readLine()) != null) 
				{
					int_depth=int_treeDepth;
					int_indexOfHash=0;
					String[] str_split = str_line.split(",");
					//System.out.println(str_line);
					isEntity=true;
					
					for (int i = 0; i < str_split.length; i++) 
					{
						
						if (i==0 && str_split[0].length()>1 && isEntity) 
						{
							
							//print(llist_depthElements,hset_groundTruth);
							CalculatePrecisionRecallFmeasure(llist_depthElements,hset_groundTruth);
							llist_depthElements.clear();
							hset_groundTruth.clear();
							//EntityAnCat
							if (!str_split[i].contains(":")&&!str_split[i+1].contains(":")&&str_split[i+1].length()>2) 
							{
								hset_groundTruth.add(str_split[i+1]);
								isEntity=false;
							}
							
						}
					
						else if (!str_split[i].contains(":") && str_split[i].length()>2) 
						{
							hset_groundTruth.add(str_split[i]);
						} 
						
						else if (str_split[i].contains(":")) 
						{
							if (llist_depthElements.size()<=int_indexOfHash) //
							{
								LinkedHashSet<String> hset_temp = new LinkedHashSet<>();
								hset_temp.add(str_split[i].substring(0, str_split[i].indexOf(":")));
								llist_depthElements.add(hset_temp);
								
							}
							else
							{
								llist_depthElements.get(int_indexOfHash).add(str_split[i].substring(0, str_split[i].indexOf(":")));
								
							}
							int_indexOfHash++;
						}
						
					}
					//print(llist_depthElements);
					
					
				}
				//print(llist_depthElements,hset_groundTruth);
				CalculatePrecisionRecallFmeasure(llist_depthElements,hset_groundTruth);
			} 
			catch (IOException e) 
			{

				e.printStackTrace();

			}
		bf_Writer.close();
		System.out.println("Finished writing");
	}
	
	public static void print(LinkedList<LinkedHashSet<String>> llist_depthElements,HashSet<String> groundTruth)
	{
		
		if (llist_depthElements.size()>0) 
		{
			for (int i = 0; i < llist_depthElements.size(); i++) 
			{
				System.out.println(llist_depthElements.get(i));
			}
			
			System.out.println("-----------GroundTruth----------");
			
			
			for (String str_gtruth: groundTruth ) 
			{
				System.out.println(str_gtruth);
				
			}
			System.out.println();
		}
		
	}
	
	public static void CalculatePrecisionRecallFmeasure(LinkedList<LinkedHashSet<String>> llist_depthElements,HashSet<String> groundTruth) throws IOException
	{
		double dbl_relevantElements,dbl_selectedElements,
		
		int_truePositive = 0;
		dbl_relevantElements=groundTruth.size();
		
		
		double precision=0.0, recall=0.0, Fmeasure=0.0;
		String str_formatedP=null;
		String str_formatedR=null;
		
		for (int i = 0; i < llist_depthElements.size(); i++) 
		{
			Iterator<String> it = llist_depthElements.get(i).iterator();
			dbl_selectedElements=llist_depthElements.get(i).size();
			
			while (it.hasNext() )
		    {
				if (groundTruth.contains(it.next()))
				{
					int_truePositive+=1;
				}
			}
				precision = int_truePositive/dbl_selectedElements;
			    recall= int_truePositive/dbl_relevantElements;
			    Fmeasure=2*((precision*recall)/ (precision+recall));
			    
			    if (str_formatedP!=null) 
			    {
			    	str_formatedP =  str_formatedP+" ,"+String.valueOf(precision)+" ,";
			    	str_formatedR =  str_formatedR+" ,"+String.valueOf(recall)+" ,";
				}
			    else
			    {
			    	str_formatedP=String.valueOf(precision)+" ,";
			    	str_formatedR=String.valueOf(recall)+" ,";
			    }
			    
			    int_truePositive=0;
			    
		}
		if (str_formatedP!=null) 
		{
			bf_Writer.write("=SPLIT(\"" + " ,"+str_formatedP + "\",\",\")");
			bf_Writer.newLine();
			bf_Writer.write("=SPLIT(\"" + " ,"+str_formatedR + "\",\",\")");
			bf_Writer.newLine();
			bf_Writer.newLine();
			
		}
	
		
		//System.out.println("=SPLIT(\"" + " ,"+str_formatedP + "\",\",\")");
	}
	
	public static void MergeTwoFromatedResultFiles(File file_First, File file_Second)
	{
		try (BufferedReader br = new BufferedReader(new FileReader(path+file_First));
				BufferedReader br_second = new BufferedReader(new FileReader(path+file_Second));)
		{
			String str_line = null;
			
			while ((str_line = br.readLine()) != null) 
			{
				if (str_line.length()<1) 
				{
//					String str_Sline = br_second.readLine();
					String str_Sline ;
					while ((str_Sline = br_second.readLine()) != null && str_Sline.length()>1) 
					{
						System.out.println(str_Sline);
					}
				}
				else
					System.out.println(str_line);
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
}
