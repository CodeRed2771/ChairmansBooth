package com.coderedrobotics;

/**
 *
 * @author Michael
 */
public class Question {
    String question;
    String explanation;
    String correctAnswer;
    String[] otherAnswers;

    public Question(String question, String explanation, String correctAnswer, String[] otherAnswers) {
        this.question = question;
        this.explanation = explanation;
        this.correctAnswer = correctAnswer;
        this.otherAnswers = otherAnswers;
    }    

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String[] getOtherAnswers() {
        return otherAnswers;
    }

    public void setOtherAnswers(String[] otherAnswers) {
        this.otherAnswers = otherAnswers;
    }
}
