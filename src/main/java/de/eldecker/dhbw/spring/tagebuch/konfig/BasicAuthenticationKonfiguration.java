package de.eldecker.dhbw.spring.tagebuch.konfig;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;
import static org.springframework.security.config.Customizer.withDefaults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


/**
 * Quellen:
 * <ul>
 * <li>https://docs.spring.io/spring-security/reference/servlet/configuration/java.html#jc-httpsecurity</li>
 * <li>https://www.baeldung.com/spring-boot-security-autoconfiguration#config</li>
 * </ul> 
 */
@Configuration
@EnableWebSecurity
public class BasicAuthenticationKonfiguration {

    private Logger LOG = LoggerFactory.getLogger( BasicAuthenticationKonfiguration.class );

    private static final String ROLLE_NUTZER = "nutzer";

    
    /**
     * Für Requests, deren Pfad mit {@code /app/} beginnt, wird Basic Authentication gefordert. 
     */
    @Bean
    @Order(1)   
    public SecurityFilterChain filterChain1(HttpSecurity http) throws Exception {
        
        return http.securityMatcher("/app/**")                
                   .authorizeHttpRequests(
                        request -> request.anyRequest().authenticated()
                   )         
                   //.formLogin( withDefaults() )
                   .httpBasic( withDefaults() )
                   .build();
    }


    /**
     * Für sonstige Requests (weil {@code Order(2)} wird keine Authentifizierung gefordert. 
     */
    @Bean
    @Order(2)   
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
        
        return http.authorizeHttpRequests(                   
                        request -> request.anyRequest().permitAll()
                   )         
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