package com.ava.myreminderapp.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ava.myreminderapp.model.InstanceModel;
import com.ava.myreminderapp.model.ReminderModel;

@Database(
    entities = {ReminderModel.class, InstanceModel.class},
    version = 1,
    exportSchema = false)
public abstract class RemindersDb extends RoomDatabase {

  public abstract ReminderDao reminderDao();

  public abstract InstanceDao reminderInstanceDao();
}
