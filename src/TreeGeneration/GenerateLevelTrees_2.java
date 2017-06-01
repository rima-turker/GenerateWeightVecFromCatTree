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
	static int levelOfTree=GlobalVariables.levelOfTheTree;
	
	public static void main() throws IOException 
	{
		BufferedReader br_MainCategory = null;
		
		//File log = new File(categoryName+"L"+Integer.parseInt(number)+1);
		String pathMainCategories= System.getProperty("user.dir")+ File.separator+"MainCategoryFile.txt";
		
		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		String line_mainCategory = null;
		while ((line_mainCategory = br_MainCategory.readLine()) != null) 
        {
			String categoryName = line_mainCategory.replace(">", "");
       
			File[] files = null;
			for (Integer i = 0; i < levelOfTree ; i++) 
			{
				files=new File[i+1];
			
				if (i==0) 
				{
					files[i]=new File(System.getProperty("user.dir")+ File.separator+"CategoryFiles"+File.separator+categoryName+File.separator+categoryName+"_L"+i.toString()); 
				
				}
				else
				{
					for (Integer j = 0; j <=i; j++) 
					{
						//files[j]=new File("C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\"+categoryName+"_L0");
						files[j]=new File(System.getProperty("user.dir")+ File.separator+"CategoryFiles/"+categoryName+"/"+categoryName+"_L"+j.toString()); 
						//System.out.println(j);
					}
				}
				
				//System.out.println("--------------------");
				File mergedFile = new File(System.getProperty("user.dir")+ File.separator+"CategoryFiles/"+categoryName+File.separator+
									categoryName+"Tree_L"+i.toString());
				
				File  oldMergedFile= new File(System.getProperty("user.dir")+ File.separator+"CategoryFiles/"+categoryName+File.separator+
						categoryName+"Tree_L"+i.toString()+"___");
				
				if (oldMergedFile.exists()) {
					oldMergedFile.delete();
				}
//				if (mergedFile.exists()) {
//					mergedFile.delete();
//				}
//				mergedFile.createNewFile();
				//mergeFiles(files,mergedFile);
			}
        }
		br_MainCategory.close();
		

	}
	
	public static void mergeFiles(File[] files, File mergedFile) throws IOException {
		 
		HashSet<String> hset = new HashSet<>();
		
		FileWriter fstream = null;
		BufferedWriter bW_MergedFile = null;
		try {
			fstream = new FileWriter(mergedFile,false);
			bW_MergedFile = new BufferedWriter(fstream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		for (File f : files) {
			System.out.println("merging: " + f.getName());
			
			try {
				
				FileInputStream fis;
				fis = new FileInputStream(f);
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));
				String aLine;
				while ((aLine = in.readLine()) != null) 
				{
					hset.add(aLine);
				}
 
				in.close();
				
				
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		for(String hsetline:hset) 
		{
			bW_MergedFile.write(hsetline);
			bW_MergedFile.newLine();
		}
		System.out.println("size"+hset.size());
			
		try {
			bW_MergedFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
	}
}
