package com.example.sysdesk.network;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    private volatile String token;

    public void setToken(String token) {
        this.token = token;
    }

    public void clearToken() {
        this.token = null;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if (token == null || token.trim().isEmpty()) {
            return chain.proceed(original);
        }

        Request requestWithToken = original.newBuilder()
                .header("Authorization", "Basic " + token)
                .build();

        return chain.proceed(requestWithToken);
    }
}
