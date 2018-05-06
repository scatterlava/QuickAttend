package com.example.nishant.quickattend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.nishant.quickattend.API.APIClient;
import com.example.nishant.quickattend.API.SectionService;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {
    private ClassesFragment.OnFragmentInteractionListener mListener;
    /*private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/
    private static EditText Cfirstname, Clastname, CSID, Cmail;


    private String fname = "", lname = "", studentid = "", email = "";

    private SectionService sectionService;

    public AccountFragment() {
        // Required empty public constructor
    }

   Button logout;

    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        //EditText linked
        Cfirstname = v.findViewById(R.id.text1);
        Clastname = v.findViewById(R.id.text2);
        CSID = v.findViewById(R.id.text3);
        Cmail = v.findViewById(R.id.text4);

        //get data for current user

        Call<JsonElement> call = sectionService.getCurrent();
        call.enqueue(new Callback<JsonElement>() {
                         @Override
                         public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                             if (response.isSuccessful()) {
                                 JsonObject jResponse = response.body().getAsJsonObject();

                                 //TODO Get shared preference then loop with session id
                                 SharedPreferences sharedPref = getActivity().getSharedPreferences
                                         (getString(R.string.shared_preference), Context.MODE_PRIVATE);

                                 try {
                                     JSONObject currentUser = new JSONObject(sharedPref.getString("currentUser", null));
                                     if (currentUser != null) {
                                         fname = currentUser.getString("firstname");
                                         lname = currentUser.getString("lastname");
                                         studentid = currentUser.getString("_id");
                                         email = currentUser.getString("email");
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

        //set data to editText fields in account
        Cfirstname.setText(fname);
        Clastname.setText(lname);
        CSID.setText(studentid);
        Cmail.setText(email);

        logout = v.findViewById(R.id.LogoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onclicking the logout button:
                //it should "log out" current user and go back to login page

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
          //  mListener = (OnFragmentInteractionListener) context;
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
     //   void onFragmentInteraction(Uri uri);
    }
}
