package de.eldecker.dhbw.spring.tagebuch.logik;

/**
 * Eigener Exception-Typ für den Fall, dass bei Erzeugung der PDF-Datei
 * für den Export des Tagebuchs eines Nutzers ein Fehler auftritt.
 */
@SuppressWarnings("serial")
public class PdfExportException extends Exception {

    public PdfExportException( String message, Exception ex ) {
        
        super( message, ex );
    }
}
