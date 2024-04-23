package de.eldecker.dhbw.spring.tagebuch.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.tagebuch.model.Nutzer;


/**
 * Manuelle Implementierung des Interface {@code RowMapper},
 * um eine Ergebniszeile einer geeigneten Datenbankabfrage
 * mit {@code JdbcTemplate} auf ein Objekt der Record-Klasse
 * {@link Nutzer} abzubilden.
 * <br><br>
 * 
 * <b>Achtung:</b> Man kann auch eine Instanz der Klasse
 * {@code DataClassRowMapper} verwenden, um eine Ergebniszeile
 * auf eine Objekt einer Record-Klasse abzubilden 
 * (die Spaltennamen in der Ergebniszeile müssen dabei ggf.
 *  mit {@code as} entsprechend dem Attributnamen der Record-Klasse
 *  umbenannt werden). 
 */
@Component
public class NutzerRowMapper implements RowMapper<Nutzer> {

    /**
     * Methode kopiert {@code id}, {@code nutzername} und {@code passwort}  
     * in das als Ergebnis zurückgegebene {@code Nutzer}-Objekt.
     * 
     * @return rs Ergebniszeile
     * 
     * @return rowNum Zeilennummer in Ergebnis, wird hier nicht verwendet
     */
    @Override
    public Nutzer mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        final int    id         = rs.getInt("id");
        final String nutzerName = rs.getString("nutzername");
        final String passwort   = rs.getString("passwort"  );
        
        return new Nutzer(id, nutzerName, passwort);
    }

}

