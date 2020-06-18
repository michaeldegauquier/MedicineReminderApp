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

// Developers android. Define relationships between objects. Geraadpleegd via
// https://developer.android.com/training/data-storage/room/relationships
// Geraadpleegd op 16 juni 2020