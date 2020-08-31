package com.example.aluminiklu.Model;

public class jobs {
    public String id, date, des,jobs;
    public String link;
    public String name,user;

    public jobs() {
    }
    public jobs(String name,String jobs, String date, String link,String user,String des,String id) {
        this.name = name;
        this.date = date;
        this.jobs=jobs;
        this.link = link;
        this.user=user;
        this.des=des;
        this.id=id;
    }
    public String getId() {
        return id;
    }

    public String getJobs() {
        return jobs;
    }

    public void setJobs(String jobs) {
        this.jobs = jobs;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}


