package de.eldecker.dhbw.spring.tagebuch.konfig;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;


/**
 * Diese Klasse enthält eine Methode, die eine Konfigurations-Bean für die
 * Auslieferung der statischen Web-Dateien aus dem Ordner
 * {@code src/main/resources/public } zurückliefert.  
 */
@Configuration
public class StatischWebContentKonfiguration {

    /**
     * Konfiguration für Auslieferung statische Dateien mit Endung {@code .js} und {@code .css},
     * damit HTTP-Header {@code Content-Type} für Encoding {@code UTF-8} an den Browser gesendet
     * wird. Das Standardverhalten ist es, dass für diese Dateitypen dieser Header nicht gesendet
     * wird, so dass die Browser Sonderzeichen (z.B. Umlaute) falsch darstellen.  
     * <br><br>
     * 
     * Der HTTP-Header {@code Content-Type} wird auf folgende Werte gesetzt:
     * <ul>
     * <li>CSS-Dateien: {@code text/css;charset=UTF-8}</li>
     * <li>JavaScript-Dateien: {@code text/javascript;charset=UTF-8}</li>
     * <ul>
     * 
     * @return Bean mit Konfiguration
     */
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> encodingFuerJavaScriptUndCSS() {
        
        final CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding( "UTF-8" );
        encodingFilter.setForceEncoding( true );
        
        final FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter( encodingFilter );
        registrationBean.addUrlPatterns( "*.js", "*.css" );
        
        return registrationBean;
    }
}