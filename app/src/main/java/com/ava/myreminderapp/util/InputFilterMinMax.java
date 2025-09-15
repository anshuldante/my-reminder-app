package com.ava.myreminderapp.util;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {
  private final int min, max;

  public InputFilterMinMax(int min, int max) {
    this.min = min;
    this.max = max;
  }

  @Override
  public CharSequence filter(CharSequence source, int start, int end,
                             Spanned dest, int dstart, int dend) {
    try {
      String newVal = dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());
      if (newVal.isEmpty()) return null;
      int input = Integer.parseInt(newVal);
      if (isInRange(min, max, input))
        return null;
    } catch (NumberFormatException nfe) {
    }
    return "";
  }

  private boolean isInRange(int a, int b, int c) {
    return b > a ? c >= a && c <= b : c >= b && c <= a;
  }
}