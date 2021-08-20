package com.ava.myreminderapp.data;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.ava.myreminderapp.model.ReminderModel;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class ReminderRepository {

  private final ReminderDao reminderDao;
  private final ExecutorService reminderDaoExecutor;
  private final LiveData<List<ReminderModel>> getAllObservable;

  public ReminderRepository(ReminderDao reminderDao, ExecutorService reminderDaoExecutor) {
    this.reminderDao = reminderDao;
    this.reminderDaoExecutor = reminderDaoExecutor;
    this.getAllObservable = reminderDao.getAll();
  }

  public void add(ReminderModel model) {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.add(model);
            Log.i("ReminderRepository: ", "Added reminder: " + model);
          } catch (Exception e) {
            Log.e("ReminderRepository: ", "Error while adding the reminder: " + model, e);
          }
        });
  }

  public void deleteAll() {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.deleteAll();
            Log.i("Reminders: ", "Deleted All reminders!");
          } catch (Exception e) {
            Log.e("Reminders: ", "Exception while deleting all reminders", e);
          }
        });
  }

  public void delete(ReminderModel model) {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.delete(model);
            Log.i("Reminders: ", "Deleted reminder: " + model.getName());
          } catch (Exception e) {
            Log.e("Reminders: ", "Exception while deleting reminder: " + model.getName(), e);
          }
        });
  }

  public void updateStatus(ReminderModel reminder, boolean isActive) {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.updateStatus(reminder.getId(), isActive);
            Log.i(
                "Reminders: ",
                "Updated reminder: " + reminder.getName() + "'s status to: " + isActive);
          } catch (Exception e) {
            Log.e(
                "Reminders: ",
                "Exception while Updating reminder: "
                    + reminder.getName()
                    + "'s status to: "
                    + isActive);
          }
        });
  }

  public LiveData<List<ReminderModel>> getAll() {
    return getAllObservable;
  }

  public LiveData<ReminderModel> getReminder(int id) {
    return reminderDao.get(id);
  }
}
