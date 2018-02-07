package com.hsehhh.sv3;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Tima on 03.02.2018.
 */

@IgnoreExtraProperties
public class Message {

    public String user_id;
    public String message;

    @Exclude
    public String key;


    Message() { }

    public Message(String user_id, String message) {
        this.user_id = user_id;
        this.message = message;

    }

    public void setKey(String key)
    {
        this.key = key;
    }


}
