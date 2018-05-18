package com.example.nishant.quickattend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nishant.quickattend.API.APIClient;
import com.example.nishant.quickattend.API.AuthenticationService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private AuthenticationService authenticationService;
    private EditText mFirstName;
    private EditText mLastName;
    private EditText mEmail;
    private EditText mStudentId;
    private EditText mPassword;
    private SignupActivity mSelf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authenticationService = APIClient.getClient().create(AuthenticationService.class);
        mFirstName = findViewById(R.id.input_first_name);
        mLastName = findViewById(R.id.input_last_name);
        mEmail = findViewById(R.id.input_email);
        mStudentId = findViewById(R.id.input_student_id);
        mPassword = findViewById(R.id.input_password);
    }

    public void signup(View v) {

        boolean error = false;

        // Check if some fields are empty

        if (TextUtils.isEmpty(mStudentId.getText().toString())) {
            mStudentId.setError("Student Id is required.");
            error = true;
        }

        if (TextUtils.isEmpty(mFirstName.getText().toString())) {
            mFirstName.setError("First Name is required.");
            error = true;
        }

        if (TextUtils.isEmpty(mLastName.getText().toString())) {
            mLastName.setError("Last Name is required.");
            error = true;
        }

        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            mPassword.setError("Password is required.");
            error = true;
        }

        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setError("Email is required.");
            error = true;
        }

        if (error) {
            return ;
        }

        JsonObject jObj = new JsonObject();

        jObj.addProperty("id", mStudentId.getText().toString());
        jObj.addProperty("firstName", mFirstName.getText().toString());
        jObj.addProperty("lastName", mLastName.getText().toString());
        jObj.addProperty("password", mPassword.getText().toString());
        jObj.addProperty("email", mEmail.getText().toString());
        jObj.addProperty("isTeacher", false);

        mSelf = this;

        // Call API to signup
        Call<JsonElement> call = authenticationService.signup(RequestBody.create(MediaType.parse("application/json"), jObj.toString()));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonObject jObj = response.body().getAsJsonObject();

                    // Set user token for next calls
                    APIClient.setToken(jObj.get("token").getAsString());
                    SharedPreferences sharedPref = mSelf.getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    // Store user info in sharedPreferences
                    editor.putString("currentUser", jObj.toString());
                    editor.apply();

                    Intent i = new Intent(mSelf, MainActivity.class);
                    startActivity(i);

                } else {
                    Snackbar snackbar = Snackbar
                            .make(v, "Student Id already exist.", Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.colorAccent));
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
