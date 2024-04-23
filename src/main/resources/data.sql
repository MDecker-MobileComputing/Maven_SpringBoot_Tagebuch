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

MERGE INTO nutzer (id, nutzername, passwort)
    KEY(nutzername)
    VALUES ( 3, 'claire', 't3st' );


-- Daten für die Tabelle "tagebucheintrag" einfügen

MERGE INTO tagebucheintrag (nutzer_id, datum, eintrag)
    KEY(nutzer_id, datum)
    VALUES ( 1, '2024-01-01', 'Erster Eintrag von Alice' );

MERGE INTO tagebucheintrag (nutzer_id, datum, eintrag)
    KEY(nutzer_id, datum)
    VALUES ( 1, '2024-01-02', 'Zweiter Eintrag von Alice: Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.' );

MERGE INTO tagebucheintrag (nutzer_id, datum, eintrag)
    KEY(nutzer_id, datum)
    VALUES ( 2, '2024-01-01', 'Erster Eintrag von Bob' );

MERGE INTO tagebucheintrag (nutzer_id, datum, eintrag)
    KEY(nutzer_id, datum)
    VALUES ( 2, '2024-03-15', 'Heute habe ich verschlafen' );


-- Vorlage INSERT-Statement für H2-Console:
--
--   INSERT INTO tagebucheintrag (nutzer_id, datum, eintrag)
--       VALUES (1, '2024-04-23', 'Heute ist es kalt aber sonnig.');
