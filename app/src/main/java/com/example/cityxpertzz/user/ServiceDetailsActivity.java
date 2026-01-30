package com.example.cityxpertzz.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cityxpertzz.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceDetailsActivity extends AppCompatActivity {

    private TextView tvServiceTitle, tvServiceDesc, tvServicePrice, tvProviderName, tvProviderPhone;
    private Button btnBookNow;

    private String serviceId, providerId, serviceTitle, serviceDescription, providerName, providerPhone;
    private double price;

    private DatabaseReference servicesRef, providersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details);

        // Initialize UI
        tvServiceTitle = findViewById(R.id.tvServiceTitle);
        tvServiceDesc = findViewById(R.id.tvServiceDesc);
        tvServicePrice = findViewById(R.id.tvServicePrice);
        tvProviderName = findViewById(R.id.tvProviderName);
        tvProviderPhone = findViewById(R.id.tvProviderPhone);
        btnBookNow = findViewById(R.id.btnBookNow);

        // Get serviceId from previous screen
        serviceId = getIntent().getStringExtra("serviceId");

        if (serviceId == null) {
            Toast.makeText(this, "Error: Missing service ID", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        servicesRef = FirebaseDatabase.getInstance().getReference("Services");
        providersRef = FirebaseDatabase.getInstance().getReference("Providers"); // ✅ fixed reference

        loadServiceDetails();

        btnBookNow.setOnClickListener(v -> openBookingPage());
    }

    private void loadServiceDetails() {
        servicesRef.child(serviceId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(ServiceDetailsActivity.this, "Service not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                serviceTitle = snapshot.child("title").getValue(String.class);
                serviceDescription = snapshot.child("description").getValue(String.class);
                providerId = snapshot.child("providerId").getValue(String.class);
                price = snapshot.child("price").getValue(Double.class) != null
                        ? snapshot.child("price").getValue(Double.class) : 0.0;

                tvServiceTitle.setText(serviceTitle != null ? serviceTitle : "Service");
                tvServiceDesc.setText(serviceDescription != null ? serviceDescription : "No description");
                tvServicePrice.setText("Price: ₹" + price);

                if (providerId != null) {
                    loadProviderDetails(providerId);
                } else {
                    tvProviderName.setText("Provider: Unknown");
                    tvProviderPhone.setText("Phone: N/A");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ServiceDetailsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadProviderDetails(String providerId) {
        providersRef.child(providerId).addListenerForSingleValueEvent(new ValueEventListener() { // ✅ fixed
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    providerName = snapshot.child("name").getValue(String.class);
                    providerPhone = snapshot.child("phone").getValue(String.class);

                    tvProviderName.setText("Provider: " + (providerName != null ? providerName : "N/A"));
                    tvProviderPhone.setText("Phone: " + (providerPhone != null ? providerPhone : "N/A"));
                } else {
                    tvProviderName.setText("Provider: Unknown");
                    tvProviderPhone.setText("Phone: N/A");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ServiceDetailsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openBookingPage() {
        if (providerId == null || serviceId == null) {
            Toast.makeText(this, "Service details not loaded yet!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ServiceDetailsActivity.this, BookServiceActivity.class);
        intent.putExtra("serviceId", serviceId);
        intent.putExtra("serviceTitle", serviceTitle);
        intent.putExtra("providerId", providerId);
        intent.putExtra("providerName", providerName);
        intent.putExtra("servicePrice", price);
        startActivity(intent);
    }
}
