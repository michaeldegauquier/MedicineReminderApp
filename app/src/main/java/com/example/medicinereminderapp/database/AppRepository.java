package com.example.medicinereminderapp.database;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import androidx.fragment.app.Fragment;

import com.example.medicinereminderapp.MainActivity;
import com.example.medicinereminderapp.daos.MedicineDao;
import com.example.medicinereminderapp.daos.ReminderDao;
import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;
import com.example.medicinereminderapp.entities.Reminder;
import com.example.medicinereminderapp.fragments.DisplayRemindersFragment;
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

    public void insertMedicine(Medicine medicine, Activity activity) {
        new insertMedicineAsync(mMedicineDao, activity).execute(medicine);
    }

    public void deleteMedicineById(int id) {
        this.mMedicineDao.deleteMedicineById(id);
    }

    //Reminders
    public long insertReminder(Reminder reminder, Fragment fragment) {
        long notificationId = 0;

        try {
            notificationId =  new insertReminderAsync(mReminderDao, fragment).execute(reminder).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return notificationId;
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

    // get MedicineWithRemindersList


    // get MedicineWithRemindersList by ID


    // insert Medicine
    private static class insertMedicineAsync extends AsyncTask<Medicine, Void, Void> {
        private MedicineDao medicineDao;
        private Activity activity;

        insertMedicineAsync(MedicineDao medicineDao, Activity activity) {
            this.medicineDao = medicineDao;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Medicine... medicines) {
            medicineDao.insertMedicine(medicines[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((MainActivity)activity).updateMedicineList();
        }
    }

    // delete Medicine by ID


    // insert Reminder
    private static class insertReminderAsync extends AsyncTask<Reminder, Void, Long> {
        private ReminderDao reminderDao;
        private Fragment fragment;

        insertReminderAsync(ReminderDao reminderDao, Fragment fragment) {
            this.reminderDao = reminderDao;
            this.fragment = fragment;
        }

        @Override
        protected Long doInBackground(Reminder... reminders) {
            return reminderDao.insertReminder(reminders[0]);
        }

        @Override
        protected void onPostExecute(Long number) {
            ((DisplayRemindersFragment)fragment).updateRemindersList();
        }
    }

    // delete Reminder by ID

}

// Medium. Android AsyncTask HTTP GET request Tutorial. Geraadpleegd via
// https://medium.com/@JasonCromer/android-asynctask-http-request-tutorial-6b429d833e28
// Geraadpleegd op 26 juni 2020