package TreeGeneration;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.Set;

import javax.print.attribute.HashAttributeSet;

import util.MapUtil;

public class Main {

	final static String str_ExampleNo = "3";
	final static String str_fileName = "page_resourcePath_Cleaned_objOnlyCategory";
	//"infoBoxCleaned_OnlyCategoryFiltered_sort"
	public static void main(String[] args) throws Exception {
		
//		ReadResultsFromFilteredFiles read = new ReadResultsFromFilteredFiles(str_fileName);
//		read.createCategoryMap();
//		CompareTwoFilesForCatFiltering.compareHashsetAndFile(str_fileName, read.getHset_allCats());
		
		for ( double the = 0.01; the<=0.09; the += 0.01) {
			// System.out.println("--------------------Threshold-------------------"+the);
			
			for (GlobalVariables.HeuristicType heu : GlobalVariables.HeuristicType.values()) {
			     new EvaluateHeuristicFunctions<Object>("GoalSet_AllCombined.tsv", the, heu).main();
			}
		}
		
		Map<String, Double> hmap_fmeasure = new HashMap<>();
		hmap_fmeasure=MapUtil.entriesSortedByValues(EvaluateHeuristicFunctions.hmap_fmeasureAll);
		int count =0;
		for (Entry <String, Double>   entry: hmap_fmeasure.entrySet()) 
		{
			count++;
			System.out.println(entry.getKey()+" "+entry.getValue());
			if (count==20) {
				break;
			}
		}
//		List<Double> sortedList = new ArrayList<Double>(EvaluateHeuristicFunctions.hset_fmeasure);
//		Collections.sort(sortedList);
//		
//		for (int i =sortedList.size()-1; i>=20 ; i--) 
//		{
//			System.out.println(sortedList.get(i));
//		}

	}
}
	 
