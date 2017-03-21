package TreeGeneration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ReadResults 
{
	static String path= System.getProperty("user.dir")+ File.separator;
	static String path_FamEntities= System.getProperty("user.dir")+ File.separator+"FamEntities.txt";
	static String path_SubCategoryCount= System.getProperty("user.dir")+ File.separator;
	static int numberOfSub=7;
	private static int[] subCatCountInt;
	static int index=0;
	static String nameAndCat=null;
	static String[] formatResult= new String[100];
	static boolean flag=true; 
	static ArrayList<String> arrListResultsWCatName = new  ArrayList<String>();
	private static final Map<String,ArrayList<Integer>> hmap_subCategoryCount = new HashMap<>();

	private static final StringBuilder finalResult = new StringBuilder();
	private static final Map<String,Map<Integer,List<String>>> finalResultMap = new HashMap<>();
	
	public static void ReadResultFromAllFile(String fileNAme)
	{
		
		path+=fileNAme;
		String fileSkos= "EntitiesAndCategories6";
		BufferedReader br = null;
		FileReader fr = null;
		
		BufferedReader br_FamEntities = null;
		FileReader fr_FamEntities = null;

		try {

			fr = new FileReader(path);
			br = new BufferedReader(fr);
			
			fr_FamEntities = new FileReader(path_FamEntities);
			br_FamEntities = new BufferedReader(fr_FamEntities);
			
			String line = null;
//			String tempEntityCategory=null;
			br = new BufferedReader(new FileReader(path));
			br_FamEntities = new BufferedReader(new FileReader(path_FamEntities));
			
//			File log = new File(fileSkos+".txt");
//			if(!log.exists())
//    	    {
//    	        log.createNewFile();
//    	    }
		    //FileWriter fileWriter = new FileWriter(log, true);
		    //BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		    HashSet<String> hsetEntity = new HashSet<>();
			int counter=0;
			String strEntityCategory=null;
			String str_tempSplitedString;
			String[] str_SplitedString;
			
			String lineCategory;

			int count = 0;

			while ((lineCategory = br_FamEntities.readLine()) != null) 
			{
				hsetEntity.add(lineCategory);
			}

			String str= "pageLinkCleaned_OnlyCategoryFiltered_L5:";
			System.out.println(str);
			while ((line = br.readLine()) != null)
			{

				if (line.contains(str)) 
				{
					line=line.substring(str.length(),line.length());
					
					strEntityCategory=line.substring(0, line.indexOf(">-<Archaeology"));
//					if (strEntityCategory.equals("Bill_Gates")) 
//					{
//						System.out.println(line);
//						break;
//					}=SPLIT("Neil_Armstrong,Outer_space,Archaeology:2,0.00576",",")
					//=SPLIT("Neil_Armstrong,Outer_space,Architecture:2,0.00311",",")

					str_tempSplitedString= line.substring(line.indexOf(">-<Archaeology:"),line.length()-2);
				
					
					
					if (hsetEntity.contains(strEntityCategory)) 
					{
						str_SplitedString = str_tempSplitedString.split(",");
						
						for (int i = 0; i < str_SplitedString.length; i++) 
						{
							if (Integer.parseInt(str_SplitedString[i].substring(str_SplitedString[i].indexOf(":")+1,str_SplitedString[i].length()))>0) 
							{
								strEntityCategory+=str_SplitedString[i];
							}
						}
						System.out.println(strEntityCategory);
						//counter++;
					}
					
				}
			}
				
			System.out.println(counter);

		} catch (IOException e) {

			e.printStackTrace();

		} 
	}
	
	public static void ReadResultFromCVSFile(String fileNAme,String subCatCount)
	{
		ReadSubCategoryNumber(subCatCount); 
		
		path+=fileNAme;
		
		try {

			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			
			String line = null;
			br = new BufferedReader(new FileReader(path));
			

			int depth=numberOfSub;
			
			String strEntityCategory=null;
			String str_tempSplitedString;
			ArrayList<String> arrList_paths = new ArrayList<>();
			
			subCatCountInt = null;
			ArrayList<Integer> numberOfPaths= new ArrayList<>();
			
			
			String lineCategory;
			while ((line = br.readLine()) != null)
			{
				//Entty&Cat
				if (!line.contains("\"")&&line.contains(",")&&!line.contains("\",\"")) 
				{
					finalResult.append("=SPLIT(\"").append(line).append(",");
					finalResultMap.put(line, new HashMap<Integer,List<String>>());					
					
//					while(formatResult[i]!=null)
//					{
//						System.out.println(formatResult[i]);
//						i++;
//					}
					for (int i = 0; i < formatResult.length; i++) 
					{						
						if (formatResult[i]!=null) 
						{
							System.out.println("=SPLIT(\""+formatResult[i]+"\",\",\")");
						}
					}
					//System.out.println("---------------------------");
					//System.out.println("=SPLIT(\""+line);
					nameAndCat=line;
					formatResult= new String[100];
					index=0;
					depth=numberOfSub;
					arrListResultsWCatName.clear();
					
				}
				else if (line.length()<1) 
				{
					//System.out.println();
					//System.out.println("numberOfPaths "+ numberOfPaths+"depth: "+depth+" "+ subCatCountInt[depth-1]);
					MyHeuristic(arrList_paths, depth,line);
					depth--;
					index=0;
					numberOfPaths.clear();
					arrList_paths.clear();
					arrListResultsWCatName.clear();
					
					
				}
				else
				{
					//System.out.println(" "+line);
					if (line.contains(":")) 
					{
						arrListResultsWCatName.add(line);
						arrList_paths.add(line);
						numberOfPaths.add(Integer.parseInt(line.substring(line.indexOf(":")+1, line.length())));
						
						if (formatResult[index]!=null) 
						{
							formatResult[index]=formatResult[index]+","+line+" ";
						}
						else
						{
							formatResult[index]=nameAndCat+line+" ";
						}
						
						index++;
					}
					
				}
			}
		} catch (IOException e) {

			e.printStackTrace();

		} 

	}
	public static void MyHeuristic(ArrayList<String> categoryandPath,int depth, String line)
	{
		double[] result = new double[categoryandPath.size()];
		//numberOfPaths*1000/(den*depth)
		
		int int_tempPath=0;
		String str_category=null;
		for (int i = 0; i < result.length; i++) 
		{
			
			int_tempPath= Integer.parseInt(categoryandPath.get(i).substring(categoryandPath.get(i).indexOf(":")+1, categoryandPath.get(i).length()));
			str_category= categoryandPath.get(i).substring(0,categoryandPath.get(i).indexOf(":"));
			result[i] =(double) ((double)(int_tempPath*1000)/(double)(hmap_subCategoryCount.get(str_category).get(depth-1)*depth));
			
			//formatResult[i]=formatResult[i]+" "+ result[i];
		
			//System.out.printf("%.5f",result[i]);
			DecimalFormat df = new DecimalFormat("0.00000");
			arrListResultsWCatName.set(i, arrListResultsWCatName.get(i)+","+df.format(result[i]));
			formatResult[i]=formatResult[i]+","+ df.format(result[i]);
			//System.out.println();
		}
		//System.out.println();
		
		for (int i = 0; i < arrListResultsWCatName.size(); i++) 
		{
			//System.out.println(arrListResultsWCatName.get(i));
			finalResult.append(arrListResultsWCatName.get(i)).append("\",\",\")\n");
//			System.out.println(formatResult[i]);
			
		}
		//System.out.println();
		
		index=0;
	}
	
	public static void ReadSubCategoryNumber(String subCatCount) 
	{
		String[] subCount = null;
		path_SubCategoryCount+=subCatCount;
		int[] int_subCount;
		BufferedReader brC;
		ArrayList<Integer> arrListTemp;
		try {
			brC = new BufferedReader(new FileReader(path_SubCategoryCount));
			String lineCategory=null;
			
			while ((lineCategory = brC.readLine()) != null)
			{
				
				arrListTemp= new ArrayList<>();
				//System.out.println(lineCategory);
				subCount= (lineCategory.substring(lineCategory.indexOf(":,")+2, lineCategory.length()).split(","));
				int_subCount= Arrays.stream(subCount).mapToInt(Integer::parseInt).toArray();

				for (int i = 0; i < int_subCount.length; i++)
				{
					arrListTemp.add(int_subCount[i]);
				}
				
				hmap_subCategoryCount.put(lineCategory.substring(0, lineCategory.indexOf(":")),arrListTemp);
				
				//arrListTemp.clear();
			}
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
