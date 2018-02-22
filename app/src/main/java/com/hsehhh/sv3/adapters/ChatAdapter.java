package com.hsehhh.sv3.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.Message;
import com.hsehhh.sv3.viewholders.MessageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tima on 22.02.2018.
 */
public class ChatAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    MainActivity presenter;

    private List<Message> messages;
    private ChildEventListener childEventListener;

    public ChatAdapter(MainActivity presenter, String eventId) {
        this.presenter = presenter;
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
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message model = dataSnapshot.getValue(Message.class);
                messages.add(model);
                notifyItemInserted(messages.size() - 1);
                }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Message model = dataSnapshot.getValue(Message.class);
                    int messageIndex = getMessageIndex(model);
                    if (messageIndex != -1) // бывает ли иначе? хм.
                        messages.set(messageIndex, model);
                    messages.add(model);
                    notifyItemChanged(messageIndex);
                }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Message model = dataSnapshot.getValue(Message.class);
                int messageIndex = getMessageIndex(model);
                if (messageIndex != -1) // бывает ли иначе? хм.
                    messages.remove(messageIndex);
                notifyItemRemoved(messageIndex);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        presenter.getEventsReference().child(eventId).addChildEventListener(childEventListener);
    }

    public void cleanup() {
        if (presenter.getEventsReference() != null)
            presenter.getEventsReference().removeEventListener(childEventListener);
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


}
