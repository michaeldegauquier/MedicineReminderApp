package com.example.medicinereminderapp.entities;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class MedicineWithRemindersList {
    public MedicineWithRemindersList() {}

    @Embedded public Medicine medicines;
    @Relation(
            parentColumn = "medicineId",
            entityColumn = "medicineId"
    )
    public List<Reminder> reminders;
}
