package com.hsehhh.sv3;
/**
 * Created by a1 on 18.01.18.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class CreateEventFragment extends Fragment

{
    SwitchToScrolling listener;
    Button done;
    String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listener = (SwitchToScrolling)getActivity();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Inflate the layout for this fragment
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_main);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        return inflater.inflate(R.layout.create_event_fragment, container, false);

    }

    @Override
    public void onViewCreated (final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        done = getView().findViewById(R.id.doneButt);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rand = new Random(24);
                Event e = new Event("one", "two", "three", false, rand.nextInt());
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("activities");
                String userId = mDatabase.push().getKey();
                mDatabase.child(userId).setValue(e);
                listener.switchToScrolling();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        listener.switchToScrolling();
        return super.onOptionsItemSelected(item);
    }
}
