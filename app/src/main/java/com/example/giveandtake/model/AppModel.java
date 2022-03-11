package com.example.giveandtake.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
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
    MutableLiveData<List<Post>> postsList = new MutableLiveData<List<Post>>();
    FirebaseAppModel firebaseAppModel = new FirebaseAppModel();

    public interface AddAdListener{
        void onComplete();
        //void onFailure();
    }

    public interface SaveImageListener{
        void onComplete(String imageUri);
    }

    public interface GetPostsListener {
        void onComplete(List<Post> posts);
    }

    public interface GetPostByIdListener {
        void onComplete(Post post);
    }

    public LiveData<PostsListLoadingState> getStudentListLoadingState() {
        return postsListLoadingState;
    }

    public LiveData<List<Post>> getAll() {
        if (postsList.getValue() == null) {
            refreshPostsList();
        }
        return postsList;
    }


    public void refreshPostsList() {
        postsListLoadingState.setValue(PostsListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = MyApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("PostsLastUpdateDate", 0);

        // firebase get all updates since lastLocalUpdateDate
        firebaseAppModel.getAllPosts(lastUpdateDate, list -> {
            // add all records to the local db
            executor.execute(() -> {
                Long lud = new Long(0);
                Log.d("TAG", "fb returned " + list.size());
                for (Post post : list) {
                    AppLocalDB.db.postDao().insertAll(post);
                    if (lud < post.getUpdateDate()) {
                        lud = post.getUpdateDate();
                    }
                }
                // update last local update date
                MyApplication.getContext()
                        .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                        .edit()
                        .putLong("PostsLastUpdateDate", lud)
                        .commit();

                //return all data to caller
                List<Post> localPostsList = AppLocalDB.db.postDao().getAll();
                postsList.postValue(localPostsList);
                postsListLoadingState.postValue(PostsListLoadingState.loaded);
            });
        });
    }

    public void addPost(Post newPost, AddAdListener listener){
        firebaseAppModel.addNewPost(newPost, ()-> listener.onComplete());
    }

    public void saveImage(Bitmap imageBitmap, String imageId, SaveImageListener listener) {
        firebaseAppModel.saveImage(imageBitmap,imageId,listener);
    }

    public Post getPostById(String noteId, GetPostByIdListener listener) {
        firebaseAppModel.getNoteById(noteId,listener);
        return null;
    }



}
