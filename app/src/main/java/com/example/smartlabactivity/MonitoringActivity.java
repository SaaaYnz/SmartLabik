package com.example.smartlabactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MonitoringActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
    }
    public void onclick(View v) {
        Intent i = new Intent(MonitoringActivity.this, AnaliseActivity.class);
        startActivity(i);
    }

    public void finish(View v) {
        Intent i = new Intent(MonitoringActivity.this, RegistrationActivity.class);
        startActivity(i);
    }
}