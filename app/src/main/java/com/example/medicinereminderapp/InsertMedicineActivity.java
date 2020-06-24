package com.example.medicinereminderapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class InsertMedicineActivity extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();

    public EditText editTextNameMedicine, editTextDateBegin, editTextDateEnd;
    public Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_medicine);

        editTextNameMedicine = (EditText) findViewById(R.id.editTextNameMedicine);
        editTextDateBegin = (EditText) findViewById(R.id.editTextDateBegin);
        editTextDateEnd = (EditText) findViewById(R.id.editTextDateEnd);
        addButton = (Button) findViewById(R.id.addMedicineButton);
    }

    public void addMedicine(View view) throws ParseException {
        // get the text from the EditText objects
        String nameMedicine = editTextNameMedicine.getText().toString();
        String dateBeginMedicine = editTextDateBegin.getText().toString();
        String dateEndMedicine = editTextDateEnd.getText().toString();

        if (validate(nameMedicine, dateBeginMedicine, dateEndMedicine)) {
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
    }

    public boolean validate(String name, String dBegin, String dEnd) throws ParseException {
        editTextDateBegin.setError(null);
        editTextDateEnd.setError(null);
        if (name.length() == 0) {
            editTextNameMedicine.requestFocus();
            editTextNameMedicine.setError(getString(R.string.required_field));
            return false;
        }
        else if (dBegin.length() == 0) {
            editTextDateBegin.requestFocus();
            editTextDateBegin.setError(getString(R.string.required_field));
            return false;
        }
        else if (dEnd.length() == 0) {
            editTextDateEnd.requestFocus();
            editTextDateEnd.setError(getString(R.string.required_field));
            return false;
        }
        else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date dateBegin = formatter.parse(dBegin);
            Date dateEnd = formatter.parse(dEnd);

            if (dateBegin.after(dateEnd)) {
                editTextDateEnd.requestFocus();
                editTextDateEnd.setError(getString(R.string.check_date_begin_end));
                Toast.makeText(InsertMedicineActivity.this,getString(R.string.check_date_begin_end),Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
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

// C-sharpcorner. How to Validate the Edit Text in Android Studio. Geraapleegd via
// https://www.c-sharpcorner.com/UploadFile/1e5156/validation/
// Geraadpleegd op 24 juni 2020