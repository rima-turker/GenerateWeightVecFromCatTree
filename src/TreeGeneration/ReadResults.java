package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;


public class ReadResults {
	static String path = System.getProperty("user.dir") + File.separator;
	static String path_FamEntities = System.getProperty("user.dir") + File.separator;
	static String path_SubCategoryCount = System.getProperty("user.dir") + File.separator;
	static int numberOfSub = 7;
	static int index = 0;
	static String nameAndCat = null;
	static String[] formatResult = new String[100];
	static boolean flag = true;
	static int levelOfTreeforEvaluation = 7;
	static ArrayList<String> arrListResultsWCatName = new ArrayList<String>();

	private static final Map<String, ArrayList<Double>> hmap_subCategoryCount = new HashMap<>();
	private static final LinkedHashMap<String, Map<Integer, List<String>>> hmap_entityCategoryCount = new LinkedHashMap<>();

	
	public static HashMap<String, HashMap<String, Double>> ReadResultFromDifferentFileEntAndCat(String str_FileName) 
	{
		HashMap<String,HashMap<String,Double >> hmap_entitCatPath = new HashMap<>();
		HashSet<String> hset_fileContent = new HashSet<>();
		HashSet<String> hset_entity = new HashSet<>();
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(path+str_FileName));
			BufferedReader br_entityList = new BufferedReader(new FileReader(path+"EntityList.txt"));
			
			ArrayList<String> arrList_fileContent = new ArrayList<>();
			String lineCategory,str_entity;

			while ((str_entity = br_entityList.readLine()) != null) 
			{
				hset_entity.add(str_entity.toLowerCase());
			}
			br_entityList.close();
			
			while ((lineCategory = br.readLine()) != null) 
			{
				arrList_fileContent.add(lineCategory.toLowerCase());
//				if (hset_fileContent.contains(lineCategory)) 
//				{
//					System.out.println(lineCategory);
//				}
				//hset_fileContent.add(lineCategory);
			}
			
			
			for (int i = 0; i < arrList_fileContent.size(); i++) 
			{
				String str_entityName = arrList_fileContent.get(i).substring(0, arrList_fileContent.get(i).indexOf("> "));
				HashMap<String, Double> hmap_catAndVal = new HashMap<>();
				if (!hmap_entitCatPath.containsKey(str_entityName)) 
				{
					for (int j = 0; j < arrList_fileContent.size(); j++) 
					{
						if (arrList_fileContent.get(j).substring(0, arrList_fileContent.get(j).indexOf("> ")).equals(str_entityName))
						{
							
							String[] str_split = arrList_fileContent.get(j).split("> "); 
							String str_catName= str_split[str_split.length-2];
							if (hmap_catAndVal.containsKey(str_catName)) 
							{
								Double int_tempValue= hmap_catAndVal.get(str_catName);
								hmap_catAndVal.put(str_catName, ++int_tempValue);
							}
							else
							{
								hmap_catAndVal.put(str_catName, 1.0);
							}
							
						}
					}
					if (hset_entity.contains(str_entityName))
					{
						hmap_entitCatPath.put(str_entityName, hmap_catAndVal);
					}
					
					
				}
				//else
					//System.out.println(str_entityName);
				
			}

			br.close();
         
        int int_count=0;
        for (Entry<String, HashMap<String, Double>> entry:hmap_entitCatPath.entrySet()) 
        {
			//HashMap<String, Integer> hmap_catAndVal = entry.getValue();
			if (hset_entity.contains(entry.getKey())) 
			{
				//System.out.println(entry.getKey()+" "+ entry.getValue());
				int_count++;
			}
			

        }
        
//       System.out.println();
//       System.out.println(int_count);
		} 
		catch (Exception e) 
		{
			// TODO: handle exception
		}
		return hmap_entitCatPath;

	}	
	public static void ReadResultFromAllFile(String fileName,String famEntities) {

		path += fileName;
		path_FamEntities+=famEntities;

		BufferedReader br = null;

		BufferedReader br_FamEntities = null;

		try {


			br_FamEntities = new BufferedReader(new FileReader(path_FamEntities));

			String line = null;
			// File log = new File(fileSkos+".txt");
			// if(!log.exists())
			// {
			// log.createNewFile();
			// }
			// FileWriter fileWriter = new FileWriter(log, true);
			// BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			LinkedHashSet<String> hsetEntity = new LinkedHashSet<>();
			int counter = 0;
			String strEntityCategory = null;
			String str_tempSplitedString=null;
			String[] str_SplitedString;

			String lineCategory;

			int count = 0;

			while ((lineCategory = br_FamEntities.readLine()) != null) 
			{
				hsetEntity.add(lineCategory);
			}

			String str = "pageLinkCleaned_OnlyCategoryFiltered_L";
			Integer level = null;

			for (String entityAndCat : hsetEntity) 
			{
				
				String entity= entityAndCat.substring(0, entityAndCat.indexOf(","));
				
				HashMap<Integer, List<String>> hmap_TempCatCount = new HashMap<>();


				br = new BufferedReader(new FileReader(path));

				while ((line = br.readLine()) != null) 
				{
					List<String> list_TempCatCount = new ArrayList<>();
					if (line.contains(":"+entity+">-<"))
					{
						if (line.contains(str)) 
						{
							level= Integer.parseInt(line.substring(str.length(),str.length()+1));
							
							str_tempSplitedString = line.substring(line.indexOf("Archaeology:"), line.length() - 2);
							str_tempSplitedString.replace(">-<", "");
							
							str_SplitedString = str_tempSplitedString.split(",");

							for (int i = 0; i < str_SplitedString.length; i++) 
							{
								if (Integer.parseInt(str_SplitedString[i].substring(str_SplitedString[i].indexOf(":") + 1,
										str_SplitedString[i].length())) > 0) 
								{
									strEntityCategory += str_SplitedString[i];
									list_TempCatCount.add(str_SplitedString[i]);
								}
							}

							hmap_TempCatCount.put(level, list_TempCatCount);
						}
					}
				}
				hmap_entityCategoryCount.put(entity, hmap_TempCatCount);
				br.close();
			}
			
			for(Entry<String, Map<Integer, List<String>>> entry: hmap_entityCategoryCount.entrySet())
			{
				String[] arr_StringPrint = new String[7];
				
				String entityName = entry.getKey();
				
				for (String entityAndCat : hsetEntity) 
				{
					if (entityAndCat.contains(entityName)) 
					{
						System.out.println(entityAndCat);
					}
				}
//				System.out.println(entityName);
				level = -1;
				List<String> list = null;
				
				for (int i = 6; i >=0; i--) 
				{
					
					List<String> list2 = entry.getValue().get(i);
					if (list2!=null) 
					{
						for (int j = 0; j < list2.size(); j++) 
						{
							System.out.println(list2.get(j));
						}
					}
					else
					{
						System.out.println("-");
					}
					if (i!=0) 
					{
						System.out.println();
					}
					
				}
//				for(Entry<Integer, List<String>> map: entry.getValue().entrySet())
//				{
//					level = map.getKey();
//					list = map.getValue();
//					
//					//System.err.println(entityName+"\t"+level+"\t"+list.toString());	
//					System.out.println(level+1);
//					for (int i = 0; i < list.size(); i++) 
//					{
//						System.out.println(list.get(i));
//					}
//				}
				//System.out.println("----------------------------------------");
			}
			
			
			for (Integer j = 0; j < 7; j++) 
			{

				br = new BufferedReader(new FileReader(path));

				str+=j.toString()+":";
			
				
				while ((line = br.readLine()) != null) {

					if (line.contains(str)) {
						line = line.substring(str.length(), line.length());

						strEntityCategory = line.substring(0, line.indexOf(">-<Archaeology"));

						//System.out.println(strEntityCategory);

						str_tempSplitedString = line.substring(line.indexOf(">-<Archaeology:"), line.length() - 2);

						if (hsetEntity.contains(strEntityCategory)) {
							str_SplitedString = str_tempSplitedString.split(",");

							for (int i = 0; i < str_SplitedString.length; i++) {
								if (Integer.parseInt(str_SplitedString[i].substring(str_SplitedString[i].indexOf(":") + 1,
										str_SplitedString[i].length())) > 0) {
									strEntityCategory += str_SplitedString[i];
								}
							}
							System.out.println(strEntityCategory);
						}

					}
				}
				br.close();
			}

			//System.out.println(counter);

		} 
		catch (IOException e) {

			e.printStackTrace();

		}
	}

	public static void ReadResultFromCVSFile(String fileNAme, String subCatCount) {
		ReadSubCategoryNumber(subCatCount);
		
		flag=false;
		
		path += fileNAme;

		try (BufferedReader br = new BufferedReader(new FileReader(path));){


			String line = null;
			int depth = numberOfSub;

			ArrayList<String> arrList_paths = new ArrayList<>();

			ArrayList<Integer> numberOfPaths = new ArrayList<>();

			while ((line = br.readLine()) != null) {
				// Entty&Cat
				if (!line.contains("\"") && line.contains(",") && !line.contains("\",\"")) {
					//					finalResult.append("=SPLIT(\"").append(line).append(",");
					//					finalResultMap.put(line, new HashMap<Integer, List<String>>());

					
//					for (int i = 0; i < formatResult.length; i++) {
//						if (formatResult[i] != null) {
//
//							for (int j = 0; j < (levelOfTreeforEvaluation * 2)
//									- StringUtils.countMatches(formatResult[i], ","); j++) {
//								formatResult[i] += ",-";
//							}
//							System.out.println("=SPLIT(\"" + formatResult[i] + "\",\",\")");
//						}
//					}
					//System.out.println(depth);
					nameAndCat = line;
					
//					formatResult = new String[100];
//					index = 0;
//					depth = numberOfSub;
//					arrListResultsWCatName.clear();

				} 
				else if (line.length() < 1) {
					// System.out.println();
					// System.out.println("numberOfPaths "+
					// numberOfPaths+"depth: "+depth+" "+
					// subCatCountInt[depth-1]);
					MyHeuristic(arrList_paths, depth, line);
					
					depth--;
					index = 0;
					numberOfPaths.clear();
					arrList_paths.clear();
					arrListResultsWCatName.clear();

				} 
				else {
					// System.out.println(" "+line);
					if (line.contains(":")) 
					{
						arrListResultsWCatName.add(line);
						arrList_paths.add(line);
						numberOfPaths.add(Integer.parseInt(line.substring(line.indexOf(":") + 1, line.length())));

						if (formatResult[index] != null) {
							formatResult[index] = formatResult[index] + "," + line + " ";
						} else {
							formatResult[index] = line + " ";
							// formatResult[index]=nameAndCat+line+" ";
						}

						index++;
					}

				}
				if (depth==1)
				{
					printResults();
					
					formatResult = new String[100];
					index = 0;
					depth = numberOfSub;
					arrListResultsWCatName.clear();
				}
			}
		} catch (IOException e) {

			e.printStackTrace();

		}

	}
	public static void printResults() throws IOException
	{
		File log = new File(System.getProperty("user.dir")+"temp_FormatedResults");
		
		if (!log.exists()) 
		{
			log.createNewFile();
			
		}
		FileWriter fileWriter = new FileWriter(log,true);
		BufferedWriter bf_Writer = new BufferedWriter(fileWriter);
		
		
		for (int i = 0; i < formatResult.length; i++) 
		{
			if (formatResult[i] != null) {

				for (int j = 0; j < (levelOfTreeforEvaluation * 2)
						- StringUtils.countMatches(formatResult[i], ","); j++) {
					formatResult[i] += ",-";
				}
				System.out.println("=SPLIT(\"" + formatResult[i] + "\",\",\")");
				bf_Writer.write("=SPLIT(\"" + formatResult[i] + "\",\",\")");
				bf_Writer.newLine();
			}
		}
		bf_Writer.newLine();
		bf_Writer.close();
	}
	public static void MyHeuristic(ArrayList<String> categoryandPath, int depth, String line) {
		double[] result = new double[categoryandPath.size()];
		// numberOfPaths*1000/(den*depth)

		int int_tempPath = 0;
		String str_category = null;
		DecimalFormat df = new DecimalFormat("0.00000");
		for (int i = 0; i < result.length; i++)
		{

			int_tempPath = Integer.parseInt(categoryandPath.get(i).substring(categoryandPath.get(i).indexOf(":") + 1,
					categoryandPath.get(i).length()));
			str_category = categoryandPath.get(i).substring(0, categoryandPath.get(i).indexOf(":"));
			// result[i] =(double)
			result[i] =((double)((double)int_tempPath*100)/(double)(hmap_subCategoryCount.get(str_category).get(depth-1)*depth));
			//			result[i] = (double)((double)(int_tempPath)/(double)(hmap_subCategoryCount.get(str_category).get(depth - 1) * depth));
			//result[i] =((double)((double)int_tempPath)/(double)(depth));

			arrListResultsWCatName.set(i, arrListResultsWCatName.get(i)+","+df.format(result[i]));
			formatResult[i]=formatResult[i]+","+ df.format(result[i]);
		}

		//Normalize
		//		if (result.length>0) 
		//		{
		//			double[] double_resultNormalized = NormalizeArray(result);
		//			for (int i = 0; i < result.length; i++) 
		//			{
		//				arrListResultsWCatName.set(i, arrListResultsWCatName.get(i) + "," + df.format(double_resultNormalized[i]));
		//				formatResult[i] = formatResult[i] + "," + df.format(double_resultNormalized[i]);
		//			}
		//		}


		index = 0;
	}

	public static void ReadSubCategoryNumber(String subCatCount) {
		String[] subCount = null;
		path_SubCategoryCount += subCatCount;
		int[] int_subCount;
		double[] double_subCount;
		BufferedReader brC;
		ArrayList<Double> arrListTemp;
		try {
			brC = new BufferedReader(new FileReader(path_SubCategoryCount));
			String lineCategory = null;

			while ((lineCategory = brC.readLine()) != null) {

				arrListTemp = new ArrayList<>();
				// System.out.println(lineCategory);
				subCount = (lineCategory.substring(lineCategory.indexOf(":,") + 2, lineCategory.length()).split(","));
				int_subCount = Arrays.stream(subCount).mapToInt(Integer::parseInt).toArray();


				for (int i = 0; i < int_subCount.length; i++)
				{
					arrListTemp.add((double)int_subCount[i]);
				}

				//Normalize
				//				double_subCount = NormalizeArray(int_subCount);
				//				for (int i = 0; i < int_subCount.length; i++) {
				//					arrListTemp.add(double_subCount[i]);
				//				}

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

	public static double[] NormalizeArray(int[] arr) {
		double[] arrNormalized = new double[arr.length];
		int min = getMin(arr);
		int max = getMax(arr);

		if (arr.length<2) 
		{
			arrNormalized[0]=2.0;
			return arrNormalized;
		}

		for (int i = 0; i < arrNormalized.length; i++) {
			//arrNormalized[i] = (double) ((double) (arr[i] - min) / (double) (max - min));
			arrNormalized[i] = 1+((double) ((double) (arr[i] - min) / (double) (max - min)));
		}

		return arrNormalized;
	}

	public static double[] NormalizeArray(double[] arr) {
		double[] arrNormalized = new double[arr.length];
		double min = getMin(arr);
		double max = getMax(arr);

		if (arr.length<2) 
		{
			arrNormalized[0]=2.0;
			return arrNormalized;
		}
		for (int i = 0; i < arrNormalized.length; i++) 
		{
			//arrNormalized[i] = (double) ((double) (arr[i] - min) / (double) (max - min));

			arrNormalized[i] = 1+((double) ((double) (arr[i] - min) / (double) (max - min)));
		}

		return arrNormalized;
	}

	public static double getMin(double[] arr) {

		return (double) Collections.min(Arrays.asList(ArrayUtils.toObject(arr)));

	}

	public static double getMax(double[] arr) {
		return (double) Collections.max(Arrays.asList(ArrayUtils.toObject(arr)));
	}

	public static int getMin(int[] arr) {

		return (int) Collections.min(Arrays.asList(ArrayUtils.toObject(arr)));

	}

	public static int getMax(int[] arr) {
		return (int) Collections.max(Arrays.asList(ArrayUtils.toObject(arr)));
	}

}
