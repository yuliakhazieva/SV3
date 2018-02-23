package com.hsehhh.sv3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.adapters.EventsAdapter;
import com.hsehhh.sv3.adapters.EventsPagerAdapter;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.interfaces.EventFilter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class ScreenSlideActivity extends FragmentActivity {

    private static final int NUM_PAGES = 2;
    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_events);

        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new EventsPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MyEventsFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
