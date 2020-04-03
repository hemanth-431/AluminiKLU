package com.example.aluminiklu;

public class upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mData;


    public upload() {
        //empty constructor needed
    }

    public upload(String name, String imageUrl,String data,String key) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mData=data;
       mKey=key;

    }

    public String getmData() {
        return mData;
    }

    public void setmData(String data) {
        mData = data;
    }
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getKey() {
        return mKey;
    }


    public void setKey(String key) {
        mKey = key;
    }


}