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

    /** Datumsformatierer für Format {@code dd.MM.yyyy}, z.B. {@code 23.04.2024}. */
    private DateTimeFormatter _datumFormatierer = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    
    /**
     * Gibt aktuelles Datum im Format {@code YYYY-MM-dd} zurück (wird bei jedem Aufruf neu
     * berechnet, stimmt also auch, wenn Anwendung mehr als einen Tag lang läuft).
     * 
     * @return Heutiges Datum im Format {@code YYYY-MM-dd}
     */
    private String getHeuteDatumString() {
        
        LocalDate heute = LocalDate.now();
        
        return _datumFormatierer.format(heute);
    }
    
    
    /**
     * Prüft, ob in absteigend sortierter Liste von Tagebucheinträgen der erste
     * Eintrag für das heutige Datum ist.
     * 
     * @param eintraegeListe List von Tagebucheinträgen, muss absteigend sortiert sein
     *                       (also Eintrag für jüngstes Datum an erster Stelle); es wird
     *                       auch der Fall berücksichtigt, dass die Liste keine Einträge
     *                       enthält.
     * 
     * @return {@code true} gdw. der erste Eintrag von {@code eintrageListe}
     *         für das heutige Datum ist.
     */
    public boolean hatEintragFuerHeute(List<TagebuchEintrag> eintraegeListe) {
        
        if (eintraegeListe.size() < 1) {
            
            return false;
        }
        
        final TagebuchEintrag eintrag = eintraegeListe.get(0);
        
        return istEintragFuerHeute( eintrag );
    }
    
    
    /**
     * Überprüft, ob {@code eintrag} für das aktuelle Datum ist.
     * 
     * @param eintrag Tagebucheintrag, für den zu überprüfen ist, ober  
     *                er für den aktuellen Tag ist.
     *                
     * @return {@code true} gdw. {@code eintrag} als Datum den Wert für
     *         den aktuellen Tag hat.                        
     */
    public boolean istEintragFuerHeute(TagebuchEintrag eintrag) {
        
        final String heuteDatumString = getHeuteDatumString();
        
        return eintrag.datum().startsWith( heuteDatumString );
    }
    
}
