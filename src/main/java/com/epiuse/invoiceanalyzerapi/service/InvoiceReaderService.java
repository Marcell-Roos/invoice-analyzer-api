package com.epiuse.invoiceanalyzerapi.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.epiuse.invoiceanalyzerapi.model.Invoice;
import com.epiuse.invoiceanalyzerapi.model.InvoiceItem;

@Service
public class InvoiceReaderService {
	 @Autowired
	    private SimpMessageSendingOperations messagingTemplate;
	
	
	
	final private static String ITEM_BLOCK_START_TRIGGER = "DESCRIPTION UNIT PRICE TOTAL"; 
	final private static String ITEM_BLOCK_END_TRIGGER = "Remarks / Payment Instructions:";
	
	private static ArrayList<String> errorFiles = new ArrayList<String>();
	private static int counter = 0;
	private static int iteration = 0;
	private static ArrayList<Invoice> invoices = new ArrayList<>();
	public void readInvoices() {
		LocalDateTime startDate = LocalDateTime.now();
		
		List<String> fileList = loadFileList();
		/*
		 * Counter will be used to keep track of how many
		 * files have been processed to give the user feedback
		 */
		
		/*
		 * Start time and end time are displayed to show user how long it took the application
		 * to complete processing all of their files
		 */
		System.out.println("Start Time: " + startDate.toString());
		for(int i = 0; i <= fileList.size() + 11; i+=10) {
			iteration = i;
			try {
				// Execute Tesseract to transform the image into an ArrayList 
				 Thread thread1 = new Thread(()->{
					 invoices.add(
							 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration),1),fileList.get(iteration))
							 );
					 counter++;
				 });
				 
				 if(iteration <= fileList.size()) {
					 thread1.start();
				 }
				 
				 
				 Thread thread2 = new Thread(()->{
					 invoices.add(
					 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration+1),2),fileList.get(iteration+1))
					 );
					 counter++;
				 });
				 if(iteration+1 <= fileList.size()) {
					 thread2.start();
				 }
				 
				 
				 Thread thread3 = new Thread(()->{
					 invoices.add(
					 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration+2),3),fileList.get(iteration+2))
					 );
					 counter++;
				 });
				 if(iteration + 2 < fileList.size()) {
					 thread3.start();
				 }
				 
				 
				 Thread thread4 = new Thread(()->{
					 invoices.add(
					 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration+3),4),fileList.get(iteration+3))
					 );
					 counter++;
				 });
				 
				 if(iteration + 3 < fileList.size()) {
					 thread4.start();
				 }
				 
				 Thread thread5 = new Thread(()->{
					 invoices.add(
					 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration+4),5),fileList.get(iteration+4))
					 );
					 counter++;
				 });
				 
				 if(iteration + 4 < fileList.size()) {
					 thread5.start();
				 }
				 
				 Thread thread6 = new Thread(()->{
					 invoices.add(
					 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration+5),6),fileList.get(iteration+5))
					 );
					 counter++;
				 });
				 
				 if(iteration + 5 < fileList.size() ) {
					 thread6.start();
				 }
				 
				 Thread thread7 = new Thread(()->{
					 invoices.add(
					 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration+6),7),fileList.get(iteration+6))
					 );
					 counter++;
				 });
				 
				 if(iteration + 6 < fileList.size() ) {
					 thread7.start();
				 }
				 
				 Thread thread8 = new Thread(()->{
					 invoices.add(
					 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration+7),8),fileList.get(iteration+7))
					 );
					 counter++;
				 });
				 
				 if(iteration + 7 < fileList.size()) {
					 thread8.start();
				 }
				 
				 Thread thread9 = new Thread(()->{
					 invoices.add(
					 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration+8),9),fileList.get(iteration+8))
					 );
					 counter++;
				 });
				 
				 if(iteration + 8 < fileList.size() ) {
					 thread9.start();
				 }
				 
				 Thread thread10 = new Thread(()->{
					 invoices.add(
					 analyzeInvoice(CommandLineService.executeTesseract(fileList.get(iteration+9),10),fileList.get(iteration+9))
					 );
					 counter++;
				 });
				 
				 if(iteration + 9 < fileList.size() ) {
					 thread10.start();
				 }
				 
				 thread1.join();
				 thread2.join();
				 thread3.join();
				 thread4.join();
				 thread5.join();
				 thread6.join();
				 thread7.join();
				 thread8.join();
				 thread9.join();
				 thread10.join();
				 double percentageDone = (Double.valueOf(counter)/Double.valueOf(fileList.size()))*100;
				 messagingTemplate.convertAndSend("/topic/public", percentageDone);
				
			} 
			catch(Exception e) {
				
			}
			
		}
		
		
		writeToFiles();
		cleanUp();
		LocalDateTime endDate = LocalDateTime.now();
		System.out.println("Done processing files.");
		System.out.println("End Time: " + endDate.toString());
		
	}
	
	public void writeToFiles() {
		WriterService writerService = new WriterService();
		// Write to excel workbook
		writerService.writeInvoicesToSpreadsheet(invoices);
		// Write Errors to text file to be examined
		writerService.writeErrorFiles(errorFiles);
	}
	
	public void cleanUp() {
		CommandLineService.cleanUploads();
		invoices.clear();
		errorFiles.clear();
		counter = 0;
		iteration = 0;
	}
	
	private static ArrayList<String> loadFileList(){
		return  CommandLineService.listUploadedFilesFiles();
	}
	
	/*
	 * Invoice has 3 main parts we look at
	 * Invoice Number
	 * Start of item breakdown table
	 * End of item breakdown table
	 */
	
	
	private static Invoice analyzeInvoice(ArrayList<String> itemBreakdownList, String currentFile) {
		// Flag to decide if we are in a processing block or not
		boolean inItemBox = false;
		// Current invoice which will be returned with completed data
		Invoice currentInvoice = new Invoice();
		// ArrayList which contains the text for each row in the breakdown table
		ArrayList<String> items = new ArrayList<>();
		// Process each line separately
		for(String line : itemBreakdownList) {
			// Get Invoice Number, it is always in the same row as email
			if(line.contains("Email")) {
				try {
					/*
					 * In the case there is no invoice number or 
					 * Tesseract fails to read an invoice number, 
					 * still process the invoice but set its invoice number to -1
					 * so it is easy to search in the excel workbook.
					 */
					try {
						currentInvoice.setInvoiceNumber(Long.parseLong(line.substring(line.indexOf("Email")+6)));
					} catch(Exception e) {
						currentInvoice.setInvoiceNumber(-1L);
						// Provide feedback
						System.out.println("Error processing: "+ currentFile);
						// Write to error log
						errorFiles.add(currentFile);
					}
					
				} catch(NumberFormatException e) {
					currentInvoice.setInvoiceNumber(-1L);
					e.printStackTrace();
				}
				// Entering Table with Items and cost breakdown
			} else if(line.equalsIgnoreCase(ITEM_BLOCK_START_TRIGGER)){
				inItemBox = true;
			} else if(line.contains(ITEM_BLOCK_END_TRIGGER)) {
				inItemBox = false;
			}
			// add line inside of breakdown
			// note the first item will contain ITEM_BLOCK_START_TRIGGER
			if(inItemBox) {
				items.add(line);
			}
		}
		// First we remove the ITEM_BLOCK_START_TRIGGER from the list.
		try {
			items.remove(0);
		} catch (Exception e) {
			/*
			 * Somehow now items were found in the breakdown,
			 * we still continue processing but report this and
			 * write to error log.
			 */
			System.out.println("No breakdown found: "+ currentFile);
			errorFiles.add(currentFile);
			
		}
		// Now convert invoice items into a list of InvoiceItem
		currentInvoice.setInvoiceItems(analyzeInvoiceItems(items));
		// Calculate and set the invoice total
		BigDecimal invoiceTotal = BigDecimal.ZERO;
		for(InvoiceItem item : currentInvoice.getInvoiceItems()) {
			invoiceTotal = invoiceTotal.add(item.getItemCost());
		}
		currentInvoice.setTotal(invoiceTotal);
		// Finally return the invoice
		return currentInvoice;
	}
	
	private static ArrayList<InvoiceItem> analyzeInvoiceItems(ArrayList<String> items){
		// Initialize list of invoiceItems to be populated
		ArrayList<InvoiceItem> invoiceItems = new ArrayList<>();
		/*
		 * carryOverItem is used for when an items description
		 * runs over multiple lines, if that is the case it will
		 * be carried over until its total is found. 
		 * NOTE
		 * This could break if the description contains a number
		 * at the point where the line break is. This however did 
		 * not happen in my testing.
		 */
		String carryOverItem = "";
		/*
		 * Loop through each item in the breakdown
		 */
		for(String currentItem : items) {
			try {
				/*
				 * Ensure the currentItem has a length greater than 0
				 * to catch any empty lines and skip them
				 */
				if(currentItem.length() > 0) {
					/*
					 * If the last char is an integer we found the total
					 * NOTE
					 * This could break if the description contains a number
					 * at the point where the line break is. This however did 
					 * not happen in my testing
					 */
					Integer.parseInt(currentItem.charAt(currentItem.length()-1)+"");
					/*
					 * Check is there is a carry over item, then add onto it
					 */
					if(!carryOverItem.isEmpty()) {
						carryOverItem += " " + currentItem;
						invoiceItems.add(createInvoiceItem(carryOverItem));
						carryOverItem = "";
						// if not carry over then just create InvoiceItem
					}else {
						invoiceItems.add(createInvoiceItem(currentItem));
					}
				}
				// String did not end in a number which means we need to carry over
			} catch(NumberFormatException e) {
				// Item is split across multiple lines
				carryOverItem += currentItem;
			}
		}
		return invoiceItems;
	}
	
	private static InvoiceItem createInvoiceItem(String line) {
		InvoiceItem invoiceItem = new InvoiceItem();
		String total = "";
		// First reverse string so total is at the start and is easier to work with
		for(int i = line.length() -1; i>0; i--) {
			char c = line.charAt(i);
			try {
				Integer.parseInt(c+"");
				total += c;
			} catch(NumberFormatException e) {
				break;
			}
			
		}
		// Now we have the total as a string but backwards so need to reverse again
		total = new StringBuilder(total).reverse().toString();
		
		// Convert the total to a double then to big decimal
		invoiceItem.setItemCost(BigDecimal.valueOf(Double.parseDouble(total)));
		
		// Format Description
		String description = line.substring(0,line.length()-total.length()-1);

		invoiceItem.setItemDescription(description);
		
		return invoiceItem;
	}
	
	
}
