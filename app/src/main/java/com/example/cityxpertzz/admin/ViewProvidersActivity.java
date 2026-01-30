package com.example.cityxpertzz.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityxpertzz.R;
import com.example.cityxpertzz.models.ProviderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewProvidersActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ArrayList<ProviderModel> approvedList;
    private ApproveProvidersActivity.SimpleUserProviderAdapter adapter;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_providers);

        rv = findViewById(R.id.rvProviders);
        rv.setLayoutManager(new LinearLayoutManager(this));

        approvedList = new ArrayList<>();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        adapter = new ApproveProvidersActivity.SimpleUserProviderAdapter(this, approvedList, false);
        rv.setAdapter(adapter);

        loadApprovedProviders();
    }

    private void loadApprovedProviders() {
        usersRef.orderByChild("userType").equalTo("provider")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        approvedList.clear();
                        for (DataSnapshot child : snapshot.getChildren()) {
                            ProviderModel provider = child.getValue(ProviderModel.class);
                            if (provider != null && provider.approved) {
                                approvedList.add(provider);
                            }
                        }
                        adapter.notifyDataSetChanged();

                        if (approvedList.isEmpty()) {
                            Toast.makeText(ViewProvidersActivity.this, "No approved providers yet.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ViewProvidersActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
