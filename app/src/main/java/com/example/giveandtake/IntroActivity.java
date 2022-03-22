package com.example.giveandtake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.giveandtake.auth.LoginActivity;
import com.example.giveandtake.model.AppModel;
import com.example.giveandtake.model.AuthenticationModel;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        AppModel.instance.executor.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (AuthenticationModel.instance.isSignedIn()){
                AppModel.instance.mainThread.post(this::toFeedActivity);
            }else{
                AppModel.instance.mainThread.post(this::toLoginActivity);
            }
        });
    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void toFeedActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}