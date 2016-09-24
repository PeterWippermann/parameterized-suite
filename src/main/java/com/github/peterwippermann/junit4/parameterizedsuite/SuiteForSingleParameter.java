package com.github.peterwippermann.junit4.parameterizedsuite;


import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.Statement;

import com.github.peterwippermann.junit4.parameterizedsuite.util.BlockJUnit4ClassRunnerWithParametersUtil;

/**
 * This extension of {@link Suite} is used by {@link ParameterizedSuite} to fork a run of the
 * suite's children for a single parameter.
 * 
 * @author Peter Wippermann
 *
 */
public final class SuiteForSingleParameter extends Suite {
    private String suiteName;
    private Object[] singleParameter;

    public SuiteForSingleParameter(RunnerBuilder runnerBuilder, Class<?> forkingSuiteClass, Class<?>[] classes, String suiteName, Object[] singleParameter)
            throws InitializationError {
        // By passing "forkingSuiteClass" (which is the forking ParameterizedSuite), the JUnit
        // framework will build the internal testClass attribute from the ParameterizedSuite and not
        // from this virtual, forked Suite.
        // This way @Before/After/Class can be evaluated.
        super(runnerBuilder, forkingSuiteClass, classes);

        this.suiteName = suiteName;
        this.singleParameter = singleParameter;
    }


    protected String getName() {
        return this.suiteName;
    }


    protected Statement classBlock(RunNotifier notifier) {
        return BlockJUnit4ClassRunnerWithParametersUtil.buildStatementWithTestRules(childrenInvoker(notifier), getTestClass(), getDescription(),
                getActiveParameter());
    }


    protected Object[] getActiveParameter() {
        return this.singleParameter;
    }
}


