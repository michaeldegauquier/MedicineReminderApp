package com.example.medicinereminderapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.medicinereminderapp.entities.Reminder;

@Dao
public interface ReminderDao {
    @Insert
    void insertReminder(Reminder reminder);

    @Update
    void updateReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);
}

// Developers android. Save data in a local database using Room. Geraadpleegd via
// https://developer.android.com/training/data-storage/room
// Geraadpleegd op 16 juni 2020