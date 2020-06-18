package com.example.medicinereminderapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.medicinereminderapp.adapters.MedicineListAdapter;
import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;

import static com.example.medicinereminderapp.InsertMedicineActivity.EXTRA_MEDICINE_OBJ;

public class MainActivity extends AppCompatActivity {
    public static final int INSERT_MEDICINE_REQUEST_CODE = 0;
    private RecyclerView mRecyclerView;
    private MedicineListAdapter mAdapter;
    private AppRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mRepository = new AppRepository(getApplication());
        this.updateMedicineList();
    }

    public void updateMedicineList() {
        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new MedicineListAdapter(this, mRepository.getAllMedicines(), mRepository);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void insertMedicine(View view) {
        Intent intent = new Intent(this, InsertMedicineActivity.class);
        startActivityForResult(intent, INSERT_MEDICINE_REQUEST_CODE);
    }

    // This method is called when the second activity (InsertMedicineActivity) finishes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the InsertMedicineActivity with an OK result
        if (requestCode == INSERT_MEDICINE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // get String data from Intent
                Bundle returnBundle = data.getExtras();
                //String title = data.getStringExtra(InsertMedicineActivity.EXTRA_MEDICINE_OBJ);

                // set text view with string
                AppRepository repo = new AppRepository(getApplication());
                Medicine med = new Medicine();

                if (returnBundle != null) {
                    med.name = returnBundle.getString("name");
                    med.dateBegin = returnBundle.getString("dateBegin");
                    med.dateEnd = returnBundle.getString("dateEnd");
                    Log.i("MEDICINE", med.name + " - " + med.dateBegin + " - " + med.dateEnd);
                    repo.insertMedicine(med);
                    updateMedicineList();
                }
            }
        }
    }
}

// Stackoverflow. How do I pass data between Activities in Android application? Geraadpleegd via
// https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
// Geraadpleegd op 17 juni 2020