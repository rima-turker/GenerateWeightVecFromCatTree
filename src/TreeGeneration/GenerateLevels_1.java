package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

public class GenerateLevels_1 {

	/**
	 * @param <E>
	 * @param args
	 * @throws IOException 
	 */
	
	final static int levelOfTheTree=GlobalVariables.levelOfTheTree;

	public static  void main() throws IOException 
	{

		String fileSeparator=File.separator;
		String sysProperty=System.getProperty("user.dir");

		BufferedReader br_MainCategory = null;
		BufferedReader br_MainFile = null;
		FileReader fr = null;

		//File log = new File(categoryName+"L"+Integer.parseInt(number)+1);
		BufferedWriter bufferedWriter = null;

		
		FileWriter fileWriter;
		String pathMainCategories= sysProperty+ fileSeparator+"MainCategoryFile.txt";

		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		String line_mainCategory = null;
		String line=null;


		br_MainFile = new BufferedReader(new FileReader(sysProperty+ fileSeparator+ "skos_broader_CatCleaned_sort.txt"));
		
		while ((line_mainCategory = br_MainCategory.readLine()) != null) 
		{
			String categoryName = line_mainCategory.replace(">", "");

			File Dir = new File(System.getProperty("user.dir")+File.separator+"CategoryFiles"+File.separator+categoryName+File.separator);
			
			if (!Dir.exists()) 
			{
				Dir.mkdir();
			}

			for (Integer i = 0; i < levelOfTheTree ; i++) 
			{
				String number= i.toString();

				
				 File log = new File(System.getProperty("user.dir")+File.separator+"CategoryFiles"+File.separator+categoryName+File.separator+
						categoryName+"_L"+(Integer.parseInt(number)));
				
				if (log.exists()) 
				{
					log.delete();
				}
				log.createNewFile();
				
				fileWriter = new FileWriter(log,false);
				bufferedWriter = new BufferedWriter(fileWriter);
				if (i==0) 
				{
					bufferedWriter.write(line_mainCategory);
				}
				else
				{

					String pathChildFile= System.getProperty("user.dir")+File.separator+"CategoryFiles"+File.separator+categoryName+
							File.separator+categoryName+"_L"+ (Integer.parseInt(number)-1);


					fileWriter = new FileWriter(log, false);
					bufferedWriter = new BufferedWriter(fileWriter);

					BufferedReader br_CategoryLevel = new BufferedReader(new FileReader(pathChildFile));
					
					br_MainFile = new BufferedReader(new FileReader(sysProperty+ fileSeparator+ "skos_broader_CatCleaned_sort.txt"));
					String lineCategory;

					int count = 0;

					HashSet<String> hsetChildCategory = new HashSet<>();
					HashSet<String> hsetParents = new HashSet<>();

					while ((lineCategory = br_CategoryLevel.readLine()) != null) 
					{
						//String onlyCategoryName=lineCategory.substring( lineCategory.indexOf("Category:"), lineCategory.length());
						hsetChildCategory.add(lineCategory);
					}

					while ((line = br_MainFile.readLine()) != null)
					{
							if (hsetChildCategory.contains(line.split(" ")[1]))
							{

								hsetParents.add(line.split(" ")[0]);
							}
					}

					for(String hsetline:hsetParents) 
					{
						bufferedWriter.write(hsetline);
						bufferedWriter.newLine();
					}
					System.out.println("size"+hsetParents.size());
					//System.out.println("count"+count);
					count=0;

					br_MainFile.close();
					br_CategoryLevel.close();
					bufferedWriter.close();
				}

				bufferedWriter.close();
			}
			System.out.println("Finish Writing");
			bufferedWriter.close();
			br_MainFile.close();
		}
		br_MainCategory.close();
		
	}
	
	
}

