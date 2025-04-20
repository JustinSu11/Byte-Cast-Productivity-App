package com.example.pickitup.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for PDF processing operations
 * @author Maaz Haque
 * @version 1.0
 */
public class PdfUtils {

    /**
     * Extracts text from a PDF file
     * 
     * @param pdfFile The PDF file
     * @return The extracted text
     * @throws IOException If there is an error reading the PDF
     */
    public static String extractTextFromPdf(File pdfFile) throws IOException {
        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
