package com.github.peterwippermann.junit4.parameterizedsuite;

/**
 * POJO that models a Q&A.
 * 
 * @author Peter Wippermann
 *
 */
public class QuestionAndAnswer {

    public static final QuestionAndAnswer COLOUR_OF_GRASS = new QuestionAndAnswer("What's the colour of grass?", "It's green!");
    public static final QuestionAndAnswer NUMBER_OF_CONTINENTS =
            new QuestionAndAnswer("How many continents are there on Earth?", "That depends. Something between four and seven!");
    public static final QuestionAndAnswer THE_KING = new QuestionAndAnswer("Who's left the building?", "That's Elvis, of course!");

    private String question;
    private String answer;

    public QuestionAndAnswer(String question, String answer) {
        super();
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String toString() {
        return question;
    }
}
