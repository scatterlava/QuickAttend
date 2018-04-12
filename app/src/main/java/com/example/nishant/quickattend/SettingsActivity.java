package com.example.nishant.quickattend;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

/**
 * Created by Nishant on 4/4/2018.
 */

public class SettingsActivity extends BaseActivity {
    FrameLayout dynamicContent;
    CoordinatorLayout base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_matching);


        //dynamically include the  current activity layout into  baseActivity layout.now all
        // the view of baseactivity is   accessible in current activity.
        dynamicContent = (FrameLayout) findViewById(R.id.dynamicContent);
        base = (CoordinatorLayout) findViewById(R.id.BaseActivity);
        View wizard = getLayoutInflater().inflate(R.layout.activity_settings, null);
        dynamicContent.addView(wizard);


//        //get the reference of RadioGroup.
//
//        RadioGroup rg = (RadioGroup) findViewById(R.id.BottomNavBar);
//        RadioButton rb = (RadioButton) findViewById(R.id.settingbutton);
//
//        // Change the corresponding icon and text color on nav button click.
//
//        rb.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_settings, 0, 0);
//        rb.setTextColor(Color.parseColor("#3F51B5"));
    }
}
