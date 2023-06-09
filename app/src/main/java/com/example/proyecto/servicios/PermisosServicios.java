package com.example.proyecto.servicios;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import javax.inject.Inject;

import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import lombok.Getter;


@Getter
@Module
@InstallIn(ActivityComponent.class)
public class PermisosServicios {
    private static final String TAG = PermisosServicios.class.getName();

    static public final int PERMISSIONS_REQUEST_READ_CONTACTS = 2002;
    static public final int PERMISSIONS_REQUEST_CAMERA = 1001;
    static public final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1002;
    static public final int PERMISSIONS_REQUEST_GALLERY = 1002;


    private boolean mContactsPermissionGranted;
    private boolean mCameraPermissionGranted;
    private boolean mGalleryPermissionGranted;
    private boolean mReadExternalStoragePermissionGranted;

    private final Context context;

    @Inject
    PermisosServicios(@ApplicationContext Context context) {
        this.context = context;
        mContactsPermissionGranted = checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        mCameraPermissionGranted = checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        mCameraPermissionGranted = checkPermission(Manifest.permission.CAMERA);
        mReadExternalStoragePermissionGranted = checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

    }

    public void getContactsPermission(Activity activity){
        if(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            mContactsPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    public void getGalleryPermission(Activity activity) {
        if(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            mReadExternalStoragePermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_GALLERY);
        }
    }

    public void getCameraPermission(Activity activity) {
        if(checkPermission(Manifest.permission.CAMERA)){
            mCameraPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        }
    }

    public void getReadExternalStoragePermission(Activity activity) {
        if(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
            mReadExternalStoragePermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private boolean checkPermission(String readExternalStorage) {
        /*
         * Request the permission. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        Log.d(TAG, "checkPermission: attempting to get permission for ("+ Manifest.permission.READ_CONTACTS +").");
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: permission "+ Manifest.permission.READ_CONTACTS +" is already granted.");
            return true;
        } else {
            Log.d(TAG, "checkPermission: permission ("+ Manifest.permission.READ_CONTACTS +") not granted, need to request it.");
            return false;
        }
    }

    public boolean ismContactsPermissionGranted() {
        return mContactsPermissionGranted;
    }

    public boolean isMReadExternalStoragePermissionGranted() {return mReadExternalStoragePermissionGranted;}

    public boolean isMCameraPermissionGranted() {
        return mCameraPermissionGranted;
    }

}