package com.github.peterwippermann.junit4.parameterizedsuite;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.github.peterwippermann.junit4.parameterizedsuite.ParameterContext;

/**
 * Exemplary test case that takes a {@link QuestionAndAnswer} as field parameter and prints the
 * question to System.out.
 * 
 * @author Peter Wippermann
 *
 */
@RunWith(Parameterized.class)
public class QuestionReaderTest {

    @Parameters(name = "Question: {0}")
    public static Iterable<Object[]> params() {
        if (ParameterContext.isParameterSet()) {
            return Collections.singletonList(ParameterContext.getParameter(Object[].class));
        }
        return Arrays.asList(new Object[][] {{QuestionAndAnswer.COLOUR_OF_GRASS}, {QuestionAndAnswer.NUMBER_OF_CONTINENTS}});
    }

    @Parameter(0)
    public QuestionAndAnswer question;

    @Test
    public void readQuestion() {
        System.out.println("Question: " + question.getQuestion());
    }

}
