package com.example.giveandtake.model;

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
    private String phone = "";
    private String content = "";
    private String imageUrl = "";
    private String location = "";
    private String userId = "";
    private Long updateDate = new Long(0);
    private boolean isDeleted = false;

    public Post(String content, String phone, String location, String userId) {
        this.content = content;
        this.phone=phone;
        this.location = location;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
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
    public String getUserId() {
        return userId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Map<String, Object> toJson(){
        Map<String,Object> outputJson = new HashMap<>();
        outputJson.put("id", id);
        outputJson.put("phone", phone);
        outputJson.put("content", content);
        outputJson.put("imageUrl", imageUrl);
        outputJson.put("location", location);
        outputJson.put("userId", userId);
        outputJson.put("isDeleted", isDeleted);
        outputJson.put("updateDate", FieldValue.serverTimestamp());
        return outputJson;
    }

    public static Post fromJson(Map<String, Object> postData) {
        String id = (String) postData.get("id");
        String phone = (String) postData.get("phone");
        String content = (String) postData.get("content");
        String location = (String) postData.get("location");
        String imageUrl = (String)postData.get("imageUrl");
        Timestamp ts = (Timestamp)postData.get("updateDate");
        String userId = (String) postData.get("userId");
        Long updateDate = ts.getSeconds();
        Boolean isDeleted = (Boolean) postData.get("isDeleted");

        Post post = new Post(content,phone,location,userId);
        post.setId(id);
        post.setDeleted(isDeleted);
        post.setUpdateDate(updateDate);
        post.setImageUrl(imageUrl);

        return post;
    }


}
