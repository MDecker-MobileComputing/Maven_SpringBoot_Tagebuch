package de.eldecker.dhbw.spring.tagebuch.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Controller, der die Anfragen für die Thymeleaf-Views bearbeitet.
 * Alle Pfade müssen mit {@code /app/} beginnen.
 */
@Controller
@RequestMapping("/app")
public class ThymeleafWebController {

    /**
     * Hauptseite der Anwendung anzeigen: Liste mit Tagebucheinträgen.
     *
     * @param model Model, in das die Daten für die Platzhalter in der
     *              Template-Datei geschrieben werden
     *
     * @return Name der Template-Datei, die angezeigt werden soll;
     *         wird in Ordner {@code src/main/resources/templates/} gesucht.
     */
    @GetMapping("/hauptseite")
    public String kuerzelAufloesen( Model model ) {

        return "hauptseite";
    }

}
