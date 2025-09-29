package com.ava.notiva.util;

import android.content.Context;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.ava.notiva.service.ReminderTriggerWorker;

public class ReminderWorkerUtils {
  public static void enqueueReminderWorker(Context context) {
    WorkManager.getInstance(context).enqueue(
        new OneTimeWorkRequest.Builder(ReminderTriggerWorker.class).build()
    );
  }
}

