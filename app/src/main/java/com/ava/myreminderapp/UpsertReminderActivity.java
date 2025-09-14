package com.ava.myreminderapp;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static com.ava.myreminderapp.util.ReminderConstants.REMINDER_ACTIVE;
import static com.ava.myreminderapp.util.ReminderConstants.REMINDER_END_TIME;
import static com.ava.myreminderapp.util.ReminderConstants.REMINDER_ID;
import static com.ava.myreminderapp.util.ReminderConstants.REMINDER_NAME;
import static com.ava.myreminderapp.util.ReminderConstants.REMINDER_RECURRENCE_DELAY;
import static com.ava.myreminderapp.util.ReminderConstants.REMINDER_RECURRENCE_TYPE;
import static com.ava.myreminderapp.util.ReminderConstants.REMINDER_START_TIME;
import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ava.myreminderapp.data.ReminderDmlViewModel;
import com.ava.myreminderapp.listener.RecurrenceDelayChangedListener;
import com.ava.myreminderapp.listener.RecurrenceTypeListener;
import com.ava.myreminderapp.listener.ReminderNameChangedListener;
import com.ava.myreminderapp.model.ReminderModel;
import com.ava.myreminderapp.service.NotificationStarterService;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UpsertReminderActivity extends AppCompatActivity {
  public static final String TAG = "MyReminderApp.UpsertReminderActivity";

  private final Calendar currentTime = Calendar.getInstance();

  @Inject ReminderDmlViewModel reminderDmlViewModel;

  private AlarmManager alarmMgr;

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

    reminderModel = buildReminderAndSetTitle();

    initComponentMappings();
    initPrimaryComponents();
    initRecurrenceComponents();
  }

  @Override
  protected void onStart() {
    super.onStart();
    findViewById(R.id.btn_cancel).setOnClickListener(v -> finish());
    findViewById(R.id.btn_save).setOnClickListener(v -> saveReminder());
  }

  private void saveReminder() {
    if (reminderModel.getId() > 0) {
      triggerNotification();
      reminderDmlViewModel.updateReminder(reminderModel);
      finish();
    } else {
      reminderDmlViewModel.addReminderWithCallback(reminderModel, (newId) -> {
        if (newId > 0) {
          reminderModel.setId(newId.intValue());
          runOnUiThread(() -> {
            triggerNotification();
            finish();
          });
        } else {
          Log.w(TAG, "Reminder not saved, skipping notification scheduling. Invalid ID: " + newId);
          runOnUiThread(this::finish);
        }
      });
    }
    finish();
  }

  private void triggerNotification() {
    Intent alarmIntent = new Intent(this, NotificationStarterService.class);

    alarmIntent.putExtra(REMINDER_ID, reminderModel.getId());
    alarmIntent.putExtra(REMINDER_NAME, reminderModel.getName());

    PendingIntent pendingIntent =
        PendingIntent.getService(this, reminderModel.getId(), alarmIntent, FLAG_IMMUTABLE);

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
      return new ReminderModel(
          id,
          intent.getStringExtra(REMINDER_NAME),
          intent.getBooleanExtra(REMINDER_ACTIVE, false),
          intent.getLongExtra(REMINDER_START_TIME, 0),
          intent.getIntExtra(REMINDER_RECURRENCE_DELAY, 0),
          intent.getStringExtra(REMINDER_RECURRENCE_TYPE),
          intent.getLongExtra(REMINDER_END_TIME, 0));
    }
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
    startTimePicker.setIs24HourView(false);
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
    startDateImageView.setOnClickListener(view -> showMaterialDatePicker(startDateTime, this::initStartDateView, true));
  }

  private void showMaterialDatePicker(Calendar dateTime, Runnable updateView, boolean isStartDate) {
    MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
        .setTitleText(isStartDate ? "Select Start Date" : "Select End Date")
        .setSelection(dateTime.getTimeInMillis())
        .build();
    datePicker.addOnPositiveButtonClickListener(selection -> {
      Calendar selected = Calendar.getInstance();
      selected.setTimeInMillis(selection);
      // Validation logic (same as before)
      currentTime.setTimeInMillis(System.currentTimeMillis());
      if (isStartDate && selected.before(currentTime)) {
        Toast.makeText(this, "Can't setup alarms for a time in the past!", Toast.LENGTH_LONG).show();
      } else if (!isStartDate && selected.before(reminderModel.getStartDateTime())) {
        Toast.makeText(this, "Recurrence date can't be before alarm start date!", Toast.LENGTH_LONG).show();
      } else {
        dateTime.set(YEAR, selected.get(YEAR));
        dateTime.set(MONTH, selected.get(MONTH));
        dateTime.set(DATE, selected.get(DATE));
        dateTime.set(SECOND, 0);
        dateTime.set(MILLISECOND, 0);
        updateView.run();
      }
    });
    datePicker.show(getSupportFragmentManager(), isStartDate ? "start_date_picker" : "end_date_picker");
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
    recurrenceDetailsCl.setVisibility(recurrenceSwitch.isChecked() ? View.VISIBLE : View.GONE);
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
        view -> showMaterialDatePicker(reminderModel.getEndDateTime(), this::initEndDateView, false));
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
    Objects.requireNonNull(tpd.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
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
    recurrenceDelayEt.setText(String.format(Locale.getDefault(), "%,d", reminderModel.getRecurrenceDelay()));
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
