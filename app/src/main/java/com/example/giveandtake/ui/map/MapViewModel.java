package com.example.giveandtake.ui.map;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;

import java.util.List;

public class MapViewModel extends ViewModel {

    private final List<Post> posts;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public MapViewModel() {
        posts = AppModel.instance.getByQuery("");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Post> getPosts() {
        return posts;
    }
}
