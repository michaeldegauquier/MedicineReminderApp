package com.example.medicinereminderapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderapp.R;
import com.example.medicinereminderapp.RemindersActivity;
import com.example.medicinereminderapp.adapters.NotificationListAdapter;
import com.example.medicinereminderapp.database.AppRepository;

public class DisplayNotificationsFragment extends Fragment {
    private static final int REMINDER_ID = 0;
    private int mReminderId;

    private OnFragmentInteractionListener mListener;
    private Button sendBackButton; //Button to go back to the reminders view

    private NotificationListAdapter mAdapter;
    private AppRepository mRepository;
    private RecyclerView mRecyclerView;
    private RemindersActivity remindersActivity;

    public DisplayNotificationsFragment() {}

    public static DisplayNotificationsFragment newInstance(int reminderId) {
        DisplayNotificationsFragment fragment = new DisplayNotificationsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(String.valueOf(REMINDER_ID), reminderId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReminderId = getArguments().getInt(String.valueOf(REMINDER_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.remindersActivity = (RemindersActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_display_notifications, container, false);
        this.mRecyclerView = view.findViewById(R.id.recyclerview_notifications);
        this.sendBackButton = view.findViewById(R.id.buttonBackFrameLayout);

        this.sendBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(mReminderId);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        this.updateNotificationsList();
    }

    //Update view
    public void updateNotificationsList() {
        this.mRepository = new AppRepository(this.remindersActivity.getApplication());
        // Create an adapter and supply the data to be displayed.
        this.mAdapter = new NotificationListAdapter(this.remindersActivity,
                this.mRepository.getNotificationsByReminderId(mReminderId), this.mRepository);
        // Connect the adapter with the RecyclerView.
        this.mRecyclerView.setAdapter(this.mAdapter);
        // Give the RecyclerView a default layout manager.
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.remindersActivity));
    }

    //To go back to the reminders view (overview of the reminders and to create a reminder)
    public void onBackPressed(int sendBackId) {
        if (mListener != null) {
            mListener.OnFragmentInteraction(sendBackId);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void OnFragmentInteraction(int sendBackId);
    }
}

// Coding in Flow. Open a Fragment with an Animation + Communicate with Activity - Android Studio Tutorial. Geraadpleegd via
// https://codinginflow.com/tutorials/android/fragment-animation-interface
// Geraadpleegd op 28 juli 2020