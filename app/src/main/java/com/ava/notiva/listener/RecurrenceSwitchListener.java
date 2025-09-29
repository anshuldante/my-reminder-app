package com.ava.notiva.listener;

import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;


// TODO: Use this instead of the logic in UpsertReminderActivity
public class RecurrenceSwitchListener implements SwitchCompat.OnCheckedChangeListener {
  private final ConstraintLayout recurrenceDetailsCl;
  private final Runnable resetRecurrenceDelayView;
  private final Runnable summaryUpdater;

  public RecurrenceSwitchListener(
      ConstraintLayout recurrenceDetailsCl,
      Runnable resetRecurrenceDelayView,
      Runnable summaryUpdater) {
    this.recurrenceDetailsCl = recurrenceDetailsCl;
    this.resetRecurrenceDelayView = resetRecurrenceDelayView;
    this.summaryUpdater = summaryUpdater;
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    recurrenceDetailsCl.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    if (!isChecked) {
      resetRecurrenceDelayView.run();
    }
    summaryUpdater.run();
  }
}