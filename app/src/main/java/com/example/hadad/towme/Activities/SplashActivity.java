package com.example.hadad.towme.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;

public class SplashActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ProfileTracker fbProfileTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        fbProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // User logged in or changed profile
            }
        };

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (AccessToken.getCurrentAccessToken() != null) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }else {
                    intent = new Intent(SplashActivity.this, LoginActivty.class);
                }
                startActivity(intent);
                finish();
            }
        }, 100);
    }

}