package com.example.macrosocietyapp.api;

import android.util.Log;

import com.example.macrosocietyapp.api.callbacks.FriendListCallback;
import com.example.macrosocietyapp.api.callbacks.FriendRequestsCallback;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.api.callbacks.UserCallback;
import com.example.macrosocietyapp.api.callbacks.UserStatsCallback;
import com.example.macrosocietyapp.api.callbacks.UsersCallback;
import com.example.macrosocietyapp.models.FriendRequest;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.models.UserStats;
import com.example.macrosocietyapp.utils.AesEncryptionService;

import java.io.IOException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainAPI {
    private static final String baseUrl = "http://10.0.2.2:5000/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient().build())
            .build();
    private static final IUserAPI userApi = retrofit.create(IUserAPI.class);
    private static final IFriendsAPI friendsApi = retrofit.create(IFriendsAPI.class);
    private static final IFriendRequestsAPI friendRequestsApi = retrofit.create(IFriendRequestsAPI.class);

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                        @Override public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                        @Override public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[]{}; }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendVerificationCode(String email,String state, Callback<Void> callback) {
        String encryptedEmail = AesEncryptionService.encrypt(email);
        userApi.sendVerificationCode(encryptedEmail,state).enqueue(callback);
    }

    public static void loginWithCode(String email, String code, Callback<User> callback) {
        String encryptedEmail = AesEncryptionService.encrypt(email);
        userApi.loginWithCode(encryptedEmail, code).enqueue(callback);
    }

    public static void registerUser(User user, String code, Callback<User> callback) {
        userApi.registerUser(user,code).enqueue(callback); // теперь отправляем открыто (по HTTPS)
    }

    // Добавить в MainAPI
    public static void getUserById(int userId, UserCallback callback) {
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(userId));
        Log.e("das555",encryptedId);
        userApi.getUserById(encryptedId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        // Пытаемся прочитать ошибку с сервера
                        String errorMessage = response.errorBody() != null
                                ? response.errorBody().string()
                                : "Неизвестная ошибка";
                        callback.onError(errorMessage);
                    } catch (IOException e) {
                        callback.onError("Ошибка чтения тела ответа");
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void getUserStats(int userId, UserStatsCallback callback) {
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(userId));
        userApi.getUserStats(encryptedId).enqueue(new Callback<UserStats>() {
            @Override
            public void onResponse(Call<UserStats> call, Response<UserStats> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка загрузки статистики");
                }
            }

            @Override
            public void onFailure(Call<UserStats> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Friends API methods
    public static void getFriends(int userId, FriendListCallback callback) {
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(userId));
        friendsApi.getFriendsWithDetails(encryptedId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка загрузки друзей");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void getFriendDetail(int userId, FriendListCallback callback) {
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(userId));
        friendsApi.getFriendsWithDetails(encryptedId).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка загрузки друзей");
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void getAllUsers(String idUser, UsersCallback callback) {
        userApi.getAllUsers(idUser).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg = "Ошибка получения пользователей";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        errorMsg = "Ошибка чтения ошибки";
                    }
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void removeFriend(int userId, int friendId, SimpleCallback callback) {
        String encryptedUserId = AesEncryptionService.encrypt(String.valueOf(userId));
        String encryptedFriendId = AesEncryptionService.encrypt(String.valueOf(friendId));

        friendsApi.removeFriend(encryptedUserId, encryptedFriendId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Ошибка удаления друга");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Friend Requests API methods
    public static void sendFriendRequest(int senderId, int receiverId, SimpleCallback callback) {
        String encryptedSenderId = AesEncryptionService.encrypt(String.valueOf(senderId));
        String encryptedReceiverId = AesEncryptionService.encrypt(String.valueOf(receiverId));

        friendRequestsApi.sendFriendRequest(encryptedSenderId, encryptedReceiverId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    String errorMsg = "Ошибка отправки заявки";
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (IOException e) {
                        errorMsg = "Ошибка чтения ошибки";
                    }
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void getIncomingRequests(int userId, FriendRequestsCallback callback) {
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(userId));
        friendRequestsApi.getIncomingRequests(encryptedId).enqueue(new Callback<List<FriendRequest>>() {
            @Override
            public void onResponse(Call<List<FriendRequest>> call, Response<List<FriendRequest>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка загрузки входящих заявок");
                }
            }

            @Override
            public void onFailure(Call<List<FriendRequest>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void acceptRequest(String encryptedSenderId, String encryptedReceiverId, SimpleCallback callback) {
        friendRequestsApi.acceptRequest(encryptedSenderId, encryptedReceiverId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Ошибка принятия заявки");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void rejectRequest(String encryptedSenderId, String encryptedReceiverId, SimpleCallback callback) {
        friendRequestsApi.rejectRequest(encryptedSenderId, encryptedReceiverId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError("Ошибка отклонения заявки");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }
}