package com.dna.converter;

import java.io.Serializable;

/**
 * @author Cristian Laiun

 */
public interface Converter<I, O> extends Serializable {
    O convert(I input) throws Exception;
}
