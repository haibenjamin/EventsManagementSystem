package com.example.client.Util.Connection;

import com.example.client.Model.SerializableCookie;

import java.io.IOException;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.HttpUrl;

public class CookieInterceptor implements Interceptor {

    private final MyCookieJar cookieJar;

    public CookieInterceptor(MyCookieJar cookieJar) {
        this.cookieJar = cookieJar;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        // Extract cookies from headers
        List<String> cookies = originalResponse.headers("Set-Cookie");
        for (String cookieHeader : cookies) {
            // Parse and save cookies into MyCookieJar
            HttpUrl url = originalResponse.request().url();
            cookieJar.saveCookie(parseCookie(cookieHeader, url));
        }

        return originalResponse;
    }

    // Method to parse Set-Cookie header into SerializableCookie
    public SerializableCookie parseCookie(String cookieHeader, HttpUrl url) {
        // Split the cookie header by ';'
        String[] parts = cookieHeader.split(";");

        // Extract name and value from the first part "jwt=..."
        String[] nameValue = parts[0].split("=");  // Split by '='
        String name = nameValue[0].trim();
        String value = nameValue[1].trim();

        // Initialize default values for other attributes
        String domain = url.host();
        String path = "/";
        long expiresAt = 0;  // Default to 0 (session cookie)
        boolean secure = false;
        boolean httpOnly = false;

        // Parse other attributes from the remaining parts
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.startsWith("Domain=")) {
                domain = part.substring("Domain=".length());
            } else if (part.startsWith("Path=")) {
                path = part.substring("Path=".length());
            } else if (part.startsWith("Expires=")) {
                // Example parsing of Expires attribute (convert to epoch time)
                String expiresStr = part.substring("Expires=".length());
                // Example parsing logic (adjust as per date format)
                // expiresAt = parseExpires(expiresStr);
            } else if (part.equals("Secure")) {
                secure = true;
            } else if (part.equals("HttpOnly")) {
                httpOnly = true;
            }
        }

        // Create an OkHttp Cookie object
        Cookie okHttpCookie = new Cookie.Builder()
                .name(name)
                .value(value)
                .domain(domain)
                .path(path)
                .expiresAt(expiresAt)
                .secure()
                .httpOnly()
                .build();

        // Return a SerializableCookie wrapping the OkHttp Cookie
        return new SerializableCookie(okHttpCookie);
    }}

