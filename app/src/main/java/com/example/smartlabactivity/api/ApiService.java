package com.example.smartlabactivity.api;

import com.example.smartlabactivity.api.dto.UserAuthRequest;
import com.example.smartlabactivity.api.dto.UserAuthResponse;
import com.example.smartlabactivity.api.dto.UserRecordRequest;
import com.example.smartlabactivity.api.dto.UserRecordResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/collections/users/auth-with-password")
    Call<UserAuthResponse> loginUser(@Body UserAuthRequest request);

    @POST("api/collections/users/records")
    Call<UserRecordResponse> registerUser(@Body UserRecordRequest request);

    @GET("api/collections/users/records/{id}")
    Call<UserRecordResponse> getUserById(@Header("Authorization") String authorization, @retrofit2.http.Path("id") String id);
}