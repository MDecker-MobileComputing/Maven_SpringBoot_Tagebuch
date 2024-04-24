package de.eldecker.dhbw.spring.tagebuch.web;

import static org.springframework.http.HttpStatus.CREATED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.eldecker.dhbw.spring.tagebuch.db.Datenbank;



/**
 * RestController für die REST-API-Endpunkte, mit denen das Frontend
 * (HTML/JavaScript) neue Tagebucheinträge oder Änderungen an das
 * Backend schickt.
 * <br><br>
 *
 * Alle Pfade beginnen mit {@code /app/api/v1/}.
 */
@RestController
@RequestMapping( "/app/api/v1" )
public class RestApiController {

    private static Logger LOG = LoggerFactory.getLogger( RestApiController.class );

    /** Repository-Bean für Zugriff auf Datenbank. */
    private final Datenbank _datenbank;


    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public RestApiController( Datenbank datenbank ) {

        _datenbank = datenbank;
    }


    /**
     * Neuen Eintrag (für heutigen Tag) für den angemeldeten Nutzer anlegen
     * oder aktualisieren.
     *
     * @param textEintrag Text von Tagebucheintrag
     *
     * @param authentication Objekt, von dem der aktuell angemeldete Nutzer
     *                       abgefragt wird
     *                       
     * @return Im Erfolgsfall wird HTTP-Status-Code 201 (Created) mit einer 
     *         Erfolgsnachricht zurückgegeben.
     */
    @PostMapping( "/eintrag" )
    public ResponseEntity<String> eintragNeuAendern( 
                                       @RequestBody String textEintrag,
                                       Authentication authentication ) {

        final String nutzername = authentication.getName();
        LOG.info( "Versuche für Nutzer \"{}\" Tagebucheintrag neu anzulegen oder zu ändern.", 
                  nutzername );
        
        return new ResponseEntity<>( "Tagebucheintrag auf DB gespeichert", CREATED );
    }

}
