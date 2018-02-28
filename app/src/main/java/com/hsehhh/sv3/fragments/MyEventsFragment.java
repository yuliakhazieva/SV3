package com.hsehhh.sv3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.interfaces.EventFilter;
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

        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        presenter.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listPagerAdapter = new ListPagerAdapter(presenter.getSupportFragmentManager(), presenter);
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
    EventsListFragment organizedEventsListFragment;
    EventsListFragment visitedEventsListFragment;

    ListPagerAdapter(FragmentManager fm, MainActivity presenter) {
        super(fm);
        this.presenter = presenter;

        organizedEventsListFragment = new EventsListFragment();
        organizedEventsListFragment.setEventFilter(new OrganizedEventsFilter(presenter.user));

        visitedEventsListFragment = new EventsListFragment();
        visitedEventsListFragment.setEventFilter(new VisitedEventsFilter(presenter.user));
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return organizedEventsListFragment;
            case 1:
                return visitedEventsListFragment;
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Организую";
            case 1:
                return "Пойду";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

}