package com.example.client.Util.Connection;

import android.util.Log;

import com.example.client.Model.SerializableCookie;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCookieJar implements CookieJar {
    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        cookieStore.put(url.host(), cookies);
        for (Cookie cookie : cookies) {
            Log.i("MyCookieJar", "Cookie Saved: " + cookie.toString());
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies = cookieStore.get(url.host());
        return cookies != null ? cookies : new ArrayList<>();
    }

    public void clearCookies() {
        cookieStore.clear();
    }

    // Add method to save cookie from serialized form
    public void saveCookie(SerializableCookie serializableCookie) {
        Cookie cookie = serializableCookie.toCookie();
        List<Cookie> cookies = new ArrayList<>();
        cookies.add(cookie);
        cookieStore.put(cookie.domain(), cookies);
    }

    // Add method to retrieve serialized cookies
    public List<SerializableCookie> getCookies() {
        List<SerializableCookie> serializableCookies = new ArrayList<>();
        for (List<Cookie> cookies : cookieStore.values()) {
            for (Cookie cookie : cookies) {
                serializableCookies.add(new SerializableCookie(cookie));
            }
        }
        return serializableCookies;
    }
}
