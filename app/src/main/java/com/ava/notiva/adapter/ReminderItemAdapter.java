package com.ava.notiva.adapter;

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

import com.ava.notiva.R;
import com.ava.notiva.data.ReminderDmlViewModel;
import com.ava.notiva.model.RecurrenceType;
import com.ava.notiva.model.ReminderModel;
import com.ava.notiva.util.DateTimeDisplayUtil;
import com.ava.notiva.util.RecurrenceDisplayUtil;

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

    String name = reminder.getName();
    if (name == null || name.trim().isEmpty()) {
      holder.alarmName.setVisibility(View.GONE);
    } else {
      holder.alarmName.setVisibility(View.VISIBLE);
      holder.alarmName.setText(name);
    }

    Calendar nextOccurrence = reminder.getStartDateTime();
    String nextOccurrenceStr = DateTimeDisplayUtil.getFriendlyDateTimeSingleLine(context, nextOccurrence);
    holder.nextOccurrence.setText(nextOccurrenceStr);

    String number = String.valueOf(reminder.getRecurrenceDelay());
    RecurrenceType type = reminder.getRecurrenceType();
    boolean recurrenceEnabled = reminder.getRecurrenceDelay() > 0;
    String endDate = DateTimeDisplayUtil.getFriendlyDate(context, reminder.getEndDateTime());
    String endTime = DateTimeDisplayUtil.getFriendlyTime(reminder.getEndDateTime());

    String summary = RecurrenceDisplayUtil.getRecurrenceSummary(
        context,
        number,
        type,
        endDate,
        endTime
    );
    holder.summary.setText(summary);

    holder.activeSwitch.setChecked(reminder.isActive());
  }

  public class ReminderItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView alarmName;
    private final TextView nextOccurrence;
    private final TextView summary;
    private final SwitchCompat activeSwitch;

    private ReminderModel reminder;

    public ReminderItemViewHolder(View itemView) {
      super(itemView);
      alarmName = itemView.findViewById(R.id.rir_tv_alarm_name);
      nextOccurrence = itemView.findViewById(R.id.rir_tv_next_occurrence);
      summary = itemView.findViewById(R.id.rir_tv_summary);
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
      if (reminder != null && isChecked != reminder.isActive()) {
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
