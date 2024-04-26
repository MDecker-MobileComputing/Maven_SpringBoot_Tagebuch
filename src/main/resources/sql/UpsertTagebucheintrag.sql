--- UPSERT (UPdate oder inSERt) für Tagebucheintrag für einen Nutzer am heutigen Tag
MERGE INTO tagebucheintrag AS t
    USING (
        SELECT id AS nutzer_id FROM nutzer WHERE nutzername = :nutzername
    ) AS n
    ON t.nutzer_id = n.nutzer_id AND t.datum = CURRENT_DATE
WHEN MATCHED THEN UPDATE SET eintrag = :eintrag
WHEN NOT MATCHED THEN INSERT (nutzer_id, datum, eintrag) VALUES (n.nutzer_id, CURRENT_DATE, :eintrag);