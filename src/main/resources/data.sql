-- MERGE statt INSERT, damit keine Fehlermeldung bei mehrfachem Ausführen der Datei auftritt.
--
-- Anhand mit KEY() definierten Spalten wird entschieden, ob ein Datensatz neu angelegt oder aktualisiert wird.


-- Daten für die Tabelle "nutzer" einfügen

MERGE INTO nutzer (id, nutzername, passwort)
    KEY(nutzername)
    VALUES ( 1, 'alice', 'g3h3im' );

MERGE INTO nutzer (id, nutzername, passwort)
    KEY(nutzername)
    VALUES ( 2, 'bob', 's3cr3t' );


-- Daten für die Tabelle "tagebucheintrag" einfügen

MERGE INTO tagebucheintrag (nutzer_id, datum, eintrag)
    KEY(nutzer_id, datum)
    VALUES ( 1, '2024-01-01', 'Erster Eintrag von Alice' );

MERGE INTO tagebucheintrag (nutzer_id, datum, eintrag)
    KEY(nutzer_id, datum)
    VALUES ( 1, '2024-01-02', 'Zweiter Eintrag von Alice' );

MERGE INTO tagebucheintrag (nutzer_id, datum, eintrag)
    KEY(nutzer_id, datum)
    VALUES ( 2, '2024-01-01', 'Erster Eintrag von Bob' );
