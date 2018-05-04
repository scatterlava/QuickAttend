package com.example.nishant.quickattend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.nishant.quickattend.API.APIClient;
import com.example.nishant.quickattend.API.AuthenticationService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private AuthenticationService authenticationService;
    private EditText mId;
    private EditText mPassword;
    private LoginActivity mSelf;
    private String  TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authenticationService = APIClient.getClient().create(AuthenticationService.class);
        mId = findViewById(R.id.studentId);
        mPassword = findViewById(R.id.password);
    }

    public void login(View v) {
        JsonObject jObj = new JsonObject();

        jObj.addProperty("id", mId.getText().toString());
        jObj.addProperty("password", mPassword.getText().toString());

        Call<JsonElement> call = authenticationService.signin(RequestBody.create(MediaType.parse("application/json"), jObj.toString()));
        mSelf = this;
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    //TODO: Get token from response
                    JsonObject jObj = response.body().getAsJsonObject();

                    APIClient.setToken(jObj.get("token").getAsString());
                    SharedPreferences sharedPref = mSelf.getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("currentUser", jObj.toString());
                    editor.apply();

                    Intent i = new Intent(mSelf, MainActivity.class);
                    startActivity(i);
                    return ;
                }

                mId.setError("Bad creditentials.");
                mPassword.setError("Bad creditentials.");
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void signup(View v) {
        Intent i = new Intent(this, SignupActivity.class);
        startActivity(i);
    }
}
