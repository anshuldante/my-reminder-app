package com.ava.myreminderapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ava.myreminderapp.R;
import com.ava.myreminderapp.model.ReminderModel;

import java.util.Calendar;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ReminderItemAdapter extends RecyclerView.Adapter<ReminderItemAdapter.ReminderItemViewHolder> {
    private final List<ReminderModel> dataset;
    private final Context context;
    private final Consumer<ReminderModel> deleteReminderConsumer;
    private final BiConsumer<ReminderModel, Boolean> updateReminderStatus;

    public ReminderItemAdapter(List<ReminderModel> dataset, Context context,
                               Consumer<ReminderModel> deleteReminderConsumer, BiConsumer<ReminderModel, Boolean> updateReminderStatus) {
        this.dataset = dataset;
        this.context = context;
        this.deleteReminderConsumer = deleteReminderConsumer;
        this.updateReminderStatus = updateReminderStatus;
    }

    @NonNull
    @Override
    public ReminderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_reminder, parent, false);
        return new ReminderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderItemViewHolder holder, int position) {
        ReminderModel reminder = dataset.get(position);

        holder.reminder = reminder;

        holder.reminderName.setText(reminder.getName());

        Calendar startDateTime = reminder.getStartDateTime();

        holder.reminderTime.setText(context.getString(R.string.ria_reminder_time, startDateTime.get(Calendar.HOUR_OF_DAY), startDateTime.get(Calendar.HOUR_OF_DAY)));

        holder.reminderDate.setText(context.getString(R.string.ria_reminder_date, startDateTime.get(Calendar.DATE), startDateTime.get(Calendar.MONTH), startDateTime.get(Calendar.YEAR)));

        holder.activeSwitch.setChecked(reminder.isActive());

        holder.nextOccurrenceDelay.setText(context.getString(R.string.ria_occurrence_delay, 1, "hours", 3, "minutes"));

        if (reminder.getRecurrenceType() != null) {
            Calendar endDateTime = reminder.getEndDateTime();
            holder.recurrenceDetails.setText(context.getString(R.string.ria_recurrence_details, reminder.getRecurrenceDelay(), reminder.getRecurrenceType()));
            holder.endDateTime.setText(context.getString(R.string.ria_end_date_time, endDateTime.get(Calendar.HOUR_OF_DAY),
                    endDateTime.get(Calendar.MINUTE), endDateTime.get(Calendar.DATE), endDateTime.get(Calendar.MONTH), endDateTime.get(Calendar.YEAR)));
        } else {
            holder.recurrenceDetails.setVisibility(View.GONE);
            holder.endDateTime.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return dataset.size();
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
            ImageView deleteReminderIv = itemView.findViewById(R.id.rir_iv_delete_reminder);
            activeSwitch = itemView.findViewById(R.id.rir_sw_active);

            activeSwitch.setOnCheckedChangeListener(this::toggleReminderStatus);
            deleteReminderIv.setOnClickListener(this::deleteReminder);
        }

        private void toggleReminderStatus(CompoundButton buttonView, boolean isChecked) {
            if (isChecked != reminder.isActive()) {
                updateReminderStatus.accept(reminder, isChecked);
            }
        }

        private void deleteReminder(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            AlertDialog alertDialog = builder.setTitle(R.string.ria_delete_dialog_title)
                    .setMessage(context.getString(R.string.ria_delete_dialog_message, reminder.getName()))
                    .setPositiveButton(R.string.ria_delete_dialog_confirm, (dialog, which) -> {
                        Log.i("Reminders: ", "Delete confirmed!");
                        deleteReminderConsumer.accept(reminder);
                    }).setNegativeButton(R.string.ria_delete_dialog_cancel, (dialog, which) -> {
                        Log.i("Reminders: ", "Delete cancelled!");
                    }).create();
            alertDialog.show();
        }
    }
}
