package com.example.giveandtake.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppModel {
    public static final AppModel instance = new AppModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    FirebaseAppModel modelFirebase = new FirebaseAppModel();
}
