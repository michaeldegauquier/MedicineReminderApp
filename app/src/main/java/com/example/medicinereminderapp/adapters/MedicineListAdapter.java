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
import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;

import java.util.List;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private AppRepository repository;
    private final List<MedicineWithRemindersList> myMedicines;

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
        MedicineWithRemindersList mCurrent = myMedicines.get(position);
        holder.medicineName.setText(mCurrent.medicines.name);

        String medDate = mCurrent.medicines.dateBegin + " - " + mCurrent.medicines.dateEnd;
        holder.medicineDate.setText(medDate);

        holder.medicineItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineWithRemindersList medicine = myMedicines.get(position);

                Log.i("CLICKED ITEM", medicine.medicines.name);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MedicineWithRemindersList medicine = myMedicines.get(position);
                Log.i("CLICKED DELETE", medicine.medicines.name);

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

    class MedicineViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView medicineName;
        public final TextView medicineDate;
        public final ImageButton deleteButton;
        public final GridLayout medicineItem;

        final MedicineListAdapter mAdapter;

        public MedicineViewHolder(View view, MedicineListAdapter adapter) {
            super(view);
            this.view = view;
            medicineName = view.findViewById(R.id.medicine_name);
            medicineDate = view.findViewById(R.id.medicine_date);
            deleteButton = view.findViewById(R.id.deleteButton);
            medicineItem = view.findViewById(R.id.medicine_item);
            this.mAdapter = adapter;
            /*
            @Override
            public void onClick(View v) {
                int position = getLayoutPosition();
                MedicineWithRemindersList list = myMedicines.get(position);

                Log.d("DevLog_ListAdapter","clicked list with id: " + list.medicines.medicineId);

                Intent intent = new Intent(context, someClass.class);
                intent.putExtra("com.example.medicinereminderapp.MEDICINE_ID",list.medicines.medicineId);
                context.startActivity(intent);
            }*/
        }
    }
}

// Codebrainer. RecyclerView for Android Beginners - How to display data. Geraadpleegd via
// https://www.codebrainer.com/blog/how-to-display-data-with-recyclerview
// Geraadpleegd op 17 juni 2020

// Android-code blogspot. Android RecyclerView add remove item example. Geraadpleegd via
// https://android--code.blogspot.com/2015/12/android-recyclerview-add-remove-item.html
// Geraadpleegd op 18 juni 2020