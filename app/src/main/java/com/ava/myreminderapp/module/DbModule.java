package com.ava.myreminderapp.module;

import android.content.Context;

import androidx.room.Room;

import com.ava.myreminderapp.data.ReminderDao;
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
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

@Module
@InstallIn(SingletonComponent.class)
public class DbModule {

    @Provides
    @Singleton
    @Named("reminderDaoExecutor")
    public ExecutorService getReminderDaoExecutorService() {
        return Executors.newFixedThreadPool(5);
    }

    @Provides
    @Singleton
    @Named("reminderDaoScheduler")
    public Scheduler getReminderDaoScheduler(@Named("reminderDaoExecutor") ExecutorService executorService) {
        return Schedulers.from(executorService);
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
}
