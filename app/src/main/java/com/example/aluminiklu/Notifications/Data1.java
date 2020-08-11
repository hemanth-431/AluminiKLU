package com.example.aluminiklu.Notifications;

public class Data1 {
    private String Title;
    private String Message;

    public Data1(String title, String message) {
        Title = title;
        Message = message;
    }

    public Data1() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

}