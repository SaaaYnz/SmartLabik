package com.example.smartlabactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PageAnaliseActivity extends AppCompatActivity {

    private TextView tvUserEmail;
    private Button btnLogout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_analise);

        tvUserEmail = findViewById(R.id.tvUserEmail);
        btnLogout = findViewById(R.id.btnLogout);

        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        String userEmail = sharedPreferences.getString("user_email", "Неизвестно");
        tvUserEmail.setText("Вы вошли как: " + userEmail);

        btnLogout.setOnClickListener(v -> logout());
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(PageAnaliseActivity.this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        Toast.makeText(this, "Вы вышли из системы", Toast.LENGTH_SHORT).show();
    }
}