package com.hsehhh.sv3.utils;

import com.hsehhh.sv3.data.Event;
import com.hsehhh.sv3.data.User;
import com.hsehhh.sv3.interfaces.EventFilter;

/**
 * Created by Tima on 26.02.2018.
 */

public class VisitedEventsFilter implements EventFilter{

    User user;

    public VisitedEventsFilter(User user) {
        this.user = user;
    }

    @Override
    public boolean filter(Event e) {
        return e.getParticipants().containsValue(user.ID);
    }
}
