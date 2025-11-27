package com.example.sysdesk.network;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import android.util.Base64;

public class TokenInterceptor implements Interceptor {

    private volatile String token;

    /**
     * Define usuário e senha para Basic Auth
     */
    public void setToken(String username, String password) {
        String userpass = username + ":" + password;
        this.token = Base64.encodeToString(userpass.getBytes(), Base64.NO_WRAP);
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

        Request requestWithAuth = original.newBuilder()
                .header("Authorization", "Basic " + token)
                .build();

        return chain.proceed(requestWithAuth);
    }
}
