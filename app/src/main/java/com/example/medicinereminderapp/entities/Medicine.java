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
