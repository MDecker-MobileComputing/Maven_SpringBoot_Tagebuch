"use strict";

/**
 * Hilfsunktion: Wert von bestimmten Cookie auslesen.
 *
 * @param {*} name Name des auszulesenden Cookies, v.a. "JSESSIONID"
 *
 * @returns Wert des Cookies oder leerer String, falls nicht gefunden
 */
function holeCookie(name) {

    const nameEQ      = name + "=";
    const cookieArray = document.cookie.split( ";" );

    for(let i = 0; i < cookieArray.length; i++) {

        let cookie = cookieArray[i];
        while ( cookie.charAt(0) === ' ' ) {

            cookie = cookie.substring( 1, cookie.length );
        }
        if ( cookie.indexOf( nameEQ ) === 0) {

            return cookie.substring( nameEQ.length, cookie.length );
        }
    }

    console.error( `Cookie "${name}" nicht gefunden.` );
    return "";
}

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

    const jsessionId = holeCookie( "JSESSIONID" ); // Session-ID aus Cookie auslesen, um sie beim POST-Request mitzugeben

    fetch( "/api/v1/eintrag", {
        method: "POST",
        headers: {
            "Content-Type": "text/plain",
            "Cookie": `JSESSIONID=${jsessionId}` },
        body: beitrag
    })
    .then( response => {

        if (!response.ok) {

            const statusText = `${response.statusText} (${response.status})`;
            throw new Error( `REST-Endpunkt hat Fehlercode zurückgeliefert: ${statusText}` );

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
