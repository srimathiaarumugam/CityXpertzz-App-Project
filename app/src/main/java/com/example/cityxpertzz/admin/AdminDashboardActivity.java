package com.example.cityxpertzz.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cityxpertzz.R;
import com.example.cityxpertzz.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    Button btnApproveProviders, btnViewProviders, btnViewServices, btnViewBookings, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnApproveProviders = findViewById(R.id.btnApproveProviders);
        btnViewProviders = findViewById(R.id.btnViewProviders);
        btnViewServices = findViewById(R.id.btnViewServices);
        btnViewBookings = findViewById(R.id.btnViewBookings);
        btnLogout = findViewById(R.id.btnLogout);

        btnApproveProviders.setOnClickListener(v -> {
            Toast.makeText(this, "Opening pending providers...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ApproveProvidersActivity.class));
        });

        btnViewProviders.setOnClickListener(v -> {
            Toast.makeText(this, "Opening approved providers...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ViewProvidersActivity.class));
        });

        btnViewServices.setOnClickListener(v -> {
            Toast.makeText(this, "Opening all services...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ViewServicesActivity.class));
        });

        btnViewBookings.setOnClickListener(v -> {
            Toast.makeText(this, "Opening all bookings...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ViewBookingsActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Admin logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
