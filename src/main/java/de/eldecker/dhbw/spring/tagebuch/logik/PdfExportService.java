package de.eldecker.dhbw.spring.tagebuch.logik;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;


@Service
public class PdfExportService {

    private static Logger LOG = LoggerFactory.getLogger( PdfExportService.class );
    
    public ByteArrayOutputStream generatePdf( String nutzerName ) throws PdfExportException {
                
        try {
            
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BufferedOutputStream  bos  = new BufferedOutputStream( baos );
        
            final Document document = new Document();
            PdfWriter.getInstance( document, bos );
        
            document.open();
            document.add( new Paragraph("Tagebuchexport für " + nutzerName + " (" + new Date() + ")" ) );
            document.close();
        
            bos.flush();
            bos.close();
        
            return baos;            
        }
        catch ( IOException | DocumentException ex ) {

            final String fehlerText = "Fehler bei PDF-Export von Tagebuch für Nutzer \"" + 
                                      nutzerName + "\" aufgetreten: " + ex.getMessage(); 
            
            LOG.error( fehlerText, ex );
            throw new PdfExportException( fehlerText , ex );
        }
    } 
    
}
