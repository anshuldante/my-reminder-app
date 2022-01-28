package com.ava.myreminderapp.service;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.ava.myreminderapp.ReminderApplication.CHANNEL_DESCRIPTION;
import static com.ava.myreminderapp.ReminderApplication.CHANNEL_ID;
import static com.ava.myreminderapp.ReminderApplication.CHANNEL_NAME;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_ID;
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
import androidx.core.app.NotificationManagerCompat;

import com.ava.myreminderapp.R;

public class ReminderNotificationService extends Service {
  private static final String ACTION_SNOOZE = "Snooze";
  private static final String ACTION_DISMISS = "Dismiss";

  public static final String TAG = "MyReminderApp.ReminderNotificationService";

  private MediaPlayer mediaPlayer;
  private Vibrator vibrator;
  private int notificationId;
  private String notificationName;
  private NotificationManagerCompat notificationManager;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "Inside onCreate");

    notificationManager = NotificationManagerCompat.from(this);
    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
    mediaPlayer.setLooping(true);
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    notificationId = intent.getIntExtra(REMINDER_ID, -1);
    notificationName = intent.getStringExtra(REMINDER_NAME);
    Log.i(TAG, "Inside onStartCommand, creating a notification for ID: " + notificationId);

    startForeground(notificationId, buildNotification());

    mediaPlayer.start();
    long[] pattern = {0, 100, 1000};
    vibrator.vibrate(pattern, 0);

    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i(TAG, "Trying to kill the notification");

    mediaPlayer.stop();
    vibrator.cancel();
    notificationManager.cancel(notificationId);
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private Notification buildNotification() {
    createNotificationChannel();
    NotificationCompat.Builder builder = createNotification();
    attachActivityIntent(builder);
    attachSnoozeAction(builder);
    attachDismissAction(builder);

    Log.i(TAG, "Notification Built for ID: " + notificationId);

    return builder.build();
  }

  private void attachDismissAction(NotificationCompat.Builder builder) {
    {
      Intent dismissIntent = new Intent(this, MediaStopperService.class);
      dismissIntent.setAction(ACTION_DISMISS);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        dismissIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
      }
      PendingIntent dismissPendingIntent =
          PendingIntent.getService(this, 0, dismissIntent, FLAG_IMMUTABLE | FLAG_UPDATE_CURRENT);
      builder.addAction(
          R.drawable.ic_baseline_cancel_24, getString(R.string.dismiss), dismissPendingIntent);
    }
  }

  private void attachSnoozeAction(NotificationCompat.Builder builder) {
    Intent snoozeIntent = new Intent(this, MediaStopperService.class);
    snoozeIntent.setAction(ACTION_SNOOZE);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
    }
    PendingIntent snoozePendingIntent =
        PendingIntent.getBroadcast(this, 0, snoozeIntent, FLAG_IMMUTABLE | FLAG_UPDATE_CURRENT);
    builder.addAction(
        R.drawable.ic_baseline_snooze_24, getString(R.string.snooze), snoozePendingIntent);
  }

  private void attachActivityIntent(NotificationCompat.Builder builder) {
    Intent intent = new Intent(this, MediaStopperService.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
    }
    PendingIntent pendingIntent =
        PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE | FLAG_UPDATE_CURRENT);
    builder.setContentIntent(pendingIntent);
  }

  private NotificationCompat.Builder createNotification() {
    return new NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_alarm)
        .setContentText("Ring Ring...Ring Ring")
        .setContentTitle(notificationName)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setCategory(NotificationCompat.CATEGORY_ALARM)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setTicker(getText(R.string.ticker_text))
        .setAutoCancel(true);
  }

  private void createNotificationChannel() {
    Log.i(TAG, "Creating Channel if needed");

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel =
          new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
      channel.setDescription(CHANNEL_DESCRIPTION);
      channel.setLightColor(Color.BLUE);
      channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
      notificationManager.createNotificationChannel(channel);
    }
  }
}
