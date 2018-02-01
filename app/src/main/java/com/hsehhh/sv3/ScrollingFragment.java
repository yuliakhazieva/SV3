package com.hsehhh.sv3;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by a1 on 20.01.18.
 */

public class ScrollingFragment extends android.support.v4.app.Fragment
{
    Toolbar mainToolbar;
    public SwitchToCreateEvent listenerCreateEvent;
    public SwitchToMyEvents listenerMyEvents;
    public Button showEvents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.new_grid_scrolling, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);

        final GridLayout grid = getActivity().findViewById(R.id.grid);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("activities");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                final Event newEvent = dataSnapshot.getValue(Event.class);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams(GridLayout.spec(25), GridLayout.spec(ThreadLocalRandom.current().nextInt(0, 5 + 1)));
                ImageButton ib = new ImageButton(getContext());
                ib.setImageResource(R.drawable.fff);
                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        //TODO
                    }
                });
                grid.addView(ib, params);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scroll_frag, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        listenerMyEvents = (SwitchToMyEvents)getActivity();
        showEvents = getView().findViewById(R.id.showMyEvents);
        showEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerMyEvents.switchToMyEvents();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.action_add:{
//                FragmentTransaction fragmentTransaction =getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(this.getId(), new CreateEventFragment());
//                fragmentTransaction.remove(this);
//                fragmentTransaction.hide(getFragmentManager().findFragmentByTag("myEvents"));
//                fragmentTransaction.commit();
                listenerCreateEvent = (SwitchToCreateEvent) getActivity();
                listenerCreateEvent.switchToCreateEvent();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
