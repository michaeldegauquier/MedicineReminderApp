package com.example.medicinereminderapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderapp.R;
import com.example.medicinereminderapp.RemindersActivity;
import com.example.medicinereminderapp.adapters.ReminderListAdapter;
import com.example.medicinereminderapp.database.AppRepository;

public class DisplayRemindersFragment extends Fragment {
    private ReminderListAdapter mAdapter;
    private AppRepository mRepository;
    private RecyclerView mRecyclerView;
    private RemindersActivity remindersActivity;

    public DisplayRemindersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.remindersActivity = (RemindersActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_display_reminders, container, false);
        this.mRecyclerView = view.findViewById(R.id.recyclerview_reminders);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.updateRemindersList();
    }

    public void updateRemindersList() {
        this.mRepository = new AppRepository(this.remindersActivity.getApplication());
        // Create an adapter and supply the data to be displayed.
        this.mAdapter = new ReminderListAdapter(this.remindersActivity,
                this.mRepository.getMedicineById(remindersActivity.medicineId).reminders, this.mRepository);
        // Connect the adapter with the RecyclerView.
        this.mRecyclerView.setAdapter(this.mAdapter);
        // Give the RecyclerView a default layout manager.
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.remindersActivity));
    }
}

// Stackoverflow. Basic communication between two fragments. Geraadpleegd via
// https://stackoverflow.com/questions/13700798/basic-communication-between-two-fragments
// Geraadpleegd op 22 juni 2020