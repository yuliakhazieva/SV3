package com.hsehhh.sv3.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.User;
import com.hsehhh.sv3.interfaces.EventFilter;
import com.hsehhh.sv3.viewholders.EventViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
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
            if (events.get(i).key.equals(e.key))
                return i;
        return -1;
    }

    public void initializeReference(final EventFilter eventFilter){
        if (eventFilter == null)
            return;

        cleanup();

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
                   } else
                    {
                        events.add(model);
                        notifyItemInserted(events.size() - 1);
                    }
                } else
                {
                    if((getEventIndex(model)) != -1) {
                        events.remove(getEventIndex(model));
                        notifyDataSetChanged();
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
        notifyDataSetChanged();
    }

    public void cleanup() {
        if (presenter.getEventsReference() != null && childEventListener != null)
            presenter.getEventsReference().removeEventListener(childEventListener);
        events.clear();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_event, viewGroup, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder holder, int i) {
        final Event model = events.get(i);

        holder.title.setText(model.title);
        holder.description.setText(model.description);
        holder.room.setText(model.room.toString());

        holder.date.setText(model.getFormattedDate());

        Query q = presenter.getUsersReference().child(model.published_by);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.published_by.setText(dataSnapshot.getValue(User.class).name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
//
    void removeEvent(final Event event) {
        if(event.published_by.equals(presenter.firebaseUser.getUid())) {
            // destroy event here
            presenter.getEventsReference().child(event.key).removeValue();

        } else {
            // unsubscribe from event here

            presenter.getUsersReference().child(presenter.user.ID).child("subscribedTo").orderByValue().equalTo(event.key).addListenerForSingleValueEvent(new ValueEventListener()
            {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   for (DataSnapshot snap : dataSnapshot.getChildren()) {
                       snap.getRef().removeValue();
                   }
               }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

            presenter.getEventsReference().child(event.key).child("participants").orderByValue().equalTo(presenter.user.ID).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        snap.getRef().removeValue();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }
    }
}
