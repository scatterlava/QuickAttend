package com.example.nishant.quickattend.API;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jeremy on 4/29/18.
 */

public interface SessionService {
    @GET("sessions/current")
    Call<JsonElement> getCurrent();
}
