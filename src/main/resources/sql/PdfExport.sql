SELECT t.id,
        FORMATDATETIME( t.datum, 'dd. MMMM yyyy (EEEE)' ) AS datum,
        CASE
            WHEN CHAR_LENGTH(eintrag) > 99
              THEN CONCAT( SUBSTRING( eintrag, 1, 99 ), '...' )
              ELSE eintrag
        END AS text,
        '' AS link
    FROM tagebucheintrag t, nutzer n
   WHERE t.nutzer_id = n.id
     AND n.nutzername = :nutzername
ORDER BY t.datum ASC
