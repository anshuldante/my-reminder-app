package com.ava.myreminderapp.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ava.myreminderapp.model.ReminderModel;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface ReminderDao {

    @Insert
    void add(ReminderModel user);

    @Delete
    void delete(ReminderModel user);

    @Query("Delete from reminders where id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM reminders WHERE id = :userId")
    ReminderModel getReminderById(int userId);

    @Query("SELECT * FROM reminders")
    Observable<List<ReminderModel>> getAllReminders();
}
