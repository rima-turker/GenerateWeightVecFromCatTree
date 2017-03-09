package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class GenerateLevelTrees_2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	
	public static void main() throws IOException 
	{
		BufferedReader br_MainCategory = null;
		BufferedReader br_ChildFile = null;
		BufferedReader br_MainFile = null;
		FileReader fr = null;

		
		//File log = new File(categoryName+"L"+Integer.parseInt(number)+1);
		String pathMainCategories= System.getProperty("user.dir")+ File.separator+"MainCategoryFile.txt";
		
		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		String line_list = null;
		String line_mainCategory = null;
		String line=null;
		
		while ((line_mainCategory = br_MainCategory.readLine()) != null) 
        {
			String categoryName = line_mainCategory.replace(">", "");
       
			String path= System.getProperty("user.dir")+ File.separator+"CategoryFiles/"+categoryName+"/";
		    
			File[] files = null;
			for (Integer i = 1; i < GenerateLevels_1.levelOfTheTree; i++) 
			{
				files=new File[i+1];
				for (Integer j = 0; j <= i; j++) 
				{
					//files[j]=new File("C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\"+categoryName+"_L0");
					files[j]=new File(System.getProperty("user.dir")+ File.separator+"CategoryFiles/"+categoryName+"/"+categoryName+"_L"+j.toString()); 
					System.out.println(j);
				}
				//System.out.println("--------------------");
				File mergedFile = new File(System.getProperty("user.dir")+ File.separator+"CategoryFiles/"+categoryName+File.separator+
									categoryName+"AllLevel_L"+i.toString());
				mergedFile.createNewFile();
				mergeFiles(files,mergedFile);
			}
        }
		br_MainCategory.close();
		

	}
	
	public static void mergeFiles(File[] files, File mergedFile) {
		 
		HashSet<String> hset = new HashSet<>();
		
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(mergedFile, true);
			 out = new BufferedWriter(fstream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for (File f : files) {
			System.out.println("merging: " + f.getName());
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 
				String aLine;
				while ((aLine = in.readLine()) != null) {
					hset.add(aLine);
					
//					out.write(aLine);
//					out.newLine();
				}
 
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
			
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
	}
}
