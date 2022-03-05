package com.example.giveandtake.model;

import java.io.Serializable;

public class Ad implements Serializable {
    private String text;
    private int image;

    public Ad(String text, int image) {
        this.text = text;
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() { return image; }

    public void setImage(int image) {
        this.image = image;
    }
}
