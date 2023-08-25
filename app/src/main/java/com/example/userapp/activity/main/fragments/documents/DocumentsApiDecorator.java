package com.example.userapp.activity.main.fragments.documents;

import com.example.userapp.activity.UserApiDecorator;
import com.example.userapp.token.TokenManager;

import org.json.JSONException;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class DocumentsApiDecorator extends UserApiDecorator {

    Boolean uploadFile(ByteBuffer data, String DocumentName) throws JSONException, IOException {





        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("pdf"),
                        data.array()
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("document", "Document.pdf", requestFile);


            return api.uploadDocument(body, TokenManager.getInstance().getId(),DocumentName,TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();

    }

    Boolean deleteDocument(String documentName) throws JSONException, IOException {

        return api.removeDocument(TokenManager.getInstance().getId(), documentName, TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
    }

     ResponseBody downloadODocument(String documentName) throws JSONException, IOException {
        return    api.getDocument(TokenManager.getInstance().getId(), documentName, TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
     }
}
