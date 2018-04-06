package com.example.nishant.quickattend;

import android.annotation.SuppressLint;
import android.support.design.widget.BottomNavigationView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class BaseActivity extends AppCompatActivity {

    RadioGroup radioGroup1;
    RadioButton setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomnavbar);


        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        setting = (RadioButton)findViewById(R.id.settingbutton);
        radioGroup1.setOnCheckedChangeListener((group, checkedId) -> {
            Intent in;
            Log.i("matching", "matching inside1 bro" + checkedId);
            switch (checkedId)
            {
                case R.id.homebutton:
                    Log.i("matching", "matching inside1 matching" +  checkedId);
                    in=new Intent(getBaseContext(),HomeActivity.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.classbutton:
                    Log.i("matching", "matching inside1 watchlistAdapter" + checkedId);

                    in = new Intent(getBaseContext(), ClassActivity.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);

                    break;
                case R.id.statsbutton:
                    Log.i("matching", "matching inside1 rate" + checkedId);

                    in = new Intent(getBaseContext(),StatsActivity.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.settingbutton:
                    Log.i("matching", "matching inside1 deals" + checkedId);
                    in = new Intent(getBaseContext(), SettingsActivity.class);
                    startActivity(in);
                    overridePendingTransition(0, 0);
                    break;
                default:
                    break;
            }
        });
    }
}