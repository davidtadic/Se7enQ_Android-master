package com.example.david.se7enqtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david.se7enqtest.apiRetrofit.ApiCall;
import com.example.david.se7enqtest.apiRetrofit.ServiceGenerator;
import com.example.david.se7enqtest.models.UserRegister;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends Activity {


    private static final String TAG = "SignupActivity";
    @InjectView(R.id.userPhoto) ImageView userPhoto;
    @InjectView(R.id.uploadPhoto) Button uploadButton;
    @InjectView(R.id.input_firstName) EditText _firstNameText;
    @InjectView(R.id.input_lastName) EditText _lastNameText;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_username) EditText usernameText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_signup) Button _signupButton;
    @InjectView(R.id.link_login) TextView _loginLink;

    private static final int RESULT_LOAD_IMAGE = 1;
    String encodedImage = "no selected image";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void onUploadButtonListener(View v){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);

    }

    public void onDefaultPhotoListener(View v){
        userPhoto.setImageResource(R.drawable.user_default);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            userPhoto.setImageURI(selectedImage);

        }


    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.MyThemeDarkDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

       /* Bitmap image = ((BitmapDrawable) userPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);*/



        UserRegister userRegister = new UserRegister();

        userRegister.setFirstName(_firstNameText.getText().toString());
        userRegister.setLastName(_lastNameText.getText().toString());
        userRegister.setEmail(_emailText.getText().toString());
        userRegister.setUsername(usernameText.getText().toString());
        userRegister.setPassword(_passwordText.getText().toString());


        ApiCall service = ServiceGenerator.createService(ApiCall.class);

        Call<UserRegister> registerCall = service.getUserRegister(userRegister);

        registerCall.enqueue(new Callback<UserRegister>() {
            @Override
            public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                progressDialog.dismiss();
                if(response.message().equals("Internal Server Error")){
                    onSignupFailed();
                    Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                } if(response.message().equals("Bad Request")){
                    onSignupFailed();
                    Toast.makeText(getBaseContext(), "Choose another username", Toast.LENGTH_LONG).show();
                    usernameText.setError("Username already taken");
                }
                else{
                    onSignupSuccess();
                    Toast.makeText(getBaseContext(), response.message(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onFailure(Call<UserRegister> call, Throwable t) {
                progressDialog.dismiss();
                onSignupFailed();
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


        /*new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }

    public void onSignupSuccess() {
        Toast.makeText(getBaseContext(), "Register successful", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Register failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String firstName = _firstNameText.getText().toString();
        String lastName = _lastNameText.getText().toString();
        String email = _emailText.getText().toString();
        String username = usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (firstName.isEmpty() || firstName.length() < 3) {
            _firstNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 3) {
            _lastNameText.setError("at least 3 characters");
            valid = false;
        } else {
            _lastNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

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
