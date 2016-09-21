package com.example.david.se7enqtest.models;

/**
 * Created by david on 20.9.2016..
 */
public class ReceiveQuestionKnowledgeModel {

    private GeneralKnowledgeModel question;
    private int opponentPoints;
    private String opponentAnswer;

    public GeneralKnowledgeModel getQuestion() {
        return question;
    }

    public void setQuestion(GeneralKnowledgeModel question) {
        this.question = question;
    }

    public int getOpponentPoints() {
        return opponentPoints;
    }

    public void setOpponentPoints(int opponentPoints) {
        this.opponentPoints = opponentPoints;
    }

    public String getOpponentAnswer() {
        return opponentAnswer;
    }

    public void setOpponentAnswer(String opponentAnswer) {
        this.opponentAnswer = opponentAnswer;
    }
}
