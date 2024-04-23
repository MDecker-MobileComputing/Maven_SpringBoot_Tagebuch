package de.eldecker.dhbw.spring.tagebuch.db;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import de.eldecker.dhbw.spring.tagebuch.model.Nutzer;
import de.eldecker.dhbw.spring.tagebuch.model.TagebuchEintrag;


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
     * Bean, mit der man benamte Platzhalter in prepared SQL-Statements verwenden
     * kann (z.B. {@code :nutzerid}.
     */
    private final NamedParameterJdbcTemplate _namedParamJdbcTemplate;

    /**
     * Bean, um Ergebnis einer Query auf die Tabelle {@code nutzer} auf Objekte
     * vom Typ {@link Nutzer} abzubilden.
     */
    private final NutzerRowMapper _nutzerRowMapper;

    /**
     * Objekt, das automatisch eine Ergebniszeile der DB-Anfrage auf ein
     * Objekt der Record-Klasse {@link TagebuchEintrag} abbildet.
     * <br><br>
     * ACHTUNG: die Ergebniszeile muss für jedes Attribut der
     * Record-Klasse eine gleichnamige Spalte haben (Spalte kann
     * mit {@code AS} in SQL umbenannt werden).
     * <br><br>
     * Es gibt auch noch die Klasse {@code BeanPropertyRowMapper}, aber
     * dieses funktioniert nicht mit Record-Klassen.
     */
    private final DataClassRowMapper<TagebuchEintrag> _eintragDataClassRowMapper;


    /**
     * Konstruktor für Dependency Injection und Erzeugung der
     * {@code DataClassRowMapper}-Instanz.
     */
    @Autowired
    public Datenbank( JdbcTemplate jdbcTemplate,
                      NutzerRowMapper nutzerRowMapper,
                      NamedParameterJdbcTemplate namedParamJdbcTemplate
                    ) {

        _jdbcTemplate           = jdbcTemplate;
        _nutzerRowMapper        = nutzerRowMapper;
        _namedParamJdbcTemplate = namedParamJdbcTemplate;

        _eintragDataClassRowMapper = new DataClassRowMapper<>( TagebuchEintrag.class );
    }


    /**
     * Liste aller Nutzer von DB holen.
     *
     * @return Liste mit allen Nutzern, enthält auch Passwort, sortiert aufsteigend
     *         nach Nutzernamen; kann leer sein, aber nicht {@code null}.
     */
    public List<Nutzer> getAlleNutzer() {

        try {

            List<Nutzer> ergebnisListe =
                    _jdbcTemplate.query( "SELECT * FROM nutzer ORDER BY nutzername",
                                         _nutzerRowMapper );

            LOG.info("Alle {} Nutzer von DB ausgelesen.", ergebnisListe.size() );

            return ergebnisListe;
        }
        catch (DataAccessException ex) {

            LOG.error("Fehler bei Abfrage aller Nutzer von Datenbank.", ex);
            return emptyList();
        }
    }


    /**
     * Alle Tagebucheinträge für {@code nutzername} abfragen, wobei der eigentliche
     * Text ggf. abgekürzt wird.
     *
     * @param nutzername Name des Nutzers, für den alle Einträge zurückgegeben
     *                   werden sollen.
     *
     * @return Liste mit allen Tagenbucheinträgen für {@code nutzername};
     *         kann leer sein, aber nicht {@code null}; nach absteigendem
     *         Datum sortiert. Der Text in einem Tagebucheintrag wird bei
     *         Bedarf gekürzt und dann mit "..." am Ende versehen.
     *         Die Datumswerte sind wie im folgenden Beispiel formatiert:
     *         {@code 24.04.2024 (Do.)}
     *         Es wird auch das Feld {@code link} gesetzt, das den Link zur
     *         Detailansicht des Eintrags enthält.
     */
    public List<TagebuchEintrag> getAlleTagebuchEintraege(String nutzername) {

        final String preparedStatement =
                """
                    SELECT t.id,
                           FORMATDATETIME(t.datum, 'dd.MM.yyyy (E)') AS datum,
                           CASE
                               WHEN CHAR_LENGTH(eintrag) > 99
                                    THEN CONCAT(SUBSTRING(eintrag, 1, 99), '...')
                                    ELSE eintrag
                           END AS text,
                           CONCAT('eintrag/', FORMATDATETIME(t.datum, 'yyyy-MM-dd')) AS link
                        FROM tagebucheintrag t, nutzer n
                        WHERE t.nutzer_id = n.id
                          AND n.nutzername = ?
                        ORDER BY t.datum DESC
                 """;
        try {

            List<TagebuchEintrag> ergebnisListe =
                    _jdbcTemplate.query( preparedStatement,
                                         _eintragDataClassRowMapper,
                                         nutzername // Platzhalterwert für Prepared Statement
                                       );

            LOG.info("Anzahl Tagebucheinträge für Nutzer \"{}\" ausgelesen: {}",
                     nutzername, ergebnisListe.size() );

            return ergebnisListe;
        }
        catch (DataAccessException ex) {

            LOG.error("Fehler bei Abfrage ALLER Tagebucheinträge für Nutzer \"{}\".",
                       nutzername, ex );
            return emptyList();
        }
    }


    /**
     * Einzelnen Tagebucheintrag für {@code nutzername} und {@code datum} auslesen.
     * Die Methode sollte nur aufgerufen werden, wenn man sicher ist, dass es den
     * Tagebucheintrag tatsächlich gibt (weil er etwa von der Methode
     * {@link #getAlleTagebuchEintraege(String)} zurückgeliefert wurde).
     *
     * @param nutzername Name des Nutzers, für den der Einträge ausgelesen werden soll
     *
     * @param datum Datum im Format {@code YYYY-MM-DD}
     *
     * @return Optional enthält den gewünschten Tagebucheintrag für {@code nutzername}
     *         und {@code datum} oder ist leer; wenn Tagebucheintrag gefunden wurde,
     *         dann ist das Attribut {@code link} leer.
     */
    public Optional<TagebuchEintrag> getTagebuchEintrag(String nutzername, String datum) {

        final String preparedStatement =
                """
                    SELECT t.id,
                           FORMATDATETIME(t.datum, 'dd.MM.yyyy (E)') AS datum,
                           eintrag as text,
                           '' AS link
                        FROM tagebucheintrag t, nutzer n
                        WHERE t.nutzer_id = n.id
                          AND n.nutzername = :nutzername
                          AND t.datum      = :datum
                """;

        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("nutzername", nutzername);
        params.addValue("datum"     , datum);

        try {

            TagebuchEintrag eintrag =
                    _namedParamJdbcTemplate.queryForObject( preparedStatement,
                                                            params,
                                                            _eintragDataClassRowMapper
                                                          );
            return Optional.of( eintrag );
        }
        catch (EmptyResultDataAccessException ex) {

            LOG.warn("Interner Fehler: Kein Tagebucheintrag für Nutzer \"{}\" und Datum \"{}\" gefunden.",
                    nutzername, datum );
            return Optional.empty();
        }
        catch (DataAccessException ex) {

            LOG.error("Fehler beim Auslesen von Tagebucheintrag für Nutzer \"{}\" und Datum \"{}\": " + ex,
                      nutzername, datum );
            return Optional.empty();
        }
    }

}
