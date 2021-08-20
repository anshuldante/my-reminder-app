package com.ava.myreminderapp.module;

import android.content.Context;

import androidx.room.Room;

import com.ava.myreminderapp.data.GetAllRemindersViewModel;
import com.ava.myreminderapp.data.GetReminderViewModel;
import com.ava.myreminderapp.data.ReminderDao;
import com.ava.myreminderapp.data.ReminderDmlViewModel;
import com.ava.myreminderapp.data.ReminderRepository;
import com.ava.myreminderapp.data.RemindersDb;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DbModule {

  @Provides
  @Singleton
  @Named("reminderDaoExecutor")
  public ExecutorService getReminderDaoExecutorService() {
    return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  }

  @Provides
  @Singleton
  public RemindersDb getRemindersDb(@ApplicationContext Context context) {
    return Room.databaseBuilder(context, RemindersDb.class, "Reminders-DB").build();
  }

  @Provides
  @Singleton
  public ReminderDao getReminderDao(RemindersDb remindersDb) {
    return remindersDb.reminderDao();
  }

  @Provides
  @Singleton
  public ReminderRepository getReminderRepository(
      ReminderDao reminderDao, @Named("reminderDaoExecutor") ExecutorService reminderDaoExecutor) {
    return new ReminderRepository(reminderDao, reminderDaoExecutor);
  }

  @Provides
  @Singleton
  public GetAllRemindersViewModel getAllNotesViewModel(ReminderRepository reminderRepository) {
    return new GetAllRemindersViewModel(reminderRepository);
  }

  @Provides
  @Singleton
  public GetReminderViewModel GetReminderViewModel(ReminderRepository reminderRepository) {
    return new GetReminderViewModel(reminderRepository);
  }

  @Provides
  @Singleton
  public ReminderDmlViewModel reminderDmlViewModel(ReminderRepository reminderRepository) {
    return new ReminderDmlViewModel(reminderRepository);
  }
}
