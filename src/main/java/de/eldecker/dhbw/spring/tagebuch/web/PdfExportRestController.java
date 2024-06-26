package de.eldecker.dhbw.spring.tagebuch.web;

import static org.springframework.http.MediaType.APPLICATION_PDF;
import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.eldecker.dhbw.spring.tagebuch.logik.PdfExportException;
import de.eldecker.dhbw.spring.tagebuch.logik.PdfExportService;
import de.eldecker.dhbw.spring.tagebuch.helferlein.DatumsFormatierer;


/**
 * REST-Controller für PDF-Export aller Tagebucheinträge eines Nutzers.
 */
@RestController
@RequestMapping( "/api/v1" )
public class PdfExportRestController {

    private static Logger LOG = LoggerFactory.getLogger( PdfExportRestController.class );

    /** Bean mit Logik für PDF-Erzeugung. */
    private final PdfExportService _pdfExportService;

    /** Bean für Formatierung von Datum-/Uhrzeit-Strings. */
    private final DatumsFormatierer _datumsFormatierer;


    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public PdfExportRestController( PdfExportService pdfExportService,
                                    DatumsFormatierer datumsFormatierer ) {

        _pdfExportService  = pdfExportService;
        _datumsFormatierer = datumsFormatierer;
    }


    /**
     * REST-Endpunkt liefert PDF-Dokument mit allen Tagebucheinträgen des angemeldeten Nutzers
     * zurück. Der Browser soll das PDF-Dokument herunterladen (und nicht versuchen, es anzuzeigen).
     *
     * @param authentication Authentifizierungs-Objekt des angemeldeten Nutzers.
     *
     * @return HTTP-Response mit PDF-Dokument als Body.
     */
    @GetMapping(value = "/generatePDF", produces = APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePDF( Authentication authentication ) throws PdfExportException {

        final String nutzerName = authentication.getName();
        LOG.info( "PDF-Export angefordert für Nutzer \"{}\".", nutzerName );

        final ByteArrayOutputStream pdfStream           = _pdfExportService.erzeugePdf( nutzerName ); // throws PdfExportException
        final ByteArrayInputStream  pdfInputStream      = new ByteArrayInputStream( pdfStream.toByteArray() );
        final InputStreamResource   inputStreamResource = new InputStreamResource( pdfInputStream );

        final HttpHeaders headers = erzeugeHeader( nutzerName );

        return ResponseEntity.ok()
                             .headers( headers )
                             .contentType( APPLICATION_PDF )
                             .body( inputStreamResource );
    }


    /**
     * Erzeugt HTTP-Header "Content-Disposition", laut dem der Browser die Datei herunterladen soll
     * (und nicht versuchen soll, sie anzuzeigen). Dieser Header enthält auch einen Vorschlag für den
     * Dateinamen.
     * <br><br>
     * 
     * Beispiel für erzeugten Dateinamen: {@code Tagebuch_alice_2024-05-06_10-52.pdf }
     *
     * @param nutzerName Name des Nutzers (wird Bestandteil für Dateiname)
     *
     * @return Header-Objekt für {@code ResponseEntity}
     */
    private HttpHeaders erzeugeHeader( String nutzerName ) {

        final String datumZeitString = _datumsFormatierer.getHeuteDatumZeitFuerDateiname();

        final String contentDispositionHeader = String.format( "attachment; filename=Tagebuch_%s_%s.pdf",
                                                               nutzerName, datumZeitString );
        final HttpHeaders headers = new HttpHeaders();
        headers.add( CONTENT_DISPOSITION, contentDispositionHeader );

        return headers;
    }

}
