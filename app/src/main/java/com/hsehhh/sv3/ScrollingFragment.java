package com.hsehhh.sv3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsehhh.sv3.data.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by a1 on 20.01.18.
 */

public class ScrollingFragment extends android.support.v4.app.Fragment
{
    FragmentSwitcher fragmentSwitcher;

    public Button showEvents;
    public TableLayout table;
    public Map<String, Event> eventsMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentSwitcher = (FragmentSwitcher) getActivity();
        setRetainInstance(true);
        eventsMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null)
            getFragmentManager().getFragment(savedInstanceState, "scroll");

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
            newRow.setMinimumHeight(90);
            table.addView(newRow, i);
        }

        showEvents = getView().findViewById(R.id.showMyEvents);
        showEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentSwitcher.switchToMyEvents();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events");

        ChildEventListener childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Event e = dataSnapshot.getValue(Event.class);
                e.setKey(dataSnapshot.getKey());
                eventsMap.put(e.key, e);

                TableRow trow = (TableRow) table.getChildAt(e.floor);
                int aptNum = e.aptNumber;
                if(trow.getChildAt(aptNum) != null)
                {
                    //тут логика двух иконок в одном месте
                } else {
                    ImageButton ib = new ImageButton(getContext());
                    ib.setImageResource(R.drawable.common_google_signin_btn_icon_light);
                    ib.setLayoutParams(new TableRow.LayoutParams(aptNum));
                    ib.setTag("one");
                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fragmentSwitcher.switchToEventDetails(e);
                        }
                    });
                    trow.addView(ib, 0);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TableRow tRow =  (TableRow) table.getChildAt(dataSnapshot.getValue(Event.class).floor);
                if(tRow.getChildAt(dataSnapshot.getValue(Event.class).aptNumber).getTag() != "one") {
                    //логика если там были еще евенты
                } else {
                    eventsMap.put(dataSnapshot.getValue(Event.class).key, dataSnapshot.getValue(Event.class));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TableRow tRow =  (TableRow) table.getChildAt(dataSnapshot.getValue(Event.class).floor);
                if(tRow.getChildAt(dataSnapshot.getValue(Event.class).aptNumber).getTag() != "one") {
                    //логика если там были еще евенты
                } else {
                    tRow.removeViewAt(dataSnapshot.getValue(Event.class).aptNumber);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //не наш случай
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //ошибаются токо ЛОХИ (я не знаю что тут делать пока)
            }
        };

        ref.addChildEventListener(childListener);

    }

    @Override
    public void onResume(){
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Toast.makeText(getContext(), "Settings", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.action_add:{
                fragmentSwitcher.switchToCreateEvent();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
