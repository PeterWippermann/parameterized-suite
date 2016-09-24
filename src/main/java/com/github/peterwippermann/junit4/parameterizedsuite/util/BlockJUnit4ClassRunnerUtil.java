package com.github.peterwippermann.junit4.parameterizedsuite.util;

import java.util.List;

import org.junit.Rule;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * A collection of useful methods extracted and duplicated from {@link BlockJUnit4ClassRunner}.
 * <p>
 * Code is under the license of JUnit: http://junit.org/junit4/license.html
 * <p>
 * 
 * Please see the package info of {@link com.github.peterwippermann.junit4.parameterizedsuite.util} for more details.
 */
public class BlockJUnit4ClassRunnerUtil {

    /**
     * Returns a {@link Statement}: apply all non-static fields annotated with {@link Rule}.
     * <p>
     * This method has a slighty changed signature compared to its original, replacing the
     * {@link FrameworkMethod} parameter with a {@link Description}.
     * 
     * @param description The description passed to the {@link Rule}
     * @param statement The base statement
     *
     * @return a RunRules statement if any class-level {@link Rule}s are found, or the base
     *         statement
     *         
     * @see BlockJUnit4ClassRunner#withTestRules(FrameworkMethod, List<TestRule>, Statement)
     */
    public static Statement withTestRules(List<TestRule> testRules, Description description, Statement statement) {
        return testRules.isEmpty() ? statement : new RunRules(statement, testRules, description);
    }

    /**
     * @param target the test case instance
     * @param testClass the {@link TestClass} where the {@link TestRule} annotations have been
     *        defined.
     * @return a list of TestRules that should be applied when executing this test
     * 
     * @see BlockJUnit4ClassRunner#getTestRules(Object)
     */
    public static List<TestRule> getTestRules(Object target, TestClass testClass) {
        List<TestRule> result = testClass.getAnnotatedMethodValues(target, Rule.class, TestRule.class);
        result.addAll(testClass.getAnnotatedFieldValues(target, Rule.class, TestRule.class));
        return result;
    }

}
