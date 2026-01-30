package com.example.cityxpertzz.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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

public class ApproveProvidersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference usersRef;
    private ArrayList<ProviderModel> providerList;
    private SimpleUserProviderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_providers);

        recyclerView = findViewById(R.id.rvProviders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        providerList = new ArrayList<>();
        usersRef = FirebaseDatabase.getInstance().getReference("Users"); // ✅ Correct node

        adapter = new SimpleUserProviderAdapter(this, providerList, true);
        recyclerView.setAdapter(adapter);

        loadPendingProviders();
    }

    private void loadPendingProviders() {
        usersRef.orderByChild("userType").equalTo("provider")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        providerList.clear();

                        for (DataSnapshot child : snapshot.getChildren()) {
                            ProviderModel provider = child.getValue(ProviderModel.class);
                            if (provider != null && !provider.approved) {
                                providerList.add(provider);
                            }
                        }

                        adapter.notifyDataSetChanged();

                        if (providerList.isEmpty()) {
                            Toast.makeText(ApproveProvidersActivity.this, "No pending providers.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ApproveProvidersActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Adapter for listing and approving providers
    public static class SimpleUserProviderAdapter extends RecyclerView.Adapter<SimpleUserProviderAdapter.VH> {

        private final ArrayList<ProviderModel> list;
        private final DatabaseReference usersRef;
        private final boolean isApprovalMode;
        private final android.content.Context context;

        public SimpleUserProviderAdapter(android.content.Context ctx, ArrayList<ProviderModel> list, boolean approvalMode) {
            this.context = ctx;
            this.list = list;
            this.isApprovalMode = approvalMode;
            this.usersRef = FirebaseDatabase.getInstance().getReference("Users");
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            android.view.View v = android.view.LayoutInflater.from(context)
                    .inflate(R.layout.item_provider, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int pos) {
            ProviderModel p = list.get(pos);
            h.tvName.setText(p.name);
            h.tvEmail.setText(p.email);
            h.tvPhone.setText(p.phone);
            h.tvStatus.setText(p.approved ? "Approved ✅" : "Pending ⏳");

            if (isApprovalMode) {
                h.btnApprove.setVisibility(android.view.View.VISIBLE);
                h.btnApprove.setOnClickListener(v -> {
                    if (p.uid != null) {
                        usersRef.child(p.uid).child("approved").setValue(true)
                                .addOnSuccessListener(a ->
                                        Toast.makeText(context, "Approved " + p.name, Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }
                });
            } else {
                h.btnApprove.setVisibility(android.view.View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvEmail, tvPhone, tvStatus;
            Button btnApprove;
            public VH(@NonNull android.view.View v) {
                super(v);
                tvName = v.findViewById(R.id.tvName);
                tvEmail = v.findViewById(R.id.tvEmail);
                tvPhone = v.findViewById(R.id.tvPhone);
                tvStatus = v.findViewById(R.id.tvStatus);
                btnApprove = v.findViewById(R.id.btnApprove);
            }
        }
    }
}
