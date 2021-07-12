package com.ava.myreminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ava.myreminderapp.adapter.ReminderItemAdapter;
import com.ava.myreminderapp.data.ReminderDao;
import com.ava.myreminderapp.data.SampleReminders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private ReminderItemAdapter reminderItemAdapter;
    private FloatingActionButton addReminderButton;
    private RecyclerView reminderRecyclerView;
    private TextView emptyReminderList;

    @Inject
    ReminderDao reminderDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addReminderButton = findViewById(R.id.am_fab_add_reminder);
        addReminderButton.setOnClickListener(v -> startActivity(new Intent(this, AddReminderActivity.class)));

        emptyReminderList = findViewById(R.id.am_tv_no_reminders);
        emptyReminderList.setVisibility(View.GONE);

        reminderItemAdapter = new ReminderItemAdapter(SampleReminders.getSampleReminderList(), this);

        reminderRecyclerView = findViewById(R.id.am_rv_reminders);

        reminderRecyclerView.setAdapter(reminderItemAdapter);
    }
}