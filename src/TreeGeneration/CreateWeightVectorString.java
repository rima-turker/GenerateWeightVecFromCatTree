package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CreateWeightVectorString {

	static String pathResultFile=System.getProperty("user.dir")+ File.separator+"CategoryTreeFilteredMainFiles" +
			File.separator+"pageLinkCleaned_OnlyCategoryFiltered_L";
	

	
	private static String CATEGORY_FOLDER = System.getProperty("user.dir")+ File.separator+"CategoryFiles";  
	private static String VECTOR_FOLDER = System.getProperty("user.dir")+ File.separator+"Vectors"; 

	private static Map<String,Integer> categoryPlaceHolder = new LinkedHashMap<>();
	private static final Map<String,HashSet<String>> categoryMap = new HashMap<>();
	public static void main() throws FileNotFoundException 
	{	
		for (int i = 0; i < GlobalVariables.levelOfTheTree; i++) 
		{
			createCategoryMap(i);
			final Map<String,int[]> entityMap = generateWeightVector(i);
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
			Map<String, int[]> entityMap, int i) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(VECTOR_FOLDER+"/WeightVector_pageLink_Str_L"+i,true);
			bw = new BufferedWriter(fw);
			
			for(final Entry<String, int[]> entity: entityMap.entrySet()) {
				final StringBuilder content = new StringBuilder();
				content.append(entity.getKey()).append("-").append("<");
				
//				for(int j=0;j<categoryPlaceHolder.size();j++) {
//					content.append(String.valueOf(entity.getValue()[j])).append(",");
//				}
				
				//String
				for(String cate: categoryPlaceHolder.keySet()) {
					int j = categoryPlaceHolder.get(cate);
					content.append(cate).append(":").append(String.valueOf(entity.getValue()[j])).append(",");
				}
				
				content.append(">").append("\n");
				bw.write(content.toString());
			}
			
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


	private static Map<String,int[]> generateWeightVector(int i) {
		final Map<String,int[]> entityMap = new LinkedHashMap<>();
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
					int map[] = entityMap.get(entityName);
					if(map == null) {
						map = new int[categoryPlaceHolder.size()];
						final List<String> returnedCategoryName = getCategoryName(categoryName);
						if(returnedCategoryName.isEmpty()) {
							//throw new IllegalArgumentException("Category not found.");
							continue;
						}
						for(String mainCategory: returnedCategoryName){
							map[categoryPlaceHolder.get(mainCategory)] =  1;
						}
						entityMap.put(entityName, map);
					}else {
						final List<String> returnedCategoryName = getCategoryName(categoryName);
						if(returnedCategoryName.isEmpty()) {
							//throw new IllegalArgumentException("Category not found.");
							continue;
						}				
						for(String mainCategory: returnedCategoryName){
							map[categoryPlaceHolder.get(mainCategory)]+=1;
						}
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
	
	
	private static List<String> getCategoryName(String categoryName) {
		final List<String> mainCategoryNames = new ArrayList<>();
		for(final Entry<String, HashSet<String>> entity: categoryMap.entrySet()) {
			if(entity.getValue().contains(categoryName)) {
				mainCategoryNames.add(entity.getKey());				
			}
		}
		return mainCategoryNames;
	}


	private static void createCategoryMap(final int level) {
		final File categoryFolder = new File(CATEGORY_FOLDER);
		String[] cat = categoryFolder.list();
		Arrays.sort(cat);
		int i=0;
		
		for(final String filename : cat){
			final File subCategoryFolder = new File(categoryFolder, filename);
			if(!subCategoryFolder.isDirectory()) {
				continue;
			}
			categoryPlaceHolder.put(filename, i);
			i++;
			final String fileName = subCategoryFolder.getName()+"Tree_L"+level;
			
			BufferedReader br = null;
			FileReader fr = null;
			try {
				final String file = CATEGORY_FOLDER+"/"+subCategoryFolder.getName()+File.separator+fileName;	
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String sCurrentLine;
				br = new BufferedReader(new FileReader(file));
				final HashSet<String> content = new HashSet<>();
				while ((sCurrentLine = br.readLine()) != null) {
					//sCurrentLine = sCurrentLine.substring(sCurrentLine.indexOf("Category:"), sCurrentLine.length());
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
