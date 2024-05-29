# My Reminder App

## Alarms and scheduled tasks

* [WorkManager writeup](https://medium.com/google-developer-experts/services-the-life-with-without-and-worker-6933111d62a6)
* [Impact of App Force Close](https://stackoverflow.com/questions/14041208/how-to-reset-alarm-if-app-is-force-closed-in-android?noredirect=1&lq=1)
* [Schedule tasks with WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
* [Notifications](https://developer.android.com/guide/topics/ui/notifiers/notifications)
* [Testing with Doze and App Standby](https://developer.android.com/training/monitoring-device-state/doze-standby#testing_doze_and_app_standby)
* [Creating a simple alarm app](https://learntodroid.com/how-to-create-a-simple-alarm-clock-app-in-android/)

## TODO (high priority)

* ~~Switch to LiveData from Observable if things don't work as expected.~~
* ~~Switch to ViewModel for CRUD.~~
* ~~Switch to ListAdapter.~~
* ~~Cleanup Add Reminder Activity.~~ 
  * ~~Add the buttons on an action bar.~~
  * ~~Clean up the views.~~
* Change all EditTexts to TextInputEditText.
* Add capabilities to select ringtones and sounds.
* Add capability to trigger an alarm.
* Add background services to trigger alarms as scheduled.
* Add notifications for upcoming alarms. 
* Add various preferences.
* Add an option to create a daily TODO list that shows up early in the morning or as soon as the phone internet is up, or maybe some other selectable conditions.

## TODO (low priority)

* Use [this](https://developer.android.com/guide/topics/ui/controls/pickers#java) to convert the date and time pickers to fragments.
* Use [this](https://developer.android.com/guide/topics/ui/dialogs) to convert dialog to DialogFragment.
* Scheduler and executer cleanup if needed.
* Look into ,TextInputLayout, SnackBars, TabLayout (for tabs), NavigationView (for sliding screens), Coordinator layout.
* AppBarLayout, BottomAppBar
* Delete All menu item in general long-click will allow selection and deletion.
* LiveData and Observables can be used well together. We can use observables for everything else and use LiveData just at the ViewModel, since LiveData is lifecycle aware.

## Bugs

* Going below the current time doesn't change the date to tomorrow.
* Date validation doesn't work for the end date's date picker.
* Need validations to skip views in the recycler view if the fields are empty.
