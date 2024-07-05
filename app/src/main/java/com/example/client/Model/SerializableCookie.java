package com.example.client.Model;
import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class SerializableCookie {
    private String name;
    private String value;
    private String domain;
    private String path;
    private long expiresAt;
    private boolean secure;
    private boolean httpOnly;

    public SerializableCookie(Cookie cookie) {
        this.name = cookie.name();
        this.value = cookie.value();
        this.domain = cookie.domain();
        this.path = cookie.path();
        this.expiresAt = cookie.expiresAt();
        this.secure = cookie.secure();
        this.httpOnly = cookie.httpOnly();
    }

    public Cookie toCookie() {
        return new Cookie.Builder()
                .name(name)
                .value(value)
                .domain(domain)
                .path(path)
                .expiresAt(expiresAt)
                .secure()
                .httpOnly()
                .build();
    }
}
