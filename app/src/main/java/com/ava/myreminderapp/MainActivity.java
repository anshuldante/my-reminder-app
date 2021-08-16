package com.ava.myreminderapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ava.myreminderapp.adapter.ReminderItemAdapter;
import com.ava.myreminderapp.data.ReminderDao;
import com.ava.myreminderapp.model.ReminderModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  private final CompositeDisposable compositeDisposable = new CompositeDisposable();
  private final List<ReminderModel> reminderList = new ArrayList<>();

  @Inject
  @Named("reminderDaoExecutor")
  ExecutorService reminderDaoExecutor;

  @Inject
  @Named("reminderDaoScheduler")
  Scheduler reminderDaoScheduler;

  @Inject ReminderDao reminderDao;
  private ReminderItemAdapter reminderItemAdapter;
  private RecyclerView reminderRecyclerView;
  private TextView emptyReminderList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    FloatingActionButton addReminderButton = findViewById(R.id.am_fab_add_reminder);
    addReminderButton.setOnClickListener(
        view -> startActivity(new Intent(this, AddReminderActivity.class)));

    Button deleteAllButton = findViewById(R.id.am_bt_delete_all);
    deleteAllButton.setOnClickListener(this::deleteAllRemindersDialog);

    emptyReminderList = findViewById(R.id.am_tv_no_reminders);

    reminderItemAdapter =
        new ReminderItemAdapter(
            reminderList, this, this::deleteReminder, this::updateReminderStatus);
    reminderRecyclerView = findViewById(R.id.am_rv_reminders);
    reminderRecyclerView.setAdapter(reminderItemAdapter);

    observeGetAllReminders();
  }

  private void observeGetAllReminders() {
    Disposable disposable =
        reminderDao
            .getAllReminders()
            .subscribeOn(reminderDaoScheduler)
            .subscribe(
                reminders -> {
                  Log.i("Reminders: ", "All reminders: " + reminders.toString());
                  runOnUiThread(
                      () -> {
                        reminderList.clear();
                        reminderList.addAll(reminders);
                        if (reminders.size() == 0) {
                          reminderRecyclerView.setVisibility(View.GONE);
                          emptyReminderList.setVisibility(View.VISIBLE);
                        } else {
                          reminderRecyclerView.setVisibility(View.VISIBLE);
                          emptyReminderList.setVisibility(View.GONE);
                        }
                        reminderItemAdapter.notifyDataSetChanged();
                      });
                },
                throwable ->
                    Log.e("Reminders: ", "Exception while fetching all reminders", throwable),
                () -> Log.i("Reminders: ", "Finished finding reminders"));
    compositeDisposable.add(disposable);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.dispose();
    reminderDaoScheduler.shutdown();
    reminderDaoExecutor.shutdown();
  }

  private void deleteReminder(ReminderModel reminder) {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.deleteById(reminder.getId());
            Log.i("Reminders: ", "Deleted reminder: " + reminder.getName());
          } catch (Exception e) {
            Log.e("Reminders: ", "Exception while deleting reminder: " + reminder.getName(), e);
          }
        });
  }

  private void updateReminderStatus(ReminderModel reminder, boolean isActive) {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.updateStatus(reminder.getId(), isActive);
            Log.i(
                "Reminders: ",
                "Updated reminder: " + reminder.getName() + "'s status to: " + isActive);
          } catch (Exception e) {
            Log.e(
                "Reminders: ",
                "Exception while Updating reminder: "
                    + reminder.getName()
                    + "'s status to: "
                    + isActive);
          }
        });
  }

  private void deleteAllRemindersDialog(View view) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    AlertDialog alertDialog =
        builder
            .setTitle(R.string.am_delete_all_dialog_title)
            .setMessage(R.string.am_delete_all_dialog_message)
            .setPositiveButton(
                R.string.am_delete_dialog_confirm,
                (dialog, which) -> {
                  Log.i("Reminders: ", "Delete All Confirmed!");
                  deleteAllReminders();
                })
            .setNegativeButton(
                R.string.ria_delete_dialog_cancel,
                (dialog, which) -> Log.i("Reminders: ", "Delete All Cancelled!"))
            .create();
    alertDialog.show();
  }

  private void deleteAllReminders() {
    reminderDaoExecutor.submit(
        () -> {
          try {
            reminderDao.deleteAll();
            Log.i("Reminders: ", "Deleted All reminders!");
          } catch (Exception e) {
            Log.e("Reminders: ", "Exception while deleting all reminders", e);
          }
        });
  }
}
