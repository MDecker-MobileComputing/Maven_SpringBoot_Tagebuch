"use strict";

/**
 * Event-Handler-Funktion für Klick auf den "Speichern"-Button.
 *
 * @returns {Boolean} false, um Laden einer anderen Seite zu verhindern.
 */
function onSpeichernButton() {

    const textfeld = document.getElementById( "textfeldBeitrag" );
    if ( !textfeld ) {

        alert( "Interner Fehler: Textfeld nicht gefunden." );
        return false;
    }

    let beitrag = textfeld.value;
    if ( beitrag === undefined || beitrag === null ) {

        alert( "Interner Fehler: Text aus Textfeld konnte nicht ausgelesen werden." );
        return false;
    }

    beitrag = beitrag.trim();

    fetch( "/app/api/v1/eintrag", {
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        body: beitrag
    })
    .then( response => {

        if (!response.ok) {

            const statusText = `${response.statusText} (${response.status})`;
            throw new Error(`REST-Endpunkt hat Fehlercode zurückgeliefert: ${statusText}`);

        } else {

            return response.text() ;
        }
    })
    .then( data => {

        console.log( "Erfolg:", data );
        window.location.href = "hauptseite";
    })
    .catch( (fehler) => {

        console.error( "Fehler HTTP-POST-Request mit Tagebucheintrag:", fehler );
        alert( "Fehler beim Speichern: " + fehler );
    });

    return false;
}
