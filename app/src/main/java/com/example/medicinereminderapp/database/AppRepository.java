package com.example.medicinereminderapp.database;

import android.app.Application;

import com.example.medicinereminderapp.daos.MedicineDao;
import com.example.medicinereminderapp.daos.ReminderDao;

public class AppRepository {
    private MedicineDao mMedicineDao;
    private ReminderDao mReminderDao;

    public AppRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mMedicineDao = db.medicineDao();
        mReminderDao = db.reminderDao();
    }


}
