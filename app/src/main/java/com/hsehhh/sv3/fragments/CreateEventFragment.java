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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.hsehhh.sv3.fragments.RoomPickerFragment.RC_ROOM_SET;

public class CreateEventFragment extends Fragment {
    public static final int RC_TIME_SET = 3;
    MainActivity presenter;

    EditText eventTitleEditText;
    EditText eventDescriptionEditText;
    EditText eventRoomEditText;
    EditText eventDateEditText;
    Spinner eventTypeSpinner;
    String date_string;

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
        roomPickerFragment = RoomPickerFragment.newInstance();
        date_string = "";
    }

    public void initViews(View view){
        eventTitleEditText = view.findViewById(R.id.edit_text_title);
        eventDescriptionEditText = view.findViewById(R.id.edit_text_description);
        eventDateEditText = view.findViewById(R.id.edit_text_date);
        eventDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.set(year, monthOfYear, dayOfMonth);
                        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                date.set(Calendar.MINUTE, minute);
                                onActivityResult(RC_TIME_SET, RESULT_OK, presenter.getIntent());
                            }
                        }, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), true).show();
                    }
                }, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE)).show();
            }
        });
        eventRoomEditText = view.findViewById(R.id.edit_text_room);
        eventRoomEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomPickerFragment.show(presenter.getSupportFragmentManager(), "roomPicker");
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
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        switch (requestCode) {
            case RC_ROOM_SET: {
                if (resultCode == RESULT_OK) {
                    eventRoomEditText.setText(roomPickerFragment.room.toString());
                    break;
                }
            }
            case RC_TIME_SET: {
                if (resultCode == RESULT_OK) {
                    date_string = new SimpleDateFormat("dd-MM HH:mm").format(date.getTime());
                    eventDateEditText.setText(date_string);
                    break;
                }
            }
            default: {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
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
                date.getTimeInMillis(), null);
        if (!isFormInvalid()) {
            presenter.getEventsReference().push().setValue(e);
            Toast.makeText(getContext(), "Событие добавлено", Toast.LENGTH_SHORT).show();
            presenter.switchToScrolling();
        } else {
            Toast.makeText(getContext(), "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isFormInvalid() {
        return roomPickerFragment.room == null ||
                eventTitleEditText.getText().toString().trim().isEmpty() ||
                eventDescriptionEditText.getText().toString().trim().isEmpty() ||
                date_string.trim().isEmpty();
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


