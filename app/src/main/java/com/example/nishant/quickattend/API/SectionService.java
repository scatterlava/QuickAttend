package com.example.nishant.quickattend.API;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by jeremy on 4/29/18.
 */

public interface SectionService {

    @GET("sections/current")
    Call<JsonElement> getCurrent();

    @GET("sections")
    Call<JsonElement>   getSections();
}
