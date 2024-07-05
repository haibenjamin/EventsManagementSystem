package com.example.client.Util.Connection;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private String accessToken;

    public AuthInterceptor(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Check if the request already has an Authorization header
        if (originalRequest.header("Authorization") == null) {
            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Access-Control-Allow-Credentials", "true");
            Request newRequest = requestBuilder.build();
            return chain.proceed(newRequest);
        }

        return chain.proceed(originalRequest);
    }
}
