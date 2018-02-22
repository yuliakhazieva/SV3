package com.hsehhh.sv3.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.Event;

/**
 * Created by a1 on 22.02.18.
 */

public class MyAlertDialogFragment extends DialogFragment {

    static Event e;
    TextView publisher;
    TextView apt;
    TextView description;
    AlertDialog.Builder builder;

    public static MyAlertDialogFragment newInstance(Event event) {
        e = event;
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View thisView = inflater.inflate(R.layout.new_event_detail, null);

        builder.setView(thisView)
                .setTitle(e.title)
                .setPositiveButton("Пойду", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // e.participants.add(FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getUid()));
                        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getUid()).child("subscribedTo").push().setValue(e.key);
                    }
                })
                .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        builder.create();
        publisher = thisView.findViewById(R.id.text_view_published_by);
        apt = thisView.findViewById(R.id.text_view_floor);
        description = thisView.findViewById(R.id.text_view_description);

        publisher.setText(e.published_by);
        apt.setText("" + e.floor+e.aptNumber);
        description.setText(e.description);

        builder.setView(thisView);

        return builder.create();
    }
}
