package de.eldecker.dhbw.spring.tagebuch.helferlein;

import de.eldecker.dhbw.spring.tagebuch.model.TagebuchEintrag;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Da ein Nutzer nur für den aktuellen Tag einen Tagebucheintrag anlegen bzw.
 * ändern kann, werden Methoden benötigt, mit denen überprüft wird, ob ein
 * {@link TagebuchEintrag} für den heutigen Tag ist oder ob eine Liste dieser
 * Objekte einen Eintrag für den heuten Tag enthält.
 */
@Component
public class HeuteEintragChecker {

    /** Bean für Formatierung von Datum-/Uhrzeit-Strings */
    private final DatumsFormatierer _datumsFormatierer;

    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public HeuteEintragChecker( DatumsFormatierer datumsFormatierer ) {

        _datumsFormatierer = datumsFormatierer;
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

        final String heuteDatumString = _datumsFormatierer.getHeuteDatumAnzeigeString();

        return eintrag.datum().startsWith( heuteDatumString );
    }

}
