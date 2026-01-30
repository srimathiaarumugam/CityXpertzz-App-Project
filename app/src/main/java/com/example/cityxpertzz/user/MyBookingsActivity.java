package com.example.cityxpertzz.user;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class MyBookingsActivity extends AppCompatActivity {

    RecyclerView rvBookings;
    List<BookingModel> bookingList = new ArrayList<>();
    BookingAdapter adapter;
    DatabaseReference bookingRef;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        rvBookings = findViewById(R.id.rvBookings);
        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bookingRef = FirebaseDatabase.getInstance().getReference(FirebasePaths.BOOKINGS);

        adapter =  new BookingAdapter(this, bookingList, null, "user");rvBookings.setAdapter(adapter);
        loadBookings();
    }

    private void loadBookings() {
        bookingRef.orderByChild("userId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookingList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            BookingModel b = snap.getValue(BookingModel.class);
                            if (b != null) bookingList.add(b);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MyBookingsActivity.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Optional: call provider
    private void callProvider(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}
