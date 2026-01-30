package com.example.cityxpertzz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 10000; // 2 seconds in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Delay splash screen
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            // Always redirect to LoginActivity
            Intent intent = new Intent(MainActivity.this, com.example.cityxpertzz.auth.LoginActivity.class);
            startActivity(intent);
            finish(); // close splash screen
        }, SPLASH_DELAY);
    }
}
