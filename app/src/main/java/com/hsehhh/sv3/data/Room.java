package com.hsehhh.sv3.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tima on 22.02.2018.
 */

public class Room implements Parcelable {
    public String section;
    public int floor;
    public int aptNumber;
    public Room() {}
    public Room(String section, int floor, int aptNumber)
    {
        this.section = section;
        this.floor = floor;
        this.aptNumber = aptNumber;
    }

    protected Room(Parcel in) {
        section = in.readString();
        floor = in.readInt();
        aptNumber = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(section);
        dest.writeInt(floor);
        dest.writeInt(aptNumber);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    @Override
    public String toString() {
        return section + (floor < 10 ? "0" : "") + floor + aptNumber;
    }
}