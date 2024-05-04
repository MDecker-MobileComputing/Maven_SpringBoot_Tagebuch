--- Abfrage aller Tagebucheinträge für einen Nutzer für PDF-Export.
---
--- Aufsteigende Reihenfolge, ausführliches Datum, Text nicht abgekürzt, Link leer.
SELECT t.id,
        FORMATDATETIME( t.datum, 'dd. MMMM yyyy (EEEE)' ) AS datum,
        eintrag                                           AS text,
        ''                                                AS link
    FROM tagebucheintrag t, nutzer n
   WHERE t.nutzer_id = n.id
     AND n.nutzername = :nutzername
ORDER BY t.datum ASC
