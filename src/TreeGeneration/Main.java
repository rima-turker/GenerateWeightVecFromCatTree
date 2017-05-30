package TreeGeneration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	final static String str_ExampleNo="3";
	
	public static void main(String[] args) throws IOException 
	{
		
	//	GenerateCategoryLevels_1.main();
	//	 CategoryToCategoryPath.main();
		ReadResultsFromFilteredFiles.createCategoryMap();
		
//		System.out.println("hmap_testSetAddResourcePathsAlsoReverse");
//		for (double the = 0.1; the <= 0.6; the+=0.1) 
//		{
//			System.out.println("--------------------Threshold-------------------"+ the);
//			for (GlobalVariables.HeuristicType heu : GlobalVariables.HeuristicType.values())
//			{
//				//System.out.println("----"+heu+"-----");
//				EvaluateHeuristicFunctions.main("GoalSet_AllCombined.tsv","Results_All_L7",the,heu);		
//			}
//			System.out.println("----------------------------------------");
//		}
//		
//		 
//	           
//	     List<Double> list = new ArrayList<Double>(EvaluateHeuristicFunctions.hset_fmeasure);
//	     java.util.Collections.sort(list);
//	       // Displaying list
//	     
//	     
//	     System.out.println("HashSet elements after sorting: "+Collections.max(list));
		
		
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
		//ReadResults.ReadResultFromCVSFile("Example_2"+File.separator+"ResultsL7.csv","SubCategory_Count.csv");
		
//		ReadResults.ReadResultFromAllFile("Example_3"+File.separator+"FamEntitiesAndCats_3_Result_L7",
//		"Example_3"+File.separator+"EntityCategory_Famous_3.csv");
		
		
		
//		ReadResults.ReadResultFromCVSFile("Example_"+str_ExampleNo+File.separator+"ResultsL7_Ex"+str_ExampleNo+".csv","SubCategory_Count.csv");
//		CalculatePrecisionAndRecall.main("Example_"+str_ExampleNo+File.separator+"GoalSet_Example_"+str_ExampleNo+"_Mary.csv");
//		CalculatePrecisionAndRecall.MergeTwoFromatedResultFiles(new File("Example_"+str_ExampleNo+File.separator+"ResultsL7_Ex"+str_ExampleNo+".csv_splitResults"), 
//											new File("Example_"+str_ExampleNo+File.separator+"GoalSet_Example_"+str_ExampleNo+"_Mary.csv_PAndR"));
		
	}
}											
