package com.tarefas.Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    // Método estático que retorna a data atual formatada
    public static String getCurrentDateTimeFormatted() {
        return LocalDateTime.now().format(FORMATTER);
    }

    // Método estático que formata qualquer LocalDateTime passado como parâmetro
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null; // ou lançar uma exceção, dependendo do seu caso
        }
        return dateTime.format(FORMATTER);
    }
}
