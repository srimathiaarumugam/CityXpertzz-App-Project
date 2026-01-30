package com.example.cityxpertzz.provider;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cityxpertzz.R;
import com.example.cityxpertzz.models.ServiceModel;
import com.example.cityxpertzz.models.UserModel;
import com.example.cityxpertzz.utils.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.UUID;

public class AddServiceActivity extends AppCompatActivity {

    EditText etTitle, etCategory, etDescription, etPrice;
    Button btnAdd;
    DatabaseReference userRef, serviceRef;
    UserModel provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        etTitle = findViewById(R.id.etTitle);
        etCategory = findViewById(R.id.etCategory);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        btnAdd = findViewById(R.id.btnAdd);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference(FirebasePaths.USERS).child(uid);
        serviceRef = FirebaseDatabase.getInstance().getReference(FirebasePaths.SERVICES);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists())
                provider = task.getResult().getValue(UserModel.class);
        });

        btnAdd.setOnClickListener(v -> addService());
    }

    private void addService() {
        String title = etTitle.getText().toString().trim();
        String cat = etCategory.getText().toString().trim();
        String desc = etDescription.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(cat) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String id = UUID.randomUUID().toString();

        if (provider == null) {
            Toast.makeText(this, "Provider data not loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        ServiceModel s = new ServiceModel(
                id, provider.uid, provider.name, cat, title, desc, price, "", true
        );

        serviceRef.child(id).setValue(s).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Service added successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
