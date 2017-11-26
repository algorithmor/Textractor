package com.textractor.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfDocumentContentParser;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;

@RestController("converter")
@RequestMapping("/convert")
public class ConversionService {
	
	@GetMapping("/")
	public String test() {
		return "RUNNING";
	}
	
	@PostMapping("/pdfToText")
	public  List<String> pdfToText(@RequestParam("file") MultipartFile file) {
		InputStream stream;
	    List<String> result = new ArrayList<String>();
	    
		try {
			stream = file.getInputStream();
			InputStream inputStream =  new BufferedInputStream(stream);		
	        PdfReader reader = new PdfReader(inputStream);
	        PdfDocument document = new PdfDocument(reader);  
	        PdfDocumentContentParser parser = new PdfDocumentContentParser(document);
	        ITextExtractionStrategy strategy;
	        for (int i = 1; i <= document.getNumberOfPages(); i++) {
	            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
	            result.add(strategy.getResultantText());
	        }
	        reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
