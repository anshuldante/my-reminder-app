package com.ava.myreminderapp.service;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.ava.myreminderapp.ReminderApplication.CHANNEL_DESCRIPTION;
import static com.ava.myreminderapp.ReminderApplication.CHANNEL_ID;
import static com.ava.myreminderapp.ReminderApplication.CHANNEL_NAME;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_ID;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_NAME;
import static com.ava.myreminderapp.util.ReminderConstants.ACTION_DISMISS;
import static com.ava.myreminderapp.util.ReminderConstants.ACTION_SNOOZE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ava.myreminderapp.R;

import java.util.Date;

public class NotificationStarterService extends Service {

  public static final String TAG = "MyReminderApp.NotificationStarterService";

  private MediaPlayer mediaPlayer;
  private Vibrator vibrator;
  private int notificationId;
  private String notificationName;
  private NotificationManagerCompat notificationManager;
  private boolean mediaPlayerReleased = false;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "Inside onCreate");

    notificationManager = NotificationManagerCompat.from(this);
    vibrator = ((VibratorManager) getSystemService(Context.VIBRATOR_MANAGER_SERVICE)).getDefaultVibrator();

    mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
    mediaPlayer.setLooping(true);
    mediaPlayer.setOnCompletionListener(mp -> safelyStopAndReleaseMediaPlayer());
    mediaPlayer.setOnErrorListener((mp, what, extra) -> {
      Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
      safelyStopAndReleaseMediaPlayer();
      return true;
    });
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    notificationId = intent.getIntExtra(REMINDER_ID, -1);
    notificationName = intent.getStringExtra(REMINDER_NAME);
    Log.i(TAG, "Inside onStartCommand, creating a notification for ID: " + notificationId);

    Log.i(TAG, "Starting alarm at: " + new Date());

    //TODO: Because of the manually triggered flow, ID is always zero, hence hard-coding a non-zero value
    startForeground(33, buildNotification());

    if (mediaPlayer == null) {
      Log.i(TAG, "Media Player was null, reinitialising at: " + new Date());
      mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
      mediaPlayer.setLooping(true);
      mediaPlayer.setOnCompletionListener(mp -> safelyStopAndReleaseMediaPlayer());
      mediaPlayer.setOnErrorListener((mp, what, extra) -> {
        Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
        safelyStopAndReleaseMediaPlayer();
        return true;
      });
    }
    mediaPlayer.start();
//    attachAlarmAutoStopper();
    vibrateWithPattern();
    return START_STICKY;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i(TAG, "Cleaning up the notification");

    safelyStopAndReleaseMediaPlayer();
    safelyCancelVibration();
    safeCancelNotification();
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private Notification buildNotification() {
    createNotificationChannel();
    NotificationCompat.Builder builder = createNotification();
    attachSnoozeAction(builder);
    attachDismissActions(builder);

    Log.i(TAG, "Notification Built for ID: " + notificationId);

    return builder.build();
  }

  private void attachDismissActions(NotificationCompat.Builder builder) {
    Intent dismissIntent = new Intent(this, NotificationStopperService.class);
    dismissIntent.setAction(ACTION_DISMISS);
    dismissIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
    dismissIntent.putExtra(REMINDER_ID, notificationId);
    dismissIntent.putExtra(REMINDER_NAME, notificationName);
    PendingIntent dismissPendingIntent =
        PendingIntent.getService(this, 0, dismissIntent, FLAG_IMMUTABLE | FLAG_UPDATE_CURRENT);
    builder.addAction(
        R.drawable.ic_baseline_cancel_24, getString(R.string.dismiss), dismissPendingIntent);
    builder.setContentIntent(dismissPendingIntent);
  }

  private void attachSnoozeAction(NotificationCompat.Builder builder) {
    Intent snoozeIntent = new Intent(this, NotificationStopperService.class);
    snoozeIntent.setAction(ACTION_SNOOZE);
    snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
    snoozeIntent.putExtra(REMINDER_ID, notificationId);
    snoozeIntent.putExtra(REMINDER_NAME, notificationName);
    PendingIntent snoozePendingIntent =
        PendingIntent.getService(this, 0, snoozeIntent, FLAG_IMMUTABLE | FLAG_UPDATE_CURRENT);
    builder.addAction(
        R.drawable.ic_baseline_snooze_24, getString(R.string.snooze), snoozePendingIntent);
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
    NotificationChannel channel =
        new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
    channel.setDescription(CHANNEL_DESCRIPTION);
    channel.setLightColor(Color.BLUE);
    channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
    notificationManager.createNotificationChannel(channel);
  }

  private void vibrateWithPattern() {
    if (vibrator != null && vibrator.hasVibrator()) {
      long[] pattern = {0, 500, 300, 500};
      vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
    }
  }

  // To be used only when absolutely necessary
  @SuppressWarnings("unused")
  private void attachAlarmAutoStopper() {
    new Handler(Looper.getMainLooper()).postDelayed(() -> {
      if (mediaPlayer != null && mediaPlayer.isPlaying()) {
        Log.i(TAG, "Stopping alarm at: " + new Date());
        mediaPlayer.stop();
        mediaPlayer.release();
      }
    }, 10_000);
  }

  private void safelyStopAndReleaseMediaPlayer() {
    if (mediaPlayer == null || mediaPlayerReleased) {
      return;
    }
    Handler mainHandler = new Handler(Looper.getMainLooper());
    mainHandler.post(() -> {
      if (mediaPlayer == null || mediaPlayerReleased) {
        return;
      }
      try {
        Log.i(TAG, "Stopping and releasing MediaPlayer on thread: " + Thread.currentThread().getName());
        mediaPlayer.setOnCompletionListener(null);
        mediaPlayer.setOnErrorListener(null);
        if (mediaPlayer.isPlaying()) {
          mediaPlayer.stop();
        }
        mediaPlayer.release();
        mediaPlayerReleased = true;
      } catch (IllegalStateException e) {
        Log.w(TAG, "MediaPlayer was in illegal state during stop/release", e);
      } finally {
        mediaPlayer = null;
      }
    });
  }

  private void safelyCancelVibration() {
    if (vibrator != null) {
      try {
        vibrator.cancel();
      } catch (Exception e) {
        Log.w(TAG, "Vibrator cancel failed", e);
      }
    }
  }

  private void safeCancelNotification() {
    if (notificationManager != null && notificationId != -1) {
      try {
        notificationManager.cancel(notificationId);
      } catch (Exception e) {
        Log.w(TAG, "NotificationManager cancel failed", e);
      }
    }
  }
}
