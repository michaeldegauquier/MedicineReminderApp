package com.example.medicinereminderapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.medicinereminderapp.entities.Reminder;

@Dao
public interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertReminder(Reminder reminder);

    @Delete
    void deleteReminder(Reminder reminder);
}

// Developers android. Save data in a local database using Room. Geraadpleegd via
// https://developer.android.com/training/data-storage/room
// Geraadpleegd op 16 juni 2020

// Stackoverflow. Get generated id after insert. Geraadpleegd via
// https://stackoverflow.com/questions/5409751/get-generated-id-after-insert
// Geraadpleegd op 24 juli 2020