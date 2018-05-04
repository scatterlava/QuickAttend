package com.example.nishant.quickattend;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.nishant.quickattend.API.APIClient;
import com.example.nishant.quickattend.API.SessionService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionActivity extends AppCompatActivity {
    private SessionService sessionService;
    private ConstraintLayout mNoCurrentSession;
    private ConstraintLayout mCurrentSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        sessionService = APIClient.getClient().create(SessionService.class);

        mNoCurrentSession = findViewById(R.id.no_current_session);
        mCurrentSession = findViewById(R.id.current_session);

        TextView className = findViewById(R.id.section_name);

        try {
            JSONObject jObj = new JSONObject(getIntent().getStringExtra("currentSection"));

            className.setText(jObj.getJSONObject("course").getString("code") + " - " + jObj.getJSONObject("course").getString("name"));
            Call<JsonElement> call = sessionService.getCurrent();
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    JsonObject jObj = response.body().getAsJsonObject();
                    if (response.isSuccessful() && jObj.entrySet().size() == 0) {
                        mNoCurrentSession.setVisibility(View.VISIBLE);
                    } else if (response.isSuccessful()) {
                        mCurrentSession.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    call.cancel();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
