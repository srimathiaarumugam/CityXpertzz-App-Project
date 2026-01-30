package com.example.cityxpertzz.provider;

import android.widget.Toast;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cityxpertzz.R;
import com.example.cityxpertzz.adapters.ServiceAdapter;
import com.example.cityxpertzz.models.ServiceModel;
import com.example.cityxpertzz.utils.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class ManageServicesActivity extends AppCompatActivity {

    RecyclerView rvServices;
    DatabaseReference serviceRef;
    List<ServiceModel> myServices = new ArrayList<>();
    ServiceAdapter adapter;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_services);

        rvServices = findViewById(R.id.rvServices);
        rvServices.setLayoutManager(new LinearLayoutManager(this));

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        serviceRef = FirebaseDatabase.getInstance().getReference(FirebasePaths.SERVICES);

        adapter = new ServiceAdapter(this, myServices, service -> {
            serviceRef.child(service.serviceId).removeValue();
            Toast.makeText(this, "Service removed", Toast.LENGTH_SHORT).show();
        });

        rvServices.setAdapter(adapter);
        loadMyServices();
    }

    private void loadMyServices() {
        serviceRef.orderByChild("providerId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        myServices.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            ServiceModel s = snap.getValue(ServiceModel.class);
                            if (s != null) myServices.add(s);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ManageServicesActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
