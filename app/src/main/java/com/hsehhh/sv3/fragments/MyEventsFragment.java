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

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MyEventsFragment extends android.support.v4.app.Fragment {
    MainActivity presenter;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewGroup eventsPager;

    public static MyEventsFragment create(int pageNumber) {
        MyEventsFragment fragment = new MyEventsFragment();
        Bundle args = new Bundle();
        args.putInt("events", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventsPager = (ViewGroup) inflater.inflate(R.layout.fragment_my_events, container, false);
        presenter.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //фильтры (которые судя по комментам требовалось поменять) в hosted b attended

        return eventsPager;
    }

//    @Override
//    public void onResume(){
//        super.onResume();
////        Тут еще надо с инициализацией похимичить. Унести в правильные места.
////        organizedEventsAdapter.initializeReference();
////        visitedEventsAdapter.initializeReference();
//    }
//
//    public void onStop(){
//        super.onStop();
////        organizedEventsAdapter.cleanup();
////        visitedEventsAdapter.cleanup();
//    }


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
