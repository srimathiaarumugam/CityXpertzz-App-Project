package com.example.cityxpertzz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cityxpertzz.R;
import com.example.cityxpertzz.models.ServiceModel;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private Context context;
    private List<ServiceModel> serviceList;
    private OnServiceClickListener listener;

    public interface OnServiceClickListener {
        void onServiceClick(ServiceModel service);
    }

    public ServiceAdapter(Context context, List<ServiceModel> serviceList, OnServiceClickListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceModel s = serviceList.get(position);
        holder.tvTitle.setText(s.title);
        holder.tvCategory.setText("Category: " + s.category);
        holder.tvProvider.setText("By: " + s.providerName);
        holder.tvPrice.setText("₹ " + s.price);

        if (s.imageUrl != null && !s.imageUrl.isEmpty()) {
            Glide.with(context).load(s.imageUrl).placeholder(R.drawable.ic_service_placeholder).into(holder.ivImage);
        }

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Selected: " + s.title, Toast.LENGTH_SHORT).show();
            if (listener != null) listener.onServiceClick(s);
        });
    }

    @Override
    public int getItemCount() {
        return serviceList != null ? serviceList.size() : 0;
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvProvider, tvPrice;
        ImageView ivImage;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvProvider = itemView.findViewById(R.id.tvProvider);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
