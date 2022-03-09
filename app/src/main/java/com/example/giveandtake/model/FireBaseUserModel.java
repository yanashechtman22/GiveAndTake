package com.example.giveandtake.model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;

public class FireBaseUserModel {

    public FireBaseUserModel() {
    }

    public User getUserByEmail(String email) {
        //todo take the user email
        return new User("linoy", "e@gmail.com", "00", "fdfdfsdsd", new ArrayList<Post>());
    }
}
