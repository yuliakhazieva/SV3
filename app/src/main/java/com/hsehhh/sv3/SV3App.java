package com.hsehhh.sv3;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by Tima on 27.01.2018.
 */

public class SV3App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}

