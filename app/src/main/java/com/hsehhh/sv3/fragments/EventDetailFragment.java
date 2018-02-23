package com.hsehhh.sv3.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.adapters.ChatAdapter;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.Message;
import java.util.Locale;

public class EventDetailFragment extends Fragment {
    // Data
    Event event;

    MainActivity presenter;

    TextView eventTitleTextView;
    TextView eventDescriptionTextView;
    TextView eventFloorTextView;
    TextView eventUserIdTextView;

    TextView messageTextView;
    Button sendMessageButton;

    RecyclerView chat;

    ChatAdapter chatAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_detail, container, false);

        // мне не нравится. буду разбираться с тем, как это лучше написать
        presenter.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getArguments();
        event = arguments.getParcelable("event");

        eventTitleTextView = v.findViewById(R.id.layout);
        eventDescriptionTextView = v.findViewById(R.id.text_view_description);
        eventFloorTextView = v.findViewById(R.id.text_view_floor);
        eventUserIdTextView = v.findViewById(R.id.text_view_published_by);

        eventTitleTextView.setText(event.title);
        eventDescriptionTextView.setText(event.description);
        eventFloorTextView.setText(String.format(Locale.ROOT, "%d", event.room.floor));
        eventUserIdTextView.setText(event.published_by);


        chatAdapter = new ChatAdapter(presenter, event.key);

        chat = v.findViewById(R.id.recycler_chat);
        chat.setLayoutManager(new LinearLayoutManager(getContext()));
        chat.setAdapter(chatAdapter);

        messageTextView = v.findViewById(R.id.edit_text_message);
        sendMessageButton = v.findViewById(R.id.button_send_message);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageTextView.getText().toString().trim();
                if (!messageText.isEmpty())
                    sendMessage(messageText);
            }
        });

        return v;
    }

    private void sendMessage(String messageText) {
        presenter.getChatsReference().push().setValue(new Message( presenter.firebaseUser.getUid(), messageText));
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
