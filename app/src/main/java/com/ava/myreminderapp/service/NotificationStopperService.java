package com.ava.myreminderapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationStopperService extends Service {

  public static final String TAG = "MyReminderApp.NotificationStopperService";

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    Log.i(TAG, "NotificationStopperService starting up");

    Log.i(TAG, "NotificationStopperService trying to kill the notification");
    Intent intentService = new Intent(getApplicationContext(), NotificationStarterService.class);
    getApplicationContext().stopService(intentService);
    return super.onStartCommand(intent, flags, startId);
  }
}
