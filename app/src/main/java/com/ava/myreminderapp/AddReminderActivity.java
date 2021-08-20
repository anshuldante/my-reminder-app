package com.ava.myreminderapp;

import static java.util.Calendar.DATE;
import static java.util.Calendar.HOUR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddReminderActivity extends AppCompatActivity {

  private final Calendar currentTime = Calendar.getInstance();

  @Inject ReminderDmlViewModel reminderDmlViewModel;

  // UI Components
  private ConstraintLayout recurrenceDetailsCl;
  private ReminderModel referenceReminderModel;
  private ArrayAdapter<CharSequence> spinnerAdapter;
  private ReminderModel reminderModel;
  private Spinner recurrenceTypeSpinner;
  private SwitchCompat recurrenceSwitch;
  private ImageView startDateImageView;
  private EditText recurrenceDelayEt;
  private TimePicker startTimePicker;
  private TextView startDateTextView;
  private TextView endDateTextView;
  private TextView endTimeTextView;
  private EditText reminderName;
  private Button saveButton;
  private Button editButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_reminder);

    referenceReminderModel = new ReminderModel();
    reminderModel = new ReminderModel(referenceReminderModel);

    initComponentMappings();
    initPrimaryComponents();
    initRecurrenceComponents();
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
    saveButton = findViewById(R.id.ara_button_save);
    editButton = findViewById(R.id.ara_button_reset);
  }

  private void initPrimaryComponents() {
    initStartTimeComponents();
    initStartDateComponents();
    initReminderNameComponents();
    initRecurrenceSwitchListener();
    initSaveButton();
    initResetButton();
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
          Log.i("View to Start Time: ", hourOfDay + ":" + minute);
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
        "View to Date: ",
        view.getDayOfMonth() + "/" + (view.getMonth() + 1) + "/" + view.getYear());

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
      runnable.run();
    }
  }

  private void initReminderNameComponents() {
    initReminderNameView();
    reminderName.addTextChangedListener(new ReminderNameChangedListener(reminderModel));
  }

  private void initRecurrenceSwitchListener() {
    initRecurrenceSwitchView();
    recurrenceSwitch.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          recurrenceDetailsCl.setVisibility(isChecked ? View.VISIBLE : View.GONE);
          if (!isChecked) {
            reminderModel.setRecurrenceDelay(null);
            initRecurrenceDelayView();
          }
        });
  }

  private void initSaveButton() {
    saveButton.setOnClickListener(this::saveButtonClickListener);
  }

  private void initResetButton() {
    editButton.setOnClickListener(this::resetButtonClickListener);
  }

  private void saveButtonClickListener(View v) {
    reminderDmlViewModel.addReminder(reminderModel);
    finish();
  }

  private void resetButtonClickListener(View v) {
    Log.i("Resetting Reminder from: ", reminderModel.toString());
    resetToInitialValues();
    Log.i("Reset Reminder to: ", reminderModel.toString());
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
            android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            ((view, hourOfDay, minute) -> timeSetListener(view, dateTime, runnable)),
            dateTime.get(HOUR_OF_DAY),
            dateTime.get(MINUTE),
            true);
    tpd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    tpd.show();
  }

  private void timeSetListener(TimePicker view, Calendar dateTime, Runnable runnable) {
    Log.i("View to Time: ", dateTime.get(HOUR) + ":" + dateTime.get(MINUTE));

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
    Log.i("Start time to view: ", startDateTime.get(HOUR_OF_DAY) + ":" + startDateTime.get(MINUTE));
    startTimePicker.setHour(startDateTime.get(HOUR_OF_DAY));
    startTimePicker.setMinute(startDateTime.get(MINUTE));
  }

  private void initStartDateView() {
    Calendar startDateTime = reminderModel.getStartDateTime();
    Log.i(
        "Start Date to view: ",
        startDateTime.get(DATE)
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
    recurrenceSwitch.setChecked(reminderModel.getRecurrenceDelay() != null);
  }

  private void initRecurrenceTypeView() {
    String recurrenceType = reminderModel.getRecurrenceType().getValue();
    Log.i("Recurrence Type to View: ", recurrenceType);
    recurrenceTypeSpinner.setSelection(spinnerAdapter.getPosition(recurrenceType));
  }

  private void initRecurrenceDelayView() {
    Integer recurrenceDelay = reminderModel.getRecurrenceDelay();
    Log.i(
        "Recurrence Delay to View: ",
        recurrenceDelay != null ? recurrenceDelay.toString() : "is null ");
    recurrenceDelayEt.setText(recurrenceDelay != null ? recurrenceDelay.toString() : null);
  }

  private void initEndDateView() {
    Calendar dateTime = reminderModel.getEndDateTime();
    Log.i(
        "End Date to view: ",
        dateTime.get(DATE) + "/" + (dateTime.get(MONTH) + 1) + "/" + dateTime.get(YEAR));
    endDateTextView.setText(
        getString(
            R.string.ara_reminder_end_date,
            dateTime.get(DATE),
            dateTime.get(MONTH) + 1,
            dateTime.get(YEAR)));
  }

  private void initEndTimeView() {
    Calendar dateTime = reminderModel.getEndDateTime();
    Log.i("End time to view: ", dateTime.get(HOUR_OF_DAY) + ":" + dateTime.get(MINUTE));
    endTimeTextView.setText(
        getString(R.string.ara_reminder_end_time, dateTime.get(HOUR_OF_DAY), dateTime.get(MINUTE)));
  }

  private void resetToInitialValues() {
    reminderModel = new ReminderModel(referenceReminderModel);
    initStartTimeView();
    initStartDateView();
    initReminderNameView();
    initRecurrenceSwitchView();
    initRecurrenceTypeView();
    initRecurrenceDelayView();
    initEndDateView();
    initEndTimeView();
  }
}
