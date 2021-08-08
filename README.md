# My Reminder App

## Alarms and scheduled tasks

* [Schedule tasks with WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
* [Notifications](https://developer.android.com/guide/topics/ui/notifiers/notifications)
* [Testing with Doze and App Standby](https://developer.android.com/training/monitoring-device-state/doze-standby#testing_doze_and_app_standby)

## TODO (high priority)

* Add capabilities to select ringtones and sounds.
* Add capability to trigger an alarm.
* Add background services to trigger alarms as scheduled.
* Add notifications for upcoming alarms.
* Add various preferences.

## TODO (low priority)

* Use [this](https://developer.android.com/guide/topics/ui/controls/pickers#java) to convert the date and time pickers to fragments.
* Use [this](https://developer.android.com/guide/topics/ui/dialogs) to convert dialog to DialogFragment.

## Bugs

* Going below the current time doesn't change the date to tomorrow.
* Date validation doesn't work for the end date's date picker.
* Need validations to skip views in the recycler view if the fields are empty.
