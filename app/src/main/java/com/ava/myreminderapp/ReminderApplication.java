package com.ava.myreminderapp;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class ReminderApplication extends Application {

  public static final String CHANNEL_ID = "MY_REMINDER_APP_CHANNEL";
  public static final String CHANNEL_NAME = "com.ava.myreminderapp";

  @Override
  public void onCreate() {
    super.onCreate();
  }
}
