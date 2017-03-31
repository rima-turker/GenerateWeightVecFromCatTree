package TreeGeneration;

import java.io.File;
import java.io.IOException;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	
	
	public static void main(String[] args) throws IOException 
	{
//		ReadResults.ReadResultFromAllFile("Example_2"+File.separator+"file_David",
//				"Example_2"+File.separator+"FamEntitiesAnCat_2 (copy)");
		//GenerateLevelTrees_2.main();
		//CountLines.CountUniqueCategories();
		//GenerateCategoryTrees.main();
		//CompareTwoFiles.main();
		//CreateWeightVectorString.main();
		//String pathMainCategories= System.getProperty("user.dir") + File.separator+"MainCategoryFile.txt";
		
//		ReadResults.ReadResultFromAllFile("FamEntAndResults"+File.separator+"FamEntitiesAndCats_2_Result_L7",
//								"FamEntAndResults"+File.separator+"FamEntitiesAnCat_2");
		
		//FamEntities_2.txt
		//ReadResults.ReadResultFromCVSFile("ResultsL6_sort.csv","SubCategory_Count.csv");
		ReadResults.ReadResultFromCVSFile("Example_2"+File.separator+"ResultsL7.csv","SubCategory_Count.csv");
		
//		ReadResults.ReadResultFromAllFile("Example_3"+File.separator+"FamEntitiesAndCats_3_Result_L7",
//		"Example_3"+File.separator+"EntityCategory_Famous_3.csv");
		
		//ReadResults.ReadResultFromCVSFile("Example_3"+File.separator+"EntityCate_Results_3_L7","SubCategory_Count.csv");
		//CalculatePrecisionAndRecall.main("Example_3"+File.separator+"PrecisionRecall_Example_3.csv");
		//CalculatePrecisionAndRecall.MergeTwoFromatedResultFiles(new File("temp_FormatedResults"), new File("temp_PrecisionAndRecall"));
		
		
	}
}
