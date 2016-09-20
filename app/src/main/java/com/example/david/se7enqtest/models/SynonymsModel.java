
package com.example.david.se7enqtest.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SynonymsModel {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("correctAnswer1")
    @Expose
    private String correctAnswer1;
    @SerializedName("correctAnswer2")
    @Expose
    private String correctAnswer2;
    @SerializedName("wrongAnswer1")
    @Expose
    private String wrongAnswer1;
    @SerializedName("wrongAnswer2")
    @Expose
    private String wrongAnswer2;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getCorrectAnswer1() {
        return correctAnswer1;
    }


    public void setCorrectAnswer1(String correctAnswer1) {
        this.correctAnswer1 = correctAnswer1;
    }


    public String getCorrectAnswer2() {
        return correctAnswer2;
    }


    public void setCorrectAnswer2(String correctAnswer2) {
        this.correctAnswer2 = correctAnswer2;
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

    public List<String> getQuestionOptions(){
        List<String> shuffle = new ArrayList<String>();

        shuffle.add(correctAnswer1);
        shuffle.add(correctAnswer2);
        shuffle.add(wrongAnswer1);
        shuffle.add(wrongAnswer2);

        Collections.shuffle(shuffle);

        return shuffle;

    }

}
