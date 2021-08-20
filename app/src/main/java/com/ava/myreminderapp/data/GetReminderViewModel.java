package com.ava.myreminderapp.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ava.myreminderapp.model.ReminderModel;

public class GetReminderViewModel extends ViewModel {

  private final ReminderRepository reminderRepository;

  public GetReminderViewModel(ReminderRepository reminderRepository) {
    this.reminderRepository = reminderRepository;
  }

  public LiveData<ReminderModel> getReminder(int id) {
    return reminderRepository.getReminder(id);
  }
}
