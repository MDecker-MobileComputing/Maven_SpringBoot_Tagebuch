package de.eldecker.dhbw.spring.tagebuch.konfig;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.config.Customizer.withDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Konfiguration nach https://www.baeldung.com/spring-boot-security-autoconfiguration#config
 */
@Configuration
public class BasicAuthenticationKonfiguration {

    private Logger LOG = LoggerFactory.getLogger( BasicAuthenticationKonfiguration.class );

    private static final String ROLLE_NUTZER = "nutzer";

    
    /**
     * Authentifizierung konfigurieren.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        return http.authorizeHttpRequests(
                        request -> request.anyRequest().authenticated()
                    )         
                   .httpBasic( withDefaults() )
                   .build();
    }


    /**
     * Nutzernamen mit Passwörtern definieren.
     * 
     * @param passwordEncoder Bean für Kodierung Passwörter.
     * 
     * @return Objekt mit allen Nutzernamen und Passwörtern
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {

        UserDetails user1 = User.withUsername("alice")
                .password(passwordEncoder.encode("g3h3im"))
                .roles(ROLLE_NUTZER)
                .build();        
        
        UserDetails user2 = User.withUsername("bob")
                .password(passwordEncoder.encode("s3cr3t"))
                .roles(ROLLE_NUTZER)
                .build();         

        return new InMemoryUserDetailsManager(user1, user2);
    }
    
    
    /**
     * Bean für Passwort-Kodierung erzeugen.
     * 
     * @return Bean für Passwort-Kodierung
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        
        return createDelegatingPasswordEncoder();
    }    

}