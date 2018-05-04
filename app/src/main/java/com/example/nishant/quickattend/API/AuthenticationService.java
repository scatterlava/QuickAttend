package com.example.nishant.quickattend.API;

import com.google.gson.JsonElement;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jeremy on 4/27/18.
 */

public interface AuthenticationService {

    @POST("signin")
    Call<JsonElement> signin(@Body RequestBody body);

    @POST("signup")
    Call<JsonElement> signup(@Body RequestBody body);
}
