package com.example.medicinereminderapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.Reminder;

import java.util.List;

@Dao
public interface MedicineDao {
    @Query("SELECT * FROM medicines")
    List<Medicine> getAllMedicines();

    @Query("SELECT * FROM medicines WHERE medicineId LIKE :id")
    List<Medicine> getMedicineById(int id);

    @Insert
    void insertMedicine(Reminder reminder);

    @Delete
    void deleteMedicine(Medicine medicine);
}
