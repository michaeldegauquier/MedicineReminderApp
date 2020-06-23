package com.example.medicinereminderapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.medicinereminderapp.R;


public class InsertReminderFragment extends Fragment {

    private InsertReminderButtonFragmentListener mCallBack;
    private View view;

    public InsertReminderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_insert_reminder, container, false);

        Button button = view.findViewById(R.id.buttonAddReminder);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextTime = view.findViewById(R.id.editTextTimeReminder);
                String time = editTextTime.getText().toString();

                EditText editTextAmount = view.findViewById(R.id.editTextAmountMedicines);
                String amount = editTextAmount.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("time", time);
                bundle.putString("amount", amount);

                mCallBack.insertReminder(bundle);
            }
        });

        return view;
    }

    public interface InsertReminderButtonFragmentListener {
        void insertReminder(Bundle bundle);
    }

    // This method insures that the Activity has actually implemented our listener and that it isn't null.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InsertReminderButtonFragmentListener) {
            mCallBack = (InsertReminderButtonFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement InsertReminderButtonFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }
}

// Stackoverflow. Basic communication between two fragments. Geraadpleegd via
// https://stackoverflow.com/questions/13700798/basic-communication-between-two-fragments
// Geraadpleegd op 22 juni 2020