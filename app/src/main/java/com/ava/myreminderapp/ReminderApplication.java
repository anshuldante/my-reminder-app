package com.ava.myreminderapp;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class ReminderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
