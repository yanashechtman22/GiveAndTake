package com.example.giveandtake.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.giveandtake.R;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private ArrayList<Post> postsMock = new ArrayList<>();
    private LiveData<List<Post>> posts;

    public HomeViewModel() {
        postsMock.add(new Post("This is first ad", R.drawable.image1, "bla"));
        postsMock.add(new Post("This is second ad", R.drawable.image2, "bla"));
        posts = AppModel.instance.getAll();
    }

    public ArrayList<Post> getPostsMock(){ return postsMock;}

    public LiveData<List<Post>> getPosts() {
        return posts;
    }
}
