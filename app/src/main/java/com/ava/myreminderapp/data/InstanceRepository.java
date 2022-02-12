package com.ava.myreminderapp.data;

import android.util.Log;

import com.ava.myreminderapp.model.InstanceModel;

import java.util.List;
import java.util.concurrent.ExecutorService;

import io.reactivex.Maybe;

public class InstanceRepository {

  private final InstanceDao dao;
  private final ExecutorService executorService;

  private static final String TAG = "MyReminderApp.InstanceRepository: ";

  public InstanceRepository(InstanceDao dao, ExecutorService executorService) {
    this.dao = dao;
    this.executorService = executorService;
  }

  public void add(InstanceModel model) {
    executorService.submit(
        () -> {
          try {
            dao.add(model);
            Log.i(TAG, "Added reminder instance: " + model);
          } catch (Exception e) {
            Log.e(TAG, "Error while adding the reminder instance: " + model, e);
          }
        });
  }

  public void deleteAll() {
    executorService.submit(
        () -> {
          try {
            dao.deleteAll();
            Log.i(TAG, "Deleted All reminder instances!");
          } catch (Exception e) {
            Log.e(TAG, "Exception while deleting all reminder instances", e);
          }
        });
  }

  public void deleteById(int id) {
    executorService.submit(
        () -> {
          try {
            dao.deleteById(id);
            Log.i(TAG, "Deleted reminder instance: " + id);
          } catch (Exception e) {
            Log.e(TAG, "Exception while deleting reminder instance: " + id, e);
          }
        });
  }

  public void deleteExpired(long currentTime) {
    deleteExpired(currentTime, () -> {});
  }

  public void deleteExpired(long currentTime, Runnable r) {
    executorService.submit(
        () -> {
          try {
            dao.deleteExpired(currentTime);
            Log.i(TAG, "Deleted expired instances before: " + currentTime);
            executorService.submit(r);
          } catch (Exception e) {
            Log.e(TAG, "Exception while deleting expired instances before: " + currentTime, e);
          }
        });
  }

  public void deleteByReminderId(int id) {
    executorService.submit(
        () -> {
          try {
            dao.deleteByReminderId(id);
            Log.i(TAG, "Deleted reminder instances for reminderId: " + id);
          } catch (Exception e) {
            Log.e(TAG, "Exception while deleting reminder instances for reminderId: " + id, e);
          }
        });
  }

  public Maybe<List<InstanceModel>> getUpcomingReminderInstances(long currentTime) {
    return dao.getUpcomingReminderInstances(currentTime);
  }
}
