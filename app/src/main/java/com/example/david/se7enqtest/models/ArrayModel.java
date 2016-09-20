
package com.example.david.se7enqtest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public long getId() {
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


    public long getCorrectNumber() {
        return correctNumber;
    }


    public void setCorrectNumber(int correctNumber) {
        this.correctNumber = correctNumber;
    }


    public long getWrongNumber1() {
        return wrongNumber1;
    }


    public void setWrongNumber1(int wrongNumber1) {
        this.wrongNumber1 = wrongNumber1;
    }


    public long getWrongNumber2() {
        return wrongNumber2;
    }


    public void setWrongNumber2(int wrongNumber2) {
        this.wrongNumber2 = wrongNumber2;
    }


    public long getWrongNumber3() {
        return wrongNumber3;
    }


    public void setWrongNumber3(int wrongNumber3) {
        this.wrongNumber3 = wrongNumber3;
    }




}
