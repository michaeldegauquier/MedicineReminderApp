package com.example.medicinereminderapp.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.medicinereminderapp.daos.MedicineDao;
import com.example.medicinereminderapp.daos.NotificationDao;
import com.example.medicinereminderapp.daos.ReminderDao;
import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.Notification;
import com.example.medicinereminderapp.entities.Reminder;

@Database(entities = {Medicine.class, Reminder.class, Notification.class}, version = 2)
public abstract class AppRoomDatabase extends RoomDatabase {
    public abstract MedicineDao medicineDao();
    public abstract ReminderDao reminderDao();
    public abstract NotificationDao notificationDao();

    private static volatile AppRoomDatabase mINSTANCE;

    static AppRoomDatabase getDatabase(final Context context) {
        if (mINSTANCE == null) {
            synchronized (AppRoomDatabase.class) {
                if (mINSTANCE == null) {
                    mINSTANCE =
                            Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppRoomDatabase.class,
                                    "app_room_database")
                                    .allowMainThreadQueries()
                                    .fallbackToDestructiveMigration()
                                    .build();
                }
            }
        }
        return mINSTANCE;
    }
}
