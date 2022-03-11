package com.example.giveandtake.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.giveandtake.R;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private LiveData<List<Post>> posts;

    public HomeViewModel() {
        posts = AppModel.instance.getAll();
    }

    public LiveData<List<Post>> getPosts() {
        return posts;
    }
}
