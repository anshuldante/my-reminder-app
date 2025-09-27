package com.ava.notiva.converter;

import androidx.room.TypeConverter;

import java.util.Calendar;

public class DbTypeConverters {

  @TypeConverter
  public static Calendar toCalendar(Long timeInMillis) {
    Calendar calendar = null;
    if (timeInMillis != null) {
      calendar = Calendar.getInstance();
      calendar.setTimeInMillis(timeInMillis);
    }
    return calendar;
  }

  @TypeConverter
  public static Long fromCalendar(Calendar calendar) {
    if (calendar != null) {
      return calendar.getTimeInMillis();
    }
    return null;
  }
}
