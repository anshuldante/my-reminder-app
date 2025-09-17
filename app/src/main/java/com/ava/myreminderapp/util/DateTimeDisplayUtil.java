package com.ava.myreminderapp.util;

import android.content.Context;

import com.ava.myreminderapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateTimeDisplayUtil {

  public static FriendlyDateType getFriendlyDateType(Calendar date) {
    Calendar today = Calendar.getInstance();
    Calendar tomorrow = (Calendar) today.clone();
    tomorrow.add(Calendar.DATE, 1);

    if (date.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
        date.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
      return FriendlyDateType.TODAY;
    } else if (date.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
        date.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
      return FriendlyDateType.TOMORROW;
    } else {
      return FriendlyDateType.OTHER;
    }
  }

  public static String getFriendlyDate(Context context, Calendar date) {
    Calendar today = Calendar.getInstance();
    Calendar tomorrow = (Calendar) today.clone();
    tomorrow.add(Calendar.DATE, 1);

    String prefix = "";
    if (date.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
        date.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
      prefix = context.getString(R.string.display_today_prefix);
    } else if (date.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
        date.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
      prefix = context.getString(R.string.display_tomorrow_prefix);
    }

    SimpleDateFormat sdf;
    if (date.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
      sdf = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());
    } else {
      sdf = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
    }

    return prefix + sdf.format(date.getTime());
  }

  public static String getFriendlyTime(Calendar date) {
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    return sdf.format(date.getTime());
  }

  public static String getFriendlyDateTimeSingleLine(Context context, Calendar dateTime) {
    Calendar today = Calendar.getInstance();
    Calendar tomorrow = (Calendar) today.clone();
    tomorrow.add(Calendar.DATE, 1);

    String timeStr = getFriendlyTime(dateTime);

    if (dateTime.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
        dateTime.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
      return context.getString(R.string.display_today_singleline, timeStr);
    } else if (dateTime.get(Calendar.YEAR) == tomorrow.get(Calendar.YEAR) &&
        dateTime.get(Calendar.DAY_OF_YEAR) == tomorrow.get(Calendar.DAY_OF_YEAR)) {
      return context.getString(R.string.display_tomorrow_singleline, timeStr);
    } else {
      SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
      return dateFormat.format(dateTime.getTime()) + ", " + timeStr;
    }
  }
}