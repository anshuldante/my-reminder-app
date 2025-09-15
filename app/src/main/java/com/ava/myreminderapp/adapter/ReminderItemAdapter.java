package com.ava.myreminderapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ava.myreminderapp.R;
import com.ava.myreminderapp.data.ReminderDmlViewModel;
import com.ava.myreminderapp.model.ReminderModel;

import java.util.Calendar;

public class ReminderItemAdapter
    extends ListAdapter<ReminderModel, ReminderItemAdapter.ReminderItemViewHolder> {

  private static final ReminderDiffCallback DIFF_CALLBACK = new ReminderDiffCallback();
  private final Context context;
  private final ReminderDmlViewModel dmlViewModel;
  private final ReminderItemClickListener itemClickListener;

  public ReminderItemAdapter(
      Context context,
      ReminderDmlViewModel dmlViewModel,
      ReminderItemClickListener itemClickListener) {
    super(DIFF_CALLBACK);
    this.context = context;
    this.dmlViewModel = dmlViewModel;
    this.itemClickListener = itemClickListener;
  }

  @NonNull
  @Override
  public ReminderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ReminderItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_reminder, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ReminderItemViewHolder holder, int position) {
    ReminderModel reminder = getItem(position);

    holder.reminder = reminder;

    holder.reminderName.setText(reminder.getName());

    Calendar startDateTime = reminder.getStartDateTime();

    holder.reminderTime.setText(
        context.getString(
            R.string.ria_reminder_time,
            startDateTime.get(Calendar.HOUR_OF_DAY),
            startDateTime.get(Calendar.MINUTE)));

    holder.reminderDate.setText(
        context.getString(
            R.string.ria_reminder_date,
            startDateTime.get(Calendar.DATE),
            startDateTime.get(Calendar.MONTH) + 1,
            startDateTime.get(Calendar.YEAR)));

    holder.activeSwitch.setChecked(reminder.isActive());

    // TODO: replace the hardcoded values.
    holder.nextOccurrenceDelay.setText(
        context.getString(R.string.ria_occurrence_delay, 1, "hours", 3, "minutes"));

    if (reminder.getRecurrenceType() != null) {
      Calendar endDateTime = reminder.getEndDateTime();
      holder.recurrenceDetails.setText(
          context.getString(
              R.string.ria_recurrence_details,
              reminder.getRecurrenceDelay(),
              reminder.getRecurrenceType()));
      holder.endDateTime.setText(
          context.getString(
              R.string.ria_end_date_time,
              endDateTime.get(Calendar.HOUR_OF_DAY),
              endDateTime.get(Calendar.MINUTE),
              endDateTime.get(Calendar.DATE),
              endDateTime.get(Calendar.MONTH) + 1,
              endDateTime.get(Calendar.YEAR)));
    } else {
      holder.recurrenceDetails.setVisibility(View.GONE);
      holder.endDateTime.setVisibility(View.GONE);
    }
  }

  public class ReminderItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView reminderName;
    private final TextView reminderTime;
    private final TextView recurrenceDetails;
    private final SwitchCompat activeSwitch;
    private final TextView reminderDate;
    private final TextView nextOccurrenceDelay;
    private final TextView endDateTime;

    private ReminderModel reminder;

    public ReminderItemViewHolder(View itemView) {
      super(itemView);
      reminderName = itemView.findViewById(R.id.rir_tv_reminderName);
      reminderTime = itemView.findViewById(R.id.rir_tv_reminder_time);
      recurrenceDetails = itemView.findViewById(R.id.rir_tv_reminder_recurrence_details);
      reminderDate = itemView.findViewById(R.id.rir_tv_reminder_date);
      nextOccurrenceDelay = itemView.findViewById(R.id.rir_tv_next_occurrence_delay);
      endDateTime = itemView.findViewById(R.id.rir_tv_end_date_time);
      activeSwitch = itemView.findViewById(R.id.rir_sw_active);

      activeSwitch.setOnCheckedChangeListener(this::toggleReminderStatus);
      itemView.setOnClickListener(this::openReminderEditor);
    }

    private void openReminderEditor(View view) {
      int position = getAdapterPosition();
      if (itemClickListener != null && position != RecyclerView.NO_POSITION) {
        itemClickListener.onItemClick(getReminderAt(position));
      }
    }

    private void toggleReminderStatus(CompoundButton buttonView, boolean isChecked) {
      if (isChecked != reminder.isActive()) {
        dmlViewModel.updateReminderStatus(reminder, isChecked);
      }
    }
  }

  public ReminderModel getReminderAt(int position) {
    return getItem(position);
  }

  public interface ReminderItemClickListener {
    void onItemClick(ReminderModel reminderAt);
  }
}
