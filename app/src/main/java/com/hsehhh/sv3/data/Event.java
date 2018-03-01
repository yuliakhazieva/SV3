package com.hsehhh.sv3.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;


@IgnoreExtraProperties
public class Event implements Parcelable {

    // инкапуслировать всё вщщи
    public String type;
    public String title;
    public String description;
    public String published_by;

    public Room room;


    public String date;
    public HashMap<String, String> participants;

    @Exclude
    public String key;

    Event() { }

    public Event(String title, String description, String type, String published_by, Room room, String date) {
        this.title = title;
        this.description = description;
        this.room = room;
        this.date = date;
        this.published_by = published_by;
        this.participants = new HashMap<>();

    }

    protected Event(Parcel in) {
        key = in.readString();
        type = in.readString();
        title = in.readString();
        description = in.readString();
        published_by = in.readString();
        room = in.readTypedObject(Room.CREATOR);
        date = in.readString();
        participants = in.readHashMap(String.class.getClassLoader());
      //  in.readHashMap(participants);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel in, int flags) {
        in.writeString(key);
        in.writeString(type);
        in.writeString(title);
        in.writeString(description);
        in.writeString(published_by);
        in.writeTypedObject(room, 0);
        in.writeString(date);
        in.writeMap(participants);
    }

    public String getTitle()
    {
        return title;
    }

    public void setParticipants(HashMap<String, String> p)
    {
        participants = p;
    }

    public HashMap<String, String> getParticipants()
    {
        return participants;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    // TODO: Написать нормальное отношение эквивалентности
    @Override
    public boolean equals(Object obj) {
        Event tmp;
        if (obj != null && obj instanceof Event)
            tmp = (Event) obj;
        else
            return false;
        return tmp.key.equals(key);
    }

    //    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
