
package com.example.david.se7enqtest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalculationModel {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("expression")
    @Expose
    private String expression;
    @SerializedName("correctResult")
    @Expose
    private int correctResult;
    @SerializedName("wrongResult1")
    @Expose
    private int wrongResult1;
    @SerializedName("wrongResult2")
    @Expose
    private int wrongResult2;
    @SerializedName("wrongResult3")
    @Expose
    private int wrongResult3;


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getExpression() {
        return expression;
    }


    public void setExpression(String expression) {
        this.expression = expression;
    }


    public int getCorrectResult() {
        return correctResult;
    }


    public void setCorrectResult(int correctResult) {
        this.correctResult = correctResult;
    }


    public int getWrongResult1() {
        return wrongResult1;
    }


    public void setWrongResult1(int wrongResult1) {
        this.wrongResult1 = wrongResult1;
    }


    public int getWrongResult2() {
        return wrongResult2;
    }


    public void setWrongResult2(int wrongResult2) {
        this.wrongResult2 = wrongResult2;
    }


    public int getWrongResult3() {
        return wrongResult3;
    }


    public void setWrongResult3(int wrongResult3) {
        this.wrongResult3 = wrongResult3;
    }

    public List<Integer> getQuestionOptions(){
        List<Integer> shuffle = new ArrayList<Integer>();

        shuffle.add(correctResult);
        shuffle.add(wrongResult1);
        shuffle.add(wrongResult2);
        shuffle.add(wrongResult3);

        Collections.shuffle(shuffle);

        return shuffle;

    }




}
