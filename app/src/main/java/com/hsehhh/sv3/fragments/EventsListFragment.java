package com.hsehhh.sv3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.EventListener;


public class EventsListFragment extends android.support.v4.app.Fragment
{
    MainActivity presenter;

    public RecyclerView eventsView;

    EventsAdapter eventsAdapter;

    EventFilter eventFilter;

    public static EventsListFragment newInstance() {
        EventsListFragment fragment = new EventsListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
        eventsAdapter = new EventsAdapter(presenter, eventFilter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_events_list, container, false);

        eventsView = v.findViewById(R.id.recycler_events);
        eventsView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventsView.setAdapter(eventsAdapter);

        return v;
    }

    public void changeEventFilter(EventFilter eventFilter) {
        this.eventFilter = eventFilter;
        if (eventsAdapter != null)
            eventsAdapter.initializeReference(eventFilter);
    }
}
