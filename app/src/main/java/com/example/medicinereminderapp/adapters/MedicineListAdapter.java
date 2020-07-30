package com.example.medicinereminderapp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderapp.MainActivity;
import com.example.medicinereminderapp.R;
import com.example.medicinereminderapp.RemindersActivity;
import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;
import com.example.medicinereminderapp.entities.Reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private AppRepository mRepository;
    private final List<MedicineWithRemindersList> myMedicines;
    public static final String MEDICINE_ID = "MEDICINE_ID";

    public MedicineListAdapter(Context context, List<MedicineWithRemindersList> myMedicines, AppRepository repository) {
        this.mInflater = LayoutInflater.from(context);
        this.myMedicines = myMedicines;
        this.context = context;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = this.mInflater.inflate(R.layout.medicine_list_item,
                parent, false);
        return new MedicineViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, final int position) {
        try {
            MedicineWithRemindersList mCurrent = this.myMedicines.get(position);
            int totalAmount = getAmountMedicinesToTake(mCurrent.medicines.medicineId);
            String medName = mCurrent.medicines.name + " - " + totalAmount;
            holder.medicineName.setText(medName);

            String medDate = mCurrent.medicines.dateBegin + " - " + mCurrent.medicines.dateEnd;
            holder.medicineDate.setText(medDate);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        holder.accessAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineWithRemindersList medicine = myMedicines.get(position);

                Intent intent = new Intent(context, RemindersActivity.class);
                intent.putExtra(MEDICINE_ID, medicine.medicines.medicineId);
                context.startActivity(intent);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineWithRemindersList medicine = myMedicines.get(position);

                for (int i = 0; i < medicine.reminders.size(); i++) {
                    Reminder reminder = medicine.reminders.get(i);
                    RemindersActivity.cancelNotification(context, reminder.reminderId);
                    RemindersActivity.cancelNotification(context, -reminder.reminderId);
                    mRepository.deleteNotificationsByReminderId(reminder.reminderId);
                    mRepository.deleteReminder2(reminder);
                }

                mRepository.deleteMedicineById(medicine.medicines.medicineId, (Activity) context);
                notifyItemRemoved(position);
                ((MainActivity)context).updateMedicineList();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.myMedicines != null) {
            return this.myMedicines.size();
        }
        return 0;
    }

    //It returns how many medicines you still have to take
    private int getAmountMedicinesToTake(int medicineId) {
        MedicineWithRemindersList medicine = this.mRepository.getMedicineById(medicineId);
        int totalAmount = this.getTotalAmountMedicines(medicine);
        int amountReminders = 0;

        for (int i = 0; i < medicine.reminders.size(); i++) {
            int amountNotifications = this.mRepository.getAmountNotificationsByReminderIdAndStatus(medicine.reminders.get(i).reminderId, true);
            amountReminders += medicine.reminders.get(i).amount * amountNotifications;
        }

        return totalAmount - amountReminders;
    }

    //It returns the total amount of medicines you have to take
    private int getTotalAmountMedicines(MedicineWithRemindersList medicine) {
        if (medicine.reminders.size() <= 0) {
            return 0;
        }
        else {
            int days = 0;
            try {
                days = getDaysBetweenDates(medicine.medicines.dateBegin, medicine.medicines.dateEnd);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            int amountMed = 0;
            for (int i = 0; i < medicine.reminders.size(); i++) {
                amountMed += days * medicine.reminders.get(i).amount;
            }
            return amountMed;
        }
    }

    //It returns the amount of days between two dates
    private int getDaysBetweenDates(String dBegin, String dEnd) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date dateBegin = null;
        Date dateEnd = null;

        try {
            dateBegin = formatter.parse(dBegin);
            dateEnd = formatter.parse(dEnd);
        } catch (NullPointerException | ParseException e) {
            e.printStackTrace();
        }

        long diff = Math.abs(dateBegin.getTime() - dateEnd.getTime());
        long diffDates = diff / (24 * 60 * 60 * 1000) + 1;
        return (int) diffDates;
    }

    class MedicineViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView medicineName;
        public final TextView medicineDate;
        public final ImageButton deleteButton;
        public final ImageButton accessAlarmButton;
        public final GridLayout medicineItem;

        final MedicineListAdapter mAdapter;

        public MedicineViewHolder(View view, MedicineListAdapter adapter) {
            super(view);
            this.view = view;
            this.medicineName = view.findViewById(R.id.medicine_name);
            this.medicineDate = view.findViewById(R.id.medicine_date);
            this.deleteButton = view.findViewById(R.id.deleteButton);
            this.accessAlarmButton = view.findViewById(R.id.accessAlarmsButton);
            this.medicineItem = view.findViewById(R.id.medicine_item);
            this.mAdapter = adapter;
        }
    }
}

// Codebrainer. RecyclerView for Android Beginners - How to display data. Geraadpleegd via
// https://www.codebrainer.com/blog/how-to-display-data-with-recyclerview
// Geraadpleegd op 17 juni 2020

// Android-code blogspot. Android RecyclerView add remove item example. Geraadpleegd via
// https://android--code.blogspot.com/2015/12/android-recyclerview-add-remove-item.html
// Geraadpleegd op 18 juni 2020

// Stackoverflow. Calculating days between two dates with Java. Geraadpleegd via
// https://stackoverflow.com/questions/20165564/calculating-days-between-two-dates-with-java
// Geraadpleegd op 23 juni 2020