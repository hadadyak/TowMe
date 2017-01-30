package com.example.hadad.towme.Activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.hadad.towme.DynamoDB.AmazonClientManager;
import com.example.hadad.towme.DynamoDB.DynamoDBManagerTask;
import com.example.hadad.towme.DynamoDB.MyQuery;
import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Others.GpsService;
import com.example.hadad.towme.Others.TowList;
import com.example.hadad.towme.Others.UserProfile;
import com.example.hadad.towme.R;
import com.example.hadad.towme.Tables.Tow;
import com.example.hadad.towme.Tables.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity implements DynamoDBManagerTask.DynamoDBManagerTaskResponse {

    CallbackManager callbackManager;
    ProfileTracker fbProfileTracker;
    public static AmazonClientManager clientManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        clientManager = new AmazonClientManager(this);
        callbackManager = CallbackManager.Factory.create();
        DynamoDBManagerTask TaskTow = new DynamoDBManagerTask(this);
        final DynamoDBManagerTask TaskTowInsert = new DynamoDBManagerTask(this);

        TaskTow.execute(new MyQuery<ArrayList<Tow>>(Constants.DynamoDBManagerType.GET_ACTIVE_TOWS, null));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Profile.getCurrentProfile() != null && AccessToken.getCurrentAccessToken() != null) {
                    MyQuery<Tow> queryTow = new MyQuery<Tow>(Constants.DynamoDBManagerType.GET_TOW_BY_ID,
                            new Tow(Long.parseLong(Profile.getCurrentProfile().getId())));
                    TaskTowInsert.execute(queryTow);

                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivty.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 100);
    }


    @Override
    public void DynamoDBManagerTaskResponse(MyQuery myQ) {
        switch (myQ.getType()) {
            case GET_TOW_BY_ID:
                UserProfile.setIsTow(true);
                startService(new Intent(this, GpsService.class));
            case TOW_NON_EXIST:
                final DynamoDBManagerTask getUser = new DynamoDBManagerTask(this);
                MyQuery<User> query = new MyQuery<User>(Constants.DynamoDBManagerType.GET_USER_BY_ID,
                        new User(Long.parseLong(Profile.getCurrentProfile().getId())));
                getUser.execute(query);
                break;
            case GET_USER_BY_ID:
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                UserProfile.setUser((User) myQ.getContent());
                startActivity(intent);
                break;
            case GET_ACTIVE_TOWS:
                TowList.hardCopy((ArrayList<Tow>) myQ.getContent());
                UserProfile.setIsTow(false);
        }
    }
}