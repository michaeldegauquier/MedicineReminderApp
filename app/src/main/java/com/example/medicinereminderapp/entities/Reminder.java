package com.example.medicinereminderapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class Reminder {
    public Reminder() {}

    @PrimaryKey(autoGenerate = true)
    public int reminderId;

    public int medicineId;

    public String timeOfDay;

    public int amount;
}

// Developers android. Save data in a local database using Room. Geraadpleegd via
// https://developer.android.com/training/data-storage/room
// Geraadpleegd op 16 juni 2020