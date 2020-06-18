package com.example.medicinereminderapp.database;

import android.app.Application;

import com.example.medicinereminderapp.daos.MedicineDao;
import com.example.medicinereminderapp.daos.ReminderDao;
import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;
import com.example.medicinereminderapp.entities.Reminder;

import java.util.List;

public class AppRepository {
    private MedicineDao mMedicineDao;
    private ReminderDao mReminderDao;

    public AppRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mMedicineDao = db.medicineDao();
        mReminderDao = db.reminderDao();
    }

    //Medicines
    public List<MedicineWithRemindersList> getAllMedicines() {
        return mMedicineDao.getAllMedicines();
    }

    public MedicineWithRemindersList getMedicineById(int id) {
        return mMedicineDao.getMedicineById(id);
    }

    public void insertMedicine(Medicine medicine) {
        mMedicineDao.insertMedicine(medicine);
    }

    public void updateMedicine(Medicine medicine) {
        mMedicineDao.updateMedicine(medicine);
    }

    public void deleteMedicineById(int id) {
        mMedicineDao.deleteMedicineById(id);
    }

    //Reminders
    public void insertReminder(Reminder reminder) {
        mReminderDao.insertReminder(reminder);
    }

    public void updateReminder(Reminder reminder) {
        mReminderDao.updateReminder(reminder);
    }

    public void deleteReminder(Reminder reminder) {
        mReminderDao.deleteReminder(reminder);
    }
}
