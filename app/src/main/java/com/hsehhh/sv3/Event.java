package com.hsehhh.sv3;

import android.view.View;
import android.widget.GridLayout;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Event  {
    public String title;
    public String description;
    public String published_by;
    public int floor;

    @Exclude
    public String type;

    public Event() { }

    public Event(String title, String description, String type, String published_by, int floor) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.published_by = published_by;
        this.floor = floor;
    }

    public int getFloor()
    {
        return floor;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    public String getPublished_by()
    {
        return published_by;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setPublished_by(String published_by)
    {
        this.published_by = published_by;
    }

    public void setFloor(int floor)
    {
        this.floor = floor;
    }

}
