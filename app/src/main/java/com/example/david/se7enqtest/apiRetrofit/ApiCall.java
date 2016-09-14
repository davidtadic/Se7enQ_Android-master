package com.example.david.se7enqtest.apiRetrofit;

import com.example.david.se7enqtest.models.TokenModel;
import com.example.david.se7enqtest.models.UserLogin;
import com.example.david.se7enqtest.models.UserRegister;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by david on 26.8.2016..
 */
public interface ApiCall {

    @POST("User/Login/")
    Call<TokenModel> getUserLogin(@Body UserLogin userLogin);

    @POST("User/Register/")
    Call<UserRegister> getUserRegister(@Body UserRegister userRegister);




}
