package TreeGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

public class CategoryToCategoryPath 
{
	final static int levelOfTheTree=GlobalVariables.levelOfTheTree+9;
	final static Map<String,Integer> lhmap_CatToCatPath= new LinkedHashMap<>();
	
	public static  void main() throws IOException 
	{
		String fileSeparator=File.separator;
		String sysProperty=System.getProperty("user.dir");
		String str_catcatSep="-";
		
		HashSet<String> hset_mainCategories = new LinkedHashSet<>();
		HashSet<String> hset_categoryTree = new LinkedHashSet<>();
		HashSet<String> hset_mainFile = new HashSet<>();
		
		BufferedReader br_MainCategory = null;
		BufferedReader br_MainFile = null;
		//File log = new File(categoryName+"L"+Integer.parseInt(number)+1);
		String pathMainCategories= sysProperty+ fileSeparator+"MainCategoryFile.txt";

		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		BufferedReader br_MainCategoryS =  new BufferedReader(new FileReader(pathMainCategories));
		
		String line_mainCategory = null;
		String line_mainCategoryS = null;
		
		while ((line_mainCategory = br_MainCategory.readLine()) != null) 
		{
			String str_categoryName = line_mainCategory.replace(">", "");
			
			while ((line_mainCategoryS = br_MainCategoryS.readLine()) != null) 
			{
				lhmap_CatToCatPath.put(str_categoryName+str_catcatSep+line_mainCategoryS.replace(">", ""), 0);
			}
			br_MainCategoryS =  new BufferedReader(new FileReader(pathMainCategories));
		}
		br_MainCategory.close();
		
		
		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		while ((line_mainCategory = br_MainCategory.readLine()) != null) 
		{
			hset_mainCategories.add(line_mainCategory.replace(">", ""));
		}
		br_MainCategory.close();
		//System.out.println("hset_mainCategories"+ hset_mainCategories.size());
		
		line_mainCategory = null;
		br_MainFile = new BufferedReader(new FileReader(sysProperty+ fileSeparator+ "skos_broader_CatCleaned_sort.txt"));
		
		String str_line= null;
		while ((str_line = br_MainFile.readLine()) != null)
		{
			hset_mainFile.add(str_line);
		}
		//System.out.println("hset_mainFile"+ hset_mainFile.size());
		
		for (String str_mainCategory:hset_mainCategories) 
		{
			
			//System.out.println("str_mainCategory"+ hset_mainCategories.size());
			
			String categoryName = str_mainCategory.replace(">", "");
			//String categoryName="Psychology";
			
			System.out.println("-------------"+categoryName+"-------------");
			HashSet<String> hsetChildCategory = new HashSet<>();
			HashSet<String> hsetParents = new HashSet<>();
			
			hsetChildCategory.add(categoryName);
			hset_categoryTree.add(categoryName);
			for (Integer i = 1; i <= levelOfTheTree ; i++) 
			{
				int count = 0;
				
				for ( String line:hset_mainFile) 
				{
					if (hsetChildCategory.contains(line.split(" ")[1].replace(">", "")))
					{
						String str_parent = line.split(" ")[0].replace(">", "");
						if (!hset_categoryTree.contains(str_parent)) 
						{
							
							hsetParents.add(str_parent);
							hset_categoryTree.add(str_parent);
							if (hset_mainCategories.contains(str_parent)) 
							{
								//System.out.println("Inside main loop");
//								if (categoryName.equals("Arts")&&str_parent.equals("Electronic_games")) 
//								{
//									System.out.println("Yes");
//								}
									if (lhmap_CatToCatPath.get(categoryName+str_catcatSep+str_parent)==0)
									{
//										if (i==1) 
//										{
//											System.out.println("i=0");
//										}
										lhmap_CatToCatPath.put(str_parent+str_catcatSep+categoryName, i);
										
									}
								
								
							}
//							else
//								System.out.println(line);

						}
						
					}
				}

				System.out.println("size"+hsetParents.size());
				hsetChildCategory.clear();
				hsetChildCategory.addAll(hsetParents);
				hsetParents.clear();
				
				//System.out.println("size"+hset_categoryTree.size());
				
				//System.out.println("count"+count);
				count=0;
				
			}
			hset_categoryTree.clear();
		}
		
		for(Entry<String, Integer> entry: lhmap_CatToCatPath.entrySet())
		{
			System.out.println(entry.getKey()+" "+entry.getValue());
		}
	}
}