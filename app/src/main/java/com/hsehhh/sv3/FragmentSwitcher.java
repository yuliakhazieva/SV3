package com.hsehhh.sv3;

import com.hsehhh.sv3.data.Event;

/**
 * Created by Tima on 04.02.2018.
 */

public interface FragmentSwitcher {
    void switchToCreateEvent();
    void switchToEventDetails(Event e);
    void switchToMyEvents();
    void switchToScrolling();
    void switchToPrevious();
<<<<<<< HEAD
    void switchToProfile();
=======
    void addDetail(Event e);
    void removeDetail();
>>>>>>> e56fa80ef3bc3d18aeff7fcba8de785bed4b4a82
}
