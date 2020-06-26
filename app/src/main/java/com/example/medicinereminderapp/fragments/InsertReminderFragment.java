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

    public EditText editTextTime, editTextAmount;
    public Button button;
    private InsertReminderButtonFragmentListener mCallBack;

    public InsertReminderFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insert_reminder, container, false);

        this.editTextTime = view.findViewById(R.id.editTextTimeReminder);
        this.editTextAmount = view.findViewById(R.id.editTextAmountMedicines);
        this.button = view.findViewById(R.id.buttonAddReminder);

        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = editTextTime.getText().toString();
                String amount = editTextAmount.getText().toString();

                if (validate(time, amount)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("time", time);
                    bundle.putString("amount", amount);

                    mCallBack.insertReminder(bundle);
                }
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
            this.mCallBack = (InsertReminderButtonFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement InsertReminderButtonFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallBack = null;
    }

    public boolean validate(String time, String amount) {
        this.editTextTime.setError(null);
        this.editTextAmount.setError(null);
        if (time.length() == 0) {
            this.editTextTime.requestFocus();
            this.editTextTime.setError(getString(R.string.required_field));
            return false;
        }
        else if (amount.length() == 0) {
            this.editTextAmount.requestFocus();
            this.editTextAmount.setError(getString(R.string.required_field));
            return false;
        }
        return true;
    }
}

// Stackoverflow. Basic communication between two fragments. Geraadpleegd via
// https://stackoverflow.com/questions/13700798/basic-communication-between-two-fragments
// Geraadpleegd op 22 juni 2020