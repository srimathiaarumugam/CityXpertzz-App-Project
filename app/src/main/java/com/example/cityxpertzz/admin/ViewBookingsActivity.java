package com.example.cityxpertzz.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityxpertzz.R;
import com.example.cityxpertzz.adapters.BookingAdapter;
import com.example.cityxpertzz.models.BookingModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerBookings;
    private BookingAdapter adapter;
    private List<BookingModel> bookingList = new ArrayList<>();
    private DatabaseReference bookingsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bookings);

        recyclerBookings = findViewById(R.id.rvBookings);
        recyclerBookings.setLayoutManager(new LinearLayoutManager(this));

        bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");

        adapter = new BookingAdapter(this, bookingList, null, "admin");
        recyclerBookings.setAdapter(adapter);

        loadAllBookings();
    }

    private void loadAllBookings() {
        bookingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookingList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    BookingModel booking = snap.getValue(BookingModel.class);
                    if (booking != null) bookingList.add(booking);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewBookingsActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
