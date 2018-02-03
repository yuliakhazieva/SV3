package com.hsehhh.sv3;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.GridLayout;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event implements Parcelable {

    public String title;
    public String description;
    public String published_by;
    public int floor;

    @Exclude
    public String type;

    Event() { }

    public Event(String title, String description, String type, String published_by, int floor) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.published_by = published_by;
        this.floor = floor;
    }


    protected Event(Parcel in) {
        title = in.readString();
        description = in.readString();
        published_by = in.readString();
        floor = in.readInt();
        type = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel in, int flags) {
        in.writeString(title);
        in.writeString(description);
        in.writeString(published_by);
        in.writeInt(floor);
        in.writeString(type);
    }

    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    // TODO: Написать нормальное отношение эквивалентности
    @Override
    public boolean equals(Object obj) {
        Event tmp;
        if (obj != null && obj instanceof Event)
            tmp = (Event) obj;
        else
            return false;
        return tmp.title.equals(title) && tmp.published_by.equals(published_by);
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
