package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashSet;

public class GenerateCategoryTrees {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	static int level=GlobalVariables.levelOfTheTree;
	static int categoryNCount=35;
	
	public static void main() throws IOException
	{
		
		BufferedReader br_MainCategory = null;
		FileReader fr = null;

		//File log = new File(categoryName+"L"+Integer.parseInt(number)+1);
		
		String pathMainCategories = GlobalVariables.pathMainCategories;
		
		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		String line_mainCategory = null;
		File[] files = null;
		Integer int_fileIndex=0;
		String categoryName=null;
		
		for (Integer i = 0; i < level; i++) 
		{
			files=new File[categoryNCount];
			
			br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
			
			while ((line_mainCategory = br_MainCategory.readLine()) != null) 
	        {
				categoryName = line_mainCategory.replace(">", "");
	       
				String pathMergeFile= System.getProperty("user.dir")+ File.separator+"CategoryFiles"+File.separator+categoryName+File.separator+
						categoryName+"Tree_L"+i.toString();
				
				files[int_fileIndex]=new File(pathMergeFile);
				int_fileIndex++;
				
	        }
			br_MainCategory.close();
			int_fileIndex=0;
			File mergedFile = new File(System.getProperty("user.dir")+File.separator+"CategoryTrees"+File.separator+"CategoryTrees_L"
								+i.toString());
				mergedFile.createNewFile();
				GenerateCategoryCompleteTrees_2.mergeFiles(files,mergedFile);
			
		}
		br_MainCategory.close();
	}
	
	

}
