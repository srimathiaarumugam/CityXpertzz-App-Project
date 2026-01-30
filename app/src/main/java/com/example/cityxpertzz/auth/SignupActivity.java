// app/src/main/java/com/example/cityxpertzz/auth/SignupActivity.java
package com.example.cityxpertzz.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.cityxpertzz.R;
import com.example.cityxpertzz.models.UserModel;
import com.example.cityxpertzz.utils.FirebasePaths;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etPhone, etEmail, etPassword;
    private RadioGroup rgRole;
    private RadioButton rbUser, rbProvider;
    private Button btnSignup;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        rbUser = findViewById(R.id.rbUser);
        rbProvider = findViewById(R.id.rbProvider);
        btnSignup = findViewById(R.id.btnSignup);

        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference(FirebasePaths.USERS);

        btnSignup.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) { Toast.makeText(this, "Enter name", Toast.LENGTH_SHORT).show(); return; }
        if (TextUtils.isEmpty(phone)) { Toast.makeText(this, "Enter phone", Toast.LENGTH_SHORT).show(); return; }
        if (TextUtils.isEmpty(email)) { Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show(); return; }
        if (TextUtils.isEmpty(pass) || pass.length() < 6) { Toast.makeText(this, "Enter password (min 6 chars)", Toast.LENGTH_SHORT).show(); return; }

        String userType = rbProvider.isChecked() ? "provider" : "user";
        boolean approved = !"provider".equals(userType); // users are approved by default; providers require admin approval

        btnSignup.setEnabled(false);
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    btnSignup.setEnabled(true);
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        UserModel user = new UserModel(uid, name, email, phone, userType, approved);
                        usersRef.child(uid).setValue(user).addOnCompleteListener(dbTask -> {
                            if (dbTask.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "Signup success. Please login.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SignupActivity.this, "Signup failed: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(SignupActivity.this, "Auth failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
