package com.hsehhh.sv3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by a1 on 21.01.18.
 */

class EventViewHolder extends RecyclerView.ViewHolder
{
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

public class MyEventsFragment extends android.support.v4.app.Fragment
{
    public SwitchToScrolling listener;

    public ImageView closeImageView;
    public RecyclerView organizedEventsView;
    public RecyclerView visitedEventsView;

    FirebaseDatabase database;
    DatabaseReference eventsReference;

    FirebaseRecyclerAdapter organizedEventsAdapter;
    FirebaseRecyclerAdapter visitedEventsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = (SwitchToScrolling) getActivity();

        database = FirebaseDatabase.getInstance();
        eventsReference = database.getReference().child("activities").child("events");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, container, false);

        //change queries to correct later
        organizedEventsAdapter = createAdapter(eventsReference);
        //change this to correct uid
        visitedEventsAdapter = createAdapter(eventsReference.equalTo("uid1"));

        closeImageView = v.findViewById(R.id.closeMyEvents);
        closeImageView.setClickable(true);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.switchToScrolling();
            }
        });

        organizedEventsView = v.findViewById(R.id.recycler_organized_events);
        organizedEventsView.setLayoutManager(new LinearLayoutManager(getContext()));
        organizedEventsView.setAdapter(organizedEventsAdapter);

        visitedEventsView = v.findViewById(R.id.recycler_visited_events);
        visitedEventsView.setLayoutManager(new LinearLayoutManager(getContext()));
        visitedEventsView.setAdapter(visitedEventsAdapter);

        return v;
    }

//<<<<<<< HEAD
//    @Override
//    public void onViewCreated (final View view, final Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        close = getView().findViewById(R.id.closeMyEvents);
//        close.setClickable(true);
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().findViewById(R.id.frame2).animate().translationY(1700).start();
////                setCloseVisibility(View.INVISIBLE);
//            }
//        });
 //   }

    public FirebaseRecyclerAdapter<Event, EventViewHolder> createAdapter(Query query)
    {
        FirebaseRecyclerOptions<Event> options =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(query, Event.class)
                        .build();

        return new FirebaseRecyclerAdapter<Event, EventViewHolder>(options) {
            @Override
            public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_item_event, parent, false);
                return new EventViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull final EventViewHolder holder, int position, @NonNull Event model) {
                holder.title.setText(model.title);
                holder.desciption.setText(model.description);
                holder.published_by.setText(model.published_by);
            }
        };
    }

    @Override
    public void onResume(){
        super.onResume();
        visitedEventsAdapter.startListening();
        organizedEventsAdapter.startListening();
    }

    public void onStop(){
        super.onStop();
        visitedEventsAdapter.startListening();
        organizedEventsAdapter.startListening();    }

}
