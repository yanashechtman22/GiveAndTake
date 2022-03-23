package com.example.giveandtake.ui.profile;

import androidx.lifecycle.ViewModel;

import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {

    public ProfileViewModel() {
    }

    public List<Post> getPosts(String uuid) {
        List<Post> allPosts = AppModel.instance.getPostByUserId(uuid);
        return allPosts;
    }
}