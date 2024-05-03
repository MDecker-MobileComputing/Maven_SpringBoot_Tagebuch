package de.eldecker.dhbw.spring.tagebuch.web;

import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

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
        
        String nutzerName = "alice";
        
        final ByteArrayOutputStream pdfStream           = _pdfExportService.generatePdf( nutzerName ); // throws PdfExportException
        final ByteArrayInputStream  pdfInputStream      = new ByteArrayInputStream( pdfStream.toByteArray() );
        final InputStreamResource   inputStreamResource = new InputStreamResource( pdfInputStream );
        
        final String contentDispositionHeader = String.format( "attachment; filename=Tagebuch_%s.pdf", nutzerName );        
        final HttpHeaders headers = new HttpHeaders();
        headers.add( CONTENT_DISPOSITION, contentDispositionHeader );
        
        return ResponseEntity.ok()
                             .headers( headers )
                             .contentType( APPLICATION_PDF )
                             .body( inputStreamResource);        
    }
    
}
