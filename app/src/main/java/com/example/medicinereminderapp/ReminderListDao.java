package com.example.medicinereminderapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReminderListDao {
    @Insert
    void insertReminder(ReminderList reminder);

    @Query("DELETE FROM reminders WHERE reminderListId LIKE :id")
    List<MedicineList> deleteReminderById(int id);
}
