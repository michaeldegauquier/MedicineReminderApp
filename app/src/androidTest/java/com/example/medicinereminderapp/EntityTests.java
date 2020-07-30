package com.example.medicinereminderapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;
import com.example.medicinereminderapp.entities.Pharmacist;
import com.example.medicinereminderapp.entities.Reminder;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class EntityTests {
    @Test
    public void makeMedicineNotNull() {
        Medicine medicine = new Medicine();
        medicine.medicineId = 1;
        medicine.name = "Test";
        medicine.dateBegin = "12/02/2020";
        medicine.dateEnd = "15/03/2020";

        Assert.assertNotNull(medicine);
    }

    @Test
    public void makePharmacistNotNull() {
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.name = "Apotheker";
        pharmacist.address = "Straat 222";
        pharmacist.phone = "0477858585";
        pharmacist.longitude = 50.845562907;
        pharmacist.latitude = 4.349116043;

        Assert.assertNotNull(pharmacist);
    }

    @Test
    public void makeReminderNotNull() {
        Reminder reminder = new Reminder();
        reminder.medicineId = 1;
        reminder.reminderId = 1;
        reminder.timeOfDay = "12:30";
        reminder.amount = 2;

        Assert.assertNotNull(reminder);
    }

    @Test
    public void makeMedicineWithReminderSize() {
        MedicineWithRemindersList medicine = new MedicineWithRemindersList();

        Reminder reminder = new Reminder();
        reminder.reminderId = 1;
        reminder.timeOfDay = "12:30";
        reminder.amount = 2;

        medicine.reminders = new ArrayList<Reminder>();
        medicine.reminders.add(reminder);

        Assert.assertEquals(1, medicine.reminders.size());
    }

    @Test
    public void makeMedicineWithReminderNotNull() {
        MedicineWithRemindersList medicine = new MedicineWithRemindersList();

        Reminder reminder = new Reminder();
        reminder.reminderId = 1;
        reminder.timeOfDay = "12:30";
        reminder.amount = 2;

        medicine.reminders = new ArrayList<Reminder>();
        medicine.reminders.add(reminder);

        Assert.assertNotNull(medicine.reminders.get(0));
    }
}
