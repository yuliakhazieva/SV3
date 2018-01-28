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

public class CreateEventFragment extends Fragment

{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Button done = getActivity().findViewById(R.id.doneButt);
        // Inflate the layout for this fragment
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar_main);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((MainActivity)getActivity()).addNewEventObject();
//            }
//        });

       // return inflater.inflate(R.layout.activity_create_event, container, false);

        Event e = new Event("title", "description", "user_id", false);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        String userId = mDatabase.push().getKey();
        mDatabase.child(userId).setValue(e);
        return inflater.inflate(R.layout.create_event_fragment, container, false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(this.getId(), new ScrollingFragment());
        fragmentTransaction.remove(this);
     //   fragmentTransaction.show(getFragmentManager().findFragmentByTag("myEvents"));
        fragmentTransaction.commit();
        return super.onOptionsItemSelected(item);
    }
}