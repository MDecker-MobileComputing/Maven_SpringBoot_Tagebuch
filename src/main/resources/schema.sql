

-- Damit die SQL-Statements in dieser Datei beim Start der Anwendung ausgeführt werden,
-- muss in der Datei application.properties die folgende Zeile stehen:
-- spring.sql.init.mode=always

CREATE TABLE IF NOT EXISTS nutzer (

    id          INT AUTO_INCREMENT PRIMARY KEY,
    nutzername  VARCHAR(100) NOT NULL,
    passwort    VARCHAR(255)
);

-- In der Spalte "passwort" wird das Passwort im Klartext gespeichert.
-- Das ist natürlich nicht sicher, aber für diese Beispielanwendung ausreichend.
-- In einer echten Anwendung sollte nur der Hashwert des Passworts in der Datenbank
-- gespeichert werden



-- Tabelle mit Tagebucheinträgen; jeder Nutzer kann für jeden Tag höchstens
-- einen Eintrag anlegen; daher ist der Primärschlüssel ein zusammengesetzter
-- Schlüssel aus "nutzer_id" und "datum".
CREATE TABLE IF NOT EXISTS tagebucheintrag (

    id          INT AUTO_INCREMENT PRIMARY KEY,
    nutzer_id   INT,
    datum       DATE,
    eintrag     TEXT,

    CONSTRAINT FK_TAGEBUCHEINTRAG_ZU_NUTZER
        FOREIGN KEY (nutzer_id)
        REFERENCES nutzer(id)
);

-- Die Fremdschlüsselbeziehung taucht danach in der Tabelle CONSTRAINT_COLUMN_USAGE auf:
-- SELECT * FROM INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE
