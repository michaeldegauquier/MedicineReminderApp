package com.example.medicinereminderapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MedicineListDao {
    @Query("SELECT * FROM medicines")
    List<MedicineList> getAllMedicines();

    @Query("SELECT * FROM medicines WHERE medicineListId LIKE :id")
    List<MedicineList> getMedicineById(int id);

    @Insert
    void insertMedicine(ReminderList reminder);

    @Query("DELETE FROM medicines WHERE medicineListId LIKE :id")
    List<MedicineList> deleteMedicineById(int id);
}
