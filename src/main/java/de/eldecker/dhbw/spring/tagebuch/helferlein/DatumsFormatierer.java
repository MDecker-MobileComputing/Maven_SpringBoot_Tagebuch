package de.eldecker.dhbw.spring.tagebuch.helferlein;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;


/**
 * Hilfs-Bean für Formatierung von Datum-/Uhrzeit-Strings.
 */
@Component
public class DatumsFormatierer {

    /** Datumsformatierer für Format {@code dd.MM.yyyy} (also zum Anzeigen), z.B. {@code 23.04.2024}. */
    private DateTimeFormatter _datumAnzeigeFormatierer = DateTimeFormatter.ofPattern( "dd.MM.yyyy" );

    /** Datumsformatierer für Format {@code yyyy-MM-dd} (also für Datenbank), z.B. {@code 2024-04-23}. */
    private DateTimeFormatter _datumDatenbankFormatierer = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );

    /** Datumsformatierer für Format {@code yyyy-MM-dd_HH-mm} (also für Dateinamen), z.B. {@code 2024-04-23_12-34}. */
    private DateTimeFormatter _datumZeitFuerDateiname = DateTimeFormatter.ofPattern( "yyyy-MM-dd_HH-mm" );


    /**
     * Gibt aktuelles Datum im Format {@code dd.MM.yyyy} zurück (wird bei jedem Aufruf
     * neu berechnet, stimmt also auch, wenn Anwendung mehr als einen Tag lang läuft).
     *
     * @return Heutiges Datum im Format {@code dd.MM.yyyy}
     */
    public String getHeuteDatumAnzeigeString() {

        final LocalDate heute = LocalDate.now();

        return _datumAnzeigeFormatierer.format( heute );
    }


    /**
     * Gibt aktuelles Datum im Format {@code yyyy-MM-dd} zurück (wird bei jedem Aufruf
     * neu berechnet, stimmt also auch, wenn Anwendung mehr als einen Tag lang läuft).
     * Dieses Format wird für die Speicherung in der Datenbank benötigt.
     *
     * @return Heutiges Datum im Format {@code yyyy-MM-dd}
     */
    public String getHeuteDatumDatenbankString() {

        final LocalDate heute = LocalDate.now();

        return _datumDatenbankFormatierer.format( heute );
    }


    /**
     * Gibt aktuelles Datum und Uhrzeit im Format {@code yyyy-MM-dd_HH-mm} zurück.
     *
     * @return Heutiges Datum und Uhrzeit im Format {@code yyyy-MM-dd_HH-mm}, z.B.
     *         {@code 2024-04-23_12-34}
     */
    public String getHeuteDatumZeitFuerDateiname() {

        final LocalDateTime jetzt = LocalDateTime.now();

        return _datumZeitFuerDateiname.format( jetzt );
    }

}
