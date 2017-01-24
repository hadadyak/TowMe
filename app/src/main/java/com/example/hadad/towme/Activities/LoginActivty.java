package com.example.hadad.towme.Activities;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hadad.towme.DynamoDB.DynamoDBManagerTask;
import com.example.hadad.towme.DynamoDB.MyQuery;
import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Others.UserProfile;
import com.example.hadad.towme.R;
import com.example.hadad.towme.Tables.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class LoginActivty extends AppCompatActivity implements DynamoDBManagerTask.DynamoDBManagerTaskResponse{

    private TextView info;
    ;
    private CallbackManager callbackManager;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login_activty);
        final DynamoDBManagerTask isRegisterd = new DynamoDBManagerTask(this);
        final ImageView guido= (ImageView) findViewById(R.id.guido);
        final LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                guido.setVisibility(View.VISIBLE);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                // Application code
                                try {

                                    User newUser = new User();
                                    newUser.setId(Long.parseLong(object.getString("id")));
                                    newUser.setFirstName(object.getString("first_name"));
                                    newUser.setLastName(object.getString("last_name"));
                                    newUser.setMail(object.getString("email"));
                                    MyQuery<User> query = new MyQuery<User>(Constants.DynamoDBManagerType.GET_USER_BY_ID,newUser);
                                    isRegisterd.execute(query);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });
    }
    public static boolean resume_log_out = false;
    @Override
    protected void onResume(){
        super.onResume();
        if(resume_log_out) {
            ((LoginButton) findViewById(R.id.login_button)).setVisibility(View.VISIBLE);
            ((ImageView) findViewById(R.id.guido)).setVisibility(View.INVISIBLE);
            resume_log_out=!resume_log_out;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void DynamoDBManagerTaskResponse(MyQuery myQ) {
        if(myQ.getType() == Constants.DynamoDBManagerType.USER_NON_EXIST){
            final DynamoDBManagerTask insertUser = new DynamoDBManagerTask(this);
            myQ.setType(Constants.DynamoDBManagerType.INSERT_USER);
            insertUser.execute(myQ);
            UserProfile.setUser((User)myQ.getContent());
            Intent intent = new Intent(LoginActivty.this, PhoneNumberActivity.class);
            startActivity(intent);

        }else if(myQ.getType() == Constants.DynamoDBManagerType.GET_USER_BY_ID){
            UserProfile.setUser((User)myQ.getContent());
            Intent intent = new Intent(LoginActivty.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
