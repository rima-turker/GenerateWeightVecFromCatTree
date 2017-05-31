package TreeGeneration;

import java.io.File;

public class Main {

	final private String str_ExampleNo = "3";
	final private static String str_fileReadResult_1 = "CatTree_7FilteredMainFiles"+File.separator+"pagelinks_CategoryTreeFilteredtill_7";
//	final private static String str_fileReadResult_2 = "CatTree_7FilteredMainFiles"+File.separator+"infoBox_CategoryTreeFilteredtill_7";
	final private static String str_fileReadResult_2 = "infoBox_CategoryTreeFilteredtill_7";
	final private static String str_fileReadResult_3 = "pageLinks_CategoryTreeFilteredtill_7";
														//pagelinks_CategoryTreeFilteredtill_7
	//"infoBoxCleaned_OnlyCategoryFiltered_sort"
	public static void main(String[] args) throws Exception {
		
		ReadResultsFromFilteredFiles read = new ReadResultsFromFilteredFiles();
		//WriteReadFromFile.writeMapToAFile(read.ReadResults(read.createCategoryMap(), str_fileReadResult_3),"WeightVector_7");
		WriteReadFromFile.writeMapToAFile(read.ReadResults_withLevel(read.createCategoryMap(), str_fileReadResult_3),"WeightVector_7_withSubCatNumbers");
		System.out.println("Finished");
		
//		double the;
//		
//		for ( the = 0.05; the<=0.09; the += 0.01) {
//			 System.out.println("--------------------Threshold-------------------"+
//			the);
//			for (GlobalVariables.HeuristicType heu : GlobalVariables.HeuristicType.values()) {
//			     new EvaluateHeuristicFunctions<Object>("GoalSet_AllCombined.tsv", the, heu).main();
//			}
//		}
//		
//		Map<String, Double> hmap_fmeasure = new HashMap<>();
//		hmap_fmeasure=MapUtil.entriesSortedByValues(EvaluateHeuristicFunctions.hmap_fmeasureAll);
//		
//		for (Entry <String, Double>   entry: hmap_fmeasure.entrySet()) 
//		{
//			System.out.println(entry.getKey()+" "+entry.getValue());
//		}
//		List<Double> sortedList = new ArrayList<Double>(EvaluateHeuristicFunctions.hset_fmeasure);
//		Collections.sort(sortedList);
//		
//		for (int i =sortedList.size()-1; i>=20 ; i--) 
//		{
//			System.out.println(sortedList.get(i));
//		}

	}
}
	 