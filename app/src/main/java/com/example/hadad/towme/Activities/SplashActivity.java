package com.example.hadad.towme.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.hadad.towme.DynamoDB.AmazonClientManager;
import com.example.hadad.towme.DynamoDB.DynamoDBManagerTask;
import com.example.hadad.towme.DynamoDB.MyQuery;
import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Others.UserProfile;
import com.example.hadad.towme.Tables.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;

public class SplashActivity extends AppCompatActivity implements DynamoDBManagerTask.DynamoDBManagerTaskResponse{

    CallbackManager callbackManager;
    ProfileTracker mProfileTracker;
    public static AmazonClientManager clientManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        clientManager = new AmazonClientManager(this);
        callbackManager = CallbackManager.Factory.create();
        final DynamoDBManagerTask getUser = new DynamoDBManagerTask(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( Profile.getCurrentProfile() != null && AccessToken.getCurrentAccessToken() != null) {
                    MyQuery<User> query= new MyQuery<User>(Constants.DynamoDBManagerType.GET_USER_BY_ID,new User(Long.parseLong(Profile.getCurrentProfile().getId())));
                    getUser.execute(query);
                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivty.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 100);
    }

    @Override
    public void DynamoDBManagerTaskResponse(MyQuery myQ) {
        if(myQ.getType() == Constants.DynamoDBManagerType.GET_USER_BY_ID) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            UserProfile.setUser((User) myQ.getContent());
            startActivity(intent);
        }else{
            Intent intent = new Intent(SplashActivity.this, LoginActivty.class);
            startActivity(intent);
            finish();
        }
    }
}