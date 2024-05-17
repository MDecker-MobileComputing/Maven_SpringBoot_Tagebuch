package de.eldecker.dhbw.spring.tagebuch.model;


/**
 * Klassen, deren Objekte einzelne Tagebucheinträge repräsentieren.
 *
 * @param id Eindeutige ID des Tagebucheintrags (über alle Nutzer hinweg,
 *           weil Primärschlüssel für DB-Tabelle).
 *
 * @param text Eigentlich Inhalt des Eintrags
 *
 * @param datum Tag des Eintrags; pro Tag und Nutzer darf es höchstens
 *              einen Tagebuch geben. Datumwert hat Format
 *              {@code dd.mm.yyyy (E)}, also z.B. {@code 22.04.2024 (Mo.)}.
 *
 * @param link Relativer Link auf Detailseite des Tagebucheintrags;
 *             wenn das Objekt für eine Detailseite erzeugt wird, dann
 *             enthält dieses Feld einen leerer String.
 */
public record TagebuchEintrag( int    id    ,
                               String text  ,
                               String datum ,
                               String link
                             ) {
}
