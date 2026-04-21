package com.example.smartlabactivity.api.dto;

import com.google.gson.annotations.SerializedName;

public class UserAuthResponse {
    @SerializedName("token")
    public String token;

    @SerializedName("record")
    public UserRecordResponse record;
}