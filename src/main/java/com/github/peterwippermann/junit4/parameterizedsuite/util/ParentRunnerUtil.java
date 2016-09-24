package com.github.peterwippermann.junit4.parameterizedsuite.util;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * A collection of useful methods extracted and duplicated from {@link ParentRunner}.
 * <p>
 * Code is under the license of JUnit: http://junit.org/junit4/license.html
 * <p>
 * 
 * Please see the package info of {@link com.github.peterwippermann.junit4.parameterizedsuite.util} for more details.
 */
public class ParentRunnerUtil {

    /**
     * Returns a {@link Statement}: run all non-overridden {@code @BeforeClass} methods on this class
     * and superclasses before executing {@code statement}; if any throws an
     * Exception, stop execution and pass the exception on.
     * 
     * @see ParentRunner#withBeforeClasses(org.junit.runners.model.Statement)
     */
    public static Statement withBeforeClasses(Statement statement, TestClass testClass) {
        List<FrameworkMethod> befores = testClass
                .getAnnotatedMethods(BeforeClass.class);
        return befores.isEmpty() ? statement :
                new RunBefores(statement, befores, null);
    }

    /**
     * Returns a {@link Statement}: run all non-overridden {@code @AfterClass} methods on this class
     * and superclasses after executing {@code statement}; all AfterClass methods are
     * always executed: exceptions thrown by previous steps are combined, if
     * necessary, with exceptions from AfterClass methods into a
     * {@link org.junit.runners.model.MultipleFailureException}.
     * 
     * @see ParentRunner#withAfterClasses(org.junit.runners.model.Statement)
     */
    public static Statement withAfterClasses(Statement statement, TestClass testClass) {
        List<FrameworkMethod> afters = testClass
                .getAnnotatedMethods(AfterClass.class);
        return afters.isEmpty() ? statement :
                new RunAfters(statement, afters, null);
    }

    /**
     * Returns a {@link Statement}: apply all
     * static fields assignable to {@link TestRule}
     * annotated with {@link ClassRule}.
     *
     * @param statement the base statement
     * @param testClass 
     * @param description the description to pass to the {@link Rule}s
     * @return a RunRules statement if any class-level {@link Rule}s are
     *         found, or the base statement
     *         
     * @see ParentRunner#withClassRules(org.junit.runners.model.Statement)
     */
    public static Statement withClassRules(Statement statement, TestClass testClass, Description description) {
        List<TestRule> classRules = getClassRules(testClass);
        return classRules.isEmpty() ? statement :
                new RunRules(statement, classRules, description);
    }

    /**
     * @param testClass 
     * @return the {@code ClassRule}s that can transform the block that runs
     *         each method in the tested class.
     *         
     * @see ParentRunner#classRules()
     */
    public static List<TestRule> getClassRules(TestClass testClass) {
        List<TestRule> result = testClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class);
        result.addAll(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));
        return result;
    }

}
