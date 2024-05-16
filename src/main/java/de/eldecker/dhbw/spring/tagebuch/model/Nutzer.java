package de.eldecker.dhbw.spring.tagebuch.model;


/**
 * Ein Objekt dieser Record-Klasse repräsentiert ein Nutzer-Objekt aus der
 * gleichnamigen Datenbanktabelle.
 * 
 * @param id Primärzschlüssel
 * 
 * @param nutzername Eindeutiger Nutzername, z.B. "alice"
 * 
 * @param passwort Passwort im Klartext
 */
public record Nutzer( int    id, 
                      String nutzername, 
                      String passwort 
                    ) {
}
