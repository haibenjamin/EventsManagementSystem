package com.example.client.Util.Connection;

import androidx.activity.result.contract.ActivityResultContracts;

import com.example.client.Activities.BaseActivity;
import com.example.client.Model.SerializableCookie;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static MyCookieJar cookieJar;

    public static Retrofit getClient(String baseUrl, String accessToken) {
        if (cookieJar == null) {
            cookieJar = new MyCookieJar();
        }

        // Set initial cookies
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(new Cookie.Builder()
                .name("jwt")
                .value(accessToken)
                .domain("localhost") // replace with your domain
                .path("/")
                .httpOnly()
                .build());
        cookieJar.saveFromResponse(HttpUrl.get(baseUrl), cookies);

        // Logging interceptor
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttpClient with interceptors
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(new AuthInterceptor(accessToken))
                .addInterceptor(new CookieInterceptor(cookieJar))
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);

                        // Check if token is expired
                        if (response.code() == 401) {
                            // Refresh token logic
                            synchronized (this) {
                                // Use refresh token to get a new access token
                                String newAccessToken = refreshAccessToken(accessToken);

                                // Update the cookie jar with new token
                                List<Cookie> newCookies = new ArrayList<>();
                                newCookies.add(new Cookie.Builder()
                                        .name("jwt")
                                        .value(newAccessToken)
                                        .domain("localhost") // replace with your domain
                                        .path("/")
                                        .httpOnly()
                                        .build());
                                cookieJar.saveFromResponse(HttpUrl.get(baseUrl), newCookies);


                                // Retry the original request with new token
                                Request newRequest = request.newBuilder()
                                        .header("Authorization", "Bearer " + newAccessToken)
                                        .build();
                                response.close();
                                return chain.proceed(newRequest);
                            }
                        }

                        return response;
                    }
                })
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static String refreshAccessToken(String refreshToken) throws IOException {
        // Make a network call to refresh the access token
        // This should be implemented to call your API and get a new access token using the refresh token
        // For example:
        Response response = new OkHttpClient().newCall(new Request.Builder()
                .url(ConnectionManager.getRoot()+"/refresh") // replace with your refresh token URL
                .post(RequestBody.create(MediaType.parse("application/json"), ""))
                .addHeader("jwt", refreshToken)
                .build()).execute();

        if (response.isSuccessful()) {
            // Parse and return the new access token
            String responseBody = response.body().string();
        } else {

            throw new IOException("Failed to refresh token");
        }
        return null;
    }

    public static void clearCookies() {
        if (cookieJar != null) {
            cookieJar.clearCookies();
            cookieJar=null;
        }
    }
    public static List<SerializableCookie> getCookies() {
        if (cookieJar != null) {
            return cookieJar.getCookies();
        }
        return new ArrayList<>();
    }
}
