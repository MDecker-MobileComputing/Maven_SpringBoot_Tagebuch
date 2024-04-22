package de.eldecker.dhbw.spring.tagebuch.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.tagebuch.model.Nutzer;
import de.eldecker.dhbw.spring.tagebuch.model.TagebuchEintrag;


@Component
public class NutzerRowMapper implements RowMapper<Nutzer> {

    /**
     * Methode kopiert {@code id}, {@code nutzername} und {@code passwort} in das 
     * als Ergebnis zurückgegebene {@code Nutzer}-Objekt.
     */
    @Override
    public Nutzer mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        final int    id         = rs.getInt("id");
        final String nutzerName = rs.getString("nutzername");
        final String passwort   = rs.getString("passwort"  );
        
        return new Nutzer(id, nutzerName, passwort);
    }

}

