package com.hsehhh.sv3.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.User;

/**
 * Created by a1 on 22.02.18.
 */

public class MyAlertDialogFragment extends DialogFragment {
    Event event;
    MainActivity presenter;

    TextView publisher;
    TextView apt;
    TextView description;
    TextView date;
    TextView time;
    AlertDialog.Builder builder;

    public static MyAlertDialogFragment newInstance(Event event) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("event", event);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        event = args.getParcelable("event");

        presenter = (MainActivity) getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(presenter);

        LayoutInflater inflater = presenter.getLayoutInflater();
        View view = inflater.inflate(R.layout.new_event_detail, null);

        builder.setView(view)
                .setTitle(event.title)
                .setPositiveButton("Пойду", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.getEventsReference().child(event.key).child("participants").push().setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        if(event.published_by != presenter.firebaseUser.getUid())
                            presenter.getUsersReference().child(presenter.firebaseUser.getUid()).child("subscribedTo").push().setValue(event.key);
                    }
                });


        builder.create();
        publisher = view.findViewById(R.id.text_view_published_by);
        apt = view.findViewById(R.id.text_view_floor);
        description = view.findViewById(R.id.text_view_description);
        date = view.findViewById(R.id.text_view_date);
        time = view.findViewById(R.id.text_view_time);

        Query q = presenter.getUsersReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                publisher.setText(dataSnapshot.getValue(User.class).name);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        apt.setText("" + event.room.floor + event.room.aptNumber);
        description.setText(event.description);
        date.setText(event.date);
        time.setText(event.time);

        builder.setView(view);

        return builder.create();
    }
}
