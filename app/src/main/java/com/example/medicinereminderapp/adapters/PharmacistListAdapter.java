package com.example.medicinereminderapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinereminderapp.R;
import com.example.medicinereminderapp.database.AppRepository;
import com.example.medicinereminderapp.entities.Pharmacist;

import java.util.List;

public class PharmacistListAdapter extends RecyclerView.Adapter<PharmacistListAdapter.PharmaViewHolder> {
    private Context context;
    private LayoutInflater mInflater;
    private AppRepository repository;
    private final List<Pharmacist> myPharmacists;

    public PharmacistListAdapter(Context context, List<Pharmacist> myPharmacists, AppRepository repository) {
        this.mInflater = LayoutInflater.from(context);
        this.myPharmacists = myPharmacists;
        this.context = context;
        this.repository = repository;
    }

    @NonNull
    @Override
    public PharmaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = this.mInflater.inflate(R.layout.pharmacies_list_item,
                parent, false);
        return new PharmaViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmaViewHolder holder, final int position) {
        Pharmacist mCurrent = this.myPharmacists.get(position);
        holder.pharmaName.setText(mCurrent.name);
        holder.pharmaAddress.setText(mCurrent.address);
        holder.pharmaPhone.setText(mCurrent.phone);

        holder.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pharmacist pharmacist = myPharmacists.get(position);

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=" + pharmacist.longitude + "," + pharmacist.latitude + " (" + pharmacist.address + ")"));
                context.startActivity(intent);
            }
        });

        holder.phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pharmacist pharmacist = myPharmacists.get(position);

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + pharmacist.phone));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.myPharmacists != null) {
            return this.myPharmacists.size();
        }
        return 0;
    }

    class PharmaViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView pharmaName;
        public final TextView pharmaAddress;
        public final TextView pharmaPhone;
        public final ImageButton phoneButton;
        public final ImageButton locationButton;

        final PharmacistListAdapter mAdapter;

        public PharmaViewHolder(View view, PharmacistListAdapter adapter) {
            super(view);
            this.view = view;
            this.pharmaName = view.findViewById(R.id.pharma_name);
            this.pharmaAddress = view.findViewById(R.id.pharma_address);
            this.pharmaPhone = view.findViewById(R.id.pharma_phone);
            this.phoneButton = view.findViewById(R.id.phoneButton);
            this.locationButton = view.findViewById(R.id.locationButton);
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

// Stackoverflow. How do I get the dialer to open with phone number displayed? Geraadpleegd via
// https://stackoverflow.com/questions/11699819/how-do-i-get-the-dialer-to-open-with-phone-number-displayed
// Geraadpleegd op 23 juni 2020

// Stackoverflow. Launching Google Maps Directions via an intent on Android. Geraadpleegd via
// https://stackoverflow.com/questions/2662531/launching-google-maps-directions-via-an-intent-on-android
// Geraadpleegd op 23 juni 2020