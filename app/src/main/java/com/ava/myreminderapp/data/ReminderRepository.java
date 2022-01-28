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

  private static final String TAG = "MyReminderApp.ReminderRepository: ";

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
            Log.i(TAG, "Added reminder: " + model);
          } catch (Exception e) {
            Log.e(TAG, "Error while adding the reminder: " + model, e);
          }
        });
  }

  public void deleteAll() {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.deleteAll();
            Log.i(TAG, "Deleted All reminders!");
          } catch (Exception e) {
            Log.e(TAG, "Exception while deleting all reminders", e);
          }
        });
  }

  public void delete(ReminderModel model) {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.delete(model);
            Log.i(TAG, "Deleted reminder: " + model.getName());
          } catch (Exception e) {
            Log.e(TAG, "Exception while deleting reminder: " + model.getName(), e);
          }
        });
  }

  public void updateStatus(ReminderModel reminder, boolean isActive) {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.updateStatus(reminder.getId(), isActive);
            Log.i(TAG, "Updated reminder: " + reminder.getName() + "'s status to: " + isActive);
          } catch (Exception e) {
            Log.e(
                TAG,
                "Exception while Updating reminder: "
                    + reminder.getName()
                    + "'s status to: "
                    + isActive);
          }
        });
  }

  public void update(ReminderModel model) {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.update(model);
            Log.i(TAG, "Updated reminder: " + model);
          } catch (Exception e) {
            Log.e(TAG, "Error while updating the reminder: " + model, e);
          }
        });
  }

  public LiveData<List<ReminderModel>> getAll() {
    return getAllObservable;
  }
}
