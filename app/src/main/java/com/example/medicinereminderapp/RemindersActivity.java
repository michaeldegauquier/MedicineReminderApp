package com.example.medicinereminderapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;
import com.example.medicinereminderapp.entities.Reminder;
import com.example.medicinereminderapp.fragments.DisplayRemindersFragment;
import com.example.medicinereminderapp.fragments.InsertReminderFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RemindersActivity extends AppCompatActivity implements InsertReminderFragment.InsertReminderButtonFragmentListener {
    final Calendar myCalendar = Calendar.getInstance();

    public EditText editTextTime;
    private AppRepository mRepository;
    private DisplayRemindersFragment displayRemindersFragment;
    public int medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        this.editTextTime = (EditText) findViewById(R.id.editTextTimeReminder);

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
        displayRemindersFragment.updateRemindersList();
    }

    public void updateView() {
        displayRemindersFragment.updateRemindersList();
    }

    TimePickerDialog.OnTimeSetListener timeOfDay = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateTime();
        }
    };

    public void onClickTime(View v) {
        new TimePickerDialog(RemindersActivity.this, timeOfDay,
                myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
    }

    private void updateTime() {
        String myFormat = "HH:mm"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        editTextTime.setText(sdf.format(myCalendar.getTime()));
    }
}

// Stackoverflow. How do I pass data between Activities in Android application? Geraadpleegd via
// https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
// Geraadpleegd op 17 juni 2020

// Stackoverflow. Basic communication between two fragments. Geraadpleegd via
// https://stackoverflow.com/questions/13700798/basic-communication-between-two-fragments
// Geraadpleegd op 22 juni 2020

// Stackoverflow. Datepicker: How to popup datepicker when click on edittext. Geraadpleegd via
// https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
// Geraadpleegd op 23 juni 2020

// Stackoverflow. TimePicker Dialog from clicking EditText. Geraadpleegd via
// https://stackoverflow.com/questions/17901946/timepicker-dialog-from-clicking-edittext
// Geraadpleegd op 23 juni 2020