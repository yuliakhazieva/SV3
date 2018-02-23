package com.hsehhh.sv3.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;


public class ProfileFragment extends Fragment {

    MainActivity presenter;

    TextView name, flat, email;
    EditText newName, newFlat;

    private ProfileSettingsFragment profileSettingsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        presenter.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getActivity().setContentView(R.layout.fragment_profile);

//        Toolbar toolbar = getView().findViewById(R.id.toolbar);
//        ((AppCompatActivity) myActivity).setSupportActionBar(toolbar);
//
        name = view.findViewById(R.id.profileName);
        flat = view.findViewById(R.id.profileFlat);
        email = view.findViewById(R.id.profileEmail);

        newName = view.findViewById(R.id.newName);
        newFlat = view.findViewById(R.id.newFlat);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.switchToProfileSettings(false);
            }
        });
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }

    public void onInfoChanged() {

        // TODO: first create database child and then get this from database every time
        name.setText(newName.getText());
        flat.setText(newFlat.getText());
        // what about email?
        // changing it manually seems stupid so it should be given from database only
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                presenter.switchToPrevious();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
