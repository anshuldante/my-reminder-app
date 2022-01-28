package com.ava.myreminderapp;

import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_ID;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ava.myreminderapp.service.ReminderNotificationService;

public class ReminderServiceActivity extends AppCompatActivity {
  public static final String TAG = "MyReminderApp.ReminderServiceActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent serviceIntent = new Intent(this, ReminderNotificationService.class);
    Log.i(TAG, "Creating Foreground Service Intent");

    serviceIntent.putExtra(REMINDER_ID, getIntent().getIntExtra(REMINDER_ID, -1));
    serviceIntent.putExtra(REMINDER_NAME, getIntent().getStringExtra(REMINDER_NAME));

    startService(serviceIntent);
  }
}
