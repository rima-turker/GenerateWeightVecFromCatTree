package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class CountLines 
{
	
	static int levelOfTree= GlobalVariables.levelOfTheTree;
	
	public static void main() throws IOException 
	{
		String fileSeparator=File.separator;
		String sysProperty=System.getProperty("user.dir");

		BufferedReader br_MainCategory = null;
		String pathMainCategories= sysProperty+ fileSeparator+"MainCategoryFile.txt";

		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		String line_mainCategory = null;
		String line=null;

		while ((line_mainCategory = br_MainCategory.readLine()) != null) 
		{

			String categoryName = line_mainCategory.replace(">", "");
			System.out.print(categoryName+": ");
			for (Integer i = 0; i < levelOfTree; i++) 
			{
				String number= i.toString();

				String pathChildFile= System.getProperty("user.dir")+File.separator+"CategoryFiles"+File.separator+categoryName+
						File.separator+categoryName+"_L"+ (Integer.parseInt(number));

				BufferedReader br_CategoryLevel = new BufferedReader(new FileReader(pathChildFile));

				String lineCategory;

				HashSet<String> hsetChildCategory = new HashSet<>();

				while ((lineCategory = br_CategoryLevel.readLine()) != null) 
				{
					//String onlyCategoryName=lineCategory.substring( lineCategory.indexOf("Category:"), lineCategory.length());
					hsetChildCategory.add(lineCategory);
				}
				System.out.print(" "+hsetChildCategory.size());
				
				br_CategoryLevel.close();
			}
			System.out.println();
			
		}
		br_MainCategory.close();
	}
	
	public static void CountUniqueCategories() throws IOException
	{
		BufferedReader br_MainFile = new BufferedReader(new FileReader(GlobalVariables.path_MainSkosFile));
		String line = null;
		HashSet<String> hset = new HashSet<>();
		while ((line = br_MainFile.readLine()) != null) 
		{
			String[] str_split = line.split(" ");
			hset.add(str_split[0]);
			hset.add(str_split[1]);
		}
		
		System.out.println("Unique Cat Number:"+hset.size() );
		br_MainFile.close();
	}

}
