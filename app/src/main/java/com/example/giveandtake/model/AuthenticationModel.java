package com.example.giveandtake.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class AuthenticationModel {
    public static final AuthenticationModel instance = new AuthenticationModel();

    FirebaseAuthenticationModel firebaseAuthenticationModel = new FirebaseAuthenticationModel();

    public interface AuthListener {
        void onComplete(FirebaseUser user);

        void onFailure(String message);
    }

    public UserInfo getUserInfo() {
        return firebaseAuthenticationModel.getUserInfo();
    }

    public FirebaseUser getFireBaseUser() {
        return firebaseAuthenticationModel.getFireBaseUser();
    }

    public void registerNewUser(String displayName, String email, String password, Uri imageUrl, AuthListener listener) {
        firebaseAuthenticationModel.registerNewUser(displayName, email, password, imageUrl, listener);
    }

    public void registerNewUser(String displayName, String email, String password, AuthListener listener) {
        firebaseAuthenticationModel.registerNewUser(displayName, email, password, listener);
    }

    public void loginUser(String email, String password, AuthListener listener) {
        firebaseAuthenticationModel.loginUser(email, password, listener);
    }

    public boolean isSignedIn() {
        return firebaseAuthenticationModel.isSignedIn();
    }

    public void logOut() {
        firebaseAuthenticationModel.signOutUser();
    }

}
