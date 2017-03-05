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
	static String pathCategory= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\FilesForComparison\\AllCategoryAllLevel_L";
	static String pathZipFile= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\PageInfoFile.zip";

	public static void main(String[] args) throws IOException 
	{
		Date start = new Date();
		try {
			ZipFile zf = new ZipFile(pathZipFile);
			Enumeration<? extends ZipEntry> entries = zf.entries();

			while (entries.hasMoreElements()) 
			{
				ZipEntry ze = (ZipEntry) entries.nextElement();
				//ze = (ZipEntry) entries.nextElement();
				long size = ze.getSize();
				if (size > 0) 
				{
					BufferedReader br_mainFile = new BufferedReader(
							new InputStreamReader(zf.getInputStream(ze)));

					for (Integer i = 0; i < 5; i++) 
					{
						pathCategory= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\FilesForComparison\\AllCategoryAllLevel_L";
						pathCategory=pathCategory+i.toString()+"_sort";


						File log=null;
						log = new File("C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\ResultFiles\\"+ze.getName()+
								"_L"+i.toString()+"_New");

						if (log.exists()) 
						{
							log.delete();
						}
						log.createNewFile();

						BufferedWriter bufferedWriter = null;
						FileWriter fileWriter;

						fileWriter = new FileWriter(log, true);
						bufferedWriter = new BufferedWriter(fileWriter);

						BufferedReader br_CategoryLevel = new BufferedReader(new FileReader(pathCategory));
						String lineMain;
						String lineCategory;

						int count = 0;

						HashSet<String> hsetCategory = new HashSet<>();
						HashSet<String> hsetResult = new HashSet<>();

						while ((lineCategory = br_CategoryLevel.readLine()) != null) 
						{
							String onlyCategoryName=lineCategory.substring( lineCategory.indexOf("Category:"), lineCategory.length());
							hsetCategory.add(onlyCategoryName);
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





//		for (Integer i = 0; i < 5; i++) 
//		{
//			pathCombineLevels= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\" +
//								"	";
//		}

//		BufferedReader reader1 = new BufferedReader(new FileReader("C:\\file1.txt"));
//         
//        BufferedReader reader2 = new BufferedReader(new FileReader("C:\\file2.txt"));
//         
//        String line1 = reader1.readLine();
//         
//        String line2 = reader2.readLine();
//         
//        boolean areEqual = true;
//         
//        int lineNum = 1;
//         
//        while (line1 != null || line2 != null)
//        {
//            if(line1 == null || line2 == null)
//            {
//                areEqual = false;
//                 
//                break;
//            }
//            else if(! line1.equalsIgnoreCase(line2))
//            {
//                areEqual = false;
//                 
//                break;
//            }
//             
//            line1 = reader1.readLine();
//             
//            line2 = reader2.readLine();
//             
//            lineNum++;
//        }
//         
//        if(areEqual)
//        {
//            System.out.println("Two files have same content.");
//        }
//        else
//        {
//            System.out.println("Two files have different content. They differ at line "+lineNum);
//             
//            System.out.println("File1 has "+line1+" and File2 has "+line2+" at line "+lineNum);
//        }
//         
//        reader1.close();
//         
//        reader2.close();
}



