package com.example.giveandtake.ui.profile;

import androidx.lifecycle.ViewModel;

import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.Post;

import java.util.List;

public class ProfileViewModel extends ViewModel {

    private List<Post> posts;

    public ProfileViewModel() {
    }

    public List<Post> getPosts(String uuid) {
        return AppModel.instance.getPostByUserId(uuid);
    }

    public void deletePost(String postId) {
        AppModel.instance.deletePostByUserId(postId);
    }
}