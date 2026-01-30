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
import com.example.cityxpertzz.models.ProviderModel;
import java.util.List;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ProviderViewHolder> {

    private Context context;
    private List<ProviderModel> providerList;
    private OnActionListener listener;

    public interface OnActionListener {
        void onApprove(ProviderModel provider);
        void onReject(ProviderModel provider);
    }

    public ProviderAdapter(Context context, List<ProviderModel> providerList, OnActionListener listener) {
        this.context = context;
        this.providerList = providerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProviderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_provider, parent, false);
        return new ProviderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderViewHolder holder, int position) {
        ProviderModel p = providerList.get(position);
        holder.tvName.setText(p.name);
        holder.tvCategory.setText("Category: " + p.category);
        holder.tvPhone.setText("Phone: " + p.phone);
        holder.tvStatus.setText(p.approved ? "Approved ✅" : "Pending ⏳");

        if (!p.approved) {
            holder.btnApprove.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
        } else {
            holder.btnApprove.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);
        }

        holder.btnApprove.setOnClickListener(v -> {
            Toast.makeText(context, "Approved " + p.name, Toast.LENGTH_SHORT).show();
            if (listener != null) listener.onApprove(p);
        });

        holder.btnReject.setOnClickListener(v -> {
            Toast.makeText(context, "Rejected " + p.name, Toast.LENGTH_SHORT).show();
            if (listener != null) listener.onReject(p);
        });
    }

    @Override
    public int getItemCount() {
        return providerList != null ? providerList.size() : 0;
    }

    public static class ProviderViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCategory, tvPhone, tvStatus;
        Button btnApprove, btnReject;

        public ProviderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
