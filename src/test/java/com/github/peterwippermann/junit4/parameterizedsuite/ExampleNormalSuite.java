package com.github.peterwippermann.junit4.parameterizedsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * A suite that runs reading test cases for question and answer.
 * <p>
 * Demonstrates that a normal {@link Suite} doesn't fit if you want to run all child test cases for
 * a parameter first before going over to the next parameter. When running this suite, all questions are printed first in-a-row followed by all answers.
 * 
 * @author Peter Wippermann
 */
@RunWith(Suite.class)
@SuiteClasses({QuestionReaderTest.class, AnswerReaderTest.class})
public class ExampleNormalSuite {

}
