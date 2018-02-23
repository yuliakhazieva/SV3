package com.hsehhh.sv3.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.User;

import static android.app.Activity.RESULT_OK;
import static com.hsehhh.sv3.MainActivity.RC_USER_CHANGED;
import static com.hsehhh.sv3.fragments.RoomPickerFragment.RC_ROOM_SET;

public class ProfileSettingsFragment extends Fragment {
    MainActivity presenter;

    EditText newFlat;
    EditText newName;
    Button submit;

    private RoomPickerFragment roomPickerFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        presenter.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newFlat = view.findViewById(R.id.newFlat);
        submit = view.findViewById(R.id.submit_changes);
        newName = view.findViewById(R.id.newName);

        // same flat number picker
        newFlat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    roomPickerFragment = RoomPickerFragment.newInstance();
                    roomPickerFragment.show(presenter.getSupportFragmentManager(), "roomPicker");
                }
            }
        });

        // submit changes and switch back
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (presenter.user == null) {
                    presenter.user = new User(newName.getText().toString(), roomPickerFragment.room);
                } else {
                    presenter.user.name = newName.getText().toString();
                    presenter.user.room = roomPickerFragment.room;
                }
                presenter.onActivityResult(RC_USER_CHANGED, Activity.RESULT_OK, presenter.getIntent());
                presenter.switchToPrevious();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RC_ROOM_SET: {
                if (resultCode == RESULT_OK) {
                    newFlat.setText(roomPickerFragment.room.toString());
                    break;
                }
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (presenter.user == null)
                    Toast.makeText(presenter, "Enter info", Toast.LENGTH_SHORT).show();
                    //presenter.onActivityResult(RC_USER_CHANGED, Activity.RESULT_CANCELED, presenter.getIntent());
                else
                    presenter.switchToPrevious();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
