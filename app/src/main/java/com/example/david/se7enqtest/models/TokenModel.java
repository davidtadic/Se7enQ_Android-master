
package com.example.david.se7enqtest.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenModel {

    @SerializedName("user")
    @Expose
    private UserLogin user;
    @SerializedName("token")
    @Expose
    private String token;

    public UserLogin getUser() {
        return user;
    }


    public void setUser(UserLogin user) {
        this.user = user;
    }


    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }

}
