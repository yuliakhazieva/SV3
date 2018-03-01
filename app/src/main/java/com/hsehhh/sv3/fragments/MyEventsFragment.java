package com.hsehhh.sv3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.utils.OrganizedEventsFilter;
import com.hsehhh.sv3.utils.VisitedEventsFilter;


public class MyEventsFragment extends android.support.v4.app.Fragment
{
    MainActivity presenter;

    ViewPager pager;
    PagerAdapter listPagerAdapter;
    TabLayout tabLayout;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
        listPagerAdapter = new ListPagerAdapter(getChildFragmentManager(), presenter);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        presenter.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pager = view.findViewById(R.id.pager);
        pager.setAdapter(listPagerAdapter);

        tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.switchToScrolling();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}


class ListPagerAdapter extends FragmentPagerAdapter {
    MainActivity presenter;
    EventsListFragment organized;
    EventsListFragment visited;

    ListPagerAdapter(FragmentManager fm, MainActivity presenter) {
        super(fm);
        this.presenter = presenter;

        organized = EventsListFragment.newInstance();
        organized.changeEventFilter(new OrganizedEventsFilter(presenter.user));

        visited = EventsListFragment.newInstance();
        visited.changeEventFilter(new VisitedEventsFilter(presenter.user));
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:

                return organized;
            case 1:

                return visited;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Organized";
            case 1:
                return "Visited";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}