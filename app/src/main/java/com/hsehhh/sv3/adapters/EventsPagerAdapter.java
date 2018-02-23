package com.hsehhh.sv3.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Фродо Бэггинс on 23.02.2018.
 */

public class EventsPagerAdapter extends FragmentStatePagerAdapter {
    public List<Fragment> fragmentList = new ArrayList<Fragment>();
    public List<String> titles = new ArrayList<String>();

    public EventsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titles.add(title);
    }
}

