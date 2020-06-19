package com.example.medicinereminderapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class InsertMedicineActivity extends AppCompatActivity {
    public final static String EXTRA_MEDICINE_OBJ = "com.example.medicinereminderapp.MEDICINE_OBJ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_medicine);
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
}

// Stackoverflow. How do I pass data between Activities in Android application? Geraadpleegd via
// https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
// Geraadpleegd op 17 juni 2020