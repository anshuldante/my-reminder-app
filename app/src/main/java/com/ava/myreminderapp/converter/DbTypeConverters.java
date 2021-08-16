package com.ava.myreminderapp.converter;

import androidx.room.TypeConverter;

import java.util.Calendar;
import java.util.Date;

public class DbTypeConverters {

  @TypeConverter
  public static Calendar toCalendar(long timeInMillis) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(timeInMillis);
    return calendar;
  }

  @TypeConverter
  public static long fromCalendar(Calendar calendar) {
    return calendar.getTimeInMillis();
  }

  @TypeConverter
  public static Date toDate(long timeInMillis) {
    return new Date(timeInMillis);
  }

  @TypeConverter
  public static long fromDate(Date date) {
    return date.getTime();
  }
}
