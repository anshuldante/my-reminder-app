package com.ava.notiva.util;

import android.content.Context;

import com.ava.notiva.R;
import com.ava.notiva.model.RecurrenceType;

public class RecurrenceDisplayUtil {
  public static String getRecurrenceSummary(
      Context context,
      String number,
      RecurrenceType type,
      String endDate,
      String endTime
  ) {
    if (type == null || type == RecurrenceType.NEVER) {
      return context.getString(R.string.ara_recurrence_summary_default);
    }
    if (number == null || number.trim().isEmpty()) {
      return context.getString(R.string.ara_recurrence_number_required);
    }

    StringBuilder summary = new StringBuilder();

    if (type == RecurrenceType.FOREVER) {
      summary.append(" forever");
    } else {
      summary.append("Every ").append(number).append(" ").append(type.toString().toLowerCase());
      boolean hasEndDate = endDate != null && !endDate.isEmpty() && !endDate.equals(context.getString(R.string.ara_date_default));
      boolean hasEndTime = endTime != null && !endTime.isEmpty() && !endTime.equals(context.getString(R.string.ara_time_default));
      if (hasEndDate || hasEndTime) {
        summary.append(" till ");
        if (hasEndTime) {
          summary.append(endTime);
          if (hasEndDate) summary.append(" ");
        }
        if (hasEndDate) {
          summary.append(endDate);
        }
      }
    }
    return summary.toString();
  }
}