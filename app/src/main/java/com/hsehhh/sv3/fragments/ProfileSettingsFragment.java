package com.hsehhh.sv3.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;

public class ProfileSettingsFragment extends Fragment {

    MainActivity presenter;
    EditText newFlat;
    EditText newName;
    Button submit;

    private RoomPickerFragment roomPickerFragment;

    public boolean IS_NEW = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        newFlat = view.findViewById(R.id.newFlat);
        submit = view.findViewById(R.id.submit_changes);
        newName = view.findViewById(R.id.newName);

        // same flat number picker
        newFlat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {
                    roomPickerFragment = RoomPickerFragment.newInstance();
                    roomPickerFragment.show(presenter.getSupportFragmentManager(), "roomPicker");
                }
            }
        });

        // submit changes and switch back
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: here all the stuff with database (adding if IS_NEW or changing if !IS_NEW should happen)
                if (!newName.getText().toString().isEmpty()) {
                    if (IS_NEW)
                        presenter.switchToScrolling();
                    else
                        presenter.switchToProfile(true);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0)
            newFlat.setText(roomPickerFragment.room.toString());

    }

}
