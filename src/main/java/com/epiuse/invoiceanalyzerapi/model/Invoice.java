package com.epiuse.invoiceanalyzerapi.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Invoice {
	
	private Long invoiceNumber;
	private ArrayList<InvoiceItem> invoiceItems;
	// Use BigDecimal when dealing with currency
	// Although in testing I did not see any values that would not fit in a Long
	// This is best practice in Java
	private BigDecimal total;
	
	public void setInvoiceNumber(Long invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public Long getInvoiceNumber() {
		return this.invoiceNumber;
	}
	
	public void setInvoiceItems(ArrayList<InvoiceItem> invoiceItems) {
		this.invoiceItems = invoiceItems;
	}
	
	public ArrayList<InvoiceItem> getInvoiceItems(){
		return this.invoiceItems;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public BigDecimal getTotal() {
		return this.total;
	}
}
