package com.example.cityxpertzz.provider;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityxpertzz.R;
import com.example.cityxpertzz.adapters.BookingAdapter;
import com.example.cityxpertzz.models.BookingModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IncomingBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerBookings;
    private BookingAdapter adapter;
    private List<BookingModel> bookings = new ArrayList<>();
    private DatabaseReference bookingsRef;
    private String providerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incoming_bookings);

        recyclerBookings = findViewById(R.id.rvBookings);
        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));

        providerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");

        adapter = new BookingAdapter(this, bookings, (booking, newStatus) -> {
            FirebaseDatabase.getInstance().getReference("Bookings")
                    .child(booking.bookingId)
                    .child("status")
                    .setValue(newStatus)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(this, "Booking marked " + newStatus, Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }, "provider");

        recyclerBookings.setAdapter(adapter);

        loadBookings();
    }

    private void loadBookings() {
        bookingsRef.orderByChild("providerId").equalTo(providerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookings.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            BookingModel b = snap.getValue(BookingModel.class);
                            if (b != null) bookings.add(b);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(IncomingBookingsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
