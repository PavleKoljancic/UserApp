package com.example.userapp.activity.nopicture;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.example.userapp.R;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class NoPictureActivity extends AppCompatActivity {

    ImageView imageView;
    Button fromGalaery;
    Button fromCammera;
    Button uploadButton;
    CircularProgressIndicator uploadProgress;
    TextView infoText;
    CropImageOptions cropImageOptions;
    ActivityResultLauncher<CropImageContractOptions> cropImage;
    Bitmap cropped;
    NoPictureController noPictureController;
    boolean backprassed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_picture);
         noPictureController = new NoPictureController(this);
        backprassed=false;
        imageView = findViewById(R.id.userImageUpload);
        fromCammera =findViewById(R.id.fromCammera);
        fromGalaery = findViewById(R.id.fromGalleryBtn);
        uploadButton = findViewById(R.id.uploadBtn);
        uploadProgress = findViewById(R.id.uploadProgress);
        infoText = findViewById(R.id.infoText);

        cropImage = registerForActivityResult(new CropImageContract(), result -> {
            if (result.isSuccessful()) {
                cropped = BitmapFactory.decodeFile(result.getUriFilePath(getApplicationContext(), true));
                noPictureController.setCroppedPhoto();
            }
        });

        cropImageOptions = new CropImageOptions();
        cropImageOptions.fixAspectRatio=true;
        cropImageOptions.aspectRatioY=800;
        cropImageOptions.aspectRatioX=800;
        cropImageOptions.maxCropResultHeight=4000;
        cropImageOptions.maxCropResultWidth=4000;
        cropImageOptions.minCropResultHeight=500;
        cropImageOptions.minCropResultWidth=500;

        fromCammera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noPictureController.fromCammera();
            }
        });

        fromGalaery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noPictureController.fromGallery();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noPictureController.uploadCurrentImage();

            }
        });

    }

    @Override
    protected void onDestroy() {
        this.noPictureController.quitHandlerThread();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        backprassed=true;
        super.onBackPressed();
    }
}