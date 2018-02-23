package com.hsehhh.sv3.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;

/**
 * Created by Tima on 21.02.2018.
 */

public class User implements Parcelable {
    public String name;
    public HashMap<String, String> subscribedTo;
    public String ID;
    public Room room;

    public User() { }

    public User(String name, Room room) {
        this.name = name;
        this.room = room;

        subscribedTo = new HashMap<>();
    }

    protected User(Parcel in) {
        name = in.readString();
        room = in.readTypedObject(Room.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedObject(room, 0);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}