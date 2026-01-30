package com.example.cityxpertzz.provider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cityxpertzz.R;
import com.example.cityxpertzz.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProviderDashboardActivity extends AppCompatActivity {

    Button btnAddService, btnManageServices, btnBookings, btnProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_dashboard);

        btnAddService = findViewById(R.id.btnAddService);
        btnManageServices = findViewById(R.id.btnManageServices);
        btnBookings = findViewById(R.id.btnBookings);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);

        btnAddService.setOnClickListener(v -> startActivity(new Intent(this, AddServiceActivity.class)));
        btnManageServices.setOnClickListener(v -> startActivity(new Intent(this, ManageServicesActivity.class)));
        btnBookings.setOnClickListener(v -> startActivity(new Intent(this, IncomingBookingsActivity.class)));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, ProviderProfileActivity.class)));

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
