package com.example.medicinereminderapp.asynctasks;

import android.os.AsyncTask;

import com.example.medicinereminderapp.daos.MedicineDao;
import com.example.medicinereminderapp.entities.Medicine;

public class getAllMedicinesAsync extends AsyncTask<Medicine, Void, Void> {
    private MedicineDao medicineDao;

    getAllMedicinesAsync(MedicineDao medicineDao) {
        this.medicineDao = medicineDao;
    }

    @Override
    protected Void doInBackground(Medicine... medicines) {
        return null;
    }
}
