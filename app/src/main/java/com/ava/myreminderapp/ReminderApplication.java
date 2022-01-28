package com.ava.myreminderapp;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class ReminderApplication extends Application {

  public static final String CHANNEL_ID = "MY_REMINDER_APP_CHANNEL";
  public static final String CHANNEL_NAME = "com.ava.myreminderapp";
  public static final String CHANNEL_DESCRIPTION =
      "This channel is used by MyReminderApp for displaying Alarms";
  public static final int REQUEST_CODE = 10051989;

  @Override
  public void onCreate() {
    super.onCreate();
  }
}
