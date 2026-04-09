package com.example.smartlabactivity.api;

import androidx.compose.foundation.text.KeyMapping_androidKt;

public class UserRecordRequest {
    public String email;
    public String password;

    public UserRecordRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
