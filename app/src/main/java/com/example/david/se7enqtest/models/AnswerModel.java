
package com.example.david.se7enqtest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AnswerModel {

    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("correct")
    @Expose
    private boolean correct;
    @SerializedName("currentQuestonIndex")
    @Expose
    private int currentQuestonIndex;


    public String getAnswer() {
        return answer;
    }


    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public boolean isCorrect() {
        return correct;
    }


    public void setCorrect(boolean correct) {
        this.correct = correct;
    }


    public long getCurrentQuestonIndex() {
        return currentQuestonIndex;
    }


    public void setCurrentQuestonIndex(int currentQuestonIndex) {
        this.currentQuestonIndex = currentQuestonIndex;
    }

}
