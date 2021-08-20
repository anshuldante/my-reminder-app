package com.ava.myreminderapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ava.myreminderapp.model.ReminderModel;

import java.util.List;

@Dao
public interface ReminderDao {

  @Insert
  void add(ReminderModel model);

  @Query("Delete from reminders")
  void deleteAll();

  @Delete
  void delete(ReminderModel model);

  @Query("update reminders set active = :isActive where id = :id")
  void updateStatus(int id, boolean isActive);

  @Query("SELECT * FROM reminders order by name")
  LiveData<List<ReminderModel>> getAll();

  @Query("SELECT * FROM reminders where id = :id")
  LiveData<ReminderModel> get(int id);
}
