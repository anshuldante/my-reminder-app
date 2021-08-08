# My Reminder App

## Alarms and scheduled tasks

* [Optimize for Doze and App Standby](https://developer.android.com/training/monitoring-device-state/doze-standby)
* [Schedule tasks with WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
* [Excessive wake ups](https://developer.android.com/topic/performance/vitals/wakeup)
* [Service to Activity Broadcast](https://stackoverflow.com/questions/7276537/)
* [Background Service Limitations](https://developer.android.com/about/versions/oreo/background#services)
* [Power Limitations](https://developer.android.com/topic/performance/power/power-details)
* using-a-broadcast-intent-broadcast-receiver-to-send-messages-from-a-service-to-a
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
