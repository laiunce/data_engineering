package com.dna.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristian Laiun

 */
public class ClassUtil {

    protected ClassUtil() {
    }

    public static <T> List<Class<T>> getClasses(String[] classNames, Class<T> type) throws ClassNotFoundException {
        if (classNames.length == 0) return new ArrayList<>();
        List<Class<T>> classList = new ArrayList<>();

        for (String className : classNames) {
            className = className.trim();
            Class<?> clazz = Class.forName(className);
            if (type == null || type.isAssignableFrom(clazz)) {
                classList.add((Class<T>) clazz);
            }
        }

        return classList;
    }
}
