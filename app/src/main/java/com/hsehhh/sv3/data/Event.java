package com.hsehhh.sv3.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@IgnoreExtraProperties
public class Event implements Parcelable {

    // инкапуслировать всё вщщи
    public String title;
    public String description;
    public String published_by;
    public int floor;
    public int aptNumber;
    public String date;
    public String time;
    public HashMap<String, String> participants;

    @Exclude
    public String type;

   // @Exclude
    public String key;

    Event() { }

    public Event(String title, String description, String type, String published_by, int floor, int aptNumber, String date, String time) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.published_by = published_by;
        this.floor = floor;
        this.time = time;
        this.date = date;
        this.aptNumber = aptNumber;
        this.participants = new HashMap<String, String>();

    }


    protected Event(Parcel in) {
        title = in.readString();
        description = in.readString();
        published_by = in.readString();
        floor = in.readInt();
        aptNumber = in.readInt();
        type = in.readString();
        key = in.readString();
        time = in.readString();
        date = in.readString();
        participants = in.readHashMap(String.class.getClassLoader());
      //  in.readHashMap(participants);
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel in, int flags) {
        in.writeString(title);
        in.writeString(description);
        in.writeString(published_by);
        in.writeInt(floor);
        in.writeInt(aptNumber);
        in.writeString(type);
        in.writeString(key);
        in.writeString(time);
        in.writeString(date);
        in.writeMap(participants);
      //  in.writeMap(participants);
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
