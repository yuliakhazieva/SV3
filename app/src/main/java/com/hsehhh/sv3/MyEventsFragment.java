package com.hsehhh.sv3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsehhh.sv3.data.Event;

import java.util.ArrayList;
import java.util.List;


public class MyEventsFragment extends android.support.v4.app.Fragment
{

    public FragmentSwitcher fragmentSwitcher;

    public RecyclerView organizedEventsView;
    public RecyclerView visitedEventsView;

    DatabaseReference eventsReference;

    EventsAdapter organizedEventsAdapter;
    EventsAdapter visitedEventsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentSwitcher = (FragmentSwitcher) getActivity();

        eventsReference = FirebaseDatabase.getInstance().getReference().child("events");
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Надо наверное написать дефолтные фильтры. Пока пусть так.
        organizedEventsAdapter = new EventsAdapter(new EventFilter() {
            @Override
            public boolean filter(Event e) { return e.published_by.equals("uid1"); }
        } );
        visitedEventsAdapter = new EventsAdapter(new EventFilter() {
            @Override
            public boolean filter(Event e) { return !e.published_by.equals("uid1"); }
        });

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
                fragmentSwitcher.switchToScrolling();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventViewHolder> {

        private List<Event> events;
        private DatabaseReference eventsReference;
        private ChildEventListener childEventListener;

        public EventsAdapter(EventFilter filter) {
            events = new ArrayList<>(0);
            initializeReference(filter);
        }

        @Override
        public int getItemCount() { return events.size(); }

        private int getEventIndex(Event e) {
            for (int i = 0; i < getItemCount(); i++)
                if (events.get(i).equals(e))
                    return i;
            return -1;
        }

        public void initializeReference(final EventFilter eventFilter){
            eventsReference = FirebaseDatabase.getInstance().getReference("events");
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Event model = dataSnapshot.getValue(Event.class);
                    model.setKey(dataSnapshot.getKey());

                    if (eventFilter.filter(model)) {
                        events.add(model);
                        notifyItemInserted(events.size() - 1);
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Event model = dataSnapshot.getValue(Event.class);
                    model.setKey(dataSnapshot.getKey());
                    if (eventFilter.filter(model)) {
                        int eventIndex = getEventIndex(model);
                        if (eventIndex != -1) { // бывает ли иначе? хм.
                            events.set(eventIndex, model);
                            notifyItemChanged(eventIndex);
                        }
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Event model = dataSnapshot.getValue(Event.class);
                    model.setKey(dataSnapshot.getKey());

                    if (eventFilter.filter(model)) {
                        int eventIndex = getEventIndex(model);
                        if (eventIndex != -1) { // бывает ли иначе? хм.
                            events.remove(eventIndex);
                            notifyItemRemoved(eventIndex);
                        }
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

                @Override
                public void onCancelled(DatabaseError databaseError) { }
            };
            eventsReference.addChildEventListener(childEventListener);
        }

        public void cleanup() {
            if (eventsReference != null)
                eventsReference.removeEventListener(childEventListener);
            events.clear();
        }

        @Override
        public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_event, viewGroup, false);
            return new EventViewHolder(v);
        }

        @Override
        public void onBindViewHolder(EventViewHolder holder, int i) {
            final Event model = events.get(i);

            holder.title.setText(model.title);
            holder.desciption.setText(model.description);
            holder.published_by.setText(model.published_by);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragmentSwitcher.switchToEventDetails(model);
                }
            });

        }


        class EventViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView desciption;
            TextView published_by;

            EventViewHolder(View v) {
                super(v);

                title = v.findViewById(R.id.event_title);
                desciption =  v.findViewById(R.id.event_description);
                published_by = v.findViewById(R.id.published_user_id);
            }
        }

    }
}
