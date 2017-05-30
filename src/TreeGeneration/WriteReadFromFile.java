package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class WriteReadFromFile 
{
	

	
	public static Map<String, HashSet<String>> readEntitiesAndCats(String fileName) {
		
		Map<String, HashSet<String>> hmap_groundTruthlist = new LinkedHashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(GlobalVariables.path_Local + fileName));) {

			String str_entity = null;
			String line;
			while ((line = br.readLine()) != null) {
				line = line.toLowerCase();
				HashSet<String> ll_goalSet = new HashSet<>();
				String[] str_split = line.split(",");
				for (int i = 0; i < str_split.length; i++) {
					str_entity = str_split[0];
					if (i != 0) {
						ll_goalSet.add(str_split[i]);
					}
				}

				hmap_groundTruthlist.put(str_entity, ll_goalSet);
			}

			for (Entry<String, HashSet<String>> entry : hmap_groundTruthlist.entrySet()) {
				str_entity = entry.getKey();
				HashSet<String> str_categories = entry.getValue();

				// System.out.println(str_entity+ " "+ str_categories);
			}
		} catch (IOException e) {

			System.out.println(e.getMessage());
			e.printStackTrace();

		}
		return hmap_groundTruthlist;
	}
	public static void writeMapFormattedForExell(Map mp,String str_fileName) {

		File file =(new File(GlobalVariables.path_Local+str_fileName+".csv"));
		try {
			file.createNewFile();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			Iterator it = mp.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				String str_entityAndCatList = pair.getKey().toString().replace(GlobalVariables.str_depthSeparator,",")+pair.getValue().toString().replace("{",",").replace("}","");
				bufferedWriter.write(str_entityAndCatList);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished Writing to a file: "+file.getName()+" "+file.getAbsolutePath());
	}

	public static void writeMapToAFile(Map mp,String str_fileName) {

		File file =(new File(GlobalVariables.path_Local+str_fileName));
		try {
			file.createNewFile();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			Iterator it = mp.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				bufferedWriter.write(pair.getKey()+"="+pair.getValue());
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Finished Writing to a file: "+file.getName()+" "+file.getAbsolutePath());
	}
	public static Map<String, HashMap<String, Double>> readTestSet(String str_fileName)
	{
		Map<String, HashMap<String, Double>> hmap_result = new LinkedHashMap<>();
		String line=null;
		try {
			BufferedReader br_MainFile = new BufferedReader(new FileReader(GlobalVariables.path_Local+File.separator+str_fileName));
			while ((line = br_MainFile.readLine()) != null) 
			{
				line= line.toLowerCase();
				String[] str_split = line.split(",");
				HashMap<String, Double> hmap_catAndValues = new HashMap<>();
				for (int i = 2; i < str_split.length; i++) 
				{
					String str_cat;
					if (!str_split[i].contains("="))
					{
						 str_cat = str_split[i];
					}
					else
					{
						 str_cat = str_split[i].substring(0,str_split[i].indexOf("="));
					}
					
					hmap_catAndValues.put(str_cat,Double.valueOf(str_split[i].substring(str_split[i].indexOf("=")+1,str_split[i].length())));
				}
				hmap_result.put((str_split[0]+GlobalVariables.str_depthSeparator+str_split[1]), hmap_catAndValues);
			}
			br_MainFile.close();
		} 	
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hmap_result;
	}
}
