package com.example.giveandtake.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.giveandtake.MyApplication;
import com.example.giveandtake.common.PostsListLoadingState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppModel {
    public static final AppModel instance = new AppModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    MutableLiveData<PostsListLoadingState> postsListLoadingState = new MutableLiveData<>();
    MutableLiveData<List<Post>> postsList = new MutableLiveData<>();
    FirebaseAppModel firebaseAppModel = new FirebaseAppModel();

    public interface CrudPostListener {
        void onComplete(boolean success);
    }

    public interface SaveImageListener {
        void onComplete(String imageUri);
    }

    public interface GetPostsListener {
        void onComplete(List<Post> posts);
    }

    public interface GetPostByIdListener {
        void onComplete(Post post);
    }

    public LiveData<PostsListLoadingState> getPostListLoadingState() {
        return postsListLoadingState;
    }

    public List<Post> getByQuery(String query){
        return AppLocalDB.db.postDao().getByQuery(query);
    }

    public LiveData<List<Post>> getAll() {
        if (postsList.getValue() == null) {
            refreshPostsList();
        }
        return postsList;
    }


    public void refreshPostsList() {
        postsListLoadingState.setValue(PostsListLoadingState.loading);
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("PostsLastUpdateDate", 0);
        firebaseAppModel.getAllPosts(lastUpdateDate, list -> executor.execute(() -> {
            Long lud = 0L;
            Log.d("TAG", "fb returned " + list.size());
            for (Post post : list) {
                AppLocalDB.db.postDao().insertAll(post);
                if (lud < post.getUpdateDate()) {
                    lud = post.getUpdateDate();
                }
            }
            MyApplication.getContext()
                    .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    .edit()
                    .putLong("PostsLastUpdateDate", lud)
                    .commit();
            List<Post> localPostsList = AppLocalDB.db.postDao().getAll();

            postsList.postValue(localPostsList);
            postsListLoadingState.postValue(PostsListLoadingState.loaded);
        }));
    }

    public void addPost(Post newPost, CrudPostListener listener) {
        postsListLoadingState.setValue(PostsListLoadingState.loading);
        firebaseAppModel.addNewPost(newPost, success -> {
            if (success) {
                executor.execute(() -> {
                    AppLocalDB.db.postDao().insert(newPost);
                    //return all data to caller
                    List<Post> localPostsList = AppLocalDB.db.postDao().getAll();
                    postsList.postValue(localPostsList);
                    postsListLoadingState.postValue(PostsListLoadingState.loaded);
                });
            }
            listener.onComplete(true);
        });
    }

    public void editPost(Post post,CrudPostListener listener){
        firebaseAppModel.updatePost(post, success -> {
            if(success){
                executor.execute(() -> {
                    AppLocalDB.db.postDao().update(post);
                    List<Post> localPostsList = AppLocalDB.db.postDao().getAll();
                    postsList.postValue(localPostsList);
                    postsListLoadingState.postValue(PostsListLoadingState.loaded);
                });
            }
            listener.onComplete(true);
        });
        AppLocalDB.db.postDao().update(post);
    }


    public void saveImage(Bitmap imageBitmap, String imageId, SaveImageListener listener) {
        firebaseAppModel.saveImage(imageBitmap, imageId, listener);
    }

    public void getPostById(String postId, GetPostByIdListener listener) {
        firebaseAppModel.getPostById(postId,listener);
    }

    public List<Post> getPostByUserId(String userId) {
        return AppLocalDB.db.postDao().getPostsByUserId(userId);
    }

    public void deletePostByUserId(String postId) {
         AppLocalDB.db.postDao().deleteById(postId);
    }

    public void deletePostById(String postId, CrudPostListener listener) {
        postsListLoadingState.setValue(PostsListLoadingState.loading);
        firebaseAppModel.deletePostById(postId, success -> {
            if(success){
                executor.execute(() -> {
                    AppLocalDB.db.postDao().deleteById(postId);
                    //return all data to caller
                    List<Post> localPostsList = AppLocalDB.db.postDao().getAll();
                    postsList.postValue(localPostsList);
                    postsListLoadingState.postValue(PostsListLoadingState.loaded);
                });
            }
            listener.onComplete(success);
        });
    }


}
