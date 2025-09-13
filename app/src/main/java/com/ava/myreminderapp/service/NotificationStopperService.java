package com.ava.myreminderapp.service;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_ID;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_NAME;
import static com.ava.myreminderapp.util.ReminderConstants.ACTION_SNOOZE;
import static com.ava.myreminderapp.util.ReminderConstants.DEFAULT_SNOOZE_TIME_10_MINUTES;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class NotificationStopperService extends Service {

  public static final String TAG = "MyReminderApp.NotificationStopperService";

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
      int reminderId = intent.getIntExtra(REMINDER_ID, -1);
      String reminderName = intent.getStringExtra(REMINDER_NAME);

      long snoozeMillis = System.currentTimeMillis() + DEFAULT_SNOOZE_TIME_10_MINUTES;
      Intent alarmIntent = new Intent(getApplicationContext(), NotificationStarterService.class);
      alarmIntent.putExtra(REMINDER_ID, reminderId);
      alarmIntent.putExtra(REMINDER_NAME, reminderName);
      PendingIntent pendingIntent = PendingIntent.getService(
          getApplicationContext(), reminderId, alarmIntent, FLAG_IMMUTABLE);
      AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
      if (alarmManager != null) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, snoozeMillis, pendingIntent);
      }
      Toast.makeText(getApplicationContext(), "Reminder snoozed for 10 minutes", Toast.LENGTH_SHORT).show();
    }
    return super.onStartCommand(intent, flags, startId);
  }
}
