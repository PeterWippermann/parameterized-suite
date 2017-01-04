package com.github.peterwippermann.junit4.parameterizedsuite.util;

import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.model.InitializationError;

/**
 * A collection of useful methods extracted and duplicated from {@link Suite}.
 * <p>
 * Code is under the license of JUnit: http://junit.org/junit4/license.html
 * <p>
 * 
 * Please see the package info of {@link com.github.peterwippermann.junit4.parameterizedsuite.util} for more details.
 */
public class SuiteUtil {

    /**
     * @see Suite#getAnnotatedClasses(Class)
     */
    public static Class<?>[] getSuiteClasses(Class<?> klass) throws InitializationError {
        SuiteClasses annotation = klass.getAnnotation(SuiteClasses.class);
        if (annotation == null) {
            throw new InitializationError(String.format("class '%s' must have a SuiteClasses annotation", klass.getName()));
        }
        return annotation.value();
    }

}
