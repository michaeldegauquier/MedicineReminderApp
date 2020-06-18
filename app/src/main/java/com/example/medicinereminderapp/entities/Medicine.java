package com.example.medicinereminderapp.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicines")
public class Medicine {
    public Medicine() {}

    @PrimaryKey(autoGenerate = true)
    public int medicineId;

    public String name;

    public String dateBegin;

    public String dateEnd;
}

// Developers android. Save data in a local database using Room. Geraadpleegd via
// https://developer.android.com/training/data-storage/room
// Geraadpleegd op 16 juni 2020