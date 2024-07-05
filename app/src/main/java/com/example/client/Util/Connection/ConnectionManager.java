package com.example.client.Util.Connection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.client.Activities.MainActivity;
import com.example.client.Callback.RequestCallback;
import com.example.client.Interface.ApiService;
import com.example.client.Model.Responses.MyResponse;
import com.example.client.Util.MySharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConnectionManager extends AsyncTask<String, Void, String> {

    private String requestType;
    private static final String root = "http://192.168.1.115:4000";
    public static String URL;
    public static Retrofit retrofit;
    public static ApiService apiService;
    private RequestCallback callback;
    private static String selectedEventId;
    private static String selectedTaskId;

    public ConnectionManager(RequestCallback callback) {
        this.callback = callback;
    }
    public static void addPathToRoot(String path){
        URL=root+path;

    }
    public static String getRoot(){
        return ConnectionManager.root;
    }
    public static void getRequest( String data, RequestCallback callback) {
        new ConnectionManager(callback).execute(URL, data, "GET");
    }
    public static void postRequest( String data,RequestCallback callback) {
        new ConnectionManager(callback).execute(URL, data, "POST");
    }

    public static void setSelectedEventId(String eventId){
        selectedEventId=eventId;
    }
    public static void setSelectedTaskId(String taskId){selectedTaskId=taskId;}
    public static String getSelectedEventId() {
        return selectedEventId;
    }

    public static String getSelectedTaskId() {
        return selectedTaskId;
    }

    public static void logout(Context context) {
        RetrofitClient.clearCookies();
        MySharedPrefManager.getInstance().setSharedPref(context,false);
        Call<MyResponse> call = ConnectionManager.apiService.getLogout();
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.i("RESPONSE_LOGOUT", response.code() + " " + response.message());
                AuthManager.clearJWT(context);
                Toast.makeText(context, "Logged Out", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }


    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0]; // URL to call
        String data = params[1]; // data to post (used for POST requests)
        requestType = params[2]; // type of request ("POST" or "GET")
        HttpURLConnection urlConnection = null;
        StringBuilder response = new StringBuilder();
        InputStream inputStream;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();

            if ("POST".equalsIgnoreCase(requestType)) {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

                Gson gson = new Gson();
                JsonObject jsonParam = gson.fromJson(data, JsonObject.class);

                Log.i("JSON", jsonParam.toString());

                try (DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream())) {
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    int responseCode = urlConnection.getResponseCode();
                    Log.i("STATUS", String.valueOf(responseCode));
                    Log.i("MSG", urlConnection.getResponseMessage());
                    Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get("Set-Cookie");

                    response.append(responseCode).append(" @ ").append(urlConnection.getResponseMessage()).append(" @ ");
                    // Determine which stream to read from
                    if (responseCode >= 200 && responseCode < 300) {
                        inputStream = urlConnection.getInputStream();
                    } else {
                        inputStream = urlConnection.getErrorStream();
                    }
                        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                        }



                }
                catch(Exception e){
                    e.printStackTrace();
                    Log.i("Exception", e.getMessage());
                }
            } else if ("GET".equalsIgnoreCase(requestType)) {
                urlConnection.setRequestMethod("GET");

                int responseCode = urlConnection.getResponseCode();
                Log.i("STATUS", String.valueOf(responseCode));
                Log.i("MSG", urlConnection.getResponseMessage());
                response.append(responseCode).append(" @ ").append(urlConnection.getResponseMessage()).append(" @ ");
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                    }
                    Log.i("RESPONSE",response.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Exception", e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        /*   if ("GET".equalsIgnoreCase(requestType) && callback != null) {
            callback.onResponse(result);
        } else {
            Log.i("POST Result", result);
        }*/
        int code;
        String message;
        String[] results;

        results = result.split(" @ ");
        if (results[0].isEmpty()) {

        } else {
            code = Integer.valueOf(results[0]);
            if (results.length > 2) {
                if (isJsonArray(results[2]))
                    message = identifyMessage(results[2]);
                else {
                    if (checkForHeader("err", results[2]))
                        message = getMessageFromJson("err", results[2]);
                    else
                        message = results[2];
                }
            } else if (results.length > 1) {
                message = results[1];
            } else {
                message = "";
            }

            callback.onResponse(code, message);
        }
    }

    private String getMessageFromJson(String header, String result) {
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        return jsonObject.get(header).toString();
    }

    private boolean checkForHeader(String header, String result) {
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
            return jsonObject.has(header);
    }


    public static boolean isJsonArray(String jsonString) {
        try {
            JsonElement jsonElement = JsonParser.parseString(jsonString);
            if(jsonElement.getAsJsonObject().has("error")){
                jsonElement=jsonElement.getAsJsonObject().get("error");
            }
            return jsonElement.isJsonArray();
        } catch (JsonSyntaxException e) {
            return false;
        }
    }



    private JSONObject convertStrToJSON(String data) {
        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("username", "admin");
            jsonParam.put("password", "12345Aa");

        } catch (Exception e) {
            Log.d("error", e.getMessage());

        }
        return jsonParam;

    }

    public static String identifyMessage(String result) {
        String msg="";
        Gson g = new Gson();
        if(result==null || result=="")
            return "";
        JsonObject jsonObject = JsonParser.parseString(result).getAsJsonObject();
        JsonArray errorArray= null;
        if (jsonObject.has("err"))
            return jsonObject.get("err").getAsString();
        if (jsonObject.has("Unauthorized"))
            return jsonObject.get("Unauthorized").getAsString();
        if (jsonObject.has("error"))
            errorArray = jsonObject.getAsJsonArray("error");



    if(errorArray!=null) {
        if (errorArray.size() > 0) {
            JsonObject errorObject = errorArray.get(0).getAsJsonObject();
            if (errorObject.has("msg")) {
                msg = errorObject.get("msg").getAsString();
                return msg;
            }
        }
    }
        return result;
    }

    public static void initRetrofit(){
        String accessToken=AuthManager.getInstance().getAccessToken();
        retrofit=RetrofitClient.getClient(root+"/", accessToken);
      //  AuthManager.getInstance().setJWT(accessToken);

        Log.i("ACCESS TOKEN",AuthManager.getInstance().getAccessToken());
        apiService=retrofit.create(ApiService.class);
    }
    public static void updateRetrofit(String newAccessToken){
        AuthManager.getInstance().setAccessToken(newAccessToken);
        retrofit=RetrofitClient.getClient(root+"/",AuthManager.getInstance().getAccessToken());
        apiService=retrofit.create(ApiService.class);

    }

    public static String getUserId() {
//decodes jwt
        return AuthManager.getInstance().getUserId();
    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+");
    }



}

