package com.hsehhh.sv3.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.interfaces.EventFilter;
import com.hsehhh.sv3.viewholders.EventViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tima on 22.02.2018.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventViewHolder> {
    MainActivity presenter;

    private List<Event> events;
    private ChildEventListener childEventListener;

    public EventsAdapter(MainActivity presenter, EventFilter filter) {
        this.presenter = presenter;
        events = new ArrayList<>(0);
        initializeReference(filter);
    }

    @Override
    public int getItemCount() { return events.size(); }

    private int getEventIndex(Event e) {
        for (int i = 0; i < getItemCount(); i++)
            if (events.get(i).equals(e))
                return i;
        return -1;
    }

    public void initializeReference(final EventFilter eventFilter){
//        cleanup();
        events.clear();
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event model = dataSnapshot.getValue(Event.class);
                model.setKey(dataSnapshot.getKey());

                if (eventFilter.filter(model)) {
                    events.add(model);
                    notifyItemInserted(events.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Event model = dataSnapshot.getValue(Event.class);
                model.setKey(dataSnapshot.getKey());
                if (eventFilter.filter(model)) {
                    int eventIndex = getEventIndex(model);
                    if (eventIndex != -1) { // бывает ли иначе? хм.
                        events.set(eventIndex, model);
                        notifyItemChanged(eventIndex);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Event model = dataSnapshot.getValue(Event.class);
                model.setKey(dataSnapshot.getKey());

                if (eventFilter.filter(model)) {
                    int eventIndex = getEventIndex(model);
                    if (eventIndex != -1) { // бывает ли иначе? хм.
                        events.remove(eventIndex);
                        notifyItemRemoved(eventIndex);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };

        presenter.getEventsReference().addChildEventListener(childEventListener);
    }

    public void cleanup() {
        if (presenter.getEventsReference() != null)
            presenter.getEventsReference().removeEventListener(childEventListener);
        events.clear();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_event, viewGroup, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int i) {
        final Event model = events.get(i);

        holder.title.setText(model.title);
        holder.description.setText(model.description);
        holder.published_by.setText(model.published_by);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.switchToEventDetails(model);
            }
        });

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.switchToEventDetails(model);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeEvent(model);
            }
        });
    }

    void removeEvent(Event event) {
        if(event.published_by.equals(presenter.firebaseUser.getUid()))
            presenter.getEventsReference().child(event.key).removeValue();
        else
        {
            presenter.getUsersReference().child(presenter.firebaseUser.getUid())
                    .child("subscribedTo").child(event.key).removeValue();
            presenter.getEventsReference().child(event.key)
                    .child("participants").child(presenter.firebaseUser.getUid()).removeValue();
        }
    }
}
