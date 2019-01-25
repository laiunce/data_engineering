package com.dna.converter;

/**
 * @author Cristian Laiun

 */
public interface StringConverter<O> extends Converter<String, O> {
    O convert(String input) throws Exception;
}
