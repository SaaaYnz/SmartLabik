package com.example.smartlabactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private static final String PREF_EMAIL = "Email";
    SharedPreferences settings;

    private EditText emailEditText;

    private EditText passwordEditText;
    private Button nextButton;
    private static final String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        settings = getSharedPreferences("app_settings", MODE_PRIVATE);

        emailEditText = findViewById(R.id.etEmail);
        nextButton = findViewById(R.id.btnSubmit);

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = s.toString().trim();
                String password = s.toString().trim();
                Log.d(TAG, "Text changed: " + email);
                updateButtonState(email);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        nextButton.setOnClickListener(v -> {
            if (nextButton.isEnabled()) {
                Log.d(TAG, "Button clicked!");
                String email = emailEditText.getText().toString().trim();
                Log.d(TAG, "Email for submission: " + email);
                int enabledResId = R.drawable.button_enable;
                nextButton.setBackgroundResource(enabledResId);

                SharedPreferences.Editor prefEditor = settings.edit();
                prefEditor.putString(PREF_EMAIL, email);
                prefEditor.apply();
                System.out.println("Почта сохранена "+ settings.getString(PREF_EMAIL, "Что"));


                Intent intent = new Intent(RegistrationActivity.this, EmailCaptchaActivity.class);
                intent.putExtra("USER_EMAIL", email);
                startActivity(intent);
            }
        });
    }

    private void updateButtonState(String email) {
        boolean isValid = isValidEmail(email);

        Log.d(TAG, "updateButtonState called. Email: '" + email + "', Valid: " + isValid);

        int disabledResId = R.drawable.button_enable;
        int enabledResId = R.drawable.button_enable;

        Log.d(TAG, "Disabled resource ID: " + disabledResId);
        Log.d(TAG, "Enabled resource ID: " + enabledResId);

        runOnUiThread(() -> {
            nextButton.setEnabled(isValid);

            if (isValid) {
                Log.d(TAG, "Setting active background");
                nextButton.setBackgroundResource(enabledResId);
                nextButton.setTextColor(getResources().getColor(android.R.color.white));
            } else {
                Log.d(TAG, "Setting disabled background");
                nextButton.setBackgroundResource(disabledResId);
                nextButton.setTextColor(getResources().getColor(android.R.color.white));
            }

            nextButton.invalidate();
        });
    }

    private boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            Log.d(TAG, "Email is empty");
            return false;
        }

        boolean isValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        Log.d(TAG, "Pattern validation result: " + isValid);

        return isValid;
    }
}