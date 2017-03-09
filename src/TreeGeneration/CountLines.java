package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class CountLines 
{
	public static void main(String[] args) throws IOException 
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

		while ((line_mainCategory = br_MainCategory.readLine()) != null) 
		{

			String categoryName = line_mainCategory.replace(">", "");
			System.out.print(categoryName+": ");
			for (Integer i = 0; i < 5; i++) 
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
	}
}
