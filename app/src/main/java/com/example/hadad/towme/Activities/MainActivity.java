package com.example.hadad.towme.Activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.hadad.towme.Activities.TowListFragment.OnListFragmentInteractionListener;
import com.example.hadad.towme.Activities.ButtonsFragments.OnButtonFragmentInteractionListener;
import com.example.hadad.towme.DynamoDB.AmazonClientManager;
import com.example.hadad.towme.R;
import com.example.hadad.towme.dummy.DummyContent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,OnListFragmentInteractionListener,OnButtonFragmentInteractionListener{

    public static AmazonClientManager clientManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clientManager = new AmazonClientManager(this);
        Fragment mapFrag = new TowMapFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_google_map,mapFrag).commit();
        Fragment listFrag = new TowListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_list_tow,listFrag).commit();
        Fragment Buttons = new ButtonsFragments();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_buttons,Buttons).commit();
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
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Log.d("got click","onListFragmentInteraction");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
