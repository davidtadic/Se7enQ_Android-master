package com.example.david.se7enqtest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.david.se7enqtest.apiRetrofit.ApiCall;
import com.example.david.se7enqtest.apiRetrofit.ServiceGenerator;
import com.example.david.se7enqtest.models.TokenModel;
import com.example.david.se7enqtest.models.WordDefinitionModel;

import java.util.List;

import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Definitions extends Activity {

    TextView wordDefinitions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definitions);

        wordDefinitions = (TextView)findViewById(R.id.viewDefinitions);

        SharedPreferences settings = getSharedPreferences("MY_PREF",0);
        String userToken = settings.getString("TOKEN","");

        ApiCall service = ServiceGenerator.createServiceAuthorization(ApiCall.class, userToken);

        Call<List<WordDefinitionModel>>  wordDefinitionsCall = service.getWordDefinitions();

        wordDefinitionsCall.enqueue(new Callback<List<WordDefinitionModel>>() {
            @Override
            public void onResponse(Call<List<WordDefinitionModel>> call, Response<List<WordDefinitionModel>> response) {
                Log.i("DefinicijeResponse", response.code()+" , "+response.message()+" ,  "+response.body());



            }

            @Override
            public void onFailure(Call<List<WordDefinitionModel>> call, Throwable t) {
                Log.e("DefinicijeFailure", t.getMessage());
            }
        });




    }
}
