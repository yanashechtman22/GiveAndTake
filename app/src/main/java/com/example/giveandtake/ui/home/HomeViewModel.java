package com.example.giveandtake.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.giveandtake.R;
import com.example.giveandtake.model.Ad;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private ArrayList<Ad> posts = new ArrayList<>();

    public HomeViewModel() {
        posts.add(new Ad("This is first ad", R.drawable.image1));
        posts.add(new Ad("This is second ad", R.drawable.image2));
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public ArrayList<Ad> getPosts(){ return posts;}
}