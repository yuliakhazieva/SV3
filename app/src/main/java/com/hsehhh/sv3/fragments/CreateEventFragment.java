package com.hsehhh.sv3.fragments;
/**
 * Created by a1 on 18.01.18.
 */
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.Room;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateEventFragment extends Fragment {
    MainActivity presenter;

    EditText eventTitleEditText;
    EditText eventDescriptionEditText;
    EditText eventRoomEditText;
    EditText eventDateEditText;
    Spinner eventTypeSpinner;

    private RoomPickerFragment roomPickerFragment;
    private DatePickerDialog datePickerDialog;
    Calendar date;
    Button submitButton;

    //Пока что так. А по-хорошему надо сделать дефолтный спиннер и унести отсюда ресурсы
    String[] eventTypes = {"fun", "request"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
        setHasOptionsMenu(true);
        date = Calendar.getInstance();
    }

    public void initViews(View view){
        eventTitleEditText = view.findViewById(R.id.edit_text_title);
        eventDescriptionEditText = view.findViewById(R.id.edit_text_description);
        eventDateEditText = view.findViewById(R.id.edit_text_date);
        eventDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            date.set(year, monthOfYear, dayOfMonth);
                            new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    date.set(Calendar.MINUTE, minute);
                                    onActivityResult(1, 0, presenter.getIntent());
                                }
                            }, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), false).show();
                        }
                    }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE)).show();
                }
            }
        });
        eventRoomEditText = view.findViewById(R.id.edit_text_room);
        eventRoomEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    roomPickerFragment = RoomPickerFragment.newInstance();
                    roomPickerFragment.show(presenter.getSupportFragmentManager(), "roomPicker");
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, eventTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventTypeSpinner = view.findViewById(R.id.spinner_select_type);
        eventTypeSpinner.setAdapter(adapter);
        eventTypeSpinner.setSelection(0);

        submitButton = view.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createEvent();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 & resultCode == 0)
            eventRoomEditText.setText(roomPickerFragment.room.toString());
        else if (requestCode == 1 & resultCode == 0)
            eventDateEditText.setText(new SimpleDateFormat().format(date.getTime()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_event, container, false);
        presenter.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews(v);
        return v;
    }

    public void createEvent() {
        Event e = new Event(eventTitleEditText.getText().toString(),
                eventDescriptionEditText.getText().toString(),
                eventTypeSpinner.getSelectedItem().toString(),
                presenter.firebaseUser.getUid(),
                roomPickerFragment.room,
                "12/12/12", "12:12");
        presenter.getEventsReference().push().setValue(e);

        Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
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


