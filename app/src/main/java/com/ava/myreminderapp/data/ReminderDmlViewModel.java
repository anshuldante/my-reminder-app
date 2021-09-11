package com.ava.myreminderapp.data;

import androidx.lifecycle.ViewModel;

import com.ava.myreminderapp.model.ReminderModel;

public class ReminderDmlViewModel extends ViewModel {

  private final ReminderRepository reminderRepository;

  public ReminderDmlViewModel(ReminderRepository reminderRepository) {
    this.reminderRepository = reminderRepository;
  }

  public void addReminder(ReminderModel model) {
    reminderRepository.add(model);
  }

  public void updateReminder(ReminderModel model) {
    reminderRepository.update(model);
  }

  public void updateReminderStatus(ReminderModel model, boolean isActive) {
    reminderRepository.updateStatus(model, isActive);
  }

  public void deleteReminder(ReminderModel model) {
    reminderRepository.delete(model);
  }

  public void deleteAllReminders() {
    reminderRepository.deleteAll();
  }
}
