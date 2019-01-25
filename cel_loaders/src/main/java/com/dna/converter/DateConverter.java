package com.dna.converter;

import java.time.LocalDate;

/**
 * @author Cristian Laiun

 */
public interface DateConverter extends StringConverter<LocalDate> {
    DateConverter ISO_DATE_PARSER = new IsoDateConverter();
    DateConverter SHORT_DATE_PARSER = new ShortDateConverter();
}
