package de.eldecker.dhbw.spring.tagebuch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Klasse mit Einstiegsmethode der Anwendung.
 */
@SpringBootApplication
public class TagebuchWebAppApplication {

	public static void main( String[] args ) {
	    
		SpringApplication.run( TagebuchWebAppApplication.class, args );
	}

}
