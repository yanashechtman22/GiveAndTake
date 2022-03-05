package com.example.giveandtake;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.giveandtake.auth.LoginActivity;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        /*Model.instance.executor.execute(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Model.instance.isSignedIn()){
                Model.instance.mainThread.post(() -> {
                    toFeedActivity();
                });
            }else{
                Model.instance.mainThread.post(() -> {
                    toFeedActivity();
                });
            }
        });*/

        toLoginActivity();

    }

    private void toLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /*private void toFeedActivity() {
        Intent intent = new Intent(this, DrawerActivity.class);
        startActivity(intent);
        finish();
    }*/
}