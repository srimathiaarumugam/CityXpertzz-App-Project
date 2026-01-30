package com.example.cityxpertzz.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cityxpertzz.R;
import com.example.cityxpertzz.adapters.ServiceAdapter;
import com.example.cityxpertzz.models.ServiceModel;
import com.example.cityxpertzz.utils.FirebasePaths;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class BrowseServicesActivity extends AppCompatActivity {

    RecyclerView rvServices;
    List<ServiceModel> serviceList = new ArrayList<>();
    ServiceAdapter adapter;
    DatabaseReference serviceRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_services);

        rvServices = findViewById(R.id.rvServices);
        rvServices.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ServiceAdapter(this, serviceList, service -> {
            Intent i = new Intent(this, ServiceDetailsActivity.class);
            i.putExtra("serviceId", service.serviceId);
            startActivity(i);
        });
        rvServices.setAdapter(adapter);

        serviceRef = FirebaseDatabase.getInstance().getReference(FirebasePaths.SERVICES);
        loadServices();
    }

    private void loadServices() {
        serviceRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                serviceList.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    ServiceModel s = snap.getValue(ServiceModel.class);
                    if (s != null && s.available)
                        serviceList.add(s);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BrowseServicesActivity.this, "Failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
