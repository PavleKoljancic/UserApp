package com.example.userapp.activity.nopicture;

import com.example.userapp.activity.ActivityModel;
import com.example.userapp.models.User;
import com.example.userapp.retrofit.RetrofitService;
import com.example.userapp.retrofit.UserAPI;
import com.example.userapp.singeltons.TokenManager;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

class NoPictureModel extends ActivityModel {


     public void setPictureBytes(byte[] pictureBytes) {
         this.pictureBytes = pictureBytes;
     }

     byte [] pictureBytes;
     NoPictureModel()
     {

        this.pictureBytes=null;
     }

     Boolean uploadCurrentPicture() throws JSONException, IOException {
         if(this.pictureBytes==null||this.pictureBytes.length==0)
             return false;
         RequestBody requestFile =
                 RequestBody.create(
                         MediaType.parse("image/jpg"), this.pictureBytes

                 );
         MultipartBody.Part body =
                 MultipartBody.Part.createFormData("profile_pic", "Picture.png", requestFile);


         Response<Boolean> resultImage = api.uploadProfilePicture(body, TokenManager.getInstance().getId(), TokenManager.bearer() + TokenManager.getInstance().getToken()).execute();
            return resultImage.body();
     }

}
