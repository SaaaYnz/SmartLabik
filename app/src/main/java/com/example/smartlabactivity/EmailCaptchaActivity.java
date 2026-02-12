package com.example.smartlabactivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EmailCaptchaActivity extends AppCompatActivity {

    private EditText[] codeFields;
    private TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_captcha);

        initViews();
        setupAutoNext();
        getEmailFromIntent();
    }

    public void onclick(View v) {
        Intent i = new Intent(EmailCaptchaActivity.this, PasswordActivity.class);
        startActivity(i);
    }

    public void back(View v) {
        Intent i = new Intent(EmailCaptchaActivity.this, RegistrationActivity.class);
        startActivity(i);
    }
    private void initViews() {
        codeFields = new EditText[]{
                findViewById(R.id.code1),
                findViewById(R.id.code2),
                findViewById(R.id.code3),
                findViewById(R.id.code4)
        };
        emailTextView = findViewById(R.id.email);
    }

    private void setupAutoNext() {
        for (int i = 0; i < codeFields.length; i++) {
            final int currentIndex = i;

            codeFields[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1 && currentIndex < codeFields.length - 1) {
                        codeFields[currentIndex + 1].requestFocus();
                    }
                }


                @Override
                public void afterTextChanged(Editable s) {}
            });

            codeFields[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == android.view.KeyEvent.KEYCODE_DEL &&
                        codeFields[currentIndex].getText().toString().isEmpty() &&
                        currentIndex > 0) {
                    codeFields[currentIndex - 1].requestFocus();
                    codeFields[currentIndex - 1].setSelection(
                            codeFields[currentIndex - 1].getText().length());
                    return true;
                }
                return false;
            });
        }
    }

    private void getEmailFromIntent() {
        String email = getIntent().getStringExtra("USER_EMAIL");
        if (email != null) {
            emailTextView.setText(email);
        }
    }
}