package com.ava.myreminderapp.module;

import android.content.Context;

import androidx.room.Room;

import com.ava.myreminderapp.data.GetAllRemindersViewModel;
import com.ava.myreminderapp.data.InstanceDao;
import com.ava.myreminderapp.data.InstanceRepository;
import com.ava.myreminderapp.data.ReminderDao;
import com.ava.myreminderapp.data.ReminderDmlViewModel;
import com.ava.myreminderapp.data.ReminderRepository;
import com.ava.myreminderapp.data.RemindersDb;
import com.ava.myreminderapp.service.ReminderManager;

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
  public ExecutorService getReminderDaoExecutor() {
    return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
  }

  @Provides
  @Singleton
  @Named("instanceDaoExecutor")
  public ExecutorService getInstanceDaoExecutor() {
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
  public InstanceDao getInstanceDao(RemindersDb remindersDb) {
    return remindersDb.reminderInstanceDao();
  }

  @Provides
  @Singleton
  public ReminderRepository getReminderRepository(
      ReminderDao dao, @Named("reminderDaoExecutor") ExecutorService executor) {
    return new ReminderRepository(dao, executor);
  }

  @Provides
  @Singleton
  public InstanceRepository getInstanceRepository(
      InstanceDao dao, @Named("instanceDaoExecutor") ExecutorService executor) {
    return new InstanceRepository(dao, executor);
  }

  @Provides
  @Singleton
  public GetAllRemindersViewModel getAllRemindersViewModel(ReminderRepository repository) {
    return new GetAllRemindersViewModel(repository);
  }

  @Provides
  @Singleton
  public ReminderDmlViewModel getReminderDmlViewModel(ReminderRepository repository) {
    return new ReminderDmlViewModel(repository);
  }

  @Provides
  @Singleton
  public ReminderManager getReminderManager(InstanceRepository repository) {
    return new ReminderManager(repository);
  }
}
