package com.ava.myreminderapp;

import android.content.Intent;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.ava.myreminderapp.adapter.ReminderItemAdapter;
import com.ava.myreminderapp.data.GetAllRemindersViewModel;
import com.ava.myreminderapp.data.ReminderDmlViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

  @Inject ReminderDmlViewModel reminderDml;
  @Inject GetAllRemindersViewModel getAllReminders;

  private ReminderItemAdapter reminderItemAdapter;
  private RecyclerView reminderRecyclerView;
  private TextView emptyReminderList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.am_tb);
    toolbar.setTitle("My Reminders");
    setSupportActionBar(toolbar);

    FloatingActionButton addReminderButton = findViewById(R.id.am_fab_add_reminder);
    addReminderButton.setOnClickListener(
        view -> startActivity(new Intent(this, AddReminderActivity.class)));

    emptyReminderList = findViewById(R.id.am_tv_no_reminders);

    reminderItemAdapter = new ReminderItemAdapter(this, reminderDml);
    reminderRecyclerView = findViewById(R.id.am_rv_reminders);
    reminderRecyclerView.setAdapter(reminderItemAdapter);
    attachItemClickHelper();
    observeAllReminders();
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
                Toast.makeText(MainActivity.this, "All notes deleted!", Toast.LENGTH_SHORT).show();
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
              Log.i("Reminders: ", "All reminders: " + reminders.toString());
              runOnUiThread(
                  () -> {
                    if (reminders.size() == 0) {
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

    if (item.getItemId() == R.id.delete_all_notes) {
      reminderDml.deleteAllReminders();
      Toast.makeText(this, "All notes deleted!", Toast.LENGTH_SHORT).show();
      return true;
    } else if (item.getItemId() == R.id.delete_selected_notes) {
      Toast.makeText(this, "Deleting selected notes", Toast.LENGTH_SHORT).show();
    }
    return super.onOptionsItemSelected(item);
  }
}
