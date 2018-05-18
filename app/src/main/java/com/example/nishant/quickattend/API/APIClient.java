package com.example.nishant.quickattend.API;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jeremy on 4/27/18.
 */

/**
 *  Class where all the settings for the API are done.
 */
public class APIClient {

    private static Retrofit retrofit = null;
    private static String mToken = "";

    static public Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(interceptor);

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("x-access-token", mToken)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        OkHttpClient client = builder.build();

        retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:6969")
                .baseUrl("http://163.172.189.61:6969")
//                .baseUrl("http://192.168.1.137:6969")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    static public void setToken(String token) {
        mToken = token;
    }

}
