package com.example.aluminiklu.Model;

public class events_data {
    String name,date,link,user,des,id;

public events_data(){

}

    public events_data(String name, String date, String link,String user,String des,String id) {
        this.name = name;
        this.date = date;
        this.link = link;
        this.user=user;
        this.des=des;
        this.id=id;
    }

    public String getDes() {
        return des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

