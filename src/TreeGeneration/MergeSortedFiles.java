package TreeGeneration;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class MergeSortedFiles {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	static int level=5;
	static int categoryNCount=35;
	public static void main(String[] args) throws IOException
	{
		
		BufferedReader br_MainCategory = null;
		FileReader fr = null;

		//File log = new File(categoryName+"L"+Integer.parseInt(number)+1);
		
		String pathMainCategories= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\MainCategoryFile.txt";
		
		br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
		String line_list = null;
		String line_mainCategory = null;
		String line=null;
		File[] files = null;
		Integer indexCategory=0;
		String categoryName=null;
		
		for (Integer i = 0; i < level; i++) 
		{
			files=new File[categoryNCount];
			br_MainCategory = new BufferedReader(new FileReader(pathMainCategories));
			while ((line_mainCategory = br_MainCategory.readLine()) != null) 
	        {
				categoryName = line_mainCategory.replace("http://dbpedia.org/resource/Category:", "");
	       
				String pathMergeFile= "C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\"+categoryName+
									"AllLevel_L"+i.toString()+"_sort";
				
				files[indexCategory]=new File(pathMergeFile);
				indexCategory++;
				
	        }
			br_MainCategory.close();
			indexCategory=0;
			File mergedFile = new File("C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\FilesForComparison\\AllCategoryAllLevel_L"
								+i.toString());
				mergedFile.createNewFile();
				mergeFiles(files,mergedFile);
			
		}
		br_MainCategory.close();
	}
	
	public static void mergeFiles(File[] files, File mergedFile) {
		 
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(mergedFile, true);
			 out = new BufferedWriter(fstream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
 
		for (File f : files) {
			System.out.println("merging: " + f.getName());
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));
 
				String aLine;
				while ((aLine = in.readLine()) != null) {
					out.write(aLine);
					out.newLine();
				}
 
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
 
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
	}
	public static void copyFile(File sourceFile, File destFile) throws IOException {
	     
		
//		File srcFile= new File("C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\"+
//				categoryName+"_L0");
//		
//		File destFile= new File("C:\\Users\\Rima\\Desktop\\JavaProjects\\GenerateTree\\CategoryFiles\\"+categoryName+"\\"+
//				categoryName+"AllLevel_L0_sort");
		
//		copyFile(srcFile, destFile);
		if(!destFile.exists()) {
	      destFile.createNewFile();
	     }

	     FileChannel source = null;
	     FileChannel destination = null;
	     try {
	      source = new RandomAccessFile(sourceFile,"rw").getChannel();
	      destination = new RandomAccessFile(destFile,"rw").getChannel();

	      long position = 0;
	      long count    = source.size();

	      source.transferTo(position, count, destination);
	     }
	     finally {
	      if(source != null) {
	       source.close();
	      }
	      if(destination != null) {
	       destination.close();
	      }
	    }
	 }

}
