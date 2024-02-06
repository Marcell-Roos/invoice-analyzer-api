package com.epiuse.invoiceanalyzerapi.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.tomcat.util.http.fileupload.FileUtils;

public class CommandLineService {
	
	// Setting up constants, this is specific to my setup but follows the generic install for tesseract
	final private static String PATH_TO_TESSERACT = "C:\\Program Files\\Tesseract-OCR\\tesseract.exe";
	final private static String PATH_TO_OUTPUT = ".\\outputs\\temp\\out";
	final private static String PATH_TO_IMAGES = ".\\uploads";
	
	/*
	 * Execute tesseract from the command line, 
	 * attempted to use tess4j API but found it was not nearly as consistent.
	 */
	public static ArrayList<String> executeTesseract(String imageFilePath, int threadNum) {
		try {
			Runtime rt = Runtime.getRuntime();
	        Process pr = rt.exec(PATH_TO_TESSERACT + " " + imageFilePath + " " + PATH_TO_OUTPUT+threadNum);
	        
	        int exitVal = pr.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * Output is stored in a textfile, first read the file and load its content
		 * into an ArrayList
		 */
		ArrayList<String> output = readfile(threadNum);
		/*
		 * Now we have the data, we can delete the file.
		 */
		deleteFile(threadNum);
		return output;
		
	}
	
	private static ArrayList<String> readfile(int threadNum) {
		// Standard read file process
		ArrayList<String> data = new ArrayList<>();
		try {
		      File textFile = new File(PATH_TO_OUTPUT+threadNum+".txt");
		      Scanner reader = new Scanner(textFile);
		      while (reader.hasNextLine()) {
		    	  data.add(reader.nextLine());
		      }
		      reader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return data;
	}
	
	private static void deleteFile(int threadNum) { 
	    File outputFile = new File(PATH_TO_OUTPUT+threadNum+".txt"); 
	    if (outputFile.delete()) { 
	    	// debug code
//	      System.out.println("Deleted the file: " + outputFile.getName());
	    } else {
//	      System.out.println("Failed to delete the file.");
	    } 
	  }
	
	// Obtain list of all files in the directory
	public static ArrayList<String> listUploadedFilesFiles() {
		Set<String> fileSet = Stream.of(new File(PATH_TO_IMAGES).listFiles())
	      .filter(file -> !file.isDirectory() && file.getName().toLowerCase().endsWith(".png"))
	      .map(File::getAbsolutePath)
	      .collect(Collectors.toSet());
		ArrayList<String> fileList = new ArrayList<String>();
		for(String file : fileSet) {
			fileList.add(file);
		}
		
	    return fileList;
	}
	
	public static void cleanUploads() {

		try {
			FileUtils.cleanDirectory(new File(PATH_TO_IMAGES));
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
