package de.eldecker.dhbw.spring.tagebuch.helferlein;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


/**
 * Klasse für Bean, um Textdateien aus dem Ressourcen-Ordner zu lesen,
 * v.a. Dateien mit <i>Prepared Statements</i> für die Datenbank.
 */
@Component
public class RessourcenDateiLader {

    private static Logger LOG = LoggerFactory.getLogger( RessourcenDateiLader.class );    
    
    /** Bean zum Laden von Ressourcen-Dateien. */
    private final ResourceLoader _resourceLoader;

    
    /**
     * Konstruktor für <i>Dependency Injection</i>.
     */
    @Autowired
    public RessourcenDateiLader( ResourceLoader resourceLoader ) {
        
        _resourceLoader = resourceLoader; 
    }
    
    
    /**
     * Textdatei mit {@code pfad} aus Ordner {@code src/main/resources/} einlesen.
     * 
     * @param pfad Relativer Pfad der zu ladenden Textdatei, z.B. {@code sql/UpsertTagebucheintrag.sql};
     *             Pfad relativ zu {@code src/main/resources/}.
     *             
     * @return Optional enthält Inhalt von Textdatei im Ressourcenordner unter {@code pfad}; 
     *         im Fehlerfall ist das Optional leer.          
     */
    public Optional<String> ladeRessourcenDatei(String pfad) {
        
        final String resourceLocation = "classpath:" + pfad;
        
        final Resource resource = _resourceLoader.getResource( resourceLocation );
        
        try {

            final Path path = resource.getFile().toPath();
            
            final byte[] byteArray = readAllBytes( path ); // throws IOException
            
            final String string = new String( byteArray, UTF_8 );
            
            LOG.debug( "Ressourcen-Datei \"{}\" erfolgreich eingelesen.", path );
            
            return Optional.of( string  );            
        }
        catch (IOException ex) {
            
            LOG.error( "Fehler beim Versuch die Ressourcendatei \"{}\" einzulesen: ", 
                       pfad, ex );
            
            return Optional.empty();
        }
    }
    
}
