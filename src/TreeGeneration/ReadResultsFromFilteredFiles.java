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


	public ReadResultsFromFilteredFiles(String str_fileName)
	{
		HashMap<String, HashSet<String>> hmap_categoryMap = new HashMap<>(createCategoryMap());
	}


	private  HashMap<String, HashSet<String>> createCategoryMap() 
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

				for(int i=1 ; i<=7; i++)
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
					}

					hmap_categoryMap.put(str_mainCat+GlobalVariables.str_depthSeparator
							+i, new HashSet<>(content));
					System.out.println(" "+Integer.toString(i)+" child size "+content.size());

				}

			}
			br_MainCategory.close();

		}
		catch (Exception e) {
			// TODO: handle exception
		}

		return hmap_categoryMap;
	}



	private static HashMap<String, HashSet<String>> ReadResults() 
	{
		long startTime = System.nanoTime();

		HashMap<String, HashSet<String>> hmap_categoryMap = new HashMap<>();
		try 
		{
			BufferedReader br_MainCategory = new BufferedReader(new FileReader(GlobalVariables.path_MainCategories));
			String line_mainCategory = null;
			String line=null;
			while ((line_mainCategory = br_MainCategory.readLine()) != null) 
			{
				System.out.println(line_mainCategory);
				String str_mainCategoryName = line_mainCategory.replace(">", "").toLowerCase();
				HashSet<String> hset_allCatsInTree = new HashSet<>();

				for (int i = 1; i <= GlobalVariables.levelOfTheTree ; i++) 
				{
					HashSet<String> hset_tempCats = new HashSet<>();
					String str_depth=Integer.toString(i);
					String str_catAndLevel = str_mainCategoryName+GlobalVariables.str_depthSeparator+str_depth;
					if (i==1) 
					{
						hset_tempCats.add(str_mainCategoryName);
						hset_allCatsInTree.add(str_mainCategoryName);
					}
					else
					{
						int int_childDepth = i-1;

						HashSet<String> hsetParents = new HashSet<>(hmap_categoryMap.get(str_mainCategoryName+GlobalVariables.str_depthSeparator+Integer.toString(int_childDepth)));
						BufferedReader br_MainFile = new BufferedReader(new FileReader(GlobalVariables.path_SkosFile));

						//						while ((line = br_MainFile.readLine()) != null)
						//						{
						//								if (hsetChildCategory.contains(line.split(" ")[1]))
						//								{
						//
						//									hsetParents.add(line.split(" ")[0]);
						//								}
						//						}



						while ((line = br_MainFile.readLine()) != null)
						{
							String str_parent = line.split(" ")[1].replace(">", "").toLowerCase();
							String str_child =line.split(" ")[0].replace(">", "").toLowerCase();
							if (hsetParents.contains(str_parent)&& !hset_allCatsInTree.contains(str_child))
							{
								hset_tempCats.add(str_child);
								hset_allCatsInTree.add(str_child);

							}
						}
						br_MainFile.close();
					}
					hmap_categoryMap.put(str_catAndLevel, hset_tempCats);
					System.out.println(" "+Integer.toString(i)+" child size "+hset_tempCats.size());
				}
			}
			System.out.println();
			br_MainCategory.close();
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
}
