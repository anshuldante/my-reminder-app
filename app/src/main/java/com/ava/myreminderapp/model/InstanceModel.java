package com.ava.myreminderapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.ava.myreminderapp.converter.DbTypeConverters;

import java.util.Calendar;
import java.util.Objects;

@Entity(tableName = "reminder_instances")
@TypeConverters(DbTypeConverters.class)
public class InstanceModel {

  @PrimaryKey(autoGenerate = true)
  private int id;

  @ColumnInfo(name = "reminder_id")
  private int reminderId;

  private String name;

  private Calendar timestamp;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getReminderId() {
    return reminderId;
  }

  public void setReminderId(int reminderId) {
    this.reminderId = reminderId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Calendar getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Calendar timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    InstanceModel that = (InstanceModel) o;
    return id == that.id && reminderId == that.reminderId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, reminderId);
  }

  @NonNull
  @Override
  public String toString() {
    return "InstanceModel{"
        + "id="
        + id
        + ", reminderId="
        + reminderId
        + ", name='"
        + name
        + '\''
        + ", timestamp="
        + timestamp
        + '}';
  }
}
