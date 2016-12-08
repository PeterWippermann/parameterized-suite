package com.github.peterwippermann.junit4.parameterizedsuite.names;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Request;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite.SuiteClasses;

import com.github.peterwippermann.junit4.parameterizedsuite.ParameterContext;
import com.github.peterwippermann.junit4.parameterizedsuite.ParameterizedSuite;

/**
 * Checks the display name of the forked suites as well as for the suite's child
 * classes.
 */
public class DisplayNamesTest {
	@RunWith(ParameterizedSuite.class)
	@SuiteClasses({ ParameterizedTestA.class, ParameterizedTestB.class })
	public static class ParametrizedSuiteWithIndexInName {

		public ParametrizedSuiteWithIndexInName(String ignorable) {
		}

		@Parameters(name = "Suite: {index}-{0}")
		public static Collection<Object[]> data() {
			// Parameter value will match its index
			return Arrays.asList(new Object[][] { { "0" }, { "1" }, { "2" } });
		}
	}

	@RunWith(Parameterized.class)
	public static class ParameterizedTestA {
		public ParameterizedTestA(String ignorable) {
		}

		@Parameters(name = "Single A: {index}-{0}")
		public static Iterable<Object[]> params() {
			return getParametersFromContext();
		}

		protected static Iterable<Object[]> getParametersFromContext() {
			if (ParameterContext.isParameterSet()) {
				return Collections.singletonList(ParameterContext.getParameter(Object[].class));
			}
			return Collections.singletonList(new Object[] {"ignorable"});
		}

		@Test
		public void test() {
		}
	}

	@RunWith(Parameterized.class)
	public static class ParameterizedTestB extends ParameterizedTestA {
		public ParameterizedTestB(String ignorable) {
			super(ignorable);
		}

		@Parameters(name = "Single B: {index}-{0}")
		public static Iterable<Object[]> params() {
			return getParametersFromContext();
		}

		@Test
		public void test() {
		}
	}

	@Test
	public void parameterizedTestsWithIndexAndValueInName() {
		Request request = Request.aClass(ParametrizedSuiteWithIndexInName.class);
		ArrayList<Description> singleParamSuites = request.getRunner().getDescription().getChildren();
		assertEquals(3, singleParamSuites.size());

		int index = 0;
		for (Description singleParamSuite : singleParamSuites) {
			// Test display name of SuiteForSingleParameter
			assertEquals("[Suite: " + index + "-" + index + "]", singleParamSuite.getDisplayName());

			// Test display name of Parameterized child tests
			ArrayList<Description> parameterizedSingleTests = singleParamSuite.getChildren();
			assertEquals(2, parameterizedSingleTests.size());

			// Single A test
			Description singleA = parameterizedSingleTests.get(0);
			assertEquals(ParameterizedTestA.class.getName(), singleA.getDisplayName());
			assertEquals("[Single A: 0-" + index + "]", singleA.getChildren().get(0).getDisplayName());

			// Single B test
			Description singleB = parameterizedSingleTests.get(1);
			assertEquals(ParameterizedTestB.class.getName(), singleB.getDisplayName());
			assertEquals("[Single B: 0-" + index + "]", singleB.getChildren().get(0).getDisplayName());

			index++;
		}
	}

}
