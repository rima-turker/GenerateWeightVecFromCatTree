package TreeGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class CalculatePrecisionAndRecall {
	
	private static final Logger LOG = Logger.getLogger(CalculatePrecisionAndRecall.class.getCanonicalName());
	private static final Logger log_testSet = Logger.getLogger("debugLogger");
	private static final Logger log_heuResult = Logger.getLogger("reportsLogger");

	static String str_path = System.getProperty("user.dir") + File.separator;
	static int int_depthOfTheTree = 7;
	static String str_depthSeparator = "__";
	private static final HashMap<String, String> hmap_groundTruth = new LinkedHashMap<>();
	private static final LinkedHashMap<String, LinkedHashMap<String, Double>> hmap_testSet = new LinkedHashMap<>();
	private static final Map<String, LinkedList<String>> hmap_groundTruthlist = new LinkedHashMap<>();
	private static final Map<String, LinkedHashMap<String, Double>> hmap_heuResult = new LinkedHashMap<>();
	private static double threshold= 0;
	
	static Map<String, LinkedHashMap<String, Double>> hmap_heuResultThresholdFiltered = new LinkedHashMap<>();
	private static final Map<String, LinkedHashMap<String, Double>> hmap_precisionRecallFmeasure = new LinkedHashMap<>();

	enum HuristicType {		
		NO_HURISTIC,
		NUMBEROFPATHS_HURISTIC,
		NUMBEROFPATHSANDSUBCAT__HURISTIC;
		
	}
	public static void main(String str_fileNameGroundTruthList,String str_fileNameTestSet,Double db_threshold) 
	{
		InitializeGroundTruthAndList(str_fileNameGroundTruthList);
		InitializeTestSet(str_fileNameTestSet);
		
		printMap(log_heuResult,hmap_groundTruth);
		printMap(log_testSet,hmap_testSet);
		
		threshold = db_threshold;
		
		CallHeuristic(hmap_testSet,HuristicType.NO_HURISTIC);
		CompareResultsWithGroundTruth(hmap_groundTruth,hmap_heuResult);
		CallCalculatePrecisionAndRecall();
		//printMap(log_testSet,hmap_testSet);

	}

	public static void CallCalculatePrecisionAndRecall()
	{

		for(Entry<String, LinkedHashMap<String, Double>> entry: hmap_heuResultThresholdFiltered.entrySet()) 
		{
			String str_entityNameAndDepth= entry.getKey();
			String str_depth= str_entityNameAndDepth.substring(str_entityNameAndDepth.indexOf(str_depthSeparator)+str_depthSeparator.length(),
					str_entityNameAndDepth.length());
			String str_entityName = str_entityNameAndDepth.substring(0,str_entityNameAndDepth.indexOf(str_depthSeparator));
			hmap_precisionRecallFmeasure.put(str_entityNameAndDepth, CalculatePrecisionRecallFmeasure(str_entityName,str_depth));
		}

		for (Integer int_depth = 1; int_depth <= int_depthOfTheTree ; int_depth++) 
		{
			int int_NumberOfEntities =0;
			Double[] arr_Pre = new Double[int_depthOfTheTree];
			Arrays.fill(arr_Pre, 0.);
			Double[] arr_Rec = new Double[int_depthOfTheTree];
			Arrays.fill(arr_Rec, 0.);
			Double[] arr_Fsco = new Double[int_depthOfTheTree];
			Arrays.fill(arr_Fsco, 0.);
			
			for(Entry<String, LinkedHashMap<String, Double>> entry: hmap_precisionRecallFmeasure.entrySet()) 
			{
				String str_entityNameAndDepth = entry.getKey();

				if (Integer.parseInt(str_entityNameAndDepth.substring(str_entityNameAndDepth.indexOf(str_depthSeparator)+str_depthSeparator.length(),
						str_entityNameAndDepth.length()))==int_depth) 
				{
					
					int_NumberOfEntities++;
					LinkedHashMap<String, Double> hmap_preRcalFsco = entry.getValue();

					

					for(Entry<String, Double> entry_preRcallFsco: hmap_preRcalFsco.entrySet()) 
					{
						if (entry_preRcallFsco.getKey().equals("Precision")) 
						{
							arr_Pre[int_depth-1]+=entry_preRcallFsco.getValue();
						}
						else if (entry_preRcallFsco.getKey().equals("Recall"))
						{
							arr_Rec[int_depth-1]+=entry_preRcallFsco.getValue();
						}
						else if (entry_preRcallFsco.getKey().equals("Fscore")) 
						{
							arr_Fsco[int_depth-1]+=entry_preRcallFsco.getValue();
						}
					}
				}
			}
			System.out.println("Depth "+ int_depth + " Precision " + arr_Pre[int_depth-1]/int_NumberOfEntities);
			System.out.println("Depth "+ int_depth + " Recall " + arr_Rec[int_depth-1]/int_NumberOfEntities);
			System.out.println("Depth "+ int_depth + " Fscore " + arr_Fsco[int_depth-1]/int_NumberOfEntities);
		}
	}
	public static double GetAverageArray(Double[] arr)
	{
		double sum =0;
		double size= arr.length;

		for (int i = 0; i < arr.length; i++) 
		{
			sum+= arr[i];
		}

		return sum/size;
	}
	public static  LinkedHashMap<String, Double> CalculatePrecisionRecallFmeasure(String str_entity,String str_depth)
	{
		LinkedHashMap<String, Double> hmap_preRCallFmea = new LinkedHashMap<>();

		double db_relevantElements,db_selectedElements,

		int_truePositive = 0;
		if(hmap_groundTruthlist.get(str_entity)==null) {
			System.out.println(str_entity);
		}
		db_relevantElements=hmap_groundTruthlist.get(str_entity).size();


		double precision=0.0, recall=0.0, Fscore=0.0;

		LinkedHashMap<String, Double> lhmap_depthElements = hmap_heuResultThresholdFiltered.get(str_entity+str_depthSeparator+str_depth);
		LinkedList<String> llist_groundTruth = hmap_groundTruthlist.get(str_entity);

		
		db_selectedElements=lhmap_depthElements.size();

		for(Entry<String, Double> entry:lhmap_depthElements.entrySet()) 
		{
			String str_Cat = entry.getKey();

			if (llist_groundTruth.contains(str_Cat)) 
			{
				int_truePositive+=1;
			}
//						int_truePositive=0;
		}
		
		if (int_truePositive!=0)
		{
			precision = int_truePositive/db_selectedElements;
			recall= int_truePositive/db_relevantElements;
			Fscore=2*((precision*recall)/ (precision+recall));
			
			hmap_preRCallFmea.put("Precision", precision);
			hmap_preRCallFmea.put("Recall", recall);
			hmap_preRCallFmea.put("Fscore", Fscore);
		}
		else
		{
			hmap_preRCallFmea.put("Precision", 0.);
			hmap_preRCallFmea.put("Recall", 0.);
			hmap_preRCallFmea.put("Fscore", 0.);
		}
		
		return hmap_preRCallFmea;
	}

	public static void InitializeGroundTruthAndList(String fileName) 
	{
		try (BufferedReader br = new BufferedReader(new FileReader(str_path + fileName));) 
		{
			
			String str_entity=null,str_mainCat=null;
			
			String line= br.readLine();
			while ((line = br.readLine()) != null) 
			{
				line =line.toLowerCase();
				if (line==null)
				{
					System.out.println("--------------------------------------");
				}
				LinkedList<String> ll_goalSet = new LinkedList<>();
				String[] str_split = line.split("\t");
				for (int i = 0; i < str_split.length; i++) 
				{
					str_entity=str_split[0];
					str_mainCat=str_split[1];
					
					if (i!=0) 
					{
						ll_goalSet.add(str_split[i]);
					}
				}
				
				hmap_groundTruth.put(str_entity, str_mainCat);
				hmap_groundTruthlist.put(str_entity, ll_goalSet);
				if (hmap_groundTruth.size()>100) {
					System.out.println("--------------------------------------");
				}
			}

			for(Entry<String, LinkedList<String>> entry: hmap_groundTruthlist.entrySet()) 
			{
				str_entity = entry.getKey();
				LinkedList<String> str_categories = entry.getValue();

				System.out.println(str_entity+ " "+ str_categories);
			}
		}
		catch (IOException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();

		}
	}

	public static void InitializeTestSet(String fileName) {
		String str_entityName = null;
		String str_catName = null;

		try (BufferedReader br = new BufferedReader(new FileReader(str_path + fileName));) {
			String line = null;
			int depth = int_depthOfTheTree;

			ArrayList<String> arrList_paths = new ArrayList<>();

			ArrayList<Integer> numberOfPaths = new ArrayList<>();
			LinkedHashMap<String, Double> hmap_catAndValue = new LinkedHashMap<>();
			while ((line = br.readLine()) != null) 
			{
				line = line.toLowerCase();
				if (line.contains(",") && !line.contains("\",\"")) {

					str_entityName = line.split(",")[0].toLowerCase();
					str_catName = line.split(",")[1].toLowerCase();
					//hmap_groundTruth.put(str_entityName, str_catName);

				} 
				else if (line.length() < 1 ) {
					hmap_testSet.put(str_entityName + "__" + depth, hmap_catAndValue);
					//System.out.println("WWW "+str_entityName + "__" + depth + " " +hmap_catAndValue);
					hmap_catAndValue = new LinkedHashMap<>();
					depth--;
					numberOfPaths.clear();
					arrList_paths.clear();
				} 
				else 
				{
					if (line.contains(":")) {

						hmap_catAndValue.put(line.substring(0, line.indexOf(":")),
								Double.parseDouble(line.substring(line.indexOf(":") + 1, line.length())));
					}
					else if (line.contains("-")) 
					{
						//depth--;
					}
				}
				if (depth == 0) 
				{
					depth = int_depthOfTheTree;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();

		}
		
		for (Integer i = 1; i <=7; i++) 
		{
			for(Entry<String, String> entry: hmap_groundTruth.entrySet()) 
			{
				if (!hmap_testSet.containsKey(entry.getKey()+str_depthSeparator+i.toString())) 
				{
					System.out.println(entry);
				}
			}
		}
		
	}

	public static void SortHeuristicResults(LinkedHashMap<String, LinkedHashMap<String, Double>> hmap)
	{
		for(Entry<String, LinkedHashMap<String, Double>> entry: hmap.entrySet()) 
		{
			final LinkedHashMap<String, Double> temp = sortByValue(entry.getValue());
			hmap_heuResult.put(entry.getKey(), temp);
		}
	}
	public static void CallHeuristic(LinkedHashMap<String, LinkedHashMap<String, Double>> hmap_testSet, HuristicType enum_heuType) 
	{
		LinkedHashMap<String, LinkedHashMap<String, Double>> hmap_tempResults = new LinkedHashMap<>();

		for(Entry<String, LinkedHashMap<String, Double>> entry: hmap_testSet.entrySet()) 
		{
			String str_entityName = entry.getKey();
			String str_depth=str_entityName.substring(str_entityName.indexOf(str_depthSeparator)+str_depthSeparator.length(),str_entityName.length());
			LinkedHashMap<String, Double> hmap_Values = (LinkedHashMap<String, Double>) entry.getValue();

			for(Entry<String, Double> entry_hmapValues: hmap_Values.entrySet()) 
			{
				String str_catName = entry_hmapValues.getKey();
				Double db_value =entry_hmapValues.getValue();
				Double db_heuValue =0.0;
				if (enum_heuType.equals(HuristicType.NO_HURISTIC)) 
				{
					 db_heuValue = Heuristic_NanHeuristic(db_value);
				}
				else if (enum_heuType.equals(HuristicType.NUMBEROFPATHS_HURISTIC)) 
				{
					 db_heuValue = Heuristic_NumberOfPaths(db_value);
				}
				else if (enum_heuType.equals(HuristicType.NUMBEROFPATHSANDSUBCAT__HURISTIC)) 
				{
					// db_heuValue = Heuristic_NumberOfPaths(db_value);
				}
				
				hmap_Values.put(str_catName, db_heuValue);
				//System.err.println(str_entityName);
			}
			hmap_tempResults.put(str_entityName, hmap_Values);
		}
		//System.err.println("ZZZZZZZZZZZ "+hmap_tempResults.containsKey("Gustav_Mahler__1"));

		SortHeuristicResults(hmap_tempResults);
		FilterHeuResults();
	}
	public static void FilterHeuResults()
	{
		for(Entry<String, LinkedHashMap<String, Double>> entry: hmap_heuResult.entrySet()) 
		{
			String str_entityName = entry.getKey();

			LinkedHashMap<String, Double> hmap_Values = (LinkedHashMap<String, Double>) entry.getValue();
			LinkedHashMap<String, Double> hmap_catAndValFiletered = new LinkedHashMap<>();

			for(Entry<String, Double> entry_hmapValues: hmap_Values.entrySet()) 
			{
				String str_catName = entry_hmapValues.getKey();
				Double db_value =entry_hmapValues.getValue();
				if (db_value > threshold) 
				{
					hmap_catAndValFiletered.put(str_catName, db_value);
				}
			}
			hmap_heuResultThresholdFiltered.put(str_entityName, hmap_catAndValFiletered);
		}
	}
	private static double Heuristic_NanHeuristic(double db_Value)
	{
		return 1.0;
	}
	private static double Heuristic_NumberOfPaths(double db_Value)
	{
		return db_Value;
	}
	private static double Heuristic_NumberOfPathsAndDepth(double db_Value,int int_depth)
	{
		return (double)(db_Value/(double)int_depth);
	}
	private static double Heuristic_NumberOfPathsAndDepth(double db_Value,int int_depth,double treshold)
	{
		return (double)(db_Value/(double)int_depth);
	}
	
	private static void CompareResultsWithGroundTruth(HashMap<String, String> hmap_GTruth,
			Map<String, LinkedHashMap<String, Double>> hmap_hResults)
	{
		int[] arr_FoundDepth = new int[int_depthOfTheTree];
		int count_Cat=0;
		int count_NotFoundCat =0;
		for(Entry<String, String> entry: hmap_GTruth.entrySet()) 
		{
			String str_entity = entry.getKey();
			String str_category = entry.getValue();

			boolean changed = false;
			for (int i = 0; i <7; i++) 
			{ 	
				Integer int_index=i+1;
				LinkedHashMap<String, Double> ll_result = hmap_hResults.get(str_entity+str_depthSeparator+int_index.toString());
				if(ll_result==null) {
					continue;
				}
				if ( ll_result.size()>0) 
				{
					//String str_resCat= ll_result.keySet().iterator().next();
					///////////////////////////////////////////////////////
					final Double maxNumber = ll_result.values().iterator().next();
					final List<String> firstElements = ll_result.entrySet().stream().filter(p -> p.getValue()>=maxNumber).map(p -> p.getKey()).collect(Collectors.toList());
					System.err.println(str_entity+"\t"+int_index+"\t"+str_category +"\t"+firstElements+"\t"+firstElements.contains(str_category));
					if (firstElements.contains(str_category))
					///////////////////////////////////////////////////////
					//if (str_resCat.equals(str_category)) 
					{
						changed = true;
						//System.out.println(int_index+" "+str_entity+" "+str_category);
						arr_FoundDepth[i]+=1; 
						count_Cat++;
						break;
					}
				}
				
			}
			if (!changed)
			{
				System.out.println("XXXXXXXXXXX "+str_entity);
				count_NotFoundCat++;
			}

		}
		
//		System.out.println(count);
//		System.out.println(count_Cat);
		for (int i = 0; i < arr_FoundDepth.length; i++) 
		{
			System.out.println("Depth"+(i+1)+":" +arr_FoundDepth[i]);
		}
		
		System.out.println("Entity Count: "+ hmap_GTruth.size());
		//System.out.println("Test Entity Count: "+ hmap_testSet.size());
		System.out.println("Total Found Category Number: "+ count_Cat );
		System.out.println("Total NOT Found Category Number: "+ count_NotFoundCat );
	}
	
	public static void printMap(Logger log, Map mp) {
		//System.out.println("----------------------------");
		log.info("----------------------------");
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			//System.out.println(pair.getKey() + " = " + pair.getValue());
			log.info(pair.getKey() + " = " + pair.getValue());
		}
	}
	private static LinkedHashMap<String, Double> sortByValue(Map<String, Double> unsortMap) {

		// 1. Convert Map to List of Map
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

		// 2. Sort list with Collections.sort(), provide a custom Comparator
		// Try switch the o1 o2 position for a different order
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// 3. Loop the sorted list and put it into a new insertion order Map
		// LinkedHashMap
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		/*
		 * //classic iterator example for (Iterator<Map.Entry<String, Integer>>
		 * it = list.iterator(); it.hasNext(); ) { Map.Entry<String, Integer>
		 * entry = it.next(); sortedMap.put(entry.getKey(), entry.getValue()); }
		 */

		return sortedMap;
	}

}