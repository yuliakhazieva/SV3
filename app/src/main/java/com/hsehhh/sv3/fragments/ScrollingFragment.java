package com.hsehhh.sv3.fragments;

import com.google.firebase.database.DatabaseError;
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
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.Event;

import java.util.HashMap;

/**
 * Created by a1 on 20.01.18.
 */

public class ScrollingFragment extends android.support.v4.app.Fragment
{
    MainActivity presenter;

    public Button showEvents;
    public TableLayout table;
    public HashMap<String, Event> eventsMap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
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

        //кнопка мои события
        showEvents = getView().findViewById(R.id.showMyEvents);
        showEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.switchToMyEvents();
            }
        });

        //делаем пустую таблицу
        table = getView().findViewById(R.id.table);
        table.setPadding(0,130,0,0);
        for(int i = 0; i < 25; i++)
        {
            TableRow newRow = new TableRow(getContext());
            newRow.setTag("r" + i);
            newRow.setMinimumHeight(90);
            table.addView(newRow, i);
        }

        //работа с бд
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("events");
        ChildEventListener childListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                final Event e = dataSnapshot.getValue(Event.class);
                e.setKey(dataSnapshot.getKey());
                eventsMap.put(e.key, e);

                TableRow trow = (TableRow) table.getChildAt(e.room.floor);
                int aptNum = e.room.aptNumber;
                if(trow.getChildAt(aptNum) != null)
                {
                    //тут логика двух иконок в одном месте
                } else {
                    ImageButton ib = new ImageButton(presenter.getBaseContext());

                    ib.setImageResource(R.drawable.common_google_signin_btn_icon_light);
                    ib.setLayoutParams(new TableRow.LayoutParams(aptNum));
                    ib.setTag("one"); //если в этой ячейке токо одно событие
                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //делаем так чтобы если что-то изменилось в объекте события мы всегда брали его последнюю версию из мапы
                            MyAlertDialogFragment dialogFragment = MyAlertDialogFragment.newInstance(e);
                            dialogFragment.show(presenter.getSupportFragmentManager(), "dlg1");
                        }
                    });
                    trow.addView(ib, 0);
                    ib.setTag(e.room.floor+e.room.aptNumber);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                TableRow tRow =  (TableRow) table.getChildAt(dataSnapshot.getValue(Event.class).room.floor);
//                if(tRow.getChildAt(dataSnapshot.getValue(Event.class).aptNumber).getTag() != "one")
            //    {

                    //логика если там были еще евенты
            //    } else {
                    eventsMap.put(dataSnapshot.getValue(Event.class).key, dataSnapshot.getValue(Event.class));
            //    }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TableRow tRow =  (TableRow) table.getChildAt(dataSnapshot.getValue(Event.class).room.floor);
//                if(tRow.getChildAt(dataSnapshot.getValue(Event.class).aptNumber).getTag() != "one")
//                {
//                    //логика если там были еще евенты
//                } else {
                    tRow.removeView(view.findViewWithTag(dataSnapshot.getValue(Event.class).room.floor+dataSnapshot.getValue(Event.class).room.aptNumber));
                    eventsMap.remove(dataSnapshot.getValue(Event.class).key);

                    //отписываем пользователя от несуществующего события
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/subscribedTo");
                    //потестить
                 ref.child(dataSnapshot.getKey()).removeValue();
                //   }
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
                presenter.switchToProfile();
                return true;
            }
            case R.id.action_add:{
                presenter.switchToCreateEvent();
                return true;
            }
            case R.id.sign_out:{
                FirebaseAuth.getInstance().signOut();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
