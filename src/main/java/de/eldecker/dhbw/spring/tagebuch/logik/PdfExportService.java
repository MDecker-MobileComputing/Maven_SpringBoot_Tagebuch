package de.eldecker.dhbw.spring.tagebuch.logik;

import static com.lowagie.text.Font.BOLD;
import static com.lowagie.text.FontFactory.HELVETICA;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.eldecker.dhbw.spring.tagebuch.db.Datenbank;
import de.eldecker.dhbw.spring.tagebuch.model.TagebuchEintrag;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;


/**
 * Diese Service-Klasse enthält die (Business)-Logik für den Export
 * des Tagebuchs eines Nutzers in ein PDF-Dokument.
 */
@Service
public class PdfExportService {

    private static Logger LOG = LoggerFactory.getLogger( PdfExportService.class );

    /** Schriftart für Überschrift (groß+fett). */
    private static final Font FONT_TITEL = FontFactory.getFont( HELVETICA, 16, BOLD );

    /** Fette Schrift normaler Größe. */
    private static final Font FONT_FETT = FontFactory.getFont( HELVETICA, 12, BOLD );

    /** Bean für Zugriff auf Datenbank. */
    private final Datenbank _datenbank;


    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public PdfExportService( Datenbank datenbank ) {

        _datenbank = datenbank;
    }


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


            final Paragraph titelAbsatz = new Paragraph( "Tagebuchexport für " + nutzerName, FONT_TITEL );
            titelAbsatz.setSpacingAfter( 20 );
            document.add( titelAbsatz );

            final List<TagebuchEintrag> eintraegeList = _datenbank.getAlleTagebuchEintraegePDF( nutzerName );

            final Paragraph anzahlZeilen = new Paragraph( "Anzahl Einträge: " + eintraegeList.size() );
            anzahlZeilen.setSpacingAfter( 20 );
            document.add( anzahlZeilen );

            eintraegeList.forEach( eintrag -> {

                final Paragraph datumParagraph = new Paragraph( eintrag.datum() + ": ", FONT_FETT );
                final Paragraph textParagraph  = new Paragraph( eintrag.text() );
                textParagraph.setSpacingAfter( 20 );

                document.add( datumParagraph );
                document.add( textParagraph  );
            });

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
