package com.example.cityxpertzz.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cityxpertzz.R;
import com.example.cityxpertzz.user.UserDashboardActivity;
import com.example.cityxpertzz.provider.ProviderDashboardActivity;
import com.example.cityxpertzz.models.UserModel;
import com.example.cityxpertzz.utils.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnAdminLogin;
    private TextView tvSignup, tvForgot;

    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        tvSignup = findViewById(R.id.tvSignup);
        tvForgot = findViewById(R.id.tvForgot);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        btnLogin.setOnClickListener(v -> loginUser());
        btnAdminLogin.setOnClickListener(v -> startActivity(new Intent(this, AdminLoginActivity.class)));
        tvSignup.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
        tvForgot.setOnClickListener(v -> startActivity(new Intent(this, PasswordResetActivity.class)));
    }

    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            btnLogin.setEnabled(true);

            if (!task.isSuccessful()) {
                Toast.makeText(this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            String uid = auth.getCurrentUser().getUid();

            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snap) {

                    if (!snap.exists()) {
                        Toast.makeText(LoginActivity.this, "User record missing in database!", Toast.LENGTH_LONG).show();
                        auth.signOut();
                        return;
                    }

                    // Safe parsing — prevents Long/String mismatches
                    String name = safeString(snap.child("name").getValue());
                    String email = safeString(snap.child("email").getValue());
                    String phone = safeString(snap.child("phone").getValue());
                    String userType = safeString(snap.child("userType").getValue());
                    boolean approved = safeBoolean(snap.child("approved").getValue());

                    if (userType.equals("provider") && !approved) {
                        Toast.makeText(LoginActivity.this, "Provider not approved by Admin.", Toast.LENGTH_LONG).show();
                        auth.signOut();
                        return;
                    }

                    if (userType.equals("admin")) {
                        Toast.makeText(LoginActivity.this, "Use *Admin Login* button.", Toast.LENGTH_SHORT).show();
                        auth.signOut();
                        return;
                    }

                    // User login success
                    if (userType.equals("provider")) {
                        Intent i = new Intent(LoginActivity.this, ProviderDashboardActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(LoginActivity.this, UserDashboardActivity.class);
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "DB Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // --- SAFE CONVERTERS ---
    private String safeString(Object obj) {
        if (obj == null) return "";
        return String.valueOf(obj);
    }

    private boolean safeBoolean(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) return (Boolean) obj;
        return obj.toString().equalsIgnoreCase("true");
    }
}
