package com.example.nishant.quickattend;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnFragmentInteractionListener,
        StatsFragment.OnFragmentInteractionListener,
        AccountFragment.OnFragmentInteractionListener,
        ClassesFragment.OnFragmentInteractionListener {

    private BottomNavigationView mBottomNav;
    private String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mBottomNav = findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(item -> selectFragment(item.getItemId()));
        selectFragment(R.id.menu_home);
    }


    public boolean selectFragment(int itemId) {
        Fragment frag = null;
        switch (itemId) {
            case R.id.menu_home:
                frag = HomeFragment.newInstance();
                break;
            case R.id.menu_classes:
                frag = ClassesFragment.newInstance();
                break;
            case R.id.menu_account:
                frag = AccountFragment.newInstance();
                break;
            case R.id.menu_stats:
                frag = StatsFragment.newInstance();
                break;
            default:
                return false;
         }

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_container, frag, frag.getTag());
        tx.commit();
        return true;
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
}
