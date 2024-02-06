package com.epiuse.invoiceanalyzerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.epiuse.invoiceanalyzerapi.service.InvoiceReaderService;

import jakarta.annotation.PostConstruct;


@SpringBootApplication
public class InvoiceanalyzerapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceanalyzerapiApplication.class, args);
		
	}
	
	@PostConstruct
	private void readInvoices() {
//		InvoiceReaderService.readInvoices();
	}

}
