package com.example.userapp.activity.main.fragments.documents;

import com.example.userapp.activity.UserApiDecorator;
import com.example.userapp.models.Document;
import com.example.userapp.models.DocumentType;
import com.example.userapp.token.TokenManager;

import org.json.JSONException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class DocumentsApiDecorator extends UserApiDecorator {

    Boolean uploadFile(ByteBuffer data, DocumentType documentType) throws JSONException, IOException {





        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("pdf"),
                        data.array()
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("document", "Document.pdf", requestFile);


            return api.uploadDocument(body, TokenManager.getInstance().getId(),documentType.getId(),TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();

    }



     ResponseBody downloadODocument(Document document) throws JSONException, IOException {
        return  api.getDocument(TokenManager.getInstance().getId(),document.getId(),TokenManager.bearer() +TokenManager.getInstance().getToken()).execute().body();
     }
     List<Document> getUserDocuments() throws JSONException, IOException {
         return  api.getUserDocuemnts(TokenManager.getInstance().getId(),TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
     }

     List<DocumentType> getValidDocumentType() throws IOException {
         return api.getValidDocumentTypes(TokenManager.bearer() + TokenManager.getInstance().getToken()).execute().body();
     }
}
