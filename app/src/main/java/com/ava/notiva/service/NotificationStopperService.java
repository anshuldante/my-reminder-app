package com.ava.notiva.service;

import static com.ava.notiva.util.ReminderConstants.ACTION_SNOOZE;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ava.notiva.util.ReminderWorkerUtils;

public class NotificationStopperService extends Service {

  public static final String TAG = "Notiva.NotificationStopperService";

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    Log.i(TAG, "NotificationStopperService starting up");
    String action = intent != null ? intent.getAction() : null;
    Log.i(TAG, "Action received: " + action);
    Intent intentService = new Intent(getApplicationContext(), NotificationStarterService.class);
    getApplicationContext().stopService(intentService);

    if (ACTION_SNOOZE.equals(action)) {
      ReminderWorkerUtils.enqueueReminderWorker(getApplicationContext());
      android.widget.Toast.makeText(getApplicationContext(), "Reminder snoozed for 10 minutes", android.widget.Toast.LENGTH_SHORT).show();
    }
    return super.onStartCommand(intent, flags, startId);
  }
}
