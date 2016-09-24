package com.github.peterwippermann.junit4.parameterizedsuite.util;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

/**
 * A collection of useful methods extracted and duplicated from {@link Parameterized}.
 * <p>
 * Code is under the license of JUnit: http://junit.org/junit4/license.html
 * <p>
 * 
 * Please see the package info of {@link com.github.peterwippermann.junit4.parameterizedsuite.util} for more details.
 */
public class ParameterizedUtil {

    /**
     * @param testClass
     * @return the parameters from the method annotated with {@link Parameters}
     * @throws Throwable
     * 
     * @see Parameterized#allParameters()
     */
    @SuppressWarnings("unchecked")
    public static Iterable<Object> getParameters(TestClass testClass) throws Throwable {
        Object parameters = getParametersMethod(testClass).invokeExplosively(null);
        if (parameters instanceof Iterable) {
            return (Iterable<Object>) parameters;
        } else if (parameters instanceof Object[]) {
            return Arrays.asList((Object[]) parameters);
        } else {
            throw parametersMethodReturnedWrongType(testClass);
        }
    }

    /**
     * @param testClass
     * @return the method annotated with {@link Parameters}
     * @throws Exception
     * 
     * @see Parameterized#allParameters()
     */
    private static FrameworkMethod getParametersMethod(TestClass testClass) throws Exception {
        List<FrameworkMethod> methods = testClass.getAnnotatedMethods(Parameters.class);
        for (FrameworkMethod each : methods) {
            if (each.isStatic() && each.isPublic()) {
                return each;
            }
        }

        throw new Exception("No public static parameters method on class " + testClass.getName());
    }

    /**
     * @see Parameterized#parametersMethodReturnedWrongType()
     */
    private static Exception parametersMethodReturnedWrongType(TestClass testClass) throws Exception {
        String className = testClass.getName();
        String methodName = getParametersMethod(testClass).getName();
        String message = MessageFormat.format("{0}.{1}() must return an Iterable of arrays.", className, methodName);
        return new Exception(message);
    }

    /**
     * Parameters of a test can either be 1.) a set of {@link Object}s or 2.) a set of Arrays of
     * Objects. This method normalizes both variants to Object[]. Single Objects are therefore
     * stored in a new Array.
     * 
     * @param singleParameterAsArrayOrObject
     * 
     * @see {@link Parameterized#createTestWithNotNormalizedParameters(String, int, Object)}
     */
    public static Object[] normalizeParameter(Object singleParameterAsArrayOrObject) {
        return (singleParameterAsArrayOrObject instanceof Object[]) ? (Object[]) singleParameterAsArrayOrObject : new Object[] {singleParameterAsArrayOrObject};
    }

    /**
     * Builds a name for a test from a given name pattern by inserting the current parameter and its index.
     * 
     * @param pattern
     * @param index
     * @param parameters
     * 
     * @see {@link Parameterized#createTestWithParameters(TestClass, String, int, Object[])}
     */
    public static String buildTestName(String pattern, int index, Object[] parameters) {
        String finalPattern = pattern.replaceAll("\\{index\\}", Integer.toString(index));
        return "[" + MessageFormat.format(finalPattern, parameters) + "]";
    }

    /**
     * @param testClass
     * @return The name pattern for tests as defined in the {@link Parameters} annotation.
     * @throws Exception
     * 
     * @see {@link Parameterized#Parameterized(Class)}
     */
    public static String getNamePatternForParameters(TestClass testClass) throws Exception {
        Parameters parametersAnnotation = getParametersMethod(testClass).getAnnotation(Parameters.class);
        return parametersAnnotation.name();
    }

}
