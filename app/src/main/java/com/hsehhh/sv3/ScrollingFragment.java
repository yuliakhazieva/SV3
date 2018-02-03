package com.hsehhh.sv3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

/**
 * Created by a1 on 20.01.18.
 */

public class ScrollingFragment extends android.support.v4.app.Fragment
{
    Toolbar mainToolbar;
    public SwitchToCreateEvent listenerCreateEvent;
    public SwitchToMyEvents listenerMyEvents;
    public SwitchToEventDetails listenerEventDetails;

    public Button showEvents;
    public TableLayout table;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listenerCreateEvent = (SwitchToCreateEvent) getActivity();
        listenerMyEvents = (SwitchToMyEvents) getActivity();
        listenerEventDetails = (SwitchToEventDetails) getActivity();
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            //Restore the fragment's instance
            getFragmentManager().getFragment(savedInstanceState, "scroll");
        }

        return inflater.inflate(R.layout.new_grid_scrolling, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getFragmentManager().putFragment(outState, "lscrol", this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scroll_frag, menu);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        table = getView().findViewById(R.id.table);
        table.setPadding(0,130,0,0);
        for(int i = 0; i < 25; i++)
        {
            TableRow newRow = new TableRow(getContext());
            newRow.setTag("r" + i);
            for(int j = 0; j < 6; j++)
            {
                Space space = new Space(getContext());
                space.setLayoutParams(new TableRow.LayoutParams(100, 175));
                space.setTag("s" + i + j);
                space.setClickable(false);
                newRow.addView(space, j);
            }
            table.addView(newRow, i);
        }

        showEvents = getView().findViewById(R.id.showMyEvents);
        showEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerMyEvents.switchToMyEvents();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren())
                    {
                        final Event e = childSnapshot.getValue(Event.class);
                        e.setKey(childSnapshot.getKey());
                        Random rand = new Random();
                        int num = rand.nextInt(5);
                        TableRow trow = (TableRow) table.getChildAt(e.floor);
                        while (trow.getChildAt(num).isClickable()) {
                            num = rand.nextInt(5);
                        }
                        trow.removeViewAt(num);
                        ImageButton ib = new ImageButton(getContext());
                        ib.setImageResource(R.drawable.common_google_signin_btn_icon_light);
                        ib.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listenerEventDetails.switchToEventDetails(e);
                            }
                        });
                        trow.addView(ib, num);
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Database error.", Toast.LENGTH_SHORT).show();
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
                listenerCreateEvent.switchToCreateEvent();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}

