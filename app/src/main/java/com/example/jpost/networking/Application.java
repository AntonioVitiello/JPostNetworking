package com.example.jpost.networking;


import com.util.Log;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiHandler.init();
        Log.init(this);
    }

}