package com.example.medicinereminderapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderapp.R;
import com.example.medicinereminderapp.entities.Medicine;
import com.example.medicinereminderapp.entities.MedicineWithRemindersList;

import java.util.List;

public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private final List<MedicineWithRemindersList> myMedicines;

    public MedicineListAdapter(Context context, List<MedicineWithRemindersList> myMedicines) {
        mInflater = LayoutInflater.from(context);
        this.myMedicines = myMedicines;
        this.context = context;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.medicine_list_item,
                parent, false);
        return new MedicineViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        MedicineWithRemindersList mCurrent = myMedicines.get(position);
        holder.medicineItemView.setText(mCurrent.medicines.name);
    }

    @Override
    public int getItemCount() {
        return myMedicines.size();
    }

    class MedicineViewHolder extends RecyclerView.ViewHolder {
        public final TextView medicineItemView;
        final MedicineListAdapter mAdapter;

        public MedicineViewHolder(View itemView, MedicineListAdapter adapter) {
            super(itemView);
            medicineItemView = itemView.findViewById(R.id.medicine_item);
            this.mAdapter = adapter;
        }
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
