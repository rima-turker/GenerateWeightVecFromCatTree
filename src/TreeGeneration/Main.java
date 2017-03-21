package TreeGeneration;

import java.io.IOException;

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
		
		//ReadResults.ReadResultFromAllFile("file_All");
		ReadResults.ReadResultFromCVSFile("ResultsL6_sort.csv","SubCategory_Count.csv");
	}
}
