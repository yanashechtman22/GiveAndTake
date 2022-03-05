package com.example.giveandtake.model;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.Executor;

public class FirebaseAuthenticationModel {
    private FirebaseAuth mAuth;

    public FirebaseAuthenticationModel() {
            mAuth = FirebaseAuth.getInstance();
    }

    public boolean isSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    public void registerNewUser(String displayName, String email, String password, AuthenticationModel.RegisterListener listener){
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

    public void loginUser(String email, String password, AuthenticationModel.LoginListener listener) {
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
