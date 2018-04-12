package com.example.nishant.quickattend;

import android.annotation.SuppressLint;
import android.support.design.widget.BottomNavigationView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class BaseActivity extends AppCompatActivity {
    private RadioGroup bottonNavBar;
   // RadioButton setting;
   private FrameLayout dynamicContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bottomnavbar);

        bottonNavBar=(RadioGroup)findViewById(R.id.BottomNavBar);

        //starts with the home page before user decides to switch layouts
        dynamicContent = (FrameLayout) findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_home_page, null);
        dynamicContent.addView(wizard);

        //setting = (RadioButton)findViewById(R.id.settingbutton);
        bottonNavBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Intent in;
            Log.i("matching", "matching inside1 bro" + checkedId);
                switch (checkedId) {
                    case R.id.homebutton:
                    Log.i("matching", "matching inside1 matching" +  checkedId);
                        in = new Intent(BaseActivity.this.getBaseContext(), HomeActivity.class);
                        BaseActivity.this.startActivity(in);
//                    overridePendingTransition(0, 0);  //animation from one slide to another
                        break;
                    case R.id.classbutton:
                    Log.i("matching", "matching inside1 watchlistAdapter" + checkedId);

                        in = new Intent(BaseActivity.this.getBaseContext(), ClassActivity.class);
                        BaseActivity.this.startActivity(in);
//                    overridePendingTransition(0, 0);

                        break;
                    case R.id.statsbutton:
                    Log.i("matching", "matching inside1 rate" + checkedId);

                        in = new Intent(BaseActivity.this.getBaseContext(), StatsActivity.class);
                        BaseActivity.this.startActivity(in);
//                    overridePendingTransition(R, 0);
                        break;
                    case R.id.settingbutton:
                    Log.i("matching", "matching inside1 deals" + checkedId);
                        in = new Intent(BaseActivity.this.getBaseContext(), SettingsActivity.class);
                        BaseActivity.this.startActivity(in);
//                    overridePendingTransition(0, 0);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}