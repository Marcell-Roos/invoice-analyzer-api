package com.epiuse.invoiceanalyzerapi.model;

import java.math.BigDecimal;

public class InvoiceItem {
	
	private String itemDescription;
	// Use BigDecimal when dealing with currency
	// Although in testing I did not see any values that would not fit in a Long
	// This is best practice in Java
	private BigDecimal itemCost;
	
	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}
	
	public String getItemDescrition() {
		return this.itemDescription;
	}
	
	public void setItemCost(BigDecimal itemCost) {
		this.itemCost = itemCost;
	}
	
	public BigDecimal getItemCost() {
		return this.itemCost;
	}

}
