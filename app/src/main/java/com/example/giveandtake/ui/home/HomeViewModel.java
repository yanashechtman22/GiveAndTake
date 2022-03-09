package com.example.giveandtake.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.giveandtake.R;
import com.example.giveandtake.model.Post;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private ArrayList<Post> posts = new ArrayList<>();

    public HomeViewModel() {
        posts.add(new Ad("This is first ad", R.drawable.image1, "bla"));
        posts.add(new Ad("This is second ad", R.drawable.image2, "bla"));
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
    public ArrayList<Post> getPosts(){ return posts;}
}
