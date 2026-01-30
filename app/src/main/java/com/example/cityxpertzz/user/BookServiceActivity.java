package com.example.cityxpertzz.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cityxpertzz.R;
import com.example.cityxpertzz.models.BookingModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookServiceActivity extends AppCompatActivity {

    private TextView tvServiceTitle, tvProviderName, tvServicePrice;
    private Button btnBookService;

    private String serviceId, serviceTitle, providerId, providerName;
    private double price;

    private FirebaseAuth auth;
    private DatabaseReference usersRef, bookingsRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service);

        // Initialize UI
        tvServiceTitle = findViewById(R.id.tvServiceTitle);
        tvProviderName = findViewById(R.id.tvProviderName);
        tvServicePrice = findViewById(R.id.tvServicePrice);
        btnBookService = findViewById(R.id.btnBookService);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");

        // Setup progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Booking your service...");
        progressDialog.setCancelable(false);

        // Get data from intent
        Intent intent = getIntent();
        serviceId = intent.getStringExtra("serviceId");
        serviceTitle = intent.getStringExtra("serviceTitle");
        providerId = intent.getStringExtra("providerId");
        providerName = intent.getStringExtra("providerName");
        price = intent.getDoubleExtra("servicePrice", 0.0);

        Log.d("BOOK_DEBUG", "ServiceId=" + serviceId + ", ProviderId=" + providerId);

        // Display data safely
        tvServiceTitle.setText(serviceTitle != null ? serviceTitle : "Unknown Service");
        tvProviderName.setText("Provider: " + (providerName != null ? providerName : "Unknown"));
        tvServicePrice.setText("Price: ₹" + price);

        btnBookService.setOnClickListener(v -> createBooking());
    }

    private void createBooking() {
        try {
            progressDialog.show();

            // Check login
            if (auth.getCurrentUser() == null) {
                progressDialog.dismiss();
                Toast.makeText(this, "You must be logged in to book a service.", Toast.LENGTH_LONG).show();
                return;
            }

            String currentUserId = auth.getCurrentUser().getUid();

            // Validate data
            if (providerId == null || providerId.trim().isEmpty() || serviceId == null || serviceId.trim().isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(this, "Error: Missing service/provider info!", Toast.LENGTH_LONG).show();
                return;
            }

            // 🔍 Fetch provider details
            DatabaseReference providerRef = usersRef.child(providerId.trim());
            providerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot providerSnap) {
                    if (!providerSnap.exists()) {
                        progressDialog.dismiss();
                        Toast.makeText(BookServiceActivity.this,
                                "Provider record not found. Please contact admin.\n(" + providerId + ")",
                                Toast.LENGTH_LONG).show();
                        Log.e("BOOK_ERROR", "Provider not found for ID: " + providerId);
                        return;
                    }

                    String providerPhone = providerSnap.child("phone").getValue(String.class);
                    String providerName = providerSnap.child("name").getValue(String.class);

                    // 🔍 Fetch user details
                    DatabaseReference userRef = usersRef.child(currentUserId);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnap) {
                            if (!userSnap.exists()) {
                                progressDialog.dismiss();
                                Toast.makeText(BookServiceActivity.this, "User not found!", Toast.LENGTH_LONG).show();
                                return;
                            }

                            String userName = userSnap.child("name").getValue(String.class);
                            String userPhone = userSnap.child("phone").getValue(String.class);

                            String bookingId = bookingsRef.push().getKey();
                            long timestamp = System.currentTimeMillis();

                            BookingModel booking = new BookingModel(
                                    bookingId,
                                    currentUserId,
                                    userName,
                                    providerId,
                                    providerName,
                                    serviceId,
                                    serviceTitle,
                                    price,
                                    "Pending",
                                    timestamp,
                                    userPhone,
                                    providerPhone
                            );

                            // ✅ Save booking to Firebase
                            bookingsRef.child(bookingId).setValue(booking)
                                    .addOnSuccessListener(aVoid -> {
                                        progressDialog.dismiss();
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
                                        String dateStr = sdf.format(new Date(timestamp));
                                        Toast.makeText(BookServiceActivity.this,
                                                "✅ Service booked successfully on " + dateStr,
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        progressDialog.dismiss();
                                        Toast.makeText(BookServiceActivity.this,
                                                "Booking failed: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(BookServiceActivity.this,
                                    "User fetch failed: " + error.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Toast.makeText(BookServiceActivity.this,
                            "Provider fetch failed: " + error.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("BOOK_ERROR", "Exception", e);
        }
    }
}
