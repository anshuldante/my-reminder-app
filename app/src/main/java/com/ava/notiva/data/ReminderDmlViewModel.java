package com.ava.notiva.data;

import androidx.lifecycle.ViewModel;

import com.ava.notiva.model.ReminderModel;

import java.util.function.Consumer;

public class ReminderDmlViewModel extends ViewModel {

  private final ReminderRepository reminderRepository;

  public ReminderDmlViewModel(ReminderRepository reminderRepository) {
    this.reminderRepository = reminderRepository;
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

  public void addReminderWithCallback(ReminderModel model, Consumer<Long> callback) {
    reminderRepository.addWithCallback(model, callback);
  }
}
