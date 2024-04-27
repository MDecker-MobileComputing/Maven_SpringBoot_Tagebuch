package de.eldecker.dhbw.spring.tagebuch.helferlein;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.tagebuch.model.TagebuchEintrag;


/**
 * Da ein Nutzer nur für den aktuellen Tag einen Tagebucheintrag anlegen bzw.
 * ändern kann, werden Methoden benötigt, mit denen überprüft wird, ob ein
 * {@link TagebuchEintrag} für den heutigen Tag ist oder ob eine Liste dieser
 * Objekte einen Eintrag für den heuten Tag enthält.
 */
@Component
public class HeuteEintragChecker {

    /** Datumsformatierer für Format {@code dd.MM.yyyy} (also zum Anzeigen), z.B. {@code 23.04.2024}. */
    private DateTimeFormatter _datumAnzeigeFormatierer = DateTimeFormatter.ofPattern( "dd.MM.yyyy" );

    /** Datumsformatierer für Format {@code yyyy-MM-dd} (also für Datenbank), z.B. {@code 2024-04-23}. */
    private DateTimeFormatter _datumDatenbankFormatierer = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );


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
     * Prüft, ob in absteigend sortierter Liste von Tagebucheinträgen der erste
     * Eintrag für das heutige Datum ist.
     *
     * @param eintraegeListe List von Tagebucheinträgen, muss absteigend sortiert
     *                       sein (also Eintrag für jüngstes Datum an erster Stelle);
     *                       es wird auch der Fall berücksichtigt, dass die Liste
     *                       keine Einträge enthält.
     *
     * @return {@code true} gdw. der erste Eintrag von {@code eintrageListe} für das
     *         heutige Datum ist.
     */
    public boolean hatEintragFuerHeute( List<TagebuchEintrag> eintraegeListe ) {

        if ( eintraegeListe.size() < 1 ) {

            return false;
        }

        final TagebuchEintrag eintrag = eintraegeListe.get( 0 );

        return istEintragFuerHeute( eintrag );
    }


    /**
     * Überprüft, ob {@code eintrag} für das aktuelle Datum ist.
     *
     * @param eintrag Tagebucheintrag, für den zu überprüfen ist,
     *                ob er für den aktuellen Tag ist.
     *
     * @return {@code true} gdw. {@code eintrag} als Datum den Wert
     *         für den aktuellen Tag hat.
     */
    public boolean istEintragFuerHeute( TagebuchEintrag eintrag ) {

        final String heuteDatumString = getHeuteDatumAnzeigeString();

        return eintrag.datum().startsWith( heuteDatumString );
    }

}
