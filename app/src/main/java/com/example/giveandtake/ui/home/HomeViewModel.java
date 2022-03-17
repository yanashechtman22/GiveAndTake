package com.example.giveandtake.ui.home;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private LiveData<List<Post>> posts;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HomeViewModel() {
        posts = AppModel.instance.getAll();
        if(posts.getValue() != null){
            posts.getValue().removeIf(post -> post.isDeleted());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<List<Post>> getPosts() {
        if(posts.getValue() != null){
            posts.getValue().removeIf(post -> post.isDeleted());
        }
        return posts;
    }
}
