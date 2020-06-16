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
