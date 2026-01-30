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
import com.example.cityxpertzz.utils.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProviderBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerBookings;
    private BookingAdapter bookingAdapter;
    private List<BookingModel> bookingList = new ArrayList<>();
    private DatabaseReference bookingRef;
    private String providerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_bookings);

        recyclerBookings = findViewById(R.id.recyclerBookings);
        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));

        providerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bookingRef = FirebaseDatabase.getInstance().getReference(FirebasePaths.BOOKINGS);

        bookingAdapter = new BookingAdapter(this, bookingList, (booking, newStatus) -> {
            FirebaseDatabase.getInstance().getReference("Bookings")
                    .child(booking.bookingId)
                    .child("status")
                    .setValue(newStatus);
            Toast.makeText(this, "Status updated to " + newStatus, Toast.LENGTH_SHORT).show();
        }, "provider");


        recyclerBookings.setAdapter(bookingAdapter);

        loadBookings();
    }

    private void loadBookings() {
        bookingRef.orderByChild("providerId").equalTo(providerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookingList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            BookingModel booking = snap.getValue(BookingModel.class);
                            if (booking != null) {
                                bookingList.add(booking);
                            }
                        }
                        bookingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProviderBookingsActivity.this,
                                "Error: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
