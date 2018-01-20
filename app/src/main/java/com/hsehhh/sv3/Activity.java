package com.hsehhh.sv3;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Tima on 20.01.2018.
 */

@IgnoreExtraProperties
public abstract class Activity {
    public String title;
    public String description;
    public String published_by;

    public Activity() { }

    public Activity(String title, String description, String published_by) {
        this.title = title;
        this.description = description;
        this.published_by = published_by;
    }

}
