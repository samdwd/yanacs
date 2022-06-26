package net.javaguides.springboot.springsecurity.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utiles {
	 public static String obtenerFechaYHoraActual() {
	        String formato = "yyyy-MM-dd HH:mm:ss";
	        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
	        LocalDateTime ahora = LocalDateTime.now();
	        return formateador.format(ahora);
	    }
	 public static String obtenerSoloFechaActual() {
	        String formato = "dd-MM-yyyy";
	        DateTimeFormatter formateador = DateTimeFormatter.ofPattern(formato);
	        LocalDateTime ahora = LocalDateTime.now();
	        return formateador.format(ahora);
	    }
}
