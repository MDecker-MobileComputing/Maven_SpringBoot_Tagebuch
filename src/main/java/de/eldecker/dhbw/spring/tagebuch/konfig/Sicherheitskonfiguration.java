package de.eldecker.dhbw.spring.tagebuch.konfig;

import static java.util.stream.Collectors.toList;
import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import de.eldecker.dhbw.spring.tagebuch.db.Datenbank;
import de.eldecker.dhbw.spring.tagebuch.model.Nutzer;


/**
 * Konfiguration von Web-Security: auf bestimmte Pfad soll man nur nach Authentifizierung zugreifen dürfen.
 */
@Configuration
@EnableWebSecurity
public class Sicherheitskonfiguration {

    private Logger LOG = LoggerFactory.getLogger( Sicherheitskonfiguration.class );

    /** Rolle, die jedem Nutzer zugefügt wird. */
    private static final String ROLLE_NUTZER = "nutzer";

    /** Array mit Pfaden, auf die auch ohne Authentifizierung zugegriffen werden kann. */
    private final static AntPathRequestMatcher[] oeffentlichePfadeArray = { antMatcher( "/index.html"      ),
                                                                            antMatcher( "/abgemeldet.html" ),
                                                                            antMatcher( "/h2-console/**"   ),
                                                                            antMatcher( "/styles.css"      ) // wird von index.html und abgemeldet.html benötigt
                                                                          };
    /** Repository-Bean für Zugriff auf Datenbank. */
    private Datenbank _datenbank;


    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public Sicherheitskonfiguration( Datenbank datenbank ) {

        _datenbank = datenbank;
    }


    /**
     * Konfiguration Sicherheit für HTTP.
     */
    @Bean
    public SecurityFilterChain httpKonfiguration(HttpSecurity http) throws Exception {

        return http.csrf( (csrf) -> csrf.disable() )
                   .authorizeHttpRequests( auth -> auth.requestMatchers( oeffentlichePfadeArray ).permitAll()
                                                       .anyRequest().authenticated() ) // alle anderen Pfade gehen nur mit Authentifizierung
                   .formLogin( formLogin -> formLogin.defaultSuccessUrl( "/app/hauptseite", true ) )
                   .logout(logout -> logout
                                           .logoutUrl( "/logout" ) // "/logout" ist Default
                                           .logoutSuccessUrl("/abgemeldet.html")
                                           .invalidateHttpSession( true )
                                           .deleteCookies( "JSESSIONID" )
                       )
                   .headers( headers -> headers.disable() ) // damit H2-Console funktioniert
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