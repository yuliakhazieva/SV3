package com.hsehhh.sv3.fragments;
/**
 * Created by a1 on 18.01.18.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.interfaces.FragmentSwitcher;

public class CreateEventFragment extends Fragment
{
    DatabaseReference mDatabase;

    MainActivity presenter;

    EditText eventTitleEditText;
    EditText eventDescriptionEditText;
    EditText eventFloorEditText;
    Spinner eventTypeSpinner;
    Button submitButton;

    //Пока что так. А по-хорошему надо сделать дефолтный спиннер и унести отсюда ресурсы
    String[] eventTypes = {"fun", "request"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("events");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_event, container, false);

        // мне не нравится. буду разбираться с тем, как это лучше написать
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        eventTitleEditText = v.findViewById(R.id.edit_text_title);
        eventDescriptionEditText = v.findViewById(R.id.edit_text_description);
        eventFloorEditText = v.findViewById(R.id.edit_text_floor);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, eventTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        eventTypeSpinner = v.findViewById(R.id.spinner_select_type);
        eventTypeSpinner.setAdapter(adapter);
        eventTypeSpinner.setSelection(0);

        submitButton = v.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event e = new Event(eventTitleEditText.getText().toString(),
                        eventDescriptionEditText.getText().toString(),
                        eventTypeSpinner.getSelectedItem().toString(),

                        presenter.firebaseUser.getUid(),

                        Integer.parseInt(eventFloorEditText.getText().toString()),
                        null);
                mDatabase.push().setValue(e);
                Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
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


