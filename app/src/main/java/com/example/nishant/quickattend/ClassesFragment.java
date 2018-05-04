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
import com.example.nishant.quickattend.Adapters.SectionAdapter;
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
 * {@link ClassesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassesFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView mListClasses;
    private List<JsonObject> classes = new ArrayList<>();
    private SectionService sectionService;

    public ClassesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ClassesFragment newInstance() {
        ClassesFragment fragment = new ClassesFragment();
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
        View v = inflater.inflate(R.layout.fragment_classes, container, false);

        mListClasses = v.findViewById(R.id.list_classes);

        SectionAdapter adapter = new SectionAdapter(this.getActivity(), classes);
        mListClasses.setAdapter(adapter);

        Call<JsonElement> call = sectionService.getSections();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonArray jObj = response.body().getAsJsonArray();

                if (response.isSuccessful() && !jObj.isJsonNull()) {
                    for (JsonElement section: jObj) {
                        classes.add(section.getAsJsonObject());
                    }
                }
                mListClasses.invalidateViews();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

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
