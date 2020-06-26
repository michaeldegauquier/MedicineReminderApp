package com.example.medicinereminderapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private AppRepository repository;
    private final List<MedicineWithRemindersList> myMedicines;
    public static final String MEDICINE_ID = "MEDICINE_ID";

    public MedicineListAdapter(Context context, List<MedicineWithRemindersList> myMedicines, AppRepository repository) {
        mInflater = LayoutInflater.from(context);
        this.myMedicines = myMedicines;
        this.context = context;
        this.repository = repository;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.medicine_list_item,
                parent, false);
        return new MedicineViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, final int position) {
        try {
            MedicineWithRemindersList mCurrent = myMedicines.get(position);
            int totalAmount = getTotalAmountMedicines(mCurrent.medicines.medicineId, repository);
            String medName = mCurrent.medicines.name + " - " + totalAmount;
            holder.medicineName.setText(medName);

            String medDate = mCurrent.medicines.dateBegin + " - " + mCurrent.medicines.dateEnd;
            holder.medicineDate.setText(medDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.medicineItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineWithRemindersList medicine = myMedicines.get(position);

                Log.i("CLICKED ITEM", medicine.medicines.name);
            }
        });

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

                repository.deleteMedicineById(medicine.medicines.medicineId);
                notifyItemRemoved(position);
                ((MainActivity)context).updateMedicineList();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (myMedicines != null) {
            return myMedicines.size();
        }
        return 0;
    }

    private int getTotalAmountMedicines(int medicineId, AppRepository repository) throws ParseException {
        MedicineWithRemindersList med = repository.getMedicineById(medicineId);
        if (med.reminders.size() <= 0) {
            return 0;
        }
        else {
            int days = getDaysBetweenDates(med.medicines.dateBegin, med.medicines.dateEnd);
            int amountMed = 0;
            for (int i = 0; i < med.reminders.size(); i++) {
                amountMed += days * med.reminders.get(i).amount;
            }
            return amountMed;
        }
    }

    private int getDaysBetweenDates(String dBegin, String dEnd) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(new Date());

        Date dateBegin = formatter.parse(dBegin);
        Date dateEnd = formatter.parse(dEnd);
        Date dateToday = formatter.parse(today);

        long diff = 0;
        if (dateToday.after(dateBegin)) {
            Log.i("date1", "Today after begin");
            diff = Math.abs(dateToday.getTime() - dateEnd.getTime());
        }
        else {
            Log.i("date2", "Today not after begin");
            diff = Math.abs(dateBegin.getTime() - dateEnd.getTime());
        }

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
            medicineName = view.findViewById(R.id.medicine_name);
            medicineDate = view.findViewById(R.id.medicine_date);
            deleteButton = view.findViewById(R.id.deleteButton);
            accessAlarmButton = view.findViewById(R.id.accessAlarmsButton);
            medicineItem = view.findViewById(R.id.medicine_item);
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