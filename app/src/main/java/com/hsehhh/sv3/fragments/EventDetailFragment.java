package com.hsehhh.sv3.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hsehhh.sv3.MainActivity;
import com.hsehhh.sv3.R;
import com.hsehhh.sv3.adapters.ChatAdapter;
import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.Message;
import com.hsehhh.sv3.data.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventDetailFragment extends Fragment {
    // Data
    Event event;

    MainActivity presenter;

    ArrayList<Map<String, String>> сhildDataItemList;
    ArrayList<Map<String, String>> сhildDataDetailList;

    TextView messageTextView;
    Button sendMessageButton;

    RecyclerView chat;

    ChatAdapter chatAdapter;

    ExpandableListView participantsList;
    ExpandableListView detailList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event_detail2, container, false);

        // мне не нравится. буду разбираться с тем, как это лучше написать
        presenter.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getArguments();
        event = arguments.getParcelable("event");
        presenter.getSupportActionBar().setTitle(event.title);

        participantsList = v.findViewById(R.id.participants_list);
        detailList = v.findViewById(R.id.info_list);


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


        Map<String, String> map;
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        map = new HashMap<>();
        map.put("groupName", "Гости");
        groupDataList.add(map);
        String groupFrom[] = new String[] { "groupName" };
        int groupTo[] = new int[] { android.R.id.text1 };

        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();
        сhildDataItemList = new ArrayList<>();


        if(event.participants != null) {
            Iterator it = event.participants.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Query q = presenter.getUsersReference().child(((Map.Entry) it.next()).getValue().toString());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    Map<String, String> map;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        map = new HashMap<>();
                        map.put("guestName", dataSnapshot.getValue(User.class).name);
                        сhildDataItemList.add(map);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        сhildDataList.add(сhildDataItemList);
        String childFrom[] = new String[] { "guestName" };
        int childTo[] = new int[] { R.id.guest_name };

        SimpleExpandableListAdapter expandableListAdapter;

        if(event.published_by.equals(presenter.firebaseUser.getUid()))
        {
            expandableListAdapter = new SimpleExpandableListAdapter(
                    getContext(),
                    groupDataList,
                    R.layout.list_of_guests_group,
                    groupFrom,
                    groupTo,
                    сhildDataList, R.layout.list_of_guests_item_for_host, childFrom, childTo);
        } else
        {
            expandableListAdapter = new SimpleExpandableListAdapter(
                    getContext(),
                    groupDataList,
                    R.layout.list_of_guests_group,
                    groupFrom,
                    groupTo,
                    сhildDataList, R.layout.list_of_guests_item, childFrom, childTo);
        }


        participantsList.setAdapter(expandableListAdapter);


        groupDataList = new ArrayList<>();
        map = new HashMap<>();
        map.put("groupName", "Информация");
        groupDataList.add(map);
        String groupFrom2[] = new String[] { "groupName" };
        int groupTo2[] = new int[] { android.R.id.text1 };

        сhildDataList = new ArrayList<>();
        сhildDataDetailList = new ArrayList<>();

        Map<String, String> map4;
        map = new HashMap<>();
        map.put("item", "Хозяин: " + presenter.getNameFromId(event.published_by));
        сhildDataDetailList.add(map);

        Map<String, String> map1;
        map = new HashMap<>();
        map.put("item", event.getFormattedDate());
        сhildDataDetailList.add(map);

        Map<String, String> map3;
        map = new HashMap<>();
        map.put("item", event.room.toString());
        сhildDataDetailList.add(map);

        Map<String, String> map2;
        map = new HashMap<>();
        map.put("item", event.description);
        сhildDataDetailList.add(map);


        сhildDataList.add(сhildDataDetailList);
        String childFrom2[] = new String[] { "item" };
        int childTo2[] = new int[] { R.id.guest_name };

        SimpleExpandableListAdapter expandableListAdapterDetail;

        expandableListAdapterDetail = new SimpleExpandableListAdapter(
                getContext(),
                groupDataList,
                R.layout.list_of_guests_group,
                groupFrom2,
                groupTo2,
                сhildDataList, R.layout.list_of_guests_item, childFrom2, childTo2
        );

        detailList.setAdapter(expandableListAdapterDetail);

        return v;
    }

    private void sendMessage(String messageText) {

        presenter.getChatsReference().child(event.key).push().setValue(new Message(presenter.user.name , messageText));
        messageTextView.setText("");
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
