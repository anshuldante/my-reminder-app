package com.ava.myreminderapp;

import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_ACTIVE;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_END_TIME;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_ID;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_NAME;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_RECURRENCE_DELAY;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_RECURRENCE_TYPE;
import static com.ava.myreminderapp.UpsertReminderActivity.REMINDER_START_TIME;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ava.myreminderapp.adapter.ReminderItemAdapter;
import com.ava.myreminderapp.data.GetAllRemindersViewModel;
import com.ava.myreminderapp.data.ReminderDmlViewModel;
import com.ava.myreminderapp.model.ReminderModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  @Inject
  ReminderDmlViewModel reminderDml;
  @Inject
  GetAllRemindersViewModel getAllReminders;

  private ReminderItemAdapter reminderItemAdapter;
  private RecyclerView reminderRecyclerView;
  private TextView emptyReminderList;

  public static final String TAG = "MyReminderApp.MainActivity";
  private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 1001;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    checkExactAlarmPermission();

    Toolbar toolbar = findViewById(R.id.am_tb);
    toolbar.setTitle(R.string.main_activity_title);
    setSupportActionBar(toolbar);

    FloatingActionButton addReminderButton = findViewById(R.id.am_fab_add_reminder);
    addReminderButton.setOnClickListener(view -> startUpsertReminderActivity(null));

    emptyReminderList = findViewById(R.id.am_tv_no_reminders);

    reminderItemAdapter =
        new ReminderItemAdapter(this, reminderDml, this::startUpsertReminderActivity);
    reminderRecyclerView = findViewById(R.id.am_rv_reminders);
    reminderRecyclerView.setAdapter(reminderItemAdapter);
    attachItemClickHelper();
    observeAllReminders();
  }

  private void startUpsertReminderActivity(ReminderModel model) {
    Intent intent = new Intent(this, UpsertReminderActivity.class);
    if (model != null) {
      intent.putExtra(REMINDER_ID, model.getId());
      intent.putExtra(REMINDER_ACTIVE, model.isActive());
      intent.putExtra(REMINDER_NAME, model.getName());
      intent.putExtra(REMINDER_START_TIME, model.getStartDateTime().getTimeInMillis());
      intent.putExtra(REMINDER_RECURRENCE_DELAY, model.getRecurrenceDelay());
      intent.putExtra(REMINDER_RECURRENCE_TYPE, model.getRecurrenceType().toString());
      intent.putExtra(REMINDER_END_TIME, model.getEndDateTime().getTimeInMillis());
    }
    startActivity(intent);
  }

  private void attachItemClickHelper() {
    new ItemTouchHelper(
        new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
          @Override
          public boolean onMove(
              @NonNull RecyclerView recyclerView,
              @NonNull RecyclerView.ViewHolder viewHolder,
              @NonNull RecyclerView.ViewHolder target) {
            return false;
          }

          @Override
          public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            reminderDml.deleteReminder(
                reminderItemAdapter.getReminderAt(viewHolder.getAdapterPosition()));
            Toast.makeText(MainActivity.this, "Deleted all reminders", Toast.LENGTH_SHORT)
                .show();
          }
        })
        .attachToRecyclerView(reminderRecyclerView);
  }

  private void observeAllReminders() {
    getAllReminders
        .getAllReminders()
        .observe(
            this,
            reminders -> {
              Log.i(TAG, "All reminders: " + reminders.toString());
              runOnUiThread(
                  () -> {
                    if (reminders.isEmpty()) {
                      reminderRecyclerView.setVisibility(View.GONE);
                      emptyReminderList.setVisibility(View.VISIBLE);
                    } else {
                      reminderRecyclerView.setVisibility(View.VISIBLE);
                      emptyReminderList.setVisibility(View.GONE);
                    }
                    reminderItemAdapter.submitList(reminders);
                  });
            });
  }


  private void checkExactAlarmPermission() {
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM)
        != PackageManager.PERMISSION_GRANTED) {
      // Permission is not granted, request it
      ActivityCompat.requestPermissions(
          this,
          new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM},
          REQUEST_CODE_SCHEDULE_EXACT_ALARM
      );
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {

    if (item.getItemId() == R.id.delete_all_reminders) {
      reminderDml.deleteAllReminders();
      Toast.makeText(this, "Deleted all reminders", Toast.LENGTH_SHORT).show();
      return true;
    } else if (item.getItemId() == R.id.delete_selected_reminder) {
      Toast.makeText(this, "Deleting selected reminders", Toast.LENGTH_SHORT).show();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
