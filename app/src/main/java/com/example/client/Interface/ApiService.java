package com.example.client.Interface;
import com.example.client.Model.EventData;
import com.example.client.Model.Guest;
import com.example.client.Model.NewTask;
import com.example.client.Model.Responses.DeleteGuestRequest;
import com.example.client.Model.Responses.EventResponse;
import com.example.client.Model.Responses.GuestResponse;
import com.example.client.Model.Responses.MyResponse;
import com.example.client.Model.Responses.RefreshResponse;
import com.example.client.Model.Responses.TaskResponse;
import com.example.client.Model.Responses.UpcomingResponse;
import com.example.client.Model.Responses.VendorResponse;
import com.example.client.Model.SugVendor;
import com.example.client.Model.Task;
import com.example.client.Model.TasksArray;
import com.example.client.Model.Upcoming;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("users/{userId}/events")
    Call<EventResponse> postEvent(@Path("userId") String userId, @Body EventData Data);

    @Headers("Content-Type: application/json")
    @GET("users/{userId}/events")
    Call<EventResponse> getEvents(@Path("userId") String userId);

    @Headers("Content-Type: application/json")
    @DELETE("users/{userId}/events/{eventId}")
    Call<EventResponse> deleteEvent(@Path("userId") String userId, @Path("eventId") String eventId);

    @Headers("Content-Type: application/json")
    @GET("users/{userId}/events/{eventID}/guests")
    Call<GuestResponse> getGuests(@Path("userId") String userId, @Path("eventID") String eventId);

    @Headers("Content-Type: application/json")
    @GET("users/{userId}/events/{eventID}/tasks")
    Call<TaskResponse> getTasks(@Path("userId") String userId, @Path("eventID") String eventId);

    @Headers("Content-Type: application/json")
    @PUT("users/{userId}/events/{eventID}/tasks")
    Call<TaskResponse> updateTasks(@Path("userId") String userId, @Path("eventID") String eventId, @Body TasksArray tasks);

    @Headers("Content-Type: application/json")
    @DELETE("users/{userId}/events/{eventID}/tasks/{taskId}")
    Call<TaskResponse> deleteTask(@Path("userId") String userId, @Path("eventID") String eventId, @Path("taskId") String taskId);

    @Headers("Content-Type: application/json")
    @POST("users/{userId}/events/{eventID}/tasks")
    Call<TaskResponse> addTask(@Path("userId") String userId, @Path("eventID") String eventId, @Body JsonObject newCard);


    @Headers("Content-Type: application/json")
    @GET("refresh")
    Call<RefreshResponse> getRefreshToken();

    @Headers("Content-Type: application/json")
    @GET("logout")
    Call<MyResponse> getLogout();

    @Headers("Content-Type: application/json")
    @POST("users/{userId}/events/{eventID}/guests")
    Call<MyResponse> addGuest(@Path("userId") String userId, @Path("eventID") String eventID, @Body Guest newGuest);

    @Headers("Content-Type: application/json")
    @PATCH("users/{userId}/events/{eventID}/guests/{guestId}")
    Call<MyResponse> updateStatus(
            @Path("userId") String userId,
            @Path("eventID") String eventID,
            @Path("guestId") String guestId,
            @Body JsonObject status
    );

    @Headers("Content-Type: application/json")
    @PATCH("users/{userId}/events/{eventID}/guests/{guestId}")
    Call<MyResponse> updateGuestsNumber(
            @Path("userId") String userId,
            @Path("eventID") String eventID,
            @Path("guestId") String guestId,
            @Body JsonObject peopleCount);

    @Headers("Content-Type: application/json")
    @PATCH("users/{userId}/events/{eventID}/guests/{guestId}")
    Call<MyResponse> updateComment(@Path("userId") String userId,
                                   @Path("eventID") String eventID,
                                   @Path("guestId") String guestId,
                                   @Body JsonObject comment);


    @HTTP(method = "DELETE", path = "users/{userId}/events/{eventID}/guests", hasBody = true)
    @Headers("Content-Type: application/json")
    Call<EventResponse> deleteGuests(@Path("userId") String userId, @Path("eventID") String eventID, @Body Map<String, ArrayList<String>> selected);

    @Headers("Content-Type: application/json")
    @GET("users/{userId}/events/{eventID}/vendors")
    Call<VendorResponse> getVendors(@Path("userId") String userId, @Path("eventID") String eventId);

    @Headers("Content-Type: application/json")
    @POST("users/{userId}/events/{eventID}/vendors/{vendorId}")
    Call<MyResponse> contactVendor(@Path("userId") String userId, @Path("eventID") String eventId, @Path("vendorId") String vendorId);

    @Headers("Content-Type: application/json")
    @PATCH("users/{userId}/events/{eventID}/vendors/{vendorId}")
    Call<MyResponse> addToMyVendors(@Path("userId") String userId, @Path("eventID") String eventId, @Path("vendorId") String vendorId,@Body JsonObject json);

    @HTTP(method = "DELETE", path = "users/{userId}/events/{eventID}/vendors", hasBody = true)
    @Headers("Content-Type: application/json")
    Call<MyResponse> deleteVendor(@Path("userId") String userId, @Path("eventID") String eventId,@Body JsonObject data);

    @Headers("Content-Type: application/json")
    @GET("users/{userId}/upcomingEvents")
    Call<ArrayList<Upcoming>> getUpcomingEvents(@Path("userId") String userId);
    @Headers("Content-Type: application/json")
    @DELETE("users/{userId}/upcomingEvents/{eventId}")
    Call<MyResponse> deleteUpcomingEvent(@Path("userId") String userId, @Path("eventId") String eventId);
}