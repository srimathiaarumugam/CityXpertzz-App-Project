package com.example.cityxpertzz.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityxpertzz.R;
import com.example.cityxpertzz.models.BookingModel;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.VH> {

    public interface OnStatusActionListener {
        void onUpdateStatus(BookingModel booking, String newStatus);
    }

    private final Context context;
    private final List<BookingModel> list;
    private final OnStatusActionListener listener;
    private final String userRole; // "user", "provider", "admin"

    public BookingAdapter(Context context, List<BookingModel> list, OnStatusActionListener listener, String userRole) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.userRole = userRole;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        BookingModel b = list.get(pos);

        h.tvService.setText(b.serviceTitle);
        h.tvProvider.setText("Provider: " + b.providerName);
        h.tvUser.setText("User: " + b.userName);
        h.tvPrice.setText("₹ " + b.price);
        h.tvStatus.setText("Status: " + b.status);
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a", java.util.Locale.getDefault());
        String dateStr = sdf.format(new java.util.Date(b.timestamp));
        h.tvDate.setText("Booked on: " + dateStr);


        // Hide all buttons by default
        h.btnAccept.setVisibility(View.GONE);
        h.btnComplete.setVisibility(View.GONE);
        h.btnContact.setVisibility(View.GONE);

        // USER — only contact provider
        if (userRole.equals("user")) {
            h.btnContact.setVisibility(View.VISIBLE);
            h.btnContact.setText("Contact Provider");
            h.btnContact.setOnClickListener(v -> {
                if (b.providerPhone != null && !b.providerPhone.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + b.providerPhone));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Provider contact not available", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // PROVIDER — can contact user + update status
        else if (userRole.equals("provider")) {
            h.btnContact.setVisibility(View.VISIBLE);
            h.btnAccept.setVisibility(View.VISIBLE);
            h.btnComplete.setVisibility(View.VISIBLE);
            h.btnContact.setText("Contact User");

            h.btnContact.setOnClickListener(v -> {
                if (b.userPhone != null && !b.userPhone.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + b.userPhone));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "User contact not available", Toast.LENGTH_SHORT).show();
                }
            });

            h.btnAccept.setOnClickListener(v -> listener.onUpdateStatus(b, "Accepted"));
            h.btnComplete.setOnClickListener(v -> listener.onUpdateStatus(b, "Completed"));
        }

        // ADMIN — view everything, no actions
        else if (userRole.equals("admin")) {
            // Buttons remain hidden — admin can just see all info and live status
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvService, tvProvider, tvUser, tvPrice, tvStatus, tvDate;
        Button btnAccept, btnComplete, btnContact;

        VH(@NonNull View v) {
            super(v);
            tvService = v.findViewById(R.id.tvService);
            tvProvider = v.findViewById(R.id.tvProvider);
            tvUser = v.findViewById(R.id.tvUser);
            tvPrice = v.findViewById(R.id.tvPrice);
            tvStatus = v.findViewById(R.id.tvStatus);
            tvDate = v.findViewById(R.id.tvDate);
            btnAccept = v.findViewById(R.id.btnAccept);
            btnComplete = v.findViewById(R.id.btnComplete);
            btnContact = v.findViewById(R.id.btnContact);
        }
    }
}
