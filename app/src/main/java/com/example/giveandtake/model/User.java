package com.example.giveandtake.model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String displayName;
    private String email;
    private String phone;
    private String password;
    private List<Post> posts;

    public User(String displayName, String email, String phone, String password, List<Post> posts) {
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.posts = posts;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}