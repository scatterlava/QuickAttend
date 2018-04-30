package com.example.nishant.quickattend;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nishant.quickattend.API.APIClient;
import com.example.nishant.quickattend.API.Adapters.SectionAdapter;
import com.example.nishant.quickattend.API.AuthenticationService;
import com.example.nishant.quickattend.API.SectionService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SectionService sectionService;
    private List<JsonObject> currentSection = new ArrayList<>();
    private List<JsonObject> nexToday = new ArrayList<>();
    private ListView mCurrentSectionView;
    private ListView mNextTodayView;

    private HomeFragment mSelf;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sectionService = APIClient.getClient().create(SectionService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mCurrentSectionView = v.findViewById(R.id.list_current_section);
        mNextTodayView = v.findViewById(R.id.list_next_today);
        mSelf = this;

        Call<JsonElement> call = sectionService.getCurrent();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && !response.body().isJsonNull()) {
                    //TODO: stock section in list
                    JsonObject jObj = response.body().getAsJsonObject();
                    currentSection.add(jObj);
                    mCurrentSectionView.invalidateViews();
                    mSelf.getSections(jObj.get("_id").getAsString());
                } else {
                    mSelf.getSections(null);
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                call.cancel();
            }
        });

        SectionAdapter sectionAdapter = new SectionAdapter(this.getActivity(), currentSection);
        mCurrentSectionView.setAdapter(sectionAdapter);

        SectionAdapter sectionAdapterNext = new SectionAdapter(this.getActivity(), nexToday);
        mNextTodayView.setAdapter(sectionAdapterNext);

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

    private void getSections(String idCurrent) {
        Call<JsonElement> call = sectionService.getSections();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonArray jObj = response.body().getAsJsonArray();

                if (response.isSuccessful() && !jObj.isJsonNull()) {
                    for (JsonElement section: jObj) {
                        if (!section.getAsJsonObject().get("_id").getAsString().contentEquals(idCurrent)) {
                            nexToday.add(section.getAsJsonObject());
                        }
                    }
                }
                mNextTodayView.invalidateViews();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                call.cancel();
            }
        });
    }
}