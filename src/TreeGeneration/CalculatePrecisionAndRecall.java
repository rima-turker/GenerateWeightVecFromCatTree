package TreeGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class CalculatePrecisionAndRecall 
{
	static String path = System.getProperty("user.dir") + File.separator;
	
	public static  void main(String fileName) 
	{
		path+=fileName;
		
			try (BufferedReader br = new BufferedReader(new FileReader(path));)
			{
				String str_line = null;
				
				HashSet<String> hset_groundTruth = new HashSet<>();
				LinkedList<LinkedHashSet<String>> llist_depthElements = new LinkedList<LinkedHashSet<String>>();
				
				Integer int_depth=7;
				
				str_line = br.readLine();
				while ((str_line = br.readLine()) != null) 
				{
					int_depth=7;
					String[] str_split = str_line.split(",");
					System.out.println(str_line);
					
					for (int i = 0; i < str_split.length; i++) 
					{
						if (i == 0) 
						{
							//EntityAndCat
							if (!str_split[i].contains(":")&&!str_split[i+1].contains(":")&&str_split[i+1].length()>2) 
							{
								hset_groundTruth.add(str_split[i+1]);
								//System.out.println("hset_groundTruth"+str_split[i+1]);
							}
								print(llist_depthElements);
							
						}
						
						else if (!str_split[i].contains(":") && str_split[i].length()>2) 
						{
							hset_groundTruth.add(str_split[i]);
						} 
						
						else 
						{
							if (llist_depthElements.size()<7) 
							{
								LinkedHashSet<String> hset_temp = new LinkedHashSet<>();
								if (str_split[i].contains(":")) 
								{
									hset_temp.add(str_split[i].substring(0, str_split[i].indexOf(":")));
									llist_depthElements.add(hset_temp);
								}
								
							}
							else
							{
								if (str_split[i].contains(":")) 
								{
									llist_depthElements.get(int_depth).add(str_split[i].substring(0, str_split[i].indexOf(":")));
								}
								
							}
							int_depth--;
							//System.out.println("int_depth"+int_depth);
						}
						
					}
					
				}
			} 
			catch (IOException e) 
			{

				e.printStackTrace();

			}
	}
	
	public static void print(LinkedList<LinkedHashSet<String>> llist_depthElements)
	{
		for (int i = 0; i < llist_depthElements.size(); i++) 
		{
			System.out.println(llist_depthElements.get(i));
		}
	}
}
