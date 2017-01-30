package com.example.hadad.towme.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hadad.towme.DynamoDB.DynamoDBManagerTask;
import com.example.hadad.towme.DynamoDB.MyQuery;
import com.example.hadad.towme.Manifest;
import com.example.hadad.towme.Others.Constants;
import com.example.hadad.towme.Others.TowList;
import com.example.hadad.towme.Others.UserProfile;
import com.example.hadad.towme.R;
import com.example.hadad.towme.Tables.Tow;
import com.example.hadad.towme.Tables.User;
import com.facebook.Profile;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LOCATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TowMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TowMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TowMapFragment extends Fragment implements OnMapReadyCallback,DynamoDBManagerTask.DynamoDBManagerTaskResponse  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters
    private GoogleMap googleMap;
    private MapView mapView;
    private OnFragmentInteractionListener mListener;
    private static final int REQUEST_FINE_LOCATION = 11;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 11;
    private LatLng MarkerPoint;
    private ArrayList<LatLng> Markers = new ArrayList<LatLng>();
    private LocationManager  locationManager;
    private double Latitude;
    private double Longitude;

    public TowMapFragment() {
        // Required empty public constructor
    }

    public static TowMapFragment newInstance() {
        TowMapFragment fragment = new TowMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void TowSendLocation(){
        final DynamoDBManagerTask getTow= new DynamoDBManagerTask(this);
        Log.d("TowSendLocation","start");
        MyQuery<Tow> getQuery = new MyQuery<Tow>(Constants.DynamoDBManagerType.GET_TOW_BY_ID
                , new Tow(Long.parseLong(Profile.getCurrentProfile().getId())));
        getTow.execute(getQuery);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        public void onMyLocationChange(Location location) {
//            Log.d("change","Latitude="+location.getLatitude()+
//                    "Longitude="+location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    // do stuff
                    Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                            R.mipmap.ic_launcher);
                    googleMap.clear();
                    for (Tow tow: TowList.ITEMS) {
                        MarkerOptions mp = new MarkerOptions();
                        mp.icon(BitmapDescriptorFactory.fromBitmap(icon));
                        LatLng loc=new LatLng(tow.getLatitude(),tow.getLongitude());
                        Log.e("fragment mp loc=", "" + loc);
                        mp.position(loc);
                        mp.visible(true);
                        googleMap.addMarker(mp);
                    }
                }
            }, 0,5, TimeUnit.SECONDS);

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        List<String> providers =locationManager.getAllProviders();
        Location location = null;
        int i = 0;
        do {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                location = locationManager.getLastKnownLocation(providers.get(i));
            }
            i++;
        }
        while (location == null && i < providers.size()) ;
        if (location == null) {
            Latitude = 0;//location.getLatitude();
            Longitude = 0;//location.getLongitude();
        } else {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Latitude = location.getLatitude();
                Longitude = location.getLongitude();
            }

            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            public void onProviderEnabled(String s) {
            }

            public void onProviderDisabled(String s) {

            }
        };

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tow_map, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void setMyLocationEnabled() {
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //TODO:
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        googleMap.setMyLocationEnabled(true);

                } else
                    Toast.makeText(getActivity(), "Coarse location has not been enabled", Toast.LENGTH_SHORT).show();
                if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        googleMap.setMyLocationEnabled(true);
                } else
                    Toast.makeText(getActivity(), "Fine location has not been enabled", Toast.LENGTH_SHORT).show();

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this.getActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                11);
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        Log.d("onMapReady", "start");
        Log.d("isTow="," "+UserProfile.isTow());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.262562, 34.802105), 14.0f));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setIndoorEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);


        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("permission","error");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        googleMap.setMyLocationEnabled(true);
        LatLng loc;
        if (this.googleMap .getMyLocation()!= null) {
            loc = new LatLng(this.googleMap.getMyLocation().getLatitude(), this.googleMap.getMyLocation().getLongitude());
        }else if(MarkerPoint!=null){
            Log.d("onMapReady MarkerPoint="," "+MarkerPoint);
            loc = MarkerPoint;
        }
        else {
            Latitude=31.2594847;
            Longitude=34.8059749;
            loc = new LatLng(Latitude, Longitude);
            Log.d("onMapReady", "googleMap is null");
        }
        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.mipmap.ic_launcher);
        for (Tow tow: TowList.ITEMS) {
            MarkerOptions mp = new MarkerOptions();
            mp.icon(BitmapDescriptorFactory.fromBitmap(icon));
            loc=new LatLng(tow.getLatitude(),tow.getLongitude());
            Log.e("fragment mp loc=", "" + loc);
            mp.position(loc);
            mp.visible(true);
            googleMap.addMarker(mp);
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        googleMap.setOnMyLocationChangeListener(myLocationChangeListener);
        Log.d("onMapReady ", " after setting listner");

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }
    @Override
    public void DynamoDBManagerTaskResponse(MyQuery myQ) {
        if (myQ.getType() == Constants.DynamoDBManagerType.GET_TOW_BY_ID) {
            myQ.setType(Constants.DynamoDBManagerType.UPDATE_TOW);
            ((MyQuery<Tow>) myQ).getContent().setLatitude(Latitude);
            ((MyQuery<Tow>) myQ).getContent().setLongitude(Longitude);
            DynamoDBManagerTask inserUser = new DynamoDBManagerTask(this);
            Log.d("mapResponse","Latitude="+Latitude);
            Log.d("mapResponse","Longitude="+Longitude);
            inserUser.execute(myQ);
        }
    }

}

