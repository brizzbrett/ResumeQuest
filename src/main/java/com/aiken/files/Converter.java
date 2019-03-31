package com.aiken.files;

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.web.multipart.MultipartFile;

import com.aiken.exceptions.WrongFileTypeException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

public class Converter 
{
	public static String docXConverter(MultipartFile file, String filename) 
	{
		
		if (!filename.contains(".docx")) 
		{
			throw new WrongFileTypeException("File must be of type docx - " + filename);
		}

		try 
		{
			XWPFDocument docx = new XWPFDocument(file.getInputStream());		
			XWPFWordExtractor extractor = new XWPFWordExtractor(docx);
			
			String extractedDoc = extractor.getText();

			extractor.close();
			
			return extractedDoc;
		}
		catch (IOException e) 
		{		
			e.printStackTrace();
			return null;
		} 

	}
	public static String pdfConverter(MultipartFile file, String filename) 
	{
		
		if (!filename.contains(".pdf")) 
		{
			throw new WrongFileTypeException("File must be of type pdf - " + filename);
		}
		
		try
		{			
			PdfReader pdf = new PdfReader(file.getInputStream());
			String extractor = PdfTextExtractor.getTextFromPage(pdf, 1);
			
			return extractor;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}	
}
