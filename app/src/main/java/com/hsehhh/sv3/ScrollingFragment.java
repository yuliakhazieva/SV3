package com.hsehhh.sv3;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
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
    public TableLayout table;
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
    public void onCreate(Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        table = getView().findViewById(R.id.table);
        for(int i = 0; i < 25; i++)
        {
            TableRow newRow = new TableRow(getContext());
            newRow.setTag("r" + i);
            for(int j = 0; j < 6; j++)
            {
                Space space = new Space(getContext());
                space.setMinimumWidth(50);
                space.setMinimumHeight(50);
                space.setTag("s" + i + j);
                space.setClickable(false);
                newRow.addView(space, j);
            }
            table.addView(newRow, i);
        }

        listenerMyEvents = (SwitchToMyEvents)getActivity();
        showEvents = getView().findViewById(R.id.showMyEvents);
        showEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenerMyEvents.switchToMyEvents();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("activities");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    for(DataSnapshot childSnapshot: postSnapshot.getChildren())
                    {
                        Event e = childSnapshot.getValue(Event.class);
                        Random rand = new Random();
                        int num = rand.nextInt(5);
                        TableRow trow = (TableRow) table.getChildAt(e.floor);
                        while (trow.getChildAt(num).isClickable()) {
                            num = rand.nextInt(5);
                        }
                        trow.removeViewAt(num);
                        ImageButton ib = new ImageButton(getContext());
                        ib.setImageResource(R.drawable.common_google_signin_btn_icon_light);
                        trow.addView(ib, num);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                listenerCreateEvent = (SwitchToCreateEvent) getActivity();
                listenerCreateEvent.switchToCreateEvent();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}

