package com.hsehhh.sv3;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by a1 on 25.01.18.
 */

@IgnoreExtraProperties
public class EventOrRequest
{
    boolean isEvent;
    String description;

    EventOrRequest()
    {

    }

    EventOrRequest(boolean isEvent, String description)
    {
        this.isEvent = isEvent;
        this.description = description;
    }
}
