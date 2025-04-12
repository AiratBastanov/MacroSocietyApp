package com.example.macrosocietyapp.api;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainAPI {
    private static final String baseUrl = "http://10.0.2.2:5000/";
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient().build())
            .build();
    private static final IAPI api = retrofit.create(IAPI.class);

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

    public static void sendVerificationCode(String email, Callback<Void> callback) {
        api.sendVerificationCode(email).enqueue(callback);
    }

    public static void loginWithCode(String email, String code, Callback<Void> callback) {
        api.loginWithCode(email, code).enqueue(callback);
    }

    public static void registerUser(String encryptedUserData, Callback<String> callback) {
        api.registerUser(encryptedUserData).enqueue(callback);
    }
}



