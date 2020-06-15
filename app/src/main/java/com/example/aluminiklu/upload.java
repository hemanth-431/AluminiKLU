package com.example.aluminiklu;

public class upload {
    private String mName;
    private String mImageUrl;
    private String mKey;
    private String mData;
    private String mtime;
    private String mSNme;
    private String s;


    public upload() {
        //empty constructor needed
    }

    public upload(String name, String imageUrl,String data,String key,String time,String ms,String sname) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mData=data;
       mKey=key;
       mSNme=sname;
       mtime=time;
       s=ms;

    }

    public String getmSNme() {
        return mSNme;
    }

    public void setmSNme(String mSNme) {
        this.mSNme = mSNme;
    }

    public String getS() {
        return mtime;
    }

    public void setS(String mtime) {
        this.mtime = mtime;
    }


    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
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