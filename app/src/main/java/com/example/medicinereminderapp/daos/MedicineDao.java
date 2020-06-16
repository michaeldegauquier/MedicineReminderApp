package com.example.medicinereminderapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.Reminder;

import java.util.List;

@Dao
public interface MedicineDao {
    @Query("SELECT * FROM medicines")
    List<Medicine> getAllMedicines();

    @Query("SELECT * FROM medicines WHERE medicineId LIKE :id")
    Medicine getMedicineById(int id);

    @Insert
    void insertMedicine(Medicine medicine);

    @Update
    void updateMedicine(Medicine medicine);

    @Delete
    void deleteMedicine(Medicine medicine);
}
