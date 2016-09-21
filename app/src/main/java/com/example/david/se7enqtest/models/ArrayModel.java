
package com.example.david.se7enqtest.models;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArrayModel {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("array")
    @Expose
    private String array;
    @SerializedName("correctNumber")
    @Expose
    private int correctNumber;
    @SerializedName("wrongNumber1")
    @Expose
    private int wrongNumber1;
    @SerializedName("wrongNumber2")
    @Expose
    private int wrongNumber2;
    @SerializedName("wrongNumber3")
    @Expose
    private int wrongNumber3;
    @SerializedName("gameQuestions")

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getArray() {
        return array;
    }


    public void setArray(String array) {
        this.array = array;
    }


    public int getCorrectNumber() {
        return correctNumber;
    }


    public void setCorrectNumber(int correctNumber) {
        this.correctNumber = correctNumber;
    }


    public int getWrongNumber1() {
        return wrongNumber1;
    }


    public void setWrongNumber1(int wrongNumber1) {
        this.wrongNumber1 = wrongNumber1;
    }


    public int getWrongNumber2() {
        return wrongNumber2;
    }


    public void setWrongNumber2(int wrongNumber2) {
        this.wrongNumber2 = wrongNumber2;
    }


    public int getWrongNumber3() {
        return wrongNumber3;
    }


    public void setWrongNumber3(int wrongNumber3) {
        this.wrongNumber3 = wrongNumber3;
    }

    public List<Integer> getQuestionOptions(){
        List<Integer> shuffle = new ArrayList<Integer>();

        shuffle.add(correctNumber);
        shuffle.add(wrongNumber1);
        shuffle.add(wrongNumber2);
        shuffle.add(wrongNumber3);

        Collections.shuffle(shuffle);

        return shuffle;

    }




}
