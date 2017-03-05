package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GenerateLevels_1 {

	/**
	 * @param <E>
	 * @param args
	 * @throws IOException 
	 */
	
	
	public static  void main(String[] args) throws IOException {
	
		BufferedReader br_MainCategory = null;
		BufferedReader br_ChildFile = null;
		BufferedReader br_MainFile = null;
		FileReader fr = null;

		//File log = new File(categoryName+"L"+Integer.parseInt(number)+1);
		BufferedWriter bufferedWriter = null;
		FileWriter fileWriter;
		
		String pathMainCategories= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\MainCategoryFile.txt";
		
		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		String line_list = null;
		String line_mainCategory = null;
		String line=null;
		
		while ((line_mainCategory = br_MainCategory.readLine()) != null) 
        {
			String categoryName = line_mainCategory.replace("http://dbpedia.org/resource/Category:", "");
       
			File Dir = new File("C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\");
			Dir.mkdir();
			
			String path= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\skos_categories_en2015_10.ttl";
		    
			for (Integer i = 0; i < 5; i++) 
			{
				String number= i.toString();
				
				File log=null;
				if (i==0) 
				{
					log = new File("C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\"+
							categoryName+"_L"+(Integer.parseInt(number)));
					if (log.exists()) 
					{
						log.delete();
					}
					log.createNewFile();
					fileWriter = new FileWriter(log, true);
				    bufferedWriter = new BufferedWriter(fileWriter);
				   
				    bufferedWriter.write("<"+line_mainCategory+">");
				    bufferedWriter.close();
				}
				else
				{
					log = new File("C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\"+
							categoryName+"_L"+(Integer.parseInt(number)));
					log.createNewFile();
					
					String pathChildFile= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\"+categoryName+
							"_L"+ (Integer.parseInt(number)-1);
					
					br_ChildFile = new BufferedReader(new FileReader(pathChildFile));
					   
					List<String> listChild = new ArrayList<>();
					
			        while ((line_list = br_ChildFile.readLine()) != null) 
			        {
			            listChild.add(line_list);
			        }
			        br_ChildFile.close();
					
			        
			        br_MainFile = new BufferedReader(new FileReader(path));
			        String str_format =null;
//					int counter =0;
//					String lastLine=null;
					
					fileWriter = new FileWriter(log, true);
				    bufferedWriter = new BufferedWriter(fileWriter);
				    
					while ((line = br_MainFile.readLine()) != null)
					{
						String[] entityAndCategory = line.split(" ");  
						
						str_format=entityAndCategory[2];
						if (listChild.contains(str_format)) 
						{
							if (line.contains("http://www.w3.org/2004/02/skos/core#broader> " +
									"<http://dbpedia.org/resource/Category:")) 
							{
								bufferedWriter.write(entityAndCategory[0]);
								bufferedWriter.newLine();
		
							}
							
						}
							
				    }
					br_MainFile.close();
				}
				bufferedWriter.close();
				}
        }
		System.out.println("Finish Writing");
		bufferedWriter.close();
		br_MainFile.close();
			
	}

}
