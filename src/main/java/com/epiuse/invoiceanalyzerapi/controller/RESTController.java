package com.epiuse.invoiceanalyzerapi.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.epiuse.invoiceanalyzerapi.service.InvoiceReaderService;

@RestController
public class RESTController {
	@Autowired
	InvoiceReaderService invoice;
	
	 private final Path root = Paths.get("uploads");
	// Upload Files
	@PostMapping("/upload")
	public ResponseEntity<String> fileUploading(@RequestParam("files[]") MultipartFile[] files) {
	    // Code to save the file to a database or disk
		for(MultipartFile file : files) {
			save(file);
		}
		invoice.readInvoices();
	    return ResponseEntity.ok("Done processing Files");
	}
	
	// Download Files
	@RequestMapping(value = "/download/{file_name}", method = RequestMethod.GET, 
			produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	public FileSystemResource getFile(@PathVariable("file_name") String fileName) {
	    return new FileSystemResource(".\\outputs\\invoices.xlsx"); 
	}
	
	 private void save(MultipartFile file) {
		    try {
		      Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		    } catch (Exception e) {
		      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		    }
		  }
}
