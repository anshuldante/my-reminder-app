package com.ava.myreminderapp.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.ava.myreminderapp.model.ReminderModel;

public class ReminderDiffCallback extends DiffUtil.ItemCallback<ReminderModel> {
  @Override
  public boolean areItemsTheSame(@NonNull ReminderModel oldItem, @NonNull ReminderModel newItem) {
    return oldItem.getId() == newItem.getId();
  }

  @Override
  public boolean areContentsTheSame(
      @NonNull ReminderModel oldItem, @NonNull ReminderModel newItem) {
    return oldItem.equals(newItem);
  }
}
