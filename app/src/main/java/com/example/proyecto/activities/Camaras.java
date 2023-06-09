package com.example.proyecto.activities;

import static java.text.DateFormat.getDateInstance;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.example.proyecto.databinding.ActivityCamarasBinding;
import com.example.proyecto.servicios.PermisosServicios;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class Camaras extends ActividadBasica {
    private static final String TAG = Camaras.class.getName();
    ActivityCamarasBinding binding;

    @Inject
    PermisosServicios permissionService;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_VIDEO_CAPTURE = 2;
    private Uri pictureImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCamarasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        binding.botonCamaravivo.setOnClickListener(v -> {
            if (!permissionService.isMCameraPermissionGranted()) {
                permissionService.getCameraPermission(this);
            } else {
                takePictureOrVideo();
            }
        });
        binding.botonCamara.setOnClickListener(v -> {
            if (!permissionService.isMReadExternalStoragePermissionGranted()) {
                permissionService.getReadExternalStoragePermission(this);
            } else {
                startGallery();
            }
        });
    }

    private void takePictureOrVideo() {
            dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Create temp file for image result
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String timeStamp = dateFormat.format(new Date());
        String imageFileName = String.format("%s.jpg", timeStamp);
        File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/" + imageFileName);
        pictureImagePath = FileProvider.getUriForFile(this,
                "com.example.proyecto.fileprovider",
                imageFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureImagePath);
        takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermisosServicios.PERMISSIONS_REQUEST_CAMERA) {
            permissionService.getCameraPermission(this);
            if (permissionService.isMCameraPermissionGranted() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                takePictureOrVideo();
            else {
                alerts.indefiniteSnackbar(binding.getRoot(), "No se pudo obtener el pemiso para acceder a la camara");
            }
            if (permissionService.isMReadExternalStoragePermissionGranted() &&
                    requestCode == PermisosServicios.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGallery();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            binding.preview.removeAllViews();
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            // Low quality image in bitmap
//            imageView.setImageBitmap((android.graphics.Bitmap) extras.get("data"));
            // High quality image in pictureImagePath
            imageView.setImageURI(pictureImagePath);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setAdjustViewBounds(true);
            binding.preview.addView(imageView);
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK && data != null) {
            binding.preview.removeAllViews();
            Log.d(TAG, "onActivityResult: Video URI: " + data.getData());
            VideoView videoView = new VideoView(this);
            videoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            Uri videoUri = data.getData();
            videoView.setVideoURI(videoUri);
            videoView.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);
            videoView.setMediaController(new MediaController(this));
            videoView.start();
            videoView.setZOrderOnTop(true);
            binding.preview.addView(videoView);
        }
        else if (requestCode == PermisosServicios.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE && data != null) {
            Uri srcUri = data.getData();
            binding.preview.removeAllViews();

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imageView.setImageURI(srcUri);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setAdjustViewBounds(true);
                binding.preview.addView(imageView);
            }
        }
    private void startGallery() {
        Intent intentPick = new Intent(Intent.ACTION_PICK);

            intentPick.setType("image/*");
        startActivityForResult(intentPick, PermisosServicios.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }
}
