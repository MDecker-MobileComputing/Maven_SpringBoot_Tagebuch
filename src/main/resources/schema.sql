
-- Damit die SQL-Statements in dieser Datei beim Start der Anwendung ausgeführt werden,
-- muss in der Datei application.properties die folgende Zeile stehen:
-- spring.sql.init.mode=always

CREATE TABLE IF NOT EXISTS nutzer (

    id                  INT AUTO_INCREMENT PRIMARY KEY,
    nutzername          VARCHAR(100) NOT NULL,
    passwort            VARCHAR(255)
);

-- In der Spalte "passwort" wird das Passwort im Klartext gespeichert.
-- Das ist natürlich nicht sicher, aber für diese Beispielanwendung ausreichend.
-- In einer echten Anwendung sollte nur der Hashwert des Passworts in der Datenbank
-- gespeichert werden

