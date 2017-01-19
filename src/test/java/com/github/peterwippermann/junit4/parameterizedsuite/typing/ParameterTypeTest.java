package com.github.peterwippermann.junit4.parameterizedsuite.typing;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite.SuiteClasses;

import com.github.peterwippermann.junit4.parameterizedsuite.ParameterContext;
import com.github.peterwippermann.junit4.parameterizedsuite.ParameterizedSuite;

public class ParameterTypeTest {
	@RunWith(ParameterizedSuite.class)
	@SuiteClasses({ TestWithStringParameter.class })
	public static class ParametrizedSuiteWithStringParameter {

		public ParametrizedSuiteWithStringParameter(String ignorable) {
		}

		@Parameters(name = "Suite: {index}-{0}")
		public static Collection<String> data() {
			return Arrays.asList(new String[] { "one", "two", "three" });
		}
	}

	@RunWith(Parameterized.class)
	public static class TestWithStringParameter {
		public TestWithStringParameter(String ignorable) {
		}

		@Parameters(name = "Single A: {index}-{0}")
		public static Iterable<String> params() {
			if (ParameterContext.isParameterSet()) {
				return Collections.singletonList(ParameterContext.getParameter(String.class));
			}
			return Collections.singletonList( "ignorable" );
		}

		@Test
		public void test() {
		}
	}


	@Test
	public void parameterizedTestsExpectingNoArrayParameter() {
		Request request = Request.aClass(ParametrizedSuiteWithStringParameter.class);
		ArrayList<Description> singleParamSuites = request.getRunner().getDescription().getChildren();
		assertEquals(3, singleParamSuites.size());
		
		JUnitCore jUnitCore = new JUnitCore();
		Result result = jUnitCore.run(request);
		
		assertEquals("Casting the ParameterContext's content to String failed!", 0, result.getFailureCount());
	}

}
