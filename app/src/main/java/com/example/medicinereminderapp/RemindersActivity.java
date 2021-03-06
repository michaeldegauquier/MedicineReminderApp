package com.example.medicinereminderapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.medicinereminderapp.adapters.MedicineListAdapter;
import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;
import com.example.medicinereminderapp.entities.Reminder;
import com.example.medicinereminderapp.fragments.DisplayNotificationsFragment;
import com.example.medicinereminderapp.fragments.DisplayRemindersFragment;
import com.example.medicinereminderapp.fragments.InsertReminderFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RemindersActivity extends AppCompatActivity implements InsertReminderFragment.InsertReminderButtonFragmentListener, DisplayNotificationsFragment.OnFragmentInteractionListener {
    final Calendar myCalendar = Calendar.getInstance();

    public EditText editTextTime, editTextAmount;
    private AppRepository mRepository;
    private DisplayRemindersFragment displayRemindersFragment;
    private FrameLayout frameContainer;
    public int medicineId;
    public MedicineWithRemindersList medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        this.mRepository = new AppRepository(getApplication());
        this.editTextTime = findViewById(R.id.editTextTimeReminder);
        this.editTextAmount = findViewById(R.id.editTextAmountMedicines);
        this.frameContainer = findViewById(R.id.fragment_display_notifications);

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
            String amount = bundle.getString("amount");
            reminder.amount = Integer.parseInt(amount);

            String dateBegin = medicine.medicines.dateBegin;
            String dateEnd = medicine.medicines.dateEnd;

            long reminderId = this.mRepository.insertReminder(reminder, this);
            Log.i("REMINDER_CREATED", "ID: " + reminderId);

            startAlarmBroadcastReceiver(this, reminder.timeOfDay, (int) reminderId, reminder.amount, dateBegin, dateEnd);
        }
        //Update the reminders view
        this.displayRemindersFragment.updateRemindersList();
        //Clear the input fields (EditText)
        this.editTextTime.setText("");
        this.editTextAmount.setText("");
        //Close the Keyboard of the phone
        this.closeKeyboard();
    }

    //Close the keyboard of your phone
    private void closeKeyboard()
    {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Update the view of the displayed reminders
    public void updateRemindersView() {
        displayRemindersFragment.updateRemindersList();
    }

    public void openFrameDisplayNotifications(int reminderId) {
        DisplayNotificationsFragment fragment = DisplayNotificationsFragment.newInstance(reminderId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragment_display_notifications, fragment, "FRAGMENT_NOTIFICATIONS").commit();
    }

    //After clicked "OK", it will update "myCalendar" with the chosen time
    TimePickerDialog.OnTimeSetListener timeOfDay = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateTime();
        }
    };

    //it opens a TimePicker
    public void onClickTime(View v) {
        new TimePickerDialog(RemindersActivity.this, timeOfDay,
                myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
    }

    //Updates the EditText field with the right time in the right format
    private void updateTime() {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        this.editTextTime.setText(sdf.format(myCalendar.getTime()));
    }

    //It starts the broadcastreceiver for dateBegin -> and repeat every day the notification
    public void startAlarmBroadcastReceiver(Context context, String timeOfDay, int reminderId, int amount, String dateBegin, String dateEnd) {
        Intent myIntent = new Intent(context, AlarmBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("medicineName", medicine.medicines.name);
        bundle.putInt("notificationId", reminderId);
        bundle.putString("timeOfDay", timeOfDay);
        bundle.putString("amount", getString(R.string.take_amount_medicines_1) + " " + amount + " " + getString(R.string.take_amount_medicines_2));
        bundle.putBoolean("cancelAll", false);
        myIntent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderId, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, getYearFromDate(dateBegin));
        calendar.set(Calendar.MONTH, getMonthFromDate(dateBegin));
        calendar.set(Calendar.DAY_OF_MONTH, getDayOfMonthFromDate(dateBegin));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeOfDay.substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeOfDay.substring(3,5)));
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);  //set repeating every 24 hours
        startAlarmBroadcastReceiverDateEnd(context, timeOfDay, reminderId, amount, dateEnd);
    }

    //It starts the broadcastreceiver for dateEnd -> Both notifiers (dateBegin and dateEnd) will be canceled on DateEnd
    public void startAlarmBroadcastReceiverDateEnd(Context context, String timeOfDay, int reminderId, int amount, String dateEnd) {
        Intent myIntent = new Intent(context, AlarmBroadcastReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putString("medicineName", medicine.medicines.name);
        bundle.putInt("notificationId", -reminderId);
        bundle.putString("timeOfDay", timeOfDay);
        bundle.putString("amount", getString(R.string.take_amount_medicines_1) + " " + amount + " " + getString(R.string.take_amount_medicines_2));
        bundle.putBoolean("cancelAll", true);
        myIntent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, -reminderId, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.YEAR, getYearFromDate(dateEnd));
        calendar.set(Calendar.MONTH, getMonthFromDate(dateEnd));
        calendar.set(Calendar.DAY_OF_MONTH, getDayOfMonthFromDate(dateEnd));
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeOfDay.substring(0,2)));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeOfDay.substring(3,5)));
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);  //set repeating every 24 hour
    }

    //It cancels the notification on ID
    public static void cancelNotification(Context context, int reminderId) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminderId, myIntent, 0);
        alarmManager.cancel(pendingIntent);
        Log.i("CANCELED_NOTIFICATION", "Notification canceled with id: " + reminderId);
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

    @Override
    public void OnFragmentInteraction(int reminderId) {
        onBackPressed();
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

// Coding in Flow. Open a Fragment with an Animation + Communicate with Activity - Android Studio Tutorial. Geraadpleegd via
// https://codinginflow.com/tutorials/android/fragment-animation-interface
// Geraadpleegd op 28 juli 2020

// GeeksforGeeks. How to programmatically hide Android soft keyboard. Geraadpleegd via
// https://www.geeksforgeeks.org/how-to-programmatically-hide-android-soft-keyboard/
// Geraadpleegd op 30 juli 2020