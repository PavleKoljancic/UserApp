package com.example.userapp.activity.nopicture;

import com.example.userapp.activity.UserApiDecorator;
import com.example.userapp.token.TokenManager;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

class NoPictureApiDecorator extends UserApiDecorator {


     public void setPictureBytes(byte[] pictureBytes) {
         this.pictureBytes = pictureBytes;
     }

     byte [] pictureBytes;
     NoPictureApiDecorator()
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
