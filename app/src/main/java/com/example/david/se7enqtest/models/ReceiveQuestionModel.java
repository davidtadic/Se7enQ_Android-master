package com.example.david.se7enqtest.models;

/**
 * Created by david on 20.9.2016..
 */
public class ReceiveQuestionModel {

    private Object typeOfQuestion;
    private String opponentAnswer;
    private int opponentPoints;

    public Object getTypeOfQuestion() {
        return typeOfQuestion;
    }

    public void setTypeOfQuestion(Object typeOfQuestion) {
        this.typeOfQuestion = typeOfQuestion;
    }

    public String getOpponentAnswer() {
        return opponentAnswer;
    }

    public void setOpponentAnswer(String opponentAnswer) {
        this.opponentAnswer = opponentAnswer;
    }

    public int getOpponentPoints() {
        return opponentPoints;
    }

    public void setOpponentPoints(int opponentPoints) {
        this.opponentPoints = opponentPoints;
    }
}
