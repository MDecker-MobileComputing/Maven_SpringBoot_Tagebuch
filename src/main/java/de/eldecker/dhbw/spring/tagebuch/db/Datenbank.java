package de.eldecker.dhbw.spring.tagebuch.db;

import static java.util.Collections.emptyList;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import de.eldecker.dhbw.spring.tagebuch.model.Nutzer;


/**
 * Diese Repository-Bean enthält Methoden für Zugriff auf die Datenbank.  
 */
@Repository
public class Datenbank {

    private static Logger LOG = LoggerFactory.getLogger( Datenbank.class );
    
    
    /**
     * Bean für Datenbankzugriffe: {@code JdbcTemplate} von Spring bietet
     * eine höhere Abstraktion als JDBC und vereinfacht die Verwendung von JDBC.
     */    
    private final JdbcTemplate _jdbcTemplate;
    
    /**
     * Bean, um Ergebnis einer Query auf die Tabelle {@code nutzer} auf Objekte
     * vom Typ {@link Nutzer} abzubilden.
     */
    private final NutzerRowMapper _nutzerRowMapper;
    
    
    /**
     * Konstruktor für Dependency Injection.
     */
    @Autowired
    public Datenbank( JdbcTemplate jdbcTemplate,
                      NutzerRowMapper nutzerRowMapper ) {

        _jdbcTemplate    = jdbcTemplate;
        _nutzerRowMapper = nutzerRowMapper;
    }
    
    
    /**
     * Liste aller Nutzer von DB holen.
     * 
     * @return Liste mit allen Nutzern, enthält auch Passwort, sortiert aufsteigend
     *         nach Nutzernamen
     */
    public List<Nutzer> getAlleNutzer() {
        
        try {

            List<Nutzer> ergebnisListe = _jdbcTemplate.query("SELECT * FROM nutzer ORDER BY nutzername", _nutzerRowMapper);
            
            LOG.info("Alle {} Nutzer von DB ausgelesen.", ergebnisListe.size());
            
            return ergebnisListe;
        }
        catch (DataAccessException ex) {
            
            LOG.error("Fehler bei Abfrage aller Nutzer von Datenbank.", ex);
            return emptyList();
        }
    }
    
}
