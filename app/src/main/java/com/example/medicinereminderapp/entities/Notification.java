package com.example.medicinereminderapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notifications")
public class Notification {
    public Notification() {}

    @PrimaryKey(autoGenerate = true)
    public int notificationId;

    public int reminderId;

    public String hour;

    public String date;

    public boolean medicineTaken;
}

// Developers android. Save data in a local database using Room. Geraadpleegd via
// https://developer.android.com/training/data-storage/room
// Geraadpleegd op 16 juni 2020