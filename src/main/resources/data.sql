/* MERGE statt INSERT, damit keine Fehlermeldung bei mehrfachem Ausführen der Datei auftritt. */


MERGE INTO nutzer (nutzername, passwort)
    KEY(nutzername) -- Anhand Werten in dieser Spalte wird entschieden, ob ein Datensatz neu angelegt oder aktualisiert wird.
    VALUES (
        'alice',
        'g3h3im'
    );

MERGE INTO nutzer (nutzername, passwort)
    KEY(nutzername)
    VALUES (
        'bob',
        's3cr3t'
    );

