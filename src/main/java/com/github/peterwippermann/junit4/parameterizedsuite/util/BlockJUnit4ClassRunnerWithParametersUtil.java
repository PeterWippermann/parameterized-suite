package com.github.peterwippermann.junit4.parameterizedsuite.util;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.junit.runners.parameterized.BlockJUnit4ClassRunnerWithParameters;

/**
 * A collection of useful methods extracted and duplicated from
 * {@link BlockJUnit4ClassRunnerWithParameters}.
 * <p>
 * Code is under the license of JUnit: http://junit.org/junit4/license.html
 * <p>
 * 
 * Please see the package info of {@link com.github.peterwippermann.junit4.parameterizedsuite.util} for more details.
 */
public class BlockJUnit4ClassRunnerWithParametersUtil {

    /**
     * @see BlockJUnit4ClassRunnerWithParameters.InjectionType()
     */
    private enum InjectionType {
        CONSTRUCTOR, FIELD
    }

    /**
     * @see BlockJUnit4ClassRunnerWithParameters#createTest()
     */
    public static Object createInstanceOfParameterizedTest(TestClass testClass, Object[] parameters) throws Exception {
        InjectionType injectionType = getInjectionType(testClass);
        switch (injectionType) {
            case CONSTRUCTOR:
                return createTestUsingConstructorInjection(testClass, parameters);
            case FIELD:
                return createTestUsingFieldInjection(testClass, parameters);
            default:
                throw new IllegalStateException("The injection type " + injectionType + " is not supported.");
        }
    }

    /**
     * @see BlockJUnit4ClassRunnerWithParameters#createTestUsingConstructorInjection()
     */
    private static Object createTestUsingConstructorInjection(TestClass testClass, Object[] parameters) throws Exception {
        return testClass.getOnlyConstructor().newInstance(parameters);
    }

    /**
     * @see BlockJUnit4ClassRunnerWithParameters#createTestUsingFieldInjection()
     */
    private static Object createTestUsingFieldInjection(TestClass testClass, Object[] parameters) throws Exception {
        List<FrameworkField> annotatedFieldsByParameter = getAnnotatedFieldsByParameter(testClass);
        if (annotatedFieldsByParameter.size() != parameters.length) {
            throw new Exception("Wrong number of parameters and @Parameter fields." + " @Parameter fields counted: " + annotatedFieldsByParameter.size()
                    + ", available parameters: " + parameters.length + ".");
        }
        Object testClassInstance = testClass.getJavaClass().newInstance();
        for (FrameworkField each : annotatedFieldsByParameter) {
            Field field = each.getField();
            Parameter annotation = field.getAnnotation(Parameter.class);
            int index = annotation.value();
            try {
                field.set(testClassInstance, parameters[index]);
            } catch (IllegalArgumentException iare) {
                throw new Exception(
                        testClass.getName() + ": Trying to set " + field.getName() + " with the value " + parameters[index] + " that is not the right type ("
                                + parameters[index].getClass().getSimpleName() + " instead of " + field.getType().getSimpleName() + ").",
                        iare);
            }
        }
        return testClassInstance;
    }

    /**
     * @see BlockJUnit4ClassRunnerWithParameters#getAnnotatedFieldsByParameter()
     */
    public static List<FrameworkField> getAnnotatedFieldsByParameter(TestClass testClass) {
        return testClass.getAnnotatedFields(Parameter.class);
    }

    /**
     * @see BlockJUnit4ClassRunnerWithParameters#getInjectionType()
     */
    private static InjectionType getInjectionType(TestClass testClass) {
        if (fieldsAreAnnotated(testClass)) {
            return InjectionType.FIELD;
        } else {
            return InjectionType.CONSTRUCTOR;
        }
    }

    /**
     * @see BlockJUnit4ClassRunnerWithParameters#fieldsAreAnnotated()
     */
    private static boolean fieldsAreAnnotated(TestClass testClass) {
        return !getAnnotatedFieldsByParameter(testClass).isEmpty();
    }

    /**
     * Extends a given {@link Statement} for a {@link TestClass} with the evaluation of
     * {@link TestRule}, {@link ClassRule}, {@link Before} and {@link After}.
     * <p>
     * Therefore the test class will be instantiated and parameters will be injected with the same
     * mechanism as in {@link Parameterized}.
     * 
     * @param baseStatementWithChildren - A {@link Statement} that includes execution of the test's
     *        children
     * @param testClass - The {@link TestClass} of the test.
     * @param description - The {@link Description} will be passed to the {@link Rule}s and
     *        {@link ClassRule}s.
     * @param parametersToInject - The parameters will be injected in attributes annotated with
     *        {@link Parameter} or passed to the constructor otherwise.
     * 
     * @see {@link BlockJUnit4ClassRunner#methodBlock(FrameworkMethod)}
     * @see {@link BlockJUnit4ClassRunnerWithParameters#createTest()}
     */
    public static Statement buildStatementWithTestRules(Statement baseStatementWithChildren, final TestClass testClass, Description description,
            final Object[] parametersToInject) {
        final Object test;
        try {
            test = new ReflectiveCallable() {
                protected Object runReflectiveCall() throws Throwable {
                    return createInstanceOfParameterizedTest(testClass, parametersToInject);
                }
            }.run();
        } catch (Throwable e) {
            return new Fail(e);
        }

        List<TestRule> testRules = BlockJUnit4ClassRunnerUtil.getTestRules(test, testClass);
        Statement statement = BlockJUnit4ClassRunnerUtil.withTestRules(testRules, description, baseStatementWithChildren);

        statement = ParentRunnerUtil.withBeforeClasses(statement, testClass);
        statement = ParentRunnerUtil.withAfterClasses(statement, testClass);
        statement = ParentRunnerUtil.withClassRules(statement, testClass, description);
        return statement;
    }

}
