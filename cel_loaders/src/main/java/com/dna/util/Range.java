package com.dna.util;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Cristian Laiun

 */
@ToString
@EqualsAndHashCode
public class Range<V> implements Map.Entry<V, V>, Comparable<Range<V>>, Serializable {

    private static final long serialVersionUID = 4954918890077093841L;

    public final V From;
    public final V To;

    public Range(V From, V To) {
        this.From = From;
        this.To = To;
    }
    
    public static <V> Range<V> of(V From, V To) {
        return new Range<>(From, To);
    }

    public V from() {
        return From;
    }

    public V to() {
        return To;
    }

    public final V getKey() {
        return this.from();
    }

    public V getValue() {
        return this.to();
    }

    public int compareTo(Range<V> other) {
        return (new CompareToBuilder()).append(this.from(), other.from()).append(this.to(), other.to()).toComparison();
    }

    @Override
    public V setValue(V value) {
        throw new UnsupportedOperationException();
    }
}
