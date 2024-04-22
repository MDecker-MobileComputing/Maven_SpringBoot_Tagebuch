package de.eldecker.dhbw.spring.tagebuch.model;


/**
 * Einzelner Tagebucheintrag.
 * 
 * @param id Eindeutige ID des Tagebucheintrags (über alle Nutzer hinweg)
 * 
 * @param text Eigentlich Inhalt des Eintrags
 * 
 * @param datum Tag des Eintrags; pro Tag und Nutzer darf es höchstens 
 *              einen Tagebuch geben. Datumwert hat Format
 *              {@code dd.mm.yyyy (E)}, also z.B. {@code 22.04.2024 (Mo.)}.
 */
public record TagebuchEintrag( int id, 
                               String text, 
                               String datum ) {
}
