package TreeGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;


public class EvaluateHeuristicFunctions {
	
	private static String str_path = System.getProperty("user.dir") + File.separator;
	private static int int_depthOfTheTree = 7;
	private static String str_depthSeparator = "__";
	private static double threshold = 0;
	private static GlobalVariables.HeuristicType heuristic;
	
	private static final Logger LOG = Logger.getLogger(CalculatePrecisionAndRecall.class.getCanonicalName());
	private static final Logger log_heuResult = Logger.getLogger("heuResultLogger");
	private static final Logger log_normalized = Logger.getLogger("reportsLogger");

	private static final Map<String, ArrayList<Double>> hmap_subCategoryCount = new HashMap<>();
	private static final HashMap<String, String> hmap_groundTruth = new LinkedHashMap<>();
	private static final Map<String, LinkedList<String>> hmap_groundTruthlist = new LinkedHashMap<>();
	//private static final Map<String, LinkedHashMap<String, Double>> hmap_precisionRecall = new LinkedHashMap<>();
	private static final Map<String, Integer> hmap_entityStartingCat = new LinkedHashMap<>();
	public static final HashSet<Double> hset_fmeasure = new HashSet<>();

	private static LinkedHashMap<String, HashMap<String, Double>> testSetDistinctPaths(Map<String, HashMap<String, Double>> map)
	{
		LinkedHashMap<String, HashMap<String, Double>> hmap_entityAndPaths_ordered = new LinkedHashMap<>();
		LinkedHashMap<String, HashMap<String, Double>> hmap_finaltestSet = new LinkedHashMap<>();
		
		for (Entry<String, String> entry_entityCatAndPath : hmap_groundTruth.entrySet()) 
		{
			String str_entityName = entry_entityCatAndPath.getKey();
			for (Integer i = GlobalVariables.levelOfTheTree; i >=1 ; i--) 
			{
				String str_entityAndDept = str_entityName+GlobalVariables.str_depthSeparator+i.toString();
				HashMap<String, Double> hmap_tempNewValues = new HashMap<>(); 
				HashMap<String, Double> hmap_currentList = map.get(str_entityAndDept);
				
				if (i == 1) 
				{
					hmap_tempNewValues = map.get(str_entityAndDept);
				}
				else
				{
					Integer int_indexBefore = i-1;
					HashMap<String, Double> hmap_beforeList = map.get(str_entityName+GlobalVariables.str_depthSeparator+int_indexBefore.toString());
					for (Entry<String, Double> entry_catAndPath : hmap_currentList.entrySet()) 
					{
						if (hmap_beforeList.containsKey(entry_catAndPath.getKey())) 
						{
							if ((entry_catAndPath.getValue()-hmap_beforeList.get(entry_catAndPath.getKey())>0)) 
							{
								hmap_tempNewValues.put(entry_catAndPath.getKey(), (entry_catAndPath.getValue()-hmap_beforeList.get(entry_catAndPath.getKey())));
							}
						}
						else
						{
							hmap_tempNewValues.put(entry_catAndPath.getKey(), entry_catAndPath.getValue());
						}
					}
				}
				hmap_finaltestSet.put(str_entityAndDept, hmap_tempNewValues);
			}
		}
		for(Entry<String,HashMap<String, Double>> entry:map.entrySet())
		{
			//System.out.println( entry.getKey() + " = " + hmap_finaltestSet.get(entry.getKey()));
			hmap_entityAndPaths_ordered.put(entry.getKey(), hmap_finaltestSet.get(entry.getKey()));
		}
	//	printMap(hmap_entityAndPaths_ordered);
	//	printMap(hmap_testSet);
		return hmap_entityAndPaths_ordered;
	}
	
	
	public static void main(String str_fileNameGroundTruthList, String str_fileNameTestSet, Double db_threshold,
			GlobalVariables.HeuristicType heu) 
	{
		
		String str_fileResourcePaths = "InfoMappingPageCombanied.txt";
		String str_fileResourcePaths_reverse = "EntityAsObj_CatFiltered_reverse";
		
		heuristic = heu;
		threshold = db_threshold;
		InitializeGroundTruthAndList(str_fileNameGroundTruthList);
		LinkedHashMap<String, HashMap<String, Double>> hmap_testSetInitial = InitializeTestSet(str_fileNameTestSet);
		//printMap(initializeMapForTFIDF(hmap_testSetInitial));
		//printMap(hmap_testSet);
		//EntityAsObj_CatFiltered_reverse
		
		LinkedHashMap<String, HashMap<String, Double>> hmap_testSetDistinctPaths= testSetDistinctPaths(hmap_testSetInitial);
		
		
		LinkedHashMap<String, HashMap<String, Double>> hmap_testSetAddResourcePaths= testSetAddResourcePaths(hmap_testSetDistinctPaths,str_fileResourcePaths);
		//printMap(initializeMapForTFIDF(hmap_testSetAddResourcePaths));
		
		//printMap(hmap_testSetAddResourcePaths);
//		
		LinkedHashMap<String, HashMap<String, Double>> hmap_testSetAddResourcePathsAlsoReverse= testSetAddResourcePaths(hmap_testSetAddResourcePaths,str_fileResourcePaths_reverse);
		//printMap(hmap_testSetAddResourcePathsAlsoReverse);
 		
		
		//printMapOrdered(ReadCleanFileRemoveCircles.testSetForEntities(),hmap_testSetDistinctPaths);
		Map<String, LinkedHashMap<String, Double>> hmap_heuResult=callHeuristic(hmap_testSetAddResourcePathsAlsoReverse);// hmap_heuResult
		//printMap(hmap_heuResult);
		//discoverIrrelaventPaths(hmap_heuResult);
		Map<String, LinkedHashMap<String, Double>> hmap_addCatValuesTillDepth= addCatValuesTillDepth(hmap_heuResult);
		//printMap(hmap_addCatValuesTillDepth);
		Map<String, LinkedHashMap<String, Double>> hmap_normalizedEntDepthBased=normalize_entityAndDepthBasedNormalization(hmap_addCatValuesTillDepth);
		//printMap(hmap_normalizedEntDepthBased);
		
		//printMap(hmap_testSet);
//		hmap_heuResult=callHeuristic(hmap_testSet);// hmap_heuResult
//		//printMap(hmap_heuResult);
//		printMap(hmap_heuResultNormalized);
		
		
//		normalization_EntityBased();// hmap_heuResultNormalized
//		printMap(hmap_heuResultNormalized);
		Map<String, LinkedHashMap<String, Double>> hmap_filteredResults= filterHeuResults(hmap_normalizedEntDepthBased);// hmap_heuResultNormalizedSortedFiltered
//		printMap(hmap_filteredResults);
		//callCalculatePrecisionAndRecall(hmap_filteredResults);
		//discoverIrrelaventPaths(hmap_filteredResults);
		//printMap(hmap_entityStartingCat);
		calculatePreRcallFscore_levelBased(hmap_filteredResults);
		
	}

	private static void discoverIrrelaventPaths(Map<String, LinkedHashMap<String, Double>> hmap_heuristic)
	{
		for (Entry<String, LinkedHashMap<String, Double>> entry : hmap_heuristic.entrySet()) 
		{
			String str_entityNameAndDepth = entry.getKey();
			String str_depth = str_entityNameAndDepth.substring(str_entityNameAndDepth.indexOf(str_depthSeparator) + str_depthSeparator.length(),
							   str_entityNameAndDepth.length());
			String str_entityName = str_entityNameAndDepth.substring(0,str_entityNameAndDepth.indexOf(str_depthSeparator));
			
			LinkedHashMap<String, Double> lhmap_catAnVal = entry.getValue();
			HashSet<String> hset_retreived = new HashSet<>();
			
			for (Entry<String, Double> entry_cat : lhmap_catAnVal.entrySet()) 
			{
				hset_retreived.add(entry_cat.getKey());
			}
			final LinkedList<String> llist_groundTruth = hmap_groundTruthlist.get(str_entityName);

//			System.out.println(llist_groundTruth);
//			System.out.println(hset_retreived);
			

			for (String str_cat: hset_retreived) 
			{
				if (!llist_groundTruth.contains(str_cat)) 
				{
					System.out.println(str_entityName+","+str_depth+","+str_cat+",");
				}
				
			}
			
		}

	}
	public static Map<String, LinkedHashMap<String, Double>> addCatValuesTillDepth(Map<String, LinkedHashMap<String, Double>> hmap_heuristic)
	{
		Map<String, LinkedHashMap<String, Double>> hmap_result = new LinkedHashMap<>();
		for (Entry<String, LinkedHashMap<String, Double>> entry : hmap_heuristic.entrySet()) 
		{
			String str_entityNameAndDepth = entry.getKey();
			String str_depth = str_entityNameAndDepth.substring(str_entityNameAndDepth.indexOf(str_depthSeparator) + str_depthSeparator.length(),
							   str_entityNameAndDepth.length());
			String str_entityName = str_entityNameAndDepth.substring(0,str_entityNameAndDepth.indexOf(str_depthSeparator));
			
			LinkedHashMap<String, Double> lhmap_catAnVal = entry.getValue();
			
			LinkedHashMap<String, Double> lhmap_resultcatAnVal = entry.getValue();
			for (Entry<String, Double> entry_cat : lhmap_catAnVal.entrySet()) 
			{
				String str_cat= entry_cat.getKey();
				Double db_catVal =entry_cat.getValue();
				for (Integer i = 1; i < Integer.parseInt(str_depth); i++)
				{
					LinkedHashMap<String, Double> lhmap_temp = hmap_heuristic.get(str_entityName+GlobalVariables.str_depthSeparator+i.toString());
					if (lhmap_temp.containsKey(str_cat)) 
					{
						db_catVal+=lhmap_temp.get(str_cat);
					} 
						
				}
				lhmap_resultcatAnVal.put(str_cat, db_catVal);
			}
			hmap_result.put(str_entityNameAndDepth, lhmap_resultcatAnVal);
		}
		//printMap(hmap_result);
		Map<String, LinkedHashMap<String, Double>> hmap_resultAddCat = new LinkedHashMap<>();
		Map<String, LinkedHashMap<String, Double>> hmap_resultAddCat_sort = new LinkedHashMap<>();
		
		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) 
		{
			String str_entity = entry.getKey();
			LinkedHashMap<String, Double> lhmap_temp = new LinkedHashMap<>();
			HashSet<Double> hset_ValuesToNormalize = new HashSet<>();
			String str_entityNameAndDepth = entry.getKey();

			for (Integer i = 0; i <GlobalVariables.levelOfTheTree ; i++) 
			{
				LinkedHashMap<String, Double> ll_currCatAndVal= hmap_result.get(str_entity + str_depthSeparator + i.toString());

				Integer int_indexNext= i+1; 
				
				if (i==0) 
				{
					hmap_resultAddCat.put(str_entity + str_depthSeparator + int_indexNext.toString(), hmap_result.get(str_entity + str_depthSeparator + int_indexNext.toString()));
					
				}
				else
				{
					LinkedHashMap<String, Double> ll_nextCatAndVal= hmap_result.get(str_entity + str_depthSeparator + int_indexNext.toString());
//					if (ll_currCatAndVal.isEmpty()) 
//					{
//						hmap_resultAddCat.put(str_entity + str_depthSeparator + i.toString(), ll_currCatAndVal);
//					}
					for (Entry<String, Double> entry_currcatAndVal : ll_currCatAndVal.entrySet()) 
					{
						String str_cat = entry_currcatAndVal.getKey();
						
						if (!ll_nextCatAndVal.containsKey(str_cat)) 
						{
							ll_nextCatAndVal.put(entry_currcatAndVal.getKey(), entry_currcatAndVal.getValue());
						}
					}
					
					hmap_resultAddCat.put(str_entity + str_depthSeparator + int_indexNext.toString(), ll_nextCatAndVal);
				}
			}
		}
		//printMap(hmap_resultAddCat);
		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) 
		{
			String str_entity = entry.getKey();
			LinkedHashMap<String, Double> lhmap_temp = new LinkedHashMap<>();
			HashSet<Double> hset_ValuesToNormalize = new HashSet<>();

			for (Integer i = GlobalVariables.levelOfTheTree; i >=1 ; i--) 
			{
				hmap_resultAddCat_sort.put(str_entity + str_depthSeparator + i.toString(), hmap_resultAddCat.get(str_entity + str_depthSeparator + i.toString()));
			}
		}
		
		return hmap_resultAddCat_sort;
		
	}
	public static Map<String, LinkedHashMap<String, Double>>  normalize_entityAndDepthBasedNormalization(Map<String, LinkedHashMap<String, Double>> hmap_heuResult) 
	{
		Map<String, LinkedHashMap<String, Double>> hmap_heuResultNormalized = new LinkedHashMap<>();
		for (Entry<String, LinkedHashMap<String, Double>> entry :hmap_heuResult.entrySet()) 
		{
			if (entry.getValue().size()>0) 
			{
				hmap_heuResultNormalized.put(entry.getKey(), NormalizeMap(entry.getValue()));
			}
			else
				hmap_heuResultNormalized.put(entry.getKey(), new LinkedHashMap<>());
		}
		return hmap_heuResultNormalized;
	}
		

	//EntityAndDepthBased{ 

	public static  Map<String, LinkedHashMap<String, Double>>  normalization_EntityBased(Map<String, LinkedHashMap<String, Double>> hmap_heuResult) {
		Map<String, LinkedHashMap<String, Double>> hmap_heuResultNormalized = new LinkedHashMap<>();
		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
			String str_entity = entry.getKey();
			LinkedHashMap<String, Double> lhmap_temp = new LinkedHashMap<>();
			HashSet<Double> hset_ValuesToNormalize = new HashSet<>();
			String str_entityNameAndDepth = entry.getKey();

			for (Integer i = 1; i <= int_depthOfTheTree; i++) {
				
				LinkedHashMap<String, Double> ll_result = hmap_heuResult.get(str_entity + str_depthSeparator + i.toString());

				for (Entry<String, Double> entry_CatAndValue : ll_result.entrySet()) {
					hset_ValuesToNormalize.add(entry_CatAndValue.getValue());
				}
				
				//System.out.println(str_entity + str_depthSeparator + i.toString()+" "+hset_ValuesToNormalize);
			}

			if (hset_ValuesToNormalize.size() > 0) {
				Map<Double, Double> hmap_NormalizationMap = NormalizeHashSet(hset_ValuesToNormalize);

				for (Integer i = int_depthOfTheTree; i > 0; i--) {
					LinkedHashMap<String, Double> ll_result = hmap_heuResult.get(str_entity + str_depthSeparator + i.toString());
					lhmap_temp = new LinkedHashMap<>();
					for (Entry<String, Double> entry_CatAndValue : ll_result.entrySet()) {

						
						str_entityNameAndDepth = entry_CatAndValue.getKey();
						lhmap_temp.put(entry_CatAndValue.getKey(),
								hmap_NormalizationMap.get(entry_CatAndValue.getValue()));
					}
					hmap_heuResultNormalized.put(str_entity + str_depthSeparator + i.toString(), lhmap_temp);

				}
			}
			else
			{
				for (Integer i = int_depthOfTheTree; i > 0; i--) {
					hmap_heuResultNormalized.put(str_entity + str_depthSeparator + i.toString(), lhmap_temp);
				}
				
			}

		}
		return hmap_heuResultNormalized;
	}
	
	public static LinkedHashMap<String, Double> calculatePrecisionRecall(String str_entity,String str_depth, HashSet<String> hset_retreived) 
	{
		
		LinkedHashMap<String, Double> hmap_preRcall = new LinkedHashMap<>();

		double db_relevantElements, db_retrievaledElements,
		int_truePositive = 0;
		
		if (hmap_groundTruthlist.get(str_entity) == null) {
			LOG.error("entity does not exist "+str_entity);
		}
		db_relevantElements = hmap_groundTruthlist.get(str_entity).size();

		double precision = 0.0, recall = 0.0;

		final LinkedList<String> llist_groundTruth = hmap_groundTruthlist.get(str_entity);

//		System.out.println(llist_groundTruth);
//		System.out.println(hset_retreived);
		
		db_retrievaledElements = hset_retreived.size();

		for (String str_cat: hset_retreived) 
		{
			if (llist_groundTruth.contains(str_cat)) 
			{
				int_truePositive += 1;
			}
			
		}
		if (int_truePositive != 0) 
		{
			precision = int_truePositive / db_retrievaledElements;
			recall = int_truePositive / db_relevantElements;
			
			hmap_preRcall.put("Precision", precision);
			hmap_preRcall.put("Recall", recall);
		} 
		else {
			hmap_preRcall.put("Precision", 0.);
			hmap_preRcall.put("Recall", 0.);
		}
		
		return hmap_preRcall;
	}

	public static void calculatePreRcallFscore_levelBased(Map<String, LinkedHashMap<String, Double>> hmap_filteredEntDepthBased) 
	{
		Map<String, LinkedHashMap<String, Double>> hmap_precisionRecall = new LinkedHashMap<>();
		int count=0;
		for (Entry<String, LinkedHashMap<String, Double>> entry : hmap_filteredEntDepthBased.entrySet()) 
		{
			String str_entityNameAndDepth = entry.getKey();
			String str_depth = str_entityNameAndDepth.substring(
					str_entityNameAndDepth.indexOf(str_depthSeparator) + str_depthSeparator.length(),
					str_entityNameAndDepth.length());
			String str_entityName = str_entityNameAndDepth.substring(0,
					str_entityNameAndDepth.indexOf(str_depthSeparator));
			
			HashSet<String> hset_temp = new HashSet<>();
			LinkedHashMap<String, Double> hmap_temp = entry.getValue();
			for (Entry<String,Double> entry_CatAndVal : hmap_temp.entrySet()) 
			{
				hset_temp.add(entry_CatAndVal.getKey());
			}
			hmap_precisionRecall.put(entry.getKey(),calculatePrecisionRecall(str_entityName,str_depth, hset_temp));
		}
	
			
		String str_Pre = "=SPLIT(\"";
		String str_Rec = "=SPLIT(\"";
		String str_Fsco = "=SPLIT(\"";

		for (Integer int_depth = int_depthOfTheTree; int_depth > 0; int_depth--)
		{
			int int_NumberOfEntities = 0;
			Double[] arr_Pre = new Double[int_depthOfTheTree];
			Arrays.fill(arr_Pre, 0.);
			Double[] arr_Rec = new Double[int_depthOfTheTree];
			Arrays.fill(arr_Rec, 0.);

			for (Entry<String, LinkedHashMap<String, Double>> entry : hmap_precisionRecall.entrySet()) {
				String str_entityNameAndDepth = entry.getKey();

				if (Integer.parseInt(str_entityNameAndDepth.substring(
						str_entityNameAndDepth.indexOf(str_depthSeparator) + str_depthSeparator.length(),
						str_entityNameAndDepth.length())) == int_depth) {
					int_NumberOfEntities++;
					LinkedHashMap<String, Double> hmap_preRcalFsco = entry.getValue();
					
					arr_Pre[int_depth - 1] += hmap_preRcalFsco.get("Precision");
					//System.out.println("precision:"+ (int_depth - 1) +" "+arr_Pre[int_depth - 1]);
					
					arr_Rec[int_depth - 1] += hmap_preRcalFsco.get("Recall");
					//System.out.println("recall:"+ (int_depth - 1)+" "+arr_Rec[int_depth - 1]);
					
				}
			}
			
			Locale.setDefault(Locale.US);
			DecimalFormat df = new DecimalFormat("0.00000");
			
			final String averagePrecision = df.format(arr_Pre[int_depth - 1] / int_NumberOfEntities);
			final String averageRecall = df.format(arr_Rec[int_depth - 1] / int_NumberOfEntities);
			
			double averageFScore=0;
			if (Double.parseDouble(averageRecall)+Double.parseDouble(averagePrecision)!=0) 
			{
				 averageFScore = 2 * (Double.parseDouble(averagePrecision)*Double.parseDouble(averageRecall)) / (Double.parseDouble(averagePrecision)+Double.parseDouble(averageRecall));
				 hset_fmeasure.add(averageFScore);
			}
			str_Pre = str_Pre + " ," + averagePrecision;
			
			str_Rec = str_Rec + " ," + averageRecall;
			str_Fsco = str_Fsco + " ," + df.format(averageFScore);
			// System.out.println("Depth "+ int_depth + " Precision " +
			// df.format(arr_Pre[int_depth-1]/int_NumberOfEntities));
			// System.out.println("Depth "+ int_depth + " Recall " +
			// df.format(arr_Rec[int_depth-1]/int_NumberOfEntities));
			// System.out.println("Depth "+ int_depth + " Fscore " +
			// df.format(arr_Fsco[int_depth-1]/int_NumberOfEntities));

		}
		str_Pre += "\",\",\")";
		str_Rec += "\",\",\")";
		str_Fsco += "\",\",\")";

		System.out.println(str_Pre.replace("=SPLIT(\" ,", "=SPLIT(\""));
		System.out.println(str_Rec.replace("=SPLIT(\" ,", "=SPLIT(\""));
		System.out.println(str_Fsco.replace("=SPLIT(\" ,", "=SPLIT(\""));
	}
		public static LinkedHashMap<String, Double> calculatePrecisionRecall(String str_entity, String str_depth,Map<String, LinkedHashMap<String, Double>> hmap_filteredEntDepthBased ) {
		
		LinkedHashMap<String, Double> hmap_preRcall = new LinkedHashMap<>();

		double db_relevantElements, db_retrievaledElements,
		int_truePositive = 0;
		
		if (hmap_groundTruthlist.get(str_entity) == null) {
			LOG.error("entity does not exist "+str_entity);
		}
		db_relevantElements = hmap_groundTruthlist.get(str_entity).size();

		double precision = 0.0, recall = 0.0;

		final LinkedHashMap<String, Double> lhmap_depthElements = hmap_filteredEntDepthBased
				.get(str_entity + str_depthSeparator + str_depth);
		final LinkedList<String> llist_groundTruth = hmap_groundTruthlist.get(str_entity);

		db_retrievaledElements = lhmap_depthElements.size();

		for (Entry<String, Double> entry : lhmap_depthElements.entrySet()) {
			String str_Cat = entry.getKey();

			if (llist_groundTruth.contains(str_Cat)) {
				int_truePositive += 1;
			}
			// int_truePositive=0;
		}

		if (int_truePositive != 0) 
		{
			precision = int_truePositive / db_retrievaledElements;
			recall = int_truePositive / db_relevantElements;

			hmap_preRcall.put("Precision", precision);
			hmap_preRcall.put("Recall", recall);
		} 
		else {
			hmap_preRcall.put("Precision", 0.);
			hmap_preRcall.put("Recall", 0.);
		}
		return hmap_preRcall;
	}

	public static double GetAverageArray(Double[] arr) {
		double sum = 0;
		double size = arr.length;

		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}

		return sum / size;
	}

	public static void InitializeGroundTruthAndList(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(str_path + fileName));) {

			String str_entity = null, str_mainCat = null;

			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase();
				if (line == null) {
					// System.out.println("--------------------------------------");
				}
				LinkedList<String> ll_goalSet = new LinkedList<>();
				String[] str_split = line.split("\t");
				for (int i = 0; i < str_split.length; i++) {
					str_entity = str_split[0];
					str_mainCat = str_split[1];

					if (i != 0) {
						ll_goalSet.add(str_split[i]);
					}
				}

				hmap_groundTruth.put(str_entity, str_mainCat);
				hmap_groundTruthlist.put(str_entity, ll_goalSet);
				if (hmap_groundTruth.size() > 100) {
					// System.out.println("--------------------------------------");
				}
			}

			for (Entry<String, LinkedList<String>> entry : hmap_groundTruthlist.entrySet()) {
				str_entity = entry.getKey();
				LinkedList<String> str_categories = entry.getValue();

				// System.out.println(str_entity+ " "+ str_categories);
			}
		} catch (IOException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();

		}
	}

	public static LinkedHashMap<String, HashMap<String, Double>> InitializeTestSet(String fileName) {
		
		LinkedHashMap<String, HashMap<String, Double>> hmap_testSet = new LinkedHashMap<>();
		String str_entityName = null;
		String str_catName = null;
		Integer int_count_= 0;
		try (BufferedReader br = new BufferedReader(new FileReader(str_path + fileName));) {
			String line = null;
			int depth = int_depthOfTheTree;

			ArrayList<String> arrList_paths = new ArrayList<>();

			ArrayList<Integer> numberOfPaths = new ArrayList<>();
			LinkedHashMap<String, Double> hmap_catAndValue = new LinkedHashMap<>();
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase();
				if (line.contains(",") && !line.contains("\",\"")) {

					str_entityName = line.split(",")[0].toLowerCase();
					str_catName = line.split(",")[1].toLowerCase();
					// hmap_groundTruth.put(str_entityName, str_catName);

				} else if (line.length() < 1) {
					hmap_testSet.put(str_entityName + "__" + depth, hmap_catAndValue);
					// System.out.println("WWW "+str_entityName + "__" + depth +
					// " " +hmap_catAndValue);
					hmap_catAndValue = new LinkedHashMap<>();
					depth--;
					numberOfPaths.clear();
					arrList_paths.clear();
				} 
				else {
					if (line.contains(":")) {

						hmap_catAndValue.put(line.substring(0, line.indexOf(":")),
								Double.parseDouble(line.substring(line.indexOf(":") + 1, line.length())));
					} else if (line.contains("-"))
					{
						int_count_++;
					}
				}
				if (depth == 0) {
					depth = int_depthOfTheTree;
					//hmap_entityStartingCat.put(str_entityName, int_count_);
					hmap_entityStartingCat.put(str_entityName, ++int_count_);
					int_count_=0;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();

		}

		for (Integer i = 1; i <= 7; i++) {
			for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
				if (!hmap_testSet.containsKey(entry.getKey() + str_depthSeparator + i.toString())) {
					System.out.println(entry);
				}
			}
		}
		
		return hmap_testSet;

	}
	public static void printHashMapFormated(LinkedHashMap<String, HashMap<String, Double>> lhmap_print)
	{
		String str_format = "=SPLIT(\"";
		for (Entry<String, HashMap<String, Double>> entry : lhmap_print.entrySet()) 
		{
			String str_entityName = entry.getKey().substring(0, entry.getKey().indexOf(str_depthSeparator));
			HashMap<String, Double> hmap_catAndVal = entry.getValue();
			Integer int_depth = Integer.parseInt(entry.getKey().substring(
					entry.getKey().indexOf(str_depthSeparator) + str_depthSeparator.length(), entry.getKey().length())); 
			
			for (int i = int_depth; i < int_depthOfTheTree; i++) 
			{
				str_format+=""+",";
				
			}
			for (Entry<String, Double> entry_CatAndVal : hmap_catAndVal.entrySet()) 
			{
				str_format+=entry_CatAndVal.getKey()+":"+ entry_CatAndVal.getValue()+" ," ;
			}
			str_format += "\",\",\")";
			System.out.println(str_format);
			str_format = "=SPLIT(\"";
		}
	}
	
	public static LinkedHashMap<String, HashMap<String, Double>> testSetAddResourcePaths(LinkedHashMap<String, HashMap<String, Double>> hmap_testSet,String str_fileName)
	{
		int int_entityCountAddPath = 0;
		HashMap<String, HashMap<String, Double>> hmap_pathsFromOthers = ReadResults.ReadResultFromDifferentFileEntAndCat("EntityAndCatFromOtherFiles"+File.separator+
			str_fileName);
		//printMap(hmap_pathsFromOthers);
		LinkedHashMap<String, HashMap<String, Double>> lhmap_testSetAdded= new LinkedHashMap<>();
		
		for (Entry<String, HashMap<String, Double>> entry : hmap_testSet.entrySet()) 
		{
			String str_entityName = entry.getKey().substring(0, entry.getKey().indexOf(str_depthSeparator));
			if (entry.getKey().contains("1")&& hmap_pathsFromOthers.containsKey(str_entityName)) 
			{
					
					HashMap<String, Double> map_catAndVal =  hmap_pathsFromOthers.get(str_entityName);
					HashMap<String, Double> map_catAndVal_original=  hmap_testSet.get(entry.getKey());
					for(Entry<String, Double> entry_CatAndVal: map_catAndVal_original.entrySet())
					{
						if (map_catAndVal.containsKey(entry_CatAndVal.getKey())) 
						{
							map_catAndVal.put(entry_CatAndVal.getKey(), map_catAndVal.get(entry_CatAndVal.getKey())+map_catAndVal_original.get(entry_CatAndVal.getKey()));
						}
						else
						{
							map_catAndVal.put(entry_CatAndVal.getKey(), map_catAndVal_original.get(entry_CatAndVal.getKey()));
						}
						
					}
					lhmap_testSetAdded.put(entry.getKey(), map_catAndVal);
					int_entityCountAddPath++;
			}
			else
			{
				lhmap_testSetAdded.put(entry.getKey(), entry.getValue());
			}
		}
		for (Entry<String, HashMap<String, Double>> entry : lhmap_testSetAdded.entrySet()) {
			
			//System.out.println(entry.getKey()+" "+entry.getValue());
		}
//		System.out.println();
//		System.out.println(int_entityCountAddPath);
		return lhmap_testSetAdded;
	}

	
	private static Map<String, Integer> initializeMapForTFIDF(LinkedHashMap<String, HashMap<String, Double>> hmap_testSet)
	{
		Map<String ,Integer> hmap_resultCatDepVal = new LinkedHashMap<>();
		
		for (Entry<String, HashMap<String, Double>> entry_EntDeptCatVal : hmap_testSet.entrySet()) 
		{
			String str_entityNameAndDepth = entry_EntDeptCatVal.getKey();
			String str_entityName = str_entityNameAndDepth.substring(0, str_entityNameAndDepth.indexOf(str_depthSeparator));
			String str_depth = str_entityNameAndDepth.substring(
					str_entityNameAndDepth.indexOf(str_depthSeparator) + str_depthSeparator.length(), str_entityNameAndDepth.length());
			
			Integer int_dept = Integer.parseInt(str_depth);
			HashMap<String, Double> hmap_CatVal = entry_EntDeptCatVal.getValue();
			
		//	LinkedHashMap<String, Double> lhmap_Results = (LinkedHashMap<String, Double>) entry_EntDeptCatVal.getValue();
			
			for (Entry<String, Double> entry_hmapValues : hmap_CatVal.entrySet()) 
			{
				String str_catName = entry_hmapValues.getKey();
				
				String str_catDepth = entry_hmapValues.getKey()+str_depthSeparator+str_depth;
				
				if (hmap_resultCatDepVal.containsKey(str_catDepth)) 
				{
					Integer intVal = hmap_resultCatDepVal.get(str_catDepth)+1;
					hmap_resultCatDepVal.put(str_catDepth, intVal);
				} 
				else 
				{
					hmap_resultCatDepVal.put(str_catDepth, 1);
				}
			}
		}
		
		//printMap(hmap_resultCatDepVal);
		return hmap_resultCatDepVal;
		
	}
	public static Map<String, LinkedHashMap<String, Double>> callHeuristic(LinkedHashMap<String, HashMap<String, Double>> hmap_testSet) {
		
		Map<String, LinkedHashMap<String, Double>> hmap_heuResult = new LinkedHashMap<>();
		
		
//		Map<String, Integer> hmap_tfidfCatDeptVal =initializeMapForTFIDF(hmap_testSet);
		Map<String, Integer> hmap_tfidfCatDeptVal =initializeMapForTFIDF(hmap_testSet);
		for (Entry<String, HashMap<String, Double>> entry : hmap_testSet.entrySet()) {
			String str_entityNameAndDepth = entry.getKey();
			String str_entityName = str_entityNameAndDepth.substring(0, str_entityNameAndDepth.indexOf(str_depthSeparator));
			String str_depth = str_entityNameAndDepth.substring(
					str_entityNameAndDepth.indexOf(str_depthSeparator) + str_depthSeparator.length(), str_entityNameAndDepth.length());
			HashMap<String, Double> hmap_Values =  entry.getValue();
//				LinkedHashMap<String, Double> lhmap_Results = (LinkedHashMap<String, Double>) entry.getValue();
			LinkedHashMap<String, Double> lhmap_Results = new LinkedHashMap<>();
			
			for (Entry<String, Double> entry_hmapValues : hmap_Values.entrySet()) {
				String str_catName = entry_hmapValues.getKey();
				Double db_pathCount = entry_hmapValues.getValue();
				Double db_heuValue = 0.0;
//				 if (heuristic.equals(GlobalVariables.HeuristicType.HEURISTIC_NO))
//				 {
//					 db_heuValue = Heuristic_NanHeuristic(db_pathCount);
//				 }else 
					 if (heuristic.equals(GlobalVariables.HeuristicType.HEURISTIC_NUMBEROFPATHS)) {
					db_heuValue = Heuristic_NumberOfPaths(db_pathCount);
				} else 
				if (heuristic.equals(GlobalVariables.HeuristicType.HEURISTIC_NUMBEROFPATHSANDDEPTH)) {
					db_heuValue = Heuristic_NumberOfPathsAndDepth(db_pathCount, Integer.parseInt(str_depth));
				}
				else if(heuristic.equals(GlobalVariables.HeuristicType.HEURISTIC_FIRSTFINDFIRSTDEPTH))
				{
					db_heuValue = Heuristic_FirstPathsAndDepth(str_entityName,db_pathCount, Integer.parseInt(str_depth)) ;
				}
				else 
				if(heuristic.equals(GlobalVariables.HeuristicType.HEURISTIC_FIRSTFINDFIRSTDEPTHEXPONENTIAL))
				{
					db_heuValue = Heuristic_FirstPathsAndDepthExp(str_entityName,db_pathCount, Integer.parseInt(str_depth)) ;
				} else
				if(heuristic.equals(GlobalVariables.HeuristicType.HEURISTIC_TFIDF))
				{
					db_heuValue = Heuristic_tfidf(str_catName,db_pathCount,str_depth,hmap_tfidfCatDeptVal) ;
					//System.out.println(db_heuValue);
				}
				else
					if(heuristic.equals(GlobalVariables.HeuristicType.HEURISTIC_COMBINATION4TH5TH))
					{
						db_heuValue = Heuristic_combination4th5th(str_entityName,str_catName,db_pathCount,str_depth,hmap_tfidfCatDeptVal) ;
						//System.out.println(db_heuValue);
					}
					 
					 
				lhmap_Results.put(str_catName, db_heuValue);
			}
			hmap_heuResult.put(str_entityNameAndDepth, lhmap_Results);
		}
		
		return hmap_heuResult;
	}

	public static Map<String, LinkedHashMap<String, Double>> filterHeuResults(Map<String, LinkedHashMap<String, Double>> hmap_normlizesResults) {
		
		Map<String, LinkedHashMap<String, Double>> hmap_resultNormalizedFiltered = new LinkedHashMap<>();
		int int_catNumberBeforeFilter = 0;
		int int_catNumberFiltered = 0;

		for (Entry<String, LinkedHashMap<String, Double>> entry : hmap_normlizesResults.entrySet()) {
//			for (Entry<String, LinkedHashMap<String, Double>> entry : hmap_heuResultNormalizedSorted.entrySet()) {
			String str_entityName = entry.getKey();

			LinkedHashMap<String, Double> hmap_Values = (LinkedHashMap<String, Double>) entry.getValue();
			LinkedHashMap<String, Double> hmap_catAndValFiletered = new LinkedHashMap<>();

			for (Entry<String, Double> entry_hmapValues : hmap_Values.entrySet()) {
				String str_catName = entry_hmapValues.getKey();
				Double db_value = entry_hmapValues.getValue();
				int_catNumberBeforeFilter++;
				if (db_value >= threshold) {
					hmap_catAndValFiletered.put(str_catName, db_value);
				} else {
					int_catNumberFiltered++;
				}
			}
			hmap_resultNormalizedFiltered.put(str_entityName, hmap_catAndValFiletered);
		}
//			 System.out.println();
//			 System.out.println("Thershold"+ threshold );
//			 System.out.println("Total Category Number Before Filtering:"+
//			 int_catNumberBeforeFilter );
//			 System.out.println("Total Category Number After Filtering:"+
//			 (int_catNumberBeforeFilter-int_catNumberFiltered) );
//			 System.out.println();
		return hmap_resultNormalizedFiltered;
	}

	private static double Heuristic_NanHeuristic(double db_Value) {
		return 1.0;
	}

	private static double Heuristic_NumberOfPaths(double db_Value) {
		return db_Value;
	}

	private static double Heuristic_NumberOfPathsAndDepth(double db_Value, int int_depth) {
		return (double) (db_Value / (double) int_depth);
	}

	private static double Heuristic_FirstPathsAndDepth(String str_entity,double db_Value, int int_depth) 
	{
		int int_entitystartingDepth= hmap_entityStartingCat.get(str_entity);
		if (!(int_entitystartingDepth>=0)) 
		{
			LOG.error("Entity not found hmap_entityStartingCat"+ str_entity);
		}
		if (int_depth<=int_entitystartingDepth) 
		{
			return 0;
		}
		return (double) (db_Value / (double) (int_depth-int_entitystartingDepth));
	}
	private static double Heuristic_tfidf(String str_catName,double db_Value, String str_depth,Map<String, Integer> hmap_tfidfCatDeptVal) 
	{
		if (hmap_tfidfCatDeptVal.containsKey(str_catName+str_depthSeparator+str_depth))
		{
			return (double) (db_Value * (double) Math.log10(hmap_groundTruth.size() / hmap_tfidfCatDeptVal.get(str_catName+str_depthSeparator+str_depth)));
		}
		else
		{
			System.out.println(str_catName+str_depthSeparator+str_depth);
			System.err.println("ERROR");
			return 0.;
		}
	}
	private static double Heuristic_combination4th5th(String str_entity,String str_catName,double db_Value, String str_depth,Map<String, Integer> hmap_tfidfCatDeptVal) 
	{
		int int_depth= Integer.parseInt(str_depth);
		int int_entitystartingDepth= hmap_entityStartingCat.get(str_entity);
		if (hmap_tfidfCatDeptVal.containsKey(str_catName+str_depthSeparator+str_depth))
		{
			return (double) ((db_Value / (double) (Math.pow(2,int_depth-(int_entitystartingDepth+1))))* (double) Math.log10(hmap_groundTruth.size() / hmap_tfidfCatDeptVal.get(str_catName+str_depthSeparator+str_depth)));
		}
		else
		{
			System.out.println(str_catName+str_depthSeparator+str_depth);
			System.err.println("ERROR");
			return 0.;
		}
	}
	private static double Heuristic_FirstPathsAndDepthExp(String str_entity,double db_Value, int int_depth) 
	{
		int int_entitystartingDepth= hmap_entityStartingCat.get(str_entity);
		if (!(int_entitystartingDepth>=0)) 
		{
			LOG.error("Entity not found hmap_entityStartingCat"+ str_entity);
		}
		if (int_depth<=int_entitystartingDepth) 
		{
			return 0;
		}
		return (double) (db_Value / (double) (Math.pow(2,int_depth-(int_entitystartingDepth+1))));
	}
	private static double Heuristic_NumberOfPathsDepthSubCat(double db_Value, int int_depth, String str_cat) {
		return (double) db_Value / (double) ((hmap_subCategoryCount.get(str_cat).get(int_depth - 1) * int_depth));
	}

	private static void compareResultsWithGroundTruth(Map<String, LinkedHashMap<String, Double>> hmap_heuResult) {
		int[] arr_FoundDepth = new int[int_depthOfTheTree];
		int count_Cat = 0;
		int count_NotFoundCat = 0;

		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
			String str_entity = entry.getKey();
			String str_category = entry.getValue();

			boolean changed = false;
			for (int i = 0; i < 7; i++) {
				Integer int_index = i + 1;
				LinkedHashMap<String, Double> ll_result = hmap_heuResult
						.get(str_entity + str_depthSeparator + int_index.toString());
				

				if (ll_result == null) {
					continue;
				}
				if (ll_result.size() > 0) {
					// String str_resCat= ll_result.keySet().iterator().next();
					///////////////////////////////////////////////////////
					final Double maxNumber = ll_result.values().iterator().next();
					final List<String> firstElements = ll_result.entrySet().stream()
							.filter(p -> p.getValue() >= maxNumber).map(p -> p.getKey()).collect(Collectors.toList());
					// System.err.println(str_entity+"\t"+int_index+"\t"+str_category
					// +"\t"+firstElements+"\t"+firstElements.contains(str_category));
					// System.out.println(str_entity+" "+firstElements);
					if (firstElements.contains(str_category))
					///////////////////////////////////////////////////////
					// if (str_resCat.equals(str_category))
					{
						if (int_index == 7) {
							// System.out.println(entry.getValue()+" "+
							// entry.getKey());
						}
						// System.out.println(entry.getValue()+" "+
						// entry.getKey());
						changed = true;
						// System.out.println(int_index+" "+str_entity+"
						// "+str_category);
						arr_FoundDepth[i] += 1;
						// System.out.println(str_entity+" "+firstElements+" "+
						// firstElements+" "+arr_FoundDepth[i]);
						count_Cat++;
						break;
					}
				}

			}
			if (!changed) {
				// System.out.println("XXXXXXXXXXX "+str_entity);
				count_NotFoundCat++;
			}

		}
		// System.out.println("=SPLIT(\"" + formatResult[i] + "\",\",\")");
		String str_formated = "=SPLIT(\"";
		for (int i = arr_FoundDepth.length - 1; i >= 0; i--) {

			str_formated = str_formated + " ," + arr_FoundDepth[i];
			// System.out.println(str_formated);
		}

		str_formated += "\",\",\")";
		System.out.println(str_formated.replace("SPLIT(\" ,", "str_formated=SPLIT(\""));
		// System.out.println(str_formated.replace("=SPLIT(\" ,", "=SPLIT(\""));

		// System.out.println("Entity Count: "+ hmap_groundTruth.size());
		// System.out.println("Total Found Category Number: "+ count_Cat );
		// System.out.println("Total NOT Found Category Number: "+
		// count_NotFoundCat );
	}

	// public static void NormalizeHashSet()
	// {
	//
	// double min = Collections.min(hset_ValuesToNormalize);
	// double max =Collections.max(hset_ValuesToNormalize);
	//
	// if (min==max)
	// {
	// hmap_NormalizationMap.put(min, 1.);
	// return;
	// }
	//
	// for (Double db_val: hset_ValuesToNormalize)
	// {
	// hmap_NormalizationMap.put(db_val, ((double) ((double) (db_val - min) /
	// (double) (max - min))));
	// }
	// //Double[] array = hmap_NormalizationMap.values().stream().toArray(x ->
	// new Double[x]);
	// //System.err.println("Median :"+median(array));
	// }


	public static LinkedHashMap<String, Double> NormalizeMap(Map<String, Double> hmap_ValuesToNormalize) {
		
		Map<String, Double> hmap_NormalizationMap = new LinkedHashMap<>();
		
		double max=1;
		try 
		{
			 max = Collections.max(hmap_ValuesToNormalize.values());
		} 
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}

		for (Entry<String, Double> entry_CatAndVal : hmap_ValuesToNormalize.entrySet()) 
		{
			hmap_NormalizationMap.put(entry_CatAndVal.getKey(), ((double) ((double) entry_CatAndVal.getValue() / (double) max)));
		}
		return (LinkedHashMap<String, Double>) hmap_NormalizationMap;
	}
	
	public static Map<Double, Double> NormalizeHashSet(HashSet<Double> hset_ValuesToNormalize) {
		Map<Double, Double> hmap_NormalizationMap = new HashMap<>();
		double min = Collections.min(hset_ValuesToNormalize);
		double max = Collections.max(hset_ValuesToNormalize);

		if (min == max) {
			hmap_NormalizationMap.put(min, 1.);
			return hmap_NormalizationMap;
		}

		for (Double db_val : hset_ValuesToNormalize) {
			hmap_NormalizationMap.put(db_val, ((double) ((double) db_val / (double) max)));
		}
		return hmap_NormalizationMap;
	}

	public static double median(Double[] m) {
		int middle = m.length / 2;
		if (m.length % 2 == 1) {
			return m[middle];
		} else {
			return (m[middle - 1] + m[middle]) / 2.0;
		}
	}

	public static void printMapOrdered(Map mp,Map<String, HashMap<String, Double>> hmap_testSet) 
	{
		for (Entry<String, HashMap<String, Double>> entry : hmap_testSet.entrySet()) 
		{
			if (mp.containsKey(entry.getKey())) 
			{
				//System.out.println(entry.getKey()+"="+mp.get(entry.getKey()));
			}
			else
			{
				System.err.println("-------------"+entry.toString());
			}
		}
	}
	public static void printMap(Map mp) {
		System.out.println("----------------------------");
		// log.info(heu);
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			// //System.out.println(pair.getKey() + " = " + pair.getValue());
			 //log.info(pair.getKey() + " = " + pair.getValue());
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
		System.out.println("----------------------------");
		// log.info("----------------------------");
		// log.info("");
	}

	private static LinkedHashMap<String, Double> sortByValue(Map<String, Double> unsortMap) {
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	public static void ReadSubCategoryNumber() {
		String[] subCount = null;

		int[] int_subCount;
		double[] double_subCount;
		BufferedReader brC;
		ArrayList<Double> arrListTemp;
		try {
			brC = new BufferedReader(new FileReader(str_path + "SubCategory_Count.csv"));
			String lineCategory = null;

			while ((lineCategory = brC.readLine()) != null) {

				lineCategory = lineCategory.toLowerCase();
				arrListTemp = new ArrayList<>();
				// System.out.println(lineCategory);
				subCount = (lineCategory.substring(lineCategory.indexOf(":,") + 2, lineCategory.length()).split(","));
				int_subCount = Arrays.stream(subCount).mapToInt(Integer::parseInt).toArray();

				for (int i = 0; i < int_subCount.length; i++) {
					arrListTemp.add((double) int_subCount[i]);
				}
				hmap_subCategoryCount.put(lineCategory.substring(0, lineCategory.indexOf(":")), arrListTemp);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void test_CompareTwoLists(Map<String, LinkedHashMap<String, Double>> hmap_1,
											Map<String, LinkedHashMap<String, Double>> hmap_2)
	{
		int int_count = 0;	
		for (Entry<String, LinkedHashMap<String, Double>> entry_1 : hmap_1.entrySet()) 
		{
			String str_entityName= entry_1.getKey();
			LinkedHashMap<String, Double> lhmap_CatAndVal= entry_1.getValue();
			
			if (!lhmap_CatAndVal.equals(hmap_2.get(str_entityName))) 
			{
				LOG.error("Two maps are not identical");
				System.out.println("ERROR");
			}
			int_count++;
		}
		System.out.println("Entities are tested:"+int_count);
	}
	public static void testNormalization(Map<String, LinkedHashMap<String, Double>> hmap_resultNormalizedFiltered )
	{
		for (Entry<String, String> entry : hmap_groundTruth.entrySet()) {
			String str_entity = entry.getKey();
			LinkedHashMap<String, Double> lhmap_temp = new LinkedHashMap<>();
			HashSet<Double> hset_ValuesToNormalize = new HashSet<>();
			String str_entityNameAndDepth = entry.getKey();

			LinkedList<Double> llist_test = new LinkedList<>(); 
			for (Integer i = 1; i <= int_depthOfTheTree; i++) {
				
				LinkedHashMap<String, Double> ll_result = hmap_resultNormalizedFiltered.get(str_entity + str_depthSeparator + i.toString());
				
				if ( hmap_resultNormalizedFiltered.get(str_entity + str_depthSeparator + i.toString())!=null)
				{
					for (Entry<String, Double> entry_CatAndValue : ll_result.entrySet()) 
					{
						llist_test.add(entry_CatAndValue.getValue());
					}
				}
			}
//				LinkedList<Double> ll_de = new LinkedList<>();
			if (llist_test.size()>0&&!Collections.max(llist_test).equals(1.)) 
			{
				System.out.println(str_entity+ "HATA");
			}
//				if (llist_test.size()==0) {
//					
//					System.out.println(str_entity);
//					
//				}
				
			}
		}
}