package de.eldecker.dhbw.spring.tagebuch.konfig;


import static java.util.stream.Collectors.toList;

import static org.springframework.security.config.http.SessionCreationPolicy.ALWAYS;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import de.eldecker.dhbw.spring.tagebuch.db.Datenbank;
import de.eldecker.dhbw.spring.tagebuch.model.Nutzer;


/**
 * Konfiguration von Web-Security: auf welche Pfade kann der Nutzer nur nach erfolgreicher
 * Konfiguration zugreifen?
 * <br><br>
 *
 * Quellen:
 * <ul>
 * <li>https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#jc-httpsecurity</li>
 * <li>https://stackoverflow.com/a/73877921/1364368</li>
 * <li>https://www.baeldung.com/spring-boot-security-autoconfiguration#config</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
public class BasicAuthenticationKonfiguration {

    private Logger LOG = LoggerFactory.getLogger( BasicAuthenticationKonfiguration.class );

    /** Rolle, die jedem Nutzer zugefügt wird. */
    private static final String ROLLE_NUTZER = "nutzer";

    /** Repository-Bean für Zugriff auf Datenbank. */
    private Datenbank _datenbank;


    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public BasicAuthenticationKonfiguration( Datenbank datenbank ) {

        _datenbank = datenbank;
    }


    /**
     * Für Requests, deren Pfad mit {@code /app/} beginnt, muss sich der Nutzer 
     * mit <b>Basic Authentication</i> authentifzieren (Nutzername und Passwort
     * müssen in einen vom Browser angezeigten Dialog eingegeben werden). 
     */
    @Bean
    @Order(1)
    public SecurityFilterChain filterKetteFuerBeschraenktePfade( HttpSecurity http ) throws Exception {

        return http.securityMatcher( "/app/**" )
                   .authorizeHttpRequests(
                        request -> request.anyRequest().authenticated()
                   )
                   .httpBasic( withDefaults() )                   
                   .sessionManagement( session-> 
                           session.sessionCreationPolicy( ALWAYS ) // JSESSIONID; in application.properties: server.servlet.session.cookie.http-only=false
                                  .sessionFixation().newSession() )                                  
                   .build();
    }


    /**
     * Für sonstige Requests (weil {@code Order(2)}) wird keine Authentifizierung gefordert.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain filterKetteFuerFreiePfade( HttpSecurity http ) throws Exception {

        return http.securityMatcher( "/", "/h2-console/**", "/api/**" )
                .authorizeHttpRequests(
                     request -> request.anyRequest().permitAll()
                )
                .csrf( (csrf) -> csrf.disable() )                
                .build();
    }


    /**
     * Nutzernamen mit Passwörtern definieren (werden aus Datenbank geladen).
     *
     * @param passwordEncoder Bean für Kodierung Passwörter.
     *
     * @return Objekt mit allen Nutzernamen und Passwörtern
     */
    @Bean
    public InMemoryUserDetailsManager nutzerLaden( PasswordEncoder passwordEncoder ) {

        /*
        UserDetails user1 = User.withUsername("alice")
                                .password(passwordEncoder.encode("g3h3im"))
                                .roles(ROLLE_NUTZER)
                                .build();
        */

        final List<Nutzer> nutzerListe = _datenbank.getAlleNutzer();

        List<UserDetails> userDetailsList =
                nutzerListe.stream()
                           .map( nutzer -> {

                                 final String nutzername        = nutzer.nutzername();
                                 final String passwortEncoded   = passwordEncoder.encode( nutzer.passwort() );

                                 final UserDetails userDetails = User.withUsername( nutzername )
                                                                     .password( passwortEncoded )
                                                                     .roles( ROLLE_NUTZER )
                                                                     .build();
                                 return userDetails;
                           })
                           .collect( toList() );

        LOG.info( "Anzahl Nutzer für Authentifzierung geladen: {}", nutzerListe.size() );

        return new InMemoryUserDetailsManager( userDetailsList );
    }


    /**
     * Bean für Passwort-Kodierung erzeugen.
     * <br><br>
     *
     * Beispielwert Passwort "g3h3im" nach Kodierung:
     * <pre>
     * {bcrypt}$2a$10$/KqHJ.PNBaEV4hX2Y4hmDeANEPqJcz4./VoLp1H66DQ
     * </pre>
     *
     * @return Bean für Passwort-Kodierung mit Algorithmus "bcrypt".
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return createDelegatingPasswordEncoder();
    }

}