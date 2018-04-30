package com.example.nishant.quickattend.API.Adapters;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by jeremy on 4/29/18.
 */

public interface StatisticService {
    @GET("statistics/bySection")
    Call<JsonElement> bySection();
}
