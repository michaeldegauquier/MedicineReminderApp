package com.example.medicinereminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.medicinereminderapp.adapters.MedicineListAdapter;
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
    public MedicineWithRemindersList medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        this.mRepository = new AppRepository(getApplication());
        this.editTextTime = (EditText) findViewById(R.id.editTextTimeReminder);

        Intent intent = getIntent();
        this.medicineId = intent.getIntExtra(MedicineListAdapter.MEDICINE_ID, -1);

        this.medicine = this.mRepository.getMedicineById(this.medicineId);
        setTitle(medicine.medicines.name);

        FragmentManager fragmentManager = getSupportFragmentManager();
        this.displayRemindersFragment = (DisplayRemindersFragment) fragmentManager.findFragmentById(R.id.fragment_display_reminders);
        if (this.displayRemindersFragment == null) {
            this.displayRemindersFragment = new DisplayRemindersFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_display_reminders, this.displayRemindersFragment).commit();
        }
        cancelNotification(this, 4);
    }

    @Override
    public void insertReminder(Bundle bundle) {
        if (bundle != null) {
            Reminder reminder = new Reminder();
            reminder.medicineId = medicineId;
            reminder.timeOfDay = bundle.getString("time");
            reminder.amount = Integer.parseInt(bundle.getString("amount"));

            int hours = Integer.parseInt(bundle.getString("time").substring(0, 2));
            int minutes = Integer.parseInt(bundle.getString("time").substring(3, 5));

            startAlarmBroadcastReceiver(this, hours, minutes);

            this.mRepository.insertReminder(reminder);
        }
        //Update the reminders
        this.displayRemindersFragment.updateRemindersList();
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

        this.editTextTime.setText(sdf.format(myCalendar.getTime()));
    }

    public void startAlarmBroadcastReceiver(Context context, int hours, int minutes) {
        Intent myIntent = new Intent(context, AlarmBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("medicineName", medicine.medicines.name);
        myIntent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 4, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, Calendar.JULY);
        calendar.set(Calendar.DAY_OF_MONTH, 23);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60 * 1000, pendingIntent);  //set repeating every 24 hours
    }

    public void cancelNotification(Context context, int notificationId) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("medicineName", medicine.medicines.name);
        myIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, myIntent, 0);
        alarmManager.cancel(pendingIntent);
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