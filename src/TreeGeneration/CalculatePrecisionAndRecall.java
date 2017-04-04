package TreeGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CalculatePrecisionAndRecall {
	static String str_path = System.getProperty("user.dir") + File.separator;
	static int numberOfSub = 7;
	
	private static final HashMap<String, String> hmap_groundTruth = new HashMap<>();
	private static final LinkedHashMap<String, HashMap<String, Double>> hmap_testSet = new LinkedHashMap<>();
	private static final Map<String, LinkedList<Double>> hmap_groundTruthlist = new HashMap<>();

	private static final Map<String, LinkedHashMap<String, Double>> hmap_heuResult = new HashMap<>();

	public static void ReadResultFromAllFile(String fileName) {

	}

	public static void InitializeTestSet(String fileName) {
		String str_entityName = null;
		String str_catName = null;

		try (BufferedReader br = new BufferedReader(new FileReader(str_path + fileName));) {
			String line = null;
			int depth = numberOfSub;

			ArrayList<String> arrList_paths = new ArrayList<>();

			ArrayList<Integer> numberOfPaths = new ArrayList<>();
			HashMap<String, Double> hmap_catAndValue = new HashMap<>();
			while ((line = br.readLine()) != null) {

				if (line.contains(",") && !line.contains("\",\"")) {

					str_entityName = line.split(",")[0];
					str_catName = line.split(",")[1];
					hmap_groundTruth.put(line.split(",")[0], str_catName);

				} 
				else if (line.length() < 1 ) {
					// MyHeuristic(arrList_paths, depth, line);
					hmap_testSet.put(str_entityName + "__" + depth, hmap_catAndValue);
					hmap_catAndValue = new HashMap<>();
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
					// printResults();

					// for(Entry<String, HashMap<String, Double>> entity:
					// hmap_testSet.entrySet()) {
					//
					// System.out.println(entity.getKey());
					// for(Double ent:entity.getValue().values())
					// {
					// System.out.println(ent);
					// }
					// }

					depth = numberOfSub;
				}
			}
		} catch (IOException e) {

			e.printStackTrace();

		}
		//printMap(hmap_groundTruth); 
		System.out.println("----------------------------");
		printMap(hmap_testSet);
	}
	
	public static void printMap(Map mp) {
	    Iterator it = mp.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	}
	
	public static Map<String, Double> Heuristic_NumberOfPaths(HashMap<String, Double> hmap_tobeRanked) {
		return sortByValue(hmap_tobeRanked);
	}

	private static Map<String, Double> sortByValue(Map<String, Double> unsortMap) {

		// 1. Convert Map to List of Map
		List<Map.Entry<String, Double>> list = new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

		// 2. Sort list with Collections.sort(), provide a custom Comparator
		// Try switch the o1 o2 position for a different order
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// 3. Loop the sorted list and put it into a new insertion order Map
		// LinkedHashMap
		Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
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
