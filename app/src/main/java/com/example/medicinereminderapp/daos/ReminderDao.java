package com.example.medicinereminderapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.example.medicinereminderapp.entities.Reminder;

@Dao
public interface ReminderDao {
    @Insert
    void insertReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);
}
