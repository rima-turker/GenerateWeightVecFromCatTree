package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DeleteDublicates_3 {

	
	static String pathMainCategories= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\MainCategoryFile.txt";
	static BufferedReader br_MainCategory = null;
	
	public static void main(String[] args) throws IOException 
	{
		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		String line_mainCategory = null;
		
//		while ((line_mainCategory = br_MainCategory.readLine()) != null) 
//        {
//			String categoryName = line_mainCategory.replace("http://dbpedia.org/resource/Category:", "");
       
		    String fileName = null;
		    String dublicatedFileName = null;
			for (Integer i = 0; i < 5; i++) 
			{
				
				
//				fileName ="C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\"+
//						categoryName+"AllCategory_L"+i.toString()+"_sort"; 
				
				fileName ="C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\FilesForComparison\\AllCategoryAllLevel_L"+i.toString()+"_sort"; 
				
				dublicatedFileName = "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\FilesForComparison\\AllCategoryAllLevel_L"+i.toString(); 
				deleteDuplicatesFromFile(dublicatedFileName,fileName);
			}
 //       }
	}
	
	public static void deleteDuplicatesFromFile(String dublicatedFile,String filename) throws IOException {
	   int size= 1000000;
	   File SortedFile = new File(filename);
	   SortedFile.createNewFile();
	   
		BufferedReader reader = new BufferedReader(new FileReader(dublicatedFile));
	    Set<String> lines = new HashSet<String>(size); 
	    String line;
	    int checkLineCount=0;
	    while ((line = reader.readLine()) != null) {
	    	if (lines.contains(line)) 
	        {
				System.out.println(line);
			}
	    	lines.add(line);
	        
	        checkLineCount++;
	    }
	    reader.close();
	    
	    if (checkLineCount>size) 
	    {
			System.out.println("HashSize smaller than number of lines from"+filename);
		}
	    BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
	    for (String unique : lines) {
	        writer.write(unique);
	        writer.newLine();
	    }
	    writer.close();
	    lines.clear();
	}

}
