package com.ava.myreminderapp.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.ava.myreminderapp.model.ReminderModel;

public class ReminderNameChangedListener implements TextWatcher {
  private final ReminderModel reminder;

  public ReminderNameChangedListener(ReminderModel reminder) {
    this.reminder = reminder;
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    String name = null;
    if (s != null) {
      Log.i("Edit Text to name: ", s.toString());
      name = s.toString().trim();
    }
    reminder.setName(name);
  }
}
