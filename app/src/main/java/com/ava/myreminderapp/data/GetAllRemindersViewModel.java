package com.ava.myreminderapp.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ava.myreminderapp.model.ReminderModel;

import java.util.List;

public class GetAllRemindersViewModel extends ViewModel {

  private final LiveData<List<ReminderModel>> getAllReminders;

  public GetAllRemindersViewModel(ReminderRepository reminderRepository) {
    getAllReminders = reminderRepository.getAll();
  }

  public LiveData<List<ReminderModel>> getAllReminders() {
    return getAllReminders;
  }
}
