package com.example.medicinereminderapp.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;

import java.util.List;

@Dao
public interface MedicineDao {
    @Query("SELECT * FROM medicines")
    List<MedicineWithRemindersList> getAllMedicines();

    @Query("SELECT * FROM medicines WHERE medicineId LIKE :id")
    MedicineWithRemindersList getMedicineById(int id);

    @Insert
    void insertMedicine(Medicine medicine);

    @Update
    void updateMedicine(Medicine medicine);

    @Delete
    void deleteMedicine(Medicine medicine);
}
