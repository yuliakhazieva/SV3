package com.hsehhh.sv3;

/**
 * Created by Tima on 04.02.2018.
 */

public interface FragmentSwitcher {
    void switchToCreateEvent();
    void switchToEventDetails(Event e);
    void switchToMyEvents();
    void switchToScrolling();
    void switchToPrevious();
}
