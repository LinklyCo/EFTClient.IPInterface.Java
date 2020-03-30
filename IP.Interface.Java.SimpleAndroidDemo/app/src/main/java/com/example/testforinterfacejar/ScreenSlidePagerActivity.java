package com.example.testforinterfacejar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class ScreenSlidePagerActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Initial activity called in the app configuration
         * Slider Fragment activity that slides between the main fragment and settings fragment
         *
         * All initialization inside this class
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenslidepager);
        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        MainFragment fragMain = new MainFragment();
        SettingsFragment fragSettings = new SettingsFragment();
        pagerAdapter.addFragment(fragMain);
        pagerAdapter.addFragment(fragSettings);
        pager.setAdapter(pagerAdapter);
    }

}
