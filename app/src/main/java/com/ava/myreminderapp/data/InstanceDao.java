package com.ava.myreminderapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ava.myreminderapp.model.InstanceModel;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface InstanceDao {
  @Insert
  void add(InstanceModel model);

  @Query("SELECT * FROM reminder_instances where timestamp < :currentTime")
  Maybe<List<InstanceModel>> getUpcomingReminderInstances(long currentTime);

  @Query("Delete from reminder_instances")
  void deleteAll();

  @Query("Delete from reminder_instances where id = :id")
  void deleteById(int id);

  @Query("Delete from reminder_instances where timestamp < :currentTime")
  void deleteExpired(long currentTime);

  @Query("Delete from reminder_instances where reminder_id = :reminderId")
  void deleteByReminderId(int reminderId);
}
