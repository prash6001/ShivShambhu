package com.pjadhav.myapplication.events;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by pjadhav on 7/1/17.
 */

public class GlobalBus {

    private static EventBus sBus;
    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }


}
