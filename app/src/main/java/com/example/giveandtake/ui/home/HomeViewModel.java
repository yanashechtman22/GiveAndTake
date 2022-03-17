package com.example.giveandtake.ui.home;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HomeViewModel extends ViewModel {

    private LiveData<List<Post>> posts;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HomeViewModel() {
        posts = AppModel.instance.getAll();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts){
        this.posts.getValue().clear();
        this.posts.getValue().addAll(posts);
    }
}
