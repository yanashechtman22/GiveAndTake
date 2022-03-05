package com.example.giveandtake.model;

import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;

import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AuthenticationModel {
    public static final AuthenticationModel instance = new AuthenticationModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());

    FirebaseAuthenticationModel firebaseAuthenticationModel = new FirebaseAuthenticationModel();

    public interface RegisterListener{
        void onComplete(FirebaseUser user);
        void onFailure(String message);
    }

    public interface LoginListener{
        void onComplete(FirebaseUser user);
        void onFailure(String message);
    }

    public void registerNewUser(String displayName, String email, String password, RegisterListener listener) {
        firebaseAuthenticationModel.registerNewUser(displayName, email, password, listener);
    }

    public void loginUser(String email, String password, LoginListener listener) {
        firebaseAuthenticationModel.loginUser(email,password, listener);
    }

    public boolean isSignedIn() {
        return firebaseAuthenticationModel.isSignedIn();
    }
}
