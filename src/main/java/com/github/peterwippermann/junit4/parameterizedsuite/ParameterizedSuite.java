package com.github.peterwippermann.junit4.parameterizedsuite;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized;
import org.junit.runners.ParentRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.Statement;

import com.github.peterwippermann.junit4.parameterizedsuite.parameter.Parameter;
import com.github.peterwippermann.junit4.parameterizedsuite.util.BlockJUnit4ClassRunnerWithParametersUtil;
import com.github.peterwippermann.junit4.parameterizedsuite.util.ParameterizedUtil;
import com.github.peterwippermann.junit4.parameterizedsuite.util.SuiteUtil;

/**
 * A replacement for {@link Suite} that combines the features of {@link Suite} with
 * {@link Parameterized}.<br>
 * Moreover, as an additional feature: If the suite test class is annotated with {@link TestRule},
 * {@link ClassRule}, {@link Before} or {@link After}, these will be evaluated as well.
 * <p>
 * 
 * This implementation doesn't extend {@link Suite}, because {@link Suite} doesn't allow to extend
 * the mechanism of determining the child {@link Runner}s.
 * 
 * @author Peter Wippermann
 *
 */
public class ParameterizedSuite extends ParentRunner<Runner> {

    private enum Mode {
        /**
         * The {@link ParameterizedSuite} is the first of its kind in a hierarchy of tests.
         */
        ROOT,

        /**
         * The {@link ParameterizedSuite} is subordinated to other {@link ParameterizedSuite}s in
         * the hierarchy of tests.
         */
        NESTED;
    }

    public static final Class<Object[]> PARAMETER_TYPE = Object[].class;

    private final List<Runner> runners;

    private final Object[] currentlyActiveParameter;

    private final Mode mode;

    public ParameterizedSuite(Class<?> suiteTestClass, RunnerBuilder runnerBuilder) throws Throwable {
        super(suiteTestClass);
        Class<?>[] childTestClasses = SuiteUtil.getSuiteClasses(suiteTestClass);
        mode = determineRuntimeMode();

        if (Mode.ROOT.equals(mode)) {
            // Forking for each parameter is necessary.
            Iterable<Object> parametersOfSuiteClass = ParameterizedUtil.getParameters(getTestClass());
            String parametersNamePattern = ParameterizedUtil.getNamePatternForParameters(getTestClass());
            currentlyActiveParameter = null;

            this.runners = createSuiteRunnersPerParameter(suiteTestClass, runnerBuilder, childTestClasses, parametersOfSuiteClass, parametersNamePattern);
        } else {
            // Forking has already been done by a ParameterizedSuite that is superior in the test
            // hierarchy. Create Runners for children like a normal Suite would do.
            currentlyActiveParameter = ParameterContext.getParameterAsArray();

            this.runners = createRunnersForChildTestClasses(suiteTestClass, runnerBuilder, childTestClasses);
        }
    }

    private static Mode determineRuntimeMode() {
        if (ParameterContext.isParameterSet()) {
            return Mode.NESTED;
        } else {
            return Mode.ROOT;
        }
    }

    protected Statement classBlock(RunNotifier notifier) {
        Statement statementWithChildren = childrenInvoker(notifier);
        if (Mode.ROOT.equals(mode)) {
            // Do nothing but execute the runners of the forked SuiteForSingleParameter.
            return statementWithChildren;
        }
        return BlockJUnit4ClassRunnerWithParametersUtil.buildStatementWithTestRules(statementWithChildren, getTestClass(), getDescription(),
                currentlyActiveParameter);
    }

//    protected Object[] getCurrentlyActiveParameter() {
//        return currentlyActiveParameter;
//    }

    /**
     * Builds the {@link Runner}s the same way as in {@link Suite#Suite(RunnerBuilder, Class[])}
     * 
     * @param suiteTestClass
     * @param runnerBuilder
     * @param suiteChildClasses
     * @return
     * @throws InitializationError
     */
    protected List<Runner> createRunnersForChildTestClasses(Class<?> suiteTestClass, RunnerBuilder runnerBuilder, Class<?>[] suiteChildClasses)
            throws InitializationError {
        return runnerBuilder.runners(suiteTestClass, suiteChildClasses);
    }

    /**
     * For every parameter a {@link Runner} will be built that runs all child tests for the given
     * parameter.
     * <p>
     * The order of execution is determined in this method. The current implementation runs all
     * tests per parameter before going over to the next parameter. Alternative implementations
     * could run a test for all parameters first before going over to the next test.
     * 
     * @param suiteTestClass
     * @param runnerBuilder
     * @param suiteChildClasses
     * @param parameters
     * @param parametersNamePattern - A pattern that will be used to create a name for the forked
     *        test executions. Placeholders for index and parameters will be replaced.
     * @return
     * @throws InitializationError
     */
    protected List<Runner> createSuiteRunnersPerParameter(Class<?> suiteTestClass, RunnerBuilder runnerBuilder, Class<?>[] suiteChildClasses,
            Iterable<Object> parameters, String parametersNamePattern) throws InitializationError {
        List<Runner> runners = new LinkedList<Runner>();

        int parameterIndex = 0;
        for (Object singleParameterRaw : parameters) {
        	Parameter parameter = Parameter.from(singleParameterRaw);
            String nameForSingleParameterSuite = ParameterizedUtil.buildTestName(parametersNamePattern, parameterIndex++, parameter.asNormalized());
            runners.add(setContextAndBuildSuiteForSingleParameter(suiteTestClass, runnerBuilder, suiteChildClasses, parameter,
                    nameForSingleParameterSuite));
        }
        return runners;
    }


    private SuiteForSingleParameter setContextAndBuildSuiteForSingleParameter(Class<?> suiteTestClass, RunnerBuilder runnerBuilder,
            Class<?>[] suiteChildClasses, Parameter parameter, String suiteName) throws InitializationError {
        try {
            ParameterContext.setParameter(parameter);
            return new SuiteForSingleParameter(runnerBuilder, suiteTestClass, suiteChildClasses, suiteName, parameter.asNormalized());
        } finally {
            ParameterContext.removeParameter();
        }
    }

    protected List<Runner> getChildren() {
        return this.runners;
    }

    protected Description describeChild(Runner child) {
        return child.getDescription();
    }

    protected void runChild(Runner runner, RunNotifier notifier) {
        runner.run(notifier);
    }
}


