package com.example.sysdesk.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.3.19:8080/";

    // Interceptor para autenticação Basic Auth
    private static final TokenInterceptor tokenInterceptor = new TokenInterceptor();

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(tokenInterceptor) // ADICIONE ISSO
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    // Torna visível para outras classes
    public static void setToken(String token) {
        tokenInterceptor.setToken(token);
    }

    public static void clearToken() {
        tokenInterceptor.clearToken();
    }
}
