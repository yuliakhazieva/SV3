package com.hsehhh.sv3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.Message;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by a1 on 21.02.18.
 */

public class NewEventDetail extends Fragment
{
    // Data
    Event event;

    FragmentSwitcher fragmentSwitcher;

    TextView eventTitleTextView;
    TextView eventDescriptionTextView;
    TextView eventFloorTextView;
    TextView eventUserIdTextView;

    TextView messageTextView;
    Button sendMessageButton;

    ImageButton close;
    Button go;

    RecyclerView chat;

    ChatAdapter chatAdapter;
    DatabaseReference messagesReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentSwitcher = (FragmentSwitcher) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_event_detail, container, false);

        // мне не нравится. буду разбираться с тем, как это лучше написать
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getArguments();
        event = arguments.getParcelable("event");
        messagesReference = FirebaseDatabase.getInstance().getReference("chats").child(event.key);

        eventTitleTextView = v.findViewById(R.id.text_view_title);
        eventDescriptionTextView = v.findViewById(R.id.text_view_description);
        eventFloorTextView = v.findViewById(R.id.text_view_floor);
        eventUserIdTextView = v.findViewById(R.id.text_view_published_by);
        close = v.findViewById(R.id.button_close);
        go = v.findViewById(R.id.button_will_go);

        eventTitleTextView.setText(event.title);
        eventDescriptionTextView.setText(event.description);
        eventFloorTextView.setText(String.format(Locale.ROOT, "%d", event.floor));
        eventUserIdTextView.setText(event.published_by);

//        chatAdapter = new ChatAdapter(event.key);
//
//        chat = v.findViewById(R.id.recycler_chat);
//        chat.setLayoutManager(new LinearLayoutManager(getContext()));
//        chat.setAdapter(chatAdapter);

//        messageTextView = v.findViewById(R.id.edit_text_message);
//        sendMessageButton = v.findViewById(R.id.button_send_message);
//        sendMessageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Toast.makeText(getContext(), "hi!", Toast.LENGTH_SHORT).show();
//                String message = messageTextView.getText().toString().trim();
//                if (!message.isEmpty())
//                    messagesReference.push().setValue(new Message( "uid1", message));
//            }
//        });

        return v;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState)
    {
        close.setClickable(true);
        close.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        fragmentSwitcher.removeDetail();
                    }
                }
        );

        go.setClickable(true);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + "uid1" + "/subscribedTo");
                ref.push().setValue(event.key);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                fragmentSwitcher.switchToPrevious();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
