package com.example.smartlabactivity.api.dto;

import com.google.gson.annotations.SerializedName;

public class UserAuthRequest {
    @SerializedName("identity")
    public String identity;

    @SerializedName("password")
    public String password;

    public UserAuthRequest(String identity, String password) {
        this.identity = identity;
        this.password = password;
    }
}