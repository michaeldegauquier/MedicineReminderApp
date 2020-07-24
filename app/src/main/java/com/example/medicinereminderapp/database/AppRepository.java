package com.example.medicinereminderapp.database;

import android.app.Application;

import com.example.medicinereminderapp.daos.MedicineDao;
import com.example.medicinereminderapp.daos.ReminderDao;
import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;
import com.example.medicinereminderapp.entities.Reminder;
import com.example.medicinereminderapp.httprequest.HttpGetRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppRepository {
    private MedicineDao mMedicineDao;
    private ReminderDao mReminderDao;

    public AppRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        this.mMedicineDao = db.medicineDao();
        this.mReminderDao = db.reminderDao();
    }

    //Medicines
    public List<MedicineWithRemindersList> getAllMedicines() {
        return this.mMedicineDao.getAllMedicines();
    }

    public MedicineWithRemindersList getMedicineById(int id) {
        return this.mMedicineDao.getMedicineById(id);
    }

    public void insertMedicine(Medicine medicine) {
        this.mMedicineDao.insertMedicine(medicine);
    }

    public void updateMedicine(Medicine medicine) {
        this.mMedicineDao.updateMedicine(medicine);
    }

    public void deleteMedicineById(int id) {
        this.mMedicineDao.deleteMedicineById(id);
    }

    //Reminders
    public long insertReminder(Reminder reminder) {
        return this.mReminderDao.insertReminder(reminder);
    }

    public void updateReminder(Reminder reminder) {
        this.mReminderDao.updateReminder(reminder);
    }

    public void deleteReminder(Reminder reminder) {
        this.mReminderDao.deleteReminder(reminder);
    }

    //Api
    public String getPharmacies() {
        String myUrl = "https://opendata.brussels.be/api/records/1.0/search/?dataset=pharmacies1&q=&rows=20";
        String result = "";
        HttpGetRequest getRequest = new HttpGetRequest();

        try {
            result = getRequest.execute(myUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }
}

// Medium. Android AsyncTask HTTP GET request Tutorial. Geraadpleegd via
// https://medium.com/@JasonCromer/android-asynctask-http-request-tutorial-6b429d833e28
// Geraadpleegd op 26 juni 2020