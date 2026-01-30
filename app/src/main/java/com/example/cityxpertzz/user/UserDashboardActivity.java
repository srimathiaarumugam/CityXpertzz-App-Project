package com.example.cityxpertzz.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cityxpertzz.R;
import com.example.cityxpertzz.auth.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class UserDashboardActivity extends AppCompatActivity {

    Button btnBrowse, btnBookings, btnProfile, btnContact, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        btnBrowse = findViewById(R.id.btnBrowse);
        btnBookings = findViewById(R.id.btnBookings);
        btnProfile = findViewById(R.id.btnProfile);
        btnContact = findViewById(R.id.btnContact);
        btnLogout = findViewById(R.id.btnLogout);

        btnBrowse.setOnClickListener(v -> {
            startActivity(new Intent(this, BrowseServicesActivity.class));
        });

        btnBookings.setOnClickListener(v -> {
            startActivity(new Intent(this, MyBookingsActivity.class));
        });

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, UserProfileActivity.class));
        });

        btnContact.setOnClickListener(v -> {
            startActivity(new Intent(this, MyBookingsActivity.class));
            Toast.makeText(this, "You can contact providers from your bookings list.", Toast.LENGTH_LONG).show();
        });

        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
