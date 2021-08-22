package com.ava.myreminderapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.ava.myreminderapp.converter.DbTypeConverters;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = "reminders")
@TypeConverters(DbTypeConverters.class)
public class ReminderModel {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private boolean active;

  private String name;

  @ColumnInfo(name = "start_date")
  private Calendar startDateTime;

  @ColumnInfo(name = "recurrence_delay")
  private int recurrenceDelay;

  @ColumnInfo(name = "recurrence_type")
  private RecurrenceType recurrenceType;

  @ColumnInfo(name = "end_date")
  private Calendar endDateTime;

  @ColumnInfo(name = "created_date")
  private Date createdDate;

  @ColumnInfo(name = "modified_date")
  private Date modifiedDate;

  public ReminderModel() {
    this.active = true;
    this.createdDate = new Date();
    this.modifiedDate = new Date();
    this.recurrenceType = RecurrenceType.DAY;
    this.endDateTime = Calendar.getInstance();
    this.startDateTime = Calendar.getInstance();
  }

  @Ignore
  public ReminderModel(String name) {
    this();
    this.name = name;
  }

  @Ignore
  public ReminderModel(
      int id,
      String name,
      boolean active,
      long startTime,
      Integer recurrenceDelay,
      String recurrenceType,
      long endTime) {
    this.id = id;
    this.name = name;
    this.active = active;
    this.startDateTime = Calendar.getInstance();
    this.startDateTime.setTime(new Date(startTime));
    this.recurrenceDelay = recurrenceDelay;
    this.recurrenceType = RecurrenceType.valueOf(recurrenceType);
    this.endDateTime = Calendar.getInstance();
    this.endDateTime.setTime(new Date(endTime));
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public int getRecurrenceDelay() {
    return recurrenceDelay;
  }

  public void setRecurrenceDelay(int recurrenceDelay) {
    this.recurrenceDelay = recurrenceDelay;
  }

  public RecurrenceType getRecurrenceType() {
    return recurrenceType;
  }

  public void setRecurrenceType(RecurrenceType recurrenceType) {
    this.recurrenceType = recurrenceType;
  }

  public Calendar getStartDateTime() {
    return startDateTime;
  }

  public void setStartDateTime(Calendar startDateTime) {
    this.startDateTime = startDateTime;
  }

  public Calendar getEndDateTime() {
    return endDateTime;
  }

  public void setEndDateTime(Calendar endDateTime) {
    this.endDateTime = endDateTime;
  }

  @NonNull
  @Override
  public String toString() {
    return "ReminderDetails{"
        + "id='"
        + id
        + '\''
        + ", active="
        + active
        + ", name='"
        + name
        + '\''
        + ", startDateTime="
        + startDateTime.getTime()
        + ", recurrenceDelay="
        + recurrenceDelay
        + ", recurrenceType="
        + recurrenceType
        + ", endDateTime="
        + endDateTime.getTime()
        + ", createdDate="
        + createdDate
        + ", modifiedDate="
        + modifiedDate
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReminderModel that = (ReminderModel) o;
    return id == that.id
        && active == that.active
        && name.equals(that.name)
        && startDateTime.equals(that.startDateTime)
        && Objects.equals(recurrenceDelay, that.recurrenceDelay)
        && recurrenceType == that.recurrenceType
        && Objects.equals(endDateTime, that.endDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id, active, name, startDateTime, recurrenceDelay, recurrenceType, endDateTime);
  }
}
