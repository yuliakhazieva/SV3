package com.hsehhh.sv3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.adapters.EventsAdapter;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.User;
import com.hsehhh.sv3.interfaces.EventFilter;
import com.hsehhh.sv3.interfaces.FragmentSwitcher;

import java.util.ArrayList;
import java.util.List;


public class MyEventsFragment extends android.support.v4.app.Fragment
{
    MainActivity presenter;

    public RecyclerView organizedEventsView;
    public RecyclerView visitedEventsView;

    DatabaseReference eventsReference;

    EventsAdapter organizedEventsAdapter;
    EventsAdapter visitedEventsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();

        eventsReference = FirebaseDatabase.getInstance().getReference().child("events");
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Надо наверное написать дефолтные фильтры. Пока пусть так.
        //EventFilter orgFilter = new EventFilter();
        organizedEventsAdapter = new EventsAdapter(presenter, new EventFilter() {
            @Override
            public boolean filter(Event e) {
                //заглушка
               // return true;
                //вот правильное

                return e.published_by.equals(FirebaseAuth.getInstance().getUid());

            }
        } );

        organizedEventsAdapter.isHosted = true;
        visitedEventsAdapter = new EventsAdapter(presenter, new EventFilter() {
            @Override
            public boolean filter(Event e) {
//                if(e.participants != null) {
//                    for (String participant : e.participants) {
//                        if (participant.equals(presenter.firebaseUser.getUid()))
//                            return true;
//                    }
//                }
                return false;


                //заглушка
               // return true;
            }
        });
        visitedEventsAdapter.isHosted = false;
        organizedEventsView = v.findViewById(R.id.recycler_organized_events);
        organizedEventsView.setLayoutManager(new LinearLayoutManager(getContext()));
        organizedEventsView.setAdapter(organizedEventsAdapter);

        visitedEventsView = v.findViewById(R.id.recycler_visited_events);
        visitedEventsView.setLayoutManager(new LinearLayoutManager(getContext()));
        visitedEventsView.setAdapter(visitedEventsAdapter);

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();
//        Тут еще надо с инициализацией похимичить. Унести в правильные места.
//        organizedEventsAdapter.initializeReference();
//        visitedEventsAdapter.initializeReference();
    }

    public void onStop(){
        super.onStop();
//        organizedEventsAdapter.cleanup();
//        visitedEventsAdapter.cleanup();
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
