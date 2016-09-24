package com.github.peterwippermann.junit4.parameterizedsuite;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.peterwippermann.junit4.parameterizedsuite.ParameterContext;

/**
 * Exemplary test case that takes a {@link QuestionAndAnswer} as constructor parameter and prints the
 * answer to System.out.
 * 
 * @author Peter Wippermann
 *
 */
@RunWith(Parameterized.class)
public class AnswerReaderTest {
    
    @Parameters(name = "Answer for: {0}")
    public static Iterable<Object[]> params() {
        if (ParameterContext.isParameterSet()) {
            return Collections.singletonList(ParameterContext.getParameter(Object[].class));
        }
        return Arrays.asList(new Object[][] {{QuestionAndAnswer.COLOUR_OF_GRASS}, {QuestionAndAnswer.NUMBER_OF_CONTINENTS}});
    }

    private QuestionAndAnswer answer;
    
    public AnswerReaderTest(QuestionAndAnswer answer) {
        super();
        this.answer = answer;
    }

    @Test
    public void readAnswer() {
        System.out.println("Answer: " + answer.getAnswer());
    }

}
