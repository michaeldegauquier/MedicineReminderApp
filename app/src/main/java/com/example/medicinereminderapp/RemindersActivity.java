package com.example.medicinereminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;
import com.example.medicinereminderapp.entities.Reminder;
import com.example.medicinereminderapp.fragments.DisplayRemindersFragment;
import com.example.medicinereminderapp.fragments.InsertReminderFragment;

public class RemindersActivity extends AppCompatActivity implements InsertReminderFragment.InsertReminderButtonFragmentListener {
    private AppRepository mRepository;
    private DisplayRemindersFragment displayRemindersFragment;
    public int medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        Intent intent = getIntent();
        medicineId = intent.getIntExtra("MEDICINE_ID", -1);

        mRepository = new AppRepository(getApplication());
        MedicineWithRemindersList medicine = mRepository.getMedicineById(medicineId);
        setTitle(medicine.medicines.name);
        FragmentManager fragmentManager = getSupportFragmentManager();
        displayRemindersFragment = (DisplayRemindersFragment) fragmentManager.findFragmentById(R.id.fragment_display_reminders);
        if (displayRemindersFragment == null) {
            displayRemindersFragment = new DisplayRemindersFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_display_reminders, displayRemindersFragment).commit();
        }
    }

    @Override
    public void insertReminder(Bundle bundle) {
        if (bundle != null) {
            Reminder reminder = new Reminder();
            reminder.medicineId = medicineId;
            reminder.timeOfDay = bundle.getString("time");
            reminder.amount = Integer.parseInt(bundle.getString("amount"));

            Log.i("id", medicineId + "");
            Log.i("time", reminder.timeOfDay + "");
            Log.i("amount", reminder.amount + "");

            mRepository.insertReminder(reminder);
        }
        //Update the reminders
        displayRemindersFragment.updateRemindersList(); //FAILS HERE
    }
}

// Stackoverflow. How do I pass data between Activities in Android application? Geraadpleegd via
// https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
// Geraadpleegd op 17 juni 2020

// Stackoverflow. Basic communication between two fragments. Geraadpleegd via
// https://stackoverflow.com/questions/13700798/basic-communication-between-two-fragments
// Geraadpleegd op 22 juni 2020