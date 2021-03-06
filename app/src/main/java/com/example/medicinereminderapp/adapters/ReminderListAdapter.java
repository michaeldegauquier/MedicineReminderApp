package com.example.medicinereminderapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderapp.R;
import com.example.medicinereminderapp.RemindersActivity;
import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.Reminder;

import java.util.List;

public class ReminderListAdapter extends RecyclerView.Adapter<ReminderListAdapter.ReminderViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private AppRepository mRepository;
    private final List<Reminder> myReminders;

    public ReminderListAdapter(Context context, List<Reminder> myReminders, AppRepository repository) {
        this.mInflater = LayoutInflater.from(context);
        this.myReminders = myReminders;
        this.context = context;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public ReminderListAdapter.ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = this.mInflater.inflate(R.layout.reminder_list_item,
                parent, false);
        return new ReminderViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, final int position) {
        Reminder mCurrent = this.myReminders.get(position);
        holder.reminderTimeOfDay.setText(mCurrent.timeOfDay);
        String amount = mCurrent.amount + "";
        holder.reminderAmountMedicines.setText(amount);

        holder.notificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reminder reminder = myReminders.get(position);
                ((RemindersActivity)context).openFrameDisplayNotifications(reminder.reminderId);
            }
        });

        holder.deleteButtonReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reminder reminder = myReminders.get(position);
                RemindersActivity.cancelNotification(context, reminder.reminderId);
                RemindersActivity.cancelNotification(context, -reminder.reminderId);

                mRepository.deleteNotificationsByReminderId(reminder.reminderId);
                mRepository.deleteReminder(reminder, (Activity) context);
                notifyItemRemoved(position);
                ((RemindersActivity)context).updateRemindersView();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.myReminders != null) {
            return this.myReminders.size();
        }
        return 0;
    }

    class ReminderViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView reminderTimeOfDay;
        public final TextView reminderAmountMedicines;
        public final ImageButton notificationsButton;
        public final ImageButton deleteButtonReminder;
        public final GridLayout reminderItem;

        final ReminderListAdapter mAdapter;

        public ReminderViewHolder(View view, ReminderListAdapter adapter) {
            super(view);
            this.view = view;
            this.reminderTimeOfDay = view.findViewById(R.id.reminder_timeofday);
            this.reminderAmountMedicines = view.findViewById(R.id.reminder_amount);
            this.notificationsButton = view.findViewById(R.id.notificationsButton);
            this.deleteButtonReminder = view.findViewById(R.id.deleteButtonReminder);
            this.reminderItem = view.findViewById(R.id.reminder_item);
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