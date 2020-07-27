package com.example.medicinereminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
            String dateBegin = medicine.medicines.dateBegin;
            String dateEnd = medicine.medicines.dateEnd;

            long reminderId = this.mRepository.insertReminder(reminder, this);
            Log.i("REMINDER_CREATED", "ID: " + reminderId);

            startAlarmBroadcastReceiver(this, hours, minutes, (int) reminderId, reminder.amount, dateBegin, dateEnd);
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

    public void startAlarmBroadcastReceiver(Context context, int hours, int minutes, int notificationId, int amount, String dateBegin, String dateEnd) {
        Intent myIntent = new Intent(context, AlarmBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("medicineName", medicine.medicines.name);
        bundle.putInt("notificationId", notificationId);
        bundle.putString("amount", getString(R.string.take_amount_medicines_1) + " " + amount + " " + getString(R.string.take_amount_medicines_2));
        bundle.putBoolean("cancelAll", false);
        myIntent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, getYearFromDate(dateBegin));
        calendar.set(Calendar.MONTH, getMonthFromDate(dateBegin));
        calendar.set(Calendar.DAY_OF_MONTH, getDayOfMonthFromDate(dateBegin));
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);  //set repeating every 24 hours
        startAlarmBroadcastReceiverDateEnd(context, hours, minutes, notificationId, amount, dateEnd);
    }

    public void startAlarmBroadcastReceiverDateEnd(Context context, int hours, int minutes, int notificationId, int amount, String dateEnd) {
        Intent myIntent = new Intent(context, AlarmBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("medicineName", medicine.medicines.name);
        bundle.putInt("notificationId", -notificationId);
        bundle.putString("amount", getString(R.string.take_amount_medicines_1) + " " + amount + " " + getString(R.string.take_amount_medicines_2));
        bundle.putBoolean("cancelAll", true);
        myIntent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -notificationId, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, getYearFromDate(dateEnd));
        calendar.set(Calendar.MONTH, getMonthFromDate(dateEnd));
        calendar.set(Calendar.DAY_OF_MONTH, getDayOfMonthFromDate(dateEnd));
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);  //set repeating every 24 hour
    }

    public static void cancelNotification(Context context, int notificationId) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, myIntent, 0);
        alarmManager.cancel(pendingIntent);
        Log.i("CANCELED", "Notification is canceled with id: " + notificationId);
    }

    public int getYearFromDate(String date) {
        //Format dd/MM/yyyy
        return Integer.parseInt(date.substring(6,10));
    }

    public int getDayOfMonthFromDate(String date) {
        //Format dd/MM/yyyy
        return Integer.parseInt(date.substring(0,2));
    }

    public int getMonthFromDate(String date) {
        //Format dd/MM/yyyy
        return Integer.parseInt(date.substring(3,5)) - 1;
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

// Stackoverflow. Android - Notification at specific time every day. Geraadpleegd via
// https://stackoverflow.com/questions/51510509/android-notification-at-specific-time-every-day
// Geraadpleegd op 23 juli 2020