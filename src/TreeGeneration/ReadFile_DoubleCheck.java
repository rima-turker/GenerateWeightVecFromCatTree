package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.text.html.parser.Entity;

public class ReadFile_DoubleCheck 
{
	public static void main(String[] args) throws IOException 
	{
//		String str_smallFile="EntityList.txt";
//		resultEntityAndPath();
		
		//HashMap<String, HashSet<String>> hmap_categoryMap = createCategoryMap();
		//test_CategoryMap();
		//writeMatchingLinesToFile(str_smallFile, "FilesCleaned.zip", "Results_fromFilesCleaned",true);
		//compareTwoHashSet("EntityList.txt", "test_infoBox.txt");
		createCategoryTree();
	}
	private static void resultEntityAndPath()
	{
		try {
				
			HashMap<String, HashSet<String>> hmap_categoryMap = createCategoryMap();
			for (Entry<String, HashSet<String>> entity_CatAndSubCats : hmap_categoryMap.entrySet()) 
			{
				HashSet<String> hset_catAndValues = entity_CatAndSubCats.getValue();
				for (String str_cat: hset_catAndValues)
				{
					if (str_cat.contains("american_astrophysicists")) 
					{
						System.out.println(str_cat);
					}
				}
			}
			
			String line=null;
			BufferedReader br_MainFile = new BufferedReader(new FileReader(GlobalVariables.path_Local+File.separator+"Results_fromFilesCleaned"));
			ArrayList<String> arrList_mainFile = WriteReadFromFile.readFileToList("Results_fromFilesCleaned");
			File Dir = new File(System.getProperty("user.dir")+File.separator+"ResultFilesBasedOnLevel");
			
			if (!Dir.exists()) 
			{
				Dir.mkdir();
			}
			
			for (Entry<String, HashSet<String>> entity_CatAndSubCats : hmap_categoryMap.entrySet()) {
			
				String str_mainCat = entity_CatAndSubCats.getKey();
				HashSet<String> hset_subCats = entity_CatAndSubCats.getValue();
				
				for (Integer i = 1; i <= GlobalVariables.levelOfTheTree; i++) 
				{
					
					File log = new File(Dir+File.separator+ "entitiesLevelBased"+i.toString());
					
					if (!log.exists()) 
					{
						log.createNewFile();
					}
					
					
					BufferedWriter bufferedWriter = new BufferedWriter( new FileWriter(log,true));
					
					HashSet<String> hset_subCatsBasedonLevel = new HashSet<>();
					for (String str_subCat: hset_subCats)
					{
						if (str_subCat.contains(GlobalVariables.str_depthSeparator+i.toString())) 
						{
							//String str_subcat = str_subCat.replace(">", "").replace("category:", "").toLowerCase();
							hset_subCatsBasedonLevel.add(str_subCat.replace(">", "").replace("category:", "").toLowerCase().substring(0, str_subCat.indexOf(GlobalVariables.str_depthSeparator)));
						}
					}
					
					for (String str_mainFileLine : arrList_mainFile) 
					{
						String str_subcat =str_mainFileLine.split(" ")[1].replace(">", "").replace("category:", "").toLowerCase();
						if (hset_subCatsBasedonLevel.contains(str_subcat)) 
						{
							if (str_subcat.contains("american_astrophysicists")) 
							{
								System.out.println("Yes");
							}
							bufferedWriter.write(str_mainFileLine.split(" ")[0]+","+str_subcat+","+i.toString()+","+str_mainCat);
							bufferedWriter.newLine();
							System.out.println(str_mainFileLine.split(" ")[0]+","+str_subcat+","+i.toString()+","+str_mainCat);
						}
							
					}
//					for (Entry<String, String> entity_entityAndCat : hmap_mainFile.entrySet()) {
//					{
//						
//						}
//					}
					bufferedWriter.close();
				}
				
//				while ((line = br_MainFile.readLine()) != null)
//				{
//					String str_entity = line.split(" ")[0].replace(">", "");
//					for (Integer i = 1; i <= GlobalVariables.levelOfTheTree; i++) 
//					{
//						String str_subcat = line.split(" ")[1].replace(">", "").replace("category:", "").toLowerCase(); 
//						String str_subcatAndLevel = line.split(" ")[1].replace(">", "").replace("category:", "").toLowerCase()+GlobalVariables.str_depthSeparator+i.toString();
//						String str_depth=str_subcatAndLevel.substring(str_subcatAndLevel.indexOf(GlobalVariables.str_depthSeparator)+GlobalVariables.str_depthSeparator.length(), str_subcatAndLevel.length());
//						if (hset_subCats.contains(str_subcatAndLevel))
//						{
//							System.out.println(str_entity+ "," +str_subcat+","+str_depth);
//						}
//					
//					}
//				}
			
			}

			
			br_MainFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/*
	 * O parent
	 * ^
	 * I
	 * O childfile
	 */
	private static HashMap<String, HashSet<String>> createCategoryTree() 
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

	private static HashMap<String, HashSet<String>> createCategoryMap() 
	{
		HashMap<String, HashSet<String>> hmap_categoryMap = new HashMap<>();
		
		try 
		{
			BufferedReader br_MainCategory = new BufferedReader(new FileReader(GlobalVariables.path_MainCategories));
			String line_mainCategory = null;
			String line=null;
			BufferedReader br_MainFile = new BufferedReader(new FileReader(GlobalVariables.path_SkosFile));
			
			
			while ((line_mainCategory = br_MainCategory.readLine()) != null) 
			{
				String str_mainCategoryName = line_mainCategory.replace(">", "").toLowerCase();
				HashSet<String> hset_catAndLevel = new HashSet<>();
				for (Integer i = 1; i <= GlobalVariables.levelOfTheTree ; i++) 
				{
					String str_depth= i.toString();
					
					if (i==1) 
					{
						hset_catAndLevel.add((line_mainCategory.replace(">", "")+GlobalVariables.str_depthSeparator+str_depth).toLowerCase());
					}
					else
					{
						HashSet<String> hsetParents = new HashSet<>();
						
						for (String str_category :hset_catAndLevel) 
						{
							Integer int_parentIndex = i-1;
							if (str_category.contains(GlobalVariables.str_depthSeparator+int_parentIndex.toString())) 
							{
								hsetParents.add(str_category.substring(0, (str_category.indexOf(GlobalVariables.str_depthSeparator))));
								//System.out.println(str_category);
							}
						}
						br_MainFile = new BufferedReader(new FileReader(GlobalVariables.path_SkosFile));
						String lineCategory;

						int count = 0;


						while ((line = br_MainFile.readLine()) != null)
						{
							if (hsetParents.contains(line.split(" ")[1].replace(">", "").toLowerCase()))
							{
								if (!hset_catAndLevel.contains((line.split(" ")[0].replace(">", "").toLowerCase()+GlobalVariables.str_depthSeparator+str_depth))) 
								{
									count++;
								}
								hset_catAndLevel.add(line.split(" ")[0].replace(">", "").toLowerCase()+GlobalVariables.str_depthSeparator+str_depth);
								
								
							}
						}
						
						System.out.println(str_mainCategoryName+GlobalVariables.str_depthSeparator+ i.toString()+" "+count+" "+hset_catAndLevel.size());
						
//						for(String hsetline:hsetParents) 
//						{
//							System.out.println(hsetline);
//						}
						//System.out.println("size"+hsetParents.size());
						//System.out.println("count"+count);
						count=0;

						br_MainFile.close();
					}

				}
				
				hmap_categoryMap.put(str_mainCategoryName, hset_catAndLevel);
				br_MainFile.close();
			}
			br_MainCategory.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hmap_categoryMap;
	}
	
	public static void test_CategoryMap()
	{
		HashSet<String> hset_categories = ReadFileReturnHashSet("MainCategoryFile.txt");
		HashMap<String, HashSet<String>> hmap_catMap = createCategoryMap() ;
		
		for (String str_mainCat : hset_categories)
		{
			HashSet<String> hset_catAndLevel = hmap_catMap.get(str_mainCat);
			
			
			for (Integer i = 1; i <= GlobalVariables.levelOfTheTree; i++) 
			{
				int int_count = 0;
				for (String string : hset_catAndLevel) 
				{
					if (string.contains(GlobalVariables.str_depthSeparator+i.toString())) 
					{
						int_count++;
					}
				}
				//System.out.println(str_mainCat+GlobalVariables.str_depthSeparator+i.toString()+" "+int_count);
			}
		}
		
	}
	
	public static void writeMatchingLinesToFile(String str_smallFile , String str_bigFile, 
												String str_resultFile,boolean isZip)
	{
		File log = new File(System.getProperty("user.dir")+ str_resultFile);
		HashSet<String> hset_smallFile = ReadFileReturnHashSet(str_smallFile);
		try 
		{
			if (log.exists()) 
			{
				log.delete();
			}
			log.createNewFile();
			
			
			FileWriter fileWriter = new FileWriter(log,true);
			BufferedWriter bf_Writer = new BufferedWriter(fileWriter);
			
			if (isZip) 
			{
				ZipFile zf = new ZipFile(GlobalVariables.path_Local+str_bigFile);
				
				Enumeration<? extends ZipEntry> entries = zf.entries();
//				while (entries.hasMoreElements()) 
//				{
//					ZipEntry ze = (ZipEntry) entries.nextElement();
//					System.out.println(ze.getName()+"size:"+ze.getSize());
//					//ze = (ZipEntry) entries.nextElement();
//					long size = ze.getSize();
//				}
				while (entries.hasMoreElements()) 
				{
					ZipEntry ze = (ZipEntry) entries.nextElement();
					//ze = (ZipEntry) entries.nextElement();
					long size = ze.getSize();
					if (size > 0) 
					{
						System.out.println("Reading "+ ze.getName()+"size:"+ze.getSize());
						
						BufferedReader br_bigFile = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
						String line = null;
						
						bf_Writer.write(ze.getName());
						while ((line = br_bigFile.readLine()) != null) 
						{
							if (hset_smallFile.contains(line.toLowerCase().split(" ")[0].replace(">", ""))) 
							{
								bf_Writer.write(line.toLowerCase());
								bf_Writer.newLine();
							}
							
						}
						bf_Writer.write("End of"+ze.getName()+" file.");
						br_bigFile.close();
					
					}
					
				}
			}
			else
			{
				BufferedReader br_bigFile = new BufferedReader(new FileReader(GlobalVariables.path_Local+str_bigFile));
				String line = null;
				while ((line = br_bigFile.readLine()) != null) 
				{
					if (hset_smallFile.contains(line.toLowerCase().split(" ")[0])) 
					{
						bf_Writer.write(line.toLowerCase());
						bf_Writer.newLine();
					}
					
				}
				br_bigFile.close();
			}
			
			bf_Writer.newLine();
			bf_Writer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	public static HashSet<String> ReadFileReturnHashSet(String str_fileName)
	{
		HashSet<String> hset_results = new HashSet<>();
		try 
		{
			BufferedReader br_FamEntities = new BufferedReader(new FileReader(GlobalVariables.path_Local+str_fileName));
			String line = null;
			while ((line = br_FamEntities.readLine()) != null) 
			{
				hset_results.add(line.split(" ")[0].toLowerCase().replace(">", ""));
			}
			br_FamEntities.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hset_results;
	}
}
