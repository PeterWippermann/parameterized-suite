package com.github.peterwippermann.junit4.parameterizedsuite;

import java.lang.reflect.Array;

/**
 * This singleton stores a parameter. This way parameters can be transferred between test classes.<br>
 * The type of the parameter is unbound and can also be an {@link Array}. Null is interpreted as "parameter is not set".
 * <p>
 * This implementation is not thread-safe. Concurrent implementations e.g. could use
 * {@link ThreadLocal}.
 *
 */
public class ParameterContext {
    private static Object context;

    public static <P> void setParameter(P parameter) {
        context = parameter;
    }

    /**
     * Retrieve the stored parameter.
     * 
     * @param parameterClass - Enables type-safety at compile time.
     */
    @SuppressWarnings("unchecked")
    public static <P> P getParameter(Class<P> parameterClass) {
        return (P) context;
    }

    public static void removeParameter() {
        setParameter(null);
    }

    /**
     * @return true, if parameter is not null.
     */
    public static boolean isParameterSet() {
        return getParameter(Object.class) != null;
    }
}


