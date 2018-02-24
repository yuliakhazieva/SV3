package com.hsehhh.sv3.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.Room;

import static android.app.Activity.RESULT_OK;

/**
 * Created by a1 on 22.02.18.
 */

public class RoomPickerFragment extends DialogFragment {
    public static final int RC_ROOM_SET = 1;
    MainActivity presenter;

    final String[] sections = new String[] {"A", "B", "C"};

    NumberPicker section;
    NumberPicker floor;
    NumberPicker aptNumber;

    Room room;

    AlertDialog.Builder builder;

    public void initPickers(View view) {
        section = view.findViewById(R.id.picker_section);
        floor = view.findViewById(R.id.picker_floor);
        aptNumber = view.findViewById(R.id.picker_aptnumber);

        section.setMinValue(0);
        section.setMaxValue(sections.length - 1);
        section.setDisplayedValues(sections);

        floor.setMinValue(1);
        floor.setMaxValue(25);

        aptNumber.setMinValue(1);
        aptNumber.setMaxValue(4);
    }

    public static RoomPickerFragment newInstance() {
        return new RoomPickerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(presenter);

        LayoutInflater inflater = presenter.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_room, null);
        initPickers(view);

        builder.setView(view)
                .setTitle("Pick room")
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        room = new Room(sections[section.getValue()], floor.getValue(), aptNumber.getValue());
                        presenter.getSupportFragmentManager().findFragmentById(R.id.frame_main)
                                .onActivityResult(RC_ROOM_SET, RESULT_OK, presenter.getIntent());
                    }
                });
        builder.setView(view);

        return builder.create();
    }
}
