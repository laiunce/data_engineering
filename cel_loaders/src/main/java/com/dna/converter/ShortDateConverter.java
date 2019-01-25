package com.dna.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Cristian Laiun

 */
public class ShortDateConverter implements DateConverter {

	private static final long serialVersionUID = 9006229153729175660L;
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("M/d/yyyy");

    @Override
    public LocalDate convert(String input) throws Exception {
        return LocalDate.parse(input, DATE_FORMATTER);
    }
}
