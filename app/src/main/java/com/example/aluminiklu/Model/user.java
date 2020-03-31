package com.example.aluminiklu.Model;

public class user {
    private String id, username, imageUrl;
private String status,search;
    public user(String id, String username, String imageUrl,String status,String search) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.status=status;
        this.search=search;
    }
    public  user(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}

 /*   public user(String name, String imageUrl,String status,String search) {
       this.name = name;
      this.imageUrl =imageUrl ;
      this.search=search;
      this.status=status;
    }

    public String getFullName() {
        return name;
    }

    public void setFullName(String fullName) {
        name = fullName;
    }

    public String getMailId() {
        return imageUrl;
    }

    public void setMailId(String mailId) {
        imageUrl = mailId;
    }

    public user(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
*/