package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CompareTwoFiles {

	/**
	 * @param args
	 * @throws IOException 
	 */
	//AllCategoryAllLevel_L0_sort
	static String pathTree= System.getProperty("user.dir")+File.separator+"CategoryTrees"+File.separator+"CompleteTree_L";
	static String pathZipFile= System.getProperty("user.dir")+File.separator+"PageInfoFile.zip";

	public static void main() throws IOException 
	{
		Date start = new Date();
		try {
			ZipFile zf = new ZipFile(pathZipFile);
			Enumeration<? extends ZipEntry> entries = zf.entries();

			while (entries.hasMoreElements()) 
			{
				ZipEntry ze = (ZipEntry) entries.nextElement();
				ze = (ZipEntry) entries.nextElement();
				long size = ze.getSize();
				if (size > 0) 
				{
					BufferedReader br_mainFile = new BufferedReader(
							new InputStreamReader(zf.getInputStream(ze)));

					for (Integer i = 0; i < GlobalVariables.levelOfTheTree; i++) 
					{
						pathTree= System.getProperty("user.dir")+File.separator+"CategoryTrees"+File.separator+"CompleteTree_L";
						pathTree=pathTree+i.toString();


						File log=null;
						log = new File(System.getProperty("user.dir")+File.separator+"CategoryTreeFilteredMainFiles"+File.separator+ze.getName()+
								"_L"+i.toString());

						if (log.exists()) 
						{
							log.delete();
						}
						log.createNewFile();

						BufferedWriter bufferedWriter = null;
						FileWriter fileWriter;

						fileWriter = new FileWriter(log, false);
						bufferedWriter = new BufferedWriter(fileWriter);

						BufferedReader br_CategoryLevel = new BufferedReader(new FileReader(pathTree));
						String lineMain;
						String lineCategory;

						int count = 0;

						HashSet<String> hsetCategory = new HashSet<>();
						HashSet<String> hsetResult = new HashSet<>();

						while ((lineCategory = br_CategoryLevel.readLine()) != null) 
						{
							//String onlyCategoryName=lineCategory.substring( lineCategory.indexOf("Category:"), lineCategory.length());
							hsetCategory.add(lineCategory);
						}


						br_mainFile = new BufferedReader(
								new InputStreamReader(zf.getInputStream(ze)));


						while ((lineMain = br_mainFile.readLine()) != null) 
						{
							count++;
							if (hsetCategory.contains(lineMain.split(" ")[1]))
							{
								//									//System.out.println(lineMain);
								//	        					    bufferedWriter.write(lineMain);
								//	        					    bufferedWriter.newLine();
								hsetResult.add(lineMain);
							}
							//System.out.println(lineMain);
						}
						
						for(String line:hsetResult) 
						{
							bufferedWriter.write(line);
							bufferedWriter.newLine();
						}
						System.out.println("size"+hsetResult.size());
						System.out.println("count"+count);
						count=0;
						
						br_mainFile.close();
						br_CategoryLevel.close();
						bufferedWriter.close();
					}
					
				}

			} 
	}
	catch (IOException e) {
		e.printStackTrace();
	}
	Date end = new Date();
	System.out.println ("MainLoop: "+(end.getTime() - start.getTime())/1000 + " seconds");

}	


//    while ((lineCategory = br_CategoryLevel.readLine()) != null) 
//    {
//    	br_mainFile = new BufferedReader(
//	            new InputStreamReader(zf.getInputStream(ze)));
//    	String onlyCategoryName=lineCategory.substring( lineCategory.indexOf("Category:"), lineCategory.length());
//    	
//    	while ((lineMain = br_mainFile.readLine()) != null) 
//        {
//    		count++;
//    		if (lineMain.contains(onlyCategoryName))
//    		{
//				//System.out.println(lineMain);
//			    bufferedWriter.write(lineMain);
//			    bufferedWriter.newLine();
//			}
//    		//System.out.println(lineMain);
//        }
//    //	System.out.println("count"+count);
//    	count=0;
//    //	System.out.println(lineCategory);
//    	br_mainFile.close();
//    }

}



