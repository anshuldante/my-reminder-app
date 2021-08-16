package com.ava.myreminderapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ava.myreminderapp.model.ReminderModel;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface ReminderDao {

  @Insert
  void add(ReminderModel user);

  @Query("Delete from reminders")
  void deleteAll();

  @Query("Delete from reminders where id = :id")
  void deleteById(int id);

  @Query("update reminders set active = :isActive where id = :id")
  void updateStatus(int id, boolean isActive);

  @Query("SELECT * FROM reminders")
  Observable<List<ReminderModel>> getAllReminders();
}
