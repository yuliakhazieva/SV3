package com.hsehhh.sv3;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by a1 on 25.01.18.
 */

@IgnoreExtraProperties
class EventOrRequest
{
    int floor;

    EventOrRequest()
    {

    }
    EventOrRequest(int floor)
    {
        this.floor = floor;
    }

    public int getFloor()
    {
        return floor;
    }

}
