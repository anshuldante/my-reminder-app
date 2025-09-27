package com.ava.notiva.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ava.notiva.model.ReminderModel;

@Database(
    entities = {ReminderModel.class},
    version = 1,
    exportSchema = false)
public abstract class RemindersDb extends RoomDatabase {
  public abstract ReminderDao reminderDao();
}
