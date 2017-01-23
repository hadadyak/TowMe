package com.example.hadad.towme.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hadad.towme.Activities.TowListFragment.OnListFragmentInteractionListener;
import com.example.hadad.towme.Activities.ButtonsFragments.OnButtonFragmentInteractionListener;
import com.example.hadad.towme.DynamoDB.AmazonClientManager;
import com.example.hadad.towme.DynamoDB.DynamoDBManagerTask;
import com.example.hadad.towme.DynamoDB.MyQuery;
import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Others.OnSwipeTouchListener;
import com.example.hadad.towme.R;
import com.example.hadad.towme.Tables.Tow;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,OnListFragmentInteractionListener{

    public static AmazonClientManager clientManager;
    private Constants.MainActivityState State = Constants.MainActivityState.DIVIDED;

    private float FragBtY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientManager = new AmazonClientManager(this);
        Fragment mapFrag = new TowMapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_google_map,mapFrag).commit();
        Fragment listFrag = new TowListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_list_tow,listFrag).commit();

    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        try{
            map.setMyLocationEnabled(true);
        }catch (SecurityException e){

        }
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onListFragmentInteraction(int pos) {
        Intent intent = new Intent(MainActivity.this, TowProfile.class);
        intent.putExtra("position",pos);
        startActivity(intent);
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        super.onWindowFocusChanged(hasFocus);

        if(hasFocus) {
            final RelativeLayout BtLayout = (RelativeLayout)findViewById(R.id.buttons_layout);
            FragBtY = BtLayout.getY();
        }

        OnSwipeTouchListener swipe = new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeTop() {
                swipeTop();
            }
            public void onSwipeBottom() {
                swipeBottom();
            }

        };

        LinearLayout layout = (LinearLayout)findViewById(R.id.main_layout);
        Button rank = (Button) findViewById(R.id.rank_bt);
        Button distance = (Button) findViewById(R.id.dist_bt);
        Button price = (Button) findViewById(R.id.price_bt);


        price.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        layout.setOnTouchListener(swipe);
        price.setOnTouchListener(swipe);
        distance.setOnTouchListener(swipe);
        rank.setOnTouchListener(swipe);

    }

    private void swipeTop(){
        final RelativeLayout BtLayout = (RelativeLayout)findViewById(R.id.buttons_layout);
        final LinearLayout ListLayout = (LinearLayout)findViewById(R.id.frag_list_tow);
        final LinearLayout MapLayout = (LinearLayout)findViewById(R.id.frag_google_map);
        Animation b = new TranslateAnimation(0.0f, 0.0f, 0.0f, -FragBtY);
        if(State == Constants.MainActivityState.MAP) {
            b.setAnimationListener(new TranslateAnimation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LinearLayout.LayoutParams MapParams = (LinearLayout.LayoutParams) MapLayout.getLayoutParams();
                    MapParams.weight = 4;
                    MapLayout.setLayoutParams(MapParams);

                    LinearLayout.LayoutParams ListParams = (LinearLayout.LayoutParams) ListLayout.getLayoutParams();
                    ListParams.weight = 4;
                    ListLayout.setLayoutParams(ListParams);

                }
            });
            State = Constants.MainActivityState.DIVIDED;
        }else if(State == Constants.MainActivityState.DIVIDED){
            b.setAnimationListener(new TranslateAnimation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LinearLayout.LayoutParams MapParams = (LinearLayout.LayoutParams) MapLayout.getLayoutParams();
                    MapParams.weight = 1;
                    MapLayout.setLayoutParams(MapParams);

                    LinearLayout.LayoutParams ListParams = (LinearLayout.LayoutParams) ListLayout.getLayoutParams();
                    ListParams.weight = 7;
                    ListLayout.setLayoutParams(ListParams);

                }
            });
            State = Constants.MainActivityState.LIST;

        }
        b.setDuration(500);
        b.setStartOffset(300);
        b.setRepeatCount(0);
        b.setRepeatMode(Animation.RESTART);
        BtLayout.startAnimation(b);
        ListLayout.startAnimation(b);
        MapLayout.startAnimation(b);
    }


    private void swipeBottom(){
        final RelativeLayout BtLayout = (RelativeLayout)findViewById(R.id.buttons_layout);
        final LinearLayout ListLayout = (LinearLayout)findViewById(R.id.frag_list_tow);
        final LinearLayout MapLayout = (LinearLayout)findViewById(R.id.frag_google_map);
        Animation a = new TranslateAnimation(0.0f, 0.0f, 0.0f, FragBtY);
        if(State == Constants.MainActivityState.DIVIDED) {

            a.setAnimationListener(new TranslateAnimation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LinearLayout.LayoutParams MapParams = (LinearLayout.LayoutParams) MapLayout.getLayoutParams();
                    MapParams.weight = 7;
                    MapLayout.setLayoutParams(MapParams);

                    LinearLayout.LayoutParams ListParams = (LinearLayout.LayoutParams) ListLayout.getLayoutParams();
                    ListParams.weight = 1;
                    ListLayout.setLayoutParams(ListParams);

                }
            });
            State = Constants.MainActivityState.MAP;
        }else if(State == Constants.MainActivityState.LIST){
            a.setAnimationListener(new TranslateAnimation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LinearLayout.LayoutParams MapParams = (LinearLayout.LayoutParams) MapLayout.getLayoutParams();
                    MapParams.weight = 4;
                    MapLayout.setLayoutParams(MapParams);

                    LinearLayout.LayoutParams ListParams = (LinearLayout.LayoutParams) ListLayout.getLayoutParams();
                    ListParams.weight = 4;
                    ListLayout.setLayoutParams(ListParams);

                }
            });
            State = Constants.MainActivityState.DIVIDED;

        }

        a.setDuration(500);
        a.setStartOffset(300);
        a.setRepeatMode(Animation.RESTART);
        a.setRepeatCount(0);
        BtLayout.startAnimation(a);
        ListLayout.startAnimation(a);
        MapLayout.startAnimation(a);
    }

}
