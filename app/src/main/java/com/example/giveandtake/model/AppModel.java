package com.example.giveandtake.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppModel {
    public static final AppModel instance = new AppModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    FirebaseAppModel firebaseAppModel = new FirebaseAppModel();

    public interface AddAdListener{
        void onComplete();
        //void onFailure();
    }

    public interface SaveImageListener{
        void onComplete(Uri metadata);
    }

    public void addAd(Ad newAd, AddAdListener listener){
        firebaseAppModel.addNewAd(newAd, ()-> listener.onComplete());
    }

    public void saveImage(Bitmap imageBitmap, String imageId, SaveImageListener listener) {
        firebaseAppModel.saveImage(imageBitmap,imageId,listener);
    }



}
