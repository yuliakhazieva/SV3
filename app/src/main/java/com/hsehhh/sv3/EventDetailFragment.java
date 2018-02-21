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
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Message> messages;
    private DatabaseReference messagesReference;
    private ChildEventListener childEventListener;

    public ChatAdapter(String eventId) {
        messages = new ArrayList<>(0);
        initializeReference(eventId);
    }

    @Override
    public int getItemCount() { return messages.size(); }

    private int getMessageIndex(Message e) {
        for (int i = 0; i < getItemCount(); i++)
            if (messages.get(i).equals(e))
                return i;
        return -1;
    }

    public void initializeReference(String eventId){
        messagesReference = FirebaseDatabase.getInstance().getReference("chats").child(eventId);
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message model = dataSnapshot.getValue(Message.class);
                messages.add(model);
                notifyDataSetChanged();
                }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Message model = dataSnapshot.getValue(Message.class);
                    int messageIndex = getMessageIndex(model);
                    if (messageIndex != -1) // бывает ли иначе? хм.
                        messages.set(messageIndex, model);
                    messages.add(model);
                    notifyDataSetChanged();
                }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message model = dataSnapshot.getValue(Message.class);
                int messageIndex = getMessageIndex(model);
                if (messageIndex != -1) // бывает ли иначе? хм.
                    messages.remove(messageIndex);
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        messagesReference.addChildEventListener(childEventListener);
    }

    public void cleanup() {
        if (messagesReference != null)
            messagesReference.removeEventListener(childEventListener);
        messages.clear();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_message, viewGroup, false);
        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int i) {
        Message model = messages.get(i);

        holder.user_id.setText(model.user_id);
        holder.message.setText(model.message);
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView user_id;
        TextView message;

        MessageViewHolder(View v) {
            super(v);

            user_id = v.findViewById(R.id.text_view_user_id);
            message =  v.findViewById(R.id.text_view_message);
        }
    }
}

public class EventDetailFragment extends Fragment {
    // Data
    Event event;

    FragmentSwitcher fragmentSwitcher;

    TextView eventTitleTextView;
    TextView eventDescriptionTextView;
    TextView eventFloorTextView;
    TextView eventUserIdTextView;

    TextView messageTextView;
    Button sendMessageButton;

    RecyclerView chat;

    ChatAdapter chatAdapter;
    DatabaseReference messagesReference;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentSwitcher = (FragmentSwitcher) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_detail, container, false);

        // мне не нравится. буду разбираться с тем, как это лучше написать
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getArguments();
        event = arguments.getParcelable("event");
        messagesReference = FirebaseDatabase.getInstance().getReference("chats").child(event.key);

        eventTitleTextView = v.findViewById(R.id.text_view_title);
        eventDescriptionTextView = v.findViewById(R.id.text_view_description);
        eventFloorTextView = v.findViewById(R.id.text_view_floor);
        eventUserIdTextView = v.findViewById(R.id.text_view_published_by);

        eventTitleTextView.setText(event.title);
        eventDescriptionTextView.setText(event.description);
        eventFloorTextView.setText(String.format(Locale.ROOT, "%d", event.floor));
        eventUserIdTextView.setText(event.published_by);

        chatAdapter = new ChatAdapter(event.key);

        chat = v.findViewById(R.id.recycler_chat);
        chat.setLayoutManager(new LinearLayoutManager(getContext()));
        chat.setAdapter(chatAdapter);

        messageTextView = v.findViewById(R.id.edit_text_message);
        sendMessageButton = v.findViewById(R.id.button_send_message);
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "hi!", Toast.LENGTH_SHORT).show();
                String message = messageTextView.getText().toString().trim();
                if (!message.isEmpty())
                    messagesReference.push().setValue(new Message( "uid1", message));
            }
        });

        return v;
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
