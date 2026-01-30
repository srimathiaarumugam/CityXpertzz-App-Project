// app/src/main/java/com/example/cityxpertzz/auth/PasswordResetActivity.java
package com.example.cityxpertzz.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cityxpertzz.R;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {
    private EditText etEmail;
    private Button btnReset;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        etEmail = findViewById(R.id.etEmail);
        btnReset = findViewById(R.id.btnReset);
        mAuth = FirebaseAuth.getInstance();

        btnReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) { Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show(); return; }
            btnReset.setEnabled(false);
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                btnReset.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(PasswordResetActivity.this, "Reset email sent. Check your inbox.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(PasswordResetActivity.this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}
