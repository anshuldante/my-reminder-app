package com.ava.myreminderapp.listener;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.ava.myreminderapp.model.ReminderModel;


public class RecurrenceDelayChangedListener implements TextWatcher {
    private static final String NUMBER_REGEX = "\\d+";

    private final ReminderModel reminder;

    public RecurrenceDelayChangedListener(ReminderModel reminder) {
        this.reminder = reminder;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        Integer recurrenceDelay = null;
        if (s != null && s.toString().trim().matches(NUMBER_REGEX)) {
            Log.i("Edit Text to Recurrence Delay: ", s.toString());
            recurrenceDelay = Integer.parseInt(s.toString());
        }
        reminder.setRecurrenceDelay(recurrenceDelay);
    }
}
