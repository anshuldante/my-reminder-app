package com.ava.notiva.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.ava.notiva.model.ReminderModel;

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
