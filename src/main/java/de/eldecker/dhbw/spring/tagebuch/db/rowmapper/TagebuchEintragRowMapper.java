package de.eldecker.dhbw.spring.tagebuch.db.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import de.eldecker.dhbw.spring.tagebuch.model.TagebuchEintrag;

@Component
public class TagebuchEintragRowMapper implements RowMapper<TagebuchEintrag> {

    @Override
    public TagebuchEintrag mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        final int    id    = rs.getInt(    "id"      );
        final String text  = rs.getString( "eintrag" );
        final String datum = rs.getString( "datum"   );
        
        return new TagebuchEintrag(id, text, datum);
    }
    
}