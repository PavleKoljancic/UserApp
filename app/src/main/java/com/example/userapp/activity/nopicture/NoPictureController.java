package com.example.userapp.activity.nopicture;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;

import com.canhub.cropper.CropImageContractOptions;
import com.example.userapp.activity.ActivityController;
import com.example.userapp.activity.main.MainActivity;
import com.example.userapp.models.TicketRequest;
import com.example.userapp.models.TicketRequestResponse;
import com.example.userapp.models.User;
import com.example.userapp.models.UserTicket;
import com.example.userapp.datamodel.user.UserDataChangeSubscriber;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
class NoPictureController extends ActivityController implements UserDataChangeSubscriber {

     NoPictureActivity noPictureActivity;
     NoPictureModel  noPictureModel;

    public NoPictureController(NoPictureActivity noPictureActivity)
    {   super("HANDLER THREAD NO PICTURE ACTIVITY");
        this.noPictureActivity= noPictureActivity;
        this.noPictureModel = new  NoPictureModel ();
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
        this.noPictureModel.setPictureBytes(stream.toByteArray());
    }

    public void uploadCurrentImage() {
        noPictureActivity.uploadProgress.setVisibility(View.VISIBLE);
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(()-> {
            Boolean result=null;
            try {
                 result =noPictureModel.uploadCurrentPicture();
                 if(result)
                     this.userDataModel.updateUser(this.noPictureModel.loadUser());
            } catch (JSONException|IOException e) {

            }
            finally {
                noPictureActivity.runOnUiThread(()->cleanUIonUploadFinished());
            }
            if(result==null||result==false)
                noPictureActivity.runOnUiThread(()->noPictureActivity.infoText.setText("Desila se greška, pokušajte ponovo"));


        });

    }
    private void cleanUIonUploadFinished() {
        noPictureActivity.uploadProgress.setProgress(100,true);
        noPictureActivity.uploadProgress.setVisibility(View.INVISIBLE);
        noPictureActivity.uploadProgress.setProgress(50,false);

    }

    @Override
    public void onUserDataChanged(User user, HashSet<UserTicket> userTickets, HashSet<TicketRequestResponse> ticketRequestResponses, HashSet<TicketRequest> unprocessedTicketRequest, Bitmap userProfilePicture) {
        if(user!=null&&user.getPictureHash()!=null) {
            this.userDataModel.unsubscribeToDataChange(this);
            this.noPictureActivity.runOnUiThread(() -> {

                this.noPictureActivity.startActivity(new Intent(this.noPictureActivity, MainActivity.class));
                this.noPictureActivity.finish();

            });
        }
    }
}
