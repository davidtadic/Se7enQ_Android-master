package com.example.david.se7enqtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.se7enqtest.apiRetrofit.ApiCall;
import com.example.david.se7enqtest.models.UserLogin;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.usernameLoginId) EditText usernameText;
    @InjectView(R.id.passwordLoginId) EditText _passwordText;
    @InjectView(R.id.LoginButtonId) Button _loginButton;
    @InjectView(R.id.SignUpButtonId) TextView _signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
        public void login(){

        Log.d(TAG, "Login");

        if(!validate()){
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LogInActivity.this,
                R.style.MyThemeDarkDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating ...");
        progressDialog.show();

        UserLogin userLogin = new UserLogin();

        userLogin.setUserName(usernameText.getText().toString());
        userLogin.setPassword(_passwordText.getText().toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.Base_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiCall service = retrofit.create(ApiCall.class);

        Call<UserLogin> userLoginCall = service.getUserLogin(userLogin);

        userLoginCall.enqueue(new Callback<UserLogin>() {
            @Override
            public void onResponse(Call<UserLogin> call, Response<UserLogin> response) {
                progressDialog.dismiss();
                if(response.message().equals("Bad Request")){
                    onLoginFailed();
                    Toast.makeText(getBaseContext(), "Wrong username or password", Toast.LENGTH_LONG).show();

                }
                else{
                    onLoginSuccess();
                    Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                }


            }

            @Override
            public void onFailure(Call<UserLogin> call, Throwable t) {
                progressDialog.dismiss();
                onLoginFailed();
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(LogInActivity.this, MainMenuActivity.class);
                startActivity(intent);
                this.finish();
            }
        }
    }*/

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        Toast.makeText(getBaseContext(), "Login successful", Toast.LENGTH_SHORT).show();

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            usernameText.setError("enter a valid username");
            valid = false;
        } else {
            usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


}
