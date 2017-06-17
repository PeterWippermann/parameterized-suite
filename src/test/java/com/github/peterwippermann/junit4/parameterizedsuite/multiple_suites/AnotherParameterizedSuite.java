package com.github.peterwippermann.junit4.parameterizedsuite.multiple_suites;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite.SuiteClasses;

import com.github.peterwippermann.junit4.parameterizedsuite.AnswerReaderTest;
import com.github.peterwippermann.junit4.parameterizedsuite.ExampleParameterizedSuite;
import com.github.peterwippermann.junit4.parameterizedsuite.ParameterizedSuite;
import com.github.peterwippermann.junit4.parameterizedsuite.QuestionAndAnswer;
import com.github.peterwippermann.junit4.parameterizedsuite.QuestionReaderTest;

/**
 * Another parameterized suite - similar to {@link ExampleParameterizedSuite}.
 * 
 * @author Peter Wippermann
 */
@RunWith(ParameterizedSuite.class)
@SuiteClasses({ QuestionReaderTest.class, AnswerReaderTest.class })
public class AnotherParameterizedSuite {
	@Parameters(name = "Q&A: {0}")
	public static Object[] params() {
		return new Object[][] { { ARE_VIRUSES_ALIVE }, { MARBURG_COLLOQUY } };
	}

	/**
	 * Always provide a target for the defined parameters - even if you only
	 * want to access them in the suite's child classes.
	 */
	@Parameter(0)
	public QuestionAndAnswer qAndA;

	private static final QuestionAndAnswer MARBURG_COLLOQUY = new QuestionAndAnswer(
			"Who took part in the Marburg Colloquy in 1529?",
			"Amongst the most famous parcipants were Martin Luther and Ulrich Zwingli.");
	private static final QuestionAndAnswer ARE_VIRUSES_ALIVE = new QuestionAndAnswer(
			"Are viruses considered to be alive?", "No, since they have no metabolism!");

}
