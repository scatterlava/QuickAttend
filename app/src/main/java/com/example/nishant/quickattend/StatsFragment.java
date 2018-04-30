package com.example.nishant.quickattend;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nishant.quickattend.API.APIClient;
import com.example.nishant.quickattend.API.Adapters.StatisticService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private StatisticService statisticService;
    private TextView mNumberSessions;
    private TextView mNumberMissings;
    private TextView mPercentage;

    public StatsFragment() {
        // Required empty public constructor
    }

    public static StatsFragment newInstance() {
        StatsFragment fragment = new StatsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statisticService = APIClient.getClient().create(StatisticService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stats, container, false);

        mNumberSessions = v.findViewById(R.id.number_sessions);
        mNumberMissings = v.findViewById(R.id.number_missings);
        mPercentage = v.findViewById(R.id.percentage);

        Call<JsonElement> call = statisticService.bySection();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()) {
                    int totalSessions = 0;
                    int totalValidate = 0;
                    JsonObject jResponse = response.body().getAsJsonObject();

                    //TODO Get shared preference then loop with session id
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);

                    try {
                        JSONObject currentUser = new JSONObject(sharedPref.getString("currentUser", null));
                        if (currentUser != null) {
                            JSONArray sections = currentUser.getJSONArray("sections");
                            for (int i = 0; i < sections.length(); i++) {
                                JsonObject section = jResponse.getAsJsonObject(sections.getJSONObject(i).getString("_id"));
                                if (section != null) {
                                    totalSessions += section.get("total").getAsInt();
                                    totalValidate += section.get("validate").getAsInt();
                                }

                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mNumberSessions.setText(String.valueOf(totalSessions));
                    mNumberMissings.setText(String.valueOf(totalSessions - totalValidate));
                    int percentage = totalValidate * 100 / totalSessions;
                    mPercentage.setText(String.valueOf(percentage));
                    if (percentage <= 70) {
                        mPercentage.setBackgroundColor(getResources().getColor(R.color.holoDarkRed));
                    } else {
                        mPercentage.setBackgroundColor(getResources().getColor(R.color.holoDarkGreen));
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                call.cancel();
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
