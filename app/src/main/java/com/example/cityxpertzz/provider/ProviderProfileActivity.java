// app/src/main/java/com/example/cityxpertzz/provider/ProviderProfileActivity.java
package com.example.cityxpertzz.provider;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cityxpertzz.R;
import com.example.cityxpertzz.models.UserModel;
import com.example.cityxpertzz.utils.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ProviderProfileActivity extends AppCompatActivity {

    EditText etName, etPhone;
    TextView tvApproved;
    Button btnUpdate;
    DatabaseReference ref;
    String uid;
    UserModel provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_profile);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        tvApproved = findViewById(R.id.tvApproved);
        btnUpdate = findViewById(R.id.btnUpdate);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref = FirebaseDatabase.getInstance().getReference(FirebasePaths.USERS).child(uid);

        loadProfile();

        btnUpdate.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            ref.child("name").setValue(name);
            ref.child("phone").setValue(phone);
            Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadProfile() {
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                provider = task.getResult().getValue(UserModel.class);
                if (provider != null) {
                    etName.setText(provider.name);
                    etPhone.setText(provider.phone);
                    tvApproved.setText(provider.approved ? "Status: Approved ✅" : "Status: Pending ⏳");
                }
            } else {
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
