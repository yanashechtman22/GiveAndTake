package com.example.giveandtake.model;

import android.net.Uri;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Post implements Serializable {
    private String id;
    private String content;
    private int image;
    private Uri imageUrl;
    private String location;

    public Post(String content, String text, int image) {
        this.content = content;
        this.image = image;
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Map<String, Object> toJson(){
        Map<String,Object> outputJson = new HashMap<>();
        outputJson.put("title", content);
        outputJson.put("imageUrl", imageUrl.toString());
        outputJson.put("location", location);
        return outputJson;
    }


}
