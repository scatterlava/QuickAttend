package com.example.nishant.quickattend;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.lang.reflect.Field;

public class homepage extends AppCompatActivity {

    //private TextView mTextMessage;
    private static final String SELECTED_ITEM = "arg_selected_item";
    private BottomNavigationView navigation;

    private int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottomnavbar);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        //mTextMessage = (TextView) findViewById(R.id.message);


        navigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   // mTextMessage.setText(R.string.title_home);

                case R.id.navigation_classes:
                   // mTextMessage.setText(R.string.title_classes);

                case R.id.navigation_statistics:
                    //mTextMessage.setText(R.string.title_statistics);

                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_settings);

            }
            return true;
        });


    }

    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

}  //end of class