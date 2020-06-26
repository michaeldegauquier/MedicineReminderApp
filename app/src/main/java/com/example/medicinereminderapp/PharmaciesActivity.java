package com.example.medicinereminderapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderapp.adapters.PharmacistListAdapter;
import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.Pharmacist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PharmaciesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private PharmacistListAdapter mAdapter;
    private AppRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacies);
        this.mRepository = new AppRepository(getApplication());
        this.updatePharmacistList();
    }

    public List<Pharmacist> getPharmacists() {
        List<Pharmacist> pharmacists = new ArrayList<Pharmacist>();
        String result = mRepository.getPharmacies();

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray records = jsonObject.getJSONArray("records");

            for (int i = 0; i <= records.length(); i++) {
                Pharmacist pharmacist = new Pharmacist();

                JSONObject fields = records.getJSONObject(i).getJSONObject("fields");
                JSONArray geo = fields.getJSONArray("geo");
                pharmacist.name = fields.getString("pharmacie_apotheek");
                pharmacist.phone = fields.getString("telephone_telefoon");
                pharmacist.address = fields.getString("adresse_adres_nl");
                pharmacist.longitude = geo.getDouble(0);
                pharmacist.latitude = geo.getDouble(1);

                pharmacists.add(pharmacist);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pharmacists;
    }

    public void updatePharmacistList() {
        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview_pharmacies);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PharmacistListAdapter(this, this.getPharmacists(), mRepository);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

// Stackoverflow. Creating JSONObject from string in JAVA (org.json). Geraadpleegd via
// https://stackoverflow.com/questions/12309279/creating-jsonobject-from-string-in-java-org-json
// Geraadpleegd op 26 juni 2020

// Crunchify. How to Read JSON Object From File in Java? Geraadpleegd via
// https://crunchify.com/how-to-read-json-object-from-file-in-java/
// Geraadpleegd op 26 juni 2020
