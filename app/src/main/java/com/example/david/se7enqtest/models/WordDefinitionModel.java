
package com.example.david.se7enqtest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WordDefinitionModel {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("correctAnswer")
    @Expose
    private String correctAnswer;
    @SerializedName("wrongAnswer1")
    @Expose
    private String wrongAnswer1;
    @SerializedName("wrongAnswer2")
    @Expose
    private String wrongAnswer2;
    @SerializedName("wrongAnswer3")
    @Expose
    private String wrongAnswer3;
    @SerializedName("word")
    @Expose
    private String word;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }


    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }


    public String getWrongAnswer1() {
        return wrongAnswer1;
    }


    public void setWrongAnswer1(String wrongAnswer1) {
        this.wrongAnswer1 = wrongAnswer1;
    }


    public String getWrongAnswer2() {
        return wrongAnswer2;
    }


    public void setWrongAnswer2(String wrongAnswer2) {
        this.wrongAnswer2 = wrongAnswer2;
    }


    public String getWrongAnswer3() {
        return wrongAnswer3;
    }

    public void setWrongAnswer3(String wrongAnswer3) {
        this.wrongAnswer3 = wrongAnswer3;
    }


    public String getWord() {
        return word;
    }


    public void setWord(String word) {
        this.word = word;
    }

}
