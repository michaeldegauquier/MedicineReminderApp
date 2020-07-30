package com.example.medicinereminderapp.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.medicinereminderapp.entities.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE reminderId LIKE :id")
    List<Notification> getNotificationsByReminderId(int id);

    @Query("SELECT COUNT(*) FROM notifications WHERE reminderId LIKE :id AND medicineTaken = :status")
    int getAmountNotificationsByReminderIdAndStatus(int id, boolean status);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNotification(Notification notification);

    @Query("UPDATE notifications SET medicineTaken = :status WHERE notificationId LIKE :id")
    void updateNotificationStatusById(int id, boolean status);

    @Query("DELETE FROM notifications WHERE reminderId LIKE :id")
    void deleteNotificationsByReminderId(int id);
}

// Developers android. Save data in a local database using Room. Geraadpleegd via
// https://developer.android.com/training/data-storage/room
// Geraadpleegd op 16 juni 2020

// Stackoverflow. Get generated id after insert. Geraadpleegd via
// https://stackoverflow.com/questions/5409751/get-generated-id-after-insert
// Geraadpleegd op 24 juli 2020