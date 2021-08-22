package com.ava.myreminderapp.listener;

import android.text.Editable;
import android.text.TextWatcher;

import com.ava.myreminderapp.model.ReminderModel;

public class RecurrenceDelayChangedListener implements TextWatcher {

  private final ReminderModel reminder;

  public RecurrenceDelayChangedListener(ReminderModel reminder) {
    this.reminder = reminder;
  }

  @Override
  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

  @Override
  public void onTextChanged(CharSequence s, int start, int before, int count) {}

  @Override
  public void afterTextChanged(Editable s) {
    reminder.setRecurrenceDelay(Integer.parseInt(s.toString()));
  }
}
