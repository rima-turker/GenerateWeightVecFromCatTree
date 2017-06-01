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
import java.util.Map;
import java.util.Map.Entry;

public class ReadResultsFromFilteredFiles 
{
	private HashSet<String> hset_allCats;

	public ReadResultsFromFilteredFiles(String str_fileName)
	{
		ReadResults(new HashMap<>(createCategoryMap()),str_fileName);
		hset_allCats = new HashSet<>();
	}

	
	public  HashMap<String, HashSet<String>> createCategoryMap() 
	{
		final File categoryFolder = new File("CategoryTrees");

		HashMap<String, HashSet<String>> hmap_categoryMap = new HashMap<>();

		try {

			BufferedReader br_MainCategory = new BufferedReader(new FileReader(GlobalVariables.path_MainCategories));
			String line_mainCategory = null;
			while ((line_mainCategory = br_MainCategory.readLine()) != null) 
			{
				String str_mainCat = line_mainCategory.replace(">", "").toLowerCase();
				System.out.println(str_mainCat);

				for(int i=1 ; i<=GlobalVariables.levelOfTheTree; i++)
				{
					String str_fileName = categoryFolder.getName()+line_mainCategory.replace(">", "").toLowerCase()+GlobalVariables.str_depthSeparator
							+i;
					final String file = categoryFolder.getName()+File.separator+str_fileName;	

					BufferedReader br = new BufferedReader(new FileReader(file));
					String sCurrentLine;
					final HashSet<String> content = new HashSet<>();
					while ((sCurrentLine = br.readLine()) != null) 
					{
						content.add(sCurrentLine);	
						getHset_allCats().add(sCurrentLine);
					}

					hmap_categoryMap.put(str_mainCat+GlobalVariables.str_depthSeparator
							+i, new HashSet<>(content));
					System.out.println(" "+Integer.toString(i)+" child size "+content.size());

				}

			}
			System.out.println(getHset_allCats().size());
			br_MainCategory.close();

		}
		catch (Exception e) {
			// TODO: handle exception
		}

		return hmap_categoryMap;
	}



	private static HashMap<String, HashSet<String>> ReadResults(HashMap<String, HashSet<String>> hmap_categoryMap,String str_fileName) 
	{
		long startTime = System.nanoTime();

		try 
		{
			BufferedReader br_MainFile = new BufferedReader(new FileReader(str_fileName));
			String line_mainCategory = null;
			String line=null;
			while ((line_mainCategory = br_MainFile.readLine()) != null) 
			{
				
				for(Entry<String, HashSet<String>>  entry_CatAndSubs : hmap_categoryMap.entrySet())
				{
					String str_Cat = entry_CatAndSubs.getKey().substring(0,entry_CatAndSubs.getKey().indexOf(GlobalVariables.str_depthSeparator));
					String str_depth = entry_CatAndSubs.getKey().substring(entry_CatAndSubs.getKey().indexOf(GlobalVariables.str_depthSeparator,entry_CatAndSubs.getKey().length()-1));
					HashSet<String> hset_subCats = new HashSet<>(entry_CatAndSubs.getValue());
					int count =0;
					
					for(String str_subcat: hset_subCats)
					{
						if (hset_subCats.contains(line_mainCategory.split(" ")[1])) 
						{
							count++;
							System.out.println();
						}
					}
					System.out.println("Counter "+count);
				
				}
				
				
				
			}
			System.out.println();
			br_MainFile.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long stopTime = System.nanoTime();
		System.out.println("Time" + (stopTime - startTime)/1000000000);
		return hmap_categoryMap;

	}

	public static void writeCategoryTreeToAFile(Map<String, HashSet <String>> hmap, String str_folderName) {


		File folder =(new File(GlobalVariables.path_Local+str_folderName));
		folder.mkdir();

		try {

			for (Entry<String, HashSet <String>> entry: hmap.entrySet()) 
			{
				String str_entName = entry.getKey();
				File file = new File(GlobalVariables.path_Local+str_folderName+ str_entName);
				file.createNewFile();
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));

				HashSet<String> hset_subCats = new HashSet<>(entry.getValue());
				for(String str_subCat: hset_subCats)
				{
					bufferedWriter.write(str_subCat);
					bufferedWriter.newLine();
				}
				bufferedWriter.close();
				System.out.println("Finished Writing to a file: "+file.getName()+" "+file.getAbsolutePath());

			}
		}
		catch (Exception e) 
		{

		}

	}
	
	public HashSet<String> getHset_allCats() {
		return hset_allCats;
	}

}