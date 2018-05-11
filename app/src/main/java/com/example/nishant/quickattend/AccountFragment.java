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
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;


public class AccountFragment extends Fragment {
    private ClassesFragment.OnFragmentInteractionListener mListener;
    private String fname = "x", lname = "y", email = "z";
    private String studentid = "a";
    private Button logout;

    public AccountFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        //EditText linked
        TextView Cfirstname = v.findViewById(R.id.text1);
        TextView Clastname = v.findViewById(R.id.text2);
        TextView CSID = v.findViewById(R.id.text3);
        TextView Cmail = v.findViewById(R.id.text4);

        //get data for current user
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preference), Context.MODE_PRIVATE);

         try {
            JSONObject currentUser = new JSONObject(sharedPref.getString("currentUser", null));
            if (currentUser != null) {
                fname = currentUser.getString("firstName");
                lname = currentUser.getString("lastName");
                studentid = currentUser.getString("id");
                email = currentUser.getString("email");
            }
         } catch (JSONException e) {
            e.printStackTrace();
         }

        //set data to editText fields in account
        Cfirstname.setText(fname);
        Clastname.setText(lname);
        CSID.setText(studentid);
        Cmail.setText(email);

        logout = v.findViewById(R.id.LogoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity() , LoginActivity.class);
                startActivity(i);

            }
        });

        return v;
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
