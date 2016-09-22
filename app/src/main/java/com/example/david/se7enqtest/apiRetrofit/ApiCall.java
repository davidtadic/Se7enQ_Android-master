package com.example.david.se7enqtest.apiRetrofit;

import com.example.david.se7enqtest.models.AnswerModel;
import com.example.david.se7enqtest.models.ArrayModel;
import com.example.david.se7enqtest.models.CalculationModel;
import com.example.david.se7enqtest.models.GeneralKnowledgeModel;
import com.example.david.se7enqtest.models.ReceiveQuestionArrayModel;
import com.example.david.se7enqtest.models.ReceiveQuestionCalculationModel;
import com.example.david.se7enqtest.models.ReceiveQuestionDefinitionModel;
import com.example.david.se7enqtest.models.ReceiveQuestionKnowledgeModel;
import com.example.david.se7enqtest.models.ReceiveQuestionSynonymModel;
import com.example.david.se7enqtest.models.ReceiveScore;
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

    @GET("training/GetWordSynonyms")
    Call<List<SynonymsModel>> getSynonym();

    @GET("training/GetWordDefinitions")
    Call<List<DefinitionModel>> getWordDefinitions();

    @GET("training/GetLogicArrays")
    Call<List<ArrayModel>> getArray();

    @GET("training/GetCalculations")
    Call<List<CalculationModel>> getCalculation();

    @GET("training/GetGeneralKnowledge")
    Call<List<GeneralKnowledgeModel>> getKnowledge();

    @POST("Game/FindOpponent")
    Call<UserRegister> findOpponent();

    @POST("Game/ReceiveQuestion")
    Call<ReceiveQuestionSynonymModel> receiveQuestionSynonym(@Body AnswerModel answer);

    @POST("Game/ReceiveQuestion")
    Call<ReceiveQuestionDefinitionModel> receiveQuestionDefinition(@Body AnswerModel answer);

    @POST("Game/ReceiveQuestion")
    Call<ReceiveQuestionArrayModel> receiveQuestionArray(@Body AnswerModel answer);

    @POST("Game/ReceiveQuestion")
    Call<ReceiveQuestionCalculationModel> receiveQuestionCalculation(@Body AnswerModel answer);

    @POST("Game/ReceiveQuestion")
    Call<ReceiveQuestionKnowledgeModel> receiveQuestionKnowledge(@Body AnswerModel answer);

    @POST("Game/ReceiveQuestion")
    Call<ReceiveScore> receiveScore(@Body AnswerModel answer);










}
