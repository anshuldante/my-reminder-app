package com.ava.myreminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ava.myreminderapp.service.ReminderNotificationService;

public class ReminderServiceActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent serviceIntent = new Intent(this, ReminderNotificationService.class);
    Log.i("Foreground Service: ", "Creating Foreground Service Intent");
    startService(serviceIntent);
  }
}
