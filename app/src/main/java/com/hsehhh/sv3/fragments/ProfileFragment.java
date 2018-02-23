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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    public void initViews(View view) {

        name = view.findViewById(R.id.profileName);
        flat = view.findViewById(R.id.profileFlat);
        email = view.findViewById(R.id.profileEmail);

        name.setText(presenter.user.name);
        flat.setText(presenter.user.room != null ? presenter.user.room.toString() : "Not specified");
        email.setText(presenter.firebaseUser.getEmail());

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.switchToProfileSettings();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        presenter.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews(view);

        return view;
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
