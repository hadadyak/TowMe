package com.example.hadad.towme.Others;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.hadad.towme.Activities.MainActivity;
import com.example.hadad.towme.DynamoDB.DynamoDBManagerTask;
import com.example.hadad.towme.DynamoDB.MyQuery;
import com.example.hadad.towme.R;
import com.example.hadad.towme.Tables.Tow;
import com.facebook.Profile;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.location.LocationListener;


public class GpsService extends Service implements DynamoDBManagerTask.DynamoDBManagerTaskResponse   {
    private static final String TAG = "GPS Service";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 0;
    public static double Latitude;
    public static double Longitude;
    private NotificationManager mNM;

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
//            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            Latitude=location.getLatitude();
            Longitude=location.getLongitude();
            TowSendLocation();

        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }


    /**
     * Show a notification while this service is running.
     */


    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
//        Log.e(TAG, "onCreate notification startes");
//        NotificationGPS.showNotification(this,true);
//        PendingIntent pending = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(),0);


        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(getApplicationContext(),
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }



    public void TowSendLocation(){
        final DynamoDBManagerTask getTow= new DynamoDBManagerTask(this);
        Log.d("GPS service","TowSendLocation ");
        MyQuery<Tow> getQuery = new MyQuery<Tow>(Constants.DynamoDBManagerType.GET_TOW_BY_ID
                , new Tow(Long.parseLong(Profile.getCurrentProfile().getId())));
        getTow.execute(getQuery);
    }

    @Override
    public void DynamoDBManagerTaskResponse(MyQuery myQ) {
        if (myQ.getType() == Constants.DynamoDBManagerType.GET_TOW_BY_ID) {
            myQ.setType(Constants.DynamoDBManagerType.UPDATE_TOW);
            ((MyQuery<Tow>) myQ).getContent().setLatitude(Latitude);
            ((MyQuery<Tow>) myQ).getContent().setLongitude(Longitude);
            DynamoDBManagerTask updateTow = new DynamoDBManagerTask(this);
            Log.d("GPS Service send","Latitude="+Latitude);
            Log.d("GPS Service send","Longitude="+Longitude);
            updateTow.execute(myQ);
        }
    }

}

