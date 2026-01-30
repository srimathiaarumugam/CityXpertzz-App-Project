package com.example.cityxpertzz.admin;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityxpertzz.R;
import com.example.cityxpertzz.adapters.ServiceAdapter;
import com.example.cityxpertzz.models.ServiceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewServicesActivity extends AppCompatActivity {

    RecyclerView rvServices;
    List<ServiceModel> allServices = new ArrayList<>();
    DatabaseReference servicesRef;
    ServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_services);

        rvServices = findViewById(R.id.rvServices);
        rvServices.setLayoutManager(new LinearLayoutManager(this));

        // Firebase → Services node
        servicesRef = FirebaseDatabase.getInstance().getReference("Services");

        adapter = new ServiceAdapter(this, allServices, service -> {
            if (service.serviceId != null) {
                servicesRef.child(service.serviceId)
                        .removeValue()
                        .addOnSuccessListener(a ->
                                Toast.makeText(this, "Service removed.", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        rvServices.setAdapter(adapter);
        loadServices();
    }

    private void loadServices() {
        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allServices.clear();

                if (!snapshot.exists()) {
                    Toast.makeText(ViewServicesActivity.this,
                            "No services found in database!",
                            Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    return;
                }

                for (DataSnapshot snap : snapshot.getChildren()) {
                    try {
                        ServiceModel s = snap.getValue(ServiceModel.class);

                        // Validate service (avoid null crashes)
                        if (s != null && s.serviceId != null) {
                            allServices.add(s);
                        }
                    } catch (Exception e) {
                        Log.e("SERVICE_PARSE", "Error reading service: " + e.getMessage());
                    }
                }

                adapter.notifyDataSetChanged();

                if (allServices.isEmpty()) {
                    Toast.makeText(ViewServicesActivity.this,
                            "No services available.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewServicesActivity.this,
                        "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
