package com.hsehhh.sv3.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Tima on 21.02.2018.
 */

public class User implements Parcelable {
    public String name;
    public String floor;
    public ArrayList<String> subscribedTo;

    public User() { }

    public User(String name, String floor) {
        this.name = name;

        subscribedTo = new ArrayList<>();
        this.floor = floor;
    }

    protected User(Parcel in) {
        name = in.readString();
        floor = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(floor);
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