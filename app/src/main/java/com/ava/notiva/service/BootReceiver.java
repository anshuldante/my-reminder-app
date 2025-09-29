package com.ava.notiva.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;


public class BootReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
      Log.i("BootReceiver", "Device booted, scheduling periodic reminders.");
      PeriodicWorkRequest periodicWorkRequest =
          new PeriodicWorkRequest.Builder(ReminderTriggerWorker.class, 1, TimeUnit.MINUTES)
              .build();
      WorkManager.getInstance(context).enqueueUniquePeriodicWork(
          "ReminderSync",
          ExistingPeriodicWorkPolicy.KEEP,
          periodicWorkRequest
      );
    }
  }
}
