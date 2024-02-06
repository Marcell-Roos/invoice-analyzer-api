package com.epiuse.invoiceanalyzerapi.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.epiuse.invoiceanalyzerapi.model.Invoice;
import com.epiuse.invoiceanalyzerapi.model.InvoiceItem;


/*
 * There is a lot of code here but it is very basic and mainly deals with styling
 * the spreadsheet cells
 */
public class WriterService {
	
	final private static String PATH_TO_OUTPUT = ".\\outputs\\temp\\errorfiles.txt";
	
	public static void WriteInvoicesToSpreadsheet(ArrayList<Invoice> invoices) {
		
		 
		// workbook object 
        XSSFWorkbook workbook = new XSSFWorkbook(); 
        // spreadsheet object 
        XSSFSheet spreadsheet 
            = workbook.createSheet("Invoices"); 
        // creating a row object 
        XSSFRow row; 
        // writing the data into the sheets... 
        int rowid = 0; 
        int cellid = 0; 
        // Title
        row = spreadsheet.createRow(rowid++);
        XSSFCell workbookTitle = row.createCell(cellid++); 
        workbookTitle.setCellValue("EPI-USE Generated Invoices"); 
        workbookTitle.setCellStyle(applyTitleText(workbook));
        
        // Spacer
        row = spreadsheet.createRow(rowid++);
        for (Invoice invoice : invoices) { 
        	
        	// Invoice No.
        	cellid = 0; 
        	row = spreadsheet.createRow(rowid++); 
            XSSFCell invoiceTitle = row.createCell(cellid++); 
            invoiceTitle.setCellValue("Invoice No. "); 
            invoiceTitle.setCellStyle(applyBoldText(workbook));
            XSSFCell invoiceNumber = row.createCell(cellid++); 
            invoiceNumber.setCellValue(invoice.getInvoiceNumber()); 

            
            // Breakdown Table Title
            cellid = 0; 
            row = spreadsheet.createRow(rowid++);
            // Description
            XSSFCell breakdownDescriptionTitle = row.createCell(cellid++); 
            breakdownDescriptionTitle.setCellValue("Description");
            breakdownDescriptionTitle.setCellStyle(applyBordersBoldText(workbook));
            // Cost
            XSSFCell breakdownCostTitle = row.createCell(cellid++); 
            breakdownCostTitle.setCellValue("Cost");
            breakdownCostTitle.setCellStyle(applyBordersBoldText(workbook));
            
            for(InvoiceItem item : invoice.getInvoiceItems()) {
            	cellid = 0;
            	row = spreadsheet.createRow(rowid++);
            	 // Description
                XSSFCell breakdownDescription = row.createCell(cellid++); 
                breakdownDescription.setCellValue(item.getItemDescrition());
                breakdownDescription.setCellStyle(applyBorders(workbook));
                // Cost
                XSSFCell breakdownCost = row.createCell(cellid++); 
                breakdownCost.setCellValue(item.getItemCost().toString());
                breakdownCost.setCellStyle(applyBorders(workbook));
            }
            // Total Cost
            cellid = 0;
        	row = spreadsheet.createRow(rowid++);
        	
        	XSSFCell totalTitle = row.createCell(cellid++); 
        	totalTitle.setCellValue("Total"); 
        	totalTitle.setCellStyle(applyBoldText(workbook));
            
            XSSFCell totalAmount = row.createCell(cellid++); 
            totalAmount.setCellValue(invoice.getTotal().toString()); 
            
            // Add Empty row spacer
            row = spreadsheet.createRow(rowid++);
        } 
  
        // .xlsx is the format for Excel Sheets... 
        // writing the workbook into the file... 
        FileOutputStream out;
		try {
			out = new FileOutputStream( 
			    new File(".\\outputs\\invoices.xlsx"));
			workbook.write(out); 
	        out.close(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private static XSSFCellStyle applyTitleText(XSSFWorkbook workbook) {
		/* Get access to HSSFCellStyle */
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font= workbook.createFont();
        font.setBold(true);
        font.setFontHeight(28);
        style.setFont(font);
		return style;
	}
	
	private static XSSFCellStyle applyBoldText(XSSFWorkbook workbook) {
		/* Get access to HSSFCellStyle */
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font= workbook.createFont();
        font.setBold(true);
        style.setFont(font);
		return style;
	}
	
	private static XSSFCellStyle applyBorders(XSSFWorkbook workbook) {
		/* Get access to HSSFCellStyle */
        XSSFCellStyle style = workbook.createCellStyle();
        
        /* First, let us draw a thick border so that the color is visible */            
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);             
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);            
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);              
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        
        /* We will use IndexedColors to specify colors to the border */
        /* bottom border color */
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        /* Top border color */
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        /* Left border color */
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        /* Right border color */
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}
	
	private static XSSFCellStyle applyBordersBoldText(XSSFWorkbook workbook) {
		/* Get access to HSSFCellStyle */
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font= workbook.createFont();
        font.setBold(true);
        
        /* First, let us draw a thick border so that the color is visible */            
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);             
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);            
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);              
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        
        /* We will use IndexedColors to specify colors to the border */
        /* bottom border color */
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        /* Top border color */
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        /* Left border color */
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        /* Right border color */
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        
        style.setFont(font);
		return style;
	}
	
	public static void writeErrorFiles(ArrayList<String> errorFiles) {
		try(FileWriter fstream = 
				new FileWriter (PATH_TO_OUTPUT);
				BufferedWriter writer = new BufferedWriter(fstream);){
		   for(String file : errorFiles){
			   writer.write(file);
			   writer.newLine();
		   }
       }catch(Exception e){
          e.printStackTrace();
	    }   
			  
	}
}
