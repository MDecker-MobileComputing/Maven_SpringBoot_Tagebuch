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


/**
 * Diese Service-Klasse enthält die (Business)-Logik für den Export
 * des Tagebuchs eines Nutzers in ein PDF-Dokument.
 */
@Service
public class PdfExportService {

    private static Logger LOG = LoggerFactory.getLogger( PdfExportService.class );


    /**
     * Erzeugt ein PDF-Dokument, das alle Einträge des Tagebuchs eines Nutzers
     * enthält.
     *
     * @param nutzerName Der Name des Nutzers, dessen Tagebuch exportiert werden soll.
     *
     * @return Ein Byte-Array, das den Inhalt des PDF-Dokuments enthält.
     *
     * @throws PdfExportException Falls ein Fehler beim Export auftritt.
     */
    public ByteArrayOutputStream generatePdf( String nutzerName ) throws PdfExportException {

        try {

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final BufferedOutputStream  bos  = new BufferedOutputStream( baos );

            final Document document = new Document();
            final PdfWriter writer = PdfWriter.getInstance( document, bos );

            writer.setViewerPreferences( PdfWriter.PageLayoutSinglePage ); // funktioniert nicht?

            // Metadaten setzen
            document.addTitle   ( "Tagebuchexport für " + nutzerName );
            document.addAuthor  ( nutzerName                         );
            document.addSubject ( "Export Tagebuch"                  );
            document.addKeywords( "Tagebuch, Export, PDF"            );
            document.addCreator ( "Tagebuch-Webapp mit Spring Boot"  );

            document.open();
            final String demoText = "Tagebuchexport für " + nutzerName + " (" + new Date() + ")";
            document.add( new Paragraph( demoText ) );
            document.close();

            bos.flush();
            bos.close();

            LOG.info( "PDF für Nutzer \"{}\" erzeugt.", nutzerName );

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
