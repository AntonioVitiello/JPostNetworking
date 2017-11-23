package com.example.jpost.networking;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiHandler.init();
    }
}