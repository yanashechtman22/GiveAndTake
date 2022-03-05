package com.example.giveandtake.model;

import com.google.firebase.auth.FirebaseUser;

public class AuthenticationModel {
    public static final AuthenticationModel instance = new AuthenticationModel();

    FirebaseAuthenticationModel firebaseAuthenticationModel = new FirebaseAuthenticationModel();

    public interface AuthListener {
        void onComplete(FirebaseUser user);
        void onFailure(String message);
    }

    public void registerNewUser(String displayName, String email, String password, AuthListener listener) {
        firebaseAuthenticationModel.registerNewUser(displayName, email, password, listener);
    }

    public void loginUser(String email, String password, AuthListener listener) {
        firebaseAuthenticationModel.loginUser(email,password, listener);
    }

    public boolean isSignedIn() {
        return firebaseAuthenticationModel.isSignedIn();
    }
}
