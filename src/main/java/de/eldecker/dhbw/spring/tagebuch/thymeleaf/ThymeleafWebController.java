package de.eldecker.dhbw.spring.tagebuch.thymeleaf;

import static java.util.Collections.emptyList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import de.eldecker.dhbw.spring.tagebuch.db.Datenbank;
import de.eldecker.dhbw.spring.tagebuch.model.TagebuchEintrag;


/**
 * Controller, der die Anfragen für die Thymeleaf-Views bearbeitet.
 * Alle Pfade müssen mit {@code /app/} beginnen.
 */
@Controller
@RequestMapping("/app")
public class ThymeleafWebController {

    private Logger LOG = LoggerFactory.getLogger( ThymeleafWebController.class );
            
    private static final String ATTRIBUT_NAME_NUTZERNAME = "nutzername";
    
    private static final String ATTRIBUT_LISTE_TAGEBUCHEINTRAEGE = "tagebucheintrage";
    
    /**
     * Repository-Bean für Zugriff auf Datenbank.
     */
    private final Datenbank _datenbank;
    
    
    /**
     * Konstruktor für Dependency Injection. 
     */
    public ThymeleafWebController(Datenbank datenbank) {
        
        _datenbank = datenbank;
    }


    /**
     * Hauptseite der Anwendung anzeigen: Liste mit Tagebucheinträgen.
     *
     * @param authentication Objekt, um Name von authentifiziertem Nutzer abzufragen;  
     *                       siehe auch: https://stackoverflow.com/questions/68595199/
     *
     * @param model Model, in das die Daten für die Platzhalter in der
     *              Template-Datei geschrieben werden
     *
     * @return Name der Template-Datei, die angezeigt werden soll;
     *         wird in Ordner {@code src/main/resources/templates/} gesucht.
     */
    @GetMapping("/hauptseite")
    public String kuerzelAufloesen( Authentication authentication,
                                    Model model ) {

        final String nutzername = authentication.getName();
        model.addAttribute(ATTRIBUT_NAME_NUTZERNAME, nutzername );
        LOG.info( "Hauptseite aufgerufen von: {}"  , nutzername );        
        
        List<TagebuchEintrag> eintrage = _datenbank.getAlleTagebuchEintraege(nutzername);
        model.addAttribute( ATTRIBUT_LISTE_TAGEBUCHEINTRAEGE, eintrage );
        
        return "hauptseite";
    }

}
