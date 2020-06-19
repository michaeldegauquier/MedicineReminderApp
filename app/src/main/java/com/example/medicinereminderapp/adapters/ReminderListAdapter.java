package com.example.medicinereminderapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderapp.R;
import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.Reminder;

import java.util.List;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ReminderViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private AppRepository repository;
    private final List<Reminder> myReminders;

    public ReminderListAdapter(Context context, List<Reminder> myReminders, AppRepository repository) {
        mInflater = LayoutInflater.from(context);
        this.myReminders = myReminders;
        this.context = context;
        this.repository = repository;
    }

    @NonNull
    @Override
    public ReminderListAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.medicine_list_item,
                parent, false);
        return new ReminderViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, final int position) {
        Reminder mCurrent = myReminders.get(position);
        holder.reminderTimeOfDay.setText(mCurrent.timeOfDay);
        holder.reminderAmountMedicines.setText(mCurrent.amount);

        holder.reminderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reminder reminder = myReminders.get(position);

                Log.i("CLICKED ITEM", reminder.timeOfDay);
            }
        });

        holder.deleteButtonReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reminder reminder = myReminders.get(position);
                Log.i("CLICKED DELETE", reminder.timeOfDay);

                //repository.deleteReminder(reminder);
                //notifyItemRemoved(position);
                //((DisplayRemindersFragment)context).updateMedicineList();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (myReminders != null) {
            return myReminders.size();
        }
        return 0;
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView reminderTimeOfDay;
        public final TextView reminderAmountMedicines;
        public final ImageButton deleteButtonReminder;
        public final GridLayout reminderItem;

        final ReminderListAdapter mAdapter;

        public ReminderViewHolder(View view, ReminderListAdapter adapter) {
            super(view);
            this.view = view;
            reminderTimeOfDay = view.findViewById(R.id.reminder_timeofday);
            reminderAmountMedicines = view.findViewById(R.id.reminder_amount);
            deleteButtonReminder = view.findViewById(R.id.deleteButtonReminder);
            reminderItem = view.findViewById(R.id.reminder_item);
            this.mAdapter = adapter;
        }
    }
}
