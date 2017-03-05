package TreeGeneration;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

public class Main {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		String path= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\skos_categories_en2015_10.ttl";
		String fileSkos= "skosBroader_Cleaned2015_10";
		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(path);
			br = new BufferedReader(fr);

			String line = null;
			String tempEntityCategory=null;
			br = new BufferedReader(new FileReader(path));
			File log = new File(fileSkos+".txt");
			if(!log.exists())
    	    {
    	        log.createNewFile();
    	    }
		    FileWriter fileWriter = new FileWriter(log, true);
		    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		    int counter=0;
//			while ((line = br.readLine()) != null)
//			{
//				counter++;
//			}
		    while ((line = br.readLine()) != null){
				String[] entityAndCategory = line.split(" ");  
				if (entityAndCategory[2].contains("<http://dbpedia.org/resource/Category:")) 
				{
					
					tempEntityCategory =entityAndCategory[2]+" "+entityAndCategory[2];
					tempEntityCategory = tempEntityCategory.replace("<http://dbpedia.org/resource/", "");
				    bufferedWriter.write(tempEntityCategory);
					bufferedWriter.newLine();
				
					//System.out.println(tempEntityCategory);
					counter++;
				}
			}
			
			bufferedWriter.close();
			System.out.println(counter);

		} catch (IOException e) {

			e.printStackTrace();

		} 

	
	
	}

}
