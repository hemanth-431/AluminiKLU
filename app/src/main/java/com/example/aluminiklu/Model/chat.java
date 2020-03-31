package com.example.aluminiklu.Model;

public class chat {
    private String sender;
    private String receiver;
    private String message;
    private String idkey;
    private boolean isseen;
    public chat(String sender, String receiver, String message,boolean isseen,String idkey) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen=isseen;//,boolean isseen
        this.idkey=idkey;
    }
    public chat()
    {

    }
    public String getIdkey() {
        return idkey;
    }

    public void setIdkey(String idkey) {
        this.idkey = idkey;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }
}
