package com.example.userapp.activity.nopicture;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.canhub.cropper.CropImageContractOptions;
import com.example.userapp.activity.AbstractViewController;
import com.example.userapp.activity.main.MainActivity;
import com.example.userapp.models.Document;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.datamodel.user.UserDataChangeSubscriber;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

class NoPictureController extends AbstractViewController implements UserDataChangeSubscriber {

     NoPictureActivity noPictureActivity;
     NoPictureApiDecorator noPictureApiDecorator;

    public NoPictureController(NoPictureActivity noPictureActivity)
    {   super("HANDLER THREAD NO PICTURE ACTIVITY");
        this.noPictureActivity= noPictureActivity;
        this.noPictureApiDecorator = new NoPictureApiDecorator();
        this.userDataModel.subscribeToDataChange(this);
    }

    void fromCammera()
    {
        noPictureActivity.cropImageOptions.imageSourceIncludeGallery = false;
        noPictureActivity.cropImageOptions.imageSourceIncludeCamera = true;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(null, noPictureActivity.cropImageOptions);
        noPictureActivity.cropImage.launch(cropImageContractOptions);
    }

    void fromGallery()
    {
        noPictureActivity.cropImageOptions.imageSourceIncludeGallery = true;
        noPictureActivity.cropImageOptions.imageSourceIncludeCamera = false;
        CropImageContractOptions cropImageContractOptions = new CropImageContractOptions(null, noPictureActivity.cropImageOptions);
        noPictureActivity.cropImage.launch(cropImageContractOptions);
    }

    public void setCroppedPhoto() {
        noPictureActivity.imageView.setImageBitmap(noPictureActivity.cropped);
        noPictureActivity.imageView.setVisibility(View.VISIBLE);
        noPictureActivity.infoText.setVisibility(View.VISIBLE);
        noPictureActivity.uploadButton.setVisibility(View.VISIBLE);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        noPictureActivity.cropped.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        this.noPictureApiDecorator.setPictureBytes(stream.toByteArray());
    }

    public void uploadCurrentImage() {
        noPictureActivity.uploadProgress.setVisibility(View.VISIBLE);
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(()-> {
            Boolean result=null;
            try {
                 result = noPictureApiDecorator.uploadCurrentPicture();
                 if(result)
                     this.userDataModel.updateUser(this.noPictureApiDecorator.loadUser(),this.noPictureApiDecorator.loadUserKey());
            } catch (JSONException|IOException e) {

            }
            finally {
                if(noPictureActivity.backprassed)
                    Toast.makeText(noPictureActivity, "Slika je uspješno postavljena.", Toast.LENGTH_LONG).show();
                else
                    noPictureActivity.runOnUiThread(()->cleanUIonUploadFinished());
            }
            if(result==null||result==false)
                if(noPictureActivity.backprassed)
                    Toast.makeText(noPictureActivity, "Desila se greška pri postavljanju slike.", Toast.LENGTH_LONG).show();
                    else
                    noPictureActivity.runOnUiThread(()->noPictureActivity.infoText.setText("Desila se greška, pokušajte ponovo"));


        });

    }
    private void cleanUIonUploadFinished() {
        noPictureActivity.uploadProgress.setProgress(100,true);
        noPictureActivity.uploadProgress.setVisibility(View.INVISIBLE);
        noPictureActivity.uploadProgress.setProgress(50,false);

    }

    @Override
    public void onUserDataChanged(User user, HashSet<UserTicket> userTickets, Bitmap userProfilePicture, List<Document> userDocuments) {
        if(user!=null&&user.getPictureHash()!=null) {
            this.userDataModel.unsubscribeToDataChange(this);
            if(!noPictureActivity.backprassed)
            this.noPictureActivity.runOnUiThread(() -> {

                this.noPictureActivity.startActivity(new Intent(this.noPictureActivity, MainActivity.class));
                this.noPictureActivity.finish();

            });
        }
    }
}
