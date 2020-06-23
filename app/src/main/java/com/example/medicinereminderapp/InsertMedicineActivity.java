package com.example.medicinereminderapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InsertMedicineActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();

    public EditText editTextDateBegin;
    public EditText editTextDateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_medicine);

        editTextDateBegin = (EditText) findViewById(R.id.editTextDateBegin);
        editTextDateEnd = (EditText) findViewById(R.id.editTextDateEnd);
    }

    public void addMedicine(View view) {
        // get the text from the EditText objects
        EditText editTextNameMedicine = (EditText) findViewById(R.id.editTextNameMedicine);
        String nameMedicine = editTextNameMedicine.getText().toString();

        EditText editTextDateBegin = (EditText) findViewById(R.id.editTextDateBegin);
        String dateBeginMedicine = editTextDateBegin.getText().toString();

        EditText editTextDateEnd = (EditText) findViewById(R.id.editTextDateEnd);
        String dateEndMedicine = editTextDateEnd.getText().toString();

        // put the editText strings into a Bundle to pass back into an Intent and close this activity
        Bundle bundle = new Bundle();
        bundle.putString("name", nameMedicine);
        bundle.putString("dateBegin", dateBeginMedicine);
        bundle.putString("dateEnd", dateEndMedicine);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    DatePickerDialog.OnDateSetListener dateBegin = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateBegin();
        }
    };

    DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateEnd();
        }
    };

    public void onClickDateBegin(View v) {
        new DatePickerDialog(InsertMedicineActivity.this, dateBegin, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void onClickDateEnd(View v) {
        new DatePickerDialog(InsertMedicineActivity.this, dateEnd, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateBegin() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        editTextDateBegin.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateDateEnd() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        editTextDateEnd.setText(sdf.format(myCalendar.getTime()));
    }
}

// Stackoverflow. How do I pass data between Activities in Android application? Geraadpleegd via
// https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
// Geraadpleegd op 17 juni 2020

// Stackoverflow. Datepicker: How to popup datepicker when click on edittext. Geraadpleegd via
// https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
// Geraadpleegd op 23 juni 2020