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
    public GridLayout.LayoutParams layoutParams;
    @Exclude
    public boolean isRequest;

    public Event() { }

    public Event(String title, String description, String published_by, boolean isRequest) {
        this.title = title;
        this.description = description;
        this.published_by = published_by;
        this.isRequest = isRequest;
    }

}
