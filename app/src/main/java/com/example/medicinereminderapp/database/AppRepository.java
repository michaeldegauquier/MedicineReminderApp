package com.example.medicinereminderapp.database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;

import com.example.medicinereminderapp.MainActivity;
import com.example.medicinereminderapp.RemindersActivity;
import com.example.medicinereminderapp.daos.MedicineDao;
import com.example.medicinereminderapp.daos.NotificationDao;
import com.example.medicinereminderapp.daos.ReminderDao;
import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;
import com.example.medicinereminderapp.entities.Notification;
import com.example.medicinereminderapp.entities.Reminder;
import com.example.medicinereminderapp.httprequest.HttpGetRequest;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppRepository {
    private MedicineDao mMedicineDao;
    private ReminderDao mReminderDao;
    private NotificationDao mNotificationDao;

    public AppRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        this.mMedicineDao = db.medicineDao();
        this.mReminderDao = db.reminderDao();
        this.mNotificationDao = db.notificationDao();
    }

    //Medicines
    public List<MedicineWithRemindersList> getAllMedicines() {
        List<MedicineWithRemindersList> medList = null;

        try {
            medList = new getAllMedicinesAsync(this.mMedicineDao).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return medList;
    }

    public MedicineWithRemindersList getMedicineById(int id) {
        MedicineWithRemindersList med = null;

        try {
            med = new getMedicineByIdAsync(this.mMedicineDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return med;
    }

    public void insertMedicine(Medicine medicine, Activity activity) {
        new insertMedicineAsync(this.mMedicineDao, activity).execute(medicine);
    }

    public void deleteMedicineById(int id, Activity activity) {
        new deleteMedicineByIdAsync(this.mMedicineDao, activity).execute(id);
    }

    //Reminders
    public long insertReminder(Reminder reminder, Activity activity) {
        long reminderId = 0;

        try {
            reminderId = new insertReminderAsync(this.mReminderDao, activity).execute(reminder).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return reminderId;
    }

    public void deleteReminder(Reminder reminder, Activity activity) {
        new deleteReminderAsync(this.mReminderDao, activity).execute(reminder);
    }

    public void deleteReminder2(Reminder reminder) {
        new deleteReminder2Async(this.mReminderDao).execute(reminder);
    }

    //Notifications
    public List<Notification> getNotificationsByReminderId(int reminderId) {
        List<Notification> notifList = null;

        try {
            notifList = new getNotificationsByReminderIdAsync(this.mNotificationDao).execute(reminderId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return notifList;
    }

    public int getAmountNotificationsByReminderIdAndStatus(int reminderId, boolean status) {
        int amount = 0;

        try {
            amount = new getAmountNotificationsByReminderIdAndStatusAsync(this.mNotificationDao, status).execute(reminderId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return amount;
    }

    public long insertNotification(Notification notification) {
        long notificationId = 0;

        try {
            notificationId = new insertNotificationAsync(this.mNotificationDao).execute(notification).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return notificationId;
    }

    public void updateNotificationStatusById(int id, boolean status) {
        new updateNotificationStatusByIdAsync(this.mNotificationDao, status).execute(id);
    }

    public void deleteNotificationsByReminderId(int reminderId) {
        new deleteNotificationsByReminderIdAsync(this.mNotificationDao).execute(reminderId);
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


    //Async Methods

    //get all Medicines (with Reminders)
    private static class getAllMedicinesAsync extends AsyncTask<Void, Void, List<MedicineWithRemindersList>> {
        private MedicineDao medicineDao;

        getAllMedicinesAsync(MedicineDao medicineDao) {
            this.medicineDao = medicineDao;
        }

        @Override
        protected List<MedicineWithRemindersList> doInBackground(Void... voids) {
            return this.medicineDao.getAllMedicines();
        }
    }

    //get Medicine (with Reminders) by ID
    private static class getMedicineByIdAsync extends AsyncTask<Integer, Void, MedicineWithRemindersList> {
        private MedicineDao medicineDao;

        getMedicineByIdAsync(MedicineDao medicineDao) {
            this.medicineDao = medicineDao;
        }

        @Override
        protected MedicineWithRemindersList doInBackground(Integer... integers) {
            return this.medicineDao.getMedicineById(integers[0]);
        }
    }

    //insert Medicine
    private static class insertMedicineAsync extends AsyncTask<Medicine, Void, Void> {
        private MedicineDao medicineDao;
        @SuppressLint("StaticFieldLeak")
        private Activity activity;

        insertMedicineAsync(MedicineDao medicineDao, Activity activity) {
            this.medicineDao = medicineDao;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Medicine... medicines) {
            this.medicineDao.insertMedicine(medicines[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((MainActivity)activity).updateMedicineList();
        }
    }

    //delete Medicine by ID
    private static class deleteMedicineByIdAsync extends AsyncTask<Integer, Void, Void> {
        private MedicineDao medicineDao;
        @SuppressLint("StaticFieldLeak")
        private Activity activity;

        deleteMedicineByIdAsync(MedicineDao medicineDao, Activity activity) {
            this.medicineDao = medicineDao;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            this.medicineDao.deleteMedicineById(integers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((MainActivity)activity).updateMedicineList();
        }
    }

    //insert Reminder
    private static class insertReminderAsync extends AsyncTask<Reminder, Void, Long> {
        private ReminderDao reminderDao;
        @SuppressLint("StaticFieldLeak")
        private Activity activity;

        insertReminderAsync(ReminderDao reminderDao, Activity activity) {
            this.reminderDao = reminderDao;
            this.activity = activity;
        }

        @Override
        protected Long doInBackground(Reminder... reminders) {
            return this.reminderDao.insertReminder(reminders[0]);
        }

        @Override
        protected void onPostExecute(Long id) {
            ((RemindersActivity)activity).updateRemindersView();
        }
    }

    //delete Reminder
    private static class deleteReminderAsync extends AsyncTask<Reminder, Void, Void> {
        private ReminderDao reminderDao;
        @SuppressLint("StaticFieldLeak")
        private Activity activity;

        deleteReminderAsync(ReminderDao reminderDao, Activity activity) {
            this.reminderDao = reminderDao;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Reminder... reminders) {
            this.reminderDao.deleteReminder(reminders[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((RemindersActivity)activity).updateRemindersView();
        }
    }

    //delete Reminder
    private static class deleteReminder2Async extends AsyncTask<Reminder, Void, Void> {
        private ReminderDao reminderDao;

        deleteReminder2Async(ReminderDao reminderDao) {
            this.reminderDao = reminderDao;
        }

        @Override
        protected Void doInBackground(Reminder... reminders) {
            this.reminderDao.deleteReminder(reminders[0]);
            return null;
        }
    }

    //get all Notifications by reminderId
    private static class getNotificationsByReminderIdAsync extends AsyncTask<Integer, Void, List<Notification>> {
        private NotificationDao notificationDao;

        getNotificationsByReminderIdAsync(NotificationDao notificationDao) {
            this.notificationDao = notificationDao;
        }

        @Override
        protected List<Notification> doInBackground(Integer... integers) {
            return this.notificationDao.getNotificationsByReminderId(integers[0]);
        }
    }

    private static class getAmountNotificationsByReminderIdAndStatusAsync extends AsyncTask<Integer, Void, Integer> {
        private NotificationDao notificationDao;
        private boolean status;

        getAmountNotificationsByReminderIdAndStatusAsync(NotificationDao notificationDao, boolean status) {
            this.notificationDao = notificationDao;
            this.status = status;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return this.notificationDao.getAmountNotificationsByReminderIdAndStatus(integers[0], this.status);
        }
    }

    //insert Notification
    private static class insertNotificationAsync extends AsyncTask<Notification, Void, Long> {
        private NotificationDao notificationDao;

        insertNotificationAsync(NotificationDao notificationDao) {
            this.notificationDao = notificationDao;
        }

        @Override
        protected Long doInBackground(Notification... notifications) {
            return this.notificationDao.insertNotification(notifications[0]);
        }
    }

    //update NotificationStatus by Id
    private static class updateNotificationStatusByIdAsync extends AsyncTask<Integer, Void, Void> {
        private NotificationDao notificationDao;
        private boolean status;

        updateNotificationStatusByIdAsync(NotificationDao notificationDao, boolean status) {
            this.notificationDao = notificationDao;
            this.status = status;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            this.notificationDao.updateNotificationStatusById(integers[0], status);
            return null;
        }
    }

    //delete all notifications by reminderId
    private static class deleteNotificationsByReminderIdAsync extends AsyncTask<Integer, Void, Void> {
        private NotificationDao notificationDao;

        deleteNotificationsByReminderIdAsync(NotificationDao notificationDao) {
            this.notificationDao = notificationDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            this.notificationDao.deleteNotificationsByReminderId(integers[0]);
            return null;
        }
    }
}

// Medium. Android AsyncTask HTTP GET request Tutorial. Geraadpleegd via
// https://medium.com/@JasonCromer/android-asynctask-http-request-tutorial-6b429d833e28
// Geraadpleegd op 26 juni 2020