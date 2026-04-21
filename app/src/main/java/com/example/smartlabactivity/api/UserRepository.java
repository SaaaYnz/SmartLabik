package com.example.smartlabactivity.api;

import com.example.smartlabactivity.api.dto.ErrorResponse;
import com.example.smartlabactivity.api.dto.UserAuthRequest;
import com.example.smartlabactivity.api.dto.UserAuthResponse;
import com.example.smartlabactivity.api.dto.UserRecordRequest;
import com.example.smartlabactivity.api.dto.UserRecordResponse;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private ApiService apiService;
    private Gson gson;

    public UserRepository() {
        apiService = ApiClient.getApiService();
        gson = new Gson();
    }

    public void registerUser(String email, String password, final RegisterCallback callback) {
        if (email == null || email.isEmpty()) {
            callback.onError("Email не может быть пустым");
            return;
        }

        if (password == null || password.isEmpty()) {
            callback.onError("Пароль не может быть пустым");
            return;
        }

        if (password.length() < 8) {
            callback.onError("Пароль должен содержать минимум 8 символов");
            return;
        }

        UserRecordRequest request = new UserRecordRequest(email, password);

        apiService.registerUser(request).enqueue(new Callback<UserRecordResponse>() {
            @Override
            public void onResponse(Call<UserRecordResponse> call, Response<UserRecordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMessage = parseErrorResponse(response);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<UserRecordResponse> call, Throwable t) {
                callback.onError("Сетевая ошибка: " + t.getMessage());
            }
        });
    }

    public void loginUser(String email, String password, final LoginCallback callback) {
        if (email == null || email.isEmpty()) {
            callback.onError("Email не может быть пустым");
            return;
        }

        if (password == null || password.isEmpty()) {
            callback.onError("Пароль не может быть пустым");
            return;
        }

        UserAuthRequest userAuthRequest = new UserAuthRequest(email, password);

        apiService.loginUser(userAuthRequest).enqueue(new Callback<UserAuthResponse>() {
            @Override
            public void onResponse(Call<UserAuthResponse> call, Response<UserAuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMessage = parseErrorResponse(response);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<UserAuthResponse> call, Throwable t) {
                callback.onError("Сетевая ошибка: " + t.getMessage());
            }
        });
    }

    public void getUserById(String token, String userId, final GetUserCallback callback) {
        if (token == null || token.isEmpty()) {
            callback.onError("Токен отсутствует");
            return;
        }

        String authHeader = "Bearer " + token;

        apiService.getUserById(authHeader, userId).enqueue(new Callback<UserRecordResponse>() {
            @Override
            public void onResponse(Call<UserRecordResponse> call, Response<UserRecordResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMessage = parseErrorResponse(response);
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<UserRecordResponse> call, Throwable t) {
                callback.onError("Сетевая ошибка: " + t.getMessage());
            }
        });
    }

    private String parseErrorResponse(Response<?> response) {
        String errorMessage = "Ошибка: " + response.code();

        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                ErrorResponse errorResponse = gson.fromJson(errorBody, ErrorResponse.class);

                if (errorResponse.message != null) {
                    errorMessage = errorResponse.message;
                }

                if (errorBody.contains("email")) {
                    errorMessage = "Пользователь с таким email уже существует";
                } else if (errorBody.contains("password")) {
                    errorMessage = "Пароль должен содержать минимум 8 символов";
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return errorMessage;
    }

    public interface RegisterCallback {
        void onSuccess(UserRecordResponse user);
        void onError(String error);
    }

    public interface LoginCallback {
        void onSuccess(UserAuthResponse userAuthResponse);
        void onError(String error);
    }

    public interface GetUserCallback {
        void onSuccess(UserRecordResponse user);
        void onError(String error);
    }
}