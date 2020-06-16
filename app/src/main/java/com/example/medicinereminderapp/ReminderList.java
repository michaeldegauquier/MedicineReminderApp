package com.example.medicinereminderapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reminders")
public class ReminderList {
    public ReminderList() {}

    @PrimaryKey(autoGenerate = true)
    public int reminderListId;

    public int medicineListId;

    public String timeOfDay;

    public int amount;
}
