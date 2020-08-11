package com.example.aluminiklu.Fragments;

import com.example.aluminiklu.Notifications.MyResponse;
import com.example.aluminiklu.Notifications.NotificationSender;
import com.example.aluminiklu.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIServer {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAuUHW_fQ:APA91bFgHsf97Vzoi8CW5QpAC0I9AQ94Fvog-NcVOgYq6xX7luEJrp4At6OKuba1BTtDFryVKxwcw9-Ej4PC5MhXorMbD_JQsX9BLQVC9kVzPfhNR7t-krn4nmLcYEJwAZWZ_8-n2ndh"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
    @POST("fcm/send")
    Call<MyResponse> SendNotification(@Body NotificationSender body);

}
