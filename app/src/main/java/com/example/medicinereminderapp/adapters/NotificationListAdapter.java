package com.example.medicinereminderapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderapp.R;
import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.Notification;

import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private AppRepository mRepository;
    private final List<Notification> myNotifications;

    public NotificationListAdapter(Context context, List<Notification> myNotifications, AppRepository repository) {
        this.mInflater = LayoutInflater.from(context);
        this.myNotifications = myNotifications;
        this.context = context;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = this.mInflater.inflate(R.layout.notification_list_item,
                parent, false);
        return new NotificationViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position) {
        Notification mCurrent = this.myNotifications.get(position);
        holder.notificationTimeOfDay.setText(mCurrent.hour);
        holder.notificationDate.setText(mCurrent.date);

        holder.notificationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification notification = myNotifications.get(position);

                Log.i("CLICKED NOTIFI", notification.notificationId + "");
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.myNotifications != null) {
            return this.myNotifications.size();
        }
        return 0;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView notificationTimeOfDay;
        public final TextView notificationDate;
        public final Switch switchStatus;
        public final GridLayout notificationItem;

        final NotificationListAdapter mAdapter;

        public NotificationViewHolder(View view, NotificationListAdapter adapter) {
            super(view);
            this.view = view;
            this.notificationTimeOfDay = view.findViewById(R.id.notification_timeofday);
            this.notificationDate = view.findViewById(R.id.notification_date);
            this.notificationItem = view.findViewById(R.id.notification_item);
            this.switchStatus = view.findViewById(R.id.switchTaken);
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
