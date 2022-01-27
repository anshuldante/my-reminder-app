package com.ava.myreminderapp.service;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.ava.myreminderapp.ReminderApplication.CHANNEL_ID;
import static com.ava.myreminderapp.ReminderApplication.CHANNEL_NAME;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_NAME;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.ava.myreminderapp.MainActivity;
import com.ava.myreminderapp.R;

public class ReminderNotificationService extends Service {
  private MediaPlayer mediaPlayer;
  private Vibrator vibrator;

  @Override
  public void onCreate() {
    super.onCreate();

    Log.i("ReminderNotificationService", "Inside onCreate");

    mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
    mediaPlayer.setLooping(true);

    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {

    Log.i("ReminderNotificationService", "Inside onStart");

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel =
          new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE);
      channel.setLightColor(Color.BLUE);
      channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
      NotificationManager manager =
          (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
      assert manager != null;
      manager.createNotificationChannel(channel);
    }

    Intent notificationIntent = new Intent(this, MainActivity.class);

    PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0, notificationIntent, FLAG_UPDATE_CURRENT);

    String title = String.format("%s Reminder", intent.getStringExtra(REMINDER_NAME));

    Notification notification =
        new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText("Ring Ring .. Ring Ring")
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.ticker_text))
            .build();

    mediaPlayer.start();

    long[] pattern = {0, 100, 1000};
    vibrator.vibrate(pattern, 0);

    Log.i("ReminderNotificationService", "Starting notification");

    startForeground(1, notification);

    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    Log.i("ReminderNotificationService", "Destroying notification");

    mediaPlayer.stop();
    vibrator.cancel();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
