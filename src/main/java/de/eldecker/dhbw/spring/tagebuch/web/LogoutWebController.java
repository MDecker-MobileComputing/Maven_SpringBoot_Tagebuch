package de.eldecker.dhbw.spring.tagebuch.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


/**
 * Separater WebController für Logout (der {@code ThymeleafWebController} bedient
 * nur Pfade, die mit Basic Authentication gesichert sind.
 */
@Controller
public class LogoutWebController {

    private Logger LOG = LoggerFactory.getLogger( LogoutWebController.class );


    /**
     * Session invalidieren (JSESSIONID) und Seite mit "Sie haben sich erfolgreich abgemeldet"
     * anzeigen.
     *
     * @param request HTTP-Request, um Sitzung zu erhalten
     *
     * @return Name Template "ausgeloggt"; wird angezeigt, wenn Nutzer sich ausgeloggt hat.
     */
    @GetMapping("/logout")
    public String testSessionListner( HttpServletRequest request ){

        final HttpSession session = request.getSession( false );
        if ( session == null ){

            LOG.warn( "Keine Session gefunden" );

        } else {
            
            securityKontextAuswerten( session );
            
            LOG.info( "Sitzung invalidiert mit JSESSIONID={}.", session.getId() );
            session.invalidate();
        }

        return "ausgeloggt";
    }
    
    
    /**
     * Liest Details aus der HTTP-Sitzung aus.
     *  
     * @param session Sitzung, aus der Details ausgelesen und auf den Logger geschrieben werden.
     */
    private void securityKontextAuswerten( HttpSession session ) {
        
        final Object sessionAttribut = session.getAttribute( "SPRING_SECURITY_CONTEXT" );
        if ( sessionAttribut != null ) {
                            
            if (sessionAttribut instanceof SecurityContext sc) {

                final String nutzerName = sc.getAuthentication().getName();
                LOG.info( "Nutzer, dessen Session gleich invalidiert wird: {}", nutzerName );                    
                
            } else {
                
                LOG.warn( "SPRING_SECURITY_CONTEXT in Session ist kein SecurityContext sondern {}.",
                          sessionAttribut.getClass() );
            }
            
        } else {
            
            LOG.warn( "In Session kein SPRING_SECURITY_CONTEXT gefunden." );
        }
    }

}
