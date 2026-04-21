package com.example.smartlabactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smartlabactivity.api.UserRepository;
import com.example.smartlabactivity.api.dto.UserAuthResponse;
import com.example.smartlabactivity.api.dto.UserRecordResponse;

public class AuthActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnSignUp;
    private UserRepository userRepository;
    private SharedPreferences sharedPreferences;
    private boolean isLoggingIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);

        initViews();
        userRepository = new UserRepository();
        sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        setupTextWatchers();
        setupButtons();
        updateButtonState();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etEmail.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
    }

    private void setupButtons() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnLogin.isEnabled() && !isLoggingIn) {
                    performLogin();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInputs(email, password)) {
            return;
        }

        isLoggingIn = true;
        btnLogin.setEnabled(false);
        btnLogin.setText("Вход...");

        userRepository.loginUser(email, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(UserAuthResponse userAuthResponse) {
                runOnUiThread(() -> {
                    // Выводим токен в консоль
                    String token = userAuthResponse.token;
                    System.out.println("========== JWT TOKEN ==========");
                    System.out.println(token);
                    System.out.println("================================");

                    // Также выводим в Logcat
                    android.util.Log.d("AUTH_TOKEN", "JWT Token: " + token);

                    // Сохраняем токен и ID пользователя
                    saveUserData(userAuthResponse);

                    Toast.makeText(AuthActivity.this, "Вход выполнен успешно!", Toast.LENGTH_SHORT).show();

                    // Проверяем токен, получая данные пользователя
                    checkTokenAndGetUser(userAuthResponse.token, userAuthResponse.record.id);

                    Intent intent = new Intent(AuthActivity.this, PageAnaliseActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    isLoggingIn = false;
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(AuthActivity.this, error, Toast.LENGTH_LONG).show();

                    btnLogin.setEnabled(true);
                    btnLogin.setText("Войти");
                    isLoggingIn = false;
                    updateButtonState();
                });
            }
        });
    }

    // Метод для проверки токена - просто выводим результат в консоль
    private void checkTokenAndGetUser(String token, String userId) {
        userRepository.getUserById(token, userId, new UserRepository.GetUserCallback() {
            @Override
            public void onSuccess(UserRecordResponse user) {
                // Выводим данные пользователя в консоль
                System.out.println("========== USER DATA ==========");
                System.out.println("User ID: " + user.id);
                System.out.println("Email: " + user.email);
                System.out.println("Verified: " + user.verified);
                System.out.println("Created: " + user.created);
                System.out.println("================================");

                android.util.Log.d("USER_DATA", "User ID: " + user.id);
                android.util.Log.d("USER_DATA", "Email: " + user.email);
            }

            @Override
            public void onError(String error) {
                System.out.println("========== ERROR ==========");
                System.out.println("Failed to get user: " + error);
                System.out.println("===========================");

                android.util.Log.e("USER_DATA", "Error: " + error);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            etEmail.setError("Введите email");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Введите корректный email");
            etEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Введите пароль");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            etPassword.setError("Пароль должен содержать минимум 8 символов");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void updateButtonState() {
        if (!isLoggingIn) {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            boolean isValid = isValidEmail(email) && isValidPassword(password);
            btnLogin.setEnabled(isValid);
        }

        btnLogin.setBackgroundResource(R.drawable.button_enable);
        btnLogin.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    }

    private boolean isValidEmail(String email) {
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        return !password.isEmpty() && password.length() >= 8;
    }

    private void saveUserData(UserAuthResponse userAuthResponse) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("jwt_token", userAuthResponse.token);

        if (userAuthResponse.record != null) {
            editor.putString("user_id", userAuthResponse.record.id);
            editor.putString("user_email", userAuthResponse.record.email);
            editor.putBoolean("user_verified", userAuthResponse.record.verified);
            editor.putString("user_created", userAuthResponse.record.created);
            editor.putString("user_updated", userAuthResponse.record.updated);

            if (userAuthResponse.record.first_name != null) {
                editor.putString("user_first_name", userAuthResponse.record.first_name);
            }
            if (userAuthResponse.record.last_name != null) {
                editor.putString("user_last_name", userAuthResponse.record.last_name);
            }
        }

        editor.apply();
    }
}