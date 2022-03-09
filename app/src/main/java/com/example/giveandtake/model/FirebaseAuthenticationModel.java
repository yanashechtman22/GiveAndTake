package com.example.giveandtake.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseAuthenticationModel {
    private FirebaseAuth mAuth;

    public FirebaseAuthenticationModel() {
            mAuth = FirebaseAuth.getInstance();
    }

    public boolean isSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public UserInfo getUserInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user;
    }

    public void registerNewUser(String displayName, String email, String password, AuthenticationModel.AuthListener listener){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest.Builder builder = new UserProfileChangeRequest.Builder();
                        builder.setDisplayName(displayName);
                        user.updateProfile(builder.build());
                        listener.onComplete(user);
                    } else {
                        Exception ex = task.getException();
                        Log.w(TAG, "createUserWithEmail:failure", ex);
                        listener.onFailure(ex.getMessage());
                    }
                });
    }

    public void loginUser(String email, String password, AuthenticationModel.AuthListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        listener.onComplete(user);
                    } else {
                        Exception ex = task.getException();
                        Log.w(TAG, "signInWithEmail:failure", ex);
                        listener.onFailure(ex.getMessage());
                    }
                });

    }
}
