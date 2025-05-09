package com.example.macrosocietyapp.api;

import android.util.Log;
import android.widget.Toast;

import com.example.macrosocietyapp.api.callbacks.AddPostCallback;
import com.example.macrosocietyapp.api.callbacks.CommunityCallback;
import com.example.macrosocietyapp.api.callbacks.CommunityListCallback;
import com.example.macrosocietyapp.api.callbacks.FriendListCallback;
import com.example.macrosocietyapp.api.callbacks.FriendRequestsCallback;
import com.example.macrosocietyapp.api.callbacks.PostsCallback;
import com.example.macrosocietyapp.api.callbacks.SimpleCallback;
import com.example.macrosocietyapp.api.callbacks.UserCallback;
import com.example.macrosocietyapp.api.callbacks.UserStatsCallback;
import com.example.macrosocietyapp.api.callbacks.UsersCallback;
import com.example.macrosocietyapp.models.Community;
import com.example.macrosocietyapp.models.CommunityCreateDto;
import com.example.macrosocietyapp.models.CommunityMember;
import com.example.macrosocietyapp.models.FriendRequest;
import com.example.macrosocietyapp.models.LeaveCommunityRequest;
import com.example.macrosocietyapp.models.Post;
import com.example.macrosocietyapp.models.PostDto;
import com.example.macrosocietyapp.models.User;
import com.example.macrosocietyapp.models.UserStats;
import com.example.macrosocietyapp.utils.AesEncryptionService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainAPI {
    private static final String baseUrl = "http://10.0.2.2:5000/";

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();

    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getUnsafeOkHttpClient().build())
            .build();

    private static final IUserAPI userApi = retrofit.create(IUserAPI.class);
    private static final IFriendsAPI friendsApi = retrofit.create(IFriendsAPI.class);
    private static final IFriendRequestsAPI friendRequestsApi = retrofit.create(IFriendRequestsAPI.class);
    private static final ICommunityAPI communityApi = retrofit.create(ICommunityAPI.class);
    private static final ICommunityMemberAPI communityMemberApi = retrofit.create(ICommunityMemberAPI.class);
    private static final IPostsAPI postsAPI = retrofit.create(IPostsAPI.class);

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

    // Получение всех сообществ(кроме своих)
    public static void getAllCommunities(int userId, CommunityListCallback callback) {
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(userId));
        communityApi.getAllCommunities(encryptedId).enqueue(new Callback<List<Community>>() {
            @Override
            public void onResponse(Call<List<Community>> call, Response<List<Community>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    handleError(response, callback::onError);
                }
            }

            @Override
            public void onFailure(Call<List<Community>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Получение сообществ пользователя
    public static void getUserCommunities(int userId, CommunityListCallback callback) {
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(userId));
        communityApi.getUserCommunities(encryptedId).enqueue(new Callback<List<Community>>() {
            @Override
            public void onResponse(Call<List<Community>> call, Response<List<Community>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    handleError(response, callback::onError);
                }
            }

            @Override
            public void onFailure(Call<List<Community>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Создание сообщества
    public static void createCommunity(Community community, CommunityCallback callback) {
        String encryptedCreatorId = AesEncryptionService.encrypt(String.valueOf(community.getCreatorId()));

        CommunityCreateDto dto = new CommunityCreateDto(
                community.getName(),
                community.getDescription(),
                encryptedCreatorId
        );

        communityApi.createCommunity(dto).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String encryptedId = response.body().string();
                        String decryptedId = AesEncryptionService.decrypt(encryptedId);

                        Community createdCommunity = new Community();
                        createdCommunity.setId(Integer.parseInt(decryptedId));
                        createdCommunity.setName(community.getName());
                        createdCommunity.setDescription(community.getDescription());
                        createdCommunity.setCreatorId(community.getCreatorId());

                        callback.onSuccess(createdCommunity);
                    } catch (Exception e) {
                        callback.onError("Ошибка расшифровки ответа: " + e.getMessage());
                    }
                } else {
                    handleError(response, callback::onError);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void subscribeToCommunity(int userId, int communityId, SimpleCallback callback) {
        String encryptedUserId = AesEncryptionService.encrypt(String.valueOf(userId));
        String encryptedCommunityId = AesEncryptionService.encrypt(String.valueOf(communityId));

        communityMemberApi.subscribeToCommunity(encryptedUserId, encryptedCommunityId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    handleError(response, callback::onError);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void unsubscribeFromCommunity(int userId, int communityId, SimpleCallback callback) {
        String encryptedUserId = AesEncryptionService.encrypt(String.valueOf(userId));
        String encryptedCommunityId = AesEncryptionService.encrypt(String.valueOf(communityId));

        communityMemberApi.unsubscribeFromCommunity(encryptedUserId, encryptedCommunityId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    handleError(response, callback::onError);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void deleteCommunity(int communityId, SimpleCallback callback) {
        String encryptedId = AesEncryptionService.encrypt(String.valueOf(communityId));
        communityApi.deleteCommunity(encryptedId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    handleError(response, callback::onError);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void transferOwnership(int communityId, SimpleCallback callback) {
        communityApi.transferOwnership(communityId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    handleError(response, callback::onError);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void getPostsByCommunity(String encryptedCommunityId, PostsCallback callback) {
        postsAPI.getCommunityPosts(encryptedCommunityId).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Ошибка загрузки постов");
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void addPost(PostDto postDto, AddPostCallback callback) {
        postsAPI.addPost(postDto).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject body = response.body();
                    String postId = body.get("postId").getAsString();
                    String createdAt = body.get("createdAt").getAsString();

                    callback.onSuccess(postId, createdAt);
                } else {
                    handleError(response, callback::onError);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    // Вспомогательный метод
    private static void handleError(Response<?> response, java.util.function.Consumer<String> errorConsumer) {
        try {
            String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Неизвестная ошибка";
            errorConsumer.accept(errorMessage);
        } catch (IOException e) {
            errorConsumer.accept("Ошибка чтения тела ответа");
        }
    }
}