package com.example.nishant.quickattend;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

//extends our custom BaseActivity
public class ClassActivity extends BaseActivity {
    RelativeLayout dynamicContent, bottonNavBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_matching);


        //dynamically include the  current activity      layout into  baseActivity layout.now all the view of baseactivity is   accessible in current activity.
        dynamicContent = (RelativeLayout) findViewById(R.id.classespage);
        bottonNavBar = (RelativeLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_classes, null);
        dynamicContent.addView(wizard);


        //get the reference of RadioGroup.

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioButton rb = (RadioButton) findViewById(R.id.classbutton);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_list, 0, 0);
        rb.setTextColor(Color.parseColor("#3F51B5"));
    }
}
