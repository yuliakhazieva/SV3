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

    TextView eventTitleTextView;
    TextView eventDescriptionTextView;
    TextView eventFloorTextView;
    TextView eventUserIdTextView;
    ArrayList<Map<String, String>> сhildDataItemList;

    TextView messageTextView;
    Button sendMessageButton;

    HashMap<String, String> m;

    RecyclerView chat;

    ChatAdapter chatAdapter;

    ExpandableListView participantsList;

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
        participantsList = v.findViewById(R.id.participants_list);

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


        Map<String, String> map;
        ArrayList<Map<String, String>> groupDataList = new ArrayList<>();
        map = new HashMap<>();
        map.put("groupName", "Гости");
        groupDataList.add(map);
        String groupFrom[] = new String[] { "groupName" };
        int groupTo[] = new int[] { android.R.id.text1 };

        ArrayList<ArrayList<Map<String, String>>> сhildDataList = new ArrayList<>();
        сhildDataItemList = new ArrayList<>();


        Query q = presenter.getUsersReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

//        Iterator it = event.participants.entrySet().iterator();
//        int i = 0;
//        while (it.hasNext()) {
//            Map.Entry pair = (Map.Entry)it.next();
//            map = new HashMap<>();
//            map.put("guestIndex", Integer.toString(i));
//            map.put("guestName", pair.getValue().toString());
//            сhildDataItemList.add(map);
//            i++;
//        }
        сhildDataList.add(сhildDataItemList);
        String childFrom[] = new String[] { "guestName" };
        int childTo[] = new int[] { R.id.guest_name };

        SimpleExpandableListAdapter expandableListAdapter;

        if(event.published_by.equals(presenter.firebaseUser.getUid()))
        {
            expandableListAdapter = new SimpleExpandableListAdapter(
                    getContext(),
                    groupDataList,
                    android.R.layout.simple_expandable_list_item_1,
                    groupFrom,
                    groupTo,
                    сhildDataList, R.layout.list_of_guests_item_for_host, childFrom, childTo);
        } else
        {
            expandableListAdapter = new SimpleExpandableListAdapter(
                    getContext(),
                    groupDataList,
                    android.R.layout.simple_expandable_list_item_1,
                    groupFrom,
                    groupTo,
                    сhildDataList, R.layout.list_of_guests_item, childFrom, childTo);
        }


        participantsList.setAdapter(expandableListAdapter);

        return v;
    }

    private void sendMessage(String messageText) {
        presenter.getChatsReference().child(event.key).push().setValue(new Message( presenter.firebaseUser.getUid(), messageText));
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



//    class CustomExpandableListAdapter extends BaseExpandableListAdapter {
//
//        private Context context;
//        private List<String> expandableListTitle;
//        private HashMap<String, List<String>> expandableListDetail;
//
//        public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
//                                           HashMap<String, List<String>> expandableListDetail) {
//            this.context = context;
//            this.expandableListTitle = expandableListTitle;
//            this.expandableListDetail = expandableListDetail;
//        }
//
//        @Override
//        public Object getChild(int listPosition, int expandedListPosition) {
//            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
//                    .get(expandedListPosition);
//        }
//
//        @Override
//        public long getChildId(int listPosition, int expandedListPosition) {
//            return expandedListPosition;
//        }
//
//        @Override
//        public View getChildView(int listPosition, final int expandedListPosition,
//                                 boolean isLastChild, View convertView, ViewGroup parent) {
//
//
//            final String expandedListText = (String) getChild(listPosition, expandedListPosition);
//            if (convertView == null) {
//                LayoutInflater layoutInflater = (LayoutInflater) this.context
//                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                if (parent.getCh)
//                    convertView = layoutInflater.inflate(R.layout.list_of_guests_item, null);
//            }
//            TextView expandedListTextView = (TextView) convertView
//                    .findViewById(R.id.expandedListItem);
//            expandedListTextView.setText(expandedListText);
//            return convertView;
//        }
//
//
//        @Override
//        public View getGroupView(int listPosition, boolean isExpanded,
//                                 View convertView, ViewGroup parent) {
//            String listTitle = (String) getGroup(listPosition);
//            if (convertView == null) {
//                LayoutInflater layoutInflater = (LayoutInflater) this.context.
//                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                convertView = layoutInflater.inflate(R.layout.list_of_guests_group, null);
//            }
//            TextView listTitleTextView = (TextView) convertView
//                    .findViewById(R.id.guests);
//            listTitleTextView.setText(listTitle);
//            return convertView;
//        }
//
//        @Override
//        public int getChildrenCount(int listPosition) {
//            return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
//                    .size();
//        }
//
//        @Override
//        public Object getGroup(int listPosition) {
//            return this.expandableListTitle.get(listPosition);
//        }
//
//        @Override
//        public int getGroupCount() {
//            return this.expandableListTitle.size();
//        }
//
//        @Override
//        public long getGroupId(int listPosition) {
//            return listPosition;
//        }
//
//        @Override
//        public boolean hasStableIds() {
//            return false;
//        }
//
//        @Override
//        public boolean isChildSelectable(int listPosition, int expandedListPosition) {
//            return true;
//        }
//    }

}
