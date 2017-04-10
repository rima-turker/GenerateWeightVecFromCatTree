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
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

public class CalculatePrecisionAndRecall {
	
	static String str_path = System.getProperty("user.dir") + File.separator;
	static int int_depthOfTheTree = 7;
	static String str_depthSeparator = "__";
	private static double threshold= 0;
	
	private static final Logger LOG = Logger.getLogger(CalculatePrecisionAndRecall.class.getCanonicalName());
	private static final Logger log_heuResult = Logger.getLogger("heuResultLogger");
	private static final Logger  log_normalized= Logger.getLogger("reportsLogger");
	
	private static final Map<String, ArrayList<Double>> hmap_subCategoryCount = new HashMap<>();
	private static final HashMap<String, String> hmap_groundTruth = new LinkedHashMap<>();
	private static final LinkedHashMap<String, LinkedHashMap<String, Double>> hmap_testSet = new LinkedHashMap<>();
	private static final Map<String, LinkedList<String>> hmap_groundTruthlist = new LinkedHashMap<>();
	
	private static final Map<String, LinkedHashMap<String, Double>> hmap_heuResult = new LinkedHashMap<>();
	private static final Map<String, LinkedHashMap<String, Double>> hmap_heuResultNormalized = new LinkedHashMap<>();
	private static final Map<String, LinkedHashMap<String, Double>> hmap_heuResultNormalizedSorted = new LinkedHashMap<>();
	private static final Map<String, LinkedHashMap<String, Double>> hmap_heuResultNormalizedSortedFiltered = new LinkedHashMap<>();
	private static final HashSet<Double> hset_ValuesToNormalize = new HashSet<>();
	private static final Map<Double,Double> hmap_NormalizationMap = new HashMap<>();
	private static final Map<String, LinkedHashMap<String, Double>> hmap_precisionRecallFmeasure = new LinkedHashMap<>();

	enum HeuristicType 
	{		
		HEURISTIC_NO,
		HEURISTIC_NUMBEROFPATHS,
		HEURISTIC_NUMBEROFPATHSANDDEPTH,
		HEURISTIC_NUMBEROFPATHSANDDEPTHANDSUBCAT;
		
	}
	private static void emptyMaps()
	{
		hmap_heuResult.clear();
		hmap_heuResultNormalized.clear();
		hmap_heuResultNormalizedSorted.clear();
		hmap_heuResultNormalizedSortedFiltered.clear();
		hset_ValuesToNormalize.clear();
		hmap_NormalizationMap.clear();
		hmap_precisionRecallFmeasure.clear();
	}
	public static void main(String str_fileNameGroundTruthList,String str_fileNameTestSet,Double db_threshold, HeuristicType heu) 
	{
		emptyMaps();
		ReadSubCategoryNumber();
		threshold = db_threshold;
		InitializeGroundTruthAndList(str_fileNameGroundTruthList);
		InitializeTestSet(str_fileNameTestSet);
		
		callHeuristic(heu);//hmap_heuResult
//		if (heu.equals(HeuristicType.HEURISTIC_NUMBEROFPATHSANDDEPTHANDSUBCAT)) 
//		{
//			printMap(log_heuResult, hmap_heuResult,heu);
//		}
		
		
		callNormalization();//hmap_heuResultNormalized
		//printMap(log_normalized, hmap_heuResultNormalized,heu);
		SortHeuristicResults();//hmap_heuResultNormalizedSorted
		filterHeuResults();//hmap_heuResultNormalizedSortedFiltered
		//printMap(log_normalized, hmap_heuResultNormalizedSortedFiltered,heu);
		
		/*
		 * 1)Heu
		 * 2)Normalization
		 * 3)Sort
		 * 4)Filter
		 *  = hmap_finalResult
		 */
		compareResultsWithGroundTruth(hmap_heuResultNormalizedSortedFiltered);
		callCalculatePrecisionAndRecall();
	}

	public static void callNormalization()
	{
		NormalizeHashSet();
		
		
		
		for(Entry<String, LinkedHashMap<String, Double>> entry: hmap_heuResult.entrySet()) 
		{
			LinkedHashMap<String, Double> lhmap_temp = new LinkedHashMap<>();
			
			String str_entityNameAndDepth = entry.getKey();
			
			LinkedHashMap<String, Double> hmap_CatAndValue = entry.getValue();

			for(Entry<String, Double> entry_CatAndValue: hmap_CatAndValue.entrySet()) 
			{
				lhmap_temp.put(entry_CatAndValue.getKey(), hmap_NormalizationMap.get(entry_CatAndValue.getValue()));
			}
			hmap_heuResultNormalized.put(str_entityNameAndDepth, lhmap_temp);
			
		}
		
	}
	public static void callCalculatePrecisionAndRecall()
	{

		for(Entry<String, LinkedHashMap<String, Double>> entry: hmap_heuResultNormalizedSortedFiltered.entrySet()) 
		{
			String str_entityNameAndDepth= entry.getKey();
			String str_depth= str_entityNameAndDepth.substring(str_entityNameAndDepth.indexOf(str_depthSeparator)+str_depthSeparator.length(),
					str_entityNameAndDepth.length());
			String str_entityName = str_entityNameAndDepth.substring(0,str_entityNameAndDepth.indexOf(str_depthSeparator));
			hmap_precisionRecallFmeasure.put(str_entityNameAndDepth, CalculatePrecisionRecallFmeasure(str_entityName,str_depth));
		}

		String str_Pre="=SPLIT(\""; 
		String str_Rec ="=SPLIT(\"";
		String str_Fsco="=SPLIT(\"";
		
		
				
		for (Integer int_depth = int_depthOfTheTree; int_depth > 0 ; int_depth--)
//		for (Integer int_depth = 1; int_depth <= int_depthOfTheTree ; int_depth++) 
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
			 DecimalFormat df = new DecimalFormat("0.00000");
			str_Pre= str_Pre+" ,"+df.format(arr_Pre[int_depth-1]/int_NumberOfEntities);
			str_Rec= str_Rec+" ,"+df.format(arr_Rec[int_depth-1]/int_NumberOfEntities);
			str_Fsco= str_Fsco+" ,"+df.format(arr_Fsco[int_depth-1]/int_NumberOfEntities);
//			System.out.println("Depth "+ int_depth + " Precision " + df.format(arr_Pre[int_depth-1]/int_NumberOfEntities));
//			System.out.println("Depth "+ int_depth + " Recall " + df.format(arr_Rec[int_depth-1]/int_NumberOfEntities));
//			System.out.println("Depth "+ int_depth + " Fscore " + df.format(arr_Fsco[int_depth-1]/int_NumberOfEntities));
			
		}
		str_Pre += "\",\",\")";
		str_Rec += "\",\",\")";
		str_Fsco += "\",\",\")";
		
		System.out.println(str_Pre.replace("=SPLIT(\" ,", "=SPLIT(\""));
		System.out.println(str_Rec.replace("=SPLIT(\" ,", "=SPLIT(\""));
		System.out.println(str_Fsco.replace("=SPLIT(\" ,", "=SPLIT(\""));
		
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

		LinkedHashMap<String, Double> lhmap_depthElements = hmap_heuResultNormalizedSortedFiltered.get(str_entity+str_depthSeparator+str_depth);
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
					//System.out.println("--------------------------------------");
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
					//System.out.println("--------------------------------------");
				}
			}

			for(Entry<String, LinkedList<String>> entry: hmap_groundTruthlist.entrySet()) 
			{
				str_entity = entry.getKey();
				LinkedList<String> str_categories = entry.getValue();

				//System.out.println(str_entity+ " "+ str_categories);
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

	public static void SortHeuristicResults()
	{
		for(Entry<String, LinkedHashMap<String, Double>> entry: hmap_heuResultNormalized.entrySet()) 
		{
			final LinkedHashMap<String, Double> temp = sortByValue(entry.getValue());
			hmap_heuResultNormalizedSorted.put(entry.getKey(), temp);
		}
	}
	public static void callHeuristic(HeuristicType enum_heuType) 
	{
		for(Entry<String, LinkedHashMap<String, Double>> entry: hmap_testSet.entrySet()) 
		{
			String str_entityName = entry.getKey();
			String str_depth=str_entityName.substring(str_entityName.indexOf(str_depthSeparator)+str_depthSeparator.length(),str_entityName.length());
			LinkedHashMap<String, Double> hmap_Values = (LinkedHashMap<String, Double>) entry.getValue();
			LinkedHashMap<String, Double> lhmap_Results = (LinkedHashMap<String, Double>) entry.getValue();
			for(Entry<String, Double> entry_hmapValues: hmap_Values.entrySet()) 
			{
				String str_catName = entry_hmapValues.getKey();
				Double db_value =entry_hmapValues.getValue();
				Double db_heuValue =0.0;
				if (enum_heuType.equals(HeuristicType.HEURISTIC_NO)) 
				{
					 db_heuValue = Heuristic_NanHeuristic(db_value);
				}
				else if (enum_heuType.equals(HeuristicType.HEURISTIC_NUMBEROFPATHS)) 
				{
					 db_heuValue = Heuristic_NumberOfPaths(db_value);
				}
				else if (enum_heuType.equals(HeuristicType.HEURISTIC_NUMBEROFPATHSANDDEPTH)) 
				{
					 db_heuValue = Heuristic_NumberOfPathsAndDepth(db_value,Integer.parseInt(str_depth));
				}
				else if (enum_heuType.equals(HeuristicType.HEURISTIC_NUMBEROFPATHSANDDEPTHANDSUBCAT)) 
				{
					db_heuValue = Heuristic_NumberOfPathsDepthSubCat(db_value,Integer.parseInt(str_depth),  str_catName);
				}
				lhmap_Results.put(str_catName, db_heuValue);
				hset_ValuesToNormalize.add(db_heuValue);
			}
			hmap_heuResult.put(str_entityName, lhmap_Results);
		}
		//System.err.println("ZZZZZZZZZZZ "+hmap_tempResults.containsKey("Gustav_Mahler__1"));
	}
	public static void filterHeuResults()
	{
		for(Entry<String, LinkedHashMap<String, Double>> entry: hmap_heuResultNormalizedSorted.entrySet()) 
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
			hmap_heuResultNormalizedSortedFiltered.put(str_entityName, hmap_catAndValFiletered);
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
	
	private static double Heuristic_NumberOfPathsDepthSubCat(double db_Value,int int_depth,String  str_cat)
	{
		return (double)db_Value/(double)((hmap_subCategoryCount.get(str_cat).get(int_depth-1)*int_depth));
	}
	
	private static void compareResultsWithGroundTruth(Map<String, LinkedHashMap<String, Double>> hmap_heuResult)
	{
		int[] arr_FoundDepth = new int[int_depthOfTheTree];
		int count_Cat=0;
		int count_NotFoundCat =0;
		
		for(Entry<String, String> entry: hmap_groundTruth.entrySet()) 
		{
			String str_entity = entry.getKey();
			String str_category = entry.getValue();

			boolean changed = false;
			for (int i = 0; i <7; i++) 
			{ 	
				Integer int_index=i+1;
				LinkedHashMap<String, Double> ll_result = hmap_heuResult.get(str_entity+str_depthSeparator+int_index.toString());
				
				if(ll_result==null) {
					continue;
				}
				if ( ll_result.size()>0) 
				{
					//String str_resCat= ll_result.keySet().iterator().next();
					///////////////////////////////////////////////////////
					final Double maxNumber = ll_result.values().iterator().next();
					final List<String> firstElements = ll_result.entrySet().stream().filter(p -> p.getValue()>=maxNumber).map(p -> p.getKey()).collect(Collectors.toList());
					//System.err.println(str_entity+"\t"+int_index+"\t"+str_category +"\t"+firstElements+"\t"+firstElements.contains(str_category));
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
				//System.out.println("XXXXXXXXXXX "+str_entity);
				count_NotFoundCat++;
			}

		}
		//System.out.println("=SPLIT(\"" + formatResult[i] + "\",\",\")");
		String str_formated="=SPLIT(\""; 
		for (int i = 0; i< arr_FoundDepth.length; i++) 
		{

			str_formated= str_formated+ " ,"+arr_FoundDepth[i];
//			System.out.println(arr_FoundDepth[i]);
		}
		
		
		str_formated += "\",\",\")";
		System.out.println(str_formated.replace("=SPLIT(\" ,", "=SPLIT(\""));
		
		System.out.println("Entity Count: "+ hmap_groundTruth.size());
		System.out.println("Total Found Category Number: "+ count_Cat );
		System.out.println("Total NOT Found Category Number: "+ count_NotFoundCat );
	}
	
	public static void NormalizeHashSet()  
	{
	
		double min = Collections.min(hset_ValuesToNormalize);
		double max =Collections.max(hset_ValuesToNormalize);
		
		if (min==max) 
		{
			hmap_NormalizationMap.put(min, 1.);
			return;
		}
		
		for (Double db_val: hset_ValuesToNormalize)
		{
			hmap_NormalizationMap.put(db_val, ((double) ((double) (db_val - min) / (double) (max - min))));
		}
		//Double[] array = hmap_NormalizationMap.values().stream().toArray(x -> new Double[x]);
		//System.err.println("Median :"+median(array));
	}
	
	public static double median(Double[] m) {
	    int middle = m.length/2;
	    if (m.length%2 == 1) {
	        return m[middle];
	    } else {
	        return (m[middle-1] + m[middle]) / 2.0;
	    }
	}
	
	public static void printMap(Logger log, Map mp, HeuristicType heu) {
		//System.out.println("----------------------------");
		log.info(heu);
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
//			//System.out.println(pair.getKey() + " = " + pair.getValue());
//			log.info(pair.getKey() + " = " + pair.getValue());
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
		log.info("----------------------------");
		log.info("");
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
			brC = new BufferedReader(new FileReader(str_path+"SubCategory_Count.csv"));
			String lineCategory = null;

			while ((lineCategory = brC.readLine()) != null) {

				lineCategory=lineCategory.toLowerCase();
				arrListTemp = new ArrayList<>();
				// System.out.println(lineCategory);
				subCount = (lineCategory.substring(lineCategory.indexOf(":,") + 2, lineCategory.length()).split(","));
				int_subCount = Arrays.stream(subCount).mapToInt(Integer::parseInt).toArray();

				for (int i = 0; i < int_subCount.length; i++)
				{
					arrListTemp.add((double)int_subCount[i]);
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
}