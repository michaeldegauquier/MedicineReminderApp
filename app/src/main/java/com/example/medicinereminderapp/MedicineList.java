package com.example.medicinereminderapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicines")
public class MedicineList {
    public MedicineList() {}

    @PrimaryKey(autoGenerate = true)
    public int medicineListId;

    public String name;

    public String dateBegin;

    public String dateEnd;
}
