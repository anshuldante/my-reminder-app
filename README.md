# My Reminder App

## Alarms and scheduled tasks

* https://developer.android.com/guide/components/foreground-services
* [Background tasks](https://developer.android.com/guide/background)
* [Schedule repeating alarms](https://developer.android.com/training/scheduling/alarms)
* [Optimize for Doze and App Standby](https://developer.android.com/training/monitoring-device-state/doze-standby)
* [Schedule tasks with WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
* [Alarm Manager](https://developer.android.com/reference/android/app/AlarmManager)
* [Excessive wake ups](https://developer.android.com/topic/performance/vitals/wakeup)
* https://stackoverflow.com/questions/7276537/using-a-broadcast-intent-broadcast-receiver-to-send-messages-from-a-service-to-a
* https://developer.android.com/guide/topics/ui/notifiers/notifications
* [Job Scheduler](https://github.com/googlearchive/android-JobScheduler/tree/master/Application)

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

## Resources

* https://github.com/commonsguy/cw-omnibus
