package com.github.peterwippermann.junit4.parameterizedsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Suite.SuiteClasses;

import com.github.peterwippermann.junit4.parameterizedsuite.ParameterizedSuite;

/**
 * A suite that runs reading test cases for question and answer.
 * <p>
 * Demonstrates that a {@link ParameterizedSuite} runs all child test cases for a parameter first
 * before going over to the next parameter. That way, the question and the correlating answer are
 * printed together.
 * 
 * @author Peter Wippermann
 */
@RunWith(ParameterizedSuite.class)
@SuiteClasses({QuestionReaderTest.class, AnswerReaderTest.class})
public class ExampleParameterizedSuite {
    @Parameters(name = "Q&A: {0}")
    public static Object[] params() {
        return new Object[][] {{QuestionAndAnswer.THE_KING}, {QuestionAndAnswer.COLOUR_OF_GRASS}, {QuestionAndAnswer.NUMBER_OF_CONTINENTS}};
    }


    /**
     * Always provide a target for the defined parameters - even if you only want to access them in the suite's child classes.
     */
    @Parameter(0)
    public QuestionAndAnswer qAndA; 
}
