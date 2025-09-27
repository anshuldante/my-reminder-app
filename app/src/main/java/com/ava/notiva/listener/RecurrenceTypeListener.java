package com.ava.notiva.listener;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.ava.notiva.model.RecurrenceType;
import com.ava.notiva.model.ReminderModel;

public class RecurrenceTypeListener implements AdapterView.OnItemSelectedListener {
  public static final String TAG = "Notiva.RecurrenceTypeListener";

  private final ReminderModel reminder;
  private final Runnable summaryUpdater;

  public RecurrenceTypeListener(ReminderModel reminder, Runnable summaryUpdater) {
    this.reminder = reminder;
    this.summaryUpdater = summaryUpdater;
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    Log.i(
        TAG,
        "Recurrence Type selected: "
            + "position: "
            + position
            + " value: "
            + parent.getItemAtPosition(position).toString());
    reminder.setRecurrenceType(
        RecurrenceType.getRecurrenceTypeByValue((String) parent.getItemAtPosition(position)));
    summaryUpdater.run();
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
  }
}
