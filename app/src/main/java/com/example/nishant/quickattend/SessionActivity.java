package com.example.nishant.quickattend;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nishant.quickattend.API.APIClient;
import com.example.nishant.quickattend.API.SessionService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionActivity extends AppCompatActivity {
    private SessionService sessionService;
    private ConstraintLayout mNoCurrentSession;
    private ConstraintLayout mCurrentSession;
    private ConstraintLayout mAttendanceDone;

    private JSONObject  mCurrentUser;
    private int REQUEST_ENABLE_BT = 1;
    BluetoothLeAdvertiser mAdvertiser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        sessionService = APIClient.getClient().create(SessionService.class);

        mNoCurrentSession = findViewById(R.id.no_current_session);
        mCurrentSession = findViewById(R.id.current_session);
        mAttendanceDone = findViewById(R.id.attendance_done);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);
        try {
            mCurrentUser = new JSONObject(sharedPref.getString("currentUser", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView className = findViewById(R.id.section_name);

        try {
            JSONObject jObj = new JSONObject(getIntent().getStringExtra("currentSection"));

            className.setText(jObj.getJSONObject("course").getString("code") + " - " + jObj.getJSONObject("course").getString("name"));

            // Check if a session is in progress
            Call<JsonElement> call = sessionService.getCurrent();
            call.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    JsonObject jObj = response.body().getAsJsonObject();
                    if (response.isSuccessful() && jObj.entrySet().size() == 0) {
                        mNoCurrentSession.setVisibility(View.VISIBLE);
                    } else if (response.isSuccessful()) {
                        mCurrentSession.setVisibility(View.VISIBLE);
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                        JsonArray studentsIdArray = jObj.get("studentIds").getAsJsonArray();

                        try {
                            boolean isHere = false;
                            for (JsonElement jElem: studentsIdArray) {
                                if (jElem.getAsString().equals(mCurrentUser.getString("id"))) {
                                    isHere = true;
                                }
                            }

                            if (isHere) {
                                mNoCurrentSession.setVisibility(View.INVISIBLE);
                                mCurrentSession.setVisibility(View.INVISIBLE);
                                mAttendanceDone.setVisibility(View.VISIBLE);
                                if (mAdvertiser != null) {
                                    mAdvertiser.stopAdvertising(new AdvertiseCallback() {});
                                }
                            } else {
                                if (!mBluetoothAdapter.isEnabled()) {
                                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                                } else {
                                    attendToSession();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            try {
                attendToSession();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void attendToSession() throws JSONException {
        mAdvertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

        AdvertiseSettings settings = new AdvertiseSettings.Builder()
                .setAdvertiseMode( AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY )
                .setTxPowerLevel( AdvertiseSettings.ADVERTISE_TX_POWER_HIGH )
                .setConnectable( false )
                .build();

        ParcelUuid pUuid = new ParcelUuid(UUID.fromString(mCurrentUser.getString("btSerial")));

        AdvertiseData data = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .addServiceUuid(pUuid)
                .build();

        mAdvertiser.startAdvertising(settings, data, new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                super.onStartSuccess(settingsInEffect);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    reloadSession();
                }, 1000);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.e( "BLE", "Advertising onStartFailure: " + errorCode );
                super.onStartFailure(errorCode);
            }
        });
    }

    public void reloadSession() {
        Call<JsonElement> call = sessionService.getCurrent();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    JsonObject jObj = response.body().getAsJsonObject();

                    JsonArray studentsIdArray = jObj.get("studentIds").getAsJsonArray();

                    try {
                        boolean isHere = false;
                        for (JsonElement jElem: studentsIdArray) {
                            if (jElem.getAsString().equals(mCurrentUser.getString("id"))) {
                                isHere = true;
                            }
                        }

                        if (isHere) {
                            mNoCurrentSession.setVisibility(View.INVISIBLE);
                            mCurrentSession.setVisibility(View.INVISIBLE);
                            mAttendanceDone.setVisibility(View.VISIBLE);
                            if (mAdvertiser != null) {
                                mAdvertiser.stopAdvertising(new AdvertiseCallback() {});
                            }
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                reloadSession();
                            }, 1000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mAdvertiser != null) {
            mAdvertiser.stopAdvertising(new AdvertiseCallback() {});
        }
        super.onDestroy();
    }
}
