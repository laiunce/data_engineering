package com.dna.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Cristian Laiun

 */
public class IsoDateConverter implements DateConverter {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate convert(String input) throws Exception {
        input = input.substring(0, 10);
        return LocalDate.parse(input, DATE_FORMATTER);
    }
}
