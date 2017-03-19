package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
	
		//CountLines.main();
		//GenerateLevels_1.main();
		//GenerateLevelTrees_2.main();
		//CountLines.CountUniqueCategories();
		//GenerateCategoryTrees.main();
		//CompareTwoFiles.main();
		//CreateWeightVectorString.main();
		//String pathMainCategories= System.getProperty("user.dir") + File.separator+"MainCategoryFile.txt";
		String path= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\file_All";
		String path_FamEntities= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\FamEntities.txt";
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
//					}
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

	
	

					//List_of_peers_1320–1329>-<Archaeology:0,Architecture:0,Arts:0,Astronomy:0,Aviation:0,Biology:0,Botany:0,Broadcasting:0,Chemistry:0,Cinematography:0,Communication:0,Computer_science:0,Economy:0,Electronic_games:0,Engineering:0,Exploration:0,Geology:0,History:0,Inventions:0,Literature:0,Mathematics:0,Medicine:0,Music:0,Outer_space:0,Paleontology:0,Philosophy:0,Photography:0,Physics:0,Poetry:0,Politics:1,Printing:0,Psychology:0,Transport:0,Women_scientists:0,Zoology:0,>
					
				
					//hsetEntity.add(line.substring(0, line.indexOf(">-<")));
//					hsetEntity.add(strEntityCategory);
					
					//System.out.println("counter: "+counter);
					
					
					
			
		
//		String fS=File.separator;
//		String sP=System.getProperty("user.dir");
//
//		BufferedReader br_Entities = null;
//		String pathEntities= sP+ fS+"FamEntities.txt";
//
//		br_Entities = new BufferedReader(new FileReader(pathEntities));
//		String line_mainCategory = null;
//		String line=null;
//
//		HashSet<String> entities = new HashSet<>();
//		while ((line_mainCategory = br_Entities.readLine()) != null) 
//		{
//			
//		}
//		br_Entities.close();
	}

}
