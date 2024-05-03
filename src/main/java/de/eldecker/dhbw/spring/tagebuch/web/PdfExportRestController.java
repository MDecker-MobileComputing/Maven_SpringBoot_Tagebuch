package de.eldecker.dhbw.spring.tagebuch.web;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.eldecker.dhbw.spring.tagebuch.logik.PdfExportException;
import de.eldecker.dhbw.spring.tagebuch.logik.PdfExportService;


@RestController
@RequestMapping( "/api/v1" )
public class PdfExportRestController {

    /** Bean mit Logik für PDF-Erzeugung */
    private final PdfExportService _pdfExportService;
    
    
    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public PdfExportRestController( PdfExportService pdfExportService ) {
        
        _pdfExportService = pdfExportService;
    }

    @GetMapping(value = "/generatePDF", produces = APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePDF() throws PdfExportException {
        
        ByteArrayOutputStream pdfStream = _pdfExportService.generatePdf("alice"); // throws PdfExportException
        ByteArrayInputStream pdfInputStream = new ByteArrayInputStream(pdfStream.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=generated.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfInputStream));        
    }
}
