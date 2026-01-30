package com.example.cityxpertzz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityxpertzz.R;
import com.example.cityxpertzz.models.UserModel;
import com.example.cityxpertzz.utils.FirebasePaths;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class SimpleProviderListAdapter extends RecyclerView.Adapter<SimpleProviderListAdapter.VH> {

    private final Context context;
    private final List<UserModel> list;
    private final boolean showButtons;
    private final DatabaseReference usersRef;

    public SimpleProviderListAdapter(Context context, List<UserModel> list, boolean showButtons) {
        this.context = context;
        this.list = list;
        this.showButtons = showButtons;
        this.usersRef = FirebaseDatabase.getInstance().getReference(FirebasePaths.USERS);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_provider, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        UserModel u = list.get(pos);
        h.tvName.setText(u.name);
        h.tvCategory.setText("Email: " + u.email);
        h.tvPhone.setText("Phone: " + u.phone);
        h.tvStatus.setText(u.approved ? "Approved ✅" : "Pending ⏳");

        if (showButtons) {
            h.btnApprove.setVisibility(View.VISIBLE);
            h.btnReject.setVisibility(View.VISIBLE);

            h.btnApprove.setOnClickListener(v -> {
                usersRef.child(u.uid).child("approved").setValue(true)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Approved " + u.name, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            });

            h.btnReject.setOnClickListener(v -> {
                usersRef.child(u.uid).removeValue()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Rejected " + u.name, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            });

        } else {
            h.btnApprove.setVisibility(View.GONE);
            h.btnReject.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPhone, tvStatus;
        Button btnApprove, btnReject;

        public VH(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvCategory = v.findViewById(R.id.tvCategory);
            tvPhone = v.findViewById(R.id.tvPhone);
            tvStatus = v.findViewById(R.id.tvStatus);
            btnApprove = v.findViewById(R.id.btnApprove);
            btnReject = v.findViewById(R.id.btnReject);
        }
    }
}
