package com.dna.util;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * @author Cristian Laiun

 */
public interface Provider<T> extends Supplier<T>, Serializable {
}
