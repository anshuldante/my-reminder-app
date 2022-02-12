package com.ava.myreminderapp.service;

import com.ava.myreminderapp.data.InstanceRepository;

/**
 * Finds all reminders to be triggered in the next x hours or the next reminder to be triggered and
 * schedules them.
 *
 * <p>Needs to be called whenever there's a Create, Update or Delete action performed on a reminder.
 * Needs to be called whenever a reminder is triggered. Needs to be called whenever the phone boots
 * up.
 */
public class ReminderManager {
  private final InstanceRepository repository;

  public ReminderManager(InstanceRepository repository) {
    this.repository = repository;
  }

  public void deleteById(int id) {
    repository.deleteById(id);
  }

  public void deleteByReminderId(int reminderId) {
    repository.deleteByReminderId(reminderId);
  }

  public void deleteAll(int reminderId) {
    repository.deleteAll();
  }

  public void deleteExpiredInstances() {
    repository.deleteExpired(System.currentTimeMillis());
  }

  public void scheduleUpcomingReminders() {
    deleteExpiredInstances(this::scheduleUpcomingInstances);
  }

  // TODO: Implement scheduling upcoming reminder instances
  private void scheduleUpcomingInstances() {}

  private void deleteExpiredInstances(Runnable r) {
    repository.deleteExpired(System.currentTimeMillis(), r);
  }
}
