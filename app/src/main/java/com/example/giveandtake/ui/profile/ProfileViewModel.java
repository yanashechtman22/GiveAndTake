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
        List<Post> newAllPosts = new ArrayList<>();
        for (int i = 0; i < allPosts.size(); i++) {
            if (!allPosts.get(i).isDeleted()) {
                newAllPosts.add(allPosts.get(i));
            }

        }
        return newAllPosts;
    }
}