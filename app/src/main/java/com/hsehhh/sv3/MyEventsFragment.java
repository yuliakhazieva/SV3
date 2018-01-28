package com.hsehhh.sv3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by a1 on 21.01.18.
 */

public class MyEventsFragment extends android.support.v4.app.Fragment
{
    public SwitchToScrolling listener;

    public ImageView closeImageView;
    public RecyclerView organizedEventsView;
    public RecyclerView visitedEventsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener = (SwitchToScrolling) getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, container, false);

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
//        organizedEventsView.setAdapter(createAdapter());
        visitedEventsView = v.findViewById(R.id.recycler_visited_events);
        visitedEventsView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

}
