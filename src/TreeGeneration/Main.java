package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
	
		//CountLines.main();
		//GenerateLevels_1.main();
		//GenerateLevelTrees_2.main();
		//CountLines.CountUniqueCategories();
		//GenerateCategoryTrees.main();
		//CompareTwoFiles.main();
		//CreateWeightVectorString.main();
		//String pathMainCategories= System.getProperty("user.dir") + File.separator+"MainCategoryFile.txt";
		
		
		String fS=File.separator;
		String sP=System.getProperty("user.dir");

		BufferedReader br_Entities = null;
		String pathEntities= sP+ fS+"FamEntities.txt";

		br_Entities = new BufferedReader(new FileReader(pathEntities));
		String line_mainCategory = null;
		String line=null;

		while ((line_mainCategory = br_Entities.readLine()) != null) 
		{
			
		}
		br_Entities.close();
	}

}
