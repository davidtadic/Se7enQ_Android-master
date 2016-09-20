package com.example.david.se7enqtest.apiRetrofit;

import com.example.david.se7enqtest.models.AnswerModel;
import com.example.david.se7enqtest.models.ArrayModel;
import com.example.david.se7enqtest.models.CalculationModel;
import com.example.david.se7enqtest.models.GeneralKnowledgeModel;
import com.example.david.se7enqtest.models.ReceiveQuestionModel;
import com.example.david.se7enqtest.models.SynonymsModel;
import com.example.david.se7enqtest.models.TokenModel;
import com.example.david.se7enqtest.models.UserLogin;
import com.example.david.se7enqtest.models.UserRegister;
import com.example.david.se7enqtest.models.DefinitionModel;

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


    @GET("training/GetWordDefinitions")
    Call<List<DefinitionModel>> getWordDefinitions();

    @POST("Game/FindOpponent")
    Call<UserRegister> findOpponent();

    @POST("Game/ReceiveQuestion")
    Call<ReceiveQuestionModel> receiveQuestion(@Body AnswerModel answer);

    /*@POST("Game/ReceiveQuestion")
    Call<DefinitionModel> receiveQuestionDefinitions(@Body AnswerModel answer);

    @POST("Game/ReceiveQuestion")
    Call<ArrayModel> receiveQuestionArray(@Body AnswerModel answer);

    @POST("Game/ReceiveQuestion")
    Call<CalculationModel> receiveQuestionCalculation(@Body AnswerModel answer);

    @POST("Game/ReceiveQuestion")
    Call<GeneralKnowledgeModel> receiveQuestionGeneralKnowledge(@Body AnswerModel answer);*/




}
