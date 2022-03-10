package com.example.giveandtake.model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Post implements Serializable {
    @PrimaryKey
    @NonNull
    private String id = "1234";
    private String content = "";
    private int image = 0;
    private String imageUrl = "";
    private String location = "";
    private Long updateDate = new Long(0);

    public Post(String content,  int image, String location) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
    }

    public Map<String, Object> toJson(){
        Map<String,Object> outputJson = new HashMap<>();
        outputJson.put("id", id);
        outputJson.put("content", content);
        outputJson.put("imageUrl", imageUrl);
        outputJson.put("location", location);
        outputJson.put("updateDate", FieldValue.serverTimestamp());
        return outputJson;
    }

    public static Post fromJson(Map<String, Object> postData) {
        String id = (String) postData.get("id");
        String content = (String) postData.get("content");
        String location = (String) postData.get("location");
        String imageUrl = (String)postData.get("imageUrl");
        Timestamp ts = (Timestamp)postData.get("updateDate");
        Long updateDate = ts.getSeconds();

        Post post = new Post(content,0,location);
        post.setId(id);
        post.setUpdateDate(updateDate);
        post.setImageUrl(imageUrl);

        return post;
    }


}
