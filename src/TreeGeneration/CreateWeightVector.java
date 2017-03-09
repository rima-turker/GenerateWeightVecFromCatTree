package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class CreateWeightVector {

//	static String pathResultFile="/home/rtue/Desktop/GenerateTree/CategoryFiles/ResultFiles" +
//			"/infoBoxCleaned_OnlyCategoryFiltered_L";
	
	static String pathResultFile="/home/rtue/Desktop/GenerateTree/ResultFiles" +
			"/pageLinkCleaned_OnlyCategoryFiltered_L";
	
	private static String CATEGORY_FOLDER = "/home/rtue/Desktop/GenerateTree/CategoryFiles"; 
	private static String VECTOR_FOLDER = "/home/rtue/Desktop/GenerateTree/Vectors"; 

	private static Map<String,Integer> categoryPlaceHolder = new LinkedHashMap<>();
	private static final Map<String,HashSet<String>> categoryMap = new HashMap<>();
	public static void main(String[] args) throws FileNotFoundException 
	{	
		for (int i = 0; i < 5; i++) 
		{
			createCategoryMap(i);
			final Map<String,Map<String,Integer>> entityMap = generateWeightVector(i);
			writeWieghtVectorToFile(entityMap,i);
			System.err.println("Iteration "+i+" is done");
			categoryMap.clear();
			System.err.println(entityMap.size());
			entityMap.clear();
			
//			for(Entry<String, Map<String, Integer>> entity: entityMap.entrySet()) {
//				int sum = 0;
//				for(int ent:entity.getValue().values()) {
//					sum+=ent;
//				}
//				if(sum>1) {
//					System.out.println(entity.getKey());
//				}
//			}
		}
		
		
	}

	private static void writeWieghtVectorToFile(
			Map<String, Map<String, Integer>> entityMap, int i) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(VECTOR_FOLDER+"/pageLinkCleaned_OnlyCategoryFiltered_L"+i);
			bw = new BufferedWriter(fw);
			final StringBuilder content = new StringBuilder();
			for(final Entry<String, Map<String, Integer>> entity: entityMap.entrySet()) {
				content.append(entity.getKey()).append("-").append("<");
				for(final Entry<String, Integer> ent: entity.getValue().entrySet()) {
					content.append(ent.getKey()).append(":").append(ent.getValue()).append(",");
				}
				content.append(">").append("\n");
			}
			bw.write(content.toString());
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}


	private static Map<String,Map<String,Integer>> generateWeightVector(int i) {
		final Map<String,Map<String,Integer>> entityMap = new LinkedHashMap<>();
		final String pathFile= pathResultFile + String.valueOf(i); 
		BufferedReader br = null;
		FileReader fr = null;
		String sCurrentLine = null;
		try {
			fr = new FileReader(pathFile);
			br = new BufferedReader(fr);
			while ((sCurrentLine = br.readLine()) != null) {			
					final String[] tokens = sCurrentLine.trim().split(" ");
					final String entityName = tokens[0];
					final String categoryName = tokens[1];
					
//					if(entityName.equals("Instrumental_magnitude>")) {
//						System.err.println(entityName);
//					}
					Map<String, Integer> map = entityMap.get(entityName);
					if(map == null) {
						map = new LinkedHashMap<>(categoryPlaceHolder);
						final String returnedCategoryName = getCategoryName(categoryName);
						if(returnedCategoryName==null) {
							//throw new IllegalArgumentException("Category not found.");
							continue;
						}
						map.put(returnedCategoryName, 1);
						entityMap.put(entityName, map);
					}else {
						final String returnedCategoryName = getCategoryName(categoryName);
						if(returnedCategoryName==null) {
							//throw new IllegalArgumentException("Category not found.");
							continue;
						}				
						map.put(returnedCategoryName, map.get(returnedCategoryName)+1);
						entityMap.put(entityName, map);
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return entityMap;
	}
	
	
	private static String getCategoryName(String categoryName) {
		for(final Entry<String, HashSet<String>> entity: categoryMap.entrySet()) {
			if(entity.getValue().contains(categoryName)) {
				return entity.getKey();
			}
		}
		return null;
	}


	private static void createCategoryMap(final int level) {
		final File categoryFolder = new File(CATEGORY_FOLDER);
		String[] cat = categoryFolder.list();
		Arrays.sort(cat);
		for(final String filename : cat){
			final File subCategoryFolder = new File(categoryFolder, filename);
			if(!subCategoryFolder.isDirectory()) {
				continue;
			}
			categoryPlaceHolder.put(filename, 0);
			final String fileName = subCategoryFolder.getName()+"AllLevel_L"+level+"_sort";

			BufferedReader br = null;
			FileReader fr = null;
			try {
				final String file = CATEGORY_FOLDER+"/"+subCategoryFolder.getName()+"/"+fileName;	
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String sCurrentLine;
				br = new BufferedReader(new FileReader(file));
				final HashSet<String> content = new HashSet<>();
				while ((sCurrentLine = br.readLine()) != null) {
					sCurrentLine = sCurrentLine.substring(sCurrentLine.indexOf("Category:"), sCurrentLine.length());
					content.add(sCurrentLine);	
				}
				categoryMap.put(subCategoryFolder.getName(), content);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)
						br.close();
					if (fr != null)
						fr.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

}
