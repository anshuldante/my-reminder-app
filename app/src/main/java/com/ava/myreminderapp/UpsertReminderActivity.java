package com.ava.myreminderapp;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ava.myreminderapp.data.ReminderDmlViewModel;
import com.ava.myreminderapp.listener.RecurrenceDelayChangedListener;
import com.ava.myreminderapp.listener.RecurrenceTypeListener;
import com.ava.myreminderapp.listener.ReminderNameChangedListener;
import com.ava.myreminderapp.model.ReminderModel;
import com.ava.myreminderapp.service.NotificationStarterService;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpsertReminderActivity extends AppCompatActivity {
  public static final String TAG = "MyReminderApp.UpsertReminderActivity";

  public static final String REMINDER_ID = "com.ava.myreminderapp.REMINDER_ID";
  public static final String REMINDER_ACTIVE = "com.ava.myreminderapp.REMINDER_ACTIVE";
  public static final String REMINDER_NAME = "com.ava.myreminderapp.REMINDER_NAME";
  public static final String REMINDER_START_TIME = "com.ava.myreminderapp.REMINDER_START_TIME";
  public static final String REMINDER_RECURRENCE_DELAY = "com.ava.myreminderapp.REMINDER_REC_DELAY";
  public static final String REMINDER_RECURRENCE_TYPE = "com.ava.myreminderapp.REMINDER_REC_TYPE";
  public static final String REMINDER_END_TIME = "com.ava.myreminderapp.REMINDER_END_TIME";

  private final Calendar currentTime = Calendar.getInstance();

  @Inject ReminderDmlViewModel reminderDmlViewModel;

  private AlarmManager alarmMgr;

  // UI Components
  private ArrayAdapter<CharSequence> spinnerAdapter;
  private ConstraintLayout recurrenceDetailsCl;
  private Spinner recurrenceTypeSpinner;
  private SwitchCompat recurrenceSwitch;
  private ImageView startDateImageView;
  private ReminderModel reminderModel;
  private EditText recurrenceDelayEt;
  private TimePicker startTimePicker;
  private TextView startDateTextView;
  private TextView endDateTextView;
  private TextView endTimeTextView;
  private EditText reminderName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upsert_reminder);
    alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);

    setupToolbar();

    reminderModel = buildReminderAndSetTitle();

    initComponentMappings();
    initPrimaryComponents();
    initRecurrenceComponents();
  }

  private void setupToolbar() {
    setSupportActionBar(findViewById(R.id.aur_tb));
    Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.upsert_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.save_reminder) {
      saveReminder();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void saveReminder() {
    if (reminderModel.getId() > 0) {
      reminderDmlViewModel.updateReminder(reminderModel);
    } else {
      reminderDmlViewModel.addReminder(reminderModel);
    }
    triggerNotification();

    finish();
  }

  private void triggerNotification() {
    Intent serviceIntent = new Intent(this, NotificationStarterService.class);

    serviceIntent.putExtra(REMINDER_ID, reminderModel.getId());
    serviceIntent.putExtra(REMINDER_NAME, reminderModel.getName());

    PendingIntent pendingIntent =
        PendingIntent.getService(this, 1234, serviceIntent, FLAG_IMMUTABLE);

    Log.i(TAG, "Creating Reminder Notification Service Intent");
    Log.i(TAG, "Current Time: " + new Date());
    Log.i(TAG, "Alarm Time: " + reminderModel.getStartDateTime().getTime());

    alarmMgr.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP, reminderModel.getStartDateTime().getTimeInMillis(), pendingIntent);
  }

  private ReminderModel buildReminderAndSetTitle() {
    Intent intent = getIntent();
    int id = intent.getIntExtra(REMINDER_ID, -1);
    if (id != -1) {
      setTitle(R.string.era_title_edit_reminder);
      return new ReminderModel(
          id,
          intent.getStringExtra(REMINDER_NAME),
          intent.getBooleanExtra(REMINDER_ACTIVE, false),
          intent.getLongExtra(REMINDER_START_TIME, 0),
          intent.getIntExtra(REMINDER_RECURRENCE_DELAY, 0),
          intent.getStringExtra(REMINDER_RECURRENCE_TYPE),
          intent.getLongExtra(REMINDER_END_TIME, 0));
    }
    setTitle(R.string.era_title_add_reminder);
    return new ReminderModel();
  }

  private void initComponentMappings() {
    spinnerAdapter =
        ArrayAdapter.createFromResource(
            this, R.array.recurrence_type_array, android.R.layout.simple_spinner_item);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    recurrenceTypeSpinner = findViewById(R.id.ara_sn_recurrence_type);
    recurrenceDetailsCl = findViewById(R.id.ara_cl_recurrence_delay);
    recurrenceDelayEt = findViewById(R.id.ara_et_recurrence_number);
    recurrenceSwitch = findViewById(R.id.ara_sc_recurring_reminder);
    startDateTextView = findViewById(R.id.ara_display_date);
    startTimePicker = findViewById(R.id.ara_time_picker_primary);
    startDateImageView = findViewById(R.id.ara_iv_calendar);
    reminderName = findViewById(R.id.ara_et_reminder_name);
    endDateTextView = findViewById(R.id.ara_tv_end_date);
    endTimeTextView = findViewById(R.id.ara_tv_end_time);
  }

  private void initPrimaryComponents() {
    initStartTimeComponents();
    initStartDateComponents();
    initReminderNameComponents();
    initRecurrenceSwitchListener();
  }

  private void initRecurrenceComponents() {
    initRecurrenceTypeSpinner();
    initRecurrenceDelayComponent();
    initEndDateComponents();
    initEndTimeComponents();
  }

  private void initStartTimeComponents() {
    initStartTimeView();

    Calendar startDateTime = reminderModel.getStartDateTime();
    startTimePicker.setIs24HourView(true);
    startTimePicker.setOnTimeChangedListener(
        (view, hourOfDay, minute) -> {
          Log.i(TAG, "View to Start Time: " + hourOfDay + ":" + minute);
          startDateTime.set(HOUR_OF_DAY, hourOfDay);
          startDateTime.set(MINUTE, minute);
        });
  }

  private void initStartDateComponents() {
    initStartDateView();
    Calendar startDateTime = reminderModel.getStartDateTime();
    startDateImageView.setOnClickListener(
        view -> attachDatePickerDialog(startDateTime, this::initStartDateView, true));
  }

  private void attachDatePickerDialog(Calendar dateTime, Runnable runnable, boolean isStartDate) {
    DatePickerDialog dpd =
        new DatePickerDialog(
            this,
            (view, year, month, dayOfMonth) ->
                dateSetListener(view, runnable, dateTime, isStartDate),
            dateTime.get(YEAR),
            dateTime.get(MONTH),
            dateTime.get(DATE));
    dpd.show();
  }

  private void dateSetListener(
      DatePicker view, Runnable runnable, Calendar dateTime, boolean isStartDate) {
    Log.i(
        TAG,
        "View to Date: "
            + view.getDayOfMonth()
            + "/"
            + (view.getMonth() + 1)
            + "/"
            + view.getYear());

    currentTime.setTimeInMillis(System.currentTimeMillis());

    if (isStartDate && dateTime.before(currentTime)) {
      Toast.makeText(this, "Can't setup alarms for a time in the past!", Toast.LENGTH_LONG).show();
    } else if (!isStartDate && dateTime.before(reminderModel.getStartDateTime())) {
      Toast.makeText(this, "Recurrence date can't be before alarm start date!", Toast.LENGTH_LONG)
          .show();
    } else {
      dateTime.set(YEAR, view.getYear());
      dateTime.set(MONTH, view.getMonth());
      dateTime.set(DATE, view.getDayOfMonth());
      dateTime.set(SECOND, 0);
      dateTime.set(MILLISECOND, 0);
      runnable.run();
    }
  }

  private void initReminderNameComponents() {
    initReminderNameView();
    reminderName.addTextChangedListener(new ReminderNameChangedListener(reminderModel));
    reminderName.requestFocus();
  }

  private void initRecurrenceSwitchListener() {
    initRecurrenceSwitchView();
    recurrenceSwitch.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          recurrenceDetailsCl.setVisibility(isChecked ? View.VISIBLE : View.GONE);
          if (!isChecked) {
            initRecurrenceDelayView();
          }
        });
  }

  private void initRecurrenceTypeSpinner() {
    initRecurrenceTypeView();
    recurrenceTypeSpinner.setAdapter(spinnerAdapter);
    recurrenceTypeSpinner.setOnItemSelectedListener(new RecurrenceTypeListener(reminderModel));
  }

  private void initRecurrenceDelayComponent() {
    initRecurrenceDelayView();
    recurrenceDelayEt.addTextChangedListener(new RecurrenceDelayChangedListener(reminderModel));
  }

  private void initEndDateComponents() {
    initEndDateView();
    ImageView endDateImageView = findViewById(R.id.ara_iv_end_calendar);
    endDateImageView.setOnClickListener(
        view ->
            attachDatePickerDialog(reminderModel.getEndDateTime(), this::initEndDateView, false));
  }

  private void initEndTimeComponents() {
    initEndTimeView();
    ImageView endTimeImageView = findViewById(R.id.ara_iv_end_clock);
    endTimeImageView.setOnClickListener(
        view -> attachTimePickerDialog(reminderModel.getEndDateTime(), this::initEndTimeView));
  }

  private void attachTimePickerDialog(Calendar dateTime, Runnable runnable) {
    TimePickerDialog tpd =
        new TimePickerDialog(
            this,
            ((view, hourOfDay, minute) -> timeSetListener(view, dateTime, runnable)),
            dateTime.get(HOUR_OF_DAY),
            dateTime.get(MINUTE),
            true);
    tpd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    tpd.show();
  }

  private void timeSetListener(TimePicker view, Calendar dateTime, Runnable runnable) {
    Log.i(TAG, "View to Time: " + dateTime.get(HOUR) + ":" + dateTime.get(MINUTE));

    currentTime.setTimeInMillis(System.currentTimeMillis());
    int hour = view.getHour();
    int minute = view.getMinute();

    if (dateTime.before(currentTime)) {
      dateTime.set(DATE, dateTime.get(DATE) + 1);
    }

    dateTime.set(HOUR_OF_DAY, hour);
    dateTime.set(MINUTE, minute);
    runnable.run();
  }

  private void initStartTimeView() {
    Calendar startDateTime = reminderModel.getStartDateTime();
    Log.i(
        TAG,
        "Start time to view: " + startDateTime.get(HOUR_OF_DAY) + ":" + startDateTime.get(MINUTE));
    startTimePicker.setHour(startDateTime.get(HOUR_OF_DAY));
    startTimePicker.setMinute(startDateTime.get(MINUTE));
  }

  private void initStartDateView() {
    Calendar startDateTime = reminderModel.getStartDateTime();
    Log.i(
        TAG,
        "Start Date to view: "
            + startDateTime.get(DATE)
            + "/"
            + (startDateTime.get(MONTH) + 1)
            + "/"
            + startDateTime.get(YEAR));
    startDateTextView.setText(
        getString(
            R.string.ria_reminder_date,
            startDateTime.get(DATE),
            startDateTime.get(MONTH) + 1,
            startDateTime.get(YEAR)));
  }

  private void initReminderNameView() {
    reminderName.setText(reminderModel.getName());
  }

  private void initRecurrenceSwitchView() {
    recurrenceSwitch.setChecked(reminderModel.getRecurrenceDelay() > 0);
  }

  private void initRecurrenceTypeView() {
    String recurrenceType = reminderModel.getRecurrenceType().getValue();
    Log.i(TAG, "Recurrence Type to View: " + recurrenceType);
    recurrenceTypeSpinner.setSelection(spinnerAdapter.getPosition(recurrenceType));
  }

  private void initRecurrenceDelayView() {
    recurrenceDelayEt.setText(Integer.toString(reminderModel.getRecurrenceDelay()));
  }

  private void initEndDateView() {
    Calendar dateTime = reminderModel.getEndDateTime();
    Log.i(
        TAG,
        "End Date to view: "
            + dateTime.get(DATE)
            + "/"
            + (dateTime.get(MONTH) + 1)
            + "/"
            + dateTime.get(YEAR));
    endDateTextView.setText(
        getString(
            R.string.ara_reminder_end_date,
            dateTime.get(DATE),
            dateTime.get(MONTH) + 1,
            dateTime.get(YEAR)));
  }

  private void initEndTimeView() {
    Calendar dateTime = reminderModel.getEndDateTime();
    Log.i(TAG, "End time to view: " + dateTime.get(HOUR_OF_DAY) + ":" + dateTime.get(MINUTE));
    endTimeTextView.setText(
        getString(R.string.ara_reminder_end_time, dateTime.get(HOUR_OF_DAY), dateTime.get(MINUTE)));
  }
}
