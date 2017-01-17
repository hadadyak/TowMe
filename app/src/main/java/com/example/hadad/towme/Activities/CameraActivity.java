package com.example.hadad.towme.Activities;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.hadad.towme.Others.CameraPreview;
import com.example.hadad.towme.R;

public class CameraActivity extends Activity {
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Camera mCamera;
    int idcam=0;
    private CameraPreview mPreview;
    FrameLayout preview = null;
    Camera.Parameters params;
    public static Boolean isFlashModeON = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Log.d("onCreate", "omCreate");
        // Create an instance of Camera
        if(checkCameraHardware(this)) {//check if there is a camera
            openCam();// go to open cam
        }
        else {
            Toast.makeText(this,
                    "No camera in this device", Toast.LENGTH_LONG)
                    .show();
        }
    }
    @Override
    protected void onPause() {
        Log.d("onPause", "onPause");
        super.onPause();
        closeCam();
    }
    @Override
    protected void onResume() {
        Log.d("onResume", "onResume");
        super.onResume();
        openCam();
    }


//                                    --*******FUNCTIONS*******--
    //                                      --***********--


    //To Check If There Is Camera On The Device
    //--------------------------------------------------
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    //--------------------------------------------------

    // To Open The Camera
    //--------------------------------------------------
    public Camera getCameraInstance(){
        Camera cam = null;
        try {
            cam = Camera.open(idcam); // attempt to get a Camera instance
        }
        catch (Exception err){
            // Camera is not available (in use or does not exist)
            Toast.makeText(this,
                    err.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return cam; // returns null if camera is unavailable
    }
    //--------------------------------------------------


    private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null){
                Log.d("cameraA", "Error");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("cameraA", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("cameraA", "Error accessing file: " + e.getMessage());
            }
            camera.startPreview();
        }
    };
/////-----------------The-File-Creating----------------------------
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "asaf pic");//add sub directory inside DIRECTORY_PICTURES (where you want to save the pic)
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("ActivityCustomCamera", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }


    //==============Opening and Closing Function For The Camera================
    public void openCam()
    {
        if(mCamera == null){	//after pause, must restart camera and preview
            //mCamera.setPreviewCallback(null);
            mCamera.open(0);
            setContentView(R.layout.activity_camera);
            mCamera = getCameraInstance();
            if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                mCamera.setDisplayOrientation(90);
            }

            // Create our Preview view and set it as the content of our activity.
            mPreview = new CameraPreview(this, mCamera);
            preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);


            //                          ====BUTTONS!====
            //=======================================================================
            ImageButton captureButton = (ImageButton) findViewById(R.id.btn_capture);
            captureButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // get an image from the camera
                            mCamera.takePicture(null, null, mPicture);
                        }
                    }
            );

            final ImageButton flashModeButton = (ImageButton) findViewById(R.id.btn_flash_mode);
            flashModeButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // set state by flipping it and set button icon
                            params = mCamera.getParameters();
                            if (isFlashModeON == false) {//need to turn on flash
                                isFlashModeON = true;
                                params.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
                                flashModeButton.setImageResource(R.drawable.flash_on_icon);
                                flashModeButton.setVisibility(View.VISIBLE);

                            } else {//need to turn off flash
                                isFlashModeON = false;
                                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                                flashModeButton.setImageResource(R.drawable.flash_off_icon);
                                flashModeButton.setVisibility(View.VISIBLE);

                            }
                            mCamera.setParameters(params);
                        }
                    }
            );

            Button switching = (Button)findViewById(R.id.swapcam);
            switching.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    closeCam();
                    if(idcam==Camera.CameraInfo.CAMERA_FACING_BACK)
                        idcam=Camera.CameraInfo.CAMERA_FACING_FRONT;
                    else
                        idcam= Camera.CameraInfo.CAMERA_FACING_BACK;
                    openCam();
                }
            });
        }
        //=======================================================================
    }

    public void closeCam()
    {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera = null;
        }
    }
}


