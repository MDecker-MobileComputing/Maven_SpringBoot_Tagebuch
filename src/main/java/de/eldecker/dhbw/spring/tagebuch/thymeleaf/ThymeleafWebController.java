package de.eldecker.dhbw.spring.tagebuch.thymeleaf;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import de.eldecker.dhbw.spring.tagebuch.db.Datenbank;
import de.eldecker.dhbw.spring.tagebuch.helferlein.HeuteEintragChecker;
import de.eldecker.dhbw.spring.tagebuch.model.TagebuchEintrag;


/**
 * Controller, der die Anfragen für die Thymeleaf-Views bearbeitet.
 * Alle Pfade müssen mit {@code /app/} beginnen.
 */
@Controller
@RequestMapping("/app")
public class ThymeleafWebController {

    private Logger LOG = LoggerFactory.getLogger( ThymeleafWebController.class );
            
    /** 
     * Name (ohne Datei-Endung) für Template "hauptseite" (Liste aller Einträge für einen Nutzer), 
     * wird im Ordner {@code src/main/resources/templates/} gesucht. 
     * 
     */
    private static final String TEMPLATE_HAUPTSEITE = "hauptseite";

    /** 
     * Name (ohne Datei-Endung) für Template "eintrag" (Anzeige einzelner Tagebucheintrag), wird  
     * im Ordner {@code src/main/resources/templates/} gesucht.
     */    
    private static final String TEMPLATE_EINTRAG = "eintrag";
    
    /** Key für String-Attribut mit Nutzername. */
    private static final String ATTRIBUT_NAME_NUTZERNAME = "nutzername";
    
    /** Key für List-Attribut im Template "hauptseite". */ 
    private static final String ATTRIBUT_LISTE_TAGEBUCHEINTRAEGE = "tagebucheintrage";
    
    /** Key für String-Attribut mit formatiertem Datums des Tagebucheintrags im Template "eintrag". */
    private static final String ATTRIBUT_EINTRAG_DATUM = "eintrag_datum";
    
    /** Key für String-Attribut mit eigentlichem Inhalt des Tagebucheintrags im Template "eintrag". */
    private static final String ATTRIBUT_EINTRAG_TEXT = "eintrag_text";
            
    /** 
     * Key für String-Attribut mit dem Nutzer anzuzeigenden Text (Warnung, Fehler, ...),
     * wird von allen Templates verwendet. 
     */
    private static final String ATTRIBUT_MELDUNG = "meldung";
    
    /**
     * Key für bool'sches Attribut das genau dann {@code true} ist, wenn die Liste auf der
     * Hauptseite auch einen Eintrag für den aktuellen Tag enthält (nämlich den obersten
     * Eintrag), oder ein auf der Detailseite angezeigter Eintrag für den aktuellen Tag
     * ist. Diese Information ist erforderlich, weil nur Tagebucheinträge für den aktuellen
     * Tag angelegt bzw. geändert werden können.
     */
    private static final String ATTRIBUT_EINTRAG_FUER_HEUTE = "eintrag_fuer_heute";
    
    /** Titel für Seite für neuen oder zu ändernden Tagebucheintrag. */
    private static final String ATTRIBUT_SEITENTITEL = "seitentitel";
    
    /** String mit bisherigem Text, wenn ein Tagebucheintrag zu ändern ist. */
    private static final String ATTRIBUT_ALTER_TEXT = "alterText";
    
    
    /** Repository-Bean für Zugriff auf Datenbank. */
    private final Datenbank _datenbank;
    
    /** Bean zur Überprüfung, ob ein bestimmter Tagebucheintrag für das heutige Datum ist. */
    private final HeuteEintragChecker _heuteEintragChecker;
    
    
    /**
     * Konstruktor für Dependency Injection. 
     */
    public ThymeleafWebController( Datenbank datenbank, 
                                   HeuteEintragChecker heuteEintragChecker ) { 
                                         
        _datenbank           = datenbank;
        _heuteEintragChecker = heuteEintragChecker;
    }


    /**
     * Hauptseite der Anwendung anzeigen: Liste mit Tagebucheinträgen.
     *
     * @param authentication Objekt, um Name von authentifiziertem Nutzer abzufragen;  
     *                       siehe auch: https://stackoverflow.com/questions/68595199/
     *
     * @param model Objekt, in das die Werte für die Platzhalter in der Template-Datei
     *              geschrieben werden.
     *
     * @return Name (ohne Suffix)  der Template-Datei {@code hauptseite.html}, die angezeigt 
     *         werden soll; wird in Ordner {@code src/main/resources/templates/} gesucht.
     */
    @GetMapping("/hauptseite")
    public String hauptseiteAnzeige( Authentication authentication,
                                     Model model ) {

        final String nutzername = authentication.getName();
        model.addAttribute( ATTRIBUT_NAME_NUTZERNAME, nutzername );
        LOG.info( "Hauptseite aufgerufen von: {}"   , nutzername );        
        
        List<TagebuchEintrag> eintraegeListe = _datenbank.getAlleTagebuchEintraege( nutzername );
        model.addAttribute( ATTRIBUT_LISTE_TAGEBUCHEINTRAEGE, eintraegeListe );
        
        final boolean hatEintragFuerHeute = _heuteEintragChecker.hatEintragFuerHeute( eintraegeListe );
        model.addAttribute ( ATTRIBUT_EINTRAG_FUER_HEUTE, hatEintragFuerHeute );
        
        if ( eintraegeListe.isEmpty() ) {
            
            model.addAttribute( ATTRIBUT_MELDUNG, "Keine Tagebucheinträge vorhanden" );
        }
        
        return TEMPLATE_HAUPTSEITE;
    }
    

    /**
     * Einzelnen Tagebucheintrag anzeigen.
     * 
     * @param authentication Objekt, um Name von authentifiziertem Nutzer abzufragen;  
     *                       siehe auch: https://stackoverflow.com/questions/68595199/
     *
     * @param model Objekt, in das die Werte für die Platzhalter in der Template-Datei
     *              geschrieben werden.
     *              
     * @param datum Datum im Format {@code YYYY-MM-DD}
     *
     * @return Name (ohne Suffix) der Template-Datei {@code eintrag.html}, die angezeigt 
     *         werden soll; wird in Ordner {@code src/main/resources/templates/} gesucht.
     */
    @GetMapping("/eintrag/{datum}")
    public String eintragAnzeigen( Authentication authentication,
                                   Model model,
                                   @PathVariable("datum") String datum ) {

        final String nutzername = authentication.getName();
        model.addAttribute( ATTRIBUT_NAME_NUTZERNAME, nutzername );
        
        Optional<TagebuchEintrag> eintragOptional = 
                                        _datenbank.getTagebuchEintrag( nutzername, 
                                                                       datum );        
        if ( eintragOptional.isPresent() ) {
            
            final TagebuchEintrag eintrag = eintragOptional.get();
            
            model.addAttribute( ATTRIBUT_EINTRAG_DATUM, eintrag.datum() );
            model.addAttribute( ATTRIBUT_EINTRAG_TEXT , eintrag.text()  );
            
            final boolean istEintragFuerHeute = _heuteEintragChecker.istEintragFuerHeute(eintrag);
            model.addAttribute( ATTRIBUT_EINTRAG_FUER_HEUTE, istEintragFuerHeute );
            
                                    
            LOG.info( "Tagebucheintrag für Nutzer \"{}\" und Datum \"{}\" wird angezeigt.",
                      nutzername, datum );            
        } else {
                        
            model.addAttribute( ATTRIBUT_MELDUNG, "Keinen Tagebucheintrag für diesen Tag gefunden." );
            
            model.addAttribute( ATTRIBUT_EINTRAG_DATUM, datum );
            model.addAttribute( ATTRIBUT_EINTRAG_TEXT , ""    );
            
            LOG.warn( "Angeforderter Tagebucheintrag wurde nicht gefunden." );
        }
        
        return TEMPLATE_EINTRAG;
    }
    
    
    /**
     * Neuen Tagebucheintrag für aktuellen 
     * 
     * @param model Objekt, in das die Werte für die Platzhalter in der Template-Datei
     *              geschrieben werden.
     * 
     * @return Name (ohne Suffix) der Template-Datei {@code neu_aendern.html}, 
     *         die angezeigt werden soll; wird in Ordner {@code src/main/resources/templates/} 
     *         gesucht.         
     */
    @GetMapping("/neu")
    public String eintragNeu( Model model ) {

        model.addAttribute( ATTRIBUT_SEITENTITEL, "Neuen Tagebucheintrag anlegen" );
        model.addAttribute( ATTRIBUT_ALTER_TEXT , "" );
        
        return "neu_aendern";
    }
    
    
    /**
     * Tagebucheintrag für heutigen Tag ändern.
     * 
     * @param model Objekt, in das die Werte für die Platzhalter in der Template-Datei
     *              geschrieben werden.
     *              
     * @return Name der Template-Datei {@code neu_aendern.html}, die angezeigt werden soll;
     *         wird in Ordner {@code src/main/resources/templates/} gesucht.
     */
    @GetMapping("/aendern")
    public String eintragAendern( Model model ) {

        model.addAttribute( ATTRIBUT_SEITENTITEL, "Tagebucheintrag für heute ändern" );
        model.addAttribute( ATTRIBUT_ALTER_TEXT , "Alter Text" );
        
        return "neu_aendern";
    }    

}
