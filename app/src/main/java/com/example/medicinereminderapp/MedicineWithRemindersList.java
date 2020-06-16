package com.example.medicinereminderapp;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class MedicineWithRemindersList {
    public MedicineWithRemindersList() {}

    @Embedded public MedicineList medicines;
    @Relation(
            parentColumn = "medicineListId",
            entityColumn = "medicineListId"
    )
    public List<ReminderList> reminders;
}
